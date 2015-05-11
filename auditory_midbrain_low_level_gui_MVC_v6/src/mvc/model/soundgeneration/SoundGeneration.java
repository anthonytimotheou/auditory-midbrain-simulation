package mvc.model.soundgeneration;

import java.util.HashMap;

import utilities.ModelUtilities;

import mvc.model.Log;
import mvc.model.abstractlayer.InputLayer;

public class SoundGeneration extends InputLayer {
  /**
 * @uml.property  name="mp"
 */
HashMap<String, String> mp;
  /**
 * @uml.property  name="generatedSound" multiplicity="(0 -1)" dimension="2"
 */
double[][] generatedSound = null;

  public SoundGeneration(Log log, int degrees) {
    super(log, degrees);
    name = "Sound Generation";
  }

  private static double[][] generateEarSounds(int soundSourceDegrees, int samplingFrequency, int length, int signalFrequency, double startSecond, int toneDuration, double radianShift90, double radianShift60, double radianShift30,
    double radianShift0) {
    double[][] earSounds = new double[2][samplingFrequency];
    int totalSamples = samplingFrequency * length; // freq x seconds needed
    int startSample = (int) (startSecond * samplingFrequency);
    int endSample = startSample + (samplingFrequency * toneDuration);
    double timePerSample = (1.0 / samplingFrequency) * Math.pow(10, 6); // get
                                                                        // time
                                                                        // in
                                                                        // microseconds
    int maxDisplacement = (int) (600 / timePerSample);
    int midDisplacement = (int) (maxDisplacement * (2.0 / 3.0));
    int minDisplacement = (int) (maxDisplacement * (1.0 / 3.0));

    switch (soundSourceDegrees) {
      case -90:
        earSounds[0] = generateAndStripSinSound(samplingFrequency, totalSamples, signalFrequency, startSample, endSample, radianShift90);
        earSounds[1] = generateAndStripSinSound(samplingFrequency, totalSamples, signalFrequency, startSample + maxDisplacement, endSample + maxDisplacement, radianShift0);
        break;
      case -60:
        earSounds[0] = generateAndStripSinSound(samplingFrequency, totalSamples, signalFrequency, startSample, endSample, radianShift60);
        earSounds[1] = generateAndStripSinSound(samplingFrequency, totalSamples, signalFrequency, startSample + midDisplacement, endSample + midDisplacement, radianShift0);
        break;
      case -30:
        earSounds[0] = generateAndStripSinSound(samplingFrequency, totalSamples, signalFrequency, startSample, endSample, radianShift30);
        earSounds[1] = generateAndStripSinSound(samplingFrequency, totalSamples, signalFrequency, startSample + minDisplacement, endSample + minDisplacement, radianShift0);
        break;
      case 0:
        earSounds[0] = generateAndStripSinSound(samplingFrequency, totalSamples, signalFrequency, startSample, endSample, radianShift0);
        earSounds[1] = generateAndStripSinSound(samplingFrequency, totalSamples, signalFrequency, startSample, endSample, radianShift0);
        break;
      case 30:
        earSounds[0] = generateAndStripSinSound(samplingFrequency, totalSamples, signalFrequency, startSample + minDisplacement, endSample + minDisplacement, radianShift0);
        earSounds[1] = generateAndStripSinSound(samplingFrequency, totalSamples, signalFrequency, startSample, endSample, radianShift30);
        break;
      case 60:
        earSounds[0] = generateAndStripSinSound(samplingFrequency, totalSamples, signalFrequency, startSample + midDisplacement, endSample + midDisplacement, radianShift0);
        earSounds[1] = generateAndStripSinSound(samplingFrequency, totalSamples, signalFrequency, startSample, endSample, radianShift60);
        break;
      case 90:
        earSounds[0] = generateAndStripSinSound(samplingFrequency, totalSamples, signalFrequency, startSample + maxDisplacement, endSample + maxDisplacement, radianShift0);
        earSounds[1] = generateAndStripSinSound(samplingFrequency, totalSamples, signalFrequency, startSample, endSample, radianShift90);
        break;
      default:
        System.out.println("wrong sound source degrees selected");
    }

    return earSounds;
  }

  private static double[] generateAndStripSinSound(int samplingFrequency, int length, int signalFrequency, double firstPoint, double secondPoint, double phaseOffset) {
    if (secondPoint < firstPoint || secondPoint > length || secondPoint < 0 || firstPoint > length || firstPoint < 0) {
      System.out.println("One of you point is smaller or greater than length or your second point is before your first point");
      System.out.println("samplingFrequency" + samplingFrequency + " length " + length + " signalFrequency " + signalFrequency + " firstPoint" + firstPoint + " secondPoint " + secondPoint + "  phaseOffset " + phaseOffset);
    }
    
    double[] sound = generateSinSound(samplingFrequency, length, signalFrequency, phaseOffset);
    return stripSound(sound, firstPoint, secondPoint, samplingFrequency);
  }

  private static double[] stripSound(double[] inputSound, double firstPoint, double secondPoint, int samplingRate) {
    for (int i = 0; i < firstPoint || i > secondPoint; i++) {
      inputSound[i] = 0;
    }
    return inputSound;
  }

  private static double[] generateSinSound(int frequency, int length, int signalFrequency, double phaseOffset) {
    double segment = 1.0 / frequency;
    double[] time = new double[length];
    double ITD = phaseOffset * Math.pow(10, -4);
    double IPD = 2.0 * Math.PI * signalFrequency * ITD;
    double[] generatedSound = new double[length];

    for (int i = 0; i < time.length; i++) {
      time[i] = i * segment;
    }

    for (int i = 0; i < generatedSound.length; i++) {
      generatedSound[i] = 0.7 * Math.sin(2.0 * Math.PI * signalFrequency * time[i] + IPD);
    }

    return generatedSound;
  }

  @Override
  public double[][] execute() {
    return generatedSound;
  }

  public double[][] getGeneratedSound() {
    return generatedSound;
  }

  @Override
  public void setup(HashMap<String, String> mp) {
    this.mp = mp;
    
    ModelUtilities.checkParamExists(mp, "samplingFrequency");
    ModelUtilities.checkParamExists(mp, "soundLength");
    ModelUtilities.checkParamExists(mp, "signalFrequency");
    ModelUtilities.checkParamExists(mp, "startSecond");
    ModelUtilities.checkParamExists(mp, "toneDuration");
    ModelUtilities.checkParamExists(mp, "radianShift90");
    ModelUtilities.checkParamExists(mp, "radianShift60");
    ModelUtilities.checkParamExists(mp, "radianShift30");
    ModelUtilities.checkParamExists(mp, "radianShift0");
    
    generatedSound = generateEarSounds(
          degrees
        , Integer.parseInt(mp.get("samplingFrequency"))
        , Integer.parseInt(mp.get("soundLength"))
        , Integer.parseInt(mp.get("signalFrequency"))
        , Double.parseDouble(mp.get("startSecond"))
        , Integer.parseInt(mp.get("toneDuration"))
        , Double.parseDouble(mp.get("radianShift90"))
        , Double.parseDouble(mp.get("radianShift60"))
        , Double.parseDouble(mp.get("radianShift30"))
        , Double.parseDouble(mp.get("radianShift0"))
    );
  }
}
