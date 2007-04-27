package net.sf.javamaildsn;

import java.io.IOException;
import java.io.OutputStream;

import javax.activation.DataSource;
import javax.mail.MessagingException;

import net.sf.javamaildsn.DeliveryStatus;
import net.sf.javamaildsn.SimpleDataContentHandler;

public class DeliveryStatusHandler extends SimpleDataContentHandler<DeliveryStatus> {
	public DeliveryStatusHandler() {
		super(DeliveryStatus.class, "message/delivery-status", "Delivery Status");
	}
	
	public DeliveryStatus getContent(DataSource dataSource) throws IOException {
		try {
			return new DeliveryStatus(dataSource.getInputStream());
		}
		catch (MessagingException ex) {
			throw new IOException(ex.getMessage());
		}
	}
	
	public void writeTo(DeliveryStatus object, OutputStream os) throws IOException {
		throw new UnsupportedOperationException();
	}
}
