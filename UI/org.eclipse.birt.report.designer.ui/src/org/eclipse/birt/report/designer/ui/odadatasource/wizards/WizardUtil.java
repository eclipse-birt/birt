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

package org.eclipse.birt.report.designer.ui.odadatasource.wizards;

import org.eclipse.jface.action.IMenuManager;

/**
 * WizardUtil
 * 
 * @deprecated this class is no longer used and will be removed in near future.
 */
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
	}

	/**
	 * Typically called from the Data View Node to create Data Source Edit menu item
	 * 
	 */
	public static void createEditDataSourceMenu(IMenuManager menu, Object selectedObject) {
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
	}

	/**
	 * Typically called from the Data View Node provider to create Data Set menu
	 * item
	 */
	public static void createEditDataSetMenu(IMenuManager menu, Object selectedObject) {
	}
}
