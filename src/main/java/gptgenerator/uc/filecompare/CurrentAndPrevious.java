package gptgenerator.uc.filecompare;

/**
 * The "current" directory and the "previous" directory
 */
public class CurrentAndPrevious {
	private String currentBaseDir;
	private String previousBaseDir;
	
	public CurrentAndPrevious(String currentBaseDir, String previousBaseDir) {
		this.currentBaseDir = currentBaseDir;
		this.previousBaseDir = previousBaseDir;
	}

	public String getCurrentBaseDir() {
		return currentBaseDir;
	}

	public String getPreviousBaseDir() {
		return previousBaseDir;
	}

}
