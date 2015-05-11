package mvc.view;

import java.awt.BorderLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import mvc.SoundModelInterface;
import mvc.controller.ControllerInterface;

/**
 * View class which represents a display of log text.
 * 
 * @author anthony timotheou
 *
 */
public class ILog extends JFrame {  
  private static final long serialVersionUID = -5885149303282653806L;
  private JTextArea log; // area where text is shown from log
  private ControllerInterface controller;
  private SoundModelInterface model; // not needed currently, but may be in future why it is kept
  
  /**
   * Construct the view by passing in the controller and model. 
   * 
   * @param controller
   * @param model
   */
  public ILog(ControllerInterface controller, SoundModelInterface model) {
    this.controller = controller;
    this.model = model;
    model.registerObserver(this);
    initialise();
  }
  
  /**
   * Initialise by creating a composite object of all the graphical objects and making the window visible. 
   * Also, add the required listeners for clicks and other actions to call the appropriate controller methods. 
   */
  private void initialise() {
    this.setBounds(100, 100, 477, 318);
    log = new JTextArea(5, 20);
    log.setMargin(new Insets(5, 5, 5, 5));
    log.setEditable(false);
    JScrollPane logScrollPane = new JScrollPane(log);
    this.setLayout(new BorderLayout());
    this.add(logScrollPane, BorderLayout.CENTER);
    
    this.addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
          controller.closeWindow();
      }
    });
    
    JMenuBar menuBar = new JMenuBar();
    this.setJMenuBar(menuBar);
    
    JMenu mnFile = new JMenu("File");
    menuBar.add(mnFile);
    
    JMenuItem mntmClose = new JMenuItem("Close");
    mntmClose.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        controller.closeLog();
      }
    });
    mnFile.add(mntmClose);
    
  }
  
  /**
   * set the text of the log.
   * 
   * @param s log text
   */
  public void setText(String s) {
    log.setText(s);
  }
  
  /**
   * Clear the log text.
   * @param s
   */
  public void clearText() {
    log.setText("");
  }
  
}
