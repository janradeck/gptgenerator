package gptgenerator.uc.configure;

import com.fasterxml.jackson.annotation.JsonIgnore;

import gptgenerator.uc.configure.merge.ITemplateConfigModel;
import gptgenerator.uc.configure.sourcepartition.ISourcePartitionModel;
import gptgenerator.uc.configure.sourcepartition.SourcePartitioning;
import gptgenerator.uc.filecompare.CurrentAndPrevious;

public interface IConfigurationModel {

	void updateDirectoriesAndNotify();
	void markAsValid();
	@JsonIgnore
	boolean isValid();
	void setProjectRoot(String root);
	void setInputCurrentDir(String dir);
	
	void setTemperature(String temperatureText);
	void setInputConfig(CurrentAndPrevious inputConfig);

	void setMergeConfig(CurrentAndPrevious mergeConfig);
	void setProd(boolean isProd);
	void setNumberOfThreads(int number);
	
	void setInstallAt (int index, ISourcePartitionModel model);
	void addPartition (ISourcePartitionModel spJava);
	void removeInstall (int index);

	String getProjectRoot();
	String getInputCurDir();
	
	boolean isProd();
	String getSystemMessage(String baseFilename);
	ITemplateConfigModel getTemplateConfig(String cur);
	String getTemperatureString();
	Double getTemperature(String baseFilename);
	Integer getNumberOfThreads();

	ISourcePartitionModel getInstall(int index);	
	SourcePartitioning getInstalls();
	
	String getReplyCacheDir();

	CurrentAndPrevious getInputConfig();
	CurrentAndPrevious getMergeConfig();
	
	String mapToDestination(String cur);
	String getInstallDir(String filename);
	String getInputPrevDir();
	String getMergeCurDir();
	String getMergePrevDir();

	void setController(IConfigurationController controller);
	void clearController();
}
