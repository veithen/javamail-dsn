package net.sf.javamaildsn.type.diagnostic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.MessagingException;

import net.sf.javamaildsn.SMTPDiagnostic;
import net.sf.javamaildsn.type.FieldType;

public class SMTPDiagnosticType implements FieldType<SMTPDiagnostic> {
	private final static Pattern linePattern = Pattern.compile("\\s*(\\d{3})[ -](.*)");
	
	public SMTPDiagnostic parse(String type, String value) throws MessagingException {
		int code = -1;
		List<String> messages = new LinkedList<String>();
		BufferedReader in = new BufferedReader(new StringReader(value));
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
		return new SMTPDiagnostic(code, messages.toArray(new String[messages.size()]));
	}
}
