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

import org.eclipse.birt.report.designer.internal.ui.editors.layout.ReportLayoutEditor;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.actions.EditGroupAction;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.ListEditPart;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.TableEditPart;
import org.eclipse.birt.report.model.api.GroupHandle;
import org.eclipse.birt.report.model.api.ListingHandle;
import org.eclipse.birt.report.model.api.SlotHandle;
import org.eclipse.gef.editparts.AbstractEditPart;
import org.eclipse.jface.action.IAction;

/**
 * EditGroup Action to be used in cheat sheet with the template grouped_listing.
 * The first parameter is the table name, the second parameter is the name of
 * the group to edit.
 * 
 */
public class TemplateEditGroupAction extends TemplateBaseAction {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.ui.cheatsheets.actions.
	 * TemplateBaseDesignerAction#getActionID()
	 */
	protected IAction getAction(ReportLayoutEditor reportDesigner) {
		AbstractEditPart part = (AbstractEditPart) selection;
		ListingHandle handle = (ListingHandle) part.getModel();

		SlotHandle groups = handle.getGroups();
		Iterator iter = groups.iterator();
		GroupHandle groupToEdit = null;

		// look for the group with the right name
		while (iter.hasNext()) {
			GroupHandle group = (GroupHandle) iter.next();
			if (group.getName() != null && group.getName().equals(params[1])) {
				groupToEdit = group;
				break;
			}
		}

		// no group with the right name found, use the first one if any
		if (groupToEdit == null && groups.getCount() > 0) {
			groupToEdit = (GroupHandle) groups.iterator().next();
		}
		if (groupToEdit != null) {
			EditGroupAction action = new EditGroupAction(reportDesigner, groupToEdit);
			return action;
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.ui.cheatsheets.actions.
	 * TemplateBaseDesignerAction#showErrorWrongElementSelection()
	 */
	protected void showErrorWrongElementSelection() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.designer.ui.actions.cheatsheets.TemplateBaseAction#
	 * checkType(java.lang.Class)
	 */
	protected boolean checkType(Class class1) {
		return (class1 == TableEditPart.class || class1 == ListEditPart.class);
	}

}
