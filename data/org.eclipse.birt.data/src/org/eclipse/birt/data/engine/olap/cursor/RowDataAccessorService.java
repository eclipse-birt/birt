package org.eclipse.birt.data.engine.olap.cursor;

import org.eclipse.birt.data.engine.olap.api.query.ILevelDefinition;
import org.eclipse.birt.data.engine.olap.api.query.IMirroredDefinition;
import org.eclipse.birt.data.engine.olap.driver.DimensionAxis;
import org.eclipse.birt.data.engine.olap.query.view.BirtEdgeView;
import org.eclipse.birt.data.engine.olap.query.view.CubeQueryDefinitionUtil;

/**
 * This class provide the available information when populating edgeInfo.
 * 
 */
public class RowDataAccessorService
{

	private int fetchLimit = -1;
	private DimensionAxis[] dimAxis;
	private BirtEdgeView view;

	/**
	 * 
	 * @param rs
	 * @param isPage
	 * @param dimAxis
	 * @param mirrorStartPosition
	 */
	public RowDataAccessorService( DimensionAxis[] dimAxis, BirtEdgeView view )
	{
		this.dimAxis = dimAxis;
		this.view = view;
	}

	public DimensionAxis[] getDimensionAxis( )
	{
		return this.dimAxis;
	}

	public int getMirrorStartPosition( )
	{
		int index = 0;
		if ( view.getMirroredDefinition( ) != null )
		{
			IMirroredDefinition mirror = view.getMirroredDefinition( );
			ILevelDefinition[] levelArray = CubeQueryDefinitionUtil.getLevelsOnEdge( view.getEdgeDefinition( ) );
			for ( int i = 0; i < levelArray.length; i++ )
			{
				if ( levelArray[i].equals( mirror.getMirrorStartingLevel( ) ) )
				{
					if ( view.getPageEndingIndex( ) >= 0 )
						index = i + view.getPageEndingIndex( ) + 1;
					else
						index = i;
					break;
				}
			}
			return index;
		}
		else
			return index;
	}
	
	public boolean isBreakHierarchy( )
	{
		if( view.getMirroredDefinition( )!= null )
		{
			return view.getMirroredDefinition( ).isBreakHierarchy( );
		}
		else
			return false;
	}
	
	public int getPagePosition( )
	{
		return this.view.getPageEndingIndex( );
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