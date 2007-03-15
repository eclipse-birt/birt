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

package org.eclipse.birt.report.model.css;

import org.eclipse.birt.report.model.api.SharedStyleHandle;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.elements.ReportDesign;
import org.eclipse.birt.report.model.elements.Style;
import org.eclipse.birt.report.model.elements.Theme;
import org.eclipse.birt.report.model.elements.interfaces.IReportDesignModel;
import org.eclipse.birt.report.model.elements.interfaces.IThemeModel;

/**
 * This class represents a shared css style which can't be modified.
 * 
 */

public class CssStyle extends Style
{
	private CssStyleSheet sheet;
	
	/**
	 * Set css style container.
	 * @param obj
	 */
	
	public void setContainer( DesignElement obj )
	{
		if ( obj instanceof ReportDesign )
		{
			super.setContainer( obj, IReportDesignModel.CSSES_PROP );
		}
		else if ( obj instanceof Theme )
		{
			super.setContainer( obj, IThemeModel.CSSES_PROP );
		}
	}

	/**
	 * Default constructor.
	 */

	public CssStyle( )
	{
	}

	/**
	 * Constructs the css style element with an optional name.
	 * 
	 * @param theName
	 *            the optional name
	 */

	public CssStyle( String theName )
	{
		super( theName );
	}

	/**
	 * Returns an API handle for this element.
	 * 
	 * @param module
	 *            the report design of the style
	 * 
	 * @return an API handle for this element
	 */

	public SharedStyleHandle handle( Module module )
	{
		if ( handle == null )
		{
			handle = new SharedStyleHandle( module, this );
		}
		return (SharedStyleHandle) handle;
	}

	/**
	 * Gets css style sheet.
	 * @return css style sheet.
	 */
	
	public CssStyleSheet getCssStyleSheet( )
	{
		return sheet;
	}

	/**
	 * Set css style sheet.
	 * @param sheet
	 */
	
	public void setCssStyleSheet( CssStyleSheet sheet )
	{
		this.sheet = sheet;
	}

}

