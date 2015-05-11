package mvc.model;

/**
 * Represents the input types for the model. 
 * 
 * @author  anthony timotheou
 */
public enum ModelInputTypes {

  /**
   * Generated sin sound data.
   */
GENERATEDSIN {
    public String toString() {
      return "Generated Sin Sounds";
    }
  },

/**
 * Pre-recorded impulse response from the small pinna of the ear of humans. 
 */
PRECORDEDHRIR {
    public String toString() {
      return "Precorded HRIR Sounds";
    }
  }
}
