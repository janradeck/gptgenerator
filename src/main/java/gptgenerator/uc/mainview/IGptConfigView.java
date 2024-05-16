package gptgenerator.uc.mainview;

public interface IGptConfigView {
	void setSystemMessage(String message);
	void setTemperature(String temperature);
	void setIndividualTemperature(Boolean isIndividual);
}
