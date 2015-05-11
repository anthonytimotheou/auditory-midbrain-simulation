package automatedexecution;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Thread safe writer implementation, because the Java default writer is not thread safe. 
 * 
 * @author  atimotheou
 */

public class ThreadSafeFileWriter extends Thread {

private volatile boolean shuttingDown; // shut down flag
private volatile boolean loggerTerminated; 
private static final ThreadSafeFileWriter singletonInstance = new ThreadSafeFileWriter(); // constructor singleton instance
/**
 * Linked blocking queue used for adding messages, dynamic allocation will increase in size until memory runs out. 
 */
private static LinkedBlockingQueue<String> statsToLog = new LinkedBlockingQueue<String>();
private BufferedWriter fileWriter;
private static int processedCount;

 /**
  * Get an instantiation of the thread safe writer. 
  * 
  * @return
  *     Singleton instantation.
  */
 public static ThreadSafeFileWriter getLogger() {
  return singletonInstance;
 }

 /**
  * Private constructor. To ensure that only one instance of the writer can be instantiated.  
  */
 private ThreadSafeFileWriter() {
  start();
 }

/**
 * Starts the thread and writes all messages in order to a file, if no messages are available will wait
 * for messages to appear and write until a shutdown signal is sent. 
 */
public void run() {
   processedCount = 0;
  try {
   fileWriter = new BufferedWriter(new FileWriter("log_output_v12.txt"));
   String item;
   long timer = System.currentTimeMillis();
   while (!this.shuttingDown) {
     item = statsToLog.take();
    fileWriter.append(item);
    processedCount++;
    if(processedCount % 1000 == 0) // every thousand models output the time taken to execute and print them. 
    {
      System.out.println("Processed: " + processedCount + " - " + (System.currentTimeMillis() - timer));
      fileWriter.flush();
      timer = System.currentTimeMillis();
    }
   }
  } catch (InterruptedException iex) {
  } catch (IOException ioex) {
  ioex.printStackTrace();
  } finally {
   loggerTerminated = true;
   try {
        fileWriter.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
  }
 }
 
 /**
  * Add a string to the queue for writing. Put method blocks waiting until a space is on the queue. 
  * 
  * @param str string to add
  */
 public void addString(String str) {
  if (shuttingDown || loggerTerminated) {
   return;
  }
  try {
   statsToLog.put(str);
  } catch (InterruptedException iex) {
   Thread.currentThread().interrupt();
   throw new RuntimeException("Unexpected interruption");
  }
 }

 /**
  * shut down the thread safe file writer by setting a boolean and allowing the class itself to write the rest of its queue
  * @throws InterruptedException
  */
 public void shutDown() throws InterruptedException {
  shuttingDown = true;
  System.out.println("Finished Processing: " + processedCount);
 }
}