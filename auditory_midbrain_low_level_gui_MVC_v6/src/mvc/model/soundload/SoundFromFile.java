package mvc.model.soundload;

import java.io.IOException;
import java.util.HashMap;

import mvc.model.Log;
import mvc.model.abstractlayer.InputLayer;
import utilities.ModelUtilities;

/**
 * Input layer which loads the sound from a previously recorded file of samples
 * per ear on two lines, with each sample delimited by a space.
 * 
 * @author Anthony Timotheou
 */
public class SoundFromFile extends InputLayer {
  public SoundFromFile(Log log, int degrees) {
    super(log, degrees);
    name = "Sound From File";
  }

  /**
 * Holds the left and right ear sounds once loaded
 */
  double[][] earSounds;
  /**
 */
HashMap<String, double[]> csvSoundsLeft;
  /**
 */
HashMap<String, double[]> csvSoundsRight;

  /**
   * Getter of ear sounds
   * 
   * @return left and right ear sound samples
   */
  public double[][] getSound() {
    return earSounds;
  }

  @Override
  public double[][] execute() {
    return this.getSound();
  }

  @Override
  public void setup(HashMap<String, String> mp) {
    ModelUtilities.checkParamExists(mp, "SoundFilename");
    ModelUtilities.checkParamExists(mp, "inputAudioLength");
    
    earSounds = new double[2][Integer.parseInt(mp.get("inputAudioLength"))]; 
    
    try {
      csvSoundsLeft = ModelUtilities.loadSoundCSV("left_" + mp.get("SoundFilename"));
      csvSoundsRight = ModelUtilities.loadSoundCSV("right_" + mp.get("SoundFilename"));
      log.addn("DEGREES::  "+degrees);
      earSounds[0] = csvSoundsLeft.get(""+degrees);
      earSounds[1] = csvSoundsRight.get(""+degrees);
    } catch (IOException e) {
      e.printStackTrace();
    }
    //Angle to Array Conversion 90 19 60 13 30 7 0 0 -30 67 -60 61 -90 55
  }
 
}
