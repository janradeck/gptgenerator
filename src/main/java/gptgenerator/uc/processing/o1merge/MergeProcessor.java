package gptgenerator.uc.processing.o1merge;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import gptgenerator.services.CacheEntry;
import gptgenerator.services.FileService;
import gptgenerator.uc.configure.IConfigurationController;
import gptgenerator.uc.configure.merge.ITemplateConfigModel;
import gptgenerator.uc.mainview.IFileStateController;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Das Template wird aus "Input (Cur)" gelesen<br>
 * Die Fragmente werden aus "Merge (Cur)" gelesen<br>
 * Wenn der Marker mit "/" anfängt, wird er als absoluter Pfad angesehen.<br>
 * Ansonsten wird das Basisverzeichnis des Templates vorangestellt. 
 *  
 */
public class MergeProcessor {
	
	private IConfigurationController configurationController;
	private IFileStateController filestateController;
	private String filename;
	private CacheEntry entry;
	private long lastModified;
	private List <MergeInclude> includes;
	
	private String replaceMarker;
	private String deleteMarker;
	
	/**
	 * @param filestateController
	 * @param templateConfig 
	 * @param fileToBeMerged
	 */
	public MergeProcessor(IConfigurationController configurationController, IFileStateController filestateController, String fileToBeMerged) {
		this.configurationController = configurationController;
		this.filestateController = filestateController;
		
		ITemplateConfigModel templateConfig = configurationController.getTemplateConfig(fileToBeMerged);
		replaceMarker = templateConfig.getMergedMarker();
		deleteMarker = templateConfig.getUnmergedMarker();
		
		this.filename = fileToBeMerged;
		entry = filestateController.getInputFileCur(fileToBeMerged);
		if (null == entry) {
			System.err.println("MergeProcessor() null pointer looking for input file to be merged " + fileToBeMerged);
			includes = new ArrayList<>();
		} else {
			lastModified = entry.getLastModified();
			String directoryOfFileToBeMerged = FileService.getDirectory(fileToBeMerged);
			includes = getIncludes(directoryOfFileToBeMerged);
		}
	}

	/**
	 * Do all includes exist?
	 * @return
	 */
	public boolean readyToMerge() {
		for (MergeInclude curInclude: includes) {
			if (! includeExists(curInclude)) {
				return false;
			}
		}
		return true;
	}
	
	private boolean includeExists(MergeInclude curInclude) {
		if (curInclude.areMarkersSubstituted()) {
			return filestateController.hasMergeFileCur(curInclude.getExpandedFilename());
		} else {
			return filestateController.hasInputFileCur(curInclude.getExpandedFilename());
		}
	}

	/**
	 * Replace all markers with included files
	 * @return
	 */
	public CacheEntry merge() {
		String text = entry.getContent();
		for (MergeInclude cur: includes) {
			text = replaceOneInclude(text, cur);
		}
		
		return new CacheEntry (filename, lastModified, text); 
	}
	
	/**
	 * Remove all markers from text, without substitution
	 * @return
	 */
	public String contentWithoutMarkers(String text, ITemplateConfigModel tcForIncludedFile) {
		String result = text.replaceAll(tcForIncludedFile.getMergedMarker(), "");
		return result.replaceAll(tcForIncludedFile.getUnmergedMarker(), "");
	}

	/**
	 * Returns the original CacheEntry
	 * @return
	 */
	public CacheEntry getSourceEntry() {
		return entry;
	}
	
	/**
	 * Replaces occurrences of a specific string pattern with a replacement string in the given text.
	 *
	 * @param text The original text where replacements will be made.
	 * @param curInclude The MergeInclude object representing the include to be replaced.
	 * @return The modified text with replacements made.
	 */
	private String replaceOneInclude(String text, MergeInclude curInclude) {
		String regexPattern = Pattern.quote(curInclude.getFullMatch());
		CacheEntry curEntry;
		String res;
		
		String fileToBeIncluded = curInclude.getExpandedFilename();
		if (curInclude.areMarkersSubstituted()) {
			curEntry = filestateController.getMergeFileCur(curInclude.getExpandedFilename());
			String replacement = java.util.regex.Matcher.quoteReplacement(curEntry.getContent());
			res = text.replaceAll(regexPattern, replacement);
		} else {
			curEntry = filestateController.getInputFileCur(curInclude.getExpandedFilename());
			ITemplateConfigModel tcForIncludedFile = configurationController.getTemplateConfig(fileToBeIncluded);
			
			String replacement = java.util.regex.Matcher.quoteReplacement(contentWithoutMarkers(curEntry.getContent(), tcForIncludedFile));
			res = text.replaceAll(regexPattern, replacement);			
		}
		if (curEntry.getLastModified() > lastModified) {
			lastModified = curEntry.getLastModified();
		}
		// String prev = text;
		/*
		if (prev.equalsIgnoreCase(res)) {
			System.err.println("No substitution done");
		}
		*/
		return res;
	}

	/**
	 * Retrieves the list of merge includes for the specified directory of the file to be merged.
	 *
	 * @param directoryOfFileToBeMerged The directory of the file to be merged.
	 * @return The list of merge includes.
	 */
	private List<MergeInclude> getIncludes(String directoryOfFileToBeMerged) {
		List<MergeInclude> result = new ArrayList<>();
		
		String prefix = directoryOfFileToBeMerged.isEmpty() ? "" : directoryOfFileToBeMerged + File.separator;
		
		result.addAll(getIncludes(entry.getContent(), prefix, replaceMarker, new MergedIncludeMaker()));
		result.addAll(getIncludes(entry.getContent(), prefix, deleteMarker, new UnmergedIncludeMaker()));
		
		return result;
	}
	
	/**
	 * Retrieves a list of MergeInclude objects based on the provided content, prefix, marker, and MergeIncludeMaker.
	 *
	 * @param content The content to search for markers.
	 * @param prefix The prefix to prepend to the include filenames.
	 * @param marker The marker pattern used to identify include statements.
	 * @param includeMaker The MergeIncludeMaker used to create MergeInclude objects.
	 * @return A list of MergeInclude objects.
	 */
	private List<MergeInclude> getIncludes(String content, String prefix, String marker, MergeIncludeMaker includeMaker) {
		List<MergeInclude> result = new ArrayList<>();

		Pattern pattern = Pattern.compile(marker); 
		Matcher matcher = pattern.matcher(content);
		while(matcher.find()) {
			String fullMatch = matcher.group();
			String includeFilename = matcher.group(1);
			String fullFilename = makeFullFilename(prefix, includeFilename); 
					
			MergeInclude cur = includeMaker.mergeInclude(fullMatch, includeFilename, fullFilename);
			result.add(cur);
		}
		
		return result;
	}
	
	private String makeFullFilename(String prefix, String filename) {
		if (! filename.startsWith("/")) {
			return prefix + filename;
		}
		return filename;
	}
	
	private interface MergeIncludeMaker {
		MergeInclude mergeInclude(String fullMatch, String filename, String fullFilename);
	}
	
	private class MergedIncludeMaker implements MergeIncludeMaker {

		@Override
		public MergeInclude mergeInclude(String fullMatch, String filename, String fullFilename) {
			return MergeInclude.withMarkersSubstituted(fullMatch, filename, fullFilename);
		}
		
	}

	private class UnmergedIncludeMaker implements MergeIncludeMaker {

		@Override
		public MergeInclude mergeInclude(String fullMatch, String filename, String fullFilename) {
			return MergeInclude.withMarkersDeleted(fullMatch, filename, fullFilename);
		}
		
	}
	
}
