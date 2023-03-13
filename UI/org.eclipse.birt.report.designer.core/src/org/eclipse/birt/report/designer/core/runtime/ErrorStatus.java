/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.designer.core.runtime;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.Status;

/**
 * The multi-status to store the information about error.The class contains the
 * error message and detailed information such as the plug-in where the error
 * happens.
 */

public class ErrorStatus extends MultiStatus {

	/**
	 * Creates a new instance of Error Status with given reason.
	 *
	 * @param pluginId  the unique identifier of the relevant plug-in
	 * @param code      the plug-in-specific status code
	 * @param reason    the error reason
	 * @param exception a low-level exception, or <code>null</code> if not
	 *                  applicable
	 */
	public ErrorStatus(String pluginId, int code, String reason, Throwable exception) {
		super(pluginId, code, reason, exception);
	}

	/**
	 * Add a status with given message and severity
	 *
	 * @param message  the status message
	 * @param severity the status severity
	 */

	public void addStatus(String message, int severity) {
		merge(new Status(severity, getPlugin(), getCode(), message, null));
	}

	/**
	 * Add a warning status with given message
	 *
	 * @param message the status message
	 */
	public void addWarning(String message) {
		addStatus(message, IStatus.WARNING);
	}

	/**
	 * Add a error status with given message
	 *
	 * @param message the status message
	 */
	public void addError(String message) {
		addStatus(message, IStatus.ERROR);
	}

	/**
	 * Add an information status with given message
	 *
	 * @param message the status message
	 */
	public void addInformation(String message) {
		addStatus(message, IStatus.INFO);
	}

	/**
	 * Add cause of error.
	 *
	 * @param e
	 */
	public void addCause(Throwable e) {
		String message = e.getLocalizedMessage();
		if (message == null) {
			message = e.getClass().getName();
		}
		merge(new Status(IStatus.ERROR, getPlugin(), getCode(), message, e));
	}

	/**
	 * Returns the error code
	 *
	 * @return the error code
	 */
	public int getErrorCode() {
		return getCode();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.core.runtime.IStatus#getSeverity()
	 */
	@Override
	public int getSeverity() {
		if (getChildren().length == 0) {// Default value
			return IStatus.ERROR;
		}
		return super.getSeverity();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.core.runtime.Status#setException(java.lang.Throwable)
	 */
	@Override
	public void setException(Throwable exception) {
		super.setException(exception);
	}
}
