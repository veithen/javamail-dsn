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
	private final MailSystemStatus status;
	private final String[] messages;
	
	public SMTPDiagnostic(int code, MailSystemStatus status, String[] messages) {
		this.code = code;
		this.status = status;
		this.messages = messages;
	}

	public int getCode() {
		return code;
	}

	public MailSystemStatus getStatus() {
		return status;
	}
	
	public String[] getMessages() {
		return messages.clone();
	}
	
	public String getMessage() {
		return StringUtils.join(messages, "\n");
	}
	
	public Cause getCause() {
		return null;
	}
}
