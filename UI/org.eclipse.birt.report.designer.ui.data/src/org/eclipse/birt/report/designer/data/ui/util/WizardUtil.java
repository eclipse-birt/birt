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

package org.eclipse.birt.report.designer.data.ui.util;

import org.eclipse.birt.report.designer.data.ui.actions.EditDataSetAction;
import org.eclipse.birt.report.designer.data.ui.actions.EditDataSourceAction;
import org.eclipse.birt.report.designer.data.ui.actions.NewDataSetAction;
import org.eclipse.birt.report.designer.data.ui.actions.NewDataSourceAction;
import org.eclipse.birt.report.designer.data.ui.actions.NewJointDataSetAction;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.ui.IWorkbenchActionConstants;

public class WizardUtil {

	/**
	 * Typically called from the Data View Node provider to create Data Source menu
	 * It creates pop up menu items, with each item being an available data source
	 * 
	 * @param menu     The menu Item to which all the other menu items are to be
	 *                 added
	 * @param provider The Selection Provider for the View
	 */
	public static void createNewDataSourceMenus(IMenuManager menu) {
		NewDataSourceAction action = new NewDataSourceAction(Messages.getString("datasource.new")); //$NON-NLS-1$
		menu.add(action);
	}

	/**
	 * Typically called from the Data View Node to create Data Source Edit menu item
	 * 
	 */
	public static void createEditDataSourceMenu(IMenuManager menu, Object selectedObject) {
		EditDataSourceAction dataSourceAction = new EditDataSourceAction(selectedObject);
		dataSourceAction.setText(Messages.getString("datasource.action.edit"));//$NON-NLS-1$
		menu.insertAfter(IWorkbenchActionConstants.MB_ADDITIONS, dataSourceAction);
	}

	/**
	 * Typically called from the Data View Node provider to create Data set menu
	 * Currently just adding the jdbc data set
	 * 
	 * @param menu     The menu Item to which all the other menu items are to be
	 *                 added
	 * @param provider The Selection Provider for the View
	 */
	public static void createNewDataSetMenu(IMenuManager menu) {
		NewDataSetAction action = new NewDataSetAction(Messages.getString("dataset.action.new"));//$NON-NLS-1$
		menu.add(action);

		NewJointDataSetAction joinaction = new NewJointDataSetAction(Messages.getString("dataset.action.join.new"));//$NON-NLS-1$
		menu.add(joinaction);
	}

	/**
	 * Typically called from the Data View Node provider to create Data Set menu
	 * item
	 */
	public static void createEditDataSetMenu(IMenuManager menu, Object selectedObject) {
		menu.insertAfter(IWorkbenchActionConstants.MB_ADDITIONS,
				new EditDataSetAction(selectedObject, Messages.getString("DataSetNodeProvider.menu.text"))); //$NON-NLS-1$
	}
}
