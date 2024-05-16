package gptgenerator.uc.mainview;

public interface IProcessingTimerView {
	void notifyReset();
	void notifyStart();
	void notifyStop(long duration);
	void setDuration(boolean running, long duration);
}
