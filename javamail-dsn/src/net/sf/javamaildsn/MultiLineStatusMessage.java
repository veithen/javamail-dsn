package net.sf.javamaildsn;

import org.apache.commons.lang.StringUtils;

public class MultiLineStatusMessage implements StatusMessage {
	private final String[] lines;

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
