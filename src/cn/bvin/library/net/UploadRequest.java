package cn.bvin.library.net;

import java.io.File;

public class UploadRequest extends Request{

	public UploadRequest(String url, File file) {
		super(url,  new RequestParam(file));
	}
	
	public void upload(UploadListener listener) {
		NetWorker nw = new NetWorker(listener);
		nw.request(this);
	}
	
	
	public interface UploadListener{
		
		public void onUploadStart(String url);
		
		public void onUploadProgress(long progress);
		
		public void onUploadSuccess(WrapResponces responces);
		
		public void onUploadFailure(Exception e);
	}
}
