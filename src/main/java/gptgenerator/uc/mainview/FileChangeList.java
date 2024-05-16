package gptgenerator.uc.mainview;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import gptgenerator.services.CacheCollection;
import gptgenerator.services.CacheEntry;
import gptgenerator.services.FileService;
import gptgenerator.services.WriteThroughCache;
import gptgenerator.uc.processing.IStatusDecider;

/**
 * 
 */
public class FileChangeList {
	private static final String PROMPT = FileService.promptExt();

	/**
	 * Compare Input (Cur) with Input (Prev)<br>
	 * Remove suffix ".prompt", if necessary, to get the "base" filename
	 * @param cacheCollection
	 * @return
	 */
	public static List<TransformationStatus> getStatusForPendingMerges(CacheCollection cacheCollection) {
		return compare(cacheCollection.getInputCur(), cacheCollection.getInputPrev());
	}

	
	/**
	 * Compare Input (Cur) with Input (Prev)<br>
	 * Only return the prompts that need to be uninstalled = SRC_MISSING.<br>
	 * @param cacheCollection
	 * @param entrySelector 
	 * @param statusDecider 
	 * @return
	 */
	public static List<TransformationStatus> getStatusForPendingUninstalls(CacheCollection cacheCollection, IStatusDecider statusDecider) {
		List<TransformationStatus> uninstallList =  compare(cacheCollection.getInputCur(), cacheCollection.getInputPrev());
		
		List<TransformationStatus> result = new ArrayList<>();
		for (TransformationStatus cur: uninstallList) {
			
			if (statusDecider.select(cur) && cur.getStatus().equals(FileChangeStatus.SRC_MISSING))  {
				result.add(cur);
			}
		}
		return result;
	}
	
	/**
	 * Compare Merge (Cur) with Merge (Prev)<br>
	 * We need to filter replies. Do not add a file "FILENAME.prompt" if a "FILENAME" exists
	 * Result will include ".fragment" files
	 * @param cacheCollection
	 * @return
	 */
	public static List<TransformationStatus> getStatusForMergedFiles(CacheCollection cacheCollection) {
		return compare(cacheCollection.getMergeCur().addIfNotExtendedExists(PROMPT),
				cacheCollection.getMergePrev().addIfNotExtendedExists(PROMPT));
	}

	/**
	 * Look for .prompt files without reply files in Merge(Cur).<br> 
	 * MergeManager makes sure that then the prompt needs to be sent
	 * Result only includes ".prompt" files
	 * @param cacheCollection
	 * @return
	 */
	public static List<TransformationStatus> getStatusForPendingPrompts(CacheCollection cacheCollection, IStatusDecider statusDecider) {
		WriteThroughCache promptsWithoutReplies = cacheCollection.getMergeCur().addIfReplyDoesNotExist();
		
		List<TransformationStatus> result = new ArrayList<>();
		for (String curFilename: promptsWithoutReplies.getFilenameSet()) {
			TransformationStatus cur = new TransformationStatus(curFilename, FileChangeStatus.CHANGED); 
			if (statusDecider.select(cur)) {
				result.add(cur);
			}
		}
		return result;
	}

	/**
	 * Compare Reply (Cur) with Reply (Prev)<br>
	 * Add a file only
	 * <ul>
	 * <li>if its name does NOT end in ".prompt"
	 * <li>if a file with the name "name.prompt" exists
	 * </ul>
	 * @param cacheCollection
	 * @return
	 */
	public static List<TransformationStatus> getReplyStatus(CacheCollection cacheCollection) {
		// Since we do not include with the suffix ".prompt", we do not need to filter it
		return compare(cacheCollection.getMergeCur().addOnlyIfExtendedExists(PROMPT), cacheCollection.getMergePrev().addOnlyIfExtendedExists(PROMPT));
	}

	/**
	 * Filter ".prompt" and ".fragment" files
	 * @param cacheCollection
	 * @return
	 */
	public static List<String> getInstallFiles(CacheCollection cacheCollection) {
		WriteThroughCache installCache = cacheCollection.getMergeCur().blockFilter(FileService.fragmentExtPattern()).blockFilter(FileService.promptExtPattern());
		List<String> result = new ArrayList<>(installCache.getFilenameSet());
		Collections.sort(result);
		return(result);
	}

	
	/**
	 * Compare two caches
	 * @param src
	 * @param dst
	 * @return
	 */
	public static List<TransformationStatus> compare(WriteThroughCache src, WriteThroughCache dst) {
		List<TransformationStatus> result = new ArrayList<TransformationStatus>();

		for (String srcFile : src.getFilenameSet()) {
			if (! dst.contains(srcFile)) {
				result.add(new TransformationStatus(srcFile, FileChangeStatus.DST_MISSING));
			} else  {
				CacheEntry srcEntry = src.getEntry(srcFile);
				CacheEntry dstEntry = dst.getEntry(srcFile);
				if (srcEntry.equals(dstEntry)) {
					result.add(new TransformationStatus(srcFile, FileChangeStatus.UNCHANGED));
				} else {
					result.add(new TransformationStatus(srcFile, FileChangeStatus.CHANGED));					
				}
			}
		}
		
		for (String dstFile : dst.getFilenameSet()) {
			if (! src.contains(dstFile)) {
				result.add(new TransformationStatus(dstFile, FileChangeStatus.SRC_MISSING));
			}
		}
		
		return result;
	}

}
