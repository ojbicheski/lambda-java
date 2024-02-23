package ca.bicheski.aws.lambda.loadfile.s3;

import ca.bicheski.aws.lambda.loadfile.message.File;
import ca.bicheski.aws.lambda.loadfile.message.Header;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.events.models.s3.S3EventNotification;
import com.amazonaws.services.lambda.runtime.logging.LogLevel;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.S3Object;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class S3 {

  private final LambdaLogger logger;
  private final AmazonS3 client;

  public S3(LambdaLogger logger) {
    this.logger = logger;
    this.client = AmazonS3ClientBuilder.defaultClient();
  }

  public S3(LambdaLogger logger, AmazonS3 client) {
    this.logger = logger;
    this.client = client;
  }

  public File loadFile(S3EventNotification.S3Entity entity) {
    String bucket = entity.getBucket().getName();
    String key = entity.getObject().getUrlDecodedKey();

    BufferedReader reader = reader(client.getObject(bucket, key));

    File file = File.builder()
            .header(Header.builder()
                    .fileName(bucket.concat("/").concat(key))
                    .build())
            .build();

    try {
      reader.lines().forEach(file::line);
      reader.close();
    } catch (IOException e) {
      logger.log(e.getMessage(), LogLevel.WARN);
    }

    return file;
  }

  private BufferedReader reader(S3Object object) {
    return new BufferedReader(new InputStreamReader(
            object.getObjectContent(),
            StandardCharsets.UTF_8
    ));
  }
}
