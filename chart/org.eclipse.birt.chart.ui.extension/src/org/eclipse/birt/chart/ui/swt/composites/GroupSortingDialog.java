/*******************************************************************************
 * Copyright (c) 2006, 2007, 2008 Actuate Corporation.
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
import org.eclipse.birt.chart.model.data.SeriesGrouping;
import org.eclipse.birt.chart.model.data.impl.QueryImpl;
import org.eclipse.birt.chart.model.data.impl.SeriesGroupingImpl;
import org.eclipse.birt.chart.ui.extension.i18n.Messages;
import org.eclipse.birt.chart.ui.swt.interfaces.IDataServiceProvider;
import org.eclipse.birt.chart.ui.swt.interfaces.IUIServiceProvider;
import org.eclipse.birt.chart.ui.swt.wizard.ChartWizardContext;
import org.eclipse.birt.chart.ui.util.ChartHelpContextIds;
import org.eclipse.birt.chart.ui.util.ChartUIUtil;
import org.eclipse.birt.chart.ui.util.UIHelper;
import org.eclipse.birt.chart.util.LiteralHelper;
import org.eclipse.birt.core.ui.frameworks.taskwizard.WizardBase;
import org.eclipse.emf.common.util.EList;
import org.eclipse.jface.dialogs.TrayDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;

/**
 * The dialog is responsible to set grouping and sort condition.
 */

