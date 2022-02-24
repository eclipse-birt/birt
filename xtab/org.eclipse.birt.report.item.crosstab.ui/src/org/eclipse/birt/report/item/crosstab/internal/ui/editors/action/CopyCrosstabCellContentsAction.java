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

package org.eclipse.birt.report.item.crosstab.internal.ui.editors.action;

import org.eclipse.birt.report.designer.internal.ui.views.actions.AbstractViewAction;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.util.DNDUtil;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabCellHandle;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.gef.ui.actions.Clipboard;

/**
 * 
 */

public class CopyCrosstabCellContentsAction extends AbstractViewAction {

	public static final String ID = CopyCrosstabCellContentsAction.class.getName();

	public CopyCrosstabCellContentsAction(Object selectedObject) {
		super(selectedObject, Messages.getString("CopyCellContentsContextAction.actionText")); //$NON-NLS-1$
		setId(ID);
	}

	public void run() {
		Object cloneElements = null;
		if (getSelection() instanceof ExtendedItemHandle) {
			PropertyHandle container = ((ExtendedItemHandle) getSelection()).getPropertyHandle("content"); //$NON-NLS-1$
			cloneElements = DNDUtil.cloneSource(container.getContents().toArray());
		}
		if (getSelection() instanceof CrosstabCellHandle) {
			cloneElements = ((CrosstabCellHandle) getSelection()).getContents().toArray();
		}
		if (cloneElements != null) {
			Clipboard.getDefault().setContents(cloneElements);
		}
	}

	public boolean isEnabled() {
		if (canCopy(getSelection()))
			return super.isEnabled();
		return false;
	}

	private boolean canCopy(Object selection) {
		if (selection instanceof ExtendedItemHandle)
			return ((ExtendedItemHandle) selection).getPropertyHandle("content") //$NON-NLS-1$
					.getContentCount() > 0;
		if (selection instanceof CrosstabCellHandle)
			return ((CrosstabCellHandle) selection).getContents().size() > 0;
		return false;
	}
}
