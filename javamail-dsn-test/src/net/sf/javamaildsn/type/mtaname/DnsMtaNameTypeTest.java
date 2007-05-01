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
		DnsMtaName mtaName = type.parse("dns", "mx.example.com", null, null);
		assertEquals("mx.example.com", mtaName.getDomainName());
		assertNull(mtaName.getAddress());
		assertEquals("mx.example.com", mtaName.toString());
	}

	public void testDomainLiteral() throws Exception {
		DnsMtaName mtaName = type.parse("dns", "[192.168.2.45]", null, null);
		assertNull(mtaName.getDomainName());
		assertEquals(InetAddress.getByName("192.168.2.45"), mtaName.getAddress());
		assertEquals("[192.168.2.45]", mtaName.toString());
	}
	
	public void testDomainNameWithAddressComment() throws Exception {
		DnsMtaName mtaName = type.parse("dns", "mx.example.com (192.168.2.45)", null, null);
		assertEquals("mx.example.com", mtaName.getDomainName());
		assertEquals(InetAddress.getByName("192.168.2.45"), mtaName.getAddress());
		assertEquals("mx.example.com (192.168.2.45)", mtaName.toString());
	}
	
	public void testDomainNameWithOtherComment() throws Exception {
		DnsMtaName mtaName = type.parse("dns", "mx.example.com (some other comment)", null, null);
		assertEquals("mx.example.com", mtaName.getDomainName());
		assertEquals(null, mtaName.getAddress());
		assertEquals("mx.example.com", mtaName.toString());
	}
	
	public void testDomainNameWithDomainLiteral() throws Exception {
		DnsMtaName mtaName = type.parse("dns", "mx.example.com [192.168.2.45]", null, null);
		assertEquals("mx.example.com", mtaName.getDomainName());
		assertEquals(InetAddress.getByName("192.168.2.45"), mtaName.getAddress());
		assertEquals("mx.example.com (192.168.2.45)", mtaName.toString());
	}
}
