/*************************************************************************************
 * Copyright (c) 2004 Actuate Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
