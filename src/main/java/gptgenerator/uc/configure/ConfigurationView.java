package gptgenerator.uc.configure;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import gptgenerator.services.GbcFactory;
import gptgenerator.services.GuiElementFactory;
import gptgenerator.services.GuiPresetsService;
import gptgenerator.uc.configure.gpt.ChatTemperature;
import gptgenerator.uc.configure.sourcepartition.ISourcePartitionModel;
import gptgenerator.uc.configure.sourcepartition.SourcePartition;

/**
 * Dialogue to edit the configurationModel values 
 */
public class ConfigurationView extends JFrame implements IConfigurationView {
	private static final String MAKE_CHAT_API_CALLS = "Make Chat API calls";
	private static final int SECOND_COLUMN_WIDTH = 250;
    private static final int FIRST_COLUMN_WIDTH = 20;
    private static final int MIN_HEIGHT = 600;
    private static final int MIN_WIDTH = 800;
    private static final long serialVersionUID = 6646662526692639816L;
	private static final int FIELD_LENGTH = 30;
	private static final int NUMBER_FIELD_LENGTH = 10;
	private static final int GRID_COLUMNS = 3;
	private static final Dimension MINIMUM_INPUT_DIMENSION = new Dimension(50,20);


    public ConfigurationController configurationController;

    private DefaultTableModel tableModel;
    private JTable table;
    private ListSelectionModel selectionModel;    
    
    private JTextField promptCurrentDirField;
    private JTextField projectRootField;
    
    private JTextField temperatureField;
    private JLabel temperatureMessage;
    private JTextField numberOfThreadsField;
    private JLabel numberOfThreadsStatus;

    private JTextField chatApiUrlField;
    private JTextField chatApiKeyField;
        
    private JCheckBox makeChatApiCallsCheckBox;
    
    private JButton saveButton;
    private JButton editInstallButton;
    private JButton addInstallButton;
    private JButton deleteInstallButton;

    public ConfigurationView(ConfigurationController configurationController) {
    	this.configurationController = configurationController;
    	configurationController.addView(this);
  	
    	initComponents();
    	localInitialization();
    	requestViewUpdate();
    }
    
