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

import org.eclipse.core.resources.IProject;
import org.eclipse.swt.graphics.Resource;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

/**
 * A utility class for UI operations
 *
 * @since 2.5
 */
public class UIUtil {

	private UIUtil() {
	}

	/**
	 * Sets the given help context id on the given control's shell.
	 *
	 * @param control   the control on which to register the context id. Should not
	 *                  be null.
	 * @param contextId the context id to use when F1 help is invoked. Should not be
	 *                  null.
	 */
	public static void bindHelp(Control control, String contextId) {
		org.eclipse.birt.report.designer.internal.ui.util.UIUtil.bindHelp(control, contextId);
	}

	/**
	 * Returns the default shell used by dialogs
	 *
	 * @return the default shell of the current display
	 */
	public static Shell getDefaultShell() {
		return org.eclipse.birt.report.designer.internal.ui.util.UIUtil.getDefaultShell();
	}

	/**
	 * Convenient method to setup button to invoke expression builder
	 *
	 * @param button The button that need set expression image. Should not be null.
	 */
	public static void setExpressionButtonImage(Button button) {
		org.eclipse.birt.report.designer.internal.ui.util.UIUtil.setExpressionButtonImage(button);
	}

	/**
	 * Returns the maximum length in pixels of given strings in a control.
	 *
	 * @param strArray the string array. each value in the array should not be null.
	 *
	 * @param control  the control in which this text will be displayed. Should not
	 *                 be null.
	 *
	 * @return the length in pixels
	 */
	public static int getMaxStringWidth(String[] strArray, Control control) {
		return org.eclipse.birt.report.designer.internal.ui.util.UIUtil.getMaxStringWidth(strArray, control);
	}

	/**
	 * @return Returns the associated project for current active editor if
	 *         applicable.
	 */
	public static IProject getCurrentProject() {
		return org.eclipse.birt.report.designer.internal.ui.util.UIUtil.getCurrentProject();
	}

	/**
	 * Utility method to dispose resources in a dispose listener.
	 *
	 * @param resource the resource to dispose
	 */
	public static void dispose(Resource resource) {
		if (resource != null && !resource.isDisposed()) {
			resource.dispose();
		}
	}
}
