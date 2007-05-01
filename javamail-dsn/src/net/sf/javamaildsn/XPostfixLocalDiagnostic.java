package net.sf.javamaildsn;

/**
 * @author Andreas Veithen
 */
public class XPostfixLocalDiagnostic implements XPostfixDiagnostic {
	private final MtaName mta;
	private final MailSystemStatus status;
	private final StatusMessage message;
	
	public XPostfixLocalDiagnostic(MtaName mta, MailSystemStatus status, String message) {
		this.mta = mta;
		this.status = status;
		this.message = new SingleLineStatusMessage(message);
	}
	
	public MtaName getMta() {
		return mta;
	}
	
	public int getCode() {
		return -1;
	}
	
	public MailSystemStatus getStatus() {
		return status;
	}

	public StatusMessage getMessage() {
		return message;
	}
}
