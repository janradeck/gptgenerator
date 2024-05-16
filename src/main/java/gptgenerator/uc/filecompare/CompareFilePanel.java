package gptgenerator.uc.filecompare;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoundedRangeModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import gptgenerator.services.FileService;
import gptgenerator.services.GbcFactory;
import gptgenerator.services.GuiElementFactory;
import gptgenerator.services.GuiPresetsService;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
/**
 * The CompareFilePanel class represents a panel that displays and compares two files.
 * It extends the JPanel class and provides functionality for toggling the size of the comparison panel,
 * copying the file content to the clipboard, and updating the comparison panel.
 */
public class CompareFilePanel extends JPanel {

	private static final long serialVersionUID = 8254496576934477942L;
	private static final int DISPLAY_HEIGHT = 300;
	private static final int DISPLAY_WIDTH = 600;

	private static final int WINDOW_HEIGHT = DISPLAY_HEIGHT;
	private static final int WINDOW_WIDTH = 2 * DISPLAY_WIDTH + 50;
       
    private JTextArea leftArea;	
	private JTextArea rightArea;	
    private BoundedRangeModel scrollModelVertLeft;
    private BoundedRangeModel scrollModelVertRight;

	private ComparedFiles comparedFiles;
	private String title;
	private String leftHeading;
	private String rightHeading;
	private int maxTextSize;

	private JButton copyLeftButton;
	private JButton copyRightButton;
	
	private boolean comparisonMaximized = true;
	private JButton comparisonToggleButton;
	private JPanel comparisonPanel;
	
