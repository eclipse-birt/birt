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

import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.ChartWithoutAxes;
import org.eclipse.birt.chart.model.attribute.SortOption;
import org.eclipse.birt.chart.model.component.Axis;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.data.DataPackage;
import org.eclipse.birt.chart.model.data.Query;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.model.data.impl.QueryImpl;
import org.eclipse.birt.chart.ui.extension.i18n.Messages;
import org.eclipse.birt.chart.ui.swt.interfaces.IUIServiceProvider;
import org.eclipse.birt.chart.ui.swt.wizard.ChartWizardContext;
import org.eclipse.birt.chart.ui.util.UIHelper;
import org.eclipse.birt.chart.util.LiteralHelper;
import org.eclipse.birt.core.ui.frameworks.taskwizard.WizardBase;
import org.eclipse.emf.common.util.EList;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

/**
 * The dialog is used to set grouping and sort condition of base series.
 */
public class BaseGroupSortingDialog extends GroupSortingDialog implements
		SelectionListener
{

	private Combo cmbSortExpr;
	private Button btnSortExprBuilder;

	public BaseGroupSortingDialog( Shell shell,
			ChartWizardContext wizardContext, SeriesDefinition sd )
	{
		super( shell, wizardContext, sd );
	}

	public void createSortArea( Composite parent )
	{
		Composite cmpSortArea = new Composite( parent, SWT.NONE );
		{
			cmpSortArea.setLayout( new GridLayout( 3, false ) );
			GridData gd = new GridData( GridData.FILL_HORIZONTAL );
			gd.horizontalSpan = 2;
			cmpSortArea.setLayoutData( gd );
		}
		Label lblSorting = new Label( cmpSortArea, SWT.NONE );
		lblSorting.setText( Messages.getString( "BaseSeriesDataSheetImpl.Lbl.DataSorting" ) ); //$NON-NLS-1$

		cmbSorting = new Combo( cmpSortArea, SWT.DROP_DOWN | SWT.READ_ONLY );
		GridData gdCMBSorting = new GridData( GridData.FILL_HORIZONTAL );
		cmbSorting.setLayoutData( gdCMBSorting );
		cmbSorting.addListener( SWT.Selection, this );

		new Label( cmpSortArea, SWT.NONE );

		// Add sort column selection composites.
		Label lblSortExpr = new Label( cmpSortArea, SWT.NONE );
		lblSortExpr.setText( Messages.getString("BaseGroupSortingDialog.Label.SortOn") ); //$NON-NLS-1$

		cmbSortExpr = new Combo( cmpSortArea, SWT.DROP_DOWN | SWT.READ_ONLY );
		GridData gdCMBSortExpr = new GridData( GridData.FILL_HORIZONTAL );
		cmbSortExpr.setLayoutData( gdCMBSortExpr );
		cmbSortExpr.addListener( SWT.Selection, this );

		btnSortExprBuilder = new Button( cmpSortArea, SWT.PUSH );
		{
			GridData gdBTNBuilder = new GridData( );
			gdBTNBuilder.heightHint = 20;
			gdBTNBuilder.widthHint = 20;
			btnSortExprBuilder.setLayoutData( gdBTNBuilder );
			btnSortExprBuilder.setImage( UIHelper.getImage( "icons/obj16/expressionbuilder.gif" ) ); //$NON-NLS-1$
			btnSortExprBuilder.addSelectionListener( this );
			btnSortExprBuilder.setToolTipText( Messages.getString( "DataDefinitionComposite.Tooltip.InvokeExpressionBuilder" ) ); //$NON-NLS-1$
			btnSortExprBuilder.getImage( )
					.setBackground( btnSortExprBuilder.getBackground( ) );
			btnSortExprBuilder.setEnabled( wizardContext.getUIServiceProvider( )
					.isInvokingSupported( ) );
			btnSortExprBuilder.setVisible( wizardContext.getUIServiceProvider( )
					.isEclipseModeSupported( ) );
		}
		
		if ( yGroupingEnabled() )
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
		if ( event.widget == cmbSorting )
		{
			if ( cmbSorting.getText( ).equals( UNSORTED_OPTION ) )
			{
				getSeriesDefinitionForProcessing( ).eUnset( DataPackage.eINSTANCE.getSeriesDefinition_Sorting( ) );
			}
			else
			{
				getSeriesDefinitionForProcessing( ).setSorting( SortOption.getByName( LiteralHelper.sortOptionSet.getNameByDisplayName( cmbSorting.getText( ) ) ) );
				if ( yGroupingEnabled() )
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
	 * @see org.eclipse.swt.events.SelectionListener#widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent)
	 */
	public void widgetDefaultSelected( SelectionEvent e )
	{
		;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.events.SelectionListener#widgetSelected(org.eclipse.swt.events.SelectionEvent)
	 */
	public void widgetSelected( SelectionEvent e )
	{
		if ( e.getSource( ).equals( btnSortExprBuilder ) )
		{
			try
			{
				String sExpr = wizardContext.getUIServiceProvider( )
						.invoke( IUIServiceProvider.COMMAND_EXPRESSION_DATA_BINDINGS,
								cmbSortExpr.getText( ),
								wizardContext.getExtendedItem( ),
								"" ); //$NON-NLS-1$

				String items[] = cmbSortExpr.getItems( );
				boolean contains = false;
				for ( int i = 0; i < items.length; i++ )
				{
					if ( items[i].equals( sExpr ) )
					{
						contains = true;
						break;
					}
				}
				if ( !contains )
				{
					cmbSortExpr.add( sExpr );
				}
				cmbSortExpr.setText( sExpr );

				getSeriesDefinitionForProcessing( ).getSortKey( )
						.setDefinition( sExpr );
			}
			catch ( ChartException e1 )
			{
				WizardBase.displayException( e1 );
			}
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
			setSortKeySelectionState( false );
		}
		else
		{
			exprList.addAll( getBaseSeriesExpression( ) );
			if ( !yGroupingEnabled( ) )
			{
				exprList.addAll( getValueSeriesExpressions( ) );
			}

			if ( !yGroupingEnabled( ) )
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

	/**
	 * Set state of SortKey components.
	 * 
	 * @param enabled
	 */
	private void setSortKeySelectionState( boolean enabled )
	{
		cmbSortExpr.setEnabled( enabled );
		btnSortExprBuilder.setEnabled( enabled );
	}

	/**
	 * Initialize SortKey if it doesn't exist.
	 */
	private void initSortKey( )
	{
		if ( getSeriesDefinitionForProcessing( ).getSortKey( ) == null )
		{
			getSeriesDefinitionForProcessing( ).setSortKey( QueryImpl.create( null ) );
		}
	}
	
	/**
	 * check if Y grouping is set.
	 * 
	 * @return
	 */
	private boolean yGroupingEnabled( )
	{
		SeriesDefinition baseSD = null;
		SeriesDefinition orthSD = null;
		Object[] orthAxisArray = null;
		Chart cm = wizardContext.getModel( );
		if ( cm instanceof ChartWithAxes )
		{
			ChartWithAxes cwa = (ChartWithAxes) cm;
			baseSD = (SeriesDefinition) cwa.getBaseAxes( )[0].getSeriesDefinitions( )
					.get( 0 );

			orthAxisArray = cwa.getOrthogonalAxes( cwa.getBaseAxes( )[0], true );
			orthSD = (SeriesDefinition) ( (Axis) orthAxisArray[0] ).getSeriesDefinitions( )
					.get( 0 );
		}
		else if ( cm instanceof ChartWithoutAxes )
		{
			ChartWithoutAxes cwoa = (ChartWithoutAxes) cm;
			baseSD = (SeriesDefinition) cwoa.getSeriesDefinitions( ).get( 0 );
			orthSD = (SeriesDefinition) baseSD.getSeriesDefinitions( ).get( 0 );
		}

		String yGroupExpr = null;
		if ( orthSD.getQuery( ) != null )
		{
			yGroupExpr = orthSD.getQuery( ).getDefinition( );
		}
		
		return yGroupExpr != null && !"".equals( yGroupExpr ); //$NON-NLS-1$
	}
	
	/**
	 * Get expressions of base series.
	 * 
	 * @return
	 */
	private Set getBaseSeriesExpression( )
	{
		Set exprList = new LinkedHashSet( );
		Chart chart = wizardContext.getModel( );
		if ( chart instanceof ChartWithAxes )
		{
			// Add the expression of base series.
			final Axis axPrimaryBase = ( (ChartWithAxes) chart ).getPrimaryBaseAxes( )[0];
			EList elSD = axPrimaryBase.getSeriesDefinitions( );
			if ( elSD != null && elSD.size( ) >= 1 )
			{
				SeriesDefinition baseSD = (SeriesDefinition) elSD.get( 0 );
				final Series seBase = baseSD.getDesignTimeSeries( );
				EList elBaseSeries = seBase.getDataDefinition( );
				if ( elBaseSeries != null && elBaseSeries.size( ) >= 1 )
				{
					String baseSeriesExpression = ( (Query) elBaseSeries.get( 0 ) ).getDefinition( );
					exprList.add( baseSeriesExpression );
				}
			}
		}
		else
		{
			EList lstSDs = ( (ChartWithoutAxes) chart ).getSeriesDefinitions( );
			for ( int i = 0; i < lstSDs.size( ); i++ )
			{
				// Add base expression.
				SeriesDefinition sd = (SeriesDefinition) lstSDs.get( i );
				Series series = sd.getDesignTimeSeries( );
				EList seriesList = series.getDataDefinition( );
				if ( seriesList.size( ) > 0 )
				{
					for ( int j = 0; j < seriesList.size( ); j++ )
					{
						Query qSeries = (Query) seriesList.get( j );
						if ( qSeries == null ) // NPE PROTECTION
						{
							continue;
						}
						if ( qSeries.getDefinition( ) != null )
						{
							exprList.add( qSeries.getDefinition( ) );
						}
					}
				}
			}
		}
		return exprList;
	}

	/**
	 * Get the expressions of value series.
	 * 
	 * @return
	 */
	private Set getValueSeriesExpressions( )
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

					Series seOrthogonal = orthoSD.getDesignTimeSeries( );
					EList elOrthogonalSeries = seOrthogonal.getDataDefinition( );
					if ( elOrthogonalSeries.size( ) > 0 )
					{
						for ( int i = 0; i < elOrthogonalSeries.size( ); i++ )
						{
							Query qOrthogonalSeries = (Query) elOrthogonalSeries.get( i );
							if ( qOrthogonalSeries == null ) // NPE
							// PROTECTION
							{
								continue;
							}
							if ( qOrthogonalSeries.getDefinition( ) != null )
							{
								exprList.add( qOrthogonalSeries.getDefinition( ) );
							}
						}
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
					Series orthSeries = orthSD.getDesignTimeSeries( );
					EList orthSeriesList = orthSeries.getDataDefinition( );
					if ( orthSeriesList.size( ) > 0 )
					{
						for ( int j = 0; j < orthSeriesList.size( ); j++ )
						{
							Query qSeries = (Query) orthSeriesList.get( j );
							if ( qSeries == null ) // NPE PROTECTION
							{
								continue;
							}
							if ( qSeries.getDefinition( ) != null )
							{
								exprList.add( qSeries.getDefinition( ) );
							}
						}
					}
				}
			}
		}

		return exprList;
	}
}
