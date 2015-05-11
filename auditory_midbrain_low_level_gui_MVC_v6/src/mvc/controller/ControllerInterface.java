package mvc.controller;

import java.io.File;

import mvc.model.ModelInputTypes;

/**
 * Controller interface provided to enable communication between model and view. 
 * @author anthony timotheou
 *
 */
public interface ControllerInterface {
  public void runModel(); // executes the model
  public void settings(); // open the settings window
  public void logs(); // open the log window
  public void inputTypeChange(ModelInputTypes m); // change model input type
  public void setFile(File f); // set the configuration file
  public void closeWindow(); // clean up after a window close
  public void soundSourceDegreesUpdate(int selectedItem); // update sound source
  public void closeSettings(); // clean up after closing settings window
  public void terminateProgram(); // exit the program 
  public void closeLog(); // clean up after closing log window
}
