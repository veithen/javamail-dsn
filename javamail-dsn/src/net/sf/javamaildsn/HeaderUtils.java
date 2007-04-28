package net.sf.javamaildsn;

import javax.mail.Address;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.InternetHeaders;
import javax.mail.internet.ParseException;

import net.sf.javamaildsn.Diagnostic;
import net.sf.javamaildsn.SMTPDiagnostic;

// TODO: Section 2.1.1 of RFC 3464 says that "Text that appears in parentheses is considered a comment and not part of the contents of that notification field."
public class HeaderUtils {
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
	
	private static String[] parseTyped(String value) {
		String[] parts = value.split(" *; *");
		return parts.length == 2 ? parts : null;
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
	public static String parseMtaName(String value) throws MessagingException {
		String[] parts = parseTyped(value);
		if (parts == null) {
			throw new ParseException("Invalid MTA format in '" + value + "'");
		}
		if (parts[0].equalsIgnoreCase("dns")) {
			return parts[1];
		} else {
			throw new MessagingException("Unrecognized MTA name type '" + parts[0] + "'");
		}
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
	public static Address parseAddress(String value) throws MessagingException {
		String[] parts = parseTyped(value);
		if (parts == null) {
			throw new ParseException("Invalid address format in '" + value + "'");
		}
		if (parts[0].equalsIgnoreCase("RFC822")) {
			return new InternetAddress(parts[1].trim());
		} else {
			throw new MessagingException("Unrecognized address type '" + parts[0] + "'");
		}
	}
	
	// TODO: this is probably used only once; so move this
	public static Diagnostic parseDiagnostic(String value) throws MessagingException {
		String[] parts = parseTyped(value);
		if (parts == null) {
			throw new MessagingException("Invalid diagnostic format");
		}
		if (parts[0].equalsIgnoreCase("SMTP")) {
			return new SMTPDiagnostic(parts[1]);
		} else if (parts[0].equalsIgnoreCase("X-POSTFIX")) {
			return XPostfixDiagnosticParser.parse(parts[1]);
		} else {
			return new UnknownDiagnostic(parts[1]);
		}
	}
}
