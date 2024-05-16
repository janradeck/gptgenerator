package gptgenerator.uc.processing.o2prompt;

public interface IChatClient {
	String sendPrompt(String systemMessage, String promptString);
}
