package gptgenerator.uc.configure;

import com.fasterxml.jackson.annotation.JsonIgnore;

import gptgenerator.uc.configure.merge.ITemplateConfigModel;
import gptgenerator.uc.configure.sourcepartition.ISourcePartitionModel;
import gptgenerator.uc.configure.sourcepartition.SourcePartitioning;
import gptgenerator.uc.filecompare.CurrentAndPrevious;

/**
 * Application-wide configuration settings<br>
 * This needs to be split up:
 * <ul>
 * <li>Configuration for the application</li>
 * <li>The list of source partitions</li>
 * <li>Configuration for the chat / LLM</li>
 * </ul>
 */
public interface IConfigurationModel {

	void updateDirectoriesAndNotify();
	void markAsValid();
	@JsonIgnore
	boolean isValid();
	void setProjectRoot(String root);
	
	void setInputCurrentDir(String inputCurrentDir);	

	// Values for the LLM
	void setChatTemperature(String temperatureText);
	void setChatApiURL(String apiURL);
	void setChatApiToken(String apiToken);

	void setInputConfig(CurrentAndPrevious inputConfig);

	void setMergeConfig(CurrentAndPrevious mergeConfig);
	void setMakeChatApiCalls(boolean makeChatApiCalls);
	void setChatNumberOfThreads(int number);
	
	void setSourcePartitionAt (int index, ISourcePartitionModel model);
	void addSourcePartition (ISourcePartitionModel spJava);
	void removeSourcePartitionAt (int index);

	String getProjectRoot();
	String getInputCurDir();
	
	// Values for the interface to the LLM
	/**
	 * true: Requests are sent to the API
	 * @return See above
	 */
	boolean makeChatApiCalls();
	
	/**
	 * The system message for the given baseFilename.<br>
	 * ISourcePartitionModel will select the system message based on the baseFilename.<br>
	 * @param baseFilename The name of the prompt file
	 * @return See above
	 */
	String getSystemMessage(String baseFilename);
	
	/**
	 * Returns the TemplateConfig for the given relative source filename<br>
	 * @param sourceRelFilename : The relative filename of the source file
	 * @return See above
	 */
	ITemplateConfigModel getTemplateConfig(String sourceRelFilename);
	/**
	 * @return
	 */
	String getChatTemperatureString();
	/**
	 * Returns the chat temperature as a Double for a given baseFilename
	 * @param baseFilename
	 * @return See above
	 */
	Double getChatTemperature(String baseFilename);
	/**
	 * @return
	 */
	String getChatApiURL();
	/**
	 * @return
	 */
	String getChatApiToken();
	/**
	 * @return The number of threads to use when sendings requests to the LLM API
	 */
	Integer getChatNumberOfThreads();

	ISourcePartitionModel getSourcePartition(int index);	
	SourcePartitioning getSourcePartitions();
	
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
