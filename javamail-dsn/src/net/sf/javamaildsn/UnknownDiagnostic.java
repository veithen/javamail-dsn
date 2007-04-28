package net.sf.javamaildsn;

import javax.mail.internet.MimeUtility;

/**
 * @author Andreas Veithen
 */
public class UnknownDiagnostic implements Diagnostic {
	private final String value;
	
	public UnknownDiagnostic(String value) {
		this.value = MimeUtility.unfold(value);
	}

	public String getRootCause() {
		return value;
	}
}
