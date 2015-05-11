package automatedexecution;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import mvc.model.Log;
import mvc.model.ModelInputTypes;

/**
 * Class which represents an evaluation framework to automate execution of the model with differing
 * parameters. Has been multi-threaded to increase speed of execution. 
 * 
 * @author anthonytimotheou
 *
 */
public class EvaluationFamework {
  /**
 * Distinct number of sound source locations.
 */
int numberOfSoundSourceLocationsHRIR = 7;
  /**
 * Length of audio input. 
 */
int inputAudioLengthHRIR = 200;
  /**
 * Initial value for threshold. 
 */
double thresholdInitialHRIR = 0;
  /**
 * Maximum value for the threshold. 
 */
double thresholdMaxHRIR = 0.2358;
/**
 * Amount to increase the threshold by each iteration. 
 */
double thresholdIncrementHRIR = 0.001;
  /**
 * Models required used for time estimation.
 */
int numberOfModelsRequiredHRIR = -1;
  /**
 * Used for logging. Normal file writer is not thread safe. 
 */
ThreadSafeFileWriter tsfw = ThreadSafeFileWriter.getLogger();

  /**
 * Distinct number of sound source locations. 
 */
int numberOfSoundSourceLocationsSIN = 7;
  /**
 * length of audio data input.
 */
int inputAudioLengthSIN = 88200;
  /**
 * initial threshold value.
 */
double thresholdInitialSIN = 0.0001;
  /**
 * Maximum value of the threshold will reach.
 */
double thresholdMaxSIN = 1;
  /**
 * The value to increase the threshold by for each iteration. 
 */
double thresholdIncrementSIN = 0.005;
  /**
 * number of Models required. 
 */
int numberOfModelsRequiredSIN = -1;
  
  /**
   * Converts milliseconds to days, hours, minutes and seconds.
   * 
   * @param milliseconds 
   *          number of seconds from the cpu clock.
   * @return
   *          formatted string of days, hours, minutes and seconds.
   */
  private static String millisecondsToDHMS(double milliseconds) {
    double x = -1;
    x = milliseconds / 1000;
    double seconds = x % 60;
    x /= 60;
    double minutes = x % 60;
    x /= 60;
    double hours = x % 24;
    x /= 24;
    double days = x;
    return (int) days + " d " + (int) hours + " h " + (int) minutes + " m " + (int) seconds + " s";
  }
  
  /**
   * Estimate the time required for the HRIR models to run, by timing the execution of one model and multiplying
   * that by the number of models needed to be executed. Not accurate because run times vary according to cache
   * changes, paging changes and other behind the scenes differences. 
   * 
   */
  public void prerecordedHRIRAnalysis() {
    Log analysis = new Log();
    long start = System.currentTimeMillis();
    ExecutableMVC test = new ExecutableMVC(analysis);
    analysis.addn("SD\tWE\t TH\t L\t rightIdx\t (rightIdx-leftIdx)\t");
    analysis.add(0 + "\t" + 500 + "\t" + 0.001 + "\t");
    test.executeModel(getParams(50, 0.001, 100, 32, 28, 16, 0, 30, 200), 0, ModelInputTypes.PRECORDEDHRIR);
    long end = System.currentTimeMillis();
    long singleModelExecutionTime = end - start;
    analysis.addn("Execution time for one model was: " + singleModelExecutionTime + " ms");
    numberOfModelsRequiredHRIR = (int) (numberOfSoundSourceLocationsHRIR * inputAudioLengthHRIR * ((thresholdMaxHRIR - thresholdInitialHRIR) / thresholdIncrementHRIR));
    analysis.addn("Number of Models Required: " + numberOfModelsRequiredHRIR);
    System.out.println("Time required to execute: " + millisecondsToDHMS((numberOfSoundSourceLocationsHRIR * inputAudioLengthHRIR * ((thresholdMaxHRIR - thresholdInitialHRIR) / thresholdIncrementHRIR)) * singleModelExecutionTime));
    System.out.println(analysis.getLog().toString());
  }

  /**
   * Estimate the time required for the SIN models to run, by timing the execution of one model and multiplying
   * that by the number of models needed to be executed. Not accurate because run times vary according to cache
   * changes, paging changes and other behind the scenes differences. 
   * 
   */
  public void prerecordedSINAnalysis() {
    Log analysis = new Log();
    long start = System.currentTimeMillis();
    ExecutableMVC test = new ExecutableMVC(analysis);
    analysis.addn("SD\tWE\t TH\t L\t rightIdx\t (rightIdx-leftIdx)\t");
    analysis.add(0 + "\t" + 900 + "\t" + 0.02 + "\t");
    test.executeModel(getParams(900, 0.02, 100, 196, 99, 6, 0, 20, 88200), 0, ModelInputTypes.GENERATEDSIN);
    long end = System.currentTimeMillis();
    long singleModelExecutionTime = end - start;
    analysis.addn("Execution time for one model was: " + singleModelExecutionTime + " ms");
    numberOfModelsRequiredSIN = (int) (numberOfSoundSourceLocationsSIN * inputAudioLengthSIN * ((thresholdMaxSIN - thresholdInitialSIN) / thresholdIncrementSIN));
    analysis.addn("Number of Models Required: " + numberOfModelsRequiredSIN);
    System.out.println("Time required to execute: " + millisecondsToDHMS((numberOfSoundSourceLocationsSIN * inputAudioLengthSIN * ((thresholdMaxSIN - thresholdInitialSIN) / thresholdIncrementSIN)) * singleModelExecutionTime));
    System.out.println(analysis.getLog().toString());
  }
  
