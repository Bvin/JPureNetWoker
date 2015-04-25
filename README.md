# JPureNetWoker
Java下的轻量级网络请求，默认是异步的
用法
--------
```Java
    	//StringRequest普通字符串请求
    	String url = "http://xxxx";
	MapParam mp = new MapParam();;
	mp.put("longitude", "103.99286");
	mp.put("latitude", "30.573796");
	StringRequest request = new StringRequest(url, new RequestParam(mp));
	request.requets(new RequestListener() {
		@Override
		public void onRequestSuccess(WrapResponces responces) {
			System.out.println("onRequestSuccess:"+responces.toString());
		}
		
		@Override
		public void onRequestStart(String url, RequestParam param) {
			System.out.println("onRequestStart:"+url+param.toString());
		}
		
		@Override
		public void onRequestProgress(long progress) {
			
		}
		
		@Override
		public void onRequestFailure(Exception e) {
			System.out.println("onRequestFailure:"+e.getLocalizedMessage());
		}
	});
	
	 //DownloadRequest文件下载请求
	 DownloadRequest downloadRequest = new DownloadRequest(url, new File("D:"));
	 downloadRequest.download(new DownloadRequest.DownloadListener() {
		
		long size;
		
		@Override
		public void onDownloadSuccess(File file) {
			System.out.println("下载成功:"+file.getAbsolutePath());
		}
		
		@Override
		public void onDownloadStart(String url, long size) {
			System.out.println("开始下载:"+size);
			this.size = size;
		}
		
		@Override
		public void onDownloadProgress(long progress) {
			System.out.println("下载..:"+progress*100/size+"%");
		}
		
		@Override
		public void onDownloadFailure(Exception e) {
			System.out.println("下载失败:"+e.getLocalizedMessage());
		}
	});
```
