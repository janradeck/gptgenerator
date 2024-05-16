package gptgenerator.processingresult;

import java.util.ArrayList;
import java.util.List;

import gptgenerator.uc.mainview.IProcessingTimerView;

public class ProcessingTimerController implements IProcessingTimerController {

	private IProcessingTimer timer;
    private List<IProcessingTimerView> views = new ArrayList<>();
	
	public ProcessingTimerController (IProcessingTimer timer) {
		this.timer = timer;
		timer.setController(this);
	}
	
	@Override
	public void reset() {
	}

	@Override
	public void start() {
		timer.start();
	}

	@Override
	public void stop() {
		timer.stop();
	}

	@Override
	public long duration() {
		return timer.duration();
	}

	@Override
	public void addView(IProcessingTimerView view) {
		views.add(view);
	}

	@Override
	public void removeView(IProcessingTimerView view) {
		views.remove(view);
	}

	@Override
	public void requestViewUpdate() {
		for (IProcessingTimerView view : views) {
			view.setDuration(timer.isRunning(), timer.duration());
		}
	}

	@Override
	public void notifyReset() {
		for (IProcessingTimerView view : views) {
			view.notifyReset();
		}
	}

	@Override
	public void notifyStart() {
		for (IProcessingTimerView view : views) {
			view.notifyStart();
		}
	}

	@Override
	public void notifyStop(long duration) {
		for (IProcessingTimerView view : views) {
			view.notifyStop(duration);
		}
	}

}
