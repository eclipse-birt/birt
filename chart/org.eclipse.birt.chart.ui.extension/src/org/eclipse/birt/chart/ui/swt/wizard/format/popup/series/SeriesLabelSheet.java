/***********************************************************************
 * Copyright (c) 2004, 2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.chart.ui.swt.wizard.format.popup.series;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.birt.chart.computation.IConstants;
import org.eclipse.birt.chart.datafeed.IDataPointDefinition;
import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.attribute.AttributePackage;
import org.eclipse.birt.chart.model.attribute.AxisType;
import org.eclipse.birt.chart.model.attribute.ColorDefinition;
import org.eclipse.birt.chart.model.attribute.DataPoint;
import org.eclipse.birt.chart.model.attribute.DataPointComponent;
import org.eclipse.birt.chart.model.attribute.DataPointComponentType;
import org.eclipse.birt.chart.model.attribute.Fill;
import org.eclipse.birt.chart.model.attribute.FontDefinition;
import org.eclipse.birt.chart.model.attribute.FormatSpecifier;
import org.eclipse.birt.chart.model.attribute.Insets;
import org.eclipse.birt.chart.model.attribute.JavaNumberFormatSpecifier;
import org.eclipse.birt.chart.model.attribute.LineStyle;
import org.eclipse.birt.chart.model.attribute.Orientation;
import org.eclipse.birt.chart.model.attribute.Position;
import org.eclipse.birt.chart.model.attribute.impl.DataPointComponentImpl;
import org.eclipse.birt.chart.model.attribute.impl.JavaNumberFormatSpecifierImpl;
import org.eclipse.birt.chart.model.component.Axis;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.ui.extension.i18n.Messages;
import org.eclipse.birt.chart.ui.swt.composites.FillChooserComposite;
import org.eclipse.birt.chart.ui.swt.composites.FontDefinitionComposite;
import org.eclipse.birt.chart.ui.swt.composites.FormatSpecifierDialog;
import org.eclipse.birt.chart.ui.swt.composites.InsetsComposite;
import org.eclipse.birt.chart.ui.swt.composites.LineAttributesComposite;
import org.eclipse.birt.chart.ui.swt.composites.TextEditorComposite;
import org.eclipse.birt.chart.ui.swt.wizard.ChartWizardContext;
import org.eclipse.birt.chart.ui.swt.wizard.format.popup.AbstractPopupSheet;
import org.eclipse.birt.chart.ui.util.ChartHelpContextIds;
import org.eclipse.birt.chart.ui.util.ChartUIUtil;
import org.eclipse.birt.chart.ui.util.UIHelper;
import org.eclipse.birt.chart.util.LiteralHelper;
import org.eclipse.birt.chart.util.PluginSettings;
import org.eclipse.birt.core.ui.frameworks.taskwizard.WizardBase;
import org.eclipse.emf.common.util.EList;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Listener;

/**
 * Popup sheet for Series Label
 * 
 */
