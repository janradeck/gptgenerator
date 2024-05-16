package gptgenerator.uc.mainview;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import gptgenerator.processingresult.IResultController;
import gptgenerator.processingresult.IResultModelView;
import gptgenerator.services.GbcFactory;
import gptgenerator.services.GuiElementFactory;
import gptgenerator.services.GuiPresetsService;
import gptgenerator.uc.configure.ConfigurationController;
import gptgenerator.uc.processing.ProcessManager;
import gptgenerator.uc.processing.o1merge.MergeManager;

public class MainView extends JFrame implements IFileStateView {
	private static final String EMPTY_LABEL_TEXT = "   ";
	private static final String SPACING = "          ";
	
	private static final long serialVersionUID = 6646662526692639816L;

	private static final int STATUS_COL_WIDTH = 120;
	private static final int FILENAME_COL_WIDTH = 600;
	
	private static final int WINDOW_HEIGHT = 600;
	private static final int WINDOW_WIDTH = 800;
	 
    private DefaultTableModel tableModel;
    private JTable table;
    
    private JLabel pendingPromptsLabel;
    private JLabel processedPromptsLabel;
    private JLabel skippedPromptsLabel;
    private JLabel donePromptsLabel;
    private JLabel durationLabel;
    
    private JButton mergeButton;    
    private JButton processButton;    
    private JButton processSelectedButton;
    private JButton refreshButton; 
    private JButton cleanButton;
    private JButton configureButton;
    private ListSelectionModel selectionModel;
    
    private IFileStateController fileStateController;
    private ConfigurationController configurationController;
    private IResultController mergeResultController;
    private IResultController promptResultController;
    private IResultModelView promptResultView;
    
    public MainView(IFileStateController fileStateController, IResultController mergeResultController, IResultController promptResultController, ConfigurationController configurationController)  {
    	this.fileStateController = fileStateController;
    	this.configurationController = configurationController;
    	this.mergeResultController = mergeResultController;
    	this.promptResultController = promptResultController;
    	this.promptResultView = new PromptResultView();
    	
    	initComponents();
    	localInitialization();
    	
    	promptResultController.addView(promptResultView);
    	fileStateController.addView(this);
    	
    	requestViewUpdate();    	
    }
    
