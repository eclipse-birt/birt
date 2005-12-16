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

package org.eclipse.birt.report.engine.script.internal.element;

import org.eclipse.birt.report.engine.api.script.ScriptException;
import org.eclipse.birt.report.engine.api.script.element.IRow;
import org.eclipse.birt.report.model.api.DimensionHandle;
import org.eclipse.birt.report.model.api.RowHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;

public class Row extends ReportElement implements IRow
{

	public Row( RowHandle row )
	{
		super( row );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.script.element.IRow#getHeight()
	 */

	public String getHeight( )
	{
		DimensionHandle height = ( ( RowHandle ) handle ).getHeight( );
		return ( height == null ? null : height.getStringValue( ) );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.script.element.IRow#getBookmark()
	 */

	public String getBookmark( )
	{
		return ( ( RowHandle ) handle ).getBookmark( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.script.element.IRow#setBookmark(java.lang.String)
	 */

	public void setBookmark( String value ) throws ScriptException
	{
		try
		{
			( ( RowHandle ) handle ).setBookmark( value );
		} catch ( SemanticException e )
		{
			throw new ScriptException( e.getLocalizedMessage( ) );
		}
	}
}
