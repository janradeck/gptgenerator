package gptgenerator.uc.processing.o2prompt;

import gptgenerator.uc.mainview.IGptConfigView;

public interface IGptConfigController {

	void setSystemMessage(String message);
	void setTemperature (String temperature);
	void setIndividualTemperature(Boolean isIndividual);
	
	void notifySetSystemMessage(String message);
	void notifySetTemperature (String temperature);
	void notifySetIndividualTemperature(Boolean isIndividual);
	
	void addView(IGptConfigView view);
	void removeView(IGptConfigView view);
	void requestViewUpdate();
}
