package net.sf.javamaildsn;

import junit.framework.TestCase;

public class SMTPReplyTest extends TestCase {
	public void testStatusPrefix() throws Exception {
		SMTPReply reply = new SMTPReply("550 5.1.1 User unknown");
		assertEquals(550, reply.getCode());
		String[] messages = reply.getMessages();
		assertEquals(1, messages.length);
		assertEquals("User unknown", messages[0]);
		assertEquals(new MailSystemStatus(5, 1, 1), reply.getStatus());
	}
	
	public void testStatusComment() throws Exception {
		SMTPReply reply = new SMTPReply("552 we don't accept email with executable content (#5.3.4)");
		assertEquals(552, reply.getCode());
		String[] messages = reply.getMessages();
		assertEquals(1, messages.length);
		assertEquals("we don't accept email with executable content", messages[0]);
		assertEquals(new MailSystemStatus(5, 3, 4), reply.getStatus());
	}
}
