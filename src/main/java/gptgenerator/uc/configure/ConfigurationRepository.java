package gptgenerator.uc.configure;

import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

interface IConfigurationRepository {
    IConfigurationModel loadConfiguration();
    void storeConfiguration(IConfigurationModel config);
}

/**
 * A repository class for the ConfigurationModel instance
 * @see ConfigurationModel
 */
public class ConfigurationRepository implements IConfigurationRepository{
    private String configPath;

    public ConfigurationRepository(String configPath) {
        this.configPath = configPath; 
    }
    @Override
    public IConfigurationModel loadConfiguration() {
		ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
		File configFile = new File(configPath);
		if (configFile.exists()) {
			try {
				IConfigurationModel result = mapper.readValue(configFile, ConfigurationModel.class);
				result.updateDirectoriesAndNotify();
				result.markAsValid();
				return result;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return new ConfigurationModel();
    }

    @Override
    public void storeConfiguration(IConfigurationModel config) {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
		mapper.enable(SerializationFeature.INDENT_OUTPUT);
		try {
			mapper.writeValue(new File(configPath), config);
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    
}
