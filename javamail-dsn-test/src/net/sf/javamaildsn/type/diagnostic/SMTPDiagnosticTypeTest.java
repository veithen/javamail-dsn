package net.sf.javamaildsn.type.diagnostic;

import junit.framework.TestCase;
import net.sf.javamaildsn.MailSystemStatus;
import net.sf.javamaildsn.SMTPDiagnostic;

public class SMTPDiagnosticTypeTest extends TestCase {
	private final SMTPDiagnosticType type = new SMTPDiagnosticType();
	
	public void testStatusPrefix() throws Exception {
		SMTPDiagnostic diag = type.parse(null, "550 5.1.1 User unknown");
		assertEquals(550, diag.getCode());
		assertEquals("User unknown", diag.getMessage());
		assertEquals(new MailSystemStatus(5, 1, 1), diag.getStatus());
	}
	
	public void testStatusComment() throws Exception {
		SMTPDiagnostic diag = type.parse(null, "552 we don't accept email with executable content (#5.3.4)");
		assertEquals(552, diag.getCode());
		assertEquals("we don't accept email with executable content", diag.getMessage());
		assertEquals(new MailSystemStatus(5, 3, 4), diag.getStatus());
	}
}
