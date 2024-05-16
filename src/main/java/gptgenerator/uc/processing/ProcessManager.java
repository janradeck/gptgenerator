package gptgenerator.uc.processing;

import java.util.List;

import gptgenerator.processingresult.IResultController;
import gptgenerator.services.FileService;
import gptgenerator.services.PrettyPrintService;
import gptgenerator.uc.configure.IConfigurationController;
import gptgenerator.uc.mainview.IFileStateController;
import gptgenerator.uc.mainview.SelectedFiles;
import gptgenerator.uc.processing.o1merge.MergeManager;
import gptgenerator.uc.processing.o2prompt.PromptManager;
import gptgenerator.uc.processing.o3install.FileInstaller;

/**
 * Verwaltet den Ablauf der Verarbeitung
 */
public class ProcessManager {
	
	private ProcessManager() {
		
	}
	
	public static void processAll(IConfigurationController configurationController, IResultController mergeResultController, IResultController promptResultController, IFileStateController fileStateController) {
		cleanWorkingFiles(configurationController, fileStateController);
		MergeManager mergeMan = new MergeManager(configurationController, fileStateController, mergeResultController);

		IStatusDecider decider = new StatusDeciderAlways();		
		PromptManager promptMan = new PromptManager(fileStateController, promptResultController, configurationController, decider);

		do {
			mergeMan.process();
			promptMan.process();
			
		} while (mergeMan.filesReady());

		PrettyPrintService.prettyPrintMergeCur(configurationController);		
		
		List<String> installFiles = fileStateController.getInstallFiles();
		
		FileInstaller.installFiles(fileStateController, configurationController, installFiles);
	}

	public static void processSelected(IConfigurationController configurationController, IResultController mergeResultController, IResultController promptResultController, IFileStateController fileStateController, SelectedFiles selectedFiles) {
		cleanWorkingFiles(configurationController, fileStateController);

		MergeManager mergeMan = new MergeManager(configurationController, fileStateController, mergeResultController);

		IStatusDecider decider = new StatusDeciderSelectedFiles(selectedFiles);
		PromptManager promptMan = new PromptManager(fileStateController, promptResultController, configurationController, decider);

		do {
			mergeMan.process();
			promptMan.process();
			
		} while (mergeMan.filesReady());

		PrettyPrintService.prettyPrintMergeCur(configurationController);		
		
		List<String> installFiles = fileStateController.getInstallFiles();
		
		FileInstaller.installFiles(fileStateController, configurationController, installFiles);		
	}

	public static void cleanWorkingFiles(IConfigurationController configurationController, IFileStateController fileStateController) {
		List<String> cleanFiles = fileStateController.getCleanFiles();
		for (String curFilename : cleanFiles) {
			FileService.remove(curFilename);
		}
	}

}
