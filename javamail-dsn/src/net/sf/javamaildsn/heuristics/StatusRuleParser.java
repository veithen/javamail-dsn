package net.sf.javamaildsn.heuristics;

import javax.mail.internet.ParseException;

import net.sf.javamaildsn.MailSystemStatus;

public class StatusRuleParser implements RuleParser {
	public Rule parse(String s) throws RuleBasedHeuristicsFactoryException {
		try {
			return new StatusRule(new MailSystemStatus(s));
		}
		catch (ParseException ex) {
			throw new RuleBasedHeuristicsFactoryException(ex);
		}
	}
}
