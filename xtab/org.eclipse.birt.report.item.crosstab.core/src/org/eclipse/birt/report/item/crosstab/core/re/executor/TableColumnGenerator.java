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

import org.eclipse.birt.report.engine.content.IReportContent;
import org.eclipse.birt.report.engine.content.ITableContent;
import org.eclipse.birt.report.engine.content.impl.Column;
import org.eclipse.birt.report.engine.ir.DimensionType;
import org.eclipse.birt.report.item.crosstab.core.ICrosstabConstants;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabCellHandle;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabReportItemHandle;
import org.eclipse.birt.report.item.crosstab.core.de.DimensionViewHandle;
import org.eclipse.birt.report.item.crosstab.core.de.MeasureViewHandle;
import org.eclipse.birt.report.model.api.DimensionHandle;

/**
 * TableColumnGenerator
 */
public class TableColumnGenerator implements ICrosstabConstants
{

	private IColumnWalker walker;
	private CrosstabReportItemHandle crosstabItem;

	TableColumnGenerator( CrosstabReportItemHandle item, IColumnWalker walker )
	{
		this.crosstabItem = item;
		this.walker = walker;
	}

	void generateColumns( IReportContent report, ITableContent table )
			throws OLAPException
	{
		while ( walker.hasNext( ) )
		{
			ColumnEvent ce = walker.next( );

			addColumn( ce, report, table );

			System.out.println( ce );
		}
	}

	private void addColumn( ColumnEvent event, IReportContent report,
			ITableContent table )
	{
		Column col = new Column( report );

		CrosstabCellHandle handle = null;

		switch ( event.type )
		{
			case ColumnEvent.ROW_EDGE_CHANGE :
				// use row level cell
				handle = crosstabItem.getDimension( ROW_AXIS_TYPE,
						event.dimensionIndex )
						.getLevel( event.levelIndex )
						.getCell( );
				break;
			case ColumnEvent.MEASURE_HEADER_CHANGE :
				// use first measure header cell
				for ( int i = 0; i < crosstabItem.getMeasureCount( ); i++ )
				{
					MeasureViewHandle mv = crosstabItem.getMeasure( i );
					if ( mv.getHeader( ) != null )
					{
						handle = mv.getHeader( );
						break;
					}
				}
				break;
			case ColumnEvent.COLUMN_EDGE_CHANGE :
				if ( crosstabItem.getMeasureCount( ) > 0
						&& event.measureIndex >= 0 )
				{
					// use measure cell
					handle = crosstabItem.getMeasure( event.measureIndex )
							.getCell( );
				}
				else
				{
					// use innerest column level cell
					DimensionViewHandle dv = crosstabItem.getDimension( COLUMN_AXIS_TYPE,
							crosstabItem.getDimensionCount( COLUMN_AXIS_TYPE ) - 1 );
					handle = dv.getLevel( dv.getLevelCount( ) - 1 ).getCell( );
				}
				break;
			case ColumnEvent.COLUMN_TOTAL_CHANGE :
				if ( crosstabItem.getMeasureCount( ) > 0
						&& event.measureIndex >= 0 )
				{
					// use measure cell
					handle = crosstabItem.getMeasure( event.measureIndex )
							.getCell( );
				}
				else
				{
					// use column sub total cell
					DimensionViewHandle dv = crosstabItem.getDimension( COLUMN_AXIS_TYPE,
							event.dimensionIndex );
					handle = dv.getLevel( event.levelIndex )
							.getAggregationHeader( );
				}
				break;
			case ColumnEvent.GRAND_TOTAL_CHANGE :
				if ( crosstabItem.getMeasureCount( ) > 0
						&& event.measureIndex >= 0 )
				{
					// use measure cell
					handle = crosstabItem.getMeasure( event.measureIndex )
							.getCell( );
				}
				else
				{
					// use column grand total cell
					handle = crosstabItem.getGrandTotal( COLUMN_AXIS_TYPE );
				}
				break;
			case ColumnEvent.MEASURE_CHANGE :
				// use measure cell
				int mx = event.measureIndex >= 0 ? event.measureIndex : 0;
				handle = crosstabItem.getMeasure( mx ).getCell( );
				break;
		}

		if ( handle != null )
		{
			// TODO setup style
			// TODO setup highlight

			DimensionType width = createDimension( handle.getWidth( ) );
			col.setWidth( width );

			// processStyle( handle );
			// processVisibility( handle );
			// processBookmark( handle );

		}

		table.addColumn( col );
	}

	private DimensionType createDimension( DimensionHandle handle )
	{
		if ( handle == null || !handle.isSet( ) )
		{
			return null;
		}

		// Extended Choice
		if ( handle.isKeyword( ) )
		{
			return new DimensionType( handle.getStringValue( ) );
		}

		// set measure and unit
		double measure = handle.getMeasure( );
		String unit = handle.getUnits( );
		return new DimensionType( measure, unit );
	}

}
