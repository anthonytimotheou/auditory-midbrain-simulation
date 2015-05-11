package mvc.run;
import mvc.SoundModelInterface;
import mvc.controller.ControllerInterface;
import mvc.controller.ModelController;
import mvc.model.SpikingSoundLocalisationModelConcurrent;

/**
 * Starts the MVC program using static method provided by Java. 
 * 
 * @author Anthony
 *
 */
public class RunApplication {
  public static void main(String[] args) {
    SoundModelInterface model = new SpikingSoundLocalisationModelConcurrent();
    ControllerInterface controller = new ModelController(model); 
  }
}
