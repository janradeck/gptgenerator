package gptgenerator.uc.filecompare;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import gptgenerator.services.GbcFactory;
import gptgenerator.services.GuiPresetsService;
import gptgenerator.uc.mainview.DisplayFilename;
import gptgenerator.uc.mainview.FileComparisonData;

/**
 * The CompareFileView class represents a JFrame that displays a comparison view of files.
 * It allows users to compare different versions of files and view additional information.
 */
public class CompareFileView extends JFrame {
	private static final long serialVersionUID = 8104993440940834981L;

	private static final int DISPLAY_HEIGHT = 300;
	private static final int DISPLAY_WIDTH = 600;

	private static final int WINDOW_HEIGHT = 2 * DISPLAY_HEIGHT + 150;
	private static final int WINDOW_WIDTH = 2 * DISPLAY_WIDTH + 50;
	
	private List<DisplayFilename> includes;
	private DefaultTableModel tableModel;
	
    public CompareFileView(final JFrame parentView, FileComparisonData files) {
   		setTitle("Comparing " + files.getRelativeFilename());
        
        JPanel comparisonPanel = new JPanel();
        
        GridBagLayout gbl = new GridBagLayout();
        GridBagConstraints gbc = new GridBagConstraints();
        gbl.setConstraints(comparisonPanel, gbc);
        comparisonPanel.setLayout(gbl);
        
        GbcFactory gbcc = new GbcFactory();

        ComparedFiles inputFiles = new ComparedFiles(files.getInputCurrent(), files.getInputPrevious());
        JPanel inputPanel = new CompareFilePanel(inputFiles, files.getRelativeFilename(), "Input (current)", "Input (previous)");
        gbc = gbcc.getBoth(GuiPresetsService.panelInsets());
        comparisonPanel.add(inputPanel, gbc);        
        gbcc.nextRow();

        
        ComparedFiles merge = new ComparedFiles(files.getMergeCurrent(), files.getMergePrevious());
        JPanel mergePanel = new CompareFilePanel(merge, files.getRelativeFilename(), "Merge (current)", "Merge (previous)");
        gbc = gbcc.getBoth(GuiPresetsService.panelInsets());
        comparisonPanel.add(mergePanel, gbc);        
        gbcc.nextRow();
        
        if (files.hasReply()) {
	        ComparedFiles reply = new ComparedFiles(files.getReplyCurrent(), files.getReplyPrevious());        
	        JPanel replyPanel = new CompareFilePanel(reply, files.getRelativeFilename(), "Reply (current)", "Reply (previous)");
	        gbc = gbcc.getBoth(GuiPresetsService.panelInsets());
	        comparisonPanel.add(replyPanel, gbc);
	        gbcc.nextRow();
        }
        
        if (files.hasIncludes()) {
        	includes = files.getIncludes();
            
            JTable table;
            String[] columnNames = {"Source", "Destination"};
            tableModel = new DefaultTableModel(columnNames, 0) {
    			private static final long serialVersionUID = -1894825464127554238L;

    			@Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };
            
            table = new JTable(tableModel);
            // Set selection mode to single selection
            table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            table.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (e.getClickCount() == 2) { // Check for double-click
                        int rowIndex = table.rowAtPoint(e.getPoint());

                        // Check if the clicked point is on a valid row
                        if (rowIndex != -1) {
                        	showIncludeFileView(table.getSelectedRow());
                        }
                    }
                }
            });
        	populateTable();

	        gbc = gbcc.getBoth(GuiPresetsService.panelInsets());
	        comparisonPanel.add(table, gbc);
	        gbcc.nextRow();
        }
        
        getContentPane().add(comparisonPanel);
        getContentPane().addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                // Not used for keyTyped
            }

            @Override
            public void keyPressed(KeyEvent e) {
                // Check if the pressed key is ESC (key code 27)
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    // Close the window
                    dispose();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                // Not used for keyReleased
            }
        });        
        // Set the content pane to be focusable to receive key events
        getContentPane().setFocusable(true);        

        setMinimumSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
        
        setLocationRelativeTo(parentView);
        setVisible(true);
    }

	private void populateTable() {
        for (DisplayFilename cur : includes) {
            tableModel.addRow(new Object[]{cur.getDisplayFilename()});
        }
	}

	protected void showIncludeFileView(int selectedRow) {
		// TODO Auto-generated method stub
	}
    
}
