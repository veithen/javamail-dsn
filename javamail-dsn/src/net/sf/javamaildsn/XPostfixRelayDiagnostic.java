package net.sf.javamaildsn;

import org.apache.commons.lang.StringUtils;

/**
 * @author Andreas Veithen
 */
public class XPostfixRelayDiagnostic implements XPostfixDiagnostic {
	private final String host;
	private final String altHost;
	private final SMTPReply smtpReply;
	private final String inReplyTo;
	
	public XPostfixRelayDiagnostic(String host, String altHost, SMTPReply smtpReply, String inReplyTo) {
		this.host = host;
		this.altHost = altHost;
		this.smtpReply = smtpReply;
		this.inReplyTo = inReplyTo;
	}
	
	public String getHost() {
		return host;
	}

	public String getAltHost() {
		return altHost;
	}

	public SMTPReply getSmtpReply() {
		return smtpReply;
	}

	public String getInReplyTo() {
		return inReplyTo;
	}
	
	public int getCode() {
		return -1;
	}
	
	public MailSystemStatus getStatus() {
		return null;
	}
	
	public String getMessage() {
		return null;
	}

	public Cause getCause() {
		return new Cause(null /* TODO */, smtpReply.getCode(), smtpReply.getStatus(), StringUtils.join(smtpReply.getMessages(), "\n"));
	}
}
