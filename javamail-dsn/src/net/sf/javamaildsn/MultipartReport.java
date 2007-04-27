package net.sf.javamaildsn;

import java.io.IOException;
import javax.activation.DataSource;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.InternetHeaders;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.ParseException;

import net.sf.javamaildsn.MimeMessageHeaders;

/**
 * RFC3462
 */
public class MultipartReport<C> {
	private final MimeMultipart content;
	private final String notification;
	private final C mainPart;
	private final MimeMessage orgHeaders;
	
	public MultipartReport(Class<C> mainPartClass, DataSource dataSource) throws MessagingException, IOException {
		content = new MimeMultipart(dataSource);
		int count = content.getCount();
		if (count < 2 || count > 3) {
			throw new ParseException("A multipart/report message must have 2 or 3 body parts");
		}
		
		Object notification = content.getBodyPart(0).getContent();
		if (notification instanceof String) {
			this.notification = (String)notification;
		} else {
			this.notification = null;
		}
		
		try {
			mainPart = mainPartClass.cast(content.getBodyPart(1).getContent());
		}
		catch (ClassCastException ex) {
			throw new ParseException("Expected a " + mainPartClass.getName() + " object as the main part of this multipart/report message");
		}
		
		if (count == 3) {
			Object orgMessage = content.getBodyPart(2).getContent();
			if (orgMessage instanceof MimeMessage) {
				orgHeaders = (MimeMessage)orgMessage;
			} else if (orgMessage instanceof InternetHeaders) {
				orgHeaders = new MimeMessageHeaders((InternetHeaders)orgMessage);
			} else {
				throw new ParseException("Expected a " + MimeMessage.class.getName() + " or " + InternetHeaders.class.getName() + " object as the last part of a multipart/report message. Found a " + orgMessage.getClass().getName() + " object.");
			}
		} else {
			orgHeaders = null;
		}
	}
	
	public String getNotification() throws MessagingException {
		if (notification == null) {
			throw new MessagingException("The notification part of the mutlipart/report could not be extracted");
		} else {
			return notification;
		}
	}
	
	public C getMainPart() { return mainPart; }
	
	public Address[] getOriginalRecipients(Message.RecipientType type) throws MessagingException {
		return orgHeaders.getRecipients(type);
	}
}
