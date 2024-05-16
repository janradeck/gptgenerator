package gptgenerator.uc.processing.o3install;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.nio.file.Files;

import gptgenerator.services.FileService;
import gptgenerator.uc.configure.IConfigurationController;
import gptgenerator.uc.configure.sourcepartition.SourcePartition;
import gptgenerator.uc.mainview.IFileStateController;

public class FileInstaller {

	public static void install(String baseDir, List<SourcePartition> mapping) {
		for (SourcePartition cur: mapping) {
		String src = cur.getSourceDirRel();
			File srcDir = java.nio.file.Paths.get(baseDir, src).toFile();
			try {
				copyDirectory(srcDir, new File(cur.getDestDirAbs()));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void copyDirectory(File source, File destination) throws IOException {
        if (!source.exists()) {
            throw new IllegalArgumentException("Source directory does not exist: " + source);
        }

        if (!destination.exists()) {
            Files.createDirectories(destination.toPath());
        }

        if (source.isDirectory()) {
            String[] files = source.list();
            if (files != null) {
                for (String file : files) {
                    copyDirectory(new File(source, file), new File(destination, file));
                }
            }
        } else {
            // Copy the file if it is different
            Path sourcePath = source.toPath();
            Path destinationPath = destination.toPath();
            FileService.copyIfChanged(sourcePath, destinationPath);
        }
    }

	public static void installFiles(IFileStateController filestateController, IConfigurationController configurationController, List<String> installFiles) {
		for (String curFilename: installFiles) {
			String destName = configurationController.getSourcePartitioning().mapToDestination(curFilename);
			filestateController.installMerged(curFilename, destName);
		}
	}
	
}
