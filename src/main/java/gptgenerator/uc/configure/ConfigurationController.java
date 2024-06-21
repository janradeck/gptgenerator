package gptgenerator.uc.configure;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

import gptgenerator.uc.configure.merge.ITemplateConfigModel;
import gptgenerator.uc.configure.sourcepartition.ISourcePartitionModel;
import gptgenerator.uc.configure.sourcepartition.SourcePartition;
import gptgenerator.uc.configure.sourcepartition.SourcePartitionView;
import gptgenerator.uc.configure.sourcepartition.SourcePartitioning;
import gptgenerator.uc.mainview.IMainModel;
import gptgenerator.uc.processing.o2prompt.ChatClientFactory;
import gptgenerator.uc.processing.o2prompt.IChatClient;

/**
 * 
 */
public class ConfigurationController implements IConfigurationController {
	private List<IConfigurationView> views = new ArrayList<>();
    
	private IMainModel mainModel;
    private IConfigurationModel configurationModel;
    
    /**
     * @param mainModel
     * @param configurationModel
     */
    public ConfigurationController(IMainModel mainModel, IConfigurationModel configurationModel) {
		this.mainModel = mainModel;
    	this.configurationModel = configurationModel;  	
    	configurationModel.setController(this);
    }

	@Override
	public void setProjectRoot(String root) {
		configurationModel.setProjectRoot(root);
	}

	@Override
	public void setInputCurrentDir(String dir) {
		configurationModel.setInputCurrentDir(dir);		
	}
	
	@Override
	public void setChatTemperature(String temperatureText) {
		configurationModel.setChatTemperature(temperatureText);
	}

	@Override
	public void setChatNumberOfThreads(int number) {
		configurationModel.setChatNumberOfThreads(number);
	}

	@Override
	public void setChatApiURL(String chatApiURL) {
		configurationModel.setChatApiURL(chatApiURL);
	}

	@Override
	public void notifySetChatApiURL(String chatApiURL) {
		for (IConfigurationView view: views) {
			view.setChatApiURL(chatApiURL);
		}
	}
	
	@Override
	public void setChatApiKey(String chatApiToken) {
		configurationModel.setChatApiToken(chatApiToken);
	}
	

	@Override
	public void notifySetChatApiToken(String chatApiToken) {
		for (IConfigurationView view : views) {
			view.setChatApiKey(chatApiToken);
		}
	}
		
	@Override
	public void setPartitionAt(int index, ISourcePartitionModel model) {
		configurationModel.setSourcePartitionAt(index, model);
	}

	@Override
	public void addPartition(ISourcePartitionModel model) {
		configurationModel.addSourcePartition(model);
	}

	@Override
	public void removePartition(int index) {
		configurationModel.removeSourcePartitionAt(index);
	}

	@Override
	public Double getChatTemperature(String baseFilename) {
		return configurationModel.getChatTemperature(baseFilename);
	}


	@Override
	public IChatClient getChatClient(String chatApiUrl, String chatApiKey, double temperature) {
		return ChatClientFactory.getChatClient(this, chatApiUrl, chatApiKey, temperature);
	}
	
	@Override
	public boolean makeApiCalls() {
		return configurationModel.makeChatApiCalls();
	}

	
	@Override
	public void notifySetChatTemperature(String temperatureText) {
		for (IConfigurationView view: views) {
			view.setChatTemperature(temperatureText);
		}
	}

	@Override
	public void notifySetPartitionAt(int index, ISourcePartitionModel model) {
		for (IConfigurationView view: views) {
			view.setInstallAt(index, model);
		}
	}

	@Override
	public void notifyRemovePartition(int index) {
		for (IConfigurationView view: views) {
			view.removeInstallAt(index);
		}
	}

	@Override
	public void notifySetChatNumberOfThreads(String numberOfThreads) {
		for (IConfigurationView view: views) {
			view.setChatNumberOfThreads(numberOfThreads);
		}
	}

	@Override
	public void notifySetSourcePartitions(SourcePartitioning installs) {
		notifyInstallsClear();
		for (ISourcePartitionModel part: installs.getPartitions()) {
			notifyAddPartition(part);
		}
	}

