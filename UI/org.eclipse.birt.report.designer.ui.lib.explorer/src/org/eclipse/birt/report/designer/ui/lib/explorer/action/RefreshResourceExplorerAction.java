/*******************************************************************************
 * Copyright (c) 2004-2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
