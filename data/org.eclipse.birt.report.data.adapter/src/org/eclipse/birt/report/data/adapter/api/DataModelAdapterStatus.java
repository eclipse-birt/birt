/*******************************************************************************
 * Copyright (c) 2021 Contributors to the Eclipse Foundation
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 *   See git history
 *******************************************************************************/
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
