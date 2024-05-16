package gptgenerator.uc.mainview;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.JFrame;

import gptgenerator.services.CacheCollection;
import gptgenerator.services.CacheEntry;
import gptgenerator.services.FileService;
import gptgenerator.uc.configure.IConfigurationModel;
import gptgenerator.uc.processing.IStatusDecider;
import gptgenerator.uc.processing.StatusDeciderOnChange;

/**
 * Determine the status for every phase of a file
 */
public class FileStateModel implements IFileStateModel {
	List<PhaseStatus> list;
	
	CacheCollection cacheCollection;
	IFileStateController controller = new NilFileStateController();
	IConfigurationModel configuration;

	public FileStateModel(CacheCollection cacheCollection, IConfigurationModel configuration) {
		this.cacheCollection = cacheCollection;
		this.configuration = configuration;
		refreshCache();
	}

	@Override
	public void setController(IFileStateController controller) {
		this.controller = controller;
	}

	@Override
	public void clearController() {
		controller = new NilFileStateController();
	}
	
	@Override
	public void refreshCache() {
		FileStatusOverview overview = new FileStatusOverview();
		
		cacheCollection.update(this.configuration);

		// Determine status for input
		for (TransformationStatus curInputStatus : FileChangeList.getStatusForPendingMerges(cacheCollection)) {
			overview.addInputStatus(curInputStatus);
		}

		// Determine status for merge
		for (TransformationStatus curMergedStatus : FileChangeList.getStatusForMergedFiles(cacheCollection)) {
			overview.addMergeStatus(curMergedStatus);
		}

		// Determine status for prompt
		IStatusDecider decider = new StatusDeciderOnChange();
		for (TransformationStatus curPromptStatus : FileChangeList.getStatusForPendingPrompts(cacheCollection, decider)) {
			overview.addPromptStatus(curPromptStatus);
		}

		// Determine status for reply
		for (TransformationStatus curPromptStatus : FileChangeList.getReplyStatus(cacheCollection)) {
			overview.addReplyStatus(curPromptStatus);
		}

		list = overview.getList();
		sort();

		controller.notifySetList(list);
	}

	@Override
	public List<PhaseStatus> getList() {
		return list;
	}

	/**
	 * Sort the list ascending by
	 * <ul>
	 * <li>input status
	 * <li>merge status
	 * <li>prompt status
	 * <li>reply status
	 * <li>filename
	 * </ul>
	 */
	public void sort() {
		Collections.sort(list, Comparator.comparingInt(PhaseStatus::getInputSortKey)
				.thenComparingInt(PhaseStatus::getMergeSortKey).thenComparingInt(PhaseStatus::getPromptSortKey).thenComparingInt(PhaseStatus::getReplySortKey).thenComparing(PhaseStatus::getFilename));
		controller.notifySetList(list);		
	}

	public PhaseStatus get(int index) {
		return list.get(index);
	}
	
	public int size() {
		return list.size();
	}
	
	public List<String> getSelected(int[] selectedRows) {
		List<String> result = new ArrayList<String>();
		for (int i = 0; i < selectedRows.length; i++) {
			int index = selectedRows[i];
			String filename = get(index).getFilename();
			result.add(filename);
		}
		return result;
	}

	public List<String> getKeyOrder() {
		List<String> result = new ArrayList<String>();
		for (PhaseStatus cur: list) {
			result.add(cur.getFilename());
		}
		return result;
	}
	
