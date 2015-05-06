package cn.bvin.library.net;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import cn.bvin.library.net.DownloadRequest.DownloadListener;
import cn.bvin.library.net.UploadRequest.UploadListener;

/**
 * Java的轻量化网络请求库
 * @ClassName: NetWorker 
 * @Description: TODO
 * @author: Bvin
 * @date: 2015年4月2日 上午11:14:49
 */
public class NetWorker {
	
	private String url;
	private RequestParam param;
	
	private RequestListener requestListener;
	private DownloadListener downloadListener;
	private UploadListener uploadListener;

	public NetWorker(String url, RequestParam param) {
		super();
		this.url = url;
		this.param = param;
		this.requestListener = new RequestListener() {
			
			@Override
			public void onRequestSuccess(WrapResponces responces) {
				
			}
			
			@Override
			public void onRequestStart(String url, RequestParam param) {
				
			}
			
			@Override
			public void onRequestProgress(long progress) {
				
			}
			
			@Override
			public void onRequestFailure(Exception e) {
				
			}
		};
	}

	
	public NetWorker(RequestListener requestListener) {
		super();
		this.requestListener = requestListener;
	}
	
	



	public NetWorker(DownloadListener downloadListener) {
		super();
		this.downloadListener = downloadListener;
	}
	
	


	public NetWorker(UploadListener uploadListener) {
		super();
		this.uploadListener = uploadListener;
	}
	


