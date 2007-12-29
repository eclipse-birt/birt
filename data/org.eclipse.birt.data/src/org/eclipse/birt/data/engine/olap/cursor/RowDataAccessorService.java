package org.eclipse.birt.data.engine.olap.cursor;

import org.eclipse.birt.data.engine.olap.data.api.IAggregationResultSet;
import org.eclipse.birt.data.engine.olap.driver.DimensionAxis;

/**
 * This class provide the available information when populating edgeInfo.
 * 
 */
public class RowDataAccessorService
{

	private int mirrorStartPosition, fetchLimit = -1;
	private IAggregationResultSet rs;
	private DimensionAxis[] dimAxis;
	private boolean isPage;

	/**
	 * 
	 * @param rs
	 * @param isPage
	 * @param dimAxis
	 * @param mirrorStartPosition
	 */
	public RowDataAccessorService( IAggregationResultSet rs, boolean isPage,
			DimensionAxis[] dimAxis, int mirrorStartPosition )
	{
		this.rs = rs;
		this.isPage = isPage;
		this.dimAxis = dimAxis;
		this.mirrorStartPosition = mirrorStartPosition;
	}

	public IAggregationResultSet getAggregationResultSet( )
	{
		return this.rs;
	}

	public boolean isPage( )
	{
		return this.isPage;
	}

	public DimensionAxis[] getDimensionAxis( )
	{
		return this.dimAxis;
	}

	public int getMirrorStartPosition( )
	{
		return this.mirrorStartPosition;
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