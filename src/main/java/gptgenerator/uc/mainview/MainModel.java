package gptgenerator.uc.mainview;

import gptgenerator.uc.configure.IConfigurationModel;
import gptgenerator.uc.configure.ConfigurationRepository;

/**
 * Responsible for loading and saving the configuration model.
 * @see ConfigurationRepository
 * @see IConfigurationModel
 */
public class MainModel implements IMainModel {
    private ConfigurationRepository confRepo = null;

    public MainModel(String configurationFilename) {
        this.confRepo = new ConfigurationRepository(configurationFilename);
    }

    @Override
    public IConfigurationModel loadConfigurationModel() {
        return confRepo.loadConfiguration();
    }

    @Override
    public void saveConfigurationModel(IConfigurationModel model) {
        confRepo.storeConfiguration(model);
    }


}
