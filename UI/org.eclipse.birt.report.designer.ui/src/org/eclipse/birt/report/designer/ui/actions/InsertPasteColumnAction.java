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

import org.eclipse.birt.report.designer.internal.ui.util.Policy;
import org.eclipse.birt.report.designer.internal.ui.views.actions.AbstractViewAction;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.util.DNDUtil;
import org.eclipse.birt.report.model.api.ColumnBandData;
import org.eclipse.birt.report.model.api.ColumnHandle;
import org.eclipse.gef.ui.actions.Clipboard;
import org.eclipse.jface.viewers.StructuredSelection;

/**
 * Action for inserting pasted column
 */

public class InsertPasteColumnAction extends AbstractViewAction {

	private static final String DEFAULT_TEXT = Messages.getString("InsertPasteColumnAction.text"); //$NON-NLS-1$

	public InsertPasteColumnAction(Object selectedObject) {
		super(selectedObject, DEFAULT_TEXT);
	}

	/*
	 * Returns whether the InsertPasteColumn Action is enabled
	 * 
	 * @see org.eclipse.jface.action.Action#isEnabled()
	 */
	public boolean isEnabled() {
		return getClipBoardContents() instanceof ColumnBandData && getSelection() instanceof ColumnHandle
				&& DNDUtil.handleValidateContainColumnPaste((ColumnHandle) getSelection(),
						(ColumnBandData) getClipBoardContents(), true);
	}

	protected Object getClipBoardContents() {
		Object obj = Clipboard.getDefault().getContents();
		if (obj instanceof Object[]) {
			return ((Object[]) obj)[0];
		}
		return obj;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.designer.internal.ui.views.actions.AbstractViewAction
	 * #getSelection()
	 */
	public Object getSelection() {
		Object selection = super.getSelection();
		if (selection instanceof StructuredSelection) {
			selection = ((StructuredSelection) selection).getFirstElement();
		}
		return selection;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.action.Action#run()
	 */
	public void run() {
		if (Policy.TRACING_ACTIONS) {
			System.out.println("Insert paste column action >> Run ..."); //$NON-NLS-1$
		}
		DNDUtil.insertPasteColumn(getClipBoardContents(), getSelection());
	}
}