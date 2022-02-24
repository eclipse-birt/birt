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

package org.eclipse.birt.report.designer.ui.editors.schematic.action;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.ui.actions.MenuUpdateAction;
import org.eclipse.birt.report.model.api.MasterPageHandle;

/**
 * Master page set action
 */

public class MasterPageAction extends MenuUpdateAction {

	public MasterPageAction() {
		super(null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.ui.actions.MenuUpdateAction#getItems()
	 */
	public List getItems() {
		ArrayList list = new ArrayList();
		for (Iterator iter = SessionHandleAdapter.getInstance().getReportDesignHandle().getMasterPages()
				.iterator(); iter.hasNext();) {
			list.add(new MasterPageSelectionAction((MasterPageHandle) iter.next()));
		}
		return list;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.ui.actions.SelectionAction#update()
	 */
	public void update() {
	}

}
