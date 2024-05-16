package gptgenerator.uc.configure.sourcepartition;

/**
 * The IPrettyPrintSettingsController interface defines the contract for a controller that manages pretty print settings.
 * It provides methods to set and notify changes to the pretty print input, pretty print merge, and prettier ignore files.
 * It also allows views to be added and removed, and provides a method to request view updates.
 */
public interface IPrettyPrintSettingsController {

	/**
	 * Sets the pretty print input flag.
	 *
	 * @param prettyPrintInput true to enable pretty print input, false otherwise
	 */
	void setPrettyPrintInput(boolean prettyPrintInput);

	/**
	 * Sets the pretty print merge flag.
	 *
	 * @param prettyPrintMerge true to enable pretty print merge, false otherwise
	 */
	void setPrettyPrintMerge(boolean prettyPrintMerge);

	/**
	 * Sets the prettier ignore files.
	 *
	 * @param prettierIgnoreFiles the prettier ignore files
	 */
	void setPrettierIgnoreFiles(String prettierIgnoreFiles);

	/**
	 * Notifies the change in the pretty print input flag.
	 *
	 * @param prettyPrintInput true if pretty print input is enabled, false otherwise
	 */
	void notifySetPrettyPrintInput(boolean prettyPrintInput);

	/**
	 * Notifies the change in the pretty print merge flag.
	 *
	 * @param prettyPrintMerge true if pretty print merge is enabled, false otherwise
	 */
	void notifySetPrettyPrintMerge(boolean prettyPrintMerge);

	/**
	 * Notifies the change in the prettier ignore files.
	 *
	 * @param prettierIgnoreFiles the updated prettier ignore files
	 */
	void notifySetPrettierIgnoreFiles(String prettierIgnoreFiles);

	/**
	 * Adds a view to the controller.
	 *
	 * @param view the view to be added
	 */
	void addView(IPrettyPrintSettingsView view);

	/**
	 * Removes a view from the controller.
	 *
	 * @param view the view to be removed
	 */
	void removeView(IPrettyPrintSettingsView view);

	/**
	 * Requests an update from the views.
	 */
	void requestViewUpdate();
}
