package mvc.view;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.LayoutStyle.ComponentPlacement;

import utilities.ModelUtilities;

import mvc.ModelObserver;
import mvc.SoundModelInterface;
import mvc.controller.ControllerInterface;

import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

/**
 * Class represents the view of the main applciation window that opens on start up.
 * Provides all functionality and calls to controller for events when appropriate. 
 * 
 * @author anthony timotheou
 *
 */
public class IMain implements ModelObserver {
	SoundModelInterface model;
	ControllerInterface controller;
	private JFrame frame;
	// Instantiate other view objects and provide them with required configuration parameters
	ISoundSourceDisplay soundSourceDisplay = new ISoundSourceDisplay(7, -90, 30, 90);
	IWaveformDisplay leftWaveForm = new IWaveformDisplay("Left Waveform");
	IWaveformDisplay rightWaveForm = new IWaveformDisplay("Right Waveform");
	JButton btnSetup;
	JButton btnStart;
	JButton btnLog;
	int updateLevel = 1;
	
	/**
	 * Clear the sound source direction displayed. 
	 */
	public void clearSoundSourceDisplay() {
	  soundSourceDisplay.clearFiring();
	}
	
	/**
	 * Update the level.
	 *  
	 * @param i the level to update to. 
	 */
	public void setUpdateLevel(int i) {
	  updateLevel = i;
	}
	
	/**
	 * Construct the view with the controller and model required by the MVC design pattern. 
	 * 
	 * @param controller
	 * @param model
	 */
	public IMain (ControllerInterface controller, SoundModelInterface model) {
	  this.model = model;
	  this.controller = controller;
	  model.registerObserver(this);
	  initialize();
	}
	
	/**
	 * Change the set up buttons ability to be clicked. 
	 * @param b enable or disable
	 */
	public void setEnabledBtnSetup(boolean b) {
	  btnSetup.setEnabled(b);
	}
	
	/**
   * Change the start buttons ability to be clicked. 
   * @param b enable or disable
   */
	public void setEnabledBtnStart(boolean b) {
    btnStart.setEnabled(b);
  }
	
	/**
   * Change the stop buttons ability to be clicked. 
   * @param b enable or disable
   */
	public void setEnabledBtnStop(boolean b) {
    btnSetup.setEnabled(b);
  }
	
	/**
   * Change the windows main ability to be clicked. 
   * @param b enable or disable
   */
	public void setEnabled(boolean b) {
	  frame.setEnabled(b);
	}	
	
	/**
	 * Initialize the contents of the frame. Create the composite object and add all gui components to it.
	 * Set it's visibility to true to display the window. 
	 */
	public void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 477, 318);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				
		btnStart = new JButton("Run Model");
		btnStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			  controller.runModel();
			}
		});
		
		btnSetup = new JButton("Settings");
		btnSetup.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			  controller.settings();
			}
		});
		
		btnLog = new JButton("Logs");
		btnLog.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			  controller.logs();
			}
		});
		
		FlowLayout flowLayout = (FlowLayout) soundSourceDisplay.getLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);
		
		JLabel lblSpikingSoundLocalisation = new JLabel("Spiking Sound Localisation Model of the Auditory Mibrain");
		
		GroupLayout groupLayout = new GroupLayout(frame.getContentPane());
		groupLayout.setHorizontalGroup(
		  groupLayout.createParallelGroup(Alignment.LEADING)
		    .addGroup(groupLayout.createSequentialGroup()
		      .addContainerGap()
		      .addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
		        .addGroup(groupLayout.createSequentialGroup()
		          .addComponent(soundSourceDisplay, GroupLayout.DEFAULT_SIZE, 322, Short.MAX_VALUE)
		          .addPreferredGap(ComponentPlacement.RELATED)
		          .addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
		            .addComponent(leftWaveForm, GroupLayout.DEFAULT_SIZE, 113, GroupLayout.PREFERRED_SIZE)
		            .addComponent(rightWaveForm, GroupLayout.PREFERRED_SIZE, 113, GroupLayout.PREFERRED_SIZE)))
		        .addGroup(groupLayout.createSequentialGroup()
		          .addComponent(btnStart)
		          .addPreferredGap(ComponentPlacement.RELATED)
		          .addComponent(btnSetup)
		          .addPreferredGap(ComponentPlacement.RELATED)
		          .addComponent(btnLog))
		        .addComponent(lblSpikingSoundLocalisation))
		      .addContainerGap())
		);
		groupLayout.setVerticalGroup(
		  groupLayout.createParallelGroup(Alignment.LEADING)
		    .addGroup(groupLayout.createSequentialGroup()
		      .addGap(7)
		      .addComponent(lblSpikingSoundLocalisation)
		      .addPreferredGap(ComponentPlacement.RELATED)
		      .addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
		        .addGroup(groupLayout.createSequentialGroup()
		          .addComponent(leftWaveForm, GroupLayout.PREFERRED_SIZE, 82, GroupLayout.PREFERRED_SIZE)
		          .addPreferredGap(ComponentPlacement.RELATED)
		          .addComponent(rightWaveForm, GroupLayout.PREFERRED_SIZE, 82, GroupLayout.PREFERRED_SIZE))
		        .addComponent(soundSourceDisplay, GroupLayout.DEFAULT_SIZE, 170, Short.MAX_VALUE))
		      .addGap(18)
		      .addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
		        .addComponent(btnStart)
		        .addComponent(btnSetup)
		        .addComponent(btnLog))
		      .addGap(42))
		);
		frame.getContentPane().setLayout(groupLayout);
		
		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);
		
		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);
		
		JMenuItem mntmClose = new JMenuItem("Close");
		mntmClose.addActionListener(new ActionListener() {
		  public void actionPerformed(ActionEvent e) {
		    controller.terminateProgram();
		  }
		});
		mnFile.add(mntmClose);
		frame.setVisible(true);
	}
	
	/**
	 * close the window. 
	 */
	public void close() {
	  frame.setDefaultCloseOperation(frame.DISPOSE_ON_CLOSE);
	  frame.dispose();
	}
	
  @Override
  /**
   * When an update is called form the model, update all components with the data required. 
   */
  public void update(double[][] process, int result, int currentLevel, String layerName) {
    if (process != null) {
      System.out.println("Settings Data: " + currentLevel);
      if (updateLevel == currentLevel) {
        System.out.println("Updating... " + currentLevel);
        leftWaveForm.setData(process[0]);
        rightWaveForm.setData(process[1]);
      }
    }
    
    if (result != -1 && result != -181) {
      soundSourceDisplay.fireSoundSource(ModelUtilities.soundSourceToArrayIndex(result));
    }
    
    if (result == -181) {
      soundSourceDisplay.noResult();
    }
    
  }
}
