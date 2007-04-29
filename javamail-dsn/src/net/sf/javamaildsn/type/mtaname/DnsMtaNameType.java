package net.sf.javamaildsn.type.mtaname;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.MessagingException;

import net.sf.javamaildsn.DnsMtaName;
import net.sf.javamaildsn.type.FieldType;

/**
 * 
 * Section 9.3 of RFC 3461 specifies the syntax as follows:
 * <pre>
 * domain = real-domain / domain-literal
 * real-domain = sub-domain *("." sub-domain)
 * sub-domain = atom
 * domain-literal = "[" 1*3DIGIT 3("." 1*3DIGIT) "]"
 * </pre>
 * Section 2.2.4 of RFC 3464 also suggests using a network address as
 * comment. This is supported. Any other comments are discarded.
 * <p>
 * Some MTAs also use the following syntax:
 * <pre>
 * real-domain SP domain-literal
 * </pre>
 * Even if this is not in accordance with the RFCs, this syntax is supported.
 * 
 * @author Andreas Veithen
 */
public class DnsMtaNameType implements FieldType<DnsMtaName> {
	private final static Pattern atomPattern = Pattern.compile("[\\x21-\\x7F&&[^\\(\\)<>@,;:\\\\\"\\.\\[\\]]]+");
	private final static Pattern addressPattern = Pattern.compile("\\d{1,3}(?:\\.\\d{1,3}){3}");
	private final static Pattern domainNamePattern = Pattern.compile(atomPattern + "(?:\\." + atomPattern + ")*");
	private final static Pattern domainLiteralPattern = Pattern.compile("\\[(" + addressPattern + ")\\]");
	private final static Pattern valuePattern = Pattern.compile("(?:(" + domainNamePattern + ")|(?:" + domainLiteralPattern + "))(?:\\s\\((.*)\\))?");
	private final static Pattern altValuePattern = Pattern.compile("(" + domainNamePattern + ")\\s(?:" + domainLiteralPattern + ")");
	
	public DnsMtaName parse(String type, String value) throws MessagingException {
		String domainName;
		String address;
		Matcher matcher = valuePattern.matcher(value);
		if (matcher.matches()) {
			domainName = matcher.group(1);
			address = matcher.group(2);
			String comment = matcher.group(3);
			if (comment != null && address == null && addressPattern.matcher(comment).matches()) {
				address = comment;
			}
		} else {
			matcher = altValuePattern.matcher(value);
			if (matcher.matches()) {
				domainName = matcher.group(1);
				address = matcher.group(2);
			} else {
				throw new MessagingException("Invalid dns MTA name format in '" + value + "'");
			}
		}
		try {
			return new DnsMtaName(domainName, address == null ? null : InetAddress.getByName(address));
		}
		catch (UnknownHostException ex) {
			// We validated the address format, so we should never get here
			throw new Error(ex);
		}
	}
}
