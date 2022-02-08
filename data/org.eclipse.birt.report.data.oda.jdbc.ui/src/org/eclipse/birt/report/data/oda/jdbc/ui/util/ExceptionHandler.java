/*******************************************************************************
 * Copyright (c) 2005 Sybase, Inc.
 * 
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors: mbarrett - initial API and implementation
 ******************************************************************************/
package org.eclipse.birt.report.data.oda.jdbc.ui.util;

import org.eclipse.swt.widgets.Shell;

/**
 * Exception handler that displays an exception in a message box, and prints a
 * stack trace.
 * 
 * @author mbarrett
 */
public class ExceptionHandler {

	public static void showException(Shell parentShell, String title, String msg, Throwable ex) {
		new ExceptionDialog(parentShell, title, msg, ex).open();
	}
}
