package net.sf.javamaildsn;

/**
 * @author Andreas Veithen
 */
public class Cause {
	private final MtaName mta;
	private final int code;
	private final MailSystemStatus status;
	private final String message;
	
	public Cause(MtaName mta, int code, MailSystemStatus status, String message) {
		this.mta = mta;
		this.code = code;
		this.status = status;
		this.message = message;
	}

	public MtaName getMta() {
		return mta;
	}
	
	public int getCode() {
		return code;
	}
	
	public MailSystemStatus getStatus() {
		return status;
	}
	
	public String getMessage() {
		return message;
	}

	@Override
	public String toString() {
		StringBuilder buffer = new StringBuilder();
		buffer.append(mta);
		buffer.append(": ");
		buffer.append(message);
		if (status != null) {
			buffer.append(" (");
			buffer.append(status);
			buffer.append(")");
		}
		return buffer.toString();
	}
}
