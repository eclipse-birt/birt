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

package org.eclipse.birt.report.designer.ui.lib.explorer.action;

import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.lib.explorer.resource.ReportResourceEntry;
import org.eclipse.birt.report.model.api.CommandStack;
import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.model.api.LibraryHandle;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.ThemeHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.jface.action.Action;

/**
 * 
 */

public class ApplyThemeAction extends Action {

	private static final String ACTION_TEXT = Messages.getString("ApplyThemeAction.Text"); //$NON-NLS-1$
	private Object element;

	/**
	 * @param text
	 * @param style
	 */
	public ApplyThemeAction() {
		super(ACTION_TEXT);
	}

	public void setSelectedTheme(Object element) {
		if (element instanceof ReportResourceEntry) {
			this.element = ((ReportResourceEntry) element).getReportElement();
		} else {
			this.element = element;
		}

	}

	/*
	 * (non-Javadoc) Method declared on IAction.
	 */
	public boolean isEnabled() {

		ModuleHandle moduleHandle = SessionHandleAdapter.getInstance().getReportDesignHandle();

		if (element == null || (!(element instanceof ThemeHandle))) {
			return false;
		}

		if (!(((ThemeHandle) element).getRoot() instanceof LibraryHandle)) {
			return false;
		}

		if (moduleHandle == null || (!(moduleHandle instanceof ReportDesignHandle))) {
			if (moduleHandle instanceof LibraryHandle) {
				if (((ThemeHandle) element).getRoot().getFileName().equals(moduleHandle.getFileName())) {
					return false;
				}
			} else {
				return false;
			}
		}

		ThemeHandle themeInReport = UIUtil.themeInModuleHandle((ThemeHandle) element, moduleHandle);
		// ReportDesignHandle report = (ReportDesignHandle) moduleHandle;
		if (themeInReport != null && moduleHandle.getTheme() == themeInReport) // already
		// exists
		// in
		// the
		// report
		{
			return false;
		}

		return true;
	}

	public void run() {
		if (!isEnabled()) {
			return;
		}
		CommandStack stack = SessionHandleAdapter.getInstance().getCommandStack();
		stack.startTrans(ACTION_TEXT);

		ModuleHandle moduleHandle = SessionHandleAdapter.getInstance().getReportDesignHandle();

		LibraryHandle library = (LibraryHandle) ((ThemeHandle) element).getRoot();
		try {
			if (UIUtil.includeLibrary(moduleHandle, library)) {
				UIUtil.applyTheme((ThemeHandle) element, moduleHandle, library);
				stack.commit();
			} else {
				stack.rollback();
			}

		} catch (DesignFileException e) {
			// TODO Auto-generated catch block
			stack.rollback();
		} catch (SemanticException e) {
			// TODO Auto-generated catch block
			stack.rollback();
		}

	}

}
