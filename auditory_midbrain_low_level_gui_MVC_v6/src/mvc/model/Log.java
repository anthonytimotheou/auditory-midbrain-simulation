package mvc.model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Class representing the log of a anything. Used here for the log of the model. 
 * Provides buffering of the string and helper methods for section and timing information 
 * to a string. 
 * 
 * @author anthony timotheou
 *
 */
public class Log {
private StringBuilder log = new StringBuilder();
  
  /**
   * add a string
   * @param s
   */
  public void add(String s) {
    log.append(s);
  }

  /**
   * Add string and a new line
   * @param s
   */
  public void addn(String s) {
    log.append(s);
    this.addNewLine();
  }
  
  /**
   * add a new line
   */
  public void addNewLine() {
    log.append("\n");
  }
  
  /**
   * add a section with a time stamp
   * @param title
   */
  public void addSection(String title) {
    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    Date date = new Date();
    this.addNewLine();
    log.append("----:: " + title + "  (" + dateFormat.format(date) + ") ::----");
    this.addNewLine();
  }
  
  /**
   * Return the string as a builder object which is buffered
   * @return
   */
  public StringBuilder getLog() {
    return log;
  }
  
  /**
   * Clear the log
   */
  public void clearLog () {
    log = new StringBuilder();
  }
}
