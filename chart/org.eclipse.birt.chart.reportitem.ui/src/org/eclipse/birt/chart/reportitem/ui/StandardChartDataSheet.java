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

import java.util.ArrayList;
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
import org.eclipse.birt.chart.reportitem.ChartReportItemUtil;
import org.eclipse.birt.chart.reportitem.ui.dialogs.ChartColumnBindingDialog;
import org.eclipse.birt.chart.reportitem.ui.dialogs.ExtendedItemFilterDialog;
import org.eclipse.birt.chart.reportitem.ui.dialogs.ReportItemParametersDialog;
import org.eclipse.birt.chart.reportitem.ui.i18n.Messages;
import org.eclipse.birt.chart.ui.swt.ColorPalette;
import org.eclipse.birt.chart.ui.swt.CustomPreviewTable;
import org.eclipse.birt.chart.ui.swt.DataDefinitionTextManager;
import org.eclipse.birt.chart.ui.swt.DefaultChartDataSheet;
import org.eclipse.birt.chart.ui.swt.SimpleTextTransfer;
import org.eclipse.birt.chart.ui.swt.wizard.ChartAdapter;
import org.eclipse.birt.chart.ui.swt.wizard.ChartWizard;
import org.eclipse.birt.chart.ui.util.ChartHelpContextIds;
import org.eclipse.birt.chart.ui.util.ChartUIUtil;
import org.eclipse.birt.core.data.ExpressionUtil;
import org.eclipse.birt.core.ui.frameworks.taskwizard.WizardBase;
import org.eclipse.birt.report.designer.internal.ui.dialogs.ExpressionFilter;
import org.eclipse.birt.report.designer.internal.ui.views.ViewsTreeProvider;
import org.eclipse.birt.report.designer.ui.actions.NewDataSetAction;
import org.eclipse.birt.report.designer.ui.dialogs.ColumnBindingDialog;
import org.eclipse.birt.report.designer.ui.dialogs.ExpressionProvider;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.metadata.IClassInfo;
import org.eclipse.birt.report.model.api.olap.CubeHandle;
import org.eclipse.birt.report.model.api.olap.DimensionHandle;
import org.eclipse.birt.report.model.api.olap.LevelHandle;
import org.eclipse.birt.report.model.api.olap.MeasureHandle;
import org.eclipse.emf.common.util.EList;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DragSourceListener;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
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

	/**
	 * The field indicates if any operation in this class cause some exception
	 * or error.
	 */
	private boolean fbException = false;

	private Button btnInherit = null;
	private Button btnUseData = null;
	private Combo cmbDataItems = null;
	private Button btnNewData = null;

	private StackLayout stackLayout = null;
	private Composite cmpStack = null;
	private Composite cmpCubeTree = null;
	private Composite cmpDataPreview = null;

	private CustomPreviewTable tablePreview = null;
	private TreeViewer cubeTreeViewer = null;

	private Button btnFilters = null;
	private Button btnParameters = null;
	private Button btnBinding = null;

	private static final int SELECT_NONE = 0;
	private static final int SELECT_NEXT = 1;
	private static final int SELECT_DATA_SET = 2;
	private static final int SELECT_DATA_CUBE = 3;
	private static final int SELECT_REPORT_ITEM = 4;
	private List selectDataTypes = new ArrayList( );

	public StandardChartDataSheet( ExtendedItemHandle itemHandle,
			ReportDataServiceProvider dataProvider )
	{
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
		btnNewData.setEnabled( btnUseData.getSelection( ) );
		if ( isCubeMode( ) )
		{
			btnFilters.setEnabled( false );
			// btnFilters.setEnabled( getDataServiceProvider(
			// ).isInvokingSupported( ) );
			btnBinding.setEnabled( getDataServiceProvider( ).isInvokingSupported( ) );
			btnParameters.setEnabled( false );
		}
		else
		{
			btnFilters.setEnabled( hasDataSet( )
					&& getDataServiceProvider( ).isInvokingSupported( ) );
			// Bugzilla#177704 Chart inheriting data from container doesn't
			// support parameters due to limitation in DtE
			btnParameters.setEnabled( getDataServiceProvider( ).getBoundDataSet( ) != null
					&& getDataServiceProvider( ).isInvokingSupported( ) );
			btnBinding.setEnabled( hasDataSet( )
					&& getDataServiceProvider( ).isInvokingSupported( ) );
		}
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
		cmpStack = new Composite( parent, SWT.NONE );
		cmpStack.setLayoutData( new GridData( GridData.FILL_BOTH ) );
		stackLayout = new StackLayout( );
		stackLayout.marginHeight = 0;
		stackLayout.marginWidth = 0;
		cmpStack.setLayout( stackLayout );

		cmpCubeTree = ChartUIUtil.createCompositeWrapper( cmpStack );
		cmpDataPreview = ChartUIUtil.createCompositeWrapper( cmpStack );

		Label label = new Label( cmpCubeTree, SWT.NONE );
		{
			label.setText( Messages.getString( "StandardChartDataSheet.Label.CubeTree" ) ); //$NON-NLS-1$
			label.setFont( JFaceResources.getBannerFont( ) );
		}

		Label description = new Label( cmpCubeTree, SWT.WRAP );
		{
			GridData gd = new GridData( GridData.FILL_HORIZONTAL );
			description.setLayoutData( gd );
			description.setText( Messages.getString( "StandardChartDataSheet.Label.DragCube" ) ); //$NON-NLS-1$
		}

		cubeTreeViewer = new TreeViewer( cmpCubeTree, SWT.SINGLE
				| SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER );
		cubeTreeViewer.getTree( )
				.setLayoutData( new GridData( GridData.FILL_BOTH ) );
		( (GridData) cubeTreeViewer.getTree( ).getLayoutData( ) ).heightHint = 120;
		ViewsTreeProvider provider = new ViewsTreeProvider( );
		cubeTreeViewer.setLabelProvider( provider );
		cubeTreeViewer.setContentProvider( provider );
		cubeTreeViewer.setInput( getCube( ) );

		final DragSource dragSource = new DragSource( cubeTreeViewer.getTree( ),
				DND.DROP_COPY );
		dragSource.setTransfer( new Transfer[]{
			SimpleTextTransfer.getInstance( )
		} );
		dragSource.addDragListener( new DragSourceListener( ) {

			private String text = null;

			public void dragFinished( DragSourceEvent event )
			{
				// TODO Auto-generated method stub

			}

			public void dragSetData( DragSourceEvent event )
			{
				event.data = text;
			}

			public void dragStart( DragSourceEvent event )
			{
				text = createCubeExpression( );
				if ( text == null )
				{
					event.doit = false;
				}
			}
		} );

		cubeTreeViewer.getTree( ).addListener( SWT.MouseDown, new Listener( ) {

			public void handleEvent( Event event )
			{
				if ( event.button == 3 && event.widget instanceof Tree )
				{
					Tree tree = (Tree) event.widget;
					TreeItem treeItem = tree.getSelection( )[0];
					if ( treeItem.getData( ) instanceof LevelHandle
							|| treeItem.getData( ) instanceof MeasureHandle )
					{
						tree.setMenu( createMenuManager( treeItem.getData( ) ).createContextMenu( tree ) );
						tree.getMenu( ).setVisible( true );
					}
					else
					{
						tree.setMenu( null );
					}
				}
			}
		} );

		label = new Label( cmpDataPreview, SWT.NONE );
		{
			label.setText( Messages.getString( "StandardChartDataSheet.Label.DataPreview" ) ); //$NON-NLS-1$
			label.setFont( JFaceResources.getBannerFont( ) );
		}

		description = new Label( cmpDataPreview, SWT.WRAP );
		{
			GridData gd = new GridData( GridData.FILL_HORIZONTAL );
			description.setLayoutData( gd );
			description.setText( Messages.getString( "StandardChartDataSheet.Label.ToBindADataColumn" ) ); //$NON-NLS-1$
		}

		tablePreview = new CustomPreviewTable( cmpDataPreview, SWT.SINGLE
				| SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION );
		{
			GridData gridData = new GridData( GridData.FILL_BOTH );
			gridData.widthHint = 400;
			gridData.heightHint = 120;
			tablePreview.setLayoutData( gridData );
			tablePreview.setHeaderAlignment( SWT.LEFT );
			tablePreview.addListener( CustomPreviewTable.MOUSE_RIGHT_CLICK_TYPE,
					this );
		}

		updateDragDataSource( );
		return cmpStack;
	}

	private void updateDragDataSource( )
	{
		if ( isCubeMode( ) )
		{
			stackLayout.topControl = cmpCubeTree;
			cubeTreeViewer.setInput( getCube( ) );
		}
		else
		{
			stackLayout.topControl = cmpDataPreview;
			refreshTablePreview( );
		}
		cmpStack.layout( );
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
			gd.verticalSpan = 2;
			compRadios.setLayoutData( gd );
		}

		btnInherit = new Button( compRadios, SWT.RADIO );
		btnInherit.setText( Messages.getString( "StandardChartDataSheet.Label.UseReportData" ) ); //$NON-NLS-1$
		btnInherit.addListener( SWT.Selection, this );

		btnUseData = new Button( compRadios, SWT.RADIO );
		btnUseData.setText( Messages.getString( "StandardChartDataSheet.Label.UseDataSet" ) ); //$NON-NLS-1$
		btnUseData.addListener( SWT.Selection, this );

		new Label( cmpDetail, SWT.NONE );
		new Label( cmpDetail, SWT.NONE );

		cmbDataItems = new Combo( cmpDetail, SWT.DROP_DOWN | SWT.READ_ONLY );
		cmbDataItems.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
		cmbDataItems.addListener( SWT.Selection, this );

		btnNewData = new Button( cmpDetail, SWT.NONE );
		{
			btnNewData.setText( Messages.getString( "StandardChartDataSheet.Label.CreateNew" ) ); //$NON-NLS-1$
			btnNewData.setToolTipText( Messages.getString( "StandardChartDataSheet.Tooltip.CreateNewDataset" ) ); //$NON-NLS-1$
			btnNewData.addListener( SWT.Selection, this );
		}

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
		ExtendedItemHandle handle = getItemHandle( );
		handle.getModuleHandle( ).getCommandStack( ).startTrans( null );
		ExtendedItemFilterDialog page = new ExtendedItemFilterDialog( handle );
		int openStatus = page.open( );
		if ( openStatus == Window.OK )
		{
			handle.getModuleHandle( ).getCommandStack( ).commit( );
		}
		else
		{
			handle.getModuleHandle( ).getCommandStack( ).rollback( );
		}

		return openStatus;
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
		
		ExtendedItemHandle handle = getItemHandle();
		handle.getModuleHandle( ).getCommandStack( ).startTrans( null );
		page.setInput( handle );
		
		ExpressionProvider ep = new ExpressionProvider( getItemHandle( ) );
		ep.addFilter( new ExpressionFilter( ) {

			public boolean select( Object parentElement, Object element )
			{
				// Remove unsupported expression. See bugzilla#132768
				return !( parentElement.equals( ExpressionProvider.BIRT_OBJECTS ) &&
						element instanceof IClassInfo && ( (IClassInfo) element ).getName( )
						.equals( "Total" ) ); //$NON-NLS-1$
			}
		} );
		page.setExpressionProvider( ep );

		int openStatus = page.open( );
		if ( openStatus == Window.OK )
		{
			handle.getModuleHandle( ).getCommandStack( ).commit( );
		}
		else
		{
			handle.getModuleHandle( ).getCommandStack( ).rollback( );
		}

		return openStatus;
	}

	private void initDataSelector( )
	{
		// create Combo items
		cmbDataItems.setItems( createDataComboItems( ) );

		// Select report item reference
		// Since handle may have data set or data cube besides reference, always
		// check reference first
		String sItemRef = getDataServiceProvider( ).getReportItemReference( );
		if ( sItemRef != null )
		{
			btnUseData.setSelection( true );
			cmbDataItems.setText( sItemRef );
			return;
		}

		// Select data set
		String sDataSet = getDataServiceProvider( ).getBoundDataSet( );
		if ( sDataSet != null )
		{
			btnUseData.setSelection( true );
			cmbDataItems.setText( sDataSet );
			if ( sDataSet != null )
			{
				switchDataTable( );
			}
			return;
		}

		// Select data cube
		String sDataCube = getDataServiceProvider( ).getDataCube( );
		if ( sDataCube != null )
		{
			btnUseData.setSelection( true );
			cmbDataItems.setText( sDataCube );
			return;
		}

		btnInherit.setSelection( true );
		cmbDataItems.select( 0 );
		cmbDataItems.setEnabled( false );
		// Initializes column bindings from container
		getDataServiceProvider( ).setDataSet( null );
		String reportDataSet = getDataServiceProvider( ).getReportDataSet( );
		if ( reportDataSet != null )
		{
			switchDataTable( );
		}

		// select reference item
		// selectItemRef( );
		// if ( cmbReferences.getSelectionIndex( ) > 0 )
		// {
		// cmbDataSet.setEnabled( false );
		// btnUseReference.setSelection( true );
		// btnUseReportData.setSelection( false );
		// btnUseDataSet.setSelection( false );
		// }
		// else
		// {
		// cmbReferences.setEnabled( false );
		// }
		//
		// String dataCube = getDataServiceProvider( ).getDataCube( );
		// if ( dataCube != null )
		// {
		// cmbCubes.setText( dataCube );
		// btnUseReference.setSelection( false );
		// btnUseReportData.setSelection( false );
		// btnUseDataSet.setSelection( false );
		// btnUseCubes.setSelection( true );
		// }
		// else
		// {
		// cmbCubes.select( 0 );
		// }
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
						header.setMenu( createMenuManager( event.data ).createContextMenu( tablePreview ) );
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
				if ( event.widget == btnInherit )
				{
					ColorPalette.getInstance( ).restore( );

					// Skip when selection is false
					if ( !btnInherit.getSelection( ) )
					{
						return;
					}
					getDataServiceProvider( ).setReportItemReference( null );
					getDataServiceProvider( ).setDataSet( null );
					switchDataSet( null );

					cmbDataItems.select( 0 );
					cmbDataItems.setEnabled( false );
					setEnabledForButtons( );
					updateDragDataSource( );
				}
				else if ( event.widget == btnUseData )
				{
					// Skip when selection is false
					if ( !btnUseData.getSelection( ) )
					{
						return;
					}

					getDataServiceProvider( ).setReportItemReference( null );
					selectDataSet( );
					cmbDataItems.setEnabled( true );
					setEnabledForButtons( );
					updateDragDataSource( );
				}
				else if ( event.widget == cmbDataItems )
				{
					ColorPalette.getInstance( ).restore( );
					int selectedIndex = cmbDataItems.getSelectionIndex( );
					Integer selectState = (Integer) selectDataTypes.get( selectedIndex );
					switch ( selectState.intValue( ) )
					{
						case SELECT_NONE :
							// Inherit data from container
							btnInherit.setSelection( true );
							btnUseData.setSelection( false );
							btnInherit.notifyListeners( SWT.Selection,
									new Event( ) );
							break;
						case SELECT_NEXT :
							selectedIndex++;
							selectState = (Integer) selectDataTypes.get( selectedIndex );
							cmbDataItems.select( selectedIndex );
							break;
					}
					switch ( selectState.intValue( ) )
					{
						case SELECT_DATA_SET :
							if ( getDataServiceProvider( ).getBoundDataSet( ) != null
									&& getDataServiceProvider( ).getBoundDataSet( )
											.equals( cmbDataItems.getText( ) ) )
							{
								return;
							}
							getDataServiceProvider( ).setDataSet( cmbDataItems.getText( ) );
							switchDataSet( cmbDataItems.getText( ) );
							setEnabledForButtons( );
							updateDragDataSource( );
							break;
						case SELECT_DATA_CUBE :
							getDataServiceProvider( ).setDataCube( cmbDataItems.getText( ) );
							updateDragDataSource( );
							setEnabledForButtons( );
							break;
						case SELECT_REPORT_ITEM :
							if ( cmbDataItems.getText( )
									.equals( getDataServiceProvider( ).getReportItemReference( ) ) )
							{
								return;
							}
							getDataServiceProvider( ).setReportItemReference( cmbDataItems.getText( ) );
							// selectDataSet( );
							// switchDataSet( cmbDataItems.getText( ) );
							setEnabledForButtons( );
							updateDragDataSource( );
							break;
					}

				}
				// else if ( event.widget == btnUseReference )
				// {
				// // Skip when selection is false
				// if ( !btnUseReference.getSelection( ) )
				// {
				// return;
				// }
				// cmbDataSet.setEnabled( false );
				// cmbReferences.setEnabled( true );
				// selectItemRef( );
				// setEnabledForButtons( );
				// }
				// else if ( event.widget == cmbReferences )
				// {
				// if ( cmbReferences.getSelectionIndex( ) == 0 )
				// {
				// if ( getDataServiceProvider( ).getReportItemReference( ) ==
				// null )
				// {
				// return;
				// }
				// getDataServiceProvider( ).setReportItemReference( null );
				//
				// // Auto select the data set
				// selectDataSet( );
				// cmbReferences.setEnabled( false );
				// cmbDataSet.setEnabled( true );
				// btnUseReference.setSelection( false );
				// btnUseDataSet.setSelection( true );
				// }
				// else
				// {
				// if ( cmbReferences.getText( )
				// .equals( getDataServiceProvider( ).getReportItemReference( )
				// ) )
				// {
				// return;
				// }
				// getDataServiceProvider( ).setReportItemReference(
				// cmbReferences.getText( ) );
				// selectDataSet( );
				// }
				// switchDataSet( cmbDataSet.getText( ) );
				// setEnabledForButtons( );
				// }
				else if ( event.widget == btnNewData )
				{
					// Bring up the dialog to create a dataset
					int result = invokeNewDataSet( );
					if ( result == Window.CANCEL )
					{
						return;
					}

					String currentDataSet = cmbDataItems.getText( );
					cmbDataItems.removeAll( );
					cmbDataItems.setItems( createDataComboItems( ) );
					cmbDataItems.setText( currentDataSet );
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
			cmbDataItems.select( 0 );
		}
		else
		{
			cmbDataItems.setText( currentDS );
		}
	}

	private void refreshTablePreview( )
	{
		if ( dataProvider.getDataSetFromHandle( ) == null )
		{
			return;
		}
		tablePreview.clearContents( );
		switchDataTable( );
		tablePreview.layout( );
	}

	private void switchDataSet( String datasetName ) throws ChartException
	{
		if ( isCubeMode( ) )
		{
			return;
		}
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
		if ( isCubeMode( ) )
		{
			return;
		}
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
		if ( isCubeMode( ) )
		{
			return;
		}
		// Reset column color
		for ( int i = 0; i < tablePreview.getColumnNumber( ); i++ )
		{
			tablePreview.setColumnColor( i,
					ColorPalette.getInstance( )
							.getColor( ExpressionUtil.createJSRowExpression( tablePreview.getColumnHeading( i ) ) ) );
		}
	}

	private void manageColorAndQuery( Query query, String expr )
	{
		// If it's not used any more, remove color binding
		if ( DataDefinitionTextManager.getInstance( )
				.getNumberOfSameDataDefinition( query.getDefinition( ) ) == 0 )
		{
			ColorPalette.getInstance( ).retrieveColor( query.getDefinition( ) );
		}
		query.setDefinition( expr );
		DataDefinitionTextManager.getInstance( ).updateText( query );
		// Reset table column color
		refreshTableColor( );
		// Refresh all data definition text
		DataDefinitionTextManager.getInstance( ).refreshAll( );
	}

	class CategoryXAxisAction extends Action
	{

		String expr;

		CategoryXAxisAction( String expr )
		{
			super( getBaseSeriesTitle( getChartModel( ) ) );
			this.expr = expr;
		}

		public void run( )
		{
			Query query = ( (Query) ( (SeriesDefinition) ChartUIUtil.getBaseSeriesDefinitions( getChartModel( ) )
					.get( 0 ) ).getDesignTimeSeries( )
					.getDataDefinition( )
					.get( 0 ) );
			manageColorAndQuery( query, expr );
		}
	}

	class GroupYSeriesAction extends Action
	{

		Query query;
		String expr;

		GroupYSeriesAction( Query query, String expr )
		{
			super( getGroupSeriesTitle( getChartModel( ) ) );
			this.query = query;
			this.expr = expr;
		}

		public void run( )
		{
			// Use the first group, and copy to the all groups
			ChartAdapter.beginIgnoreNotifications( );
			ChartUIUtil.setAllGroupingQueryExceptFirst( getChartModel( ), expr );
			ChartAdapter.endIgnoreNotifications( );

			manageColorAndQuery( query, expr );
		}
	}

	class ValueYSeriesAction extends Action
	{

		Query query;
		String expr;

		ValueYSeriesAction( Query query, String expr )
		{
			super( getOrthogonalSeriesTitle( getChartModel( ) ) );
			this.query = query;
			this.expr = expr;
		}

		public void run( )
		{
			manageColorAndQuery( query, expr );
		}
	}

	class HeaderShowAction extends Action
	{

		HeaderShowAction( String header )
		{
			super( header );
			setEnabled( false );
		}
	}

	ExtendedItemHandle getItemHandle( )
	{
		return this.itemHandle;
	}

	ReportDataServiceProvider getDataServiceProvider( )
	{
		return this.dataProvider;
	}

	private MenuManager createMenuManager( final Object data )
	{
		MenuManager menuManager = new MenuManager( );
		menuManager.setRemoveAllWhenShown( true );
		menuManager.addMenuListener( new IMenuListener( ) {

			public void menuAboutToShow( IMenuManager manager )
			{
				if ( data instanceof Integer )
				{
					// Menu for table
					addMenu( manager,
							new HeaderShowAction( tablePreview.getCurrentColumnHeading( ) ) );
					String expr = ExpressionUtil.createJSRowExpression( tablePreview.getCurrentColumnHeading( ) );
					addMenu( manager,
							getBaseSeriesMenu( getChartModel( ), expr ) );
					addMenu( manager,
							getOrthogonalSeriesMenu( getChartModel( ), expr ) );
					addMenu( manager, getGroupSeriesMenu( getChartModel( ),
							expr ) );
				}
				else if ( data instanceof MeasureHandle )
				{
					// Menu for Measure
					String expr = createCubeExpression( );
					addMenu( manager,
							getOrthogonalSeriesMenu( getChartModel( ), expr ) );
				}
				else if ( data instanceof LevelHandle )
				{
					// Menu for Level
					String expr = createCubeExpression( );
					addMenu( manager,
							getBaseSeriesMenu( getChartModel( ), expr ) );
					addMenu( manager, getGroupSeriesMenu( getChartModel( ),
							expr ) );
				}
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
		return menuManager;
	}

	private Object getBaseSeriesMenu( Chart chart, String expr )
	{
		EList sds = ChartUIUtil.getBaseSeriesDefinitions( chart );
		if ( sds.size( ) == 1 )
		{
			return new CategoryXAxisAction( expr );
		}
		return null;
	}

	private Object getGroupSeriesMenu( Chart chart, String expr )
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
				IAction action = new GroupYSeriesAction( sd.getQuery( ), expr );
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

	private Object getOrthogonalSeriesMenu( Chart chart, String expr )
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
						IAction action = new ValueYSeriesAction( (Query) dataDefns.get( j ),
								expr );
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
						IAction action = new ValueYSeriesAction( (Query) dataDefns.get( j ),
								expr );
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
						IAction action = new ValueYSeriesAction( (Query) dataDefns.get( j ),
								expr );
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
						IAction action = new ValueYSeriesAction( (Query) dataDefns.get( j ),
								expr );
						action.setText( ChartUIUtil.getGanttTitle( j )
								+ Messages.getString( "StandardChartDataSheet.Label.Component" ) ); //$NON-NLS-1$
						secondManager.add( action );
					}
				}
				else
				{
					IAction action = new ValueYSeriesAction( (Query) dataDefns.get( 0 ),
							expr );
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

	private boolean isCubeMode( )
	{
		return ChartReportItemUtil.getBindingCube( itemHandle ) != null;
	}

	private CubeHandle getCube( )
	{
		return ChartReportItemUtil.getBindingCube( itemHandle );
	}

	/**
	 * Creates the cube expression
	 * 
	 * @return expression
	 */
	private String createCubeExpression( )
	{
		if ( cubeTreeViewer == null )
		{
			return null;
		}
		TreeItem[] selection = cubeTreeViewer.getTree( ).getSelection( );
		String expr = null;
		if ( selection.length > 0 )
		{
			TreeItem treeItem = selection[0];
			if ( treeItem.getData( ) instanceof LevelHandle )
			{
				TreeItem dimensionItem = treeItem.getParentItem( );
				while ( !( dimensionItem.getData( ) instanceof DimensionHandle ) )
				{
					dimensionItem = dimensionItem.getParentItem( );
				}
				expr = ExpressionUtil.createJSDimensionExpression( dimensionItem.getText( ),
						treeItem.getText( ) );
			}
			else if ( treeItem.getData( ) instanceof MeasureHandle )
			{
				expr = ExpressionUtil.createJSMeasureExpression( treeItem.getText( ) );
			}
		}
		return expr;
	}

	private String[] createDataComboItems( )
	{
		List items = new ArrayList( );
		selectDataTypes.clear( );

		items.add( ReportDataServiceProvider.OPTION_NONE );
		selectDataTypes.add( new Integer( SELECT_NONE ) );

		String[] dataSets = getDataServiceProvider( ).getAllDataSets( );
		if ( dataSets.length > 0 )
		{
			items.add( Messages.getString( "StandardChartDataSheet.Combo.DataSets" ) ); //$NON-NLS-1$
			selectDataTypes.add( new Integer( SELECT_NEXT ) );
			for ( int i = 0; i < dataSets.length; i++ )
			{
				items.add( dataSets[i] );
				selectDataTypes.add( new Integer( SELECT_DATA_SET ) );
			}
		}

		String[] dataCubes = getDataServiceProvider( ).getAllDataCubes( );
		if ( dataCubes.length > 0 )
		{
			items.add( Messages.getString( "StandardChartDataSheet.Combo.DataCubes" ) ); //$NON-NLS-1$
			selectDataTypes.add( new Integer( SELECT_NEXT ) );
			for ( int i = 0; i < dataCubes.length; i++ )
			{
				items.add( dataCubes[i] );
				selectDataTypes.add( new Integer( SELECT_DATA_CUBE ) );
			}
		}

		String[] dataRefs = getDataServiceProvider( ).getAllReportItemReferences( );
		if ( dataRefs.length > 0 )
		{
			items.add( Messages.getString( "StandardChartDataSheet.Combo.ReportItems" ) ); //$NON-NLS-1$
			selectDataTypes.add( new Integer( SELECT_NEXT ) );
			for ( int i = 0; i < dataRefs.length; i++ )
			{
				items.add( dataRefs[i] );
				selectDataTypes.add( new Integer( SELECT_REPORT_ITEM ) );
			}
		}
		return (String[]) items.toArray( new String[items.size( )] );
	}
}
