package ca.bicheski.aws.lambda.loadfile;

import ca.bicheski.aws.lambda.loadfile.message.File;
import ca.bicheski.aws.lambda.loadfile.s3.S3;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.S3Event;
import com.amazonaws.services.lambda.runtime.events.models.s3.S3EventNotification;
import com.amazonaws.services.s3.AmazonS3;

import java.util.List;
import java.util.Objects;

public class S3FileHandler implements RequestHandler<S3Event, String> {

	private AmazonS3 amazonS3;

	public S3FileHandler() { }

	public S3FileHandler(AmazonS3 amazonS3) {
		this.amazonS3 = amazonS3;
	}

	@Override
	public String handleRequest(S3Event input, Context context) {
		LambdaLogger logger = context.getLogger();
		S3 s3 = s3(logger);

		List<File> files = input.getRecords().stream()
						.map(S3EventNotification.S3EventNotificationRecord::getS3)
						.map(s3::loadFile)
						.filter(File::notEmpty)
						.toList();

		return toReturn(files);
	}

	private S3 s3(LambdaLogger logger) {
		if (Objects.isNull(amazonS3)) {
			return new S3(logger);
		}
		return new S3(logger, amazonS3);
	}

	private String toReturn(List<File> files) {
		StringBuilder builder = new StringBuilder();

		builder.append("Files loaded: ").append(files.size());
		files.stream()
						.map(File::getHeader)
						.forEach(header -> builder
										.append("\n  File [")
										.append(header.getFileName())
										.append("] Records: ")
										.append(header.getLines())
						);
		return builder.toString();
	}
}
