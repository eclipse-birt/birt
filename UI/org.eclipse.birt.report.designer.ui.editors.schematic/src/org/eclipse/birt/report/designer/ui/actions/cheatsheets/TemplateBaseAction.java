/*************************************************************************************
 * Copyright (c) 2004 Actuate Corporation and others.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 * Actuate Corporation - Initial implementation.
 ************************************************************************************/

package org.eclipse.birt.report.designer.ui.actions.cheatsheets;

import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.designer.internal.ui.editors.layout.ReportLayoutEditor;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.ui.editors.MultiPageReportEditor;
import org.eclipse.birt.report.designer.ui.editors.pages.ReportLayoutEditorFormPage;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.ui.parts.AbstractEditPartViewer;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.cheatsheets.ICheatSheetAction;
import org.eclipse.ui.cheatsheets.ICheatSheetManager;

/**
 * Base class for All cheat sheets action that apply when a specific editpart is
 * selected inside the editor
 *
 */
public abstract class TemplateBaseAction extends Action implements ICheatSheetAction {

	protected EditPart selection;
	protected String[] params;

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.ui.cheatsheets.ICheatSheetAction#run(java.lang.String[],
	 * org.eclipse.ui.cheatsheets.ICheatSheetManager)
	 */
	@Override
	public void run(String[] params, ICheatSheetManager manager) {
		this.params = params;
		IEditorPart editor = UIUtil.getActiveReportEditor();
		if (editor instanceof MultiPageReportEditor) {
			// switch to Design Page
			((MultiPageReportEditor) editor).setActivePage(ReportLayoutEditorFormPage.ID);

			// init some variables
			ReportLayoutEditor reportDesigner = (ReportLayoutEditor) ((MultiPageReportEditor) editor)
					.getActivePageInstance();
			AbstractEditPartViewer viewer = (AbstractEditPartViewer) reportDesigner.getGraphicalViewer();

			// tries to select the EditPart for the item name
			selectEditPartForItemName(params[0], (MultiPageReportEditor) editor, viewer);

			// if the viewer selection contains a match for the class, proceed
			selection = matchSelectionType(viewer);
			if (selection != null) {
				IAction action = getAction(reportDesigner);
				if (action != null && action.isEnabled()) {
					action.run();
				}
			} else {
				// show an error dialog asking to select the right element
				showErrorWrongElementSelection();
			}
		} else {
			// show an error asking to select the right editor
			showErrorWrongEditor();
		}
	}

	/**
	 * show an error asking to select the right editor
	 */
	protected void showErrorWrongEditor() {
		// TODO Auto-generated method stub
	}

	/**
	 * show an error dialog asking to select the applicable element for that action
	 */
	protected abstract void showErrorWrongElementSelection();

	/**
	 * Check that the viewer selection is a good match for this action
	 *
	 * @param viewer The Edit Part viewer
	 */
	protected EditPart matchSelectionType(AbstractEditPartViewer viewer) {
		EditPart part = null;
		List editParts = viewer.getSelectedEditParts();

		for (Iterator iter = editParts.iterator(); iter.hasNext();) {
			Object iterEditPart = iter.next();
			if (checkType(iterEditPart.getClass())) {
				part = (EditPart) iterEditPart;
				break;
			}
		}
		return part;
	}

	/**
	 * Check if the type is appropriate for this action
	 *
	 * @param class1 type of the selected EditPart
	 * @return true if the type matches an appropriate EditPart
	 */
	protected abstract boolean checkType(Class class1);

	/**
	 * select the EditPart in the editor if we find it if we don't find it, just try
	 * with the current selection.
	 *
	 * @param itemName The name of the Item in the report to select
	 * @param editor   The Report Editor
	 * @param viewer   The EditPart Viewer
	 */
	protected void selectEditPartForItemName(String itemName, MultiPageReportEditor editor,
			AbstractEditPartViewer viewer) {
		ModuleHandle reportDesign = editor.getModel();
		DesignElementHandle elementHandle = reportDesign.findElement(itemName);
		if (elementHandle != null) {
			EditPart editPart = (EditPart) viewer.getEditPartRegistry().get(elementHandle);
			if (editPart != null) {
				viewer.select(editPart);
			}
		}
	}

	protected abstract IAction getAction(ReportLayoutEditor reportDesigner);

}
