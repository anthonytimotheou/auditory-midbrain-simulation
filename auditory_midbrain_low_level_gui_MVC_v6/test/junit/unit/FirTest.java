package junit.unit;

import static org.junit.Assert.*;

import mvc.model.filter.FIR;

import org.junit.Test;

public class FirTest {

  @Test
  public void testFIR() {
    double[] coefficients = new double[] {0.0001, 0.001};
    FIR f = new FIR(coefficients);
    assertNotNull(f);
    assertEquals(f.getCoefficients(), coefficients);
  }

  @Test
  public void testSetCoefficients() {
    double[] coefficients = new double[] {0.0001, 0.001};
    FIR f = new FIR(new double[] {1,1});
    f.setCoefficients(coefficients);
    assertNotNull(f);
    assertEquals(f.getCoefficients(), coefficients);
  }

  @Test
  public void testReset() {
    FIR f = new FIR(new double[] {1,1});
    f.getOutputSample(0.001);
    assertEquals(f.getSampleCount(), 1);
    f.reset();
    assertEquals(f.getSampleCount(), 0);
    assertNotNull(f);
  }

  @Test
  public void testGetOutputSample() {
    FIR f = new FIR(new double[] {1});
    double d  = f.getOutputSample(10);
    assertNotNull(d);
    assertEquals(d, 10, 0);
  }

}
