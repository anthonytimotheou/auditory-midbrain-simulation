package junit.unit;

import static org.junit.Assert.*;

import java.util.HashMap;

import mvc.model.Log;
import mvc.model.neuralnetwork.spikingCoincidenceNeuralNetwork;
import mvc.model.outputlayer.SpikeCountOutput;

import org.junit.Test;

public class SpikeCountOutputTest {

  @Test
  public void testSetup() {
    SpikeCountOutput sc = new SpikeCountOutput(new Log());
    assertNull(sc.getMp());
    sc.setup(new HashMap<String, String>());
    assertNotNull(sc.getMp());
  }

  @Test
  public void testExecute() {
    SpikeCountOutput sc = new SpikeCountOutput(new Log());
    assertNull(sc.getMp());
    sc.setup(new HashMap<String, String>());
    assertNotNull(sc.getMp());
    int result = sc.execute(new double[][] {{0,0,0},{0,0,0},{0,0,0},{0,1,0},{0,0,0},{0,0,0},{0,0,0}});
    assertEquals(result, 0);
    int result2 = sc.execute(new double[][] {{1,0,0},{0,0,0},{0,0,0},{0,0,0},{0,0,0},{0,0,0},{0,0,0}});
    assertEquals(result2, -90);
    int result3 = sc.execute(new double[][] {{0,0,0},{0,0,0},{0,0,0},{0,0,0},{0,0,0},{0,0,0},{0,1,0}});
    assertEquals(result3, 90);
  }

  @Test
  public void testSpikeCountOutput() {
    SpikeCountOutput sc = new SpikeCountOutput(new Log());
    assertNotNull(sc);
  }

  @Test
  public void testGetName() {
    SpikeCountOutput sc = new SpikeCountOutput(new Log());
    assertEquals(sc.getName(), "Spike Count Output");
  }

}
