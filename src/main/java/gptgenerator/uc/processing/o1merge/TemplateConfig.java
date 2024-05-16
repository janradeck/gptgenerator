package gptgenerator.uc.processing.o1merge;

import java.util.regex.Pattern;

import com.fasterxml.jackson.annotation.JsonIgnore;

import gptgenerator.uc.configure.merge.ITemplateConfigController;
import gptgenerator.uc.configure.merge.ITemplateConfigModel;
import gptgenerator.uc.configure.merge.ITemplateConfigView;

/**
 * ConfigurationModel values for template processing<br>
 * In the setter methods leading and trailing whitespace is removed via strip()
 */
public class TemplateConfig implements ITemplateConfigModel {
	private static final String basicMergedStart = "#(";
	private static final String basicUnmergedStart = "#U(";
	private static final String filenameGroup = "([^)]+)";
	private static final String basicMarkerEnd = ")#";
	
	private String markerStart = "";
	private String markerEnd = "";
	
	
	private ITemplateConfigController controller = new NilTemplateConfigController();
	
	public TemplateConfig() {
		markerStart = "";
		markerEnd = "";		
	}
	
	public TemplateConfig(ITemplateConfigModel templateConfig) {
		this.markerStart = templateConfig.getMarkerStart();
		this.markerEnd = templateConfig.getMarkerEnd();
	}
	
	@Override
	public String getMarkerStart() {
		return markerStart;
	}
	@Override
	public String getMarkerEnd() {
		return markerEnd;
	}
	/**
	 * Leading and trailing whitespace is removed via strip()
	 * @param newMarkerStart
	 */
	@Override
	public void setMarkerStart(String newMarkerStart) {
		String newValue = newMarkerStart.strip();
		if (!newValue.equals(this.markerStart)) {
			this.markerStart = newValue;
			controller.notifySetMarkerStart(this.markerStart);
		}
	}
	/**
	 * Leading and trailing whitespace is removed via strip()
	 * @param newMarkerEnd
	 */
	@Override
	public void setMarkerEnd(String newMarkerEnd) {
		String newValue = newMarkerEnd.strip();
		if (!newValue.equals(this.markerEnd)) {
			this.markerEnd = newValue;
			controller.notifySetMarkerEnd(this.markerEnd);
		} 
	}
	
	@Override
	public void setController(ITemplateConfigController controller) {
		this.controller = controller;		
	}
	
	@Override
	public void clearController() {
		this.controller = new NilTemplateConfigController();
	}

	@JsonIgnore
	@Override
	public String getMergedMarker() {
		return Pattern.quote(markerStart + basicMergedStart) + filenameGroup + Pattern.quote(basicMarkerEnd + markerEnd); 
	}

	@JsonIgnore	
	@Override	
	public String getUnmergedMarker() {
		return Pattern.quote(markerStart + basicUnmergedStart) + filenameGroup + Pattern.quote(basicMarkerEnd + markerEnd); 
	}
	
	private class NilTemplateConfigController implements ITemplateConfigController {

		@Override
		public void setMarkerStart(String newValue) {
		}

		@Override
		public void setMarkerEnd(String newValue) {
		}

		@Override
		public void notifySetMarkerStart(String newValue) {
		}

		@Override
		public void notifySetMarkerEnd(String newValue) {
		}

		@Override
		public void addView(ITemplateConfigView view) {
			System.err.println("NilTemplateConfigController::addView() called");
		}

		@Override
		public void removeView(ITemplateConfigView view) {
			System.err.println("NilTemplateConfigController::removeView() called");
		}

		@Override
		public void requestViewUpdate() {
		}
		
	}

}
