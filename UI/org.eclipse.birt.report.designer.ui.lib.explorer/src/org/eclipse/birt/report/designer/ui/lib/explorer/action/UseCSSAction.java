/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
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

package org.eclipse.birt.report.designer.ui.lib.explorer.action;

import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.lib.explorer.LibraryExplorerTreeViewPage;
import org.eclipse.birt.report.model.api.LibraryHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.jface.action.IAction;

/**
 * The action used to add CSS resource to a report or library.
 */
public class UseCSSAction extends ResourceAction {

	/** The action used to add CSS resource to a report. */
	private final IAction useCssInReportDesign;

	/** The action used to add CSS resource to a library. */
	private final IAction useCssInTheme;

	/**
	 * Constructs an action for use CSS resource.
	 * 
	 * @param page the resource explorer page
	 */
	public UseCSSAction(LibraryExplorerTreeViewPage page) {
		super(Messages.getString("UseCSSAction.Text"), page); //$NON-NLS-1$
		useCssInReportDesign = new UseCssInReportDesignAction(page);
		useCssInTheme = new UseCssInThemeAction(page);
	}

	@Override
	public boolean isEnabled() {
		if (isAddingInReport()) {
			return useCssInReportDesign.isEnabled();
		} else if (isAddingInLibrary()) {
			return useCssInTheme.isEnabled();
		}
		return false;
	}

	@Override
	public void run() {
		if (isAddingInReport()) {
			useCssInReportDesign.run();
		} else if (isAddingInLibrary()) {
			useCssInTheme.run();
		}
	}

	/**
	 * Returns <code>true</code> if is Adding in a report, <code>false</code>
	 * otherwise.
	 * 
	 * @return <code>true</code> if is Adding in a report, <code>false</code>
	 *         otherwise.
	 */
	private boolean isAddingInReport() {
		Object obj = SessionHandleAdapter.getInstance().getReportDesignHandle();

		return obj instanceof ReportDesignHandle;
	}

	/**
	 * Returns <code>true</code> if is Adding in a library, <code>false</code>
	 * otherwise.
	 * 
	 * @return <code>true</code> if is Adding in a library, <code>false</code>
	 *         otherwise.
	 */
	private boolean isAddingInLibrary() {
		Object obj = SessionHandleAdapter.getInstance().getReportDesignHandle();

		return obj instanceof LibraryHandle;
	}
}
