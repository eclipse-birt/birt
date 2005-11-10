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

package org.eclipse.birt.chart.ui.swt.wizard;

import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.chart.datafeed.IDataSetProcessor;
import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.log.impl.DefaultLoggerImpl;
import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.ChartWithoutAxes;
import org.eclipse.birt.chart.model.DialChart;
import org.eclipse.birt.chart.model.component.Axis;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.data.BaseSampleData;
import org.eclipse.birt.chart.model.data.DataFactory;
import org.eclipse.birt.chart.model.data.OrthogonalSampleData;
import org.eclipse.birt.chart.model.data.Query;
import org.eclipse.birt.chart.model.data.SampleData;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.model.type.StockSeries;
import org.eclipse.birt.chart.plugin.ChartEnginePlugin;
import org.eclipse.birt.chart.ui.extension.i18n.Messages;
import org.eclipse.birt.chart.ui.swt.interfaces.IDataServiceProvider;
import org.eclipse.birt.chart.ui.swt.interfaces.ISelectDataCustomizeUI;
import org.eclipse.birt.chart.ui.swt.interfaces.ITaskChangeListener;
import org.eclipse.birt.chart.ui.swt.wizard.data.SelectDataDynamicArea;
import org.eclipse.birt.chart.ui.swt.wizard.internal.ChartPreviewPainter;
import org.eclipse.birt.chart.ui.swt.wizard.internal.ColorPalette;
import org.eclipse.birt.chart.ui.swt.wizard.internal.CustomPreviewTable;
import org.eclipse.birt.chart.ui.swt.wizard.internal.DataDefinitionTextManager;
import org.eclipse.birt.chart.ui.util.ChartUIUtil;
import org.eclipse.birt.chart.util.PluginSettings;
import org.eclipse.birt.core.ui.frameworks.taskwizard.SimpleTask;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;

/**
 * 
 */

