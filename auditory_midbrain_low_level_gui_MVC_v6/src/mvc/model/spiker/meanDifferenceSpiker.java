package mvc.model.spiker;

import java.util.HashMap;

import mvc.model.Log;
import mvc.model.abstractlayer.ProcessLayer;
import utilities.ModelUtilities;

/**
 * This process layer simulates the impulses of the auditory nerve by mimicking
 * the cochlea spiking rhythms on the inner hair cells of the auditory nerve. 
 * This layer takes in audio data and produces spikes by splitting the audio data
 * into windowed sample means and then if the mean goes up it shows the waveform
 * is increasing so spike, if it goes down, then it is decreasing so do nothing.
 * A threshold of the amount it must go up by is applied to deal with the small up
 * and down mean of noise. The user must tune these two parameters effectively for
 * the frequency of the waveform.   
 * 
 * @author Anthony Timotheou
 */
public class meanDifferenceSpiker extends ProcessLayer {
  public meanDifferenceSpiker(Log log) {
    super(log);
    name = "Mean Difference Spiker";
  }

  /**
 * Global model parameters
 */
  HashMap<String, String> mp;

  /**
 * length of input audio, convenient variable to prevent multiple calls to length of array
 */
  int inputAudioLength = -1;

  /**
 * Holds the spike trains generated from the incoming audio signal
 */
  double[][] spikeTrains = null;

  /**
 * holds the number of times the incoming signal shall be split before analysed for spikes
 */
  int windowEquidistance = -1;

  /**
 * determines the number of samples per window = signal length / window equidistance
 */
  int samplesPerWindow = -1; // check this works

  /**
 * Threshold describes the value of mean difference in samples to generate a spike
 */
  double threshold = -1; 
  // TODO changed audio to normalised so this can be specified more generally
  // TODO look at System.arraycopy(oldArray, srcPos, newArray, destPos, length);
  
  /**
 */
int recoverySampleNumber = -1; 
  
  /**
 */
double recoverySampleThreshold = -1;
  
  /**
 */
boolean spikeOccured;
  
  @Override
  public double[][] execute(double[][] input) {
    log.addSection("Mean Difference Spiker Layer");
    int leftEarLength = input[0].length;
    int rightEarLength = input[1].length;
    spikeTrains = new double[2][Math.max(leftEarLength, rightEarLength)]; // both ear sounds assumed to be equal length

    for (int i = 0; i < 2; i++) {
      log.addn("Executing Model for sound array " + i);
      double lastSampleMean = ModelUtilities.mean(ModelUtilities.getArrayPart(input[i], 0, samplesPerWindow - 1));
      double currentSampleMean = 0;
      int recoverySampleCount = 0;
      spikeOccured = false;
      
      for (int j = samplesPerWindow; j < Math.max(leftEarLength, rightEarLength) - samplesPerWindow; j += samplesPerWindow) {
        if (j > input[i].length) {
          break;
        }
        
        if (spikeOccured) {
          if (currentSampleMean - lastSampleMean > recoverySampleThreshold) {
            recoverySampleCount++;
          }
          
          if (recoverySampleCount == recoverySampleNumber) {
            spikeOccured = false;
          }
        }
        
        currentSampleMean = ModelUtilities.mean(ModelUtilities.getArrayPart(input[i], j, j + samplesPerWindow));
        
        if (currentSampleMean - lastSampleMean > threshold && currentSampleMean > lastSampleMean && !spikeOccured) {
          spikeOccured = true; 
          recoverySampleThreshold = (currentSampleMean - lastSampleMean) / 10;
          log.add("Fired for array " + i + " at index " + j + ". ");
          log.addn("  Mean sample difference is " + (currentSampleMean - lastSampleMean));
          int maxIdx = ModelUtilities.maxIdx(ModelUtilities.getArrayPart(input[i], j, j + samplesPerWindow));
          if ((j+maxIdx) < input[i].length) {
            spikeTrains[i][j + maxIdx] = 1; // Only 1 spike per equidistance
          }
        }
      }
    }

    int leftIdx = ModelUtilities.firstNonZero(spikeTrains[1]);
    int rightIdx = ModelUtilities.firstNonZero(spikeTrains[0]);
    log.addn("Left Index: " + leftIdx + "\t Right Index" + rightIdx + "\t Difference(R-L): " + (rightIdx - leftIdx));

    return spikeTrains;
  }
  
  public int getInputAudioLength() {
    return inputAudioLength;
  }

  public void setInputAudioLength(int inputAudioLength) {
    this.inputAudioLength = inputAudioLength;
  }

  public int getWindowEquidistance() {
    return windowEquidistance;
  }

  public void setWindowEquidistance(int windowEquidistance) {
    this.windowEquidistance = windowEquidistance;
  }

  public int getRecoverySampleNumber() {
    return recoverySampleNumber;
  }

  public void setRecoverySampleNumber(int recoverySampleNumber) {
    this.recoverySampleNumber = recoverySampleNumber;
  }

  @Override
  public void setup(HashMap<String, String> mp) {
    this.mp = mp;

    ModelUtilities.checkParamExists(mp, "inputAudioLength");
    ModelUtilities.checkParamExists(mp, "windowEquidistance");
    ModelUtilities.checkParamExists(mp, "recoverySampleNumber");
    ModelUtilities.checkParamExists(mp, "threshold");
    
    inputAudioLength = Integer.parseInt(mp.get("inputAudioLength"));
    windowEquidistance = Integer.parseInt(mp.get("windowEquidistance"));
    recoverySampleNumber = Integer.parseInt(mp.get("recoverySampleNumber"));
    
    samplesPerWindow = (int) Math.ceil(inputAudioLength / windowEquidistance);
    threshold = Double.parseDouble(mp.get("threshold"));
  }
}