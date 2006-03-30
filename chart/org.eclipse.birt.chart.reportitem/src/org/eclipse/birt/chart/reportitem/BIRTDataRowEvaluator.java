
package org.eclipse.birt.chart.reportitem;

/***********************************************************************
 * Copyright (c) 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

import java.util.HashMap;
import java.util.Iterator;

import org.eclipse.birt.chart.factory.DataRowExpressionEvaluatorAdapter;
import org.eclipse.birt.data.engine.api.IBaseExpression;
import org.eclipse.birt.data.engine.api.IBaseQueryDefinition;
import org.eclipse.birt.data.engine.api.IScriptExpression;
import org.eclipse.birt.report.engine.extension.IRowSet;

/**
 * A BIRT implementation of IDataRowExpressionEvaluator.
 */
public class BIRTDataRowEvaluator extends DataRowExpressionEvaluatorAdapter
{

	private IRowSet set;

	private HashMap map;

	private HashMap globalMap;

	/**
	 * The constructor.
	 * 
	 * @param set
	 * @param definition
	 */
	public BIRTDataRowEvaluator( IRowSet set, IBaseQueryDefinition definition )
	{
		this.set = set;
		this.map = new HashMap( );
		this.globalMap = new HashMap( );
		for ( Iterator iter = definition.getRowExpressions( ).iterator( ); iter.hasNext( ); )
		{
			IScriptExpression exp = (IScriptExpression) iter.next( );
			map.put( exp.getText( ), exp );
		}
		for ( Iterator iter = definition.getBeforeExpressions( ).iterator( ); iter.hasNext( ); )
		{
			IScriptExpression exp = (IScriptExpression) iter.next( );
			globalMap.put( exp.getText( ), exp );
		}
		for ( Iterator iter = definition.getAfterExpressions( ).iterator( ); iter.hasNext( ); )
		{
			IScriptExpression exp = (IScriptExpression) iter.next( );
			globalMap.put( exp.getText( ), exp );
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.factory.IDataRowExpressionEvaluator#evaluate(java.lang.String)
	 */
	public Object evaluate( String expression )
	{
		IBaseExpression ibe = (IBaseExpression) map.get( expression );
		if ( ibe == null )
		{
			return null;
		}
		return set.evaluate( ibe );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.factory.IDataRowExpressionEvaluator#evaluateGlobal(java.lang.String)
	 */
	public Object evaluateGlobal( String expression )
	{
		IBaseExpression ibe = (IBaseExpression) globalMap.get( expression );
		if ( ibe == null )
		{
			return null;
		}
		return set.evaluate( ibe );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.factory.IDataRowExpressionEvaluator#next()
	 */
	public boolean next( )
	{
		return set.next( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.factory.IDataRowExpressionEvaluator#close()
	 */
	public void close( )
	{
		set.close( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.factory.IDataRowExpressionEvaluator#first()
	 */
	public boolean first( )
	{
		return set.next( );
	}

}
