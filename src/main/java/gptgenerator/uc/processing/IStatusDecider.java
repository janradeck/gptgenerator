package gptgenerator.uc.processing;

import gptgenerator.uc.mainview.TransformationStatus;

public interface IStatusDecider {
	boolean select(TransformationStatus status);
}
