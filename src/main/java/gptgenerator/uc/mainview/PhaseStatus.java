package gptgenerator.uc.mainview;

/**
 * A status for every phase of the process
 * <ul>
 * <li>input
 * <li>merge
 * <li>prompt
 * <li>reply
 * </ul>
 */
public class PhaseStatus {
		
		private String relFilename = "";
		private FileChangeStatus inputStatus = FileChangeStatus.UNCHANGED;
		private FileChangeStatus mergeStatus = FileChangeStatus.UNCHANGED;
		private FileChangeStatus promptStatus = FileChangeStatus.UNCHANGED;
		private FileChangeStatus replyStatus = FileChangeStatus.UNCHANGED;
		
		public PhaseStatus(String relFilename) {
			this.relFilename = relFilename;
		}
		
		public void setInputStatus(FileChangeStatus inputStatus) {
			this.inputStatus = inputStatus;
		}

		public void setMergeStatus(FileChangeStatus mergeStatus) {
			this.mergeStatus = mergeStatus;
		}

		public void setPromptStatus(FileChangeStatus promptStatus) {
			this.promptStatus = promptStatus;
		}

		public void setReplyStatus(FileChangeStatus replyStatus) {
			this.replyStatus = replyStatus;
		}

		public String getFilename() {
			return relFilename;
		}	
				
		public int getInputSortKey() {
			return inputStatus.getSortKey();
		}

		public int getMergeSortKey() {
			return mergeStatus.getSortKey();
		}
		public int getPromptSortKey() {
			return promptStatus.getSortKey();
		}

		public int getReplySortKey() {
			return replyStatus.getSortKey();
		}

		public String getInputRepresentation() {
			return inputStatus.toString();
		}

		public String getMergeRepresentation() {
			return mergeStatus.toString();
		}
		
		public String getPromptRepresentation() {
			return promptStatus.toString();
		}

		public String getReplyRepresentation() {
			return replyStatus.toString();
		}

		public boolean hasReply() {
			return ! replyStatus.equals(FileChangeStatus.NOT_APPLICABLE);
		}
		
	}
