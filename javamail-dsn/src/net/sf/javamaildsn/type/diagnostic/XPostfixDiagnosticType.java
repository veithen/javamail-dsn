package net.sf.javamaildsn.type.diagnostic;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.MessagingException;

import net.sf.javamaildsn.XPostfixDiagnostic;
import net.sf.javamaildsn.XPostfixLocalDiagnostic;
import net.sf.javamaildsn.XPostfixRelayDiagnostic;
import net.sf.javamaildsn.type.FieldType;

/**
 * @author Andreas Veithen
 */
public class XPostfixDiagnosticType implements FieldType<XPostfixDiagnostic> {
	private final static SMTPDiagnosticType smtpDiagnosticType = new SMTPDiagnosticType(); // TODO: this is ugly
	private final static Pattern relayPattern = Pattern.compile("host (.+)\\[(.+)\\] said: (\\d{3}[ -].*?)( \\(in reply to (.*)\\))?");
	
	public XPostfixDiagnostic parse(String type, String value) throws MessagingException {
		Matcher matcher = relayPattern.matcher(value);
		if (matcher.matches()) {
			return new XPostfixRelayDiagnostic(matcher.group(1), matcher.group(2), smtpDiagnosticType.parse(null, matcher.group(3)), matcher.group(5));
		} else {
			return new XPostfixLocalDiagnostic(value);
		}
	}
}
