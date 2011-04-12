package org.eclipse.birt.data.engine.impl.document;

import org.eclipse.birt.data.engine.core.DataException;


public interface IRDGroupUtil
{
	public void setCacheProvider( CacheProvider cacheProvider );
	public void next( boolean hasNext ) throws DataException;
	public int getCurrentGroupIndex( int groupLevel ) throws DataException;
	public void move( ) throws DataException;
	public int getEndingGroupLevel( ) throws DataException;
	public int getStartingGroupLevel( ) throws DataException;
	public void last( int groupLevel ) throws DataException;
	public void close( ) throws DataException;
	public int[] getGroupStartAndEndIndex( int groupIndex ) throws DataException;
}
