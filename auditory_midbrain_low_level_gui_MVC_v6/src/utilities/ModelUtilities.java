package utilities;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.NoSuchElementException;

public class ModelUtilities {

  public static void checkParamExists(HashMap<String, String> mp, String parameterName) {
    if ( mp.get(parameterName) != null) {
      // parameter present, do nothing
    } else {
      throw new NoSuchElementException("Parameter Not Present: " + parameterName);
    }
  }
  
  public static void writeStringArrayToFile(String fileName, ArrayList<String> info) throws IOException {
    FileWriter fstream = new FileWriter(fileName);
    BufferedWriter out = new BufferedWriter(fstream);
    for (String s : info) {
      out.write(s + "\n");
    }
    out.close();
  }

  public static double[] convert(byte[] b) {
    double[] d = new double[b.length];
    for (int i = 0; i < b.length; i++)
      d[i] = (double) b[i];
    return d;
  }

  public static void printArray(double[][] array) {
    for (double[] dd : array) {
      for (double d : dd) {
        System.out.print(d);
      }
      System.out.print("\n");
    }
  }

  public static int arrayIndexToSoundSource(int index) {
    int soundSourceDegrees = -181;
    switch (index) {
      case 0:
        soundSourceDegrees = -90;
        break;
      case 1:
        soundSourceDegrees = -60;
        break;
      case 2:
        soundSourceDegrees = -30;
        break;
      case 3:
        soundSourceDegrees = 0;
        break;
      case 4:
        soundSourceDegrees = 30;
        break;
      case 5:
        soundSourceDegrees = 60;
        break;
      case 6:
        soundSourceDegrees = 90;
        break;
    }
    return soundSourceDegrees;
  }

  public static int soundSourceToArrayIndex(int degrees) {
    int soundSourceDegrees = -181;

    switch (degrees) {
      case -90:
        soundSourceDegrees = 6;
        break;
      case -60:
        soundSourceDegrees = 5;
        break;
      case -30:
        soundSourceDegrees = 4;
        break;
      case 0:
        soundSourceDegrees = 3;
        break;
      case 30:
        soundSourceDegrees = 2;
        break;
      case 60:
        soundSourceDegrees = 1;
        break;
      case 90:
        soundSourceDegrees = 0;
        break;
    }

    return soundSourceDegrees;
  }

  public static double[][] loadSound(String filename) throws IOException {
    double[][] earSounds = new double[2][44100]; // TODO change to dynamic
                                                 // length, arraylist and assign
                                                 // length on sound from file?
    FileInputStream fstream = new FileInputStream(filename);
    DataInputStream in = new DataInputStream(fstream);
    BufferedReader br = new BufferedReader(new InputStreamReader(in));
    String data;
    data = br.readLine();
    earSounds[0] = ArrayToDouble(data.split(" "));
    data = br.readLine();
    earSounds[1] = ArrayToDouble(data.split(" "));
    br.close();
    return earSounds;
  }

  public static double[][] loadSound(String filenameLeft, String filenameRight) throws IOException {
    double[][] earSounds = new double[2][44100]; // TODO change to dynamic
                                                 // length
    FileInputStream fstreamLeft = new FileInputStream(filenameLeft);
    DataInputStream inLeft = new DataInputStream(fstreamLeft);
    BufferedReader brLeft = new BufferedReader(new InputStreamReader(inLeft));
    FileInputStream fstreamRight = new FileInputStream(filenameRight);
    DataInputStream inRight = new DataInputStream(fstreamRight);
    BufferedReader brRight = new BufferedReader(new InputStreamReader(inRight));
    String data;
    data = brLeft.readLine();
    earSounds[0] = ArrayToDouble(data.split(" "));
    data = brRight.readLine();
    earSounds[1] = ArrayToDouble(data.split(" "));
    brLeft.close();
    brRight.close();
    return earSounds;
  }

  public static HashMap<String, double[]> loadSoundCSV(String filename) throws IOException {
    HashMap<String, double[]> sounds = new HashMap<String, double[]>();
    double[][] earSounds = new double[72][200];
    FileInputStream fstream = new FileInputStream(filename);
    DataInputStream in = new DataInputStream(fstream);
    BufferedReader br = new BufferedReader(new InputStreamReader(in));
    String data;

    for (int j = 0; j < 199; j++) {
      data = br.readLine();
      if (data != null) {
        String[] splitData = data.split(" ");
        for (int i = 0; i < 71; i++) {
          earSounds[i][j] = Double.parseDouble(splitData[i]);
        }
      }
    }
    
    br.close();
    
    // TODO inline the process here to make it faster, isn't significant for now.
    int degRef = 0;
    for (int i = 0; i < 72; i++) {
      sounds.put("" + degRef, earSounds[i]);
      degRef += 5;
    }
    
    // System.out.println("Loaded Sounds Left...");
    // for (int i = 0; i < 200; i++) {
    // System.out.print(earSounds[0][i] + " ");
    // System.out.println();
    // }
    // System.out.println("Loaded Sounds Right...");
    // for (int i = 0; i < 200; i++) {
    // System.out.print(earSounds[1][i] + " ");
    // System.out.println();
    // }

    
    return sounds;
  }

