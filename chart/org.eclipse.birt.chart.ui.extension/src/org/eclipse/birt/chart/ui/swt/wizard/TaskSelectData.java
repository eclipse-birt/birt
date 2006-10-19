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

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.attribute.AxisType;
import org.eclipse.birt.chart.model.attribute.DataType;
import org.eclipse.birt.chart.model.DialChart;
import org.eclipse.birt.chart.model.component.Axis;
import org.eclipse.birt.chart.model.component.MarkerLine;
import org.eclipse.birt.chart.model.component.MarkerRange;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.data.BaseSampleData;
import org.eclipse.birt.chart.model.data.OrthogonalSampleData;
import org.eclipse.birt.chart.model.data.Query;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.model.type.StockSeries;
import org.eclipse.birt.chart.plugin.ChartEnginePlugin;
import org.eclipse.birt.chart.ui.extension.i18n.Messages;
import org.eclipse.birt.chart.ui.swt.interfaces.IDataServiceProvider;
import org.eclipse.birt.chart.ui.swt.interfaces.ISelectDataCustomizeUI;
import org.eclipse.birt.chart.ui.swt.interfaces.ISeriesUIProvider;
import org.eclipse.birt.chart.ui.swt.interfaces.ITaskChangeListener;
import org.eclipse.birt.chart.ui.swt.wizard.data.SelectDataDynamicArea;
import org.eclipse.birt.chart.ui.swt.wizard.internal.ChartPreviewPainter;
import org.eclipse.birt.chart.ui.swt.wizard.internal.ColorPalette;
import org.eclipse.birt.chart.ui.swt.wizard.internal.CustomPreviewTable;
import org.eclipse.birt.chart.ui.swt.wizard.internal.DataDefinitionTextManager;
import org.eclipse.birt.chart.ui.util.ChartHelpContextIds;
import org.eclipse.birt.chart.ui.util.ChartUIUtil;
import org.eclipse.birt.core.ui.frameworks.taskwizard.SimpleTask;
import org.eclipse.birt.core.ui.frameworks.taskwizard.WizardBase;
import org.eclipse.emf.ecore.EObject;
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
import org.eclipse.swt.widgets.Text;


