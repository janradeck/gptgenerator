package gptgenerator.uc.configure;

import java.util.List;

import gptgenerator.uc.configure.sourcepartition.ISourcePartitionModel;
import gptgenerator.uc.configure.sourcepartition.SourcePartition;

public interface IConfigurationView {

	void setProjectRoot(String newProjectRoot);
	void setInputCurrentDir(String newDir);
	void setIsProd(boolean isProd);

	void clearInstalls();

	void addInstall(ISourcePartitionModel partition);

	void setInstallAt(int index, ISourcePartitionModel model);

	void removeInstallAt(int deleteIndex);

	void setInstall(List<SourcePartition> list);

	void setTemperature(String temperatureText);
	void setNumberOfThreads(String numberOfTreads);
}
