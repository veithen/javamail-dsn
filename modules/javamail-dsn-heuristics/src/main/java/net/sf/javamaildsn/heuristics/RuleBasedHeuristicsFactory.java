package net.sf.javamaildsn.heuristics;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class RuleBasedHeuristicsFactory {
	private final Map<String,RuleParser> parsers = new HashMap<String,RuleParser>();
	
	public void addRuleParser(String suffix, RuleParser ruleParser) {
		parsers.put(suffix, ruleParser);
	}
	
	public RuleBasedHeuristics create(String packageName) throws RuleBasedHeuristicsFactoryException {
		RuleBasedHeuristics result = new RuleBasedHeuristics();
		String root = "/" + packageName.replaceAll("\\.", "/") + "/";
		try {
			boolean found = false;
			for (Reason reason : Reason.values()) {
				for (Map.Entry<String,RuleParser> entry : parsers.entrySet()) {
					InputStream is = RuleBasedHeuristicsFactory.class.getResourceAsStream(root + reason.toString().toLowerCase() + "." + entry.getKey());
					if (is != null) {
						BufferedReader in = new BufferedReader(new InputStreamReader(is));
						String line;
						while ((line = in.readLine()) != null) {
							result.addRule(reason, entry.getValue().parse(line));
						}
						in.close();
						found = true;
					}
				}
			}
			if (!found) {
				throw new RuleBasedHeuristicsFactoryException("No heuristics found");
			}
		}
		catch (IOException ex) {
			throw new RuleBasedHeuristicsFactoryException("Unexpected IOException", ex);
		}
		return result;
	}
}
