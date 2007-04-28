package net.sf.javamaildsn.type.address;

import javax.mail.MessagingException;

import net.sf.javamaildsn.UnknownAddress;
import net.sf.javamaildsn.type.FieldType;

/**
 * @author Andreas Veithen
 */
public class UnknownAddressType implements FieldType<UnknownAddress> {
	public UnknownAddress parse(String type, String value) throws MessagingException {
		return new UnknownAddress(type, value);
	}
}
