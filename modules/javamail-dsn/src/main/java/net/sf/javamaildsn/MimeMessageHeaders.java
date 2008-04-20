package net.sf.javamaildsn;

import javax.mail.Session;
import javax.mail.internet.InternetHeaders;
import javax.mail.internet.MimeMessage;

/**
 * @author Andreas Veithen
 */
public class MimeMessageHeaders extends MimeMessage {
	public MimeMessageHeaders(InternetHeaders headers) {
		super((Session)null);
		this.headers = headers;
	}
}