	/**
	 * Die Einträge, sortiert wie die Dateinamen in keyOrder.<br>
	 * Einträge, die nicht in keyOrder vorhanden sind, werden im Anschluss einsortiert 
	 * @param keyOrder
	 * @return
	 */
	public void sort(List<String> keyOrder) {
		List<PhaseStatus> sorted = new ArrayList<>();
		
		Map<String, Integer> keyMap = new HashMap<String, Integer>();
		// Alle Einträge notieren
		for (int i = 0; i < list.size(); i ++) {
			PhaseStatus entry = list.get(i);
			keyMap.put(entry.getFilename(), i);
		}
		
		for (String curKey: keyOrder) {
			int index = keyMap.getOrDefault(curKey, -1);
			if (index >= 0) {
				sorted.add(get(index));
				keyMap.remove(curKey);
			}
		}

		// Add the remaining entries, by their original index
		Collection<Integer> values = keyMap.values();
        List<Integer> sortedList = new ArrayList<>(values);
        Collections.sort(sortedList);
        for (Integer index: sortedList) {
        	sorted.add(get(index));
        }
		
		list = sorted;
		controller.notifySetList(list);		
	}

	@Override
	public List<TransformationStatus> getPromptPendingList(IStatusDecider decider) {
		return FileChangeList.getStatusForPendingPrompts(cacheCollection, decider);
	}
	
	@Override
	public List<TransformationStatus> getMergePendingList() {
		return FileChangeList.getStatusForPendingMerges(cacheCollection);
	}

	@Override
	public List<String> getInstallFiles() {
		return FileChangeList.getInstallFiles(cacheCollection);
	}

	@Override
	public boolean hasInputFileCur(String filename) {
		return cacheCollection.hasInputFileCur(filename);
	}

	
	@Override
	public CacheEntry getInputFileCur(String baseFilename) {
		return cacheCollection.getInputFileCur(baseFilename);
	}

	@Override
	public boolean hasMergeFileCur(String baseFilename) {
		return cacheCollection.hasMergeFileCur(baseFilename);
	}

	@Override
	public CacheEntry getMergeFileCur(String baseFilename) {
		return cacheCollection.getMergeFileCur(baseFilename);
	}

	@Override
	public Set<String> getCurPromptList() {
		return cacheCollection.getCurPromptList();
	}
	
	@Override
	public Set<String> getCurReplyList() {
		return cacheCollection.getCurReplyList();
	}

	@Override
	public void saveToReplyCache(Set<String> promptList) {
		cacheCollection.saveToReplyCache(promptList);
	}

	@Override
	public CacheEntry getFromReplyCache(String filename) {
		return cacheCollection.getFromReplyCache(filename);
	}

	@Override
	public void saveInputFilePrev(CacheEntry entry) {
		cacheCollection.saveInputFilePrev(entry);		
	}

	@Override
	public void saveMergeFileCur(CacheEntry entry) {
		cacheCollection.saveMergeFileCur(entry);		
	}

	@Override
	public void saveMergeFilePrev(CacheEntry entry) {
		cacheCollection.saveMergeFilePrev(entry);
		
	}
	
	@Override
	public void installMerged(String curFilename, String destName) {
		CacheEntry entry = cacheCollection.getMergeFileCur(curFilename);
		FileService.installIfChanged(destName, entry.getContent(), entry.getLastModified());
	}

	/**
	 * All files from INPUT(PREV) that are NOT in INPUT(CUR) == SRC_MISSING 
	 */
	@Override
	public List<String> getMissingInputs() {
		Set<String> missingInputs = new HashSet<>();
		
		for (TransformationStatus curPromptStatus : FileChangeList.getStatusForPendingMerges(cacheCollection)) {
			if (curPromptStatus.getStatus().equals(FileChangeStatus.SRC_MISSING)) {				
				missingInputs.add(curPromptStatus.getBaseFilename());
			}
		}
		List<String> sortedList = new ArrayList<>(missingInputs);
        Collections.sort(sortedList);		
		return sortedList;
	}
	
	@Override
	public List<String> getInputFiles() {
		Set<String> inputs =  cacheCollection.getInputCur().getFilenameSet();
		List<String> result = new ArrayList<String>(inputs);
		Collections.sort(result);
		return result;
	}

	private static class FileStatusOverview {

