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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.internal.ui.dnd.InsertInLayoutUtil;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.ListBandEditPart;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.ListEditPart;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.MultipleEditPart;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.ReportElementEditPart;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.TableCellEditPart;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.TableEditPart;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.TableUtil;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.util.DNDUtil;
import org.eclipse.birt.report.model.api.CommandStack;
import org.eclipse.birt.report.model.api.TableHandle;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.expressions.IEvaluationContext;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.ISources;

/**
 * 
 */

public class SelectionHandler extends AbstractHandler {

	protected Logger logger = Logger.getLogger(SelectionHandler.class.getName());
	private ExecutionEvent event;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.core.commands.AbstractHandler#execute(org.eclipse.core.commands.
	 * ExecutionEvent)
	 */
	public Object execute(ExecutionEvent arg0) throws ExecutionException {
		this.event = arg0;
		return null;
	}

	/**
	 * Gets element handles.
	 * 
	 * @return element handles
	 */
	protected List getElements(List list) {
		boolean isEditPart = false;
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i) instanceof ReportElementEditPart) {
				isEditPart = true;
			}
		}
		if (isEditPart) {
			return InsertInLayoutUtil.editPart2Model(list).toList();
		} else {
			return list;
		}

	}

	/**
	 * Gets element handles.
	 * 
	 * @return element handles
	 */
	protected List getElements(ISelection selection) {
		return InsertInLayoutUtil.editPart2Model(selection).toList();
	}

	/**
	 * Gets the first selected object.
	 * 
	 * @return The first selected object
	 */
	protected Object getFirstElement(List list) {
		Object[] array = getElements(list).toArray();
		if (array.length > 0) {
			return array[0];
		}
		return null;
	}

	protected Object getFirstSelectVariable() {
		IEvaluationContext context = (IEvaluationContext) event.getApplicationContext();
		Object selectVariable = UIUtil.getVariableFromContext(context, ISources.ACTIVE_CURRENT_SELECTION_NAME);
		Object selectList = selectVariable;
		if (selectVariable instanceof StructuredSelection) {
			selectList = ((StructuredSelection) selectVariable).toList();
		}

		if (selectList instanceof List && ((List) selectList).size() > 0) {
			selectVariable = getFirstElement((List) selectList);
		}

		return selectVariable;
	}

	/**
	 * Gets table edit part.
	 * 
	 * @return the table edit part
	 */
	protected TableEditPart getTableEditPart() {
		List list = (List) getSelectedObjects();
		if (list.isEmpty()) {
			return null;
		}
		TableEditPart part = null;
		for (int i = 0; i < list.size(); i++) {
			Object obj = list.get(i);
			if (obj instanceof TableEditPart) {
				part = (TableEditPart) obj;
			} else if (obj instanceof TableCellEditPart) {
				part = (TableEditPart) ((TableCellEditPart) obj).getParent();
			}
		}
		return part;
	}

	/**
	 * @return
	 */
	protected ReportElementEditPart getTableMultipleEditPart() {
		List list = getSelectedObjects();
		if (list.isEmpty()) {
			return null;
		}
		ReportElementEditPart part = null;
		for (int i = 0; i < list.size(); i++) {
			Object obj = list.get(i);
			if (obj instanceof MultipleEditPart && ((MultipleEditPart) obj).getModel() instanceof TableHandle) {
				part = (ReportElementEditPart) obj;
			}

		}
		return part;
	}

	/**
	 * Gets list edit part.
	 * 
	 * @return The current selected list edit part, null if no list edit part is
	 *         selected.
	 */
	protected ListEditPart getListEditPart() {
		List list = getSelectedObjects();
		if (list.isEmpty()) {
			return null;
		}
		ListEditPart part = null;
		for (int i = 0; i < list.size(); i++) {
			Object obj = list.get(i);
			if (obj instanceof ListEditPart) {
				part = (ListEditPart) obj;
			} else if (obj instanceof ListBandEditPart) {
				part = (ListEditPart) ((ListBandEditPart) obj).getParent();
			}
		}
		return part;
	}

	/**
	 * Returns a <code>List</code> containing the currently selected objects.
	 * 
	 * @return A List containing the currently selected objects.
	 */
	protected List getSelectedObjects() {
		IStructuredSelection selectVariable = getSelection();
		if (selectVariable == null)
			return Collections.EMPTY_LIST;
		return selectVariable.toList();
	}

	/**
	 * Returns a <code>List</code> containing the currently selected objects.
	 * 
	 * @return A List containing the currently selected objects.
	 */
	protected IStructuredSelection getSelection() {
		IEvaluationContext context = (IEvaluationContext) event.getApplicationContext();
		Object selectVariable = UIUtil.getVariableFromContext(context, ISources.ACTIVE_CURRENT_SELECTION_NAME);
		if (selectVariable != null) {
			if (selectVariable instanceof IStructuredSelection) {
				return (IStructuredSelection) selectVariable;
			} else {
				return new StructuredSelection(selectVariable);
			}
		}
		return null;
	}

	/**
	 * Gets the activity stack of the report
	 * 
	 * @return returns the stack
	 */
	protected CommandStack getActiveCommandStack() {
		return SessionHandleAdapter.getInstance().getCommandStack();
	}

	/**
	 * Gets models of selected elements
	 * 
	 * @return
	 */
	protected List getElementHandles() {
		boolean isEditPart = false;
		List selList = null;
		IEvaluationContext context = (IEvaluationContext) event.getApplicationContext();
		Object obj = UIUtil.getVariableFromContext(context, ICommandParameterNameContants.SELECTION);
		if (obj != null) {
			selList = new ArrayList();
			selList.add(obj);
		}

		if (selList == null || selList.size() < 1) {
			selList = getSelectedObjects();
		}
		for (int i = 0; i < selList.size(); i++) {
			if (selList.get(i) instanceof ReportElementEditPart) {
				isEditPart = true;
			}
		}
		if (isEditPart) {
			selList = DNDUtil.unwrapToModel(InsertInLayoutUtil
					.editPart2Model(TableUtil.filletCellInSelectionEditorpart(getSelection())).toList());
		}

		return selList;
	}

}
