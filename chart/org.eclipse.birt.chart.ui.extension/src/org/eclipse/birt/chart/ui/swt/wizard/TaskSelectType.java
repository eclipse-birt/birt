/*******************************************************************************
 * Copyright (c) 2004, 2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.chart.ui.swt.wizard;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;
import java.util.Map.Entry;

import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.ChartWithoutAxes;
import org.eclipse.birt.chart.model.attribute.AxisType;
import org.eclipse.birt.chart.model.attribute.ChartDimension;
import org.eclipse.birt.chart.model.attribute.DataType;
import org.eclipse.birt.chart.model.attribute.Orientation;
import org.eclipse.birt.chart.model.component.Axis;
import org.eclipse.birt.chart.model.component.MarkerLine;
import org.eclipse.birt.chart.model.component.MarkerRange;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.data.BaseSampleData;
import org.eclipse.birt.chart.model.data.OrthogonalSampleData;
import org.eclipse.birt.chart.model.data.Query;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.model.type.StockSeries;
import org.eclipse.birt.chart.ui.extension.i18n.Messages;
import org.eclipse.birt.chart.ui.swt.ChartPreviewPainter;
import org.eclipse.birt.chart.ui.swt.interfaces.IChartPreviewPainter;
import org.eclipse.birt.chart.ui.swt.interfaces.IChartSubType;
import org.eclipse.birt.chart.ui.swt.interfaces.IChartType;
import org.eclipse.birt.chart.ui.swt.interfaces.IDataServiceProvider;
import org.eclipse.birt.chart.ui.swt.interfaces.ISeriesUIProvider;
import org.eclipse.birt.chart.ui.swt.interfaces.ITaskChangeListener;
import org.eclipse.birt.chart.ui.swt.interfaces.ITaskPreviewable;
import org.eclipse.birt.chart.ui.util.ChartCacheManager;
import org.eclipse.birt.chart.ui.util.ChartHelpContextIds;
import org.eclipse.birt.chart.ui.util.ChartUIConstants;
import org.eclipse.birt.chart.ui.util.ChartUIUtil;
import org.eclipse.birt.chart.ui.util.UIHelper;
import org.eclipse.birt.chart.util.ChartUtil;
import org.eclipse.birt.core.ui.frameworks.taskwizard.SimpleTask;
import org.eclipse.birt.core.ui.frameworks.taskwizard.WizardBase;
import org.eclipse.birt.core.ui.frameworks.taskwizard.interfaces.IWizardContext;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EContentAdapter;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;

/**
 * TaskSelectType
 */