		private Map<String, PhaseStatus> overview = new HashMap<String, PhaseStatus>();

		public void addInputStatus(TransformationStatus input) {
			if (!overview.containsKey(input.getBaseFilename())) {
				overview.put(input.getBaseFilename(), new PhaseStatus(input.getBaseFilename()));
			}
			overview.get(input.getBaseFilename()).setInputStatus(input.getStatus());
		}
		
		public void addMergeStatus(TransformationStatus merge) {
			if (!overview.containsKey(merge.getBaseFilename())) {
				overview.put(merge.getBaseFilename(), new PhaseStatus(merge.getBaseFilename()));
			}
			overview.get(merge.getBaseFilename()).setMergeStatus(merge.getStatus());
		}
		
		public void addPromptStatus(TransformationStatus prompt) {
			if (!overview.containsKey(prompt.getBaseFilename())) {
				overview.put(prompt.getBaseFilename(), new PhaseStatus(prompt.getBaseFilename()));
			}
			overview.get(prompt.getBaseFilename()).setPromptStatus(prompt.getStatus());
		}

		public void addReplyStatus(TransformationStatus reply) {
			if (!overview.containsKey(reply.getBaseFilename())) {
				overview.put(reply.getBaseFilename(), new PhaseStatus(reply.getBaseFilename()));
			}
			overview.get(reply.getBaseFilename()).setReplyStatus(reply.getStatus());
		}

		public List<PhaseStatus> getList() {
			List<PhaseStatus> result = new ArrayList<>(overview.values());
			return result;
		}

	}
	
	private class NilFileStateController implements IFileStateController {

		@Override
		public void showFileComparisonView(JFrame parent, int selectedRow) {
		}

		@Override
		public void refreshCache() {
		}

		@Override
		public void addView(IFileStateView view) {
			System.err.println("NilFileStateController::addView() called");
		}

		@Override
		public void removeView(IFileStateView view) {
			System.err.println("NilFileStateController::removeView() called");
		}

		@Override
		public void requestViewUpdate() {
		}

		@Override
		public void notifySetList(List<PhaseStatus> list) {
		}

		@Override
		public CacheEntry getInputFileCur(String baseFilename) {
			return null;
		}

		@Override
		public boolean hasMergeFileCur(String baseFilename) {
			return false;
		}

		@Override
		public CacheEntry getMergeFileCur(String baseFilename) {
			return null;
		}

		@Override
		public void saveMergeFileCur(CacheEntry mergeFile) {
		}

		@Override
		public void saveInputFilePrev(CacheEntry entry) {
		}

		@Override
		public void saveMergeFilePrev(CacheEntry entry) {
		}

		@Override
		public List<TransformationStatus> getMergePendingList() {
			return null;
		}

		@Override
		public List<String> getInstallFiles() {
			return null;
		}

		@Override
		public void installMerged(String curFilename, String destName) {
		}

		@Override
		public List<TransformationStatus> getPromptPendingList(IStatusDecider decider) {
			return null;
		}

		@Override
		public List<String> getCleanFiles() {
			return null;
		}

		@Override
		public List<String> getInputFiles() {
			return null;
		}

		@Override
		public String validateInputDirIsMappedBySourcePartitioning() {
			return null;
		}

		@Override
		public void clearMergeCur() {
		}

		@Override
		public boolean hasInputFileCur(String filename) {
			return false;
		}

		@Override
		public Set<String> getCurPromptList() {
			return null;
		}

		@Override
		public Set<String> getCurReplyList() {
			return null;
		}

		@Override
		public void saveToReplyCache(Set<String> promptList) {			
		}

		@Override
		public CacheEntry getFromReplyCache(String replyFile) {
			return null;
		}

		@Override
		public void moveMergeCurToMergePrev() {
		}

	}

	@Override
	public void moveMergeCurToMergePrev() {
		cacheCollection.moveMergeCurToMergePrev();
	}

}
