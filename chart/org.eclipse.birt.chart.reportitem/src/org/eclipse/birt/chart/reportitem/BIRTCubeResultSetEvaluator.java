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

import javax.olap.OLAPException;
import javax.olap.cursor.EdgeCursor;

import org.eclipse.birt.chart.factory.DataRowExpressionEvaluatorAdapter;
import org.eclipse.birt.chart.log.ILogger;
import org.eclipse.birt.chart.log.Logger;
import org.eclipse.birt.report.engine.extension.ICubeResultSet;

/**
 * 
 */

public class BIRTCubeResultSetEvaluator
		extends
			DataRowExpressionEvaluatorAdapter
{

	private static ILogger logger = Logger.getLogger( "org.eclipse.birt.chart.reportitem/trace" ); //$NON-NLS-1$

	final private ICubeResultSet rs;

	/**
	 * The constructor.
	 * 
	 * @param set
	 * @param definition
	 */
	public BIRTCubeResultSetEvaluator( ICubeResultSet set )
	{
		this.rs = set;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.factory.IDataRowExpressionEvaluator#evaluate(java.lang.String)
	 */
	public Object evaluate( String expression )
	{
		Object result = null;

		// Optional means: Engine's evaluate method
		// try
		// {
		// result = rs.evaluate( expression );
		// }
		// catch ( BirtException e )
		// {
		// logger.log( e );
		// }

		try
		{
			// Use DtE's method to evaluate expression for the sake of
			// performance
			result = rs.getCubeCursor( )
					.getObject( ChartCubeQueryHelper.getBindingName( expression ) );
		}
		catch ( OLAPException e )
		{
			logger.log( e );
		}

		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.factory.IDataRowExpressionEvaluator#evaluateGlobal(java.lang.String)
	 */
	public Object evaluateGlobal( String expression )
	{
		return evaluate( expression );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.factory.IDataRowExpressionEvaluator#next()
	 */
	public boolean next( )
	{
		try
		{
			EdgeCursor edge = getEdge( );
			return edge.next( );
		}
		catch ( OLAPException e )
		{
			logger.log( e );
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.factory.IDataRowExpressionEvaluator#close()
	 */
	public void close( )
	{
		rs.close( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.factory.IDataRowExpressionEvaluator#first()
	 */
	public boolean first( )
	{
		try
		{
			EdgeCursor edge = getEdge( );
			return edge.first( );
		}
		catch ( OLAPException e )
		{
			logger.log( e );
		}
		return false;
	}

	protected EdgeCursor getEdge( ) throws OLAPException
	{
		// TODO to support multiple edge cursors
		return (EdgeCursor) rs.getCubeCursor( ).getOrdinateEdge( ).get( 0 );
	}

}
