package gptgenerator.uc.processing.o2prompt;

public class DummyGPTClient implements IChatClient {
	private double temperature;

	public DummyGPTClient(double temperature) {
		this.temperature = temperature;
	}

	@Override
	public String sendPrompt(String systemMessage, String request) {
		return String.format("This is the response for:%nSystem message: %s%nTemperature: %.1f%n%s", systemMessage, temperature, request);
	}

}
