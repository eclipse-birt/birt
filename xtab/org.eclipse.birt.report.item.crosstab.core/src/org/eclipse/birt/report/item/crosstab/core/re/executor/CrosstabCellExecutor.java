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

import org.eclipse.birt.report.engine.content.ICellContent;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.executor.IReportItemExecutor;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabCellHandle;

/**
 * CrosstabCellExecutor
 */
public class CrosstabCellExecutor extends BaseCrosstabExecutor
{

	private CrosstabCellHandle cellHandle;
	private int rowSpan, colSpan, colIndex;
	private List contents;
	private int currentChild;

	private long position = -1;
	private boolean isForceEmpty;

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
	}

	public void setPosition( long pos )
	{
		this.position = pos;
	}

	public void setForceEmpty( boolean isEmpty )
	{
		this.isForceEmpty = isEmpty;
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
		if ( isForceEmpty )
		{
			return null;
		}

		// must reset data position
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

		IReportItemExecutor executor = context.createExecutor( this,
				contents.get( currentChild++ ) );

		return executor;
	}

	public boolean hasNextChild( )
	{
		if ( isForceEmpty )
		{
			return false;
		}

		if ( contents != null )
		{
			return currentChild < contents.size( );
		}

		return false;
	}
}
