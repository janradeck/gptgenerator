package gptgenerator.uc.configure.sourcepartition;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import gptgenerator.services.GbcFactory;
import gptgenerator.services.GuiElementFactory;
import gptgenerator.services.GuiPresetsService;
import gptgenerator.uc.configure.IConfigurationController;
import gptgenerator.uc.configure.gpt.GptTemperature;
import gptgenerator.uc.configure.merge.ITemplateConfigController;
import gptgenerator.uc.configure.merge.ITemplateConfigView;
import gptgenerator.uc.mainview.IGptConfigView;
import gptgenerator.uc.processing.o2prompt.IGptConfigController;

public class SourcePartitionView extends JFrame implements ISourcePartitionView, ITemplateConfigView, IGptConfigView, IPrettyPrintSettingsView {
	private static final long serialVersionUID = 4087995902033083312L;
	private static final int DIRECTORIY_FIELD_LENGTH = 30;
	private static final int NUMBER_FIELD_LENGTH = 3;
	private static final int MARKER_FIELD_LENGTH = 15;
	
	private JTextField sourceField;
    private JTextField destField;

    private JTextArea systemMessageField;
    private JCheckBox temperatureCheckBox;
    private JTextField temperatureField;
    private JLabel temperatureMessage;

    private JTextField markerStartField;
    private JTextField markerEndField;
    
    private JCheckBox prettyPrintInputCheckBox;
    private JCheckBox prettyPrintMergeCheckBox;
    private JTextArea prettierIgnoreFilesArea;
        
    private JButton addButton;
    
    private int index;

    private static final int EDIT_PANEL_COLUMNS = 3;
    private JFrame parentView;
    
    private ISourcePartitionController sourcePartitionController;  
    private ITemplateConfigController templateConfigController;
    private IGptConfigController gptConfigController;
    private IConfigurationController configurationController;
    private IPrettyPrintSettingsController ppSettingsController;
    private ISourcePartitionModel sourcePartition;
   
    public SourcePartitionView(final JFrame parentView, IConfigurationController configurationController, ISourcePartitionModel modelCopy, int index) {
    	this.parentView = parentView;
    	this.sourcePartitionController = new SourcePartitionController(modelCopy);
    	this.templateConfigController = sourcePartitionController.getTemplateConfigController();
    	this.gptConfigController = sourcePartitionController.getGptConfigController();
    	this.ppSettingsController = sourcePartitionController.getPrettyPrintSettingsController();
    	this.index = index;
    	this.configurationController = configurationController;
    	this.sourcePartition = modelCopy;
    	initComponents();
    	localInitialization();
    	requestViewUpdate();
    }
    	
    private void initComponents() {	
    	if (index < 0) {
    		setTitle("Create new installation");
    	} else {
    		setTitle("Edit installation");
    	}
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 700);

        sourceField = new JTextField();
        sourceField.setColumns(DIRECTORIY_FIELD_LENGTH);
        destField = new JTextField();
        destField.setColumns(DIRECTORIY_FIELD_LENGTH);
        systemMessageField = new JTextArea();
        systemMessageField.setColumns(DIRECTORIY_FIELD_LENGTH);
        JScrollPane systemMessageScrollPane = new JScrollPane(systemMessageField);

        temperatureField = new JTextField();
        temperatureField.setColumns(NUMBER_FIELD_LENGTH);
        
        String buttonTitle = index < 0? "Add": "Save";
        addButton = new JButton(buttonTitle);
        temperatureMessage = new JLabel(GptTemperature.validationMessage());
        
        temperatureCheckBox = new JCheckBox();
        
        markerStartField = new JTextField();
        markerStartField.setColumns(MARKER_FIELD_LENGTH );
        
