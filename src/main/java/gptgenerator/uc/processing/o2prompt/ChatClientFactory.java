package gptgenerator.uc.processing.o2prompt;

import gptgenerator.uc.configure.IConfigurationController;

/**
 * 
 */
public class ChatClientFactory {
	/**
	 * Return a ChatClient for PROD oder DEBUG
	 * @param controller
	 * @return
	 */
	public static IChatClient getChatClient(IConfigurationController controller, double temperature) {
		if (controller.isProd()) {
			return new GPTClient(temperature);
		} else {
			return new DummyGPTClient(temperature);
		}
	}
}
