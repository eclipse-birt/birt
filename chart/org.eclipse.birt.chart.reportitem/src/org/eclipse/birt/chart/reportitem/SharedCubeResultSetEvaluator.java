/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.chart.reportitem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.olap.OLAPException;
import javax.olap.cursor.EdgeCursor;
import javax.olap.cursor.RowDataNavigation;

import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.reportitem.i18n.Messages;
import org.eclipse.birt.chart.reportitem.plugin.ChartReportItemPlugin;
import org.eclipse.birt.chart.util.ChartUtil;
import org.eclipse.birt.core.data.ExpressionUtil;
import org.eclipse.birt.core.data.IColumnBinding;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.olap.api.ICubeCursor;
import org.eclipse.birt.data.engine.olap.api.ICubeQueryResults;
import org.eclipse.birt.data.engine.olap.api.query.IBaseCubeQueryDefinition;
import org.eclipse.birt.data.engine.olap.api.query.ICubeQueryDefinition;
import org.eclipse.birt.data.engine.olap.api.query.IDimensionDefinition;
import org.eclipse.birt.data.engine.olap.api.query.IEdgeDefinition;
import org.eclipse.birt.data.engine.olap.api.query.IHierarchyDefinition;
import org.eclipse.birt.data.engine.olap.api.query.ILevelDefinition;
import org.eclipse.birt.report.engine.extension.ICubeResultSet;

/**
 * The class implements evaluating for sharing xtab or chart view for xtab.
 */

public class SharedCubeResultSetEvaluator extends BIRTCubeResultSetEvaluator
{

	private int fRowInnerLevelIndex;
	private int fColInnerLevelIndex;

	private CursorPositionNode fMainPositionNodes;
	private CursorPositionNode fSubPositionNodes;
	private boolean fIsColEdgeAsMainCursor;

	/**
	 * Constructor.
	 * 
	 * @param rs
	 * @param cm
	 */
	public SharedCubeResultSetEvaluator( ICubeResultSet rs, Chart cm )
	{
		super( rs );
		parseLevelIndex( rs.getCubeQuery( ), cm );
	}

	/**
	 * Constructor.
	 * 
	 * @param qr
	 * @param queryDefinition
	 * @param cm
	 */
	public SharedCubeResultSetEvaluator( ICubeQueryResults qr,
			IBaseCubeQueryDefinition queryDefinition, Chart cm )
	{
		super( qr );
		parseLevelIndex( queryDefinition, cm );
	}

	/**
	 * Parse the dimension levels on row edge and column edge to find out the
	 * level index used by category series and Y optional.
	 * 
	 * @param queryDefintion
	 * @param cm
	 */
	private void parseLevelIndex( IBaseCubeQueryDefinition queryDefintion,
			Chart cm )
	{
		fRowInnerLevelIndex = -1;
		fColInnerLevelIndex = -1;
		if ( queryDefintion instanceof ICubeQueryDefinition )
		{
			List rowLevelNames = Collections.EMPTY_LIST;
			List colLevelNames = Collections.EMPTY_LIST;

			String[] categoryExprs = ChartUtil.getCategoryExpressions( cm );

			ICubeQueryDefinition cqd = (ICubeQueryDefinition) queryDefintion;
			IEdgeDefinition rowED = cqd.getEdge( ICubeQueryDefinition.ROW_EDGE );
			IEdgeDefinition colED = cqd.getEdge( ICubeQueryDefinition.COLUMN_EDGE );
			if ( rowED != null )
			{
				rowLevelNames = getLevelNames( rowED );
				if ( categoryExprs != null && categoryExprs.length > 0 )
				{
					fRowInnerLevelIndex = findInnerLevelIndex( categoryExprs[0],
							rowLevelNames );
					if ( fRowInnerLevelIndex < 0 && colED != null )
					{
						// Row level isn't find on row edge, find it on column
						// edge.
						rowLevelNames = getLevelNames( colED );
						fRowInnerLevelIndex = findInnerLevelIndex( categoryExprs[0],
								rowLevelNames );
						fIsColEdgeAsMainCursor = true;
						return;
					}
				}
			}

			if ( colED != null )
			{
				if ( rowED == null && fRowInnerLevelIndex < 0 )
				{
					// Only column edge is defined on xtab, find row level index
					// on column edge.
					rowLevelNames = getLevelNames( colED );
					if ( categoryExprs != null && categoryExprs.length > 0 )
					{
						fRowInnerLevelIndex = findInnerLevelIndex( categoryExprs[0],
								rowLevelNames );
						fIsColEdgeAsMainCursor = true;
					}
				}
				else
				{
					colLevelNames = getLevelNames( colED );
					String[] yOptionalExprs = ChartUtil.getYOptoinalExpressions( cm );
					if ( yOptionalExprs != null && yOptionalExprs.length > 0 )
					{
						fColInnerLevelIndex = findInnerLevelIndex( yOptionalExprs[0],
								colLevelNames );
					}
				}
			}
		}
	}


