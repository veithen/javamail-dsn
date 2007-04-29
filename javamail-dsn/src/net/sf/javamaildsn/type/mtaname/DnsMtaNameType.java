package net.sf.javamaildsn.type.mtaname;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.MessagingException;

import net.sf.javamaildsn.DnsMtaName;
import net.sf.javamaildsn.type.FieldType;

/**
 * @author Andreas Veithen
 */
public class DnsMtaNameType implements FieldType<DnsMtaName> {
	private final static Pattern valuePattern = Pattern.compile("([^\\s]*)(\\s\\((.*)\\))?");
	private final static Pattern domainLiteralPattern = Pattern.compile("\\[(\\d{1,3}(\\.\\d{1,3}){3})\\]");
	private final static Pattern addressPattern = Pattern.compile("\\d{1,3}(\\.\\d{1,3}){3}");
	
	public DnsMtaName parse(String type, String value) throws MessagingException {
		Matcher matcher = valuePattern.matcher(value);
		if (!matcher.matches()) {
			throw new MessagingException("Invalid dns MTA name format in '" + value + "'");
		}
		String domainName;
		String address;
		String mtaName = matcher.group(1);
		String comment = matcher.group(3);
		matcher = domainLiteralPattern.matcher(mtaName);
		if (matcher.matches()) {
			domainName = null;
			address = matcher.group(1);
		} else {
			domainName = mtaName;
			if (comment != null && addressPattern.matcher(comment).matches()) {
				address = comment;
			} else {
				address = null;
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
