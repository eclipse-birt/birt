/*
 * Java(TM) OLAP Interface
 */

package javax.olap;

public class OLAPException extends org.eclipse.birt.data.engine.core.DataException {

	private static final long serialVersionUID = 1L;

	public OLAPException(String reason) {
		super(reason);
	}

	public OLAPException(String reason, String OLAPState) {
		super(reason);
	}

	public OLAPException(String reason, Throwable cause) {
		super(reason, cause);
	}

	public OLAPException(String reason, String OLAPState, int vendorCode) {
		super(reason);
	}

	public String getOLAPState() {
		return "OLAPException";
	}

	public void setNextException(OLAPException exception) {
	}
}
