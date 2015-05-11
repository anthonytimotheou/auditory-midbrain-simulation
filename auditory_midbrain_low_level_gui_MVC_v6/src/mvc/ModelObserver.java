package mvc;

/**
 * Interface representing a model observer, method called when state changes in model with all data required.
 *  
 * @author anthony timotheou
 *
 */
public interface ModelObserver {
  public void update(double[][] process, int result, int currentLevel, String levelName);
}
