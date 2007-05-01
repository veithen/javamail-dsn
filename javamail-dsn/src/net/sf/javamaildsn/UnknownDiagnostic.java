package net.sf.javamaildsn;

import javax.mail.internet.MimeUtility;

/**
 * @author Andreas Veithen
 */
public class UnknownDiagnostic implements Diagnostic {
	private final StatusMessage message;
	
	public UnknownDiagnostic(String value) {
		message = new SingleLineStatusMessage(MimeUtility.unfold(value));
	}
	
	public MtaName getMta() {
		return null; // TODO
	}
	
	public int getCode() {
		return -1;
	}
	
	public MailSystemStatus getStatus() {
		return null;
	}
	
	public StatusMessage getMessage() {
		return message;
	}
}
