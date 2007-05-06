package net.sf.javamaildsn;

import java.util.Enumeration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.Address;
import javax.mail.MessagingException;
import javax.mail.internet.InternetHeaders;
import javax.mail.internet.ParseException;

import net.sf.javamaildsn.type.TypedFieldParser;
import net.sf.javamaildsn.type.address.RFC822AddressType;
import net.sf.javamaildsn.type.address.UnknownAddressType;
import net.sf.javamaildsn.type.diagnostic.SMTPDiagnosticType;
import net.sf.javamaildsn.type.diagnostic.UnknownDiagnosticType;
import net.sf.javamaildsn.type.diagnostic.XPostfixDiagnosticType;
import net.sf.javamaildsn.type.mtaname.DnsMtaNameType;
import net.sf.javamaildsn.type.mtaname.UnknownMtaNameType;

/**
 * @author Andreas Veithen
 */
// TODO: Section 2.1.1 of RFC 3464 says that "Text that appears in parentheses is considered a comment and not part of the contents of that notification field."
public class HeaderUtils {
	private static final TypedFieldParser<MtaName> mtaNameParser = new TypedFieldParser<MtaName>();
	private static final TypedFieldParser<Address> addressParser = new TypedFieldParser<Address>();
	private static final TypedFieldParser<Diagnostic> diagnosticParser = new TypedFieldParser<Diagnostic>();
	
	static {
		mtaNameParser.addType("dns", new DnsMtaNameType());
		mtaNameParser.setDefaultType(new UnknownMtaNameType());
		addressParser.addType("rfc822", new RFC822AddressType());
		addressParser.setDefaultType(new UnknownAddressType());
		diagnosticParser.addType("smtp", new SMTPDiagnosticType());
		diagnosticParser.addType("x-postfix", new XPostfixDiagnosticType());
		diagnosticParser.setDefaultType(new UnknownDiagnosticType());
	}
	
	/**
	 * Get the value of a header with multiplicity 0..1, i.e. that is unique but optional.
	 * 
	 * @param headers an <code>InternetHeaders</code> object
	 * @param name the name of the header
	 * @return the unique value of the header or <code>null</code> if a header with the specified name doesn't exist
	 * @throws ParseException if multiple values were found
	 */
	public static String getOptionalUniqueHeader(InternetHeaders headers, String name) throws ParseException {
		String[] result = headers.getHeader(name);
		if (result == null || result.length == 0) {
			return null;
		} else if (result.length == 1) {
			return result[0];
		} else {
			throw new ParseException("Did not expect multiple headers '" + name + "'");
		}
	}
	
	/**
	 * Get the value of a header field with multiplicity 1, i.e. that is required and unique.
	 * 
	 * @param headers an <code>InternetHeaders</code> object
	 * @param name the name of the header
	 * @return the unique value of the header
	 * @throws ParseException if no header with the specified name exists or if more than one value was found
	 */
	public static String getRequiredUniqueHeader(InternetHeaders headers, String name) throws ParseException {
		String[] result = headers.getHeader(name);
		if (result == null || result.length != 1) {
			throw new ParseException("Required one and only one header '" + name + "'");
		} else {
			return result[0];
		}
	}
	
	/**
	 * Parse the value of an MTA name header as defined in section 9.3 of RFC 3461 and referred to in
	 * section 2.1.2 of RFC 3464 and
	 * extract the host name.
	 * <p>
	 * Note: Currently the only registered MTA name type is "dns" (see
	 * http://www.iana.org/assignments/dsn-types).
	 * 
	 * @param value the value of the MTA name header
	 * @return the host name of the MTA
	 * @throws ParseException if the format of the value is incorrect
	 * @throws MessagingException if the MTA name type is not "dns"
	 */
	public static MtaName parseMtaName(String value, DeliveryStatus ds, PerRecipientDeliveryStatus rds) throws MessagingException {
		return mtaNameParser.parse(value, ds, rds);
	}
	
	/**
	 * Parse the value of an address header as defined in section 2.1.2 of RFC 3464.
	 * <p>
	 * Currently the only registered address type is "rfc822" (see
	 * http://www.iana.org/assignments/dsn-types). For this type, an
	 * <code>InternetAddress</code> object will be returned.
	 * 
	 * @param value the value of the address header 
	 * @return an <code>Address</code> object representing the address
	 * @throws ParseException if the format of the value is incorrect
	 * @throws MessagingException if the address type is unrecognized
	 */
	public static Address parseAddress(String value, DeliveryStatus ds, PerRecipientDeliveryStatus rds) throws MessagingException {
		return addressParser.parse(value, ds, rds);
	}
	
	// TODO: this is probably used only once; so move this
	public static Diagnostic parseDiagnostic(String value, DeliveryStatus ds, PerRecipientDeliveryStatus rds) throws MessagingException {
		return diagnosticParser.parse(value, ds, rds);
	}
	
	private final static Pattern commentPattern = Pattern.compile("(.*)\\s\\(.*\\)");
	
	public static String stripComment(String value) {
		Matcher matcher = commentPattern.matcher(value);
		return matcher.matches() ? matcher.group(1) : value;
	}
	
	@SuppressWarnings("unchecked")
	public static String dumpHeaders(InternetHeaders headers) {
		StringBuilder buffer = new StringBuilder();
		Enumeration<String> e = headers.getAllHeaderLines();
		while (e.hasMoreElements()) {
			buffer.append(e.nextElement());
			buffer.append('\n');
		}
		return buffer.toString();
	}
}
