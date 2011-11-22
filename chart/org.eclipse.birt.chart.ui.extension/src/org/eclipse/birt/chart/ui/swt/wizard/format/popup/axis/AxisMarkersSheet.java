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

package org.eclipse.birt.chart.ui.swt.wizard.format.popup.axis;

import java.text.MessageFormat;

import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.attribute.Anchor;
import org.eclipse.birt.chart.model.attribute.AxisType;
import org.eclipse.birt.chart.model.attribute.ColorDefinition;
import org.eclipse.birt.chart.model.attribute.Fill;
import org.eclipse.birt.chart.model.attribute.FontDefinition;
import org.eclipse.birt.chart.model.attribute.FormatSpecifier;
import org.eclipse.birt.chart.model.attribute.FractionNumberFormatSpecifier;
import org.eclipse.birt.chart.model.attribute.Insets;
import org.eclipse.birt.chart.model.attribute.Orientation;
import org.eclipse.birt.chart.model.attribute.Position;
import org.eclipse.birt.chart.model.attribute.impl.FractionNumberFormatSpecifierImpl;
import org.eclipse.birt.chart.model.component.Axis;
import org.eclipse.birt.chart.model.component.MarkerLine;
import org.eclipse.birt.chart.model.component.MarkerRange;
import org.eclipse.birt.chart.model.component.impl.LabelImpl;
import org.eclipse.birt.chart.model.component.impl.MarkerLineImpl;
import org.eclipse.birt.chart.model.component.impl.MarkerRangeImpl;
import org.eclipse.birt.chart.model.data.DataElement;
import org.eclipse.birt.chart.model.data.DateTimeDataElement;
import org.eclipse.birt.chart.model.data.TextDataElement;
import org.eclipse.birt.chart.model.data.impl.DateTimeDataElementImpl;
import org.eclipse.birt.chart.model.data.impl.NumberDataElementImpl;
import org.eclipse.birt.chart.model.util.ChartElementUtil;
import org.eclipse.birt.chart.model.util.DefaultValueProvider;
import org.eclipse.birt.chart.ui.extension.i18n.Messages;
import org.eclipse.birt.chart.ui.swt.composites.DateTimeDataElementComposite;
import org.eclipse.birt.chart.ui.swt.composites.ExternalizedTextEditorComposite;
import org.eclipse.birt.chart.ui.swt.composites.FillChooserComposite;
import org.eclipse.birt.chart.ui.swt.composites.LabelAttributesComposite;
import org.eclipse.birt.chart.ui.swt.composites.LabelAttributesComposite.LabelAttributesContext;
import org.eclipse.birt.chart.ui.swt.composites.LineAttributesComposite;
import org.eclipse.birt.chart.ui.swt.composites.NumberDataElementComposite;
import org.eclipse.birt.chart.ui.swt.composites.TriggerEditorDialog;
import org.eclipse.birt.chart.ui.swt.interfaces.IDataElementComposite;
import org.eclipse.birt.chart.ui.swt.wizard.ChartWizardContext;
import org.eclipse.birt.chart.ui.swt.wizard.format.popup.AbstractPopupSheet;
import org.eclipse.birt.chart.ui.util.ChartHelpContextIds;
import org.eclipse.birt.chart.ui.util.ChartUIExtensionUtil;
import org.eclipse.birt.chart.ui.util.ChartUIUtil;
import org.eclipse.birt.chart.util.LiteralHelper;
import org.eclipse.birt.chart.util.NameSet;
import org.eclipse.birt.chart.util.TriggerSupportMatrix;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
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

import com.ibm.icu.util.Calendar;

/**
 * Axis Markers popup sheet
 */

