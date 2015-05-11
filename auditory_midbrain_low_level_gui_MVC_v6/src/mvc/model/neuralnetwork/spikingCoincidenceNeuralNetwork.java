package mvc.model.neuralnetwork;

import java.util.HashMap;

import mvc.model.Log;
import mvc.model.abstractlayer.ProcessLayer;
import utilities.ModelUtilities;

/**
 * Spiking neural network configured to hold a number of coincidence neurons which fire for a certain degrees 
 * of sound by using different delay lines on a linear set of neurons. Currently supports -90 to 90 degrees in
 * 30 degree intervals. Currently implemented in a primitive way to increase computational efficiency, although
 * it does this by sacrificing flexibility. Simulates the model as a spiking neural network based on time. 
 * Model time granularity is dependent on sampling rate, as it loops per sample if the sampling rate is high, say 
 * 44100Hz then each sample represents 22.6 microseconds in time and that is the granularity of the model, the 
 * lower sample rate the lower the granularity
 * 
 * @author Anthony Timotheou
 *
 */
public class spikingCoincidenceNeuralNetwork extends ProcessLayer {
	public spikingCoincidenceNeuralNetwork(Log log) {
    super(log);
    name = "Spiking Neural Network";
  }

  /**
 * Global parameters of the model
 * @uml.property  name="mp"
 * @uml.associationEnd  qualifier="constant:java.lang.String java.lang.String"
 */
  HashMap<String, String> mp;
  
  /**
 * Contains all neurons which will be used for coincidence detection including each neurons parameter, allowing customisation per neuron, set on setup.
 * @uml.property  name="neurons" multiplicity="(0 -1)" dimension="2"
 */
	double[][] neurons = null;
  
	/**
	 * Represents the membrane potential of the neuron
	 * @uml.property  name="v" multiplicity="(0 -1)" dimension="1"
	 */
	double[] v = null;
  
	/**
	 * Represents the recovery period of the neurons
	 * @uml.property  name="u" multiplicity="(0 -1)" dimension="1"
	 */
	double[] u = null;
  
	/**
	 * The firing rates of all neurons over the time period they are simulated for, currently hard-coded to be based on the IzhikevichNeuron layout of parameters.
	 * @see  IzhikevichNeuron
	 * @uml.property  name="firingRates" multiplicity="(0 -1)" dimension="2"
	 */
	double[][] firingRates = null;
  
	/**
	 * The input to all neurons over the time period they are simulated for
	 * @uml.property  name="neuronInput" multiplicity="(0 -1)" dimension="2"
	 */
	int[][] neuronInput = null;

	/**
	 * Sets up the base set of the neurons used in the simulation. By initialising the set, adding parameters from global parameters, 
	 * setting delays from global parameters, and returning the set of neurons.
	 * 
	 * @return
	 *     Get all neurons. 
	 */
	private double[][] getNeuronBaseSet() {
		/** initialised array to hold neurons and their parameters */
	  double[][] neurons = new double[Integer.parseInt(mp.get("neuronNumber"))][IzhikevichNeuron.neuronType[0].length]; // parameter length assumed equal to first array length
		
	  //loop through all neurons and set parameters according to the chosen neuron type in the global parameters
		for (int i = 0; i < neurons.length; i++) {
			for (int j = 0; j < neurons[0].length; j ++) {
				neurons[i][j] = IzhikevichNeuron.neuronType[Integer.parseInt(mp.get("neuronTypeChosen"))][j];
			}
		}
		
		// TODO Check that this shit is right for the degrees chosen, e.g. -90 is really the correct delay
		// set neuron weights
		neurons[0][4] = Integer.parseInt(mp.get("delay90"));
		neurons[0][5] = Integer.parseInt(mp.get("delay0"));
		
		neurons[1][4] = Integer.parseInt(mp.get("delay60"));
		neurons[1][5] = Integer.parseInt(mp.get("delay0"));
		
		neurons[2][4] = Integer.parseInt(mp.get("delay30"));
		neurons[2][5] = Integer.parseInt(mp.get("delay0"));
		
		neurons[3][4] = Integer.parseInt(mp.get("delay0"));
		neurons[3][5] = Integer.parseInt(mp.get("delay0"));
		
		neurons[4][4] = Integer.parseInt(mp.get("delay0"));
		neurons[4][5] = Integer.parseInt(mp.get("delay30"));
		
		neurons[5][4] = Integer.parseInt(mp.get("delay0"));
		neurons[5][5] = Integer.parseInt(mp.get("delay60"));
		
		neurons[6][4] = Integer.parseInt(mp.get("delay0"));
		neurons[6][5] = Integer.parseInt(mp.get("delay90"));
		
		return neurons;
	}
	
