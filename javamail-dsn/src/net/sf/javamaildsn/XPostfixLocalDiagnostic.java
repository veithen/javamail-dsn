package net.sf.javamaildsn;

/**
 * @author Andreas Veithen
 */
public class XPostfixLocalDiagnostic implements XPostfixDiagnostic {
	private final String message;
	
	public XPostfixLocalDiagnostic(String message) {
		this.message = message;
	}
	
	public String getRootCause() {
		return message;
	}
}
