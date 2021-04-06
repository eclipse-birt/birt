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

package org.eclipse.birt.report.designer.ui.actions;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.designer.internal.ui.dnd.InsertInLayoutUtil;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.actions.ApplyStyleAction;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.TableUtil;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.designer.util.DNDUtil;
import org.eclipse.birt.report.model.api.ColumnHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.RowHandle;
import org.eclipse.birt.report.model.api.SharedStyleHandle;
import org.eclipse.birt.report.model.api.StyleHandle;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchPart;

/**
 * Updates "Apply style" menu.
 */

public class ApplyStyleMenuAction extends MenuUpdateAction {

	public static final String ID = "apply style menu"; //$NON-NLS-1$

	/**
	 * @param part
	 */
	public ApplyStyleMenuAction(IWorkbenchPart part) {
		super(part);
		setId(ID);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.ui.actions.MenuUpdateAction#getItems()
	 */
	protected List getItems() {
		ArrayList actionList = new ArrayList();

		StyleHandle currentStyle = getStyleHandle();
		ApplyStyleAction reset = new ApplyStyleAction(null);
		reset.setSelection(getSelection());
		reset.setChecked(currentStyle == null);
		actionList.add(reset);
		actionList.add(null);// Adds separator

		Iterator iterator = DEUtil.getStyles();
		if (iterator != null) {
			while (iterator.hasNext()) {
				SharedStyleHandle handle = (SharedStyleHandle) iterator.next();
				if (!handle.isPredefined()) {
					ApplyStyleAction action = new ApplyStyleAction(handle);
					action.setSelection(getSelection());
					action.setChecked(currentStyle == handle);
					actionList.add(action);
				}
			}
		}
		return actionList;
	}

	private StyleHandle getStyleHandle() {
		IStructuredSelection selection = InsertInLayoutUtil.editPart2Model(getSelection());
		if (!selection.isEmpty()) {
			Object firstElement = DNDUtil.unwrapToModel(selection.getFirstElement());

			if (firstElement instanceof DesignElementHandle) {
				if (firstElement instanceof RowHandle || firstElement instanceof ColumnHandle) {
					selection = InsertInLayoutUtil
							.editPart2Model(TableUtil.filletCellInSelectionEditorpart(getSelection()));
				}

				List modelList = DNDUtil.unwrapToModel(selection.toList());

				SharedStyleHandle style = ((DesignElementHandle) firstElement).getStyle();
				for (Iterator iterator = modelList.iterator(); iterator.hasNext();) {
					Object obj = iterator.next();
					if (!(obj instanceof DesignElementHandle)) {
						return null;
					}

					if (((DesignElementHandle) obj).getStyle() != style) {
						return null;
					}
				}
				return style;
			}
		}
		return null;
	}
}