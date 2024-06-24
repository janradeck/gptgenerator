package gptgenerator.uc.processing.o2prompt;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import gptgenerator.processingresult.IResultController;
import gptgenerator.services.CacheEntry;
import gptgenerator.services.FileService;
import gptgenerator.uc.configure.IConfigurationController;
import gptgenerator.uc.mainview.IFileStateController;
import gptgenerator.uc.mainview.TransformationStatus;
import gptgenerator.uc.processing.IStatusDecider;
import gptgenerator.uc.processing.IStepManager;

/**
 * The PromptManager uses the ChatClient to send prompts to the Chat API
 * @see IChatClient
 */
public class PromptManager implements IStepManager {
	private static final long MINUTE_VALUE = 5;

	private IFileStateController filestateController;
	private IResultController promptResultController;
	private IConfigurationController configurationController;
	private IStatusDecider decider;

	public PromptManager(IFileStateController filestateController, IResultController promptResultController, IConfigurationController configurationController, IStatusDecider decider) {
		this.filestateController = filestateController;
		this.promptResultController = promptResultController;
		this.configurationController = configurationController;
		this.decider = decider;		
	}

	@Override
	public boolean filesReady() {
		return ! filestateController.getPromptPendingList(decider).isEmpty();
	}

	@Override
	public boolean queueEmpty() {
		return filestateController.getPromptPendingList(decider).isEmpty();
	}

	@Override
	public void process() {
		ExecutorService executorService = Executors.newFixedThreadPool(configurationController.getChatNumberOfThreads());		
		for (TransformationStatus curFile: filestateController.getPromptPendingList(decider)) {
			CacheEntry userPrompt = filestateController.getMergeFileCur(curFile.getBaseFilename());
			String systemMessage = configurationController.getSystemMessage(curFile.getBaseFilename());
			String chatApiUrl = configurationController.getChatApiURL();
			String chatApiKey = configurationController.getChatApiToken();
			String chatModel = configurationController.getChatModel();
			double chatTemperature = configurationController.getChatTemperature(curFile.getBaseFilename());
			promptResultController.addProcessedFile();

			// System.err.println("PromptManager::process() "+ prompt.toString());
			IChatClient client = configurationController.getChatClient(chatApiUrl, chatApiKey, chatModel, chatTemperature);
			String replyFilename = FileService.removePromptExt(curFile.getBaseFilename());
			CacheEntry prevReply = filestateController.getMergeFileCur(replyFilename);
			
			executorService.execute(new ChatRunner(client, filestateController, systemMessage, userPrompt, prevReply));			
		}
        executorService.shutdown();
        try {
			executorService.awaitTermination(MINUTE_VALUE, TimeUnit.MINUTES);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}		
	}
}