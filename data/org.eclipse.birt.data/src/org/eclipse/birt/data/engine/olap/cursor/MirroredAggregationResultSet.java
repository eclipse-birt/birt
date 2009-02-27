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
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.eclipse.birt.data.engine.olap.data.api.DimLevel;
import org.eclipse.birt.data.engine.olap.data.api.IAggregationResultRow;
import org.eclipse.birt.data.engine.olap.data.api.IAggregationResultSet;
import org.eclipse.birt.data.engine.olap.data.api.IDimensionSortDefn;
import org.eclipse.birt.data.engine.olap.data.impl.AggregationDefinition;
import org.eclipse.birt.data.engine.olap.data.impl.aggregation.sort.AggrSortDefinition;

/**
 * This class is a wrapper class of AggregationResultSet in the case of using
 * mirror feature. The mirror is divided into 2 types: one is breakHierarchy,
 * another is no breakHierarchy. For the first one, we just use MemberTreeNode
 * for those axis who is not mirrored. For the second case, we just map all
 * members into one search tree
 * 
 */
public class MirroredAggregationResultSet implements IAggregationResultSet
{

	private IAggregationResultSet rs = null;
	private int mirrorLevel;
	private boolean breakHierarchy = false;
	private int length = 0, position = -1;
	private Object[] resultObject;
	private MemberTreeNode rootNode;
	private List[] mirrorValue;
	private List sortList;
	
	public MirroredAggregationResultSet( IAggregationResultSet rs, int mirrorLevel,
			boolean breakHierarchy, List sortList ) throws IOException
	{
		this.mirrorLevel = mirrorLevel;
		this.breakHierarchy = breakHierarchy;
		this.rootNode = new MemberTreeNode( "ROOT" );
		this.resultObject = new Object[ rs.getLevelCount( )];
		this.rs = rs;
		this.sortList = sortList;
		
		if ( this.breakHierarchy )
		{
			this.mirrorValue = new ArrayList[rs.getLevelCount( ) - mirrorLevel];

			for ( int j = 0; j < mirrorValue.length; j++ )
			{
				mirrorValue[j] = new ArrayList( );
			}
			preparedMemberBreakHierarchy( );
		}
		else
		{
			populateMemberTreeNoHierarchy( );
			this.length = getLength( this.rootNode );
		}
	}
	
	/**
	 * 
	 * @param levelIndex
	 * @return
	 */
	private AggrSortDefinition findAggregationSort( int levelIndex )
	{
		AggrSortDefinition aggrSortDefn = null;

		if ( this.sortList != null )
		{
			DimLevel level = this.rs.getLevel( levelIndex );
			for ( int i = 0; i < this.sortList.size( ); i++ )
			{
				AggrSortDefinition defn = (AggrSortDefinition) sortList.get( i );
				if ( level.equals( defn.getTargetLevel( ) ) )
				{
					aggrSortDefn = defn;
					break;
				}
			}
		}
		return aggrSortDefn;
	}

