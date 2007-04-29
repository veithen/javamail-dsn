package net.sf.javamaildsn;

import java.net.InetAddress;

/**
 * @author Andreas Veithen
 */
public class DnsMtaName implements MtaName {
	private final String domainName;
	private final InetAddress address;
	
	public DnsMtaName(String domainName, InetAddress address) {
		assert domainName != null || address != null;
		this.domainName = domainName;
		this.address = address;
	}
	
	public String getDomainName() {
		return domainName;
	}

	public InetAddress getAddress() {
		return address;
	}
	
	// TODO: equals, hashCode

	@Override
	public String toString() {
		if (domainName != null && address != null) {
			return domainName + " (" + address.getHostAddress() + ")";
		} else if (domainName != null) {
			return domainName;
		} else {
			return "[" + address.getHostAddress() + "]";
		}
	}
}
