package gptgenerator.processingresult;

import gptgenerator.uc.mainview.IProcessingTimerView;

public class ProcessingTimerModel implements IProcessingTimerModel {
	private long start = 0L;
	private long end = 0L;
	private boolean running = false;
	
	private IProcessingTimerController controller = new NilProcessingTimerController();
	
	public ProcessingTimerModel(IProcessingTimerController controller) {
		this.controller = controller;
	}
	
	@Override
	public void reset() {
		start = 0L;
		end = 0L;
		running = false;
		controller.notifyReset();
	}

	@Override
	public void start() {
		start = System.currentTimeMillis();
		running = true;
		controller.notifyStart();
	}

	@Override
	public void stop() {
		end = System.currentTimeMillis();
		running = false;
		controller.notifyStop(duration());
	}

	@Override
	public long duration() {
		if (running) {
			return (System.currentTimeMillis() - start) / 1000;
		} else {
			return (end - start) / 1000;
		}
	}

	@Override
	public boolean isRunning() {
		return running;
	}
	
	@Override
	public void setController(IProcessingTimerController controller) {
		this.controller = controller;		
	}

	@Override
	public void clearController() {
		controller = new NilProcessingTimerController();		
	}
	
	private class NilProcessingTimerController implements IProcessingTimerController {

		@Override
		public void reset() {
		}

		@Override
		public void start() {
		}

		@Override
		public void stop() {
		}

		@Override
		public long duration() {
			return 0;
		}

		@Override
		public void addView(IProcessingTimerView view) {
		}

		@Override
		public void removeView(IProcessingTimerView view) {
		}

		@Override
		public void requestViewUpdate() {
		}

		@Override
		public void notifyReset() {
		}

		@Override
		public void notifyStart() {
		}

		@Override
		public void notifyStop(long duration) {
		}
		
	}

}
