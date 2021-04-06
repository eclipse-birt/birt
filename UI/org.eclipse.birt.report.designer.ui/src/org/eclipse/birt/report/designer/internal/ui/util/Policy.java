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

package org.eclipse.birt.report.designer.internal.ui.util;

import org.eclipse.birt.report.designer.ui.ReportPlugin;
import org.eclipse.core.runtime.Platform;

/**
 * The utility class for tracing
 */

public class Policy {

	public static final boolean DEBUG = ReportPlugin.getDefault().isDebugging();

	public static final boolean TRACING_ACTIONS = getDebugOption("actions"); //$NON-NLS-1$

	public static final boolean TRACING_COMMANDS = getDebugOption("commands"); //$NON-NLS-1$

	public static final boolean TRACING_DIALOGS = getDebugOption("dialogs"); //$NON-NLS-1$

	public static final boolean TRACING_DND_DRAG = getDebugOption("dnd.drag"); //$NON-NLS-1$
	public static final boolean TRACING_DND_DROP = getDebugOption("dnd.drop"); //$NON-NLS-1$

	public static final boolean TRACING_RULER = getDebugOption("ruler"); //$NON-NLS-1$

	public static final boolean TRACING_EXTENSION_LOAD = getDebugOption("extension.load"); //$NON-NLS-1$

	public static final boolean TRACING_EDITPART_CREATE = getDebugOption("editpart.create"); //$NON-NLS-1$

	public static final boolean TRACING_MENU_SHOW = getDebugOption("menu.show"); //$NON-NLS-1$

	public static final boolean TRACING_PAGE_CHANGE = getDebugOption("page.change"); //$NON-NLS-1$

	public static final boolean TRACING_PAGE_CLOSE = getDebugOption("page.close"); //$NON-NLS-1$

	public static final boolean TRACING_DND = getDebugOption("dnd"); //$NON-NLS-1$

	/**
	 * Returns the debug option with the given id
	 * 
	 * @param id the id of the debug option
	 * @return the debug option,or false if the id doesn't exist.
	 */
	public static boolean getDebugOption(String id) {
		boolean option = false;
		if (DEBUG) {
			option = "true".equalsIgnoreCase(Platform.getDebugOption("org.eclipse.birt.report.designer.ui/tracing/" //$NON-NLS-1$ //$NON-NLS-2$
					+ id));
		}
		return option;
	}

}
