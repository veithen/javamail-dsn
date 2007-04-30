package net.sf.javamaildsn;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.MessagingException;

public class SMTPReply {
	private final static Pattern statusPrefixPattern = Pattern.compile("(" + MailSystemStatus.PATTERN + ")\\s(.*)");
	private final static Pattern statusCommentPattern = Pattern.compile("(.*)\\s\\(#(" + MailSystemStatus.PATTERN + ")\\)");
	
	private final int code;
	private final MailSystemStatus status;
	private final String[] messages;
	
	public SMTPReply(String value) throws MessagingException {
		if (value.length() < 4) {
			throw new MessagingException("Invalid SMTP diagnostic format");
		}
		List<String> messages = new LinkedList<String>();
		String codeString = value.substring(0, 3);
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
				String message = value.substring(index);
				Matcher matcher;
				if ((matcher = statusPrefixPattern.matcher(message)).matches()) {
					status = new MailSystemStatus(matcher.group(1));
					message = matcher.group(2);
				} else if ((matcher = statusCommentPattern.matcher(message)).matches()) {
					message = matcher.group(1);
					status = new MailSystemStatus(matcher.group(2));
				} else {
					status = null;
				}
				messages.add(message);
				break;
			}
		}
		this.messages = messages.toArray(new String[messages.size()]);
	}

	public int getCode() {
		return code;
	}

	public MailSystemStatus getStatus() {
		return status;
	}
	
	public String[] getMessages() {
		return messages;
	}
}
