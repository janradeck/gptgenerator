package gptgenerator.uc.processing;

import gptgenerator.uc.mainview.FileChangeStatus;
import gptgenerator.uc.mainview.TransformationStatus;

public class StatusDeciderOnChange implements IStatusDecider {

	@Override
	public boolean select(TransformationStatus status) {
		return status.getStatus().equals(FileChangeStatus.CHANGED) || status.getStatus().equals(FileChangeStatus.DST_MISSING);
	}
}
