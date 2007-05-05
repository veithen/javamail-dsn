package net.sf.javamaildsn.heuristics;

import net.sf.javamaildsn.MailSystemStatus;

public class KeywordRule implements Rule {
	private final String keyword;
	private final BoundaryMatcher boundaryMatcher;
	
	public KeywordRule(String keyword, BoundaryMatcher boundaryMatcher) {
		this.keyword = RuleBasedHeuristics.normalizeMessage(keyword);
		this.boundaryMatcher = boundaryMatcher;
	}
	
	// TODO: if boundaries do not match then continue the search... (create test case for this)
	public boolean matches(int code, MailSystemStatus status, String normalizedMessage) {
		int startIndex = normalizedMessage.indexOf(keyword);
		int endIndex = startIndex + keyword.length();
		return startIndex != -1 && boundaryMatcher.matches(normalizedMessage, startIndex, endIndex);
	}
}
