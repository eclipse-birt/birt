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

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.olap.OLAPException;
import javax.olap.cursor.EdgeCursor;

import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.ITableContent;
import org.eclipse.birt.report.engine.extension.IExecutorContext;
import org.eclipse.birt.report.engine.extension.IReportItemExecutor;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabReportItemHandle;
import org.eclipse.birt.report.item.crosstab.core.i18n.Messages;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.extension.ExtendedElementException;
import org.eclipse.birt.report.model.api.extension.IReportItem;

/**
 * CrosstabReportItemExecutor
 */
public class CrosstabReportItemExecutor extends BaseCrosstabExecutor
{

	private static Logger logger = Logger.getLogger( CrosstabReportItemExecutor.class.getName( ) );

	private List children;
	private int currentChild;

	public CrosstabReportItemExecutor( )
	{
		super( );
	}

	public CrosstabReportItemExecutor( CrosstabReportItemHandle item,
			IExecutorContext context, IReportItemExecutor parentExecutor )
	{
		super( context, item, parentExecutor );
	}

	public void close( )
	{
		super.close( );

		closeQuery( );

		children = null;
	}

	public void setModelObject( Object handle )
	{
		super.setModelObject( handle );

		if ( handle instanceof ExtendedItemHandle )
		{
			ExtendedItemHandle exHandle = (ExtendedItemHandle) handle;
			IReportItem item = null;

			try
			{
				item = exHandle.getReportItem( );
			}
			catch ( ExtendedElementException e )
			{
				logger.log( Level.SEVERE,
						Messages.getString( "CrosstabReportItemExecutor.error.crosstab.loading" ), //$NON-NLS-1$
						e );
			}

			crosstabItem = (CrosstabReportItemHandle) item;
		}
	}

	public IContent execute( )
	{
		ITableContent content = context.getReportContent( )
				.createTableContent( );

		executeQuery( crosstabItem );

		initializeContent( content, crosstabItem );

		processStyle( crosstabItem );
		processVisibility( crosstabItem );
		processBookmark( crosstabItem );
		processAction( crosstabItem );

		// handle table caption
		content.setCaption( crosstabItem.getCaption( ) );
		content.setCaptionKey( crosstabItem.getCaptionKey( ) );

		// check repeate header
		content.setHeaderRepeat( crosstabItem.isRepeatRowHeader( ) );

		if ( getCubeCursor( ) != null )
		{
			// generate table columns
			try
			{
				rowGroups = GroupUtil.getGroups( crosstabItem, ROW_AXIS_TYPE );
				columnGroups = GroupUtil.getGroups( crosstabItem,
						COLUMN_AXIS_TYPE );

				walker = new CachedColumnWalker( crosstabItem,
						getColumnEdgeCursor( ) );
				new TableColumnGenerator( crosstabItem,
						walker,
						getCubeResultSet( ) ).generateColumns( context.getReportContent( ),
						content );
			}
			catch ( OLAPException e )
			{
				logger.log( Level.SEVERE,
						Messages.getString( "CrosstabReportItemExecutor.error.generate.columns" ), //$NON-NLS-1$
						e );
			}

			prepareChildren( );
		}
		else
		{
			logger.log( Level.SEVERE,
					Messages.getString( "CrosstabReportItemExecutor.error.invalid.cube.result" ) ); //$NON-NLS-1$
		}

		currentChild = 0;

		return content;
	}

	private void prepareChildren( )
	{
		int measureCount = crosstabItem.getMeasureCount( );

		if ( columnGroups.size( ) > 0
				|| GroupUtil.hasMeasureHeader( crosstabItem, COLUMN_AXIS_TYPE ) )
		{
			if ( children == null )
			{
				children = new ArrayList( );
			}
			CrosstabHeaderExecutor headerExecutor = new CrosstabHeaderExecutor( this );
			children.add( headerExecutor );
		}

		if ( rowGroups.size( ) > 0 || measureCount > 0 )
		{
			if ( children == null )
			{
				children = new ArrayList( );
			}

			try
			{
				EdgeCursor rowCursor = getRowEdgeCursor( );

				if ( rowCursor != null )
				{
					rowCursor.beforeFirst( );

					if ( rowCursor.next( ) )
					{
						CrosstabGroupExecutor groupExecutor = new CrosstabGroupExecutor( this,
								0,
								rowCursor );
						children.add( groupExecutor );
					}
				}
				else
				{
					CrosstabMeasureExecutor measureExecutor = new CrosstabMeasureExecutor( this );
					children.add( measureExecutor );
				}
			}
			catch ( OLAPException e )
			{
				logger.log( Level.SEVERE,
						Messages.getString( "CrosstabReportItemExecutor.error.prepare.children" ), //$NON-NLS-1$
						e );
			}
		}

		if ( rowGroups.size( ) > 0
				&& crosstabItem.getGrandTotal( ROW_AXIS_TYPE ) != null
				&& ( measureCount > 0 || !IColumnWalker.IGNORE_TOTAL_COLUMN_WITHOUT_MEASURE ) )
		{
			if ( children == null )
			{
				children = new ArrayList( );
			}
			CrosstabFooterExecutor totalExecutor = new CrosstabFooterExecutor( this );
			children.add( totalExecutor );
		}
	}

	public boolean hasNextChild( )
	{
		return children != null && currentChild < children.size( );
	}

	public IReportItemExecutor getNextChild( )
	{
		if ( hasNextChild( ) )
		{
			return (IReportItemExecutor) children.get( currentChild++ );
		}
		return null;
	}

}
