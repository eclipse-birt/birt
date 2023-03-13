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

package org.eclipse.birt.report.designer.internal.ui.util;

import java.io.FileNotFoundException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.designer.core.runtime.ErrorStatus;
import org.eclipse.birt.report.designer.core.runtime.GUIException;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.ReportPlugin;
import org.eclipse.birt.report.designer.ui.extensions.ExceptionHandlerRegistry;
import org.eclipse.birt.report.designer.ui.extensions.IDesignerExceptionHandler;
import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

/**
 * The utility to handle exceptions
 */

public class ExceptionHandler {

	private static final String TITLE_ERROR = Messages.getString("ExceptionHandler.Title.Error"); //$NON-NLS-1$

	private static final String TITLE_FILE_NOT_FOUND = Messages.getString("ExceptionHandler.Title.FileNotFound"); //$NON-NLS-1$

	private static final String MSG_FILE_NOT_FOUND_PREFIX = Messages.getString("ExceptionHandler.Title.FileNotFound"); //$NON-NLS-1$

	private static final String TITLE_UNKNOWN_HOST = Messages.getString("ExceptionHandler.Title.UnknownHost"); //$NON-NLS-1$

	private static final String TITLE_PART_INIT_ERROR = Messages.getString("ExceptionHandler.Title.PartInitError"); //$NON-NLS-1$

	private static final String MSG_UNKNOWN_HOST = Messages.getString("ExceptionHandler.Message.UnknownHost"); //$NON-NLS-1$

	private static final String MSG_PART_INIT_ERROR = Messages.getString("ExceptionHandler.Message.PartInitError"); //$NON-NLS-1$

	private static final String MSG_BIRT_EXCEPTION_OCURR = Messages
			.getString("ExceptionHandler.Message.BirtExceptionOccur"); //$NON-NLS-1$

	private static final String LABEL_PLUGIN_PROVIDER = Messages.getString("ExceptionHandler.Label.PluginProvider"); //$NON-NLS-1$

	private static final String LABEL_PLUGIN_NAME = Messages.getString("ExceptionHandler.Label.PluginName"); //$NON-NLS-1$

	private static final String LABEL_PLUGIN_ID = Messages.getString("ExceptionHandler.Label.PluginId"); //$NON-NLS-1$

	private static final String LABEL_PLUGIN_VERSION = Messages.getString("ExceptionHandler.Label.PluginVersion"); //$NON-NLS-1$

	private static final String LABEL_ERROR_MESSAGE = Messages.getString("ExceptionHandler.Label.ErrorMessage"); //$NON-NLS-1$

	private static final String LABEL_ERROR_CODE = Messages.getString("ExceptionHandler.Label.ErrorCode"); //$NON-NLS-1$

	private static final String UNKNOWN_PLUGIN = Messages.getString("ExceptionHandler.Label.UnknownPlugin"); //$NON-NLS-1$

	private static List ExpectedExceptionList = new ArrayList();

	static {
		ExpectedExceptionList.add(SemanticException.class);
	}

	/**
	 * Handles the exception
	 *
	 * @param e the exception to be handled
	 *
	 */
	public static void handle(Throwable e) {
		handle(e, false);
	}

	/**
	 * Handles the exception
	 *
	 * @param e the exception to be handled
	 *
	 */
	public static void handle(Throwable e, boolean logOnly) {

		IDesignerExceptionHandler customizeHandler = ExceptionHandlerRegistry.getInstance().getExceptionHandler();
		if (customizeHandler != null) {
			// Override the BIRT exception handler, handle exception with the
			// customize one
			customizeHandler.handle(e);
			return;
		}

		if (logOnly) {
			if (!(e instanceof BirtException)) {
				e = GUIException.createGUIException(ReportPlugin.REPORT_UI, e);
			}

			if (needLog(e)) {
				ErrorStatus status = createErrorStatus(e);
				if (status != null) {
					status.setException(e);
					ReportPlugin.getDefault().getLog().log(status);
				}
			}
		} else {
			String title = TITLE_ERROR;
			String message = e.getLocalizedMessage();
			if (e instanceof UnknownHostException) {
				title = TITLE_UNKNOWN_HOST;
				message = MSG_UNKNOWN_HOST + message;
			} else if (e instanceof FileNotFoundException) {
				title = TITLE_FILE_NOT_FOUND;
				message = MSG_FILE_NOT_FOUND_PREFIX + ":" + e.getLocalizedMessage(); //$NON-NLS-1$
			} else if (e instanceof PartInitException) {
				title = TITLE_PART_INIT_ERROR;
				message = MSG_PART_INIT_ERROR;
			}

			handle(e, title, message);
		}
	}

