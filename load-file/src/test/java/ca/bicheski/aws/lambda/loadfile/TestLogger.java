package ca.bicheski.aws.lambda.loadfile;

import com.amazonaws.services.lambda.runtime.LambdaLogger;
import org.junit.platform.commons.logging.Logger;
import org.junit.platform.commons.logging.LoggerFactory;

import java.util.function.Supplier;

public class TestLogger implements LambdaLogger {
  private static final Logger logger = LoggerFactory.getLogger(TestLogger.class);
  public TestLogger() {}

  public void log(String message) {
    logger.info(new Supplier<String>() {
      @Override
      public String get() {
        return message;
      }
    });
  }

  public void log(byte[] message) {
    logger.info(new Supplier<String>() {
      @Override
      public String get() {
        return new String(message);
      }
    });
  }
}
