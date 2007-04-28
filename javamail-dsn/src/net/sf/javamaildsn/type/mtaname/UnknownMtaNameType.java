package net.sf.javamaildsn.type.mtaname;

import javax.mail.MessagingException;

import net.sf.javamaildsn.type.FieldType;

public class UnknownMtaNameType implements FieldType<String> {
	public String parse(String type, String value) throws MessagingException {
		return value;
	}
}
