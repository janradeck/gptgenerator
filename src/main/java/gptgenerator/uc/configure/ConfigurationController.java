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

public class ConfigurationController implements IConfigurationController {
	private List<IConfigurationView> views = new ArrayList<>();
    
	private IMainModel mainModel;
    private IConfigurationModel configurationModel;
    
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
	public void setTemperature(String temperatureText) {
		configurationModel.setTemperature(temperatureText);
	}

	@Override
	public void setNumberOfThreads(int number) {
		configurationModel.setNumberOfThreads(number);
	}
	
	@Override
	public void setPartitionAt(int index, ISourcePartitionModel model) {
		configurationModel.setInstallAt(index, model);
	}

	@Override
	public void addPartition(ISourcePartitionModel model) {
		configurationModel.addPartition(model);
	}

	@Override
	public void removePartition(int index) {
		configurationModel.removeInstall(index);
	}

	@Override
	public Double getTemperature(String baseFilename) {
		return configurationModel.getTemperature(baseFilename);
	}


	@Override
	public IChatClient getChatClient(double temperature) {
		return ChatClientFactory.getChatClient(this, temperature);
	}
	
	@Override
	public boolean isProd() {
		return configurationModel.isProd();
	}

	
	@Override
	public void notifySetTemperature(String temperatureText) {
		for (IConfigurationView view: views) {
			view.setTemperature(temperatureText);
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
	public void notifySetNumberOfThreads(String numberOfThreads) {
		for (IConfigurationView view: views) {
			view.setNumberOfThreads(numberOfThreads);
		}
	}

	@Override
	public void notifySetInstalls(SourcePartitioning installs) {
		notifyInstallsClear();
		for (ISourcePartitionModel part: installs.getPartitions()) {
			notifyAddPartition(part);
		}
	}

	@Override
	public void setProd(boolean isProd) {
		configurationModel.setProd(isProd);
	}

	@Override
	public void notifySetIsProd(boolean isProd) {
		for (IConfigurationView view: views) {
			view.setIsProd(isProd);
		}
	}

	
	@Override
	public Integer getNumberOfThreads() {
		return configurationModel.getNumberOfThreads();
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
		return configurationModel.getInstalls();
	}
	
	@Override
	public String getSystemMessage(String baseFilename) {
		return configurationModel.getSystemMessage(baseFilename);
	}

	public void save() {
		mainModel.saveConfigurationModel(configurationModel);
	}

	public void editTargetSubDir(JFrame parentView, ConfigurationView configurationView, int index) {
		ISourcePartitionModel modelCopy = new SourcePartition(configurationModel.getInstall(index));
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
		notifySetTemperature(configurationModel.getTemperatureString());
		notifySetNumberOfThreads(configurationModel.getNumberOfThreads().toString());
		notifySetIsProd(configurationModel.isProd());
		
		notifyInstallsClear();
		for (ISourcePartitionModel part: configurationModel.getInstalls().getPartitions()) {
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


}