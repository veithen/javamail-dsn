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
	
	public void testMultilineRelayDiagnostic() {
		Diagnostic diagnostic = XPostfixDiagnosticParser.parse(
			"host 127.0.0.1[127.0.0.1] said: 550-Mailbox\n" +
			"\tunknown.  Either there is no mailbox associated with this 550-name or you\n" +
			"\tdo not have authorization to see it. 550 5.1.1 User unknown (in reply to\n" +
			"\tRCPT TO command)");
		assertNotNull(diagnostic);
		assertEquals(XPostfixRelayDiagnostic.class, diagnostic.getClass());
		XPostfixRelayDiagnostic relayDiagnostic = (XPostfixRelayDiagnostic)diagnostic;
		SMTPDiagnostic smtpDiagnostic = relayDiagnostic.getSmtpDiagnostic();
		assertEquals(550, smtpDiagnostic.getCode());
		String[] messages = smtpDiagnostic.getMessages();
		assertEquals(3, messages.length);
		assertEquals("Mailbox unknown.  Either there is no mailbox associated with this", messages[0]);
		assertEquals("name or you do not have authorization to see it.", messages[1]);
		assertEquals("5.1.1 User unknown", messages[2]);
		assertEquals("end of DATA command", relayDiagnostic.getInReplyTo());
	}
}
