package mvc.controller;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import mvc.SoundModelInterface;
import mvc.model.Log;
import mvc.model.ModelInputTypes;
import mvc.model.abstractlayer.InputLayer;
import mvc.model.abstractlayer.OutputLayer;
import mvc.model.abstractlayer.ProcessLayer;
import mvc.model.filter.lowPassFilter;
import mvc.model.neuralnetwork.spikingCoincidenceNeuralNetwork;
import mvc.model.outputlayer.SpikeCountOutput;
import mvc.model.soundgeneration.SoundGeneration;
import mvc.model.soundload.SoundFromFile;
import mvc.model.spiker.meanDifferenceSpiker;
import mvc.view.ILog;
import mvc.view.IMain;
import mvc.view.ISettings;
import utilities.ModelUtilities;

/**
 * Model Controller class which is a generic controller for the model with a GUI. 
 * 
 * @author anthony timotheou
 *
 */
public class ModelController implements ControllerInterface {
  private SoundModelInterface model;  
  private IMain iMainView;
  private ISettings iSettingsView;
  private ILog iLogView;
  private Log log = new Log();
  
  /**
   * Constructor requiring a reference to the model. Creates all views as well. 
   * 
   * @param model
   */
  public ModelController(SoundModelInterface model) {
    this.model = model;
    this.iMainView = new IMain(this, model);
    this.iSettingsView = new ISettings(this, model);
    this.iLogView = new ILog(this, model);
    initialise();
  }
  
  /**
   * Method which cleans up after closing the log. 
   */
  public void closeLog() {
    iMainView.setEnabled(true);
    iLogView.setVisible(false);
  }
  
  /**
   * initialise the view. 
   */
  public void initialise() {
    iMainView.setEnabledBtnStart(false);
  }
  
  @Override
  /**
   * terminates the program.
   */
  public void terminateProgram() {
    iMainView.close();
  }
  
  @Override
  /**
   * cleans up after closing a window. 
   */
  public void closeWindow() {
    iMainView.setEnabled(true);
  }
  
  @Override
  /**
   * cleans up after closing the settings dialog. 
   */
  public void closeSettings() {
    iMainView.setEnabled(true);
    iSettingsView.setVisible(false);
    
    if (model.getConfigFile() != null) {
      iMainView.setEnabledBtnStart(true);
    } else {
      iMainView.setEnabledBtnStart(false);
    }
      
  }
  
  @Override
  /**
   * call the settings view. 
   */
  public void settings() {
    iMainView.setEnabled(false);
    iSettingsView.setVisible(true);
  }

  @Override
  /**
   * call the logs view. 
   */
  public void logs() {
    iMainView.setEnabled(false);
    iLogView.setVisible(true);
  }

  @Override
  /**
   * changed input type of the model. 
   */
  public void inputTypeChange(ModelInputTypes m) {
    model.inputTypeChange(m);
  }
  
  @Override
  /**
   * changes set file of the configuration of the model. 
   */
  public void setFile(File f) {
    //iSettingsView.setVisible(false); // TODO find out why this line breaks GUI when choosing a config file
    model.setConfigFile(f);
  }
  
  /**
   * Helper method to convert degrees to polar
   * 
   * @param degrees
   * @return
   */
  private int convertDegreesToPolar(int degrees) {
    switch (degrees) {
      case -90:
        return 270;
      case -60:
        return 300;
      case -30:
        return 330;
     default:
        return degrees;
    }
  }
  
  /**
   * Helper method to get the input layer from the model input type selected.
   * 
   * @param m
   * @return
   */
  public InputLayer selectInputLayer(ModelInputTypes m) {
    switch(m) {
      case GENERATEDSIN:
        return new SoundGeneration(log, model.getDegrees());
      case PRECORDEDHRIR:
        return new SoundFromFile(log, convertDegreesToPolar(model.getDegrees()));
      default: 
        throw new IllegalArgumentException("No Model Input Type Found.");
    }
  }
  
  @Override
  /**
   * Method to execute the model, instantiates all required layers and parameters and passing these on to
   * the model for execution. Updates for output as well. 
   */
  public void runModel() {
    HashMap<String, String> mp;
    try {
      iMainView.clearSoundSourceDisplay();
      iMainView.setUpdateLevel(1);
      iMainView.setEnabledBtnSetup(false);
      mp = ModelUtilities.loadParameters(model.getConfigFile());
      InputLayer l = selectInputLayer(model.getModelInputTypes()); 
      ArrayList<ProcessLayer> p = new ArrayList<ProcessLayer>();
      p.add(new lowPassFilter(log));
      p.add(new meanDifferenceSpiker(log));
      p.add(new spikingCoincidenceNeuralNetwork(log));
      OutputLayer o = new SpikeCountOutput(log);
      model.setLayers(mp, log, l, p, o);
      model.setup();
      model.simulate();
      iLogView.setText(log.getLog().toString());
      iMainView.setEnabledBtnSetup(true);
      } catch (IOException e) {
        e.printStackTrace();
      }
  }

  @Override
  /**
   * method if the sound source degrees is updated. 
   */
  public void soundSourceDegreesUpdate(int selectedItem) {
    model.setDegrees(selectedItem);
  }

}
