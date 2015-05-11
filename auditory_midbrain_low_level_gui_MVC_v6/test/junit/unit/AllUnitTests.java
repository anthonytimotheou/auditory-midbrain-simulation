package junit.unit;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ 
    FirTest.class
  , lowPassFilterTest.class
  , meanDifferenceSpikerTest.class
  , SoundFromFileTest.class
  , SoundGenerationTest.class
  , SpikeCountOutputTest.class
  , spikingCoincidenceNeuralNetworkTest.class 
})
public class AllUnitTests {
}
