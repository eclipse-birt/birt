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

import java.util.List;

import org.eclipse.birt.core.data.ExpressionUtil;
import org.eclipse.birt.core.data.IColumnBinding;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.elements.GroupElement;
import org.eclipse.birt.report.model.util.DataBoundColumnUtil;
import org.xml.sax.SAXException;

/**
 * Parse the valueExpr of DataItem in BIRT 2.1M5 to BIRT 2.1 RC0.
 */

class CompatibleDataValueExprState extends CompatibleMiscExpressionState
{

	/**
	 * Constructs a compatible state.
	 * 
	 * @param theHandler
	 *            the handler to parse the design file.
	 * @param element
	 *            the data item
	 */

	CompatibleDataValueExprState( ModuleParserHandler theHandler,
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

		if ( value == null )
			return;

		List newExprs = null;

		try
		{
			newExprs = ExpressionUtil.extractColumnExpressions( value );
		}
		catch ( BirtException e )
		{
			newExprs = null;
		}

		DesignElement target = DataBoundColumnUtil.findTargetOfBoundColumns(
				element, null, handler.module );

		if ( newExprs != null && newExprs.size( ) == 1 )
		{
			IColumnBinding column = (IColumnBinding) newExprs.get( 0 );

			if ( value.equals( ExpressionUtil.createRowExpression( column
					.getResultSetColumnName( ) ) ) )
			{
				String newName = column.getResultSetColumnName( );
				if ( target instanceof GroupElement )
				{
					newName = appendBoundColumnsToCachedGroup(
							(GroupElement) target, newName, column
									.getBoundExpression( ) );
				}
				else
				{
					newName = DataBoundColumnUtil.createBoundDataColumn(
							target, newName, column.getBoundExpression( ),
							handler.getModule( ) );
				}

				// set the property for the result set column property of
				// DataItem.

				doEnd( newName );

				return;
			}

			setupBoundDataColumns( target, value );
		}

		if ( newExprs != null && newExprs.size( ) > 1 )
		{
			setupBoundDataColumns( target, value );
		}

		String trimmedValue = value.trim( );
		String newName = trimmedValue;

		if ( trimmedValue.length( ) == 0 )
		{
			doEnd( newName );
			return;
		}

		if ( target instanceof GroupElement )
		{
			newName = appendBoundColumnsToCachedGroup( (GroupElement) target,
					trimmedValue, trimmedValue );
		}
		else
		{
			newName = DataBoundColumnUtil.createBoundDataColumn( target,
					trimmedValue, trimmedValue, handler.getModule( ) );
		}

		// set the property for the result set column property of DataItem.

		doEnd( newName );
	}
}