public class AxisMarkersSheet extends AbstractPopupSheet implements
		SelectionListener,
		Listener
{

	protected Composite cmpContent;

	private Composite cmpList = null;

	private Button btnAddLine = null;

	private Button btnAddRange = null;

	private Button btnRemove = null;

	private List lstMarkers = null;

	private Group grpGeneral = null;

	private StackLayout slMarkers = null;

	// Composite for Marker Line

	private Composite cmpLine = null;

	private Group grpMarkerLine = null;

	private Label lblValue = null;

	private IDataElementComposite txtValue = null;

	private Button btnLineFormatSpecifier = null;

	private Label lblAnchor = null;

	private Combo cmbLineAnchor = null;

	private LineAttributesComposite liacMarkerLine = null;

	// Composite for Marker Range

	private Composite cmpRange = null;

	private Group grpMarkerRange = null;

	private Label lblStartValue = null;

	private IDataElementComposite txtStartValue = null;

	private Button btnStartFormatSpecifier = null;

	private Label lblEndValue = null;

	private IDataElementComposite txtEndValue = null;

	private Button btnEndFormatSpecifier = null;

	private Label lblRangeAnchor = null;

	private Combo cmbRangeAnchor = null;

	private Label lblRangeFill = null;

	private FillChooserComposite fccRange = null;

	private LineAttributesComposite liacMarkerRange = null;

	private LabelAttributesComposite lacLabel = null;

	private Button btnLineTriggers;

	private Button btnRangeTriggers;

	private int iLineCount = 0;

	private int iRangeCount = 0;

	private Axis axis;

	private ChartWizardContext context;

	private String MARKER_LINE_LABEL = Messages.getString( "AxisMarkersSheet.MarkerLine.displayName" ); //$NON-NLS-1$

	private String MARKER_RANGE_LABEL = Messages.getString( "AxisMarkersSheet.MarkerRange.displayName" ); //$NON-NLS-1$

	private Button btnTxtEndValueAuto;

	private Button btnTxtStartValueAuto;

	private Button btnTxtValueAuto;

	public AxisMarkersSheet( String title, ChartWizardContext context, Axis axis )
	{
		super( title, context, true );
		this.axis = axis;
		this.context = context;
	}
	
	@Override
	protected void bindHelp( Composite parent )
	{
		ChartUIUtil.bindHelp( parent, ChartHelpContextIds.POPUP_AXIS_MARKERS );
	}

	protected Composite getComponent( Composite parent )
	{
		// Layout for the main composite
		GridLayout glContent = new GridLayout( );
		glContent.numColumns = 2;
		glContent.horizontalSpacing = 5;
		glContent.verticalSpacing = 5;
		glContent.marginHeight = 7;
		glContent.marginWidth = 7;

		cmpContent = new Composite( parent, SWT.NONE );
		cmpContent.setLayout( glContent );

		// Layout for the List composite
		GridLayout glList = new GridLayout( );
		glList.numColumns = 3;
		glList.horizontalSpacing = 5;
		glList.verticalSpacing = 5;
		glList.marginHeight = 0;
		glList.marginWidth = 0;

		cmpList = new Composite( cmpContent, SWT.NONE );
		GridData gdCMPList = new GridData( GridData.FILL_BOTH );
		gdCMPList.horizontalSpan = 2;
		cmpList.setLayoutData( gdCMPList );
		cmpList.setLayout( glList );

		// Layout for the buttons composite
		GridLayout glButtons = new GridLayout( );
		glButtons.numColumns = 3;
		glButtons.horizontalSpacing = 5;
		glButtons.verticalSpacing = 5;
		glButtons.marginHeight = 5;
		glButtons.marginWidth = 0;

		Composite cmpButtons = new Composite( cmpList, SWT.NONE );
		GridData gdCMPButtons = new GridData( GridData.FILL_HORIZONTAL );
		gdCMPButtons.horizontalSpan = 3;
		cmpButtons.setLayoutData( gdCMPButtons );
		cmpButtons.setLayout( glButtons );

		btnAddLine = new Button( cmpButtons, SWT.PUSH );
		GridData gdBTNAddLine = new GridData( GridData.FILL_HORIZONTAL );
		btnAddLine.setLayoutData( gdBTNAddLine );
		btnAddLine.setText( Messages.getString( "BaseAxisMarkerAttributeSheetImpl.Lbl.AddLine" ) ); //$NON-NLS-1$
		btnAddLine.addSelectionListener( this );

		btnAddRange = new Button( cmpButtons, SWT.PUSH );
		GridData gdBTNAddRange = new GridData( GridData.FILL_HORIZONTAL );
		btnAddRange.setLayoutData( gdBTNAddRange );
		btnAddRange.setText( Messages.getString( "BaseAxisMarkerAttributeSheetImpl.Lbl.AddRange" ) ); //$NON-NLS-1$
		btnAddRange.addSelectionListener( this );

		btnRemove = new Button( cmpButtons, SWT.PUSH );
		GridData gdBTNRemove = new GridData( GridData.FILL_HORIZONTAL );
		btnRemove.setLayoutData( gdBTNRemove );
		btnRemove.setText( Messages.getString( "BaseAxisMarkerAttributeSheetImpl.Lbl.RemoveEntry" ) ); //$NON-NLS-1$
		btnRemove.addSelectionListener( this );

		lstMarkers = new List( cmpList, SWT.BORDER | SWT.SINGLE | SWT.V_SCROLL );
		GridData gdLSTMarkers = new GridData( GridData.FILL_HORIZONTAL );
		gdLSTMarkers.horizontalSpan = 3;
		gdLSTMarkers.heightHint = 100;
		lstMarkers.setLayoutData( gdLSTMarkers );
		lstMarkers.addSelectionListener( this );

		// Layout for the general composite
		slMarkers = new StackLayout( );
		slMarkers.marginHeight = 0;
		slMarkers.marginWidth = 0;

		grpGeneral = new Group( cmpContent, SWT.NONE );
		GridData gdCMPGeneral = new GridData( GridData.VERTICAL_ALIGN_BEGINNING
				| GridData.FILL_HORIZONTAL );
		grpGeneral.setLayoutData( gdCMPGeneral );
		grpGeneral.setLayout( slMarkers );
		grpGeneral.setText( Messages.getString( "BaseAxisMarkerAttributeSheetImpl.Lbl.MarkerProperties" ) ); //$NON-NLS-1$

		// Layout for the Marker Line composite
		GridLayout glMarkerLine = new GridLayout( );
		glMarkerLine.numColumns = 4;
		glMarkerLine.horizontalSpacing = 5;
		glMarkerLine.verticalSpacing = 5;
		glMarkerLine.marginHeight = 7;
		glMarkerLine.marginWidth = 7;

		cmpLine = new Composite( grpGeneral, SWT.NONE );
		GridData gdGRPLine = new GridData( GridData.FILL_HORIZONTAL );
		cmpLine.setLayoutData( gdGRPLine );
		cmpLine.setLayout( glMarkerLine );

		lblValue = new Label( cmpLine, SWT.NONE );
		GridData gdLBLValue = new GridData( );
		gdLBLValue.horizontalIndent = 5;
		lblValue.setLayoutData( gdLBLValue );
		lblValue.setText( Messages.getString( "BaseAxisMarkerAttributeSheetImpl.Lbl.Value" ) ); //$NON-NLS-1$

		txtValue = createValuePicker( cmpLine, null );
		
		btnTxtValueAuto = new Button( cmpLine, SWT.CHECK );
		btnTxtValueAuto.setText( ChartUIExtensionUtil.getAutoMessage( ) );
		btnTxtValueAuto.addSelectionListener( this );
		btnTxtValueAuto.setSelection( true );
		txtValue.setEnabled( !btnTxtValueAuto.getSelection( ) );

		btnLineFormatSpecifier = new Button( cmpLine, SWT.PUSH );
		GridData gdBTNLineFormatSpecifier = new GridData( );
		btnLineFormatSpecifier.setLayoutData( gdBTNLineFormatSpecifier );
		btnLineFormatSpecifier.setToolTipText( Messages.getString( "Shared.Tooltip.FormatSpecifier" ) ); //$NON-NLS-1$
		//		btnLineFormatSpecifier.setImage( UIHelper.getImage( "icons/obj16/formatbuilder.gif" ) ); //$NON-NLS-1$
		btnLineFormatSpecifier.addSelectionListener( this );
		btnLineFormatSpecifier.setText( Messages.getString( "Format.Button.Lbl&" ) ); //$NON-NLS-1$

		lblAnchor = new Label( cmpLine, SWT.NONE );
		GridData gdLBLAnchor = new GridData( );
		gdLBLAnchor.horizontalIndent = 5;
		lblAnchor.setLayoutData( gdLBLAnchor );
		lblAnchor.setText( Messages.getString( "BaseAxisMarkerAttributeSheetImpl.Lbl.Anchor" ) ); //$NON-NLS-1$

		cmbLineAnchor = new Combo( cmpLine, SWT.DROP_DOWN | SWT.READ_ONLY );
		GridData gdCMBAnchor = new GridData( GridData.FILL_HORIZONTAL );
		gdCMBAnchor.horizontalSpan = 3;
		cmbLineAnchor.setLayoutData( gdCMBAnchor );
		cmbLineAnchor.addSelectionListener( this );
		cmbLineAnchor.setVisibleItemCount( 30 );

		grpMarkerLine = new Group( cmpLine, SWT.NONE );
		GridData gdGRPMarkerLine = new GridData( GridData.FILL_HORIZONTAL );
		gdGRPMarkerLine.horizontalSpan = 4;
		grpMarkerLine.setLayoutData( gdGRPMarkerLine );
		grpMarkerLine.setLayout( new FillLayout( ) );
		grpMarkerLine.setText( Messages.getString( "BaseAxisMarkerAttributeSheetImpl.Lbl.MarkerLineAttributes" ) ); //$NON-NLS-1$

		int lineStyles = LineAttributesComposite.ENABLE_VISIBILITY
				| LineAttributesComposite.ENABLE_STYLES
				| LineAttributesComposite.ENABLE_WIDTH
				| LineAttributesComposite.ENABLE_COLOR
				| LineAttributesComposite.ENABLE_AUTO_COLOR;
		liacMarkerLine = new LineAttributesComposite( grpMarkerLine,
				SWT.NONE,
				lineStyles,
				getContext( ),
				null );
		liacMarkerLine.addListener( this );

		btnLineTriggers = new Button( cmpLine, SWT.PUSH );
		{
			GridData gd = new GridData( );
			gd.horizontalSpan = 3;
			btnLineTriggers.setLayoutData( gd );
			btnLineTriggers.setText( Messages.getString( "AxisMarkersSheet.Label.Interactivity" ) ); //$NON-NLS-1$
			btnLineTriggers.addSelectionListener( this );
			btnLineTriggers.setEnabled( getChart( ).getInteractivity( )
					.isEnable( ) );
		}

		// Layout for the Marker Range composite
		GridLayout glMarkerRange = new GridLayout( );
		glMarkerRange.numColumns = 4;
		glMarkerRange.horizontalSpacing = 5;
		glMarkerRange.verticalSpacing = 5;
		glMarkerRange.marginHeight = 7;
		glMarkerRange.marginWidth = 7;

		cmpRange = new Composite( grpGeneral, SWT.NONE );
		GridData gdGRPRange = new GridData( GridData.FILL_HORIZONTAL );
		cmpRange.setLayoutData( gdGRPRange );
		cmpRange.setLayout( glMarkerRange );

		lblStartValue = new Label( cmpRange, SWT.NONE );
		GridData gdLBLStartValue = new GridData( );
		gdLBLStartValue.horizontalIndent = 5;
		lblStartValue.setLayoutData( gdLBLStartValue );
		lblStartValue.setText( Messages.getString( "BaseAxisMarkerAttributeSheetImpl.Lbl.StartValue" ) ); //$NON-NLS-1$

		txtStartValue = createValuePicker( cmpRange, null );
		
		btnTxtStartValueAuto = new Button( cmpRange, SWT.CHECK );
		btnTxtStartValueAuto.setText( ChartUIExtensionUtil.getAutoMessage( ) );
		btnTxtStartValueAuto.addSelectionListener( this );
		btnTxtStartValueAuto.setSelection( true );
		txtStartValue.setEnabled( !btnTxtStartValueAuto.getSelection( ) );

		btnStartFormatSpecifier = new Button( cmpRange, SWT.PUSH );
		GridData gdBTNStartFormatSpecifier = new GridData( );
		gdBTNStartFormatSpecifier.heightHint = 18;
		btnStartFormatSpecifier.setLayoutData( gdBTNStartFormatSpecifier );
		btnStartFormatSpecifier.setToolTipText( Messages.getString( "Shared.Tooltip.FormatSpecifier" ) ); //$NON-NLS-1$
		btnStartFormatSpecifier.addSelectionListener( this );
		btnStartFormatSpecifier.setText( Messages.getString( "Format.Button.Lbl&" ) ); //$NON-NLS-1$

		lblEndValue = new Label( cmpRange, SWT.NONE );
		GridData gdLBLEndValue = new GridData( );
		gdLBLEndValue.horizontalIndent = 5;
		lblEndValue.setLayoutData( gdLBLEndValue );
		lblEndValue.setText( Messages.getString( "BaseAxisMarkerAttributeSheetImpl.Lbl.EndValue" ) ); //$NON-NLS-1$

		txtEndValue = createValuePicker( cmpRange, null );
		
		btnTxtEndValueAuto = new Button(cmpRange, SWT.CHECK );
		btnTxtEndValueAuto.setText( ChartUIExtensionUtil.getAutoMessage( ) );
		btnTxtEndValueAuto.addSelectionListener( this );
		btnTxtEndValueAuto.setSelection( true );
		txtEndValue.setEnabled( !btnTxtEndValueAuto.getSelection( ) );

		btnEndFormatSpecifier = new Button( cmpRange, SWT.PUSH );
		GridData gdBTNEndFormatSpecifier = new GridData( );
		gdBTNEndFormatSpecifier.heightHint = 18;
		btnEndFormatSpecifier.setLayoutData( gdBTNEndFormatSpecifier );
		btnEndFormatSpecifier.setToolTipText( Messages.getString( "Shared.Tooltip.FormatSpecifier" ) ); //$NON-NLS-1$
		btnEndFormatSpecifier.addSelectionListener( this );
		btnEndFormatSpecifier.setText( Messages.getString( "Format.Button.Label" ) ); //$NON-NLS-1$

		lblRangeAnchor = new Label( cmpRange, SWT.NONE );
		GridData gdLBLRangeAnchor = new GridData( );
		gdLBLRangeAnchor.horizontalIndent = 5;
		lblRangeAnchor.setLayoutData( gdLBLRangeAnchor );
		lblRangeAnchor.setText( Messages.getString( "BaseAxisMarkerAttributeSheetImpl.Lbl.Anchor" ) ); //$NON-NLS-1$

		cmbRangeAnchor = new Combo( cmpRange, SWT.DROP_DOWN | SWT.READ_ONLY );
		GridData gdCMBRangeAnchor = new GridData( GridData.FILL_HORIZONTAL );
		gdCMBRangeAnchor.horizontalSpan = 3;
		cmbRangeAnchor.setLayoutData( gdCMBRangeAnchor );
		cmbRangeAnchor.addSelectionListener( this );
		cmbRangeAnchor.setVisibleItemCount( 30 );

		lblRangeFill = new Label( cmpRange, SWT.NONE );
		GridData gdLBLRangeFill = new GridData( );
		gdLBLRangeFill.horizontalIndent = 5;
		lblRangeFill.setLayoutData( gdLBLRangeFill );
		lblRangeFill.setText( Messages.getString( "BaseAxisMarkerAttributeSheetImpl.Lbl.Fill" ) ); //$NON-NLS-1$

		int fillStyles = FillChooserComposite.ENABLE_AUTO
				| FillChooserComposite.ENABLE_GRADIENT
				| FillChooserComposite.ENABLE_IMAGE
				| FillChooserComposite.ENABLE_TRANSPARENT
				| FillChooserComposite.ENABLE_TRANSPARENT_SLIDER;
		fccRange = new FillChooserComposite( cmpRange,
				SWT.NONE,
				fillStyles,
				getContext( ),
				null );
		GridData gdFCCRange = new GridData( GridData.FILL_HORIZONTAL );
		gdFCCRange.horizontalSpan = 3 	;
		fccRange.setLayoutData( gdFCCRange );
		fccRange.addListener( this );

		grpMarkerRange = new Group( cmpRange, SWT.NONE );
		GridData gdGRPMarkerRange = new GridData( GridData.FILL_HORIZONTAL );
		gdGRPMarkerRange.horizontalSpan = 4;
		grpMarkerRange.setLayoutData( gdGRPMarkerRange );
		grpMarkerRange.setLayout( new FillLayout( ) );
		grpMarkerRange.setText( Messages.getString( "BaseAxisMarkerAttributeSheetImpl.Lbl.RangeOutline" ) ); //$NON-NLS-1$

		liacMarkerRange = new LineAttributesComposite( grpMarkerRange,
				SWT.NONE,
				lineStyles,
				getContext( ),
				null );
		liacMarkerRange.addListener( this );

		btnRangeTriggers = new Button( cmpRange, SWT.PUSH );
		{
			GridData gd = new GridData( );
			gd.horizontalSpan = 3;
			btnRangeTriggers.setLayoutData( gd );
			btnRangeTriggers.setText( Messages.getString( "AxisMarkersSheet.Label.Interactivity" ) ); //$NON-NLS-1$
			btnRangeTriggers.addSelectionListener( this );
			btnRangeTriggers.setEnabled( getChart( ).getInteractivity( )
					.isEnable( ) );
		}

		lacLabel = new LabelAttributesComposite( cmpContent,
				SWT.NONE,
				getContext( ),
				getLabelAttributesContext( ),
				Messages.getString( "BaseAxisMarkerAttributeSheetImpl.Lbl.MarkerLabelProperties" ), //$NON-NLS-1$
				Position.ABOVE_LITERAL,
				LabelImpl.create( ),
				getChart( ).getUnits( ) );
		GridData gdLACLabel = new GridData( GridData.VERTICAL_ALIGN_BEGINNING
				| GridData.FILL_HORIZONTAL );
		lacLabel.setLayoutData( gdLACLabel );
		lacLabel.addListener( this );
		lacLabel.setDefaultLabelValue( DefaultValueProvider.defLabel( ) );

		slMarkers.topControl = cmpLine;

		populateLists( );

		refreshButtons( );

		return cmpContent;
	}
	
	protected LabelAttributesContext getLabelAttributesContext( )
	{
		LabelAttributesContext attributesContext = new LabelAttributesContext( );
		attributesContext.isPositionEnabled = false;
		attributesContext.isFontAlignmentEnabled = false;
		attributesContext.isLabelEnabled = true;
		return attributesContext;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.swt.widgets.Listener#handleEvent(org.eclipse.swt.widgets.
	 * Event)
	 */
	public void handleEvent( Event event )
	{
		boolean isUnset = ( event.detail == ChartUIExtensionUtil.PROPERTY_UNSET );
		if ( event.widget.equals( lacLabel ) )
		{
			if ( this.lstMarkers.getSelection( ).length != 0 )
			{
				switch ( event.type )
				{
					case LabelAttributesComposite.VISIBILITY_CHANGED_EVENT :
						ChartElementUtil.setEObjectAttribute( getSelectedMarkerLabel( ),
								"visible", //$NON-NLS-1$
								( (Boolean) event.data ).booleanValue( ),
								isUnset );
						break;
					case LabelAttributesComposite.FONT_CHANGED_EVENT :
						getSelectedMarkerLabel( ).getCaption( )
								.setFont( (FontDefinition) ( (Object[]) event.data )[0] );
						getSelectedMarkerLabel( ).getCaption( )
								.setColor( (ColorDefinition) ( (Object[]) event.data )[1] );
						break;
					case LabelAttributesComposite.BACKGROUND_CHANGED_EVENT :
						getSelectedMarkerLabel( ).setBackground( (Fill) event.data );
						break;
					case LabelAttributesComposite.SHADOW_CHANGED_EVENT :
						getSelectedMarkerLabel( ).setShadowColor( (ColorDefinition) event.data );
						break;
					case LabelAttributesComposite.OUTLINE_STYLE_CHANGED_EVENT :
						ChartElementUtil.setEObjectAttribute( getSelectedMarkerLabel( ).getOutline( ),
								"style", //$NON-NLS-1$
								event.data,
								isUnset );
						break;
					case LabelAttributesComposite.OUTLINE_WIDTH_CHANGED_EVENT :
						ChartElementUtil.setEObjectAttribute( getSelectedMarkerLabel( ).getOutline( ),
								"thickness", //$NON-NLS-1$
								( (Integer) event.data ).intValue( ),
								isUnset );
						break;
					case LabelAttributesComposite.OUTLINE_COLOR_CHANGED_EVENT :
						getSelectedMarkerLabel( ).getOutline( )
								.setColor( (ColorDefinition) event.data );
						break;
					case LabelAttributesComposite.OUTLINE_VISIBILITY_CHANGED_EVENT :
						ChartElementUtil.setEObjectAttribute( getSelectedMarkerLabel( ).getOutline( ),
								"visible", //$NON-NLS-1$
								( (Boolean) event.data ).booleanValue( ),
								isUnset );
						break;
					case LabelAttributesComposite.INSETS_CHANGED_EVENT :
						getSelectedMarkerLabel( ).setInsets( (Insets) event.data );
						break;
					case LabelAttributesComposite.LABEL_CHANGED_EVENT :
						getSelectedMarkerLabel( ).getCaption( )
								.setValue( (String) event.data );
						break;
				}
			}
		}
		else if ( event.widget.equals( fccRange ) )
		{
			getAxisForProcessing( ).getMarkerRanges( )
					.get( getMarkerIndex( ) )
					.setFill( (Fill) event.data );
		}
		else if ( event.widget.equals( txtValue ) )
		{
			MarkerLine line = getAxisForProcessing( ).getMarkerLines( )
					.get( getMarkerIndex( ) );
			if ( event.type == IDataElementComposite.DATA_MODIFIED )
			{
				line.setValue( getNotNullDataElement( txtValue.getDataElement( ) ) );
			}
			else if ( event.type == IDataElementComposite.FRACTION_CONVERTED )
			{
				// Change FormatSpecifier if the text is fraction and has been
				// converted
				if ( !( line.getFormatSpecifier( ) instanceof FractionNumberFormatSpecifier ) )
				{
					line.setFormatSpecifier( createDefaultFractionFormatSpecifier( ) );
				}
			}
		}
		else if ( event.widget.equals( txtStartValue ) )
		{
			MarkerRange range = getAxisForProcessing( ).getMarkerRanges( )
					.get( getMarkerIndex( ) );
			if ( event.type == IDataElementComposite.DATA_MODIFIED )
			{
				range.setStartValue( getNotNullDataElement( txtStartValue.getDataElement( ) ) );
			}
			else if ( event.type == IDataElementComposite.FRACTION_CONVERTED )
			{
				// Change FormatSpecifier if the text is fraction and has been
				// converted
				if ( !( range.getFormatSpecifier( ) instanceof FractionNumberFormatSpecifier ) )
				{
					range.setFormatSpecifier( createDefaultFractionFormatSpecifier( ) );
				}
			}
		}
		else if ( event.widget.equals( txtEndValue ) )
		{
			MarkerRange range = getAxisForProcessing( ).getMarkerRanges( )
					.get( getMarkerIndex( ) );
			if ( event.type == IDataElementComposite.DATA_MODIFIED )
			{
				range.setEndValue( getNotNullDataElement( txtEndValue.getDataElement( ) ) );
			}
			else if ( event.type == IDataElementComposite.FRACTION_CONVERTED )
			{
				// Change FormatSpecifier if the text is fraction and has been
				// converted
				if ( !( range.getFormatSpecifier( ) instanceof FractionNumberFormatSpecifier ) )
				{
					range.setFormatSpecifier( createDefaultFractionFormatSpecifier( ) );
				}
			}
		}
		else if ( event.widget.equals( liacMarkerLine ) )
		{
			if ( event.type == LineAttributesComposite.STYLE_CHANGED_EVENT )
			{
				ChartElementUtil.setEObjectAttribute( getAxisForProcessing( ).getMarkerLines( )
						.get( getMarkerIndex( ) )
						.getLineAttributes( ),
						"style", //$NON-NLS-1$
						event.data,
						isUnset );
			}
			else if ( event.type == LineAttributesComposite.WIDTH_CHANGED_EVENT )
			{
				ChartElementUtil.setEObjectAttribute( getAxisForProcessing( ).getMarkerLines( )
						.get( getMarkerIndex( ) )
						.getLineAttributes( ),
						"thickness", //$NON-NLS-1$
						( (Integer) event.data ).intValue( ),
						isUnset );
			}
			else if ( event.type == LineAttributesComposite.COLOR_CHANGED_EVENT )
			{
				getAxisForProcessing( ).getMarkerLines( )
						.get( getMarkerIndex( ) )
						.getLineAttributes( )
						.setColor( (ColorDefinition) event.data );
			}
			else
			{
				ChartElementUtil.setEObjectAttribute( getAxisForProcessing( ).getMarkerLines( )
						.get( getMarkerIndex( ) )
						.getLineAttributes( ),
						"visible", //$NON-NLS-1$
						( (Boolean) event.data ).booleanValue( ),
						isUnset );
			}
		}
		else if ( event.widget.equals( liacMarkerRange ) )
		{
			if ( event.type == LineAttributesComposite.STYLE_CHANGED_EVENT )
			{
				ChartElementUtil.setEObjectAttribute( getAxisForProcessing( ).getMarkerRanges( )
						.get( getMarkerIndex( ) )
						.getOutline( ),
						"style", //$NON-NLS-1$
						event.data,
						isUnset );
			}
			else if ( event.type == LineAttributesComposite.WIDTH_CHANGED_EVENT )
			{
				ChartElementUtil.setEObjectAttribute( getAxisForProcessing( ).getMarkerRanges( )
						.get( getMarkerIndex( ) )
						.getOutline( ),
						"thickness", //$NON-NLS-1$
						( (Integer) event.data ).intValue( ),
						isUnset );
			}
			else if ( event.type == LineAttributesComposite.COLOR_CHANGED_EVENT )
			{
				getAxisForProcessing( ).getMarkerRanges( )
						.get( getMarkerIndex( ) )
						.getOutline( )
						.setColor( (ColorDefinition) event.data );
			}
			else
			{
				ChartElementUtil.setEObjectAttribute( getAxisForProcessing( ).getMarkerRanges( )
						.get( getMarkerIndex( ) )
						.getOutline( ),
						"visible", //$NON-NLS-1$
						( (Boolean) event.data ).booleanValue( ),
						isUnset );
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.swt.events.SelectionListener#widgetSelected(org.eclipse.swt
	 * .events.SelectionEvent)
	 */
	public void widgetSelected( SelectionEvent e )
	{
		if ( e.getSource( ).equals( btnAddLine ) )
		{
			MarkerLine line = MarkerLineImpl.createDefault( getAxisForProcessing( ),
					createDefaultDataElement( ),
					null );
			line.eAdapters( ).addAll( getAxisForProcessing( ).eAdapters( ) );
			iLineCount++;
			buildList( );
			lstMarkers.select( lstMarkers.getItemCount( ) - 1 );
			updateUIForSelection( );
			if ( lstMarkers.getItemCount( ) >= 1 )
			{
				// Enable UI elements
				setState( true );
			}

			refreshButtons( );
		}
		else if ( e.getSource( ).equals( btnAddRange ) )
		{
			MarkerRange range = MarkerRangeImpl.createDefault( getAxisForProcessing( ),
					createDefaultDataElement( ),
					createDefaultDataElement( ),
					null,
					null );
			range.eAdapters( ).addAll( getAxisForProcessing( ).eAdapters( ) );
			iRangeCount++;
			buildList( );
			lstMarkers.select( lstMarkers.getItemCount( ) - 1 );
			updateUIForSelection( );
			if ( lstMarkers.getItemCount( ) >= 1 )
			{
				// Enable UI elements
				setState( true );
			}

			refreshButtons( );
		}
		else if ( e.getSource( ).equals( btnRemove ) )
		{
			if ( lstMarkers.getSelection( ).length == 0 )
			{
				return;
			}
			String sSelectedMarker = lstMarkers.getSelection( )[0];
			boolean bLine = sSelectedMarker.startsWith( MARKER_LINE_LABEL );
			int iMarkerIndex = getMarkerIndex( );
			if ( bLine )
			{
				getAxisForProcessing( ).getMarkerLines( ).remove( iMarkerIndex );
				iLineCount--;
			}
			else
			{
				getAxisForProcessing( ).getMarkerRanges( )
						.remove( iMarkerIndex );
				iRangeCount--;
			}
			buildList( );
			if ( lstMarkers.getItemCount( ) > 0 )
			{
				lstMarkers.select( 0 );
				updateUIForSelection( );
				setState( true );
			}
			else
			{
				setState( false );
				resetUI( );
			}

			refreshButtons( );
		}
		else if ( e.getSource( ).equals( lstMarkers ) )
		{
			updateUIForSelection( );
			setState( true );
			refreshButtons( );
		}
		else if ( e.getSource( ).equals( cmbLineAnchor ) )
		{
			if ( cmbLineAnchor.getSelectionIndex( ) == 0 )
			{
				getAxisForProcessing( ).getMarkerLines( )
						.get( getMarkerIndex( ) )
						.unsetLabelAnchor( );
			}
			else
			{
				getAxisForProcessing( ).getMarkerLines( )
						.get( getMarkerIndex( ) )
						.setLabelAnchor( ChartUIUtil.getFlippedAnchor( Anchor.getByName( LiteralHelper.anchorSet.getNameByDisplayName( cmbLineAnchor.getText( ) ) ),
								isFlippedAxes( ) ) );
			}
		}
		else if ( e.getSource( ).equals( cmbRangeAnchor ) )
		{
			if ( cmbRangeAnchor.getSelectionIndex( ) == 0 )
			{
				getAxisForProcessing( ).getMarkerRanges( )
						.get( getMarkerIndex( ) )
						.unsetLabelAnchor( );
			}
			else
			{
				getAxisForProcessing( ).getMarkerRanges( )
						.get( getMarkerIndex( ) )
						.setLabelAnchor( ChartUIUtil.getFlippedAnchor( Anchor.getByName( LiteralHelper.anchorSet.getNameByDisplayName( cmbRangeAnchor.getText( ) ) ),
								isFlippedAxes( ) ) );
			}
		}
		else if ( e.getSource( ).equals( btnLineFormatSpecifier ) )
		{
			handleMarkerLineFormatBtnSelected( );
		}
		else if ( e.getSource( ).equals( btnStartFormatSpecifier )
				|| e.getSource( ).equals( btnEndFormatSpecifier ) )
		{
			handleMarkerRangeFormatBtnSelected( );
		}
		else if ( e.widget.equals( btnLineTriggers ) )
		{
			new TriggerEditorDialog( cmpContent.getShell( ),
					getAxisForProcessing( ).getMarkerLines( )
							.get( getMarkerIndex( ) )
							.getTriggers( ),
					getAxisForProcessing( ).getMarkerLines( )
							.get( getMarkerIndex( ) ),
					getContext( ),
					Messages.getString( "AxisMarkersSheet.Title.MarkerLine" ), TriggerSupportMatrix.TYPE_MARKERLINE, false, true ).open( ); //$NON-NLS-1$
		}
		else if ( e.widget.equals( btnRangeTriggers ) )
		{
			new TriggerEditorDialog( cmpContent.getShell( ),
					getAxisForProcessing( ).getMarkerRanges( )
							.get( getMarkerIndex( ) )
							.getTriggers( ),
					getAxisForProcessing( ).getMarkerRanges( )
							.get( getMarkerIndex( ) ),
					getContext( ),
					Messages.getString( "AxisMarkersSheet.Title.MarkerRange" ), TriggerSupportMatrix.TYPE_MARKERRANGE, false, true ).open( ); //$NON-NLS-1$
		}
		else if ( e.widget == btnTxtValueAuto )
		{
			MarkerLine line = getAxisForProcessing( ).getMarkerLines( )
					.get( getMarkerIndex( ) );
			ChartElementUtil.setEObjectAttribute( line,
					"value", //$NON-NLS-1$
					getNotNullDataElement( txtValue.getDataElement( ) ),
					btnTxtValueAuto.getSelection( ) );
			txtValue.setDataElement( line.getValue( ) );
			txtValue.setEnabled( !btnTxtValueAuto.getSelection( ) );
		}
		else if ( e.widget == btnTxtStartValueAuto )
		{
			MarkerRange range = getAxisForProcessing( ).getMarkerRanges( )
					.get( getMarkerIndex( ) );
			ChartElementUtil.setEObjectAttribute( range,
					"startValue", //$NON-NLS-1$
					getNotNullDataElement( txtStartValue.getDataElement( ) ),
					btnTxtStartValueAuto.getSelection( ) );
			txtStartValue.setDataElement( range.getStartValue( ) );
			txtStartValue.setEnabled( !btnTxtStartValueAuto.getSelection( ) );
		}
		else if ( e.widget == btnTxtEndValueAuto )
		{
			MarkerRange range = getAxisForProcessing( ).getMarkerRanges( )
					.get( getMarkerIndex( ) );
			ChartElementUtil.setEObjectAttribute( range,
					"endValue", //$NON-NLS-1$
					getNotNullDataElement( txtEndValue.getDataElement( ) ),
					btnTxtEndValueAuto.getSelection( ) );
			txtEndValue.setDataElement( range.getStartValue( ) );
			txtEndValue.setEnabled( !btnTxtEndValueAuto.getSelection( ) );
		}
	}

	protected void handleMarkerRangeFormatBtnSelected( )
	{
		String sAxisTitle = ""; //$NON-NLS-1$
		try
		{
			String sTitleString = getAxisForProcessing( ).getTitle( )
					.getCaption( )
					.getValue( );
			int iSeparatorIndex = sTitleString.indexOf( ExternalizedTextEditorComposite.SEPARATOR );
			if ( iSeparatorIndex > 0 )
			{
				sTitleString = sTitleString.substring( iSeparatorIndex );
			}
			else if ( iSeparatorIndex == 0 )
			{
				sTitleString = sTitleString.substring( ExternalizedTextEditorComposite.SEPARATOR.length( ) );
			}
			sAxisTitle = new MessageFormat( Messages.getString( "BaseAxisMarkerAttributeSheetImpl.Lbl.ForAxis" ) ).format( new Object[]{sTitleString} ); //$NON-NLS-1$ 
		}
		catch ( NullPointerException e1 )
		{
		}

		FormatSpecifier formatspecifier = null;
		if ( getAxisForProcessing( ).getMarkerRanges( )
				.get( getMarkerIndex( ) )
				.getFormatSpecifier( ) != null )
		{
			formatspecifier = getAxisForProcessing( ).getMarkerRanges( )
					.get( getMarkerIndex( ) )
					.getFormatSpecifier( );
		}
		
		getContext( ).getUIServiceProvider( )
				.getFormatSpecifierHandler( )
				.handleFormatSpecifier( cmpContent.getShell( ),
						new MessageFormat( Messages.getString( "BaseAxisMarkerAttributeSheetImpl.Lbl.MarkerRange" ) ).format( new Object[]{Integer.valueOf( getMarkerIndex( ) + 1 ), sAxisTitle} ), //$NON-NLS-1$
						new AxisType[]{
							getDataElementType( getAxisForProcessing( ).getMarkerRanges( )
									.get( getMarkerIndex( ) )
									.getStartValue( ) )
						},
						formatspecifier,
						getAxisForProcessing( ).getMarkerRanges( )
								.get( getMarkerIndex( ) ),
						"formatSpecifier", //$NON-NLS-1$
						getContext( ) );
	}

	protected void handleMarkerLineFormatBtnSelected( )
	{
		String sAxisTitle = ""; //$NON-NLS-1$
		try
		{
			sAxisTitle = new MessageFormat( Messages.getString( "BaseAxisMarkerAttributeSheetImpl.Lbl.ForAxis" ) ).format( new Object[]{getAxisForProcessing( ).getTitle( ).getCaption( ).getValue( )} ); //$NON-NLS-1$
		}
		catch ( NullPointerException e1 )
		{
		}

		FormatSpecifier formatspecifier = null;
		if ( getAxisForProcessing( ).getMarkerLines( )
				.get( getMarkerIndex( ) )
				.getFormatSpecifier( ) != null )
		{
			formatspecifier = getAxisForProcessing( ).getMarkerLines( )
					.get( getMarkerIndex( ) )
					.getFormatSpecifier( );
		}

		getContext( ).getUIServiceProvider( )
				.getFormatSpecifierHandler( )
				.handleFormatSpecifier( cmpContent.getShell( ),
						new MessageFormat( Messages.getString( "BaseAxisMarkerAttributeSheetImpl.Lbl.MarkerLine" ) ).format( new Object[]{Integer.valueOf( getMarkerIndex( ) + 1 ), sAxisTitle} ), //$NON-NLS-1$
						new AxisType[]{
							getDataElementType( getAxisForProcessing( ).getMarkerLines( )
									.get( getMarkerIndex( ) )
									.getValue( ) )
						},
						formatspecifier,
						getAxisForProcessing( ).getMarkerLines( )
								.get( getMarkerIndex( ) ),
						"formatSpecifier", //$NON-NLS-1$
						getContext( ) );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.swt.events.SelectionListener#widgetDefaultSelected(org.eclipse
	 * .swt.events.SelectionEvent)
	 */
	public void widgetDefaultSelected( SelectionEvent e )
	{
	}

	protected Axis getAxisForProcessing( )
	{
		return axis;
	}

	protected int getMarkerIndex( )
	{
		String sSelectedMarker = lstMarkers.getSelection( )[0];
		int iSelectionIndex = lstMarkers.getSelectionIndex( );
		if ( sSelectedMarker.startsWith( MARKER_LINE_LABEL ) )
		{
			return iSelectionIndex;
		}
		return iSelectionIndex
				- ( getAxisForProcessing( ).getMarkerLines( ).size( ) );
	}

	private void buildList( )
	{
		// Clear any existing contents
		lstMarkers.removeAll( );

		// Populate list of markers
		iLineCount = getAxisForProcessing( ).getMarkerLines( ).size( );
		iRangeCount = getAxisForProcessing( ).getMarkerRanges( ).size( );
		for ( int iLines = 0; iLines < iLineCount; iLines++ )
		{
			lstMarkers.add( MARKER_LINE_LABEL + " - " + ( iLines + 1 ) ); //$NON-NLS-1$
		}
		for ( int iRanges = 0; iRanges < iRangeCount; iRanges++ )
		{
			lstMarkers.add( MARKER_RANGE_LABEL + " - " + ( iRanges + 1 ) ); //$NON-NLS-1$
		}
	}

	private void refreshButtons( )
	{
		btnRemove.setEnabled( lstMarkers.getSelectionIndex( ) != -1 );
	}

	private void updateUIForSelection( )
	{
		String sSelectedMarker = lstMarkers.getSelection( )[0];
		// Switch stack layout topControl
		if ( sSelectedMarker.startsWith( MARKER_LINE_LABEL ) )
		{
			slMarkers.topControl = cmpLine;
			grpGeneral.layout( );

			MarkerLine line = getAxisForProcessing( ).getMarkerLines( )
					.get( getMarkerIndex( ) );

			// Update the value fields
			txtValue.setDataElement( line.getValue( ) );
			btnTxtValueAuto.setSelection( line.getValue( ) == null );

			// Update the Anchor field
			if ( !line.isSetLabelAnchor( ) )
			{
				cmbLineAnchor.select( 0 );
			}
			else
			{
				cmbLineAnchor.setText( LiteralHelper.anchorSet.getDisplayNameByName( ChartUIUtil.getFlippedAnchor( line.getLabelAnchor( ),
					isFlippedAxes( ) )
					.getName( ) ) );
			}

			// Update the Line attribute fields
			liacMarkerLine.setLineAttributes( line.getLineAttributes( ) );

			// Update the Label attribute fields
			lacLabel.setLabel( line.getLabel( ), getChart( ).getUnits( ) );
		}
		else
		{
			slMarkers.topControl = cmpRange;
			grpGeneral.layout( );

			MarkerRange range = getAxisForProcessing( ).getMarkerRanges( )
					.get( getMarkerIndex( ) );

			// Update the value fields
			txtStartValue.setDataElement( range.getStartValue( ) );
			btnTxtStartValueAuto.setSelection( range.getStartValue( ) == null );
			
			txtEndValue.setDataElement( range.getEndValue( ) );
			btnTxtEndValueAuto.setSelection( range.getEndValue( ) == null );

			// Update the anchor field
			if ( !range.isSetLabelAnchor( ) )
			{
				cmbRangeAnchor.select( 0 );
			}
			else
			{
				cmbRangeAnchor.setText( LiteralHelper.anchorSet.getDisplayNameByName( ChartUIUtil.getFlippedAnchor( range.getLabelAnchor( ),
						isFlippedAxes( ) )
						.getName( ) ) );
			}

			// Update the fill
			fccRange.setFill( range.getFill( ) );

			// Update the Line attribute fields
			liacMarkerRange.setLineAttributes( range.getOutline( ) );

			// Update the Label attribute fields
			lacLabel.setLabel( range.getLabel( ), getChart( ).getUnits( ) );
		}
	}

	private void populateLists( )
	{
		buildList( );

		NameSet ns = LiteralHelper.anchorSet;

		// Populate combo boxes
		cmbLineAnchor.setItems( ChartUIExtensionUtil.getItemsWithAuto( ns.getDisplayNames( ) ) );
		cmbLineAnchor.select( 0 );

		cmbRangeAnchor.setItems( ChartUIExtensionUtil.getItemsWithAuto( ns.getDisplayNames( ) ) );
		cmbRangeAnchor.select( 0 );

		if ( lstMarkers.getItemCount( ) > 0 )
		{
			lstMarkers.select( 0 );
			updateUIForSelection( );
			setState( true );
		}
		else
		{
			setState( false );
		}
	}

	private void setState( boolean bState )
	{
		btnLineFormatSpecifier.setEnabled( bState );
		lblAnchor.setEnabled( bState );
		cmbLineAnchor.setEnabled( bState );
		lblValue.setEnabled( bState );
		btnTxtValueAuto.setEnabled( bState );
		txtValue.setEnabled( bState && !btnTxtValueAuto.getSelection( ) );
		liacMarkerLine.setAttributesEnabled( bState );
		btnLineTriggers.setEnabled( bState
				&& getChart( ).getInteractivity( ).isEnable( ) );

		btnStartFormatSpecifier.setEnabled( bState );
		btnEndFormatSpecifier.setEnabled( bState );
		lblRangeAnchor.setEnabled( bState );
		cmbRangeAnchor.setEnabled( bState );
		lblStartValue.setEnabled( bState );
		btnTxtStartValueAuto.setEnabled( bState );
		txtStartValue.setEnabled( bState && !btnTxtStartValueAuto.getSelection( ) );
		lblEndValue.setEnabled( bState );
		btnTxtEndValueAuto.setEnabled( bState );
		txtEndValue.setEnabled( bState && !btnTxtEndValueAuto.getSelection( ) );
		liacMarkerRange.setAttributesEnabled( bState );
		lacLabel.setEnabled( bState );
		lblRangeFill.setEnabled( bState );
		fccRange.setEnabled( bState );
		btnRangeTriggers.setEnabled( bState
				&& getChart( ).getInteractivity( ).isEnable( ) );

		this.grpGeneral.setEnabled( bState );
		this.grpMarkerLine.setEnabled( bState );
		this.grpMarkerRange.setEnabled( bState );
	}

	private void resetUI( )
	{
		cmbLineAnchor.select( 0 );
		cmbRangeAnchor.select( 0 );

		slMarkers.topControl = cmpLine;
		txtValue.setDataElement( null );
		liacMarkerLine.setLineAttributes( null );
		liacMarkerLine.layout( );
		txtStartValue.setDataElement( null );
		txtEndValue.setDataElement( null );
		fccRange.setFill( null );
		liacMarkerRange.setLineAttributes( null );
		liacMarkerRange.layout( );
		lacLabel.setLabel( LabelImpl.create( ), getChart( ).getUnits( ) );
		lacLabel.layout( );
	}

	private org.eclipse.birt.chart.model.component.Label getSelectedMarkerLabel( )
	{
		String sSelectedMarker = lstMarkers.getSelection( )[0];
		int iMarkerIndex = getMarkerIndex( );
		if ( sSelectedMarker.startsWith( MARKER_LINE_LABEL ) )
		{
			return getAxisForProcessing( ).getMarkerLines( )
					.get( iMarkerIndex )
					.getLabel( );
		}
		return getAxisForProcessing( ).getMarkerRanges( )
				.get( iMarkerIndex )
				.getLabel( );
	}

	private DataElement createDefaultDataElement( )
	{
		Axis axis = getAxisForProcessing( );
		
		if ( axis.isSetType( ) )
		{
			if ( axis.getType( ).equals( AxisType.DATE_TIME_LITERAL )
					&& !axis.isCategoryAxis( ) )
			{
				Calendar c = Calendar.getInstance( );
				c.set( 1970, 0, 1, 0, 0, 0 );
				return DateTimeDataElementImpl.create( c );
			}
			return NumberDataElementImpl.create( 0.0 );
		}
		return null;
	}

	protected AxisType getDataElementType( DataElement de )
	{
		if ( de instanceof TextDataElement )
		{
			return AxisType.TEXT_LITERAL;
		}
		else if ( de instanceof DateTimeDataElement )
		{
			return AxisType.DATE_TIME_LITERAL;
		}
		else
		{
			return AxisType.LINEAR_LITERAL;
		}
	}

	private boolean isFlippedAxes( )
	{
		return ( (ChartWithAxes) context.getModel( ) ).getOrientation( )
				.equals( Orientation.HORIZONTAL_LITERAL );
	}

	private IDataElementComposite createValuePicker( Composite parent,
			DataElement data )
	{
		IDataElementComposite picker = null;
		Axis axis = getAxisForProcessing( );
		if ( axis.getType( ).getValue( ) == AxisType.DATE_TIME
				&& !axis.isCategoryAxis( ) )
		{
			try
			{
				picker = new DateTimeDataElementComposite( parent,
						SWT.BORDER,
						(DateTimeDataElement) data,
						true );
			}
			catch ( Exception e )
			{
				picker = new DateTimeDataElementComposite( parent,
						SWT.BORDER,
						null,
						true );
			}
		}
		else
		{
			try
			{
				picker = new NumberDataElementComposite( parent, data );
			}
			catch ( Exception e )
			{
				picker = new NumberDataElementComposite( parent, null );
			}
		}

		if ( picker != null )
		{
			GridData gd = new GridData( GridData.FILL_HORIZONTAL );
			picker.setLayoutData( gd );
			picker.addListener( this );
		}

		return picker;
	}

	private DataElement getNotNullDataElement( DataElement de )
	{
//		if ( de == null )
//		{
//			return createDefaultDataElement( );
//		}
		return de;
	}

	private FractionNumberFormatSpecifier createDefaultFractionFormatSpecifier( )
	{
		FractionNumberFormatSpecifier ffs = FractionNumberFormatSpecifierImpl.create( );
		ffs.setPrecise( false );
		return ffs;
	}

}