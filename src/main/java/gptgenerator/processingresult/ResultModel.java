package gptgenerator.processingresult;

/**
 * Statistics on a processing run
 */
public class ResultModel implements IResultModel {
	private int pending = 0;
	private int processed = 0;
	private int skipped = 0;
	private int done = 0;
	
	private IResultController controller = new NilResultController();

	@Override
	public void setController(IResultController resultController) {
		controller = resultController;		
	}
	
	@Override
	public void clearController() {
		controller = new NilResultController();
	}
	
	@Override
	public void clear() {
		pending = 0;
		controller.notifySetPending(pending);

		processed = 0;
		controller.notifySetProcessed(processed);

		skipped = 0;
		controller.notifySetSkipped(skipped);
		
		done = 0;
		controller.notifySetDone(done);		
	}
	
	@Override
	public void addPending() {
		incPending();
	}
		
	@Override
	public void addProcessed() {
		decPending();
		incProcessed();
		incDone();
	}

	@Override
	public void addSkipped() {
		decPending();
		incSkipped();
		incDone();
	}

	private void incDone() {
		done ++;
		controller.notifySetDone(done);
	}

	private void incSkipped() {
		skipped ++;
		controller.notifySetSkipped(skipped);
	}
	
	private void decPending() {
		pending --;
		controller.notifySetPending(pending);
	}

	private void incPending() {
		pending ++;
		controller.notifySetPending(pending);
	}
	
	private void incProcessed() {
		processed ++;
		controller.notifySetProcessed(processed);		
	}
	
	@Override
	public int getPending() {
		return pending;
	}
	
	@Override
	public int getProcessed() {
		return processed;
	}

	@Override
	public int getSkipped() {
		return skipped;
	}

	@Override
	public int getDone() {
		return done;
	}
	
	@Override
	public String toString() {
		return String.format("Pending: %4d, Processed: %4d, Skipped: %4d", getPending(), getProcessed(), getSkipped());
	}
	
	/**
	 * Temporary controller to avoid null-pointer exceptions
	 */
	private static class NilResultController implements IResultController {

		@Override
		public void notifySetPending(int newPending) {
		}

		@Override
		public void notifySetProcessed(int newProcessed) {
		}

		@Override
		public void notifySetSkipped(int newSkipped) {
		}

		@Override
		public void notifySetDone(int newTotal) {
		}


		@Override
		public void addView(IResultModelView view)  {
			System.err.println("NilResultController::addView() called!");
		}

		@Override
		public void removeView(IResultModelView view) {
			System.err.println("NilResultController::removeView() called!");
		}

		@Override
		public void requestViewUpdate() {
			System.err.println("NilResultController::requestViewUpdate() called!");
		}

		@Override
		public void clear() {
		}

		@Override
		public void addProcessedFile() {
		}

		@Override
		public void addPendingFile() {
		}

		@Override
		public void addSkippedFile() {
		}

	}

}