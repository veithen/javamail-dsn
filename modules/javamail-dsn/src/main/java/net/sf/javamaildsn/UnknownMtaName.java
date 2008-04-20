package net.sf.javamaildsn;

import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * @author Andreas Veithen
 */
public class UnknownMtaName implements MtaName {
	private final String type;
	private final String name;

	public UnknownMtaName(String type, String name) {
		this.type = type;
		this.name = name;
	}

	@Override
	public String toString() {
		return name;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj != null && obj instanceof UnknownMtaName) {
			UnknownMtaName cobj = (UnknownMtaName)obj;
			return type.equals(cobj.type) && name.equals(cobj.name);
		} else {
			return false;
		}
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(type).append(name).toHashCode();
	}
}
