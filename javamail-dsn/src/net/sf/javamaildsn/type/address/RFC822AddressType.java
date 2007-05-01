package net.sf.javamaildsn.type.address;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;

import net.sf.javamaildsn.DeliveryStatus;
import net.sf.javamaildsn.PerRecipientDeliveryStatus;
import net.sf.javamaildsn.type.FieldType;

/**
 * @author Andreas Veithen
 */
public class RFC822AddressType implements FieldType<InternetAddress> {
	public InternetAddress parse(String type, String value, DeliveryStatus ds, PerRecipientDeliveryStatus rds) throws MessagingException {
		return new InternetAddress(value);
	}
}
