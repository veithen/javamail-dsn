package net.sf.javamaildsn;

public class SingleLineStatusMessage implements StatusMessage {
	private final String message;

	public SingleLineStatusMessage(String message) {
		this.message = message;
	}

	public String[] getLines() {
		return new String[] { message };
	}

	public String getUnfolded() {
		return message;
	}

	@Override
	public String toString() {
		return message;
	}
}
