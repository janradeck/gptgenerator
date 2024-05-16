package gptgenerator.uc.configure;

import java.io.File;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import gptgenerator.uc.configure.gpt.GPTConfig;
import gptgenerator.uc.configure.sourcepartition.SourcePartition;
import gptgenerator.uc.processing.o1merge.TemplateConfig;

/**
 * This test serves to generate the current version of the configuration file
 */
class ConfigurationModelTest {
	private static final String savePath = "./src/test/resources/config.yaml";
	@Test
	void test() {
		ConfigurationRepository cr = new ConfigurationRepository(savePath);
		File target = new File(savePath);
		target.delete();
		Assertions.assertFalse(target.exists());
		ConfigurationModel cfm = new ConfigurationModel();
		cfm.setProd(false);
		SourcePartition spJava = new SourcePartition();
		spJava.setSourceDirRel("java");
		spJava.setDestDirAbs("D:\\project\\src_gen");
		TemplateConfig tcJava =  new TemplateConfig();
		tcJava.setMarkerStart("#(");
		tcJava.setMarkerEnd(")#");
		spJava.setTemplateConfig(tcJava);
		
		GPTConfig gptJava = new GPTConfig();
		gptJava.setSystemMessage("You are a Java developer. Reply only with code. Do not include example usage or explanations.");
		gptJava.setIndividualTemperature(false);
		gptJava.setTemperature(1.0);
		spJava.setGptConfig(gptJava);
		cfm.addPartition(spJava);

		SourcePartition spJs = new SourcePartition();
		spJs.setSourceDirRel("js");		
		spJs.setDestDirAbs("D:\\develop\\robotconfig-client\\src\\app\\generated");

		TemplateConfig tcJs =  new TemplateConfig();
		tcJs.setMarkerStart("#(");
		tcJs.setMarkerEnd(")#");
		spJs.setTemplateConfig(tcJs);
		
		GPTConfig gptJs = new GPTConfig();
		gptJs.setSystemMessage("You are a Typescript developer. Reply only with code. Do not include example usage or explanations.");
		gptJs.setIndividualTemperature(false);
		gptJs.setTemperature(1.0);
		spJava.setGptConfig(gptJs);
		
		cfm.addPartition(spJs);
		
		cfm.setInputCurrentDir("C:\\Users\\Jan\\MPSProjects\\WebApplication\\solutions\\Playground\\source_gen\\Playground\\Playground\\src_gen");
		cfm.setTemperature(1.2);
		cfm.setNumberOfThreads(50);
		cfm.setProjectRoot("d:\\develop\\appgenerator-init");
		cr.storeConfiguration(cfm);
		Assertions.assertTrue(target.exists());
		Assertions.assertTrue(target.isFile());
	}

}
