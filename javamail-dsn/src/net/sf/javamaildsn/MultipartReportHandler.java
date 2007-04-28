package net.sf.javamaildsn;

import java.io.IOException;
import java.io.OutputStream;

import javax.activation.DataSource;
import javax.mail.MessagingException;
import javax.mail.internet.ContentType;

import net.sf.javamaildsn.MultipartReport;
import net.sf.javamaildsn.MultipartReportDeliveryStatus;
import net.sf.javamaildsn.SimpleDataContentHandler;

/**
 * @author Andreas Veithen
 */
public class MultipartReportHandler extends SimpleDataContentHandler<MultipartReport> {
	public MultipartReportHandler() {
		super(MultipartReport.class, "multipart/report", "Multipart Report");
	}
	
	@Override
	public MultipartReport getContent(DataSource dataSource) throws IOException {
		try {
			String reportType = new ContentType(dataSource.getContentType()).getParameter("report-type");
			if ("delivery-status".equals(reportType)) {
				return new MultipartReportDeliveryStatus(dataSource);
			} else {
				throw new IOException("Unknown report type '" + reportType + "'");
			}
		}
		catch (MessagingException ex) {
			IOException ex2 = new IOException(ex.getMessage());
			ex2.initCause(ex);
			throw ex2;
		}
	}
	
	@Override
	public void writeTo(MultipartReport object, OutputStream os) throws IOException {
		throw new UnsupportedOperationException();
	}
}
