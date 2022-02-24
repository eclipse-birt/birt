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

import org.eclipse.birt.report.designer.internal.ui.views.actions.InsertAction;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.model.api.GroupHandle;
import org.eclipse.birt.report.model.api.ReportElementHandle;
import org.eclipse.birt.report.model.api.SlotHandle;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.jface.action.Action;

/**
 * 
 */

public class InsertGroupHeaderFooterAction extends Action {

	// private static final String STACK_MSG_INSERT_GROUP_HEADER_FOOTER =
	// Messages.getString( "InsertGroupHeaderFooterAction.stackMsg.editGroup" );
	// //$NON-NLS-1$

	public static final String ID = "org.eclipse.birt.report.designer.internal.ui.editors.schematic.actions.InsertGroupHeaderFooterAction"; //$NON-NLS-1$

	protected GroupHandle handle;

	public static final int HEADER = 1;

	public static final int FOOTER = 2;

	public static final String INSERT_HEADER_TEXT = Messages.getString("InsertGroupHeaderFooterAction.Text.Header"); //$NON-NLS-1$

	public static final String INSERT_FOOTER_TEXT = Messages.getString("InsertGroupHeaderFooterAction.Text.Footer"); //$NON-NLS-1$

	private SlotHandle slotHandle;

	private InsertAction insertAction;

	/**
	 * 
	 */
	public InsertGroupHeaderFooterAction(GroupHandle grouphandle, int type) {
		// TODO Auto-generated constructor stub
		super();
		setId(ID);
		handle = grouphandle;
		if (handle == null) {
			slotHandle = null;
			setText(Messages.getString("NoneAction.text")); //$NON-NLS-1$
			return;
		}
		switch (type) {
		case HEADER:
			slotHandle = handle.getHeader();
			setText(INSERT_HEADER_TEXT);
			break;
		case FOOTER:
			slotHandle = handle.getFooter();
			setText(INSERT_FOOTER_TEXT);
			break;
		default:
			slotHandle = null;
		}

	}

	/*
	 * (non-Javadoc) Method declared on IAction.
	 */
	public boolean isEnabled() {
		// update later
		if (handle == null || slotHandle == null) {
			return false;
		}
		if (slotHandle.canContain(ReportDesignConstants.ROW_ELEMENT) && slotHandle.getCount() == 0) {
			SlotHandle model = slotHandle;
			if (((ReportElementHandle) model.getElementHandle()).isValidLayoutForCompoundElement()) {
				insertAction = new InsertAction(model, "", //$NON-NLS-1$
						ReportDesignConstants.ROW_ELEMENT);
				return insertAction.isEnabled();
			}
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.action.Action#run()
	 */
	public void run() {
		if (insertAction != null) {
			insertAction.run();
		}

	}

}
