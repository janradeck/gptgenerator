package gptgenerator.uc.processing.o2prompt;

import java.util.ArrayList;
import java.util.List;

public class ChatRequest {
	private static final String MODEL = "gpt-3.5-turbo"; 
    private String model;
    private List<Message> messages;
    private double temperature = 1.0;

    /**
     * @param systemMessage
     * @param prompt
     */
    public ChatRequest(String systemMessage, String prompt) {
    	this.model = MODEL;
        this.messages = new ArrayList<>();
        if (!systemMessage.isBlank()) {
        	this.messages.add(new Message("system", systemMessage));
        }
        this.messages.add(new Message("user", prompt));
    }

	public String getModel() {
		return model;
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