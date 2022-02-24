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

package org.eclipse.birt.report.designer.internal.ui.actions;

import java.util.ArrayList;

import org.eclipse.birt.report.designer.internal.ui.resourcelocator.ResourceFilter;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.IReportGraphicConstants;
import org.eclipse.birt.report.designer.ui.ReportPlatformUIImages;
import org.eclipse.birt.report.designer.ui.ReportPlugin;
import org.eclipse.birt.report.designer.ui.dialogs.ProjectFileDialog;
import org.eclipse.birt.report.designer.ui.dialogs.ResourceFilterDialog;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.window.Window;

public class ProjectFilterAction extends Action {

	private static final String ACTION_TEXT = Messages.getString("ProjectFilterAction.text"); //$NON-NLS-1$

	private ProjectFileDialog dialog;

	public ProjectFilterAction(ProjectFileDialog dialog) {
		super(ACTION_TEXT);
		this.dialog = dialog;
		setImageDescriptor(ReportPlatformUIImages.getImageDescriptor(IReportGraphicConstants.ICON_TOOL_FILTER));
		setToolTipText(ACTION_TEXT);
	}

	public void run() {
		ResourceFilterDialog dialog = new ResourceFilterDialog();
		ArrayList filters = new ArrayList();
		filters.addAll(ReportPlugin.getFilterMap().values());
		dialog.setInput(filters);
		if (dialog.open() == Window.OK) {
			IPreferenceStore store = ReportPlugin.getDefault().getPreferenceStore();
			for (int i = 0; i < filters.size(); i++) {
				ResourceFilter filter = (ResourceFilter) filters.get(i);
				store.setValue(filter.getType(), filter.isEnabled());
			}
		}
		this.dialog.refreshRoot();
	}
}
