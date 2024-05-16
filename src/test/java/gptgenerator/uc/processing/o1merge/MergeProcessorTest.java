package gptgenerator.uc.processing.o1merge;

import java.io.File;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import gptgenerator.services.CacheCollection;
import gptgenerator.services.CacheEntry;
import gptgenerator.services.FileService;
import gptgenerator.uc.configure.ConfigurationController;
import gptgenerator.uc.configure.IConfigurationController;
import gptgenerator.uc.configure.IConfigurationModel;
import gptgenerator.uc.filecompare.CurrentAndPrevious;
import gptgenerator.uc.mainview.FileStateController;
import gptgenerator.uc.mainview.FileStateModel;
import gptgenerator.uc.mainview.MainModel;
import gptgenerator.uc.mainview.TransformationStatus;
import gptgenerator.uc.processing.IStatusDecider;
import gptgenerator.uc.processing.ProcessManagerTestSetup;
import gptgenerator.uc.processing.StatusDeciderOnChange;

class MergeProcessorTest {
	private static final String inputBaseDir = "./src/test/resources/merge";

	@Test
	void testIncomplete() {
		final String testCaseDir = "incomplete"; 
		CacheCollection cacheCollection = prepareTest(testCaseDir);
		String projectRoot = inputBaseDir + File.separator + testCaseDir;
		IConfigurationModel configurationModel = ProcessManagerTestSetup.getConfigurationModel(projectRoot);
		MainModel mainModel = new MainModel(null);	
		IConfigurationController configurationController = new ConfigurationController(mainModel, configurationModel);
		
		Assertions.assertEquals(5, cacheCollection.getInputCur().getFilenameSet().size());
		FileStateModel fsModel = new FileStateModel(cacheCollection, configurationModel);

		FileStateController fsc = new FileStateController(configurationModel, fsModel);

		List<TransformationStatus> transformList = fsc.getMergePendingList();
		Assertions.assertEquals(5, transformList.size());

		for (int i = 0; i < 2; i++) {
			for (TransformationStatus cur : transformList) {
				MergeProcessor mp = new MergeProcessor(configurationController, fsc, cur.getBaseFilename());
				if (mp.readyToMerge()) {
					CacheEntry mergeResult = mp.merge();
					fsc.saveMergeFileCur(mergeResult);
				}
			}
		}
		Assertions.assertEquals(1, fsc.getInstallFiles().size());
	}
	
	@Test
	void testComplete() {
		final String testCaseDir = "complete"; 
		String projectRoot = inputBaseDir + File.separator + testCaseDir;
		MainModel mainModel = new MainModel(null);
		IConfigurationModel configurationModel = ProcessManagerTestSetup.getConfigurationModel(projectRoot);
		IConfigurationController configurationController = new ConfigurationController(mainModel, configurationModel);
		CacheCollection cacheCollection = prepareTest(testCaseDir);

		Assertions.assertEquals(6, cacheCollection.getInputCur().getFilenameSet().size());
		FileStateModel fsModel = new FileStateModel(cacheCollection, configurationModel);

		FileStateController fsc = new FileStateController(configurationModel, fsModel);

		List<TransformationStatus> transformList = fsc.getMergePendingList();
		Assertions.assertEquals(6, transformList.size());

		for (int i = 0; i < 2; i++) {
			for (TransformationStatus cur : transformList) {
				MergeProcessor mp = new MergeProcessor(configurationController, fsc, cur.getBaseFilename());
				if (mp.readyToMerge()) {
					CacheEntry mergeResult = mp.merge();
					fsc.saveMergeFileCur(mergeResult);
				}
			}
		}
		Assertions.assertEquals(2, fsc.getInstallFiles().size());
	}
	
