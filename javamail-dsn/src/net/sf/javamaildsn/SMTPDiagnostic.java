package net.sf.javamaildsn;

import org.apache.commons.lang.StringUtils;

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
	private final int code;
	private final String[] messages;
	
	public SMTPDiagnostic(int code, String[] messages) {
		this.code = code;
		this.messages = messages;
	}

	public int getCode() {
		return code;
	}
	
	public String[] getMessages() {
		return messages.clone();
	}
	
	public String getMessage() {
		return StringUtils.join(messages, "\n");
	}

	public String getRootCause() {
		return getMessage();
	}
}
