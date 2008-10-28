/*******************************************************************************
 * Copyright (c) 2007, 2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.chart.ui.swt.composites;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.ChartWithoutAxes;
import org.eclipse.birt.chart.model.component.Axis;
import org.eclipse.birt.chart.model.data.DataPackage;
import org.eclipse.birt.chart.model.data.Query;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.model.data.SeriesGrouping;
import org.eclipse.birt.chart.model.data.impl.SeriesGroupingImpl;
import org.eclipse.birt.chart.ui.extension.i18n.Messages;
import org.eclipse.birt.chart.ui.swt.wizard.ChartWizardContext;
import org.eclipse.emf.common.util.EList;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
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
		
		setSortKeyInModel( );
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
	
	/* (non-Javadoc)
	 * @see org.eclipse.birt.chart.ui.swt.composites.GroupSortingDialog#createSeriesGroupingComposite(org.eclipse.swt.widgets.Composite)
	 */
	protected SeriesGroupingComposite createSeriesGroupingComposite( Composite parent )
	{
		SeriesGrouping grouping = getSeriesDefinitionForProcessing( ).getQuery( ).getGrouping( );
		if ( grouping == null )
		{
			grouping = SeriesGroupingImpl.create( );
			getSeriesDefinitionForProcessing( ).getQuery( ).setGrouping( grouping );
		}
		
		SeriesGroupingComposite sgc = new YSeriesGroupingComposite( parent,
				SWT.NONE,
				grouping,
				fEnableAggregation,
				wizardContext,
				null );
		sgc.setGroupingButtionEnabled( false );
		return sgc;
	}
	
	/**
	 * 
	 */
	class YSeriesGroupingComposite extends SeriesGroupingComposite
	{
		private List<String[]> preDTPatterns = new ArrayList<String[]>( );
		private Label lblpattern;
		private CCombo txtpattern;

		public YSeriesGroupingComposite( Composite parent, int style,
				SeriesGrouping grouping, boolean aggEnabled,
				ChartWizardContext context, String title )
		{
			super( parent, style, grouping, aggEnabled, context, title );
			fillDatePatterns( );
			updatePatternStatus( );
		}

		private void fillDatePatterns( )
		{
			preDTPatterns.add( new String[]{
					"ss", "mm:ss", "HH:mm:ss" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			} );
			preDTPatterns.add( new String[]{
					"mm", "HH:mm", "dd, HH:mm", "MM-dd, HH:mm", "MMM dd, HH:mm" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
			} );
			preDTPatterns.add( new String[]{
					"HH", "EEE, HH'h'", "MMM-dd, hh, a", "MMMM, dd, HH" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
			} );
			preDTPatterns.add( new String[]{
					"MMM dd", "DDD 'of' yyyy", "MMM dd, yyyy" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			} );
			preDTPatterns.add( new String[]{
					"yyyy, 'Week'ww", "yyyy-LLL, W" //$NON-NLS-1$ //$NON-NLS-2$
			} );
			preDTPatterns.add( new String[]{
					"LLL", "MMMM", "yyyy, LLL", "yyyy, MMMM" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
			} );
			preDTPatterns.add( new String[]{
					"yyyy, QQQ", "yyyy, QQQQ" //$NON-NLS-1$ //$NON-NLS-2$
			} );
			preDTPatterns.add( new String[]{
					"'Y' yyyy", "'Year' yy" //$NON-NLS-1$ //$NON-NLS-2$
			} );
		}

		/* (non-Javadoc)
		 * @see org.eclipse.birt.chart.ui.swt.composites.SeriesGroupingComposite#setGroupingButtonStatus()
		 */
		protected void setGroupingButtonSelection( )
		{
			Query query = getSeriesDefinitionForProcessing( ).getQuery( );
			if ( query != null &&
					query.getDefinition( ) != null &&
					!"".equals( query.getDefinition( ) ) ) //$NON-NLS-1$
			{
				btnEnabled.setSelection( true );
			}
			else
			{
				btnEnabled.setSelection( false );
			}
		}
		
		@Override
		protected void placeComponents( )
		{
			super.placeComponents( );
			
			lblpattern = new Label( grpContent, SWT.NONE );
			GridData gd = new GridData( );
			lblpattern.setLayoutData( gd );
			lblpattern.setText( Messages.getString("YOptionalGroupSortingDialog.CustomPattern") ); //$NON-NLS-1$

			txtpattern = new CCombo( grpContent, SWT.BORDER );
			gd = new GridData( );
			gd.widthHint = 90;
			gd.horizontalSpan = 3;
			txtpattern.setLayoutData( gd );
			if ( fGrouping.isSetPatternForYOptional( ) )
			{
				txtpattern.setText( fGrouping.getPatternForYOptional( ) );
			}
			txtpattern.addSelectionListener( this );
			txtpattern.addFocusListener( new FocusAdapter( ) {
				@Override
				public void focusLost( FocusEvent e )
				{
					fGrouping.setPatternForYOptional( txtpattern.getText( )
							.trim( ) );
				}
			} );
		}
		
		@Override
		protected void setIntervalButtonsStatus( boolean enableUI )
		{
			super.setIntervalButtonsStatus( enableUI );
			lblpattern.setEnabled( enableUI );
			txtpattern.setEnabled( enableUI );
		}
		
		private void updatePatternListForDateTime( )
		{
			if ( isDateTimeGrouping( cmbType.getText( ) ) )
			{
				txtpattern.setItems( preDTPatterns.get( cmbUnit.getSelectionIndex( ) ) );
			}
			else
			{
				// it will be disabled if its type is text
				 txtpattern.setItems( new String[]{
						"##.###", //$NON-NLS-1$
						"00.###", //$NON-NLS-1$
						"##,###.00", //$NON-NLS-1$
						"0.00'K'", //$NON-NLS-1$
						"¤ ##0.00", //$NON-NLS-1$
				} );
			}
			txtpattern.setVisibleItemCount( txtpattern.getItemCount( ) + 1 );
		}
		
		private void updatePatternStatus( )
		{
			if ( cmbType.isEnabled( ) )
			{
				if ( isTextGrouping( cmbType.getText( ) ) )
				{
					lblpattern.setEnabled( false );
					txtpattern.setEnabled( false );
				}
				else
				{
					lblpattern.setEnabled( true );
					txtpattern.setEnabled( true );
				}
				updatePatternListForDateTime( );
			}
		}
		
		@Override
		public void widgetSelected( SelectionEvent e )
		{
			super.widgetSelected( e );
			if ( e.widget == cmbType )
			{
				updatePatternStatus( );
			}
			else if ( e.widget == cmbUnit )
			{
				updatePatternListForDateTime( );
			}
			else if ( e.widget == txtpattern )
			{
				fGrouping.setPatternForYOptional( txtpattern.getText( ).trim( ) );
			}
			
			
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
