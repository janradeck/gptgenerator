package gptgenerator.processingresult;

public interface IResultModelView {
	void setPending(int newPending);
	void setProcessed(int newProcessed);
	void setSkipped(int newSkipped);
	void setDone(int newDone);
}
