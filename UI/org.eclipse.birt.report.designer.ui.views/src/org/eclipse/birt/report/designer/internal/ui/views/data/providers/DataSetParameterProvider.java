/*******************************************************************************
* Copyright (c) 2004,2005 Actuate Corporation.
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v2.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-2.0.html
*
* Contributors:
*  Actuate Corporation  - initial API and implementation
*******************************************************************************/
package org.eclipse.birt.report.designer.internal.ui.views.data.providers;

import org.eclipse.birt.report.designer.internal.ui.views.DefaultNodeProvider;
import org.eclipse.birt.report.designer.ui.IReportGraphicConstants;
import org.eclipse.birt.report.model.api.DataSetParameterHandle;

/**
 * 
 */

public class DataSetParameterProvider extends DefaultNodeProvider {

	/**
	 * Returns the right ICON name constant of given element
	 * 
	 * @param model
	 * @return icon name
	 */
	public String getIconName(Object model) {
		return IReportGraphicConstants.ICON_ELEMENT_PARAMETER;
	}

	/**
	 * Gets the children element of the given model using visitor
	 * 
	 * @param object the handle
	 */
	public Object[] getChildren(Object object) {
		return new DataSetParameterHandle[] {};
	}

	/*
	 * (non-Javadoc
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.views.INodeProvider#
	 * getNodeDisplayName(java.lang.Object)
	 */
	public String getNodeDisplayName(Object model) {
		return ((DataSetParameterHandle) model).getName();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.designer.internal.ui.views.INodeProvider#hasChildren(
	 * java.lang.Object)
	 */
	public boolean hasChildren(Object object) {
		// Optimize expand time
		return false;
	}

}