	@Override
	public void setMakeApiCalls(boolean isProd) {
		configurationModel.setMakeChatApiCalls(isProd);
	}

	@Override
	public void notifySetMakeApiCalls(boolean isProd) {
		for (IConfigurationView view: views) {
			view.setMakeApiCalls(isProd);
		}
	}

	
	
	@Override
	public Integer getChatNumberOfThreads() {
		return configurationModel.getChatNumberOfThreads();
	}
    
	@Override
    public void notifySetProjectRoot(String newProjectRoot) {
    	for (IConfigurationView view: views) {
    		view.setProjectRoot(newProjectRoot);
    	}
    }

	@Override
    public void notifySetInputCurrentDir(String newDir) {
    	for (IConfigurationView view: views) {
    		view.setInputCurrentDir(newDir);
    	}
    }

    public void notifyInstallsClear() {
    	for (IConfigurationView view: views) {
    		view.clearInstalls();
    	}
    }

    public void notifySetInstalls(List<SourcePartition> list) {
    	for (IConfigurationView view: views) {
    		view.setInstall(list);
    	}        
    }

	public void notifyRemoveInstallAt(int deleteIndex) {
    	for (IConfigurationView view: views) {
    		view.removeInstallAt(deleteIndex);
    	}        
	}
    
	public void editConfiguration() {
		new ConfigurationView(this);
	}


	@Override
	public void notifyAddPartition(ISourcePartitionModel partition) {
    	for (IConfigurationView view: views) {
    		view.addInstall(partition);
    	}        
	}

	@Override
	public SourcePartitioning getSourcePartitioning() {
		return configurationModel.getSourcePartitions();
	}
	
	@Override
	public String getSystemMessage(String baseFilename) {
		return configurationModel.getSystemMessage(baseFilename);
	}

	public void save() {
		mainModel.saveConfigurationModel(configurationModel);
	}

	public void editTargetSubDir(JFrame parentView, ConfigurationView configurationView, int index) {
		ISourcePartitionModel modelCopy = new SourcePartition(configurationModel.getSourcePartition(index));
		SourcePartitionView editInstallationView = new SourcePartitionView(parentView, this, modelCopy,  index);
    	editInstallationView.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent windowEvent) {
            }
        });    	
        editInstallationView.setVisible(true);		
	}

	public void editNewTargetSubDir(JFrame parentView, ConfigurationView configurationView) {
		SourcePartition model = new SourcePartition();
		
    	SourcePartitionView editInstallationView = new SourcePartitionView(parentView, this, model, -1);
    	editInstallationView.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent windowEvent) {
            }
        });    	
        editInstallationView.setVisible(true);
		
	}

	@Override
	public void addView(IConfigurationView view) {
		views.add(view);		
	}

	@Override
	public void removeView(IConfigurationView view) {
		views.remove(view);		
	}

	@Override
	public void requestViewUpdate() {
		notifySetProjectRoot(configurationModel.getProjectRoot());
		notifySetInputCurrentDir(configurationModel.getInputCurDir());
		notifySetChatTemperature(configurationModel.getChatTemperatureString());
		notifySetChatNumberOfThreads(configurationModel.getChatNumberOfThreads().toString());
		notifySetMakeApiCalls(configurationModel.makeChatApiCalls());
		
		notifyInstallsClear();
		for (ISourcePartitionModel part: configurationModel.getSourcePartitions().getPartitions()) {
			notifyAddPartition(part);
		}
	}

	@Override
	public ITemplateConfigModel getTemplateConfig(String cur) {
		return configurationModel.getTemplateConfig(cur);
	}

	@Override
	public String getReplyCacheDir() {
		return configurationModel.getReplyCacheDir();
	}

	@Override
	public String getInputCurDir() {
		return configurationModel.getInputCurDir();
	}

	@Override
	public String getMergeCurDir() {
		return configurationModel.getMergeCurDir();
	}

	@Override
	public String getChatApiURL() {
		return configurationModel.getChatApiURL();
	}

	@Override
	public String getChatApiToken() {
		return configurationModel.getChatApiToken();
	}



}