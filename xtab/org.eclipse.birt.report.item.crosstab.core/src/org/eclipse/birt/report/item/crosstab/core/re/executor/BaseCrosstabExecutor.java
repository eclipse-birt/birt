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

package org.eclipse.birt.report.item.crosstab.core.re.executor;

import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.olap.OLAPException;
import javax.olap.cursor.CubeCursor;
import javax.olap.cursor.DimensionCursor;
import javax.olap.cursor.EdgeCursor;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.IDataQueryDefinition;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IRowContent;
import org.eclipse.birt.report.engine.extension.IBaseResultSet;
import org.eclipse.birt.report.engine.extension.ICubeResultSet;
import org.eclipse.birt.report.engine.extension.IExecutorContext;
import org.eclipse.birt.report.engine.extension.IReportItemExecutor;
import org.eclipse.birt.report.engine.ir.DimensionType;
import org.eclipse.birt.report.item.crosstab.core.CrosstabException;
import org.eclipse.birt.report.item.crosstab.core.ICrosstabConstants;
import org.eclipse.birt.report.item.crosstab.core.de.AbstractCrosstabItemHandle;
import org.eclipse.birt.report.item.crosstab.core.de.AggregationCellHandle;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabCellHandle;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabReportItemHandle;
import org.eclipse.birt.report.item.crosstab.core.de.MeasureViewHandle;
import org.eclipse.birt.report.item.crosstab.core.i18n.Messages;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.olap.LevelHandle;

/**
 * the base class for all crosstab element executor
 */
