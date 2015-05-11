package mvc.view;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import java.awt.GridLayout;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

import mvc.ConfigObserver;
import mvc.SoundModelInterface;
import mvc.controller.ControllerInterface;
import mvc.model.ModelInputTypes;

/**
 * Class represents the view for settings of the model. 
 *  
 * @author anthony timotheou
 *
 */
public class ISettings extends JFrame implements ConfigObserver {
  
  /**
   * All gui components and labels of the window
   */
  private static final long serialVersionUID = 1259736515364157245L;
  private JPanel contentPane;
  private File selectedFile = null;
  private JComboBox  inputSelector;
  private JLabel lblFileName;
  ControllerInterface controller;
  SoundModelInterface model;
  private JLabel lblSoundSourceDegrees;
  private JComboBox soundSourceDegreesComboBox;
  private JLabel label;
  private JLabel label_1;
  private JLabel label_2;
  private JLabel label_3;
  private JLabel label_4;
  private JButton btnNewButton;
  private JMenuBar menuBar;
  private JMenu mnNewMenu;
  private JMenuItem mntmClose;
  
  /**
   * Create the view by passing in the controller and model references.
   * 
   * @param controller
   * @param model
   */
  public ISettings(ControllerInterface controller, SoundModelInterface model) {
    this.controller = controller;
    this.model = model;
    model.registerObserver(this); // register as an observer to receive callbacks to update method
    initialise();
  }
  
  /**
   * Initialise the gui by creating all components in a composite and adding them to the frame. 
   * Also, add all event handlers for clicks, etc and appropriate calls to controller object. 
   */
  public void initialise() {
    setBounds(100, 100, 450, 300);
    
    menuBar = new JMenuBar();
    setJMenuBar(menuBar);
    
    mnNewMenu = new JMenu("File");
    menuBar.add(mnNewMenu);
    
    mntmClose = new JMenuItem("Close");
    mntmClose.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        controller.closeSettings();
      }
    });
    mnNewMenu.add(mntmClose);
    contentPane = new JPanel();
    contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
    setContentPane(contentPane);
    
    this.addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
          controller.closeWindow();
      }
    });
    contentPane.setLayout(new GridLayout(6, 6, 0, 0));
    
    JLabel lblInputData = new JLabel("Input Data:");
    contentPane.add(lblInputData);
    
    // TODO bug no default values for this, affects performance, hinks default value are generatedSin and 90
    inputSelector = new JComboBox();
    inputSelector.setModel(new DefaultComboBoxModel(ModelInputTypes.values()));
    inputSelector.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        controller.inputTypeChange((ModelInputTypes)inputSelector.getSelectedItem());
      }
    });
    contentPane.add(inputSelector);
    
        lblSoundSourceDegrees = new JLabel("Sound Source Degrees:");
        contentPane.add(lblSoundSourceDegrees);
    
    soundSourceDegreesComboBox = new JComboBox();
    soundSourceDegreesComboBox.setModel(new DefaultComboBoxModel(new String[] {"90", "60", "30", "0", "-30", "-60", "-90"}));
    soundSourceDegreesComboBox.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        controller.soundSourceDegreesUpdate((int)Integer.parseInt(soundSourceDegreesComboBox.getSelectedItem().toString()));
      }
    });
    contentPane.add(soundSourceDegreesComboBox);
    
        lblFileName = new JLabel("Chosen File: ");
        lblFileName.setBackground(UIManager.getColor("Button.background"));
        contentPane.add(lblFileName); 
    
    JButton btnConfigFile = new JButton("Choose Configuration File");
    btnConfigFile.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        JFileChooser fc = new JFileChooser();
        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int fileStatus = fc.showOpenDialog(contentPane);

        if (fileStatus == JFileChooser.APPROVE_OPTION) {
           controller.setFile(fc.getSelectedFile());
        } 
      }
    });
    contentPane.add(btnConfigFile);
    
    btnNewButton = new JButton("Ok");
    btnNewButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent arg0) {
        controller.closeWindow();
        controller.closeSettings();
      }
    });
    contentPane.add(btnNewButton);
    
    label = new JLabel("");
    contentPane.add(label);
    
    label_3 = new JLabel("");
    contentPane.add(label_3);
    
    label_1 = new JLabel("");
    contentPane.add(label_1);
    
    label_2 = new JLabel("");
    contentPane.add(label_2);
    
    label_4 = new JLabel("");
    contentPane.add(label_4);
  }

  @Override
  /**
   * Update all settings when this method is called by a model. 
   */
  public void update(ModelInputTypes m, File f) {
    if (m != null) {
      inputSelector.setSelectedItem(m); // TODO convert to index for speed
    } 
    
    if (f != null) {
      lblFileName.setText(f.getName());
    }
  }

}
