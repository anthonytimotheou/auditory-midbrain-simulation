package mvc.model.abstractlayer;

import mvc.model.Log;

/**
 * This class represents an input layer that gathers and provides the input to
 * the processing layers.
 * 
 * @author Anthony Timotheou
 * @see processLayer
 * @see SoundLocalisationModel
 */
public abstract class InputLayer extends Layer {
  protected Log log;

  protected int degrees;

  public InputLayer(Log log, int degrees) {
    this.log = log;
    this.degrees = degrees;
  }

  /**
   * Execute method should gather the ear sounds and return a array of two by
   * the length of sound, for each ear.
   * 
   * @return Return ear data that has been gathered, left and right ears.
   */
  public abstract double[][] execute();
}
