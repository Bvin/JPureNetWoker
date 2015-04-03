package cn.bvin.library.net;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

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
	
	private RequestListener listener;

	public NetWorker(String url, RequestParam param) {
		super();
		this.url = url;
		this.param = param;
		this.listener = new RequestListener() {
			
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
	
	public NetWorker() {
		super();
	}

	public void request() {
		request(this.url, this.param);
	}
	
	private WrapResponces request(String urlStr, RequestParam param) {
		try {
			//1.构建URL
			URL url = new URL(urlStr);
			//2.执行请求得到HttpURLConnection
			HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
			//3.获取一个输出流，用于写入数据 
            OutputStream outputStream = httpConn.getOutputStream();  
            if (param.getFile()!=null) {//写文件
				write(outputStream, param.getFile());
			} else if (param.getBuffer()!=null) {//写Byte数组
				write(outputStream, param.getBuffer());
			} else {
			}
            outputStream.close();
			if (httpConn.getResponseCode() == 200) {
				return new WrapResponces(httpConn.getInputStream(), httpConn.getContentLength());
			} else {//状态码!=200
				return null;
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private long writed;//写入多少，什么时候得清零吧？
	
	/**
	 * 写入文件(上传)，用{@link #write(OutputStream, byte[], int, int)}方法写入
	 * @param outputStream 输出流
	 * @param file 输入文件
	 */
	private void write(OutputStream outputStream,File file) {
		try {
			InputStream is = new FileInputStream(file);
			byte[] buffer = new byte[1024];    
            int len = 0;    
            while ((len = is.read(buffer)) != -1) {    
            	write(outputStream, buffer, 0, len);
            }    
            is.close();  
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 分段写入
	 * @param outputStream 输出流
	 * @param buffer  需要写入的byte数组
	 * @param offset 从哪开始写
	 * @param count 写多少
	 */
	private void write(OutputStream outputStream, byte[] buffer, int offset, int count) {
		try {
			outputStream.write(buffer, offset, count);
			writed+=count;
			listener.onRequestProgress(writed);
		} catch (IOException e) {
			e.printStackTrace();
		}
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
			writed+=buffer.length;
			listener.onRequestProgress(writed);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
