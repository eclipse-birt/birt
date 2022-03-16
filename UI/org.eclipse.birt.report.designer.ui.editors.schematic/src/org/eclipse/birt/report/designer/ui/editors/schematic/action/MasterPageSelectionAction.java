/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
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

package org.eclipse.birt.report.designer.ui.editors.schematic.action;

import java.util.ArrayList;

import org.eclipse.birt.report.designer.core.util.mediator.request.ReportRequest;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.ui.actions.MenuUpdateAction.DynamicItemAction;
import org.eclipse.birt.report.designer.ui.editors.pages.ReportMasterPageEditorFormPage;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.MasterPageHandle;
import org.eclipse.jface.action.IAction;
import org.eclipse.ui.forms.editor.FormEditor;

public class MasterPageSelectionAction extends DynamicItemAction {

	private MasterPageHandle handle;

	public MasterPageSelectionAction(MasterPageHandle handle) {
		super(DEUtil.getDisplayLabel(handle), IAction.AS_RADIO_BUTTON);
		this.handle = handle;
		boolean checked = false;
		FormEditor formEditor = UIUtil.getActiveReportEditor();
		if (formEditor != null) {
			if (ReportMasterPageEditorFormPage.ID.equals(formEditor.getActivePageInstance().getId())) {
				checked = (((ReportMasterPageEditorFormPage) (formEditor.getActivePageInstance())).getGraphicalViewer()
						.getContents().getModel() == handle);
			}
		}
		setChecked(checked);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.jface.action.Action#isEnabled()
	 */
	@Override
	public boolean isEnabled() {
		return handle != null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.jface.action.Action#run()
	 */
	@Override
	public void run() {
		UIUtil.getActiveReportEditor().setActivePage(ReportMasterPageEditorFormPage.ID);
		ReportRequest request = new ReportRequest(ReportRequest.LOAD_MASTERPAGE);
		ArrayList list = new ArrayList(1);
		list.add(handle);
		request.setSelectionObject(list);
		((ReportMasterPageEditorFormPage) UIUtil.getActiveReportEditor().getActivePageInstance())
				.performRequest(request);
	}
}
