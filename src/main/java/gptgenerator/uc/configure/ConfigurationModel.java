package gptgenerator.uc.configure;

import java.io.File;

import com.fasterxml.jackson.annotation.JsonIgnore;

import gptgenerator.services.FileService;
import gptgenerator.uc.configure.gpt.ChatTemperature;
import gptgenerator.uc.configure.merge.ITemplateConfigModel;
import gptgenerator.uc.configure.sourcepartition.ISourcePartitionModel;
import gptgenerator.uc.configure.sourcepartition.SourcePartitioning;
import gptgenerator.uc.filecompare.CurrentAndPrevious;
import gptgenerator.uc.processing.o1merge.TemplateConfig;
import gptgenerator.uc.processing.o2prompt.IChatClient;

/**
 * Application-wide configuration values
 */
public class ConfigurationModel implements IConfigurationModel {
	public static final String INPUT_PREV = "InputPrev";	
	public static final String MERGE_CUR = "MergeCur";	
	public static final String MERGE_PREV = "MergePrev";
	public static final String REPLY_CACHE = "PromptReplyCache";

	public static final String PROCESSING_DIR = "processing";
	
	private String projectRoot = "";
	private String inputCurDir = "";
	
	private SourcePartitioning sourcePartitions = new SourcePartitioning();
	
	private ChatTemperature chatTemperature = new ChatTemperature();
	private ProcessingThreadCount chatNumberOfThreads = new ProcessingThreadCount();
	private Boolean chatMakeApiCalls = false;
	private String chatApiURL = "";
	private String chatApiToken = "";
	private String chatModel = "";
	
	@JsonIgnore
	private IConfigurationController controller = new NilConfigurationController();
	
	@JsonIgnore
	private boolean isValid = false;

	@JsonIgnore
	private CurrentAndPrevious inputConfig;
	
	@JsonIgnore
	private CurrentAndPrevious mergeConfig;
	
	@JsonIgnore
	private String promptReplyCache;
	
	public ConfigurationModel() {
	}

	public void setInstalls(SourcePartitioning sourcePartitions) {
		this.sourcePartitions = sourcePartitions;
		controller.notifySetSourcePartitions(sourcePartitions);
	}
	
	@Override
	public void setProjectRoot(String projectRoot) {
		if (! this.projectRoot.equals(projectRoot)) {
			this.projectRoot = projectRoot;
			updateDirectoriesAndNotify();
		}
	}

	@Override
	public void setInputCurrentDir(String newInputCurrentDir) {
		if (!inputCurDir.equals(newInputCurrentDir)) {
			this.inputCurDir = newInputCurrentDir;
			controller.notifySetInputCurrentDir(newInputCurrentDir);
		}
	}

	@Override
	public void setChatNumberOfThreads(int newChatNumberOfThreads) {
		if (this.chatNumberOfThreads.getCount() != newChatNumberOfThreads) {
			this.chatNumberOfThreads.setCount(newChatNumberOfThreads);
			controller.notifySetChatNumberOfThreads(String.format("%d", newChatNumberOfThreads));
		}
	}
	
	@Override
	public void setChatTemperature(Double newChatTemperatureValue) {
		if (!chatTemperature.getTemperature().equals(newChatTemperatureValue)) {
			chatTemperature.setTemperature(newChatTemperatureValue);
			controller.notifySetChatTemperature(getChatNumberOfThreadsString().formatted("%.1f", newChatTemperatureValue));
		}
	}

	@Override
	public void setChatTemperature(String newChatTemperatureString) {
		if (!chatTemperature.getTemperatureString().equals(newChatTemperatureString)) {
			chatTemperature.setTemperature(newChatTemperatureString);
			controller.notifySetChatTemperature(newChatTemperatureString);
		}
	}

	@Override
	public void setChatNumberOfThreads(Integer newChatNumberOfThreads) {
		if (this.chatNumberOfThreads.getCount() != newChatNumberOfThreads) {
			this.chatNumberOfThreads.setCount(newChatNumberOfThreads);
			controller.notifySetChatNumberOfThreads(newChatNumberOfThreads.toString());
		}
	}
	
	@Override
	public void setChatModel(String newChatModel) {
		if (!chatModel.equals(newChatModel)) {
			chatModel = newChatModel;
			controller.notifySetChatModel(newChatModel);
		}
	}

