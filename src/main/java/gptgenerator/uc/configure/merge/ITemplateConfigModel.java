package gptgenerator.uc.configure.merge;

public interface ITemplateConfigModel {

	String getMarkerStart();
	String getMarkerEnd();
	
	/**
	 * Leading and trailing whitespace is removed via strip()
	 * @param newMarkerStart
	 */
	void setMarkerStart(String newMarkerStart);
	/**
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
