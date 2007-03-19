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

import javax.olap.OLAPException;

import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IRowContent;
import org.eclipse.birt.report.engine.extension.IReportItemExecutor;

/**
 * CrosstabMeasureHeaderRowExecutor
 */
public class CrosstabMeasureHeaderRowExecutor extends BaseCrosstabExecutor
{

	private int rowSpan, colSpan;
	private int currentChangeType;
	private int currentColIndex;
	private int lastMeasureIndex;

	private boolean hasColumnGroups;
	private boolean blankStarted;
	private boolean hasLast;

	public CrosstabMeasureHeaderRowExecutor( BaseCrosstabExecutor parent )
	{
		super( parent );
	}

	public IContent execute( )
	{
		IRowContent content = context.getReportContent( ).createRowContent( );

		initializeContent( content, null );

		prepareChildren( );

		return content;
	}

	private void prepareChildren( )
	{
		currentChangeType = ColumnEvent.UNKNOWN_CHANGE;
		currentColIndex = -1;

		blankStarted = false;
		hasColumnGroups = columnGroups != null && columnGroups.size( ) > 0;

		rowSpan = 1;
		colSpan = 0;
		lastMeasureIndex = -1;

		hasLast = false;

		walker.reload( );
	}

	public IReportItemExecutor getNextChild( )
	{
		IReportItemExecutor nextExecutor = null;

		try
		{
			while ( walker.hasNext( ) )
			{
				ColumnEvent ev = walker.next( );

				switch ( currentChangeType )
				{
					case ColumnEvent.ROW_EDGE_CHANGE :
					case ColumnEvent.MEASURE_HEADER_CHANGE :

						if ( blankStarted
								&& ev.type != ColumnEvent.ROW_EDGE_CHANGE
								&& ev.type != ColumnEvent.MEASURE_HEADER_CHANGE )
						{
							nextExecutor = new CrosstabCellExecutor( this,
									null,
									rowSpan,
									colSpan,
									currentColIndex - colSpan + 1 );

							blankStarted = false;
							hasLast = false;
						}
						break;
					case ColumnEvent.MEASURE_CHANGE :
					case ColumnEvent.COLUMN_EDGE_CHANGE :
					case ColumnEvent.COLUMN_TOTAL_CHANGE :
					case ColumnEvent.GRAND_TOTAL_CHANGE :

						nextExecutor = new CrosstabCellExecutor( this,
								crosstabItem.getMeasure( lastMeasureIndex )
										.getHeader( ),
								rowSpan,
								colSpan,
								currentColIndex - colSpan + 1 );
						hasLast = false;
						break;
				}

				if ( ev.type == ColumnEvent.MEASURE_CHANGE
						|| ev.type == ColumnEvent.COLUMN_TOTAL_CHANGE
						|| ev.type == ColumnEvent.COLUMN_EDGE_CHANGE
						|| ev.type == ColumnEvent.GRAND_TOTAL_CHANGE )
				{
					rowSpan = 1;
					colSpan = 0;
					lastMeasureIndex = ev.measureIndex;
					hasLast = true;
				}
				else if ( !blankStarted
						&& !hasColumnGroups
						&& ( ev.type == ColumnEvent.ROW_EDGE_CHANGE || ev.type == ColumnEvent.MEASURE_HEADER_CHANGE ) )
				{
					blankStarted = true;
					rowSpan = 1;
					colSpan = 0;
					hasLast = true;
				}

				currentChangeType = ev.type;
				colSpan++;
				currentColIndex++;

				if ( nextExecutor != null )
				{
					return nextExecutor;
				}
			}

		}
		catch ( OLAPException e )
		{
			e.printStackTrace( );
		}

		if ( hasLast )
		{
			hasLast = false;

			// handle last column
			switch ( currentChangeType )
			{
				case ColumnEvent.ROW_EDGE_CHANGE :
				case ColumnEvent.MEASURE_HEADER_CHANGE :

					if ( blankStarted )
					{
						nextExecutor = new CrosstabCellExecutor( this,
								null,
								rowSpan,
								colSpan,
								currentColIndex - colSpan + 1 );
					}
					break;
				case ColumnEvent.MEASURE_CHANGE :
				case ColumnEvent.COLUMN_EDGE_CHANGE :
				case ColumnEvent.COLUMN_TOTAL_CHANGE :
				case ColumnEvent.GRAND_TOTAL_CHANGE :

					nextExecutor = new CrosstabCellExecutor( this,
							crosstabItem.getMeasure( lastMeasureIndex )
									.getHeader( ),
							rowSpan,
							colSpan,
							currentColIndex - colSpan + 1 );
					break;
			}
		}

		return nextExecutor;
	}

	public boolean hasNextChild( )
	{
		try
		{
			return walker.hasNext( ) || hasLast;
		}
		catch ( OLAPException e )
		{
			e.printStackTrace( );
		}
		return false;
	}

}
