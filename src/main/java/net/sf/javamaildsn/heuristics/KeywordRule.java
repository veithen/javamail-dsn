package net.sf.javamaildsn.heuristics;

import net.sf.javamaildsn.MailSystemStatus;

public class KeywordRule implements Rule {
	private final String keyword;
	private final BoundaryMatcher boundaryMatcher;
	
	public KeywordRule(String keyword, BoundaryMatcher boundaryMatcher) {
		this.keyword = RuleBasedHeuristics.normalizeMessage(keyword);
		this.boundaryMatcher = boundaryMatcher;
	}
	
	public boolean matches(int code, MailSystemStatus status, String normalizedMessage) {
		int endIndex = 0;
		while (true) {
			int startIndex = normalizedMessage.indexOf(keyword, endIndex);
			if (startIndex == -1) {
				return false;
			}
			endIndex = startIndex + keyword.length();
			if (boundaryMatcher.matches(normalizedMessage, startIndex, endIndex)) {
				return true;
			}
		}
	}
}
