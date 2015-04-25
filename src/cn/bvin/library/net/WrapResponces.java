package cn.bvin.library.net;

import java.io.BufferedInputStream;
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

	private Exception e;
	
	private String content;
	
	public WrapResponces(InputStream inputStream, long contentLength) {
		super();
		this.inputStream = inputStream;
		this.contentLength = contentLength;
	}

	public WrapResponces(String content) {
		super();
		this.content = content;
	}

	public WrapResponces(Exception error) {
		super();
		this.e = error;
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
	
	public Exception getError() {
		return e;
	}

	@Override
	public String toString() {
		if (inputStream!=null) {
			return readString(inputStream).toString();
		}else if (e!=null) {
			return e.getMessage();
		}else if (content!=null) {
			return content;
		}else {
			return super.toString();
		}
	}
	
	private StringBuilder readString(InputStream inputStream) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
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
	
	/**
	 * 没有异常发生就是成功，或者判断是否有实体内容
	 * @return: boolean 请求是否成功
	 */
	public boolean isSuccess() {
		return e==null;
	}
}
