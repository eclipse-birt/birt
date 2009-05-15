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
import org.eclipse.birt.chart.model.data.impl.QueryImpl;
import org.eclipse.birt.chart.model.type.BubbleSeries;
import org.eclipse.birt.chart.model.type.DifferenceSeries;
import org.eclipse.birt.chart.model.type.GanttSeries;
import org.eclipse.birt.chart.model.type.StockSeries;
import org.eclipse.birt.chart.plugin.ChartEnginePlugin;
import org.eclipse.birt.chart.reportitem.ChartReportItemUtil;
import org.eclipse.birt.chart.reportitem.ChartXTabUtil;
import org.eclipse.birt.chart.reportitem.ui.dialogs.ChartColumnBindingDialog;
import org.eclipse.birt.chart.reportitem.ui.dialogs.ExtendedItemFilterDialog;
import org.eclipse.birt.chart.reportitem.ui.dialogs.ReportItemParametersDialog;
import org.eclipse.birt.chart.reportitem.ui.i18n.Messages;
import org.eclipse.birt.chart.reportitem.ui.views.attributes.provider.ChartCubeFilterHandleProvider;
import org.eclipse.birt.chart.reportitem.ui.views.attributes.provider.ChartFilterProviderDelegate;
import org.eclipse.birt.chart.ui.swt.ColorPalette;
import org.eclipse.birt.chart.ui.swt.ColumnBindingInfo;
import org.eclipse.birt.chart.ui.swt.CustomPreviewTable;
import org.eclipse.birt.chart.ui.swt.DataDefinitionTextManager;
import org.eclipse.birt.chart.ui.swt.DefaultChartDataSheet;
import org.eclipse.birt.chart.ui.swt.SimpleTextTransfer;
import org.eclipse.birt.chart.ui.swt.interfaces.IChartDataSheet;
import org.eclipse.birt.chart.ui.swt.interfaces.IDataServiceProvider;
import org.eclipse.birt.chart.ui.swt.interfaces.ISelectDataComponent;
import org.eclipse.birt.chart.ui.swt.wizard.ChartAdapter;
import org.eclipse.birt.chart.ui.swt.wizard.ChartWizard;
import org.eclipse.birt.chart.ui.util.ChartUIConstants;
import org.eclipse.birt.chart.ui.util.ChartUIUtil;
import org.eclipse.birt.chart.ui.util.UIHelper;
import org.eclipse.birt.chart.util.ChartUtil;
import org.eclipse.birt.core.data.ExpressionUtil;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.ui.frameworks.taskwizard.WizardBase;
import org.eclipse.birt.report.designer.internal.ui.data.DataService;
import org.eclipse.birt.report.designer.internal.ui.views.ViewsTreeProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.AbstractFilterHandleProvider;
import org.eclipse.birt.report.designer.ui.cubebuilder.action.NewCubeAction;
import org.eclipse.birt.report.designer.ui.dialogs.ColumnBindingDialog;
import org.eclipse.birt.report.designer.ui.dialogs.ExpressionProvider;
import org.eclipse.birt.report.designer.ui.expressions.ExpressionFilter;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabReportItemHandle;
import org.eclipse.birt.report.model.api.ComputedColumnHandle;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.metadata.IClassInfo;
import org.eclipse.birt.report.model.api.olap.CubeHandle;
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
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DragSourceListener;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.swt.widgets.Widget;

/**
 * Data sheet implementation for Standard Chart
 */

