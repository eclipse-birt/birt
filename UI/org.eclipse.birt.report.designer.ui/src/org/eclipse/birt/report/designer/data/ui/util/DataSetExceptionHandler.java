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

package org.eclipse.birt.report.designer.data.ui.util;

import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.util.UIUtil;

public class DataSetExceptionHandler {

	public static void handle(Throwable e) {
		handle(Messages.getString("ExceptionHandler.Title.Error"), //$NON-NLS-1$
				Messages.getString("ExceptionHandler.Meesage.ExceptionOccur"), e); //$NON-NLS-1$
	}

	public static void handle(String message, Throwable e) {
		handle(Messages.getString("ExceptionHandler.Title.Error"), message, e); //$NON-NLS-1$
	}

	public static void handle(String dialogTitle, String message, Throwable e) {
		new ExceptionDialog(UIUtil.getDefaultShell(), dialogTitle, message, e).open();
	}

}
