package mvc.model.outputlayer;

import java.util.HashMap;

import mvc.model.Log;
import mvc.model.abstractlayer.OutputLayer;
import utilities.ModelUtilities;

/**
 * A output layer which simply sums up the spikes in each neuron and establishes the 
 * location by the neuron with the most spikes. 
 * 
 * @author Anthony Timotheou
 *
 */
public class SpikeCountOutput extends OutputLayer {
  public SpikeCountOutput(Log log) {
    super(log);
    name = "Spike Count Output";
  }

  /**
 * @uml.property  name="mp"
 */
HashMap<String, String> mp; 
	
	@Override
	public int execute(double[][] input) {
	  /**
		 * Simply does a rolling addition over each neuron, the one with the most
		 * spikes wins, and will be returned. 
		 */
	  
	  log.addSection("Spike Output Counter Layer");
	  log.addn("Executing...");
	  
		// sum number of spikes for each neuron
		int[] sums = new int[input.length];
		
		//TODO might be possibly to sum whole array, should be 0.0 + 1.0 which will end up with result, check if this is faster. 
		// loop through every neurons spikes and increment there counter accordingly
		for (int i = 0; i < input.length; i++) {
			for(int j = 0; j < input[0].length; j ++) { // assume input same length
				if (input[i][j] == 1) { // 1 denotes a spike
					sums[i] += 1; // + 1 for the spike
					log.addn("Array " + i + " has spike at: " + j);
				}
			}
		}
		
		//TODO change this code into own Max function which takes ... dynamic arguments. if 0 then no spike, else spiek occured.  
		// loop through all sums and choose the max one 
		boolean spikeOccured = false;
		int maxSpikeIdx = 0;
		for (int i =0; i < sums.length; i++) {		 
		  if (sums[0] > 0) {
		    spikeOccured = true;
		  }
		    
		  if (sums[i] > sums[maxSpikeIdx]) {
				//TODO redundant reassign of true if spike more than once, shouldn't be an issue. check. 
			  spikeOccured = true;
				maxSpikeIdx = i;
			}
		}
		
		// if spike occured then return the location
		if (spikeOccured == true) {
		  log.addn("Successfully Executed. Degrees Detected: " + ModelUtilities.arrayIndexToSoundSource(maxSpikeIdx));
			return ModelUtilities.arrayIndexToSoundSource(maxSpikeIdx);
		}
    
		log.addn("Successfully Executed. No Degrees Detected.");
		// if none have spiked return no location
		return -181;
	}
	
	
	/**
	 * Get the parameters for this layer.
	 * 
	 * @return parameters 
	 *           Configuration variables for the entire model
	 */
	public HashMap<String, String> getMp() {
    return mp;
  }



  @Override
	public void setup(HashMap<String, String> mp) {
		this.mp = mp;
	}
	
}
