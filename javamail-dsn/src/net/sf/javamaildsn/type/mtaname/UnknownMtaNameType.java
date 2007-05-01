package net.sf.javamaildsn.type.mtaname;

import javax.mail.MessagingException;

import net.sf.javamaildsn.DeliveryStatus;
import net.sf.javamaildsn.PerRecipientDeliveryStatus;
import net.sf.javamaildsn.UnknownMtaName;
import net.sf.javamaildsn.type.FieldType;

/**
 * @author Andreas Veithen
 */
public class UnknownMtaNameType implements FieldType<UnknownMtaName> {
	public UnknownMtaName parse(String type, String value, DeliveryStatus ds, PerRecipientDeliveryStatus rds) throws MessagingException {
		return new UnknownMtaName(type, value);
	}
}
