package net.sf.javamaildsn;

import java.io.InputStream;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.InternetHeaders;
import javax.mail.internet.MailDateFormat;
import javax.mail.internet.ParseException;

/**
 * This class represents a <tt>message/delivery-status</tt> message as defined in RFC 1894 and 3464.
 * 
 * TODO: per message info and per recipient info
 * 
 * @author Andreas Veithen
 */
public class DeliveryStatus {
	private static MailDateFormat mailDateFormat = new MailDateFormat();
	
	private final InternetHeaders headers;
	private final List<PerRecipientDeliveryStatus> rdsList = new LinkedList<PerRecipientDeliveryStatus>();
	
	private MtaName cachedReportingMta;
	
	private boolean isArrivalDateCached;
	private Date cachedArrivalDate;
	
	public DeliveryStatus(InputStream is) throws MessagingException {
		headers = new InternetHeaders(is);
		while (true) {
			try {
				rdsList.add(new PerRecipientDeliveryStatus(this, is));
			}
			catch (MessagingException ex) {
				// Suppose that this is because we reached the end of the input stream
				break;
			}
		}
	}
	
	public String getOriginalEnvelopeId() throws ParseException {
		return HeaderUtils.getOptionalUniqueHeader(headers, "Original-Envelope-Id");
	}
	
	/**
	 * Get the name of the MTA that attempted to perform the delivery, relay, or gateway
     * operation described in this DSN.
     * Since this information is mandatory, an exception will be thrown if it can't be extracted.
     * <p>
     * For more information, see section 2.2.2 of RFC 3464. 
	 * 
	 * @return the name of the reporting MTA
	 * @throws MessagingException if the name of the MTA could not be extracted
	 */
	public MtaName getReportingMta() throws MessagingException {
		if (cachedReportingMta == null) {
			cachedReportingMta = HeaderUtils.parseMtaName(HeaderUtils.getRequiredUniqueHeader(headers, "Reporting-MTA"), this, null);
		}
		return cachedReportingMta;
	}
	
	/**
	 * Get the name of the gateway or MTA that translated a foreign (non-Internet) delivery
	 * status notification into this DSN. If the DSN was not translated by a gateway from a
	 * foreign system into DSN format, <code>null</code> is returned. If the DSN was translated by
	 * a gateway but the name could not be extracted, an exception is thrown.
     * <p>
     * For more information, see section 2.2.3 of RFC 3464.
	 * 
	 * @return the name of the translating gateway or <code>null</code> if the DSN was not translated
	 * @throws MessagingException if the name of the translating gateway could not be extracted
	 */
    public MtaName getDsnGateway() throws MessagingException {
		return HeaderUtils.parseMtaName(HeaderUtils.getRequiredUniqueHeader(headers, "DSN-Gateway"), this, null);
	}
    
    /**
     * Get the name of the MTA from which the message was received. If this information is
     * not available or could not be extracted, <code>null</code> is returned.
     * <p>
     * For more information, see section 2.2.4 of RFC 3464.
     *  
     * @return the name of the MTA from which the message was received, or <code>null</code>
     * if this information is not available
     */
	public MtaName getReceivedFromMta() {
		try {
			String value = HeaderUtils.getOptionalUniqueHeader(headers, "Received-From-MTA");
			return value == null ? null : HeaderUtils.parseMtaName(value, this, null);
		}
		catch (MessagingException ex) {
			return null;
		}
	}
	
	/**
	 * Get the date and time at which the message arrived at the reporting MTA. If this
	 * information is not available or could not be extracted, <code>null</code> is returned.
     * <p>
     * For more information, see section 2.2.5 of RFC 3464.
	 * 
	 * @return the date and time at which the message arrived at the reporting MTA, or <code>null</code>
	 * if this information is not available
	 */
	public Date getArrivalDate() {
		if (!isArrivalDateCached) {
			String value = null;
			try {
				value = HeaderUtils.getOptionalUniqueHeader(headers, "Arrival-Date");
			}
			catch (MessagingException ex) {
				// Do nothing
			}
			if (value != null) {
				try {
					cachedArrivalDate = mailDateFormat.parse(value);
				}
				catch (java.text.ParseException ex) {
					// Do nothing
				}
			}
			isArrivalDateCached = true;
		}
		return (Date)cachedArrivalDate.clone();
	}
	
	/**
	 * Get the per recipient parts of the DSN.
	 * 
	 * @return an array of <code>PerRecipientDeliveryStatus</code> objects
	 */
	public PerRecipientDeliveryStatus[] getPerRecipientParts() {
		return rdsList.toArray(new PerRecipientDeliveryStatus[rdsList.size()]);
	}
	
	@Override
	public String toString() {
		StringBuilder buffer = new StringBuilder();
		buffer.append(HeaderUtils.dumpHeaders(headers));
		buffer.append('\n');
		for (PerRecipientDeliveryStatus rds : rdsList) {
			buffer.append(rds);
			buffer.append('\n');
		}
		return buffer.toString();
	}
}
