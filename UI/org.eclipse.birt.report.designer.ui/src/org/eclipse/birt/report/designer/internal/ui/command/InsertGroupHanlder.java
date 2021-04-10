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

package org.eclipse.birt.report.designer.internal.ui.command;

import java.util.List;

import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.DummyEditpart;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.GridEditPart;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.ListBandEditPart;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.ListEditPart;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.MultipleEditPart;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.ReportElementEditPart;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.TableCellEditPart;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.TableEditPart;
import org.eclipse.birt.report.designer.internal.ui.util.Policy;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.model.api.CommandStack;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.TableHandle;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.expressions.IEvaluationContext;

/**
 * 
 */

public class InsertGroupHanlder extends SelectionHandler {

	// private Object currentModel;

	private static final String STACK_MSG_ADD_GROUP = Messages.getString("AddGroupAction.stackMsg.addGroup"); //$NON-NLS-1$

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.core.commands.AbstractHandler#execute(org.eclipse.core.commands.
	 * ExecutionEvent)
	 */
	public Object execute(ExecutionEvent event) throws ExecutionException {
		super.execute(event);

		IEvaluationContext context = (IEvaluationContext) event.getApplicationContext();
		// currentModel = context.getVariable(
		// ICommandParameterNameContants.INSERT_GROUP_CURRENT_MODEL_NAME );

		int position = 0;
		Object obj = UIUtil.getVariableFromContext(context, ICommandParameterNameContants.INSERT_GROUP_POSITION);
		if (obj != null && obj instanceof Integer) {
			position = ((Integer) obj).intValue();
		}

		if (Policy.TRACING_ACTIONS) {
			System.out.println("Insert group action >> Run ..."); //$NON-NLS-1$
		}
		CommandStack stack = getActiveCommandStack();
		stack.startTrans(STACK_MSG_ADD_GROUP);
		boolean retValue = false;
		if (getTableEditPart() != null) {
			retValue = getTableEditPart().insertGroup(position);
		} else if (getTableMultipleEditPart() != null) {
			retValue = UIUtil.createGroup((DesignElementHandle) getTableMultipleEditPart().getModel(), position);
		} else {
			retValue = getListEditPart().insertGroup(position);
		}
		if (retValue) {
			stack.commit();
		} else {
			stack.rollbackAll();
		}

		return Boolean.TRUE;
	}

	// fix bug 217589
	protected ReportElementEditPart getTableMultipleEditPart() {
		if (getSelection() == null || getSelection().isEmpty())
			return null;
		List list = getSelectedObjects();
		int size = list.size();
		ReportElementEditPart part = null;
		for (int i = 0; i < size; i++) {
			Object obj = list.get(i);
			if (i == 0 && obj instanceof ReportElementEditPart) {
				// currentModel = ( (ReportElementEditPart) obj ).getModel( );
			}

			ReportElementEditPart currentEditPart = null;
			if (obj instanceof MultipleEditPart && ((MultipleEditPart) obj).getModel() instanceof TableHandle) {
				currentEditPart = (ReportElementEditPart) obj;
			}

			else if (obj instanceof DummyEditpart) {
				continue;
			}
			if (part == null) {
				part = currentEditPart;
			}
			// Check if select only one table
			if (currentEditPart == null || currentEditPart != null && part != currentEditPart) {
				return null;
			}
		}
		// Only table permitted
		if (part instanceof GridEditPart)
			return null;
		return part;
	}

	/**
	 * Gets table edit part.
	 * 
	 * @return The current selected table edit part, null if no table edit part is
	 *         selected.
	 */
	protected TableEditPart getTableEditPart() {
		if (getSelection() == null || getSelection().isEmpty())
			return null;
		List list = getSelectedObjects();
		int size = list.size();
		TableEditPart part = null;
		for (int i = 0; i < size; i++) {
			Object obj = list.get(i);
			if (i == 0 && obj instanceof ReportElementEditPart) {
				// currentModel = ( (ReportElementEditPart) obj ).getModel( );
			}

			TableEditPart currentEditPart = null;
			if (obj instanceof TableEditPart) {
				currentEditPart = (TableEditPart) obj;
			} else if (obj instanceof TableCellEditPart) {
				currentEditPart = (TableEditPart) ((TableCellEditPart) obj).getParent();
			} else if (obj instanceof DummyEditpart) {
				continue;
			}
			if (part == null) {
				part = currentEditPart;
			}
			// Check if select only one table
			if (currentEditPart == null || currentEditPart != null && part != currentEditPart) {
				return null;
			}
		}
		// Only table permitted
		if (part instanceof GridEditPart)
			return null;
		return part;
	}

	/**
	 * Gets list edit part.
	 * 
	 * @return The current selected list edit part, null if no list edit part is
	 *         selected.
	 */
	protected ListEditPart getListEditPart() {
		if (getSelection() == null || getSelection().isEmpty())
			return null;
		List list = getSelectedObjects();
		int size = list.size();
		ListEditPart part = null;
		for (int i = 0; i < size; i++) {
			Object obj = list.get(i);
			if (i == 0 && obj instanceof ReportElementEditPart) {
				// currentModel = ( (ReportElementEditPart) obj ).getModel( );
			}

			ListEditPart currentEditPart = null;
			if (obj instanceof ListEditPart) {
				currentEditPart = (ListEditPart) obj;
			} else if (obj instanceof ListBandEditPart) {
				currentEditPart = (ListEditPart) ((ListBandEditPart) obj).getParent();
			}
			if (part == null) {
				part = currentEditPart;
			}
			// Check if select only one list
			if (currentEditPart == null || currentEditPart != null && part != currentEditPart) {
				return null;
			}
		}
		return part;
	}

	/**
	 * Gets the activity stack of the report
	 * 
	 * @return returns the stack
	 */
	protected CommandStack getActiveCommandStack() {
		return SessionHandleAdapter.getInstance().getCommandStack();
	}
}
