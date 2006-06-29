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

package org.eclipse.birt.chart.computation;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.model.data.DataSet;
import org.eclipse.emf.common.util.EMap;

/**
 * This class provides the ability to process the user dataSets.
 * 
 * WARNING: This is an internal class and subject to change
 */
public class UserDataSetHints
{

	private String[] keys;
	private DataSetIterator[] dsis;

	/**
	 * The constructor.
	 * 
	 * @param allSeriesDataSets
	 * @throws ChartException
	 */
	public UserDataSetHints( EMap allSeriesDataSets ) throws ChartException
	{
		List keyList = new ArrayList( );
		List dsiList = new ArrayList( );

		for ( Iterator itr = allSeriesDataSets.entrySet( ).iterator( ); itr.hasNext( ); )
		{
			Map.Entry entry = (Map.Entry) itr.next( );

			if ( entry.getKey( ) != null )
			{
				String key = (String) entry.getKey( );
				DataSet ds = (DataSet) entry.getValue( );
				DataSetIterator dsi = new DataSetIterator( ds );

				keyList.add( key );
				dsiList.add( dsi );
			}
		}

		keys = (String[]) keyList.toArray( new String[keyList.size( )] );
		dsis = (DataSetIterator[]) dsiList.toArray( new DataSetIterator[dsiList.size( )] );
	}

	/**
	 * Resets all associated datasetiterators.
	 */
	public final void reset( )
	{
		for ( int i = 0; i < dsis.length; i++ )
		{
			dsis[i].reset( );
		}
	}

	/**
	 * Next all associated datasetiterators and update the datapointhints
	 * object.
	 * 
	 * @param dph
	 */
	public final void next( DataPointHints dph )
	{
		for ( int i = 0; i < keys.length; i++ )
		{
			Object val = dsis[i].next( );
			if ( dph != null )
			{
				dph.setUserValue( keys[i], val );
			}
		}
	}
}