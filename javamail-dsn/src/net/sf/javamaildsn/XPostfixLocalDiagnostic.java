package net.sf.javamaildsn;

/**
 * @author Andreas Veithen
 */
public class XPostfixLocalDiagnostic implements XPostfixDiagnostic {
	private final MtaName mta;
	private final String message;
	
	public XPostfixLocalDiagnostic(MtaName mta, String message) {
		this.mta = mta;
		this.message = message;
	}
	
	public MtaName getMta() {
		return mta;
	}
	
	public int getCode() {
		return -1;
	}
	
	public MailSystemStatus getStatus() {
		return null;
	}

	public String getMessage() {
		return message;
	}
}
