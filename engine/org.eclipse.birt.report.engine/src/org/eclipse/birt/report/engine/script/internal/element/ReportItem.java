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
import org.eclipse.birt.report.engine.api.script.element.IHideRule;
import org.eclipse.birt.report.engine.api.script.element.IHighlightRule;
import org.eclipse.birt.report.engine.api.script.element.IReportItem;
import org.eclipse.birt.report.model.api.ComputedColumnHandle;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.DimensionHandle;
import org.eclipse.birt.report.model.api.HideRuleHandle;
import org.eclipse.birt.report.model.api.HighlightRuleHandle;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.elements.interfaces.IReportItemModel;
import org.eclipse.birt.report.model.elements.interfaces.IStyleModel;

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

	public String getDataBinding( String bindingName )
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

	public IDataBinding[] getDataBindings( )
	{
		Iterator iterator = ( (ReportItemHandle) handle )
				.columnBindingsIterator( );
		List rList = new ArrayList( );
		int count = 0;
		while ( iterator.hasNext( ) )
		{
			ComputedColumnHandle columnHandle = (ComputedColumnHandle) iterator
					.next( );
			DataBindingImpl d = new DataBindingImpl( columnHandle );
			rList.add( d );
			++count;
		}

		return (IDataBinding[]) rList.toArray( new IDataBinding[count] );
	}

	public void removeDataBinding( String bindingName ) throws ScriptException
	{
		if ( bindingName == null || bindingName.length( ) == 0 )
			return;

		PropertyHandle propHandle = ( (ReportItemHandle) handle )
				.getPropertyHandle( IReportItemModel.BOUND_DATA_COLUMNS_PROP );
		List structureList = new ArrayList( );
		Iterator iterator = propHandle.iterator( );

		while ( iterator.hasNext( ) )
		{
			ComputedColumnHandle columnHandle = (ComputedColumnHandle) iterator
					.next( );
			if ( bindingName.equals( columnHandle.getName( ) ) )
			{
				structureList.add( columnHandle );
			}
		}
		try
		{
			propHandle.removeItems( structureList );
		}
		catch ( SemanticException e )
		{
			throw new ScriptException( e.getLocalizedMessage( ) );
		}
	}

	/**
	 * Removes all data bindings.
	 * 
	 * @throws ScriptException
	 */

	public void removeDataBindings( ) throws ScriptException
	{
		PropertyHandle propHandle = handle
				.getPropertyHandle( IReportItemModel.BOUND_DATA_COLUMNS_PROP );
		try
		{
			propHandle.clearValue( );
		}
		catch ( SemanticException e )
		{
			throw new ScriptException( e.getLocalizedMessage( ) );
		}
	}

	/**
	 * Adds ComputedColumn.
	 * 
	 * @param binding
	 * @throws ScriptException
	 */

	public void addDataBinding( IDataBinding binding ) throws ScriptException
	{
		if ( binding == null )
			return;

		PropertyHandle propHandle = handle
				.getPropertyHandle( IReportItemModel.BOUND_DATA_COLUMNS_PROP );
		try
		{
			propHandle.addItem( binding.getStructure( ) );
		}
		catch ( SemanticException e )
		{
			throw new ScriptException( e.getLocalizedMessage( ) );
		}
	}

	/**
	 * Gets all hide rules.
	 */

	public IHideRule[] getHideRules( )
	{
		PropertyHandle propHandle = handle
				.getPropertyHandle( IReportItemModel.VISIBILITY_PROP );
		Iterator iterator = propHandle.iterator( );
		List rList = new ArrayList( );
		int count = 0;

		while ( iterator.hasNext( ) )
		{
			HideRuleHandle ruleHandle = (HideRuleHandle) iterator.next( );
			HideRuleImpl rule = new HideRuleImpl( ruleHandle );
			rList.add( rule );
			++count;
		}
		return (IHideRule[]) rList.toArray( new IHideRule[count] );
	}

	/**
	 * Removes Hide Rule through format type.
	 */

	public void removeHideRule( IHideRule rule ) throws ScriptException
	{
		PropertyHandle propHandle = handle
				.getPropertyHandle( IReportItemModel.VISIBILITY_PROP );
		try
		{
			propHandle.removeItem( rule.getStructure() );
		}
		catch ( SemanticException e )
		{
			throw new ScriptException( e.getLocalizedMessage( ) );
		}
	}

	/**
	 * Add HideRule.
	 * 
	 * @param rule
	 * @throws ScriptException
	 */

	public void addHideRule( IHideRule rule ) throws ScriptException
	{
		if ( rule == null )
			return;

		PropertyHandle propHandle = handle
				.getPropertyHandle( IReportItemModel.VISIBILITY_PROP );
		try
		{
			propHandle.addItem( rule.getStructure( ) );
		}
		catch ( SemanticException e )
		{
			throw new ScriptException( e.getLocalizedMessage( ) );
		}
	}

	/**
	 * Removes Hide Rules.
	 */

	public void removeHideRules( ) throws ScriptException
	{
		PropertyHandle propHandle = handle
				.getPropertyHandle( IReportItemModel.VISIBILITY_PROP );
		try
		{
			propHandle.clearValue( );
		}
		catch ( SemanticException e )
		{
			throw new ScriptException( e.getLocalizedMessage( ) );
		}
	}

	public void addHighlightRule( IHighlightRule rule ) throws ScriptException
	{
		PropertyHandle propHandle = handle
				.getPropertyHandle( IStyleModel.HIGHLIGHT_RULES_PROP );
		try
		{
			propHandle.addItem( rule.getStructure( ) );
		}
		catch ( SemanticException e )
		{
			throw new ScriptException( e.getLocalizedMessage( ) );
		}
	}

	public IHighlightRule[] getHighlightRules( )
	{
		PropertyHandle propHandle = handle
				.getPropertyHandle( IStyleModel.HIGHLIGHT_RULES_PROP );
		Iterator iterator = propHandle.iterator( );
		List rList = new ArrayList( );
		int count = 0;

		while ( iterator.hasNext( ) )
		{
			HighlightRuleHandle ruleHandle = (HighlightRuleHandle) iterator
					.next( );
			HighlightRuleImpl rule = new HighlightRuleImpl( ruleHandle );
			rList.add( rule );
			++count;
		}
		return (IHighlightRule[]) rList.toArray( new IHighlightRule[count] );
	}

	public void removeHighlightRule( IHighlightRule rule )
			throws ScriptException
	{
		PropertyHandle propHandle = handle
				.getPropertyHandle( IStyleModel.HIGHLIGHT_RULES_PROP );
		try
		{
			propHandle.removeItem( rule.getStructure( ) );
		}
		catch ( SemanticException e )
		{
			throw new ScriptException( e.getLocalizedMessage( ) );
		}
	}

	public void removeHighlightRules( ) throws ScriptException
	{
		PropertyHandle propHandle = handle
				.getPropertyHandle( IStyleModel.HIGHLIGHT_RULES_PROP );
		try
		{
			propHandle.clearValue( );
		}
		catch ( SemanticException e )
		{
			throw new ScriptException( e.getLocalizedMessage( ) );
		}
	}

}
