package hello.data;

public class OutputMessage {

	public String name;
	public String message;

	public OutputMessage(final String name, final String message) {
		this.name = name;
		this.message = message;
	}

	public String getName() {
		return name;
	}

	public String getMessage() {
		return message;
	}
}
