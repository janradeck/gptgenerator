package gptgenerator.uc.processing;

import java.io.File;

import gptgenerator.processingresult.IResultController;
import gptgenerator.processingresult.IResultModel;
import gptgenerator.processingresult.ResultController;
import gptgenerator.processingresult.ResultModel;
import gptgenerator.services.CacheCollection;
import gptgenerator.uc.configure.ConfigurationController;
import gptgenerator.uc.configure.ConfigurationModel;
import gptgenerator.uc.configure.IConfigurationController;
import gptgenerator.uc.configure.IConfigurationModel;
import gptgenerator.uc.configure.gpt.ChatConfigModel;
import gptgenerator.uc.configure.sourcepartition.PrettyPrintSettingsModel;
import gptgenerator.uc.configure.sourcepartition.SourcePartitionModel;
import gptgenerator.uc.configure.sourcepartition.ISourcePartitionModel;
import gptgenerator.uc.mainview.FileStateController;
import gptgenerator.uc.mainview.FileStateModel;
import gptgenerator.uc.mainview.IFileStateController;
import gptgenerator.uc.mainview.IFileStateModel;
import gptgenerator.uc.mainview.MainModel;
import gptgenerator.uc.processing.o1merge.TemplateConfig;

public class ProcessManagerTestSetup {
		private IConfigurationModel configurationModel;
		private IConfigurationController configurationController;
		private CacheCollection cacheCollection;
		private IFileStateModel filestateModel;
		private IFileStateController filestateController;
		private IResultModel mergeResult = new ResultModel();
		private IResultController mergeResultController = new ResultController(mergeResult);

		private IResultModel promptResult = new ResultModel();
		private IResultController promptResultController = new ResultController(promptResult);
		
		
		public ProcessManagerTestSetup(String testCaseBase) {
			
			configurationModel = getConfigurationModel(testCaseBase);
			configurationModel.setMakeChatApiCalls(false);
			MainModel mainModel = new MainModel(testCaseBase);	
			configurationController = new ConfigurationController(mainModel, configurationModel);

			cacheCollection = new CacheCollection();
			cacheCollection.updateInput(configurationModel.getInputConfig());
			cacheCollection.updateMerge(configurationModel.getMergeConfig());

			filestateModel = new FileStateModel(cacheCollection, configurationModel);		
			filestateController = new FileStateController(configurationModel, filestateModel);
		}
		
		public static IConfigurationModel getConfigurationModel(String projectRoot) {
			IConfigurationModel configurationModel = new ConfigurationModel();
			String inputCurrentDir = projectRoot + File.separator + "InputCur";
			configurationModel.setInputCurrentDir(inputCurrentDir);
			configurationModel.setProjectRoot(projectRoot);
			
			configurationModel.addSourcePartition(makeSourcePart(projectRoot, "You are a Java developer", "java"));
			configurationModel.addSourcePartition(makeSourcePart(projectRoot, "You are a Typescript developer", "js"));
			return configurationModel;
		}
		
		private static ISourcePartitionModel makeSourcePart(String projectRoot, String systemMessage, String filetype) {
			ISourcePartitionModel result = new SourcePartitionModel();
			result.setSourceDirRel(filetype);
			result.setDestDirAbs(projectRoot + File.separator + "install" + File.separator + filetype);
			
			PrettyPrintSettingsModel ppSettings = new PrettyPrintSettingsModel();
			ppSettings.setPrettyPrintInput(false);
			ppSettings.setPrettyPrintMerge(false);
			ppSettings.setPrettierIgnoreFiles("");
			
			result.setPrettyPrintSettings(ppSettings);
			
			TemplateConfig javaTC = new TemplateConfig();
			javaTC.setMarkerStart("//");
			result.setTemplateConfig(javaTC);
			
			ChatConfigModel gConfig = new ChatConfigModel();
			gConfig.setSystemMessage(systemMessage);
			gConfig.setTemperature(1.15);
			gConfig.setIndividualTemperature(true);			
			result.setGptConfig(gConfig);
			return result;
		}
		

		public IConfigurationController getConfigurationController() {
			return configurationController;
		}

		public IConfigurationModel getConfigurationModel() {
			return configurationModel;
		}

		public CacheCollection getCacheCollection() {
			return cacheCollection;
		}

		public IFileStateModel getFilestateModel() {
			return filestateModel;
		}

		public IFileStateController getFilestateController() {
			return filestateController;
		}
		
		public IResultController getMergeResultController() {
			return mergeResultController;
		}

		public IResultController getPromptResultController() {
			return promptResultController;
		}

		public String inputCurDirectory() {
			return configurationModel.getInputCurDir();
		}

		public String inputPrevDirectory() {
			return configurationModel.getInputPrevDir();
		}

		public String mergeCurDirectory() {
			return configurationModel.getMergeCurDir();
		}

		public String mergePrevDirectory() {
			return configurationModel.getMergePrevDir();
		}

		public String getDestDirAbs(String filename) {
			return configurationModel.getInstallDir(filename);
		}

		public String inputPrevFile(String testFilename) {
			return configurationModel.getInputPrevDir() + File.separator + testFilename;
			}

		public String inputCurFile(String testFilename) {
			return configurationModel.getInputCurDir() + File.separator + testFilename;	
		}

		public String mergeCurFile(String testFilename) {
			return configurationModel.getMergeCurDir() + File.separator + testFilename;
		}

		public String mergePrevFile(String testFilename) {
			return configurationModel.getMergePrevDir() + File.separator + testFilename;
		}

		public String installFile(String testFilename) {
			return configurationModel.mapToDestination(testFilename);
		}
		
		public String installDirectory(String testFilename) {
			return configurationModel.getInstallDir(testFilename);
		}
		
}
