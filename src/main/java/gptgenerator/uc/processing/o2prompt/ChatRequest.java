package gptgenerator.uc.processing.o2prompt;

import java.util.ArrayList;
import java.util.List;

/**
 * A single request to the chat engine
 */
public class ChatRequest {
	private String model;
	private Boolean stream = false;
    private List<Message> messages;
    private double temperature = 1.0;

    /**
     * @param model
     * @param systemMessage
     * @param prompt
     */
    public ChatRequest(String model, String systemMessage, String prompt) {
    	this.model = model;
        this.messages = new ArrayList<>();
        if (!systemMessage.isBlank()) {
        	this.messages.add(new Message("system", systemMessage));
        }
        this.messages.add(new Message("user", prompt));
    }

	public String getModel() {
		return model;
	}
	
	/**
	 * true: The chat engine should send messages as they are generated<br>
	 * false: Only one message at the end of the conversation
	 * @return See above
	 */
	public Boolean getStream() {
		return stream;
	}

	public List<Message> getMessages() {
		return messages;
	}

	public double getTemperature() {
		return temperature;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public void setMessages(List<Message> messages) {
		this.messages = messages;
	}

	public void setTemperature(double temperature) {
		this.temperature = temperature;
	}

}