	@Override
	public void addSourcePartition(ISourcePartitionModel newPartition) {
		sourcePartitions.addPartition(newPartition);
		controller.notifyAddPartition(newPartition);
	}

	@Override
	public void removeSourcePartitionAt(int deleteIndex) {
		sourcePartitions.remove(deleteIndex);
		controller.notifyRemovePartition(deleteIndex);
	}
	
	@Override
	public void setSourcePartitionAt(int index, ISourcePartitionModel sourcePartition) {
		sourcePartitions.setPartition(index, sourcePartition);
		controller.notifySetPartitionAt(index, sourcePartition);
	}
	
	@JsonIgnore
	public ISourcePartitionModel getSourcePartition(int index) {
		return sourcePartitions.getPartition(index);
	}

	@Override
	public String getProjectRoot() {
		return projectRoot;
	}

	@Override
	public String getInputCurDir() {
		return inputCurDir;
	}

	@JsonIgnore
	@Override
	public String getInputPrevDir() {
		return inputConfig.getPreviousBaseDir();
	}

	@Override
	public Integer getChatNumberOfThreads() {
		return chatNumberOfThreads.getCount();
	}
		
	@Override
	public CurrentAndPrevious getInputConfig() {
		return inputConfig;
	}
	
	@Override
	public CurrentAndPrevious getMergeConfig() {
		return mergeConfig;
	}

	@Override
	@JsonIgnore
	public String getChatTemperatureString() {
		return chatTemperature.getTemperatureString();
	}

	@JsonIgnore
	public String getChatNumberOfThreadsString() {
		return String.format("%d", chatNumberOfThreads.getCount());
	}
	
	@Override
	public String getChatModel() {
		return chatModel;
	}

	@Override
	public SourcePartitioning getSourcePartitions() {
		return sourcePartitions;
	}
	
	/**
	 * baseFilename: filename without a directory like INPUT(CUR)
	 */
	@Override
	public String mapToDestination(String baseFilename) {
		return sourcePartitions.mapToDestination(baseFilename);
	}
	
	@Override
	public void setInputConfig(CurrentAndPrevious inputConfig) {
		this.inputConfig = inputConfig;
	}

	@Override
	public void setMergeConfig(CurrentAndPrevious mergeConfig) {
		this.mergeConfig = mergeConfig;
	}

	
	@JsonIgnore
	@Override
	public String getInstallDir(String filename) {
		return sourcePartitions.getDestDirAbs(filename);
	}

	@JsonIgnore
	@Override
	public String getMergeCurDir() {
		return mergeConfig.getCurrentBaseDir();
	}

	@JsonIgnore
	@Override
	public String getMergePrevDir() {
		return mergeConfig.getPreviousBaseDir();
	}
	
	/**
	 *  Update directories and configuration after "projectRoot" or "inputCurDir" has been set
	 */
	@Override
	public void updateDirectoriesAndNotify() {
		projectRoot = FileService.stripSeparator(projectRoot);
		String workDir = projectRoot + File.separator + PROCESSING_DIR + File.separator;
		inputCurDir = FileService.stripSeparator(inputCurDir);

		inputConfig = new CurrentAndPrevious(inputCurDir, workDir + INPUT_PREV);
		mergeConfig = new CurrentAndPrevious(workDir + MERGE_CUR, workDir + MERGE_PREV);
		promptReplyCache = workDir + REPLY_CACHE;
		// TODO: Notify listeners for directories and inputConfig / mergeConfig
		this.controller.notifySetProjectRoot(projectRoot);
	}

	@Override
	public void setController(IConfigurationController controller) {
		this.controller = controller;
	}

	@Override
	public void clearController() {
		this.controller = new NilConfigurationController();
	}

	@Override
	public Boolean getChatMakeApiCalls() {
		return chatMakeApiCalls;
	}

	@Override
	public void setMakeChatApiCalls(boolean makeApiCalls) {
		if(this.chatMakeApiCalls != makeApiCalls) {
			this.chatMakeApiCalls = makeApiCalls;
			controller.notifySetMakeApiCalls(makeApiCalls);
		}
	}
	
	@JsonIgnore
	@Override
	public String getSystemMessage(String baseFilename) {
		return sourcePartitions.getSystemMessage(baseFilename);
	}