public class StandardChartDataSheet extends DefaultChartDataSheet implements
		Listener
{

	private static final String KEY_PREVIEW_DATA = "Preview Data"; //$NON-NLS-1$
	final private ExtendedItemHandle itemHandle;
	final private ReportDataServiceProvider dataProvider;

	private Button btnInherit = null;
	private Button btnUseData = null;
	private boolean bIsInheritSelected = true;

	private CCombo cmbInherit = null;
	private CCombo cmbDataItems = null;

	private StackLayout stackLayout = null;
	private Composite cmpStack = null;
	private Composite cmpCubeTree = null;
	private Composite cmpDataPreview = null;
	private Composite cmpColumnsList = null;

	private CustomPreviewTable tablePreview = null;
	private TreeViewer cubeTreeViewer = null;

	private Button btnFilters = null;
	private Button btnParameters = null;
	private Button btnBinding = null;
	private String currentData = null;
	private String previousData = null;

	public static final int SELECT_NONE = 1;
	public static final int SELECT_NEXT = 2;
	public static final int SELECT_DATA_SET = 4;
	public static final int SELECT_DATA_CUBE = 8;
	public static final int SELECT_REPORT_ITEM = 16;
	public static final int SELECT_NEW_DATASET = 32;
	public static final int SELECT_NEW_DATACUBE = 64;
	
	private final int iSupportedDataItems;

	private List<Integer> selectDataTypes = new ArrayList<Integer>( );
	private Button btnShowDataPreviewA;
	private Button btnShowDataPreviewB;
	private TableViewer tableViewerColumns;
	private Label columnListDescription;

	public StandardChartDataSheet( ExtendedItemHandle itemHandle,
			ReportDataServiceProvider dataProvider, int iSupportedDataItems )
	{
		this.itemHandle = itemHandle;
		this.dataProvider = dataProvider;
		this.iSupportedDataItems = iSupportedDataItems;
		addListener( this );
	}

	public StandardChartDataSheet( ExtendedItemHandle itemHandle,
			ReportDataServiceProvider dataProvider )
	{
		this( itemHandle, dataProvider, 0 );
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
		if ( isCubeMode( ) )
		{
			// getDataServiceProvider( ).checkState(
			// IDataServiceProvider.SHARE_QUERY )
			boolean disabled = getDataServiceProvider( ).isInXTabAggrCell( )
					|| getDataServiceProvider( ).isInXTabMeasureCell( );
			btnFilters.setEnabled( !disabled );
			btnBinding.setEnabled( getDataServiceProvider( ).isInvokingSupported( )
					|| getDataServiceProvider( ).isSharedBinding( ) );
			btnParameters.setEnabled( false );
		}
		else
		{
			 btnFilters.setEnabled( hasDataSet( ) );
	
			// Bugzilla#177704 Chart inheriting data from container doesn't
			// support parameters due to limitation in DtE
			btnParameters.setEnabled( getDataServiceProvider( ).getBoundDataSet( ) != null
					&& getDataServiceProvider( ).isInvokingSupported( ) );
			btnBinding.setEnabled( hasDataSet( )
					&& ( getDataServiceProvider( ).isInvokingSupported( ) || getDataServiceProvider( ).isSharedBinding( ) ) );
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
		notifyListeners( event );
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
		
		createColumnsViewerArea( cmpStack );
		
		Label label = new Label( cmpCubeTree, SWT.NONE );
		{
			label.setText( Messages.getString( "StandardChartDataSheet.Label.CubeTree" ) ); //$NON-NLS-1$
			label.setFont( JFaceResources.getBannerFont( ) );
		}

		if ( !dataProvider.isInXTabMeasureCell( )
				&& !dataProvider.isInMultiView( ) )
		{
			// No description if dnd is disabled
			Label description = new Label( cmpCubeTree, SWT.WRAP );
			{
				GridData gd = new GridData( GridData.FILL_HORIZONTAL );
				description.setLayoutData( gd );
				description.setText( Messages.getString( "StandardChartDataSheet.Label.DragCube" ) ); //$NON-NLS-1$
			}
		}

		cubeTreeViewer = new TreeViewer( cmpCubeTree, SWT.SINGLE
				| SWT.H_SCROLL
				| SWT.V_SCROLL
				| SWT.BORDER );
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
						if ( dataProvider.checkState( IDataServiceProvider.SHARE_CHART_QUERY ))
						{
							tree.setMenu( null );
						}
						else
						{
							tree.setMenu( createMenuManager( treeItem.getData( ) ).createContextMenu( tree ) );
						// tree.getMenu( ).setVisible( true );
						}
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

		if ( !dataProvider.isInXTabMeasureCell( )
				&& !dataProvider.isInMultiView( ) )
		{
			// No description if dnd is disabled
			Label description = new Label( cmpDataPreview, SWT.WRAP );
			{
				GridData gd = new GridData( GridData.FILL_HORIZONTAL );
				description.setLayoutData( gd );
				description.setText( Messages.getString( "StandardChartDataSheet.Label.ToBindADataColumn" ) ); //$NON-NLS-1$
			}
		}
		
		btnShowDataPreviewA = new Button( cmpDataPreview, SWT.CHECK );
		btnShowDataPreviewA.setText( Messages.getString("StandardChartDataSheet.Label.ShowDataPreview") ); //$NON-NLS-1$
		btnShowDataPreviewA.addListener( SWT.Selection, this );
		
		tablePreview = new CustomPreviewTable( cmpDataPreview, SWT.SINGLE
				| SWT.H_SCROLL
				| SWT.V_SCROLL
				| SWT.FULL_SELECTION );
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

	private void createColumnsViewerArea( Composite parent )
	{
		cmpColumnsList = ChartUIUtil.createCompositeWrapper(  parent );
		
		Label label = new Label( cmpColumnsList, SWT.NONE );
		{
			label.setText( Messages.getString( "StandardChartDataSheet.Label.DataPreview" ) ); //$NON-NLS-1$
			label.setFont( JFaceResources.getBannerFont( ) );
		}

		if ( !dataProvider.isInXTabMeasureCell( )
				&& !dataProvider.isInMultiView( ) )
		{
			columnListDescription = new Label( cmpColumnsList, SWT.WRAP );
			{
				GridData gd = new GridData( GridData.FILL_HORIZONTAL );
				columnListDescription.setLayoutData( gd );
				columnListDescription.setText( Messages.getString( "StandardChartDataSheet.Label.ToBindADataColumn" ) ); //$NON-NLS-1$
			}
		}
		
		btnShowDataPreviewB = new Button( cmpColumnsList, SWT.CHECK );
		btnShowDataPreviewB.setText( Messages.getString("StandardChartDataSheet.Label.ShowDataPreview") ); //$NON-NLS-1$
		btnShowDataPreviewB.addListener( SWT.Selection, this );
		
		// Add a list to display all columns.
		final Table table = new Table( cmpColumnsList, SWT.SINGLE
				| SWT.BORDER
				| SWT.H_SCROLL
				| SWT.V_SCROLL
				| SWT.FULL_SELECTION);
		GridData gd = new GridData( GridData.FILL_BOTH );
		table.setLayoutData( gd );
		table.setLinesVisible( true );
		tableViewerColumns = new TableViewer( table );
		tableViewerColumns.setUseHashlookup( true );
		new TableColumn( table, SWT.LEFT );
		
		table.addMouseMoveListener( new MouseMoveListener() {

			public void mouseMove( MouseEvent e )
			{
				if ( !dataProvider.isLivePreviewEnabled( ) )
				{
					table.setToolTipText( null );
					return;
				}
				
				String tooltip = null;
				TableItem item = ((Table)e.widget).getItem( new Point( e.x, e.y ) );
				if ( item != null )
				{
					List<Object[]> data = (List<Object[]> ) tableViewerColumns.getData( KEY_PREVIEW_DATA );
					if ( data != null )
					{
						StringBuilder sb = new StringBuilder( );

						int index = ( (Table) e.widget ).indexOf( item );
						int i = 0;
						for ( ; i < data.size( ); i++ )
						{
							if ( sb.length( ) > 45 )
							{
								break;
							}
							if ( data.get( i )[index] != null )
							{
								if ( i != 0 )
									sb.append( "; " ); //$NON-NLS-1$
								sb.append( String.valueOf( data.get( i )[index] ) );
							}
						}
						
						if ( i == 1 && sb.length( ) > 45 )
						{
							sb = new StringBuilder( sb.substring( 0, 45 ) );
							sb.append( "..." );//$NON-NLS-1$
						}
						else if ( i < data.size( ) )
						{
							sb.append( ";..." ); //$NON-NLS-1$
						}

						tooltip = sb.toString( );
					}
					
				}
				table.setToolTipText( tooltip );
				
			}} );
		
		table.addMouseListener( new MouseAdapter() {
			public void mouseDown( MouseEvent e )
			{
				if ( e.button == 3 )
				{
					if ( isCubeMode( ) )
					{
						// share cube
						table.setMenu( null );
					}
					else
					{
						TableItem item = ( (Table) e.widget ).getItem( new Point( e.x,
								e.y ) );
						if ( item == null )
						{
							tableViewerColumns.getTable( ).select( -1 );
						}
						// Bind context menu to each header button
						boolean isSharingChart = dataProvider.checkState( IDataServiceProvider.SHARE_CHART_QUERY );
						if ( item != null && !isSharingChart )
						{
							if ( table.getMenu( ) != null )
							{
								table.getMenu( ).dispose( );
							}
							table.setMenu( createMenuManager( item.getData( ) ).createContextMenu( table ) );
						}
						else
						{
							table.setMenu( null );
						}

						if ( table.getMenu( ) != null && !isSharingChart )
						{
							table.getMenu( ).setVisible( true );
						}
					}
					

				}
			}
		} ) ;
		
		table.addListener( SWT.Resize, new Listener( ) {

			public void handleEvent( Event event )
			{
				Table table = (Table) event.widget;
				int totalWidth = table.getClientArea( ).width;
				table.getColumn( 0 ).setWidth( totalWidth );
			}
		} );
		
		// Set drag/drop.
		DragSource ds = new DragSource( table, DND.DROP_COPY | DND.DROP_MOVE);
		ds.setTransfer( new Transfer[]{
			SimpleTextTransfer.getInstance( )
		} );
		ColumnNamesTableDragListener dragSourceAdapter = new ColumnNamesTableDragListener( table,
				itemHandle );
		ds.addDragListener( dragSourceAdapter );
		
		tableViewerColumns.setContentProvider( new IStructuredContentProvider() {
			  /**
			   * Gets the food items for the list
			   * 
			   * @param arg0
			   *            the data model
			   * @return Object[]
			   */
			  public Object[] getElements(Object arg0) {
				if ( arg0 == null )
					return null;
			    return (ColumnBindingInfo[])arg0;
			  }

			  /**
			   * Disposes any created resources
			   */
			  public void dispose() {
			    // Do nothing
			  }

			  /**
			   * Called when the input changes
			   * 
			   * @param arg0
			   *            the viewer
			   * @param arg1
			   *            the old input
			   * @param arg2
			   *            the new input
			   */
			  public void inputChanged(Viewer arg0, Object arg1, Object arg2) {
			    // Do nothing
			  }
			} );
		tableViewerColumns.setLabelProvider( new ILabelProvider() {

			  /**
			   * images
			   * 
			   * @param arg0
			   *            the element
			   * @return Image
			   */
			  public Image getImage(Object arg0) {
				  String imageName = ((ColumnBindingInfo) arg0).getImageName( );
				  if ( imageName == null )
					  return null;
				  return UIHelper.getImage( imageName );
			  }

			  /**
			   * Gets the text for an element
			   * 
			   * @param arg0
			   *            the element
			   * @return String
			   */
			  public String getText(Object arg0) {
			    return ((ColumnBindingInfo) arg0).getName();
			  }

			  /**
			   * Adds a listener
			   * 
			   * @param arg0
			   *            the listener
			   */
			  public void addListener(ILabelProviderListener arg0) {
			    // Throw it away
			  }

			  /**
			   * Disposes any resources
			   */
			  public void dispose() {
			    // Nothing to dispose
			  }

			  /**
			   * Returns whether changing the specified property for the specified element
			   * affect the label
			   * 
			   * @param arg0
			   *            the element
			   * @param arg1
			   *            the property
			   * @return boolean
			   */
			  public boolean isLabelProperty(Object arg0, String arg1) {
			    return false;
			  }

			  /**
			   * Removes a listener
			   * 
			   * @param arg0
			   *            the listener
			   */
			  public void removeListener(ILabelProviderListener arg0) {
			    // Ignore
			  }
			} );
		
	}

	private void updateDragDataSource( )
	{
		
		if ( isCubeMode( ) )
		{
			if ( getDataServiceProvider( ).getReportItemReference( ) != null )
			{// share cube

				if ( !getDataServiceProvider( ).checkState( IDataServiceProvider.SHARE_CHART_QUERY ) )
				{
					( (GridData) columnListDescription.getLayoutData( ) ).exclude = false;
					columnListDescription.setVisible( true );
					columnListDescription.setText( Messages.getString("StandardChartDataSheet.Label.ShareCrossTab") ); //$NON-NLS-1$
				}
				else
				{
					( (GridData) columnListDescription.getLayoutData( ) ).exclude = true;
					columnListDescription.setVisible( false );
				}
				cmpColumnsList.layout( );

				getContext( ).setShowingDataPreview( Boolean.FALSE );
				btnShowDataPreviewB.setSelection( false );
				btnShowDataPreviewB.setEnabled( false );

				stackLayout.topControl = cmpColumnsList;
				refreshDataPreviewPane( );
			}
			else
			{
				stackLayout.topControl = cmpCubeTree;
				cubeTreeViewer.setInput( getCube( ) );

			}

			cmpStack.layout( );
			return;
		}
		
		if ( columnListDescription != null )
		{
			( (GridData) columnListDescription.getLayoutData( ) ).exclude = false;
			columnListDescription.setVisible( true );
			columnListDescription.setText( Messages.getString( "StandardChartDataSheet.Label.ToBindADataColumn" ) ); //$NON-NLS-1$
		}
		btnShowDataPreviewB.setEnabled( true );
		cmpColumnsList.layout( );

		// Clear data preview setting if current data item was changed.
		String pValue = ( previousData == null ) ? "" : previousData; //$NON-NLS-1$
		String cValue = ( currentData == null ) ? "" : currentData; //$NON-NLS-1$
		if ( !pValue.equals( cValue ) )
		{
			getContext( ).setShowingDataPreview( null );
		}
		previousData = currentData;
		
		try
		{
			// If it is initial state and the columns are equal and greater
			// than 6, do not use data preview, just use columns list view.
			if ( !getContext( ).isSetShowingDataPreview( )
					&& getDataServiceProvider( ).getPreviewHeadersInfo( ).length >= 6 )
			{
				getContext().setShowingDataPreview( Boolean.FALSE );
			}
			ChartWizard.removeException( ChartWizard.StaChartDSh_gHeaders_ID );
		}
		catch ( NullPointerException e )
		{
			// Do not do anything.
		}
		catch ( ChartException e )
		{
			ChartWizard.showException( ChartWizard.StaChartDSh_gHeaders_ID,
					e.getMessage( ) );
		}

		btnShowDataPreviewA.setSelection( getContext().isShowingDataPreview( ) );
		btnShowDataPreviewB.setSelection( getContext().isShowingDataPreview( ) );
		
		if ( getContext().isShowingDataPreview( )  )
		{
			stackLayout.topControl = cmpDataPreview;
		}
		else
		{
			stackLayout.topControl = cmpColumnsList;
		}
		
		refreshDataPreviewPane( );
		
		cmpStack.layout( );
	}

	/**
	 * 
	 */
	private void refreshDataPreviewPane( )
	{
		if ( getContext().isShowingDataPreview( ) )
		{
			refreshTablePreview( );
		}
		else
		{
			refreshColumnsListView( );
		}
	}

	/**
	 * 
	 */
	private void refreshColumnsListView( )
	{
		// if ( dataProvider.getDataSetFromHandle( ) == null )
		// {
		// return;
		// }
		//		
		// if ( isCubeMode( ) )
		// {
		//
		// }
		
		// 1. Create a runnable.
		Runnable runnable = new Runnable( ) {

			/*
			 * (non-Javadoc)
			 * 
			 * @see org.eclipse.jface.operation.IRunnableWithProgress#run(org.eclipse.core.runtime.IProgressMonitor)
			 */
			public void run( )
			{
				ColumnBindingInfo[] headers = null;
				List<?> dataList = null;
				try
				{
					// Get header and data in other thread.
					headers = getDataServiceProvider( ).getPreviewHeadersInfo( );
					// Only when live preview is enabled, it retrieves data.
					if ( dataProvider.isLivePreviewEnabled( ) )
					{
						dataList = getPreviewData( );
					}

					final ColumnBindingInfo[] headerInfo = headers;
					final List<?> data = dataList;
					// Execute UI operation in UI thread.
					Display.getDefault( ).syncExec( new Runnable( ) {

						public void run( )
						{
							updateColumnsTableViewer( headerInfo, data );
							ChartWizard.removeException( ChartWizard.StaChartDSh_dPreview_ID );
						}

					} );
				}
				catch ( Exception e )
				{
					final ColumnBindingInfo[] headerInfo = headers;
					final List<?> data = dataList;

					// Catch any exception.
					final String message = e.getLocalizedMessage( );
					Display.getDefault( ).syncExec( new Runnable( ) {

						/*
						 * (non-Javadoc)
						 * 
						 * @see java.lang.Runnable#run()
						 */
						public void run( )
						{

							updateColumnsTableViewer( headerInfo, data );
							ChartWizard.showException( ChartWizard.StaChartDSh_dPreview_ID,
									message );
						}
					} );
				}
			}
		};

		// 2. Run it.
		new Thread( runnable ).start( );
	}

	/**
	 * @param headerInfo
	 * @param data
	 */
	private void updateColumnsTableViewer(
			final ColumnBindingInfo[] headerInfo,
			final List<?> data )
	{
		// Set input.
		tableViewerColumns.setInput( headerInfo );
		tableViewerColumns.setData( KEY_PREVIEW_DATA, data );
		
		// Make the selected column visible and active.
		int index = tablePreview.getCurrentColumnIndex( );
		if ( index >= 0 )
		{
			tableViewerColumns.getTable( ).setFocus( );
			tableViewerColumns.getTable( ).select( index );
			tableViewerColumns.getTable( ).showSelection( );
		}
		
		updateColumnsTableViewerColor( );
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
			GridLayout gridLayout = new GridLayout( 2, false );
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

		cmbInherit = new CCombo( cmpDetail, SWT.DROP_DOWN
				| SWT.READ_ONLY
				| SWT.BORDER );
		cmbInherit.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
		cmbInherit.addListener( SWT.Selection, this );

		cmbDataItems = new CCombo( cmpDetail, SWT.DROP_DOWN
				| SWT.READ_ONLY
				| SWT.BORDER );
		cmbDataItems.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
		cmbDataItems.addListener( SWT.Selection, this );

		initDataSelector( );
		updatePredefinedQueries( );
		return cmpDataSet;
	}

	int invokeNewDataSet( )
	{
		DataService.getInstance( ).createDataSet( );
		
		// Due to the limitation of the action execution, always return ok
		return Window.OK;
	}

	int invokeEditFilter( )
	{
		ExtendedItemHandle handle = getItemHandle( );
		handle.getModuleHandle( ).getCommandStack( ).startTrans( null );
		ExtendedItemFilterDialog page = new ExtendedItemFilterDialog( handle );

		AbstractFilterHandleProvider provider = ChartFilterProviderDelegate.createFilterProvider( handle,
				handle );
		if ( provider instanceof ChartCubeFilterHandleProvider )
		{
			( (ChartCubeFilterHandleProvider) provider ).setContext( getContext( ) );
		}
		page.setFilterHandleProvider( provider );

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
				| SWT.RESIZE
				| SWT.APPLICATION_MODAL );
		// #194163: Do not register CS help in chart since it's registered in
		// super column binding dialog.
		// ChartUIUtil.bindHelp( shell,
		// ChartHelpContextIds.DIALOG_DATA_SET_COLUMN_BINDING );
		ExtendedItemHandle handle = getItemHandle( );

		handle.getModuleHandle( ).getCommandStack( ).startTrans( null );
		ColumnBindingDialog page = new ChartColumnBindingDialog( handle,
				shell,
				getContext( ) );

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

		// Make all bindings under share binding case read-only.
		( (ChartColumnBindingDialog) page ).setReadOnly( getDataServiceProvider( ).isSharedBinding( )
				|| getDataServiceProvider( ).isInheritanceOnly( ) );

		int openStatus = page.open( );
		if ( openStatus == Window.OK )
		{
			handle.getModuleHandle( ).getCommandStack( ).commit( );
			updatePredefinedQueries( );
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
		cmbInherit.setItems( new String[]{
				Messages.getString( "StandardChartDataSheet.Combo.InheritColumnsGroups" ), //$NON-NLS-1$ 
				Messages.getString( "StandardChartDataSheet.Combo.InheritColumnsOnly" ) //$NON-NLS-1$ 
		} );
		if ( dataProvider.isInheritColumnsSet( ) )
		{
			cmbInherit.select( dataProvider.isInheritColumnsOnly( ) ? 1 : 0 );
		}
		else
		{
			// Set default inheritance value
			if ( ChartReportItemUtil.hasAggregation( getChartModel( ) ) )
			{
				// If aggregations found, set inherit columns only
				cmbInherit.select( 1 );
				getContext( ).setInheritColumnsOnly( true );
			}
			else
			{
				// Default value is set as Inherit groups
				cmbInherit.select( 0 );
				getContext( ).setInheritColumnsOnly( false );
			}
		}
		cmbInherit.setEnabled( false );
		
		cmbDataItems.setItems( createDataComboItems( ) );
		cmbDataItems.setVisibleItemCount( cmbDataItems.getItemCount( ) );

		// Select report item reference
		// Since handle may have data set or data cube besides reference, always
		// check reference first
		String sItemRef = getDataServiceProvider( ).getReportItemReference( );
		if ( sItemRef != null )
		{
			btnUseData.setSelection( true );
			bIsInheritSelected = false;
			cmbDataItems.setText( sItemRef );
			currentData = sItemRef;
			return;
		}

		// Select data set
		String sDataSet = getDataServiceProvider( ).getBoundDataSet( );
		if ( sDataSet != null && !getDataServiceProvider( ).isInheritanceOnly( ) )
		{
			btnUseData.setSelection( true );
			bIsInheritSelected = false;
			cmbDataItems.setText( sDataSet );
			currentData = sDataSet;
			if ( sDataSet != null )
			{
				switchDataTable( );
			}
			return;
		}

		// Select data cube
		String sDataCube = getDataServiceProvider( ).getDataCube( );
		if ( sDataCube != null
				&& !getDataServiceProvider( ).isInheritanceOnly( ) )
		{
			btnUseData.setSelection( true );
			bIsInheritSelected = false;
			cmbDataItems.setText( sDataCube );
			currentData = sDataCube;
			return;
		}

		cmbInherit.setEnabled( getDataServiceProvider( ).getReportDataSet( ) != null );
		btnInherit.setSelection( true );		
		bIsInheritSelected = true;
		if ( getDataServiceProvider( ).isInheritanceOnly( ) )
		{
			btnUseData.setSelection( false );
			btnUseData.setEnabled( false );
		}
		cmbDataItems.select( 0 );
		currentData = null;
		cmbDataItems.setEnabled( false );
		// Initializes column bindings from container
		getDataServiceProvider( ).setDataSet( null );
		String reportDataSet = getDataServiceProvider( ).getReportDataSet( );
		if ( reportDataSet != null )
		{
			switchDataTable( );
		}

	}

	public void handleEvent( Event event )
	{
		if ( event.data instanceof ISelectDataComponent )
		{
			// When user select expression in drop&down list of live preview
			// area, the event will be handled to update related column color.
			if ( event.type == IChartDataSheet.EVENT_QUERY
					&& event.detail == IChartDataSheet.DETAIL_UPDATE_COLOR )
			{
				refreshTableColor( );
			}
			return;
		}
		// Right click to display the menu. Menu display by clicking
		// application key is triggered by os, so do nothing.
		// bug 261340, now we use the field doit to indicate whether it's menu
		// initialization or event triggering.
		if ( event.type == CustomPreviewTable.MOUSE_RIGHT_CLICK_TYPE )
		{
			if ( getDataServiceProvider( ).getBoundDataSet( ) != null
					|| getDataServiceProvider( ).getReportDataSet( ) != null )
			{
				if ( event.widget instanceof Button )
				{
					Button header = (Button) event.widget;

					// Bind context menu to each header button
					boolean isSharingChart = dataProvider.checkState( IDataServiceProvider.SHARE_CHART_QUERY );
					if ( header.getMenu( ) == null && !isSharingChart) 
					{
						header.setMenu( createMenuManager( event.data ).createContextMenu( tablePreview ) );
					}

					if ( event.doit && !isSharingChart )
					{
						header.getMenu( ).setVisible( true );
					}
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
					refreshDataPreviewPane( );
					// Update preview via event
					fireEvent( btnFilters, EVENT_PREVIEW );
				}
			}
			else if ( event.widget == btnParameters )
			{
				if ( invokeEditParameter( ) == Window.OK )
				{
					refreshDataPreviewPane( );
					// Update preview via event
					fireEvent( btnParameters, EVENT_PREVIEW );
				}
			}
			else if ( event.widget == btnBinding )
			{
				if ( invokeDataBinding( ) == Window.OK )
				{
					refreshDataPreviewPane( );
					// Update preview via event
					fireEvent( btnBinding, EVENT_PREVIEW );
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

					// Avoid duplicate loading data set.
					if ( bIsInheritSelected )
					{
						return;
					}

					bIsInheritSelected = true;

					getDataServiceProvider( ).setReportItemReference( null );
					getDataServiceProvider( ).setDataSet( null );
					switchDataSet( null );

					cmbDataItems.select( 0 );
					currentData = null;
					cmbDataItems.setEnabled( false );
					cmbInherit.setEnabled( getDataServiceProvider( ).getReportDataSet( ) != null );
					setEnabledForButtons( );
					updateDragDataSource( );
					updatePredefinedQueries( );
				}
				else if ( event.widget == btnUseData )
				{
					// Skip when selection is false
					if ( !btnUseData.getSelection( ) )
					{
						return;
					}

					// Avoid duplicate loading data set.
					if ( !bIsInheritSelected )
					{
						return;
					}

					bIsInheritSelected = false;

					getDataServiceProvider( ).setReportItemReference( null );
					getDataServiceProvider( ).setDataSet( null );
					selectDataSet( );
					cmbDataItems.setEnabled( true );
					cmbInherit.setEnabled( false );
					setEnabledForButtons( );
					updateDragDataSource( );
					updatePredefinedQueries( );
				}
				else if ( event.widget == cmbInherit )
				{
					getContext( ).setInheritColumnsOnly( cmbInherit.getSelectionIndex( ) == 1 );

					// Fire event to update outside UI
					fireEvent( btnBinding, EVENT_QUERY );
					refreshDataPreviewPane( );
				}
				else if ( event.widget == cmbDataItems )
				{
					ColorPalette.getInstance( ).restore( );
					int selectedIndex = cmbDataItems.getSelectionIndex( );
					Integer selectState = selectDataTypes.get( selectedIndex );
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
							selectState = selectDataTypes.get( selectedIndex );
							cmbDataItems.select( selectedIndex );
							break;
					}
					switch ( selectState.intValue( ) )
					{
						case SELECT_DATA_SET :
							if ( getDataServiceProvider( ).getReportItemReference( ) == null
									&& getDataServiceProvider( ).getBoundDataSet( ) != null
									&& getDataServiceProvider( ).getBoundDataSet( )
											.equals( cmbDataItems.getText( ) ) )
							{
								return;
							}
							getDataServiceProvider( ).setDataSet( cmbDataItems.getText( ) );
							currentData = cmbDataItems.getText( );
							switchDataSet( cmbDataItems.getText( ) );
							setEnabledForButtons( );
							updateDragDataSource( );
							break;
						case SELECT_DATA_CUBE :
							getDataServiceProvider( ).setDataCube( cmbDataItems.getText( ) );
							currentData = cmbDataItems.getText( );
							updateDragDataSource( );
							setEnabledForButtons( );
							// Update preview via event
							DataDefinitionTextManager.getInstance( )
									.refreshAll( );
							fireEvent( tablePreview, EVENT_PREVIEW );
							break;
						case SELECT_REPORT_ITEM :
							if ( cmbDataItems.getText( )
									.equals( getDataServiceProvider( ).getReportItemReference( ) ) )
							{
								return;
							}
							getDataServiceProvider( ).setReportItemReference( cmbDataItems.getText( ) );
							
							// TED 10163
							// Following calls will revise chart model for
							// report item sharing case, in older version of
							// chart, it is allowed to set grouping on category
							// series when sharing report item, but now it isn't
							// allowed, so this calls will revise chart model to
							// remove category series grouping flag for the
							// case.
							ChartReportItemUtil.reviseChartModel( ChartReportItemUtil.REVISE_REFERENCE_REPORT_ITEM,
									this.getContext( ).getModel( ),
									itemHandle );
							
							// Bugzilla 265077.
							ChartAdapter.beginIgnoreNotifications( );
							if ( dataProvider.checkState( IDataServiceProvider.SHARE_CHART_QUERY ))
							{
								ExtendedItemHandle refHandle = ChartReportItemUtil.getChartReferenceItemHandle( itemHandle );
								if ( refHandle != null )
								{
									ChartReportItemUtil.copyChartSeriesDefinition( ChartReportItemUtil.getChartFromHandle( refHandle ),
										getChartModel( ) );
								}
							}
							ChartAdapter.endIgnoreNotifications( );
							
							currentData = cmbDataItems.getText( );
							// selectDataSet( );
							// switchDataSet( cmbDataItems.getText( ) );

							// Update preview via event
							DataDefinitionTextManager.getInstance( )
									.refreshAll( );
							fireEvent( tablePreview, EVENT_PREVIEW );

							setEnabledForButtons( );
							updateDragDataSource( );
							break;
						case SELECT_NEW_DATASET :
							// Bring up the dialog to create a dataset
							int result = invokeNewDataSet( );
							if ( result == Window.CANCEL )
							{
								return;
							}

							cmbDataItems.removeAll( );
							cmbDataItems.setItems( createDataComboItems( ) );
							cmbDataItems.setVisibleItemCount( cmbDataItems.getItemCount( ) );
							if ( currentData == null )
							{
								cmbDataItems.select( 0 );
							}
							else
							{
								cmbDataItems.setText( currentData );
							}
							break;
						case SELECT_NEW_DATACUBE :
							if ( getDataServiceProvider( ).getAllDataSets( ).length == 0 )
							{
								invokeNewDataSet( );
							}
							if ( getDataServiceProvider( ).getAllDataSets( ).length != 0 )
							{
								new NewCubeAction( ).run( );
							}

							cmbDataItems.removeAll( );
							cmbDataItems.setItems( createDataComboItems( ) );
							cmbDataItems.setVisibleItemCount( cmbDataItems.getItemCount( ) );
							if ( currentData == null )
							{
								cmbDataItems.select( 0 );
							}
							else
							{
								cmbDataItems.setText( currentData );
							}
							break;
					}
					updatePredefinedQueries( );
				}
				else if ( event.widget == btnShowDataPreviewA || event.widget == btnShowDataPreviewB )
				{
					Button w =  (Button) event.widget;
					getContext().setShowingDataPreview( Boolean.valueOf( w.getSelection( ) ) );
					updateDragDataSource( );
				}
				ChartWizard.removeException( ChartWizard.StaChartDSh_switch_ID );
			}
			catch ( ChartException e1 )
			{
				ChartWizard.showException( ChartWizard.StaChartDSh_switch_ID,
						e1.getLocalizedMessage( ) );
			}
		}
	}



	private void selectDataSet( )
	{
		String currentDS = getDataServiceProvider( ).getBoundDataSet( );
		if ( currentDS == null )
		{
			cmbDataItems.select( 0 );
			currentData = null;
		}
		else
		{
			cmbDataItems.setText( currentDS );
			currentData = currentDS;
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
			tableViewerColumns.setInput( null );

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
		fireEvent( tablePreview, EVENT_PREVIEW );
	}

	/**
	 * Update column headers and data to table.
	 * 
	 * @param headers
	 * @param dataList
	 */
	private void updateTablePreview( final ColumnBindingInfo[] headers,
			final List<?> dataList )
	{
		fireEvent( tablePreview, EVENT_QUERY );

		if ( tablePreview.isDisposed( ) )
		{
			return;
		}

		if ( headers == null || headers.length == 0 )
		{
			tablePreview.setEnabled( false );
			tablePreview.createDummyTable( );
		}
		else
		{
			tablePreview.setEnabled( true );
			tablePreview.setColumns( headers );

			refreshTableColor( );

			// Add data value
			if ( dataList != null )
			{
				for ( Iterator<?> iterator = dataList.iterator( ); iterator.hasNext( ); )
				{
					String[] dataRow = (String[]) iterator.next( );
					for ( int i = 0; i < dataRow.length; i++ )
					{
						tablePreview.addEntry( dataRow[i], i );
					}
				}
			}
		}
		tablePreview.layout( );
		
		// Make the selected column visible and active.
		int index = tableViewerColumns.getTable( ).getSelectionIndex( );
		if ( index >= 0 )
		{
			tablePreview.moveTo( index );
		}
	}

	private synchronized List<?> getPreviewData( ) throws ChartException
	{
		return getDataServiceProvider( ).getPreviewData( );
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
				ColumnBindingInfo[] headers = null;
				List<?> dataList = null;
				try
				{
					// Get header and data in other thread.
					headers = getDataServiceProvider( ).getPreviewHeadersInfo( );
					dataList = getPreviewData( );

					final ColumnBindingInfo[] headerInfo = headers;
					final List<?> data = dataList;
					// Execute UI operation in UI thread.
					Display.getDefault( ).syncExec( new Runnable( ) {

						public void run( )
						{
							updateTablePreview( headerInfo, data );
							ChartWizard.removeException( ChartWizard.StaChartDSh_dPreview_ID );
						}
					} );
				}
				catch ( Exception e )
				{
					final ColumnBindingInfo[] headerInfo = headers;
					final List<?> data = dataList;

					// Catch any exception.
					final String message = e.getMessage( );
					Display.getDefault( ).syncExec( new Runnable( ) {

						/*
						 * (non-Javadoc)
						 * 
						 * @see java.lang.Runnable#run()
						 */
						public void run( )
						{
							// Still update table preview in here to ensure the
							// column headers of table preview can be updated
							// and user can select expression from table preview
							// even if there is no preview data.
							updateTablePreview( headerInfo, data );

							ChartWizard.showException( ChartWizard.StaChartDSh_dPreview_ID,
									message );
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
		if ( getContext( ).isShowingDataPreview( ) )
		{
			for ( int i = 0; i < tablePreview.getColumnNumber( ); i++ )
			{
				tablePreview.setColumnColor( i,
						ColorPalette.getInstance( )
								.getColor( ExpressionUtil.createJSRowExpression( tablePreview.getColumnHeading( i ) ) ) );
			}
		}
		else
		{
			updateColumnsTableViewerColor( );
		}
	}

	/**
	 * 
	 */
	private void updateColumnsTableViewerColor( )
	{
		for ( TableItem item : tableViewerColumns.getTable( ).getItems( ) )
		{
			ColumnBindingInfo cbi = (ColumnBindingInfo) item.getData( );
			Color c = ColorPalette.getInstance( )
					.getColor( ExpressionUtil.createJSRowExpression( cbi.getName( ) ) );
			if ( c == null )
			{
				c = Display.getDefault( )
						.getSystemColor( SWT.COLOR_LIST_BACKGROUND );
			}
			item.setBackground( c );
		}
	}

	/**
	 * Returns actual expression for common and sharing query case.
	 * 
	 * @param query
	 * @param expr
	 * @return
	 */
	private String getActualExpression( String expr )
	{
		if ( !dataProvider.checkState( IDataServiceProvider.SHARE_QUERY ) )
		{
			return expr;
		}

		// Convert to actual expression.
		Object obj = getCurrentColumnHeadObject( );
		if ( obj instanceof ColumnBindingInfo )
		{
			ColumnBindingInfo cbi = (ColumnBindingInfo) obj;
			int type = cbi.getColumnType( );
			if ( type == ColumnBindingInfo.GROUP_COLUMN
					|| type == ColumnBindingInfo.AGGREGATE_COLUMN )
			{
				return cbi.getExpression( );
			}
		}

		return expr;
	}

	protected void manageColorAndQuery( Query query, String expr )
	{
		// If it's not used any more, remove color binding
		if ( DataDefinitionTextManager.getInstance( )
				.getNumberOfSameDataDefinition( query.getDefinition( ) ) == 1 )
		{
			ColorPalette.getInstance( ).retrieveColor( query.getDefinition( ) );
		}

		// Update query, if it is sharing binding case, the specified expression
		// will be converted and set to query, else directly set specified
		// expression to query.
		// DataDefinitionTextManager.getInstance( ).updateQuery( query, expr );
		query.setDefinition( getActualExpression( expr ) );

		DataDefinitionTextManager.getInstance( ).updateText( query );
		// Reset table column color
		refreshTableColor( );
		// Refresh all data definition text
		DataDefinitionTextManager.getInstance( ).refreshAll( );
	}

	/**
	 * @param queryType
	 * @param query
	 * @param expr
	 * @param seriesDefinition
	 * @since 2.5
	 */
	protected void manageColorAndQuery( String queryType, Query query, String expr,
 SeriesDefinition seriesDefinition )
	{
		// If it's not used any more, remove color binding
		if ( dataProvider.getNumberOfSameDataDefinition( query.getDefinition( ) ) == 1 )
		{
			ColorPalette.getInstance( ).retrieveColor( query.getDefinition( ) );
		}

		// Update query, if it is sharing binding case, the specified expression
		// will be converted and set to query, else directly set specified
		// expression to query.
		updateQuery( queryType, query, expr, seriesDefinition );

		// 236018--add the logic of register color with display expression
		// as it does in the input text refreshing, since the text may not be
		// shown at current.
		ColorPalette.getInstance( ).putColor( expr );
		
		DataDefinitionTextManager.getInstance( ).updateText( query );
		// Reset table column color
		refreshTableColor( );
		// Refresh all data definition text
		DataDefinitionTextManager.getInstance( ).refreshAll( );
	}

	private void updateQuery( String queryType, Query query, String expr,
			SeriesDefinition seriesDefinition )
	{
		String actualExpr = expr;

		if ( dataProvider.checkState( IDataServiceProvider.SHARE_QUERY )
				|| dataProvider.checkState( IDataServiceProvider.INHERIT_COLUMNS_GROUPS ) )
		{
			boolean isGroupOrAggr = false;
			// Convert to actual expression.
			Object obj = getCurrentColumnHeadObject( );
			if ( obj instanceof ColumnBindingInfo )
			{
				ColumnBindingInfo cbi = (ColumnBindingInfo) obj;
				int type = cbi.getColumnType( );
				if ( type == ColumnBindingInfo.GROUP_COLUMN
						|| type == ColumnBindingInfo.AGGREGATE_COLUMN )
				{
					actualExpr = cbi.getExpression( );
					isGroupOrAggr = true;
				}
			}

			// Update group state.
			if ( seriesDefinition != null
					&& ( queryType.equals( ChartUIConstants.QUERY_CATEGORY ) || queryType.equals( ChartUIConstants.QUERY_VALUE ) ) )
			{
				seriesDefinition.getGrouping( ).setEnabled( isGroupOrAggr );
			}
		}

		query.setDefinition( actualExpr );
	}

	class CategoryXAxisAction extends Action
	{

		Query query;
		String expr;
		private SeriesDefinition seriesDefintion;

		CategoryXAxisAction( String expr )
		{
			super( getBaseSeriesTitle( getChartModel( ) ) );
			seriesDefintion = ChartUIUtil.getBaseSeriesDefinitions( getChartModel( ) )
					.get( 0 );
			this.query = seriesDefintion.getDesignTimeSeries( )
					.getDataDefinition( )
					.get( 0 );
			this.expr = expr;

			setEnabled( DataDefinitionTextManager.getInstance( )
					.isAcceptableExpression( query,
							expr,
							dataProvider.isSharedBinding( ) ) );
		}

		public void run( )
		{
			manageColorAndQuery( ChartUIConstants.QUERY_CATEGORY,
					query,
					expr,
					seriesDefintion );
		}
	}

	class GroupYSeriesAction extends Action
	{

		Query query;
		String expr;
		private SeriesDefinition seriesDefinition;

		GroupYSeriesAction( Query query, String expr,
				SeriesDefinition seriesDefinition )
		{
			super( getGroupSeriesTitle( getChartModel( ) ) );
			this.seriesDefinition = seriesDefinition;
			this.query = query;
			this.expr = expr;

			setEnabled( DataDefinitionTextManager.getInstance( )
					.isAcceptableExpression( query,
							expr,
							dataProvider.isSharedBinding( ) ) );
		}

		public void run( )
		{
			// Use the first group, and copy to the all groups
			ChartAdapter.beginIgnoreNotifications( );
			ChartUIUtil.setAllGroupingQueryExceptFirst( getChartModel( ), expr );
			ChartAdapter.endIgnoreNotifications( );

			manageColorAndQuery( ChartUIConstants.QUERY_OPTIONAL,
					query,
					expr,
					seriesDefinition );
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

			// Grouping expressions can't be set on value series.
			boolean enabled = true;
			if ( dataProvider.checkState( IDataServiceProvider.SHARE_QUERY ) )
			{
				Object obj = getCurrentColumnHeadObject( );
				if ( obj instanceof ColumnBindingInfo
						&& ( (ColumnBindingInfo) obj ).getColumnType( ) == ColumnBindingInfo.GROUP_COLUMN )
				{
					enabled = false;
				}
			}

			setEnabled( enabled );
		}

		public void run( )
		{
			manageColorAndQuery( ChartUIConstants.QUERY_VALUE,
					query,
					expr,
					null );
		}
	}

	Object getCurrentColumnHeadObject()
	{
		if ( getContext( ).isShowingDataPreview( ) )
		{
			return tablePreview.getCurrentColumnHeadObject( );
		}
		int index = tableViewerColumns.getTable( ).getSelectionIndex( );
		if ( index < 0 )
			return null;
		return tableViewerColumns.getTable( ).getItem( index ).getData( );
	}
	
	static class HeaderShowAction extends Action
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

	protected List<Object> getActionsForTableHead( String expr )
	{
		List<Object> actions = new ArrayList<Object>( 3 );
		actions.add( getBaseSeriesMenu( getChartModel( ), expr ) );
		actions.add( getOrthogonalSeriesMenu( getChartModel( ), expr ) );
		actions.add( getGroupSeriesMenu( getChartModel( ), expr ) );
		return actions;
	}
	
	private MenuManager createMenuManager( final Object data )
	{
		MenuManager menuManager = new MenuManager( );
		menuManager.setRemoveAllWhenShown( true );
		menuManager.addMenuListener( new IMenuListener( ) {

			public void menuAboutToShow( IMenuManager manager )
			{
				if ( data instanceof ColumnBindingInfo )
				{
					// Menu for columns table.
					addMenu( manager,
							new HeaderShowAction( ((ColumnBindingInfo)data).getName( ) ) );
					String expr = ExpressionUtil.createJSRowExpression( ((ColumnBindingInfo)data).getName( ) );
					List<Object> actions = getActionsForTableHead( expr );
					for ( Object act : actions )
					{
						addMenu( manager, act );
					}
				}
				else if ( data instanceof Integer )
				{
					// Menu for table
					addMenu( manager,
							new HeaderShowAction( tablePreview.getCurrentColumnHeading( ) ) );
					String expr = ExpressionUtil.createJSRowExpression( tablePreview.getCurrentColumnHeading( ) );
					List<Object> actions = getActionsForTableHead( expr );
					for ( Object act : actions )
					{
						addMenu( manager, act );
					}
				}
				else if ( data instanceof MeasureHandle )
				{
					// Menu for Measure
					String expr = createCubeExpression( );
					if ( expr != null )
					{
						addMenu( manager,
								getOrthogonalSeriesMenu( getChartModel( ), expr ) );
					}
				}
				else if ( data instanceof LevelHandle )
				{
					// Menu for Level
					String expr = createCubeExpression( );
					if ( expr != null )
					{
						// bug#220724
						if ( ( (Boolean) dataProvider.checkData( ChartUIConstants.QUERY_CATEGORY,
								expr ) ).booleanValue( ) )
						{
							addMenu( manager,
									getBaseSeriesMenu( getChartModel( ),
								expr ) );
						}
						
						if ( dataProvider.checkState( IDataServiceProvider.MULTI_CUBE_DIMENSIONS )
								&& ( (Boolean) dataProvider.checkData( ChartUIConstants.QUERY_OPTIONAL,
										expr ) ).booleanValue( ) )
						{
							addMenu( manager,
									getGroupSeriesMenu( getChartModel( ),
								expr ) );
						}
						
					}
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

				// Do not allow customized query in xtab
				if ( getDataServiceProvider( ).isPartChart( ) )
				{
					if ( item instanceof IAction )
					{
						( (IAction) item ).setEnabled( false );
					}
				}
			}
		} );
		return menuManager;
	}

	private Object getBaseSeriesMenu( Chart chart, String expr )
	{
		EList<SeriesDefinition> sds = ChartUIUtil.getBaseSeriesDefinitions( chart );
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
			List<SeriesDefinition> sds = ChartUIUtil.getOrthogonalSeriesDefinitions( chart,
					axisIndex );
			if ( !sds.isEmpty( ) )
			{
				SeriesDefinition sd = sds.get( 0 );
				IAction action = new GroupYSeriesAction( sd.getQuery( ),
						expr,
						sd );
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
			List<SeriesDefinition> sds = ChartUIUtil.getOrthogonalSeriesDefinitions( chart,
					axisIndex );
			for ( int i = 0; i < sds.size( ); i++ )
			{
				Series series = sds.get( i ).getDesignTimeSeries( );
				EList<Query> dataDefns = series.getDataDefinition( );

				if ( series instanceof StockSeries )
				{
					IMenuManager secondManager = new MenuManager( getSecondMenuText( axisIndex,
							i,
							series ) );
					topManager.add( secondManager );
					for ( int j = 0; j < dataDefns.size( ); j++ )
					{
						IAction action = new ValueYSeriesAction( dataDefns.get( j ),
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
						IAction action = new ValueYSeriesAction( dataDefns.get( j ),
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
						IAction action = new ValueYSeriesAction( dataDefns.get( j ),
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
						IAction action = new ValueYSeriesAction( dataDefns.get( j ),
								expr );
						action.setText( ChartUIUtil.getGanttTitle( j )
								+ Messages.getString( "StandardChartDataSheet.Label.Component" ) ); //$NON-NLS-1$
						secondManager.add( action );
					}
				}
				else
				{
					IAction action = new ValueYSeriesAction( dataDefns.get( 0 ),
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
		if ( ChartUIUtil.getOrthogonalAxisNumber( getChartModel( ) ) > 1 )
		{
			sb.append( Messages.getString( "StandardChartDataSheet.Label.Axis" ) ); //$NON-NLS-1$
			sb.append( axisIndex + 1 );
			sb.append( " - " ); //$NON-NLS-1$
		}
		sb.append( Messages.getString( "StandardChartDataSheet.Label.Series" ) //$NON-NLS-1$
				+ ( seriesIndex + 1 )
				+ " (" + series.getDisplayName( ) + ")" ); //$NON-NLS-1$ //$NON-NLS-2$
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
		boolean bCube = ChartXTabUtil.getBindingCube( itemHandle ) != null;
		if ( bCube )
		{
			// If current item doesn't support cube, referenced cube should be
			// invalid.
			return isDataItemSupported( SELECT_DATA_CUBE );
		}
		return false;
	}

	private CubeHandle getCube( )
	{
		return ChartXTabUtil.getBindingCube( itemHandle );
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
		if ( selection.length > 0
				&& !dataProvider.isSharedBinding( )
				&& !dataProvider.isPartChart( ) )
		{
			TreeItem treeItem = selection[0];
			ComputedColumnHandle binding = null;
			if ( treeItem.getData( ) instanceof LevelHandle )
			{
				binding = ChartXTabUtil.findBinding( itemHandle,
						ChartXTabUtil.createDimensionExpression( (LevelHandle) treeItem.getData( ) ) );
			}
			else if ( treeItem.getData( ) instanceof MeasureHandle )
			{
				binding = ChartXTabUtil.findBinding( itemHandle,
						ChartXTabUtil.createMeasureExpression( (MeasureHandle) treeItem.getData( ) ) );
			}
			if ( binding != null )
			{
				expr = ExpressionUtil.createJSDataExpression( binding.getName( ) );
			}
		}
		return expr;
	}
	
	private boolean isDataItemSupported( int type )
	{
		return iSupportedDataItems == 0
				|| ( iSupportedDataItems & type ) == type;
	}

	private String[] createDataComboItems( )
	{
		List<String> items = new ArrayList<String>( );
		selectDataTypes.clear( );

		if ( isDataItemSupported( SELECT_NONE ) )
		{
			if ( DEUtil.getDataSetList( itemHandle.getContainer( ) )
							.size( ) > 0 )
			{
				items.add( Messages.getString( "ReportDataServiceProvider.Option.Inherits", //$NON-NLS-1$
						( (DataSetHandle) DEUtil.getDataSetList( itemHandle.getContainer( ) )
								.get( 0 ) ).getName( ) ) );
			}
			else
			{
				items.add( ReportDataServiceProvider.OPTION_NONE );
			}
			selectDataTypes.add( Integer.valueOf( SELECT_NONE ) );
		}

		if ( isDataItemSupported( SELECT_DATA_SET ) )
		{
			String[] dataSets = getDataServiceProvider( ).getAllDataSets( );
			if ( dataSets.length > 0 )
			{
				if ( isDataItemSupported( SELECT_NEXT ) )
				{
					items.add( Messages.getString( "StandardChartDataSheet.Combo.DataSets" ) ); //$NON-NLS-1$
					selectDataTypes.add( Integer.valueOf( SELECT_NEXT ) );
				}

				for ( int i = 0; i < dataSets.length; i++ )
				{
					items.add( dataSets[i] );
					selectDataTypes.add( Integer.valueOf( SELECT_DATA_SET ) );
				}
			}
			if ( isDataItemSupported( SELECT_NEW_DATASET ) )
			{
				items.add( Messages.getString( "StandardChartDataSheet.NewDataSet" ) ); //$NON-NLS-1$
				selectDataTypes.add( Integer.valueOf( SELECT_NEW_DATASET ) );
			}
		}

		if ( isDataItemSupported( SELECT_DATA_CUBE ) )
		{
			String[] dataCubes = getDataServiceProvider( ).getAllDataCubes( );
			if ( dataCubes.length > 0 )
			{
				if ( isDataItemSupported( SELECT_NEXT ) )
				{
					items.add( Messages.getString( "StandardChartDataSheet.Combo.DataCubes" ) ); //$NON-NLS-1$
					selectDataTypes.add( Integer.valueOf( SELECT_NEXT ) );
				}
				for ( int i = 0; i < dataCubes.length; i++ )
				{
					items.add( dataCubes[i] );
					selectDataTypes.add( Integer.valueOf( SELECT_DATA_CUBE ) );
				}
			}
			if ( isDataItemSupported( SELECT_NEW_DATACUBE ) )
			{
				items.add( Messages.getString( "StandardChartDataSheet.NewDataCube" ) ); //$NON-NLS-1$
				selectDataTypes.add( Integer.valueOf( SELECT_NEW_DATACUBE ) );
			}
		}

		if ( isDataItemSupported( SELECT_REPORT_ITEM ) )
		{
			String[] dataRefs = getDataServiceProvider( ).getAllReportItemReferences( );
			if ( dataRefs.length > 0 )
			{
				int curSize = items.size( );
				if ( isDataItemSupported( SELECT_NEXT ) )
				{
					items.add( Messages.getString( "StandardChartDataSheet.Combo.ReportItems" ) ); //$NON-NLS-1$
					selectDataTypes.add( Integer.valueOf( SELECT_NEXT ) );
				}
				for ( int i = 0; i < dataRefs.length; i++ )
				{
					// if cube is not supported, do not list the report item
					// consuming a cube
					if ( !isDataItemSupported( SELECT_DATA_CUBE ) )
					{
						if ( ( (ReportItemHandle) getDataServiceProvider( ).getReportDesignHandle( )
								.findElement( dataRefs[i] ) ).getCube( ) != null )
						{
							continue;
						}
					}
					items.add( dataRefs[i] );
					selectDataTypes.add( Integer.valueOf( SELECT_REPORT_ITEM ) );
				}
				// didn't add any reportitem reference
				if ( items.size( ) == curSize + 1 )
				{
					items.remove( curSize );
					selectDataTypes.remove( curSize );
				}
			}
		}
		return items.toArray( new String[items.size( )] );
	}

	private void updatePredefinedQueries( )
	{
		if ( dataProvider.isInXTabMeasureCell( ) )
		{
			try
			{
				CrosstabReportItemHandle xtab = ChartXTabUtil.getXtabContainerCell( itemHandle )
						.getCrosstab( );

				if ( dataProvider.isPartChart( ) )
				{
					List<String> levels = ChartXTabUtil.getAllLevelsBindingExpression( xtab );
					String[] exprs = levels.toArray( new String[levels.size( )] );
					if ( exprs.length == 2 && dataProvider.isInXTabAggrCell( ) )
					{
						// Only one direction is valid for chart in total cell
						if ( ( (ChartWithAxes) getChartModel( ) ).isTransposed( ) )
						{
							exprs = new String[]{
								exprs[1]
							};
						}
						else
						{
							exprs = new String[]{
								exprs[0]
							};
						}
					}
					getContext( ).addPredefinedQuery( ChartUIConstants.QUERY_CATEGORY,
							exprs );
				}
				else
				{
					Iterator<ComputedColumnHandle> columnBindings = ChartXTabUtil.getAllColumnBindingsIterator( itemHandle );
					List<String> levels = ChartXTabUtil.getAllLevelsBindingExpression( columnBindings );
					String[] exprs = levels.toArray( new String[levels.size( )] );
					getContext( ).addPredefinedQuery( ChartUIConstants.QUERY_CATEGORY,
							exprs );
					getContext( ).addPredefinedQuery( ChartUIConstants.QUERY_OPTIONAL,
							exprs );

					columnBindings = ChartXTabUtil.getAllColumnBindingsIterator( itemHandle );
					List<String> measures = ChartXTabUtil.getAllMeasuresBindingExpression( columnBindings );
					exprs = measures.toArray( new String[measures.size( )] );
					getContext( ).addPredefinedQuery( ChartUIConstants.QUERY_VALUE,
							exprs );
				}

			}
			catch ( BirtException e )
			{
				WizardBase.displayException( e );
			}
		}
		else
		{
			if ( getCube( ) == null )
			{
				try
				{
					ColumnBindingInfo[] headers = dataProvider.getPreviewHeadersInfo( );
					getDataServiceProvider( ).setPredefinedExpressions( headers );
				}
				catch ( ChartException e )
				{
					getContext( ).addPredefinedQuery( ChartUIConstants.QUERY_CATEGORY,
							null );
					getContext( ).addPredefinedQuery( ChartUIConstants.QUERY_VALUE,
							null );
					getContext( ).addPredefinedQuery( ChartUIConstants.QUERY_OPTIONAL,
							null );
				}
				
			}
			else if ( isDataItemSupported( SELECT_DATA_CUBE ) )
			{
				if ( dataProvider.isInheritanceOnly( )
						|| dataProvider.isSharedBinding( ) )
				{
					// Get all column bindings.
					List<String> dimensionExprs = new ArrayList<String>( );
					List<String> measureExprs = new ArrayList<String>( );
					ReportItemHandle reportItemHandle = dataProvider.getReportItemHandle( );
					for ( Iterator<ComputedColumnHandle> iter = reportItemHandle.getColumnBindings( )
							.iterator( ); iter.hasNext( ); )
					{
						ComputedColumnHandle cch = iter.next( );
						String dataExpr = ExpressionUtil.createJSDataExpression( cch.getName( ) );
						if ( ChartXTabUtil.isDimensionExpresion( cch.getExpression( ) ) )
						{
							dimensionExprs.add( dataExpr );
						}
						else if ( ChartXTabUtil.isMeasureExpresion( cch.getExpression( ) ) )
						{
							// Fixed issue ED 28.
							// Underlying code was reverted to the earlier than
							// bugzilla 246683, since we have enhanced it to
							// support all available measures defined in shared
							// item.

							// Bugzilla 246683.
							// Here if it is sharing with crosstab or
							// multi-view, we just put the measure expression
							// whose aggregate-ons is most into prepared
							// expression query. It will keep correct value to
							// shared crosstab or multi-view.
							measureExprs.add( dataExpr );

						}
					}
					String[] categoryExprs = dimensionExprs.toArray( new String[dimensionExprs.size( )] );
					String[] yOptionalExprs = categoryExprs;
					String[] valueExprs = measureExprs.toArray( new String[measureExprs.size( )] );

					ReportItemHandle referenceHandle = ChartReportItemUtil.getReportItemReference( itemHandle );
					if ( referenceHandle instanceof ExtendedItemHandle
							&& ChartReportItemUtil.isChartReportItemHandle( referenceHandle ) )
					{
						// If the final reference handle is cube with other
						// chart, the valid category and Y optional expressions
						// only allow those expressions defined in shared chart.
						Chart referenceCM = ChartReportItemUtil.getChartFromHandle( (ExtendedItemHandle) referenceHandle );
						categoryExprs = ChartUtil.getCategoryExpressions( referenceCM );
						yOptionalExprs = ChartUtil.getYOptoinalExpressions( referenceCM );
						valueExprs = ChartUtil.getValueSeriesExpressions( referenceCM );

						Chart cm = getChartModel( );
						if ( categoryExprs.length > 0 )
						{
							updateCategoryExpression( cm, categoryExprs[0] );
						}
						if ( yOptionalExprs.length > 0 )
						{
							updateYOptionalExpressions( cm, yOptionalExprs[0] );
						}
					}
					else if ( dataProvider.checkState( IDataServiceProvider.SHARE_CROSSTAB_QUERY ) )
					{
						// In sharing query with crosstab, the category
						// expression and Y optional expression is decided by
						// value series expression, so here set them to null.
						// And in UI, when the value series expression is
						// selected, it will trigger to set correct category and
						// Y optional expressions.
						categoryExprs = null;
						yOptionalExprs = null;
					}

					getContext( ).addPredefinedQuery( ChartUIConstants.QUERY_CATEGORY,
							categoryExprs );
					getContext( ).addPredefinedQuery( ChartUIConstants.QUERY_OPTIONAL,
							yOptionalExprs );
					getContext( ).addPredefinedQuery( ChartUIConstants.QUERY_VALUE,
							valueExprs );
				}
				// TODO do we need to handle xtab inheritance case? currently we
				// just inherit the cube from xtab essentially
//				else if ( ChartXTabUIUtil.isInheritXTabCell( itemHandle ) )
//				{
//					// Chart in xtab cell and inherits its cube
//					List<String> measureExprs = new ArrayList<String>( );
//					for ( Iterator<ComputedColumnHandle> iter = ChartReportItemUtil.getBindingHolder( itemHandle )
//							.getColumnBindings( )
//							.iterator( ); iter.hasNext( ); )
//					{
//						ComputedColumnHandle cch = iter.next( );
//						if ( ChartXTabUtil.isMeasureExpresion( cch.getExpression( ) ) )
//						{
//							measureExprs.add( ExpressionUtil.createJSDataExpression( cch.getName( ) ) );
//						}
//					}
//					String[] valueExprs = measureExprs.toArray( new String[measureExprs.size( )] );
//					getContext( ).addPredefinedQuery( ChartUIConstants.QUERY_CATEGORY,
//							null );
//					getContext( ).addPredefinedQuery( ChartUIConstants.QUERY_OPTIONAL,
//							null );
//					getContext( ).addPredefinedQuery( ChartUIConstants.QUERY_VALUE,
//							valueExprs );
//				}
				else
				{
					Iterator<ComputedColumnHandle> columnBindings = ChartXTabUtil.getAllColumnBindingsIterator( itemHandle );
					List<String> levels = ChartXTabUtil.getAllLevelsBindingExpression( columnBindings );
					String[] exprs = levels.toArray( new String[levels.size( )] );
					getContext( ).addPredefinedQuery( ChartUIConstants.QUERY_CATEGORY,
							exprs );
					getContext( ).addPredefinedQuery( ChartUIConstants.QUERY_OPTIONAL,
							exprs );

					columnBindings = ChartXTabUtil.getAllColumnBindingsIterator( itemHandle );
					List<String> measures = ChartXTabUtil.getAllMeasuresBindingExpression( columnBindings );
					exprs = measures.toArray( new String[measures.size( )] );
					getContext( ).addPredefinedQuery( ChartUIConstants.QUERY_VALUE,
							exprs );
				}
			}
		}

		// Fire event to update predefined queries in outside UI
		fireEvent( btnBinding, EVENT_QUERY );
	}

	/**
	 * Update Y Optional expression with specified expression if current Y
	 * optional expression is null or empty.
	 * 
	 * @param cm
	 *            chart model.
	 * @param expr
	 *            specified expression.
	 */
	private void updateYOptionalExpressions( Chart cm, String expr )
	{
		List<SeriesDefinition> orthSDs = ChartUtil.getAllOrthogonalSeriesDefinitions( cm );
		for ( SeriesDefinition sd : orthSDs )
		{
			Query q = sd.getQuery( );

			if ( q == null )
			{
				sd.setQuery( QueryImpl.create( expr ) );
				continue;
			}

			if ( q.getDefinition( ) == null
					|| "".equals( q.getDefinition( ).trim( ) ) ) //$NON-NLS-1$
			{
				q.setDefinition( expr );
			}
		}
	}

	/**
	 * Update category expression with specified expression if current category
	 * expression is null or empty.
	 * 
	 * @param cm
	 *            chart model.
	 * @param expr
	 *            specified expression.
	 */
	private void updateCategoryExpression( Chart cm, String expr )
	{
		EList<SeriesDefinition> baseSDs = ChartUtil.getBaseSeriesDefinitions( cm );
		for ( SeriesDefinition sd : baseSDs )
		{
			EList<Query> dds = sd.getDesignTimeSeries( ).getDataDefinition( );
			Query q = dds.get( 0 );
			if ( q.getDefinition( ) == null
					|| "".equals( q.getDefinition( ).trim( ) ) ) //$NON-NLS-1$
			{
				q.setDefinition( expr );
			}
		}
	}
}