public abstract class BaseCrosstabExecutor implements
		ICrosstabConstants,
		IReportItemExecutor
{

	private static Logger logger = Logger.getLogger( BaseCrosstabExecutor.class.getName( ) );

	protected IExecutorContext context;
	protected CrosstabReportItemHandle crosstabItem;
	protected int[] rowCounter;
	protected IColumnWalker walker;

	private IContent content;
	protected ICubeResultSet cubeRset;
	protected CubeCursor cubeCursor;

	protected Map styleCache;
	protected List rowGroups, columnGroups;

	private Object modelHandle;
	private IReportItemExecutor parentExecutor;

	protected BaseCrosstabExecutor( )
	{
		this.rowCounter = new int[1];
	}

	protected BaseCrosstabExecutor( IExecutorContext context,
			CrosstabReportItemHandle item, IReportItemExecutor parentExecutor )
	{
		this( );

		this.context = context;
		this.crosstabItem = item;
		this.parentExecutor = parentExecutor;
	}

	protected BaseCrosstabExecutor( BaseCrosstabExecutor parent )
	{
		this( parent.context, parent.crosstabItem, parent );
		this.rowCounter = parent.rowCounter;
		this.walker = parent.walker;

		this.columnGroups = parent.columnGroups;
		this.rowGroups = parent.rowGroups;
		this.styleCache = parent.styleCache;
	}

	protected void executeQuery( AbstractCrosstabItemHandle handle )
	{
		DesignElementHandle elementHandle = crosstabItem.getModelHandle( );

		IDataQueryDefinition query = context.getQueries( elementHandle )[0];

		IBaseResultSet rset = context.executeQuery( getParentResultSet( ),
				query,
				elementHandle );

		if ( rset instanceof ICubeResultSet )
		{
			cubeRset = (ICubeResultSet) rset;
			cubeCursor = cubeRset.getCubeCursor( );
		}
	}

	protected void closeQuery( )
	{
		if ( cubeRset != null )
		{
			cubeRset.close( );
			cubeRset = null;
			cubeCursor = null;
		}
	}

	protected CrosstabReportItemHandle getCrosstabItemHandle( )
	{
		return crosstabItem;
	}

	protected void processStyle( AbstractCrosstabItemHandle handle )
	{
		try
		{
			ContentUtil.processStyle( context,
					content,
					handle,
					getCubeResultSet( ),
					styleCache );
		}
		catch ( BirtException e )
		{
			logger.log( Level.SEVERE,
					Messages.getString( "BaseCrosstabExecutor.error.process.style" ), //$NON-NLS-1$
					e );
		}
	}

	protected void processVisibility( AbstractCrosstabItemHandle handle )
	{
		try
		{
			ContentUtil.processVisibility( context,
					content,
					handle,
					getCubeResultSet( ) );
		}
		catch ( BirtException e )
		{
			logger.log( Level.SEVERE,
					Messages.getString( "BaseCrosstabExecutor.error.process.visibility" ), //$NON-NLS-1$
					e );
		}
	}

	protected void processBookmark( AbstractCrosstabItemHandle handle )
	{
		try
		{
			ContentUtil.processBookmark( context,
					content,
					handle,
					getCubeResultSet( ) );
		}
		catch ( BirtException e )
		{
			logger.log( Level.SEVERE,
					Messages.getString( "BaseCrosstabExecutor.error.process.bookmark" ), //$NON-NLS-1$
					e );
		}
	}

	protected void processAction( AbstractCrosstabItemHandle handle )
	{
		ContentUtil.processAction( context, content, handle );
	}

	protected void processRowHeight( CrosstabCellHandle cell )
	{
		if ( cell != null )
		{
			try
			{
				DimensionType height = ContentUtil.createDimension( crosstabItem.getRowHeight( cell ) );

				if ( height != null )
				{
					content.setHeight( height );
				}
			}
			catch ( CrosstabException e )
			{
				logger.log( Level.SEVERE,
						Messages.getString( "BaseCrosstabExecutor.error.process.row.height" ), //$NON-NLS-1$
						e );
			}
		}
	}

	protected CrosstabCellHandle findHeaderRowCell( int dimIndex, int levelIndex )
	{
		return crosstabItem.getDimension( COLUMN_AXIS_TYPE, dimIndex )
				.getLevel( levelIndex )
				.getCell( );
	}

	protected CrosstabCellHandle findMeasureHeaderCell( )
	{
		for ( int i = 0; i < crosstabItem.getMeasureCount( ); i++ )
		{
			CrosstabCellHandle headerCell = crosstabItem.getMeasure( i )
					.getHeader( );

			if ( headerCell != null )
			{
				return headerCell;
			}
		}

		return null;
	}

	protected CrosstabCellHandle findMeasureRowCell( int rowIndex )
	{
		return crosstabItem.getMeasure( rowIndex ).getCell( );
	}

	protected CrosstabCellHandle findDetailRowCell( int rowIndex )
	{
		return crosstabItem.getMeasure( rowIndex ).getCell( );
	}

	protected CrosstabCellHandle findSubTotalRowCell( int dimIndex,
			int levelIndex, int rowIndex )
	{
		MeasureViewHandle mv = crosstabItem.getMeasure( rowIndex );
		int count = mv.getAggregationCount( );

		LevelHandle lh = crosstabItem.getDimension( ROW_AXIS_TYPE, dimIndex )
				.getLevel( levelIndex )
				.getCubeLevel( );

		for ( int i = 0; i < count; i++ )
		{
			AggregationCellHandle cell = mv.getAggregationCell( i );

			if ( cell.getAggregationOnRow( ) == lh )
			{
				return cell;
			}
		}

		return null;
	}

	protected CrosstabCellHandle findGrandTotalRowCell( int rowIndex )
	{
		MeasureViewHandle mv = crosstabItem.getMeasure( rowIndex );
		int count = mv.getAggregationCount( );

		for ( int i = 0; i < count; i++ )
		{
			AggregationCellHandle cell = mv.getAggregationCell( i );

			if ( cell.getAggregationOnRow( ) == null )
			{
				return cell;
			}
		}

		return null;
	}

	protected void initializeContent( IContent content,
			AbstractCrosstabItemHandle handle )
	{
		this.content = content;

		// increase row index
		if ( content instanceof IRowContent )
		{
			( (IRowContent) content ).setRowID( rowCounter[0]++ );
		}

		IContent parent = getParentContent( );
		if ( parent != null )
		{
			content.setParent( parent );
		}
	}

	private IContent getParentContent( )
	{
		IReportItemExecutor re = parentExecutor;

		while ( re != null )
		{
			IContent cont = re.getContent( );
			if ( cont != null )
			{
				return cont;
			}
			re = re.getParent( );
		}
		return null;
	}

	private IBaseResultSet getParentResultSet( )
	{
		IReportItemExecutor re = parentExecutor;

		while ( re != null )
		{
			IBaseResultSet[] rsa = re.getQueryResults( );
			if ( rsa != null && rsa.length > 0 )
			{
				return rsa[0];
			}
			re = re.getParent( );
		}
		return null;
	}

	public IContent getContent( )
	{
		return content;
	}

	protected ICubeResultSet getCubeResultSet( )
	{
		if ( cubeRset != null )
		{
			return cubeRset;
		}
		else if ( parentExecutor instanceof BaseCrosstabExecutor )
		{
			return ( (BaseCrosstabExecutor) parentExecutor ).getCubeResultSet( );
		}

		return null;
	}

	protected CubeCursor getCubeCursor( )
	{
		if ( cubeCursor != null )
		{
			return cubeCursor;
		}
		else if ( parentExecutor instanceof BaseCrosstabExecutor )
		{
			return ( (BaseCrosstabExecutor) parentExecutor ).getCubeCursor( );
		}

		return null;
	}

	protected EdgeCursor getColumnEdgeCursor( ) throws OLAPException
	{
		CubeCursor cs = getCubeCursor( );

		if ( cs != null )
		{
			List ordinates = cs.getOrdinateEdge( );

			if ( columnGroups != null
					&& columnGroups.size( ) > 0
					&& ordinates.size( ) > 0 )
			{
				// the first is always column edge if has column definition
				return (EdgeCursor) ordinates.get( 0 );
			}
		}
		return null;
	}

	protected EdgeCursor getRowEdgeCursor( ) throws OLAPException
	{
		CubeCursor cs = getCubeCursor( );

		if ( cs != null )
		{
			List ordinates = cs.getOrdinateEdge( );

			if ( rowGroups != null
					&& rowGroups.size( ) > 0
					&& ordinates.size( ) > 0 )
			{
				// the last is always row edge if has row definition
				return (EdgeCursor) ordinates.get( ordinates.size( ) - 1 );
			}
		}
		return null;
	}

	/**
	 * Returns 1-based starting group index
	 */
	protected int getStartingGroupLevel( EdgeCursor rowCursor, List groupCursors )
			throws OLAPException
	{
		if ( rowCursor.isFirst( ) )
		{
			return 0;
		}

		for ( int i = 0; i < groupCursors.size( ) - 1; i++ )
		{
			DimensionCursor dc = (DimensionCursor) groupCursors.get( i );

			if ( GroupUtil.isDummyGroup( dc ) )
			{
				// if first level is dummy, we still return the first index,
				// otherwise, we return the previous index
				return i == 0 ? 1 : i;
			}

			if ( dc.getEdgeStart( ) == rowCursor.getPosition( ) )
			{
				return i + 1;
			}
		}

		return groupCursors.size( );
	}

	/**
	 * Returns 1-based ending group index
	 */
	protected int getEndingGroupLevel( EdgeCursor rowCursor, List groupCursors )
			throws OLAPException
	{
		if ( rowCursor.isLast( ) )
		{
			return 0;
		}

		for ( int i = 0; i < groupCursors.size( ) - 1; i++ )
		{
			DimensionCursor dc = (DimensionCursor) groupCursors.get( i );

			if ( GroupUtil.isDummyGroup( dc ) )
			{
				// if first level is dummy, we still return the first index,
				// otherwise, we return the previous index
				return i == 0 ? 1 : i;
			}

			if ( dc.getEdgeEnd( ) == rowCursor.getPosition( ) )
			{
				return i + 1;
			}
		}

		return groupCursors.size( );
	}

	public void close( )
	{
		// TODO clean up
	}

	public Object getModelObject( )
	{
		return modelHandle;
	}

	public void setModelObject( Object handle )
	{
		modelHandle = handle;
	}

	public IReportItemExecutor getParent( )
	{
		return parentExecutor;
	}

	public void setParent( IReportItemExecutor parent )
	{
		parentExecutor = parent;
	}

	public IBaseResultSet[] getQueryResults( )
	{
		if ( cubeRset == null )
		{
			return null;
		}

		return new IBaseResultSet[]{
			cubeRset
		};
	}

	public IExecutorContext getContext( )
	{
		return context;
	}

	public void setContext( IExecutorContext context )
	{
		this.context = context;
	}

}