  /**
   * Automate the evaluation by creating and executing all models for the configuration parameters defined above.
   * Be careful, too many threads will result in memory overload or a high contention while each thread waits for
   * resources which lowers the speed of execution. The number of threads must be tuned for the machine of execution.   
   */
  public void evaluateHRIR() {
    // init variables
    List<WorkerThread> workers = new ArrayList<WorkerThread>();
    String printMessage = "";
    int workerCount = 0;
    
    // log to writer the column names
    tsfw.addString("SD\tWE\t TH\t L\t rightIdx\t (rightIdx-leftIdx)\t");
    // for each window distance and threshold and sound degrees create a thread for execution
    for (int windowEquidistance = 1; windowEquidistance < inputAudioLengthHRIR + 1; windowEquidistance++) {
      for (double threshold = thresholdInitialHRIR; threshold < thresholdMaxHRIR; threshold += thresholdIncrementHRIR) {
        for (int soundDegrees = -90; soundDegrees < 91; soundDegrees += 30) {
          printMessage = soundDegrees + "\t" + windowEquidistance + "\t" + threshold + "\t";
          // if maximum number of threads reached then remove threads not working and add a new one
          // Otherwise, just add a new thread
          if (workers.size() < 17) { // number of simultaneous threads allowed is 17
            workers.add(new WorkerThread(tsfw, printMessage, workerCount++, soundDegrees, windowEquidistance, threshold, 100, 32, 28, 16, 0, 30, 200, ModelInputTypes.PRECORDEDHRIR));
          } else {
            try {
              for (int i = 0; i < workers.size(); i++) {
                if (workers.get(i).FinishedProcessing()) {
                  workers.remove(i);
                }
              }
              Thread.sleep(2); // sleep for 2 milliseconds to give more time to worker threads if the queue is full
              workers.add(new WorkerThread(tsfw, printMessage, workerCount++, soundDegrees, windowEquidistance, threshold, 100, 32, 28, 16, 0, 30, 200, ModelInputTypes.PRECORDEDHRIR));
            } catch (InterruptedException e) {
              e.printStackTrace();
            }
          }
        }
      }
    }
    
    // once processing is finished, shut down the thread safe writer. 
    try {
      tsfw.shutDown();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
  
  /**
   * Automate the evaluation by creating and executing all models for the configuration parameters defined above.
   * Be careful, too many threads will result in memory overload or a high contention while each thread waits for
   * resources which lowers the speed of execution. The number of threads must be tuned for the machine of execution.   
   */
  public void evaluateSIN() {
    List<WorkerThread> workers = new ArrayList<WorkerThread>();
    int workerCount = 0;
    String printMessage = "";

    tsfw.addString("SD\tWE\t TH\t L\t rightIdx\t (rightIdx-leftIdx)\t");
    for (int windowEquidistance = 1; windowEquidistance < inputAudioLengthSIN + 1; windowEquidistance += 500) {
      for (double threshold = thresholdInitialSIN; threshold < thresholdMaxSIN; threshold += thresholdIncrementSIN) {
        for (int soundDegrees = -90; soundDegrees < 91; soundDegrees += 30) {
          printMessage = soundDegrees + "\t" + windowEquidistance + "\t" + threshold + "\t";
          // if maximum number of threads reached then remove threads not working and add a new one
          // Otherwise, just add a new thread
          if (workers.size() < 2) { // maximum worker threads is two
            workers.add(new WorkerThread(tsfw, printMessage, workerCount++, soundDegrees, windowEquidistance, threshold, 4441, 196, 99, 6, 0, 20, 88200, ModelInputTypes.GENERATEDSIN));
          } else {
            try {
              for (int i = 0; i < workers.size(); i++) {
                if (workers.get(i).FinishedProcessing()) {
                  workers.remove(i);
                }
              }
              Thread.sleep(2); // sleep for 2 milliseconds to give more time to worker threads if the queue is full
              workers.add(new WorkerThread(tsfw, printMessage, workerCount++, soundDegrees, windowEquidistance, threshold, 4441, 196, 99, 6, 0, 20, 88200, ModelInputTypes.GENERATEDSIN));
            } catch (InterruptedException e) {
              e.printStackTrace();
            }
          }
        }
      }
    }
    // once processing is finished shut down the thread safe writer. 
    try {
      tsfw.shutDown();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
  
  /**
   * Main function which executes all simulates and analysis methods. 
   */
  public void evaluate() {
    prerecordedHRIRAnalysis();
    evaluateHRIR();
    prerecordedSINAnalysis();
    evaluateSIN();
  }
  
  /**
   * Main method which creates and executes an evaluation framework.  
   * 
   * @param args command line arguments
   */
  public static void main(String[] args) {
    EvaluationFamework ef = new EvaluationFamework();
    ef.evaluate();
  }
  
  /**
   * Convenience method to create a parameter set to pass to the model.  All the parameters are those of the model. 
   * 
   * @param windowEquidistance spike layer parameter
   * @param threshold spike layer parameter
   * @param recoverySampleNumber spike layer parameter
   * @param delay90 neural layer parameter
   * @param delay60 neural layer parameter
   * @param delay30 neural layer parameter
   * @param delay0 neural layer parameter
   * @param fireThreshold neural layer parameter
   * @param inputAudioLength model parameter
   * 
   * @return hashmap of parameters used by the model
   */
  public static HashMap<String, String> getParams(int windowEquidistance, double threshold, int recoverySampleNumber, int delay90, int delay60, int delay30, int delay0, int fireThreshold, int inputAudioLength) {
    HashMap<String, String> mp = new HashMap<String, String>();
    // GENERATEDSIN SOUND INPUT LAYER PARAMETERS
    mp.put("inputAudioLength", "" + inputAudioLength);
    mp.put("samplingFrequency", "44100");
    mp.put("signalFrequency", "1000");
    mp.put("radianShift90", "3.76");
    mp.put("radianShift60", "2.52");
    mp.put("radianShift30", "1.25");
    mp.put("radianShift0", "0");
    mp.put("soundLength", "2");
    mp.put("startSecond", "0.5");
    mp.put("toneDuration", "1");
    // PRERECORDED HRIR INPUT LAYER PARAMETERS
    mp.put("SoundFilename", "ear_small_pinna.csv");
    mp.put("logSound", "true");
    // Spike Layer
    mp.put("windowEquidistance", "" + windowEquidistance);
    mp.put("threshold", "" + threshold);
    mp.put("recoverySampleNumber", "" + recoverySampleNumber);
    // Neural Network Layer
    mp.put("neuronFireThreshold", "" + fireThreshold);
    mp.put("neuronNumber", "7");
    mp.put("neuronTypeChosen", "0");
    mp.put("maxDelay", "" + Math.max(Math.max(Math.max(delay0, delay30), delay60), delay90));
    mp.put("delay90", "" + delay90);
    mp.put("delay60", "" + delay60);
    mp.put("delay30", "" + delay30);
    mp.put("delay0", "" + delay0);
    return mp;
  }
}

class WorkerThread implements Runnable {
/**
 * All parameters for the model to execute in this thread. Plus the thread itself. 
 */
Thread worker;
private int windowEquidistance;
private double threshold;
private int recoverySampleNumber;
private int delay90;
private int delay60;
private int delay30;
private int delay0;
private int workerNo;
private int fireThreshold;
private int inputAudioLength;
private ThreadSafeFileWriter tsfw;
private int soundDegrees;
private String printMessage;
private ModelInputTypes mit;
private boolean finishedProcessing;

  @SuppressWarnings("unused")
  private WorkerThread() {
  }
  
  /**
   * Instantiate the thread with all the model parameters so the thread can create a model and execute it. It creates
   * one model and executes it. 
   * 
   * @param tsfw
   * @param printMessage
   * @param workerNo
   * @param soundDegrees
   * @param windowEquidistance
   * @param threshold
   * @param recoverySampleNumber
   * @param delay90
   * @param delay60
   * @param delay30
   * @param delay0
   * @param fireThreshold
   * @param inputAudioLength
   * @param mit
   */
  public WorkerThread(ThreadSafeFileWriter tsfw, String printMessage, int workerNo, int soundDegrees, int windowEquidistance, double threshold, int recoverySampleNumber, int delay90, int delay60, int delay30, int delay0, int fireThreshold,
      int inputAudioLength, ModelInputTypes mit) {
    this.printMessage = printMessage;
    this.tsfw = tsfw;
    this.workerNo = workerNo;
    this.soundDegrees = soundDegrees;
    this.windowEquidistance = windowEquidistance;
    this.threshold = threshold;
    this.recoverySampleNumber = recoverySampleNumber;
    this.delay90 = delay90;
    this.delay60 = delay60;
    this.delay30 = delay30;
    this.delay0 = delay0;
    this.fireThreshold = fireThreshold;
    this.inputAudioLength = inputAudioLength;
    this.mit = mit;

    worker = new Thread(this);
    worker.start();
  }
  
  /**
   * Initiate the thread to start working and execute a model. 
   */
  public void run() {
    try {
      ExecutableMVC test = new ExecutableMVC(printMessage, tsfw);
      test.executeModel(EvaluationFamework.getParams(windowEquidistance, threshold, recoverySampleNumber, delay90, delay60, delay30, delay0, fireThreshold, inputAudioLength), soundDegrees, mit);
      this.finishedProcessing = true;
      System.gc();
    } catch (Exception e) {
      System.out.println("Exception in : " + workerNo);
    }
  }
  
  /**
   * Return parameter to check if the thread has stopped processing. 
   * 
   * @return
   */
  public boolean FinishedProcessing() {
    return this.finishedProcessing;
  }
}