  public static HashMap<String, String> loadParameters(String filename) throws IOException {
    HashMap<String, String> parameters = new HashMap<String, String>();
    FileInputStream fstream = new FileInputStream(filename);
    DataInputStream in = new DataInputStream(fstream);
    BufferedReader br = new BufferedReader(new InputStreamReader(in));
    String data;

    while ((data = br.readLine()) != null) {
      if (data.startsWith("//")) {
        continue;
      }
      String[] splitData = data.split(" ");
      if (splitData.length == 2) {
        throw new IllegalArgumentException("Incorrect Parameter given. Check spaces.");
      } // "A parameter with more than one value has been loaded. Check spaces in parameter names and values."
      parameters.put(splitData[0], splitData[1]);
    }

    return parameters;
  }

  public static HashMap<String, String> loadParameters(File file) throws IOException {
    HashMap<String, String> parameters = new HashMap<String, String>();
    FileInputStream fstream = new FileInputStream(file);
    DataInputStream in = new DataInputStream(fstream);
    BufferedReader br = new BufferedReader(new InputStreamReader(in));
    String data;

    while ((data = br.readLine()) != null) {
      if (data.startsWith("//")) {
        continue;
      }
      String[] splitData = data.split(" ");
      if (!(splitData.length == 2)) {
        throw new IllegalArgumentException("Incorrect Parameter given. Check spaces.");
      } // "A parameter with more than one value has been loaded. Check spaces in parameter names and values."
      parameters.put(splitData[0], splitData[1]);
    }

    return parameters;
  }

  public static double[] ArrayToDouble(String[] data) {
    double[] convertedData = new double[data.length];
    for (int i = 0; i < data.length; i++) {
      convertedData[i] = Double.parseDouble(data[i]);
    }
    return convertedData;
  }

  public static byte[] ArrayToByte(String[] data) {
    byte[] convertedData = new byte[data.length];
    for (int i = 0; i < data.length; i++) {
      convertedData[i] = Byte.parseByte(data[i]);
    }
    return convertedData;
  }

  public static double[] getArrayPart(double[] inputArray, int firstPoint, int secondPoint) {
    double[] arrayPart = new double[secondPoint - firstPoint];
    int arrayIdx = 0;
    for (int i = firstPoint; i < secondPoint; i++) {
      if (i < inputArray.length) {
        arrayPart[arrayIdx] = inputArray[i];
      } else {
        // do nothing
      }
      arrayIdx++;
    }
    return arrayPart;
  }

  public static int maxIdx(double[] inputArray) {
    int maxIdx = 0;
    double lastMaxValue = inputArray[0];
    for (int i = 0; i < inputArray.length; i++) {
      if (inputArray[i] > lastMaxValue) {
        maxIdx = i;
      }
      lastMaxValue = inputArray[i];
    }
    return maxIdx;
  }

  public static int firstNonZero(double[] earSounds) {
    int maxIdx = -1;

    for (int j = 0; j < earSounds.length; j++) {
      if (earSounds[j] == 1.0) {
        maxIdx = j;
        return maxIdx;
      }
    }

    return -1;
  }

  public static double differenceInFirstNonzero(double[][] earSounds) {
    double[] maxIdx = { -1, -1 };

    for (int i = 0; i < 2; i++) {
      for (int j = 0; j < earSounds[i].length; j++) {
        if (earSounds[i][j] != 0) {
          System.out.println("maxIdx " + i + "  " + j);
          maxIdx[i] = j;
          break;
        }
      }
    }

    if (maxIdx[0] != -1 && maxIdx[1] != -1) {
      return maxIdx[1] - maxIdx[0];
    }

    return -1;
  }

  public static double[] MaxVal(double[][] earSounds) {
    double[] maxIdx = { -1, -1 };

    for (int i = 0; i < 2; i++) {
      double lastMaxVal = -1;
      for (int j = 0; j < earSounds[i].length; j++) {
        if (earSounds[i][j] > lastMaxVal) {
          lastMaxVal = earSounds[i][j];
          maxIdx[i] = lastMaxVal;
        }
      }
    }

    return maxIdx;
  }

  public static double differenceInMaxVal(double[][] earSounds) {
    double[] maxIdx = { -1, -1 };

    for (int i = 0; i < 2; i++) {
      double lastMaxVal = -1;
      for (int j = 0; j < earSounds[i].length; j++) {
        if (earSounds[i][j] > lastMaxVal) {
          lastMaxVal = earSounds[i][j];
          maxIdx[i] = lastMaxVal;
        }
      }
    }

    if (maxIdx[0] != -1 && maxIdx[1] != -1) {
      return maxIdx[1] - maxIdx[0];
    }

    return -1;
  }

  public static double mean(double[] inputArray) {
    double mean = 0;
    for (double d : inputArray) {
      mean += d;
    }
    return mean / inputArray.length;
  }
}
