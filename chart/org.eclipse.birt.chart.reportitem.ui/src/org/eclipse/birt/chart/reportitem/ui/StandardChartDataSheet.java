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

package org.eclipse.birt.chart.reportitem.ui;

import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.DialChart;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.data.Query;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.model.type.BubbleSeries;
import org.eclipse.birt.chart.model.type.DifferenceSeries;
import org.eclipse.birt.chart.model.type.GanttSeries;
import org.eclipse.birt.chart.model.type.StockSeries;
import org.eclipse.birt.chart.plugin.ChartEnginePlugin;
import org.eclipse.birt.chart.reportitem.ui.dialogs.ChartColumnBindingDialog;
import org.eclipse.birt.chart.reportitem.ui.dialogs.ExtendedItemFilterDialog;
import org.eclipse.birt.chart.reportitem.ui.dialogs.ReportItemParametersDialog;
import org.eclipse.birt.chart.reportitem.ui.i18n.Messages;
import org.eclipse.birt.chart.ui.swt.ColorPalette;
import org.eclipse.birt.chart.ui.swt.CustomPreviewTable;
import org.eclipse.birt.chart.ui.swt.DataDefinitionTextManager;
import org.eclipse.birt.chart.ui.swt.DefaultChartDataSheet;
import org.eclipse.birt.chart.ui.swt.wizard.ChartAdapter;
import org.eclipse.birt.chart.ui.swt.wizard.ChartWizard;
import org.eclipse.birt.chart.ui.util.ChartHelpContextIds;
import org.eclipse.birt.chart.ui.util.ChartUIUtil;
import org.eclipse.birt.core.ui.frameworks.taskwizard.WizardBase;
import org.eclipse.birt.report.designer.internal.ui.dialogs.ExpressionFilter;
import org.eclipse.birt.report.designer.ui.actions.NewDataSetAction;
import org.eclipse.birt.report.designer.ui.dialogs.ColumnBindingDialog;
import org.eclipse.birt.report.designer.ui.dialogs.ExpressionProvider;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.metadata.IClassInfo;
import org.eclipse.emf.common.util.EList;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.ui.PlatformUI;

/**
 * Data sheet implementation for Standard Chart
 */

