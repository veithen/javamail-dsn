package net.sf.javamaildsn;

public class XPostfixRelayDiagnostic implements Diagnostic {
	private final String host;
	private final String altHost;
	private final SMTPDiagnostic smtpDiagnostic;
	private final String inReplyTo;
	
	public XPostfixRelayDiagnostic(String host, String altHost, String message, String inReplyTo) {
		this.host = host;
		this.altHost = altHost;
		smtpDiagnostic = new SMTPDiagnostic(message);
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

	public String getRootCause() {
		return smtpDiagnostic.getRootCause();
	}
}
