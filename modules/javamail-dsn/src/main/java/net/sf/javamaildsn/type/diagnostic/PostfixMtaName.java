package net.sf.javamaildsn.type.diagnostic;

import net.sf.javamaildsn.MtaName;

/**
 * MTA name as reported by Postfix.
 * 
 * @author Andreas Veithen
 */
public class PostfixMtaName implements MtaName {
	private final String name;
	private final String address;
	
	public PostfixMtaName(String name, String address) {
		this.name = name;
		this.address = address;
	}

	public String getName() {
		return name;
	}
	
	public String getAddress() {
		return address;
	}

	@Override
	public String toString() {
		return name + "[" + address + "]";
	}
}
