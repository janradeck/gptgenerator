package gptgenerator.uc.mainview;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.swing.JFrame;

import gptgenerator.services.CacheEntry;
import gptgenerator.services.FileService;
import gptgenerator.uc.configure.IConfigurationModel;
import gptgenerator.uc.filecompare.CompareFileView;
import gptgenerator.uc.processing.IStatusDecider;

public class FileStateController implements IFileStateController {

    private IFileStateModel stateModel;
    private IConfigurationModel configuration;

    private List<IFileStateView> registeredViews = new ArrayList<>();
    
	public FileStateController(IConfigurationModel configurationModel, IFileStateModel stateModel) {
		this.configuration = configurationModel;
		this.stateModel = stateModel;
		stateModel.setController(this);
		// TODO: Add as listener to ConfigurationModel
	}

	/**
	 * 
	 * @param selectedRow
	 */
	public void showFileComparisonView(JFrame parent, int selectedRow) {
    	PhaseStatus curComparison = stateModel.get(selectedRow);
    	FileComparisonData fcd = new FileComparisonData();
    	fcd.setRelativeFilename(curComparison.getFilename());
    	
    	String filename = curComparison.getFilename();
    	String replyFilename = curComparison.getFilename();
    	if (curComparison.hasReply()) {
    		replyFilename = FileService.removePromptExt(filename);
    	}

    	fcd.setInputCurrent(configuration.getInputConfig().getCurrentBaseDir(), filename );
    	fcd.setInputPrevious(configuration.getInputConfig().getPreviousBaseDir(), filename );
    	fcd.setMergeCurrent(configuration.getMergeConfig().getCurrentBaseDir(), filename );
    	fcd.setMergePrevious(configuration.getMergeConfig().getPreviousBaseDir(), filename );
    	
    	if (curComparison.hasReply()) {
    		fcd.setReplyCurrent(configuration.getMergeConfig().getCurrentBaseDir(), replyFilename );
    		fcd.setReplyPrevious(configuration.getMergeConfig().getPreviousBaseDir(), replyFilename );
    	}
    	
    	CompareFileView fileComparisonView = new CompareFileView(parent, fcd);
        fileComparisonView.setVisible(true);
	}
		
	@Override
	public void refreshCache() {
		stateModel.refreshCache();
	}

	@Override
	public void notifySetList(List<PhaseStatus> list) {
		for (IFileStateView view: registeredViews) {
			view.setList(list);
		}
	}

	@Override
	public List<String> getInputFiles() {
		return stateModel.getInputFiles();
	}

	@Override
	public Set<String> getCurPromptList() {
		return stateModel.getCurPromptList();
	}
	
	@Override
	public Set<String> getCurReplyList() {
		return stateModel.getCurReplyList();
	}

	@Override
	public List<TransformationStatus> getMergePendingList() {
		return stateModel.getMergePendingList();
	}
	
	@Override
	public List<String> getInstallFiles() {
		return stateModel.getInstallFiles();
	}

	@Override
	public CacheEntry getInputFileCur(String baseFilename) {
		return stateModel.getInputFileCur(baseFilename);
	}

	@Override
	public boolean hasInputFileCur(String filename) {
		return stateModel.hasInputFileCur(filename);
	}

	@Override
	public boolean hasMergeFileCur(String baseFilename) {
		return stateModel.hasMergeFileCur(baseFilename);
	}

	@Override
	public CacheEntry getMergeFileCur(String baseFilename) {
		return stateModel.getMergeFileCur(baseFilename);
	}

	@Override
	public void saveInputFilePrev(CacheEntry entry) {
		stateModel.saveInputFilePrev(entry);
	}

	@Override
	public void saveMergeFileCur(CacheEntry entry) {
		stateModel.saveMergeFileCur(entry);
	}

	@Override
	public void saveMergeFilePrev(CacheEntry entry) {
		stateModel.saveMergeFilePrev(entry);
	}

	@Override
	public void installMerged(String curFilename, String destName) {
		stateModel.installMerged(curFilename, destName);
	}

	@Override
	public List<TransformationStatus> getPromptPendingList(IStatusDecider decider) {
		return stateModel.getPromptPendingList(decider);
	}
	
	@Override
	public List<String> getCleanFiles() {
		List<String> missingInputs = stateModel.getMissingInputs();
		
		List<String> result = new ArrayList<>();
		for (String cur: missingInputs) {
			result.add(configuration.getInputConfig().getCurrentBaseDir() + File.separator + cur);
			result.add(configuration.getInputConfig().getPreviousBaseDir() + File.separator + cur);
			result.add(configuration.getMergeConfig().getCurrentBaseDir() + File.separator + cur);
			result.add(configuration.getMergeConfig().getPreviousBaseDir() + File.separator + cur);
			
			if (cur.endsWith(FileService.promptExt())) {
				String reply = FileService.removePromptExt(cur);
				result.add(configuration.getMergeConfig().getCurrentBaseDir() + File.separator + reply);
				result.add(configuration.getMergeConfig().getPreviousBaseDir() + File.separator + reply);	
				result.add(configuration.mapToDestination(reply));
			}
			
		}
		return result;
	}

	@Override
	public String validateInputDirIsMappedBySourcePartitioning() {
		for (String curFilename: stateModel.getInputFiles()) {
			if (null == configuration.mapToDestination(curFilename)) {
				return "No SourcePartition for input file " + curFilename;
			}
		}
		return "";
	}

	@Override
	public void clearMergeCur() {
		FileService.clearDirectory(configuration.getMergeConfig().getCurrentBaseDir());
	}

	@Override
	public void saveToReplyCache(Set<String> promptList) {
		stateModel.saveToReplyCache(promptList);
	}
	
	@Override
	public void addView(IFileStateView view) {
		registeredViews.add(view);		
	}

	@Override
	public void removeView(IFileStateView view) {
		registeredViews.remove(view);		
	}

	@Override
	public void requestViewUpdate() {
		notifySetList(stateModel.getList());
	}

	@Override
	public CacheEntry getFromReplyCache(String filename) {
		return stateModel.getFromReplyCache(filename);
	}

	@Override
	public void moveMergeCurToMergePrev() {
		stateModel.moveMergeCurToMergePrev();		
	}

}
