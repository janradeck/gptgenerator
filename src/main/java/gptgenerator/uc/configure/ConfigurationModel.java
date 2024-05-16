package gptgenerator.uc.configure;

import java.io.File;

import com.fasterxml.jackson.annotation.JsonIgnore;

import gptgenerator.services.FileService;
import gptgenerator.uc.configure.gpt.GptTemperature;
import gptgenerator.uc.configure.merge.ITemplateConfigModel;
import gptgenerator.uc.configure.sourcepartition.ISourcePartitionModel;
import gptgenerator.uc.configure.sourcepartition.SourcePartitioning;
import gptgenerator.uc.filecompare.CurrentAndPrevious;
import gptgenerator.uc.processing.o1merge.TemplateConfig;
import gptgenerator.uc.processing.o2prompt.IChatClient;

/**
 * ConfigurationModel values
 */
public class ConfigurationModel implements IConfigurationModel {
	public static final String INPUT_PREV = "InputPrev";	
	public static final String MERGE_CUR = "MergeCur";	
	public static final String MERGE_PREV = "MergePrev";
	public static final String REPLY_CACHE = "PromptReplyCache";

	public static final String PROCESSING_DIR = "processing";
	
	private String projectRoot = "";
	private String inputCurDir = "";
	
	private SourcePartitioning installs = new SourcePartitioning();
	
	private GptTemperature temperature = new GptTemperature();
	private ProcessingThreadCount numberOfThreads = new ProcessingThreadCount();
	
	private boolean isProd = true;
	
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

	public void setInstalls(SourcePartitioning installs) {
		this.installs = installs;
		controller.notifySetInstalls(installs);
	}
	
	@Override
	public void setProjectRoot(String projectRoot) {
		if (! this.projectRoot.equals(projectRoot)) {
			this.projectRoot = projectRoot;
			updateDirectoriesAndNotify();
		}
	}

	@Override
	public void setInputCurrentDir(String dir) {
		if (!inputCurDir.equals(dir)) {
			this.inputCurDir = dir;
			controller.notifySetInputCurrentDir(dir);
		}
	}

	@Override
	public void setNumberOfThreads(int number) {
		if (this.numberOfThreads.getCount() != number) {
			this.numberOfThreads.setCount(number);
			controller.notifySetNumberOfThreads(String.format("%d", number));
		}
	}
	
	public void setTemperature(Double temperatureValue) {
		if (!temperature.getTemperature().equals(temperatureValue)) {
			temperature.setTemperature(temperatureValue);
			controller.notifySetTemperature(getNumberOfThreadsString().formatted("%.1f", temperatureValue));
		}
	}

	public void setTemperature(String temperatureText) {
		if (!temperature.getTemperatureString().equals(temperatureText)) {
			temperature.setTemperature(temperatureText);
			controller.notifySetTemperature(temperatureText);
		}
	}

	public void setNumberOfThreads(Integer numberOfThreads) {
		if (this.numberOfThreads.getCount() != numberOfThreads) {
			this.numberOfThreads.setCount(numberOfThreads);
			controller.notifySetNumberOfThreads(numberOfThreads.toString());
		}
	}

	@Override
	public void addPartition(ISourcePartitionModel installCfg) {
		installs.addPartition(installCfg);
		controller.notifyAddPartition(installCfg);
	}

	@Override
	public void removeInstall(int deleteIndex) {
		installs.remove(deleteIndex);
		controller.notifyRemovePartition(deleteIndex);
	}
	
	@Override
	public void setInstallAt(int index, ISourcePartitionModel installCfg) {
		installs.setPartition(index, installCfg);
		controller.notifySetPartitionAt(index, installCfg);
	}
	
	@JsonIgnore
	public ISourcePartitionModel getInstall(int index) {
		return installs.getPartition(index);
	}

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

	public Double getTemperature() {
		return temperature.getTemperature();
	}

	public Integer getNumberOfThreads() {
		return numberOfThreads.getCount();
	}
		
	public CurrentAndPrevious getInputConfig() {
		return inputConfig;
	}
	
	public CurrentAndPrevious getMergeConfig() {
		return mergeConfig;
	}

	@Override
	@JsonIgnore
	public String getTemperatureString() {
		return temperature.getTemperatureString();
	}

	@JsonIgnore
	public String getNumberOfThreadsString() {
		return String.format("%d", numberOfThreads.getCount());
	}

	@Override
	public SourcePartitioning getInstalls() {
		return installs;
	}
	
	/**
	 * baseFilename: filename without a directory like INPUT(CUR)
	 */
	@Override
	public String mapToDestination(String baseFilename) {
		return installs.mapToDestination(baseFilename);
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
		return installs.getDestDirAbs(filename);
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
	}

	@Override
	public void setController(IConfigurationController controller) {
		this.controller = controller;
	}

	@Override
	public void clearController() {
		this.controller = new NilConfigurationController();
	}

	public boolean isProd() {
		return isProd;
	}

	public void setProd(boolean isProd) {
		if(this.isProd != isProd) {
			this.isProd = isProd;
			controller.notifySetIsProd(isProd);
		}
	}
	
	@JsonIgnore
	@Override
	public String getSystemMessage(String baseFilename) {
		return installs.getSystemMessage(baseFilename);
	}

	@JsonIgnore
	@Override
	public Double getTemperature(String baseFilename) {
		if (installs.hasIndividualTemperature(baseFilename)) {
			return installs.getIndividualTemperature(baseFilename);
		}
		return temperature.getTemperature();
	}
	
	@Override
	public ITemplateConfigModel getTemplateConfig(String cur) {
		return installs.getTemplateConfig(cur);
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
		public void setTemperature(String temperatureText) {
		}

		@Override
		public void removePartition(int index) {
		}

		@Override
		public void notifySetProjectRoot(String root) {
		}

		@Override
		public void notifySetTemperature(String temperatureText) {
		}

		@Override
		public void notifyRemovePartition(int index) {
		}

		@Override
		public void notifySetNumberOfThreads(String numberOfThreads) {
		}

		@Override
		public Integer getNumberOfThreads() {
			return null;
		}

		@Override
		public SourcePartitioning getSourcePartitioning() {
			return null;
		}

		@Override
		public void setNumberOfThreads(int number) {
		}

		@Override
		public void setInputCurrentDir(String dir) {
		}

		@Override
		public void notifySetInputCurrentDir(String dir) {
		}

		@Override
		public void notifySetInstalls(SourcePartitioning installs) {
		}

		@Override
		public boolean isProd() {
			return false;
		}

		@Override
		public void setProd(boolean isProd) {
		}

		@Override
		public void notifySetIsProd(boolean isProd) {
		}

		@Override
		public String getSystemMessage(String baseFilename) {
			return null;
		}

		@Override
		public Double getTemperature(String string) {
			return null;
		}

		@Override
		public IChatClient getChatClient(double temperature) {
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

	}

}
