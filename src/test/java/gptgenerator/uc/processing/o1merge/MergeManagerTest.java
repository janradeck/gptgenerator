package gptgenerator.uc.processing.o1merge;

import java.io.File;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import gptgenerator.processingresult.ResultController;
import gptgenerator.processingresult.ResultModel;
import gptgenerator.services.CacheCollection;
import gptgenerator.services.FileService;
import gptgenerator.uc.configure.IConfigurationController;
import gptgenerator.uc.configure.IConfigurationModel;
import gptgenerator.uc.configure.ConfigurationController;
import gptgenerator.uc.filecompare.CurrentAndPrevious;
import gptgenerator.uc.mainview.FileStateController;
import gptgenerator.uc.mainview.FileStateModel;
import gptgenerator.uc.mainview.MainModel;
import gptgenerator.uc.processing.ProcessManagerTestSetup;

class MergeManagerTest {

	@Test
	void testFlatIncomplete() {
		final String base = "./src/test/resources/merge/incomplete";

		MainModel mainModel = new MainModel(base);

		IConfigurationModel configurationModel = ProcessManagerTestSetup.getConfigurationModel(base);
		
		FileService.clearAndDeleteDirectory(inputPrevDirectory(base));
		FileService.clearAndDeleteDirectory(mergeCurDirectory(base));
		FileService.clearAndDeleteDirectory(mergePrevDirectory(base));

		CurrentAndPrevious input = new CurrentAndPrevious(inputCurDirectory(base), inputPrevDirectory(base));
		CurrentAndPrevious merge = new CurrentAndPrevious(mergeCurDirectory(base), mergePrevDirectory(base));
		CacheCollection cacheCollection = new CacheCollection();
		cacheCollection.updateInput(input);
		cacheCollection.updateMerge(merge);
		IConfigurationController configurationController = new ConfigurationController(mainModel, configurationModel);
		
		ResultModel mergeResultModel = new ResultModel();
		ResultController mergeResultController = new ResultController(mergeResultModel);		

		Assertions.assertEquals(5, cacheCollection.getInputCur().getFilenameSet().size());
		FileStateModel fsModel = new FileStateModel(cacheCollection, configurationModel);

		FileStateController fsc = new FileStateController(configurationModel, fsModel);
		MergeManager mergeManager = new MergeManager(configurationController, fsc, mergeResultController);
		
		for (int i = 0; i < 3; i ++) {
			if (mergeManager.filesReady()) {
				mergeManager.process();
			}
		}
		Assertions.assertEquals(1, fsc.getInstallFiles().size());
		
		Assertions.assertFalse(mergeManager.queueEmpty());
		Assertions.assertFalse(mergeManager.filesReady());
	}

	/**
	 * The input directory should contain (at least) one .java file<br>
	 * This is required by<br>
	 * Assertions.assertEquals(1, fsc.getInstallFiles().size());<br>
	 * A .prompt file does not count as installable
	 */
	@Test
	void testSubdirComplete() {
		String base = "./src/test/resources/merge/subdir";
		FileService.clearAndDeleteDirectory(inputPrevDirectory(base));
		FileService.clearAndDeleteDirectory(mergeCurDirectory(base));
		FileService.clearAndDeleteDirectory(mergePrevDirectory(base));

		CurrentAndPrevious input = new CurrentAndPrevious(inputCurDirectory(base), inputPrevDirectory(base));
		CurrentAndPrevious merge = new CurrentAndPrevious(mergeCurDirectory(base), mergePrevDirectory(base));
		CacheCollection cacheCollection = new CacheCollection();
		cacheCollection.updateInput(input);
		cacheCollection.updateMerge(merge);

		MainModel mainModel = new MainModel(null);
		IConfigurationModel configurationModel = ProcessManagerTestSetup.getConfigurationModel(base);
		IConfigurationController configurationController = new ConfigurationController(mainModel, configurationModel);
		
		ResultModel mergeResultModel = new ResultModel();
		ResultController mergeResultController = new ResultController(mergeResultModel);		

		Assertions.assertEquals(3, cacheCollection.getInputCur().getFilenameSet().size());
		FileStateModel fsModel = new FileStateModel(cacheCollection, configurationModel);

		FileStateController fsc = new FileStateController(configurationModel, fsModel);
		MergeManager mergeManager = new MergeManager(configurationController, fsc, mergeResultController);
		
		for (int i = 0; i < 3; i ++) {
			if (mergeManager.filesReady()) {
				mergeManager.process();
			}
		}
		Assertions.assertEquals(1, fsc.getInstallFiles().size());
		
		Assertions.assertTrue(mergeManager.queueEmpty());
		Assertions.assertFalse(mergeManager.filesReady());
	}

	
	private String inputCurDirectory(String testBaseDirectory) {
		return testBaseDirectory + File.separator + "inputCur";
	}
	
	private String mergeCurDirectory (String testBaseDirectory) {
		return testBaseDirectory + File.separator + "mergeCur";
	}
	
	private String inputPrevDirectory (String testBaseDirectory) {
		return testBaseDirectory + File.separator + "inputPrev";
	}
	
	private String mergePrevDirectory (String testBaseDirectory) {
		return testBaseDirectory + File.separator + "mergePrev";
	}

}
