package net.sf.javamaildsn;

import javax.mail.internet.MimeUtility;

public class UnknownDiagnostic implements Diagnostic {
	private final String value;
	
	public UnknownDiagnostic(String value) {
		this.value = MimeUtility.unfold(value);
	}

	public String getRootCause() {
		return value;
	}
}
