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

package org.eclipse.birt.report.item.crosstab.core.re;

import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.core.data.ExpressionUtil;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.IDataQueryDefinition;
import org.eclipse.birt.report.engine.extension.ReportItemQueryBase;
import org.eclipse.birt.report.item.crosstab.core.CrosstabException;
import org.eclipse.birt.report.item.crosstab.core.ICrosstabConstants;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabCellHandle;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabReportItemHandle;
import org.eclipse.birt.report.item.crosstab.core.de.DimensionViewHandle;
import org.eclipse.birt.report.item.crosstab.core.de.LevelViewHandle;
import org.eclipse.birt.report.item.crosstab.core.de.MeasureViewHandle;
import org.eclipse.birt.report.item.crosstab.core.i18n.Messages;
import org.eclipse.birt.report.model.api.DataItemHandle;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.birt.report.model.api.ReportElementHandle;
import org.eclipse.birt.report.model.api.StructureFactory;
import org.eclipse.birt.report.model.api.StyleHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.elements.structures.MapRule;
import org.eclipse.birt.report.model.api.extension.ExtendedElementException;

/**
 * CrosstabReportItemQuery
 */
public class CrosstabReportItemQuery extends ReportItemQueryBase implements
		ICrosstabConstants
{

	private static Logger logger = Logger.getLogger( CrosstabReportItemQuery.class.getName( ) );

	private CrosstabReportItemHandle crosstabItem;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.extension.ReportItemQueryBase#setModelObject(org.eclipse.birt.report.model.api.ExtendedItemHandle)
	 */
	public void setModelObject( ExtendedItemHandle modelHandle )
	{
		super.setModelObject( modelHandle );

		try
		{
			crosstabItem = (CrosstabReportItemHandle) modelHandle.getReportItem( );
		}
		catch ( ExtendedElementException e )
		{
			logger.log( Level.SEVERE,
					Messages.getString( "CrosstabReportItemQuery.error.crosstab.loading" ) ); //$NON-NLS-1$
			crosstabItem = null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.extension.ReportItemQueryBase#createReportQueries(org.eclipse.birt.data.engine.api.IDataQueryDefinition)
	 */
	public IDataQueryDefinition[] createReportQueries(
			IDataQueryDefinition parent ) throws BirtException
	{
		if ( crosstabItem == null )
		{
			throw new CrosstabException( modelHandle == null ? null
					: modelHandle.getElement( ),
					Messages.getString( "CrosstabReportItemQuery.error.query.building" ) ); //$NON-NLS-1$
		}

		IDataQueryDefinition cubeQuery = CrosstabQueryHelper.buildQuery( crosstabItem,
				parent );

		String emptyValue = crosstabItem.getEmptyCellValue( );

		// build child element query
		if ( context != null )
		{
			// process measure
			for ( int i = 0; i < crosstabItem.getMeasureCount( ); i++ )
			{
				// TODO check visibility?
				MeasureViewHandle mv = crosstabItem.getMeasure( i );

				processChildQuery( cubeQuery, mv.getCell( ), emptyValue );
				processChildQuery( cubeQuery, mv.getHeader( ), emptyValue );

				for ( int j = 0; j < mv.getAggregationCount( ); j++ )
				{
					processChildQuery( cubeQuery,
							mv.getAggregationCell( j ),
							emptyValue );
				}
			}

			// process row edge
			if ( crosstabItem.getDimensionCount( ROW_AXIS_TYPE ) > 0 )
			{
				// TODO check visibility?
				for ( int i = 0; i < crosstabItem.getDimensionCount( ROW_AXIS_TYPE ); i++ )
				{
					DimensionViewHandle dv = crosstabItem.getDimension( ROW_AXIS_TYPE,
							i );

					for ( int j = 0; j < dv.getLevelCount( ); j++ )
					{
						LevelViewHandle lv = dv.getLevel( j );

						processChildQuery( cubeQuery, lv.getCell( ), emptyValue );
						processChildQuery( cubeQuery,
								lv.getAggregationHeader( ),
								emptyValue );
					}
				}

			}

			// process column edge
			if ( crosstabItem.getDimensionCount( COLUMN_AXIS_TYPE ) > 0 )
			{
				// TODO check visibility?
				for ( int i = 0; i < crosstabItem.getDimensionCount( COLUMN_AXIS_TYPE ); i++ )
				{
					DimensionViewHandle dv = crosstabItem.getDimension( COLUMN_AXIS_TYPE,
							i );

					for ( int j = 0; j < dv.getLevelCount( ); j++ )
					{
						LevelViewHandle lv = dv.getLevel( j );

						processChildQuery( cubeQuery, lv.getCell( ), emptyValue );
						processChildQuery( cubeQuery,
								lv.getAggregationHeader( ),
								emptyValue );
					}
				}

			}

			// process grandtotal header
			processChildQuery( cubeQuery,
					crosstabItem.getGrandTotal( ROW_AXIS_TYPE ),
					emptyValue );
			processChildQuery( cubeQuery,
					crosstabItem.getGrandTotal( COLUMN_AXIS_TYPE ),
					emptyValue );
		}

		return new IDataQueryDefinition[]{
			cubeQuery
		};
	}

	private void processChildQuery( IDataQueryDefinition parent,
			CrosstabCellHandle cell, String emptyVlaue )
	{
		if ( cell != null )
		{
			for ( Iterator itr = cell.getContents( ).iterator( ); itr.hasNext( ); )
			{
				ReportElementHandle handle = (ReportElementHandle) itr.next( );

				// handle empty value mapping
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

				context.createQuery( parent, handle );
			}
		}
	}

}
