package gptgenerator.services;

import java.util.Set;

import gptgenerator.uc.configure.IConfigurationModel;
import gptgenerator.uc.filecompare.CurrentAndPrevious;

public class CacheCollection {

	private WriteThroughCache inputCur = new WriteThroughCache("");
	private WriteThroughCache inputPrev = new WriteThroughCache("");
	private WriteThroughCache mergeCur = new WriteThroughCache("");
	private WriteThroughCache mergePrev = new WriteThroughCache("");
	private WriteThroughCache reply = new WriteThroughCache("");
	
	public CacheCollection() {
		 
	}
	
	public void updateInput (CurrentAndPrevious inputConfig) {
		inputCur = new WriteThroughCache(inputConfig.getCurrentBaseDir());
		inputPrev = new WriteThroughCache(inputConfig.getPreviousBaseDir());
	}
	
	public void updateMerge(CurrentAndPrevious mergeConfig) {
		mergeCur = new WriteThroughCache(mergeConfig.getCurrentBaseDir());
		mergePrev = new WriteThroughCache(mergeConfig.getPreviousBaseDir());
	}
	
	public void update(IConfigurationModel model) {
		updateInput(model.getInputConfig());
		updateMerge(model.getMergeConfig());
	}
	
	public WriteThroughCache getInputCur() {
		return inputCur;
	}

	public WriteThroughCache getInputPrev() {
		return inputPrev;
	}

	public WriteThroughCache getMergeCur() {
		return mergeCur;
	}

	public WriteThroughCache getMergePrev() {
		return mergePrev;
	}

	public boolean hasInputFileCur(String filename) {
		return inputCur.contains(filename);
	}
	
	public CacheEntry getInputFileCur(String baseFilename) {
		return inputCur.getEntry(baseFilename);
	}

	public boolean hasMergeFileCur(String baseFilename) {
		return mergeCur.contains(baseFilename); 
	}

	public CacheEntry getMergeFileCur(String baseFilename) {
		return mergeCur.getEntry(baseFilename);
	}

	public void saveInputFilePrev(CacheEntry entry) {
		inputPrev.saveAndAdd(entry);
	}

	public void saveMergeFileCur(CacheEntry entry) {
		mergeCur.saveAndAdd(entry);
	}

	public void saveMergeFilePrev(CacheEntry entry) {
		mergePrev.saveAndAdd(entry);
	}

	public void initPromptAndReplyCache(String replyCacheDir) {
		FileService.clearDirectory(replyCacheDir);
		reply = new WriteThroughCache(replyCacheDir);
	}
	
	public void saveReply(CacheEntry entry) {
		reply.saveAndAdd(entry);
	}
	
	public boolean hasReply(String baseFilename) {
		return reply.contains(baseFilename);
	}

	public Set<String> getCurPromptList() {
		return mergeCur.passFilter(FileService.promptExtPattern()).getFilenameSet();
	}
	
	public Set<String> getCurReplyList() {		
		return mergeCur.addOnlyIfExtendedExists(FileService.promptExtPattern()).getFilenameSet();
	}
	
	public void saveToReplyCache(Set<String> filenames) {
		for (String curFilename: filenames) {
			CacheEntry cur = mergeCur.getEntry(curFilename);
			reply.saveAndAdd(cur);
		}
	}

	public CacheEntry getFromReplyCache(String filename) {
		return reply.getEntry(filename);
	}

	public void moveMergeCurToMergePrev() {
		mergePrev.clear();
		for (String filename: mergeCur.getFilenameSet()) {
			mergePrev.saveAndAdd(mergeCur.getEntry(filename));
		}
		mergeCur.clear();
		
	}	
	
}
