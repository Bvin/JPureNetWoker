package cn.bvin.library.net;

public class Request {

	private String url;
	private RequestParam param;
	
	private RequestPerform perform = RequestPerform.Async;
	
	public enum RequestPerform {Sync,Async} ;
	
	public Request(String url) {
		super();
		this.url = url;
	}

	public Request(String url, RequestParam param) {
		super();
		this.url = url;
		this.param = param;
	}

	public void requets(RequestListener listener) {
		NetWorker nw = new NetWorker(listener);
		nw.request(this);
	}
	
	public void requets(RequestParam param,RequestListener listener) {
		this.param = param;
		NetWorker nw = new NetWorker(listener);
		nw.request(this);
	}
	
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public RequestParam getParam() {
		return param;
	}

	public void setParam(RequestParam param) {
		this.param = param;
	}

	public RequestPerform getPerform() {
		return perform;
	}

	public void setPerform(RequestPerform perform) {
		this.perform = perform;
	}
	
}
