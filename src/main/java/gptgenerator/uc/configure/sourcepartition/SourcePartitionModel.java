package gptgenerator.uc.configure.sourcepartition;

import gptgenerator.uc.configure.gpt.ChatConfigModel;
import gptgenerator.uc.configure.gpt.IChatConfigModel;
import gptgenerator.uc.configure.merge.ITemplateConfigModel;
import gptgenerator.uc.configure.merge.TemplateConfigController;
import gptgenerator.uc.processing.o1merge.TemplateConfig;
import gptgenerator.uc.processing.o2prompt.ChatConfigController;

/**
 * Ein Unterverzeichnis der generierten Prompts.
 * <ul>
 * <li>Basisverzeichnis für die Installation der Antwort
 * <li>ChatConfigModel für dieses Unterverzeichnis
 * </ul>
 * 
 */
public class SourcePartitionModel implements ISourcePartitionModel {
    private String sourceDirRel;
	private String destDirAbs;
	
	private IPrettyPrintSettings prettyPrintSettings;
	private IChatConfigModel gptConfig;
	private ITemplateConfigModel templateConfig;
	private ISourcePartitionController controller;
		
	public SourcePartitionModel() {
		destDirAbs = "";
		sourceDirRel = "";
		prettyPrintSettings = new PrettyPrintSettingsModel();
		gptConfig = new ChatConfigModel();
		templateConfig = new TemplateConfig();
		controller = new NilSourcePartitionController();
	}
	
	public SourcePartitionModel(String source, String dest, IPrettyPrintSettings ppSettings, IChatConfigModel gptConfig, ITemplateConfigModel templateConfig) {
		this.sourceDirRel = source;
		this.destDirAbs = dest;
		this.prettyPrintSettings = ppSettings;
		this.gptConfig = gptConfig;
		this.templateConfig = templateConfig;
		controller = new NilSourcePartitionController();
	}
	
    public SourcePartitionModel(ISourcePartitionModel iSourcePartitionModel) {
		this.sourceDirRel = iSourcePartitionModel.getSourceDirRel();
		this.destDirAbs = iSourcePartitionModel.getDestDirAbs();
		this.prettyPrintSettings = new PrettyPrintSettingsModel(iSourcePartitionModel.getPrettyPrintSettings());		
		this.gptConfig = new ChatConfigModel(iSourcePartitionModel.getGptConfig());
		this.templateConfig = new TemplateConfig(iSourcePartitionModel.getTemplateConfig());
		this.controller = new NilSourcePartitionController();
	}

	public String getSourceDirRel() {
		return sourceDirRel;
	}

	@Override
	public String mapToDestination(String sourceRelFilename) {
		return destDirAbs + sourceRelFilename.substring(sourceDirRel.length());
	}

	@Override
	public boolean isHomeOf(String sourceRelFilename) {
		if (sourceDirRel.isEmpty()) { return true; }
		return sourceRelFilename.startsWith(sourceDirRel);
	}
	
	@Override
	public void setSourceDirRel(String sourceDir) {
		if (! sourceDir.equals(this.sourceDirRel)) {
			this.sourceDirRel = sourceDir;
			controller.notifySetSourceDirRel(sourceDir);
		}
	}

	@Override
	public void setDestDirAbs(String destDir) {
		if (!destDir.equals(this.destDirAbs)) {
			this.destDirAbs = destDir;
			controller.notifySetDestDirAbs(destDir);
		}
	}

	public String getDestDirAbs() {
		return destDirAbs;
	}

	public IChatConfigModel getGptConfig() {
		return gptConfig;
	}

	
	@Override
	public void setGptConfig(ChatConfigModel gptConfig) {
		this.gptConfig = gptConfig;
	}

	@Override
	public ITemplateConfigModel getTemplateConfig() {
		return templateConfig;
	}

	@Override
	public IPrettyPrintSettings getPrettyPrintSettings() {
		return prettyPrintSettings;
	}

	@Override
	public void setPrettyPrintSettings(IPrettyPrintSettings ppSettings) {
		this.prettyPrintSettings = ppSettings;		
	}

	
	@Override
	public void setTemplateConfig(TemplateConfig templateConfig) {
		this.templateConfig = templateConfig;
	}

	@Override
	public void setController(ISourcePartitionController controller) {
		this.controller = controller;		
	}

	@Override
	public void clearController() {
		this.controller = new NilSourcePartitionController();
	}
	
	private class NilSourcePartitionController implements ISourcePartitionController {

		@Override
		public ChatConfigController getGptConfigController() {
			return null;
		}

		@Override
		public TemplateConfigController getTemplateConfigController() {
			return null;
		}

		@Override
		public void setSourceDirRel(String sourceDirRel) {
		}

		@Override
		public void setDestDirAbs(String destDirAbs) {
		}

		@Override
		public void notifySetSourceDirRel(String newValue) {
		}

		@Override
		public void notifySetDestDirAbs(String newValue) {
		}

		@Override
		public void addView(ISourcePartitionView view) {
		}

		@Override
		public void removeView(ISourcePartitionView view) {
		}

		@Override
		public void requestViewUpdate() {
		}

		@Override
		public IPrettyPrintSettingsController getPrettyPrintSettingsController() {
			return null;
		}
		
	}

}

 