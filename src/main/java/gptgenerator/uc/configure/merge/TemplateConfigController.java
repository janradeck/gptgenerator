package gptgenerator.uc.configure.merge;

import java.util.ArrayList;
import java.util.List;

public class TemplateConfigController implements ITemplateConfigController {

    private ITemplateConfigModel config;
    private List<ITemplateConfigView> views = new ArrayList<>();
    
    public TemplateConfigController (ITemplateConfigModel config) {
    	this.config = config;
    }
    
    @Override
    public void setMarkerStart(String value) {
   		config.setMarkerStart(value);
    }
    
    @Override
    public void setMarkerEnd(String value) {
   		config.setMarkerEnd(value);
    }

	@Override
	public void notifySetMarkerStart(String newValue) {
		for (ITemplateConfigView view: views) {
			view.setElementMarkerStart(newValue);
		}
	}

	@Override
	public void notifySetMarkerEnd(String newValue) {
		for (ITemplateConfigView view: views) {
			view.setElementMarkerEnd(newValue);
		}		
	}

	@Override
	public void addView(ITemplateConfigView view) {
		views.add(view);
	}

	@Override
	public void removeView(ITemplateConfigView view) {
		views.remove(view);
	}

	@Override
	public void requestViewUpdate() {
		notifySetMarkerStart(config.getMarkerStart());
		notifySetMarkerEnd(config.getMarkerEnd());
	}
}
