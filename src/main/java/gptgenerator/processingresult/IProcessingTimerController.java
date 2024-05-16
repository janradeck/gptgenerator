package gptgenerator.processingresult;

import gptgenerator.uc.mainview.IProcessingTimerView;

public interface IProcessingTimerController {
	void reset();
	void start();
	void stop();
	long duration();
	
	void notifyReset();
	void notifyStart();
	void notifyStop(long duration);

    void addView(IProcessingTimerView view);
    void removeView(IProcessingTimerView view);
    void requestViewUpdate();
}
