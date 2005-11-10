/***********************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.chart.datafeed;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.eclipse.birt.chart.computation.Methods;
import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.model.data.DataSet;
import org.eclipse.birt.chart.plugin.ChartEnginePlugin;

/**
 * Provides a no-op implementation of the
 * {@link org.eclipse.birt.chart.datafeed.IDataSetProcessor}interface
 * definition to be subclassed by each extension writer as needed.
 */
public class DataSetAdapter extends Methods implements IDataSetProcessor
{

	/**
	 * An internal instance of the locale being used for processing
	 */
	private transient Locale lcl = null;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.datafeed.IDataSetProcessor#fromString(java.lang.String,
	 *      org.eclipse.birt.chart.model.data.DataSet)
	 */
	public DataSet fromString( String sDataSetRepresentation, DataSet ds )
			throws ChartException
	{
		// NO-OP IMPL
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.datafeed.IDataSetProcessor#populate(java.lang.Object,
	 *      org.eclipse.birt.chart.model.data.DataSet)
	 */
	public DataSet populate( Object oResultSetDef, DataSet ds )
			throws ChartException
	{
		// NO-OP IMPL
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.datafeed.IDataSetProcessor#getMinimum(org.eclipse.birt.chart.model.data.DataSet)
	 */
	public Object getMinimum( DataSet ds ) throws ChartException
	{
		// NO-OP IMPL
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.datafeed.IDataSetProcessor#getMaximum(org.eclipse.birt.chart.model.data.DataSet)
	 */
	public Object getMaximum( DataSet ds ) throws ChartException
	{
		// NO-OP IMPL
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.datafeed.IDataSetProcessor#getExpectedStringFormat()
	 */
	public String getExpectedStringFormat( )
	{
		// NO-OP IMPL
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.datafeed.IDataSetProcessor#getLocale()
	 */
	public Locale getLocale( )
	{
		return ( lcl == null ) ? Locale.getDefault( ) : lcl;
	}

	/**
	 * A convenience method provided to associate a locale with a display server
	 * 
	 * @param lcl
	 *            The locale to be set
	 */
	public final void setLocale( Locale lcl )
	{
		this.lcl = lcl;
	}

	public String toString( Object[] columnData ) throws ChartException
	{
		if ( columnData == null || columnData.length == 0 )
		{
			throw new ChartException( ChartEnginePlugin.ID,
					ChartException.DATA_SET,
					"Invalid column data" ); //$NON-NLS-1$
		}
		StringBuffer buffer = new StringBuffer( );
		SimpleDateFormat sdf = new SimpleDateFormat( "yyyy/MM/dd" ); //$NON-NLS-1$
		for ( int i = 0; i < columnData.length; i++ )
		{
			// Unwrap array
			if ( columnData[i] instanceof Object[] )
			{
				columnData[i] = ( (Object[]) columnData[i] )[0];
			}
			if ( columnData[i] == null )
			{
				throw new ChartException( ChartEnginePlugin.ID,
						ChartException.DATA_SET,
						"Invalid data set column" ); //$NON-NLS-1$
			}

			if ( columnData[i] instanceof String )
			{
				buffer.append( columnData[i] );
			}
			else if ( columnData[i] instanceof Date )
			{
				buffer.append( sdf.format( (Date) columnData[i] ) );
			}
			else if ( columnData[i] instanceof Number )
			{
				buffer.append( String.valueOf( columnData[i] ) );
			}
			if ( i < columnData.length - 1 )
			{
				buffer.append( "," ); //$NON-NLS-1$
			}
		}
		return buffer.toString( );
	}
}