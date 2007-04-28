package net.sf.javamaildsn;

import java.io.IOException;
import java.io.OutputStream;

import javax.activation.DataSource;
import javax.mail.MessagingException;
import javax.mail.internet.InternetHeaders;

import net.sf.javamaildsn.SimpleDataContentHandler;

/**
 * @author Andreas Veithen
 */
public class TextRFC822Handler extends SimpleDataContentHandler<InternetHeaders> {
	public TextRFC822Handler() {
		super(InternetHeaders.class, "text/rfc822", "RFC822 Headers");
	}
	
	@Override
	public InternetHeaders getContent(DataSource dataSource) throws IOException {
		try {
			return new InternetHeaders(dataSource.getInputStream());
		}
		catch (MessagingException ex) {
			throw new IOException(ex.getMessage());
		}
	}
	
	@Override
	public void writeTo(InternetHeaders headers, OutputStream os) throws IOException {
		throw new UnsupportedOperationException();
	}
}
