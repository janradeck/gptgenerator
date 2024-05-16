package gptgenerator.services;

public class UpdateResponse {
	public enum CODE {
		OKAY,
		ERROR;
	}
	
	private CODE code;
	private String message;
	
	private static final UpdateResponse okayResponse = new UpdateResponse(CODE.OKAY, "");
	
	public UpdateResponse(CODE code, String message) {
		this.code = code;
		this.message = message;
	}
	
	public static UpdateResponse okayResponse() {
		return okayResponse;
	}
	
	public static UpdateResponse errorResponse(String message) {
		return new UpdateResponse(CODE.ERROR, message);
	}
		
	public boolean isOkay() {
		return code == CODE.OKAY;
	}
	
	public String getMessage() {
		return message;
	}
}