public class TaskSelectData extends SimpleTask
		implements
			SelectionListener,
			DisposeListener,
			ITaskChangeListener,
			Listener
{

	private final static int CENTER_WIDTH_HINT = 500;
	private transient ChartPreviewPainter previewPainter = null;

	private transient Composite cmpTask = null;

	private transient Color whiteColor = null;

	private transient Composite cmpPreview = null;
	private transient Canvas previewCanvas = null;

	private transient Button btnUseReportData = null;
	private transient Button btnUseDataSet = null;
	private transient Combo cmbDataSet = null;
	private transient Button btnNewData = null;

	private transient CustomPreviewTable tablePreview = null;
	// private transient Button btnFilters = null;
	private transient Button btnParameters = null;

	// private transient Action actionInsertAggregation = new
	// InsertAggregationAction( );

	private transient SelectDataDynamicArea dynamicArea;
	private boolean isInited = false;
	private String reportDataSet;

	private SampleData oldSample = null;

	public TaskSelectData( )
	{
		super( Messages.getString( "TaskSelectData.TaskExp" ) ); //$NON-NLS-1$
	}

	public Composite getUI( Composite parent )
	{
		if ( cmpTask == null || cmpTask.isDisposed( ) )
		{
			cmpTask = new Composite( parent, SWT.NONE );
			GridLayout gridLayout = new GridLayout( 3, false );
			gridLayout.marginWidth = 10;
			gridLayout.marginHeight = 0;
			cmpTask.setLayout( gridLayout );
			cmpTask.setLayoutData( new GridData( GridData.GRAB_HORIZONTAL
					| GridData.GRAB_VERTICAL ) );
			cmpTask.addDisposeListener( this );
			dynamicArea = new SelectDataDynamicArea( this );
			placeComponents( );
			initWithoutChart( );
		}
		if ( !isInited )
		{
			if ( getWizardContext( ) != null
					&& getWizardContext( ).getDataServiceProvider( ) != null )
			{
				isInited = true;
				init( );
			}
		}
		createPreviewPainter( );
		customizeUI( );
		// Refresh all data definitino text
		DataDefinitionTextManager.getInstance( ).refreshAll( );
		return cmpTask;
	}

	protected void customizeUI( )
	{
		refreshLeftArea( );
		refreshRightArea( );
		refreshBottomArea( );
		cmpTask.layout( );
	}

	private void refreshLeftArea( )
	{
		getCustomizeUI( ).refreshLeftBindingArea( );
		getCustomizeUI( ).selectLeftBindingArea( true, null );
	}

	private void refreshRightArea( )
	{
		getCustomizeUI( ).refreshRightBindingArea( );
		getCustomizeUI( ).selectRightBindingArea( true, null );
	}

	private void refreshBottomArea( )
	{
		getCustomizeUI( ).refreshBottomBindingArea( );
		getCustomizeUI( ).selectBottomBindingArea( true, null );
	}

	private void placeComponents( )
	{
		ChartAdapter.ignoreNotifications( true );
		try
		{
			createHeadArea( );// place two rows

			new Label( cmpTask, SWT.NONE );
			createDataSetArea( cmpTask );
			new Label( cmpTask, SWT.NONE );

			new Label( cmpTask, SWT.NONE );
			createDataPreviewTableArea( cmpTask );
			createDataPreviewButtonArea( cmpTask );

			new Label( cmpTask, SWT.NONE );
			Label description = new Label( cmpTask, SWT.WRAP );
			{
				GridData gd = new GridData( );
				gd.widthHint = CENTER_WIDTH_HINT;
				description.setLayoutData( gd );
				description.setText( Messages.getString( "TaskSelectData.Label.ToBindADataColumn" ) ); //$NON-NLS-1$
			}
			new Label( cmpTask, SWT.NONE );
		}
		finally
		{
			// THIS IS IN A FINALLY BLOCK TO ENSURE THAT NOTIFICATIONS ARE
			// ENABLED EVEN IF ERRORS OCCUR DURING UI INITIALIZATION
			ChartAdapter.ignoreNotifications( false );
		}
	}

	private void createHeadArea( )
	{
		{
			Composite cmpLeftContainer = createCompositeWrapper( cmpTask );
			GridData gridData = new GridData( );
			gridData.verticalSpan = 2;
			gridData.verticalAlignment = SWT.CENTER;
			gridData.horizontalAlignment = SWT.END;
			cmpLeftContainer.setLayoutData( gridData );
			getCustomizeUI( ).createLeftBindingArea( cmpLeftContainer );
		}
		createPreviewArea( );
		{
			Composite cmpRightContainer = createCompositeWrapper( cmpTask );
			GridData gridData = new GridData( );
			gridData.verticalSpan = 2;
			gridData.verticalAlignment = SWT.CENTER;
			gridData.horizontalAlignment = SWT.BEGINNING;
			cmpRightContainer.setLayoutData( gridData );
			getCustomizeUI( ).createRightBindingArea( cmpRightContainer );
		}
		{
			Composite cmpBottomContainer = createCompositeWrapper( cmpTask );
			GridData gridData = new GridData( );
			gridData.verticalAlignment = SWT.BEGINNING;
			gridData.horizontalAlignment = SWT.CENTER;
			cmpBottomContainer.setLayoutData( gridData );
			getCustomizeUI( ).createBottomBindingArea( cmpBottomContainer );
		}
	}

	private Composite createCompositeWrapper( Composite parent )
	{
		Composite cmp = new Composite( parent, SWT.NONE );
		GridLayout gridLayout = new GridLayout( );
		gridLayout.marginWidth = 0;
		gridLayout.marginHeight = 0;
		cmp.setLayout( gridLayout );
		return cmp;
	}

	private void createPreviewArea( )
	{
		cmpPreview = createCompositeWrapper( cmpTask );
		{
			GridData gridData = new GridData( GridData.FILL_BOTH );
			gridData.widthHint = CENTER_WIDTH_HINT;
			gridData.heightHint = 300;
			cmpPreview.setLayoutData( gridData );
		}

		Label label = new Label( cmpPreview, SWT.NONE );
		{
			label.setFont( JFaceResources.getBannerFont( ) );
			label.setText( Messages.getString( "TaskSelectData.Label.ChartPreview" ) ); //$NON-NLS-1$
		}

		previewCanvas = new Canvas( cmpPreview, SWT.NONE );
		{
			previewCanvas.setLayoutData( new GridData( GridData.FILL_BOTH ) );
			whiteColor = new Color( Display.getDefault( ), 255, 255, 255 );
			previewCanvas.setBackground( whiteColor );
		}
	}

	private void createDataSetArea( Composite parent )
	{
		Composite cmpDataSet = new Composite( parent, SWT.NONE );
		{
			GridLayout gridLayout = new GridLayout( 4, false );
			gridLayout.marginWidth = 0;
			gridLayout.marginHeight = 0;
			cmpDataSet.setLayout( gridLayout );
			cmpDataSet.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
		}

		Label label = new Label( cmpDataSet, SWT.NONE );
		{
			label.setText( Messages.getString( "TaskSelectData.Label.SelectDataSet" ) ); //$NON-NLS-1$
			label.setFont( JFaceResources.getBannerFont( ) );
			label.setLayoutData( new GridData( GridData.VERTICAL_ALIGN_BEGINNING ) );
		}

		Composite comp = createCompositeWrapper( cmpDataSet );

		btnUseReportData = new Button( comp, SWT.RADIO );
		btnUseReportData.setText( Messages.getString( "TaskSelectData.Label.UseReportData" ) ); //$NON-NLS-1$
		btnUseReportData.addSelectionListener( this );

		btnUseDataSet = new Button( comp, SWT.RADIO );
		btnUseDataSet.setText( Messages.getString( "TaskSelectData.Label.UseDataSet" ) ); //$NON-NLS-1$
		btnUseDataSet.addSelectionListener( this );

		cmbDataSet = new Combo( cmpDataSet, SWT.DROP_DOWN | SWT.READ_ONLY );
		cmbDataSet.setLayoutData( new GridData( GridData.VERTICAL_ALIGN_END
				| GridData.FILL_HORIZONTAL ) );
		cmbDataSet.addSelectionListener( this );

		btnNewData = new Button( cmpDataSet, SWT.NONE );
		{
			btnNewData.setLayoutData( new GridData( GridData.VERTICAL_ALIGN_END ) );
			btnNewData.setText( Messages.getString( "TaskSelectData.Label.CreateNew" ) ); //$NON-NLS-1$
			btnNewData.setToolTipText( Messages.getString( "TaskSelectData.Tooltip.CreateNewDataset" ) ); //$NON-NLS-1$
			btnNewData.addSelectionListener( this );
		}
	}

	private void createDataPreviewTableArea( Composite parent )
	{
		Composite composite = createCompositeWrapper( parent );
		{
			composite.setLayoutData( new GridData( GridData.FILL_BOTH ) );
		}
		Label label = new Label( composite, SWT.NONE );
		{
			label.setText( Messages.getString( "TaskSelectData.Label.DataPreview" ) ); //$NON-NLS-1$
			label.setFont( JFaceResources.getBannerFont( ) );
		}

		tablePreview = new CustomPreviewTable( composite, SWT.SINGLE
				| SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION );
		{
			GridData gridData = new GridData( GridData.FILL_BOTH );
			gridData.widthHint = CENTER_WIDTH_HINT;
			gridData.heightHint = 250;
			tablePreview.setLayoutData( gridData );
			tablePreview.setHeaderAlignment( SWT.LEFT );
			tablePreview.addListener( CustomPreviewTable.MOUSE_RIGHT_CLICK_TYPE,
					this );
		}
		dynamicArea.setCustomPreviewTable( tablePreview );
	}

	private void createDataPreviewButtonArea( Composite parent )
	{
		Composite composite = createCompositeWrapper( parent );
		{
			composite.setLayoutData( new GridData( GridData.VERTICAL_ALIGN_END ) );
		}

		// btnFilters = new Button( composite, SWT.NONE );
		// {
		// btnFilters.setAlignment( SWT.CENTER );
		// GridData gridData = new GridData( );
		// gridData.widthHint = 80;
		// btnFilters.setLayoutData( gridData );
		// btnFilters.setText( Messages.getString(
		// "TaskSelectData.Label.Filters" ) ); //$NON-NLS-1$
		// btnFilters.addSelectionListener( this );
		// }

		btnParameters = new Button( composite, SWT.NONE );
		{
			btnParameters.setAlignment( SWT.CENTER );
			GridData gridData = new GridData( );
			gridData.widthHint = 80;
			btnParameters.setLayoutData( gridData );
			btnParameters.setText( Messages.getString( "TaskSelectData.Label.Parameters" ) ); //$NON-NLS-1$
			btnParameters.addSelectionListener( this );
		}
	}

	protected void init( )
	{
		// Create data set list
		getWizardContext( ).getDataServiceProvider( )
				.setContext( getWizardContext( ).getExtendedItem( ) );
		reportDataSet = getWizardContext( ).getDataServiceProvider( )
				.getCurrentDataSet( );
		if ( reportDataSet != null )
		{
			cmbDataSet.setItems( getWizardContext( ).getDataServiceProvider( )
					.getAllDataSets( ) );
			cmbDataSet.setText( reportDataSet );
			useDataSet( true );
			switchDataTable( cmbDataSet.getText( ) );
		}
		else
		{
			cmbDataSet.setText( "" ); //$NON-NLS-1$
		}
	}

	protected void initWithoutChart( )
	{
		useDataSet( false );
	}

	private void useDataSet( boolean bDS )
	{
		btnUseDataSet.setSelection( bDS );
		btnUseReportData.setSelection( !bDS );
		checkUseDataSet( bDS );
	}

	private void refreshTableColor( )
	{
		// Reset column color
		for ( int i = 0; i < tablePreview.getColumnNumber( ); i++ )
		{
			tablePreview.setColumnColor( i, ColorPalette.getInstance( )
					.getColor( tablePreview.getColumnHeading( i ) ) );
		}
	}

	private void switchDataTable( String datasetName )
	{
		// Add data header
		String[] header = getWizardContext( ).getDataServiceProvider( )
				.getPreviewHeader( );
		tablePreview.setColumns( header );

		refreshTableColor( );

		// Add data value
		List dataList = getWizardContext( ).getDataServiceProvider( )
				.getPreviewData( );
		for ( Iterator iterator = dataList.iterator( ); iterator.hasNext( ); )
		{
			String[] dataRow = (String[]) iterator.next( );
			for ( int i = 0; i < dataRow.length; i++ )
			{
				tablePreview.addEntry( dataRow[i], i );
			}
		}
	}

	private void createPreviewPainter( )
	{
		if ( previewPainter == null )
		{
			previewPainter = new ChartPreviewPainter( getWizardContext( ).getProcessor( ) );
			previewCanvas.addPaintListener( previewPainter.getPaintListener( ) );
			previewPainter.setPreview( previewCanvas );
		}

		previewPainter.renderModel( getChartModel( ) );
	}

	protected Chart getChartModel( )
	{
		if ( getContext( ) == null )
		{
			return null;
		}
		return getWizardContext( ).getModel( );
	}

	private void switchDataSet( String datasetName ) throws ChartException
	{
		try
		{
			getWizardContext( ).getDataServiceProvider( )
					.setDataSet( datasetName );
			tablePreview.clearContents( );
			if ( datasetName != null )
			{
				switchDataTable( datasetName );
			}
			tablePreview.layout( );
		}
		catch ( Throwable t )
		{
			throw new ChartException( ChartEnginePlugin.ID, 1, t );
		}
	}

	public void widgetSelected( SelectionEvent e )
	{
		if ( e.getSource( ).equals( btnUseReportData ) )
		{
			try
			{
				switchDataSet( null );
			}
			catch ( ChartException e1 )
			{
				container.displayException( e1 );
			}
			tablePreview.createDummyTable( );
			checkUseDataSet( false );
		}
		else if ( e.getSource( ).equals( btnUseDataSet ) )
		{
			if ( cmbDataSet.getText( ).length( ) == 0 )
			{
				cmbDataSet.setItems( getWizardContext( ).getDataServiceProvider( )
						.getAllDataSets( ) );
				cmbDataSet.select( 0 );
			}
			if ( cmbDataSet.getText( ).length( ) != 0 )
			{
				try
				{
					switchDataSet( cmbDataSet.getText( ) );
				}
				catch ( ChartException e1 )
				{
					DefaultLoggerImpl.instance( )
							.log( DefaultLoggerImpl.ERROR,
									Messages.getString( "TaskSelectData.Exception.UnableToSwitchToDataset" ) //$NON-NLS-1$
											+ cmbDataSet.getText( ) );
				}
			}
			checkUseDataSet( true );
		}
		else if ( e.getSource( ).equals( cmbDataSet ) )
		{
			try
			{
				ColorPalette.getInstance( ).restore( );
				switchDataSet( cmbDataSet.getText( ) );
			}
			catch ( ChartException e1 )
			{
				DefaultLoggerImpl.instance( )
						.log( DefaultLoggerImpl.ERROR,
								Messages.getString( "TaskSelectData.Exception.UnableToSwitchToDataset" ) + cmbDataSet.getText( ) ); //$NON-NLS-1$
			}
		}
		else if ( e.getSource( ).equals( btnNewData ) )
		{
			String[] sAllDS = getWizardContext( ).getDataServiceProvider( )
					.getAllDataSets( );
			String sCurrentDS = ""; //$NON-NLS-1$
			if ( sAllDS.length > 0 )
			{
				sCurrentDS = getWizardContext( ).getDataServiceProvider( )
						.getCurrentDataSet( );
			}
			getWizardContext( ).getDataServiceProvider( )
					.invoke( IDataServiceProvider.COMMAND_NEW_DATASET );
			sAllDS = ( (ChartWizardContext) context ).getDataServiceProvider( )
					.getAllDataSets( );
			// Update UI with DS list
			cmbDataSet.setItems( sAllDS );
			checkUseDataSet( true );

			if ( sCurrentDS.length( ) > 0 )
			{
				cmbDataSet.setText( sCurrentDS );
			}
			else if ( sAllDS.length > 0 )
			{
				// If at least one dataset is defined in the report design...AND
				// if a dataset had not already been bound to the chart...
				// bind the first dataset in the list to the chart
				cmbDataSet.setText( sAllDS[0] );
				try
				{
					switchDataSet( sAllDS[0] );
				}
				catch ( ChartException e1 )
				{
					DefaultLoggerImpl.instance( )
							.log( DefaultLoggerImpl.ERROR,
									Messages.getString( "TaskSelectData.Exception.UnableToSwitchToDataset" ) + sAllDS[0] ); //$NON-NLS-1$
				}
			}
		}
		// else if ( e.getSource( ).equals( btnFilters ) )
		// {
		// if ( getWizardContext( ).getDataServiceProvider( )
		// .invoke( IDataServiceProvider.COMMAND_EDIT_FILTER ) == Window.OK )
		// {
		// refreshTablePreview( );
		// }
		// }
		else if ( e.getSource( ).equals( btnParameters ) )
		{
			if ( getWizardContext( ).getDataServiceProvider( )
					.invoke( IDataServiceProvider.COMMAND_EDIT_PARAMETER ) == Window.OK )
			{
				refreshTablePreview( );
			}
		}
		else if ( e.getSource( ) instanceof MenuItem )
		{
			MenuItem item = (MenuItem) e.getSource( );
			IAction action = (IAction) item.getData( );
			action.setChecked( !action.isChecked( ) );
			action.run( );
		}
	}

	private void refreshTablePreview( )
	{
		tablePreview.clearContents( );
		if ( cmbDataSet.getText( ) != null )
		{
			switchDataTable( cmbDataSet.getText( ) );
		}
		tablePreview.layout( );
	}

	protected ChartWizardContext getWizardContext( )
	{
		return (ChartWizardContext) getContext( );
	}

	public void widgetDefaultSelected( SelectionEvent e )
	{
	}

	private void checkUseDataSet( boolean checked )
	{
		boolean hasDataset = cmbDataSet.getItems( ).length > 0;
		cmbDataSet.setEnabled( checked );
		btnNewData.setEnabled( checked );
		// btnFilters.setEnabled( checked && hasDataset );
		btnParameters.setEnabled( checked && hasDataset );
	}

	public void widgetDisposed( DisposeEvent e )
	{
		// Not dispose other widgets any more
		cmpTask = null;

		previewPainter = null;
		isInited = false;
		dynamicArea.dispose( );
		dynamicArea = null;
		reportDataSet = null;

		disposeResource( whiteColor );
		whiteColor = null;

		// Restore color registry
		ColorPalette.getInstance( ).restore( );

		// Remove all registered data definition text
		DataDefinitionTextManager.getInstance( ).removeAll( );
	}

	private void disposeResource( Color color )
	{
		if ( color != null && !color.isDisposed( ) )
		{
			color.dispose( );
		}
	}

	private ISelectDataCustomizeUI getCustomizeUI( )
	{
		return dynamicArea;
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
			refreshBottomArea( );
			// Refresh all data definitino text
			DataDefinitionTextManager.getInstance( ).refreshAll( );
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
			manageColorAndQuery( query );
			refreshRightArea( );
			// Refresh all data definitino text
			DataDefinitionTextManager.getInstance( ).refreshAll( );
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
			if ( getChartModel( ) instanceof DialChart )
			{
				// Only part in dial type is bottom area
				refreshBottomArea( );
			}
			else
			{
				refreshLeftArea( );
			}
			// Refresh all data definitino text
			DataDefinitionTextManager.getInstance( ).refreshAll( );
		}
	}

	// This popup menu action is removed
	// class InsertAggregationAction extends Action
	// {
	//
	// InsertAggregationAction( )
	// {
	// super( Messages.getString( "TaskSelectData.Label.InsertAggregation" ) );
	// //$NON-NLS-1$
	// }
	//
	// public void run( )
	// {
	// // TODO: Invoke expression builder...create a column with the
	// // resulting expression as header
	// String sExpr = getWizardContext( ).getUIServiceProvider( )
	// .invoke( "", //$NON-NLS-1$
	// getWizardContext( ).getExtendedItem( ),
	// Messages.getString( "TaskSelectData.Label.DataExpressionBuilder" ) );
	// //$NON-NLS-1$
	// tablePreview.addColumn( sExpr, null, 200 );
	// tablePreview.layout( );
	// }
	// }

	class HeaderShowAction extends Action
	{

		HeaderShowAction( )
		{
			super( tablePreview.getCurrentColumnHeading( ) );
			setEnabled( false );
		}
	}

	public void handleEvent( Event event )
	{
		if ( event.type == CustomPreviewTable.MOUSE_RIGHT_CLICK_TYPE
				&& getWizardContext( ).getDataServiceProvider( )
						.getCurrentDataSet( ) != null )
		{
			MenuManager menuManager = new MenuManager( );
			menuManager.setRemoveAllWhenShown( true );
			menuManager.addMenuListener( new IMenuListener( ) {

				public void menuAboutToShow( IMenuManager manager )
				{
					addMenu( manager, new HeaderShowAction( ) );
					addMenu( manager, getBaseSeriesMenu( getChartModel( ) ) );
					addMenu( manager,
							getOrthogonalSeriesMenu( getChartModel( ) ) );
					if ( !( getChartModel( ) instanceof DialChart ) )
					{
						addMenu( manager, getGroupSeriesMenu( getChartModel( ) ) );
					}
					// manager.add( actionInsertAggregation );
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

			Menu menu = menuManager.createContextMenu( tablePreview );
			menu.setVisible( true );
		}
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
								+ Messages.getString( "TaskSelectData.Label.Component" ) ); //$NON-NLS-1$
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
				if ( axisNum == 1 && sds.size( ) == 1 )
				{
					// Simply cascade menu
					return action;
				}
				action.setText( getSecondMenuText( axisIndex,
						i,
						sd.getDesignTimeSeries( ) ) );
				topManager.add( action );
			}
		}
		return topManager;
	}

	private String getSecondMenuText( int axisIndex, int seriesIndex,
			Series series )
	{
		String text = axisIndex == 0
				? "" : Messages.getString( "TaskSelectData.Label.Overlay" ); //$NON-NLS-1$ //$NON-NLS-2$
		text += Messages.getString( "TaskSelectData.Label.Series" ) //$NON-NLS-1$
				+ ( seriesIndex + 1 ) + " (" + series.getDisplayName( ) + ")"; //$NON-NLS-1$ //$NON-NLS-2$
		return text;
	}

	private String getBaseSeriesTitle( Chart chart )
	{
		if ( chart instanceof ChartWithAxes )
		{
			return Messages.getString( "TaskSelectData.Label.UseAsCategoryXAxis" ); //$NON-NLS-1$
		}
		return Messages.getString( "TaskSelectData.Label.UseAsCategoryBaseAxis" ); //$NON-NLS-1$
	}

	private String getOrthogonalSeriesTitle( Chart chart )
	{
		if ( chart instanceof ChartWithAxes )
		{
			return Messages.getString( "TaskSelectData.Label.PlotAsValueYSeries" ); //$NON-NLS-1$
		}
		else if ( chart instanceof DialChart )
		{
			return Messages.getString( "TaskSelectData.Label.PlotAsGaugeValue" ); //$NON-NLS-1$
		}
		return Messages.getString( "TaskSelectData.Label.PlotAsValueOrthogonalSeries" ); //$NON-NLS-1$
	}

	private String getGroupSeriesTitle( Chart chart )
	{
		if ( chart instanceof ChartWithAxes )
		{
			return Messages.getString( "TaskSelectData.Label.UseToGroupYSeries" ); //$NON-NLS-1$
		}
		return Messages.getString( "TaskSelectData.Label.UseToGroupOrthogonalSeries" ); //$NON-NLS-1$
	}

	public void changeTask( Notification notification )
	{
		if ( cmpTask != null )
		{
			if ( notification.getNotifier( ) instanceof Query )
			{
				doLivePreview( );
			}
			previewPainter.renderModel( getChartModel( ) );
		}
	}

	private SampleData updateSampleData( SampleData sdOld )
	{
		SampleData sdNew = DataFactory.eINSTANCE.createSampleData( );
		Chart chart = getChartModel( );
		// CREATE BaseSampleData
		Series baseSeries = null;
		if ( chart instanceof ChartWithAxes )
		{
			baseSeries = ( (SeriesDefinition) ( (Axis) ( (ChartWithAxes) chart ).getAxes( )
					.get( 0 ) ).getSeriesDefinitions( ).get( 0 ) ).getDesignTimeSeries( );
		}
		else
		{
			baseSeries = ( (SeriesDefinition) ( (ChartWithoutAxes) chart ).getSeriesDefinitions( )
					.get( 0 ) ).getDesignTimeSeries( );
		}
		if ( baseSeries == null )
		{
			throw new IllegalStateException( "Chart does not have a Base Series!" ); //$NON-NLS-1$
		}
		String sBaseData = ""; //$NON-NLS-1$
		try
		{
			sBaseData = getDataForSeries( baseSeries );
			BaseSampleData bsd = DataFactory.eINSTANCE.createBaseSampleData( );
			bsd.setDataSetRepresentation( sBaseData );
			sdNew.getBaseSampleData( ).add( bsd );
		}
		catch ( ChartException e )
		{
			container.displayException( e );
			return sdOld;
		}
		// CREATE OrthogonalSampleData
		Series orthogonalSeries = null;
		if ( chart instanceof ChartWithAxes )
		{
			Axis xAxis = ( (Axis) ( (ChartWithAxes) chart ).getAxes( ).get( 0 ) );
			int iYAxes = xAxis.getAssociatedAxes( ).size( );
			// FOR EACH Y AXIS
			for ( int i = 0; i < iYAxes; i++ )
			{
				Axis yAxis = (Axis) xAxis.getAssociatedAxes( ).get( i );
				int iYSeries = yAxis.getSeriesDefinitions( ).size( );
				// FOR EACH SERIES
				for ( int iS = 0; iS < iYSeries; iS++ )
				{
					orthogonalSeries = ( (SeriesDefinition) yAxis.getSeriesDefinitions( )
							.get( iS ) ).getDesignTimeSeries( );
					if ( iS == 0 && orthogonalSeries == null )
					{
						throw new IllegalStateException( "Chart does not have an Orthogonal Series!" ); //$NON-NLS-1$
					}
					String sOrthogonalData = ""; //$NON-NLS-1$
					try
					{
						sOrthogonalData = getDataForSeries( orthogonalSeries );
						OrthogonalSampleData osd = DataFactory.eINSTANCE.createOrthogonalSampleData( );
						osd.setDataSetRepresentation( sOrthogonalData );
						osd.setSeriesDefinitionIndex( iS );
						osd.eAdapters( ).addAll( sdOld.eAdapters( ) );
						sdNew.getOrthogonalSampleData( ).add( osd );
					}
					catch ( ChartException e )
					{
						container.displayException( e );
						return sdOld;
					}
				}
			}
		}
		else
		{
			orthogonalSeries = ( (SeriesDefinition) ( (ChartWithoutAxes) chart ).getSeriesDefinitions( )
					.get( 0 ) ).getDesignTimeSeries( );
		}
		// CREATE AncillarySampleData
		// ADD ADAPTERS
		// SET SampleData INTO MODEL
		return sdNew;
	}

	private String getDataForSeries( Series series ) throws ChartException
	{
		StringBuffer sbData = new StringBuffer( );
		Class clSeries = series.getClass( );
		IDataSetProcessor iDSP = PluginSettings.instance( )
				.getDataSetProcessor( clSeries );
		// String sFormat = iDSP.getExpectedStringFormat( );
		int iSeriesComponents = series.getDataDefinition( ).size( );
		// GET DATA FOR EACH COMPONENT...THE FIRST DIMENSION INDEX IN THE ARRAY
		// REPRESENTS THE COMPONENT FOR WHICH THE DATA IN THE SECOND DIMENSION
		// APPLIES
		String[] exprArray = new String[iSeriesComponents];
		for ( int i = 0; i < iSeriesComponents; i++ )
		{
			String sExpr = ""; //$NON-NLS-1$
			try
			{
				sExpr = ( (Query) series.getDataDefinition( ).get( i ) ).getDefinition( );
			}
			catch ( Exception e )
			{
				// IF DATA FOR ALL THE COMPONENTS HAS NOT BEEN SPECIFIED...USE
				// THE DATA FOR THE FIRST COMPONENT
				sExpr = ( (Query) series.getDataDefinition( ).get( 0 ) ).getDefinition( );
			}
			exprArray[i] = sExpr;
		}
		Object[] columnData = getWizardContext( ).getDataServiceProvider( )
				.getDataForColumns( exprArray, -1, true );
		String[] seriesdata = new String[]{
			iDSP.toString( columnData )
		};
		// BUILD THE STRING TO BE SET INTO THE SAMPLE DATA...NEEDS TO USE THE
		// FORMAT RETURNED BY THE SERIES DATASETPROCESSOR
		for ( int i = 0; i < seriesdata.length; i++ )
		{
			// TODO: HANDLE MULTIPLE COMPONENTS...AND USE THE STRING FORMAT
			// INFORMATION!
			if ( i > 0 )
			{
				sbData.append( "," ); //$NON-NLS-1$
			}
			sbData.append( seriesdata[i] );
		}
		return sbData.toString( );
	}

	private void manageColorAndQuery( Query query )
	{
		// If it's last element, remove color binding
		if ( DataDefinitionTextManager.getInstance( )
				.getNumberOfSameDataDefinition( query.getDefinition( ) ) == 1 )
		{
			ColorPalette.getInstance( )
					.retrieveColor( ChartUIUtil.getColumnName( query.getDefinition( ) ) );
		}
		query.setDefinition( ChartUIUtil.getExpressionString( tablePreview.getCurrentColumnHeading( ) ) );
		ColorPalette.getInstance( )
				.putColor( tablePreview.getCurrentColumnHeading( ) );
		// Reset table column color
		refreshTableColor( );
	}

	private void doLivePreview( )
	{
		String errorInfo = Messages.getString( "exception.data.DataBindingsAreNull" ); //$NON-NLS-1$
		if ( ChartUIUtil.checkDataBinding( getChartModel( ) ) )
		{
			oldSample = (SampleData) EcoreUtil.copy( getChartModel( ).getSampleData( ) );
			SampleData newSample = updateSampleData( oldSample );
			// ADD ALL ADAPTERS...AND REFRESH PREVIEW
			newSample.eAdapters( ).addAll( getChartModel( ).eAdapters( ) );
			getChartModel( ).setSampleData( newSample );
			removeError( errorInfo );
		}
		else
		{
			addError( errorInfo );
		}
	}
}
