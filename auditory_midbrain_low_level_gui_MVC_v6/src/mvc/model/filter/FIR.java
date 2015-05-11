package mvc.model.filter;

/**
 * Class representing a Finite Impulse Response filter, which operates on a
 * distinct number of samples in a discrete domain. Provides methods to set the
 * coefficients, reset the loop for gathering output and get the output audio
 * sample.
 * 
 * @author Anthony Timotheou
 */
public class FIR {

  /**
   * Represents the delay line of the filter, the number of previous samples
   * which will be used when providing current input
   * 
   * @uml.property name="delayLine" multiplicity="(0 -1)" dimension="1"
   */
  private double[] summationLine;

  /**
   * Impulse response of the filter is it's coefficients, used when filtering
   * 
   * @uml.property name="coefficients" multiplicity="(0 -1)" dimension="1"
   */
  private double[] coefficients;

  /**
   * Count used to keep the delay line in step with the rest of the audio data
   * 
   * @uml.property name="count"
   */
  private int sampleCount = 0;

  /**
   * Constructor method which sets the coefficients of the filter.
   * 
   * @param coefficients
   *          The coefficients of the filter, it's impulse response.
   */
  public FIR(double[] coefficients) {
    this.setCoefficients(coefficients);
    summationLine = new double[coefficients.length];
  }

  /**
   * @param coefficients
   *          The coefficients of the filter, it's impulse response.
   * @uml.property name="coefficients"
   */
  public void setCoefficients(double[] coefficients) {
    this.coefficients = coefficients;
  }
  
  
  /**
   * Get the coefficients this Finite Impulse Response filter represents
   * 
   * @return coefficients
   *            the coefficients of the filter
   */
  public double[] getCoefficients() {
    return coefficients;
  }

  /**
   * Resets the FIR filter to be used again in filtering new audio
   */
  public void reset() {
    sampleCount = 0;
  }
  
  
  /**
   * Return the current sample count, used in applying the coefficients.
   * 
   * @return sampleCount
   *            Current count of samples being interpreted by this filter. 
   */
  public int getSampleCount() {
    return sampleCount;
  }

  // TODO reference where it was taken from, explain getOutputSample method
  // better?
  /**
   * This method gets the filtered audio data by a simple summation of the
   * coefficients to all previous filter samples in the delay line (specified by
   * the length of the coefficients) and multiples the coefficient by the
   * sample. The result is returned and is one filtered sample for one sample
   * input.
   * 
   * @param inputSample
   *          One sample of audio
   * @return One sample of filtered audio
   */
  public double getOutputSample(double inputSample) {
    summationLine[sampleCount] = inputSample;
    
    double result = 0.0;
    
    int index = sampleCount;

    for (int i = 0; i < coefficients.length; i++) {

      result += coefficients[i] * summationLine[index--];

      if (index < 0) {
        index = coefficients.length - 1;
      }

    }

    if (++sampleCount >= coefficients.length) {
      sampleCount = 0;
    }

    return result;
  }
}
