package gptgenerator.uc.mainview;

/**
 * Comparison between cur and previous for a file
 */
public enum FileChangeStatus {
	CHANGED       (1, "Changed"),
	SRC_MISSING   (2, "SRC MISSING"),
	DST_MISSING   (3, "DST MISSING"),
	NOT_APPLICABLE(4, "N/A"),
	UNCHANGED     (5, "");
	
	private int sortKey;
	private String representation;
	
	private FileChangeStatus(int sortKey, String representation) {
		this.sortKey = sortKey;
		this.representation = representation;
	}
	
	public int getSortKey() {
		return sortKey;
	}
	
	@Override
	public String toString() {
		return representation;
	}

	/**
	 * Does the file need to be processed?
	 * @return
	 */
	public boolean requiresProcessing() {
		return this.equals(CHANGED) || this.equals(DST_MISSING);
	}
}
