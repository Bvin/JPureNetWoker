package cn.bvin.library.net;

public class StringResponces extends WrapResponces{

	public StringResponces(String content) {
		super(content);
	}

	public String getContent() {
		return super.toString();
	}
	
}
