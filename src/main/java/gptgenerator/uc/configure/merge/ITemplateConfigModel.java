package gptgenerator.uc.configure.merge;

/**
 * For every type of file that can be merged, there is a template configuration.<br>
 * This allows the user to specify the markers for the merge process. 
 */
public interface ITemplateConfigModel {

	/**
	 * @return The String with which the "include" marker starts
	 */
	String getMarkerStart();

	/**
	 * @return The String with which the "include" marker ends
	 */
	String getMarkerEnd();
	
	/**
	 * Set a new value for the start of the "include" marker.<br>
	 * Leading and trailing whitespace is removed via strip()
	 * @param newMarkerStart
	 */
	void setMarkerStart(String newMarkerStart);
	
	/**
	 * Set a new value for the end of the "include" marker.<br>
	 * Leading and trailing whitespace is removed via strip()
	 * @param newMarkerStart
	 */
	void setMarkerEnd(String newMarkerEnd);

	/**
	 * The pattern String for an "include" where in the content all markers are replaced by the content of the to-be-included file.
	 * @return
	 */
	public String getMergedMarker();
	/**
	 * The pattern String for an "include" where in the content all markers are replaced by "".
	 * @return
	 */
	public String getUnmergedMarker();	
	
	void setController (ITemplateConfigController controller);
	void clearController();
}
