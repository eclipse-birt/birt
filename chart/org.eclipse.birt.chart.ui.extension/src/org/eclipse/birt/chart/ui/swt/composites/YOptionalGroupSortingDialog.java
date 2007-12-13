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

package org.eclipse.birt.chart.ui.swt.composites;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.ChartWithoutAxes;
import org.eclipse.birt.chart.model.component.Axis;
import org.eclipse.birt.chart.model.data.DataPackage;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.ui.swt.wizard.ChartWizardContext;
import org.eclipse.emf.common.util.EList;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Shell;


/**
 * The dialog is used to set Y grouping and sorting attributes.
 * @since BIRT 2.3
 */
public class YOptionalGroupSortingDialog extends GroupSortingDialog
{

	/**
	 * Constructor of the class.
	 * 
	 * @param shell
	 * @param wizardContext
	 * @param sd
	 * @param disableAggregation
	 * @param hasExprBuilder
	 */
	public YOptionalGroupSortingDialog( Shell shell, ChartWizardContext wizardContext,
			SeriesDefinition sd, boolean disableAggregation,
			boolean hasExprBuilder )
	{
		super( shell, wizardContext, sd, disableAggregation, hasExprBuilder );
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.ui.swt.composites.GroupSortingDialog#handleEvent(org.eclipse.swt.widgets.Event)
	 */
	public void handleEvent( Event event )
	{
		super.handleEvent( event );
		
		if ( event.widget == cmbSorting )
		{
			populateSortKeyList( );
		}
		else if ( event.widget == cmbSortExpr )
		{
			getSeriesDefinitionForProcessing( ).getSortKey( )
					.setDefinition( cmbSortExpr.getText( ) );
		}
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.birt.chart.ui.swt.composites.GroupSortingDialog#populateLists()
	 */
	protected void populateLists( )
	{
		super.populateLists( );
		populateSortKeyList( );
	}
	
	/**
	 * Populate sort key expressions.
	 */
	private void populateSortKeyList( )
	{
		initSortKey( );
		
		Set exprList = new LinkedHashSet( );
		String sortExpr = null;
		
		if ( !isYGroupingEnabled( ) || cmbSorting.getText( ).equals( UNSORTED_OPTION ) )
		{
			getSeriesDefinitionForProcessing( ).eUnset( DataPackage.eINSTANCE.getSeriesDefinition_Sorting( ) );
			exprList.add( "" ); //$NON-NLS-1$
		}
		else
		{
			exprList.addAll( getYGroupingExpressions( ) );
			exprList.addAll( getValueSeriesExpressions( ) );

			sortExpr = this.getSeriesDefinitionForProcessing( )
						.getSortKey( )
						.getDefinition( );
			setSortKeySelectionState( true );
		}

		if ( sortExpr != null && !"".equals( sortExpr ) ) //$NON-NLS-1$
		{
			exprList.add( sortExpr );
		}

		cmbSortExpr.removeAll( );
		for ( Iterator iter = exprList.iterator( ); iter.hasNext( ); )
		{
			cmbSortExpr.add( (String) iter.next( ) );
		}

		if ( sortExpr != null && !"".equals( sortExpr ) ) //$NON-NLS-1$
		{
			cmbSortExpr.setText( sortExpr );
		}
		else
		{
			cmbSortExpr.select( 0 );
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.birt.chart.ui.swt.composites.GroupSortingDialog#createSortArea(org.eclipse.swt.widgets.Composite)
	 */
	public void createSortArea( Composite parent )
	{
		super.createSortArea( parent );
		if ( !isYGroupingEnabled() )
		{
			cmbSorting.setEnabled( false );
			cmbSortExpr.setEnabled( false );
		}
	}
	
	/**
	 * Get the Y Grouping expression.
	 * 
	 * @return
	 */
	protected Set getYGroupingExpressions( )
	{
		Set exprList = new LinkedHashSet( );
		Chart chart = wizardContext.getModel( );
		if ( chart instanceof ChartWithAxes )
		{
			final Axis axPrimaryBase = ( (ChartWithAxes) chart ).getPrimaryBaseAxes( )[0];

			// Add expressions of value series.
			final Axis[] axaOrthogonal = ( (ChartWithAxes) chart ).getOrthogonalAxes( axPrimaryBase,
					true );
			for ( int j = 0; j < axaOrthogonal.length; j++ )
			{
				EList lstOrthogonalSDs = axaOrthogonal[j].getSeriesDefinitions( );
				for ( int k = 0; k < lstOrthogonalSDs.size( ); k++ )
				{
					SeriesDefinition orthoSD = (SeriesDefinition) lstOrthogonalSDs.get( k );
					if ( orthoSD.getQuery( ) != null &&
							orthoSD.getQuery( ).getDefinition( ) != null )
					{
						exprList.add( orthoSD.getQuery( ).getDefinition( ) );
					}
				}
			}
		}
		else
		{
			EList lstSDs = ( (ChartWithoutAxes) chart ).getSeriesDefinitions( );
			for ( int i = 0; i < lstSDs.size( ); i++ )
			{
				SeriesDefinition sd = (SeriesDefinition) lstSDs.get( i );

				// Add value series expressions.
				EList orthSDs = sd.getSeriesDefinitions( );
				for ( Iterator iter = orthSDs.iterator( ); iter.hasNext( ); )
				{

					SeriesDefinition orthSD = (SeriesDefinition) iter.next( );;
					if ( orthSD.getQuery( ) != null &&
							orthSD.getQuery( ).getDefinition( ) != null )
					{
						exprList.add( orthSD.getQuery( ).getDefinition( ) );
					}
				}
			}
		}

		return exprList;
	}
}
