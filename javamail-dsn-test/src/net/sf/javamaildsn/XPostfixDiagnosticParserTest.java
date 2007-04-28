package net.sf.javamaildsn;

import junit.framework.TestCase;

public class XPostfixDiagnosticParserTest extends TestCase {
	public void testRelayDiagnostic() {
		Diagnostic diagnostic = XPostfixDiagnosticParser.parse("host mx.example.com[10.0.0.1] said: 550 Error: Message content rejected");
		assertNotNull(diagnostic);
		assertEquals(XPostfixRelayDiagnostic.class, diagnostic.getClass());
		XPostfixRelayDiagnostic relayDiagnostic = (XPostfixRelayDiagnostic)diagnostic;
		assertEquals("mx.example.com", relayDiagnostic.getHost());
		assertEquals("10.0.0.1", relayDiagnostic.getAltHost());
		SMTPDiagnostic smtpDiagnostic = relayDiagnostic.getSmtpDiagnostic();
		assertEquals(550, smtpDiagnostic.getCode());
		String[] messages = smtpDiagnostic.getMessages();
		assertEquals(1, messages.length);
		assertEquals("Error: Message content rejected", messages[0]);
	}
	
	public void testFoldedLMTPRelayDiagnostic() {
		Diagnostic diagnostic = XPostfixDiagnosticParser.parse(
			"host /var/imap/socket/lmtp[/var/imap/socket/lmtp]\n" +
			"\tsaid: 554 5.6.0 Message contains NUL characters (in reply to end of DATA\n" +
			"\tcommand)");
		assertNotNull(diagnostic);
		assertEquals(XPostfixRelayDiagnostic.class, diagnostic.getClass());
		XPostfixRelayDiagnostic relayDiagnostic = (XPostfixRelayDiagnostic)diagnostic;
		SMTPDiagnostic smtpDiagnostic = relayDiagnostic.getSmtpDiagnostic();
		assertEquals(554, smtpDiagnostic.getCode());
		String[] messages = smtpDiagnostic.getMessages();
		assertEquals(1, messages.length);
		assertEquals("5.6.0 Message contains NUL characters", messages[0]);
		assertEquals("end of DATA command", relayDiagnostic.getInReplyTo());
	}
}
