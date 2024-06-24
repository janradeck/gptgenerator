package gptgenerator.uc.processing.o2prompt;

import gptgenerator.uc.mainview.IChatConfigView;

public interface IChatConfigController {

	void setSystemMessage(String message);
	void setTemperature (String temperature);
	void setIndividualTemperature(Boolean isIndividual);
	
	void notifySetSystemMessage(String message);
	void notifySetTemperature (String temperature);
	void notifySetIndividualTemperature(Boolean isIndividual);
	
	void addView(IChatConfigView view);
	void removeView(IChatConfigView view);
	void requestViewUpdate();
}