public class TaskSelectType extends SimpleTask implements
		SelectionListener,
		ITaskChangeListener,
		ITaskPreviewable
{

	/**
	 * 
	 * TaskSelectTypeUIDescriptor is used to create UI in misc area according to
	 * the order of index
	 */
	public abstract class TaskSelectTypeUIDescriptor
	{

		private boolean bVisible = true;

		public boolean isVisible( )
		{
			return bVisible;
		}

		public void setVisible( boolean bVisible )
		{
			this.bVisible = bVisible;
		}

		public abstract int getIndex( );

		public abstract void createControl( Composite parent );

	}

	private Chart chartModel = null;

	private ChartAdapter adapter = null;

	private Composite cmpType = null;

	private Composite cmpMisc = null;

	private Composite cmpRight = null;

	private Composite cmpLeft = null;

	private Composite cmpTypeButtons = null;

	private Composite cmpSubTypes = null;

	protected IChartPreviewPainter previewPainter = null;
	private Canvas previewCanvas = null;

	private LinkedHashMap<String, IChartType> htTypes = null;

	private RowData rowData = new RowData( 80, 80 );

	protected String sSubType = null;

	protected String sType = null;

	private String sOldType = null;

	// Stored in IChartType
	protected String sDimension = null;

	private Table table = null;

	private Vector<String> vSubTypeNames = null;

	protected Orientation orientation = null;

	private Label lblOrientation = null;
	private Button cbOrientation = null;

	private Label lblMultipleY = null;
	protected Combo cbMultipleY = null;

	private Label lblSeriesType = null;
	private Combo cbSeriesType = null;

	private Combo cbDimension = null;

	private SashForm foSashForm;

	protected int pageMargin = 80;

	private static final String LEADING_BLANKS = "  "; //$NON-NLS-1$

	private static Hashtable<String, Series> htSeriesNames = null;
	
	private static String[] outputFormats, outputDisplayNames;
	static
	{
		try
		{
			outputFormats = ChartUtil.getSupportedOutputFormats( );
			outputDisplayNames = ChartUtil.getSupportedOutputDisplayNames( );
		}
		catch ( ChartException e )
		{
			WizardBase.displayException( e );
			outputFormats = new String[0];
			outputDisplayNames = new String[0];
		}
	}

	protected List<TaskSelectTypeUIDescriptor> lstDescriptor = new LinkedList<TaskSelectTypeUIDescriptor>( );

	public TaskSelectType( )
	{
		super( Messages.getString( "TaskSelectType.TaskExp" ) ); //$NON-NLS-1$
		setDescription( Messages.getString( "TaskSelectType.Task.Description" ) ); //$NON-NLS-1$

		if ( chartModel != null )
		{
			sType = chartModel.getType( );
			sOldType = sType;
			sSubType = chartModel.getSubType( );
			sDimension = translateDimensionString( chartModel.getDimension( )
					.getName( ) );
			if ( chartModel instanceof ChartWithAxes )
			{
				orientation = ( (ChartWithAxes) chartModel ).getOrientation( );
			}
		}
		htTypes = new LinkedHashMap<String, IChartType>( );
	}

	public void createControl( Composite parent )
	{
		if ( topControl == null || topControl.isDisposed( ) )
		{
			topControl = new Composite( parent, SWT.NONE );
			GridLayout gridLayout = new GridLayout( 2, false );
			gridLayout.marginWidth = pageMargin;
			topControl.setLayout( gridLayout );
			topControl.setLayoutData( new GridData( GridData.GRAB_HORIZONTAL
					| GridData.GRAB_VERTICAL ) );
			if ( context != null )
			{
				chartModel = ( (ChartWizardContext) context ).getModel( );
			}
			placeComponents( );
			updateAdapters( );
		}

		// Update dimension combo and related sub-types in case of axes changed
		// outside
		if ( ( (ChartWizardContext) getContext( ) ).isMoreAxesSupported( ) )
		{
			updateDimensionCombo( sType );
			createAndDisplayTypesSheet( sType );
			setDefaultSubtypeSelection( );
			cmpMisc.layout( );
		}
		doPreview( );
		bindHelp( );
	}

	protected void bindHelp( )
	{
		ChartUIUtil.bindHelp( getControl( ),
				ChartHelpContextIds.TASK_SELECT_TYPE );
	}

	private void placeComponents( )
	{
		foSashForm = new SashForm( topControl, SWT.VERTICAL );
		{
			GridLayout layout = new GridLayout( );
			foSashForm.setLayout( layout );
			GridData gridData = new GridData( GridData.FILL_BOTH );
			// TODO verify Bug 194391 in Linux
			gridData.heightHint = 570;
			foSashForm.setLayoutData( gridData );
		}

		createPreviewArea( );
		createTypeArea( );
		setDefaultTypeSelection( );

		refreshChart( );
		populateSeriesTypesList( );
	}

	private void createPreviewArea( )
	{
		Composite cmpPreview = new Composite( foSashForm, SWT.NONE );
		cmpPreview.setLayout( new GridLayout( ) );

		GridData gridData = new GridData( GridData.FILL_BOTH );
		gridData.horizontalSpan = 2;
		gridData.heightHint = 270;
		cmpPreview.setLayoutData( gridData );

		Label label = new Label( cmpPreview, SWT.NONE );
		{
			label.setText( Messages.getString( "TaskSelectType.Label.Preview" ) ); //$NON-NLS-1$
			label.setFont( JFaceResources.getBannerFont( ) );
		}

		previewCanvas = new Canvas( cmpPreview, SWT.BORDER );
		previewCanvas.setLayoutData( new GridData( GridData.FILL_BOTH ) );
		previewCanvas.setBackground( Display.getDefault( )
				.getSystemColor( SWT.COLOR_WIDGET_BACKGROUND ) );

		previewPainter = createPreviewPainter( );
	}

	private void createTypeArea( )
	{
		ScrolledComposite sc = new ScrolledComposite( foSashForm, SWT.V_SCROLL );
		{
			GridLayout layout = new GridLayout( );
			sc.setLayout( layout );
			GridData gridData = new GridData( GridData.FILL_HORIZONTAL );
			sc.setLayoutData( gridData );
			sc.setExpandHorizontal( true );
			sc.setExpandVertical( true );
		}

		cmpType = new Composite( sc, SWT.NONE );
		cmpType.setLayout( new GridLayout( 2, false ) );
		cmpType.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
		sc.setContent( cmpType );

		createTypeTable( );
		addChartTypes( );

		createDetails( );

		Point size = cmpType.computeSize( SWT.DEFAULT, SWT.DEFAULT );
		sc.setMinSize( size );
	}

	private void createDetails( )
	{
		cmpRight = new Composite( cmpType, SWT.NONE );
		cmpRight.setLayout( new GridLayout( ) );
		cmpRight.setLayoutData( new GridData( GridData.FILL_BOTH ) );
		createComposite( new Vector<IChartSubType>( ) );
		createMiscArea( );
	}

	private void createMiscArea( )
	{
		cmpMisc = new Composite( cmpRight, SWT.NONE );
		cmpMisc.setLayout( new GridLayout( 4, false ) );
		cmpMisc.setLayoutData( new GridData( GridData.FILL_BOTH ) );

		addTypeUIDescriptor( new TaskSelectTypeUIDescriptor( ) {

			public int getIndex( )
			{
				return 10;
			}

			public void createControl( Composite parent )
			{
				Label lblDimension = new Label( parent, SWT.WRAP );
				{
					GridData gd = new GridData( GridData.FILL_HORIZONTAL );
					lblDimension.setLayoutData( gd );
					lblDimension.setText( Messages.getString( "TaskSelectType.Label.Dimension" ) ); //$NON-NLS-1$
				}

				// Add the ComboBox for Dimensions
				cbDimension = new Combo( parent, SWT.DROP_DOWN | SWT.READ_ONLY );
				{
					GridData gd = new GridData( GridData.FILL_HORIZONTAL );
					cbDimension.setLayoutData( gd );
					cbDimension.addSelectionListener( TaskSelectType.this );
				}
			}
		} );

		addTypeUIDescriptor( new TaskSelectTypeUIDescriptor( ) {

			public int getIndex( )
			{
				return 30;
			}

			public void createControl( Composite parent )
			{
				lblMultipleY = new Label( parent, SWT.WRAP );
				{
					GridData gd = new GridData( GridData.FILL_HORIZONTAL );
					lblMultipleY.setLayoutData( gd );
					lblMultipleY.setText( Messages.getString( "TaskSelectType.Label.MultipleYAxis" ) ); //$NON-NLS-1$
				}

				// Add the checkBox for Multiple Y Axis
				cbMultipleY = new Combo( parent, SWT.DROP_DOWN | SWT.READ_ONLY );
				{
					cbMultipleY.setItems( new String[]{
							Messages.getString( "TaskSelectType.Selection.None" ), //$NON-NLS-1$
							Messages.getString( "TaskSelectType.Selection.SecondaryAxis" ), //$NON-NLS-1$
							Messages.getString( "TaskSelectType.Selection.MoreAxes" ) //$NON-NLS-1$
					} );
					GridData gd = new GridData( GridData.FILL_HORIZONTAL );
					cbMultipleY.setLayoutData( gd );
					cbMultipleY.addSelectionListener( TaskSelectType.this );

					int axisNum = ChartUIUtil.getOrthogonalAxisNumber( chartModel );
					selectMultipleAxis( axisNum );
				}
			}
		} );

		addTypeUIDescriptor( new TaskSelectTypeUIDescriptor( ) {

			public int getIndex( )
			{
				return 40;
			}

			public void createControl( Composite parent )
			{
				lblSeriesType = new Label( parent, SWT.WRAP );
				{
					GridData gd = new GridData( GridData.FILL_HORIZONTAL );
					gd.horizontalIndent = 10;
					lblSeriesType.setLayoutData( gd );
					lblSeriesType.setText( Messages.getString( "TaskSelectType.Label.SeriesType" ) ); //$NON-NLS-1$
					lblSeriesType.setEnabled( false );
				}

				// Add the ComboBox for Series Type
				cbSeriesType = new Combo( parent, SWT.DROP_DOWN | SWT.READ_ONLY );
				{
					GridData gd = new GridData( GridData.FILL_HORIZONTAL );
					cbSeriesType.setLayoutData( gd );
					cbSeriesType.setEnabled( false );
					cbSeriesType.addSelectionListener( TaskSelectType.this );
				}
			}
		} );

		addTypeUIDescriptor( new TaskSelectTypeUIDescriptor( ) {

			public int getIndex( )
			{
				return 50;
			}

			public void createControl( Composite parent )
			{
				lblOrientation = new Label( parent, SWT.WRAP );
				{
					GridData gd = new GridData( GridData.FILL_HORIZONTAL );
					lblOrientation.setLayoutData( gd );
					lblOrientation.setText( Messages.getString( "TaskSelectType.Label.Oritention" ) ); //$NON-NLS-1$
				}

				// Add the CheckBox for Orientation
				cbOrientation = new Button( parent, SWT.CHECK );
				{
					cbOrientation.setText( Messages.getString( "TaskSelectType.Label.FlipAxis" ) ); //$NON-NLS-1$
					GridData gd = new GridData( );
					cbOrientation.setLayoutData( gd );
					cbOrientation.addSelectionListener( TaskSelectType.this );
				}

				if ( TaskSelectType.this.orientation == Orientation.HORIZONTAL_LITERAL )
				{
					cbOrientation.setSelection( true );
				}
				else
				{
					cbOrientation.setSelection( false );
				}
			}
		} );

		addOptionalUIDescriptor( );

		createUIDescriptors( cmpMisc );
	}

	/**
	 * This method initializes table
	 * 
	 */
	private void createTypeTable( )
	{
		cmpLeft = new Composite( cmpType, SWT.NONE );
		cmpLeft.setLayout( new GridLayout( ) );
		cmpLeft.setLayoutData( new GridData( GridData.FILL_BOTH ) );

		Label lblTypes = new Label( cmpLeft, SWT.WRAP );
		{
			GridData gd = new GridData( GridData.FILL_HORIZONTAL );
			lblTypes.setLayoutData( gd );
			lblTypes.setText( Messages.getString( "TaskSelectType.Label.SelectChartType" ) ); //$NON-NLS-1$
		}

		table = new Table( cmpLeft, SWT.BORDER );
		{
			GridData gd = new GridData( GridData.FILL_BOTH );
			table.setLayoutData( gd );
			table.setToolTipText( Messages.getString( "TaskSelectType.Label.ChartTypes" ) ); //$NON-NLS-1$
			table.addSelectionListener( this );
		}
	}

	/**
	 * 
	 */
	private void addChartTypes( )
	{
		populateTypesTable( );
		updateUI( );
	}

	/**
	 * 
	 */
	private void populateTypesTable( )
	{
		htTypes.clear( );

		Collection<IChartType> cTypes = ChartUIExtensionsImpl.instance( )
				.getUIChartTypeExtensions( getContext( ).getClass( )
						.getSimpleName( ) );
		Iterator<IChartType> iterTypes = cTypes.iterator( );
		while ( iterTypes.hasNext( ) )
		{
			IChartType type = iterTypes.next( );
			// Only support enabled chart types
			if ( ( (ChartWizardContext) context ).isEnabled( type.getName( ) ) )
			{
				htTypes.put( type.getName( ), type );
			}
		}
	}

	private void updateUI( )
	{
		Iterator<String> iter = htTypes.keySet( ).iterator( );
		while ( iter.hasNext( ) )
		{
			String sTypeTmp = iter.next( );
			TableItem tItem = new TableItem( table, SWT.NONE );
			tItem.setText( LEADING_BLANKS
					+ ( htTypes.get( sTypeTmp ) ).getDisplayName( ) );
			tItem.setData( ( htTypes.get( sTypeTmp ) ).getName( ) );
			tItem.setImage( ( htTypes.get( sTypeTmp ) ).getImage( ) );
		}
	}
	
	protected Chart getChartModel( )
	{
		return chartModel;
	}

	/**
	 * This method initializes cmpSubTypes
	 * 
	 */
	private void createComposite( Vector<IChartSubType> vSubTypes )
	{
		Label lblSubtypes = new Label( cmpRight, SWT.NO_FOCUS );
		{
			lblSubtypes.setText( Messages.getString( "TaskSelectType.Label.SelectSubtype" ) ); //$NON-NLS-1$
			GridData gd = new GridData( GridData.FILL_HORIZONTAL );
			gd.horizontalIndent = 5;
			lblSubtypes.setLayoutData( gd );
		}

		GridData gdTypes = new GridData( GridData.FILL_HORIZONTAL
				| GridData.VERTICAL_ALIGN_FILL );
		cmpSubTypes = new Composite( cmpRight, SWT.NONE );
		createGroups( vSubTypes );
		cmpSubTypes.setLayoutData( gdTypes );
		cmpSubTypes.setToolTipText( Messages.getString( "TaskSelectType.Label.ChartSubtypes" ) ); //$NON-NLS-1$
		cmpSubTypes.setLayout( new GridLayout( ) );
		cmpSubTypes.setVisible( true );
	}

	/**
	 * This method initializes cmpTypeButtons
	 * 
	 */
	private void createGroups( Vector<IChartSubType> vSubTypes )
	{
		vSubTypeNames = new Vector<String>( );
		if ( cmpTypeButtons != null && !cmpTypeButtons.isDisposed( ) )
		{
			// Remove existing buttons
			cmpTypeButtons.dispose( );
		}
		cmpTypeButtons = new Composite( cmpSubTypes, SWT.NONE );
		RowLayout rowLayout = new RowLayout( );
		rowLayout.marginTop = 0;
		rowLayout.marginLeft = 0;
		rowLayout.marginBottom = 12;
		rowLayout.marginRight = 12;
		rowLayout.spacing = 4;
		cmpTypeButtons.setLayout( rowLayout );

		// Add new buttons for this type
		for ( int iC = 0; iC < vSubTypes.size( ); iC++ )
		{
			IChartSubType subType = vSubTypes.get( iC );
			vSubTypeNames.add( subType.getName( ) );
			Button btnType = new Button( cmpTypeButtons, SWT.TOGGLE );
			btnType.setData( subType.getName( ) );
			btnType.setImage( subType.getImage( ) );
			btnType.setLayoutData( rowData );
			btnType.addSelectionListener( this );
			btnType.setToolTipText( subType.getDescription( ) );
			btnType.getImage( ).setBackground( btnType.getBackground( ) );
			btnType.setVisible( true );
			cmpTypeButtons.layout( true );
			
			if ( getDataServiceProvider( ).checkState( IDataServiceProvider.PART_CHART ) )
			{
				// Only support the first sub-type in xtab part case.
				break;
			}
		}
		cmpTypeButtons.setLayoutData( new GridData( GridData.FILL_BOTH ) );
		cmpSubTypes.layout( true );
	}

	private void populateSeriesTypes( Collection<IChartType> allChartType,
			Series series, Orientation orientation )
	{
		Iterator<IChartType> iterTypes = allChartType.iterator( );
		while ( iterTypes.hasNext( ) )
		{
			IChartType type = iterTypes.next( );
			Series newSeries = type.getSeries( );

			if ( htSeriesNames == null )
			{
				htSeriesNames = new Hashtable<String, Series>( 20 );
			}

			if ( type.canCombine( ) )
			{
				// Horizontal Stock is not supported and can't be mixed with
				// other charts
				if ( !( newSeries instanceof StockSeries )
						|| ( orientation.getValue( ) == Orientation.VERTICAL ) )
				{
					String sDisplayName = newSeries.getDisplayName( );
					htSeriesNames.put( sDisplayName, newSeries );
					cbSeriesType.add( sDisplayName );
				}
				if ( type.getName( ).equals( chartModel.getType( ) ) )
				{
					cbSeriesType.select( cbSeriesType.getItemCount( ) - 1 );
				}
			}
		}
	}

	/*
	 * This method translates the dimension string from the model to that
	 * maintained by the UI (for readability).
	 */
	private String translateDimensionString( String sDimensionValue )
	{
		String dimensionName = ""; //$NON-NLS-1$
		if ( sDimensionValue.equals( ChartDimension.TWO_DIMENSIONAL_LITERAL.getName( ) ) )
		{
			dimensionName = IChartType.TWO_DIMENSION_TYPE;
		}
		else if ( sDimensionValue.equals( ChartDimension.TWO_DIMENSIONAL_WITH_DEPTH_LITERAL.getName( ) ) )
		{
			dimensionName = IChartType.TWO_DIMENSION_WITH_DEPTH_TYPE;
		}
		else if ( sDimensionValue.equals( ChartDimension.THREE_DIMENSIONAL_LITERAL.getName( ) ) )
		{
			dimensionName = IChartType.THREE_DIMENSION_TYPE;
		}
		return dimensionName;
	}

	public void widgetDefaultSelected( SelectionEvent e )
	{
	}

	public void widgetSelected( SelectionEvent e )
	{
		// Indicates whether need to update chart model
		boolean needUpdateModel = false;
		Object oSelected = e.getSource( );
		if ( oSelected.getClass( ).equals( Button.class ) )
		{
			needUpdateModel = true;

			if ( oSelected.equals( cbOrientation ) )
			{
				if ( cbOrientation.getSelection( ) )
				{
					orientation = Orientation.HORIZONTAL_LITERAL;
				}
				else
				{
					orientation = Orientation.VERTICAL_LITERAL;
				}
				createAndDisplayTypesSheet( sType );
				setDefaultSubtypeSelection( );
				populateSeriesTypesList( );
				ChartCacheManager.getInstance( ).cacheOrientation( sType,
						orientation );
			}
			else
			{
				Button btn = (Button) e.getSource( );
				if ( btn.getSelection( ) )
				{
					if ( this.sSubType != null
							&& !getSubtypeFromButton( btn ).equals( sSubType ) )
					{
						int iTypeIndex = vSubTypeNames.indexOf( sSubType );
						if ( iTypeIndex >= 0 )
						{
							( (Button) cmpTypeButtons.getChildren( )[iTypeIndex] ).setSelection( false );
							cmpTypeButtons.redraw( );
						}
					}

					// Cache label position for stacked or non-stacked case.
					ChartUIUtil.saveLabelPositionIntoCache( getSeriesDefinitionForProcessing( ) );

					sSubType = getSubtypeFromButton( btn );
					ChartCacheManager.getInstance( ).cacheSubtype( sType,
							sSubType );
				}
				else
				{
					if ( this.sSubType != null
							&& getSubtypeFromButton( btn ).equals( sSubType ) )
					{
						// Clicking on the same button should not cause it to be
						// unselected
						btn.setSelection( true );

						// Disable the statement to avoid when un-check all
						// stacked attributes of series on format tab, the
						// default chart is painted as side-by-side, but it
						// can't select stacked button to change chart type to
						// stacked in chart type tab.
						// needUpdateModel = false;
					}
				}
			}
		}
		else if ( oSelected.getClass( ).equals( Table.class ) )
		{
			sType = ( (String) ( (TableItem) e.item ).getData( ) ).trim( );
			if ( !sOldType.equals( sType ) )
			{
				sOldType = sType;

				// Get orientation for non-xtab case. In xtab, orientation won't
				// be changed
				if ( !getDataServiceProvider( ).checkState( IDataServiceProvider.PART_CHART ) )
				{
					// Get the cached orientation
					if ( chartModel != null
							&& chartModel instanceof ChartWithAxes )
					{
						Orientation lastOrientation = ChartCacheManager.getInstance( )
								.findOrientation( sType );

						if ( lastOrientation != null
								&& this.orientation != lastOrientation )
						{
							this.orientation = lastOrientation;
							this.rotateAxisTitle( (ChartWithAxes) chartModel );
						}
						if ( lastOrientation == null )
						{
							Orientation currentOrientation = this.orientation;
							this.orientation = htTypes.get( sType )
									.getDefaultOrientation( );
							if ( currentOrientation != this.orientation )
							{
								this.rotateAxisTitle( (ChartWithAxes) chartModel );
							}
						}
					}
				}

				if ( chartModel != null
						&& chartModel instanceof ChartWithAxes
						&& ChartCacheManager.getInstance( )
								.findCategory( sType ) != null )
				{
					boolean bCategory = ChartCacheManager.getInstance( )
							.findCategory( sType )
							.booleanValue( );
					( (ChartWithAxes) chartModel ).getAxes( ).get( 0 ).setCategoryAxis( bCategory );
				}
				sSubType = null;
				createAndDisplayTypesSheet( sType );
				setDefaultSubtypeSelection( );

				cmpMisc.layout( );

				needUpdateModel = true;
			}
		}
		else if ( oSelected.equals( cbMultipleY ) )
		{
			needUpdateModel = true;
			lblSeriesType.setEnabled( isTwoAxesEnabled( ) );

			Axis xAxis = ( (ChartWithAxes) chartModel ).getAxes( ).get( 0 );

			( (ChartWizardContext) getContext( ) ).setMoreAxesSupported( cbMultipleY.getSelectionIndex( ) == 2 );

			if ( chartModel instanceof ChartWithoutAxes )
			{
				throw new IllegalArgumentException( Messages.getString( "TaskSelectType.Exception.CannotSupportAxes" ) ); //$NON-NLS-1$
			}

			// Prevent notifications rendering preview
			ChartAdapter.beginIgnoreNotifications( );
			int iAxisNumber = ChartUIUtil.getOrthogonalAxisNumber( chartModel );
			if ( cbMultipleY.getSelectionIndex( ) == 0 )
			{
				// Remove series type cache
				ChartCacheManager.getInstance( ).cacheSeriesType( null );

				// Keeps one axis
				if ( iAxisNumber > 1 )
				{
					ChartUIUtil.removeLastAxes( (ChartWithAxes) chartModel,
							iAxisNumber - 1 );
				}
			}
			else if ( cbMultipleY.getSelectionIndex( ) == 1 )
			{
				// Keeps two axes
				if ( iAxisNumber == 1 )
				{
					ChartUIUtil.addAxis( (ChartWithAxes) chartModel );
				}
				else if ( iAxisNumber > 2 )
				{
					ChartUIUtil.removeLastAxes( (ChartWithAxes) chartModel,
							iAxisNumber - 2 );
				}
			}
			ChartAdapter.endIgnoreNotifications( );

			if ( xAxis.getAssociatedAxes( ).size( ) > 1 )
			{
				String lastSeriesType = ChartCacheManager.getInstance( )
						.findSeriesType( );
				if ( lastSeriesType != null )
				{
					cbSeriesType.setText( lastSeriesType );
				}
				else
				{
					Axis overlayAxis = xAxis.getAssociatedAxes( ).get( 1 );
					String sDisplayName = overlayAxis.getSeriesDefinitions( )
							.get( 0 )
							.getDesignTimeSeries( )
							.getDisplayName( );
					cbSeriesType.setText( sDisplayName );
				}
				changeOverlaySeriesType( );
			}
			cbSeriesType.setEnabled( isTwoAxesEnabled( ) );

			// Update dimension combo and related sub-types
			if ( updateDimensionCombo( sType ) )
			{
				createAndDisplayTypesSheet( sType );
				setDefaultSubtypeSelection( );
			}

			// Pack to display enough space for combo
			cmpMisc.layout( );
		}
		else if ( oSelected.equals( cbDimension ) )
		{
			String newDimension = cbDimension.getItem( cbDimension.getSelectionIndex( ) );
			if ( !newDimension.equals( sDimension ) )
			{
				sDimension = newDimension;
				ChartCacheManager.getInstance( ).cacheDimension( sType,
						sDimension );
				createAndDisplayTypesSheet( sType );
				setDefaultSubtypeSelection( );

				needUpdateModel = true;
			}
		}
		else if ( oSelected.equals( cbSeriesType ) )
		{
			// if ( !cbSeriesType.getText( ).equals( oldSeriesName ) )
			// {
			needUpdateModel = true;
			changeOverlaySeriesType( );
			// }
		}

		// Following operations need new model
		if ( needUpdateModel )
		{
			// Update apply button
			ChartAdapter.notifyUpdateApply( );
			// Update chart model
			refreshChart( );

			if ( oSelected.getClass( ).equals( Table.class ) )
			{
				// Ensure populate list after chart model generated
				populateSeriesTypesList( );
			}
			else if ( oSelected.equals( cbOrientation ) )
			{
				// Auto rotates Axis title when transposing
				if ( chartModel instanceof ChartWithAxes )
				{
					rotateAxisTitle( (ChartWithAxes) chartModel );
				}
			}
			// Preview after all model changes
			doPreview( );
		}
	}

	/**
	 * Updates the dimension combo according to chart type and axes number
	 * 
	 * @param sSelectedType
	 *            Chart type
	 * @return whether the dimension is changed after updating
	 */
	private boolean updateDimensionCombo( String sSelectedType )
	{
		// Remember last selection
		boolean isOldExist = false;

		// Update valid dimension list
		IChartType chartType = htTypes.get( sSelectedType );
		String[] dimensionArray = chartType.getSupportedDimensions( );
		int axesNum = ChartUIUtil.getOrthogonalAxisNumber( chartModel );

		if ( sDimension == null )
		{
			// Initialize dimension
			sDimension = chartType.getDefaultDimension( );
			isOldExist = true;
		}
		cbDimension.removeAll( );
		for ( int i = 0; i < dimensionArray.length; i++ )
		{
			boolean isSupported = chartType.isDimensionSupported( dimensionArray[i],
					(ChartWizardContext) context,
					axesNum,
					0 );
			if ( isSupported )
			{
				cbDimension.add( dimensionArray[i] );
			}
			if ( !isOldExist && sDimension.equals( dimensionArray[i] ) )
			{
				isOldExist = isSupported;
			}
		}
		
		String cache = ChartCacheManager.getInstance( )
				.getDimension( sSelectedType );
		if ( cache != null )
		{
			sDimension = cache;
			isOldExist = true;
		}

		// Select the previous selection or the default
		if ( !isOldExist )
		{
			sDimension = chartType.getDefaultDimension( );
		}
		cbDimension.setText( sDimension );
		return !isOldExist;
	}

	private boolean isTwoAxesEnabled( )
	{
		return cbMultipleY.getSelectionIndex( ) == 1;
	}

	/**
	 * Returns the object which can add adapters
	 * 
	 * @return chart model object to add adapters
	 */
	protected EObject getChartModelObject( )
	{
		return ( (ChartWizardContext) context ).getModel( );
	}
	
	private void updateAdapters( )
	{
		EObject model = getChartModelObject( );
		
		if ( container instanceof ChartWizard )
		{
			// Refresh all adapters
			EContentAdapter adapter = ( (ChartWizard) container ).getAdapter( );

			model.eAdapters( ).remove( adapter );
			TreeIterator<EObject> iterator = model.eAllContents( );
			while ( iterator.hasNext( ) )
			{
				EObject oModel = iterator.next( );
				oModel.eAdapters( ).remove( adapter );
			}
			model.eAdapters( ).add( adapter );
		}
		else
		{
			// For extension case, create an adapter and add change listener
			EList<Adapter> adapters = model.eAdapters( );
			if ( adapters.isEmpty( ) )
			{
				// Get the previous adapter if existent
				if ( adapter == null )
				{
					adapter = new ChartAdapter( container );
					adapter.addListener( this );
				}
				adapters.add( adapter );
			}
			else
			{
				if ( adapters.get( 0 ) instanceof ChartAdapter )
				{
					( (ChartAdapter) adapters.get( 0 ) ).addListener( this );
				}
			}
		}
	}

	private boolean is3D( )
	{
		return IChartType.THREE_DIMENSION_TYPE.equals( sDimension );
	}

	private void changeOverlaySeriesType( )
	{
		// cache the second axis series type if it can be combined.
		if ( getCurrentChartType( ).canCombine( ) )
		{
			ChartCacheManager.getInstance( )
					.cacheSeriesType( cbSeriesType.getText( ) );
		}
		try
		{
			// CHANGE ALL OVERLAY SERIES TO NEW SELECTED TYPE
			Axis XAxis = ( (ChartWithAxes) chartModel ).getAxes( )
					.get( 0 );
			int iSeriesDefinitionIndex = 0 + ( XAxis.getAssociatedAxes( )
					.get( 0 ) ).getSeriesDefinitions( ).size( ); // SINCE
			// THIS IS FOR THE ORTHOGONAL OVERLAY SERIES DEFINITION
			int iOverlaySeriesCount = ( XAxis.getAssociatedAxes( )
					.get( 1 ) ).getSeriesDefinitions( ).size( );
			// DISABLE NOTIFICATIONS WHILE MODEL UPDATE TAKES PLACE
			ChartAdapter.beginIgnoreNotifications( );
			for ( int i = 0; i < iOverlaySeriesCount; i++ )
			{
				Series lastSeries = ( ( XAxis.getAssociatedAxes( ).get( 1 ) ).getSeriesDefinitions( ).get( i ) ).getDesignTimeSeries( );
				if ( !lastSeries.getDisplayName( )
						.equals( cbSeriesType.getText( ) ) )
				{
					Series newSeries = htSeriesNames.get( cbSeriesType.getText( ) )
							.copyInstance( );
					newSeries.translateFrom( lastSeries,
							iSeriesDefinitionIndex,
							chartModel );
					// ADD THE MODEL ADAPTERS TO THE NEW SERIES
					newSeries.eAdapters( ).addAll( chartModel.eAdapters( ) );
					// UPDATE THE SERIES DEFINITION WITH THE SERIES INSTANCE
					( ( XAxis.getAssociatedAxes( ).get( 1 ) ).getSeriesDefinitions( ).get( i ) ).getSeries( )
							.clear( );
					( ( XAxis.getAssociatedAxes( ).get( 1 ) ).getSeriesDefinitions( ).get( i ) ).getSeries( )
							.add( newSeries );
					ChartUIUtil.setSeriesName( chartModel );
				}
			}
			ChartWizard.removeException( ChartWizard.TaskSelType_chOvST_ID );
		}
		catch ( Exception e )
		{
			ChartWizard.showException( ChartWizard.TaskSelType_chOvST_ID,
					e.getLocalizedMessage( ) );
		}
		finally
		{
			// ENABLE NOTIFICATIONS IN CASE EXCEPTIONS OCCUR
			ChartAdapter.endIgnoreNotifications( );
		}
	}

	private void populateSeriesTypesList( )
	{
		if ( cbSeriesType == null )
		{
			return;
		}
		
		// Populate Series Types List
		cbSeriesType.removeAll( );
		Series series = getSeriesDefinitionForProcessing( ).getDesignTimeSeries( );
		if ( getCurrentChartType( ).canCombine( ) )
		{
			populateSeriesTypes( ChartUIExtensionsImpl.instance( )
					.getUIChartTypeExtensions( getContext( ).getClass( )
							.getSimpleName( ) ), series, this.orientation );
		}
		else
		{
			String seriesName = series.getDisplayName( );
			cbSeriesType.add( seriesName );
			cbSeriesType.select( 0 );
		}

		// Select the appropriate current series type if overlay series exists
		if ( this.chartModel instanceof ChartWithAxes )
		{
			Axis xAxis = ( ( (ChartWithAxes) chartModel ).getAxes( )
					.get( 0 ) );
			if ( xAxis.getAssociatedAxes( ).size( ) > 1 )
			{
				// Set series name from cache or model
				String lastType = ChartCacheManager.getInstance( )
						.findSeriesType( );

				Axis overlayAxis = xAxis.getAssociatedAxes( ).get( 1 );
				if ( !overlayAxis.getSeriesDefinitions( ).isEmpty( ) )
				{
					Series oseries = ( overlayAxis.getSeriesDefinitions( )
							.get( 0 ) ).getDesignTimeSeries( );
					String sDisplayName = oseries.getDisplayName( );
					if ( lastType != null )
					{
						cbSeriesType.setText( lastType );
					}
					else
					{
						cbSeriesType.setText( sDisplayName );
					}

					String seriesName = oseries.getSeriesIdentifier( )
							.toString( );
					if ( seriesName.trim( ).length( ) != 0 )
					{
						Iterator<Entry<String, Series>> itr = htSeriesNames.entrySet( )
								.iterator( );
						while ( itr.hasNext( ) )
						{
							Entry<String, Series> entry = itr.next( );
							entry.getValue( ).setSeriesIdentifier( seriesName );
						}
					}

				}

				// Update overlay series
				changeOverlaySeriesType( );
			}
		}
	}

	/**
	 * This method populates the subtype panel (creating its components if
	 * necessary). It gets called when the type selection changes or when the
	 * dimension selection changes (since not all sub types are supported for
	 * all dimension selections).
	 * 
	 * @param sSelectedType
	 *            Type from Type List
	 */
	private void createAndDisplayTypesSheet( String sSelectedType )
	{
		IChartType chartType = htTypes.get( sSelectedType );
		if ( cbOrientation != null )
		{
			lblOrientation.setEnabled( chartType.supportsTransposition( )
					&& !is3D( ) );
			cbOrientation.setEnabled( chartType.supportsTransposition( )
					&& !is3D( ) );
		}

		// Update dimension
		updateDimensionCombo( sSelectedType );

		if ( this.sDimension == null )
		{
			this.sDimension = chartType.getDefaultDimension( );
		}
		if ( this.orientation == null )
		{
			this.orientation = chartType.getDefaultOrientation( );
		}

		// Show the subtypes for the selected type based on current selections
		// of dimension and orientation
		Vector<IChartSubType> vSubTypes = new Vector<IChartSubType>( chartType.getChartSubtypes( sDimension,
				orientation ) );

		if ( vSubTypes.size( ) == 0 )
		{
			vSubTypes = new Vector<IChartSubType>( chartType.getChartSubtypes( chartType.getDefaultDimension( ),
					chartType.getDefaultOrientation( ) ) );
			this.sDimension = chartType.getDefaultDimension( );
			this.orientation = chartType.getDefaultOrientation( );
		}

		// If two orientations are not supported, to get the default.
		if ( cbOrientation == null || !cbOrientation.isEnabled( ) )
		{
			this.orientation = chartType.getDefaultOrientation( );
		}
		// Cache the orientation for each chart type.
		ChartCacheManager.getInstance( ).cacheOrientation( sType, orientation );

		if ( chartModel == null )
		{
			ChartCacheManager.getInstance( ).cacheCategory( sType, true );
		}
		else if ( chartModel instanceof ChartWithAxes )
		{
			ChartCacheManager.getInstance( )
					.cacheCategory( sType,
							( ( (ChartWithAxes) chartModel ).getAxes( )
									.get( 0 ) ).isCategoryAxis( ) );
		}

		// Update the UI with information for selected type
		createGroups( vSubTypes );
		if ( this.cbOrientation != null )
		{
			if ( this.orientation == Orientation.HORIZONTAL_LITERAL )
			{
				this.cbOrientation.setSelection( true );
			}
			else
			{
				this.cbOrientation.setSelection( false );
			}
		}
		cmpRight.layout( );
	}

	private void setDefaultSubtypeSelection( )
	{
		if ( sSubType == null )
		{
			// Try to get cached subtype
			sSubType = ChartCacheManager.getInstance( ).findSubtype( sType );
		}

		if ( sSubType == null )
		{
			// Get the default subtype
			( (Button) cmpTypeButtons.getChildren( )[0] ).setSelection( true );
			sSubType = getSubtypeFromButton( cmpTypeButtons.getChildren( )[0] );
			ChartCacheManager.getInstance( ).cacheSubtype( sType, sSubType );
		}
		else
		{
			Control[] buttons = cmpTypeButtons.getChildren( );
			boolean bSelected = false;
			for ( int iB = 0; iB < buttons.length; iB++ )
			{
				if ( getSubtypeFromButton( buttons[iB] ).equals( sSubType ) )
				{
					( (Button) buttons[iB] ).setSelection( true );
					bSelected = true;
					break;
				}
			}
			// If specified subType is not found, select default
			if ( !bSelected )
			{
				( (Button) cmpTypeButtons.getChildren( )[0] ).setSelection( true );
				sSubType = getSubtypeFromButton( cmpTypeButtons.getChildren( )[0] );
				ChartCacheManager.getInstance( ).cacheSubtype( sType, sSubType );
			}
		}

		cmpTypeButtons.redraw( );
	}

	private void setDefaultTypeSelection( )
	{
		if ( table.getItems( ).length > 0 )
		{
			if ( sType == null )
			{
				table.select( 0 );
				sType = (String) ( table.getSelection( )[0] ).getData( );
			}
			else
			{
				TableItem[] tiAll = table.getItems( );
				for ( int iTI = 0; iTI < tiAll.length; iTI++ )
				{
					if ( tiAll[iTI].getData( ).equals( sType ) )
					{
						table.select( iTI );
						break;
					}
				}
			}
			sOldType = sType;
			createAndDisplayTypesSheet( sType );
			setDefaultSubtypeSelection( );
		}
	}

	public void dispose( )
	{
		super.dispose( );

		lstDescriptor.clear( );

		chartModel = null;
		adapter = null;
		if ( previewPainter != null )
		{
			previewPainter.dispose( );
		}
		previewPainter = null;
		sSubType = null;
		sType = null;
		sDimension = null;
		vSubTypeNames = null;
		orientation = null;
	}

	private void refreshChart( )
	{
		// DISABLE PREVIEW REFRESH DURING CONVERSION
		ChartAdapter.beginIgnoreNotifications( );
		IChartType chartType = htTypes.get( sType );
		try
		{
			chartModel = chartType.getModel( sSubType,
					this.orientation,
					this.sDimension,
					this.chartModel );
			
			( (ChartWizardContext) context ).setModel( chartModel );
			( (ChartWizardContext) context ).setChartType( chartType );
			
			updateAdapters( );
			ChartWizard.removeException( ChartWizard.TaskSelType_refreCh_ID );
		}
		catch ( Exception e )
		{
			ChartWizard.showException( ChartWizard.TaskSelType_refreCh_ID,
					e.getLocalizedMessage( ) );
		}

		// RE-ENABLE PREVIEW REFRESH
		ChartAdapter.endIgnoreNotifications( );

		updateSelection( );
		( (ChartWizardContext) context ).setModel( chartModel );
		( (ChartWizardContext) context ).setChartType( chartType );
		setContext( context );
	}

	private SeriesDefinition getSeriesDefinitionForProcessing( )
	{
		// TODO Attention: all index is 0
		SeriesDefinition sd = null;
		if ( chartModel instanceof ChartWithAxes )
		{
			sd = ( (ChartWithAxes) chartModel ).getAxes( )
					.get( 0 )
					.getAssociatedAxes( )
					.get( 0 )
					.getSeriesDefinitions( )
					.get( 0 );
		}
		else if ( chartModel instanceof ChartWithoutAxes )
		{
			sd = ( ( (ChartWithoutAxes) chartModel ).getSeriesDefinitions( ).get( 0 ) ).getSeriesDefinitions( )
					.get( 0 );
		}
		return sd;
	}

	/**
	 * Updates UI selection according to Chart type
	 * 
	 */
	protected void updateSelection( )
	{
		boolean bOutXtab = !getDataServiceProvider( ).checkState( IDataServiceProvider.PART_CHART );
		if ( chartModel instanceof ChartWithAxes )
		{
			if ( cbMultipleY != null )
			{
				lblMultipleY.setEnabled( bOutXtab && !is3D( ) );
				cbMultipleY.setEnabled( bOutXtab && !is3D( ) );
			}
			if ( cbSeriesType != null )
			{
				lblSeriesType.setEnabled( bOutXtab && isTwoAxesEnabled( ) );
				cbSeriesType.setEnabled( bOutXtab && isTwoAxesEnabled( ) );
			}
		}
		else
		{
			if ( cbMultipleY != null )
			{
				cbMultipleY.select( 0 );
				( (ChartWizardContext) getContext( ) ).setMoreAxesSupported( false );
				lblMultipleY.setEnabled( false );
				cbMultipleY.setEnabled( false );
			}
			if ( cbSeriesType != null )
			{
				lblSeriesType.setEnabled( false );
				cbSeriesType.setEnabled( false );
			}
		}
		if ( cbOrientation != null )
		{
			lblOrientation.setEnabled( bOutXtab && lblOrientation.isEnabled( ) );
			cbOrientation.setEnabled( bOutXtab && cbOrientation.isEnabled( ) );
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.frameworks.taskwizard.interfaces.ITask#getContext()
	 */
	public IWizardContext getContext( )
	{
		ChartWizardContext context = (ChartWizardContext) super.getContext( );
		context.setModel( this.chartModel );
		return context;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.frameworks.taskwizard.interfaces.ITask#setContext(org.eclipse.birt.frameworks.taskwizard.interfaces.IWizardContext)
	 */
	public void setContext( IWizardContext context )
	{
		super.setContext( context );
		this.chartModel = ( (ChartWizardContext) context ).getModel( );
		if ( chartModel != null )
		{
			this.sType = ( (ChartWizardContext) context ).getChartType( )
					.getName( );
			this.sSubType = chartModel.getSubType( );
			this.sDimension = translateDimensionString( chartModel.getDimension( )
					.getName( ) );
			if ( chartModel instanceof ChartWithAxes )
			{
				this.orientation = ( (ChartWithAxes) chartModel ).getOrientation( );
				int iYAxesCount = ChartUIUtil.getOrthogonalAxisNumber( chartModel );
				// IF THE UI HAS BEEN INITIALIZED...I.E. IF setContext() IS
				// CALLED AFTER getUI()
				if ( iYAxesCount > 1
						&& ( lblMultipleY != null && !lblMultipleY.isDisposed( ) ) )
				{
					lblMultipleY.setEnabled( !is3D( ) );
					cbMultipleY.setEnabled( !is3D( ) );
					lblSeriesType.setEnabled( !is3D( ) && isTwoAxesEnabled( ) );
					cbSeriesType.setEnabled( !is3D( ) && isTwoAxesEnabled( ) );
					selectMultipleAxis( iYAxesCount );
					// TODO: Update the series type based on series type for the
					// second Y axis
				}
			}

		}
	}

	private void selectMultipleAxis( int yAxisNum )
	{
		if ( ( (ChartWizardContext) getContext( ) ).isMoreAxesSupported( ) )
		{
			cbMultipleY.select( 2 );
		}
		else
		{
			if ( yAxisNum > 2 )
			{
				cbMultipleY.select( 2 );
				( (ChartWizardContext) getContext( ) ).setMoreAxesSupported( true );
			}
			else
			{
				cbMultipleY.select( yAxisNum > 0 ? yAxisNum - 1 : 0 );
			}
		}
	}

	public void changeTask( Notification notification )
	{
		doPreview( );
	}

	private void checkDataTypeForChartWithAxes( Chart cm )
	{
		// To check the data type of base series and orthogonal series in chart
		// with axes
		List<SeriesDefinition> sdList = new ArrayList<SeriesDefinition>( );
		sdList.addAll( ChartUIUtil.getBaseSeriesDefinitions( cm ) );
		for ( int i = 0; i < sdList.size( ); i++ )
		{
			SeriesDefinition sd = sdList.get( i );
			Series series = sd.getDesignTimeSeries( );
			checkDataTypeForBaseSeries( ChartUIUtil.getDataQuery( sd, 0 ),
					series );
		}

		sdList.clear( );
		sdList.addAll( ChartUIUtil.getAllOrthogonalSeriesDefinitions( cm ) );
		for ( int i = 0; i < sdList.size( ); i++ )
		{
			SeriesDefinition sd = sdList.get( i );
			Series series = sd.getDesignTimeSeries( );
			checkDataTypeForOrthoSeries( ChartUIUtil.getDataQuery( sd, 0 ),
					series );
		}
	}

	protected IDataServiceProvider getDataServiceProvider( )
	{
		return ( (ChartWizardContext) getContext( ) ).getDataServiceProvider( );
	}

	private String getSubtypeFromButton( Control button )
	{
		return (String) button.getData( );
	}

	private void checkDataTypeForBaseSeries( Query query, Series series )
	{
		checkDataTypeImpl( query, series, true );
	}

	private void checkDataTypeForOrthoSeries( Query query, Series series )
	{
		checkDataTypeImpl( query, series, false );
	}

	private void checkDataTypeImpl( Query query, Series series,
			boolean isBaseSeries )
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

		Collection<ISeriesUIProvider> cRegisteredEntries = ChartUIExtensionsImpl.instance( )
				.getSeriesUIComponents( getContext( ).getClass( ).getSimpleName( ) );
		Iterator<ISeriesUIProvider> iterEntries = cRegisteredEntries.iterator( );

		String sSeries = null;
		while ( iterEntries.hasNext( ) )
		{
			ISeriesUIProvider provider = iterEntries.next( );
			sSeries = provider.getSeriesClass( );

			if ( sSeries.equals( series.getClass( ).getName( ) ) )
			{
				if ( chartModel instanceof ChartWithAxes )
				{
					DataType dataType = getDataServiceProvider( ).getDataType( expression );
					SeriesDefinition baseSD = ( ChartUIUtil.getBaseSeriesDefinitions( chartModel ).get( 0 ) );
					SeriesDefinition orthSD = null;
					orthSD = (SeriesDefinition) series.eContainer( );
					String aggFunc = null;
					try
					{
						aggFunc = ChartUtil.getAggregateFuncExpr( orthSD,
								baseSD,
								query );
						ChartWizard.removeException( ChartWizard.PluginSet_getAggF_ID );
					}
					catch ( ChartException e )
					{
						ChartWizard.showException( ChartWizard.PluginSet_getAggF_ID,
								e.getLocalizedMessage( ) );
					}

					if ( baseSD != null )
					{
						// If aggregation is set to count/distinctcount on base
						// series, don't change data type to numeric.
						if ( !isBaseSeries
								&& baseSD != orthSD
								&& ChartUtil.isMagicAggregate( aggFunc ) )
						{
							dataType = DataType.NUMERIC_LITERAL;
						}
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
							// Avoid modifying model to notify an event loop
							ChartAdapter.beginIgnoreNotifications( );
							axis.setType( axisTypes[i] );
							ChartAdapter.endIgnoreNotifications( );
							break;
						}
					}
				}

				try
				{
					provider.validateSeriesBindingType( series,
							getDataServiceProvider( ) );
					ChartWizard.removeException( ChartWizard.CheckSeriesBindingType_ID
							+ series.eContainer( ).hashCode( ) );
				}
				catch ( ChartException ce )
				{
					ChartWizard.showException( ChartWizard.CheckSeriesBindingType_ID
							+ series.eContainer( ).hashCode( ),
							Messages.getFormattedString( "TaskSelectData.Warning.TypeCheck",//$NON-NLS-1$ 
							new String[]{
									ce.getLocalizedMessage( ),
									series.getDisplayName( )
							} ) );
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

			EList<MarkerLine> markerLines = axis.getMarkerLines( );
			for ( int i = 0; i < markerLines.size( ); i++ )
			{
				( markerLines.get( i ) ).setFormatSpecifier( null );
			}

			EList<MarkerRange> markerRanges = axis.getMarkerRanges( );
			for ( int i = 0; i < markerRanges.size( ); i++ )
			{
				( markerRanges.get( i ) ).setFormatSpecifier( null );
			}
		}
		ChartAdapter.endIgnoreNotifications( );
	}

	private void convertSampleData( Axis axis, AxisType axisType )
	{
		if ( ( axis.getAssociatedAxes( ) != null )
				&& ( axis.getAssociatedAxes( ).size( ) != 0 ) )
		{
			BaseSampleData bsd = chartModel.getSampleData( )
					.getBaseSampleData( )
					.get( 0 );
			bsd.setDataSetRepresentation( ChartUIUtil.getConvertedSampleDataRepresentation( axisType,
					bsd.getDataSetRepresentation( ),
					0 ) );
		}
		else
		{
			int iStartIndex = getFirstSeriesDefinitionIndexForAxis( axis );
			int iEndIndex = iStartIndex + axis.getSeriesDefinitions( ).size( );

			int iOSDSize = chartModel.getSampleData( )
					.getOrthogonalSampleData( )
					.size( );
			for ( int i = 0; i < iOSDSize; i++ )
			{
				OrthogonalSampleData osd = chartModel.getSampleData( )
						.getOrthogonalSampleData( )
						.get( i );
				if ( osd.getSeriesDefinitionIndex( ) >= iStartIndex
						&& osd.getSeriesDefinitionIndex( ) < iEndIndex )
				{
					osd.setDataSetRepresentation( ChartUIUtil.getConvertedSampleDataRepresentation( axisType,
							osd.getDataSetRepresentation( ),
							i ) );
				}
			}
		}
	}

	private int getFirstSeriesDefinitionIndexForAxis( Axis axis )
	{
		List<Axis> axisList = ( (ChartWithAxes) chartModel ).getAxes( )
				.get( 0 )
				.getAssociatedAxes( );
		int index = 0;
		for ( int i = 0; i < axisList.size( ); i++ )
		{
			if ( axis.equals( axisList.get( i ) ) )
			{
				index = i;
				break;
			}
		}
		int iTmp = 0;
		for ( int i = 0; i < index; i++ )
		{
			iTmp += ChartUIUtil.getAxisYForProcessing( (ChartWithAxes) chartModel,
					i )
					.getSeriesDefinitions( )
					.size( );
		}
		return iTmp;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.core.ui.frameworks.taskwizard.SimpleTask#getImage()
	 */
	public Image getImage( )
	{
		return UIHelper.getImage( ChartUIConstants.IMAGE_TASK_TYPE );
	}

	/**
	 * Rotates Axis Title when transposing
	 * 
	 * @param cwa
	 *            chart model
	 */
	private void rotateAxisTitle( ChartWithAxes cwa )
	{
		boolean bRender = false;
		ChartAdapter.beginIgnoreNotifications( );
		Axis aX = ChartUIUtil.getAxisXForProcessing( cwa );
		if ( aX.getTitle( ).isVisible( ) )
		{
			bRender = true;
		}
		double curRotation = aX.getTitle( )
				.getCaption( )
				.getFont( )
				.getRotation( );
		aX.getTitle( )
				.getCaption( )
				.getFont( )
				.setRotation( curRotation >= 0 ? 90 - curRotation : -90
						- curRotation );
		EList<Axis> aYs = aX.getAssociatedAxes( );
		for ( int i = 0; i < aYs.size( ); i++ )
		{
			Axis aY = aYs.get( i );
			if ( aY.getTitle( ).isVisible( ) )
			{
				bRender = true;
			}
			curRotation = aY.getTitle( ).getCaption( ).getFont( ).getRotation( );
			aY.getTitle( )
					.getCaption( )
					.getFont( )
					.setRotation( curRotation >= 0 ? 90 - curRotation : -90
							- curRotation );
		}
		ChartAdapter.endIgnoreNotifications( );
		if ( bRender )
		{
			doPreview( );
		}
	}

	public IChartPreviewPainter createPreviewPainter( )
	{
		ChartPreviewPainter painter = new ChartPreviewPainter( (ChartWizardContext) getContext( ) );
		getPreviewCanvas( ).addPaintListener( painter );
		getPreviewCanvas( ).addControlListener( painter );
		painter.setPreview( getPreviewCanvas( ) );
		return painter;
	}

	public void doPreview( )
	{
		ChartUIUtil.prepareLivePreview( chartModel,
				getDataServiceProvider( ),
				( (ChartWizardContext) context ).getActionEvaluator( ) );

		// Repaint chart.
		if ( previewPainter != null )
		{
			// To update data type after chart type conversion
			if ( chartModel instanceof ChartWithAxes )
			{
				ChartAdapter.beginIgnoreNotifications( );
				checkDataTypeForChartWithAxes( chartModel );
				ChartAdapter.endIgnoreNotifications( );
			}
			previewPainter.renderModel( chartModel );
		}
	}

	public Canvas getPreviewCanvas( )
	{
		return previewCanvas;
	}

	public boolean isPreviewable( )
	{
		return true;
	}

	protected IChartType getCurrentChartType( )
	{
		return htTypes.get( sType );
	}

	private void createUIDescriptors( Composite parent )
	{
		for ( TaskSelectTypeUIDescriptor descriptor : lstDescriptor )
		{
			if ( descriptor.isVisible( ) )
			{
				descriptor.createControl( parent );
			}
		}
	}

	protected final void addTypeUIDescriptor(
			TaskSelectTypeUIDescriptor newDescriptor )
	{
		if ( newDescriptor.getIndex( ) < 0 )
		{
			// add to the last if it's negative
			lstDescriptor.add( newDescriptor );
			return;
		}
		int lastIndex = -1;
		for ( int i = 0; i < lstDescriptor.size( ); i++ )
		{
			TaskSelectTypeUIDescriptor descriptor = lstDescriptor.get( i );
			if ( newDescriptor.getIndex( ) > lastIndex
					&& newDescriptor.getIndex( ) <= descriptor.getIndex( ) )
			{
				lstDescriptor.add( i, newDescriptor );
				return;
			}
			lastIndex = descriptor.getIndex( );
		}
		lstDescriptor.add( newDescriptor );
	}

	protected void addOptionalUIDescriptor( )
	{
		addTypeUIDescriptor( new TaskSelectTypeUIDescriptor( ) {

			public int getIndex( )
			{
				return 20;
			}

			public void createControl( Composite parent )
			{
				Label lblOutput = new Label( parent, SWT.WRAP );
				{
					GridData gd = new GridData( GridData.FILL_HORIZONTAL );
					gd.horizontalIndent = 10;
					lblOutput.setLayoutData( gd );
					lblOutput.setText( Messages.getString( "TaskSelectType.Label.OutputFormat" ) ); //$NON-NLS-1$
				}

				// Add the ComboBox for Output Format
				final Combo cbOutput = new Combo( parent, SWT.DROP_DOWN
						| SWT.READ_ONLY );
				{
					GridData gd = new GridData( GridData.FILL_HORIZONTAL );
					cbOutput.setLayoutData( gd );
					cbOutput.addListener( SWT.Selection, new Listener( ) {

						public void handleEvent( Event event )
						{
							String outputFormat = outputFormats[cbOutput.getSelectionIndex( )];
							( (ChartWizardContext) getContext( ) ).setOutputFormat( outputFormat );
							
							// Update apply button
							if ( container != null && container instanceof ChartWizard )
							{
								( (ChartWizard) container ).updateApplyButton( );
							}
						}
					} );
				}

				cbOutput.setItems( outputDisplayNames );

				String sCurrentFormat = ( (ChartWizardContext) getContext( ) ).getOutputFormat( );
				for ( int index = 0; index < outputFormats.length; index++ )
				{
					if ( outputFormats[index].equals( sCurrentFormat ) )
					{
						cbOutput.select( index );
						break;
					}
				}				
			}
		} );
	}
}