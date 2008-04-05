package net.sf.javamaildsn.heuristics;

import net.sf.javamaildsn.Diagnostic;

public interface Heuristics {
	Reason getReason(Diagnostic diagnostic);
}
