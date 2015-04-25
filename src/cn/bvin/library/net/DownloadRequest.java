package cn.bvin.library.net;

import java.io.File;

public class DownloadRequest extends Request{
	
	public DownloadRequest(String url, File file) {
		super(url, new RequestParam(file));
	}
	
	public DownloadRequest(String url, File dir,String fileName) {
		super(url, new RequestParam(new File(dir, fileName)));
	}

	public void download(DownloadListener listener) {
		NetWorker nw = new NetWorker(listener);
		nw.request(this);
	}
	
	public interface DownloadListener{
		
		public void onDownloadStart(String url,long size);
		
		public void onDownloadProgress(long progress);
		
		public void onDownloadSuccess(File file);
		
		public void onDownloadFailure(Exception e);
	}
}
