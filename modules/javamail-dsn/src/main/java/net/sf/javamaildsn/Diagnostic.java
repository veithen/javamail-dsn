package net.sf.javamaildsn;

/**
 * @author Andreas Veithen
 */
public interface Diagnostic {
    /**
     * Get the name of the MTA that provided the diagnostic.
     * 
     * @return the MTA name
     */
	MtaName getMta();
	
	/**
	 * Get the SMTP reply code describing the error condition.
	 * 
	 * @return the SMTP reply code, or -1 if no reply code is available
	 */
	int getCode();
	
	/**
	 * Get the mail system status describing the error condition
	 * 
	 * @return
	 */
	MailSystemStatus getStatus();
	
	/**
	 * Get the message describing the error condition.
	 * 
	 * @return the message describing the error conditions
	 */
	StatusMessage getMessage();
}
