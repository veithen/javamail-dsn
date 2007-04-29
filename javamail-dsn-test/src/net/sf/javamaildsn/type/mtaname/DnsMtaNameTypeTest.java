package net.sf.javamaildsn.type.mtaname;

import java.net.InetAddress;

import junit.framework.TestCase;
import net.sf.javamaildsn.DnsMtaName;

/**
 * @author Andreas Veithen
 */
public class DnsMtaNameTypeTest extends TestCase {
	private final DnsMtaNameType type = new DnsMtaNameType();
	
	public void testDomainName() throws Exception {
		DnsMtaName mtaName = type.parse("dns", "mx.example.com");
		assertEquals("mx.example.com", mtaName.getDomainName());
		assertNull(mtaName.getAddress());
	}

	public void testDomainLiteral() throws Exception {
		DnsMtaName mtaName = type.parse("dns", "[192.168.2.45]");
		assertNull(mtaName.getDomainName());
		assertEquals(InetAddress.getByName("192.168.2.45"), mtaName.getAddress());
	}
	
	public void testDomainNameWithAddressComment() throws Exception {
		DnsMtaName mtaName = type.parse("dns", "mx.example.com (192.168.2.45)");
		assertEquals("mx.example.com", mtaName.getDomainName());
		assertEquals(InetAddress.getByName("192.168.2.45"), mtaName.getAddress());
	}
	
	public void testDomainNameWithOtherComment() throws Exception {
		DnsMtaName mtaName = type.parse("dns", "mx.example.com (some other comment)");
		assertEquals("mx.example.com", mtaName.getDomainName());
		assertEquals(null, mtaName.getAddress());
	}
	
	public void testDomainNameWithDomainLiteral() throws Exception {
		DnsMtaName mtaName = type.parse("dns", "mx.example.com [192.168.2.45]");
		assertEquals("mx.example.com", mtaName.getDomainName());
		assertEquals(InetAddress.getByName("192.168.2.45"), mtaName.getAddress());
	}
}
