package net.sf.javamaildsn;

/**
 * {@link StatusMessage} implementation for single-line messages.
 * 
 * @author Andreas Veithen
 *
 */
public class SingleLineStatusMessage implements StatusMessage {
	private final String message;
	
	/**
	 * Constructor.
	 * 
	 * @param message The status message. In order to respect the {@link StatusMessage}
	 *                contract, the provided string must not contain any newlines.
	 */
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
