package gptgenerator.uc.configure.gpt;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import gptgenerator.uc.processing.o2prompt.IGptConfigController;

@JsonDeserialize(as = GPTConfig.class)
public interface IGPTConfigModel {

	String getSystemMessage();
	String getTemperatureString();
	Boolean getIndividualTemperature();
	
	void setSystemMessage(String systemMessage);
	void setTemperature(String temperature);
	void setIndividualTemperature(Boolean individualTemperature);
	
	void setController(IGptConfigController controller);
	void clrController();
	Double getTemperature();
	GptTemperature getTemperatureInstance();
}
