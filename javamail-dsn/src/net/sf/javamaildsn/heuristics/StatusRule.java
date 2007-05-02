package net.sf.javamaildsn.heuristics;

import net.sf.javamaildsn.MailSystemStatus;

public class StatusRule implements Rule {
	private final MailSystemStatus status;
	
	public StatusRule(MailSystemStatus status) {
		this.status = status;
	}

	public boolean matches(int code, MailSystemStatus status, String normalizedMessage) {
		return this.status.equals(status);
	}
}
