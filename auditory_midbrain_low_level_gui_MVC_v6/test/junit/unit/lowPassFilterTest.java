package junit.unit;

import static org.junit.Assert.*;

import java.util.HashMap;

import mvc.model.Log;
import mvc.model.filter.lowPassFilter;

import org.junit.Test;

public class lowPassFilterTest {

  @Test
  public void testSetup() {
    lowPassFilter lpf = new lowPassFilter(new Log());
    lpf.setup(new HashMap<String, String>());
    assertNotNull(lpf.getF());
    assertNotNull(lpf.getF2());
  }

  @Test
  public void testExecute() {
    double[][] sound = new double[2][44100];
    lowPassFilter lpf = new lowPassFilter(new Log());
    lpf.setup(new HashMap<String, String>());
    double[][] processed = lpf.execute(sound);
    assertNotNull(processed);
    assertEquals(processed.length, 2);
    assertEquals(processed[0].length, 44100);
    assertEquals(processed[1].length, 44100);
  }

  @Test
  public void testLowPassFilter() {
    lowPassFilter lpf = new lowPassFilter(new Log());
    assertNotNull(lpf);
  }

  @Test
  public void testGetName() {
    lowPassFilter lpf = new lowPassFilter(new Log());
    assertEquals(lpf.getName(), "Low Pass Filter");
  }

}
