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
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.core.DesignElement;
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

}
