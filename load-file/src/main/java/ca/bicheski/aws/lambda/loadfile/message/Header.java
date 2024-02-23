/**
 * 
 */
package ca.bicheski.aws.lambda.loadfile.message;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;

/**
 * 
 */
@Builder
public class Header {

	@Getter
	@Setter
	private String fileName;
	@Getter
	@Builder.Default
	private int lines = 0;
	@Getter
	@Builder.Default
	private ZonedDateTime timestamp = ZonedDateTime.now();

	public void increaseLine() {
		lines++;
	}
}
