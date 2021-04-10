/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.designer.internal.ui.editors.schematic.actions;

import java.util.List;

import org.eclipse.birt.report.designer.internal.ui.dnd.InsertInLayoutUtil;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.ListEditPart;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.TableEditPart;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.TableUtil;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.model.api.CellHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ListGroupHandle;
import org.eclipse.birt.report.model.api.RowHandle;
import org.eclipse.birt.report.model.api.SlotHandle;
import org.eclipse.birt.report.model.api.TableGroupHandle;
import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.jface.action.Action;
import org.eclipse.ui.IWorkbenchPart;

/**
 * Add group action
 */

public class AddGroupAction extends SelectionAction {

//	private static final String STACK_MSG_ADD_GROUP = Messages
//			.getString( "AddGroupAction.stackMsg.addGroup" ); //$NON-NLS-1$

	private static final String ACTION_MSG_ADD_GROUP = Messages.getString("AddGroupAction.actionMsg.addGroup"); //$NON-NLS-1$

	public static final String ID = "AddGroupAction"; //$NON-NLS-1$

	private Action action = null;

	/**
	 * Constructor
	 * 
	 * @param part
	 */
	public AddGroupAction(IWorkbenchPart part) {
		super(part);
		setId(ID);
		setText(ACTION_MSG_ADD_GROUP);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.ui.actions.WorkbenchPartAction#calculateEnabled()
	 */
	protected boolean calculateEnabled() {
		// return getTableEditPart( ) != null ^ getListEditPart( ) != null;
		action = getAction();
		if (action != null) {
			return action.isEnabled();
		}
		return false;
	}

	/**
	 * Runs action.
	 * 
	 */
	public void run() {
		if (action != null) {
			action.run();
		}
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

	/**
	 * Gets the first selected object.
	 * 
	 * @return The first selected object
	 */
	protected Object getFirstElement() {
		Object[] array = getElements().toArray();
		if (array.length > 0) {
			return array[0];
		}
		return null;
	}

	/**
	 * Gets element handles.
	 * 
	 * @return element handles
	 */
	protected List getElements() {
		return InsertInLayoutUtil.editPart2Model(TableUtil.filletCellInSelectionEditorpart(getSelection())).toList();
	}

	private Action getAction() {
		Action action = null;
		if (getFirstElement() instanceof CellHandle || getFirstElement() instanceof RowHandle) {
			RowHandle row;
			if (getFirstElement() instanceof CellHandle) {
				row = (RowHandle) ((CellHandle) getFirstElement()).getContainer();
			} else {
				row = (RowHandle) getFirstElement();
			}
			if (!(row.getContainer() instanceof TableGroupHandle)) {
				int slotID = row.getContainerSlotHandle().getSlotID();
				action = (InsertGroupActionFactory.createInsertGroupAction(slotID, getSelectedObjects()));
				return action;
			}

		}

		if (getFirstElement() instanceof SlotHandle) {
			DesignElementHandle container = ((SlotHandle) getFirstElement()).getElementHandle();
			if (!(container instanceof ListGroupHandle)) {
				int slotID = ((SlotHandle) getFirstElement()).getSlotID();
				action = InsertGroupActionFactory.createInsertGroupAction(slotID, getSelectedObjects());
				return action;
			}
		}
		return action;
	}

}