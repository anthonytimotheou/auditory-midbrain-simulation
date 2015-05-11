package mvc.model;


import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import mvc.ConfigObserver;
import mvc.ModelObserver;
import mvc.SoundModelInterface;
import mvc.model.abstractlayer.InputLayer;
import mvc.model.abstractlayer.OutputLayer;
import mvc.model.abstractlayer.ProcessLayer;

/**
 * This class represents an entire sound localisation model, with one input
 * layer a number of process layers and a output layer. The method setup should
 * be called before any processing, simulate will start the model, currently
 * implemented as concurrent execution, override for custom behaviour. The log
 * method provides information about the model's different layers and is called
 * when needed. The model is tuned according to global parameters which can be
 * changed by seeing ModelParameters.
 * 
 * @author Anthony Timotheou
 * @see inputLayer
 * @see processLayer
 * @see outputLayer
 * @see ModelParameters
 */
public abstract class SoundLocalisationModel implements SoundModelInterface {
  protected double[][] process; // data as it is processed
  protected File configFile; // file with parameters for set up of model
  protected ModelInputTypes modelInputTypes; // type of model input
  protected int degrees; // sound source degrees
  protected String currentLevel; // current level of execution
  protected int result = -1; // intermediate result if set
  protected int level = -1; // intermediate level if set
  
  /**
 * Observers of the model
 */
  protected ArrayList<Object> observers;
    
  /**
 * Global parameters setup for model
 */
  HashMap<String, String> mp;

  /**
 * File holding he parameters to be loaded
 */
  protected File parameterFile;
  
  /**
 * Logging mechanism centralised through this class
 */
  private Log log;

  /**
 * Input layer need return a array of left and right sounds, currently only one input layer is supported
 */
  InputLayer il;

  /**
 * List of process layers which take the sounds, process and output them. Process layers should be added in the order they wish to be executed. Input will be passed from the lowest to the highest. Must have at least one process layer
 */
  ArrayList<ProcessLayer> pl;
  
  /**
 * Output layer which receives last processed sounds and outputs a sound source location
 */
  OutputLayer ol;
  
  public int getDegrees() {
      return degrees;
    }
  
  public void setDegrees(int degrees) {
      this.degrees = degrees;
    }
  
  
  public ModelInputTypes getModelInputTypes() {
      return modelInputTypes;
    }
  
  public void setModelInputTypes(ModelInputTypes modelInputTypes) {
      this.modelInputTypes = modelInputTypes;
    }

  @Override
  public void setConfigFile(File f) {
    this.configFile = f;
    this.notifyObservers();
  }
  
  @Override
  public File getConfigFile() {
    return configFile;
  }

  @Override
  public void inputTypeChange(ModelInputTypes m) {
    modelInputTypes = m;
    this.notifyObservers();
  }
  
  public SoundLocalisationModel() {
    observers = new ArrayList<Object>();
  }
  
  /**
   * Constructor allowing the model parameters and layers to be specified.
   * 
   * @param mp
   *          The global parameters of the model, for each layer needed.
   * @param inputLayer
   *          The input layer to be executed
   * @param processLayer
   *          The process layers to be executed
   * @param outputLayer
   *          The output layer that will return the result
   */
  public SoundLocalisationModel(HashMap<String, String> mp, Log log, InputLayer il, ArrayList<ProcessLayer> pl, OutputLayer ol) {
    setLayers(mp, log, il, pl, ol);
  }

  /**
   * Sets the layers and parameters of the model, need at least one layer in the
   * process layer list.
   * 
   * @param mp
   *          The global parameters of the model, for each layer needed.
   * @param inputLayer
   *          The input layer to be executed
   * @param processLayer
   *          The process layers to be executed
   * @param outputLayer
   *          The output layer that will return the result
   */
  public void setLayers(HashMap<String, String> mp, Log log, InputLayer il, ArrayList<ProcessLayer> pl, OutputLayer ol) {
    this.mp = mp;
    this.log = log;
    this.il = il;
    this.pl = pl;
    this.ol = ol;
  }
  
  /**
   * Sets up the model by calling the corresponding setup functions on all
   * layers, this should be executed before any simulate method is attempted or
   * an error will occur.
   */
  public void setup() {
    log.addSection("Model Setup");
    log.addn("Setting up...");

    il.setup(mp);

    for (ProcessLayer p : pl) {
      p.setup(mp);
    }

    ol.setup(mp);

    log.addn("Set up successfully.");
  }
  
  /**
   * Helper method to convert HRIR data degrees to polar system degrees.
   * @param inDegrees
   * @return
   */
  private int convertDegreesToHorizontalPolarSystem(int inDegrees) {
    switch(inDegrees) {
      case -30:
        return 330;
      case -60:
        return 300;
      case -90:
        return 270;
    }
    
    return inDegrees;
  }
  
  /**
   * Simulates the model by calling execute once on every layer in turn, this is
   * a concurrent implementation and will only execute the input once. To change
   * this behaviour override. Must have at least one process layer.
   */
  public void simulate() {
    level = 0;
    log.addSection("Model Execution.");
    log.add("Executing Model...");
    
    // TODO - bad hack, worth over engineering?
    if (modelInputTypes == ModelInputTypes.PRECORDEDHRIR) {
      degrees = convertDegreesToHorizontalPolarSystem(degrees);
    }
    
    process  = il.execute(); //TODO do i need a seperate double[][] for input or not
    level++;
    currentLevel = il.getName();
    this.notifyObservers();
    
    process = pl.get(0).execute(process); // TODO throws null error if no
    level++;
    currentLevel = pl.get(0).getName();
    this.notifyObservers();
    
    for (int i = 1; i < pl.size(); i++) {
      process = pl.get(i).execute(process);
      level++;
      currentLevel = pl.get(i).getName();
      this.notifyObservers();
    }
    
    result = ol.execute(process);
    level++;
    currentLevel = ol.getName();
    this.notifyObservers();
    
    log.addn("Result: " + result);
    log.addSection("Model Executed Successfully");
    result = -1;
  }
  
  @Override
  public void registerObserver(Object o) {
    observers.add(o);
  }

  @Override
  public void removeObserver(Object o) {
    observers.remove(o);
  }

  @Override
  public void notifyObservers() {
    for (Object o : observers) { //TODO is instanceof really needed?
      if (o instanceof ConfigObserver) {
        ((ConfigObserver) o).update(modelInputTypes, configFile);
      } else {
        if (o instanceof ModelObserver) {
          ((ModelObserver) o).update(process, result, level, currentLevel);
        }
      }
    }
  }

}
