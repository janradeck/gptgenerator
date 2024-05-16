package gptgenerator.uc.configure.sourcepartition;

public interface IPrettyPrintSettingsView {

	void setPrettyPrintInput(boolean prettyPrint);
	void setPrettyPrintMerge(boolean prettyPrint);
	void setPrettierIgnoreFiles(String ignoreFiles);
}