public final class StandardChartDataSheet extends DefaultChartDataSheet
		implements
			Listener
{

	final private ExtendedItemHandle itemHandle;
	final private ReportDataServiceProvider dataProvider;
	final private Chart chart;

	/**
	 * The field indicates if any operation in this class cause some exception
	 * or error.
	 */
	private boolean fbException = false;

	private Button btnUseReportData = null;
	private Button btnUseDataSet = null;
	private Combo cmbDataSet = null;
	private Button btnNewData = null;
	private Button btnUseReference;
	private Combo cmbReferences = null;

	private CustomPreviewTable tablePreview = null;

	private Button btnFilters = null;
	private Button btnParameters = null;
	private Button btnBinding = null;

	public StandardChartDataSheet( Chart chart, ExtendedItemHandle itemHandle,
			ReportDataServiceProvider dataProvider )
	{
		this.chart = chart;
		this.itemHandle = itemHandle;
		this.dataProvider = dataProvider;
	}

	public Composite createActionButtons( Composite parent )
	{
		Composite composite = ChartUIUtil.createCompositeWrapper( parent );
		{
			composite.setLayoutData( new GridData( GridData.FILL_HORIZONTAL
					| GridData.VERTICAL_ALIGN_END ) );
		}

		btnFilters = new Button( composite, SWT.NONE );
		{
			btnFilters.setAlignment( SWT.CENTER );
			GridData gridData = new GridData( GridData.FILL_HORIZONTAL );
			btnFilters.setLayoutData( gridData );
			btnFilters.setText( Messages.getString( "StandardChartDataSheet.Label.Filters" ) ); //$NON-NLS-1$
			btnFilters.addListener( SWT.Selection, this );
		}

		btnParameters = new Button( composite, SWT.NONE );
		{
			btnParameters.setAlignment( SWT.CENTER );
			GridData gridData = new GridData( GridData.FILL_HORIZONTAL );
			btnParameters.setLayoutData( gridData );
			btnParameters.setText( Messages.getString( "StandardChartDataSheet.Label.Parameters" ) ); //$NON-NLS-1$
			btnParameters.addListener( SWT.Selection, this );
		}

		btnBinding = new Button( composite, SWT.NONE );
		{
			btnBinding.setAlignment( SWT.CENTER );
			GridData gridData = new GridData( GridData.FILL_HORIZONTAL );
			btnBinding.setLayoutData( gridData );
			btnBinding.setText( Messages.getString( "StandardChartDataSheet.Label.DataBinding" ) ); //$NON-NLS-1$
			btnBinding.addListener( SWT.Selection, this );
		}

		setEnabledForButtons( );
		return composite;
	}

	private void setEnabledForButtons( )
	{
		btnNewData.setEnabled( btnUseDataSet.getSelection( )
				&& getDataServiceProvider( ).isInvokingSupported( ) );
		btnFilters.setEnabled( hasDataSet( )
				&& getDataServiceProvider( ).isInvokingSupported( ) );
		// Bugzilla#177704 Chart inheriting data from container doesn't
		// support parameters due to limitation in DtE
		btnParameters.setEnabled( getDataServiceProvider( ).getBoundDataSet( ) != null
				&& getDataServiceProvider( ).isInvokingSupported( ) );
		btnBinding.setEnabled( hasDataSet( )
				&& getDataServiceProvider( ).isInvokingSupported( ) );
	}

	private boolean hasDataSet( )
	{
		return getDataServiceProvider( ).getReportDataSet( ) != null
				|| getDataServiceProvider( ).getBoundDataSet( ) != null;
	}

	void fireEvent( Widget widget, int eventType )
	{
		Event event = new Event( );
		event.data = this;
		event.widget = widget;
		event.type = eventType;
		notifyListeners( eventType, event );
	}

	public Composite createDataDragSource( Composite parent )
	{
		Composite composite = ChartUIUtil.createCompositeWrapper( parent );
		{
			composite.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
		}
		Label label = new Label( composite, SWT.NONE );
		{
			label.setText( Messages.getString( "StandardChartDataSheet.Label.DataPreview" ) ); //$NON-NLS-1$
			label.setFont( JFaceResources.getBannerFont( ) );
		}
		Label description = new Label( composite, SWT.WRAP );
		{
			GridData gd = new GridData( GridData.FILL_HORIZONTAL );
			description.setLayoutData( gd );
			description.setText( Messages.getString( "StandardChartDataSheet.Label.ToBindADataColumn" ) ); //$NON-NLS-1$
		}

		tablePreview = new CustomPreviewTable( composite, SWT.SINGLE
				| SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION );
		{
			GridData gridData = new GridData( GridData.FILL_HORIZONTAL );
			gridData.widthHint = 400;
			gridData.heightHint = 120;
			tablePreview.setLayoutData( gridData );
			tablePreview.setHeaderAlignment( SWT.LEFT );
			tablePreview.addListener( CustomPreviewTable.MOUSE_RIGHT_CLICK_TYPE,
					this );
		}
		return composite;
	}

	public Composite createDataSelector( Composite parent )
	{
		Composite cmpDataSet = ChartUIUtil.createCompositeWrapper( parent );
		{
			cmpDataSet.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
		}

		Label label = new Label( cmpDataSet, SWT.NONE );
		{
			label.setText( Messages.getString( "StandardChartDataSheet.Label.SelectDataSet" ) ); //$NON-NLS-1$
			label.setFont( JFaceResources.getBannerFont( ) );
		}

		Composite cmpDetail = new Composite( cmpDataSet, SWT.NONE );
		{
			GridLayout gridLayout = new GridLayout( 3, false );
			gridLayout.marginWidth = 10;
			gridLayout.marginHeight = 0;
			cmpDetail.setLayout( gridLayout );
			cmpDetail.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
		}

		Composite compRadios = ChartUIUtil.createCompositeWrapper( cmpDetail );
		{
			GridData gd = new GridData( );
			gd.verticalSpan = 3;
			compRadios.setLayoutData( gd );
		}

		btnUseReportData = new Button( compRadios, SWT.RADIO );
		btnUseReportData.setText( Messages.getString( "StandardChartDataSheet.Label.UseReportData" ) ); //$NON-NLS-1$
		btnUseReportData.addListener( SWT.Selection, this );

		btnUseDataSet = new Button( compRadios, SWT.RADIO );
		btnUseDataSet.setText( Messages.getString( "StandardChartDataSheet.Label.UseDataSet" ) ); //$NON-NLS-1$
		btnUseDataSet.addListener( SWT.Selection, this );

		btnUseReference = new Button( compRadios, SWT.RADIO );
		btnUseReference.setText( Messages.getString( "StandardChartDataSheet.Label.UseReportItem" ) ); //$NON-NLS-1$
		btnUseReference.addListener( SWT.Selection, this );

		new Label( cmpDetail, SWT.NONE );
		new Label( cmpDetail, SWT.NONE );

		cmbDataSet = new Combo( cmpDetail, SWT.DROP_DOWN | SWT.READ_ONLY );
		cmbDataSet.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
		cmbDataSet.addListener( SWT.Selection, this );

		btnNewData = new Button( cmpDetail, SWT.NONE );
		{
			btnNewData.setText( Messages.getString( "StandardChartDataSheet.Label.CreateNew" ) ); //$NON-NLS-1$
			btnNewData.setToolTipText( Messages.getString( "StandardChartDataSheet.Tooltip.CreateNewDataset" ) ); //$NON-NLS-1$
			btnNewData.addListener( SWT.Selection, this );
		}

		cmbReferences = new Combo( cmpDetail, SWT.DROP_DOWN | SWT.READ_ONLY );
		cmbReferences.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
		cmbReferences.addListener( SWT.Selection, this );

		initDataSelector( );
		return cmpDataSet;
	}

	int invokeNewDataSet( )
	{
		IAction action = new NewDataSetAction( );
		PlatformUI.getWorkbench( ).getHelpSystem( ).setHelp( action,
				ChartHelpContextIds.DIALOG_NEW_DATA_SET );
		action.run( );
		// Due to the limitation of the action execution, always return ok
		return Window.OK;
	}

	int invokeEditFilter( )
	{
		ExtendedItemFilterDialog page = new ExtendedItemFilterDialog( getItemHandle( ) );
		return page.open( );
	}

	int invokeEditParameter( )
	{
		ReportItemParametersDialog page = new ReportItemParametersDialog( getItemHandle( ) );
		return page.open( );
	}

	int invokeDataBinding( )
	{
		Shell shell = new Shell( Display.getDefault( ), SWT.DIALOG_TRIM
				| SWT.RESIZE | SWT.APPLICATION_MODAL );
		// #194163: Do not register CS help in chart since it's registered in
		// super column binding dialog.
		// ChartUIUtil.bindHelp( shell,
		// ChartHelpContextIds.DIALOG_DATA_SET_COLUMN_BINDING );
		ColumnBindingDialog page = new ChartColumnBindingDialog( shell );
		page.setInput( getItemHandle( ) );

		ExpressionProvider ep = new ExpressionProvider( getItemHandle( ) );
		ep.addFilter( new ExpressionFilter( ) {

			public boolean select( Object parentElement, Object element )
			{
				// Remove unsupported expression. See bugzilla#132768
				return !( parentElement.equals( ExpressionProvider.BIRT_OBJECTS )
						&& element instanceof IClassInfo && ( (IClassInfo) element ).getName( )
						.equals( "Total" ) ); //$NON-NLS-1$
			}
		} );
		page.setExpressionProvider( ep );
		return page.open( );
	}

	private void initDataSelector( )
	{
		// create Combo items
		cmbDataSet.setItems( getDataServiceProvider( ).getAllDataSets( ) );
		cmbReferences.setItems( getDataServiceProvider( ).getAllReportItemReferences( ) );

		// select data set
		String currentDataSet = getDataServiceProvider( ).getBoundDataSet( );
		if ( currentDataSet != null )
		{
			btnUseDataSet.setSelection( true );
			cmbDataSet.setText( currentDataSet );
			if ( currentDataSet != null )
			{
				switchDataTable( );
			}
		}
		else
		{
			btnUseReportData.setSelection( true );
			cmbDataSet.select( 0 );
			cmbDataSet.setEnabled( false );
			// Initializes column bindings from container
			getDataServiceProvider( ).setDataSet( null );

			String reportDataSet = getDataServiceProvider( ).getReportDataSet( );
			if ( reportDataSet != null )
			{
				switchDataTable( );
			}
		}

		// select reference item
		selectItemRef( );
		if ( cmbReferences.getSelectionIndex( ) > 0 )
		{
			cmbDataSet.setEnabled( false );
			btnUseReference.setSelection( true );
			btnUseReportData.setSelection( false );
			btnUseDataSet.setSelection( false );
		}
		else
		{
			cmbReferences.setEnabled( false );
		}
	}

	public void handleEvent( Event event )
	{
		fbException = false;

		// Right click to display the menu. Menu display by clicking
		// application key is triggered by os, so do nothing.
		if ( event.type == CustomPreviewTable.MOUSE_RIGHT_CLICK_TYPE )
		{
			if ( getDataServiceProvider( ).getBoundDataSet( ) != null
					|| getDataServiceProvider( ).getReportDataSet( ) != null )
			{
				if ( event.widget instanceof Button )
				{
					Button header = (Button) event.widget;

					// Bind context menu to each header button
					if ( header.getMenu( ) == null )
					{
						header.setMenu( createMenu( ) );
					}

					header.getMenu( ).setVisible( true );
				}
			}

		}
		else if ( event.type == SWT.Selection )
		{
			if ( event.widget instanceof MenuItem )
			{
				MenuItem item = (MenuItem) event.widget;
				IAction action = (IAction) item.getData( );
				action.setChecked( !action.isChecked( ) );
				action.run( );
			}
			else if ( event.widget == btnFilters )
			{
				if ( invokeEditFilter( ) == Window.OK )
				{
					refreshTablePreview( );
					// Update preview via event
					fireEvent( btnFilters, EVENT_UPDATE );
				}
			}
			else if ( event.widget == btnParameters )
			{
				if ( invokeEditParameter( ) == Window.OK )
				{
					refreshTablePreview( );
					// Update preview via event
					fireEvent( btnParameters, EVENT_UPDATE );
				}
			}
			else if ( event.widget == btnBinding )
			{
				if ( invokeDataBinding( ) == Window.OK )
				{
					refreshTablePreview( );
					// Update preview via event
					fireEvent( btnBinding, EVENT_UPDATE );
				}
			}

			try
			{
				if ( event.widget == btnUseReportData )
				{
					ColorPalette.getInstance( ).restore( );

					// Skip when selection is false
					if ( !btnUseReportData.getSelection( ) )
					{
						return;
					}
					getDataServiceProvider( ).setReportItemReference( null );
					getDataServiceProvider( ).setDataSet( null );
					switchDataSet( null );

					cmbDataSet.select( 0 );
					cmbDataSet.setEnabled( false );
					cmbReferences.select( 0 );
					cmbReferences.setEnabled( false );
					setEnabledForButtons( );
				}
				else if ( event.widget == btnUseDataSet )
				{
					// Skip when selection is false
					if ( !btnUseDataSet.getSelection( ) )
					{
						return;
					}

					getDataServiceProvider( ).setReportItemReference( null );
					selectDataSet( );
					cmbDataSet.setEnabled( true );
					cmbReferences.setEnabled( false );

					setEnabledForButtons( );
				}
				else if ( event.widget == cmbDataSet )
				{
					ColorPalette.getInstance( ).restore( );
					if ( cmbDataSet.getSelectionIndex( ) > 0 )
					{
						if ( getDataServiceProvider( ).getBoundDataSet( ) != null
								&& getDataServiceProvider( ).getBoundDataSet( )
										.equals( cmbDataSet.getText( ) ) )
						{
							return;
						}
						getDataServiceProvider( ).setDataSet( cmbDataSet.getText( ) );
						switchDataSet( cmbDataSet.getText( ) );

						setEnabledForButtons( );
					}
					else
					{
						// Inherit data from container
						btnUseReportData.setSelection( true );
						btnUseDataSet.setSelection( false );
						btnUseReportData.notifyListeners( SWT.Selection,
								new Event( ) );
					}
				}
				else if ( event.widget == btnUseReference )
				{
					// Skip when selection is false
					if ( !btnUseReference.getSelection( ) )
					{
						return;
					}
					cmbDataSet.setEnabled( false );
					cmbReferences.setEnabled( true );
					selectItemRef( );
					setEnabledForButtons( );
				}
				else if ( event.widget == cmbReferences )
				{
					if ( cmbReferences.getSelectionIndex( ) == 0 )
					{
						if ( getDataServiceProvider( ).getReportItemReference( ) == null )
						{
							return;
						}
						getDataServiceProvider( ).setReportItemReference( null );

						// Auto select the data set
						selectDataSet( );
						cmbReferences.setEnabled( false );
						cmbDataSet.setEnabled( true );
						btnUseReference.setSelection( false );
						btnUseDataSet.setSelection( true );
					}
					else
					{
						if ( cmbReferences.getText( )
								.equals( getDataServiceProvider( ).getReportItemReference( ) ) )
						{
							return;
						}
						getDataServiceProvider( ).setReportItemReference( cmbReferences.getText( ) );
						selectDataSet( );
					}
					switchDataSet( cmbDataSet.getText( ) );
					setEnabledForButtons( );
				}
				else if ( event.widget == btnNewData )
				{
					// Bring up the dialog to create a dataset
					int result = invokeNewDataSet( );
					if ( result == Window.CANCEL )
					{
						return;
					}

					String currentDataSet = cmbDataSet.getText( );
					cmbDataSet.removeAll( );
					cmbDataSet.setItems( getDataServiceProvider( ).getAllDataSets( ) );
					cmbDataSet.setText( currentDataSet );
				}
			}
			catch ( ChartException e1 )
			{
				fbException = true;
				ChartWizard.showException( e1.getLocalizedMessage( ) );
			}
		}
		if ( !fbException )
		{
			WizardBase.removeException( );
		}
	}

	private void selectDataSet( )
	{
		String currentDS = getDataServiceProvider( ).getBoundDataSet( );
		if ( currentDS == null )
		{
			cmbDataSet.select( 0 );
		}
		else
		{
			cmbDataSet.setText( currentDS );
		}
	}

	private void selectItemRef( )
	{
		String currentRef = getDataServiceProvider( ).getReportItemReference( );
		if ( currentRef == null )
		{
			cmbReferences.select( 0 );
		}
		else
		{
			cmbReferences.setText( currentRef );
		}
	}

	private void refreshTablePreview( )
	{
		tablePreview.clearContents( );
		if ( cmbDataSet.getText( ) != null )
		{
			switchDataTable( );
		}
		tablePreview.layout( );
	}

	private void switchDataSet( String datasetName ) throws ChartException
	{
		try
		{
			// Clear old dataset and preview data
			tablePreview.clearContents( );

			// Try to get report data set
			if ( datasetName == null )
			{
				datasetName = getDataServiceProvider( ).getReportDataSet( );
			}

			if ( datasetName != null )
			{
				switchDataTable( );
			}
			else
			{
				tablePreview.createDummyTable( );
			}
			tablePreview.layout( );
		}
		catch ( Throwable t )
		{
			throw new ChartException( ChartEnginePlugin.ID,
					ChartException.DATA_BINDING,
					t );
		}

		DataDefinitionTextManager.getInstance( ).refreshAll( );
		// Update preview via event
		fireEvent( tablePreview, EVENT_UPDATE );
	}

	private void switchDataTable( )
	{
		// 1. Create a runnable.
		Runnable runnable = new Runnable( ) {

			/*
			 * (non-Javadoc)
			 * 
			 * @see org.eclipse.jface.operation.IRunnableWithProgress#run(org.eclipse.core.runtime.IProgressMonitor)
			 */
			public void run( )
			{
				try
				{
					// Get header and data in other thread.
					final String[] header = getDataServiceProvider( ).getPreviewHeader( );
					final List dataList = getDataServiceProvider( ).getPreviewData( );

					// Execute UI operation in UI thread.
					Display.getDefault( ).syncExec( new Runnable( ) {

						public void run( )
						{
							if ( tablePreview.isDisposed( ) )
							{
								return;
							}

							if ( header == null )
							{
								tablePreview.setEnabled( false );
								tablePreview.createDummyTable( );
							}
							else
							{
								tablePreview.setEnabled( true );
								tablePreview.setColumns( header );

								refreshTableColor( );

								// Add data value
								for ( Iterator iterator = dataList.iterator( ); iterator.hasNext( ); )
								{
									String[] dataRow = (String[]) iterator.next( );
									for ( int i = 0; i < dataRow.length; i++ )
									{
										tablePreview.addEntry( dataRow[i], i );
									}
								}
							}
							tablePreview.layout( );
						}
					} );
				}
				catch ( Exception e )
				{
					// Catch any exception.
					final String msg = e.getMessage( );
					Display.getDefault( ).syncExec( new Runnable( ) {

						/*
						 * (non-Javadoc)
						 * 
						 * @see java.lang.Runnable#run()
						 */
						public void run( )
						{
							fbException = true;
							WizardBase.showException( msg );
						}
					} );
				}
			}
		};

		// 2. Run it.
		new Thread( runnable ).start( );
	}

	private void refreshTableColor( )
	{
		// Reset column color
		for ( int i = 0; i < tablePreview.getColumnNumber( ); i++ )
		{
			tablePreview.setColumnColor( i,
					ColorPalette.getInstance( )
							.getColor( ChartUIUtil.getExpressionString( tablePreview.getColumnHeading( i ) ) ) );
		}
	}

	private void manageColorAndQuery( Query query )
	{
		// If it's not used any more, remove color binding
		if ( DataDefinitionTextManager.getInstance( )
				.getNumberOfSameDataDefinition( query.getDefinition( ) ) == 0 )
		{
			ColorPalette.getInstance( ).retrieveColor( query.getDefinition( ) );
		}
		query.setDefinition( ChartUIUtil.getExpressionString( tablePreview.getCurrentColumnHeading( ) ) );
		DataDefinitionTextManager.getInstance( ).updateText( query );
		// Reset table column color
		refreshTableColor( );
		// Refresh all data definition text
		DataDefinitionTextManager.getInstance( ).refreshAll( );
	}

	class CategoryXAxisAction extends Action
	{

		CategoryXAxisAction( )
		{
			super( getBaseSeriesTitle( getChartModel( ) ) );
		}

		public void run( )
		{
			Query query = ( (Query) ( (SeriesDefinition) ChartUIUtil.getBaseSeriesDefinitions( getChartModel( ) )
					.get( 0 ) ).getDesignTimeSeries( )
					.getDataDefinition( )
					.get( 0 ) );
			manageColorAndQuery( query );
		}
	}

	class GroupYSeriesAction extends Action
	{

		Query query;

		GroupYSeriesAction( Query query )
		{
			super( getGroupSeriesTitle( getChartModel( ) ) );
			this.query = query;
		}

		public void run( )
		{
			// Use the first group, and copy to the all groups
			ChartAdapter.beginIgnoreNotifications( );
			ChartUIUtil.setAllGroupingQueryExceptFirst( getChartModel( ),
					ChartUIUtil.getExpressionString( tablePreview.getCurrentColumnHeading( ) ) );
			ChartAdapter.endIgnoreNotifications( );

			manageColorAndQuery( query );
		}
	}

	class ValueYSeriesAction extends Action
	{

		Query query;

		ValueYSeriesAction( Query query )
		{
			super( getOrthogonalSeriesTitle( getChartModel( ) ) );
			this.query = query;
		}

		public void run( )
		{
			manageColorAndQuery( query );
		}
	}

	class HeaderShowAction extends Action
	{

		HeaderShowAction( )
		{
			super( tablePreview.getCurrentColumnHeading( ) );
			setEnabled( false );
		}
	}

	Chart getChartModel( )
	{
		return this.chart;
	}

	ExtendedItemHandle getItemHandle( )
	{
		return this.itemHandle;
	}

	ReportDataServiceProvider getDataServiceProvider( )
	{
		return this.dataProvider;
	}

	private Menu createMenu( )
	{
		MenuManager menuManager = new MenuManager( );
		menuManager.setRemoveAllWhenShown( true );
		menuManager.addMenuListener( new IMenuListener( ) {

			public void menuAboutToShow( IMenuManager manager )
			{
				addMenu( manager, new HeaderShowAction( ) );
				addMenu( manager, getBaseSeriesMenu( getChartModel( ) ) );
				addMenu( manager, getOrthogonalSeriesMenu( getChartModel( ) ) );
				addMenu( manager, getGroupSeriesMenu( getChartModel( ) ) );
			}

			private void addMenu( IMenuManager manager, Object item )
			{
				if ( item instanceof IAction )
				{
					manager.add( (IAction) item );
				}
				else if ( item instanceof IContributionItem )
				{
					manager.add( (IContributionItem) item );
				}
			}
		} );

		return menuManager.createContextMenu( tablePreview );
	}

	private Object getBaseSeriesMenu( Chart chart )
	{
		EList sds = ChartUIUtil.getBaseSeriesDefinitions( chart );
		if ( sds.size( ) == 1 )
		{
			return new CategoryXAxisAction( );
		}
		return null;
	}

	private Object getGroupSeriesMenu( Chart chart )
	{
		IMenuManager topManager = new MenuManager( getGroupSeriesTitle( getChartModel( ) ) );
		int axisNum = ChartUIUtil.getOrthogonalAxisNumber( chart );
		for ( int axisIndex = 0; axisIndex < axisNum; axisIndex++ )
		{
			List sds = ChartUIUtil.getOrthogonalSeriesDefinitions( chart,
					axisIndex );
			for ( int i = 0; i < sds.size( ); i++ )
			{
				SeriesDefinition sd = (SeriesDefinition) sds.get( i );
				IAction action = new GroupYSeriesAction( sd.getQuery( ) );
				// ONLY USE FIRST GROUPING SERIES FOR CHART ENGINE SUPPORT
				// if ( axisNum == 1 && sds.size( ) == 1 )
				{
					// Simply cascade menu
					return action;
				}
				// action.setText( getSecondMenuText( axisIndex,
				// i,
				// sd.getDesignTimeSeries( ) ) );
				// topManager.add( action );
			}
		}
		return topManager;
	}

	private Object getOrthogonalSeriesMenu( Chart chart )
	{
		IMenuManager topManager = new MenuManager( getOrthogonalSeriesTitle( getChartModel( ) ) );
		int axisNum = ChartUIUtil.getOrthogonalAxisNumber( chart );
		for ( int axisIndex = 0; axisIndex < axisNum; axisIndex++ )
		{
			List sds = ChartUIUtil.getOrthogonalSeriesDefinitions( chart,
					axisIndex );
			for ( int i = 0; i < sds.size( ); i++ )
			{
				Series series = ( (SeriesDefinition) sds.get( i ) ).getDesignTimeSeries( );
				EList dataDefns = series.getDataDefinition( );

				if ( series instanceof StockSeries )
				{
					IMenuManager secondManager = new MenuManager( getSecondMenuText( axisIndex,
							i,
							series ) );
					topManager.add( secondManager );
					for ( int j = 0; j < dataDefns.size( ); j++ )
					{
						IAction action = new ValueYSeriesAction( (Query) dataDefns.get( j ) );
						action.setText( ChartUIUtil.getStockTitle( j )
								+ Messages.getString( "StandardChartDataSheet.Label.Component" ) ); //$NON-NLS-1$
						secondManager.add( action );
					}
				}
				else if ( series instanceof BubbleSeries )
				{
					IMenuManager secondManager = new MenuManager( getSecondMenuText( axisIndex,
							i,
							series ) );
					topManager.add( secondManager );
					for ( int j = 0; j < dataDefns.size( ); j++ )
					{
						IAction action = new ValueYSeriesAction( (Query) dataDefns.get( j ) );
						action.setText( ChartUIUtil.getBubbleTitle( j )
								+ Messages.getString( "StandardChartDataSheet.Label.Component" ) ); //$NON-NLS-1$
						secondManager.add( action );
					}
				}
				else if ( series instanceof DifferenceSeries )
				{
					IMenuManager secondManager = new MenuManager( getSecondMenuText( axisIndex,
							i,
							series ) );
					topManager.add( secondManager );
					for ( int j = 0; j < dataDefns.size( ); j++ )
					{
						IAction action = new ValueYSeriesAction( (Query) dataDefns.get( j ) );
						action.setText( ChartUIUtil.getDifferenceTitle( j )
								+ Messages.getString( "StandardChartDataSheet.Label.Component" ) ); //$NON-NLS-1$
						secondManager.add( action );
					}
				}
				else if ( series instanceof GanttSeries )
				{
					IMenuManager secondManager = new MenuManager( getSecondMenuText( axisIndex,
							i,
							series ) );
					topManager.add( secondManager );
					for ( int j = 0; j < dataDefns.size( ); j++ )
					{
						IAction action = new ValueYSeriesAction( (Query) dataDefns.get( j ) );
						action.setText( ChartUIUtil.getGanttTitle( j )
								+ Messages.getString( "StandardChartDataSheet.Label.Component" ) ); //$NON-NLS-1$
						secondManager.add( action );
					}
				}
				else
				{
					IAction action = new ValueYSeriesAction( (Query) dataDefns.get( 0 ) );
					if ( axisNum == 1 && sds.size( ) == 1 )
					{
						// Simplify cascade menu
						return action;
					}
					action.setText( getSecondMenuText( axisIndex, i, series ) );
					topManager.add( action );
				}
			}
		}
		return topManager;
	}

	private String getSecondMenuText( int axisIndex, int seriesIndex,
			Series series )
	{
		StringBuffer sb = new StringBuffer( );
		if ( ChartUIUtil.getOrthogonalAxisNumber( getChartModel( ) ) > 2 )
		{
			sb.append( Messages.getString( "DataDefinitionSelector.Label.Axis" ) ); //$NON-NLS-1$
			sb.append( axisIndex + 1 );
			sb.append( " - " ); //$NON-NLS-1$
		}
		else
		{
			if ( axisIndex > 0 )
			{
				sb.append( Messages.getString( "StandardChartDataSheet.Label.Overlay" ) ); //$NON-NLS-1$ 
			}
		}
		sb.append( Messages.getString( "StandardChartDataSheet.Label.Series" ) //$NON-NLS-1$
				+ ( seriesIndex + 1 ) + " (" + series.getDisplayName( ) + ")" ); //$NON-NLS-1$ //$NON-NLS-2$
		return sb.toString( );
	}

	private String getBaseSeriesTitle( Chart chart )
	{
		if ( chart instanceof ChartWithAxes )
		{
			return Messages.getString( "StandardChartDataSheet.Label.UseAsCategoryXAxis" ); //$NON-NLS-1$
		}
		return Messages.getString( "StandardChartDataSheet.Label.UseAsCategorySeries" ); //$NON-NLS-1$
	}

	private String getOrthogonalSeriesTitle( Chart chart )
	{
		if ( chart instanceof ChartWithAxes )
		{
			return Messages.getString( "StandardChartDataSheet.Label.PlotAsValueYSeries" ); //$NON-NLS-1$
		}
		else if ( chart instanceof DialChart )
		{
			return Messages.getString( "StandardChartDataSheet.Label.PlotAsGaugeValue" ); //$NON-NLS-1$
		}
		return Messages.getString( "StandardChartDataSheet.Label.PlotAsValueSeries" ); //$NON-NLS-1$
	}

	private String getGroupSeriesTitle( Chart chart )
	{
		if ( chart instanceof ChartWithAxes )
		{
			return Messages.getString( "StandardChartDataSheet.Label.UseToGroupYSeries" ); //$NON-NLS-1$
		}
		return Messages.getString( "StandardChartDataSheet.Label.UseToGroupValueSeries" ); //$NON-NLS-1$
	}

	public void dispose( )
	{
		super.dispose( );
		getDataServiceProvider( ).dispose( );
	}
}
