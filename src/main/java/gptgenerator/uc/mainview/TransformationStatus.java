package gptgenerator.uc.mainview;

/**
 * A file and its change status 
 */
public class TransformationStatus {
	
	/**
	 * What is the status of the file?
	 */
	private String baseFilename;
	private FileChangeStatus status;

	public TransformationStatus(String filename, FileChangeStatus status) {
		this.baseFilename = filename;
		this.status = status;
	}

	public String getBaseFilename() {
		return baseFilename;
	}

	public FileChangeStatus getStatus() {
		return status;
	}
	
	@Override
	public String toString() {
		return baseFilename + " " + status.toString();
	}
	
}
