package mvc;


import java.io.File;

import mvc.model.ModelInputTypes;

/**
 * Interface representing an observer to configuration changes.
 * 
 * @author anthony timotheou
 *
 */
public interface ConfigObserver {
  /**
   * Method called when changes to either variable occur.  
   * 
   * @param m model input type
   * @param f file representing the chosen configuration file
   */
  public void update(ModelInputTypes m, File f);
}
