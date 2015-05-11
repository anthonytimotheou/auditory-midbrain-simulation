package mvc.model;


import java.util.ArrayList;
import java.util.HashMap;

import mvc.model.abstractlayer.InputLayer;
import mvc.model.abstractlayer.OutputLayer;
import mvc.model.abstractlayer.ProcessLayer;

/**
 * A concurrent Spiking sound localisation model, inherits all classes from above as they are
 * implemented concurrently. Therefore nothing is required to change.
 * 
 * @author Anthony Timotheou
 * @see inputLayer
 * @see processLayer
 * @see outputLayer
 * @see ModelParameters
 * 
 */
public class SpikingSoundLocalisationModelConcurrent extends SoundLocalisationModel  {
	/**
	 * Constructor allowing the model parameters and layers to be specified.
	 * 
	 * @param mp 
	 * 		The global parameters of the model, for each layer needed. 
	 * @param inputLayer
	 * 		The input layer to be executed
	 * @param processLayer
	 * 		The process layers to be executed
	 * @param outputLayer
	 * 		The output layer that will return the result
	 */
	public SpikingSoundLocalisationModelConcurrent(HashMap<String, String> mp, Log log, InputLayer inputLayer, ArrayList<ProcessLayer> processLayer, OutputLayer outputLayer) {
		super(mp, log, inputLayer, processLayer, outputLayer);
	}

	/**
	 * Construct a sound localisation model with no layers, these must be set later. 
	 */
  public SpikingSoundLocalisationModelConcurrent() {
  }
  
}