	private void initComponents() {
        setTitle("GPT Generator");        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      
        String[] columnNames = {"Filename", "Input", "Merge", "Reply"};
        tableModel = new DefaultTableModel(columnNames, 0) {
			private static final long serialVersionUID = -1894825464127554238L;

			@Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JPanel mainPanel = new JPanel();

        GridBagLayout gbl = new GridBagLayout();
        GridBagConstraints gbc = new GridBagConstraints();
        gbl.setConstraints(mainPanel, gbc);
        mainPanel.setLayout(gbl);
        
        Insets buttonPanelInsets = GuiPresetsService.buttonPanelInsets();
        Insets statusPanelInsets = GuiPresetsService.statusPanelInsets();
        Insets tableInsets = GuiPresetsService.tableInsets(); 
        Insets labelInsets = GuiPresetsService.labelInsets();

        GbcFactory gbcc = new GbcFactory();

        // SECTION HEADING for table
        gbc = gbcc.getHorizontal(labelInsets);
        mainPanel.add(GuiElementFactory.subHeadingPanel("Files"), gbc);
        gbcc.nextRow();
        
        table = new JTable(tableModel);
        setColumnWidths(table, 0, FILENAME_COL_WIDTH);
        setColumnWidths(table, 1, STATUS_COL_WIDTH);        
        setColumnWidths(table, 2, STATUS_COL_WIDTH);        
        setColumnWidths(table, 3, STATUS_COL_WIDTH);        

        JScrollPane scrollPane = new JScrollPane(table);
        
        Dimension paneDim = new Dimension();
        paneDim.width = WINDOW_WIDTH;
        paneDim.height = WINDOW_HEIGHT;
        scrollPane.setPreferredSize(paneDim);
        scrollPane.setMinimumSize(paneDim);
        
        mergeButton = new JButton("Merge");
        processSelectedButton = new JButton("Process selected");
        processButton = new JButton("Process");
        updateSelectedStatus(false);        
        
        refreshButton = new JButton("Refresh");
        cleanButton = new JButton("Clean");
   
        configureButton = new JButton("Configure");
        
        JButton quitButton = new JButton("Quit");
        final IFileStateView fileStateView = this;
        quitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	promptResultController.removeView(promptResultView);
            	fileStateController.removeView(fileStateView);
                dispose();
            }
        });
        JPanel statusPanel = new JPanel();
        
        pendingPromptsLabel = new JLabel(EMPTY_LABEL_TEXT);
        processedPromptsLabel = new JLabel(EMPTY_LABEL_TEXT);
        skippedPromptsLabel = new JLabel(EMPTY_LABEL_TEXT);
        donePromptsLabel = new JLabel(EMPTY_LABEL_TEXT);
        durationLabel = new JLabel(EMPTY_LABEL_TEXT);

        statusPanel.add(new JLabel("Pending files: "));
        statusPanel.add(pendingPromptsLabel);

        statusPanel.add(new JLabel(SPACING));
        statusPanel.add(new JLabel("Processed files: "));
        statusPanel.add(processedPromptsLabel);
        
        statusPanel.add(new JLabel(SPACING));
        statusPanel.add(new JLabel("Skipped files: "));
        statusPanel.add(skippedPromptsLabel);

        statusPanel.add(new JLabel(SPACING));
        statusPanel.add(new JLabel("Total files: "));
        statusPanel.add(donePromptsLabel);
        
        statusPanel.add(new JLabel(SPACING));
        statusPanel.add(new JLabel("Duration: "));
        statusPanel.add(durationLabel);
        statusPanel.add(new JLabel(" Seconds"));
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(mergeButton);
        buttonPanel.add(processButton);
        buttonPanel.add(processSelectedButton);
        buttonPanel.add(refreshButton);
        buttonPanel.add(cleanButton);
        
        buttonPanel.add(configureButton);
        buttonPanel.add(quitButton);

        gbc = gbcc.getBoth(tableInsets);
        mainPanel.add(scrollPane, gbc);
        gbcc.nextRow();

        gbc = gbcc.getHorizontal(statusPanelInsets);
        mainPanel.add(statusPanel, gbc);
        gbcc.nextRow();
        
        gbc = gbcc.getHorizontal(buttonPanelInsets);
        mainPanel.add(buttonPanel, gbc);
        gbcc.nextRow();
        
        // Set selection mode to multiple selection
        table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        selectionModel = table.getSelectionModel();        
        
        getContentPane().add(mainPanel);
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setMinimumSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));        
        setLocationRelativeTo(null);
        setVisible(true);		
	}

	private void localInitialization() {
		final JFrame parent = this;
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) { // Check for double-click
                    int rowIndex = table.rowAtPoint(e.getPoint());

                    // Check if the clicked point is on a valid row
                    if (rowIndex != -1) {
                    	fileStateController.showFileComparisonView(parent, table.getSelectedRow());
                    }
                }
            }
        });

        mergeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                merge();
            }
        });
        
        processButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                processAll();
            }
        });
		
        processSelectedButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	processSelected();
            }
        });
		
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	refresh();
            }
        });
		
        cleanButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	clean();
            }
        });

        configureButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	configurationController.editConfiguration();
            }
        });
		
        selectionModel.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    boolean rowsAreSelected = table.getSelectedRowCount() > 0;
                    updateSelectedStatus(rowsAreSelected);
                }
            }
        });        
	}
	
	protected void refresh() {
		fileStateController.refreshCache();
	}

	protected void clean() {
        ProcessManager.cleanWorkingFiles(configurationController, fileStateController);
	}

	private void requestViewUpdate() {
    	SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				configurationController.requestViewUpdate();
				promptResultController.requestViewUpdate();
				fileStateController.requestViewUpdate();		    	
			}
		});
	}
	
	@Override
	public void setList(List<PhaseStatus> list) {
		updateTable(list);
	}
	
	protected void updateSelectedStatus(boolean rowsAreSelected) {
        if (rowsAreSelected) {
        	processSelectedButton.setToolTipText(null);
        } else {
        	processSelectedButton.setToolTipText("Please select at least one line");                    	
        }
        processSelectedButton.setEnabled(rowsAreSelected);
	}
	
	private static void setColumnWidths(JTable table, int columnIndex, int width) {
        TableColumn column = table.getColumnModel().getColumn(columnIndex);
        column.setPreferredWidth(width);
    }

	public void updateTable(List<PhaseStatus> list) {
        tableModel.setRowCount(0);
        for (PhaseStatus status : list) {
            tableModel.addRow(new Object[]{status.getFilename(), status.getInputRepresentation(), status.getMergeRepresentation(), status.getReplyRepresentation()});
        }
        tableModel.fireTableDataChanged();
	}

	private void merge() {
		String message = fileStateController.validateInputDirIsMappedBySourcePartitioning();
		if (! message.isEmpty()) {
			JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
		} else {
			MergeManager mm = new MergeManager(configurationController, fileStateController, mergeResultController);
			for (int i = 0; (i < 10) && mm.filesReady(); i ++) {
				mm.process();
			}
			fileStateController.refreshCache();			
			fileStateController.requestViewUpdate();
		}
	}
	
	private void processAll() {
		String message = fileStateController.validateInputDirIsMappedBySourcePartitioning();
		if (! message.isEmpty()) {
			JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);			
		} else {
			promptResultController.clear();
			ProcessManager.processAll(configurationController, mergeResultController, promptResultController, fileStateController);
			fileStateController.refreshCache();			
			promptResultController.requestViewUpdate();
			fileStateController.requestViewUpdate();
		}
	}
	
	private SelectedFiles getSelected(int[] selectedRows) {
		SelectedFiles result = new SelectedFiles();
		for (int i = 0; i < selectedRows.length; i ++) {
			int index = selectedRows[i];
			if ((index >= 0) && (index < tableModel.getRowCount())) {
				result.add( (String) tableModel.getValueAt(index, 0));
			}
		}
		return result;
	}
	
	private List<String> getKeyOrder() {
		List<String> result = new ArrayList<String>();

		for (int i = 0; i < tableModel.getRowCount(); i ++) {
			result.add( (String) tableModel.getValueAt(i, 0));
		}
		
		return result;
	}
	
	private void processSelected() {
		String message = fileStateController.validateInputDirIsMappedBySourcePartitioning();
		if (! message.isEmpty()) {
			JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
		} else {

		}
		List<String> order = getKeyOrder(); 
		
    	SelectedFiles selectedFiles = getSelected(table.getSelectedRows());
    	ProcessManager.processSelected(configurationController, mergeResultController, promptResultController, fileStateController, selectedFiles);
    	// updateTable(order);    	
		restoreSelection(selectedFiles);
	}
	/*
	private void processSelected(int[] selectedRows) {
		mainViewController.processSelected(selectedRows);
		List<String> order = fileStatus.getKeyOrder(); 
		List<String> selected = fileStatus.getSelected(selectedRows); 

		clearStatsDisplay();
		ResultModel stats = new ResultModel();
		stats.startTimer();
				
		PromptProcessor.processSelected(configuration, cacheCollection, selected, stats);
		FileInstaller.install(configuration.getMergeConfig().getCurrentBaseDir(), configuration.getInstalls());
		stats.stopTimer();
		
		processedPromptsLabel.setText(String.format(NUMBER_FORMAT, stats.getProcessed()));
		skippedPromptsLabel.setText(String.format(NUMBER_FORMAT, stats.getSkipped()));
		durationLabel.setText(String.format(NUMBER_FORMAT, stats.getDuration()));
		
		updateStatsDisplay(stats);
		updateTable(order);
		
		restoreSelection(selected);
	}
	*/
	
	private void restoreSelection(SelectedFiles selectedEntries) {
		ListSelectionModel selectionModel = table.getSelectionModel();

		for (int i = 0; i < tableModel.getRowCount(); i ++) {
			if (selectedEntries.contains(((String)tableModel.getValueAt(i, 0)))) {
				selectionModel.addSelectionInterval(i,  i);
			}
		}
	}
	
	private class MergeResultView implements IResultModelView {
		
		public MergeResultView() {
		}

		@Override
		public void setPending(int newPending) {
			//pendingFiles0Label.setText(String.format("%4d", newPending));
		}

		@Override
		public void setProcessed(int newProcessed) {
			//processedPromptsLabel.setText(String.format("%4d", newProcessed));
		}

		@Override
		public void setSkipped(int newSkipped) {
			//skippedPromptsLabel.setText(String.format("%4d", newSkipped));
		}

		@Override
		public void setDone(int newDone) {
			//donePromptsLabel.setText(String.format("%4d", newDone));
		}

	}
	
	private class PromptResultView implements IResultModelView {
		
		public PromptResultView() {
		}

		@Override
		public void setPending(int newPending) {
			pendingPromptsLabel.setText(String.format("%4d", newPending));
		}

		@Override
		public void setProcessed(int newProcessed) {
			processedPromptsLabel.setText(String.format("%4d", newProcessed));
		}

		@Override
		public void setSkipped(int newSkipped) {
			skippedPromptsLabel.setText(String.format("%4d", newSkipped));
		}

		@Override
		public void setDone(int newDone) {
			donePromptsLabel.setText(String.format("%4d", newDone));
		}

	}
	

}