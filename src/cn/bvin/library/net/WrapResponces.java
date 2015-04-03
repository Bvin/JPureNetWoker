package cn.bvin.library.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


/**
 * 
 * @ClassName: WrapResponces 
 * @Description: 封装的网络响应，包括InputStream和Content长度以及响应状态码
 * @author: Bvin
 * @date: 2015年3月19日 下午5:25:06
 */
public class WrapResponces {

	//状态码，可能不需要，因为这个类存在的意义就是状态码=200的时候
	private int statusCode;
	
	private InputStream inputStream;
	
	
	private long contentLength;

	public WrapResponces(InputStream inputStream, long contentLength) {
		super();
		this.inputStream = inputStream;
		this.contentLength = contentLength;
	}


	public InputStream getInputStream() {
		return inputStream;
	}

	public long getContentLength() {
		return contentLength;
	}


	@Deprecated
	public int getStatusCode() {
		return statusCode;
	}

	@Deprecated
	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}
	
	/**以字符Buffer的方式来读*/
	private String readString(InputStream inputStream) {
		StringBuilder sb = new StringBuilder();
		if (inputStream!=null) {
			BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream)); 
			int read;
			char[] buff = new char[1024];
			try {
				while ((read = reader.read(buff)) != -1) {
					sb.append(buff, 0, read);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}
	
	/**一行一行读*/
	private String readStringByLine(InputStream inputStream) {
		StringBuffer sb = new StringBuffer();
		if (inputStream != null) {
			String readLine;
			BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
			try {
				while ((readLine = reader.readLine()) != null) {
					sb.append(readLine).append("\n");
				}
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}


	
	@Override
	public String toString() {
		if (inputStream!=null) {
			return readStringByLine(inputStream);
		}else {
			return super.toString();
		}
	}
	
}
