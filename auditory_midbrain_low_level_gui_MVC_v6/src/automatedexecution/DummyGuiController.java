package automatedexecution;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import utilities.ModelUtilities;

import mvc.SoundModelInterface;
import mvc.controller.ControllerInterface;
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


/**
 * A different controller used for the dummy GUI. Allows different functionality. 
 * 
 * @author anthony timotheou
 *
 */
public class DummyGuiController implements ControllerInterface {

SoundModelInterface model;  

DummyGui view;

Log log = new Log();
  
/**
 * Constructor that executes the model. 
 * 
 * @param model
 */
  public DummyGuiController(SoundModelInterface model) {
    this.model = model;
    view = new DummyGui(model);
    this.runModel();
  }
  
  /**
   * Helper method to convert degrees to polar axes
   * @param degrees
   * @return converted degrees
   */
  private static int convertDegreesToPolar(int degrees) {
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
   * Method to select input layer type of model based on model type.  
   * 
   * @param m
   * @return input layer.
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
   * execute the model, loading the configuration file and creating and adding all layers then simulating the model. 
   */
  public void runModel() {
    HashMap<String, String> mp;
    model.setConfigFile(new File("C:\\Users\\unknown\\Dropbox\\Eclipse WorkSpaces\\Computer Science Level 3\\auditory_midbrain_low_level_gui_MVC_v4\\GeneratedSinDataConfig.param"));
    model.setDegrees(0);
    try {
      mp = ModelUtilities.loadParameters(model.getConfigFile());
      InputLayer l = selectInputLayer(ModelInputTypes.GENERATEDSIN); 
      ArrayList<ProcessLayer> p = new ArrayList<ProcessLayer>();
      p.add(new lowPassFilter(log));
      p.add(new meanDifferenceSpiker(log));
      p.add(new spikingCoincidenceNeuralNetwork(log));
      OutputLayer o = new SpikeCountOutput(log);
      model.setLayers(mp, log, l, p, o);
      model.setup();
      model.simulate();
      System.out.println(log.getLog().toString());
      } catch (IOException e) {
        e.printStackTrace();
      }    
  }

  // Uneeded method stubs from interface, ignored if called in model. Only want to print the required log output. 
  
  @Override
  public void settings() {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void logs() {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void inputTypeChange(ModelInputTypes m) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void setFile(File f) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void closeWindow() {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void soundSourceDegreesUpdate(int selectedItem) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void closeSettings() {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void terminateProgram() {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void closeLog() {
    // TODO Auto-generated method stub
    
  }

}
