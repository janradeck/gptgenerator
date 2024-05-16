package gptgenerator.processingresult;

public interface IProcessingTimer {
	void reset();
	void start();
	void stop();
	long duration();
	
	void setController(IProcessingTimerController controller);
	void clearController();
	boolean isRunning();
}
