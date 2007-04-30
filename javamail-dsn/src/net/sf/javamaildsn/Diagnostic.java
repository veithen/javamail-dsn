package net.sf.javamaildsn;

/**
 * @author Andreas Veithen
 */
public interface Diagnostic {
	int getCode();
	MailSystemStatus getStatus();
	String getMessage();
	Cause getCause();
}
