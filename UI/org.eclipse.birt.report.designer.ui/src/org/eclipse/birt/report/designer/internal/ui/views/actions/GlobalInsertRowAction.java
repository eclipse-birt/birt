/*******************************************************************************
* Copyright (c) 2004 Actuate Corporation.
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v2.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-2.0.html
*
* Contributors:
*  Actuate Corporation  - initial API and implementation
*******************************************************************************/
package org.eclipse.birt.report.designer.internal.ui.views.actions;

import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.model.api.LibraryHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.RowHandle;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.StructuredSelection;

/**
 * 
 */

public class GlobalInsertRowAction extends AbstractGlobalSelectionAction {
	private String pos;

	/**
	 * @param provider The selection provider
	 * @param id       The id of the action
	 * @param pos      the insert position
	 */
	public GlobalInsertRowAction(ISelectionProvider provider, String id, String pos) {
		super(provider, id);
		this.pos = pos;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.ui.actions.AbstractGlobalSelectionAction#run()
	 */
	public void run() {
		new InsertAction(((StructuredSelection) getSelection()).toArray()[0],
				Messages.getString("RowProvider.action.text.above"), //$NON-NLS-1$
				ReportDesignConstants.ROW_ELEMENT, pos).run();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.ui.actions.WorkbenchPartAction#calculateEnabled()
	 */
	protected boolean calculateEnabled() {
		if (((StructuredSelection) getSelection()).toArray().length != 1
				|| ((StructuredSelection) getSelection()).toArray()[0] == null
				|| ((StructuredSelection) getSelection()).toArray()[0] instanceof RowHandle == false) {
			return false;
		}

		if (((StructuredSelection) getSelection()).toArray()[0] instanceof RowHandle
				&& ((RowHandle) (((StructuredSelection) getSelection()).toArray()[0]))
						.getRoot() instanceof LibraryHandle
				&& SessionHandleAdapter.getInstance().getReportDesignHandle() instanceof ReportDesignHandle) {
			return false;
		}
		return new InsertAction(((StructuredSelection) getSelection()).toArray()[0],
				Messages.getString("RowProvider.action.text.above"), //$NON-NLS-1$
				ReportDesignConstants.ROW_ELEMENT, pos).isEnabled();
	}

}
