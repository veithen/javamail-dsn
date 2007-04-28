package net.sf.javamaildsn.type;

import java.util.HashMap;
import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.MimeUtility;

/**
 * @author Andreas Veithen
 */
public class TypedFieldParser<T> {
	private final Map<String,FieldType<? extends T>> types = new HashMap<String,FieldType<? extends T>>();
	private FieldType<? extends T> defaultType;
	
	private String normalizeIdentifier(String identifier) {
		return identifier.toLowerCase();
	}
	
	public void addType(String identifier, FieldType<? extends T> type) {
		types.put(normalizeIdentifier(identifier), type);
	}
	
	public void setDefaultType(FieldType<? extends T> defaultType) {
		this.defaultType = defaultType;
	}
	
	public T parse(String value) throws MessagingException {
		int index = value.indexOf(';');
		if (index == -1) {
			throw new MessagingException("Invalid format for typed field");
		} else {
			String identifier = normalizeIdentifier(value.substring(0, index).trim());
			FieldType<? extends T> type = types.get(identifier);
			if (type == null) {
				type = defaultType;
			}
			return type.parse(identifier, MimeUtility.unfold(value.substring(index+1).trim()));
		}
	}
}
