package net.sf.javamaildsn.type;

import javax.mail.MessagingException;

public interface FieldType<T> {
	/**
	 * 
	 * value must be unfolded
	 * @param type TODO
	 * @param value
	 * 
	 * @return
	 * @throws MessagingException TODO
	 */
	T parse(String type, String value) throws MessagingException;
}
