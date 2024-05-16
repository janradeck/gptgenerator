package gptgenerator.processingresult;

public interface IResultController {
	void clear();
	
	void addPendingFile();
	void addProcessedFile();
	void addSkippedFile();
	
	void notifySetPending(int newPending);
	void notifySetProcessed(int newProcessed);
	void notifySetSkipped(int newSkipped);
	void notifySetDone(int newTotal);
	
	void addView(IResultModelView view);
	void removeView(IResultModelView view);
	void requestViewUpdate();
}
