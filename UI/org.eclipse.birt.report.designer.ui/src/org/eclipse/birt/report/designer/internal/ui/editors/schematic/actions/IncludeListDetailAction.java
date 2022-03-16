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

package org.eclipse.birt.report.designer.internal.ui.editors.schematic.actions;

import java.util.List;

import org.eclipse.birt.report.designer.core.model.schematic.ListHandleAdapter;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.ListBandEditPart;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.ListEditPart;
import org.eclipse.birt.report.designer.internal.ui.util.Policy;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.ui.IWorkbenchPart;

/**
 * Action of whether or not including footer of table
 */
public class IncludeListDetailAction extends SelectionAction {

	private static final String ACTION_MSG_INCLUDE_DETAIL = Messages
			.getString("IncludeListDetailAction.actionMsg.includeDetail"); //$NON-NLS-1$

	/** action ID */
	public static final String ID = "org.eclipse.birt.report.designer.internal.ui.editors.schematic.actions.IncludeListDetailAction"; //$NON-NLS-1$

	/**
	 * Constructs new instance.
	 *
	 * @param part current work bench part
	 */
	public IncludeListDetailAction(IWorkbenchPart part) {
		super(part);
		setId(ID);
		setChecked(true);
		setText(ACTION_MSG_INCLUDE_DETAIL);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.gef.ui.actions.WorkbenchPartAction#calculateEnabled()
	 */
	@Override
	protected boolean calculateEnabled() {
		return true;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.gef.ui.actions.SelectionAction#update()
	 */
	@Override
	public void update() {
		super.update();
		if (getListEditpart() != null) {
			ListEditPart part = getListEditpart();
			setChecked(part.isIncludeSlotHandle(ListHandleAdapter.DETAIL));
		}
	}

	/**
	 * Runs action.
	 */
	@Override
	public void run() {
		if (Policy.TRACING_ACTIONS) {
			System.out.println("Include list detail action >> Run ..."); //$NON-NLS-1$
		}
		getListEditpart().includeSlotHandle(isChecked(), ListHandleAdapter.DETAIL);
	}

	/**
	 * Gets list edit part.
	 *
	 * @return list edit part The current list edit part
	 */
	protected ListEditPart getListEditpart() {
		if (getSelectedObjects() == null || getSelectedObjects().isEmpty()) {
			return null;
		}
		List list = getSelectedObjects();
		int size = list.size();
		ListEditPart part = null;
		for (int i = 0; i < size; i++) {
			Object obj = getSelectedObjects().get(i);

			if (obj instanceof ListEditPart) {
				part = (ListEditPart) obj;
			} else if (obj instanceof ListBandEditPart) {
				part = (ListEditPart) ((ListBandEditPart) obj).getParent();
			}
		}
		return part;
	}
}
