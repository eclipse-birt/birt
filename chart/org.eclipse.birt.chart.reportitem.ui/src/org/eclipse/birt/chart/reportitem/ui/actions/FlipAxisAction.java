/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.chart.reportitem.ui.actions;

import java.util.List;

import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.component.Axis;
import org.eclipse.birt.chart.model.data.Query;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.reportitem.ChartXTabUtil;
import org.eclipse.birt.chart.reportitem.ui.ChartXTabUIUtil;
import org.eclipse.birt.chart.reportitem.ui.i18n.Messages;
import org.eclipse.birt.chart.ui.swt.wizard.ChartWizard;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.item.crosstab.core.de.AggregationCellHandle;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.jface.action.Action;

/**
 * 
 */

public class FlipAxisAction extends Action
{

	private final ExtendedItemHandle eih;
	private final ChartWithAxes cwa;

	public FlipAxisAction( ExtendedItemHandle eih )
	{
		super( Messages.getString( "FlipAxisAction.Text.FlipAxis" ),//$NON-NLS-1$
				Action.AS_CHECK_BOX );
		this.eih = eih;
		this.cwa = (ChartWithAxes) ChartXTabUtil.getChartFromHandle( eih );
		this.setChecked( cwa.isTransposed( ) );
		this.setEnabled( checkEnabled( ) );
	}

	private boolean checkEnabled( )
	{
		try
		{
			AggregationCellHandle containerCell = ChartXTabUtil.getXtabContainerCell( eih );
			if ( containerCell != null )
			{
				List<String> exprs = ChartXTabUtil.getAllLevelsBindingExpression( containerCell.getCrosstab( ) );
				// Grand total always supports only one drection
				return exprs.size( ) == 2
						&& !ChartXTabUtil.isAggregationCell( containerCell );
			}
		}
		catch ( BirtException e )
		{
			ChartWizard.displayException( e );
		}
		return false;
	}

	@Override
	public void run( )
	{
		try
		{
			AggregationCellHandle containerCell = ChartXTabUtil.getXtabContainerCell( eih );
			if ( containerCell != null )
			{
				List<String> exprs = ChartXTabUtil.getAllLevelsBindingExpression( containerCell.getCrosstab( ) );
				Query query = (Query) ( (SeriesDefinition) ( (Axis) cwa.getAxes( )
						.get( 0 ) ).getSeriesDefinitions( ).get( 0 ) ).getDesignTimeSeries( )
						.getDataDefinition( )
						.get( 0 );
				if ( cwa.isTransposed( ) )
				{
					cwa.setTransposed( false );
					query.setDefinition( exprs.get( 0 ) );
					ChartXTabUIUtil.updateXTabForAxis( containerCell,
							eih,
							true,
							cwa );
				}
				else
				{
					cwa.setTransposed( true );
					query.setDefinition( exprs.get( 1 ) );
					ChartXTabUIUtil.updateXTabForAxis( containerCell,
							eih,
							false,
							cwa );
				}
			}
		}
		catch ( BirtException e )
		{
			ChartWizard.displayException( e );
		}
	}
}
