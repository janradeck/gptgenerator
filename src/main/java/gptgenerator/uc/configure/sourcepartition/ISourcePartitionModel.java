package gptgenerator.uc.configure.sourcepartition;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import gptgenerator.uc.configure.gpt.GPTConfig;
import gptgenerator.uc.configure.gpt.IGPTConfigModel;
import gptgenerator.uc.configure.merge.ITemplateConfigModel;
import gptgenerator.uc.processing.o1merge.TemplateConfig;

@JsonDeserialize(as = SourcePartition.class)
public interface ISourcePartitionModel {
	
	void setSourceDirRel(String source);
	void setDestDirAbs(String dest);
	
	String getSourceDirRel();
	String getDestDirAbs();
	
	IPrettyPrintSettings getPrettyPrintSettings();
	void setGptConfig(GPTConfig gptConfig);
	void setTemplateConfig(TemplateConfig templateConfig);
	ITemplateConfigModel getTemplateConfig();

	String mapToDestination(String sourceRelFilename);
	boolean isHomeOf(String sourceRelFilename);
	
	void setController(ISourcePartitionController controller);
	void clearController();
	void setPrettyPrintSettings(IPrettyPrintSettings ppSettings);
	IGPTConfigModel getGptConfig();	
}
