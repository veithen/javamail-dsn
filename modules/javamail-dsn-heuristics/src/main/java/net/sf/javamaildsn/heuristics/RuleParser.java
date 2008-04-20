package net.sf.javamaildsn.heuristics;

public interface RuleParser {
	Rule parse(String s) throws RuleBasedHeuristicsFactoryException;
}
