/*******************************************************************************
 * Copyright (c) 2004, 2009 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/
package org.eclipse.birt.data.engine.olap.cursor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.eclipse.birt.data.engine.olap.api.query.IEdgeDrillFilter;
import org.eclipse.birt.data.engine.olap.data.api.DimLevel;
import org.eclipse.birt.data.engine.olap.data.api.IAggregationResultRow;
import org.eclipse.birt.data.engine.olap.data.api.IAggregationResultSet;
import org.eclipse.birt.data.engine.olap.data.impl.AggregationDefinition;
import org.eclipse.birt.data.engine.olap.data.impl.aggregation.AggregationResultRow;
import org.eclipse.birt.data.engine.olap.data.impl.aggregation.AggregationResultSet;
import org.eclipse.birt.data.engine.olap.data.impl.dimension.Member;
import org.eclipse.birt.data.engine.olap.data.util.BufferedStructureArray;
import org.eclipse.birt.data.engine.olap.data.util.DataType;
import org.eclipse.birt.data.engine.olap.data.util.IDiskArray;


public class DrilledAggregateResultSet implements IAggregationResultSet
{

	private static Logger logger = Logger.getLogger( DrilledAggregateResultSet.class.getName( ) );

	private List drillsDefn, drillsRs;
	private int levelCount = 0;
	private int currentPosition;
	private IDiskArray bufferedStructureArray;
	private IAggregationResultRow resultObject;
	private IAggregationResultSet metaResultSet;
	private int[][] attributeDataTypes;
	private int[][] keyDataTypes;

	//merge the drill result set with base result into bufferdStructureArray.
	public DrilledAggregateResultSet( IAggregationResultSet baseResultSet,
			List drillsRs, List drillsDefn ) throws IOException
	{
		Object[] params = {
				baseResultSet, drillsRs, drillsDefn
		};
		logger.entering( AggregationResultSet.class.getName( ),
				"MergedAggregateResultSet",
				params );

		this.drillsDefn = drillsDefn;
		this.drillsRs = drillsRs;
		this.levelCount = baseResultSet.getLevelCount( );
		this.metaResultSet = baseResultSet;
		for ( int i = 0; i < drillsRs.size( ); i++ )
		{
			if ( levelCount < ( (IAggregationResultSet) drillsRs.get( i ) ).getLevelCount( ) )
			{
				metaResultSet = ( (IAggregationResultSet) drillsRs.get( i ) );
				levelCount = metaResultSet.getLevelCount( );
			}
		}

		bufferedStructureArray = new BufferedStructureArray( Member.getCreator( ),
				2000 );
		
		List currentMemberList;
		List previewMemberList = new ArrayList( );
		for ( int i = 0; i < baseResultSet.length( ); i++ )
		{
			baseResultSet.seek( i );
			IAggregationResultRow row = baseResultSet.getCurrentRow( );

			currentMemberList = getDrilledMemberList( row.getLevelMembers( ),
					drillsRs,
					drillsDefn,
					0 );
			
			boolean addToBuffer = false;
			if ( previewMemberList.size( ) == currentMemberList.size( ) )
			{
				for ( int t1 = 0; t1 < currentMemberList.size( ); t1++ )
				{
					Member[] previousMember = (Member[]) previewMemberList.get( t1 );
					Member[] currentMember = (Member[]) currentMemberList.get( t1 );
					if ( previousMember.length == currentMember.length )
					{
						for ( int t2 = 0; t2 < previousMember.length; t2++ )
						{
							if ( !previousMember[t2].equals( currentMember[t2] ) )
							{
								addToBuffer = true;
								break;
							}
						}
					}
					else
					{
						addToBuffer = true;
					}
					if ( addToBuffer )
						break;
				}
			}
			else
			{
				addToBuffer = true;
			}

			if ( addToBuffer )
			{
				for ( int j = 0; j < currentMemberList.size( ); j++ )
				{
					bufferedStructureArray.add( new AggregationResultRow( (Member[]) currentMemberList.get( j ),
							null ) );
				}
				previewMemberList.clear( );
				previewMemberList.addAll( currentMemberList );
			}
		}
		
		this.resultObject = (IAggregationResultRow) bufferedStructureArray.get( 0 );
		if ( resultObject.getLevelMembers( ) != null )
		{
			keyDataTypes = new int[resultObject.getLevelMembers( ).length][];
			attributeDataTypes = new int[resultObject.getLevelMembers( ).length][];

			for ( int i = 0; i < resultObject.getLevelMembers( ).length; i++ )
			{
				keyDataTypes[i] = new int[resultObject.getLevelMembers( )[i].getKeyValues( ).length];
				for ( int j = 0; j < resultObject.getLevelMembers( )[i].getKeyValues( ).length; j++ )
				{
					keyDataTypes[i][j] = DataType.getDataType( resultObject.getLevelMembers( )[i].getKeyValues( )[j].getClass( ) );
				}
				if ( resultObject.getLevelMembers( )[i].getAttributes( ) != null )
				{
					attributeDataTypes[i] = new int[resultObject.getLevelMembers( )[i].getAttributes( ).length];

					for ( int j = 0; j < attributeDataTypes[i].length; j++ )
					{
						if ( resultObject.getLevelMembers( )[i].getAttributes( )[j] != null )
							attributeDataTypes[i][j] = DataType.getDataType( resultObject.getLevelMembers( )[i].getAttributes( )[j].getClass( ) );
					}
				}
			}
		}
	}

	private List getDrilledMemberList( Member[] member, List drillRs,
			List drillDefn, int startSearchIndex ) throws IOException
	{
		boolean matched = false;
		List result = new ArrayList( );
		for ( int i = startSearchIndex; i < drillsDefn.size( ); i++ )
		{
			IEdgeDrillFilter defn = (IEdgeDrillFilter) drillsDefn.get( i );
			for ( int j = 0; j < defn.getTuple( ).size( ); j++ )
			{
				Member drillMember = new Member( );
				drillMember.setKeyValues( (Object[]) defn.getTuple( ).toArray( )[j] );
				if ( member[j] == null || !member[j].equals( drillMember ) )
				{
					matched = false;
					break;
				}
				matched = true;
			}
			if ( matched )
			{
				IAggregationResultSet rs = ( (IAggregationResultSet) drillRs.get( i ) );
				for ( int k = 0; k < rs.length( ); k++ )
				{
					rs.seek( k );
					IAggregationResultRow row = rs.getCurrentRow( );

					Member[] drillMember = new Member[rs.getLevelCount( )];
					System.arraycopy( row.getLevelMembers( ),
							0,
							drillMember,
							0,
							row.getLevelMembers( ).length );

					result.addAll( getDrilledMemberList( drillMember,
							drillsRs,
							drillsDefn, i + 1 ) );
				}
				return result;
			}
		}
		if ( !matched )
		{
			result.add( member );
		}
		return result;
	}

	public void clear( ) throws IOException
	{
		bufferedStructureArray.clear( );
	}

	public void close( ) throws IOException
	{
		bufferedStructureArray.close( );
	}

	public int getAggregationCount( )
	{
		return 0;
	}

	public int getAggregationDataType( int aggregationIndex )
			throws IOException
	{
		return 0;
	}

	public int[] getAggregationDataType( )
	{
		return null;
	}

	public AggregationDefinition getAggregationDefinition( )
	{
		return null;
	}

	public int getAggregationIndex( String name ) throws IOException
	{
		return 0;
	}

	public String getAggregationName( int index )
	{
		return null;
	}

	public Object getAggregationValue( int aggregationIndex )
			throws IOException
	{
		return null;
	}

	public DimLevel[] getAllLevels( )
	{
		return metaResultSet.getAllLevels( );
	}

	public String[][] getAttributeNames( )
	{
		return metaResultSet.getAttributeNames( );
	}

	public IAggregationResultRow getCurrentRow( ) throws IOException
	{
		return this.resultObject;
	}

	public String[][] getKeyNames( )
	{
		return metaResultSet.getKeyNames( );
	}

	public DimLevel getLevel( int levelIndex )
	{
		return metaResultSet.getLevel( levelIndex );
	}

	public Object getLevelAttribute( int levelIndex, int attributeIndex )
	{
		return metaResultSet.getLevelAttribute( levelIndex, attributeIndex );
	}

	public int getLevelAttributeColCount( int levelIndex )
	{
		return metaResultSet.getLevelAttributeColCount( levelIndex );
	}

	public int getLevelAttributeDataType( DimLevel level, String attributeName )
	{
		int levelIndex = getLevelIndex( level );
		if ( attributeDataTypes == null || attributeDataTypes[levelIndex] == null )
		{
			return DataType.UNKNOWN_TYPE;
		}
		return this.attributeDataTypes[levelIndex][getLevelAttributeIndex( level,
				attributeName )];
	}

	public int getLevelAttributeDataType( int levelIndex, String attributeName )
	{
		return metaResultSet.getLevelAttributeDataType( levelIndex, attributeName );
	}

	public int[][] getLevelAttributeDataType( )
	{
		return this.attributeDataTypes;
	}

	public int getLevelAttributeIndex( int levelIndex, String attributeName )
	{
		return metaResultSet.getLevelAttributeIndex( levelIndex, attributeName );
	}

	public int getLevelAttributeIndex( DimLevel level, String attributeName )
	{
		return metaResultSet.getLevelAttributeIndex( level, attributeName );
	}

	public String[] getLevelAttributes( int levelIndex )
	{
		return metaResultSet.getLevelAttributes( levelIndex );
	}

	public String[][] getLevelAttributes( )
	{
		return metaResultSet.getAttributeNames( );
	}

	public int getLevelCount( )
	{
		return this.levelCount;
	}

	public int getLevelIndex( DimLevel level )
	{
		return metaResultSet.getLevelIndex( level );
	}

	public int getLevelKeyColCount( int levelIndex )
	{
		return metaResultSet.getLevelKeyColCount( levelIndex );
	}

	public int getLevelKeyDataType( DimLevel level, String keyName )
	{
		return metaResultSet.getLevelKeyDataType( level, keyName );
	}

	public int getLevelKeyDataType( int levelIndex, String keyName )
	{
		return metaResultSet.getLevelKeyDataType( levelIndex, keyName );
	}

	public int[][] getLevelKeyDataType( )
	{
		return metaResultSet.getLevelKeyDataType( );
	}

	public int getLevelKeyIndex( int levelIndex, String keyName )
	{
		return metaResultSet.getLevelKeyIndex( levelIndex, keyName );
	}

	public int getLevelKeyIndex( DimLevel level, String keyName )
	{
		return metaResultSet.getLevelKeyIndex( level, keyName );
	}

	public String getLevelKeyName( int levelIndex, int keyIndex )
	{
		return metaResultSet.getLevelKeyName( levelIndex, keyIndex );
	}

	public Object[] getLevelKeyValue( int levelIndex )
	{
		if ( resultObject.getLevelMembers( ) == null
				|| levelIndex < 0
				|| levelIndex > resultObject.getLevelMembers( ).length - 1 )
		{
			return null;
		}
		return resultObject.getLevelMembers()[levelIndex].getKeyValues();
	}

	public String[][] getLevelKeys( )
	{
		return metaResultSet.getLevelKeys( );
	}

	public int getPosition( )
	{
		return currentPosition;
	}

	public int getSortType( int levelIndex )
	{
		return metaResultSet.getSortType( levelIndex );
	}

	public int[] getSortType( )
	{
		return metaResultSet.getSortType( );
	}

	public int length( )
	{
		return bufferedStructureArray.size( );
	}

	public void seek( int index ) throws IOException
	{
		if ( index >= bufferedStructureArray.size( ) )
		{
			throw new IndexOutOfBoundsException( "Index: "
					+ index + ", Size: " + bufferedStructureArray.size( ) );
		}
		currentPosition = index;
		resultObject = (IAggregationResultRow) bufferedStructureArray.get( index );
	}

	public Object[] getLevelAttributesValue( int levelIndex )
	{
		return metaResultSet.getLevelAttributesValue( levelIndex );
	}

}
