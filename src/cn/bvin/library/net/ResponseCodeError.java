package cn.bvin.library.net;

public class ResponseCodeError extends Exception{

	int responseCode;
	
	public ResponseCodeError(int responseCode) {
		super();
		this.responseCode = responseCode;
	}

	@Override
	public String getLocalizedMessage() {
		return super.getLocalizedMessage();
	}

	@Override
	public String getMessage() {
		return super.getMessage();
	}

	@Override
	public String toString() {
		return "the response code is not 200,but "+responseCode;
	}

	public int getResponseCode() {
		return responseCode;
	}
}
