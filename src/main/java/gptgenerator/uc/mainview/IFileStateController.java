package gptgenerator.uc.mainview;

import java.util.List;
import java.util.Set;

import javax.swing.JFrame;

import gptgenerator.services.CacheEntry;
import gptgenerator.uc.processing.IStatusDecider;

public interface IFileStateController {
	
	void showFileComparisonView(JFrame parent, int selectedRow);
	void refreshCache();
	void notifySetList(List<PhaseStatus> list);	
	
	void addView(IFileStateView view);
	void removeView(IFileStateView view);
	void requestViewUpdate();
	
	/**
	 * All files in Input(Cur)
	 * @return
	 */
	List<String> getInputFiles();
	/**
	 * All files in Input(Cur) that are not in Merge(Cur)
	 * @return
	 */
	List<TransformationStatus> getMergePendingList();
	/**
	 * All files in Merge(Cur) that end in .prompt, where Merge(Cur) != Merge(Prev)
	 * @param decider
	 * @return
	 */
	List<TransformationStatus> getPromptPendingList(IStatusDecider decider);
	
	/**
	 * All files in Input(Prev) that do not exist in Input(Cur)
	 * @return
	 */
	List<String> getCleanFiles();
	/**
	 * All files in Merge(Cur) that do not end in .prompt or .fragment
	 * @return
	 */
	List<String> getInstallFiles();
	
	/**
	 * Does this file exist in Input(Cur)?
	 * @param baseFilename
	 * @return
	 */
	boolean hasInputFileCur(String filename);	
	
	CacheEntry getInputFileCur(String baseFilename);
	/**
	 * Does this file exist in Merge(Cur)?
	 * @param baseFilename
	 * @return
	 */
	boolean hasMergeFileCur(String baseFilename);
	
	CacheEntry getMergeFileCur(String baseFilename);
	
	void saveMergeFileCur(CacheEntry mergeFile);
	void saveInputFilePrev(CacheEntry entry);
	void saveMergeFilePrev(CacheEntry entry);
	
	void installMerged(String curFilename, String destName);
	/**
	 * Are all files in InputDir mapped by a SourcePartitionModel
	 * @return
	 */
	public String validateInputDirIsMappedBySourcePartitioning();
	/**
	 * Remove files and subdirectories of Merge(Cur)
	 */
	void clearMergeCur();
	Set<String> getCurPromptList();
	Set<String> getCurReplyList();
	void saveToReplyCache(Set<String> promptList);
	CacheEntry getFromReplyCache(String replyFile);
	void moveMergeCurToMergePrev();
}