	private void populateMemberTreeNoHierarchy( )
			throws IOException
	{
		MemberTreeNode parent;
		MemberTreeNode child;
		Object[] preValue = new Object[rs.getLevelCount( )];
		Object[] currValue = new Object[rs.getLevelCount( )];

		for ( int i = 0; i < rs.length( ); i++ )
		{
			rs.seek( i );
			parent = this.rootNode;

			for ( int j = 0; j < rs.getLevelCount( ); j++ )
			{
				currValue[j] = rs.getLevelKeyValue( j )[0];
			}
			
			for ( int j = 0; j < rs.getLevelCount( ); j++ )
			{
				if ( !isEqualObject( preValue[j], currValue[j] ) )
				{
					if ( !parent.containsChild( currValue[j] ) )
					{
						child = new MemberTreeNode( currValue[j] );
						parent.insertNode( child );
						child.parentNode = parent;
						if ( j >= this.mirrorLevel )
						{
							populateChild( child, parent, j - 1 );
						}
						parent = child;
					}
					else
					{
						parent = parent.getChild( currValue[j] );
					}
				}
				else
				{
					if ( parent.containsChild( currValue[j] ) )
						parent = parent.getChild( currValue[j] );
					else
					{
						child = new MemberTreeNode( currValue[j] );
						parent.insertNode( child );
						child.parentNode = parent;
						if ( j >= this.mirrorLevel )
						{
							populateChild( child, parent, j - 1 );
						}
						parent = child;
					}
				}
			}

			for ( int k = 0; k <  rs.getLevelCount( ); k++ )
			{
				preValue[k] = currValue[k];
			}
		}
		
		for ( int k = mirrorLevel; k < rs.getLevelCount( ); k++ )
		{
			final int sortType = this.getSortTypeOnMirroredLevel( k );

			if ( sortType != IDimensionSortDefn.SORT_UNDEFINED )
			{
				int level = 1;
				List nodeList1 = new ArrayList( );
				List nodeList2 = new ArrayList( );
				nodeList1.addAll( this.rootNode.childNodesList );
				while ( level != k )
				{
					{

						for ( int j = 0; j < nodeList1.size( ); j++ )
						{
							nodeList2.addAll( ( (MemberTreeNode) nodeList1.get( j ) ).childNodesList );

						}
						nodeList1.clear( );
						nodeList1.addAll( nodeList2 );
						nodeList2.clear( );
					}
					level++;
				}
				if ( level == k )
				{
					for ( int j = 0; j < nodeList1.size( ); j++ )
					{
						MemberTreeNode node = (MemberTreeNode) nodeList1.get( j );

						Collections.sort( node.childNodesList,
								new Comparator( ) {

									public int compare( final Object arg0,
											final Object arg1 )
									{
										if ( sortType == IDimensionSortDefn.SORT_ASC )
											return ( (Comparable) ( (MemberTreeNode) arg0 ).key ).compareTo( ( (MemberTreeNode) arg1 ).key );
										else
											return ( (Comparable) ( (MemberTreeNode) arg0 ).key ).compareTo( ( (MemberTreeNode) arg1 ).key )
													* -1;
									}
								} );
					}
				}
			}
		}
	}

	private void preparedMemberBreakHierarchy( ) throws IOException
	{
		MemberTreeNode parent;
		MemberTreeNode child;
		Object[] preValue = new Object[mirrorLevel];
		Object[] currValue = new Object[mirrorLevel];

		for ( int i = 0; i < rs.length( ); i++ )
		{
			rs.seek( i );
			parent = this.rootNode;

			for ( int j = 0; j < mirrorLevel; j++ )
			{
				currValue[j] = rs.getLevelKeyValue( j )[0];
			}
			for ( int j = 0; j < mirrorLevel; j++ )
			{
				if ( !isEqualObject( preValue[j], currValue[j] ) )
				{
					child = new MemberTreeNode( currValue[j] );
					parent.insertNode( child );
					child.parentNode = parent;
					parent = child;
				}
				else
				{
					if ( parent.childNodesList.size( ) > 0 )
						parent = (MemberTreeNode) parent.childNodesList
								.get( parent.childNodesList.size( ) - 1 );
					else
					{
						child = new MemberTreeNode( currValue[j] );
						parent.insertNode( child );
						child.parentNode = parent;
						parent = child;
					}
				}
			}

			for ( int j = 0; j < mirrorValue.length; j++ )
			{
				if ( !mirrorValue[j].contains( rs.getLevelKeyValue( j
						+ mirrorLevel )[0] ) )
					mirrorValue[j].add( rs.getLevelKeyValue( j
							+ mirrorLevel )[0] );
			}

			for ( int k = 0; k < mirrorLevel; k++ )
			{
				preValue[k] = currValue[k];
			}
		}
		this.length = getLength( this.rootNode );
		for ( int k = 0; k < mirrorValue.length; k++ )
		{
			final int sortType = getSortTypeOnMirroredLevel( k + mirrorLevel );
			if ( sortType != IDimensionSortDefn.SORT_UNDEFINED )
			{
				Collections.sort( mirrorValue[k], new Comparator( ) {

					public int compare( final Object arg0, final Object arg1 )
					{
						if ( sortType == IDimensionSortDefn.SORT_ASC )
							return ( (Comparable) arg0 ).compareTo( arg1 );
						else
							return ( (Comparable) arg0 ).compareTo( arg1 ) * -1;
					}
				} );
			}
			this.length *= mirrorValue[k].size( );
		}
	}
	
