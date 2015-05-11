package junit.unit;

import static org.junit.Assert.*;
import java.util.HashMap;

import mvc.model.Log;
import mvc.model.spiker.meanDifferenceSpiker;

import org.junit.Test;

public class meanDifferenceSpikerTest {

  @Test
  public void testSetup() {
    HashMap<String, String> params = new HashMap<String, String>();
    params.put("inputAudioLength", "200");
    params.put("windowEquidistance", "50");
    params.put("threshold", "0.001");
    params.put("recoverySampleNumber", "5");
    
    meanDifferenceSpiker msd =  new meanDifferenceSpiker(new Log());
    msd.setup(params);
    assertEquals(msd.getInputAudioLength(), 200);
    assertEquals(msd.getWindowEquidistance(), 50);    
  }

  @Test
  public void testExecute() {
    HashMap<String, String> params = new HashMap<String, String>();
    params.put("inputAudioLength", "6");
    params.put("windowEquidistance", "3");
    params.put("threshold", "3");
    params.put("recoverySampleNumber", "5");
    
    meanDifferenceSpiker msd =  new meanDifferenceSpiker(new Log());
    msd.setup(params);
    double[][] result = msd.execute(new double[][] {{1,1,1,10,1,1},{1,1,1,10,1,1}});
    assertNotNull(result);
    assertEquals(result.length, 2);
    assertEquals(result[0].length, 6);
    assertEquals(result[1].length, 6);
    assertEquals(result[0][0], 0, 0);
    assertEquals(result[0][1], 0, 0);
    assertEquals(result[0][2], 0, 0);
    assertEquals(result[0][3], 1, 0);
    assertEquals(result[0][4], 0, 0);
    assertEquals(result[0][5], 0, 0);
    assertEquals(result[1][0], 0, 0);
    assertEquals(result[1][1], 0, 0);
    assertEquals(result[1][2], 0, 0);
    assertEquals(result[1][3], 1, 0);
    assertEquals(result[1][4], 0, 0);
    assertEquals(result[1][5], 0, 0);
  }

  @Test
  public void testMeanDifferenceSpiker() {
    meanDifferenceSpiker msd = new meanDifferenceSpiker(new Log());
    assertNotNull(msd);
  }

  @Test
  public void testGetName() {
    meanDifferenceSpiker msd = new meanDifferenceSpiker(new Log());
    assertEquals(msd.getName(), "Mean Difference Spiker");
  }

}
