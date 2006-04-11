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
import org.eclipse.birt.report.model.core.DesignElement;
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

		IColumnBinding boundColumn = ExpressionUtil.getColumnBinding( value );
		if ( boundColumn == null )
		{
			// set the property for the result set column property of DataItem.

			doEnd( value );
			
			return;
		}

		String newName = DataBoundColumnUtil.setupBoundDataColumn( element,
				boundColumn.getResultSetColumnName( ), boundColumn
						.getBoundExpression( ), handler.getModule( ) );

		// set the property for the result set column property of DataItem.

		doEnd( newName );
	}

}
