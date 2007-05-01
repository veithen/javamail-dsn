package net.sf.javamaildsn;

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
	private final MtaName mta;
	private final MailSystemStatus status;
	private final SMTPReply reply;
	
	public SMTPDiagnostic(MtaName mta, MailSystemStatus status, SMTPReply reply) {
		this.mta = mta;
		this.status = status;
		this.reply = reply;
	}
	
	public MtaName getMta() {
		return mta;
	}

	public int getCode() {
		return reply.getCode();
	}

	public MailSystemStatus getStatus() {
		return status;
	}
	
	public StatusMessage getMessage() {
		return reply.getMessage();
	}
}
