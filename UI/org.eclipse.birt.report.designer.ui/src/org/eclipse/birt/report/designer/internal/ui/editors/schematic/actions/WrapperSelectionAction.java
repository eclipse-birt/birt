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

import org.eclipse.birt.report.designer.internal.ui.dnd.InsertInLayoutUtil;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.TableUtil;
import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchPart;

/**
 * Wrapper class of action handler.
 */

public abstract class WrapperSelectionAction extends SelectionAction {

	protected IAction actionHandler;

	/**
	 * Constructor
	 * 
	 * @param part
	 */
	public WrapperSelectionAction(IWorkbenchPart part) {
		super(part);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.ui.actions.WorkbenchPartAction#calculateEnabled()
	 */
	protected boolean calculateEnabled() {
		if (actionHandler == null) {
			return false;
		}
		return actionHandler.isEnabled();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.action.Action#run()
	 */
	public void run() {
		actionHandler.run();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.ui.actions.SelectionAction#handleSelectionChanged()
	 */
	protected void handleSelectionChanged() {
		ISelection model = InsertInLayoutUtil.editPart2Model(TableUtil.filletCellInSelectionEditorpart(getSelection()));
		if (model.isEmpty()) {
			actionHandler = null;
		} else {
			actionHandler = createActionHandler(model);
		}
		super.handleSelectionChanged();
	}

	/**
	 * Creates action handler. All action operation will use this action handler.
	 * 
	 * @param model operation handler
	 * @return action handler
	 */
	abstract protected IAction createActionHandler(ISelection model);
}