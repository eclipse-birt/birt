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

package org.eclipse.birt.report.item.crosstab.internal.ui.editors.editparts;

import java.util.List;

import org.eclipse.birt.report.designer.internal.ui.dnd.InsertInLayoutUtil;
import org.eclipse.birt.report.item.crosstab.internal.ui.editors.action.AddLevelHandleAction;
import org.eclipse.birt.report.item.crosstab.internal.ui.editors.action.AddSubTotalAction;
import org.eclipse.birt.report.item.crosstab.internal.ui.editors.action.DeleteDimensionViewHandleAction;
import org.eclipse.birt.report.item.crosstab.internal.ui.editors.model.CrosstabCellAdapter;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.gef.ContextMenuProvider;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.viewers.ISelection;

/**
 * When mouse click the triangle, popup the menu, this class provider the menu.
 */
public class LevelCrosstabPopMenuProvider extends ContextMenuProvider {

	/**
	 * Constructor
	 *
	 * @param viewer
	 */
	public LevelCrosstabPopMenuProvider(EditPartViewer viewer) {
		super(viewer);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.gef.ContextMenuProvider#buildContextMenu(org.eclipse.jface.action
	 * .IMenuManager)
	 */
	@Override
	public void buildContextMenu(IMenuManager menu) {
		if (getElements().size() != 1) {
			return;
		}
		Object firstSelectedElement = getFirstElement();
		DesignElementHandle element = null;
		if (firstSelectedElement instanceof DesignElementHandle) {
			element = (DesignElementHandle) firstSelectedElement;
		} else if (firstSelectedElement instanceof CrosstabCellAdapter) {
			element = ((CrosstabCellAdapter) firstSelectedElement).getDesignElementHandle();
		}
		// if ( firstSelectedElement instanceof DesignElementHandle )
		if (element != null) {
			IAction action = new AddLevelHandleAction(element);
			// if (!CrosstabUtil.isBoundToLinkedDataSet( getCrosstab(element) ) )
			{
				menu.add(action);
			}

			action = new AddSubTotalAction(element);
			menu.add(action);

			action = new DeleteDimensionViewHandleAction(element);
			menu.add(action);
		}
	}

	/**
	 * Gets the current selection.
	 *
	 * @return The current selection
	 */
	protected ISelection getSelection() {
		return getViewer().getSelection();
	}

	/**
	 * Gets element handles.
	 *
	 * @return element handles
	 */
	protected List getElements() {
		return InsertInLayoutUtil.editPart2Model(getSelection()).toList();
	}

	/**
	 * Gets the current selected object.
	 *
	 * @return The current selected object array. If length is one, return the first
	 */
	protected Object getSelectedElement() {
		Object[] array = getElements().toArray();
		if (array.length == 1) {
			return array[0];
		}
		return array;
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
}
