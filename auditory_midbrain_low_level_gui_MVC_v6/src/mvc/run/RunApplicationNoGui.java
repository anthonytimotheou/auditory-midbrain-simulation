package mvc.run;

import automatedexecution.DummyGuiController;
import mvc.SoundModelInterface;
import mvc.controller.ControllerInterface;
import mvc.model.SpikingSoundLocalisationModelConcurrent;

/**
 * Calls the application with no GUI. Outputs to console. 
 * 
 * @author anthony timotheou
 *
 */
public class RunApplicationNoGui {
  public static void main(String[] args) {
    SoundModelInterface model = new SpikingSoundLocalisationModelConcurrent();
    ControllerInterface controller = new DummyGuiController(model);
  }
 
}
