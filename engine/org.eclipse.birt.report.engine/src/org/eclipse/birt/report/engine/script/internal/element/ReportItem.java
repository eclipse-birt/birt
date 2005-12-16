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
import org.eclipse.birt.report.engine.api.script.element.IReportItem;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.DimensionHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;

public class ReportItem extends ReportElement implements IReportItem
{

	public ReportItem( ReportItemHandle handle )
	{
		super( handle );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.script.element.IReportItem#getDataSet()
	 */

	public DataSetHandle getDataSet( )
	{
		return ( ( ReportItemHandle ) handle ).getDataSet( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.script.element.IReportItem#setDataSet(org.eclipse.birt.report.model.api.DataSetHandle)
	 */

	public void setDataSet( DataSetHandle dataSet ) throws ScriptException
	{
		try
		{
			( ( ReportItemHandle ) handle ).setDataSet( dataSet );
		} catch ( SemanticException e )
		{
			throw new ScriptException( e.getLocalizedMessage( ) );
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.script.element.IReportItem#getX()
	 */

	public String getX( )
	{
		DimensionHandle x = ( ( ReportItemHandle ) handle ).getX( );
		return ( x == null ? null : x.getStringValue( ) );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.script.element.IReportItem#getY()
	 */

	public String getY( )
	{
		DimensionHandle y = ( ( ReportItemHandle ) handle ).getY( );
		return ( y == null ? null : y.getStringValue( ) );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.script.element.IReportItem#setX(java.lang.String)
	 */

	public void setX( String dimension ) throws ScriptException
	{
		try
		{
			( ( ReportItemHandle ) handle ).setX( dimension );
		} catch ( SemanticException e )
		{
			throw new ScriptException( e.getLocalizedMessage( ) );
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.script.element.IReportItem#setX(double)
	 */

	public void setX( double dimension ) throws ScriptException
	{
		try
		{
			( ( ReportItemHandle ) handle ).setX( dimension );
		} catch ( SemanticException e )
		{
			throw new ScriptException( e.getLocalizedMessage( ) );
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.script.element.IReportItem#setY(java.lang.String)
	 */

	public void setY( String dimension ) throws ScriptException
	{
		try
		{
			( ( ReportItemHandle ) handle ).setY( dimension );
		} catch ( SemanticException e )
		{
			throw new ScriptException( e.getLocalizedMessage( ) );
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.script.element.IReportItem#setY(double)
	 */

	public void setY( double dimension ) throws ScriptException
	{
		try
		{
			( ( ReportItemHandle ) handle ).setY( dimension );
		} catch ( SemanticException e )
		{
			throw new ScriptException( e.getLocalizedMessage( ) );
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.script.element.IReportItem#setHeight(java.lang.String)
	 */

	public void setHeight( String dimension ) throws ScriptException
	{
		try
		{
			( ( ReportItemHandle ) handle ).setHeight( dimension );
		} catch ( SemanticException e )
		{
			throw new ScriptException( e.getLocalizedMessage( ) );
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.script.element.IReportItem#setHeight(double)
	 */

	public void setHeight( double dimension ) throws ScriptException
	{
		try
		{
			( ( ReportItemHandle ) handle ).setHeight( dimension );
		} catch ( SemanticException e )
		{
			throw new ScriptException( e.getLocalizedMessage( ) );
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.script.element.IReportItem#setWidth(java.lang.String)
	 */

	public void setWidth( String dimension ) throws ScriptException
	{
		try
		{
			( ( ReportItemHandle ) handle ).setWidth( dimension );
		} catch ( SemanticException e )
		{
			throw new ScriptException( e.getLocalizedMessage( ) );
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.script.element.IReportItem#setWidth(double)
	 */

	public void setWidth( double dimension ) throws ScriptException
	{
		try
		{
			( ( ReportItemHandle ) handle ).setWidth( dimension );
		} catch ( SemanticException e )
		{
			throw new ScriptException( e.getLocalizedMessage( ) );
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.script.element.IReportItem#getWidth()
	 */

	public String getWidth( )
	{
		return ( ( ReportItemHandle ) handle ).getWidth( ).getDisplayValue( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.script.element.IReportItem#getHeight()
	 */
	public String getHeight( )
	{
		return ( ( ReportItemHandle ) handle ).getHeight( ).getDisplayValue( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.script.element.IReportItem#getBookmark()
	 */

	public String getBookmark( )
	{
		return ( ( ReportItemHandle ) handle ).getBookmark( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.script.element.IReportItem#setBookmark(java.lang.String)
	 */

	public void setBookmark( String value ) throws ScriptException
	{
		try
		{
			( ( ReportItemHandle ) handle ).setBookmark( value );
		} catch ( SemanticException e )
		{
			throw new ScriptException( e.getLocalizedMessage( ) );
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.script.element.IReportItem#setTocExpression(java.lang.String)
	 */

	public void setTocExpression( String expression ) throws ScriptException
	{
		try
		{
			( ( ReportItemHandle ) handle ).setTocExpression( expression );
		} catch ( SemanticException e )
		{
			throw new ScriptException( e.getLocalizedMessage( ) );
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.script.element.IReportItem#getTocExpression()
	 */

	public String getTocExpression( )
	{
		return ( ( ReportItemHandle ) handle ).getTocExpression( );
	}

}
