package org.eclipse.birt.data.engine.olap.cursor;

import org.eclipse.birt.data.engine.olap.data.api.IAggregationResultSet;
import org.eclipse.birt.data.engine.olap.driver.DimensionAxis;

/**
 * This class provide the available information when populating edgeInfo.
 * 
 */
public class RowDataAccessorService
{

	private int mirrorStartPosition, pagePosition, fetchLimit = -1;
	private IAggregationResultSet rs;
	private DimensionAxis[] dimAxis;

	/**
	 * 
	 * @param rs
	 * @param isPage
	 * @param dimAxis
	 * @param mirrorStartPosition
	 */
	public RowDataAccessorService( IAggregationResultSet rs,
			DimensionAxis[] dimAxis, int mirrorStartPosition, int pagePosition )
	{
		this.rs = rs;
		this.dimAxis = dimAxis;
		this.mirrorStartPosition = mirrorStartPosition;
		this.pagePosition = pagePosition;
	}

	public IAggregationResultSet getAggregationResultSet( )
	{
		return this.rs;
	}

	public DimensionAxis[] getDimensionAxis( )
	{
		return this.dimAxis;
	}

	public int getMirrorStartPosition( )
	{
		return this.mirrorStartPosition;
	}
	
	public int getPagePosition()
	{
		return this.pagePosition;
	}
	
	public int getFetchSize( )
	{
		return fetchLimit;
	}
	
	public void setFetchSize( int fetchSize )
	{
		this.fetchLimit = fetchSize;	
	}
}