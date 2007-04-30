package net.sf.javamaildsn;

import java.io.InputStream;
import javax.mail.Address;
import javax.mail.MessagingException;
import javax.mail.internet.InternetHeaders;
import javax.mail.internet.ParseException;

import net.sf.javamaildsn.Diagnostic;
import net.sf.javamaildsn.HeaderUtils;
import net.sf.javamaildsn.MailSystemStatus;

/**
 * @author Andreas Veithen
 */
public class PerRecipientDeliveryStatus {
	public enum Action {
		/**
		 * Indicates that the message could not be delivered to the
		 * recipient. The Reporting MTA has abandoned any attempts
         * to deliver the message to this recipient. No further
         * notifications should be expected.
		 */
		FAILED,
		
		/**
		 * Indicates that the Reporting MTA has so far been unable
		 * to deliver or relay the message, but it will continue to
		 * attempt to do so. Additional notification messages may
		 * be issued as the message is further delayed or
		 * successfully delivered, or if delivery attempts are later
		 * abandoned.
		 */
		DELAYED,
		
		/**
		 * Indicates that the message was successfully delivered to
		 * the recipient address specified by the sender, which
		 * includes "delivery" to a mailing list exploder. It does
		 * not indicate that the message has been read. This is a
		 * terminal state and no further DSN for the recipient
		 * should be expected.
		 */
		DELIVERED,
		
		/**
		 * Indicates that the message has been relayed or gatewayed
		 * into an environment that does not accept responsibility
		 * for generating DSNs upon successful delivery. This
		 * action-value is only used if the sender has requested
		 * notification of successful delivery for the recipient.
		 */
		RELAYED,
		
		/**
		 * Indicates that the message has been successfully delivered
		 * to the recipient address as specified by the sender, and
		 * forwarded by the reporting MTA beyond that destination
		 * to multiple additional recipient addresses.
		 * <code>EXPANDED</code> differs from <code>DELIVERED</code> in
		 * that <code>EXPANDED</code> is not a terminal state. Further
         * <code>FAILED</code> and/or <code>DELAYED</code> notifications
         * may be provided.
		 */
		EXPANDED
	};
	
	private final InternetHeaders headers;
	
	public Action getAction() throws MessagingException {
		String value = HeaderUtils.getRequiredUniqueHeader(headers, "Action");
		try {
			return Action.valueOf(value.toUpperCase());
		}
		catch (IllegalArgumentException ex) {
			throw new ParseException("Unexpected action '" + value + "'");
		}
	}
	
	public MailSystemStatus getStatus() throws MessagingException {
		return new MailSystemStatus(HeaderUtils.stripComment(HeaderUtils.getRequiredUniqueHeader(headers, "Status")));
	}
	
	// TODO: rename to getDiagnosticCode
	public Diagnostic getDiagnostic() throws MessagingException {
		String value = HeaderUtils.getOptionalUniqueHeader(headers, "Diagnostic-Code");
		if (value == null) {
			return null;
		} else {
			return HeaderUtils.parseDiagnostic(value);
		}
	}
	
	public MtaName getRemoteMta() throws MessagingException {
		String value = HeaderUtils.getOptionalUniqueHeader(headers, "Remote-MTA");
		return value == null ? null : HeaderUtils.parseMtaName(value);
	}
	
	
	private Address finalRecipient;
	private Address originalRecipient;
	
	public PerRecipientDeliveryStatus(InputStream is) throws MessagingException {
		headers = new InternetHeaders(is);
		finalRecipient = HeaderUtils.parseAddress(HeaderUtils.getRequiredUniqueHeader(headers, "Final-Recipient"));
		{
			String s = HeaderUtils.getOptionalUniqueHeader(headers, "Original-Recipient");
			originalRecipient = s == null ? null : HeaderUtils.parseAddress(s);
		}
	}
	
	public Address getFinalRecipient() { return finalRecipient; }
	public Address getOriginalRecipient() { return originalRecipient; }
}
