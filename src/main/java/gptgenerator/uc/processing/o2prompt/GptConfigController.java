package gptgenerator.uc.processing.o2prompt;

import java.util.ArrayList;
import java.util.List;

import gptgenerator.uc.configure.gpt.IChatConfigModel;
import gptgenerator.uc.mainview.IGptConfigView;

public class GptConfigController implements IGptConfigController {

	private IChatConfigModel config;
    private List<IGptConfigView> registeredViews = new ArrayList<IGptConfigView>();
	
	public GptConfigController (IChatConfigModel config) {
		this.config = config;
	}
	
	public void setSystemMessage(String message) {
		config.setSystemMessage(message);
	}
	
	public void setTemperature (String temperature) {
		config.setTemperature(temperature);
	}
	
	public void setIndividualTemperature(Boolean isIndividual) {
		config.setIndividualTemperature(isIndividual);
	}

	@Override
	public void requestViewUpdate() {
		notifySetSystemMessage(config.getSystemMessage());
		notifySetTemperature(config.getTemperatureString());
		notifySetIndividualTemperature(config.getIndividualTemperature());
	}

	@Override
	public void notifySetSystemMessage(String message) {
		for (IGptConfigView view: registeredViews) {
			view.setSystemMessage(message);
		}		
	}

	@Override
	public void notifySetTemperature(String temperature) {
		for (IGptConfigView view: registeredViews) {
			view.setTemperature(temperature);
		}		
	}

	@Override
	public void notifySetIndividualTemperature(Boolean isIndividual) {
		for (IGptConfigView view: registeredViews) {
			view.setIndividualTemperature(isIndividual);
		}		
	}

	@Override
	public void addView(IGptConfigView view) {
		registeredViews.add(view);
	}

	@Override
	public void removeView(IGptConfigView view) {
		registeredViews.remove(view);
	}
	
}
