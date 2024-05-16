package gptgenerator.app;

import gptgenerator.processingresult.IResultController;
import gptgenerator.processingresult.ResultController;
import gptgenerator.processingresult.ResultModel;
import gptgenerator.services.CacheCollection;
import gptgenerator.uc.configure.ConfigurationController;
import gptgenerator.uc.configure.IConfigurationModel;
import gptgenerator.uc.mainview.FileStateController;
import gptgenerator.uc.mainview.FileStateModel;
import gptgenerator.uc.mainview.IMainModel;
import gptgenerator.uc.mainview.MainModel;
import gptgenerator.uc.mainview.MainView;
import gptgenerator.uc.processing.ProcessManager;

import java.nio.file.Path;

import javax.swing.*;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import joptsimple.OptionSpec;

/**
 * Parameters:
 * <ul>
 * <li>--nogui: Run without GUI</li>
 * <li>--config: Path to the configuration file</li>
 * </ul>
 * 
 * The configuration file is called "config.yaml" and expected to be in YAML format.
 */
public class AppMain {
	private static final String NOGUI_OPT = "nogui";
	private static final String CONFIGFILE_OPT = "config";
	private static final String CONFIG_FILENAME = "config.yaml";
	
	private static final String DEFAULT_CONFIG_PATH = Path.of(".", CONFIG_FILENAME).toString();
	
	public static void main(String[] args) {

		OptionParser parser = new OptionParser();
		parser.accepts(NOGUI_OPT);
		OptionSpec<String> configFileOpt = parser.accepts( CONFIGFILE_OPT ).withRequiredArg().ofType( String.class ).defaultsTo(DEFAULT_CONFIG_PATH);
		OptionSet options = parser.parse(args);

		String configPath = configFileOpt.value(options);
		IMainModel mainModel = new MainModel(configPath);

		IConfigurationModel configuration = mainModel.loadConfigurationModel();
		CacheCollection cacheCollection = new CacheCollection();
		FileStateModel fileState = new FileStateModel(cacheCollection, configuration);
		ConfigurationController configurationController = new ConfigurationController(mainModel, configuration);
		
		IResultController mergeResultController = new ResultController(new ResultModel());
		IResultController promptResultController = new ResultController(new ResultModel());
		
		FileStateController fileStateController = new FileStateController(configuration, fileState);
		
		if (options.has(NOGUI_OPT)) {
			// Run without GUI
			ResultModel result = new ResultModel();
			ProcessManager.processAll(configurationController, mergeResultController, promptResultController, fileStateController); 
			System.out.println(result.toString());
		} else {
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					new MainView(fileStateController, mergeResultController, promptResultController, configurationController);
				}
			});
		}
	}
}
