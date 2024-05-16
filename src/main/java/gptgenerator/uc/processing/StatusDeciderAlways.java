package gptgenerator.uc.processing;

import gptgenerator.uc.mainview.TransformationStatus;

public class StatusDeciderAlways implements IStatusDecider {

	@Override
	public boolean select(TransformationStatus status) {
		return true;
	}

}
