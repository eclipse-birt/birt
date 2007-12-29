/*******************************************************************************
 * Copyright (c) 2004, 2007 Actuate Corporation.
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
import java.util.List;
import java.util.Vector;

/**
 * This class contains the relation description between dimension and its
 * belonging edge, and some shared information in dimension and edge traverse.
 * 
 * The relation[axisIndex] is an Vector of EdgeInfo objects at the specified axis index.
 * axisIndex is a 0-based index.
 * Example: 
 * Dim  Country 	City 	    Product 	
 * 0: 	CHINA 		BEIJING 	P1 
 * 1: 	CHINA		BEIJING		P2 
 * 2: 	CHINA		BEIJING		P3
 * 3: 	CHINA		SHANGHAI	P1 
 * 4:	CHINA		SHANGHAI	P2
 * 5: 	CHINA 		SHANGHAI	P3
 * 6: 	USA 		CHICAGO		P1
 * 7: 	USA			NEW YORK	P1
 * 8: 	USA 		NEW YORK	P2
 * 
 * edgeInfo: (start, end) 
 * 		Country		    City 		    Product
 * ============================================ 
 * 0: 	-1,0 			0,0 			0,0
 * 1: 	-1,2 			0,3 			0,1 
 * 2:	 				1,6 			0,2 
 * 3: 					1,7 			1,3 
 * 4: 									1,4 
 * 5: 									1,5  
 * 6:                                   2,6  
 * 7:                                   3,7
 * 8:                                   3,8
 * 
 * If this edge has mirrored level, the non-mirrored level will be only populated its edgeInfo
 * Example: Product level has been mirrored
 * edgeInfo: (start, end) 
 * 		Country		    City 		    Product
 * ============================================ 
 * 0: 	-1,0 			0,0 	       [P1,P2,P3]
 * 1: 	-1,2 			0,3 			
 * 2:	 				1,6 			
 * 3: 					1,7 	
 * The product level's value will be sorted according to its basic sort definition. But in case 
 * of aggregation sort, we should try to keep its original sort result.
 * 
 */
class EdgeDimensionRelation
{
	Vector[] relation;
	int traverseLength, mirrorStartPosition;
	// the dimension cursor position for each dimension axis
	int[] mirrorLength;
	ResultSetFetcher fetcher;
	
	/**
	 * 
	 * @param service
	 * @param fetcher
	 * @param fetchSize
	 * @throws IOException
	 */
	EdgeDimensionRelation( RowDataAccessorService service,
			ResultSetFetcher fetcher, int fetchSize ) throws IOException
	{
		this( service, fetcher, fetchSize, 0, service.getAggregationResultSet( )
				.length( ) - 1 );
	}

	/**
	 * 
	 * @param service
	 * @param fetcher
	 * @param fetchSize
	 * @param edgeStart
	 * @param edgeEnd
	 * @throws IOException
	 */
	EdgeDimensionRelation( RowDataAccessorService service,
			ResultSetFetcher fetcher, int fetchSize, int edgeStart, int edgeEnd )
			throws IOException
	{
		int dimensionLength = service.getDimensionAxis( ).length;

		this.fetcher = fetcher;
		this.mirrorStartPosition = service.getMirrorStartPosition( );
		this.mirrorLength = new int[dimensionLength];
		
		for ( int i = 0; i < dimensionLength; i++ )
		{
			mirrorLength[i] = 0;
		}

		if ( mirrorStartPosition > 0 )
		{
			for ( int i = mirrorStartPosition; i < dimensionLength; i++ )
			{
				mirrorLength[i] = service.getDimensionAxis( )[i].getDisctinctValue( )
						.size( );
			}
		}

		int customDimSize = 0;
		if ( mirrorStartPosition > 0 )
		{
			customDimSize = mirrorStartPosition;
		}
		else
		{
			customDimSize = service.getDimensionAxis( ).length;
		}

		relation = new Vector[customDimSize];

		for ( int i = 0; i < customDimSize; i++ )
		{
			relation[i] = new Vector( );
		}

		if ( fetchSize > 0 && edgeEnd - edgeStart >= fetchSize )
		{
			this.traverseLength = fetchSize;
		}
		else
		{
			this.traverseLength = edgeEnd - edgeStart + 1;
		}

		Object[] preValue = new Object[customDimSize];
		Object[] currValue = new Object[customDimSize];

		for ( int rowId = 0; rowId < traverseLength; rowId++ )
		{
			if ( rowId + edgeStart >= service.getAggregationResultSet( ).length( ) )
			{
				break;
			}
			service.getAggregationResultSet( ).seek( rowId + edgeStart );
			if ( !service.isPage( ) )
				for ( int i = 0; i < customDimSize; i++ )
				{
					// TODO Default use 0 index as level key
					currValue[i] = fetcher.getLevelKeyValue( service.getDimensionAxis( )[i].getLevelIndex( ) )[fetcher.getAggrResultSet( )
							.getLevelKeyColCount( service.getDimensionAxis( )[i].getLevelIndex( ) ) - 1];
				}
			int breakLevel;
			if ( rowId == 0 || service.isPage( ) )
				breakLevel = 0;
			else
				breakLevel = getBreakLevel( currValue, preValue, rowId );

			for ( int level = breakLevel; level < customDimSize; level++ )
			{
				EdgeInfo edge = new EdgeInfo( );

				if ( level != 0 )
					edge.parent = relation[level - 1].size( ) - 1;
				if ( level == relation.length - 1 )
				{
					edge.firstChild = rowId;
				}
				else
				{
					edge.firstChild = relation[level + 1].size( );
				}
				relation[level].add( edge );
			}
			for ( int i = 0; i < customDimSize; i++ )
			{
				preValue[i] = currValue[i];
			}
		}

		if ( mirrorStartPosition > 0 )
		{
			this.traverseLength = this.relation[mirrorStartPosition - 1].size( );
			for ( int i = mirrorStartPosition; i < dimensionLength; i++ )
			{
				this.traverseLength = this.traverseLength *
						this.mirrorLength[i];
			}
		}
	}
	
	/**
	 * 
	 * @param currValue
	 * @param preValue
	 * @param rowId
	 * @return
	 */
	private int getBreakLevel( Object[] currValue, Object[] preValue, int rowId )
	{
		assert preValue != null && currValue != null;
		int breakLevel = 0;
		for ( ; breakLevel < currValue.length; breakLevel++ )
		{
			// get the first child of current group in level of breakLevel
			List list = this.relation[breakLevel];
			EdgeInfo edgeInfo = (EdgeInfo) list.get( list.size( ) - 1 );
			int child = edgeInfo.firstChild;

			Object currObjectValue = currValue[breakLevel];
			Object prevObjectValue = preValue[breakLevel];

			for ( int level = breakLevel + 1; level < this.relation.length; level++ )
			{
				list = this.relation[level];
				edgeInfo = (EdgeInfo) list.get( child );
				child = edgeInfo.firstChild;
			}

			// determines whether next row is in current group
			if ( isEqualObject( currObjectValue, prevObjectValue ) == false )
			{
				break;
			}

		}
		return breakLevel;
	}

	/**
	 * 
	 * @param preValue
	 * @param currentValue
	 * @return
	 */
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
}

class EdgeInfo
{

	int parent = -1;
	int firstChild = -1;
}
