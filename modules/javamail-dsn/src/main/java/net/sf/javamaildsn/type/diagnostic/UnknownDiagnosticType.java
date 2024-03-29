package net.sf.javamaildsn.type.diagnostic;

import javax.mail.MessagingException;

import net.sf.javamaildsn.DeliveryStatus;
import net.sf.javamaildsn.PerRecipientDeliveryStatus;
import net.sf.javamaildsn.UnknownDiagnostic;
import net.sf.javamaildsn.type.FieldType;

/**
 * @author Andreas Veithen
 */
public class UnknownDiagnosticType implements FieldType<UnknownDiagnostic> {
	public UnknownDiagnostic parse(String type, String value, DeliveryStatus ds, PerRecipientDeliveryStatus rds) throws MessagingException {
		return new UnknownDiagnostic(value);
	}
}