	private void initComponents() {
        setTitle("Edit configurationModel");        
   		setSize(MIN_WIDTH, MIN_HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
   		
        JPanel mainPanel = new JPanel();

        GridBagLayout gbl = new GridBagLayout();
        GridBagConstraints gbc = new GridBagConstraints();
        gbl.setConstraints(mainPanel, gbc);
        mainPanel.setLayout(gbl);
        
        Insets buttonPanelInsets = GuiPresetsService.buttonPanelInsets();
        Insets editInsets = GuiPresetsService.editInsets();
        Insets tableInsets = GuiPresetsService.tableInsets(); 
        Insets labelInsets = GuiPresetsService.labelInsets();

        GbcFactory gbcc = new GbcFactory();
        
        // SECTION HEADING for table
        gbc = gbcc.getHorizontal(labelInsets);
        gbc.gridwidth = GRID_COLUMNS;
        mainPanel.add(GuiElementFactory.subHeadingLabel("Installation configurations"), gbc);
        gbcc.nextRow();
        
        // TABLE with Installation configurations
        String[] columnNames = {"Source", "Destination"};
        tableModel = new DefaultTableModel(columnNames, 0) {
			private static final long serialVersionUID = -1894825464127554238L;

			@Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        table = new JTable(tableModel);
        setColumnWidths(table, 0, FIRST_COLUMN_WIDTH);
        setColumnWidths(table, 1, SECOND_COLUMN_WIDTH);        
        // Set selection mode to single selection
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) { // Check for double-click
                    int rowIndex = table.rowAtPoint(e.getPoint());

                    // Check if the clicked point is on a valid row
                    if (rowIndex != -1) {
                    	showEditExistingInstallationView(table.getSelectedRow());
                    }
                }
            }
        });
        
        
        JScrollPane scrollPane = new JScrollPane(table);
       
        if (table.getRowCount() > 0) {
            table.setRowSelectionInterval(0, 0);
        }
        selectionModel = table.getSelectionModel();
        selectionModel.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    boolean rowsAreSelected = table.getSelectedRowCount() > 0;
                    updateSelectedStatus(rowsAreSelected);
                }
            }
        });   

        gbc = gbcc.getBoth(tableInsets);
        gbc.gridwidth = GRID_COLUMNS;
        mainPanel.add(scrollPane, gbc);
        gbcc.nextRow();

        // PANEL with buttons for manipulating the table entries
        JPanel tableButtonsPanel = new JPanel();

        editInstallButton = new JButton("Edit Install");
        addInstallButton = new JButton("Add Install");
        deleteInstallButton = new JButton("Delete Install");
          
        tableButtonsPanel.add(editInstallButton);
        tableButtonsPanel.add(addInstallButton);
        tableButtonsPanel.add(deleteInstallButton);
        
        gbc = gbcc.getHorizontal(buttonPanelInsets);
        gbc.gridwidth = GRID_COLUMNS;
        mainPanel.add(tableButtonsPanel, gbc);
        gbcc.nextRow();
        
        // SECTION HEADING for project settings
        gbc = gbcc.getHorizontal(labelInsets);
        gbc.gridwidth = GRID_COLUMNS;
        mainPanel.add(GuiElementFactory.subHeadingLabel("Project settings"), gbc);
        gbcc.nextRow();
       
        // PROJECT ROOT
        gbc = gbcc.getNone(editInsets);
        gbc.anchor = GridBagConstraints.EAST;        		
        mainPanel.add(new JLabel("Project root"), gbc);

        projectRootField = new JTextField();
        projectRootField.setColumns(FIELD_LENGTH);

        gbc = gbcc.getHorizontal(editInsets);
        gbc.gridwidth = GRID_COLUMNS - 1;
        mainPanel.add(projectRootField, gbc);        
        gbcc.nextRow();
        
        // PROMPT CURRENT DIR
        gbc = gbcc.getNone(editInsets);
        gbc.anchor = GridBagConstraints.EAST;        
        mainPanel.add(new JLabel("MPS output dir"), gbc);

        promptCurrentDirField = new JTextField();
        promptCurrentDirField.setColumns(FIELD_LENGTH);
        
        gbc = gbcc.getHorizontal(editInsets);
        gbc.gridwidth = GRID_COLUMNS - 1;
        mainPanel.add(promptCurrentDirField, gbc);
        gbcc.nextRow();

        
        // SECTION HEADING for chat API settings        
        gbc = gbcc.getHorizontal(labelInsets);
        gbc.gridwidth = GRID_COLUMNS;
        mainPanel.add(GuiElementFactory.subHeadingLabel("Chat API settings"), gbc);
        gbcc.nextRow();
            
        // PROD mode
        gbc = gbcc.getNone(editInsets);
        gbc.anchor = GridBagConstraints.EAST;        
        mainPanel.add(new JLabel(MAKE_CHAT_API_CALLS), gbc);

        makeChatApiCallsCheckBox = new JCheckBox();
        
        gbc = gbcc.getNone(editInsets);
        gbc.anchor = GridBagConstraints.LINE_START;
        mainPanel.add(makeChatApiCallsCheckBox, gbc);

        // Warning: Setting production mode to true will result in charges to your account!
        gbc = gbcc.getNone(editInsets);
        gbc.anchor = GridBagConstraints.WEST;        		
        mainPanel.add(new JLabel("Attention! If this check box is set the application will make calls to the API."), gbc);
        
        gbcc.nextRow();
        
        gbc = gbcc.getNone(editInsets);
        gbc = gbcc.getNone(editInsets);
        gbc = gbcc.getNone(editInsets);
        gbc.anchor = GridBagConstraints.WEST;        		
        mainPanel.add(new JLabel("And you will be billed by your provider."), gbc);
        gbcc.nextRow();
               
        
        // CHAT TEMPERATURE
        gbc = gbcc.getNone(editInsets);
        gbc.anchor = GridBagConstraints.EAST;                
        mainPanel.add(new JLabel("Chat temperature"), gbc);
        
        temperatureField = new JTextField();
        temperatureField.setColumns(NUMBER_FIELD_LENGTH);
        temperatureField.setMinimumSize(MINIMUM_INPUT_DIMENSION);
        
        gbc = gbcc.getNone(editInsets);
        gbc.anchor = GridBagConstraints.WEST;                
        mainPanel.add(temperatureField, gbc);
                
        temperatureMessage = new JLabel(ChatTemperature.validationMessage());
        gbc = gbcc.getNone(editInsets);
        gbc.anchor = GridBagConstraints.LINE_START;
        
        mainPanel.add(temperatureMessage, gbc);
        temperatureMessage.setVisible(false);        
        gbcc.nextRow();

        // CHAT NUMBER OF THREADS
        gbc = gbcc.getNone(editInsets);
        gbc.anchor = GridBagConstraints.EAST;                
        mainPanel.add(new JLabel("Number of chat threads"), gbc);
        
        numberOfThreadsField = new JTextField();
        numberOfThreadsField.setColumns(NUMBER_FIELD_LENGTH);
        numberOfThreadsField.setMinimumSize(MINIMUM_INPUT_DIMENSION);
        gbc = gbcc.getNone(editInsets);
        gbc.anchor = GridBagConstraints.WEST;                
        
        mainPanel.add(numberOfThreadsField, gbc);
        
        numberOfThreadsStatus = new JLabel(String.format("Error! Valid values: %d <= number of threads <= %d", ProcessingThreadCount.MIN, ProcessingThreadCount.MAX));
        gbc = gbcc.getNone(editInsets);
        gbc.anchor = GridBagConstraints.WEST;
        		
        mainPanel.add(numberOfThreadsStatus, gbc);
        numberOfThreadsStatus.setVisible(false);
        gbcc.nextRow();
        
        // CHAT API URL
        gbc = gbcc.getNone(editInsets);
        gbc.anchor = GridBagConstraints.EAST;                
        mainPanel.add(new JLabel("Chat API URL"), gbc);     
       
        chatApiUrlField = new JTextField();
        chatApiUrlField.setColumns(FIELD_LENGTH);
        
        gbc = gbcc.getHorizontal(editInsets);
        gbc.gridwidth = GRID_COLUMNS - 1;
        gbc.anchor = GridBagConstraints.WEST;                
        mainPanel.add(chatApiUrlField, gbc);
        gbcc.nextRow();
        
        // CHAT API KEY
        gbc = gbcc.getNone(editInsets);
        gbc.anchor = GridBagConstraints.EAST;                
        mainPanel.add(new JLabel("Chat API Key"), gbc);
        
        chatApiKeyField = new JTextField();
        chatApiKeyField.setColumns(FIELD_LENGTH);
        chatApiKeyField.setMinimumSize(MINIMUM_INPUT_DIMENSION);
        gbc = gbcc.getHorizontal(editInsets);
        gbc.gridwidth = GRID_COLUMNS - 1;
        gbc.anchor = GridBagConstraints.WEST;                
        
        mainPanel.add(chatApiKeyField, gbc);
        gbcc.nextRow();
                

        // MAIN BUTTON PANEL
        JPanel mainButtonPanel = new JPanel();

        saveButton = new JButton("Save");
        saveButton.setEnabled(false);        
        
        JButton closeButton = new JButton("Close");
        final IConfigurationView view = this;
        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	configurationController.removeView(view);            	
                dispose();
            }
        });

        mainButtonPanel.add(saveButton);
        mainButtonPanel.add(closeButton);
        
        gbc = gbcc.getHorizontal(labelInsets);
        gbc.gridwidth = GRID_COLUMNS;        
        mainPanel.add(mainButtonPanel, gbc);

        getContentPane().add(mainPanel);

        setLocationRelativeTo(null);
        setVisible(true);
    }
    
    protected void updateSelectedStatus(boolean rowsAreSelected) {
        editInstallButton.setEnabled(rowsAreSelected);
        deleteInstallButton.setEnabled(rowsAreSelected);		
	}

	private void localInitialization() {
        addInstallButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showCreateNewInstallationView();
            }
        });

        editInstallButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	showEditExistingInstallationView(table.getSelectedRow());
            }
        });
        editInstallButton.setEnabled(false);

        deleteInstallButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedIndex = table.getSelectedRow();
                showDeleteConfirmation(selectedIndex);
            }
        });
        deleteInstallButton.setEnabled(false);
        
        projectRootField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                configurationController.setProjectRoot(projectRootField.getText());
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                configurationController.setProjectRoot(projectRootField.getText());
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                // Plain text components do not fire these events
            }
        });        
           
        promptCurrentDirField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                configurationController.setInputCurrentDir(promptCurrentDirField.getText());
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                configurationController.setInputCurrentDir(promptCurrentDirField.getText());
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                // Plain text components do not fire these events
            }
        });        
        
        makeChatApiCallsCheckBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    configurationController.setMakeApiCalls(true);
                } else if (e.getStateChange() == ItemEvent.DESELECTED) {
                    configurationController.setMakeApiCalls(false);
                }
            }
        });
    	
        temperatureField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
            	temperatureWasChanged();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
            	temperatureWasChanged();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                // Plain text components do not fire these events
            }
        });        
    	
        numberOfThreadsField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
            	numberOfThreadsWasChanged();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
            	numberOfThreadsWasChanged();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                // Plain text components do not fire these events
            }
        });
        
        
        chatApiUrlField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
            	chatApiUrlWasChanged();
            }


			@Override
            public void removeUpdate(DocumentEvent e) {
            	chatApiUrlWasChanged();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                // Plain text components do not fire these events
            }
        });        
        
        chatApiKeyField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
            	chatApiKeyWasChanged();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
            	chatApiKeyWasChanged();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                // Plain text components do not fire these events
            }
        });        
    	
    	
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	configurationController.save();
            	dispose();
            }
        });
    	
    }


	private void requestViewUpdate() {
    	SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
		    	configurationController.requestViewUpdate();
			}
		});    	
	}
	
    private static void setColumnWidths(JTable table, int columnIndex, int width) {
        TableColumn column = table.getColumnModel().getColumn(columnIndex);
        column.setPreferredWidth(width);
    }
    
    private void showEditExistingInstallationView(int index) {
    	configurationController.editTargetSubDir(this, this, index);
    }
    
    private void showCreateNewInstallationView() {
    	configurationController.editNewTargetSubDir(this, this);    	
    }
   
    private void showDeleteConfirmation(int deleteIndex) {
        int option = JOptionPane.showConfirmDialog(
                this,
                "Do you want to delete configurationModel " + deleteIndex + "?",
                "Confirmation",
                JOptionPane.OK_CANCEL_OPTION
        );

        if (option == JOptionPane.OK_OPTION) {
            configurationController.removePartition(deleteIndex);
        }
    }
    
    /**
     * Disable the save button if the temperature and numberOfThreads fields are not valid
     */
    private void validateFields() {
        boolean isTemperatureValid = validateTemperature();
        boolean isNumberValid = validateNumberOfThreads();
        saveButton.setEnabled(isTemperatureValid && isNumberValid);
    }

    private void temperatureWasChanged() {
        if (validateTemperature()) {
        	configurationController.setChatTemperature(temperatureField.getText());
        }
        validateFields();
    }
    
    private void numberOfThreadsWasChanged() {
    	if (validateNumberOfThreads()) {
        		int numberOfThreads = Integer.parseInt(numberOfThreadsField.getText());
        		configurationController.setChatNumberOfThreads(numberOfThreads);
    	}
    	validateFields();
    }
    
    private void chatApiUrlWasChanged() {
    	configurationController.setChatApiURL(chatApiUrlField.getText());		
	}

    private void chatApiKeyWasChanged() {
		configurationController.setChatApiKey(chatApiKeyField.getText());
	}
        
    private boolean validateTemperature() {
        String temperatureText = temperatureField.getText();

        boolean isValid = ChatTemperature.validateString(temperatureText);
        temperatureMessage.setVisible(!isValid);
        return isValid;
    }

    private boolean validateNumberOfThreads() {
    	boolean isValid = ProcessingThreadCount.validateString(numberOfThreadsField.getText());
    	numberOfThreadsStatus.setVisible(!isValid);
        return isValid;
    }
    
	@Override
	public void setProjectRoot(String newProjectRoot) {
		if (!projectRootField.getText().equals(newProjectRoot)) {
			projectRootField.setText(newProjectRoot);
			}
	}
		

	@Override
	public void setInputCurrentDir(String newDir) {
		if (!promptCurrentDirField.getText().equals(newDir)) {
			promptCurrentDirField.setText(newDir);
		}
	}

	@Override
	public void clearInstalls() {
		tableModel.setRowCount(0);
	}

	@Override
	public void addInstall(ISourcePartitionModel newDir) {
		tableModel.addRow(makeTableRow(newDir));
	}

	private Object[] makeTableRow(ISourcePartitionModel newDir) {
		return new Object[] { newDir.getSourceDirRel(), newDir.getDestDirAbs() };
	}

	@Override
	public void setInstallAt(int index, ISourcePartitionModel newDir) {
		tableModel.removeRow(index);
		tableModel.insertRow(index, makeTableRow(newDir));
	}

	@Override
	public void removeInstallAt(int deleteIndex) {
		tableModel.removeRow(deleteIndex);
	}

	@Override
	public void setInstall(List<SourcePartition> list) {
		for (SourcePartition cur: list) {
			tableModel.addRow(makeTableRow(cur));			
		}
		tableModel.fireTableDataChanged();		
	}

	@Override
	public void setChatTemperature(String temperatureText) {
		if (!temperatureField.getText().equals(temperatureText)) {
			temperatureField.setText(temperatureText);
		}
	}

	@Override
	public void setChatNumberOfThreads(String numberOfTreads) {
		if (! numberOfThreadsField.getText().equals(numberOfTreads)) {
			numberOfThreadsField.setText(numberOfTreads);
		}
	}

	@Override
	public void setMakeApiCalls(boolean makeApiCalls) {
		if (makeChatApiCallsCheckBox.isSelected() != makeApiCalls) {
			makeChatApiCallsCheckBox.setSelected(makeApiCalls);
		}
	}

	@Override
	public void setChatApiURL(String chatApiURL) {
		if (!chatApiUrlField.getText().equals(chatApiURL)) {
			chatApiUrlField.setText(chatApiURL);
		}
	}

	@Override
	public void setChatApiKey(String chatApiKey) {
		if (!chatApiKeyField.getText().equals(chatApiKey)) {
			chatApiKeyField.setText(chatApiKey);
		}
	}

}