	@JsonIgnore
	@Override
	public Double getChatTemperature(String baseFilename) {
		if (sourcePartitions.hasIndividualTemperature(baseFilename)) {
			return sourcePartitions.getIndividualTemperature(baseFilename);
		}
		return chatTemperature.getTemperature();
	}
	
	@Override
	public ITemplateConfigModel getTemplateConfig(String sourceRelFilename) {
		return sourcePartitions.getTemplateConfig(sourceRelFilename);
	}

	@JsonIgnore
	@Override
	public String getReplyCacheDir() {
		return promptReplyCache;
	}


	@Override
	public void markAsValid() {
		this.isValid = true;
	}

	@Override
	public boolean isValid() {
		return this.isValid;
	}

	@Override
	public void setChatApiURL(String apiURL) {
		if (!this.chatApiURL.equals(apiURL)) {
			this.chatApiURL = apiURL;
			controller.notifySetChatApiURL(apiURL);
		}
	}

	@Override
	public String getChatApiURL() {
		return chatApiURL;
	}

	@Override
	public void setChatApiToken(String apiToken) {
		if (!this.chatApiToken.equals(apiToken)) {
			this.chatApiToken = apiToken;
			controller.notifySetChatApiToken(apiToken);
		}
	}	

	@Override
	public String getChatApiToken() {
		return chatApiToken;
	}
	
	
	private class NilConfigurationController implements IConfigurationController {

		@Override
		public void addView(IConfigurationView view) {
			System.err.println("NilConfigurationController::addView() called");			
		}

		@Override
		public void removeView(IConfigurationView view) {
			System.err.println("NilConfigurationController::removeView() called");			
		}

		@Override
		public void requestViewUpdate() {
		}

		@Override
		public void setProjectRoot(String root) {
		}

		@Override
		public void setChatTemperature(String temperatureText) {
		}

		@Override
		public void removePartition(int index) {
		}

		@Override
		public void notifySetProjectRoot(String root) {
		}

		@Override
		public void notifySetChatTemperature(String temperatureText) {
		}

		@Override
		public void notifyRemovePartition(int index) {
		}

		@Override
		public void notifySetChatNumberOfThreads(String numberOfThreads) {
		}

		@Override
		public Integer getChatNumberOfThreads() {
			return null;
		}

		@Override
		public SourcePartitioning getSourcePartitioning() {
			return null;
		}

		@Override
		public void setChatNumberOfThreads(int number) {
		}

		@Override
		public void setInputCurrentDir(String dir) {
		}

		@Override
		public void notifySetInputCurrentDir(String dir) {
		}

		@Override
		public void notifySetSourcePartitions(SourcePartitioning installs) {
		}

		@Override
		public boolean makeApiCalls() {
			return false;
		}

		@Override
		public void setMakeApiCalls(boolean isProd) {
		}

		@Override
		public void notifySetMakeApiCalls(boolean isProd) {
		}

		@Override
		public String getSystemMessage(String baseFilename) {
			return null;
		}

		@Override
		public Double getChatTemperature(String string) {
			return null;
		}

		@Override
		public IChatClient getChatClient(String chatApiUrl, String chatApiKey, String chatModel, double temperature) {
			return null;
		}

		@Override
		public TemplateConfig getTemplateConfig(String cur) {
			return null;
		}

		@Override
		public String getReplyCacheDir() {
			return null;
		}

		@Override
		public String getInputCurDir() {
			return null;
		}

		@Override
		public void notifyAddPartition(ISourcePartitionModel installCfg) {
		}

		@Override
		public void setPartitionAt(int index, ISourcePartitionModel sourcePartition) {
		}

		@Override
		public void addPartition(ISourcePartitionModel sourcePartition) {
		}

		@Override
		public void notifySetPartitionAt(int index, ISourcePartitionModel installCfg) {
		}

		@Override
		public String getMergeCurDir() {
			return null;
		}

		@Override
		public void notifySetChatApiURL(String apiURL) {
		}

		@Override
		public void setChatApiURL(String apiURL) {
		}

		@Override
		public void setChatApiKey(String apiToken) {
		}

		@Override
		public void notifySetChatApiToken(String apiToken) {
		}

		@Override
		public String getChatApiURL() {
			return null;
		}

		@Override
		public String getChatApiToken() {
			return null;
		}

		@Override
		public void notifySetChatModel(String newChatModel) {
		}

		@Override
		public String getChatModel() {
			return null;
		}

	}

}
