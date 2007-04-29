package net.sf.javamaildsn;

import java.net.InetAddress;

/**
 * @author Andreas Veithen
 */
public class DnsMtaName implements MtaName {
	private final String domainName;
	private final InetAddress address;
	
	public DnsMtaName(String domainName, InetAddress address) {
		this.domainName = domainName;
		this.address = address;
	}
	
	public String getDomainName() {
		return domainName;
	}

	public InetAddress getAddress() {
		return address;
	}
	
	// TODO: equals, hashCode, toString
}