	/**
	 * 请求成功或失败都会返回一个WrapResponces
	 * @param urlStr 请求地址
	 * @param param 请求参数
	 * @return: WrapResponces 返回响应
	 */
	public WrapResponces request(final Request request) {
		if (request.getPerform()==Request.RequestPerform.Sync) {
			return performRequest(request);
		}else {
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					performRequest(request);
				}
			}).start();
			return null;//暂时return null，后面可用Fure改造
		}
		
	}
	
	private WrapResponces performRequest(Request request) {
		RequestParam param = request.getParam();
		try {
			//1.构建URL
			URL url = new URL(request.getUrl());
			//如果是Request，读取字符串数据的就在这回调请求开始接口方法
			if (this.requestListener!=null) {
				this.requestListener.onRequestStart(request.getUrl(), param);
			}
			
			//2.执行请求得到HttpURLConnection
			HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
            if (param==null) {
            	//没有参数
			}else {
				httpConn.setDoOutput(true);
				//3.获取一个输出流，用于向服务器写入数据 
	            OutputStream outputStream = httpConn.getOutputStream();  
	          //如果request是UploadRequest并且请求参数还有文件，就上传文件写入到服务器
				if(param.getFile()!=null&&request instanceof UploadRequest) {//向服务器写文件，上传文件
	            	if (uploadListener!=null) {
	            		this.uploadListener.onUploadStart(request.getUrl());
					}
	            	uploadFile(outputStream, param.getFile());
				} else if (param.getBuffer()!=null) {//写Byte数组，Post数据
					write(outputStream, param.getBuffer());
				} else {
					//其他方式...
				}
	            outputStream.close();
			}
            //4.获取输入流，读成字符串图片等数据或者写入文件
			if (httpConn.getResponseCode() == 200) {
				if (request instanceof DownloadRequest) {
					//下载接口是从这触发开始下载方法
					if (this.downloadListener!=null) {
						this.downloadListener.onDownloadStart(request.getUrl(), httpConn.getContentLength());
					}
					writeToFile(httpConn.getInputStream(), fixFileName(request));
				} else { //无论是上传还是字符串数据都是Ok=200为成功，如果需要对服务器返回数据判断是否成功需自行扩展
					String result = transferStreamWithChar(httpConn.getInputStream()).toString();
					StringResponces responces = new StringResponces(result);
					if (request instanceof UploadRequest) {
						if (this.uploadListener!=null) {
							this.uploadListener.onUploadSuccess(responces);
						}
					}else {
						if (this.requestListener!=null) {
							this.requestListener.onRequestSuccess(responces);
						}
						
					}
					
				}
				return new WrapResponces(httpConn.getInputStream(), httpConn.getContentLength());
			} else {//状态码!=200
				if (this.requestListener!=null) {
					this.requestListener.onRequestFailure(new ResponseCodeError(httpConn.getResponseCode()));
				}
				return new WrapResponces(new ResponseCodeError(httpConn.getResponseCode()));
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
			if (this.requestListener!=null) {
				this.requestListener.onRequestFailure(e);
			}
			return new WrapResponces(e);
		} catch (IOException e) {
			e.printStackTrace();
			if (this.requestListener!=null) {
				this.requestListener.onRequestFailure(e);
			}
			return new WrapResponces(e);
		}
	}
	
	//待改善
	private File fixFileName(Request request) {
		File file  = request.getParam().getFile();
		if (file.isDirectory()) {//如果是目录就自动从url获取文件名
			String fileName = request.getUrl().substring(request.getUrl().lastIndexOf("/")+1, request.getUrl().length());
			file = new File(file, fileName);
		}
		if (file.exists()&&file.isFile()) {//如果文件已存在
			int n = 1;
			String fileNameNoSuffix = file.getName().substring(0, file.getName().lastIndexOf("."));
			String suffix = file.getName().substring(file.getName().lastIndexOf("."), file.getName().length());
			for (File eachFile :file.getParentFile().listFiles()) {
				if (eachFile.getName().startsWith(fileNameNoSuffix)) {
					String number = null;
					if (file.getName().contains("-")) {
						number = eachFile.getName().substring(eachFile.getName().lastIndexOf("-")+1,eachFile.getName().lastIndexOf("."));
					}
					try {
						if (n<=Integer.parseInt(number)) {
							n = Integer.parseInt(number)+1;
						}else {
							continue;
						}
					} catch (NumberFormatException e) {
						e.printStackTrace();
						continue;
					}
				}
			}
			System.out.println(n);
			/*if (file.getName().contains("-")) {
				String number = file.getName().substring(file.getName().lastIndexOf("-")+1);
				try {
					n = Integer.parseInt(number)+1;
				} catch (NumberFormatException e) {
					e.printStackTrace();
					//如果文件名后面包含-，但不是数组就直接在后面-1
				}
			}*/
			file = new File(file.getParentFile(),fileNameNoSuffix+"-"+n+suffix);
		}
		return file;
	}
	
	
	/**
	 * 写入文件(上传)，用{@link #write(OutputStream, byte[], int, int)}方法写入
	 * @param outputStream 输出流
	 * @param file 输入文件
	 */
	private void uploadFile(OutputStream outputStream,File file) {
		try {
			InputStream is = new FileInputStream(file);
			byte[] buffer = new byte[1024];   
			int writed = 0;//写入多少，什么时候得清零吧？
            int len = 0;    
            while ((len = is.read(buffer)) != -1) {   
            	write(outputStream, buffer, 0, len);
            	writed+=len;
            	if (uploadListener!=null) {
    				uploadListener.onUploadProgress(writed);
    			}
            }    
            is.close();  
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			if (uploadListener!=null) {
				uploadListener.onUploadFailure(e);;
			}
		} catch (IOException e) {
			e.printStackTrace();
			if (uploadListener!=null) {
				uploadListener.onUploadFailure(e);
			}
		}
	}
	
	/**
	 * 分段写入
	 * @param outputStream 输出流
	 * @param buffer  需要写入的byte数组
	 * @param offset 从哪开始写
	 * @param count 写多少
	 * @throws IOException 
	 */
	private void write(OutputStream outputStream, byte[] buffer, int offset, int count) throws IOException {
		outputStream.write(buffer, offset, count);
	}
	
	/**
	 * 将buffer一次性写入，如果很大应该建议用{@link #write(OutputStream, byte[], int, int)}
	 * 方法
	 * @param outputStream 输出流
	 * @param buffer 需要写入的byte数组
	 */
	private void write(OutputStream outputStream, byte[] buffer) {
		try {
			outputStream.write(buffer);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//字符流方式读取
	private String transferStreamWithChar(InputStream inputStream) {
		if (inputStream!=null) {
			BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream)); 
			return readString(reader).toString();
		}
		return null;
	}
	
	//带进度的读取字符串方法
	private StringBuilder readStringWithProgress(BufferedReader reader,RequestListener listener) {
		StringBuilder sb = new StringBuilder();
		int read;
		char[] buff = new char[1024];
		try {
			while ((read = reader.read(buff)) != -1) {
				sb.append(buff, 0, read);
				listener.onRequestProgress(read);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return sb;
	}
	
	//读取字符串方法
	private StringBuilder readString(BufferedReader reader) {
		StringBuilder sb = new StringBuilder();
		try {
			String readLine;
			while ((readLine = reader.readLine()) != null) {
				StringBuilder sbs = new StringBuilder();
				sb.append(readLine).append("\n");
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return sb;
	}
	
	//写入文件
	private void writeToFile(InputStream inputStream,File file) {
		try {
			OutputStream outputStream = new FileOutputStream(file);
			transferStreamWithByte(inputStream, outputStream);
			outputStream.flush();
			outputStream.close();
			if (this.downloadListener!=null) {
				this.downloadListener.onDownloadSuccess(file);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			if (this.downloadListener!=null) {
				this.downloadListener.onDownloadFailure(e);
			}
			
		} catch (IOException e) {
			e.printStackTrace();
			if (this.downloadListener!=null) {
				this.downloadListener.onDownloadFailure(e);
			}
		}
	}
	
	//字节流方式读写流数据
	private void transferStreamWithByte(InputStream inputStream,OutputStream outputStream) throws IOException {
		if (inputStream!=null) {
			BufferedInputStream bis = new BufferedInputStream(inputStream);
			int len;
			int count = 0;
			byte[] buff = new byte[1024];
			while ((len = bis.read(buff)) != -1) {
				outputStream.write(buff, 0, len);
				count += len;
				if (downloadListener!=null) {
					downloadListener.onDownloadProgress(count);
				}
			}
		}
	}
}
