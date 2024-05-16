package gptgenerator.uc.processing.o1merge;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import gptgenerator.processingresult.IResultController;
import gptgenerator.services.CacheEntry;
import gptgenerator.services.FileService;
import gptgenerator.services.PrettyPrintService;
import gptgenerator.uc.configure.IConfigurationController;
import gptgenerator.uc.mainview.IFileStateController;
import gptgenerator.uc.processing.IStepManager;

/**
 * Sorgt dafür, dass alle Dateien in "Input (Cur)" behandelt werden.<br>
 * INPUT
 * <ul>
 * <li>WriteThroughCache "Input (Cur)"
 * <li>WriteThroughCache "Input (Prev)"
 * <li>WriteThroughCache "Merge (Cur)"
 * </ul>
 * OUTPUT
 * <ul>
 * <li>WriteThroughCache "Merge (Cur)"
 * <li>WriteThroughCache "Merge (Prev)"
 * </ul>
 */
public class MergeManager implements IStepManager {

	private IConfigurationController configurationController;
	private IFileStateController filestateController;
	private IResultController mergeResultController;
	private List<String> pending = new ArrayList<>();
	
	public MergeManager(IConfigurationController configurationController, IFileStateController filestateController, IResultController mergeResultController) {
		this.configurationController = configurationController;
		this.filestateController = filestateController;
		this.mergeResultController = mergeResultController;
		prettyPrintInputIfDesired();
		saveCurrentPromptAndReply();
		moveMergeCurToMergePrev();
		setupPending();
	}
	
	private void prettyPrintInputIfDesired() {
		PrettyPrintService.prettyPrintInput(configurationController);		
	}
	
	private void saveCurrentPromptAndReply() {
		Set<String> promptList = filestateController.getCurPromptList();
		Set<String> replyList = filestateController.getCurReplyList();
		filestateController.saveToReplyCache(promptList);
		filestateController.saveToReplyCache(replyList);
	}

	private void moveMergeCurToMergePrev() {
		filestateController.moveMergeCurToMergePrev();
	}

	/**
	 * Are files ready to be merged?
	 */
	@Override
	public boolean filesReady() {
		for (String curFilename: pending) {
			MergeProcessor mp = new MergeProcessor(configurationController, filestateController, curFilename);
			if (mp.readyToMerge()) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean queueEmpty() {
		return pending.isEmpty();
	}

	/**
	 * Processes the pending files by merging them using the MergeProcessor.
	 * This method iterates over the pending files and checks if each file is ready to be merged.
	 * If a file is ready, it merges the file using the MergeProcessor, saves the merge result,
	 * updates the file state, and removes the file from the pending list.
	 * If the merged file is a prompt file, it restores the reply if the prompt has not changed.
	 */
	@Override
	public void process() {
		Iterator<String> i = pending.iterator();
		while (i.hasNext()) {
			String curFilename = i.next(); // must be called before you can call i.remove()
			MergeProcessor mp = new MergeProcessor(configurationController, filestateController, curFilename);
			if (mp.readyToMerge()) {
				CacheEntry merged = mp.merge();
				filestateController.saveMergeFileCur(merged);
				filestateController.saveInputFilePrev(mp.getSourceEntry());
				mergeResultController.addProcessedFile();
				i.remove();
				
				if (FileService.isPrompt(curFilename)) {
					restoreReplyIfPromptUnchanged(merged);
				}
			}
		}		
	}

	private void restoreReplyIfPromptUnchanged(CacheEntry curPromptEntry) {
		String promptFilename = curPromptEntry.getFilename();
		String replyFilename = FileService.removePromptExt(promptFilename);
		CacheEntry cachedReply = filestateController.getFromReplyCache(replyFilename);
		CacheEntry cachedPrompt = filestateController.getFromReplyCache(promptFilename);
		if (!cachedPrompt.isEmpty() && !cachedReply.isEmpty()) {
			String curPromptContent = curPromptEntry.getContent();
			String cachedPromptContent = cachedPrompt.getContent();
			if (curPromptContent.equals(cachedPromptContent)) {
				filestateController.saveMergeFileCur(cachedReply);
			}
		}
	}

	/**
	 * All files are merged, to avoid dependency management
	 */
	private void setupPending() {
		for (String cur: filestateController.getInputFiles()) {
			pending.add(cur);
			mergeResultController.addPendingFile();
		}		
	}

}
