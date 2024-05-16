package gptgenerator.services;

import java.io.File;

import gptgenerator.uc.configure.IConfigurationController;
import gptgenerator.uc.configure.sourcepartition.IPrettyPrintSettings;
import gptgenerator.uc.configure.sourcepartition.ISourcePartitionModel;

public class PrettyPrintService {

	public static void prettyPrint(String directory) {
		String command = String.format("npx prettier %s -w", directory);
		// System.err.println("PrettyPrintService.prettyPrint " + command);
		ExecutionService.executeCommandline(command);
	}
	
	public static void prettyPrintInput(IConfigurationController configurationController) {
		for (ISourcePartitionModel curPartition: configurationController.getSourcePartitioning().getPartitions()) {
			IPrettyPrintSettings curSettings = curPartition.getPrettyPrintSettings();
			if (curSettings.isPrettyPrintInput()) {
				String srcDir = configurationController.getInputCurDir() + File.separator + curPartition.getSourceDirRel();
				PrettyPrintService.prettyPrint(srcDir);
			}
		}
	}

	public static void prettyPrintMergeCur(IConfigurationController configurationController) {
		for (ISourcePartitionModel curPartition: configurationController.getSourcePartitioning().getPartitions()) {
			IPrettyPrintSettings curSettings = curPartition.getPrettyPrintSettings();
			if (curSettings.isPrettyPrintInput()) {
				String srcDir = configurationController.getMergeCurDir() + File.separator + curPartition.getSourceDirRel();
				PrettyPrintService.prettyPrint(srcDir);
			}
		}
	}
	
}
