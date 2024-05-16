package gptgenerator.services;

import java.awt.Component;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class GuiElementFactory {
	private static final int HEADING_FONT_SIZE = 16;
	private static final int SUB_HEADING_FONT_SIZE = 14;

    public static JLabel headingLabel(String text) {
    	JLabel result = new JLabel(text);
    	Font labelFont = result.getFont();
    	result.setFont(new Font(labelFont.getName(), Font.PLAIN, HEADING_FONT_SIZE));
    	return result;
    }
	
	/**
     * Create a label with FontSize SUB_HEADING_FONT_SIZE
     * @param text
     * @return
     */
    public static JLabel subHeadingLabel(String text) {
    	JLabel result = new JLabel(text);
    	Font labelFont = result.getFont();
    	result.setFont(new Font(labelFont.getName(), Font.PLAIN, SUB_HEADING_FONT_SIZE));
    	return result;
    }
    
    /**
     * Create a TextArea with the content of a file
     * @param filename
     * @return
     */
    public static JTextArea createJTextArea() {
    	JTextArea area = new JTextArea();
        area.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(area);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        
    	return area;
    }

	public static Component subHeadingPanel(String string) {
		JPanel result = new JPanel();
		result.add(subHeadingLabel(string));
		return result;
	}

	public static Component labelPanel(String string) {
		JPanel result = new JPanel();
		result.add(new JLabel(string));
		return result;
	}
	
	public static Font makeBigBoldFont(Font originalFont) {
		return new Font(originalFont.getName(), Font.BOLD, SUB_HEADING_FONT_SIZE);		
	}
	
}
