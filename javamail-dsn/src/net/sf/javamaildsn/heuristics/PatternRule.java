package net.sf.javamaildsn.heuristics;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sf.javamaildsn.MailSystemStatus;

public class PatternRule implements Rule {
	private final Pattern pattern;
	private final BoundaryMatcher boundaryMatcher;
	
	public PatternRule(Pattern pattern, BoundaryMatcher boundaryMatcher) {
		this.pattern = pattern;
		this.boundaryMatcher = boundaryMatcher;
	}

	public boolean matches(int code, MailSystemStatus status, String normalizedMessage) {
		Matcher matcher = pattern.matcher(normalizedMessage);
		while (matcher.find()) {
			if (boundaryMatcher.matches(normalizedMessage, matcher.start(), matcher.end())) {
				return true;
			}
		}
		return false;
	}
}
