package net.sf.javamaildsn;

/**
 * @author Andreas Veithen
 */
public interface Diagnostic {
	MailSystemStatus getStatus();
	String getRootCause();
}