	private int getSortTypeOnMirroredLevel( int level )
	{
		AggrSortDefinition aggrSort = this.findAggregationSort( level );
		int sortType;

		if ( aggrSort != null )
		{
			if ( aggrSort.getAxisQualifierLevel( ).length == 0 )
				sortType = aggrSort.getSortDirection( );
			else
				sortType = IDimensionSortDefn.SORT_UNDEFINED;
		}
		else
		{
			sortType = this.rs.getSortType( level );
			if ( sortType == IDimensionSortDefn.SORT_UNDEFINED )
				sortType = IDimensionSortDefn.SORT_ASC;
		}
		return sortType;
	}

	private void populateChild( MemberTreeNode child, MemberTreeNode parent, int level )
	{
		int deep = level;
		List temp1 = new ArrayList( );
		List temp2 = new ArrayList( );
		temp1.addAll( this.rootNode.childNodesList );
		while ( deep-- > 0 )
		{
			temp2.clear( );
			for ( int i = 0; i < temp1.size( ); i++ )
			{
				temp2.addAll( ( (MemberTreeNode) temp1.get( i ) ).childNodesList );
			}
			temp1.clear( );
			temp1.addAll( temp2 );
		}
		for ( int i = 0; i < temp2.size( ); i++ )
		{
			MemberTreeNode node = (MemberTreeNode) temp2.get( i );
			if ( needAddIntoTree( parent, node, level + 1 ) )
			{
				if ( !node.containsChild( child.key ) )
				{
					MemberTreeNode cloneNode = new MemberTreeNode( child.key );
					node.insertNode( cloneNode );
					cloneNode.parentNode = node;
				}
				processChildren( node, parent, child );
			}
		}
	}

	private boolean needAddIntoTree( MemberTreeNode parent,
			MemberTreeNode node, int nodeIndex )
	{
		if ( node == parent )
			return false;
		else
		{
			boolean flag = true;
			MemberTreeNode node1 = node;
			MemberTreeNode node2 = parent;

			for ( int i = this.mirrorLevel; i <= nodeIndex; i++ )
			{
				if ( !node1.key.equals( node2.key ) )
				{
					flag = false;
					break;
				}
				else
				{
					node1 = node1.parentNode;
					node2 = node2.parentNode;
				}
			}
			return flag;

		}
	}
	
	private void processChildren( MemberTreeNode node, MemberTreeNode parent, MemberTreeNode child )
	{
		for ( int k = 0; k < node.childNodesList.size( ); k++ )
		{
			if ( !parent.containsChild( ( (MemberTreeNode) node.childNodesList
					.get( k ) ).key ) )
			{
				MemberTreeNode cloneNode;
				MemberTreeNode nodeChild = ( (MemberTreeNode) node.childNodesList
						.get( k ) );
				cloneNode = new MemberTreeNode( nodeChild.key );
				
				parent.insertNode( cloneNode );
				cloneNode.parentNode = parent;
				
				populateAllChildren(  cloneNode, nodeChild );
			}
		}
	}

