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

import org.eclipse.birt.report.designer.internal.ui.dnd.DNDLocation;
import org.eclipse.birt.report.designer.internal.ui.dnd.DNDService;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ViewerDropAdapter;
import org.eclipse.swt.dnd.DND;
import org.eclipse.ui.IWorkbenchPart;

/**
 * 
 */

public class InsertAggregationAction extends BaseInsertMenuAction {

	public static final String TEXT = Messages.getString("InsertAggregationAction.text"); //$NON-NLS-1$

	public static final String ID = "InsertAggregationAction"; //$NON-NLS-1$

	private static final String TYPE = "DATA_AGG"; //$NON-NLS-1$

	public InsertAggregationAction(IWorkbenchPart part) {
		super(part, TYPE);
		setId(ID);
	}

	public void run() {
		DNDService.getInstance().performDrop(TYPE, ((IStructuredSelection) getSelection()).getFirstElement(),
				DND.DROP_DEFAULT, new DNDLocation(ViewerDropAdapter.LOCATION_ON));
	}

	protected boolean calculateEnabled() {
		if (getSelection() instanceof IStructuredSelection)
			return DNDService.getInstance().validDrop(TYPE, ((IStructuredSelection) getSelection()).getFirstElement(),
					DND.DROP_DEFAULT, new DNDLocation(ViewerDropAdapter.LOCATION_ON));
		return false;
	}

}
