package gptgenerator.uc.processing.o2prompt;

import gptgenerator.services.CacheEntry;
import gptgenerator.services.FileService;
import gptgenerator.uc.mainview.IFileStateController;

/**
 * 
 */
public class ChatRunner implements Runnable {

	private IChatClient client;
	private String systemMessage;
	private CacheEntry curPromptFile;
	private CacheEntry prevReplyFile;
	private IFileStateController filestateController;

	public ChatRunner(IChatClient client, IFileStateController filestateController, String systemMessage, CacheEntry curPromptFile, CacheEntry prevReplyFile) {
		this.filestateController = filestateController;
		this.systemMessage = systemMessage;
		this.client = client;
		this.curPromptFile = curPromptFile;
		this.prevReplyFile = prevReplyFile;
	}

	/**
	 * Sends a single prompt<br>
	 * Saves the prompt in Merge(Prev) (with ".prompt" extension)<br>
	 * Saves the reply in Merge(Cur) and Merge(Prev)
	 */
	public void run() {		
		filestateController.saveMergeFilePrev(curPromptFile);
		String promptString = curPromptFile.getContent();
		String reply = this.client.sendPrompt(systemMessage, promptString);
		String replyFilename = FileService.removePromptExt(curPromptFile.getFilename());
		CacheEntry curReplyFile = new CacheEntry(replyFilename, curPromptFile.getLastModified(), reply);
		filestateController.saveMergeFileCur(curReplyFile);
		filestateController.saveMergeFilePrev(prevReplyFile);
	}

}
