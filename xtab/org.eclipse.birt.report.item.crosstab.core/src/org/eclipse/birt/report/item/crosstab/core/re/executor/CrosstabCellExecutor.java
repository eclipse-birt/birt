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

import javax.olap.OLAPException;
import javax.olap.cursor.EdgeCursor;

import org.eclipse.birt.report.engine.content.ICellContent;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.executor.IReportItemExecutor;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabCellHandle;
import org.eclipse.birt.report.model.api.ReportElementHandle;

/**
 * CrosstabCellExecutor
 */
public class CrosstabCellExecutor extends BaseCrosstabExecutor
{

	// TODO tmp
	public CrosstabCellHandle cellHandle;
	private int rowSpan, colSpan, colIndex;
	private List contents;
	private int currentChild;

	private long position = -1;

	public CrosstabCellExecutor( BaseCrosstabExecutor parent,
			CrosstabCellHandle handle, int rowSpan, int colSpan, int colIndex )
	{
		super( parent );
		this.cellHandle = handle;
		if ( cellHandle != null )
		{
			contents = cellHandle.getContents( );
		}
		this.rowSpan = rowSpan;
		this.colSpan = colSpan;
		this.colIndex = colIndex;

		// TODO tmp
		contents = new ArrayList( );
		contents.add( null );
	}

	public void setPosition( long pos )
	{
		this.position = pos;
	}

	// TODO tmp
	public IContent getContent( )
	{
		return super.getContent( );
	}

	// TODO tmp
	public EdgeCursor getColumnEdgeCursor( ) throws OLAPException
	{
		return super.getColumnEdgeCursor( );
	}

	// TODO tmp
	public EdgeCursor getRowEdgeCursor( ) throws OLAPException
	{
		return super.getRowEdgeCursor( );
	}

	public IContent execute( )
	{
		ICellContent content = context.getReportContent( ).createCellContent( );

		initializeContent( content, cellHandle );

		content.setRowSpan( rowSpan );
		content.setColSpan( colSpan );
		content.setColumn( colIndex );

		processStyle( cellHandle );
		processVisibility( cellHandle );
		processBookmark( cellHandle );
		processAction( cellHandle );

		// ! no drop needed, row/column span already prepared.
		// if ( isRowLevelCell( ) )
		// {
		// content.setDrop( DesignChoiceConstants.DROP_TYPE_DETAIL );
		// }

		currentChild = 0;

		return content;
	}

	// private boolean isRowLevelCell( )
	// {
	// if ( cellHandle != null
	// && cellHandle.getContainer( ) instanceof LevelViewHandle
	// && ILevelViewConstants.MEMBER_PROP.equals( cellHandle.getModelHandle( )
	// .getContainerPropertyHandle( )
	// .getPropertyDefn( )
	// .getName( ) ) )
	// {
	// AbstractCrosstabItemHandle cd = cellHandle.getContainer( )
	// .getContainer( )
	// .getContainer( );
	//
	// if ( cd instanceof CrosstabViewHandle
	// && ICrosstabReportItemConstants.ROWS_PROP.equals( cd.getModelHandle( )
	// .getContainerPropertyHandle( )
	// .getPropertyDefn( )
	// .getName( ) ) )
	// {
	// return true;
	// }
	// }
	//
	// return false;
	// }

	public IReportItemExecutor getNextChild( )
	{
		// TODO reset data position
		if ( position != -1 )
		{
			try
			{
				getColumnEdgeCursor( ).setPosition( position );
			}
			catch ( OLAPException e )
			{
				e.printStackTrace( );
			}
		}

		return context.createExecutor( this,
				(ReportElementHandle) contents.get( currentChild++ ) );
	}

	public boolean hasNextChild( )
	{
		if ( contents != null )
		{
			return currentChild < contents.size( );
		}

		return false;
	}
}
