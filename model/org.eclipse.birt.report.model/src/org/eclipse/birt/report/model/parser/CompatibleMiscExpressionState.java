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

import org.eclipse.birt.core.data.ExpressionUtil;
import org.eclipse.birt.core.data.IColumnBinding;
import org.eclipse.birt.report.model.api.core.IStructure;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.metadata.PropertyDefn;
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

	/**
	 * Constructs a compatible state.
	 * 
	 * @param theHandler
	 *            the handler to parse the design file.
	 * @param element
	 *            the data item
	 */

	CompatibleMiscExpressionState( ModuleParserHandler theHandler,
			DesignElement element, PropertyDefn propDefn, IStructure struct )
	{
		super( theHandler, element, propDefn, struct );
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

		IColumnBinding boundColumn = null;

		boundColumn = ExpressionUtil.getColumnBinding( value );
		if ( boundColumn == null )
		{
			// set the property for the result set column property of DataItem.

			doEnd( value );
		}

		String newName = DataBoundColumnUtil.setupBoundDataColumn( element,
				boundColumn.getResultSetColumnName( ), boundColumn
						.getBoundExpression( ), handler.getModule( ) );

		// set the property for the result set column property of DataItem.

		if ( newName != null )
			doEnd( ExpressionUtil.createRowExpression( newName ) );
		else
			doEnd( value );

	}
}
