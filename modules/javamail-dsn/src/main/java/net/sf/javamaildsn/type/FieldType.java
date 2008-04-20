package net.sf.javamaildsn.type;

import javax.mail.MessagingException;

import net.sf.javamaildsn.DeliveryStatus;
import net.sf.javamaildsn.PerRecipientDeliveryStatus;

/**
 * @author Andreas Veithen
 */
// TODO: consider renaming this TypeHandler
public interface FieldType<T> {
	/**
	 * 
	 * value is unfolded
	 * value may contain comments
	 * 
	 * @param type TODO
	 * @param value
	 * @param ds TODO
	 * @param rds TODO
	 * @return
	 * @throws MessagingException TODO
	 */
	T parse(String type, String value, DeliveryStatus ds, PerRecipientDeliveryStatus rds) throws MessagingException;
}
