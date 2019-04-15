package hello;

import hello.data.User;

class OutputMessage extends Message{
	public String time;


	public OutputMessage(final String name, final String message) {
		super(name, message);
	}
}
