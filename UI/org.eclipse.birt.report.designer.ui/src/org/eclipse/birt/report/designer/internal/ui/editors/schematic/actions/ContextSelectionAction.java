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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.core.model.schematic.HandleAdapterFactory;
import org.eclipse.birt.report.designer.core.model.schematic.ListBandProxy;
import org.eclipse.birt.report.designer.internal.ui.dnd.InsertInLayoutUtil;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.DummyEditpart;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.ListBandEditPart;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.ListEditPart;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.MultipleEditPart;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.ReportElementEditPart;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.TableCellEditPart;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.TableEditPart;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.TableUtil;
import org.eclipse.birt.report.model.api.ColumnHandle;
import org.eclipse.birt.report.model.api.CommandStack;
import org.eclipse.birt.report.model.api.ListGroupHandle;
import org.eclipse.birt.report.model.api.RowHandle;
import org.eclipse.birt.report.model.api.TableGroupHandle;
import org.eclipse.birt.report.model.api.TableHandle;
import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.ui.IWorkbenchPart;

/**
 * Selection action within context menu.
 */

public class ContextSelectionAction extends SelectionAction {

	protected Logger logger = Logger.getLogger(ContextSelectionAction.class.getName());

	/**
	 * @param part
	 * @param style
	 */
	public ContextSelectionAction(IWorkbenchPart part, int style) {
		super(part);
	}

	/**
	 * @param part
	 */
	public ContextSelectionAction(IWorkbenchPart part) {
		super(part);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.gef.ui.actions.WorkbenchPartAction#calculateEnabled()
	 */
	@Override
	protected boolean calculateEnabled() {
		return false;
	}

	/**
	 * Gets table edit part.
	 *
	 * @return the table edit part
	 */
	protected TableEditPart getTableEditPart() {
		List list = getSelectedObjects();
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
	 * Gets list group.
	 *
	 * @return The current selected list group part, null if no list group is
	 *         selected.
	 */
	protected Object getListGroup() {
		List list = getSelectedObjects();
		if (list.isEmpty()) {
			return null;
		}
		if (list.get(0) instanceof ListBandEditPart) {
			ListBandProxy group = (ListBandProxy) ((ListBandEditPart) list.get(0)).getModel();
			if (group.getElemtHandle() instanceof ListGroupHandle) {
				return group;
			}
		}
		return null;
	}

	/**
	 * Gets the current selected table group object.
	 *
	 * @return the selected table group object
	 */
	protected Object getTableGroup() {
		List list = getSelectedObjects();
		if (list.isEmpty()) {
			return null;
		}
		List groups = new ArrayList();
		TableGroupHandle groupHandle = null;
		for (Iterator itor = list.iterator(); itor.hasNext();) {
			Object obj = itor.next();
			if (obj instanceof DummyEditpart) {
				DummyEditpart part = (DummyEditpart) obj;
				if (part.getModel() instanceof RowHandle) {
					RowHandle group = (RowHandle) part.getModel();
					if (group.getContainer() instanceof TableGroupHandle) {
						TableGroupHandle handle = (TableGroupHandle) group.getContainer();
						if (groupHandle != handle) {
							// stores different group row handles.
							groups.add(group);
							groupHandle = handle;
						}
					} else {
						return null;
					}
				}
			}
		}
		// only returns one group row handle, if more, returns null.
		if (groups.size() == 1) {
			return groups.get(0);
		}
		return null;
	}

	/**
	 * Gets the current selected column objects.
	 *
	 * @return The current column objects
	 */
	protected List getColumnHandles() {
		List list = getSelectedObjects();
		if (list.isEmpty()) {
			return Collections.EMPTY_LIST;
		}
		List columnHandles = new ArrayList();
		for (int i = 0; i < list.size(); i++) {
			Object obj = list.get(i);
			if (obj instanceof DummyEditpart) {
				if (((DummyEditpart) obj).getModel() instanceof ColumnHandle) {
					columnHandles.add(((DummyEditpart) obj).getModel());
				}
			}
		}
		return columnHandles;
	}

	/**
	 * Gets the current selected row objects.
	 *
	 * @return The current selected row objects.
	 */

	protected List getRowHandles() {
		List list = getSelectedObjects();
		if (list.isEmpty()) {
			return Collections.EMPTY_LIST;
		}
		List rowHandles = new ArrayList();
		for (int i = 0; i < list.size(); i++) {
			Object obj = list.get(i);
			if (obj instanceof DummyEditpart) {
				if (((DummyEditpart) obj).getModel() instanceof RowHandle) {
					rowHandles.add(((DummyEditpart) obj).getModel());
				}
			}
		}
		return rowHandles;
	}

	/**
	 * Gets row numbers of selected rows. And sorts the array of ints into ascending
	 * numerical order.
	 */
	protected int[] getRowNumbers() {
		List rowHandles = getRowHandles();
		if (rowHandles.isEmpty()) {
			return new int[0];
		}
		int size = rowHandles.size();
		int[] rowNumbers = new int[size];

		for (int i = 0; i < size; i++) {
			rowNumbers[i] = getRowNumber(rowHandles.get(i));
		}

		// sorts array before returning.
		int[] a = rowNumbers;
		Arrays.sort(a);
		return a;
	}

	/**
	 * Gets row number given the row handle.
	 *
	 * @return The row number of the selected row object.
	 */
	public int getRowNumber(Object rowHandle) {
		return HandleAdapterFactory.getInstance().getRowHandleAdapter(rowHandle).getRowNumber();
	}

	/**
	 * Gets column numbers of selected columns.. And sorts the array of ints into
	 * ascending numerical order.
	 */
	public int[] getColumnNumbers() {
		List columnHandles = getColumnHandles();
		if (columnHandles.isEmpty()) {
			return new int[0];
		}
		int size = columnHandles.size();
		int[] colNumbers = new int[size];

		for (int i = 0; i < size; i++) {
			colNumbers[i] = getColumnNumber(columnHandles.get(i));
		}

		// sorts array before returning.
		int[] a = colNumbers;
		Arrays.sort(a);
		return a;
	}

	/**
	 * Gets column number given the column handle.
	 *
	 * @return the column number
	 */
	public int getColumnNumber(Object columnHandle) {
		return HandleAdapterFactory.getInstance().getColumnHandleAdapter(columnHandle).getColumnNumber();
	}

	/**
	 * Gets models of selected elements
	 *
	 * @return
	 */
	protected List getElementHandles() {
		return InsertInLayoutUtil.editPart2Model(TableUtil.filletCellInSelectionEditorpart(getSelection())).toList();
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
