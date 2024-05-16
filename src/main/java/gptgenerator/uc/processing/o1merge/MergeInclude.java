package gptgenerator.uc.processing.o1merge;

/**
 */
/**
 * Represents a merge include, which is used to specify how markers should be handled when including a file during processing.<br>

 * If the template is not located in the root directory, then "filename" and "expandedFilename" will be different
 */
public class MergeInclude {

	/**
	 * Enumeration for marker modes.
	 */
	enum MarkerMode {
		/**
		 * Markers are substituted with the content of the referenced file.
		 */
		SUBSTITUTE_MARKERS,

		/**
		 * Markers will be deleted during processing.
		 */
		DELETE_MARKERS
	};

	private String fullMatch;
	private String filename;
	private String expandedFilename;
	private MarkerMode markerMode;

	/**
	 * Private constructor for MergeInclude.
	 * Use {@link #withMarkersSubstituted(String, String, String)} or {@link #withMarkersDeleted(String, String, String)} instead.
	 *
	 * @param fullMatch         the full match
	 * @param filename          the filename
	 * @param expandedFilename  the expanded filename
	 * @param markerMode        the marker mode
	 */
	private MergeInclude(String fullMatch, String filename, String expandedFilename, MarkerMode markerMode) {
		this.fullMatch = fullMatch;
		this.filename = filename;
		this.expandedFilename = expandedFilename;
		this.markerMode = markerMode;
	}

	/**
	 * Creates a new MergeInclude instance with markers substituted.
	 *
	 * @param fullMatch         the full match
	 * @param filename          the filename
	 * @param expandedFilename  the expanded filename
	 * @return a new MergeInclude instance with markers substituted
	 */
	public static MergeInclude withMarkersSubstituted(String fullMatch, String filename, String expandedFilename) {
		return new MergeInclude(fullMatch, filename, expandedFilename, MarkerMode.SUBSTITUTE_MARKERS);
	}

	/**
	 * Creates a new MergeInclude instance with markers deleted.
	 *
	 * @param fullMatch         the full match
	 * @param filename          the filename
	 * @param expandedFilename  the expanded filename
	 * @return a new MergeInclude instance with markers deleted
	 */
	public static MergeInclude withMarkersDeleted(String fullMatch, String filename, String expandedFilename) {
		return new MergeInclude(fullMatch, filename, expandedFilename, MarkerMode.DELETE_MARKERS);
	}

	/**
	 * Gets the full match.
	 *
	 * @return the full match
	 */
	public String getFullMatch() {
		return fullMatch;
	}

	/**
	 * Gets the expanded filename.
	 *
	 * @return the expanded filename
	 */
	public String getExpandedFilename() {
		return expandedFilename;
	}

	/**
	 * Gets the filename.
	 *
	 * @return the filename
	 */
	public String getFilename() {
		return filename;
	}

	/**
	 * Checks if markers are substituted.
	 *
	 * @return true if markers are substituted, false otherwise
	 */
	public boolean areMarkersSubstituted() {
		return markerMode.equals(MarkerMode.SUBSTITUTE_MARKERS);
	}
}
