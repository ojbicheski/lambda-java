package ca.bicheski.aws.lambda.loadfile;

import com.amazonaws.services.lambda.runtime.events.S3Event;
import com.amazonaws.services.lambda.runtime.tests.EventLoader;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class S3FileHandlerTests {

	private TestContext context;

	@Mock
	private AmazonS3 client;

	@Mock
	S3ObjectInputStream s3ObjectInputStream;

	@Mock
	private S3Object s3Object;

	private S3FileHandler handler;

	private InputStream stream;

	@BeforeEach
	void setup() throws UnsupportedEncodingException {
		handler = new S3FileHandler(client);
		context = new TestContext();

		StringBuilder builder = new StringBuilder();
		builder.append("FirstName1,LastName,Content to be loaded").append("\n");
		builder.append("FirstName2,LastName,Content to be loaded");

		stream = IOUtils.toInputStream(builder.toString());
	}

	@Test
	void testLoadEventBridgeEvent() throws IOException {
		// GIven
		s3ObjectInputStream = new S3ObjectInputStream(stream,null);
		// When
		when(s3Object.getObjectContent()).thenReturn(s3ObjectInputStream);
		when(client.getObject("bucket-name", "s3/file.csv")).thenReturn(s3Object);

		// Then
		S3Event event = EventLoader.loadS3Event("s3/s3Event.json");
		assertEquals(
						"Files loaded: 1\n  File [bucket-name/s3/file.csv] Records: 2",
						handler.handleRequest(event, context)
		);
	}

}
