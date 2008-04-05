package net.sf.javamaildsn.heuristics;

import java.util.regex.Pattern;

public class PatternRuleParser implements RuleParser {
	private final BoundaryMatcher boundaryMatcher;
	
	public PatternRuleParser(BoundaryMatcher boundaryMatcher) {
		this.boundaryMatcher = boundaryMatcher;
	}
	
	public Rule parse(String s) throws RuleBasedHeuristicsFactoryException {
		return new PatternRule(Pattern.compile(s), boundaryMatcher);
	}
}
