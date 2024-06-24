package gptgenerator.processingresult;

public interface IProcessingTimerModel {
	void reset();
	void start();
	void stop();
	long duration();
	
	void setController(IProcessingTimerController controller);
	void clearController();
	boolean isRunning();
}
