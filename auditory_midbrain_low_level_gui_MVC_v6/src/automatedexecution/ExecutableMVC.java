package automatedexecution;


import java.util.ArrayList;
import java.util.HashMap;

import mvc.ModelObserver;
import mvc.model.Log;
import mvc.model.ModelInputTypes;
import mvc.model.SoundLocalisationModel;
import mvc.model.SpikingSoundLocalisationModelConcurrent;
import mvc.model.abstractlayer.InputLayer;
import mvc.model.abstractlayer.OutputLayer;
import mvc.model.abstractlayer.ProcessLayer;
import mvc.model.filter.lowPassFilter;
import mvc.model.neuralnetwork.spikingCoincidenceNeuralNetwork;
import mvc.model.outputlayer.SpikeCountOutput;
import mvc.model.soundgeneration.SoundGeneration;
import mvc.model.soundload.SoundFromFile;
import mvc.model.spiker.meanDifferenceSpiker;
import utilities.ModelUtilities;

/**
 * Class represents the whole model-view-controller aspects of a model and will create all components.
 * 
 * @author anthony timotheou
 *
 */
public class ExecutableMVC implements ModelObserver {
private Log modelLog; // log object required to be passed to model, not used. 
private Log log = new Log();
private String printMessage;
private ThreadSafeFileWriter tsfw;
  
  /**
   * Instantiate passing in the file writer to print its log too. 
   * And the information message to write as well, this is the parameters for the model. 
   * 
   * @param printMessage
   * @param tsfw
   */
  public ExecutableMVC(String printMessage, ThreadSafeFileWriter tsfw) {
    this.printMessage = printMessage;
    this.tsfw = tsfw;
  }
  
  /**
   * Create the MVC in a different way passing just a log for returning and inspecting for
   * its output, not used for threading. 
   * @param modelLog
   */
  public ExecutableMVC(Log modelLog) {
    this.modelLog = modelLog;
  }
  
  /**
   *  handy method to convert degrees to polar axes for use in choose a sound source direction. 
   * @param degrees
   * @return
   *    converted degrees
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

  private InputLayer selectInputLayer(ModelInputTypes m, SoundLocalisationModel model) {
    switch (m) {
      case GENERATEDSIN:
        return new SoundGeneration(log, model.getDegrees());
      case PRECORDEDHRIR:
        return new SoundFromFile(log, convertDegreesToPolar(model.getDegrees()));
      default:
        throw new IllegalArgumentException("No Model Input Type Found.");
    }
  }
  
  /**
   * Execute the model by creating a model, setting the sound source degrees creating all layers and simulating it.
   * 
   * @param mp model parameters
   * @param degrees sound source direction degrees
   * @param mit type of input to run the model with
   */
  public void executeModel(HashMap<String, String> mp, int degrees, ModelInputTypes mit) {
    SoundLocalisationModel model = new SpikingSoundLocalisationModelConcurrent();
    model.setDegrees(degrees);
    model.registerObserver(this);
    InputLayer l = selectInputLayer(mit, model);
    ArrayList<ProcessLayer> p = new ArrayList<ProcessLayer>();
    p.add(new lowPassFilter(log));
    p.add(new meanDifferenceSpiker(log));
    p.add(new spikingCoincidenceNeuralNetwork(log));
    OutputLayer o = new SpikeCountOutput(log);
    model.setLayers(mp, log, l, p, o);
    model.setup();
    model.simulate();
  }

  @Override
  /**
   * Called when the model is executed and updated, prints the results. 
   */
  public void update(double[][] process, int result, int currentLevel, String levelName) {
    if (levelName.equals("Mean Difference Spiker")) {
      int leftIdx = ModelUtilities.firstNonZero(process[1]);
      int rightIdx = ModelUtilities.firstNonZero(process[0]);

      if (tsfw != null) {
        tsfw.addString(printMessage + rightIdx + "\t" + leftIdx + "\t" + (rightIdx - leftIdx) + "\t\n");
      }

      if (modelLog != null) {
        modelLog.addn(rightIdx + "\t" + leftIdx + "\t" + (rightIdx - leftIdx) + "\t\n");
      }
    }
  }

}
