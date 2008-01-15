/*******************************************************************************
 * Copyright (c) 2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.chart.reportitem;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.extension.IQueryResultSet;

/**
 * The implementation of <code>IGroupedDataResultSet</code> for chart.
 * 
 * @since BIRT 2.3
 */
public class BIRTGroupedQueryResultSetEvaluator
		extends
		ChartBuilderGrupedQueryResultSetEvaluator
{

	private IQueryResultSet fQueryResultSet;

	/**
	 * Constructor.
	 * 
	 * @param resultSet
     * @param hasAggregation
	 */
	public BIRTGroupedQueryResultSetEvaluator( IQueryResultSet resultSet, boolean hasAggregation )
	{
		super( resultSet.getResultIterator( ), hasAggregation );
		
		fQueryResultSet = resultSet;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.factory.IDataRowExpressionEvaluator#close()
	 */
	public void close( )
	{
		fQueryResultSet.close( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.factory.IDataRowExpressionEvaluator#evaluate(java.lang.String)
	 */
	public Object evaluate( String expression )
	{
		try
		{
			return fQueryResultSet.evaluate( expression );
		}
		catch ( BirtException e )
		{
			fLogger.log( e );
		}
		return null;
	}
}
