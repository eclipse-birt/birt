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

import javax.olap.OLAPException;
import javax.olap.cursor.CubeCursor;
import javax.olap.cursor.EdgeCursor;

import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IRowContent;
import org.eclipse.birt.report.engine.data.IResultSet;
import org.eclipse.birt.report.engine.executor.IReportItemExecutor;
import org.eclipse.birt.report.engine.extension.IExecutorContext;
import org.eclipse.birt.report.item.crosstab.core.ICrosstabConstants;
import org.eclipse.birt.report.item.crosstab.core.de.AbstractCrosstabItemHandle;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabReportItemHandle;
import org.eclipse.birt.report.item.crosstab.core.re.ICubeResultSet;

/**
 * the base class for all crosstab element executor
 */
public abstract class BaseCrosstabExecutor implements
		ICrosstabConstants,
		IReportItemExecutor
{

	protected IExecutorContext context;
	protected CrosstabReportItemHandle crosstabItem;
	protected int[] rowCounter;
	protected IColumnWalker walker;

	private IContent content;
	protected ICubeResultSet cubeRset;
	protected CubeCursor cubeCursor;

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
	}

	protected void executeQuery( AbstractCrosstabItemHandle handle )
	{
		// TODO
		// IBaseQueryDefinition query = context.getQueries(
		// crosstabItem.getModelHandle( ) )[0];
		cubeRset = (ICubeResultSet) context.executeQuery( getParentResultSet( ),
				null/* query */);

		cubeCursor = cubeRset.getCubeCursor( );
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
		ContentUtil.processStyle( context, content, handle );
	}

	protected void processVisibility( AbstractCrosstabItemHandle handle )
	{
		ContentUtil.processVisibility( context,
				content,
				handle,
				getCubeResultSet( ) );
	}

	protected void processBookmark( AbstractCrosstabItemHandle handle )
	{
		ContentUtil.processBookmark( context,
				content,
				handle,
				getCubeResultSet( ) );
	}

	protected void processAction( AbstractCrosstabItemHandle handle )
	{
		ContentUtil.processAction( context, content, handle );
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

		content.setParent( getParentContent( ) );
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

	private IResultSet getParentResultSet( )
	{
		IReportItemExecutor re = parentExecutor;

		while ( re != null )
		{
			IResultSet[] rsa = re.getResultSets( );
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

			if ( ordinates.size( ) > 0 )
			{
				// TODO ensure first is column edge
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

			if ( ordinates.size( ) > 1 )
			{
				// TODO ensure second is row edge
				return (EdgeCursor) ordinates.get( 1 );
			}
		}
		return null;
	}

	protected boolean hasMeasureHeader( int axisType )
	{
		int mc = crosstabItem.getMeasureCount( );

		if ( mc > 0 )
		{
			if ( axisType == COLUMN_AXIS_TYPE )
			{
				if ( MEASURE_DIRECTION_HORIZONTAL.equals( crosstabItem.getMeasureDirection( ) ) )
				{
					for ( int i = 0; i < mc; i++ )
					{
						if ( crosstabItem.getMeasure( i ).getHeader( ) != null )
						{
							return true;
						}
					}
				}
			}
			else
			{
				if ( MEASURE_DIRECTION_VERTICAL.equals( crosstabItem.getMeasureDirection( ) ) )
				{
					for ( int i = 0; i < mc; i++ )
					{
						if ( crosstabItem.getMeasure( i ).getHeader( ) != null )
						{
							return true;
						}
					}
				}
			}
		}

		return false;
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
		// TODO init crosstab item
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

	public IResultSet[] getResultSets( )
	{
		if ( cubeRset == null )
		{
			return null;
		}

		return new IResultSet[]{
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
