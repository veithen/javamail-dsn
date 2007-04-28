package net.sf.javamaildsn;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;

import net.sf.javamaildsn.PerRecipientDeliveryStatus.Action;

import junit.framework.TestCase;

public class DeliveryStatusTest extends TestCase {
	public void testMultilineSmtpDiagnostic() throws MessagingException {
		DeliveryStatus ds = new DeliveryStatus(getClass().getResourceAsStream("/multiline-smtp-diagnostic"));
		PerRecipientDeliveryStatus[] rdsArray = ds.getPerRecipientParts();
		assertEquals(1, rdsArray.length);
		PerRecipientDeliveryStatus rds = rdsArray[0];
		assertEquals(new InternetAddress("michelle.konzack@freenet.de"), rds.getFinalRecipient());
		assertEquals(Action.FAILED, rds.getAction());
		assertEquals(new MailSystemStatus(5, 0, 0), rds.getStatus());
		Diagnostic diagnostic = rds.getDiagnostic();
		assertNotNull(diagnostic);
		assertEquals(SMTPDiagnostic.class, diagnostic.getClass());
		SMTPDiagnostic smtpDiagnostic = (SMTPDiagnostic)diagnostic;
		assertEquals(550, smtpDiagnostic.getCode());
		String[] messages = smtpDiagnostic.getMessages();
		assertEquals(3, messages.length);
		assertEquals("Verification failed for <root@tamay-dogan.net>", messages[0]);
		assertEquals("unrouteable mail domain \"tamay-dogan.net\"", messages[1]);
		assertEquals("verifying root@tamay-dogan.net failed", messages[2]);
	}
	
	public void testMultilineSmtpDiagnostic2() throws MessagingException {
		DeliveryStatus ds = new DeliveryStatus(getClass().getResourceAsStream("/multiline-smtp-diagnostic-2"));
		PerRecipientDeliveryStatus[] rdsArray = ds.getPerRecipientParts();
		assertEquals(1, rdsArray.length);
		PerRecipientDeliveryStatus rds = rdsArray[0];
		assertEquals(new InternetAddress("publicfolder+support.go6.support@hexago.com"), rds.getFinalRecipient());
		assertEquals(new InternetAddress("publicfolder+support.go6.support@hexago.com"), rds.getOriginalRecipient());
		assertEquals(Action.FAILED, rds.getAction());
		assertEquals(new MailSystemStatus(5, 1, 1), rds.getStatus());
		Diagnostic diagnostic = rds.getDiagnostic();
		assertNotNull(diagnostic);
		assertEquals(SMTPDiagnostic.class, diagnostic.getClass());
		SMTPDiagnostic smtpDiagnostic = (SMTPDiagnostic)diagnostic;
		assertEquals(550, smtpDiagnostic.getCode());
		String[] messages = smtpDiagnostic.getMessages();
		assertEquals(3, messages.length);
		assertEquals("Mailbox unknown. Either there is no mailbox associated with this", messages[0]);
		assertEquals("name or you do not have authorization to see it.", messages[1]);
		assertEquals("5.1.1 User unknown", messages[2]);
	}
	
	public void testXPostfixHostSaid() throws MessagingException {
		DeliveryStatus ds = new DeliveryStatus(getClass().getResourceAsStream("/x-postfix-host-said"));
		PerRecipientDeliveryStatus rds = ds.getRecipientDeliveryStatus(new InternetAddress("kde-devel-request@kde.org"));
		assertNotNull(rds);
		assertEquals(Action.FAILED, rds.getAction());
		assertEquals(new MailSystemStatus(5, 0, 0), rds.getStatus());
		Diagnostic diagnostic = rds.getDiagnostic();
		assertNotNull(diagnostic);
		assertEquals(XPostfixRelayDiagnostic.class, diagnostic.getClass());
		XPostfixRelayDiagnostic postfixDiagnostic = (XPostfixRelayDiagnostic)diagnostic;
		SMTPDiagnostic smtpDiagnostic = postfixDiagnostic.getSmtpDiagnostic();
		assertEquals(554, smtpDiagnostic.getCode());
		String[] messages = smtpDiagnostic.getMessages();
		assertEquals(1, messages.length);
		assertEquals("mail server permanently rejected message (#5.3.0)", messages[0]);
		assertEquals("end of DATA command", postfixDiagnostic.getInReplyTo());
	}
	
	public void testXPostfixMultilineLMTP() throws MessagingException {
		DeliveryStatus ds = new DeliveryStatus(getClass().getResourceAsStream("/x-postfix-host-said-multiline-lmtp"));
		PerRecipientDeliveryStatus[] rdsArray = ds.getPerRecipientParts();
		assertEquals(1, rdsArray.length);
		PerRecipientDeliveryStatus rds = rdsArray[0];
		assertEquals(new InternetAddress("admin@localhost.qtec.biz"), rds.getFinalRecipient());
		assertEquals(new InternetAddress("admin@mx1.qtec.biz"), rds.getOriginalRecipient());
		assertEquals(Action.FAILED, rds.getAction());
		assertEquals(new MailSystemStatus(5, 0, 0), rds.getStatus());
		Diagnostic diagnostic = rds.getDiagnostic();
		assertNotNull(diagnostic);
		assertEquals(XPostfixRelayDiagnostic.class, diagnostic.getClass());
		XPostfixRelayDiagnostic postfixDiagnostic = (XPostfixRelayDiagnostic)diagnostic;
		SMTPDiagnostic smtpDiagnostic = postfixDiagnostic.getSmtpDiagnostic();
		assertEquals(550, smtpDiagnostic.getCode());
		String[] messages = smtpDiagnostic.getMessages();
		assertEquals(3, messages.length);
		assertEquals("Mailbox unknown.  Either there is no mailbox associated with this", messages[0]);
		assertEquals("name or you do not have authorization to see it.", messages[1]);
		assertEquals("5.1.1 User unknown", messages[2]);
		assertEquals("end of DATA command", postfixDiagnostic.getInReplyTo());
	}
}
