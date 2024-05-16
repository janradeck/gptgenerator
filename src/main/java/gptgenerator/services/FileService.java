package gptgenerator.services;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class FileService {
	
	public static String readFromFile(String filename) {
		File file = new File(filename);
    	if (! file.exists() || ! file.isFile() ) {
    		return "";
    	}		
		try {
			StringBuilder resultStringBuilder = new StringBuilder();
			try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)))) {
				String line;
				while ((line = br.readLine()) != null) {
					resultStringBuilder.append(line).append("\n");
				}
			}
			return resultStringBuilder.toString();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}
	
	public static void saveToFile(String replyFilename, String fileContent, long lastModified) {
		File file = new File(replyFilename);
		try {
			Files.createDirectories(file.toPath().getParent());			
			BufferedWriter writer = new BufferedWriter(new FileWriter(file));
			writer.write(fileContent);
			writer.close();
			file.setLastModified(lastModified);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	

    public static int lineCnt(String text) {
    	return text.split("\n", -1).length - 1;
    }

	public static void copyIfChanged(Path sourcePath, Path destinationPath) {
		if (!filesIdentical(sourcePath.toString(), destinationPath.toString())) {
		    FileService.copyFile(sourcePath, destinationPath);
		}
	}
	
	/**
	 * Install, if the destination does not exist, or its contents differs from "content".<br>
	 * Set "lastModified" of the created file
	 * @param destName
	 * @param content
	 * @param lastModified
	 */
	public static void installIfChanged(String destName, String content, long lastModified) {
		File destinationFile = new File(destName);
		if (!destinationFile.exists()) {
		    saveToFile(destName, content, lastModified);
		}
	}
	
    
    public static void copyFile(Path source, Path destination) {
		try {
			Files.createDirectories(destination.getParent());

			CopyOption[] options = { StandardCopyOption.COPY_ATTRIBUTES, StandardCopyOption.REPLACE_EXISTING };
			Files.copy(source, destination, options);
		} catch (IOException e) {
			e.printStackTrace();
		}    	
    }

	public static void copyFile(String sourceFile, String destFile) {
		Path source = Paths.get(sourceFile);
		Path destination = Paths.get(destFile);
		copyFile(source, destination);
	}
	
	public static String stripSeparator(String path) {
		path = path.strip();
		if (path.endsWith("/") || path.endsWith("\\")) {
			path = path.substring(0, path.length() - 1);
		}
		return(path);
	}

	public static void remove(String filename) {
		Path path = Paths.get(filename);
        try {
        	if (Files.exists(path, LinkOption.NOFOLLOW_LINKS)) {
        		Files.delete(path);
        	}
		} catch (IOException e) {
			e.printStackTrace();
		}				
	}
	
	/**
	 * Removes all files and subdirectories from "directory" and removes the directory "directory" as well
	 * @param directory
	 */
	public static void clearAndDeleteDirectory(String directory) {		
		clearDirectory(directory);
		File output = new File(directory);
		if (output.isDirectory()) {
			output.delete();
		}	
	}
	
	/**
	 * Removes all files and subdirectories from directory
	 * @param directory
	 */
	public static void clearDirectory(String directory) {
		File output = new File(directory);
		if (output.isDirectory()) {
			File[] files = output.listFiles();
			if (files != null) {
				for (File cur : files) {
					if (cur.isFile()) {
						cur.delete();
					} else {
						clearAndDeleteDirectory(directory + File.separator + cur.getName());
					}
				}
				output.delete();
			}
		}
	}
	
	public static String promptExt() {
		return ".prompt";
	}
	
	public static String fragmentExt() {
		return ".fragment";
	}

	public static String fragmentExtPattern() {
		return "\\.fragment$";
	}
	
	public static String promptExtPattern() {
		return "\\.prompt$";
	}

	public static String removePromptExt(String filename) {
		return filename.replaceAll(promptExtPattern(), "");
	}

	public static String getDirectory(String filename) {
		File file = new File(filename);
		return null == file.getParent() ? "" : file.getParent();
	}

	public static int recursiveFileCount(String directory) {
		int result = 0;

		File output = new File(directory);
		if (output.isDirectory()) {
			File[] files = output.listFiles();
			if (files != null) {
				for (File cur : files) {
					if (cur.isFile()) {
						result ++;
					} else {
						result += recursiveFileCount(directory + File.separator + cur.getName());
					}
				}
			}
		}
		
		return result;
	}

	public static boolean fileExists(String filename) {
		File file = new File(filename);
		return (file.exists() && file.isFile());
	}
	
	/**
	 * We compare the content
	 * @param leftFilename
	 * @param rightFilename
	 * @return
	 */
	public static boolean filesIdentical (String leftFilename, String rightFilename) {
		if (fileExists(leftFilename) && fileExists(rightFilename)) {
			String leftContent = readFromFile(leftFilename);
			String rightContent = readFromFile(rightFilename);
			return leftContent.equals(rightContent);
		}
		return false;
	}

	public static boolean isPrompt(String curFilename) {
		return curFilename.toLowerCase().endsWith(FileService.promptExt());
	}
	
}
