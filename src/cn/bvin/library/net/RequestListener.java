package cn.bvin.library.net;

public interface RequestListener {

	/**
	 *请求开始...
	 * @param url 请求地址
	 * @param param 参数
	 */
	public void onRequestStart(String url,RequestParam param);
	
	/**
	 * 请求进度监听，包括上传下载
	 * @param progress 进度
	 */
	public void onRequestProgress(long progress);
	
	/**
	 * 请求成功后返回WrapResponces对象
	 * @param responces 返回封装的响应结果
	 */
	public void onRequestSuccess(WrapResponces responces);
	
	/**
	 * 请求失败
	 * @param e 失败异常信息
	 */
	public void onRequestFailure(Exception e);
}
