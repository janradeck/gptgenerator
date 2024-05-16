package gptgenerator.processingresult;

import java.util.ArrayList;
import java.util.List;

public class ResultController implements IResultController {
	
    private List<IResultModelView> registeredViews = new ArrayList<IResultModelView>();
    private IResultModel model;
	
	public ResultController(IResultModel model) {
		this.model = model;
		model.setController(this);
	}

	@Override
	public void clear() {
		model.clear();		
	}
	
	@Override
	public void notifySetPending(int newPending) {
		for (IResultModelView view: registeredViews) {
			view.setPending(newPending);
		}
    }	
	
	@Override
	public void notifySetProcessed(int newProcessed) {
		for (IResultModelView view: registeredViews) {
			view.setProcessed(newProcessed);
		}
    }	

	@Override
	public void notifySetSkipped(int newSkipped) {
		for (IResultModelView view: registeredViews) {
			view.setSkipped(newSkipped);
		}
    }

	@Override
	public void notifySetDone(int newDone) {
		for (IResultModelView view: registeredViews) {
			view.setDone(newDone);
		}
    }
	
	@Override
	public void addView(IResultModelView view) {
		registeredViews.add(view);		
	}

	@Override
	public void removeView(IResultModelView view) {
		registeredViews.remove(view);
	}

	@Override
	public void requestViewUpdate() {
		notifySetPending(model.getPending());
		notifySetSkipped(model.getSkipped());
		notifySetProcessed(model.getProcessed());
		notifySetDone(model.getDone());
	}

	@Override
	public void addProcessedFile() {
		model.addProcessed();		
	}

	@Override
	public void addPendingFile() {
		model.addPending();		
	}

	@Override
	public void addSkippedFile() {
		model.addSkipped();		
	}

}
