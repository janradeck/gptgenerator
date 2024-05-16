package gptgenerator.uc.processing;

import gptgenerator.uc.mainview.SelectedFiles;
import gptgenerator.uc.mainview.TransformationStatus;

public class StatusDeciderSelectedFiles implements IStatusDecider {
private SelectedFiles selectedFiles;
	
	public StatusDeciderSelectedFiles(SelectedFiles selectedFiles) {
		this.selectedFiles = selectedFiles;
	}
	
	@Override
	public boolean select(TransformationStatus status) {
		return selectedFiles.contains(status.getBaseFilename());
	}

}
