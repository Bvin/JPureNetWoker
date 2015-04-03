package cn.bvin.library.net;

import java.io.File;

import cn.bvin.library.utils.StringUtils;

/**
 * 没有set方法，只有get方法，必须用构造传入参数，只能存在一种形式
 * @ClassName: RequestParam 
 * @Description: 请求参数封装类
 * @author: Bvin
 * @date: 2015年4月2日 上午11:28:56
 */
public class RequestParam {

	private byte[] buffer;
	
	private File file;
	
	private MapParam params;

	/**
	 * 一次性写如一个byte数组，适合数据不大的写入情景
	 * @param buffer
	 */
	public RequestParam(byte[] buffer) {
		super();
		this.buffer = buffer;
	}

	/**
	 * TODO 传string可能需要区分是用HttpClient还是用URLConection
	 * @param buffer
	 */
	public RequestParam(String buffer) {
		this(buffer.getBytes());
	}
	
	/**
	 * 写入文件类型
	 * @param file
	 */
	public RequestParam(File file) {
		super();
		this.file = file;
	}
	

	/**
	 * GET请求，Map参数集合直接转换为字符串
	 * @param params
	 */
	public RequestParam(MapParam params) {
		super();
		this.params = params;
	}

	public byte[] getBuffer() {
		return buffer;
	}

	public File getFile() {
		return file;
	}

	public MapParam getParams() {
		return params;
	}

	@Override
	public String toString() {
		if (this.params!=null) {
			return StringUtils.generateUrlString(params.get());
		}else {
			return super.toString();
		}
	}
	
	
}
