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
package org.eclipse.birt.data.engine.impl.document;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.core.util.IOUtil;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.executor.transform.group.GroupInfo;
import org.eclipse.birt.data.engine.executor.transform.group.GroupUtil;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;

/**
 * This class is read-only part of complete GroupUtil. Its group information
 * data will be loaded from external stream, not generated from a SmartCache.
 */
public final class RDGroupUtil
{
	/*
	 * groups[level] is an ArrayList of GroupInfo objects at the specified level.
	 * Level is a 0-based group index, with 0 denoting the outermost group, etc.
	 * Example: 
	 * Row GroupKey1 	GroupKey2 	GroupKey3 	Column4 	Column5 
	 * 0: 	CHINA 		BEIJING 	2003 		Cola 		$100 
	 * 1: 	CHINA		BEIJING		2003		Pizza 		$320 
	 * 2: 	CHINA		BEIJING		2004 		Cola 		$402 
	 * 3: 	CHINA		SHANGHAI	2003		Cola		$553 
	 * 4:	CHINA		SHANGHAI	2003		Pizza		$223
	 * 5: 	CHINA 		SHANGHAI	2004		Cola		$226
	 * 6: 	USA 		CHICAGO		2004		Pizza		$133
	 * 7: 	USA			NEW YORK	2004		Cola		$339
	 * 8: 	USA 		NEW YORK	2004		Cola		$297
	 * 
	 * groups: (parent, child) 
	 * 		LEVEL 0 		LEVEL 1 		LEVEL 2
	 * ============================================ 
	 * 0: 	-,0 			0,0 			0,0 
	 * 1: 	-,2 			0,2 			0,2 
	 * 2:	 				1,4 			1,3 
	 * 3: 					1,5 			1,5 
	 * 4: 									2,6 
	 * 5: 									3,7 
	 */
	private List[] groups;

	// index of the current innermost group
	private int leafGroupIdx = 0;

	// provide service of current data cache
	private CacheProvider cacheProvider;
	
	/**
	 * @param inputStream
	 * @param cacheProvider
	 * @throws DataException
	 */
	RDGroupUtil( InputStream inputStream, CacheProvider cacheProvider )
			throws DataException
	{
		try
		{
			int size = IOUtil.readInt( inputStream );
			this.groups = new ArrayList[size];

			for ( int i = 0; i < size; i++ )
			{
				List list = new ArrayList( );
				int asize = IOUtil.readInt( inputStream );
				for ( int j = 0; j < asize; j++ )
				{

					GroupInfo groupInfo = new GroupInfo( );
					groupInfo.parent = IOUtil.readInt( inputStream );
					groupInfo.firstChild = IOUtil.readInt( inputStream );
					list.add( groupInfo );
				}
				this.groups[i] = list;
			}
		}
		catch ( IOException e )
		{
			throw new DataException( ResourceConstants.RD_LOAD_ERROR,
					e,
					"Group Info" );
		}
		
		this.cacheProvider = cacheProvider;
	}
	
	/**
	 * @param outputStream
	 * @throws IOException
	 */
	public void saveGroupsToStream( OutputStream outputStream ) throws IOException
	{
		int size = groups.length;
		IOUtil.writeInt( outputStream, size );
		for ( int i = 0; i < size; i++ )
		{
			List list = groups[i];

			int asize = list.size( );
			IOUtil.writeInt( outputStream, asize );

			for ( int j = 0; j < asize; j++ )
			{
				GroupInfo groupInfo = (GroupInfo) list.get( j );
				IOUtil.writeInt( outputStream, groupInfo.parent );
				IOUtil.writeInt( outputStream, groupInfo.firstChild );
			}
		}
	}
	
	/**
	 * @param inputStream
	 * @param cacheProvider
	 * @throws DataException
	 */
	RDGroupUtil( InputStream inputStream ) throws DataException
	{
		this( inputStream, null );
	}
	
	/**
	 * @param cacheProvider
	 */
	public void setCacheProvider( CacheProvider cacheProvider )
	{
		this.cacheProvider = cacheProvider;
	}
	
	/**
	 * use if with care
	 * 
	 * @param groups
	 */
	public List[] getGroups( )
	{
		return this.groups;
	}

	/**
	 * use if with care
	 * 
	 * @param groups
	 */
	public void setGroups( List[] groups )
	{
		this.groups = groups;
	}
	