	/**
	 * Handles the exception
	 *
	 * @param e           the exception to be handled
	 * @param dialogTitle the title of the error dialog
	 * @param message     the error message
	 *
	 */
	public static void handle(Throwable e, String dialogTitle, String message) {
		if (!(e instanceof BirtException)) {
			e = GUIException.createGUIException(ReportPlugin.REPORT_UI, e);
		}
		ErrorStatus status = createErrorStatus(e);
		if (status != null) {
			ErrorDialog.openError(PlatformUI.getWorkbench().getDisplay().getActiveShell(), dialogTitle, message,
					status);
			if (needLog(e)) {
				status.setException(e);
				ReportPlugin.getDefault().getLog().log(status);
			}
		} else {
			openErrorMessageBox(dialogTitle, message);
		}
	}

	private static ErrorStatus createErrorStatus(Throwable e) {
		String reason = null;
		String[] detail = null;
		BirtException birtException = (BirtException) e;
		if (e instanceof DesignFileException) {
			detail = e.toString().split("\n"); //$NON-NLS-1$
			reason = detail[0];
			detail[0] = LABEL_ERROR_MESSAGE + ":" + detail[0]; //$NON-NLS-1$
		} else {
			if (e instanceof GUIException) {
				reason = ((GUIException) e).getReason();
			} else {
				reason = MSG_BIRT_EXCEPTION_OCURR;
			}
			detail = new String[] { LABEL_ERROR_MESSAGE + ":" //$NON-NLS-1$
					+ birtException.getLocalizedMessage(), };
		}
		String id = birtException.getPluginId();
		if (id == null) {
			id = UNKNOWN_PLUGIN;
		}
		ErrorStatus status = new ErrorStatus(id, 1001, reason, null);
		if (!UNKNOWN_PLUGIN.equals(id)) {
			status.addInformation(LABEL_PLUGIN_PROVIDER + UIUtil.getPluginProvider(id));
			status.addInformation(LABEL_PLUGIN_NAME + UIUtil.getPluginName(id));
			status.addInformation(LABEL_PLUGIN_ID + id);
			status.addInformation(LABEL_PLUGIN_VERSION + UIUtil.getPluginVersion(id));
		}
		int severity = birtException.getSeverity();
		if (severity == (BirtException.INFO | BirtException.ERROR)) {
			severity = IStatus.ERROR;
		}
		status.addStatus(LABEL_ERROR_CODE + ":" + birtException.getErrorCode(), severity); //$NON-NLS-1$
		for (int i = 0; i < detail.length; i++) {
			status.addStatus(detail[i], severity);
		}
		return status;
	}

	private static boolean needLog(Throwable e) {
		for (Iterator iter = ExpectedExceptionList.iterator(); iter.hasNext();) {
			if (((Class) iter.next()).isInstance(e)) {
				return false;
			}
		}
		if (e instanceof BirtException) {
			return (((BirtException) e).getSeverity() ^ BirtException.INFO) != BirtException.INFO;
		}
		return true;
	}

	/**
	 * Opens a message box with given title and message in the specified style
	 *
	 * @param title   the title of the message box
	 * @param message the message displayed in the message box
	 * @param style   the style of the message box
	 */
	public static int openMessageBox(String title, String message, int style) {
		MessageBox box = new MessageBox(UIUtil.getDefaultShell(), style);
		box.setText(title);
		box.setMessage(message);
		return box.open();
	}

	/**
	 * Opens an error message box with given title and message. It equals to call
	 * openMessageBox(title,message,SWT.ICON_ERROR)
	 *
	 * @param title        the title of the message box
	 * @param errorMessage the message displayed in the message box
	 */
	public static int openErrorMessageBox(String title, String errorMessage) {
		return openMessageBox(title, errorMessage, SWT.ICON_ERROR);
	}

}
