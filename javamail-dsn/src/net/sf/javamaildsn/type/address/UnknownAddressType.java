package net.sf.javamaildsn.type.address;

import javax.mail.MessagingException;

import net.sf.javamaildsn.DeliveryStatus;
import net.sf.javamaildsn.PerRecipientDeliveryStatus;
import net.sf.javamaildsn.UnknownAddress;
import net.sf.javamaildsn.type.FieldType;

/**
 * @author Andreas Veithen
 */
public class UnknownAddressType implements FieldType<UnknownAddress> {
	public UnknownAddress parse(String type, String value, DeliveryStatus ds, PerRecipientDeliveryStatus rds) throws MessagingException {
		return new UnknownAddress(type, value);
	}
}
