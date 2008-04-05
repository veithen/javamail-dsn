package net.sf.javamaildsn;

/**
 * @author Andreas Veithen
 */
public interface Diagnostic {
	MtaName getMta();
	int getCode();
	MailSystemStatus getStatus();
	StatusMessage getMessage();
}
