/*******************************************************************************
 * Copyright (c) 2004-2008 Actuate Corporation.
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

import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.IReportGraphicConstants;
import org.eclipse.birt.report.designer.ui.ReportPlatformUIImages;
import org.eclipse.birt.report.designer.ui.lib.explorer.LibraryExplorerTreeViewPage;
import org.eclipse.swt.SWT;
import org.eclipse.ui.actions.ActionFactory;

/**
 * The action class for refreshing all resources in resource explorer.
 */
public class RefreshResourceExplorerAction extends ResourceAction {

	/**
	 * Constructs an action for refreshing resource.
	 * 
	 * @param page the resource explorer page
	 */
	public RefreshResourceExplorerAction(LibraryExplorerTreeViewPage page) {
		super(Messages.getString("RefreshLibExplorerAction.Text"), page); //$NON-NLS-1$
		setId(ActionFactory.REFRESH.getId());
		setAccelerator(SWT.F5);
		setImageDescriptor(ReportPlatformUIImages.getImageDescriptor(IReportGraphicConstants.ICON_REFRESH));
		setDisabledImageDescriptor(
				ReportPlatformUIImages.getImageDescriptor(IReportGraphicConstants.ICON_REFRESH_DISABLE));
	}

	@Override
	public void run() {
		refreshAll();
	}
}
