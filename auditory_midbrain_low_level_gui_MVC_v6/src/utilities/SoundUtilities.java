package utilities;

import java.util.HashMap;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Mixer;

public class SoundUtilities {
	public static AudioFormat getAudioFormat(float sampleRate, int sampleSizeInBits, int channels, boolean signed, boolean bigEndian) {
		return new AudioFormat(sampleRate, sampleSizeInBits, channels, signed, bigEndian);
	}
	
	public static AudioFormat getModelAudioFormat(HashMap<String, String> mp) {
		return new AudioFormat(
		      Float.parseFloat(mp.get("msampleRate"))
		    , Integer.parseInt(mp.get("sampleSizeInBits"))
		    , Integer.parseInt(mp.get("channels"))
		    , Boolean.parseBoolean(mp.get("signed"))
		    , Boolean.parseBoolean(mp.get("bigEndian")));
	}
	
	public static String getAvailableMixers() {
		String mixers = "Available Mixers on Your System:\n";
		Mixer.Info[] mixerInfo = AudioSystem.getMixerInfo();
	
		// loop through and print available mixers from mixerInfo array
		int i = 0;
		for (Mixer.Info m: mixerInfo) {
			mixers += " " + i + ": " +  m.getName() + "\n";
			i++;
		}
		
		return mixers;
	}
	
}
