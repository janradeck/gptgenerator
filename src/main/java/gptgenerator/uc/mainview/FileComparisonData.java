package gptgenerator.uc.mainview;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * The relative relativeFilename and the full filenames of the four files displayed in the FileComparison dialogue
 */
public class FileComparisonData {
	private String relativeFilename;

	private String inputCurrent = "";
	private String inputPrevious = "";
	private String mergeCurrent = "";
	private String mergePrevious = "";
	
	private String replyCurrent = "";
	private String replyPrevious = "";
	private List<DisplayFilename> includes = new ArrayList<>();
	
	public String getRelativeFilename() {
		return relativeFilename;
	}
	
	public void setRelativeFilename(String filename) {
		this.relativeFilename = filename;
	}
	
	public void setInputCurrent(String dir, String filename) {
		this.inputCurrent =  concatenate(dir, filename);
	}

	public void setInputPrevious(String dir, String filename) {
		this.inputPrevious = concatenate(dir, filename);
	}

	public void setMergeCurrent(String dir, String filename) {
		this.mergeCurrent = concatenate(dir, filename);
	}

	public void setMergePrevious(String dir, String filename) {
		this.mergePrevious = concatenate(dir, filename);
	}

	public void setReplyCurrent(String dir, String filename) {
		this.replyCurrent = concatenate(dir, filename);
	}
	public void setReplyPrevious(String dir, String filename) {
		this.replyPrevious = concatenate(dir, filename);
	}
	
	public void addInclude (DisplayFilename include) {
		includes.add(include);
	}
	
	public String getInputCurrent() {
		return inputCurrent;
	}

	public String getInputPrevious() {
		return inputPrevious;
	}

	public String getMergeCurrent() {
		return mergeCurrent;
	}

	public String getMergePrevious() {
		return mergePrevious;
	}
	
	public String getReplyCurrent() {
		return replyCurrent;
	}
	public String getReplyPrevious() {
		return replyPrevious;
	}

	private static String concatenate(String pathString, String filename) {
		return Paths.get(pathString, filename).toString();
	}

	public boolean hasReply() {
		return ! replyCurrent.isEmpty();
	}

	public boolean hasIncludes() {
		return ! includes.isEmpty();
	}
	
	public List<DisplayFilename> getIncludes() {
		return includes;
	}
}
