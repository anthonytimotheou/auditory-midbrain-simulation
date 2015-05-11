package automatedexecution;

import mvc.ModelObserver;
import mvc.SoundModelInterface;

/**
 * GUI class to print output to console. 
 * 
 * @author anthony timotheou
 *
 */
public class DummyGui implements ModelObserver {
  
  /**
   * Constructor which registers the gui to the model. 
   * 
   * @param model
   */
  public DummyGui (SoundModelInterface model) {
    model.registerObserver(this);
  }
  
  
  /**
   * Print out the results when a update is called from the model. 
   * 
   */
  @Override
  public void update(double[][] process, int result, int currentLevel, String levelName) {
    if (process != null) {
        System.out.println("Process Completed of: " + currentLevel);
    }

    if (result != -1) {
      System.out.println("Result of model is " + result);
    }

  }

}