        markerEndField = new JTextField();
        markerEndField.setColumns(MARKER_FIELD_LENGTH );
        markerEndField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                validateFields();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                validateFields();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                // Plain text components do not fire these events
            }
        });             
        
        prettyPrintInputCheckBox = new JCheckBox();
        prettyPrintMergeCheckBox = new JCheckBox();
        prettierIgnoreFilesArea = new JTextArea();
        prettierIgnoreFilesArea.setColumns(DIRECTORIY_FIELD_LENGTH);
        JScrollPane prettierIgnoreFilesScrollPane = new JScrollPane(prettierIgnoreFilesArea);
        
        JButton abortButton = new JButton("Abort");
        abortButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	removeViewBindings();
                dispose();
            }
        });
        
        JPanel editPanel = new JPanel();
        
        GbcFactory gbcc = new GbcFactory();
        
        GridBagLayout gbl = new GridBagLayout();
        GridBagConstraints gbc = new GridBagConstraints();
        gbl.setConstraints(editPanel, gbc);
        editPanel.setLayout(gbl);        

        addSubheading(editPanel, gbcc, "Directories");
        
        addFieldWithTitle(editPanel, gbcc, "Source directory", sourceField);
        addFieldWithTitle(editPanel, gbcc, "Destination directory", destField);

        addSubheading(editPanel, gbcc, "Chat GPT settings");

        gbc = gbcc.getNone(GuiPresetsService.labelInsets()); 
        gbc.gridwidth = EDIT_PANEL_COLUMNS;
        gbc.anchor = GridBagConstraints.WEST;
        editPanel.add(new JLabel("ChatGPT: Message in role \"system\""), gbc);
        gbcc.nextRow();
        
        gbc = gbcc.getBoth(GuiPresetsService.editInsets()); 
        gbc.gridwidth = EDIT_PANEL_COLUMNS;
        editPanel.add(systemMessageScrollPane, gbc);
        gbcc.nextRow();

        addSubheading(editPanel, gbcc, "Temperature settings");

        addFieldWithTitle(editPanel, gbcc, "Individual temperature", temperatureCheckBox);
        addFieldWithTitleAndMessage(editPanel, gbcc, "Temperature", temperatureField, temperatureMessage);

        addSubheading(editPanel, gbcc, "Template settings");

        addFieldWithTitle(editPanel, gbcc, "Marker start", markerStartField);
        addFieldWithTitle(editPanel, gbcc, "Marker end", markerEndField);
                
        // PRETTY PRINT SETTINGS
        addSubheading(editPanel, gbcc, "Pretty Print settings");

        addFieldWithTitle(editPanel, gbcc, "Pretty print input", prettyPrintInputCheckBox);
        addFieldWithTitle(editPanel, gbcc, "Pretty print merge", prettyPrintMergeCheckBox);

        gbc = gbcc.getNone(GuiPresetsService.labelInsets()); 
        gbc.gridwidth = EDIT_PANEL_COLUMNS;
        gbc.anchor = GridBagConstraints.WEST;
        editPanel.add(new JLabel("Prettier ignore files"), gbc);
        gbcc.nextRow();
        
        gbc = gbcc.getBoth(GuiPresetsService.editInsets()); 
        gbc.gridwidth = EDIT_PANEL_COLUMNS;
        editPanel.add(prettierIgnoreFilesScrollPane, gbc);

        gbcc.nextRow();
        
        JPanel buttonPanel = new JPanel();
        
        buttonPanel.add(addButton);
        buttonPanel.add(abortButton);
        
        gbc = gbcc.getHorizontal(GuiPresetsService.buttonPanelInsets()); 
        gbc.gridwidth = EDIT_PANEL_COLUMNS;
        editPanel.add(buttonPanel, gbc);
             
        getContentPane().add(editPanel);
        add(editPanel);

        setDiscardOnEscape();        
        
        setLocationRelativeTo(parentView);
        setVisible(true);
        validateFields();        
    }
    
    private void addSubheading(JPanel destination, GbcFactory factory, String heading) {
    	GridBagConstraints gbc = factory.getHorizontal(GuiPresetsService.labelInsets());
        gbc.gridwidth = EDIT_PANEL_COLUMNS;
        JLabel subheading = GuiElementFactory.subHeadingLabel(heading);
        destination.add(subheading, gbc);
        factory.nextRow();    	
    }
    
    private void addFieldWithTitle(JPanel destination, GbcFactory factory, String title, Component component) {
        GridBagConstraints gbc = factory.getNone(GuiPresetsService.labelInsets()); 
        gbc.anchor = GridBagConstraints.EAST;
        destination.add(new JLabel(title), gbc);
        
        gbc = factory.getNone(GuiPresetsService.labelInsets());
        gbc.anchor = GridBagConstraints.WEST;        
        destination.add(component, gbc);
   
        factory.nextRow();
    }

    private void addFieldWithTitleAndMessage(JPanel destination, GbcFactory factory, String title, Component component, Component message) {
        GridBagConstraints gbc = factory.getNone(GuiPresetsService.labelInsets()); 
        gbc.anchor = GridBagConstraints.EAST;
        destination.add(new JLabel(title), gbc);
        
        gbc = factory.getNone(GuiPresetsService.labelInsets());
        gbc.anchor = GridBagConstraints.WEST;        
        destination.add(component, gbc);

        gbc = factory.getNone(GuiPresetsService.labelInsets());
        gbc.anchor = GridBagConstraints.WEST;        
        destination.add(message, gbc);
        
        factory.nextRow();
    }
    
	private void setDiscardOnEscape() {
        // Close window if <Esc> is pressed
        getContentPane().addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                // Not used for keyTyped
            }

            @Override
            public void keyPressed(KeyEvent e) {
                // Check if the pressed key is ESC (key code 27)
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    removeViewBindings();
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
	}

	private void localInitialization() {
		linkSourcePartitionFields();
		linkGptConfigFields();
		linkTemplateConfigFields();
		linkPrettyPrintSettingsFields();
     
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	addOrReplaceSourcePartition();
            }
        });
        
        sourceField.requestFocus();
    	sourcePartitionController.addView(this);
    	templateConfigController.addView(this);
    	gptConfigController.addView(this);
    	ppSettingsController.addView(this);
	}
	
	private void linkSourcePartitionFields() {
		sourceField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
            	sourcePartitionController.setSourceDirRel(sourceField.getText());
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
            	sourcePartitionController.setSourceDirRel(sourceField.getText());
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                // Plain text components do not fire these events
            }
        });             
        destField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
            	sourcePartitionController.setDestDirAbs(destField.getText());
                validateFields();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
            	sourcePartitionController.setDestDirAbs(destField.getText());
                validateFields();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                // Plain text components do not fire these events
            }
        });             
	}

	private void linkTemplateConfigFields() {
        markerStartField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
            	templateConfigController.setMarkerStart(markerStartField.getText());
                validateFields();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
            	templateConfigController.setMarkerStart(markerStartField.getText());            	
                validateFields();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                // Plain text components do not fire these events
            }
        });             
        markerEndField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
            	templateConfigController.setMarkerEnd(markerEndField.getText());
                validateFields();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
            	templateConfigController.setMarkerEnd(markerEndField.getText());            	
                validateFields();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                // Plain text components do not fire these events
            }
        });             
	}

	private void linkGptConfigFields() {
        temperatureField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
            	gptConfigController.setTemperature(temperatureField.getText());
                validateFields();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
            	gptConfigController.setTemperature(temperatureField.getText());
                validateFields();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                // Plain text components do not fire these events
            }
        });             
        temperatureCheckBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
            	gptConfigController.setIndividualTemperature(temperatureCheckBox.isEnabled());
                boolean isChecked = e.getStateChange() == ItemEvent.SELECTED;
                temperatureField.setEnabled(isChecked);
            }
        });        
        systemMessageField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
            	gptConfigController.setSystemMessage(systemMessageField.getText());
                validateFields();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
            	gptConfigController.setSystemMessage(systemMessageField.getText());            	
                validateFields();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                // Plain text components do not fire these events
            }
        });
		
	}
	
	private void linkPrettyPrintSettingsFields() {
		prettyPrintInputCheckBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                boolean isChecked = e.getStateChange() == ItemEvent.SELECTED;
            	ppSettingsController.setPrettyPrintInput(isChecked);
            }
        });        
		prettyPrintMergeCheckBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                boolean isChecked = e.getStateChange() == ItemEvent.SELECTED;
            	ppSettingsController.setPrettyPrintMerge(isChecked);
            }
        });
		prettierIgnoreFilesArea.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
            	ppSettingsController.setPrettierIgnoreFiles(prettierIgnoreFilesArea.getText());
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
            	ppSettingsController.setPrettierIgnoreFiles(prettierIgnoreFilesArea.getText());
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                // Plain text components do not fire these events
            }
        });
		
	}

	private void requestViewUpdate() {
    	SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				sourcePartitionController.requestViewUpdate();
				templateConfigController.requestViewUpdate();
				gptConfigController.requestViewUpdate();
				ppSettingsController.requestViewUpdate();
			}
		});    			
	}

    private void validateFields() {
       	boolean validTemp = validateTemperature();
        	
       	addButton.setEnabled(validTemp);
    }

    private boolean validateTemperature() {
    	if (temperatureCheckBox.isSelected()) {
	        String temperatureText = temperatureField.getText();
	
	        boolean isValid = GptTemperature.validateString(temperatureText);
	        temperatureMessage.setVisible(!isValid);
	        return isValid;
    	} else {
            temperatureMessage.setVisible(false);
    		return true;
    	}
    }

	@Override
	public void setSrcDirRelative(String newValue) {
		if (!sourceField.getText().equals(newValue)) {
			sourceField.setText(newValue);
		}
	}

	@Override
	public void setDstDirAbsolute(String newValue) {
		if (!destField.getText().equals(newValue)) {
			destField.setText(newValue);
		}
	}

	@Override
	public void setElementMarkerStart(String newValue) {
		if (!markerStartField.getText().equals(newValue)) {
			markerStartField.setText(newValue);
		}
	}

	@Override
	public void setElementMarkerEnd(String newValue) {
		if (!markerEndField.getText().equals(newValue)) {
			markerEndField.setText(newValue);
		}
	}

	@Override
	public void setSystemMessage(String newValue) {
		if (!systemMessageField.getText().equals(newValue)) {
			systemMessageField.setText(newValue);
		}
	}

	@Override
	public void setTemperature(String newValue) {
		if (!temperatureField.getText().equals(newValue)) {
			temperatureField.setText(newValue);
		}
	}

	@Override
	public void setIndividualTemperature(Boolean isIndividual) {
		if (temperatureCheckBox.isSelected() != isIndividual) {
			temperatureCheckBox.setSelected(isIndividual);
			temperatureField.setEnabled(isIndividual);
		}
	}

    private void addOrReplaceSourcePartition() {
    	removeViewBindings();
    	sourcePartition.clearController();
    	if (index < 0) {
    		configurationController.addPartition(sourcePartition);
    	} else {
    		configurationController.setPartitionAt(index, sourcePartition);
    	}
    	configurationController.requestViewUpdate();
        dispose();
    }
		
	protected void removeViewBindings() {
    	templateConfigController.removeView(this);
    	sourcePartitionController.removeView(this);
    	gptConfigController.removeView(this);
    	ppSettingsController.removeView(this);
	}

	@Override
	public void setPrettyPrintInput(boolean prettyPrint) {
		prettyPrintInputCheckBox.setSelected(prettyPrint);
	}

	@Override
	public void setPrettyPrintMerge(boolean prettyPrint) {
		prettyPrintMergeCheckBox.setSelected(prettyPrint);
	}

	@Override
	public void setPrettierIgnoreFiles(String ignoreFiles) {
		prettierIgnoreFilesArea.setText(ignoreFiles);
	}
	
}
