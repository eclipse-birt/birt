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

package org.eclipse.birt.data.engine.executor;

import java.util.Collection;

import org.eclipse.birt.data.engine.api.IBaseDataSetDesign;
import org.eclipse.birt.data.engine.api.IBaseDataSourceDesign;


/**
 * Wrap the design of data source and data set. This class will determins
 * whether the equality of two data sets, which will be used if data set needs
 * to be put into map. To ensure it works correctly, a comprehensive junit test
 * case should be designed for it.
 */
public class DataSourceAndDataSet
{
	/** current data source and data set for cache*/
	private IBaseDataSourceDesign dataSourceDesign;
	private IBaseDataSetDesign dataSetDesign;
	private Collection paramterHints;

	/**
	 * @param dataSourceDesign
	 * @param dataSetDesign
	 * @return
	 */
	public static DataSourceAndDataSet newInstance(
			IBaseDataSourceDesign dataSourceDesign,
			IBaseDataSetDesign dataSetDesign, Collection paramterHints )
	{
		DataSourceAndDataSet dataSourceAndSet = new DataSourceAndDataSet( );
		dataSourceAndSet.dataSourceDesign = dataSourceDesign;
		dataSourceAndSet.dataSetDesign = dataSetDesign;
		dataSourceAndSet.paramterHints = paramterHints;
		
		return dataSourceAndSet;
	}

	/*
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode( )
	{
		int hashCode = 0;
		if ( dataSourceDesign != null )
			hashCode += dataSourceDesign.getName( ).hashCode( );
		if ( dataSetDesign != null )
			hashCode += dataSetDesign.getName( ).hashCode( );
		return hashCode;
	}

	/**
	 * 
	 * 
	 * @param obj
	 * @param considerParam
	 * @return
	 */
	public boolean isDataSourceDataSetEqual( DataSourceAndDataSet obj, boolean considerParam )
	{
		IBaseDataSourceDesign dataSourceDesign2 = ( (DataSourceAndDataSet) obj ).dataSourceDesign;
		IBaseDataSetDesign dataSetDesign2 = ( (DataSourceAndDataSet) obj ).dataSetDesign;
		Collection paramterHints2 = ( (DataSourceAndDataSet) obj ).paramterHints;

		if ( this.dataSourceDesign == dataSourceDesign2 )
		{
			if ( this.dataSetDesign == dataSetDesign2 )
			{
				if ( !considerParam )
					return true;
				
				if ( ComparatorUtil.isEqualParameterHints( this.paramterHints,
						paramterHints2 ) )
					return true;
			}
			else if ( this.dataSetDesign == null || dataSetDesign2 == null )
			{
				return false;
			}
		}
		else if ( this.dataSourceDesign == null || dataSourceDesign2 == null )
		{
			return false;
		}
		else
		{
			if ( ( this.dataSetDesign != dataSetDesign2 )
					&& ( this.dataSetDesign == null || dataSetDesign2 == null ) )
				return false;
		}

		// data source compare
		if ( isEqualDataSourceDesign( dataSourceDesign, dataSourceDesign2 ) == false )
			return false;

		// data set compare
		if ( isEqualDataSetDesign( dataSetDesign, dataSetDesign2 ) == false )
			return false;

		if ( !considerParam )
			return true;
		
		// parameter bindings compare
		if ( ComparatorUtil.isEqualParameterHints( this.paramterHints,
				paramterHints2 ) == false )
			return false;

		return true;

	}
	
	/*
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals( Object obj )
	{
		if ( obj == null || obj instanceof DataSourceAndDataSet == false )
			return false;

		if ( this == obj )
			return true;
		
		return this.isDataSourceDataSetEqual((DataSourceAndDataSet)obj, true );
	}





	/**
	 * compare data source design
	 * @param dataSourceDesign
	 * @param dataSourceDesign2
	 * @return
	 */
	private boolean isEqualDataSourceDesign(
			IBaseDataSourceDesign dataSourceDesign,
			IBaseDataSourceDesign dataSourceDesign2 )
	{
		return DataSourceDesignComparator.isEqualDataSourceDesign( dataSourceDesign, dataSourceDesign2 );
	}

	/**
	 * compare data set design
	 * @param dataSetDesign
	 * @param dataSetDesign2
	 * @return
	 */
	private boolean isEqualDataSetDesign( IBaseDataSetDesign dataSetDesign,
			IBaseDataSetDesign dataSetDesign2 )
	{
		return DataSetDesignComparator.isEqualDataSetDesign( dataSetDesign, dataSetDesign2 );
	}


}
