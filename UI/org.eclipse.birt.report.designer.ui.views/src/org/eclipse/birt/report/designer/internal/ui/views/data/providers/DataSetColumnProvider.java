/*************************************************************************************
 * Copyright (c) 2004 Actuate Corporation and others.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Actuate Corporation - Initial implementation.
 ************************************************************************************/

package org.eclipse.birt.report.designer.internal.ui.views.data.providers;

import org.eclipse.birt.report.designer.core.model.views.data.DataSetItemModel;
import org.eclipse.birt.report.designer.internal.ui.views.DefaultNodeProvider;
import org.eclipse.birt.report.designer.ui.IReportGraphicConstants;

/**
 * Deals with dataset column
 *
 */
public class DataSetColumnProvider extends DefaultNodeProvider {

	/**
	 * Returns the right ICON name constant of given elemen
	 *
	 * @param model
	 * @return icon name
	 */
	@Override
	public String getIconName(Object model) {
		return IReportGraphicConstants.ICON_DATA_COLUMN;
	}

	/**
	 * Gets the children element of the given model using visitor
	 *
	 * @param object the handle
	 */
	@Override
	public Object[] getChildren(Object object) {
		return new DataSetItemModel[] {};
	}

	/*
	 * (non-Javadoc
	 *
	 * @see org.eclipse.birt.report.designer.internal.ui.views.INodeProvider#
	 * getNodeDisplayName(java.lang.Object)
	 */
	@Override
	public String getNodeDisplayName(Object model) {
		return ((DataSetItemModel) model).getDisplayName();
	}
}
