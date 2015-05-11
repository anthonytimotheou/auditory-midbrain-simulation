package junit.unit;

import static org.junit.Assert.*;

import java.util.HashMap;

import mvc.model.Log;
import mvc.model.soundgeneration.SoundGeneration;

import org.junit.Test;

public class SoundGenerationTest {

  @Test
  public void testSetup() {
    HashMap<String, String> params = new HashMap<String, String>();
    params.put("inputAudioLength", "88200");
    params.put("samplingFrequency", "44100");
    params.put("signalFrequency", "1000");
    params.put("radianShift90", "3.76");
    params.put("radianShift60", "2.52");
    params.put("radianShift30", "1.25");
    params.put("radianShift0", "0");
    params.put("soundLength", "2");
    params.put("startSecond", "0.5");
    params.put("toneDuration", "1");
    
    SoundGeneration s =  new SoundGeneration(new Log(), 0);
    s.setup(params);
    double[][] sound = s.getGeneratedSound();
    assertEquals(sound.length, 2);
    assertEquals(sound[0].length, 88200);
    assertEquals(sound[1].length, 88200);
  }

  @Test
  public void testExecute() {
    HashMap<String, String> params = new HashMap<String, String>();
    params.put("inputAudioLength", "88200");
    params.put("samplingFrequency", "44100");
    params.put("signalFrequency", "1000");
    params.put("radianShift90", "3.76");
    params.put("radianShift60", "2.52");
    params.put("radianShift30", "1.25");
    params.put("radianShift0", "0");
    params.put("soundLength", "2");
    params.put("startSecond", "0.5");
    params.put("toneDuration", "1");
    
    SoundGeneration s =  new SoundGeneration(new Log(), 0);
    s.setup(params);
    double[][] sound = s.execute();
    assertEquals(sound.length, 2);
    assertEquals(sound[0].length, 88200);
    assertEquals(sound[1].length, 88200);
  }

  @Test
  public void testSoundGeneration() {
    SoundGeneration s = new SoundGeneration(new Log(), 0);
    assertNotNull(s);
  }

  @Test
  public void testGetName() {
    SoundGeneration s = new SoundGeneration(new Log(), 0);
    assertEquals(s.getName(), "Sound Generation");
  }

}
