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

package org.eclipse.birt.report.model.api;

import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.metadata.IElementDefn;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.elements.interfaces.IReportItemThemeModel;

/**
 * Represents a report item theme in the library. Each theme contains some
 * number of styles.
 * 
 * 
 * @see org.eclipse.birt.report.model.elements.ReportItemTheme
 */

public class ReportItemThemeHandle extends AbstractThemeHandle
		implements
			IReportItemThemeModel
{

	/**
	 * Constructs the handle for a theme with the given design and element. The
	 * application generally does not create handles directly. Instead, it uses
	 * one of the navigation methods available on other element handles.
	 * 
	 * @param module
	 *            the module
	 * @param element
	 *            the model representation of the element
	 */

	public ReportItemThemeHandle( Module module, DesignElement element )
	{
		super( module, element );
	}

	/**
	 * Gets the type for this report item theme. The type for this theme is
	 * required. If it is not set, no style can be inserted to this theme. The
	 * type can be the predefined choices as one of the following:
	 * <ul>
	 * <li>REPORT_ITEM_THEME_TYPE_TABLE
	 * <li>REPORT_ITEM_THEME_TYPE_LIST
	 * <li>REPORT_ITEM_THEME_TYPE_GRID
	 * </ul>
	 * At the another side, the type can be extension name for the extended
	 * item, such as crosstab and chart and others. Generally, the type is the
	 * name of the element definition{@link IElementDefn#getName()} .
	 * 
	 * @return
	 */
	public String getType( )
	{
		return getStringProperty( TYPE_PROP );
	}

	/**
	 * Sets the type for this report item theme. The type for this theme is
	 * required. If it is not set, no style can be inserted to this theme. The
	 * type can be the predefined choices as one of the following:
	 * <ul>
	 * <li>REPORT_ITEM_THEME_TYPE_TABLE
	 * <li>REPORT_ITEM_THEME_TYPE_LIST
	 * <li>REPORT_ITEM_THEME_TYPE_GRID
	 * </ul>
	 * At the another side, the type can be extension name for the extended
	 * item, such as crosstab and chart and others. Generally, the type is the
	 * name of the element definition{@link IElementDefn#getName()} .
	 * 
	 * @param type
	 *            the type to set for this report item theme
	 */
	public void setType( String type ) throws SemanticException
	{
		setStringProperty( TYPE_PROP, type );
	}
}
