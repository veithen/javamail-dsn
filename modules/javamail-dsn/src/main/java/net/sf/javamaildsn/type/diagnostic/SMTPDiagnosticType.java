package net.sf.javamaildsn.type.diagnostic;

import javax.mail.MessagingException;

import net.sf.javamaildsn.DeliveryStatus;
import net.sf.javamaildsn.MailSystemStatus;
import net.sf.javamaildsn.MtaName;
import net.sf.javamaildsn.PerRecipientDeliveryStatus;
import net.sf.javamaildsn.SMTPDiagnostic;
import net.sf.javamaildsn.SMTPReply;
import net.sf.javamaildsn.type.FieldType;

/**
 * Implementation of the <tt>smtp</tt> type for the <tt>Diagnostic-Code</tt> DSN field.
 * <p>
 * This type is described in section 9.2 of RFC3461:
 * <blockquote>
 * An SMTP diagnostic-code is of the form
 * <pre>
 *    *( 3*DIGIT "-" *text ) 3*DIGIT SPACE *text</pre>
 * For a single-line SMTP reply to an SMTP command, the
 * diagnostic-code SHOULD be an exact transcription of the reply.
 * For multi-line SMTP replies, it is necessary to insert a SPACE
 * before each line after the first.  For example, an SMTP reply
 * of:
 * <pre>
 *    550-mailbox unavailable
 *    550 user has moved with no forwarding address</pre>
 * could appear as follows in a Diagnostic-Code DSN field:
 * <pre>
 *    Diagnostic-Code: smtp ; 550-mailbox unavailable
 *     550 user has moved with no forwarding address</pre>
 * </blockquote>
 * 
 * @author Andreas Veithen
 */
public class SMTPDiagnosticType implements FieldType<SMTPDiagnostic> {
	public SMTPDiagnostic parse(String type, String value, DeliveryStatus ds, PerRecipientDeliveryStatus rds) throws MessagingException {
		SMTPReply smtpReply = new SMTPReply(value);
		MtaName mta;
		MailSystemStatus status = smtpReply.getStatus();
		MtaName remoteMta = rds.getRemoteMta();
		if (remoteMta == null) {
			mta = ds.getReportingMta();
			// Always keep the status from the SMTP reply, if it is present
			if (status == null) {
				// The status in the DSN is only reliable if it comes from the reporting MTA 
				status = rds.getStatus();
			}
		} else {
			mta = remoteMta;
		}
		return new SMTPDiagnostic(mta, status, smtpReply);
	}
}
