/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.designer.core.runtime;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.UnknownHostException;
import java.text.MessageFormat;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.swt.SWTException;

/**
 * The subclass which extends from BirtException used for GUI to wrap expected
 * internal exception
 */

public class GUIException extends BirtException {

	private static final long serialVersionUID = 1L;

	private static final String MSG_FILE_NOT_FOUND = Messages.getString("ExceptionHandler.Message.FileNotFound"); //$NON-NLS-1$

	private static final String MSG_UNKNOWN_HOST = Messages.getString("ExceptionHandler.Message.UnknownHost"); //$NON-NLS-1$

	private static final String MSG_OUT_OF_MEMORY = Messages.getString("ExceptionHandler.Message.OutOfMemory"); //$NON-NLS-1$

	private static final String MSG_UNEXPECTED_EXCEPTION_OCURR = Messages
			.getString("ExceptionHandler.Meesage.UnexceptedExceptionOccur"); //$NON-NLS-1$

	private static final String MSG_CAUSED_BY = Messages.getString("ExceptionHandler.Message.CausedBy"); //$NON-NLS-1$

	public static final String GUI_ERROR_CODE_IO = "Error.GUIException.invokedByIOException"; //$NON-NLS-1$

	public static final String GUI_ERROR_CODE_SWT = "Error.GUIException.invokedBySWTException"; //$NON-NLS-1$

	public static final String GUI_ERROR_CODE_OUT_OF_MEMORY = "Error.GUIException.invokedByOutOfMemory"; //$NON-NLS-1$

	public static final String GUI_ERROR_CODE_UNEXPECTED = "Error.GUIException.invokedByUnexpectedException"; //$NON-NLS-1$

	/**
	 * Creates a new instance of GUI exception
	 * 
	 * @param pluginId the id of the plugin
	 * @param cause    the cause which invoked the exception
	 * 
	 * @return the GUIException created
	 */
	public static GUIException createGUIException(String pluginId, Throwable cause) {
		String errorCode = GUI_ERROR_CODE_UNEXPECTED;
		if (cause instanceof IOException) {
			errorCode = GUI_ERROR_CODE_IO;
		} else if (cause instanceof OutOfMemoryError) {
			errorCode = GUI_ERROR_CODE_OUT_OF_MEMORY;
		} else if (cause instanceof SWTException) {
			errorCode = GUI_ERROR_CODE_SWT;
		}
		GUIException ex = new GUIException(pluginId, errorCode, cause);
		if (errorCode != GUI_ERROR_CODE_UNEXPECTED) {
			ex.setSeverity(BirtException.INFO | BirtException.ERROR);
		}
		return ex;
	}

	/**
	 * Creates a new instance of GUI exception
	 * 
	 * @param pluginId the id of the plugin
	 * @param cause    the cause which invoked the exception
	 * 
	 * @return the GUIException created
	 */
	public static GUIException createGUIException(String pluginId, Throwable cause, String errorCode) {
		GUIException ex = new GUIException(pluginId, errorCode, cause);
		if (!GUI_ERROR_CODE_UNEXPECTED.equals(errorCode)) {
			ex.setSeverity(BirtException.INFO | BirtException.ERROR);
		}
		return ex;
	}

	/**
	 * Creates a new instance of GUI exception with the specified error code
	 * 
	 * @param pluginId  the id of the plugin
	 * @param errorCode the error code of the exception
	 * @param cause     the cause which invoked the exception
	 */
	private GUIException(String pluginId, String errorCode, Throwable cause) {
		super(pluginId, errorCode, null);
		initCause(cause);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Throwable#getLocalizedMessage()
	 */
	public String getMessage() {
		String message = Messages.getString(getErrorCode());

		if (message.equalsIgnoreCase(getErrorCode())) {
			message = getCause().getLocalizedMessage();
			if (getCause() instanceof UnknownHostException) {
				message = MSG_UNKNOWN_HOST + message;
			} else if (getCause() instanceof FileNotFoundException) {
				message = MSG_FILE_NOT_FOUND + message;
			} else if (getCause() instanceof OutOfMemoryError) {
				message = MSG_OUT_OF_MEMORY;
			}
			if (StringUtil.isBlank(message)) {
				message = MessageFormat.format(MSG_CAUSED_BY, new String[] { getCause().getClass().getName() });
			}
		}
		return message;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Throwable#getLocalizedMessage()
	 */
	public String getLocalizedMessage() {
		return getMessage();
	}

	/**
	 * Returns the reason for error status
	 * 
	 * @return the reason
	 */
	public String getReason() {
		String reason = null;
		if (getCause() instanceof OutOfMemoryError) {
			reason = MSG_OUT_OF_MEMORY;
		} else if (getCause() instanceof IOException || getCause() instanceof SWTException) {
			reason = getLocalizedMessage();
		} else {
			reason = MSG_UNEXPECTED_EXCEPTION_OCURR;
		}
		return reason;
	}
}
