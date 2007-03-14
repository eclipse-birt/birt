
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
package org.eclipse.birt.data.engine.olap.data.impl.facttable;

import org.eclipse.birt.data.engine.olap.data.document.IDocumentManager;

/**
 * This class describes a fact table.
 */

public class FactTable
{
	private String name;
	private DimensionInfo[] dimensionInfo;
	private MeasureInfo[] measureInfo;
	private int segmentCount;
	private DimensionDivision[] dimensionDivision;
	
	private IDocumentManager documentManager;
	private CombinedPositionContructor combinedPositionCalculator;
	
	/**
	 * 
	 * @param name
	 * @param documentManager
	 * @param dimensionInfo
	 * @param measureInfo
	 * @param segmentCount
	 * @param dimensionDivision
	 */
	FactTable( String name, IDocumentManager documentManager, DimensionInfo[] dimensionInfo,
			MeasureInfo[] measureInfo, int segmentCount,
			DimensionDivision[] dimensionDivision )
	{
		this.name = name;
		this.dimensionInfo = dimensionInfo;
		this.measureInfo = measureInfo;
		this.segmentCount = segmentCount;
		this.dimensionDivision = dimensionDivision;
		this.documentManager = documentManager;
		this.combinedPositionCalculator = new CombinedPositionContructor( dimensionDivision );
	}
	
	/**
	 * 
	 * @return
	 */
	public DimensionDivision[] getDimensionDivision( )
	{
		return dimensionDivision;
	}

	/**
	 * 
	 * @return
	 */
	public CombinedPositionContructor getCombinedPositionCalculator( )
	{
		return combinedPositionCalculator;
	}
	

	/**
	 * 
	 * @return
	 */
	public DimensionInfo[] getDimensionInfo( )
	{
		return dimensionInfo;
	}
	

	/**
	 * 
	 * @return
	 */
	public MeasureInfo[] getMeasureInfo( )
	{
		return measureInfo;
	}
	

	/**
	 * 
	 * @return
	 */
	public int getSegmentCount( )
	{
		return segmentCount;
	}
	

	/**
	 * 
	 * @return
	 */
	public DimensionDivision[] getSubDimensions( )
	{
		return dimensionDivision;
	}
	

	/**
	 * 
	 * @return
	 */
	public IDocumentManager getDocumentManager( )
	{
		return documentManager;
	}
	
	
	/**
	 * 
	 * @param dimensionName
	 * @return
	 */
	public int getDimensionIndex( String dimensionName )
	{
		for ( int i = 0; i < dimensionInfo.length; i++ )
		{
			if(dimensionInfo[i].dimensionName.equals( dimensionName ))
			{
				return i;
			}
		}
		return -1;
	}
	
	/**
	 * 
	 * @param measureName
	 * @return
	 */
	public int getMeasureIndex( String measureName )
	{
		for ( int i = 0; i < measureInfo.length; i++ )
		{
			if(measureInfo[i].measureName.equals( measureName ))
			{
				return i;
			}
		}
		return -1;
	}

	/**
	 * 
	 * @return
	 */
	public String getName( )
	{
		return name;
	}

}
