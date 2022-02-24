/*******************************************************************************
 * Copyright (c) 2007, 2008 Actuate Corporation.
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

import java.util.ArrayList;

import org.eclipse.birt.report.designer.internal.ui.resourcelocator.ResourceFilter;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.IReportGraphicConstants;
import org.eclipse.birt.report.designer.ui.ReportPlatformUIImages;
import org.eclipse.birt.report.designer.ui.dialogs.ResourceFilterDialog;
import org.eclipse.birt.report.designer.ui.lib.explorer.LibraryExplorerPlugin;
import org.eclipse.birt.report.designer.ui.lib.explorer.LibraryExplorerTreeViewPage;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.window.Window;

/**
 * The action class for filtering resource in resource explorer.
 */
public class FilterResourceAction extends ResourceAction {

	/**
	 * Constructs an action for filtering resource.
	 * 
	 * @param page the resource explorer page
	 */
	public FilterResourceAction(LibraryExplorerTreeViewPage page) {
		super(Messages.getString("LibraryFileFilterAction.Text"), page); //$NON-NLS-1$
		setImageDescriptor(ReportPlatformUIImages.getImageDescriptor(IReportGraphicConstants.ICON_TOOL_FILTER));
	}

	@Override
	public void run() {
		ResourceFilterDialog dialog = new ResourceFilterDialog();
		ArrayList<ResourceFilter> filters = new ArrayList<ResourceFilter>();

		filters.addAll(LibraryExplorerPlugin.getFilterMap().values());
		dialog.setInput(filters);

		if (dialog.open() == Window.OK) {
			IPreferenceStore store = LibraryExplorerPlugin.getDefault().getPreferenceStore();

			for (int i = 0; i < filters.size(); i++) {
				ResourceFilter filter = filters.get(i);

				store.setValue(filter.getType(), filter.isEnabled());
			}
			refreshAll();
		}
	}
}
