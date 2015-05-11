package mvc.model.abstractlayer;

import java.util.HashMap;

/**
 * This class represents a layer of the model, the model is made up of a number
 * of layers. At the very basic level, each layer must setup itself based on
 * global parameters and log information needed.
 * 
 * @author Anthony Timotheou
 */
abstract class Layer {
  protected String name = null;

  public String getName() {
    return name;
  }

  /**
   * Set up any memory or processing that is required before execution of the
   * layer.
   * 
   * @param mp
   *          Global model parameters
   * @return
   */
  public abstract void setup(HashMap<String, String> mp);

}
