package net.sf.javamaildsn.heuristics;

import net.sf.javamaildsn.MailSystemStatus;

public interface Rule {
	public boolean matches(int code, MailSystemStatus status, String normalizedMessage);
}
