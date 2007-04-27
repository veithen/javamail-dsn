package net.sf.javamaildsn;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 
 * 
 * According to section 6.3 of RFC 3461, the SMTP diagnostic is supplied
 * for DSNs resulting from attempts to relay a message to one or
 * more recipients via SMTP.
 * 
 * @author Andreas Veithen
 */
public class SMTPDiagnostic implements Diagnostic {
	private final static Pattern linePattern = Pattern.compile("\\s*(\\d{3})[ -](.*)");
	
	private final int code;
	private final List<String> messages = new LinkedList<String>();
	
	public SMTPDiagnostic(String value) {
		BufferedReader in = new BufferedReader(new StringReader(value));
		int code = -1;
		String line;
		StringBuilder buffer = new StringBuilder();
		do {
			try {
				line = in.readLine();
			}
			catch (IOException ex) {
				// Reading from a StringReader should never cause an IOException
				throw new Error(ex);
			}
			boolean flush; // True if the current content of the buffer should be flushed
			String chunk; // The piece of message text recovered in the current iteration
			if (line == null) {
				flush = true;
				chunk = null;
			} else {
				Matcher matcher = linePattern.matcher(line);
				if (matcher.matches()) {
					code = Integer.valueOf(matcher.group(1));
					chunk = matcher.group(2);
					flush = true;
				} else {
					chunk = line;
					flush = false;
				}
			}
			if (flush && (buffer.length() > 0)) {
				messages.add(buffer.toString());
				buffer.setLength(0);
			}
			if (chunk != null) {
				if (buffer.length() > 0) {
					buffer.append(' ');
				}
				buffer.append(chunk);
			}
		} while (line != null);
		this.code = code;
	}

	public int getCode() {
		return code;
	}
	
	public String[] getMessages() {
		return messages.toArray(new String[messages.size()]);
	}
}
