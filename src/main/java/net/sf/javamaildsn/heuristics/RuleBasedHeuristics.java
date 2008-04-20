package net.sf.javamaildsn.heuristics;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.sf.javamaildsn.Diagnostic;
import net.sf.javamaildsn.MailSystemStatus;

public class RuleBasedHeuristics implements Heuristics {
	private static RuleBasedHeuristics defaultInstance;
	
	private final Map<Reason,List<Rule>> ruleSets = new HashMap<Reason,List<Rule>>();
	
	public static synchronized RuleBasedHeuristics getDefaultInstance() throws RuleBasedHeuristicsFactoryException {
		if (defaultInstance == null) {
			RuleBasedHeuristicsFactory factory = new RuleBasedHeuristicsFactory();
			BoundaryMatcher boundaryMatcher = new BoundaryMatcher(new char[] { ' ', '.', ':', '(', ')', '"', '/' });
			factory.addRuleParser("keywords", new KeywordRuleParser(boundaryMatcher));
			factory.addRuleParser("pattern", new PatternRuleParser(boundaryMatcher));
			factory.addRuleParser("status", new StatusRuleParser());
			defaultInstance = factory.create("net.sf.javamaildsn.heuristics.def");
		}
		return defaultInstance;
	}
	
	
	public void addRule(Reason reason, Rule rule) {
		List<Rule> ruleList = ruleSets.get(reason);
		if (ruleList == null) {
			ruleList = new LinkedList<Rule>();
			ruleSets.put(reason, ruleList);
		}
		ruleList.add(rule);
	}
	
	public static String normalizeMessage(String message) {
		return message.replaceAll("\\s+", " ").toLowerCase();
	}
	
	public Reason getReason(Diagnostic diagnostic) {
		Reason reason = null;
		int code = diagnostic.getCode();
		MailSystemStatus status = diagnostic.getStatus();
		String message = normalizeMessage(diagnostic.getMessage().getUnfolded());
		for (Map.Entry<Reason,List<Rule>> entry : ruleSets.entrySet()) {
			Reason heuristicReason = entry.getKey();
			for (Rule rule : entry.getValue()) {
				if (rule.matches(code, status, message)) {
					if (reason != null && reason != heuristicReason) {
						return null;
					} else {
						reason = heuristicReason;
						break; // Skip evaluating rules for the current reason and go to next reason
					}
				}
			}
		}
		return reason;
	}
}
