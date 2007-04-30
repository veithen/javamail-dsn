package net.sf.javamaildsn;

/**
 * @author Andreas Veithen
 */
public class XPostfixLocalDiagnostic implements XPostfixDiagnostic {
	private final String message;
	
	public XPostfixLocalDiagnostic(String message) {
		this.message = message;
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
	
	public Cause getCause() {
		return null;
	}
}
