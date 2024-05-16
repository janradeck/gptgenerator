package gptgenerator.uc.mainview;

import gptgenerator.uc.configure.IConfigurationModel;

public interface IMainModel {

    IConfigurationModel loadConfigurationModel();

    void saveConfigurationModel(IConfigurationModel model);

}