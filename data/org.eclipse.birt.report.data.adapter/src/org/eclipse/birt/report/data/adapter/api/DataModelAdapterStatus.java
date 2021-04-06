package org.eclipse.birt.report.data.adapter.api;

public class DataModelAdapterStatus {
	public static enum Status {
		SUCCESS, FAIL
	}

	private Status status;
	private String message;

	public DataModelAdapterStatus(Status status, String message) {
		this.status = status;
		this.message = message;
	}

	public Status getStatus() {
		return this.status;
	}

	public String getMessage() {
		return this.message;
	}

}
