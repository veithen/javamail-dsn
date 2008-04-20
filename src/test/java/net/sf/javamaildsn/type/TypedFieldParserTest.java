package net.sf.javamaildsn.type;

import javax.mail.Address;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;

import junit.framework.TestCase;
import net.sf.javamaildsn.DnsMtaName;
import net.sf.javamaildsn.MtaName;
import net.sf.javamaildsn.UnknownAddress;

public class TypedFieldParserTest extends TestCase {
    public void testAddress() throws MessagingException {
        TypedFieldParser<Address> parser = TypedFieldParser.getInstance(Address.class);
        Address address = parser.parse("rfc822; test@example.com", null, null);
        assertEquals(InternetAddress.class, address.getClass());
        address = parser.parse("x-dummy; dummy-address", null, null);
        assertEquals(UnknownAddress.class, address.getClass());
    }
    
    public void testMtaName() throws MessagingException {
        TypedFieldParser<MtaName> parser = TypedFieldParser.getInstance(MtaName.class);
        MtaName mtaName = parser.parse("dns; mx.example.com", null, null);
        assertEquals(DnsMtaName.class, mtaName.getClass());
        assertEquals("mx.example.com", ((DnsMtaName)mtaName).getDomainName());
    }
}