public class GroupSortingDialog extends TrayDialog
		implements
			Listener,
			SelectionListener
{

	protected static final String UNSORTED_OPTION = Messages.getString( "BaseSeriesDataSheetImpl.Choice.Unsorted" ); //$NON-NLS-1$

	protected ChartWizardContext wizardContext;

	private SeriesDefinition sd;

	protected Label lblSorting;

	protected Label lblSortExpr;

	protected Combo cmbSorting;

	protected Combo cmbSortExpr;

	protected Button btnSortExprBuilder;

	/** The field indicates if the aggregation composite should be enabled. */
	protected boolean fEnableAggregation = true;

	/** The field indicates if Expression builder button is enabled. */
	private boolean fHasExprBuilder = true;

	protected SeriesGroupingComposite fGroupingComposite;

	public GroupSortingDialog( Shell shell, ChartWizardContext wizardContext,
			SeriesDefinition sd )
	{
		super( shell );
		this.wizardContext = wizardContext;
		this.sd = sd;
	}

	public GroupSortingDialog( Shell shell, ChartWizardContext wizardContext,
			SeriesDefinition sd, boolean disableAggregation,
			boolean hasExprBuilder )
	{
		super( shell );
		this.wizardContext = wizardContext;
		this.sd = sd;
		this.fEnableAggregation = disableAggregation;
		this.fHasExprBuilder = hasExprBuilder;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.dialogs.Dialog#createContents(org.eclipse.swt.widgets.Composite)
	 */
	protected Control createContents( Composite parent )
	{
		Control c = super.createContents( parent );
		// Pack shell for dynamic creating aggregate parameters widgets.
		c.pack( );
		return c;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.dialogs.Dialog#createDialogArea(org.eclipse.swt.widgets.Composite)
	 */
	protected Control createDialogArea( Composite parent )
	{
		ChartUIUtil.bindHelp( parent, ChartHelpContextIds.DIALOG_GROUP_AND_SORT );
		getShell( ).setText( Messages.getString( "GroupSortingDialog.Label.GroupAndSorting" ) ); //$NON-NLS-1$

		Composite cmpContent = new Composite( parent, SWT.NONE );
		{
			GridLayout glContent = new GridLayout( 2, false );
			cmpContent.setLayout( glContent );
			GridData gd = new GridData( GridData.FILL_BOTH );
			cmpContent.setLayoutData( gd );
		}

		Composite cmpBasic = new Composite( cmpContent, SWT.NONE );
		{
			cmpBasic.setLayout( new GridLayout( 2, false ) );
			GridData gd = new GridData( GridData.FILL_HORIZONTAL );
			cmpBasic.setLayoutData( gd );
		}

		createSortArea( cmpBasic );

		if ( ChartUIUtil.isGroupingSupported( wizardContext ) )
		{
			createGroupArea( cmpBasic );
		}

		initSortKey( );

		populateLists( );

		return cmpContent;
	}

	/**
	 * Create composite of group area.
	 * 
	 * @param cmpBasic
	 */
	protected void createGroupArea( Composite cmpBasic )
	{
		Composite cmpGrouping = new Composite( cmpBasic, SWT.NONE );
		GridData gdCMPGrouping = new GridData( GridData.FILL_HORIZONTAL );
		gdCMPGrouping.horizontalSpan = 2;
		cmpGrouping.setLayoutData( gdCMPGrouping );
		cmpGrouping.setLayout( new FillLayout( ) );
		fGroupingComposite = createSeriesGroupingComposite( cmpGrouping );
	}

	/**
	 * Create runtime instance of <code>SeriesGroupingComposite</code>.
	 * 
	 * @param parent
	 * @since 2.3
	 */
	protected SeriesGroupingComposite createSeriesGroupingComposite( Composite parent )
	{
		SeriesGrouping grouping = getSeriesDefinitionForProcessing( ).getGrouping( );
		if ( grouping == null )
		{
			grouping = SeriesGroupingImpl.create( );
			getSeriesDefinitionForProcessing( ).setGrouping( grouping );
		}
		
		return new SeriesGroupingComposite( parent,
				SWT.NONE,
				grouping,
				fEnableAggregation,
				wizardContext,
				null );
	}
	
	/**
	 * Create composite of sort area.
	 * 
	 * @param cmpBasic
	 */
	public void createSortArea( Composite parent )
	{
		Composite cmpSortArea = new Composite( parent, SWT.NONE );
		{
			cmpSortArea.setLayout( new GridLayout( 3, false ) );
			GridData gd = new GridData( GridData.FILL_HORIZONTAL );
			gd.horizontalSpan = 2;
			cmpSortArea.setLayoutData( gd );
		}
		lblSorting = new Label( cmpSortArea, SWT.NONE );
		lblSorting.setText( Messages.getString( "BaseSeriesDataSheetImpl.Lbl.DataSorting" ) ); //$NON-NLS-1$

		cmbSorting = new Combo( cmpSortArea, SWT.DROP_DOWN | SWT.READ_ONLY );
		GridData gdCMBSorting = new GridData( GridData.FILL_HORIZONTAL );
		cmbSorting.setLayoutData( gdCMBSorting );
		cmbSorting.addListener( SWT.Selection, this );

		new Label( cmpSortArea, SWT.NONE );

		// Add sort column selection composites.
		lblSortExpr = new Label( cmpSortArea, SWT.NONE );
		lblSortExpr.setText( Messages.getString( "BaseGroupSortingDialog.Label.SortOn" ) ); //$NON-NLS-1$

		cmbSortExpr = new Combo( cmpSortArea, SWT.DROP_DOWN );
		GridData gdCMBSortExpr = new GridData( GridData.FILL_HORIZONTAL );
		cmbSortExpr.setLayoutData( gdCMBSortExpr );
		cmbSortExpr.addListener( SWT.Selection, this );
		cmbSortExpr.addFocusListener( new FocusAdapter( ) {

			public void focusLost( FocusEvent e )
			{
				String sExpr = cmbSortExpr.getText( );
				registerSortKey( sExpr );
				getSeriesDefinitionForProcessing( ).getSortKey( )
						.setDefinition( sExpr );
			}
		} );

		if ( fHasExprBuilder )
		{
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
		}

		if ( isInheritColumnsGroups( ) )
		{
			disableSorting( );
		}

	}

	protected SeriesDefinition getSeriesDefinitionForProcessing( )
	{
		return sd;
	}

	/**
	 * Populate sort direction list.
	 */
	protected void populateLists( )
	{
		// populate sorting combo
		cmbSorting.add( UNSORTED_OPTION );

		String[] nss = LiteralHelper.sortOptionSet.getDisplayNames( );
		for ( int i = 0; i < nss.length; i++ )
		{
			cmbSorting.add( nss[i] );
		}

		// Select value
		if ( !getSeriesDefinitionForProcessing( ).eIsSet( DataPackage.eINSTANCE.getSeriesDefinition_Sorting( ) ) )
		{
			cmbSorting.select( 0 );
		}
		else
		{
			// plus one for the first is unsorted option.
			cmbSorting.select( LiteralHelper.sortOptionSet.getNameIndex( getSeriesDefinitionForProcessing( ).getSorting( )
					.getName( ) ) + 1 );
		}

		diableSortKeySelectionStateBySortDirection( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.widgets.Listener#handleEvent(org.eclipse.swt.widgets.Event)
	 */
	public void handleEvent( Event event )
	{
		if ( event.widget == cmbSorting )
		{
			diableSortKeySelectionStateBySortDirection( );

			if ( cmbSorting.getText( ).equals( UNSORTED_OPTION ) )
			{
				getSeriesDefinitionForProcessing( ).eUnset( DataPackage.eINSTANCE.getSeriesDefinition_Sorting( ) );
				getSeriesDefinitionForProcessing( ).getSortKey( )
						.setDefinition( null );
			}
			else
			{
				getSeriesDefinitionForProcessing( ).setSorting( SortOption.getByName( LiteralHelper.sortOptionSet.getNameByDisplayName( cmbSorting.getText( ) ) ) );
			}
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

				registerSortKey( sExpr );
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

	private void registerSortKey( String sExpr )
	{
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
	}

	/**
	 * Set state of SortKey components.
	 * 
	 * @param enabled
	 * @since BIRT 2.3
	 */
	protected void setSortKeySelectionState( boolean enabled )
	{
		lblSortExpr.setEnabled( enabled );
		cmbSortExpr.setEnabled( enabled );
		if ( btnSortExprBuilder != null )
		{
			btnSortExprBuilder.setEnabled( enabled );
		}
	}

	/**
	 * Disable SortKey selection state by check sort direction.
	 * 
	 * @since BIRT 2.3
	 */
	protected void diableSortKeySelectionStateBySortDirection( )
	{
		if ( cmbSorting.getText( ).equals( UNSORTED_OPTION ) )
		{
			setSortKeySelectionState( false );
		}
	}

	/**
	 * Initialize SortKey object of chart model if it doesn't exist.
	 * 
	 * @since BIRT 2.3
	 */
	protected void initSortKey( )
	{
		if ( getSeriesDefinitionForProcessing( ).getSortKey( ) == null )
		{
			getSeriesDefinitionForProcessing( ).setSortKey( QueryImpl.create( (String) null ) );
		}
	}

	/**
	 * Check if Y grouping is enabled and current is using cube, only category
	 * expression is allowed as category sort key.
	 * 
	 * @return
	 * @since BIRT 2.3
	 */
	protected boolean onlyCategoryExprAsCategorySortKey( )
	{
		int stateInfo = wizardContext.getDataServiceProvider( ).getState( );
		boolean isCube = ( stateInfo & IDataServiceProvider.HAS_CUBE ) == IDataServiceProvider.HAS_CUBE
				&& ( stateInfo & IDataServiceProvider.SHARE_QUERY ) != IDataServiceProvider.SHARE_QUERY;

		if ( isYGroupingEnabled( ) && !isCube )

		{
			return true;
		}

		return false;
	}

	protected boolean isInheritColumnsGroups( )
	{
		int stateInfo = wizardContext.getDataServiceProvider( ).getState( );
		return ( stateInfo & IDataServiceProvider.HAS_DATA_SET ) == 0
				&& ( stateInfo & IDataServiceProvider.HAS_CUBE ) == 0
				&& ( stateInfo & IDataServiceProvider.INHERIT_DATA_SET ) != 0
				&& ( stateInfo & IDataServiceProvider.INHERIT_COLUMNS_GROUPS ) != 0;
	}

	protected void disableSorting( )
	{
		lblSorting.setEnabled( false );
		cmbSorting.setEnabled( false );
		lblSortExpr.setEnabled( false );
		cmbSortExpr.setEnabled( false );
		if ( btnSortExprBuilder != null )
		{
			btnSortExprBuilder.setEnabled( false );
		}
	}

	/**
	 * check if Y grouping is set.
	 * 
	 * @return
	 * @since BIRT 2.3
	 */
	protected boolean isYGroupingEnabled( )
	{
		SeriesDefinition baseSD = null;
		SeriesDefinition orthSD = null;
		Object[] orthAxisArray = null;
		Chart cm = wizardContext.getModel( );
		if ( cm instanceof ChartWithAxes )
		{
			ChartWithAxes cwa = (ChartWithAxes) cm;

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
		if ( orthSD != null && orthSD.getQuery( ) != null )
		{
			yGroupExpr = orthSD.getQuery( ).getDefinition( );
		}

		return yGroupExpr != null && !"".equals( yGroupExpr ); //$NON-NLS-1$
	}
	
	/**
	 * Get expressions of base series.
	 * 
	 * @return
	 * @since BIRT 2.3
	 */
	protected Set getBaseSeriesExpression( )
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
	 * @since BIRT 2.3
	 */
	protected Set getValueSeriesExpressions( )
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

	/**
	 * Set SortKey attribute by UI value.
	 */
	protected void setSortKeyInModel( )
	{
		String sortKey = cmbSortExpr.getText( );
		if ( "".equals( sortKey ) ) //$NON-NLS-1$
		{
			sortKey = null;
		}

		getSeriesDefinitionForProcessing( ).getSortKey( )
				.setDefinition( sortKey );
	}
}
