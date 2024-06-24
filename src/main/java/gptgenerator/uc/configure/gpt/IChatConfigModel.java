package gptgenerator.uc.configure.gpt;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import gptgenerator.uc.processing.o2prompt.IChatConfigController;

@JsonDeserialize(as = ChatConfigModel.class)
public interface IChatConfigModel {

	String getSystemMessage();
	String getTemperatureString();
	Boolean getIndividualTemperature();
	
	void setSystemMessage(String systemMessage);
	void setTemperature(String temperature);
	void setIndividualTemperature(Boolean individualTemperature);
	
	void setController(IChatConfigController controller);
	void clrController();
	Double getTemperature();
	ChatTemperature getTemperatureInstance();
}
