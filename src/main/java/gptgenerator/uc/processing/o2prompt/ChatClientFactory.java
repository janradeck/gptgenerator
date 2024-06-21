package gptgenerator.uc.processing.o2prompt;

import gptgenerator.uc.configure.IConfigurationController;

/**
 * A factory that isolates the client from creating instances of IChatClient
 */
public class ChatClientFactory {
	/**
	 * if controller is in production mode, return a GPTClient, otherwise return a DummyGPTClient<br>
	 * The GPTClient is a wrapper for the OpenAI API, the DummyGPTClient is a mock for testing<br>
	 * @param controller
	 * @param apiUrl
	 * @param apiKey
	 * @param temperature
	 * @return
	 */
	public static IChatClient getChatClient(IConfigurationController controller, String apiUrl, String apiKey, double temperature) {
		if (controller.makeApiCalls()) {
			return new GPTClient(apiUrl, apiKey, temperature);
		} else {
			return new DummyGPTClient(temperature);
		}
	}
}
