package gptgenerator.uc.mainview;

import java.util.List;
import java.util.Set;

import gptgenerator.services.CacheEntry;
import gptgenerator.uc.processing.IStatusDecider;

public interface IFileStateModel {
	void refreshCache();
	void sort();
	PhaseStatus get(int index);
	List<PhaseStatus> getList();
	
	List<TransformationStatus> getMergePendingList();
	List<TransformationStatus> getPromptPendingList(IStatusDecider decider);
	List<String> getInstallFiles();
	List<String> getMissingInputs();
	List<String> getInputFiles();
	
	void setController(IFileStateController controller);
	void clearController();
	boolean hasInputFileCur(String filename);
	CacheEntry getInputFileCur(String baseFilename);
	boolean hasMergeFileCur(String baseFilename);
	CacheEntry getMergeFileCur(String baseFilename);
	void saveInputFilePrev(CacheEntry entry);
	void saveMergeFileCur(CacheEntry entry);
	void saveMergeFilePrev(CacheEntry entry);
	void installMerged(String curFilename, String destName);
	Set<String> getCurPromptList();
	Set<String> getCurReplyList();
	void saveToReplyCache(Set<String> promptList);
	CacheEntry getFromReplyCache(String filename);
	void moveMergeCurToMergePrev();
}
