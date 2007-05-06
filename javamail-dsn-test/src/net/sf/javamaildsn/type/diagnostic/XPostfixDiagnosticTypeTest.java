package net.sf.javamaildsn.type.diagnostic;

import javax.mail.MessagingException;
import javax.mail.internet.MimeUtility;

import junit.framework.TestCase;
import net.sf.javamaildsn.Diagnostic;
import net.sf.javamaildsn.MailSystemStatus;
import net.sf.javamaildsn.SMTPReply;

/**
 * @author Andreas Veithen
 */
public class XPostfixDiagnosticTypeTest extends TestCase {
	private final XPostfixDiagnosticType type = new XPostfixDiagnosticType();
	
	public void testRelayDiagnostic() throws MessagingException {
		Diagnostic diagnostic = type.parse(null, "host mx.example.com[10.0.0.1] said: 550 Error: Message content rejected", null, null);
		assertNotNull(diagnostic);
		assertEquals(XPostfixRelayDiagnostic.class, diagnostic.getClass());
		XPostfixRelayDiagnostic relayDiagnostic = (XPostfixRelayDiagnostic)diagnostic;
		assertEquals("mx.example.com", relayDiagnostic.getMta().getName());
		assertEquals("10.0.0.1", relayDiagnostic.getMta().getAddress());
		SMTPReply smtpReply = relayDiagnostic.getSmtpReply();
		assertEquals(550, smtpReply.getCode());
		String[] messages = smtpReply.getMessage().getLines();
		assertEquals(1, messages.length);
		assertEquals("Error: Message content rejected", messages[0]);
	}
	
	public void testFoldedLMTPRelayDiagnostic() throws MessagingException {
		Diagnostic diagnostic = type.parse(null, MimeUtility.unfold(
			"host /var/imap/socket/lmtp[/var/imap/socket/lmtp]\n" +
			"\tsaid: 554 5.6.0 Message contains NUL characters (in reply to end of DATA\n" +
			"\tcommand)"), null, null);
		assertNotNull(diagnostic);
		assertEquals(XPostfixRelayDiagnostic.class, diagnostic.getClass());
		XPostfixRelayDiagnostic relayDiagnostic = (XPostfixRelayDiagnostic)diagnostic;
		SMTPReply smtpReply = relayDiagnostic.getSmtpReply();
		assertEquals(554, smtpReply.getCode());
		String[] messages = smtpReply.getMessage().getLines();
		assertEquals(1, messages.length);
		assertEquals("Message contains NUL characters", messages[0]);
		assertEquals("end of DATA command", relayDiagnostic.getInReplyTo());
		assertEquals(new MailSystemStatus(5, 6, 0), smtpReply.getStatus());
	}
	
	public void testMultilineRelayDiagnostic() throws MessagingException {
		Diagnostic diagnostic = type.parse(null, MimeUtility.unfold(
			"host 127.0.0.1[127.0.0.1] said: 550-Mailbox\n" +
			"\tunknown.  Either there is no mailbox associated with this 550-name or you\n" +
			"\tdo not have authorization to see it. 550 5.1.1 User unknown (in reply to\n" +
			"\tRCPT TO command)"), null, null);
		assertNotNull(diagnostic);
		assertEquals(XPostfixRelayDiagnostic.class, diagnostic.getClass());
		XPostfixRelayDiagnostic relayDiagnostic = (XPostfixRelayDiagnostic)diagnostic;
		SMTPReply smtpReply = relayDiagnostic.getSmtpReply();
		assertEquals(550, smtpReply.getCode());
		String[] messages = smtpReply.getMessage().getLines();
		assertEquals(3, messages.length);
		assertEquals("Mailbox unknown.  Either there is no mailbox associated with this", messages[0]);
		assertEquals("name or you do not have authorization to see it.", messages[1]);
		assertEquals("User unknown", messages[2]);
		assertEquals("RCPT TO command", relayDiagnostic.getInReplyTo());
		assertEquals(new MailSystemStatus(5, 1, 1), smtpReply.getStatus());
	}
}
