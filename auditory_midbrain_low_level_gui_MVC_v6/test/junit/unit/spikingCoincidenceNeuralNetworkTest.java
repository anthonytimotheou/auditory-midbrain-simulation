package junit.unit;

import static org.junit.Assert.*;

import java.util.HashMap;

import mvc.model.Log;
import mvc.model.neuralnetwork.spikingCoincidenceNeuralNetwork;

import org.junit.Test;

public class spikingCoincidenceNeuralNetworkTest {

  @Test
  public void testSetup() {
    HashMap<String, String> params = new HashMap<String, String>();
    params.put("neuronFireThreshold", "30");
    params.put("neuronNumber", "7");
    params.put("neuronTypeChosen", "0");
    params.put("maxDelay", "32");
    params.put("delay90", "32");
    params.put("delay60", "28");
    params.put("delay30", "16");
    params.put("delay0", "0");
    
    spikingCoincidenceNeuralNetwork scnn = new spikingCoincidenceNeuralNetwork(new Log());
    scnn.setup(params);
    
    HashMap<String, String> rParams = scnn.getMp();
    
    assertEquals(rParams.get("neuronFireThreshold"), "30");
    assertEquals(rParams.get("neuronNumber"), "7");
    assertEquals(rParams.get("neuronTypeChosen"), "0");
    assertEquals(rParams.get("maxDelay"), "32");
    assertEquals(rParams.get("delay90"), "32");
    assertEquals(rParams.get("delay60"), "28");
    assertEquals(rParams.get("delay30"), "16");
    assertEquals(rParams.get("delay0"), "0");
  }

  @Test
  public void testExecute() {
    HashMap<String, String> params = new HashMap<String, String>();
    params.put("neuronFireThreshold", "30");
    params.put("neuronNumber", "7");
    params.put("neuronTypeChosen", "0");
    params.put("maxDelay", "32");
    params.put("delay90", "32");
    params.put("delay60", "28");
    params.put("delay30", "16");
    params.put("delay0", "0");
    
    spikingCoincidenceNeuralNetwork scnn = new spikingCoincidenceNeuralNetwork(new Log());
    scnn.setup(params);
    double[][] result = scnn.execute(new double[][] {{1,1,1},{1,1,1}});
    assertEquals(result.length, 7);
    assertEquals(result[0].length, 3+32);
    assertEquals(result[1].length, 3+32);
    assertEquals(result[2].length, 3+32);
    assertEquals(result[3].length, 3+32);
    assertEquals(result[4].length, 3+32);
    assertEquals(result[5].length, 3+32);
    assertEquals(result[6].length, 3+32);
  }

  @Test
  public void testSpikingCoincidenceNeuralNetwork() {
    spikingCoincidenceNeuralNetwork scnn = new spikingCoincidenceNeuralNetwork(new Log());
    assertNotNull(scnn);
  }

  @Test
  public void testGetName() {
    spikingCoincidenceNeuralNetwork scnn = new spikingCoincidenceNeuralNetwork(new Log());
    assertEquals(scnn.getName(), "Spiking Neural Network");
  }

}
