package gptgenerator.uc.configure;

import gptgenerator.uc.configure.merge.ITemplateConfigModel;
import gptgenerator.uc.configure.sourcepartition.ISourcePartitionModel;
import gptgenerator.uc.configure.sourcepartition.SourcePartitioning;
import gptgenerator.uc.processing.o2prompt.IChatClient;

public interface IConfigurationController {
	
	void setProd(boolean isProd);
	void setProjectRoot(String root);
	void setInputCurrentDir(String dir);
	void setNumberOfThreads(int number);
	void setTemperature(String temperatureText);
	
	void setPartitionAt (int index, ISourcePartitionModel sourcePartition);
	void addPartition (ISourcePartitionModel sourcePartition);
	void removePartition (int index);

	Integer getNumberOfThreads();
	Double getTemperature(String string);
	SourcePartitioning getSourcePartitioning();
	boolean isProd();

	String getSystemMessage(String baseFilename);
	IChatClient getChatClient(double temperature);
	ITemplateConfigModel getTemplateConfig(String cur);
	String getReplyCacheDir();
	String getInputCurDir();	
	String getMergeCurDir();
	
	void notifySetProjectRoot(String root);
	void notifySetInputCurrentDir(String dir);
	void notifySetNumberOfThreads(String numberOfThreads);
	void notifySetTemperature(String temperatureText);
	void notifySetIsProd(boolean isProd);
	
	void notifySetPartitionAt (int index, ISourcePartitionModel installCfg);
	void notifySetInstalls(SourcePartitioning installs);
	void notifyAddPartition(ISourcePartitionModel installCfg);
	void notifyRemovePartition (int index);
	
	void addView(IConfigurationView view);
	void removeView(IConfigurationView view);
	void requestViewUpdate();
}
