package net.sf.javamaildsn.handler;

import java.util.Properties;

import javax.activation.MailcapCommandMap;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;

import net.sf.javamaildsn.MultipartReportDeliveryStatus;

import junit.framework.TestCase;

public class HandlerTest extends TestCase {
    /**
     * Test that the information in <tt>META-INF/mailcap</tt> is loaded correctly.
     */
    public void testMailcap() {
        MailcapCommandMap commandMap = new MailcapCommandMap();
        assertEquals(MultipartReportHandler.class, commandMap.createDataContentHandler("multipart/report").getClass());
        assertEquals(DeliveryStatusHandler.class, commandMap.createDataContentHandler("message/delivery-status").getClass());
        assertEquals(TextRFC822Handler.class, commandMap.createDataContentHandler("text/rfc822").getClass());
        assertEquals(TextRFC822Handler.class, commandMap.createDataContentHandler("text/rfc822-headers").getClass());
    }
    
    public void test1() throws Exception {
        Session session = Session.getDefaultInstance(new Properties());
        MimeMessage message = new MimeMessage(session, getClass().getResourceAsStream("/msg/msg1"));
        Object content = message.getContent();
        assertEquals(MultipartReportDeliveryStatus.class, content.getClass());
    }
}
