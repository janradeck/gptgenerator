package gptgenerator.uc.processing.o2prompt;

/**
 * Information from the ChatGPT reply
 */
public class ChatReply {
	private String reply;
	private int totalTokens;
	
	public String getReply() {
		return reply;
	}
	public int getTotalTokens() {
		return totalTokens;
	}
	public void setReply(String reply) {
		this.reply = reply;
	}
	public void setTotalTokens(int totalTokens) {
		this.totalTokens = totalTokens;
	}
	
}
