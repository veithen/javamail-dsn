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
