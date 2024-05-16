package gptgenerator.uc.configure.sourcepartition;

public class PrettyPrintSettings implements IPrettyPrintSettings {
	private boolean prettyPrintInput;
	private boolean prettyPrintMerge;
	private String  prettyPrintIgnoreFiles;
	
	private IPrettyPrintSettingsController controller = new NilPrettyPrintSettingsController();
	
	public PrettyPrintSettings() {
		prettyPrintInput=false;
		prettyPrintMerge=false;
		prettyPrintIgnoreFiles="";		
	}
	
	public PrettyPrintSettings(IPrettyPrintSettings prettyPrintSettings) {
		this.prettyPrintInput = prettyPrintSettings.isPrettyPrintInput();
		this.prettyPrintMerge = prettyPrintSettings.isPrettyPrintMerge();
		this.prettyPrintIgnoreFiles = prettyPrintSettings.getPrettierIgnoreFiles();
	}

	@Override
	public void setPrettyPrintInput(boolean prettyPrintInput) {
		if (this.prettyPrintInput != prettyPrintInput) {
			this.prettyPrintInput = prettyPrintInput;
			controller.notifySetPrettyPrintInput(prettyPrintInput);
		}
	}

	@Override
	public void setPrettyPrintMerge(boolean prettyPrintMerge) {
		if(this.prettyPrintMerge != prettyPrintMerge) {
			this.prettyPrintMerge = prettyPrintMerge;
			controller.notifySetPrettyPrintMerge(prettyPrintMerge);
		}
	}

	@Override
	public boolean isPrettyPrintInput() {
		return prettyPrintInput;
	}
	
	@Override
	public boolean isPrettyPrintMerge() {
		return prettyPrintMerge;
	}
	
	@Override
	public String getPrettierIgnoreFiles() {
		return prettyPrintIgnoreFiles;
	}
	
	@Override
	public void setPrettierIgnoreFiles(String prettyPrintIgnoreFiles) {
		if (!this.prettyPrintIgnoreFiles.equals(prettyPrintIgnoreFiles)) {
			this.prettyPrintIgnoreFiles = prettyPrintIgnoreFiles;
			controller.notifySetPrettierIgnoreFiles(prettyPrintIgnoreFiles);
		}
	}
	
	@Override
	public void setController(IPrettyPrintSettingsController controller) {
		this.controller = controller;
	}

	@Override
	public void clearController() {
		this.controller = new NilPrettyPrintSettingsController();
	}
	
	
	private static class NilPrettyPrintSettingsController implements IPrettyPrintSettingsController {

		@Override
		public void addView(IPrettyPrintSettingsView view) {
		}

		@Override
		public void removeView(IPrettyPrintSettingsView view) {
		}

		@Override
		public void requestViewUpdate() {
		}

		@Override
		public void setPrettyPrintInput(boolean prettyPrintInput) {
		}

		@Override
		public void setPrettyPrintMerge(boolean prettyPrintInput) {
		}

		@Override
		public void setPrettierIgnoreFiles(String prettierIgnoreFiles) {
		}

		@Override
		public void notifySetPrettyPrintInput(boolean prettyPrintInput) {
		}

		@Override
		public void notifySetPrettyPrintMerge(boolean prettyPrintInput) {
		}

		@Override
		public void notifySetPrettierIgnoreFiles(String prettierIgnoreFiles) {
		}
		
	}
	
}
