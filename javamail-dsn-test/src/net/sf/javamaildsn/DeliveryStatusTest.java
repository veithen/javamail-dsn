package net.sf.javamaildsn;

import java.net.InetAddress;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;

import junit.framework.TestCase;
import net.sf.javamaildsn.PerRecipientDeliveryStatus.Action;

/**
 * @author Andreas Veithen
 */
public class DeliveryStatusTest extends TestCase {
	public void testDnsDomainLiteral() throws Exception {
		DeliveryStatus ds = new DeliveryStatus(getClass().getResourceAsStream("/dns-domain-literal"));
		PerRecipientDeliveryStatus[] rdsArray = ds.getPerRecipientParts();
		assertEquals(1, rdsArray.length);
		PerRecipientDeliveryStatus rds = rdsArray[0];
		DnsMtaName mtaName = (DnsMtaName)rds.getRemoteMta();
		assertEquals(InetAddress.getByName("127.0.0.1"), mtaName.getAddress());
	}
	
	public void testMultilineSmtpDiagnostic() throws Exception {
		DeliveryStatus ds = new DeliveryStatus(getClass().getResourceAsStream("/multiline-smtp-diagnostic"));
		assertEquals("mail.tamay-dogan.net", ((DnsMtaName)ds.getReportingMta()).getDomainName());
		assertEquals("localhost", ((DnsMtaName)ds.getReceivedFromMta()).getDomainName());
		PerRecipientDeliveryStatus[] rdsArray = ds.getPerRecipientParts();
		assertEquals(1, rdsArray.length);
		PerRecipientDeliveryStatus rds = rdsArray[0];
		assertEquals(new InternetAddress("michelle.konzack@freenet.de"), rds.getFinalRecipient());
		assertEquals(Action.FAILED, rds.getAction());
		assertEquals(new MailSystemStatus(5, 0, 0), rds.getStatus());
		assertEquals("mx.freenet.de", ((DnsMtaName)rds.getRemoteMta()).getDomainName());
		assertEquals(InetAddress.getByName("194.97.55.148"), ((DnsMtaName)rds.getRemoteMta()).getAddress());
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
		assertEquals("ex.hexago.com", ((DnsMtaName)ds.getReportingMta()).getDomainName());
		PerRecipientDeliveryStatus[] rdsArray = ds.getPerRecipientParts();
		assertEquals(1, rdsArray.length);
		PerRecipientDeliveryStatus rds = rdsArray[0];
		assertEquals(new InternetAddress("publicfolder+support.go6.support@hexago.com"), rds.getFinalRecipient());
		assertEquals(new InternetAddress("publicfolder+support.go6.support@hexago.com"), rds.getOriginalRecipient());
		assertEquals(Action.FAILED, rds.getAction());
		assertEquals(new MailSystemStatus(5, 1, 1), rds.getStatus());
		assertEquals("ex.hexago.com", ((DnsMtaName)rds.getRemoteMta()).getDomainName());
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
	
	public void testReceivedFromMta() throws Exception {
		DeliveryStatus ds = new DeliveryStatus(getClass().getResourceAsStream("/received-from-mta"));
		DnsMtaName mtaName = (DnsMtaName)ds.getReceivedFromMta();
		assertEquals("inbound-mx1.atl.registeredsite.com", mtaName.getDomainName());
		assertEquals(InetAddress.getByName("64.224.219.89"), mtaName.getAddress());
	}
	
	public void testXPostfixHostSaid() throws MessagingException {
		DeliveryStatus ds = new DeliveryStatus(getClass().getResourceAsStream("/x-postfix-host-said"));
		assertEquals("office.kde.org", ((DnsMtaName)ds.getReportingMta()).getDomainName());
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
		assertEquals("RCPT TO command", postfixDiagnostic.getInReplyTo());
	}
}
