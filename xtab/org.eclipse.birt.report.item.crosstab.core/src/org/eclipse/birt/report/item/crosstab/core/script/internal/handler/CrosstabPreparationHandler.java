/*******************************************************************************
 * Copyright (c) 2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.item.crosstab.core.script.internal.handler;

import java.util.Iterator;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.extension.IPreparationContext;
import org.eclipse.birt.report.item.crosstab.core.ICrosstabConstants;
import org.eclipse.birt.report.item.crosstab.core.ICrosstabReportItemConstants;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabCellHandle;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabReportItemHandle;
import org.eclipse.birt.report.item.crosstab.core.de.DimensionViewHandle;
import org.eclipse.birt.report.item.crosstab.core.de.LevelViewHandle;
import org.eclipse.birt.report.item.crosstab.core.de.MeasureViewHandle;
import org.eclipse.birt.report.item.crosstab.core.script.ICrosstab;
import org.eclipse.birt.report.item.crosstab.core.script.ICrosstabCell;
import org.eclipse.birt.report.item.crosstab.core.script.internal.CrosstabCellImpl;
import org.eclipse.birt.report.item.crosstab.core.script.internal.CrosstabImpl;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;

/**
 * CrosstabPreparationHandler
 */
public class CrosstabPreparationHandler extends BaseCrosstabEventHandler implements
		ICrosstabConstants
{

	private CrosstabScriptHandler handler;
	private CrosstabReportItemHandle crosstab;
	private IPreparationContext context;

	public CrosstabPreparationHandler( CrosstabReportItemHandle crosstab,
			IPreparationContext context ) throws BirtException
	{
		ExtendedItemHandle modelHandle = (ExtendedItemHandle) crosstab.getModelHandle( );

		String javaClass = modelHandle.getEventHandlerClass( );
		String script = modelHandle.getOnPrepare( );

		if ( ( javaClass == null || javaClass.trim( ).length( ) == 0 )
				&& ( script == null || script.trim( ).length( ) == 0 ) )
		{
			return;
		}

		this.crosstab = crosstab;
		this.context = context;

		handler = createScriptHandler( modelHandle,
				ICrosstabReportItemConstants.ON_PREPARE_METHOD,
				script,
				context.getApplicationClassLoader( ) );

	}

	public void handle( ) throws BirtException
	{
		if ( handler == null )
		{
			return;
		}

		ICrosstab crosstabItem = new CrosstabImpl( crosstab );

		handler.callFunction( CrosstabScriptHandler.ON_PREPARE_CROSSTAB,
				crosstabItem,
				context );

		handleChildren( );
	}

	private void handleChildren( ) throws BirtException
	{
		// process crosstab header
		handleCell( crosstab.getHeader( ) );

		// process column edge
		if ( crosstab.getDimensionCount( COLUMN_AXIS_TYPE ) > 0 )
		{
			// TODO check visibility?
			for ( int i = 0; i < crosstab.getDimensionCount( COLUMN_AXIS_TYPE ); i++ )
			{
				DimensionViewHandle dv = crosstab.getDimension( COLUMN_AXIS_TYPE,
						i );

				for ( int j = 0; j < dv.getLevelCount( ); j++ )
				{
					LevelViewHandle lv = dv.getLevel( j );

					handleCell( lv.getCell( ) );
					handleCell( lv.getAggregationHeader( ) );
				}
			}

		}

		// process column grandtotal header
		handleCell( crosstab.getGrandTotal( COLUMN_AXIS_TYPE ) );

		// process row edge
		if ( crosstab.getDimensionCount( ROW_AXIS_TYPE ) > 0 )
		{
			// TODO check visibility?
			for ( int i = 0; i < crosstab.getDimensionCount( ROW_AXIS_TYPE ); i++ )
			{
				DimensionViewHandle dv = crosstab.getDimension( ROW_AXIS_TYPE,
						i );

				for ( int j = 0; j < dv.getLevelCount( ); j++ )
				{
					LevelViewHandle lv = dv.getLevel( j );

					handleCell( lv.getCell( ) );
					handleCell( lv.getAggregationHeader( ) );
				}
			}

		}

		// process row grandtotal header
		handleCell( crosstab.getGrandTotal( ROW_AXIS_TYPE ) );

		// process measure
		for ( int i = 0; i < crosstab.getMeasureCount( ); i++ )
		{
			// TODO check visibility?
			MeasureViewHandle mv = crosstab.getMeasure( i );

			handleCell( mv.getHeader( ) );
			handleCell( mv.getCell( ) );

			for ( int j = 0; j < mv.getAggregationCount( ); j++ )
			{
				handleCell( mv.getAggregationCell( j ) );
			}
		}
	}

	@SuppressWarnings("unchecked")
	private void handleCell( CrosstabCellHandle cell ) throws BirtException
	{
		if ( cell == null )
		{
			return;
		}

		ICrosstabCell cellItem = new CrosstabCellImpl( cell );

		handler.callFunction( CrosstabScriptHandler.ON_PREPARE_CELL,
				cellItem,
				context );

		// prepare contents
		for ( Iterator itr = cell.getContents( ).iterator( ); itr.hasNext( ); )
		{
			context.prepare( (DesignElementHandle) itr.next( ) );
		}
	}
}