public class TaskSelectData extends SimpleTask implements
		SelectionListener,
		ITaskChangeListener,
		Listener
{

	private final static int CENTER_WIDTH_HINT = 400;
	private transient ChartPreviewPainter previewPainter = null;

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

	public TaskSelectData( )
	{
		super( Messages.getString( "TaskSelectData.TaskExp" ) ); //$NON-NLS-1$
		setDescription( Messages.getString( "TaskSelectData.Task.Description" ) ); //$NON-NLS-1$
	}

	public void createControl( Composite parent )
	{
		if ( topControl == null || topControl.isDisposed( ) )
		{
			topControl = new Composite( parent, SWT.NONE );
			GridLayout gridLayout = new GridLayout( 3, false );
			gridLayout.marginWidth = 10;
			gridLayout.marginHeight = 10;
			topControl.setLayout( gridLayout );
			topControl.setLayoutData( new GridData( GridData.GRAB_HORIZONTAL
					| GridData.GRAB_VERTICAL ) );

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
		// Refresh all data definition text
		DataDefinitionTextManager.getInstance( ).refreshAll( );

		ChartUIUtil.bindHelp( getControl( ),
				ChartHelpContextIds.TASK_SELECT_DATA );
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
		ChartAdapter.beginIgnoreNotifications( );
		try
		{
			createHeadArea( );// place two rows

			new Label( topControl, SWT.NONE );
			createDataSetArea( topControl );
			new Label( topControl, SWT.NONE );

			new Label( topControl, SWT.NONE );
			createDataPreviewTableArea( topControl );
			createDataPreviewButtonArea( topControl );

			new Label( topControl, SWT.NONE );
			Label description = new Label( topControl, SWT.WRAP );
			{
				GridData gd = new GridData( );
				gd.widthHint = CENTER_WIDTH_HINT;
				description.setLayoutData( gd );
				description.setText( Messages.getString( "TaskSelectData.Label.ToBindADataColumn" ) ); //$NON-NLS-1$
			}
			new Label( topControl, SWT.NONE );
		}
		finally
		{
			// THIS IS IN A FINALLY BLOCK TO ENSURE THAT NOTIFICATIONS ARE
			// ENABLED EVEN IF ERRORS OCCUR DURING UI INITIALIZATION
			ChartAdapter.endIgnoreNotifications( );
		}
	}

	private void createHeadArea( )
	{
		{
			Composite cmpLeftContainer = ChartUIUtil.createCompositeWrapper( topControl );
			GridData gridData = new GridData( GridData.FILL_HORIZONTAL
					| GridData.VERTICAL_ALIGN_CENTER );
			gridData.verticalSpan = 2;
			cmpLeftContainer.setLayoutData( gridData );
			getCustomizeUI( ).createLeftBindingArea( cmpLeftContainer );
		}
		createPreviewArea( );
		{
			Composite cmpRightContainer = ChartUIUtil.createCompositeWrapper( topControl );
			GridData gridData = new GridData( GridData.FILL_HORIZONTAL
					| GridData.VERTICAL_ALIGN_CENTER );
			gridData.verticalSpan = 2;
			cmpRightContainer.setLayoutData( gridData );
			getCustomizeUI( ).createRightBindingArea( cmpRightContainer );
		}
		{
			Composite cmpBottomContainer = ChartUIUtil.createCompositeWrapper( topControl );
			GridData gridData = new GridData( GridData.FILL_HORIZONTAL
					| GridData.VERTICAL_ALIGN_BEGINNING );
			cmpBottomContainer.setLayoutData( gridData );
			getCustomizeUI( ).createBottomBindingArea( cmpBottomContainer );
		}
	}

	private void createPreviewArea( )
	{
		cmpPreview = ChartUIUtil.createCompositeWrapper( topControl );
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
				| SWT.H_SCROLL
				| SWT.V_SCROLL
				| SWT.FULL_SELECTION );
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

			// Switch to sample data slicently
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
		updateApplyButton( );
		return bCancel;
	}

	public void widgetSelected( SelectionEvent e )
	{
		if ( e.getSource( ).equals( btnUseReportData ) )
		{
			ColorPalette.getInstance( ).restore( );

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
				updateApplyButton( );
			}
		}
		else if ( e.getSource( ).equals( btnParameters ) )
		{
			if ( getDataServiceProvider( ).invoke( IDataServiceProvider.COMMAND_EDIT_PARAMETER ) == Window.OK )
			{
				refreshTablePreview( );
				doLivePreview( );
				updateApplyButton( );
			}
		}
		else if ( e.getSource( ).equals( btnBinding ) )
		{
			if ( getDataServiceProvider( ).invoke( IDataServiceProvider.COMMAND_EDIT_BINDING ) == Window.OK )
			{
				refreshTablePreview( );
				doLivePreview( );
				updateApplyButton( );
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

	public void dispose( )
	{
		super.dispose( );
		// No need to dispose other widgets
		if ( previewPainter != null )
		{
			previewPainter.dispose( );
		}
		previewPainter = null;
		if ( dynamicArea != null )
		{
			dynamicArea.dispose( );
		}
		dynamicArea = null;

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

	public void handleEvent( Event event )
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

				// Right click to display the menu. Menu display by clicking
				// application key is triggered by os, so do nothing.
				if ( event.type == CustomPreviewTable.MOUSE_RIGHT_CLICK_TYPE )
				{
					header.getMenu( ).setVisible( true );
				}
			}

		}
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
				+ ( seriesIndex + 1 )
				+ " (" + series.getDisplayName( ) + ")" ); //$NON-NLS-1$ //$NON-NLS-2$
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
			// Only data definition query (not group query) will be validated
			if ( ( notification.getNotifier( ) instanceof Query && ( (Query) notification.getNotifier( ) ).eContainer( ) instanceof Series ) )
			{
				checkDataType( (Query) notification.getNotifier( ),
						(Series) ( (Query) notification.getNotifier( ) ).eContainer( ) );
			}
			
			if ( notification.getNotifier( ) instanceof SeriesDefinition
					&& getChartModel( ) instanceof ChartWithAxes )
			{
				Iterator axes = ((Axis)((SeriesDefinition) notification.getNotifier( )).eContainer( )).getAssociatedAxes( ).iterator( );
				while ( axes.hasNext( ) )
				{
					Series[] series = ( (Axis) axes.next( ) ).getRuntimeSeries( );
					for ( int i = 0; i < series.length; i++ )
					{
						checkDataType( (Query) series[i].getDataDefinition( )
								.get( 0 ),
								series[i] );
					}
				}		
			}

			// Query and series change need to update Live Preview
			if ( notification.getNotifier( ) instanceof Query
					|| notification.getNotifier( ) instanceof Axis
					|| notification.getNotifier( ) instanceof SeriesDefinition )
			{
				doLivePreview( );
			}
			else if ( ChartPreviewPainter.isLivePreviewActive( ) )
			{
				ChartAdapter.beginIgnoreNotifications( );
				ChartUIUtil.syncRuntimeSeries( getChartModel( ) );
				ChartAdapter.endIgnoreNotifications( );

				previewPainter.renderModel( getChartModel( ) );
			}
			else
			{
				previewPainter.renderModel( getChartModel( ) );
			}
		}
	}

	private void checkDataType( Query query, Series series )
	{
		String expression = query.getDefinition( );

		Axis axis = null;
		for ( EObject o = query; o != null; )
		{
			o = o.eContainer( );
			if ( o instanceof Axis )
			{
				axis = (Axis) o;
				break;
			}
		}

		Collection cRegisteredEntries = ChartUIExtensionsImpl.instance( )
				.getSeriesUIComponents( );
		Iterator iterEntries = cRegisteredEntries.iterator( );

		String sSeries = null;
		while ( iterEntries.hasNext( ) )
		{
			ISeriesUIProvider provider = (ISeriesUIProvider) iterEntries.next( );
			sSeries = provider.getSeriesClass( );

			if ( sSeries.equals( series.getClass( ).getName( ) ) )
			{
				try
				{
					provider.validateSeriesBindingType( series,
							getDataServiceProvider( ) );
				}
				catch ( ChartException ce )
				{
					if ( expression != null && expression.trim( ).length( ) > 0 )
					{
						Text text = DataDefinitionTextManager.getInstance( )
								.findText( query );
						if ( text != null )
						{
							// Display the text even if it's useless and will be
							// changed
							text.setText( query.getDefinition( ) );
						}
						WizardBase.displayException( new RuntimeException( Messages.getFormattedString( "TaskSelectData.Warning.TypeCheck",//$NON-NLS-1$
								new String[]{
										expression, sSeries
								} ) ) );
					}
					query.setDefinition( "" ); //$NON-NLS-1$
					DataDefinitionTextManager.getInstance( ).updateText( query );
				}

				if ( getChartModel( ) instanceof ChartWithAxes )
				{
					DataType dataType = getDataServiceProvider( ).getDataType( expression );
					SeriesDefinition sd = (SeriesDefinition) ( ChartUIUtil.getBaseSeriesDefinitions( getChartModel( ) ).get( 0 ) );
					if ( sd!= null && sd.getGrouping( ).isEnabled( )
							&& ( sd.getGrouping( )
									.getAggregateExpression( )
									.equals( "Count" )//$NON-NLS-1$
							|| sd.getGrouping( )
									.getAggregateExpression( )
									.equals( "DistinctCount" ) ) ) //$NON-NLS-1$
					{
						dataType = DataType.NUMERIC_LITERAL;
					}

					if ( isValidatedAxis( dataType, axis.getType( ) ) )
					{
						break;
					}

					AxisType[] axisTypes = provider.getCompatibleAxisType( series );
					for ( int i = 0; i < axisTypes.length; i++ )
					{
						if ( isValidatedAxis( dataType, axisTypes[i] ) )
						{
							axisNotification( axis, axisTypes[i] );
							axis.setType( axisTypes[i] );
							break;
						}
					}
				}
				break;
			}
		}
	}
	
	private boolean isValidatedAxis( DataType dataType, AxisType axisType )
	{
		if ( dataType == null )
		{
			return true;
		}
		else if ( ( dataType == DataType.DATE_TIME_LITERAL )
				&& ( axisType == AxisType.DATE_TIME_LITERAL ) )
		{
			return true;
		}
		else if ( ( dataType == DataType.NUMERIC_LITERAL )
				&& ( ( axisType == AxisType.LINEAR_LITERAL ) || ( axisType == AxisType.LOGARITHMIC_LITERAL ) ) )
		{
			return true;
		}
		else if ( ( dataType == DataType.TEXT_LITERAL )
				&& ( axisType == AxisType.TEXT_LITERAL ) )
		{
			return true;
		}
		return false;
	}

	private void axisNotification( Axis axis, AxisType type )
	{
		ChartAdapter.beginIgnoreNotifications( );
		{
			convertSampleData( axis, type );
			axis.setFormatSpecifier( null );

			EList markerLines = axis.getMarkerLines( );
			for ( int i = 0; i < markerLines.size( ); i++ )
			{
				( (MarkerLine) markerLines.get( i ) ).setFormatSpecifier( null );
			}

			EList markerRanges = axis.getMarkerRanges( );
			for ( int i = 0; i < markerRanges.size( ); i++ )
			{
				( (MarkerRange) markerRanges.get( i ) ).setFormatSpecifier( null );
			}
		}
		ChartAdapter.endIgnoreNotifications( );
	}

	private void convertSampleData( Axis axis, AxisType axisType )
	{
		if ( ( axis.getAssociatedAxes( ) != null )
				&& ( axis.getAssociatedAxes( ).size( ) != 0 ) )
		{
			BaseSampleData bsd = (BaseSampleData) getChartModel( ).getSampleData( )
					.getBaseSampleData( )
					.get( 0 );
			bsd.setDataSetRepresentation( ChartUIUtil.getConvertedSampleDataRepresentation( axisType,
					bsd.getDataSetRepresentation( ) ) );
		}
		else
		{
			int iStartIndex = getFirstSeriesDefinitionIndexForAxis( axis );
			int iEndIndex = iStartIndex
					+ axis.getSeriesDefinitions( ).size( );

			int iOSDSize = getChartModel( ).getSampleData( )
					.getOrthogonalSampleData( )
					.size( );
			for ( int i = 0; i < iOSDSize; i++ )
			{
				OrthogonalSampleData osd = (OrthogonalSampleData) getChartModel( ).getSampleData( )
						.getOrthogonalSampleData( )
						.get( i );
				if ( osd.getSeriesDefinitionIndex( ) >= iStartIndex
						&& osd.getSeriesDefinitionIndex( ) < iEndIndex )
				{
					osd.setDataSetRepresentation( ChartUIUtil.getConvertedSampleDataRepresentation( axisType,
							osd.getDataSetRepresentation( ) ) );
				}
			}
		}
	}
	
	private int getFirstSeriesDefinitionIndexForAxis( Axis axis )
	{
		List axisList = ( (Axis) ( (ChartWithAxes) getChartModel( ) ).getAxes( )
				.get( 0 ) ).getAssociatedAxes( );
		int index = 0;
		for ( int i = 0; i < axisList.size( ); i++ )
		{
			if ( axis.equals( (Axis) axisList.get( i ) ) )
			{
				index = i;
				break;
			}
		}
		int iTmp = 0;
		for ( int i = 0; i < index; i++ )
		{
			iTmp += ChartUIUtil.getAxisYForProcessing( (ChartWithAxes) getChartModel( ),
					i )
					.getSeriesDefinitions( )
					.size( );
		}
		return iTmp;
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

	private void updateApplyButton( )
	{
		( (ChartWizard) container ).updateApplayButton( );
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
			ChartAdapter.beginIgnoreNotifications( );
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
			ChartAdapter.endIgnoreNotifications( );
		}
		else
		{
			// Disable live preview
			ChartPreviewPainter.activateLivePreview( false );
		}
		previewPainter.renderModel( getChartModel( ) );
	}

}