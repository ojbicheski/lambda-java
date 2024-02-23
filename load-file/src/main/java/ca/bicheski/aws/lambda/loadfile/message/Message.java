/**
 * 
 */
package ca.bicheski.aws.lambda.loadfile.message;

import com.fasterxml.jackson.databind.JsonNode;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * 
 */
@Builder
@Getter
@Setter
public class Message {

	private int line;
	private Header header;
	private JsonNode body;
}
