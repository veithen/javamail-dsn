package net.sf.javamaildsn.type;

import javax.mail.MessagingException;

/**
 * @author Andreas Veithen
 */
public interface FieldType<T> {
	/**
	 * 
	 * value is unfolded
	 * value may contain comments
	 * 
	 * @param type TODO
	 * @param value
	 * 
	 * @return
	 * @throws MessagingException TODO
	 */
	T parse(String type, String value) throws MessagingException;
}
