package gptgenerator.uc.processing.o2prompt;

import java.util.ArrayList;
import java.util.List;

import gptgenerator.uc.configure.gpt.IChatConfigModel;
import gptgenerator.uc.mainview.IChatConfigView;

public class ChatConfigController implements IChatConfigController {

	private IChatConfigModel config;
    private List<IChatConfigView> registeredViews = new ArrayList<>();
	
	public ChatConfigController (IChatConfigModel config) {
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
		for (IChatConfigView view: registeredViews) {
			view.setSystemMessage(message);
		}		
	}

	@Override
	public void notifySetTemperature(String temperature) {
		for (IChatConfigView view: registeredViews) {
			view.setTemperature(temperature);
		}		
	}

	@Override
	public void notifySetIndividualTemperature(Boolean isIndividual) {
		for (IChatConfigView view: registeredViews) {
			view.setIndividualTemperature(isIndividual);
		}		
	}

	@Override
	public void addView(IChatConfigView view) {
		registeredViews.add(view);
	}

	@Override
	public void removeView(IChatConfigView view) {
		registeredViews.remove(view);
	}
	
}
