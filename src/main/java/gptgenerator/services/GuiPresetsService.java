package gptgenerator.services;

import java.awt.Insets;

/**
 * 
 */
public class GuiPresetsService {
    private static final Insets _buttonPanelInsets = new Insets(5,5,5,5);
    private static final Insets _statusPanelInsets = new Insets(5,5,5,5);
    private static final Insets _editInsets = new Insets(5,5,5,5);
    private static final Insets _labelInsets = new Insets(5,5,5,5);
    private static final Insets _tableInsets = new Insets(5,5,5,5);
    private static final Insets _scrollPaneLeftInsets = new Insets(5,5,5,5);
    private static final Insets _scrollPaneRightInsets = new Insets(5,5,5,5);
    private static final Insets _titleLeftInsets = new Insets(5,5,5,5);
    private static final Insets _titleRightInsets = new Insets(5,5,5,5);
    private static final Insets _panelInsets = new Insets(5,5,5,5);
    
    
    public static Insets buttonPanelInsets() {
    	return _buttonPanelInsets;
    }
    
    public static Insets editInsets() {
    	return _editInsets;
    }
    
    public static Insets labelInsets() {
    	return _labelInsets;
    }


	public static Insets scrollPaneLeftInsets() {
		return _scrollPaneLeftInsets;
	}

	public static Insets scrollPaneRightInsets() {
		return _scrollPaneRightInsets;
	}

	public static Insets titleLeftInsets() {
		return _titleLeftInsets;
	}

	public static Insets titleRightInsets() {
		return _titleRightInsets;
	}

	public static Insets panelInsets() {
		return _panelInsets;
	}

	public static Insets tableInsets() {
		return _tableInsets;
	}

	public static Insets statusPanelInsets() {
		return _statusPanelInsets;
	}
	
}
