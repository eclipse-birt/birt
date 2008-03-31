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
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.core.data.ExpressionUtil;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.extension.IPreparationContext;
import org.eclipse.birt.report.item.crosstab.core.ICrosstabConstants;
import org.eclipse.birt.report.item.crosstab.core.ICrosstabReportItemConstants;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabCellHandle;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabReportItemHandle;
import org.eclipse.birt.report.item.crosstab.core.de.DimensionViewHandle;
import org.eclipse.birt.report.item.crosstab.core.de.LevelViewHandle;
import org.eclipse.birt.report.item.crosstab.core.de.MeasureViewHandle;
import org.eclipse.birt.report.item.crosstab.core.i18n.Messages;
import org.eclipse.birt.report.item.crosstab.core.script.ICrosstab;
import org.eclipse.birt.report.item.crosstab.core.script.ICrosstabCell;
import org.eclipse.birt.report.item.crosstab.core.script.internal.CrosstabCellImpl;
import org.eclipse.birt.report.item.crosstab.core.script.internal.CrosstabImpl;
import org.eclipse.birt.report.model.api.DataItemHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.birt.report.model.api.ReportElementHandle;
import org.eclipse.birt.report.model.api.StructureFactory;
import org.eclipse.birt.report.model.api.StyleHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.elements.structures.MapRule;

/**
 * CrosstabPreparationHandler
 */
public class CrosstabPreparationHandler extends BaseCrosstabEventHandler implements
		ICrosstabConstants
{

	private static Logger logger = Logger.getLogger( CrosstabPreparationHandler.class.getName( ) );

	private CrosstabScriptHandler handler;
	private CrosstabReportItemHandle crosstab;
	private IPreparationContext context;

	public CrosstabPreparationHandler( CrosstabReportItemHandle crosstab,
			IPreparationContext context ) throws BirtException
	{
		ExtendedItemHandle modelHandle = (ExtendedItemHandle) crosstab.getModelHandle( );

		String javaClass = modelHandle.getEventHandlerClass( );
		String script = modelHandle.getOnPrepare( );

		this.crosstab = crosstab;
		this.context = context;

		if ( ( javaClass == null || javaClass.trim( ).length( ) == 0 )
				&& ( script == null || script.trim( ).length( ) == 0 ) )
		{
			return;
		}

		handler = createScriptHandler( modelHandle,
				ICrosstabReportItemConstants.ON_PREPARE_METHOD,
				script,
				context.getApplicationClassLoader( ) );

	}

	public void handle( ) throws BirtException
	{
		if ( handler != null )
		{
			ICrosstab crosstabItem = new CrosstabImpl( crosstab );

			handler.callFunction( CrosstabScriptHandler.ON_PREPARE_CROSSTAB,
					crosstabItem,
					context );
		}

		handleChildren( );
	}

	private void handleChildren( ) throws BirtException
	{
		String emptyValue = crosstab.getEmptyCellValue( );

		// process crosstab header
		handleCell( crosstab.getHeader( ), emptyValue );

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

					handleCell( lv.getCell( ), emptyValue );
					handleCell( lv.getAggregationHeader( ), emptyValue );
				}
			}

		}

		// process column grandtotal header
		handleCell( crosstab.getGrandTotal( COLUMN_AXIS_TYPE ), emptyValue );

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

					handleCell( lv.getCell( ), emptyValue );
					handleCell( lv.getAggregationHeader( ), emptyValue );
				}
			}

		}

		// process row grandtotal header
		handleCell( crosstab.getGrandTotal( ROW_AXIS_TYPE ), emptyValue );

		// process measure
		for ( int i = 0; i < crosstab.getMeasureCount( ); i++ )
		{
			// TODO check visibility?
			MeasureViewHandle mv = crosstab.getMeasure( i );

			handleCell( mv.getHeader( ), emptyValue );
			handleCell( mv.getCell( ), emptyValue );

			for ( int j = 0; j < mv.getAggregationCount( ); j++ )
			{
				handleCell( mv.getAggregationCell( j ), emptyValue );
			}
		}
	}

	@SuppressWarnings("unchecked")
	private void handleCell( CrosstabCellHandle cell, String emptyVlaue )
			throws BirtException
	{
		if ( cell == null )
		{
			return;
		}

		if ( handler != null )
		{
			ICrosstabCell cellItem = new CrosstabCellImpl( cell );

			handler.callFunction( CrosstabScriptHandler.ON_PREPARE_CELL,
					cellItem,
					context );
		}

		// prepare contents
		for ( Iterator itr = cell.getContents( ).iterator( ); itr.hasNext( ); )
		{

			ReportElementHandle handle = (ReportElementHandle) itr.next( );

			context.prepare( handle );

			// handle empty value mapping, this is done by adding an extra
			// mapping rule to related data item.
			if ( emptyVlaue != null && handle instanceof DataItemHandle )
			{
				DataItemHandle dataHandle = (DataItemHandle) handle;

				MapRule rule = StructureFactory.createMapRule( );

				rule.setTestExpression( ExpressionUtil.createJSDataExpression( dataHandle.getResultSetColumn( ) ) );
				rule.setOperator( DesignChoiceConstants.MAP_OPERATOR_NULL );
				rule.setDisplay( emptyVlaue );

				PropertyHandle mapHandle = dataHandle.getPropertyHandle( StyleHandle.MAP_RULES_PROP );

				try
				{
					mapHandle.addItem( rule );
				}
				catch ( SemanticException e )
				{
					logger.log( Level.SEVERE,
							Messages.getString( "CrosstabReportItemQuery.error.register.empty.cell.value" ), //$NON-NLS-1$
							e );
				}
			}
		}
	}
}
