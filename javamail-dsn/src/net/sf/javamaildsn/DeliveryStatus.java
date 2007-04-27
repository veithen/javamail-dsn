package net.sf.javamaildsn;

import java.io.InputStream;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.mail.Address;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.InternetHeaders;
import javax.mail.internet.MailDateFormat;
import javax.mail.internet.ParseException;

import net.sf.javamaildsn.HeaderUtils;
import net.sf.javamaildsn.PerRecipientDeliveryStatus;

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
	private final Map<Address,PerRecipientDeliveryStatus> recip = new LinkedHashMap<Address,PerRecipientDeliveryStatus>();
	
	public DeliveryStatus(InputStream is) throws MessagingException {
		headers = new InternetHeaders(is);
		while (true) {
			try {
				PerRecipientDeliveryStatus recipDSN = new PerRecipientDeliveryStatus(is);
				// TODO: we should also hash on original recipients
				recip.put(recipDSN.getFinalRecipient(), recipDSN);
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
	 * Get the host name of the MTA that attempted to perform the delivery, relay, or gateway
     * operation described in this DSN.
     * Since this information is mandatory, an exception will be thrown if it can't be extracted.
     * <p>
     * For more information, see section 2.2.2 of RFC 3464. 
	 * 
	 * @return the host name of the reporting MTA
	 * @throws MessagingException if the host name of the MTA could not be extracted
	 */
	public String getReportingMtaHostName() throws MessagingException {
		return HeaderUtils.parseMtaName(HeaderUtils.getRequiredUniqueHeader(headers, "Reporting-MTA"));
	}
	
	/**
	 * Get the host name of the gateway or MTA that translated a foreign (non-Internet) delivery
	 * status notification into this DSN. If the DSN was not translated by a gateway from a
	 * foreign system into DSN format, <code>null</code> is returned. If the DSN was translated by
	 * a gateway but the host name could not be extracted, an exception is thrown.
     * <p>
     * For more information, see section 2.2.3 of RFC 3464.
	 * 
	 * @return the host name of the translating gateway or <code>null</code> if the DSN was not translated
	 * @throws MessagingException if the host name of the translating gateway could not be extracted
	 */
    public String getDsnGatewayHostName() throws MessagingException {
		return HeaderUtils.parseMtaName(HeaderUtils.getRequiredUniqueHeader(headers, "DSN-Gateway"));
	}
    
    /**
     * Get the host name of the MTA from which the message was received. If this information is
     * not available or could not be extracted, <code>null</code> is returned.
     * <p>
     * For more information, see section 2.2.4 of RFC 3464.
     *  
     * @return the host name of the MTA from which the message was received, or <code>null</code>
     * if this information is not available
     */
	public String getReceivedFromMtaHostName() {
		try {
			String value = HeaderUtils.getOptionalUniqueHeader(headers, "Received-From-MTA");
			return value == null ? null : HeaderUtils.parseMtaName(value);
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
		String value;
		try {
			value = HeaderUtils.getOptionalUniqueHeader(headers, "Arrival-Date");
		}
		catch (MessagingException ex) {
			return null;
		}
		if (value == null) {
			return null;
		} else {
			try {
				return mailDateFormat.parse(value);
			}
			catch (java.text.ParseException ex) {
				return null;
			}
		}
	}
	
	public PerRecipientDeliveryStatus[] getPerRecipientParts() {
		Collection<PerRecipientDeliveryStatus> result = recip.values();
		return result.toArray(new PerRecipientDeliveryStatus[result.size()]);
	}
	
	public PerRecipientDeliveryStatus getRecipientDeliveryStatus(InternetAddress address) { return recip.get(address); }
    
	public Map<Address,Address> getAliases() {
		Map<Address,Address> result = new HashMap<Address,Address>();
		for (PerRecipientDeliveryStatus rdsn : recip.values()) {
			Address orgRecipient = rdsn.getOriginalRecipient();
			Address finalRecipient = rdsn.getFinalRecipient();
			if (orgRecipient != null && !orgRecipient.equals(finalRecipient)) {
				result.put(orgRecipient, finalRecipient);
			}
		}
		return result;
	}
}