	@Override
	public double[][] execute(double[][] input) {
	  // Initialise firing rate and neuron input arrays, because input can be delayed to the neuron
    // the simulation must run for the entire input length plus the delay allowed incase a firing
    // or input happens after the end of the simulation. Initialised here for input length
    firingRates = new double[Integer.parseInt(mp.get("neuronNumber"))][input[0].length+Integer.parseInt(mp.get("maxDelay"))];
    neuronInput = new int[Integer.parseInt(mp.get("neuronNumber"))][input[0].length+Integer.parseInt(mp.get("maxDelay"))];  
    int leftEarLength = input[0].length;
    int rightEarLength = input[1].length;

	  // loop through each sample in the ear sounds 
	    //TODO not simulating for total time plus max delay, must it do this? CHECK!
	    for (int j = 0; j < Math.max(leftEarLength, rightEarLength); j++) { // assumed input length equal for both ear sounds
	      // for every neuron in each sample 
	      for (int k = 0; k < neurons.length; k++) {
	        	
	          // if the left ear for this current time step has spiked then 
	          // add input to the neuron giving the delay specified in 
	          // the neuron base set created earlier
	          
	          if (j < leftEarLength && input[0][j] == 1) {
	        	  neuronInput[k][(int) (j+neurons[k][4])] += neurons[k][6];  
	          }
	        	
	          // Do the same as above for the right ear
	          if (j < rightEarLength && input[1][j] == 1) {
	        	  neuronInput[k][(int) (j+neurons[k][5])] += neurons[k][6];  
	          }
	          
	          // if the neuron has fired, based on threshold in global parameters then reset it
	          if (v[k] > Integer.parseInt(mp.get("neuronFireThreshold"))) {
	            firingRates[k][j] = 1; // log the fire  
	            v[k] = neurons[k][2]; // update membrane potential
	            u[k] = u[k] + neurons[k][3]; // update recovery potential
	          }
	          
	          //TODO add reference to Izhivekich formulae for neural network
	          // update the membrane potential for the network of this neuron based on Izhivekich formulae 
	          v[k] = v[k] + 0.5 * ((0.04 * v[k] + 5) * v[k] + 140 - u[k] + neuronInput[k][j]);
	          v[k] = v[k] + 0.5 * ((0.04 * v[k] + 5) * v[k] + 140 - u[k] + neuronInput[k][j]);
	          u[k] = u[k] + neurons[k][0] * (neurons[k][1] * v[k] - u[k]);
	        }
	      }
		
		return firingRates;
	}
	
	/**
	 * Used for junit testing to check the parameters load ok. 
	 */
	public HashMap<String, String> getMp() {
	  return mp; 
	}
	
  @Override
	public void setup(HashMap<String, String> mp) {
	    this.mp = mp; // set global parameters 
	    
	    ModelUtilities.checkParamExists(mp, "delay90");
      ModelUtilities.checkParamExists(mp, "delay60");
      ModelUtilities.checkParamExists(mp, "delay30");
      ModelUtilities.checkParamExists(mp, "delay0");
      ModelUtilities.checkParamExists(mp, "neuronNumber");
      ModelUtilities.checkParamExists(mp, "neuronTypeChosen");
      ModelUtilities.checkParamExists(mp, "maxDelay");
      ModelUtilities.checkParamExists(mp, "neuronFireThreshold");
	    
	    neurons = getNeuronBaseSet(); // create base set of neurons
	    v = new double[Integer.parseInt(mp.get("neuronNumber"))]; // initialise membrane potential
	    u = new double[Integer.parseInt(mp.get("neuronNumber"))]; // initialise recovery potential
	    
	    // initialise neurons membrane potential and recovery potential
	    for (int i = 0; i < Integer.parseInt(mp.get("neuronNumber")); i++) {
	      v[i] = neurons[i][2];
	      u[i] = neurons[i][1] * v[i];
	    }

	}
}
