package net.sf.javamaildsn.heuristics;

public class KeywordRuleParser implements RuleParser {
	private final BoundaryMatcher boundaryMatcher;
	
	public KeywordRuleParser(BoundaryMatcher boundaryMatcher) {
		this.boundaryMatcher = boundaryMatcher;
	}

	public Rule parse(String s) throws RuleBasedHeuristicsFactoryException {
		return new KeywordRule(s, boundaryMatcher);
	}
}
