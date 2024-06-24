package gptgenerator.uc.configure.gpt;

import com.fasterxml.jackson.annotation.JsonIgnore;

import gptgenerator.uc.mainview.IChatConfigView;
import gptgenerator.uc.processing.o2prompt.IChatConfigController;

/**
 * Einstellungen für ChatGPT, für ein Unterverzeichnis
 * <ul>
 * <li>systemMessage: Message an ChatGPT in der Rolle "system"
 * <li>individualTemperature: Soll für dieses Verzeichnis ein individueller Wert für "temperature" verwendet werden?
 * <li>temperature: Verzeichnis-individueller Wert für "temperature"
 * </ul>
 */
public class ChatConfigModel implements IChatConfigModel {
	private String systemMessage;
	private ChatTemperature temperature;
	private Boolean individualTemperature;

	@JsonIgnore
	private IChatConfigController controller = new NilGptConfigController();

	public ChatConfigModel() {
		systemMessage = "";
		individualTemperature = false;
		temperature = new ChatTemperature();
	}

	public ChatConfigModel(IChatConfigModel source) {
		this.systemMessage = source.getSystemMessage();
		this.individualTemperature = source.getIndividualTemperature();
		this.temperature = new ChatTemperature(source.getTemperatureInstance());
	}

	@Override
	public void setController(IChatConfigController controller) {
		this.controller = controller;		
	}

	@Override
	public void clrController() {
		controller = new NilGptConfigController();
	}
	
	/**
	 * Message an ChatGPT, in der Rolle "system"
	 * @return
	 */
	public String getSystemMessage() {
		return systemMessage;
	}
	
	/**
	 * Verzeichnis-individueller Wert für "temperature"
	 * @return
	 */
	public Double getTemperature() {
		return temperature.getTemperature();
	}

	@JsonIgnore
	public ChatTemperature getTemperatureInstance() {
		return temperature;
	}
	
	
	@JsonIgnore
	public String getTemperatureString() {
		return temperature.getTemperatureString();
	}
	
	/**
	 * Soll für dieses Verzeichnis ein individueller Wert für "temperature" verwendet werden?<br>
	 * Ansonsten soll die globale Einstellung verwendet werden.
	 * @return
	 */
	public Boolean getIndividualTemperature() {
		return individualTemperature;
	}
	
	public void setSystemMessage(String systemMessage) {
		if (!systemMessage.equals(this.systemMessage)) {
			this.systemMessage = systemMessage;
			controller.notifySetSystemMessage(systemMessage);
		}
	}
	
	public void setTemperature(Double temperature) {
		this.temperature.setTemperature(temperature);
	}

	public void setTemperature(String temperature) {
		if (!temperature.equals(this.getTemperatureString())) {
			this.temperature.setTemperature(temperature);
			controller.notifySetTemperature(temperature);
		}		
	}
	
	public void setIndividualTemperature(Boolean individualTemperature) {
		if (individualTemperature != this.individualTemperature) {
			this.individualTemperature = individualTemperature;
			controller.notifySetIndividualTemperature(individualTemperature);
		}
	}
	
	public static boolean validateTemperatureString(String temperatureString) {
		return ChatTemperature.validateString(temperatureString);
	}
	

	private class NilGptConfigController implements IChatConfigController {

		@Override
		public void setSystemMessage(String message) {
		}

		@Override
		public void setTemperature(String temperature) {
		}

		@Override
		public void setIndividualTemperature(Boolean isIndividual) {
		}

		@Override
		public void notifySetSystemMessage(String message) {
		}

		@Override
		public void notifySetTemperature(String temperature) {
		}

		@Override
		public void notifySetIndividualTemperature(Boolean isIndividual) {
		}

		@Override
		public void addView(IChatConfigView view) {
			System.err.println("NilGptController.addView() called");
		}

		@Override
		public void removeView(IChatConfigView view) {
			System.err.println("NilGptController.removeView() called");
		}

		@Override
		public void requestViewUpdate() {
		}
		
	}


}