	@Test
	void testSubdirPrompt() {
		final String testCaseDir = "subdirPrompt"; 
		
		CacheCollection cacheCollection = prepareTest(testCaseDir);
		String projectRoot = inputBaseDir + File.separator + testCaseDir;
		MainModel mainModel = new MainModel(null);
		IConfigurationModel configurationModel = ProcessManagerTestSetup.getConfigurationModel(projectRoot);
		IConfigurationController configurationController = new ConfigurationController(mainModel, configurationModel);

		Assertions.assertEquals(3, cacheCollection.getInputCur().getFilenameSet().size());
		FileStateModel fsModel = new FileStateModel(cacheCollection, configurationModel);

		FileStateController fsc = new FileStateController(configurationModel, fsModel);

		List<TransformationStatus> transformList = fsc.getMergePendingList();
		Assertions.assertEquals(3, transformList.size());

		for (int i = 0; i < 2; i++) {
			for (TransformationStatus cur : transformList) {
				MergeProcessor mp = new MergeProcessor(configurationController, fsc, cur.getBaseFilename());
				if (mp.readyToMerge()) {
					CacheEntry mergeResult = mp.merge();
					fsc.saveMergeFileCur(mergeResult);
				}
			}
		}
		IStatusDecider decider = new StatusDeciderOnChange();		
		Assertions.assertEquals(1, fsc.getPromptPendingList(decider).size());
	}

	@Test
	void testUnmerged() {
		final String testCaseDir = "unmerged"; 
		
		CacheCollection cacheCollection = prepareTest(testCaseDir);
		String projectRoot = inputBaseDir + File.separator + testCaseDir;
		MainModel mainModel = new MainModel(null);		
		IConfigurationModel configurationModel = ProcessManagerTestSetup.getConfigurationModel(projectRoot);
		IConfigurationController configurationController = new ConfigurationController(mainModel, configurationModel);

		Assertions.assertEquals(2, cacheCollection.getInputCur().getFilenameSet().size());
		FileStateModel fsModel = new FileStateModel(cacheCollection, configurationModel);

		FileStateController fsc = new FileStateController(configurationModel, fsModel);

		List<TransformationStatus> transformList = fsc.getMergePendingList();
		Assertions.assertEquals(2, transformList.size());

		for (int i = 0; i < 2; i++) {
			for (TransformationStatus cur : transformList) {
				MergeProcessor mp = new MergeProcessor(configurationController, fsc, cur.getBaseFilename());
				if (mp.readyToMerge()) {
					CacheEntry mergeResult = mp.merge();
					fsc.saveMergeFileCur(mergeResult);
				}
			}
		}
		Assertions.assertEquals(1, fsc.getInstallFiles().size());
	}
	
	private static CacheCollection prepareTest(String testCaseDir) {
		final String inputCurDirectory = inputCurDirectory(testCaseDir);
		final String mergeCurDirectory = mergeCurDirectory(testCaseDir);
		final String inputPrevDirectory = inputPrevDirectory(testCaseDir);
		final String mergePrevDirectory = mergePrevDirectory(testCaseDir);
		
		FileService.clearAndDeleteDirectory(inputPrevDirectory);
		FileService.clearAndDeleteDirectory(mergeCurDirectory);
		FileService.clearAndDeleteDirectory(mergePrevDirectory);

		CurrentAndPrevious input = new CurrentAndPrevious(inputCurDirectory, inputPrevDirectory);
		CurrentAndPrevious merge = new CurrentAndPrevious(mergeCurDirectory, mergePrevDirectory);
		CacheCollection cacheCollection = new CacheCollection();
		cacheCollection.updateInput(input);
		cacheCollection.updateMerge(merge);
		return cacheCollection;
	}

	private static String inputCurDirectory(String testCaseDir) {
		return inputBaseDir + File.separator + testCaseDir + File.separator + "inputCur";
	}
	
	private static String mergeCurDirectory(String testCaseDir) {
		return inputBaseDir + File.separator + testCaseDir + File.separator + "mergeCur";
	}
	
	
	private static String inputPrevDirectory(String testCaseDir) {
			return inputBaseDir + File.separator + testCaseDir + File.separator + "inputPrev";
}
	private static String mergePrevDirectory(String testCaseDir) {
		return inputBaseDir + File.separator + testCaseDir + File.separator + "mergePrev";
	}
	
}