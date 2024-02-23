/**
 * 
 */
package ca.bicheski.aws.lambda.loadfile.message;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * 
 */
@Builder
public class File {

	@Setter
	@Getter
	private Header header;

	@Builder.Default
	private List<JsonNode> lines = new ArrayList<>();

	@Builder.Default
	private ObjectMapper om = new ObjectMapper();

	public void line(String line) {
		if(Objects.isNull(line) || line.isBlank()) {
			throw new RuntimeException("Line invalid");
		}

		String[] values = line.split(",");
		if(values.length != 3) {
			throw new RuntimeException("Line invalid > Number fields: " + values.length);
		}

		ObjectNode node = om.createObjectNode();
		node.put("firstName", values[0]);
		node.put("lastName", values[1]);
		node.put("content", values[2]);

		lines.add(node);
		Optional.ofNullable(header)
						.ifPresent(Header::increaseLine);
	}

	/**
	 * Returns all messages loaded from File.
	 *
	 * @return {@link List<Message>}
	 */
	public List<Message> messages() {
		int count = 1;
		return lines.stream()
						.map(body -> Message.builder()
										.line(line(count))
										.header(header)
										.body(body)
										.build()
						)
						.toList();
	}

	public boolean notEmpty() {
		return !lines.isEmpty();
	}

	private int line(int count) {
		return count++;
	}

}
