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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.engine.api.script.ScriptException;
import org.eclipse.birt.report.engine.api.script.element.IDataBinding;
import org.eclipse.birt.report.engine.api.script.element.IHighLightRule;
import org.eclipse.birt.report.engine.api.script.element.IReportItem;
import org.eclipse.birt.report.model.api.ComputedColumnHandle;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.DimensionHandle;
import org.eclipse.birt.report.model.api.HideRuleHandle;
import org.eclipse.birt.report.model.api.HighlightRuleHandle;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.elements.Style;
import org.eclipse.birt.report.model.elements.interfaces.IReportItemModel;

/**
 * Implements of ReportItem
 * 
 */

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
		return ( (ReportItemHandle) handle ).getDataSet( );
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
			( (ReportItemHandle) handle ).setDataSet( dataSet );
		}
		catch ( SemanticException e )
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
		DimensionHandle x = ( (ReportItemHandle) handle ).getX( );
		return ( x == null ? null : x.getStringValue( ) );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.script.element.IReportItem#getY()
	 */

	public String getY( )
	{
		DimensionHandle y = ( (ReportItemHandle) handle ).getY( );
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
			( (ReportItemHandle) handle ).setX( dimension );
		}
		catch ( SemanticException e )
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
			( (ReportItemHandle) handle ).setX( dimension );
		}
		catch ( SemanticException e )
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
			( (ReportItemHandle) handle ).setY( dimension );
		}
		catch ( SemanticException e )
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
			( (ReportItemHandle) handle ).setY( dimension );
		}
		catch ( SemanticException e )
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
			( (ReportItemHandle) handle ).setHeight( dimension );
		}
		catch ( SemanticException e )
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
			( (ReportItemHandle) handle ).setHeight( dimension );
		}
		catch ( SemanticException e )
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
			( (ReportItemHandle) handle ).setWidth( dimension );
		}
		catch ( SemanticException e )
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
			( (ReportItemHandle) handle ).setWidth( dimension );
		}
		catch ( SemanticException e )
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
		return ( (ReportItemHandle) handle ).getWidth( ).getDisplayValue( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.script.element.IReportItem#getHeight()
	 */
	public String getHeight( )
	{
		return ( (ReportItemHandle) handle ).getHeight( ).getDisplayValue( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.script.element.IReportItem#getBookmark()
	 */

	public String getBookmark( )
	{
		return ( (ReportItemHandle) handle ).getBookmark( );
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
			( (ReportItemHandle) handle ).setBookmark( value );
		}
		catch ( SemanticException e )
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
			( (ReportItemHandle) handle ).setTocExpression( expression );
		}
		catch ( SemanticException e )
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
		return ( (ReportItemHandle) handle ).getTocExpression( );
	}

	public String getColumnBinding( String bindingName )
	{
		if ( bindingName == null || bindingName.length( ) == 0 )
			return null;

		Iterator iterator = ( (ReportItemHandle) handle )
				.columnBindingsIterator( );
		while ( iterator.hasNext( ) )
		{
			ComputedColumnHandle columnHandle = (ComputedColumnHandle) iterator
					.next( );
			if ( columnHandle.getName( ).equals( bindingName ) )
			{
				return columnHandle.getExpression( );
			}
		}

		return null;
	}

	public IDataBinding[] getColumnBindings( )
	{
		Iterator iterator = ( (ReportItemHandle) handle )
				.columnBindingsIterator( );
		List rList = new ArrayList( );
		int count = 0;
		while ( iterator.hasNext( ) )
		{
			ComputedColumnHandle columnHandle = (ComputedColumnHandle) iterator
					.next( );
			DataBindingImpl d = new DataBindingImpl( columnHandle,
					(ReportItemHandle) handle );
			rList.add( d );
			++count;
		}

		return (IDataBinding[]) rList.toArray( new IDataBinding[count] );
	}

	public IHighLightRule[] getHighLightRule( )
	{
		PropertyHandle propHandle = ( (ReportItemHandle) handle )
				.getPropertyHandle( Style.HIGHLIGHT_RULES_PROP );
		Iterator iterator = propHandle.iterator( );

		List rList = new ArrayList( );
		int count = 0;

		while ( iterator.hasNext( ) )
		{
			HighlightRuleHandle ruleHandle = (HighlightRuleHandle) iterator
					.next( );
			HighLightRuleImpl h = new HighLightRuleImpl( ruleHandle,
					(ReportItemHandle) handle );
			rList.add( h );
			++count;
		}

		return (IHighLightRule[]) rList.toArray( new IHighLightRule[count] );
	}

	public void removeColumnBinding( String bindingName )
			throws ScriptException
	{
		if ( bindingName == null || bindingName.length( ) == 0 )
			return;

		PropertyHandle propHandle = ( (ReportItemHandle) handle )
				.getPropertyHandle( IReportItemModel.BOUND_DATA_COLUMNS_PROP );
		Iterator iterator = propHandle.iterator( );

		while ( iterator.hasNext( ) )
		{
			ComputedColumnHandle columnHandle = (ComputedColumnHandle) iterator
					.next( );
			if ( bindingName.equals( columnHandle.getName( ) ) )
			{
				try
				{
					propHandle.removeItem( columnHandle );
					break;
				}
				catch ( SemanticException e )
				{
					throw new ScriptException( e.getLocalizedMessage( ) );
				}
			}
		}
	}

	public String[] getHideRuleExpression( String formatType )
	{
		PropertyHandle propHandle = handle
				.getPropertyHandle( IReportItemModel.VISIBILITY_PROP );
		Iterator iterator = propHandle.iterator( );
		List rList = new ArrayList( );
		int count = 0;

		while ( iterator.hasNext( ) )
		{
			HideRuleHandle ruleHandle = (HideRuleHandle) iterator.next( );
			rList.add( ruleHandle.getFormat( ) );
			++count;
		}

		return (String[]) rList.toArray( new String[count] );

	}

	public void removeHideRule( String formatType ) throws ScriptException
	{
		if( formatType == null )
			return;
		PropertyHandle propHandle = handle
				.getPropertyHandle( IReportItemModel.VISIBILITY_PROP );
		Iterator iterator = propHandle.iterator( );
		while ( iterator.hasNext( ) )
		{
			HideRuleHandle ruleHandle = (HideRuleHandle) iterator.next( );
			if ( formatType.equals( ruleHandle.getFormat( )  ) )
			{
				try
				{
					propHandle.removeItem( ruleHandle );
					break;
				}
				catch ( SemanticException e )
				{
					throw new ScriptException( e.getLocalizedMessage( ) );
				}
			}
		}

	}
}