	// Helper function to find information about a group, given the group level
	// and the group index at that level. Returns null if groupIndex exceeds
	// max group index
	private GroupInfo findGroup( int groupLevel, int groupIndex )
	{
		if ( groupIndex >= groups[groupLevel].size( ) )
			return null;
		else
			return (GroupInfo) groups[groupLevel].get( groupIndex );
	}

	private void checkStarted( ) throws DataException
	{
		if ( cacheProvider == null )
			throw new DataException( ResourceConstants.NO_CURRENT_ROW );
	}

	private void checkHasCurrentRow( ) throws DataException
	{
		checkStarted( );
		if ( cacheProvider.getCurrentIndex( ) >= cacheProvider.getCount( ) )
			throw new DataException( ResourceConstants.NO_CURRENT_ROW );
	}

	/**
	 * Returns the 1-based index of the outermost group
	 * in which the current row is the last row. 
	 * For example, if a query contain N groups 
	 * (group with index 1 being the outermost group, and group with 
	 * index N being the innermost group),
	 * and this function returns a value M, it indicates that the 
	 * current row is the last row in groups with 
	 * indexes (M, M+1, ..., N ). 
	 * @return	The 1-based index of the outermost group in which 
	 * 			the current row is the last row;
	 * 			(N+1) if the current row is not at the end of any group;
	 */
	public int getEndingGroupLevel( ) throws DataException
	{
		checkHasCurrentRow( );

		// Always return 0 for last row (which ends group 0 - the entire list)
		if ( cacheProvider.getCurrentIndex( ) == cacheProvider.getCount( ) - 1 )
			return 0;

		// 1 is returned if no groups are defined
		if ( groups.length == 0 )
			return 1;

		// Find outermost group that current row ends
		int childGroupIdx = cacheProvider.getCurrentIndex( );
		int currentGroupIdx = leafGroupIdx;
		int level;
		for ( level = groups.length - 1; level >= 0; level-- )
		{
			// Current row is known to end child group with index childGroupIdx
			// Does it also end this group?
			GroupInfo nextGroup = findGroup( level, currentGroupIdx + 1 );
			if ( nextGroup != null && childGroupIdx == nextGroup.firstChild - 1 )
			{
				// Yes it also ends this group; check if it ends parent as well
				childGroupIdx = currentGroupIdx;
				currentGroupIdx = findGroup( level, currentGroupIdx ).parent;
				continue;
			}
			break;
		}

		// current row ends group (level +1 ). Note that the group index we
		// return is 1-based
		return level + 2;
	}

	/**
	 * Returns the 1-based index of the outermost group
	 * in which the current row is the first row. 
	 * For example, if a query contain N groups 
	 * (group with index 1 being the outermost group, and group with 
	 * index N being the innermost group),
	 * and this function returns a value M, it indicates that the 
	 * current row is the first row in groups with 
	 * indexes (M, M+1, ..., N ).
	 * @return	The 1-based index of the outermost group in which 
	 * 			the current row is the first row;
	 * 			(N+1) if the current row is not at the start of any group;
	 */
	public int getStartingGroupLevel( ) throws DataException
	{
		checkHasCurrentRow( );

		// Always return 0 for first row, which starts group 0 - the entire list
		if ( cacheProvider.getCurrentIndex( ) == 0 )
			return 0;

		// If no groups defined, return 1
		if ( groups.length == 0 )
			return 1;

		// Find outermost group that current row starts
		int childGroupIdx = cacheProvider.getCurrentIndex( );
		int currentGroupIdx = leafGroupIdx;
		int level;
		for ( level = groups.length - 1; level >= 0; level-- )
		{
			// Current row is known to start child group with index
			// childGroupIdx
			// Does it also start this group?
			GroupInfo currentGroup = findGroup( level, currentGroupIdx );
			if ( childGroupIdx == currentGroup.firstChild )
			{
				// Yes it also starts this group; check if it starts parent as
				// well
				childGroupIdx = currentGroupIdx;
				currentGroupIdx = currentGroup.parent;
				continue;
			}
			break;
		}
		// current row starts group (level +1 ). Note that the group index we
		// return is 1-based
		return level + 2;
	}

	// Finds index of current group at the specified group level
	private int findCurrentGroup( int groupLevel )
	{
		// Walk up the group chain from leaf group
		int currentGroupIdx = leafGroupIdx;
		for ( int i = groups.length - 1; i > groupLevel; i-- )
			currentGroupIdx = findGroup( i, currentGroupIdx ).parent;
		return currentGroupIdx;
	}

