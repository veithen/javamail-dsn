package net.sf.javamaildsn;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.internet.MimeUtility;

public class XPostfixDiagnosticParser {
	private final static Pattern relayPattern = Pattern.compile("host (.+)\\[(.+)\\] said: (\\d{3}[ -].*?)( \\(in reply to (.*)\\))?");
	
	public static Diagnostic parse(String value) {
		Matcher matcher = relayPattern.matcher(MimeUtility.unfold(value));
		if (matcher.matches()) {
			return new XPostfixRelayDiagnostic(matcher.group(1), matcher.group(2), matcher.group(3), matcher.group(5));
		} else {
			return null;
		}
	}
}
