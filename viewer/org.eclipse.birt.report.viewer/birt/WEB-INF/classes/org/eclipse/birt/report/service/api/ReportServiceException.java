/*************************************************************************************
 * Copyright (c) 2004 Actuate Corporation and others.
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 *     Actuate Corporation - Initial implementation.
 ************************************************************************************/

package org.eclipse.birt.report.service.api;

/**
 * Exception thrown by the report service
 * 
 */
public class ReportServiceException extends Exception {
	private static final long serialVersionUID = 1L;

	public ReportServiceException(String message) {
		super(message);
	}

	public ReportServiceException(String message, Throwable e) {
		super(message, e);
	}
}
