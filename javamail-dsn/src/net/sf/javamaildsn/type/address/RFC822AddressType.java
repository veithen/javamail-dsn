package net.sf.javamaildsn.type.address;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;

import net.sf.javamaildsn.type.FieldType;

/**
 * @author Andreas Veithen
 */
public class RFC822AddressType implements FieldType<InternetAddress> {
	public InternetAddress parse(String type, String value) throws MessagingException {
		return new InternetAddress(value);
	}
}
