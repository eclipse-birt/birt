/*
 *************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *  
 *************************************************************************
 */ 
package org.eclipse.birt.data.engine.olap.data.impl.aggregation;

import java.io.IOException;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.olap.data.api.MeasureInfo;
import org.eclipse.birt.data.engine.olap.data.impl.DimColumn;
import org.eclipse.birt.data.engine.olap.data.impl.dimension.Member;

/**
 * The data provider that want to use <code>org.eclipse.birt.data.engine.olap.data.impl.aggregation.AggregationExecutor</code> to calculate aggregation
 */
public interface IDataSet4Aggregation
{
	//Meta info
	MetaInfo getMetaInfo();
	
	//Move to next row
	boolean next() throws DataException, IOException;
	
	//current row values
	Object getMeasureValue(int measureIndex) throws DataException, IOException;
	Member getMember(int dimIndex, int levelIndex) throws DataException, IOException;
	
	public static interface MetaInfo
	{
		MeasureInfo[] getMeasureInfos( ) throws IOException;
		int getMeasureIndex( String measureName ) throws IOException;
		int getDimensionIndex(String dimensionName);
		int getLevelIndex(String dimensionName, String levelName);
		ColumnInfo getColumnInfo(DimColumn dimColumn) throws DataException;
		String[] getKeyNames(int dimIndex, int levelIndex);
		String[] getAttributeNames(int dimIndex, int levelIndex);
	}
	
	public static class ColumnInfo
	{
		private int dimIndex;
		private int levelIndex;
		private int columnIndex;
		private boolean isKey;
		
		
		public ColumnInfo(int dimIndex, int levelIndex, int columnIndex,  boolean isKey)
		{
			this.dimIndex = dimIndex;
			this.levelIndex = levelIndex;
			this.columnIndex = columnIndex;
			this.isKey = isKey;
			
		}

		int getDimIndex( )
		{
			return dimIndex;
		}
		
		int getLevelIndex( )
		{
			return levelIndex;
		}
		
		int getColumnIndex( )
		{
			return columnIndex;
		}
		
		boolean isKey( )
		{
			return isKey;
		}
	}
}
