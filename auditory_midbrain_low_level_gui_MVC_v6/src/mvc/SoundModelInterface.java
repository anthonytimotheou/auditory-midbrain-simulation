package mvc;


import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import mvc.model.Log;
import mvc.model.ModelInputTypes;
import mvc.model.abstractlayer.InputLayer;
import mvc.model.abstractlayer.OutputLayer;
import mvc.model.abstractlayer.ProcessLayer;

/**
 * Interface to represent the model. Logic method are stored here and methods for updating all 
 * objects registered as a listener for state changes. 
 * 
 * @author   anthony timotheou
 */
public interface SoundModelInterface {
  // observer pattern methods
  public void registerObserver(Object o); 
  public void removeObserver(Object o);
  public void notifyObservers();
  // set and get configuration file 
  public void setConfigFile(File f);
  public File getConfigFile();
  // change input type
  public void inputTypeChange(ModelInputTypes m);
  // set layers for execute
  public void setLayers(HashMap<String, String> mp, Log log, InputLayer il, ArrayList<ProcessLayer> pl, OutputLayer ol);
  // set up model ready for execution
  public void setup();
  // execute the model
  public void simulate();
  // get the sound source degrees for execution
  public int getDegrees();
  // get the model input type
  public ModelInputTypes getModelInputTypes();
  // set the degrees
  public void setDegrees(int degrees);
}
