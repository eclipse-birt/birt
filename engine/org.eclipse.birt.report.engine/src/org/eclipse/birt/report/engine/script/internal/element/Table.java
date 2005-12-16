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
import org.eclipse.birt.report.engine.api.script.element.ITable;
import org.eclipse.birt.report.model.api.TableHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;

public class Table extends ReportItem implements ITable
{

	public Table( TableHandle table )
	{
		super( table );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.script.element.ITable#getColumnCount()
	 */

	public int getColumnCount( )
	{
		return ( ( TableHandle ) handle ).getColumnCount( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.script.element.ITable#repeatHeader()
	 */

	public boolean repeatHeader( )
	{
		return ( ( TableHandle ) handle ).repeatHeader( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.script.element.ITable#setRepeatHeader(boolean)
	 */

	public void setRepeatHeader( boolean value ) throws ScriptException
	{
		try
		{
			( ( TableHandle ) handle ).setRepeatHeader( value );
		} catch ( SemanticException e )
		{
			throw new ScriptException( e.getLocalizedMessage( ) );
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.script.element.ITable#getCaption()
	 */

	public String getCaption( )
	{
		return ( ( TableHandle ) handle ).getCaption( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.script.element.ITable#setCaption(java.lang.String)
	 */

	public void setCaption( String caption ) throws ScriptException
	{
		try
		{
			( ( TableHandle ) handle ).setCaption( caption );
		} catch ( SemanticException e )
		{
			throw new ScriptException( e.getLocalizedMessage( ) );
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.script.element.ITable#getCaptionKey()
	 */

	public String getCaptionKey( )
	{
		return ( ( TableHandle ) handle ).getCaptionKey( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.script.element.ITable#setCaptionKey(java.lang.String)
	 */

	public void setCaptionKey( String captionKey ) throws ScriptException
	{
		try
		{
			( ( TableHandle ) handle ).setCaptionKey( captionKey );
		} catch ( SemanticException e )
		{
			throw new ScriptException( e.getLocalizedMessage( ) );
		}
	}

}
