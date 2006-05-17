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

import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.DialChart;
import org.eclipse.birt.chart.model.component.Axis;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.data.Query;
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
import org.eclipse.birt.core.ui.frameworks.taskwizard.SimpleTask;
import org.eclipse.emf.common.notify.Notification;
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
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
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

import com.ibm.icu.util.ULocale;

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

	private final static int CENTER_WIDTH_HINT = 400;
	private transient ChartPreviewPainter previewPainter = null;

	private transient Composite cmpTask = null;

	private transient Composite cmpPreview = null;
	private transient Canvas previewCanvas = null;

	private transient Button btnUseReportData = null;
	private transient Button btnUseDataSet = null;
	private transient Combo cmbDataSet = null;
	private transient Button btnNewData = null;

	private transient CustomPreviewTable tablePreview = null;
	private transient Button btnFilters = null;
	private transient Button btnParameters = null;
	private transient Button btnBinding = null;

	private transient SelectDataDynamicArea dynamicArea;
	private transient String BLANK_DATASET = ""; //$NON-NLS-1$

	// private SampleData oldSample = null;

	public TaskSelectData( )
	{
		super( Messages.getString( "TaskSelectData.TaskExp" ) ); //$NON-NLS-1$
	}

	public String getDescription( ULocale locale )
	{
		return Messages.getString( "TaskSelectData.Task.Description" ); //$NON-NLS-1$
	}

	public Composite getUI( Composite parent )
	{
		if ( cmpTask == null || cmpTask.isDisposed( ) )
		{
			cmpTask = new Composite( parent, SWT.NONE );
			GridLayout gridLayout = new GridLayout( 3, false );
			gridLayout.marginWidth = 10;
			gridLayout.marginHeight = 10;
			cmpTask.setLayout( gridLayout );
			cmpTask.setLayoutData( new GridData( GridData.GRAB_HORIZONTAL
					| GridData.GRAB_VERTICAL ) );
			cmpTask.addDisposeListener( this );

			dynamicArea = new SelectDataDynamicArea( this );
			getCustomizeUI( ).init( );

			placeComponents( );
			createPreviewPainter( );
			init( );
		}
		else
		{
			customizeUI( );
		}
		doLivePreview( );
		// Refresh all data definitino text
		DataDefinitionTextManager.getInstance( ).refreshAll( );
		return cmpTask;
	}

	protected void customizeUI( )
	{
		getCustomizeUI( ).init( );
		refreshLeftArea( );
		refreshRightArea( );
		refreshBottomArea( );
		getCustomizeUI( ).layoutAll( );
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
			Composite cmpLeftContainer = ChartUIUtil.createCompositeWrapper( cmpTask );
			GridData gridData = new GridData( GridData.FILL_HORIZONTAL
					| GridData.VERTICAL_ALIGN_CENTER );
			gridData.verticalSpan = 2;
			cmpLeftContainer.setLayoutData( gridData );
			getCustomizeUI( ).createLeftBindingArea( cmpLeftContainer );
		}
		createPreviewArea( );
		{
			Composite cmpRightContainer = ChartUIUtil.createCompositeWrapper( cmpTask );
			GridData gridData = new GridData( GridData.FILL_HORIZONTAL
					| GridData.VERTICAL_ALIGN_CENTER );
			gridData.verticalSpan = 2;
			cmpRightContainer.setLayoutData( gridData );
			getCustomizeUI( ).createRightBindingArea( cmpRightContainer );
		}
		{
			Composite cmpBottomContainer = ChartUIUtil.createCompositeWrapper( cmpTask );
			GridData gridData = new GridData( GridData.FILL_HORIZONTAL
					| GridData.VERTICAL_ALIGN_BEGINNING );
			cmpBottomContainer.setLayoutData( gridData );
			getCustomizeUI( ).createBottomBindingArea( cmpBottomContainer );
		}
	}

	private void createPreviewArea( )
	{
		cmpPreview = ChartUIUtil.createCompositeWrapper( cmpTask );
		{
			GridData gridData = new GridData( GridData.FILL_BOTH );
			gridData.widthHint = CENTER_WIDTH_HINT;
			gridData.heightHint = 200;
			cmpPreview.setLayoutData( gridData );
		}

		Label label = new Label( cmpPreview, SWT.NONE );
		{
			label.setFont( JFaceResources.getBannerFont( ) );
			label.setText( Messages.getString( "TaskSelectData.Label.ChartPreview" ) ); //$NON-NLS-1$
		}

		previewCanvas = new Canvas( cmpPreview, SWT.BORDER );
		{
			GridData gd = new GridData( GridData.FILL_BOTH );
			previewCanvas.setLayoutData( gd );
			previewCanvas.setBackground( Display.getDefault( )
					.getSystemColor( SWT.COLOR_WHITE ) );
		}
	}

	private void createDataSetArea( Composite parent )
	{
		Composite cmpDataSet = ChartUIUtil.createCompositeWrapper( parent );
		{
			cmpDataSet.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
		}

		Label label = new Label( cmpDataSet, SWT.NONE );
		{
			label.setText( Messages.getString( "TaskSelectData.Label.SelectDataSet" ) ); //$NON-NLS-1$
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

		Composite comp = ChartUIUtil.createCompositeWrapper( cmpDetail );

		btnUseReportData = new Button( comp, SWT.RADIO );
		btnUseReportData.setText( Messages.getString( "TaskSelectData.Label.UseReportData" ) ); //$NON-NLS-1$
		btnUseReportData.addSelectionListener( this );

		btnUseDataSet = new Button( comp, SWT.RADIO );
		btnUseDataSet.setText( Messages.getString( "TaskSelectData.Label.UseDataSet" ) ); //$NON-NLS-1$
		btnUseDataSet.addSelectionListener( this );

		cmbDataSet = new Combo( cmpDetail, SWT.DROP_DOWN | SWT.READ_ONLY );
		cmbDataSet.setLayoutData( new GridData( GridData.VERTICAL_ALIGN_END
				| GridData.FILL_HORIZONTAL ) );
		cmbDataSet.addSelectionListener( this );

		btnNewData = new Button( cmpDetail, SWT.NONE );
		{
			btnNewData.setLayoutData( new GridData( GridData.VERTICAL_ALIGN_END ) );
			btnNewData.setText( Messages.getString( "TaskSelectData.Label.CreateNew" ) ); //$NON-NLS-1$
			btnNewData.setToolTipText( Messages.getString( "TaskSelectData.Tooltip.CreateNewDataset" ) ); //$NON-NLS-1$
			btnNewData.addSelectionListener( this );
		}
	}

	private void createDataPreviewTableArea( Composite parent )
	{
		Composite composite = ChartUIUtil.createCompositeWrapper( parent );
		{
			composite.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
		}
		Label label = new Label( composite, SWT.NONE );
		{
			label.setText( Messages.getString( "TaskSelectData.Label.DataPreview" ) ); //$NON-NLS-1$
			label.setFont( JFaceResources.getBannerFont( ) );
		}

		tablePreview = new CustomPreviewTable( composite, SWT.SINGLE
				| SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION );
		{
			GridData gridData = new GridData( GridData.FILL_HORIZONTAL );
			gridData.widthHint = CENTER_WIDTH_HINT;
			gridData.heightHint = 150;
			tablePreview.setLayoutData( gridData );
			tablePreview.setHeaderAlignment( SWT.LEFT );
			tablePreview.addListener( CustomPreviewTable.MOUSE_RIGHT_CLICK_TYPE,
					this );
		}
		dynamicArea.setCustomPreviewTable( tablePreview );
	}

	private void createDataPreviewButtonArea( Composite parent )
	{
		Composite composite = ChartUIUtil.createCompositeWrapper( parent );
		{
			composite.setLayoutData( new GridData( GridData.VERTICAL_ALIGN_END ) );
		}

		btnFilters = new Button( composite, SWT.NONE );
		{
			btnFilters.setAlignment( SWT.CENTER );
			GridData gridData = new GridData( GridData.FILL_HORIZONTAL );
			btnFilters.setLayoutData( gridData );
			btnFilters.setText( Messages.getString( "TaskSelectData.Label.Filters" ) ); //$NON-NLS-1$
			btnFilters.addSelectionListener( this );
		}

		btnParameters = new Button( composite, SWT.NONE );
		{
			btnParameters.setAlignment( SWT.CENTER );
			GridData gridData = new GridData( GridData.FILL_HORIZONTAL );
			btnParameters.setLayoutData( gridData );
			btnParameters.setText( Messages.getString( "TaskSelectData.Label.Parameters" ) ); //$NON-NLS-1$
			btnParameters.addSelectionListener( this );
		}

		btnBinding = new Button( composite, SWT.NONE );
		{
			btnBinding.setAlignment( SWT.CENTER );
			GridData gridData = new GridData( GridData.FILL_HORIZONTAL );
			btnBinding.setLayoutData( gridData );
			btnBinding.setText( Messages.getString( "TaskSelectData.Label.DataBinding" ) ); //$NON-NLS-1$
			btnBinding.addSelectionListener( this );
		}
	}

	protected void init( )
	{
		// Create data set list
		String currentDataSet = getDataServiceProvider( ).getBoundDataSet( );
		if ( currentDataSet != null )
		{
			cmbDataSet.setItems( getDataServiceProvider( ).getAllDataSets( ) );
			cmbDataSet.setText( ( currentDataSet == null ) ? BLANK_DATASET
					: currentDataSet );
			useReportDataSet( false );
			if ( currentDataSet != null )
			{
				switchDataTable( );
			}
		}
		else
		{
			useReportDataSet( true );
			String reportDataSet = getDataServiceProvider( ).getReportDataSet( );
			if ( reportDataSet != null )
			{
				switchDataTable( );
			}
		}

		btnFilters.setEnabled( hasDataSet( )
				&& getDataServiceProvider( ).isInvokingSupported( ) );
		btnParameters.setEnabled( hasDataSet( )
				&& getDataServiceProvider( ).isInvokingSupported( ) );
		btnBinding.setEnabled( hasDataSet( )
				&& getDataServiceProvider( ).isInvokingSupported( ) );
	}

	private void useReportDataSet( boolean bDS )
	{
		btnUseReportData.setSelection( bDS );

		btnUseDataSet.setSelection( !bDS );
		cmbDataSet.setEnabled( !bDS );
		btnNewData.setEnabled( !bDS
				&& getDataServiceProvider( ).isInvokingSupported( ) );
		
		if ( bDS )
		{
			// Initializes column bindings from container
			getDataServiceProvider( ).setDataSet( null );
		}
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

	private void switchDataTable( )
	{
		try
		{
			// Add data header
			String[] header = getDataServiceProvider( ).getPreviewHeader( );
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
				List dataList = getDataServiceProvider( ).getPreviewData( );
				for ( Iterator iterator = dataList.iterator( ); iterator.hasNext( ); )
				{
					String[] dataRow = (String[]) iterator.next( );
					for ( int i = 0; i < dataRow.length; i++ )
					{
						tablePreview.addEntry( dataRow[i], i );
					}
				}
			}
		}
		catch ( ChartException e )
		{
			ChartWizard.displayException( e );
		}

	}

	private void createPreviewPainter( )
	{
		previewPainter = new ChartPreviewPainter( (ChartWizardContext) getContext( ) );
		previewCanvas.addPaintListener( previewPainter );
		previewCanvas.addControlListener( previewPainter );
		previewPainter.setPreview( previewCanvas );
	}

	protected Chart getChartModel( )
	{
		if ( getContext( ) == null )
		{
			return null;
		}
		return ( (ChartWizardContext) getContext( ) ).getModel( );
	}

	private int switchDataSet( String datasetName ) throws ChartException
	{
		int bCancel = Window.OK;
		if ( getDataServiceProvider( ).getBoundDataSet( ) != null
				&& getDataServiceProvider( ).getBoundDataSet( )
						.equals( datasetName ) )
		{
			return bCancel;
		}

		try
		{
			// Clear old dataset and preview data
			getDataServiceProvider( ).setDataSet( datasetName );
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
		doLivePreview( );
		return bCancel;
	}

	public void widgetSelected( SelectionEvent e )
	{
		if ( e.getSource( ).equals( btnUseReportData ) )
		{
			// Skip when selection is false
			if ( !btnUseReportData.getSelection( ) )
			{
				return;
			}
			try
			{
				switchDataSet( null );
			}
			catch ( ChartException e1 )
			{
				ChartWizard.displayException( e1 );
			}
			cmbDataSet.add( BLANK_DATASET, 0 );
			cmbDataSet.select( 0 );
			cmbDataSet.setEnabled( false );
			btnNewData.setEnabled( false );
			btnFilters.setEnabled( hasDataSet( )
					&& getDataServiceProvider( ).isInvokingSupported( ) );
			btnParameters.setEnabled( hasDataSet( )
					&& getDataServiceProvider( ).isInvokingSupported( ) );
			btnBinding.setEnabled( hasDataSet( )
					&& getDataServiceProvider( ).isInvokingSupported( ) );
		}
		else if ( e.getSource( ).equals( btnUseDataSet ) )
		{
			// Skip when selection is false
			if ( !btnUseDataSet.getSelection( ) )
			{
				return;
			}

			cmbDataSet.removeAll( );
			cmbDataSet.add( BLANK_DATASET, 0 );

			String[] dataSets = getDataServiceProvider( ).getAllDataSets( );
			if ( dataSets != null )
				for ( int i = 0; i < dataSets.length; i++ )
				{
					cmbDataSet.add( dataSets[i], i + 1 );
				}
			cmbDataSet.select( 0 );
			cmbDataSet.setEnabled( true );
			btnNewData.setEnabled( getDataServiceProvider( ).isInvokingSupported( ) );
		}
		else if ( e.getSource( ).equals( cmbDataSet ) )
		{
			try
			{
				ColorPalette.getInstance( ).restore( );
				if ( !cmbDataSet.getText( ).equals( BLANK_DATASET ) )
				{
					int bCancel = switchDataSet( cmbDataSet.getText( ) );
					if ( bCancel == Window.OK
							&& cmbDataSet.getItem( 0 ).equals( BLANK_DATASET ) )
					{
						cmbDataSet.remove( BLANK_DATASET );
					}
					else if ( bCancel == Window.CANCEL )
					{
						String[] datasetNames = cmbDataSet.getItems( );
						for ( int i = 0; i < datasetNames.length; i++ )
						{
							if ( datasetNames[i].equals( getDataServiceProvider( ).getBoundDataSet( ) ) )
							{
								cmbDataSet.select( i );
								if ( cmbDataSet.getItem( 0 )
										.equals( BLANK_DATASET ) )
								{
									cmbDataSet.remove( BLANK_DATASET );
								}
								return;
							}
						}
						cmbDataSet.select( 0 );
					}
				}
			}
			catch ( ChartException e1 )
			{
				ChartWizard.displayException( e1 );
			}

			btnNewData.setEnabled( getDataServiceProvider( ).isInvokingSupported( ) );
			btnFilters.setEnabled( hasDataSet( )
					&& getDataServiceProvider( ).isInvokingSupported( ) );
			btnParameters.setEnabled( hasDataSet( )
					&& getDataServiceProvider( ).isInvokingSupported( ) );
			btnBinding.setEnabled( hasDataSet( )
					&& getDataServiceProvider( ).isInvokingSupported( ) );
		}
		else if ( e.getSource( ).equals( btnNewData ) )
		{
			// Bring up the dialog to create a dataset
			int result = getDataServiceProvider( ).invoke( IDataServiceProvider.COMMAND_NEW_DATASET );
			if ( result == Window.CANCEL )
			{
				return;
			}

			String[] sAllDS = getDataServiceProvider( ).getAllDataSets( );

			String currentDataSet = cmbDataSet.getText( );
			int dataSetCount = cmbDataSet.getItemCount( );
			if ( currentDataSet.equals( BLANK_DATASET ) )
			{
				dataSetCount = dataSetCount - 1;
			}

			if ( sAllDS.length == dataSetCount )
			{
				return;
			}
			if ( currentDataSet.equals( BLANK_DATASET ) )
			{
				cmbDataSet.removeAll( );
				cmbDataSet.add( BLANK_DATASET, 0 );

				for ( int i = 0; i < sAllDS.length; i++ )
				{
					cmbDataSet.add( sAllDS[i], i + 1 );
				}
			}
			else
			{
				cmbDataSet.setItems( sAllDS );
			}
			cmbDataSet.setText( currentDataSet );
		}
		else if ( e.getSource( ).equals( btnFilters ) )
		{
			if ( getDataServiceProvider( ).invoke( IDataServiceProvider.COMMAND_EDIT_FILTER ) == Window.OK )
			{
				refreshTablePreview( );
				doLivePreview( );
			}
		}
		else if ( e.getSource( ).equals( btnParameters ) )
		{
			if ( getDataServiceProvider( ).invoke( IDataServiceProvider.COMMAND_EDIT_PARAMETER ) == Window.OK )
			{
				refreshTablePreview( );
				doLivePreview( );
			}
		}
		else if ( e.getSource( ).equals( btnBinding ) )
		{
			if ( getDataServiceProvider( ).invoke( IDataServiceProvider.COMMAND_EDIT_BINDING ) == Window.OK )
			{
				refreshTablePreview( );
				doLivePreview( );
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
			switchDataTable( );
		}
		tablePreview.layout( );
	}

	protected IDataServiceProvider getDataServiceProvider( )
	{
		return ( (ChartWizardContext) getContext( ) ).getDataServiceProvider( );
	}

	public void widgetDefaultSelected( SelectionEvent e )
	{
	}

	private boolean hasDataSet( )
	{
		return getDataServiceProvider( ).getReportDataSet( ) != null
				|| getDataServiceProvider( ).getBoundDataSet( ) != null;
	}

	public void widgetDisposed( DisposeEvent e )
	{
		super.dispose( );
		// No need to dispose other widgets
		cmpTask = null;
		previewPainter.dispose( );
		previewPainter = null;
		dynamicArea.dispose( );
		dynamicArea = null;
		// oldSample = null;

		// Restore color registry
		ColorPalette.getInstance( ).restore( );

		// Remove all registered data definition text
		DataDefinitionTextManager.getInstance( ).removeAll( );
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
			// Use the first group, and copy to the all groups
			ChartAdapter.ignoreNotifications( true );
			ChartUIUtil.setAllGroupingQueryExceptFirst( getChartModel( ),
					ChartUIUtil.getExpressionString( tablePreview.getCurrentColumnHeading( ) ) );
			ChartAdapter.ignoreNotifications( false );

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
			refreshLeftArea( );
			// Refresh all data definitino text
			DataDefinitionTextManager.getInstance( ).refreshAll( );
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

	public void handleEvent( Event event )
	{
		if ( event.type == CustomPreviewTable.MOUSE_RIGHT_CLICK_TYPE
				&& ( getDataServiceProvider( ).getBoundDataSet( ) != null || getDataServiceProvider( ).getReportDataSet( ) != null ) )
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
					addMenu( manager, getGroupSeriesMenu( getChartModel( ) ) );
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
				sb.append( Messages.getString( "TaskSelectData.Label.Overlay" ) ); //$NON-NLS-1$ 
			}
		}
		sb.append( Messages.getString( "TaskSelectData.Label.Series" ) //$NON-NLS-1$
				+ ( seriesIndex + 1 ) + " (" + series.getDisplayName( ) + ")" ); //$NON-NLS-1$ //$NON-NLS-2$
		return sb.toString( );
	}

	private String getBaseSeriesTitle( Chart chart )
	{
		if ( chart instanceof ChartWithAxes )
		{
			return Messages.getString( "TaskSelectData.Label.UseAsCategoryXAxis" ); //$NON-NLS-1$
		}
		return Messages.getString( "TaskSelectData.Label.UseAsCategorySeries" ); //$NON-NLS-1$
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
		return Messages.getString( "TaskSelectData.Label.PlotAsValueSeries" ); //$NON-NLS-1$
	}

	private String getGroupSeriesTitle( Chart chart )
	{
		if ( chart instanceof ChartWithAxes )
		{
			return Messages.getString( "TaskSelectData.Label.UseToGroupYSeries" ); //$NON-NLS-1$
		}
		return Messages.getString( "TaskSelectData.Label.UseToGroupValueSeries" ); //$NON-NLS-1$
	}

	public void changeTask( Notification notification )
	{
		if ( previewPainter != null )
		{
			// Query and series change need to update Live Preview
			if ( notification.getNotifier( ) instanceof Query
					|| notification.getNotifier( ) instanceof Axis
					|| notification.getNotifier( ) instanceof SeriesDefinition )
			{
				doLivePreview( );
			}
			else if ( ChartPreviewPainter.isLivePreviewActive( ) )
			{
				ChartAdapter.ignoreNotifications( true );
				ChartUIUtil.syncRuntimeSeries( getChartModel( ) );
				ChartAdapter.ignoreNotifications( false );

				previewPainter.renderModel( getChartModel( ) );
			}
			else
			{
				previewPainter.renderModel( getChartModel( ) );
			}
		}
	}

	private void manageColorAndQuery( Query query )
	{
		// If it's last element, remove color binding
		if ( DataDefinitionTextManager.getInstance( )
				.getNumberOfSameDataDefinition( query.getDefinition( ) ) == 1 )
		{
			ColorPalette.getInstance( ).retrieveColor( query.getDefinition( ) );
		}
		query.setDefinition( ChartUIUtil.getExpressionString( tablePreview.getCurrentColumnHeading( ) ) );
		ColorPalette.getInstance( ).putColor( query.getDefinition( ) );
		// Reset table column color
		refreshTableColor( );
	}

	private void doLivePreview( )
	{
		if ( getDataServiceProvider( ).isLivePreviewEnabled( )
				&& ChartUIUtil.checkDataBinding( getChartModel( ) )
				&& hasDataSet( ) )
		{
			// Enable live preview
			ChartPreviewPainter.activateLivePreview( true );
			// Make sure not affect model changed
			ChartAdapter.ignoreNotifications( true );
			try
			{
				ChartUIUtil.doLivePreview( getChartModel( ),
						getDataServiceProvider( ) );
			}
			// Includes RuntimeException
			catch ( Exception e )
			{
				// Enable sample data instead
				ChartPreviewPainter.activateLivePreview( false );
			}
			ChartAdapter.ignoreNotifications( false );
		}
		else
		{
			// Disable live preview
			ChartPreviewPainter.activateLivePreview( false );
		}
		previewPainter.renderModel( getChartModel( ) );
	}

}
