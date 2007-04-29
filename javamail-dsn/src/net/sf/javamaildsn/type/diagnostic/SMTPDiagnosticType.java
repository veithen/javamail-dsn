package net.sf.javamaildsn.type.diagnostic;

import java.util.LinkedList;
import java.util.List;

import javax.mail.MessagingException;

import net.sf.javamaildsn.SMTPDiagnostic;
import net.sf.javamaildsn.type.FieldType;

/**
 * @author Andreas Veithen
 */
public class SMTPDiagnosticType implements FieldType<SMTPDiagnostic> {
	public SMTPDiagnostic parse(String type, String value) throws MessagingException {
		if (value.length() < 4) {
			throw new MessagingException("Invalid SMTP diagnostic format");
		}
		List<String> messages = new LinkedList<String>();
		String codeString = value.substring(0, 3);
		int code;
		boolean hasMoreLines;
		try {
			code = Integer.parseInt(codeString);
		}
		catch (NumberFormatException ex) {
			throw new MessagingException("Invalid SMTP diagnostic format: expected SMTP reply code");
		}
		switch (value.charAt(3)) {
			case '-': hasMoreLines = true; break;
			case ' ': hasMoreLines = false; break;
			default: throw new MessagingException("Invalid SMTP diagnostic format: expected '-' or space after SMTP reply code");
		}
		int index = 4;
		while (true) {
			if (hasMoreLines) {
				loop: while (true) {
					int newIndex = value.indexOf(codeString, index);
					if (newIndex == -1) {
						throw new MessagingException("Invalid SMTP diagnostic format");
					} else {
						switch (value.charAt(newIndex+3)) {
							case ' ':
								hasMoreLines = false;
							case '-':
								messages.add(value.substring(index, newIndex).trim());
								index = newIndex+4;
								break loop;
						}
					}
				}
			} else {
				messages.add(value.substring(index));
				break;
			}
		}
		return new SMTPDiagnostic(code, messages.toArray(new String[messages.size()]));
	}
}
