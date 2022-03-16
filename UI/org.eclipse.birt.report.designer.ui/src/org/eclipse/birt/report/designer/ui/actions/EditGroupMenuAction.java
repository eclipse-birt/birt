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

package org.eclipse.birt.report.designer.ui.actions;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.designer.internal.ui.editors.schematic.actions.EditGroupAction;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.ListEditPart;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.TableEditPart;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.model.api.GroupHandle;
import org.eclipse.birt.report.model.api.ListingHandle;
import org.eclipse.birt.report.model.api.SlotHandle;
import org.eclipse.ui.IWorkbenchPart;

/**
 * Updates "Edit group" menu.
 */

public class EditGroupMenuAction extends MenuUpdateAction {

	public static final String ID = "edit group menu"; //$NON-NLS-1$

	/**
	 * @param part
	 */
	public EditGroupMenuAction(IWorkbenchPart part) {
		super(part);
		setId(ID);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.designer.ui.actions.MenuUpdateAction#getItems()
	 */
	@Override
	protected List getItems() {
		ListingHandle parentHandle = null;
		if (getTableEditPart() != null && getListEditPart() == null) {
			parentHandle = (ListingHandle) getTableEditPart().getModel();
		} else if (getListEditPart() != null && getTableEditPart() == null) {
			parentHandle = (ListingHandle) getListEditPart().getModel();
		} else if (UIUtil.getTableMultipleEditPart(getSelectedObjects()) != null) {
			parentHandle = (ListingHandle) UIUtil.getTableMultipleEditPart(getSelectedObjects()).getModel();
		} else {
			return new ArrayList();
		}

		SlotHandle handle = parentHandle.getGroups();
		Iterator iter = handle.iterator();
		ArrayList actionList = new ArrayList();
		while (iter.hasNext()) {
			GroupHandle groupHandle = (GroupHandle) iter.next();
			actionList.add(new EditGroupAction(null, groupHandle));
		}
		return actionList;
	}

	/**
	 * Gets table edit part.
	 *
	 * @return The current selected table edit part, null if no table edit part is
	 *         selected.
	 */
	protected TableEditPart getTableEditPart() {
		return UIUtil.getTableEditPart(getSelectedObjects());
	}

	/**
	 * Gets list edit part.
	 *
	 * @return The current selected list edit part, null if no list edit part is
	 *         selected.
	 */
	protected ListEditPart getListEditPart() {
		return UIUtil.getListEditPart(getSelectedObjects());
	}
}
