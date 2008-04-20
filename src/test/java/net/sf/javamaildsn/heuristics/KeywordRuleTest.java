package net.sf.javamaildsn.heuristics;

import junit.framework.TestCase;

public class KeywordRuleTest extends TestCase {
	private final BoundaryMatcher boundaryMatcher = new BoundaryMatcher(new char[] { ' ' });
	
	public void test1() {
		String normalizedMessage = RuleBasedHeuristics.normalizeMessage("xxx match xxx");
		assertTrue(new KeywordRule("match", boundaryMatcher).matches(-1, null, normalizedMessage));
	}

	public void test2() {
		String normalizedMessage = RuleBasedHeuristics.normalizeMessage("xxx yyy xxx");
		assertFalse(new KeywordRule("match", boundaryMatcher).matches(-1, null, normalizedMessage));
	}

	public void test3() {
		String normalizedMessage = RuleBasedHeuristics.normalizeMessage("xxx nomatch xxx");
		assertFalse(new KeywordRule("match", boundaryMatcher).matches(-1, null, normalizedMessage));
	}
	
	public void test4() {
		String normalizedMessage = RuleBasedHeuristics.normalizeMessage("xxx nomatch match xxx");
		assertTrue(new KeywordRule("match", boundaryMatcher).matches(-1, null, normalizedMessage));
	}
	
    public void test5() {
        String normalizedMessage = RuleBasedHeuristics.normalizeMessage("match xxx");
        assertTrue(new KeywordRule("match", boundaryMatcher).matches(-1, null, normalizedMessage));
    }
    
    public void test6() {
        String normalizedMessage = RuleBasedHeuristics.normalizeMessage("xxx match");
        assertTrue(new KeywordRule("match", boundaryMatcher).matches(-1, null, normalizedMessage));
    }
}
