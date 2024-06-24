package gptgenerator.uc.mainview;

public interface IChatConfigView {
	void setSystemMessage(String message);
	void setTemperature(String temperature);
	void setIndividualTemperature(Boolean isIndividual);
}
