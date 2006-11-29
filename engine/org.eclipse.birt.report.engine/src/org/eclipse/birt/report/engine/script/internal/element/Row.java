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
import org.eclipse.birt.report.engine.api.script.element.IHighlightRule;
import org.eclipse.birt.report.engine.api.script.element.IRow;
import org.eclipse.birt.report.model.api.DimensionHandle;
import org.eclipse.birt.report.model.api.HighlightRuleHandle;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.birt.report.model.api.RowHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.elements.interfaces.IStyleModel;

public class Row extends ReportElement implements IRow
{

	public Row( RowHandle row )
	{
		super( row );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.script.element.IRow#getHeight()
	 */

	public String getHeight( )
	{
		DimensionHandle height = ( (RowHandle) handle ).getHeight( );
		return ( height == null ? null : height.getStringValue( ) );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.script.element.IRow#getBookmark()
	 */

	public String getBookmark( )
	{
		return ( (RowHandle) handle ).getBookmark( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.script.element.IRow#setBookmark(java.lang.String)
	 */

	public void setBookmark( String value ) throws ScriptException
	{
		try
		{
			( (RowHandle) handle ).setBookmark( value );
		}
		catch ( SemanticException e )
		{
			throw new ScriptException( e.getLocalizedMessage( ) );
		}
	}

	/**
	 * Add HighLightRule
	 * 
	 * @param rule
	 * @throws ScriptException
	 */

	public void addHighlightRule( IHighlightRule rule ) throws ScriptException
	{
		if ( rule == null )
			return;

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
			HighlightRuleImpl h = new HighlightRuleImpl( ruleHandle );
			rList.add( h );
			++count;
		}

		return (IHighlightRule[]) rList.toArray( new IHighlightRule[count] );
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

	public void removeHighlightRule( IHighlightRule rule ) throws ScriptException
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
}
