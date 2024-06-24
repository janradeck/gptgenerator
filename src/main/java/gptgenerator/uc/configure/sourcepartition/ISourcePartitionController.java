package gptgenerator.uc.configure.sourcepartition;

import gptgenerator.uc.configure.merge.ITemplateConfigController;
import gptgenerator.uc.processing.o2prompt.IChatConfigController;

public interface ISourcePartitionController {
    IChatConfigController getGptConfigController();
    ITemplateConfigController getTemplateConfigController();
    
    void setSourceDirRel(String sourceDirRel);
    void setDestDirAbs(String destDirAbs);
    
    void notifySetSourceDirRel(String newValue);
    void notifySetDestDirAbs(String newValue);
    
    void addView(ISourcePartitionView view);
    void removeView(ISourcePartitionView view);
    void requestViewUpdate();
	IPrettyPrintSettingsController getPrettyPrintSettingsController();
    
}
