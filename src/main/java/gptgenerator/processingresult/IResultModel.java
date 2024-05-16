package gptgenerator.processingresult;

public interface IResultModel {
	void clear();
	
	void addPending();
	void addProcessed();
	void addSkipped();

	int getPending();
	int getProcessed();
	int getSkipped();
	int getDone();

	void setController(IResultController resultController);
	void clearController();
}
