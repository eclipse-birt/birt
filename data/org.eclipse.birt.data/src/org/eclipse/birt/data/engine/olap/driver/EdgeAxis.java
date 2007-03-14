/*******************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/
package org.eclipse.birt.data.engine.olap.driver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.olap.OLAPException;

import org.eclipse.birt.data.engine.olap.cursor.EdgeInfoGenerator;
import org.eclipse.birt.data.engine.olap.data.api.IAggregationResultSet;
import org.eclipse.birt.data.engine.olap.query.view.BirtDimensionView;
import org.eclipse.birt.data.engine.olap.query.view.BirtEdgeView;

/**
 * 
 *
 */
public class EdgeAxis
{

	private DimensionAxis[] dimensionAxis;
	private IAggregationResultSet rs;
	private EdgeInfoGenerator edgeInfoUtil;
	

	public EdgeAxis( IAggregationResultSet resultSet, BirtEdgeView view,
			boolean isPage ) throws IOException
	{
		this.dimensionAxis = null;
		this.rs = resultSet;
		populateDimensionAxis( resultSet, view, isPage );
	}

	/**
	 * 
	 * @param rs
	 * @param view
	 * @param isPage
	 * @throws OLAPException
	 * @throws IOException
	 */
	private void populateDimensionAxis( IAggregationResultSet rs,
			BirtEdgeView view, boolean isPage ) throws IOException
	{
		List dimensionAxisList = new ArrayList( );

		int index = 0;
		if ( !isPage )
		{
			int levelIndex = 0;
			for ( int i = 0; i < view.getDimensionViews( ).size( ); i++ )
			{
				BirtDimensionView dv = (BirtDimensionView) ( view.getDimensionViews( ).get( i ) );
				Iterator levelIter = dv.getMemberSelection( ).iterator( );

				while ( levelIter.hasNext( ) )
				{
					levelIter.next( );
					DimensionAxis axis = new DimensionAxis( this,
							rs,
							index,
							levelIndex++,
							0 );
					index++;
					dimensionAxisList.add( axis );
				}
			}
		}
		else if ( isPage )
		{
			DimensionAxis axis = new DimensionAxis( this, rs, index, 0, 0 );
			dimensionAxisList.add( axis );
		}
		this.dimensionAxis = new DimensionAxis[dimensionAxisList.size( )];
		for ( int i = 0; i < dimensionAxisList.size( ); i++ )
		{
			this.dimensionAxis[i] = (DimensionAxis) dimensionAxisList.get( i );
		}

		edgeInfoUtil = new EdgeInfoGenerator( rs, this.dimensionAxis );
		try
		{
			edgeInfoUtil.populateEdgeInfo( isPage );
		}
		catch ( IOException e )
		{
			throw e;
		}

		for ( int i = 0; i < this.dimensionAxis.length; i++ )
		{
			this.dimensionAxis[i].setEdgeInfo( edgeInfoUtil );
		}
	}
	
	/**
	 * 
	 * @return
	 */
	public EdgeInfoGenerator getEdgeInfo( )
	{
		return this.edgeInfoUtil;
	}

	/**
	 * 
	 * @param index
	 * @return
	 */
	public DimensionAxis getDimensionAxis( int index )
	{
		return dimensionAxis[index];
	}
	
	/**
	 * 
	 * @return
	 */
	public DimensionAxis[] getAllDimensionAxis( )
	{
		return this.dimensionAxis;
	}

	/**
	 * 
	 * @return
	 */
	public IAggregationResultSet getQueryResultSet( )
	{
		return rs;
	}

	/**
	 * 
	 * @return
	 */
	public EdgeInfoGenerator getEdgeInfoUtil( )
	{
		return this.edgeInfoUtil;
	}	
}
