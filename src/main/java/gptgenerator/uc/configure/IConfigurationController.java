package gptgenerator.uc.configure;

import gptgenerator.uc.configure.merge.ITemplateConfigModel;
import gptgenerator.uc.configure.sourcepartition.ISourcePartitionModel;
import gptgenerator.uc.configure.sourcepartition.SourcePartitioning;
import gptgenerator.uc.processing.o2prompt.IChatClient;

/**
 * The controller for the configuration model. 
 */
public interface IConfigurationController {
	/**
	 * Enable the calls to the chat API, if set to true
	 * @param makeApiCalls
	 */
	void setMakeApiCalls(boolean makeApiCalls);
	
	void setProjectRoot(String projectRoot);
	void setInputCurrentDir(String inputCurrentDir);
	void setChatNumberOfThreads(int numberOfThreads);
	void setChatTemperature(String temperatureText);
	void setChatApiURL(String chatApiURL);
	void setChatApiKey(String chatApiToken);
	/**
	 * Set a new chat model
	 * @param newChatModel
	 */
	void notifySetChatModel(String newChatModel);
	
	void setPartitionAt (int partitionIndex, ISourcePartitionModel sourcePartition);
	void addPartition (ISourcePartitionModel sourcePartition);
	void removePartition (int partitionIndex);

	Integer getChatNumberOfThreads();
	String getChatApiURL();
	String getChatApiToken();
	Double getChatTemperature(String temperatureString);
	String getChatModel();
	boolean makeApiCalls();
	
	SourcePartitioning getSourcePartitioning();

	String getSystemMessage(String baseFilename);
	IChatClient getChatClient(String chatApiUrl, String chatApiKey, String chatModel, double chatTemperature);
	
	ITemplateConfigModel getTemplateConfig(String cur);
	String getReplyCacheDir();
	String getInputCurDir();	
	String getMergeCurDir();
	
	void notifySetProjectRoot(String projectRoot);
	void notifySetInputCurrentDir(String inputCurrentDir);
	void notifySetChatNumberOfThreads(String numberOfThreads);
	void notifySetChatTemperature(String temperatureText);
	void notifySetMakeApiCalls(boolean makeApiCalls);

	void notifySetChatApiURL(String chatApiURL);
	void notifySetChatApiToken(String chatApiToken);
	
	/**
	 * @param partitionIndex
	 * @param sourcePartitionModel
	 */
	void notifySetPartitionAt (int partitionIndex, ISourcePartitionModel sourcePartitionModel);
	/**
	 * @param sourcePartition
	 */
	void notifySetSourcePartitions(SourcePartitioning sourcePartition);
	/**
	 * Add a new SourcePartitionModel
	 * @param sourcePartition
	 */
	void notifyAddPartition(ISourcePartitionModel sourcePartition);
	/**
	 * Notify the listeners that the partition at the given index has been removed
	 * @param partitionIndex
	 */
	void notifyRemovePartition (int partitionIndex);
	
	/**
	 * Add a view to the list of views that will be updated when the model changes
	 * @param configurationView
	 */
	void addView(IConfigurationView configurationView);
	
	/**
	 * Remove a view from the list of views that will be updated when the model changes
	 * @param configurationView
	 */
	void removeView(IConfigurationView configurationView);
	/**
	 * Requests that all views update their display
	 */
	void requestViewUpdate();



}