public class SeriesLabelSheet extends AbstractPopupSheet implements
		SelectionListener,
		Listener
{

	private Composite cmpContent = null;

	private Group grpDataPoint = null;

	private List lstComponents = null;

	private Combo cmbComponentTypes = null;

	private Button btnAddComponent = null;

	private Button btnRemoveComponent = null;

	private Button btnFormatSpecifier = null;

	private TextEditorComposite txtPrefix = null;

	private TextEditorComposite txtSuffix = null;

	private TextEditorComposite txtSeparator = null;

	private LineAttributesComposite liacOutline = null;

	private InsetsComposite icInsets = null;

	private SeriesDefinition seriesDefn = null;

	private Label lblPosition;

	private Combo cmbPosition;

	private Label lblFont;

	private FontDefinitionComposite fdcFont;

	private Label lblFill;

	private FillChooserComposite fccBackground;

	private Label lblShadow;

	private FillChooserComposite fccShadow;

	private Group grpAttributes;

	private Label lblPrefix;

	private Label lblSuffix;

	private Label lblSeparator;

	private Group grpOutline;

	private ChartWizardContext context;

	/** Caches the pairs of datapoint display name and name */
	private Map<String, String> mapDataPointNames;

	/**
	 * Caches corresponding index in model of each List item
	 */
	private ArrayList<Integer> dataPointIndex;

	/** The DataPointDefinition stores data point component for current Chart. */
	private IDataPointDefinition foDataPointDefinition;

	public SeriesLabelSheet( String title, ChartWizardContext context,
			SeriesDefinition seriesDefn )
	{
		super( title, context, true );
		this.seriesDefn = seriesDefn;
		this.context = context;
		mapDataPointNames = new HashMap<String, String>( );
		dataPointIndex = new ArrayList<Integer>( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.ui.swt.ISheet#getComponent(org.eclipse.swt.widgets.Composite)
	 */
	public Composite getComponent( Composite parent )
	{
		ChartUIUtil.bindHelp( parent, ChartHelpContextIds.POPUP_SERIES_LABEL );
		cmpContent = new Composite( parent, SWT.NONE );
		{
			// Layout for the content composite
			GridLayout glContent = new GridLayout( );
			glContent.numColumns = 2;
			cmpContent.setLayout( glContent );
		}

		Composite cmpTop = new Composite( cmpContent, SWT.NONE );
		{
			GridLayout layout = new GridLayout( 2, false );
			layout.horizontalSpacing = 0;
			cmpTop.setLayout( layout );
			cmpTop.setLayoutData( new GridData( GridData.FILL_BOTH ) );
		}

		Composite cmpLeft = new Composite( cmpTop, SWT.NONE );
		{
			cmpLeft.setLayout( new GridLayout( ) );
			cmpLeft.setLayoutData( new GridData( GridData.FILL_BOTH ) );
		}

		Composite cmpRight = new Composite( cmpTop, SWT.NONE );
		{
			cmpRight.setLayout( new GridLayout( ) );
			cmpRight.setLayoutData( new GridData( GridData.FILL_BOTH ) );
		}

		createAttributeArea( cmpLeft );

		grpOutline = new Group( cmpLeft, SWT.NONE );
		GridData gdGOutline = new GridData( GridData.FILL_HORIZONTAL );
		gdGOutline.heightHint = 110;
		grpOutline.setLayoutData( gdGOutline );
		grpOutline.setText( Messages.getString( "LabelAttributesComposite.Lbl.Outline" ) ); //$NON-NLS-1$
		grpOutline.setLayout( new FillLayout( ) );
		// grpOutline.setEnabled(bEnableUI);

		liacOutline = new LineAttributesComposite( grpOutline,
				SWT.NONE,
				getContext( ),
				getSeriesForProcessing( ).getLabel( ).getOutline( ),
				true,
				true,
				true );
		{
			liacOutline.addListener( this );
		}

		createDataPointArea( cmpRight );

		icInsets = new InsetsComposite( cmpRight,
				SWT.NONE,
				getSeriesForProcessing( ).getLabel( ).getInsets( ),
				getChart( ).getUnits( ),
				getContext( ).getUIServiceProvider( ) );
		{
			GridData gdICInsets = new GridData( GridData.FILL_HORIZONTAL );
			gdICInsets.heightHint = icInsets.getPreferredSize( ).y;
			gdICInsets.grabExcessVerticalSpace = false;
			icInsets.setLayoutData( gdICInsets );
			icInsets.addListener( this );
		}

		// Populate lists
		populateLists( getSeriesForProcessing( ) );

		setEnabled( getSeriesForProcessing( ).getLabel( ).isVisible( ) );

		refreshDataPointButtons( );

		return cmpContent;
	}

	private void populateLists( Series series )
	{
		// Populate DataPoint Components List
		String[] componentsDisplayName = LiteralHelper.dataPointComponentTypeSet.getDisplayNames( );
		boolean bException = false;
		try
		{
			mapDataPointNames.clear( );
			// Add series-specific datapoint components
			foDataPointDefinition = PluginSettings.instance( )
					.getDataPointDefinition( getSeriesForProcessing( ).getClass( ) );
			if ( foDataPointDefinition != null )
			{
				String[] dpType = foDataPointDefinition.getDataPointTypes( );
				String[] dpTypeDisplay = new String[dpType.length];
				for ( int i = 0; i < dpType.length; i++ )
				{
					dpTypeDisplay[i] = foDataPointDefinition.getDisplayText( dpType[i] );
					mapDataPointNames.put( dpType[i], dpTypeDisplay[i] );
					mapDataPointNames.put( dpTypeDisplay[i], dpType[i] );
				}
				componentsDisplayName = concatenateArrays( dpTypeDisplay,
						componentsDisplayName );
			}
		}
		catch ( ChartException e )
		{
			bException = true;
			WizardBase.showException( e.getLocalizedMessage( ) );
		}
		if ( !bException )
		{
			WizardBase.removeException( );
		}
		cmbComponentTypes.setItems( componentsDisplayName );
		cmbComponentTypes.select( 0 );

		// Populate Current list of DataPointComponents
		this.lstComponents.setItems( getDataPointComponents( series.getDataPoint( ) ) );

		String str = series.getDataPoint( ).getPrefix( );
		this.txtPrefix.setText( ( str == null ) ? "" : str ); //$NON-NLS-1$
		str = series.getDataPoint( ).getSuffix( );
		this.txtSuffix.setText( ( str == null ) ? "" : str ); //$NON-NLS-1$
		str = series.getDataPoint( ).getSeparator( );
		this.txtSeparator.setText( ( str == null ) ? "" : str ); //$NON-NLS-1$

		// Position
		int positionScope = ChartUIUtil.getPositionScopeOfSeriesLabel( getSeriesForProcessing( ),
				getContext( ).getModel( ).getDimension( ) );
		cmbPosition.setItems( ChartUIUtil.getPositionDisplayNames( positionScope,
				isFlippedAxes( ) ) );

		Position lpCurrent = getSeriesForProcessing( ).getLabelPosition( );
		if ( lpCurrent != null )
		{
			String positionName = ChartUIUtil.getFlippedPosition( lpCurrent,
					isFlippedAxes( ) ).getName( );
			for ( int i = 0; i < cmbPosition.getItemCount( ); i++ )
			{
				if ( positionName.equals( LiteralHelper.fullPositionSet.getNameByDisplayName( cmbPosition.getItem( i ) ) ) )
				{
					cmbPosition.select( i );
				}
			}
		}

		// For compatibility with old model, set the first selection by
		// default.
		if ( cmbPosition.getSelectionIndex( ) < 0 )
		{
			cmbPosition.select( 0 );
			cmbPosition.notifyListeners( SWT.Selection, new Event( ) );
		}
	}

	private String[] getDataPointComponents( DataPoint datapoint )
	{
		EList<?> oArr = datapoint.getComponents( );
		ArrayList<String> sArr = new ArrayList<String>( oArr.size( ) );
		for ( int i = 0; i < oArr.size( ); i++ )
		{
			DataPointComponent dpc = (DataPointComponent) oArr.get( i );
			if ( dpc.getOrthogonalType( ).length( ) == 0 )
			{
				// Add general components
				sArr.add( LiteralHelper.dataPointComponentTypeSet.getDisplayNameByName( dpc.getType( )
						.getName( ) ) );
				dataPointIndex.add( Integer.valueOf( i ) );
			}
			else if ( mapDataPointNames.containsKey( dpc.getOrthogonalType( ) ) )
			{
				// Add series-specific components of current series
				sArr.add( mapDataPointNames.get( dpc.getOrthogonalType( ) ) );
				dataPointIndex.add( Integer.valueOf( i ) );
			}
		}
		return sArr.toArray( new String[0] );
	}

	private void createAttributeArea( Composite parent )
	{
		grpAttributes = new Group( parent, SWT.NONE );
		{
			grpAttributes.setLayoutData( new GridData( GridData.FILL_BOTH ) );
			grpAttributes.setLayout( new GridLayout( 2, false ) );
			grpAttributes.setText( Messages.getString( "SeriesLabelSheet.Label.Format" ) ); //$NON-NLS-1$
		}

		lblPosition = new Label( grpAttributes, SWT.NONE );
		GridData gdLBLPosition = new GridData( );
		lblPosition.setLayoutData( gdLBLPosition );
		lblPosition.setText( Messages.getString( "LabelAttributesComposite.Lbl.Position" ) ); //$NON-NLS-1$

		cmbPosition = new Combo( grpAttributes, SWT.DROP_DOWN | SWT.READ_ONLY );
		GridData gdCMBPosition = new GridData( GridData.FILL_BOTH );
		cmbPosition.setLayoutData( gdCMBPosition );
		cmbPosition.addSelectionListener( this );

		lblFont = new Label( grpAttributes, SWT.NONE );
		GridData gdLFont = new GridData( );
		lblFont.setLayoutData( gdLFont );
		lblFont.setText( Messages.getString( "LabelAttributesComposite.Lbl.Font" ) ); //$NON-NLS-1$

		fdcFont = new FontDefinitionComposite( grpAttributes,
				SWT.NONE,
				getContext( ),
				getSeriesForProcessing( ).getLabel( ).getCaption( ).getFont( ),
				getSeriesForProcessing( ).getLabel( ).getCaption( ).getColor( ),
				false );
		GridData gdFDCFont = new GridData( GridData.FILL_BOTH );
		gdFDCFont.heightHint = fdcFont.getPreferredSize( ).y;
		gdFDCFont.widthHint = 96;
		gdFDCFont.grabExcessVerticalSpace = false;
		fdcFont.setLayoutData( gdFDCFont );
		fdcFont.addListener( this );

		lblFill = new Label( grpAttributes, SWT.NONE );
		GridData gdLFill = new GridData( );
		lblFill.setLayoutData( gdLFill );
		lblFill.setText( Messages.getString( "LabelAttributesComposite.Lbl.Background" ) ); //$NON-NLS-1$

		fccBackground = new FillChooserComposite( grpAttributes,
				SWT.NONE,
				getContext( ),
				getSeriesForProcessing( ).getLabel( ).getBackground( ),
				false,
				false );
		GridData gdFCCBackground = new GridData( GridData.FILL_BOTH );
		gdFCCBackground.heightHint = fccBackground.getPreferredSize( ).y;
		fccBackground.setLayoutData( gdFCCBackground );
		fccBackground.addListener( this );

		lblShadow = new Label( grpAttributes, SWT.NONE );
		GridData gdLBLShadow = new GridData( );
		lblShadow.setLayoutData( gdLBLShadow );
		lblShadow.setText( Messages.getString( "LabelAttributesComposite.Lbl.Shadow" ) ); //$NON-NLS-1$

		fccShadow = new FillChooserComposite( grpAttributes,
				SWT.NONE,
				getContext( ),
				getSeriesForProcessing( ).getLabel( ).getShadowColor( ),
				false,
				false );
		GridData gdFCCShadow = new GridData( GridData.FILL_BOTH );
		fccShadow.setLayoutData( gdFCCShadow );
		fccShadow.addListener( this );

	}

	private void setEnabled( boolean bEnableUI )
	{
		grpOutline.setEnabled( bEnableUI );
		liacOutline.setAttributesEnabled( bEnableUI );
		icInsets.setEnabled( bEnableUI );

		grpAttributes.setEnabled( bEnableUI );
		lblPosition.setEnabled( bEnableUI );
		cmbPosition.setEnabled( bEnableUI );
		lblFont.setEnabled( bEnableUI );
		fdcFont.setEnabled( bEnableUI );
		lblFill.setEnabled( bEnableUI );
		fccBackground.setEnabled( bEnableUI );
		lblShadow.setEnabled( bEnableUI );
		fccShadow.setEnabled( bEnableUI );

		// Do not disable data point labels even if the label is visible,
		// because it can be used as tooltips
		
		// grpDataPoint.setEnabled( bEnableUI );
		// lstComponents.setEnabled( bEnableUI );
		// btnFormatSpecifier.setEnabled( bEnableUI );
		// btnRemoveComponent.setEnabled( bEnableUI );
		// btnAddComponent.setEnabled( bEnableUI );
		// cmbComponentTypes.setEnabled( bEnableUI );
		// lblPrefix.setEnabled( bEnableUI );
		// lblSuffix.setEnabled( bEnableUI );
		// lblSeparator.setEnabled( bEnableUI );
		// txtPrefix.setEnabled( bEnableUI );
		// txtSuffix.setEnabled( bEnableUI );
		// txtSeparator.setEnabled( bEnableUI );
	}

	private void createDataPointArea( Composite parent )
	{
		// DataPoint composite
		grpDataPoint = new Group( parent, SWT.NONE );
		{
			GridData gdCMPDataPoint = new GridData( GridData.FILL_BOTH );
			gdCMPDataPoint.heightHint = 160;
			grpDataPoint.setLayoutData( gdCMPDataPoint );
			GridLayout glCMPDataPoint = new GridLayout( );
			glCMPDataPoint.numColumns = 4;
			glCMPDataPoint.horizontalSpacing = 4;
			glCMPDataPoint.marginHeight = 2;
			glCMPDataPoint.marginWidth = 2;
			grpDataPoint.setLayout( glCMPDataPoint );
			grpDataPoint.setText( Messages.getString( "SeriesLabelSheet.Label.Values" ) ); //$NON-NLS-1$
		}

		// Selected DataPoint components list
		lstComponents = new List( grpDataPoint, SWT.BORDER
				| SWT.SINGLE
				| SWT.V_SCROLL );
		GridData gdLSTComponents = new GridData( GridData.FILL_BOTH );
		gdLSTComponents.horizontalSpan = 4;
		lstComponents.setLayoutData( gdLSTComponents );
		lstComponents.addSelectionListener( this );

		// Remove DataPoint component button
		btnFormatSpecifier = new Button( grpDataPoint, SWT.PUSH );
		GridData gdBTNFormatSpecifier = new GridData( );
		ChartUIUtil.setChartImageButtonSizeByPlatform( gdBTNFormatSpecifier );
		btnFormatSpecifier.setLayoutData( gdBTNFormatSpecifier );
		btnFormatSpecifier.setToolTipText( Messages.getString( "Shared.Tooltip.FormatSpecifier" ) ); //$NON-NLS-1$
		btnFormatSpecifier.setImage( UIHelper.getImage( "icons/obj16/formatbuilder.gif" ) ); //$NON-NLS-1$
		btnFormatSpecifier.getImage( )
				.setBackground( btnFormatSpecifier.getBackground( ) );
		btnFormatSpecifier.addSelectionListener( this );

		btnRemoveComponent = new Button( grpDataPoint, SWT.PUSH );
		GridData gdBTNRemoveComponent = new GridData( );
		btnRemoveComponent.setLayoutData( gdBTNRemoveComponent );
		btnRemoveComponent.setText( Messages.getString( "OrthogonalSeriesAttributeSheetImpl.Lbl.Remove" ) ); //$NON-NLS-1$
		btnRemoveComponent.addSelectionListener( this );

		// Add DataPoint component button
		btnAddComponent = new Button( grpDataPoint, SWT.PUSH );
		GridData gdBTNAddComponent = new GridData( );
		btnAddComponent.setLayoutData( gdBTNAddComponent );
		btnAddComponent.setText( Messages.getString( "OrthogonalSeriesAttributeSheetImpl.Lbl.Add" ) ); //$NON-NLS-1$
		btnAddComponent.addSelectionListener( this );

		// Available DataPoint components
		cmbComponentTypes = new Combo( grpDataPoint, SWT.DROP_DOWN
				| SWT.READ_ONLY );
		GridData gdCMBComponentTypes = new GridData( GridData.FILL_HORIZONTAL );
		gdCMBComponentTypes.grabExcessHorizontalSpace = true;
		cmbComponentTypes.setLayoutData( gdCMBComponentTypes );

		// Format prefix composite
		lblPrefix = new Label( grpDataPoint, SWT.NONE );
		GridData gdLBLPrefix = new GridData( );
		lblPrefix.setLayoutData( gdLBLPrefix );
		lblPrefix.setText( Messages.getString( "OrthogonalSeriesAttributeSheetImpl.Lbl.Prefix" ) ); //$NON-NLS-1$

		txtPrefix = new TextEditorComposite( grpDataPoint, SWT.BORDER
				| SWT.SINGLE );
		GridData gdTXTPrefix = new GridData( GridData.FILL_HORIZONTAL );
		gdTXTPrefix.horizontalSpan = 3;
		txtPrefix.setLayoutData( gdTXTPrefix );
		txtPrefix.addListener( this );

		// Format suffix composite
		lblSuffix = new Label( grpDataPoint, SWT.NONE );
		GridData gdLBLSuffix = new GridData( );
		lblSuffix.setLayoutData( gdLBLSuffix );
		lblSuffix.setText( Messages.getString( "OrthogonalSeriesAttributeSheetImpl.Lbl.Suffix" ) ); //$NON-NLS-1$

		txtSuffix = new TextEditorComposite( grpDataPoint, SWT.BORDER
				| SWT.SINGLE );
		GridData gdTXTSuffix = new GridData( GridData.FILL_HORIZONTAL );
		gdTXTSuffix.horizontalSpan = 3;
		txtSuffix.setLayoutData( gdTXTSuffix );
		txtSuffix.addListener( this );

		// Format separator composite
		lblSeparator = new Label( grpDataPoint, SWT.NONE );
		GridData gdLBLSeparator = new GridData( );
		lblSeparator.setLayoutData( gdLBLSeparator );
		lblSeparator.setText( Messages.getString( "OrthogonalSeriesAttributeSheetImpl.Lbl.Separator" ) ); //$NON-NLS-1$

		txtSeparator = new TextEditorComposite( grpDataPoint, SWT.BORDER
				| SWT.SINGLE );
		GridData gdTXTSeparator = new GridData( GridData.FILL_HORIZONTAL );
		gdTXTSeparator.horizontalSpan = 3;
		txtSeparator.setLayoutData( gdTXTSeparator );
		txtSeparator.addListener( this );
	}

	private Series getSeriesForProcessing( )
	{
		return seriesDefn.getDesignTimeSeries( );
	}

	private AxisType getAxisType( DataPointComponentType dpct )
	{
		if ( dpct == DataPointComponentType.BASE_VALUE_LITERAL )
		{
			if ( context.getModel( ) instanceof ChartWithAxes )
			{
				ChartWithAxes chart = (ChartWithAxes) context.getModel( );
				if ( chart.getPrimaryBaseAxes( ).length > 0 )
				{
					return chart.getPrimaryBaseAxes( )[0].getType( );
				}
			}
		}
		else if ( dpct == DataPointComponentType.ORTHOGONAL_VALUE_LITERAL )
		{
			if ( context.getModel( ) instanceof ChartWithAxes )
			{
				ChartWithAxes chart = (ChartWithAxes) context.getModel( );
				if ( chart.getPrimaryBaseAxes( ).length > 0 )
				{
					Axis ax = chart.getPrimaryOrthogonalAxis( chart.getPrimaryBaseAxes( )[0] );
					if ( ax != null )
					{
						return ax.getType( );
					}
				}
			}
		}
		else if ( dpct == DataPointComponentType.PERCENTILE_ORTHOGONAL_VALUE_LITERAL )
		{
			return AxisType.LOGARITHMIC_LITERAL;
		}

		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.widgets.Listener#handleEvent(org.eclipse.swt.widgets.Event)
	 */
	public void handleEvent( Event event )
	{
		if ( event.widget.equals( fdcFont ) )
		{
			getSeriesForProcessing( ).getLabel( )
					.getCaption( )
					.setFont( (FontDefinition) ( (Object[]) event.data )[0] );
			getSeriesForProcessing( ).getLabel( )
					.getCaption( )
					.setColor( (ColorDefinition) ( (Object[]) event.data )[1] );
		}
		else if ( event.widget.equals( liacOutline ) )
		{
			switch ( event.type )
			{
				case LineAttributesComposite.STYLE_CHANGED_EVENT :
					getSeriesForProcessing( ).getLabel( )
							.getOutline( )
							.setStyle( (LineStyle) event.data );
					break;
				case LineAttributesComposite.WIDTH_CHANGED_EVENT :
					getSeriesForProcessing( ).getLabel( )
							.getOutline( )
							.setThickness( ( (Integer) event.data ).intValue( ) );
					break;
				case LineAttributesComposite.COLOR_CHANGED_EVENT :
					getSeriesForProcessing( ).getLabel( )
							.getOutline( )
							.setColor( (ColorDefinition) event.data );
					break;
				case LineAttributesComposite.VISIBILITY_CHANGED_EVENT :
					getSeriesForProcessing( ).getLabel( )
							.getOutline( )
							.setVisible( ( (Boolean) event.data ).booleanValue( ) );
					break;
			}
		}
		else if ( event.widget.equals( fccBackground ) )
		{
			getSeriesForProcessing( ).getLabel( )
					.setBackground( (Fill) event.data );
		}
		else if ( event.widget.equals( fccShadow ) )
		{
			getSeriesForProcessing( ).getLabel( )
					.setShadowColor( (ColorDefinition) event.data );
		}
		else if ( event.widget.equals( icInsets ) )
		{
			getSeriesForProcessing( ).getLabel( )
					.setInsets( (Insets) event.data );
		}
		else if ( event.widget.equals( this.txtPrefix ) )
		{
			getSeriesForProcessing( ).getDataPoint( )
					.setPrefix( txtPrefix.getText( ) );
		}
		else if ( event.widget.equals( this.txtSuffix ) )
		{
			getSeriesForProcessing( ).getDataPoint( )
					.setSuffix( txtSuffix.getText( ) );
		}
		else if ( event.widget.equals( this.txtSeparator ) )
		{
			getSeriesForProcessing( ).getDataPoint( )
					.setSeparator( txtSeparator.getText( ) );
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.events.SelectionListener#widgetSelected(org.eclipse.swt.events.SelectionEvent)
	 */
	public void widgetSelected( SelectionEvent e )
	{
		if ( e.getSource( ).equals( cmbPosition ) )
		{
			if ( context.getModel( ) instanceof ChartWithAxes )
			{
				getSeriesForProcessing( ).setLabelPosition( ChartUIUtil.getFlippedPosition( Position.getByName( LiteralHelper.fullPositionSet.getNameByDisplayName( cmbPosition.getText( ) ) ),
						isFlippedAxes( ) ) );
			}
			else
			{
				getSeriesForProcessing( ).setLabelPosition( Position.getByName( LiteralHelper.fullPositionSet.getNameByDisplayName( cmbPosition.getText( ) ) ) );
			}
		}
		else if ( e.getSource( ).equals( btnAddComponent ) )
		{
			lstComponents.add( this.cmbComponentTypes.getText( ) );
			addDataPointComponent( lstComponents.getItemCount( ) - 1 );
			refreshDataPointButtons( );
		}
		else if ( e.getSource( ).equals( btnRemoveComponent ) )
		{
			if ( lstComponents.getSelectionCount( ) == 0 )
			{
				return;
			}
			int iSelected = lstComponents.getSelectionIndices( )[0];
			if ( iSelected != -1 )
			{
				removeDataPointComponent( iSelected );
				lstComponents.remove( iSelected );
			}
			refreshDataPointButtons( );
		}
		else if ( e.getSource( ).equals( btnFormatSpecifier ) )
		{
			if ( lstComponents.getSelectionCount( ) == 0 )
			{
				return;
			}
			int iSelected = lstComponents.getSelectionIndices( )[0];
			if ( iSelected != -1 )
			{
				setDataPointComponentFormatSpecifier( iSelected );
			}
		}
		else if ( e.getSource( ).equals( lstComponents ) )
		{
			refreshDataPointButtons( );
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.events.SelectionListener#widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent)
	 */
	public void widgetDefaultSelected( SelectionEvent e )
	{
	}

	private void refreshDataPointButtons( )
	{
		btnRemoveComponent.setEnabled( lstComponents.getSelectionIndex( ) != -1 );

		boolean formatEnable = ( lstComponents.getSelectionIndex( ) != -1 );
		// Disable format specifier for Value Series Name due to #179426
		if ( formatEnable )
		{
			String sName = lstComponents.getItem( lstComponents.getSelectionIndex( ) );
			String aa = LiteralHelper.dataPointComponentTypeSet.getNameByDisplayName( sName );
			DataPointComponentType dpct = DataPointComponentType.getByName( aa );
			if ( dpct == DataPointComponentType.ORTHOGONAL_VALUE_LITERAL
					&& mapDataPointNames != null
					&& mapDataPointNames.size( ) > 0 )
			{
				formatEnable = false;
			}
			if ( dpct == DataPointComponentType.SERIES_VALUE_LITERAL )
			{
				formatEnable = false;
			}
			else if ( getAxisType( dpct ) == AxisType.TEXT_LITERAL )
			{
				formatEnable = false;
			}
			else if ( dpct == null )
			{
				String sTypeName = mapDataPointNames.get( lstComponents.getItem( lstComponents.getSelectionIndex( ) ) );
				int iType = foDataPointDefinition.getCompatibleDataType( sTypeName );
				formatEnable = ( ( iType & ( IConstants.NUMERICAL | IConstants.DATE_TIME ) ) != 0 );
			}
		}
		btnFormatSpecifier.setEnabled( formatEnable );
	}

	private void addDataPointComponent( int iComponentIndex )
	{
		DataPoint dp = getSeriesForProcessing( ).getDataPoint( );
		DataPointComponent dpc = null;
		String dpDisplayName = lstComponents.getItem( iComponentIndex );
		if ( mapDataPointNames.containsKey( dpDisplayName ) )
		{
			// Handles series-specific datapoint component
			dpc = DataPointComponentImpl.create( DataPointComponentType.ORTHOGONAL_VALUE_LITERAL,
					null );
			dpc.setOrthogonalType( mapDataPointNames.get( dpDisplayName ) );
		}
		else
		{
			DataPointComponentType dpct = DataPointComponentType.getByName( LiteralHelper.dataPointComponentTypeSet.getNameByDisplayName( lstComponents.getItem( iComponentIndex ) ) );
			dpc = DataPointComponentImpl.create( dpct, null );
			// Set a predefined format specifier to percentile type
			if ( dpct == DataPointComponentType.PERCENTILE_ORTHOGONAL_VALUE_LITERAL )
			{
				JavaNumberFormatSpecifier fs = JavaNumberFormatSpecifierImpl.create( "##.##%" ); //$NON-NLS-1$
				dpc.setFormatSpecifier( fs );
			}
		}
		dpc.eAdapters( ).addAll( dp.eAdapters( ) );
		dp.getComponents( ).add( dpc );

		dataPointIndex.add( iComponentIndex, Integer.valueOf( dp.getComponents( )
				.size( ) - 1 ) );
	}

	private void setDataPointComponentFormatSpecifier( int iComponentIndex )
	{
		DataPointComponent dpc = (DataPointComponent) getSeriesForProcessing( ).getDataPoint( )
				.getComponents( )
				.get( getIndexOfListItem( iComponentIndex ) );
		FormatSpecifier formatspecifier = dpc.getFormatSpecifier( );

		String sContext = new MessageFormat( Messages.getString( "OrthogonalSeriesAttributeSheetImpl.Lbl.SeriesDataPointComponent" ) ).format( new Object[]{dpc.getType( ).getName( )} ); //$NON-NLS-1$
		if ( sContext == null )
		{
			sContext = Messages.getString( "OrthogonalSeriesAttributeSheetImpl.Lbl.SeriesDataPoint" ); //$NON-NLS-1$
		}

		FormatSpecifierDialog editor = null;
		AxisType axisType = getAxisType( dpc.getType( ) );
		boolean isAnyType = false;
		if ( mapDataPointNames != null && mapDataPointNames.size( ) > 0 )
		{
			int iType = foDataPointDefinition.getCompatibleDataType( mapDataPointNames.get( lstComponents.getItem( iComponentIndex ) ) );
			isAnyType = ( ( iType & IConstants.DATE_TIME )
					* ( iType & IConstants.NUMERICAL ) != 0 );
		}
		if ( axisType != null && !isAnyType )
			editor = new FormatSpecifierDialog( cmpContent.getShell( ),
					formatspecifier,
					axisType,
					sContext );
		else
		{
			editor = new FormatSpecifierDialog( cmpContent.getShell( ),
					formatspecifier,
					sContext );
		}

		if ( editor.open( ) == Window.OK )
		{
			if ( editor.getFormatSpecifier( ) == null )
			{
				dpc.eUnset( AttributePackage.eINSTANCE.getDataPointComponent_FormatSpecifier( ) );
				return;
			}
			dpc.setFormatSpecifier( editor.getFormatSpecifier( ) );
		}
	}

	private int getIndexOfListItem( int indexOfListItem )
	{
		return dataPointIndex.get( indexOfListItem );
	}

	private void removeDataPointComponent( int iComponentIndex )
	{
		getSeriesForProcessing( ).getDataPoint( )
				.getComponents( )
				.remove( getIndexOfListItem( iComponentIndex ) );
		dataPointIndex.remove( iComponentIndex );
		if ( dataPointIndex.size( ) != 0 )
		{
			for ( int i = iComponentIndex; i < dataPointIndex.size( ); i++ )
			{
				dataPointIndex.set( i,
						Integer.valueOf( dataPointIndex.get( i ) - 1 ) );
			}
		}
	}

	private boolean isFlippedAxes( )
	{
		return context.getModel( ) instanceof ChartWithAxes
				&& ( (ChartWithAxes) context.getModel( ) ).getOrientation( )
						.equals( Orientation.HORIZONTAL_LITERAL );
	}

	private String[] concatenateArrays( String[] array1, String[] array2 )
	{
		if ( array1 == null || array2 == null )
		{
			return null;
		}
		String[] array = new String[array1.length + array2.length];
		for ( int i = 0; i < array.length; i++ )
		{
			if ( i < array1.length )
			{
				array[i] = array1[i];
			}
			else
			{
				array[i] = array2[i - array1.length];
			}
		}
		return array;
	}

}