	private Dimension minPanelDim = new Dimension(WINDOW_WIDTH, 0);
	private Dimension maxPanelDim = new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT);
	
	public CompareFilePanel(ComparedFiles comparedFiles, String title, String leftHeading, String rightHeading) {
		this.comparedFiles = comparedFiles;
		this.title = title;
		this.leftHeading = leftHeading;
		this.rightHeading = rightHeading;
		
    	initComponents();
    	localInitialization();
	}
	
	private void initComponents() {
		comparisonToggleButton = new JButton();
    	comparisonToggleButton.setFont(GuiElementFactory.makeBigBoldFont(comparisonToggleButton.getFont()));
		
        comparisonToggleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	toggleComparisonPanelSize();
            }
        });		
		
        
		String contentLeft = comparedFiles.getContentCur();
		String contentRight = comparedFiles.getContentPrev();

		if (FileService.lineCnt(contentLeft) > FileService.lineCnt(contentRight)) {
			int lineDifference = FileService.lineCnt(contentLeft) - FileService.lineCnt(contentRight);
			contentRight = contentRight + emptyLines(lineDifference);
		}
		
        leftArea = GuiElementFactory.createJTextArea();
        leftArea.setText(contentLeft);

        rightArea = GuiElementFactory.createJTextArea();
        rightArea.setText(contentRight);
        
		
    	JScrollPane scrollPaneLeft = new JScrollPane(leftArea);
        //scrollPaneLeft.setMinimumSize(new Dimension(DISPLAY_WIDTH,DISPLAY_HEIGHT));
        scrollPaneLeft.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        scrollPaneLeft.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        
        JScrollPane scrollPaneRight = new JScrollPane(rightArea);
        //scrollPaneRight.setMinimumSize(new Dimension(DISPLAY_WIDTH,DISPLAY_HEIGHT));

        scrollModelVertLeft = scrollPaneLeft.getVerticalScrollBar().getModel();
        scrollModelVertRight = scrollPaneRight.getVerticalScrollBar().getModel();

        // Add a ChangeListener to the first scrollbar
        scrollModelVertRight.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                scrollModelVertLeft.setValue(scrollModelVertRight.getValue());
            }
        });
        
        BoundedRangeModel scrollModelHorLeft = scrollPaneLeft.getHorizontalScrollBar().getModel();
        BoundedRangeModel scrollModelHorRight = scrollPaneRight.getHorizontalScrollBar().getModel();

        // Add a ChangeListener to the first scrollbar
        scrollModelHorRight.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                scrollModelHorLeft.setValue(scrollModelHorRight.getValue());
            }
        });    	
        maxTextSize = Math.max(FileService.lineCnt(contentLeft), FileService.lineCnt(contentRight));
        scrollModelVertLeft.setMaximum(maxTextSize);
        scrollModelVertRight.setMaximum(maxTextSize);
          
        GbcFactory gbccComparison = new GbcFactory();
        
        GridBagLayout gbl = new GridBagLayout();
        GridBagConstraints gbc = new GridBagConstraints();
        
        comparisonPanel = new JPanel();
        gbl.setConstraints(comparisonPanel, gbc);
        comparisonPanel.setLayout(gbl);

        gbc = gbccComparison.getHorizontal(GuiPresetsService.titleLeftInsets());  
        comparisonPanel.add(GuiElementFactory.subHeadingLabel(leftHeading), gbc);

        gbc = gbccComparison.getHorizontal(GuiPresetsService.titleRightInsets());        
        comparisonPanel.add(GuiElementFactory.subHeadingLabel(rightHeading), gbc);
        
        gbccComparison.nextRow();

        gbc = gbccComparison.getBoth(GuiPresetsService.scrollPaneRightInsets());
        comparisonPanel.add(scrollPaneLeft, gbc);

        gbc = gbccComparison.getBoth(GuiPresetsService.scrollPaneLeftInsets());
        comparisonPanel.add(scrollPaneRight, gbc);
        gbccComparison.nextRow();
        
        // Copy buttons
		copyLeftButton = new JButton("Copy to clipboard");
    	copyLeftButton.setFont(GuiElementFactory.makeBigBoldFont(copyLeftButton.getFont()));
		
        copyLeftButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	copyToClipboard(contentLeft);
            }
        });		

        
        gbc = gbccComparison.getNone(GuiPresetsService.buttonPanelInsets());
        gbc.anchor = GridBagConstraints.WEST;
        
        comparisonPanel.add(copyLeftButton, gbc);

		copyRightButton = new JButton("Copy to clipboard");
    	copyRightButton.setFont(GuiElementFactory.makeBigBoldFont(copyRightButton.getFont()));
		
        copyRightButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	copyToClipboard(contentLeft);
            }
        });		

        
        gbc = gbccComparison.getNone(GuiPresetsService.buttonPanelInsets());
        gbc.anchor = GridBagConstraints.WEST;
        
        comparisonPanel.add(copyRightButton, gbc);
        gbccComparison.nextRow();
        
        
        
        gbl = new GridBagLayout();
        gbc = new GridBagConstraints();
        
        gbl.setConstraints(this, gbc);
        this.setLayout(gbl);

        GbcFactory gbccPanel = new GbcFactory();
        
        gbc = gbccPanel.getNone(GuiPresetsService.buttonPanelInsets());
        gbc.anchor = GridBagConstraints.WEST;
        
        this.add(comparisonToggleButton, gbc);
        
        gbc = gbccPanel.getNone(GuiPresetsService.buttonPanelInsets());
        gbc.anchor = GridBagConstraints.WEST;
        JLabel titleLabel = GuiElementFactory.subHeadingLabel(title);
        this.add(titleLabel, gbc);
        
        gbccPanel.nextRow();
        
        gbc = gbccPanel.getBoth(GuiPresetsService.buttonPanelInsets());
        gbc.gridwidth = 2;
        this.add(comparisonPanel, gbc);
        
        maximizeComparisonPanel();
	}
	
	protected void copyToClipboard(String content) {
        // Get the system clipboard
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();

        // Create a StringSelection object to hold the text
        StringSelection stringSelection = new StringSelection(content);

        // Set the clipboard content with the StringSelection
        clipboard.setContents(stringSelection, null);
	}

	private void localInitialization() {
        scrollModelVertRight.setValue(0);
        scrollModelVertLeft.setValue(0);
	}
	
	private String emptyLines(int lineDifference) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < lineDifference; i ++) {
			sb.append("\n");
		}
		return sb.toString();
	}

	public void maximizeComparisonPanel() {
        comparisonMaximized = true;
        updateToggleButton();
        updateComparisonPanel();
	}
	
	public void minimizeComparisonPanel() {
        comparisonMaximized = false;
        updateToggleButton();
        updateComparisonPanel();		
	}
	
	public void toggleComparisonPanelSize() {
        comparisonMaximized = !comparisonMaximized;
        updateToggleButton();
        updateComparisonPanel();		
	}
	
	private void updateToggleButton() {
        if (comparisonMaximized) {
            comparisonToggleButton.setText("-");
        } else {
            comparisonToggleButton.setText("+");		
        }
	}
	
	private void updateComparisonPanel() {
		if (comparisonMaximized) {
			comparisonPanel.setMaximumSize(maxPanelDim);			
			comparisonPanel.setMinimumSize(maxPanelDim);
			comparisonPanel.setSize(maxPanelDim);
		} else {
			comparisonPanel.setMaximumSize(minPanelDim);			
			comparisonPanel.setMinimumSize(minPanelDim);			
			comparisonPanel.setSize(minPanelDim);
		}
        scrollModelVertLeft.setMaximum(maxTextSize);
        scrollModelVertRight.setMaximum(maxTextSize);		
	}

}
