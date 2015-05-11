package junit.unit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.HashMap;

import junit.framework.Assert;

import mvc.model.Log;
import mvc.model.soundload.SoundFromFile;

import org.junit.Test;

import utilities.ModelUtilities;

/**
 * @author anthony timotheou
 *
 */
public class SoundFromFileTest {

  /**
   * Test method for {@link mvc.model.soundload.SoundFromFile#setup(java.util.HashMap)}.
   */
  @Test
  public void testSetup() {
    HashMap<String, double[]> dummyData;
    try {
      dummyData = ModelUtilities.loadSoundCSV("left_ear_small_pinna.csv");
    Log l = new Log();
    
    HashMap<String, String> params = new HashMap<String, String>();
    params.put("inputAudioLength", "200");
    params.put("SoundFilename", "ear_small_pinna.csv");
    
    System.out.println(dummyData.get("0")[50]);
    SoundFromFile s = new SoundFromFile(l, 0);
    s.setup(params);
    
    double[][] retrievedSound = s.getSound();
    assertEquals(retrievedSound[0].length, 200);
    assertEquals(retrievedSound[1].length, 200);
    assertEquals(retrievedSound.length, 2);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Test method for {@link mvc.model.soundload.SoundFromFile#execute()}.
   */
  @Test
  public void testExecute() {
    HashMap<String, double[]> dummyData;
    try {
      dummyData = ModelUtilities.loadSoundCSV("left_ear_small_pinna.csv");
    Log l = new Log();
    
    HashMap<String, String> params = new HashMap<String, String>();
    params.put("inputAudioLength", "200");
    params.put("SoundFilename", "ear_small_pinna.csv");
    
    System.out.println(dummyData.get("0")[50]);
    SoundFromFile s = new SoundFromFile(l, 0);
    s.setup(params);

    double[][] retrievedSound = s.execute();
    assertEquals(retrievedSound[0].length, 200);
    assertEquals(retrievedSound[1].length, 200);
    assertEquals(retrievedSound.length, 2);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Test method for {@link mvc.model.soundload.SoundFromFile#SoundFromFile(mvc.model.Log, int)}.
   */
  @Test
  public void testSoundFromFile() {
    SoundFromFile s = new SoundFromFile(new Log(), 0);
    assertNotNull(s);    
  }

  /**
   * Test method for {@link mvc.model.soundload.SoundFromFile#getSound()}.
   */
  @Test
  public void testGetSound() {
    HashMap<String, String> params = new HashMap<String, String>();
    params.put("inputAudioLength", "200");
    params.put("SoundFilename", "ear_small_pinna.csv");

    SoundFromFile s = new SoundFromFile(new Log(), 0);
    double[][] sound = s.getSound();
    Assert.assertNull(sound);
    s.setup(params);
  }

  /**
   * Test method for {@link mvc.model.abstractlayer.Layer#getName()}.
   */
  @Test
  public void testGetName() {
    SoundFromFile s = new SoundFromFile(new Log(), 0);
    assertEquals(s.getName(), "Sound From File");
  }

}
