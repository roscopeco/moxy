import com.roscopeco.moxy.Moxy;
import org.junit.jupiter.api.Test;

public class TestRegressionMoxy3 {
  @Test
  void testMockInDefaultPackage() {
    Moxy.mock(DefaultPackageClass.class);
  }
}
