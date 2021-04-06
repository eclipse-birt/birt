/*******************************************************************************
 * Copyright (c) 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.designer.ui.cubebuilder.provider;

import java.util.ArrayList;

import org.eclipse.birt.report.designer.internal.ui.views.outline.providers.ReportDesignNodeProvider;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.viewers.TreeViewer;

/**
 * Root node - Report design node provider - Implements the getChildren -
 * Implements the getNodeDiplayName
 * 
 * 
 */
public class ReportCubesNodeProvider extends ReportDesignNodeProvider {

	/**
	 * Gets the children of the given model. The default children element include
	 * following: Body,Styles,MasterPage
	 * 
	 * @param model the given report design
	 * @return the result list that contains the model
	 */
	public Object[] getChildren(Object model) {

		// Report design may not be the current, use model to get.
		ReportDesignHandle handle = ((ReportDesignHandle) model);
		ArrayList list = new ArrayList();

		list.add(handle.getCubes());

		return list.toArray();
	}

	public void createContextMenu(TreeViewer sourceViewer, Object object, IMenuManager menu) {
	}

}