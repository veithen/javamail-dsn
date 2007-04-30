package net.sf.javamaildsn.type.diagnostic;

import javax.mail.MessagingException;

import net.sf.javamaildsn.SMTPDiagnostic;
import net.sf.javamaildsn.SMTPReply;
import net.sf.javamaildsn.type.FieldType;

/**
 * @author Andreas Veithen
 */
public class SMTPDiagnosticType implements FieldType<SMTPDiagnostic> {
	public SMTPDiagnostic parse(String type, String value) throws MessagingException {
		SMTPReply reply = new SMTPReply(value);
		return new SMTPDiagnostic(reply.getCode(), reply.getStatus(), reply.getMessages());
	}
}
