package net.sf.javamaildsn;

/**
 * @author Andreas Veithen
 */
public class XPostfixRelayDiagnostic implements XPostfixDiagnostic {
	private final String host;
	private final String altHost;
	private final SMTPDiagnostic smtpDiagnostic;
	private final String inReplyTo;
	
	public XPostfixRelayDiagnostic(String host, String altHost, SMTPDiagnostic smtpDiagnostic, String inReplyTo) {
		this.host = host;
		this.altHost = altHost;
		this.smtpDiagnostic = smtpDiagnostic;
		this.inReplyTo = inReplyTo;
	}
	
	public String getHost() {
		return host;
	}

	public String getAltHost() {
		return altHost;
	}

	public SMTPDiagnostic getSmtpDiagnostic() {
		return smtpDiagnostic;
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
		return new Cause(null /* TODO */, smtpDiagnostic.getCode(), smtpDiagnostic.getStatus(), smtpDiagnostic.getMessage());
	}
}
