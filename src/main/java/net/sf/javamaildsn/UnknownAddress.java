package net.sf.javamaildsn;

import javax.mail.Address;

import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * {@link Address} implementation for unknown address types.
 * 
 * @author Andreas Veithen
 */
public class UnknownAddress extends Address {
	private static final long serialVersionUID = -7087873424928532764L;
	
	private final String type;
	private final String address;

	public UnknownAddress(String type, String address) {
		this.type = type;
		this.address = address;
	}

	@Override
	public String getType() {
		return type;
	}

	@Override
	public String toString() {
		return address;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj != null && obj instanceof UnknownAddress) {
			UnknownAddress cobj = (UnknownAddress)obj;
			return type.equals(cobj.type) && address.equals(cobj.address);
		} else {
			return false;
		}
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(type).append(address).toHashCode();
	}
}
