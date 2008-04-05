package net.sf.javamaildsn.type.diagnostic;

import net.sf.javamaildsn.MailSystemStatus;
import net.sf.javamaildsn.SMTPReply;
import net.sf.javamaildsn.StatusMessage;

/**
 * @author Andreas Veithen
 */
public class XPostfixRelayDiagnostic implements XPostfixDiagnostic {
	private final PostfixMtaName mta;
	private final SMTPReply smtpReply;
	private final String inReplyTo;
	
	public XPostfixRelayDiagnostic(PostfixMtaName mta, SMTPReply smtpReply, String inReplyTo) {
		this.mta = mta;
		this.smtpReply = smtpReply;
		this.inReplyTo = inReplyTo;
	}
	
	public PostfixMtaName getMta() {
		return mta;
	}

	public SMTPReply getSmtpReply() {
		return smtpReply;
	}

	public String getInReplyTo() {
		return inReplyTo;
	}
	
	public int getCode() {
		return smtpReply.getCode();
	}
	
	public MailSystemStatus getStatus() {
		return smtpReply.getStatus();
	}
	
	public StatusMessage getMessage() {
		return smtpReply.getMessage();
	}
}
