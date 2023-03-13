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

import java.util.List;

import org.eclipse.birt.report.designer.ui.cubebuilder.action.EditCubeAction;
import org.eclipse.birt.report.designer.ui.extensions.IMenuBuilder;
import org.eclipse.birt.report.item.crosstab.core.ICrosstabConstants;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabReportItemHandle;
import org.eclipse.birt.report.item.crosstab.core.util.CrosstabUtil;
import org.eclipse.birt.report.item.crosstab.ui.i18n.Messages;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.jface.action.IMenuManager;

/**
 * Creata the Cross tab menu
 */
public class CrosstabMenuBuilder implements IMenuBuilder {

	// private static final String EDITCUBE_NAME = "Edit Data Cube";
	private static final String EDITCUBE_NAME = Messages.getString("CrosstabMenuBuilder.EditCubeAction.DisplayName");//$NON-NLS-1$

	/**
	 * Constructor
	 */
	public CrosstabMenuBuilder() {
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.designer.ui.extensions.IMenuBuilder#buildMenu(org.
	 * eclipse.jface.action.IMenuManager, java.util.List)
	 */
	@Override
	public void buildMenu(IMenuManager menu, List selectedList) {
		if (selectedList != null && selectedList.size() == 1 && selectedList.get(0) instanceof ExtendedItemHandle) {
			// for ctross tab test
			ExtendedItemHandle handle = (ExtendedItemHandle) selectedList.get(0);
			if (ICrosstabConstants.CROSSTAB_EXTENSION_NAME.equals(handle.getExtensionName())) {
				CrosstabReportItemHandle reportHandle = (CrosstabReportItemHandle) CrosstabUtil.getReportItem(handle);
				EditCubeAction action = new EditCubeAction(reportHandle.getCube(), EDITCUBE_NAME);
				menu.add(action);
			}
		}

	}

}
