package gptgenerator.uc.configure;

import java.util.List;

import gptgenerator.uc.configure.sourcepartition.ISourcePartitionModel;
import gptgenerator.uc.configure.sourcepartition.SourcePartition;

/**
 * A view for the configuration model
 * @see SourcePartition
 * @see ISourcePartitionModel
 * @see IConfigurationController
 */
public interface IConfigurationView {

	void setProjectRoot(String newProjectRoot);
	
	void setInputCurrentDir(String newDir);
	
	void setMakeApiCalls(boolean makeApiCalls);

	void clearInstalls();

	void addInstall(ISourcePartitionModel partition);

	void setInstallAt(int index, ISourcePartitionModel model);

	void removeInstallAt(int deleteIndex);

	void setInstall(List<SourcePartition> list);

	void setChatTemperature(String temperatureText);
	
	void setChatNumberOfThreads(String numberOfTreads);
	
	void setChatApiURL(String chatApiURL);
	
	void setChatApiKey(String chatApiKey);
}
