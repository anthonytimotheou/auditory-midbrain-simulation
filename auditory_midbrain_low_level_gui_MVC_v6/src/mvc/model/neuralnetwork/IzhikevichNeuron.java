package mvc.model.neuralnetwork;

//TODO add reference to izhikevick work on neurons
/**
 * This class represents a Izhikevick neuron which is composed of four parameters and a number of 
 * types can be simulated. Initial weight of the neuron and it's input when spiked is also specified. 
 * 
 * @author Anthony Timotheou
 *
 */
public class IzhikevichNeuron {
  /**
   * Array holding different parameters for the various types of izhikevick neurons
   * The array is composed as follows: a, b, c, d, initialWeight, input. 
   */
  public static double[][] neuronType =  {
      { 0.008, 0.2, -65, 6, 0, 0, 12}, // tonic
      { 0.02, 0.25, -65, 6, 0, 0, 14}, // phasic
      { 0.02, 0.2, -65, 6, 0, 0, 12}, // integrator
    };
}
