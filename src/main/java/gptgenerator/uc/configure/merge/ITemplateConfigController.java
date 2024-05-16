package gptgenerator.uc.configure.merge;

public interface ITemplateConfigController {

	void setMarkerStart(String newValue);
	void setMarkerEnd(String newValue);

	void notifySetMarkerStart(String newValue);
	void notifySetMarkerEnd(String newValue);
	
	void addView(ITemplateConfigView view);
	void removeView(ITemplateConfigView view);
	void requestViewUpdate();
}
