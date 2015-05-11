package mvc.model.abstractlayer;

import mvc.model.Log;

/**
 * This class represents a processing layer of the model, which is tasked with
 * processing the input or the output of a previous processing layer and
 * outputting its result to the next layer.
 * 
 * @author Anthony Timotheou
 */
public abstract class ProcessLayer extends Layer {
  protected Log log;

  public ProcessLayer(Log log) {
    this.log = log;
  }

  /**
   * Method which executes this process layer doing all calculation and
   * returning the processed audio data.
   * 
   * @param input
   *          Processed or input ear sounds passed in
   * @return Processed ear sounds passed out
   */
  public abstract double[][] execute(double[][] input);
}
