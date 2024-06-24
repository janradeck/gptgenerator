package gptgenerator.uc.configure.sourcepartition;

import java.util.ArrayList;
import java.util.List;

import gptgenerator.uc.configure.merge.ITemplateConfigController;
import gptgenerator.uc.configure.merge.TemplateConfigController;
import gptgenerator.uc.processing.o2prompt.ChatConfigController;
import gptgenerator.uc.processing.o2prompt.IChatConfigController;

/**
 * 
 */
public class SourcePartitionController implements ISourcePartitionController {
	private ISourcePartitionModel model;
	private IChatConfigController gptController;
	private ITemplateConfigController templateConfigController;
	private IPrettyPrintSettingsController ppSettingsController;
	
	private List<ISourcePartitionView> registeredViews = new ArrayList<>();
	
	public SourcePartitionController (ISourcePartitionModel modelCopy) {
		this.model = modelCopy;
		this.gptController = new ChatConfigController(modelCopy.getGptConfig());
		this.templateConfigController = new TemplateConfigController(modelCopy.getTemplateConfig());
		this.ppSettingsController = new PrettyPrintSettingsController(modelCopy.getPrettyPrintSettings());
	}

	@Override
	public void setSourceDirRel(String sourceDirRel) {
		model.setSourceDirRel(sourceDirRel);		
	}

	@Override
	public void setDestDirAbs(String destDirAbs) {
		model.setDestDirAbs(destDirAbs);
	}
	 
	@Override
	public IChatConfigController getGptConfigController() {
		return gptController;
	}

	@Override
	public ITemplateConfigController getTemplateConfigController() {
		return templateConfigController;
	}
	
	@Override
	public IPrettyPrintSettingsController getPrettyPrintSettingsController() {
		return ppSettingsController;
	}
    	
	@Override
    public void notifySetSourceDirRel(String newValue) {
		for (ISourcePartitionView view: registeredViews) {
			view.setSrcDirRelative(newValue);
		}
    }

	@Override
    public void notifySetDestDirAbs(String newValue) {
		for (ISourcePartitionView view: registeredViews) {
			view.setDstDirAbsolute(newValue);
		}
    }


	@Override
	public void addView(ISourcePartitionView view) {
		registeredViews.add(view);		
	}

	@Override
	public void removeView(ISourcePartitionView view) {
		registeredViews.remove(view);		
	}

	@Override
	public void requestViewUpdate() {
		notifySetSourceDirRel(model.getSourceDirRel());
		notifySetDestDirAbs(model.getDestDirAbs());
	}

}
