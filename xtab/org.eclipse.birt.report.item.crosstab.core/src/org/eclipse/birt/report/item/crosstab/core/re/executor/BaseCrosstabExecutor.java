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
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.olap.OLAPException;
import javax.olap.cursor.CubeCursor;
import javax.olap.cursor.EdgeCursor;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.IDataQueryDefinition;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IRowContent;
import org.eclipse.birt.report.engine.content.IStyle;
import org.eclipse.birt.report.engine.extension.IBaseResultSet;
import org.eclipse.birt.report.engine.extension.ICubeResultSet;
import org.eclipse.birt.report.engine.extension.IExecutorContext;
import org.eclipse.birt.report.engine.extension.IReportItemExecutor;
import org.eclipse.birt.report.item.crosstab.core.ICrosstabConstants;
import org.eclipse.birt.report.item.crosstab.core.de.AbstractCrosstabItemHandle;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabReportItemHandle;
import org.eclipse.birt.report.item.crosstab.core.de.LevelViewHandle;
import org.eclipse.birt.report.item.crosstab.core.i18n.Messages;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;

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
					getCubeResultSet( ) );
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

	protected void handleGroupPageBreakBefore( LevelViewHandle level,
			EdgeCursor rowCursor ) throws OLAPException
	{
		if ( level != null )
		{
			String pageBreakBefore = level.getPageBreakBefore( );
			if ( DesignChoiceConstants.PAGE_BREAK_BEFORE_ALWAYS.equals( pageBreakBefore )
					|| ( DesignChoiceConstants.PAGE_BREAK_BEFORE_ALWAYS_EXCLUDING_FIRST.equals( pageBreakBefore ) && !rowCursor.isFirst( ) ) )
			{
				getContent( ).getStyle( )
						.setProperty( IStyle.STYLE_PAGE_BREAK_BEFORE,
								IStyle.ALWAYS_VALUE );
			}

			String pageBreakAfter = level.getPageBreakAfter( );
			if ( DesignChoiceConstants.PAGE_BREAK_AFTER_ALWAYS.equals( pageBreakAfter ) )
			{
				getContent( ).getStyle( )
						.setProperty( IStyle.STYLE_PAGE_BREAK_AFTER,
								IStyle.ALWAYS_VALUE );
			}
		}

		// handle special logic for page_break_after_excluding_last
		// TODO confirm the correct behavior
		boolean hasPageBreak = false;
		IReportItemExecutor parentExecutor = getParent( );

		while ( parentExecutor instanceof CrosstabGroupExecutor )
		{
			if ( ( (CrosstabGroupExecutor) parentExecutor ).notifyNextGroupPageBreak )
			{
				( (CrosstabGroupExecutor) parentExecutor ).notifyNextGroupPageBreak = false;

				hasPageBreak = true;
			}

			parentExecutor = parentExecutor.getParent( );
		}

		if ( hasPageBreak )
		{
			getContent( ).getStyle( )
					.setProperty( IStyle.STYLE_PAGE_BREAK_BEFORE,
							IStyle.ALWAYS_VALUE );
		}
	}

	protected void handleGroupPageBreakAfter( LevelViewHandle level,
			EdgeCursor rowCursor ) throws OLAPException
	{
		if ( level != null )
		{
			// handle page_break_after_excluding_last
			String pageBreakAfter = level.getPageBreakAfter( );
			IReportItemExecutor parentExecutor = getParent( );

			if ( parentExecutor instanceof CrosstabGroupExecutor
					&& DesignChoiceConstants.PAGE_BREAK_AFTER_ALWAYS_EXCLUDING_LAST.equals( pageBreakAfter )
					&& !rowCursor.isLast( ) )
			{

				// TODO confirm the correct behavior
				while ( parentExecutor instanceof CrosstabGroupExecutor )
				{
					( (CrosstabGroupExecutor) parentExecutor ).notifyNextGroupPageBreak = true;

					parentExecutor = parentExecutor.getParent( );
				}
			}
		}
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
