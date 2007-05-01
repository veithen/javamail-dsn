package net.sf.javamaildsn;

import net.sf.javamaildsn.type.diagnostic.PostfixMtaName;

import org.apache.commons.lang.StringUtils;

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
	
	public String getMessage() {
		return StringUtils.join(smtpReply.getMessages(), "\n");
	}
}
