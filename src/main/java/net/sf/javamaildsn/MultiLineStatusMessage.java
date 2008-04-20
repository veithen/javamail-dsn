package net.sf.javamaildsn;

import org.apache.commons.lang.StringUtils;

/**
 * {@link StatusMessage} implementation for multi-line messages.
 * 
 * @author Andreas Veithen
 */
public class MultiLineStatusMessage implements StatusMessage {
	private final String[] lines;

	/**
	 * Constructor.
	 * 
	 * @param lines An array with the individual lines of the status message.
	 *              In order to respect the {@link StatusMessage} contract, the
	 *              elements must not contain newlines.
	 */
	public MultiLineStatusMessage(String[] lines) {
		this.lines = lines;
	}

	public String[] getLines() {
		return lines.clone();
	}

	public String getUnfolded() {
		return StringUtils.join(lines, " ");
	}

	@Override
	public String toString() {
		return StringUtils.join(lines, "\n");
	}
}