	private void populateAllChildren( MemberTreeNode target,
			MemberTreeNode source )
	{
		MemberTreeNode targetNode = target;
		for ( int i = 0; i < source.childNodesList.size( ); i++ )
		{
			MemberTreeNode source1 = (MemberTreeNode) source.childNodesList
					.get( i );
			MemberTreeNode c = new MemberTreeNode( source1.key );
			targetNode.insertNode( c );
			c.parentNode = targetNode;
			populateAllChildren( c, source1 );
		}
	}
	
	private int getLength( MemberTreeNode node )
	{
		int length = 0;
		for ( int i = 0; i < node.childNodesList.size( ); i++ )
		{
			MemberTreeNode child = (MemberTreeNode) node.childNodesList.get( i );
			if ( child.childNodesList.size( ) == 0 )
			{
				length++;
			}
			else
			{
				length += getLength( child );
			}
		}
		return length;
	}

	private boolean isEqualObject( Object preValue, Object currentValue )
	{
		if ( preValue == currentValue )
		{
			return true;
		}
		if ( preValue == null || currentValue == null )
		{
			return false;
		}
		return preValue.equals( currentValue );
	}

	public void clear( ) throws IOException
	{
		rs.clear( );
	}

	public void close( ) throws IOException
	{
		this.rs.close( );
	}

	public int getAggregationCount( )
	{
		return this.rs.getAggregationCount( );
	}

	public int getAggregationDataType( int aggregationIndex )
			throws IOException
	{
		return this.rs.getAggregationDataType( aggregationIndex );
	}

	public AggregationDefinition getAggregationDefinition( )
	{
		return this.rs.getAggregationDefinition( );
	}

	public int getAggregationIndex( String name ) throws IOException
	{
		return this.rs.getAggregationIndex( name );
	}

	public String getAggregationName( int index )
	{
		return this.rs.getAggregationName( index );
	}

	public Object getAggregationValue( int aggregationIndex )
			throws IOException
	{
		return this.rs.getAggregationValue( aggregationIndex );
	}

	public String[][] getAttributeNames( )
	{
		return this.rs.getAttributeNames( );
	}

	public DimLevel[] getAllLevels( )
	{
		return this.rs.getAllLevels( );
	}

	public IAggregationResultRow getCurrentRow( ) throws IOException
	{
		return rs.getCurrentRow( );
	}

	public String[][] getKeyNames( )
	{
		return this.rs.getKeyNames( );
	}

	public DimLevel getLevel( int levelIndex )
	{
		return this.rs.getLevel( levelIndex );
	}

	public Object getLevelAttribute( int levelIndex, int attributeIndex )
	{
		return this.rs.getLevelAttribute( levelIndex, attributeIndex );
	}

	public int getLevelAttributeColCount( int levelIndex )
	{
		return this.rs.getLevelAttributeColCount( levelIndex );
	}

	public int getLevelAttributeDataType( DimLevel level, String attributeName )
	{
		return this.rs.getLevelAttributeDataType( level, attributeName );
	}

	public int getLevelAttributeDataType( int levelIndex, String attributeName )
	{
		return this.rs.getLevelAttributeDataType( levelIndex, attributeName );
	}

	public int getLevelAttributeIndex( int levelIndex, String attributeName )
	{
		return this.rs.getLevelAttributeIndex( levelIndex, attributeName );
	}

	public int getLevelAttributeIndex( DimLevel level, String attributeName )
	{
		return this.rs.getLevelAttributeIndex( level, attributeName );
	}

	public String[] getLevelAttributes( int levelIndex )
	{
		return this.rs.getLevelAttributes( levelIndex );
	}

	public int getLevelCount( )
	{
		return this.rs.getLevelCount( );
	}

	public int getLevelIndex( DimLevel level )
	{
		return this.rs.getLevelIndex( level );
	}

	public int getLevelKeyColCount( int levelIndex )
	{
		return this.rs.getLevelKeyColCount( levelIndex );
	}

	public int getLevelKeyDataType( DimLevel level, String keyName )
	{
		return this.rs.getLevelKeyDataType( level, keyName );
	}

