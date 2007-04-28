package net.sf.javamaildsn;

import java.io.IOException;
import javax.activation.DataSource;
import javax.mail.MessagingException;

import net.sf.javamaildsn.DeliveryStatus;
import net.sf.javamaildsn.MultipartReport;

/**
 * @author Andreas Veithen
 */
public class MultipartReportDeliveryStatus extends MultipartReport<DeliveryStatus> {
	public MultipartReportDeliveryStatus(DataSource dataSource) throws MessagingException, IOException {
		super(DeliveryStatus.class, dataSource);
	}
	
	public DeliveryStatus getDeliveryStatus() { return getMainPart(); }
}
