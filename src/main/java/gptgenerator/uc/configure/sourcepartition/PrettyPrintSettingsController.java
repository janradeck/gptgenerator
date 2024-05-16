package gptgenerator.uc.configure.sourcepartition;

import java.util.ArrayList;
import java.util.List;

public class PrettyPrintSettingsController implements IPrettyPrintSettingsController {
	private List<IPrettyPrintSettingsView> views = new ArrayList<>();
	private IPrettyPrintSettings ppSettings;

	public PrettyPrintSettingsController(IPrettyPrintSettings ppSettings) {
		this.ppSettings = ppSettings;
		this.ppSettings.setController(this);
	}
	@Override
	public void setPrettyPrintInput(boolean prettyPrintInput) {
		ppSettings.setPrettyPrintInput(prettyPrintInput);
	}

	@Override
	public void setPrettyPrintMerge(boolean prettyPrintInput) {
		ppSettings.setPrettyPrintMerge(prettyPrintInput);
	}

	@Override
	public void setPrettierIgnoreFiles(String prettierIgnoreFiles) {
		ppSettings.setPrettierIgnoreFiles(prettierIgnoreFiles);
	}

	@Override
	public void notifySetPrettyPrintInput(boolean prettyPrintInput) {
		for (IPrettyPrintSettingsView view: views) {
			view.setPrettyPrintInput(prettyPrintInput);
		}
	}

	@Override
	public void notifySetPrettyPrintMerge(boolean prettyPrintInput) {
		for (IPrettyPrintSettingsView view: views) {
			view.setPrettyPrintMerge(prettyPrintInput);
		}
	}

	@Override
	public void notifySetPrettierIgnoreFiles(String prettierIgnoreFiles) {
		for (IPrettyPrintSettingsView view: views) {
			view.setPrettierIgnoreFiles(prettierIgnoreFiles);
		}
	}

	@Override
	public void addView(IPrettyPrintSettingsView view) {
		views.add(view);
	}

	@Override
	public void removeView(IPrettyPrintSettingsView view) {
		views.remove(view);
	}

	@Override
	public void requestViewUpdate() {
		notifySetPrettyPrintInput(ppSettings.isPrettyPrintInput());
		notifySetPrettyPrintMerge(ppSettings.isPrettyPrintMerge());
		notifySetPrettierIgnoreFiles(ppSettings.getPrettierIgnoreFiles());
	}

}