	public int getLevelKeyDataType( int levelIndex, String keyName )
	{
		return this.rs.getLevelKeyDataType( levelIndex, keyName );
	}

	public int getLevelKeyIndex( int levelIndex, String keyName )
	{
		return this.rs.getLevelKeyIndex( levelIndex, keyName );
	}

	public int getLevelKeyIndex( DimLevel level, String keyName )
	{
		return this.rs.getLevelKeyIndex( level, keyName );
	}

	public String getLevelKeyName( int levelIndex, int keyIndex )
	{
		return this.rs.getLevelKeyName( levelIndex, keyIndex );
	}

	public Object[] getLevelKeyValue( int levelIndex )
	{
		return new Object[]{
			this.resultObject[levelIndex]
		};
	}

	public int getPosition( )
	{
		return this.position;
	}

	public int getSortType( int levelIndex )
	{
		return this.rs.getSortType( levelIndex );
	}

	public int length( )
	{
		return length;
	}

	public void seek( int index ) throws IOException
	{
		this.position = index;
		if( this.breakHierarchy )
		{
			int remainder = 0, number, mirrorPlus = 1;
			for ( int j = 0; j < this.mirrorValue.length; j++ )
			{
				mirrorPlus *= mirrorValue[j].size( );
			}
			number = (int) Math.floor( index / mirrorPlus );
			remainder = index % mirrorPlus;

			MemberTreeNode node = findOuterMostChild( this.rootNode,
					number + 1,
					0 );

			for ( int j = mirrorLevel - 1; j >= 0; j-- )
			{
				this.resultObject[j] = node.key;
				node = node.parentNode;
			}

			for ( int i = this.mirrorLevel; i < this.rs.getLevelCount( ); i++ )
			{
				mirrorPlus = 1;
				if ( i < rs.getLevelCount( ) - 1 )
				{
					for ( int j = i + 1; j < rs.getLevelCount( ); j++ )
					{
						mirrorPlus *= mirrorValue[j - mirrorLevel].size( );
					}
					number = (int) Math.floor( remainder / mirrorPlus );
				}
				else
				{
					number = remainder;
				}
				this.resultObject[i] = this.mirrorValue[i - this.mirrorLevel].get( number );
				remainder = remainder % mirrorPlus;
			}
		}
		else
		{
			MemberTreeNode node = findOuterMostChild( this.rootNode,
					index + 1,
					0 );

			for ( int j = rs.getLevelCount( ) - 1; j >= 0; j-- )
			{
				this.resultObject[j] = node.key;
				node = node.parentNode;
			}			
		}
	}
	
	private MemberTreeNode findOuterMostChild( MemberTreeNode node, int index,
			int startIndex )
	{
		int temp = startIndex;
		for ( int i = 0; i < node.childNodesList.size( ); i++ )
		{
			MemberTreeNode child = (MemberTreeNode) node.childNodesList.get( i );
			if ( child.childNodesList.size( ) == 0 )
			{
				temp++;
				if ( temp == index )
					return child;
			}
			else
			{
				MemberTreeNode find = findOuterMostChild( child, index, temp );
				if ( find != null )
					return find;
				else
					temp += this.getLength( child );
			}
		}
		return null;
	}

	public int[] getAggregationDataType( )
	{
		return this.rs.getAggregationDataType( );
	}

	public int[][] getLevelAttributeDataType( )
	{
		return this.rs.getLevelAttributeDataType( );
	}

	public String[][] getLevelAttributes( )
	{
		return this.rs.getLevelAttributes( );
	}

	public int[][] getLevelKeyDataType( )
	{
		return this.rs.getLevelKeyDataType( );
	}

	public String[][] getLevelKeys( )
	{
		return this.rs.getLevelKeys( );
	}

	public int[] getSortType( )
	{
		return this.rs.getSortType( );
	}

}