	/**
	 * Advances row cursor to the last row at the specified group level
	 * 
	 * @param groupLevel
	 *            the specified group level that will be skipped, 1 indicate the
	 *            highest level. 0 indicates whole list.
	 */
	public void last( int groupLevel ) throws DataException
	{
		if ( groupLevel > groups.length || groupLevel < 0 )
			throw new DataException( ResourceConstants.INVALID_GROUP_LEVEL,
					new Integer( groupLevel ) );

		groupLevel--; // change to 0-based index

		// First find current group at the specified group level
		int currentGroupIdx = -1;
		if ( groupLevel >= 0 )
			currentGroupIdx = findCurrentGroup( groupLevel );

		if ( groupLevel < 0 || // an input of 0 means moving to last row in
				// list
				currentGroupIdx >= groups[groupLevel].size( ) - 1 )
		{
			// Move to last row in entire list
			// Last row is in the last leaf group
			int currentRowID = cacheProvider.getCount( ) - 1;
			
			cacheProvider.moveTo( currentRowID );
			if ( groups.length > 0 )
				leafGroupIdx = groups[groups.length - 1].size( ) - 1;
			return;
		}

		// Find first row in the next group
		++currentGroupIdx;

		for ( int i = groupLevel + 1; i < groups.length; i++ )
		{
			currentGroupIdx = findGroup( i - 1, currentGroupIdx ).firstChild;
		}

		// Move back one row and one leaf group
		int currentRowID = findGroup( groups.length - 1, currentGroupIdx ).firstChild - 1;
		cacheProvider.moveTo( currentRowID );
		leafGroupIdx = currentGroupIdx - 1;
	}
	
	/**
	 * When the smartCache is proceed (IResultIterator.next() is called), the leafGroupIdx
	 * should be re-calculated.
	 * @param hasNext
	 * @throws DataException
	 */
	public void next( boolean hasNext ) throws DataException
	{
		// Adjust leaf group index
		// Have we advanced into the next leaf group?
		if ( hasNext == true && groups.length > 0 )
		{
			GroupInfo nextLeafGroup = findGroup( groups.length - 1,
					leafGroupIdx + 1 );
			if ( nextLeafGroup != null
					&& cacheProvider.getCurrentIndex( ) >= nextLeafGroup.firstChild )
			{
				// Move to next leaft group
				++leafGroupIdx;
			}
		}
	}
	
	/**
	 * @return
	 */
	public int getGroupLevel( )
	{
		return this.groups.length;
	}
	
	/**
	 * Gets the index of the current group at the specified group level.
	 * The index starts at 0  
	 */
	public int getCurrentGroupIndex( int groupLevel ) throws DataException
	{
		checkHasCurrentRow( );
		if ( groupLevel < 0 || groupLevel > groups.length )
			throw new DataException( ResourceConstants.INVALID_GROUP_LEVEL,
					new Integer( groupLevel ) );

		int currentGroupIdx = leafGroupIdx;
		int level;
		for ( level = groups.length - 1; level > groupLevel - 1; level-- )
		{
			GroupInfo currentGroup = findGroup( level, currentGroupIdx );
			currentGroupIdx = currentGroup.parent;
		}
		return currentGroupIdx;
	}
	
	/**
	 * For a particual group level, it might consists of several group units.
	 * For each group unit, it has its start row index and end row index, and
	 * then the total index will be the group unit number*2.
	 * 
	 * @param groupLevel
	 * @return int[]
	 */
	public int[] getGroupStartAndEndIndex( int groupLevel )
	{
		int max = -1;
		if( this.cacheProvider != null )
			max = this.cacheProvider.getCount();

		if ( groupLevel == 0 )
		{
			return new int[]{
					0,
					max
			};
		}

		int unitCountInOneGroup = this.groups[groupLevel - 1].size( );
		if ( unitCountInOneGroup == 1 )
		{
			return new int[]{
					0,
					max
			};
		}
		else
		{
			int[] unitInfo = new int[unitCountInOneGroup * 2];
			for ( int i = 0; i < unitCountInOneGroup; i++ )
			{
				int startIndex = i;
				int endIndex = startIndex + 1;

				startIndex = GroupUtil.getGroupFirstRowIndex( groupLevel,
						startIndex,
						this.groups,
						max );
				endIndex = GroupUtil.getGroupFirstRowIndex( groupLevel,
						endIndex,
						this.groups,
						max );

				unitInfo[i * 2] = startIndex;
				unitInfo[i * 2 + 1] = endIndex;
			}
			return unitInfo;
		}
	}
	
}