	/**
	 * Find the inner level index from specified expression.
	 * 
	 * @param expr
	 * @return
	 */
	private int findInnerLevelIndex( String expr, List<String> levelNames )
	{
		int index = -1;
		if ( ChartUtil.isEmpty( expr ) )
		{
			return index;
		}
		List<IColumnBinding> bindings;
		try
		{
			bindings = ExpressionUtil.extractColumnExpressions( expr,
					ExpressionUtil.DATA_INDICATOR );
		}
		catch ( BirtException e )
		{
			return index;
		}

		for ( IColumnBinding bind : bindings )
		{
			String name = bind.getResultSetColumnName( );
			int levelIndex = levelNames.indexOf( name );
			if ( levelIndex > index )
			{
				index = levelIndex;
			}
		}

		return index;
	}

	/**
	 * Returns level names in edge definition.
	 * 
	 * @param ed
	 * @return
	 */
	private List getLevelNames( IEdgeDefinition ed )
	{
		List levelNames = new ArrayList( );
		List<IDimensionDefinition> dimensions = ed.getDimensions( );
		for ( IDimensionDefinition d : dimensions )
		{
			List<IHierarchyDefinition> hieDefs = d.getHierarchy( );
			for ( IHierarchyDefinition hd : hieDefs )
			{
				List<ILevelDefinition> levels = hd.getLevels( );
				for ( ILevelDefinition ld : levels )
				{
					levelNames.add( ld.getName( ) );
				}
			}

		}
		return levelNames;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.birt.chart.reportitem.BIRTCubeResultSetEvaluator#initCubeCursor()
	 */
	protected void initCubeCursor( ) throws OLAPException, BirtException
	{
		// Find row and column edge cursor.
		if ( cubeCursor == null )
		{
			cubeCursor = getCubeCursor( );

			List<EdgeCursor> edges = cubeCursor.getOrdinateEdge( );
			if ( edges.size( ) == 0 )
			{
				throw new ChartException( ChartReportItemPlugin.ID,
						ChartException.DATA_BINDING,
						Messages.getString( "exception.no.cube.edge" ) ); //$NON-NLS-1$
			}
			else if ( edges.size( ) == 1 )
			{
				this.mainEdgeCursor = (EdgeCursor) edges.get( 0 );
				this.subEdgeCursor = null;
			}
			else
			{
				this.mainEdgeCursor = (EdgeCursor) edges.get( 0 );
				this.subEdgeCursor = (EdgeCursor) edges.get( 1 );
			}
		}

		// It means the shared xtab has defined row and column edges, but chart
		// just select row or column edge. The edge cursor should be adjusted
		// for chart to evaluate expressions.
		if ( fRowInnerLevelIndex >= 0
				&& fColInnerLevelIndex < 0
				&& subEdgeCursor != null )
		{
			if ( !fIsColEdgeAsMainCursor )
			{
				// Row edge is not used by chart, set subEdgeCursor(column edge)
				// to mainEdgeCursor.
				mainEdgeCursor = subEdgeCursor;
			}

			subEdgeCursor = null;
		}

		// Map dimension cursor, find out the right row dimension cursor and
		// column dimension cursor which is selected by chart.
		if ( subEdgeCursor == null )
		{
			List dimCursors = mainEdgeCursor.getDimensionCursor( );
			if ( fRowInnerLevelIndex >= 0 )
			{
				fMainPositionNodes = initCursorPositionsNodes( dimCursors,
						fRowInnerLevelIndex );
			}
			else if ( fColInnerLevelIndex >= 0 )
			{
				fMainPositionNodes = initCursorPositionsNodes( dimCursors,
						fColInnerLevelIndex );
			}
		}
		else
		{
			if ( fRowInnerLevelIndex >= 0 )
			{
				List dimCursors = subEdgeCursor.getDimensionCursor( );
				fSubPositionNodes = initCursorPositionsNodes( dimCursors,
						fRowInnerLevelIndex );
			}
			if ( fColInnerLevelIndex >= 0 )
			{
				List dimCursors = mainEdgeCursor.getDimensionCursor( );
				fMainPositionNodes = initCursorPositionsNodes( dimCursors,
						fColInnerLevelIndex );
			}
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.birt.chart.reportitem.BIRTCubeResultSetEvaluator#getCubeCursor()
	 */
	protected ICubeCursor getCubeCursor( ) throws BirtException
	{
		if ( rs != null )
		{
			return (ICubeCursor) rs.getCubeCursor( );
		}
		else
		{
			return qr.getCubeCursor( );
		}
	}

	private CursorPositionNode initCursorPositionsNodes( List dimCursorList,
			int innerLevelIndex )
	{
		CursorPositionNode pn = null;
		CursorPositionNode rootPN = null;
		for ( int i = innerLevelIndex; i >= 0; i-- )
		{
			if ( pn == null )
			{
				pn = new CursorPositionNode( (RowDataNavigation) dimCursorList.get( i ) );
				rootPN = pn;
			}
			else
			{
				pn.setParentNode( new CursorPositionNode( (RowDataNavigation) dimCursorList.get( i ) ) );
				pn = pn.getParentNode( );
			}
		}
		return rootPN;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.factory.IDataRowExpressionEvaluator#next()
	 */
	public boolean next( )
	{
		// In here, we use position to check if current edge cursor is moved on
		// right position. If the previous position equals current position, it
		// means the edge cursor is still in one data set on related dimension
		// cursor. If the position is changed, it means the edge cursor is moved
		// on the next data set of related dimension cursor.
		iIndex++;
		try
		{
			if ( subEdgeCursor != null )
			{
				// Break if sub cursor reaches end
				boolean hasNext = false;
				while ( hasNext = subEdgeCursor.next( ) )
				{
					if ( fSubPositionNodes.positionIsChanged( ) )
					{
						break;
					}
				}

				fSubPositionNodes.updatePosition( );

				if ( hasNext )
				{
					return true;
				}

				// Add break index for each start point
				lstBreaks.add( Integer.valueOf( iIndex ) );
				subEdgeCursor.first( );
				fSubPositionNodes.updatePosition( );

				hasNext = false;
				while ( hasNext = mainEdgeCursor.next( ) )
				{
					if ( fMainPositionNodes.positionIsChanged( ) )
					{
						break;
					}
				}
				fMainPositionNodes.updatePosition( );

				if ( hasNext )
				{
					return true;
				}
			}
			else
			{
				boolean hasNext = false;
				while ( hasNext = mainEdgeCursor.next( ) )
				{
					// if ( fColPosition != fMainCursor.getPosition( ) )
					// {
					// break;
					// }
					if ( fMainPositionNodes.positionIsChanged( ) )
					{
						break;
					}
				}

				// fColPosition = fMainCursor.getPosition( );
				fMainPositionNodes.updatePosition( );

				if ( hasNext )
				{
					return true;
				}
			}
		}
		catch ( OLAPException e )
		{
			logger.log( e );
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.factory.IDataRowExpressionEvaluator#first()
	 */
	public boolean first( )
	{
		try
		{
			initCubeCursor( );

			mainEdgeCursor.first( );
			fMainPositionNodes.updatePosition( );
			if ( subEdgeCursor != null )
			{
				subEdgeCursor.first( );
				fSubPositionNodes.updatePosition( );
			}
			else
			{
				bWithoutSub = true;
			}
			return true;
		}
		catch ( OLAPException e )
		{
			logger.log( e );
		}
		catch ( BirtException e )
		{
			logger.log( e );
		}
		return false;
	}

	/**
	 * The class records the position of dimension cursor and parent dimension
	 * cursor.
	 */
	static class CursorPositionNode
	{

		private RowDataNavigation fCursor;

		private CursorPositionNode fParentNode;

		private long fPosition = -1;

		public CursorPositionNode getParentNode( )
		{
			return fParentNode;
		}

		void setParentNode( CursorPositionNode parentNode )
		{
			fParentNode = parentNode;
		}

		CursorPositionNode( RowDataNavigation cursor )
		{
			fCursor = cursor;
		}

		long getPosition( )
		{
			return fPosition;
		}

		void updatePosition( ) throws OLAPException
		{
			fPosition = fCursor.getPosition( );
			if ( fParentNode != null )
			{
				fParentNode.updatePosition( );
			}
		}

		boolean positionIsChanged( ) throws OLAPException
		{
			if ( fPosition != fCursor.getPosition( ) )
			{
				return true;
			}
			else if ( fCursor.getPosition( ) == 0 && fParentNode != null )
			{
				return fParentNode.positionIsChanged( );
			}
			return false;
		}
	}
}
