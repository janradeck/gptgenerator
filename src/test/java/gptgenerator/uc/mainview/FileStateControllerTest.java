package gptgenerator.uc.mainview;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import gptgenerator.services.CacheCollection;
import gptgenerator.uc.configure.ConfigurationModel;
import gptgenerator.uc.configure.IConfigurationModel;
import gptgenerator.uc.configure.gpt.GPTConfig;
import gptgenerator.uc.configure.sourcepartition.PrettyPrintSettings;
import gptgenerator.uc.configure.sourcepartition.SourcePartition;
import gptgenerator.uc.filecompare.CurrentAndPrevious;
import gptgenerator.uc.processing.o1merge.TemplateConfig;

class FileStateControllerTest {

	@Test
	void testGetCleanFiles() {
		String testBaseDirectory = "./src/test/resources/filestatecontroller/cleanfiles";
		String inputCurDirectory = testBaseDirectory + File.separator + "inputCur";
		String inputPrevDirectory = testBaseDirectory + File.separator + "inputPrev";
		String mergeCurDirectory = testBaseDirectory + File.separator + "mergeCur";
		String mergePrevDirectory = testBaseDirectory + File.separator + "mergePrev";

		String installDirectory = testBaseDirectory + File.separator + "install";

		CurrentAndPrevious input = new CurrentAndPrevious(inputCurDirectory, inputPrevDirectory);
		CurrentAndPrevious merge = new CurrentAndPrevious(mergeCurDirectory, mergePrevDirectory);
		CacheCollection cacheCollection = new CacheCollection();
		cacheCollection.updateInput(input);
		cacheCollection.updateMerge(merge);
		
		IConfigurationModel configurationModel = new ConfigurationModel();
		configurationModel.setProd(false);
		configurationModel.setInputConfig(input);
		configurationModel.setMergeConfig(merge);
		GPTConfig gConfig = new GPTConfig();
		IFileStateModel filestateModel = new FileStateModel(cacheCollection, configurationModel);
		TemplateConfig tConfig = new TemplateConfig();
		PrettyPrintSettings ppSettings = new PrettyPrintSettings();
		SourcePartition javaPart = new SourcePartition("java", installDirectory, ppSettings, gConfig, tConfig);
		configurationModel.addPartition(javaPart);
		
		IFileStateController filestateController = new FileStateController(configurationModel, filestateModel);
		
		String promptFile = "Generated.java.prompt";
		String replyFile = "Generated.java";
		String templateFile = "SingleClass.java";
		String fragmentFile = "SingleClass_01.fragment";
		
		Set<String> expected = new HashSet<>();
		
		String javaDir = File.separator + "java" + File.separator;
		
		expected.add(inputCurDirectory + javaDir + promptFile);
		expected.add(inputPrevDirectory + javaDir + promptFile);
		expected.add(mergeCurDirectory + javaDir + promptFile);
		expected.add(mergePrevDirectory + javaDir + promptFile);

		expected.add(mergeCurDirectory + javaDir + replyFile);
		expected.add(mergePrevDirectory + javaDir + replyFile);
		expected.add(installDirectory + File.separator + replyFile);

		expected.add(inputCurDirectory + javaDir + fragmentFile);
		expected.add(inputPrevDirectory + javaDir + fragmentFile);
		expected.add(mergeCurDirectory + javaDir + fragmentFile);
		expected.add(mergePrevDirectory + javaDir + fragmentFile);
		
		expected.add(inputCurDirectory + javaDir + templateFile);
		expected.add(inputPrevDirectory + javaDir + templateFile);
		expected.add(mergeCurDirectory + javaDir + templateFile);
		expected.add(mergePrevDirectory + javaDir + templateFile);
		
		List<String> cleanFiles = filestateController.getCleanFiles();

		for (String cur: cleanFiles) {
			if (! expected.contains(cur)) {
				System.err.println("Not found " + cur);
			}
		}

		Assertions.assertEquals(expected.size(), cleanFiles.size());
	}

}
