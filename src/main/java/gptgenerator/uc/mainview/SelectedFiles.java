package gptgenerator.uc.mainview;

import java.util.HashSet;
import java.util.Set;

public class SelectedFiles {
	private Set<String> filenames;
	
	public SelectedFiles() {
		filenames = new HashSet<>();
	}
	
	public void add(String filename) {
		filenames.add(filename);
	}
	
	public boolean contains(String filename) {
		return filenames.contains(filename);
	}
}
