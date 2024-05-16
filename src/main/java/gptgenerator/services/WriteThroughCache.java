package gptgenerator.services;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import gptgenerator.uc.mainview.SelectedFiles;

public class WriteThroughCache {
	private String basePath;
	Map<String, CacheEntry> entries;

    public WriteThroughCache(String basePath) {
    	this.basePath = basePath;
        this.entries = new HashMap<>();
        addAll(basePath);
    }
    
	public WriteThroughCache emptyCopy() {
    	WriteThroughCache result = new WriteThroughCache();
    	result.basePath = basePath;
        result.entries = new HashMap<>();
        return result;
    }

    private WriteThroughCache() {
    	this.basePath = "";
        this.entries = new HashMap<>();
	}
    
    /**
     * @param filenamePattern
     * @return
     */
    public WriteThroughCache blockFilter (String filenamePattern) {
    	WriteThroughCache result = emptyCopy();
    	Pattern pattern = Pattern.compile(filenamePattern);

    	for (String filename : entries.keySet()) {
    		Matcher matcher = pattern.matcher(filename);
    		if (!matcher.find()) {
    			result.entries.put(filename, entries.get(filename));
    		}
    	}
    	return result;
    }
    
    /**
     * @param filenamePattern
     * @return
     */
    public WriteThroughCache passFilter (String filenamePattern) {
    	WriteThroughCache result = emptyCopy();
    	Pattern pattern = Pattern.compile(filenamePattern);

    	for (String filename : entries.keySet()) {
    		Matcher matcher = pattern.matcher(filename);
    		if (matcher.find()) {
    			result.addEntry(getEntry(filename));
    		}
    	}
    	return result;
    }
    
    public WriteThroughCache passFilter(SelectedFiles selectedFiles) {
    	WriteThroughCache result = emptyCopy();
    	for (String filename : entries.keySet()) {
    		if (selectedFiles.contains(filename)) {
    			result.addEntry(getEntry(filename));
    		}
    	}
    	return result;
    }

    private void addEntry(CacheEntry entry) {
		entries.put(entry.getFilename(), entry);
	}

    /**
     * Remove a file from files system and from cache
     * @param entry
     */
    public void removeFile(CacheEntry entry) {
    	FileService.remove(basePath + File.separator + entry.getFilename());
    	entries.remove(entry.getFilename());
	}
    
	/**
     * Remove a suffix from a filename
     * @param filenameTail
     * @return
     */
    public WriteThroughCache stripFilenameTail (String filenameTail) {
    	WriteThroughCache result = emptyCopy();

    	int removeLength = filenameTail.length();
    	for (String filename : entries.keySet()) {
    		if (filename.endsWith(filenameTail)) {
    			String newFilename = filename.substring(0, filename.length() - removeLength);
    			result.entries.put(newFilename, entries.get(filename));
    		}
    	}
    	return result;
    }

	/**
	 * Do not add the entry to the result, if an entry with the same name, plus "filenameSuffix" exists<br>
	 * E.g. for filenameSuffix = ".prompt":<br>
	 * IF ! FILENAME exist THEN<br>
	 *   add FILENAME.prompt
	 * @param filenameSuffix
	 * @return
	 */
	public WriteThroughCache addIfNotExtendedExists(String filenameSuffix) {
		WriteThroughCache result = emptyCopy();
		for (String filename : entries.keySet()) {
			String extendedFilename = filename + filenameSuffix;
			
			// System.err.println(String.format("addIfNotExtendedExists %s / %s", filename, extendedFilename));
			if (!entries.containsKey(extendedFilename)) {
				result.entries.put(filename, entries.get(filename));
			}
		}

		return result;
	}

	public WriteThroughCache addIfReplyDoesNotExist() {
		WriteThroughCache result = emptyCopy();
		for (String filename : entries.keySet()) {
			if (FileService.isPrompt(filename)) {
				String replyFilename = FileService.removePromptExt(filename);
				if (!entries.containsKey(replyFilename)) {
					result.entries.put(filename, entries.get(filename));
				}
			}
		}

		return result;
	}
    
	
	/**
	 * Only add files without suffix, and only if an entry with the same name, plus "filenameSuffix" exists<br>
	 * This is used to find replies for ".prompt" files
	 * @param filenameSuffix
	 * @return
	 */
	public WriteThroughCache addOnlyIfExtendedExists(String filenameSuffix) {
    	WriteThroughCache result = emptyCopy();
    	
    	for (String filename : entries.keySet()) {
    		if (! filename.endsWith(filenameSuffix)) {
    			String elongatedFilename = filename + filenameSuffix;
    			if (!entries.containsKey(elongatedFilename)) {
    				result.entries.put(filename, entries.get(filename));
    			}
    		}
    	}
    	
    	return result;
	}

    /**
     * Recursively add all files in a directory
     * @param directory
     */
    private void addAll(String directory) {
        recursivelyReadFiles(directory, "");
    }

	private void recursivelyReadFiles(String dstBase, String relDir) {
		String dstBaseName = dstBase + (relDir.isEmpty() ? "" : File.separator + relDir);
		String dirPrefix = (relDir.isEmpty() ? "" : relDir + File.separator);

		File[] files = new File(dstBaseName).listFiles();
		if (files != null) {
			for (File curFile : files) {
				if (curFile.isFile()) {
					String relativeFilename = dirPrefix + curFile.getName();
					addEntryFromFile(relativeFilename, curFile);
				} else if (curFile.isDirectory()) {
					String newRelDir = dirPrefix + curFile.getName();
					recursivelyReadFiles(dstBase, newRelDir);
				}
			}
		}
	}

    private void addEntryFromFile(String filename, File file) {
        long lastModified = file.lastModified();

        String content = FileService.readFromFile(file.getAbsolutePath());

        CacheEntry entry = new CacheEntry(filename, lastModified, content);
        entries.put(filename, entry);
    }

 
    /**
     * Save a file and add it to cache. Set modification date
     * @param filename
     * @param content
     * @param lastModified
     */
    public void saveAndAdd(String filename, String content, long lastModified) {
        CacheEntry entry = new CacheEntry(filename, lastModified, content);
        entries.put(filename, entry);

        String filePath = basePath + File.separator + filename;
        FileService.saveToFile(filePath, content, lastModified);
    }
    
    
    public Set<String> getFilenameSet() {
    	return entries.keySet();
    }

    /**
     * Returns an empty CacheEntry, if the file was not found
     * @param filename
     * @return
     */
    public CacheEntry getEntry(String filename) {
    	return entries.getOrDefault(makeKey(filename), CacheEntry.emptyEntry(filename));
    }
    
    public boolean contains(String filename) {
    	return entries.containsKey(makeKey(filename));
    }
    
    private static String makeKey(String filename) {
    	return filename.replaceAll("/", "\\\\");
    }

    /**
     * Save a file and add it to cache. Set modification date<br>
     * Does nothing, if CacheEntry.isEmpty()
     * @param filename
     * @param content
     * @param modificationDate
     */
	public void saveAndAdd(CacheEntry entry) {
		if (entry.isEmpty()) return;
        entries.put(entry.getFilename(), new CacheEntry(entry));

        String filePath = basePath + File.separator + entry.getFilename();
        FileService.saveToFile(filePath, entry.getContent(), entry.getLastModified());
	}

	/**
	 * Remove all files from directory and clear cache entries
	 */
	public void clear() {
		for (CacheEntry curEntry: entries.values()) {
			FileService.remove(curEntry.getFilename());
		}
		entries = new HashMap<>(); 
	}

}
