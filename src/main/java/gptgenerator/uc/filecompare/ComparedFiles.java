package gptgenerator.uc.filecompare;

import gptgenerator.services.FileService;

/**
 * The filename of the current and previous version of a file 
 */
public class ComparedFiles {
	private String filenameCur;
	private String filenamePrev;
	
	public ComparedFiles(String filenameCur, String filenamePrev) {
		this.filenameCur = filenameCur;
		this.filenamePrev = filenamePrev;
	}

	public String getFilenameCur() {
		return filenameCur;
	}

	public String getFilenamePrev() {
		return filenamePrev;
	}
	
	public String getContentCur() {
		return FileService.readFromFile(filenameCur);
	}
	
	public String getContentPrev() {
		return FileService.readFromFile(filenamePrev);
	}
	
}
