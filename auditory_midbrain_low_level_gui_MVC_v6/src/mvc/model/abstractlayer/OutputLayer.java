package mvc.model.abstractlayer;

import mvc.model.Log;

/**
 * This layer represents an output layer of the model, it must determine a sound
 * source location from the processed data passed to it.
 * 
 * @author Anthony Timotheou
 */
public abstract class OutputLayer extends Layer {

  protected Log log;

  public OutputLayer(Log log) {
    this.log = log;
  }

  /**
   * This method will take the processed sound and calculate the sound source
   * location from it.
   * 
   * @param input
   *          Processed sound passed to this layer
   * @return Sound source location in degrees
   */
  public abstract int execute(double[][] input);
}
