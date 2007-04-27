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
	private final String value;
	
	public SMTPDiagnostic(String value) {
		this.value = value;
	}
	
	@Override
	public String toString() {
		return value;
	}
}
