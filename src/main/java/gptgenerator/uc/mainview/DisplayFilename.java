package gptgenerator.uc.mainview;

public class DisplayFilename {
	private String displayFilename;
	private String fullFilename;
	
	public DisplayFilename (String displayFilename, String fullFilename) {
		this.displayFilename = displayFilename;
		this.fullFilename = fullFilename;
	}

	public String getDisplayFilename() {
		return displayFilename;
	}

	public String getFullFilename() {
		return fullFilename;
	}
	
}
