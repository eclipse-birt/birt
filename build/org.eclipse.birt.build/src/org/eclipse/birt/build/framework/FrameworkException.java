package org.eclipse.birt.build.framework;

public class FrameworkException extends Exception {

	private static final long serialVersionUID = -5458164264848066689L;

	public FrameworkException(String message) {
		super(message);
	}

	public FrameworkException(String message, Exception cause) {
		super(message, cause);
	}

	public FrameworkException(Exception cause) {
		super(cause);
	}

}
