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

package org.eclipse.birt.report.model.parser;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.core.data.ExpressionUtil;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.model.api.StructureFactory;
import org.eclipse.birt.report.model.api.elements.structures.ComputedColumn;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.elements.ReportItem;
import org.eclipse.birt.report.model.elements.interfaces.IReportItemModel;
import org.eclipse.birt.report.model.metadata.ElementRefValue;
import org.eclipse.birt.report.model.util.DataBoundColumnUtil;
import org.xml.sax.SAXException;

/**
 * Parses expression values from BIRT 2.1M5 to BIRT 2.1 RC0. The rule is that if
 * any expression contains the DtE authorized string, creates the corresponding
 * bound data columns.
 * <p>
 * This is a part of backward compatibility work from BIRT 2.1M5 to BIRT 2.1.0.
 */

class CompatibleMiscExpressionState extends ExpressionState
{

	/**
	 * Constructs a compatible state.
	 * 
	 * @param theHandler
	 *            the handler to parse the design file.
	 * @param element
	 *            the data item
	 */

	CompatibleMiscExpressionState( ModuleParserHandler theHandler,
			DesignElement element )
	{
		super( theHandler, element );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.util.AbstractParseState#end()
	 */

	public void end( ) throws SAXException
	{
		String value = text.toString( );

		if ( StringUtil.isBlank( value ) )
			return;

		List exprs = null;

		try
		{
			exprs = ExpressionUtil.extractColumnExpressions( value );
		}
		catch ( BirtException e )
		{
			exprs = null;
		}

		// setupBoundDataColumns( exprs );

		// set the property for the result set column property of DataItem.

		doEnd( value );
	}

	/**
	 * Creates bound data columns on the report item.
	 * 
	 * @param exprs
	 *            a list containing DtE authorized expressions.
	 */

	protected void setupBoundDataColumns( List exprs )
	{
		if ( exprs == null || exprs.isEmpty( ) )
			return;

		DesignElement target = findContainerWithDataSet( null );

		if ( target == null )
			target = element;

		assert target instanceof ReportItem;

		for ( int i = 0; i < exprs.size( ); i++ )
		{
			String expr = (String) exprs.get( i );
			createBoundDataColumn( target, expr, expr );
		}
	}

	/**
	 * Sets up the data binding for the compatiblity work for the design file
	 * that is older than 3.1.0 version.
	 * 
	 * @param resultSetColumn
	 *            the expected column name
	 * @param expression
	 *            the column expression in data binding
	 * @return the new name of the bound column
	 */

	protected String setupBoundDataColumn( String resultSetColumn,
			String expression )
	{
		DesignElement target = findContainerWithDataSet( expression );
		if ( target == null )
			target = element;

		return createBoundDataColumn( target, resultSetColumn, expression );
	}

	/**
	 * Creates a data binding on the target element.
	 * 
	 * @param target
	 *            the element
	 * @param value
	 *            the column expression. In default, it is also the column name.
	 */

	private String createBoundDataColumn( DesignElement target,
			String columnName, String expression )
	{
		Module module = handler.getModule( );

		List columns = (List) target.getLocalProperty( module,
				IReportItemModel.BOUND_DATA_COLUMNS_PROP );

		if ( columns == null )
		{
			columns = new ArrayList( );
			target.setProperty( IReportItemModel.BOUND_DATA_COLUMNS_PROP,
					columns );
		}

		String newName = columnName;

		String foundName = DataBoundColumnUtil.getColumnName( columns,
				expression );
		if ( foundName == null )
		{
			ComputedColumn column = StructureFactory.createComputedColumn( );
			columns.add( column );

			newName = DataBoundColumnUtil.handleDuplicateName( columns );

			column.setName( newName );
			column.setExpression( expression );
		}
		else
			newName = foundName;

		assert target instanceof ReportItem;

		ElementRefValue dataSetRef = (ElementRefValue) target.getProperty(
				module, IReportItemModel.DATA_SET_PROP );
		if ( dataSetRef == null )
			return newName;

		return newName;
	}

	/**
	 * Returns the nearest container or the element self if there is a not
	 * <code>null</code> dataSet property value.
	 * 
	 * @return the element has the dataSet value or <code>null</code> when not
	 *         found.
	 */

	private ReportItem findContainerWithDataSet( String expression )
	{
		DesignElement tmpElement = element;
		Module module = handler.getModule( );

		ReportItem retElement = null;

		while ( tmpElement != null )
		{
			if ( !( tmpElement instanceof ReportItem ) )
			{
				tmpElement = tmpElement.getContainer( );
				continue;
			}

			if ( retElement == null )
				retElement = (ReportItem) tmpElement;

			ElementRefValue dataSetRef = (ElementRefValue) tmpElement
					.getProperty( module, IReportItemModel.DATA_SET_PROP );
			if ( dataSetRef != null )
			{
				retElement = (ReportItem) tmpElement;
				break;
			}

			List columns = (List) tmpElement.getLocalProperty( module,
					IReportItemModel.BOUND_DATA_COLUMNS_PROP );
			if ( columns == null )
			{
				tmpElement = tmpElement.getContainer( );
				continue;
			}

			if ( DataBoundColumnUtil.getColumnName( columns, expression ) != null )
				break;

			tmpElement = tmpElement.getContainer( );
		}

		return retElement;
	}

}
