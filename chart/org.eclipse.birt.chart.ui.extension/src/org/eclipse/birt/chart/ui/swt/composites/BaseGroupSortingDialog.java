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

import org.eclipse.birt.chart.model.data.DataPackage;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.ui.swt.wizard.ChartWizardContext;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Shell;

/**
 * The dialog is used to set grouping and sort condition of base series.
 */
public class BaseGroupSortingDialog extends GroupSortingDialog
{
	public BaseGroupSortingDialog( Shell shell,
			ChartWizardContext wizardContext, SeriesDefinition sd )
	{
		super( shell, wizardContext, sd );
	}

	public void createSortArea( Composite parent )
	{
		super.createSortArea( parent );
		
		if ( isYGroupingEnabled() )
		{
			setSortKeySelectionState( false );
		}
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
			if ( !cmbSorting.getText( ).equals( UNSORTED_OPTION ) )
			{
				if ( isYGroupingEnabled() )
				{
					// Select base series expression.
					cmbSortExpr.setText( (String) getBaseSeriesExpression().toArray( )[0] );
				}
			}
			
			populateSortKeyList( );
		}
		else if ( event.widget == cmbSortExpr )
		{
			getSeriesDefinitionForProcessing( ).getSortKey( )
					.setDefinition( cmbSortExpr.getText( ) );
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.ui.swt.composites.GroupSortingDialog#populateLists()
	 */
	protected void populateLists( )
	{
		super.populateLists( );
		populateSortKeyList( );
	}

	/**
	 * Populate SortKey list.
	 */
	private void populateSortKeyList( )
	{
		initSortKey( );
		
		Set exprList = new LinkedHashSet( );
		String sortExpr = null;
		
		if ( cmbSorting.getText( ).equals( UNSORTED_OPTION ) )
		{
			getSeriesDefinitionForProcessing( ).eUnset( DataPackage.eINSTANCE.getSeriesDefinition_Sorting( ) );
			exprList.add( "" ); //$NON-NLS-1$
		}
		else
		{
			exprList.addAll( getBaseSeriesExpression( ) );
			if ( !isYGroupingEnabled( ) )
			{
				exprList.addAll( getValueSeriesExpressions( ) );
			}

			if ( !isYGroupingEnabled( ) )
			{
				sortExpr = this.getSeriesDefinitionForProcessing( )
						.getSortKey( )
						.getDefinition( );
				setSortKeySelectionState( true );
			}
			else
			{
				setSortKeySelectionState( false );
			}
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
}
