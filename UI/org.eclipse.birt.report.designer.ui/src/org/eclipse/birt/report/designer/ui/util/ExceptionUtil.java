/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.designer.ui.util;

import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.swt.SWT;

/**
 * Utility class for exceptoin and message handling.
 * 
 * @since 2.5
 */
public class ExceptionUtil {

	private ExceptionUtil() {
	}

	/**
	 * Handles the exceptoin in default way.
	 * 
	 * @param e
	 */
	public static void handle(Throwable e) {
		ExceptionHandler.handle(e);
	}

	/**
	 * Handles the exception with given dialog title and message.
	 * 
	 * @param e
	 * @param dialogTitle
	 * @param message
	 */
	public static void handle(Throwable e, String dialogTitle, String message) {
		ExceptionHandler.handle(e, dialogTitle, message);
	}

	/**
	 * Opens a message box with given title and message in the specified style
	 * 
	 * @param title   the title of the message box
	 * @param message the message displayed in the message box
	 * @param style   the style of the message box
	 * @return Returns the buttion id that selected to dismiss the dialog
	 */
	public static int openMessage(String title, String message, int style) {
		return ExceptionHandler.openMessageBox(title, message, style);
	}

	/**
	 * Opens an error message box with given title and message. It equals to call
	 * openMessageBox(title,message,SWT.ICON_ERROR)
	 * 
	 * @param title        the title of the message box
	 * @param errorMessage the message displayed in the message box
	 * @return Returns the buttion id that selected to dismiss the dialog
	 */
	public static int openError(String title, String errorMessage) {
		return openMessage(title, errorMessage, SWT.ICON_ERROR);
	}

}
