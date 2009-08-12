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

package org.eclipse.birt.chart.extension.datafeed;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.chart.computation.DataSetIterator;
import org.eclipse.birt.chart.datafeed.DataSetAdapter;
import org.eclipse.birt.chart.datafeed.IResultSetDataSet;
import org.eclipse.birt.chart.engine.extension.i18n.Messages;
import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.model.data.DataSet;
import org.eclipse.birt.chart.model.data.impl.DifferenceDataSetImpl;
import org.eclipse.birt.chart.model.data.impl.StockDataSetImpl;
import org.eclipse.birt.chart.plugin.ChartEngineExtensionPlugin;

import com.ibm.icu.util.StringTokenizer;

/**
 * Capable of processing data sets that contains Difference entry that wrap 2
 * values - positive value, negative value.
 */
public final class DifferenceDataSetProcessorImpl extends DataSetAdapter
{

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.datafeed.IDataSetProcessor#populate(java.lang.Object,
	 *      org.eclipse.birt.chart.model.data.DataSet)
	 */
	public final DataSet populate( Object oResultSetDef, DataSet ds )
			throws ChartException
	{
		if ( oResultSetDef instanceof IResultSetDataSet )
		{
			final IResultSetDataSet rsds = (IResultSetDataSet) oResultSetDef;
			final long lRowCount = rsds.getSize( );

			if ( lRowCount <= 0 )
			{
				throw new ChartException( ChartEngineExtensionPlugin.ID,
						ChartException.ZERO_DATASET,
						"exception.empty.dataset",//$NON-NLS-1$
						Messages.getResourceBundle( getULocale( ) ) );
			}

			int i = 0;

			final DifferenceEntry[] sea = new DifferenceEntry[(int) lRowCount];
			while ( rsds.hasNext( ) )
			{
				Object[] oTwoComponents = rsds.next( );

				validateDifferenceEntryData( oTwoComponents );

				sea[i++] = new DifferenceEntry( oTwoComponents );
			}
			if ( ds == null )
			{
				ds = DifferenceDataSetImpl.create( sea );
			}
			else
			{
				ds.setValues( sea );
			}
		}
		else
		{
			throw new ChartException( ChartEngineExtensionPlugin.ID,
					ChartException.DATA_SET,
					"exception.unknown.custom.dataset",//$NON-NLS-1$
					new Object[]{
							ds, oResultSetDef
					},
					Messages.getResourceBundle( getULocale( ) ) );
		}
		return ds;
	}

	private void validateDifferenceEntryData( Object[] obja )
			throws ChartException
	{
		boolean valid = true;

		if ( obja == null )
		{
			valid = false;
		}
		else if ( obja.length != 2 )
		{
			throw new ChartException( ChartEngineExtensionPlugin.ID,
					ChartException.DATA_SET,
					"exception.dataset.differenceseries",//$NON-NLS-1$
					Messages.getResourceBundle( getULocale( ) ) );
		}

		if ( !valid )
		{
			throw new ChartException( ChartEngineExtensionPlugin.ID,
					ChartException.VALIDATION,
					"exception.dataset.null.differenceentry", //$NON-NLS-1$
					Messages.getResourceBundle( getULocale( ) ) );
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.datafeed.IDataSetProcessor#getMinimum(org.eclipse.birt.chart.model.data.DataSet)
	 */
	public final Object getMinimum( DataSet ds ) throws ChartException
	{
		DataSetIterator dsi = null;
		try
		{
			dsi = new DataSetIterator( ds );
			dsi.reset( );
		}
		catch ( IllegalArgumentException uiex )
		{
			throw new ChartException( ChartEngineExtensionPlugin.ID,
					ChartException.DATA_SET,
					uiex );
		}
		if ( dsi.size( ) == 0 )
		{
			throw new ChartException( ChartEngineExtensionPlugin.ID,
					ChartException.DATA_SET,
					"exception.empty.dataset", //$NON-NLS-1$
					Messages.getResourceBundle( getULocale( ) ) );
		}

		DifferenceEntry dde;

		double[] da = new double[2];
		double dMin = Double.MAX_VALUE;
		while ( dsi.hasNext( ) )
		{
			dde = (DifferenceEntry) dsi.next( );
			if ( dde != null )
			{
				da[0] = dde.getPositiveValue( );
				da[1] = dde.getNegativeValue( );

				for ( int j = 0; j < 2; j++ )
				{
					if ( dMin > da[j] )
					{
						dMin = da[j];
					}
				}
			}
		}
		return new Double( dMin );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.datafeed.IDataSetProcessor#getMaximum(org.eclipse.birt.chart.model.data.DataSet)
	 */
	public final Object getMaximum( DataSet ds ) throws ChartException
	{
		DataSetIterator dsi = null;
		try
		{
			dsi = new DataSetIterator( ds );
			dsi.reset( );
		}
		catch ( IllegalArgumentException uiex )
		{
			throw new ChartException( ChartEngineExtensionPlugin.ID,
					ChartException.DATA_SET,
					uiex );
		}
		if ( dsi.size( ) == 0 )
		{
			throw new ChartException( ChartEngineExtensionPlugin.ID,
					ChartException.DATA_SET,
					"exception.empty.dataset", //$NON-NLS-1$
					Messages.getResourceBundle( getULocale( ) ) );
		}

		DifferenceEntry dde;

		double[] da = new double[2];
		double dMax = -Double.MAX_VALUE;
		while ( dsi.hasNext( ) )
		{
			dde = (DifferenceEntry) dsi.next( );
			if ( dde != null )
			{
				da[0] = dde.getPositiveValue( );
				da[1] = dde.getNegativeValue( );

				for ( int j = 0; j < 2; j++ )
				{
					if ( dMax < da[j] )
					{
						dMax = da[j];
					}
				}
			}
		}
		return new Double( dMax );
	}

	/**
	 * This method takes the data in String form and populates the DataSet
	 * (creating one if necessary). For the DifferenceEntry, the data should be
	 * provided in the form: 'P <positive value> N <negative value>, P <next
	 * positive value> N...' i.e. 'P', 'N' are used to designate a value as
	 * either the positive, negative component of the data element. DataElements
	 * should be separated by commas (,). Components within the data element are
	 * separated by a space and their sequence is not important.
	 * 
	 * @return DataSet populated by the entries in the String or null if the
	 *         String is null.
	 * @throws ChartException
	 *             if there is any problem parsing the String passed in.
	 * @see org.eclipse.birt.chart.datafeed.IDataSetProcessor#fromString(java.lang.String,
	 *      org.eclipse.birt.chart.model.data.DataSet)
	 */
	public final DataSet fromString( String sDataSetRepresentation, DataSet ds )
			throws ChartException
	{
		// Do NOT create a DataSet if the content string is null
		if ( sDataSetRepresentation == null )
		{
			return ds;
		}
		// Create an EMPTY DataSet if the content string is an empty string
		if ( ds == null )
		{
			ds = StockDataSetImpl.create( null );
		}
		StringTokenizer strTokDataElement = new StringTokenizer( sDataSetRepresentation,
				"," ); //$NON-NLS-1$
		StringTokenizer strTokComponents = null;
		String strDataElement = null;
		String strComponent = null;
		List vData = new ArrayList( );
		while ( strTokDataElement.hasMoreTokens( ) )
		{
			strDataElement = strTokDataElement.nextToken( ).trim( );

			// Build a DifferenceEntry from this token
			strTokComponents = new StringTokenizer( strDataElement );

			// Compatible with other sample data
			if ( strTokComponents.countTokens( ) == 1 )
			{
				double dComponent = Double.parseDouble( strDataElement );
				vData.add( new DifferenceEntry( dComponent, dComponent - 2 ) );
				continue;
			}

			DifferenceEntry entry = new DifferenceEntry( Double.NaN, Double.NaN );
			while ( strTokComponents.hasMoreTokens( ) )
			{
				strComponent = strTokComponents.nextToken( )
						.trim( )
						.toUpperCase( );
				double dComponent = Double.parseDouble( strComponent.substring( 1 ) );
				if ( strComponent.startsWith( "P" ) ) //$NON-NLS-1$
				{
					entry.setPositiveValue( dComponent );
				}
				else if ( strComponent.startsWith( "N" ) ) //$NON-NLS-1$
				{
					entry.setNegativeValue( dComponent );
				}
			}
			vData.add( entry );
		}
		ds.setValues( vData );
		return ds;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.datafeed.DataSetProcessor#getExpectedStringFormat()
	 */
	public String getExpectedStringFormat( )
	{
		return Messages.getString( "info.difference.sample.format", //$NON-NLS-1$
				getULocale( ) );
	}

	public String toString( Object[] columnData ) throws ChartException
	{
		if ( columnData == null || columnData.length == 0 )
		{
			throw new ChartException( ChartEngineExtensionPlugin.ID,
					ChartException.DATA_SET,
					"exception.base.orthogonal.null.datadefinition", //$NON-NLS-1$
					Messages.getResourceBundle( getULocale( ) ) );
		}
		StringBuffer buffer = new StringBuffer( );
		for ( int i = 0; i < columnData.length; i++ )
		{
			if ( columnData[i] == null )
			{
				throw new ChartException( ChartEngineExtensionPlugin.ID,
						ChartException.DATA_SET,
						"exception.base.orthogonal.null.datadefinition", //$NON-NLS-1$
						Messages.getResourceBundle( getULocale( ) ) );
			}
			if ( columnData[i] instanceof Object[] )
			{
				buffer.append( toDifferenceString( (Object[]) columnData[i] ) );
			}
			if ( i < columnData.length - 1 )
			{
				buffer.append( "," ); //$NON-NLS-1$
			}
		}
		return buffer.toString( );
	}

	private StringBuffer toDifferenceString( Object[] differenceArray )
			throws ChartException
	{
		if ( differenceArray.length != 2 || differenceArray[0] == null )
		{
			throw new ChartException( ChartEngineExtensionPlugin.ID,
					ChartException.DATA_SET,
					"Invalid data set column" ); //$NON-NLS-1$
		}
		StringBuffer buffer = new StringBuffer( );
		buffer.append( "P" + String.valueOf( differenceArray[0] ) + " " ); //$NON-NLS-1$ //$NON-NLS-2$
		buffer.append( "N" + String.valueOf( differenceArray[1] ) ); //$NON-NLS-1$
		return buffer;
	}
}