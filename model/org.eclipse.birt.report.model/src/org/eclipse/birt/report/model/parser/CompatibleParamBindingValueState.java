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
import org.eclipse.birt.report.model.api.core.IStructure;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.metadata.PropertyDefn;
import org.eclipse.birt.report.model.util.DataBoundColumnUtil;
import org.xml.sax.SAXException;

/**
 * Parse the value of ParamBinding in BIRT 2.1M5 to BIRT 2.1 RC0.
 */

public class CompatibleParamBindingValueState
		extends
			CompatibleMiscExpressionState
{

	/**
	 * Constructs a compatible state.
	 * 
	 * @param theHandler
	 *            the handler to parse the design file.
	 * @param element
	 *            the data item
	 */

	CompatibleParamBindingValueState( ModuleParserHandler theHandler,
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

		if ( value == null )
			return;

		// keep the expression as same.

		doEnd( value, true );

		if ( StringUtil.compareVersion( handler.getVersion( ), "3.2.0" ) >= 0 ) //$NON-NLS-1$
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

		if ( newExprs == null || newExprs.isEmpty( ) )
		{
			return;
		}

		DesignElement target = DataBoundColumnUtil
				.findTargetElementOfParamBinding( element, handler.getModule( ) );
		for ( int i = 0; i < newExprs.size( ); i++ )
		{
			// ignore the outer level here.

			IColumnBinding boundColumn = (IColumnBinding) newExprs.get( i );
			String newExpression = boundColumn.getBoundExpression( );
			if ( newExpression == null )
				continue;

			DataBoundColumnUtil.createBoundDataColumn( target, boundColumn
					.getResultSetColumnName( ), newExpression, handler.module );
		}
	}

}
