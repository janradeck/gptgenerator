package gptgenerator.uc.configure.sourcepartition;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/**
 * The IPrettyPrintSettings interface represents the settings for pretty printing in the GPT Generator.
 * It provides methods to set and retrieve the pretty print settings for input and merge operations.
 */
@JsonDeserialize(as = PrettyPrintSettingsModel.class)
public interface IPrettyPrintSettings {
	/**
	 * Sets the pretty print setting for input operations.
	 * 
	 * @param prettyPrint true to enable pretty printing, false otherwise
	 */
	void setPrettyPrintInput(boolean prettyPrint);

	/**
	 * Sets the pretty print setting for merge operations.
	 * 
	 * @param prettyPrint true to enable pretty printing, false otherwise
	 */
	void setPrettyPrintMerge(boolean prettyPrint);

	/**
	 * Returns the current pretty print setting for input operations.
	 * 
	 * @return true if pretty printing is enabled, false otherwise
	 */
	boolean isPrettyPrintInput();

	/**
	 * Returns the current pretty print setting for merge operations.
	 * 
	 * @return true if pretty printing is enabled, false otherwise
	 */
	boolean isPrettyPrintMerge();

	/**
	 * Returns the ignore files for the Prettier formatter.
	 * 
	 * @return the ignore files for the Prettier formatter
	 */
	String getPrettierIgnoreFiles();

	/**
	 * Sets the ignore files for the Prettier formatter.
	 * 
	 * @param ignoreFiles the ignore files for the Prettier formatter
	 */
	void setPrettierIgnoreFiles(String ignoreFiles);

	/**
	 * Clears the controller for the pretty print settings.
	 */
	void clearController();

	/**
	 * Sets the controller for the pretty print settings.
	 * 
	 * @param controller the controller for the pretty print settings
	 */
	void setController(IPrettyPrintSettingsController controller);
}
