package net.sf.javamaildsn;

import java.util.ResourceBundle;
import java.util.regex.Pattern;

import javax.mail.internet.ParseException;

import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * Class representing an enhanced mail system status code as defined by RFC 3463.
 * 
 * @author Andreas Veithen
 *
 */
public class MailSystemStatus {
	public final static Pattern PATTERN = Pattern.compile("[1-5]\\.\\d{1,3}\\.\\d{1,3}");
	
	private final static ResourceBundle messages = ResourceBundle.getBundle(MailSystemStatus.class.getName());
	
	private final int clazz;
	private final int subject;
	private final int detail;
	
	public MailSystemStatus(int clazz, int subject, int detail) {
		this.clazz = clazz;
		this.subject = subject;
		this.detail = detail;
	}
	
	public MailSystemStatus(String value) throws ParseException {
		String[] parts = value.split("\\.");
		if (parts.length == 3) {
			try {
				clazz = Integer.parseInt(parts[0]);
				subject = Integer.parseInt(parts[1]);
				detail = Integer.parseInt(parts[2]);
			}
			catch (NumberFormatException ex) {
				throw new ParseException("Invalid status code " + value);
			}
		} else {
			throw new ParseException("Invalid status code " + value);
		}
	}
	
	public String getCanonicalMessage() {
		return messages.getString(subject + "." + detail);
	}
	
	@Override
	public String toString() {
		return clazz + "." + subject + "." + detail;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(clazz).append(subject).append(detail).toHashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj != null && obj instanceof MailSystemStatus) {
			MailSystemStatus cobj = (MailSystemStatus)obj;
			return clazz == cobj.clazz && subject == cobj.subject && detail == cobj.detail;
		} else {
			return false;
		}
	}
}
