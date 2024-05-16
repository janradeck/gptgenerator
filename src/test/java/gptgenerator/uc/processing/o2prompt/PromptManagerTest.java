package gptgenerator.uc.processing.o2prompt;

import java.io.File;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import gptgenerator.processingresult.IResultController;
import gptgenerator.processingresult.IResultModel;
import gptgenerator.processingresult.ResultController;
import gptgenerator.processingresult.ResultModel;
import gptgenerator.services.CacheCollection;
import gptgenerator.services.FileService;
import gptgenerator.uc.configure.ConfigurationController;
import gptgenerator.uc.configure.IConfigurationController;
import gptgenerator.uc.configure.IConfigurationModel;
import gptgenerator.uc.mainview.FileStateController;
import gptgenerator.uc.mainview.FileStateModel;
import gptgenerator.uc.mainview.IFileStateController;
import gptgenerator.uc.mainview.IFileStateModel;
import gptgenerator.uc.mainview.MainModel;
import gptgenerator.uc.processing.IStatusDecider;
import gptgenerator.uc.processing.ProcessManagerTestSetup;
import gptgenerator.uc.processing.StatusDeciderOnChange;

class PromptManagerTest {
	private static final String testBaseDirectory = "./src/test/resources/prompt";
	
	private static final String replyFilename = "request.file";

	@Test
	void test() {
		String projectRoot = testBaseDirectory;
		MainModel mainModel = new MainModel(null);
		IConfigurationModel configurationModel = ProcessManagerTestSetup.getConfigurationModel(projectRoot);

		FileService.remove(configurationModel.getMergeCurDir() + File.separator + replyFilename);
		FileService.clearDirectory(configurationModel.getMergePrevDir());
		
		CacheCollection cacheCollection = new CacheCollection();
		
		cacheCollection.updateInput(configurationModel.getInputConfig());
		cacheCollection.updateMerge(configurationModel.getMergeConfig());
		
		configurationModel.setProd(false);
		IFileStateModel filestateModel = new FileStateModel(cacheCollection, configurationModel);
		IFileStateController filestateController = new FileStateController(configurationModel, filestateModel);
		IResultModel promptResult = new ResultModel();
		IResultController promptResultController = new ResultController(promptResult);
		IConfigurationController configurationController = new ConfigurationController(mainModel, configurationModel);

		IStatusDecider decider = new StatusDeciderOnChange();
		
		PromptManager pm = new PromptManager(filestateController, promptResultController, configurationController, decider);
		
		Assertions.assertTrue(pm.filesReady());
		pm.process();
		Assertions.assertTrue(pm.queueEmpty());
		}

}
