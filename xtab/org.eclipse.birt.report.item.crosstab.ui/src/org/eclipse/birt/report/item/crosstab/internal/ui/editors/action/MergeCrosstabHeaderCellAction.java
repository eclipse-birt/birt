/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.item.crosstab.internal.ui.editors.action;

import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabCellHandle;
import org.eclipse.birt.report.item.crosstab.core.util.CrosstabUtil;
import org.eclipse.birt.report.item.crosstab.internal.ui.editors.model.CrosstabAdaptUtil;
import org.eclipse.birt.report.item.crosstab.ui.i18n.Messages;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.extension.ExtendedElementException;

/**
 * 
 */

public class MergeCrosstabHeaderCellAction extends AbstractCrosstabAction {
	public static final String ID = "org.eclipse.birt.report.item.crosstab.internal.ui.editors.action.MergeCrosstabHeaderCellAction"; //$NON-NLS-1$
	private static final String NAME = Messages.getString("MergeCrosstabHeaderCellAction_name"); //$NON-NLS-1$
	private CrosstabCellHandle cellHandle;

	public MergeCrosstabHeaderCellAction(DesignElementHandle handle) {
		super(handle);

		setId(ID);
		setText(NAME);

		ExtendedItemHandle extendedHandle = CrosstabAdaptUtil.getExtendedItemHandle(handle);
		setHandle(extendedHandle);
		try {
			cellHandle = (CrosstabCellHandle) extendedHandle.getReportItem();
		} catch (ExtendedElementException e) {
			cellHandle = null;
		}
	}

	@Override
	public void run() {
		transStar(NAME);
		CrosstabUtil.mergeCrosstabHeaderCell(cellHandle.getCrosstab());
		transEnd();
	}

	@Override
	public boolean isEnabled() {
		if (cellHandle == null) {
			return false;
		}
		if (DEUtil.isReferenceElement(cellHandle.getCrosstab().getModelHandle())) {
			return false;
		}

		return CrosstabUtil.canMergeCrosstabHeaderCell(cellHandle.getCrosstab());
	}

}
