/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Actuate Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.chart.ui.swt.wizard.format.axis;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.attribute.AngleType;
import org.eclipse.birt.chart.model.attribute.AxisType;
import org.eclipse.birt.chart.model.attribute.ChartDimension;
import org.eclipse.birt.chart.model.attribute.FormatSpecifier;
import org.eclipse.birt.chart.model.attribute.IntersectionType;
import org.eclipse.birt.chart.model.component.Axis;
import org.eclipse.birt.chart.model.component.ComponentPackage;
import org.eclipse.birt.chart.model.component.MarkerLine;
import org.eclipse.birt.chart.model.component.MarkerRange;
import org.eclipse.birt.chart.model.data.BaseSampleData;
import org.eclipse.birt.chart.model.data.DataElement;
import org.eclipse.birt.chart.model.data.DateTimeDataElement;
import org.eclipse.birt.chart.model.data.NumberDataElement;
import org.eclipse.birt.chart.model.data.OrthogonalSampleData;
import org.eclipse.birt.chart.model.data.impl.DateTimeDataElementImpl;
import org.eclipse.birt.chart.model.data.impl.NumberDataElementImpl;
import org.eclipse.birt.chart.ui.extension.i18n.Messages;
import org.eclipse.birt.chart.ui.swt.composites.ExternalizedTextEditorComposite;
import org.eclipse.birt.chart.ui.swt.composites.FontDefinitionComposite;
import org.eclipse.birt.chart.ui.swt.composites.FormatSpecifierDialog;
import org.eclipse.birt.chart.ui.swt.composites.TextEditorComposite;
import org.eclipse.birt.chart.ui.swt.interfaces.ITaskPopupSheet;
import org.eclipse.birt.chart.ui.swt.interfaces.IUIServiceProvider;
import org.eclipse.birt.chart.ui.swt.wizard.ChartAdapter;
import org.eclipse.birt.chart.ui.swt.wizard.format.SubtaskSheetImpl;
import org.eclipse.birt.chart.ui.swt.wizard.format.popup.InteractivitySheet;
import org.eclipse.birt.chart.ui.swt.wizard.format.popup.axis.AxisGridLinesSheet;
import org.eclipse.birt.chart.ui.swt.wizard.format.popup.axis.AxisMarkersSheet;
import org.eclipse.birt.chart.ui.swt.wizard.format.popup.axis.AxisScaleSheet;
import org.eclipse.birt.chart.ui.swt.wizard.format.popup.axis.AxisLabelSheet;
import org.eclipse.birt.chart.ui.swt.wizard.format.popup.axis.AxisTitleSheet;
import org.eclipse.birt.chart.ui.util.ChartUIUtil;
import org.eclipse.birt.chart.ui.util.UIHelper;
import org.eclipse.birt.chart.util.LiteralHelper;
import org.eclipse.birt.chart.util.NameSet;
import org.eclipse.emf.common.util.EList;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;

import com.ibm.icu.text.NumberFormat;
import com.ibm.icu.text.SimpleDateFormat;
import com.ibm.icu.util.Calendar;
import com.ibm.icu.util.TimeZone;

/**
 * @author Actuate Corporation
 * 
 */
abstract class AbstractAxisSubtask extends SubtaskSheetImpl implements
		Listener,
		SelectionListener
{

	private transient ExternalizedTextEditorComposite txtTitle;

	private transient Button btnTitleVisible;

	private transient Combo cmbTypes;

	private transient Combo cmbOrigin;

	private transient Button btnFormatSpecifier;

	private transient Label lblValue;

	private transient TextEditorComposite txtValue;

	private transient Button btnLabelVisible;

	private transient FontDefinitionComposite fdcFont;

	private transient Button cbStaggered;

	private Button btnAxisTitle;

	private Button btnAxisLabel;

	AbstractAxisSubtask( )
	{
		super( );
	}

	abstract protected Axis getAxisForProcessing( );

	/**
	 * Returns the axis angle type
	 * 
	 * @return <code>AngleType.X</code>, <code>AngleType.Y</code> or
	 *         <code>AngleType.Z</code>
	 */
	abstract protected int getAxisAngleType( );

	public void createControl( Composite parent )
	{
		cmpContent = new Composite( parent, SWT.NONE );
		{
			GridLayout glContent = new GridLayout( 2, false );
			cmpContent.setLayout( glContent );
			GridData gd = new GridData( GridData.FILL_BOTH );
			cmpContent.setLayoutData( gd );
		}

		Composite cmpBasic = new Composite( cmpContent, SWT.NONE );
		{
			cmpBasic.setLayout( new GridLayout( 3, false ) );
			cmpBasic.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
		}

		Label lblTitle = new Label( cmpBasic, SWT.NONE );
		lblTitle.setText( Messages.getString( "AxisYSheetImpl.Label.Title" ) ); //$NON-NLS-1$

		List keys = null;
		IUIServiceProvider serviceprovider = getContext( ).getUIServiceProvider( );
		if ( serviceprovider != null )
		{
			keys = serviceprovider.getRegisteredKeys( );
		}

		txtTitle = new ExternalizedTextEditorComposite( cmpBasic,
				SWT.BORDER | SWT.SINGLE,
				-1,
				-1,
				keys,
				serviceprovider,
				getAxisForProcessing( ).getTitle( ).getCaption( ).getValue( ) );
		{
			GridData gd = new GridData( );
			gd.widthHint = 250;
			txtTitle.setLayoutData( gd );
			txtTitle.addListener( this );
		}

		btnTitleVisible = new Button( cmpBasic, SWT.CHECK );
		{
			btnTitleVisible.setText( Messages.getString( "ChartSheetImpl.Label.Visible" ) ); //$NON-NLS-1$
			btnTitleVisible.setSelection( getChart( ).getTitle( ).isVisible( ) );
			btnTitleVisible.addSelectionListener( this );
			btnTitleVisible.setSelection( getAxisForProcessing( ).getTitle( )
					.isVisible( ) );
		}

		if ( getAxisAngleType( ) != AngleType.Z )
		{
			Label lblType = new Label( cmpBasic, SWT.NONE );
			lblType.setText( Messages.getString( "OrthogonalAxisDataSheetImpl.Lbl.Type" ) ); //$NON-NLS-1$

			cmbTypes = new Combo( cmpBasic, SWT.DROP_DOWN | SWT.READ_ONLY );
			{
				GridData gd = new GridData( );
				gd.widthHint = 220;
				cmbTypes.setLayoutData( gd );
				cmbTypes.addSelectionListener( this );
			}

			btnFormatSpecifier = new Button( cmpBasic, SWT.PUSH );
			{
				GridData gdBTNFormatSpecifier = new GridData( );
				gdBTNFormatSpecifier.widthHint = 20;
				gdBTNFormatSpecifier.heightHint = 20;
				gdBTNFormatSpecifier.horizontalIndent = -3;
				btnFormatSpecifier.setLayoutData( gdBTNFormatSpecifier );
				btnFormatSpecifier.setImage( UIHelper.getImage( "icons/obj16/formatbuilder.gif" ) ); //$NON-NLS-1$
				btnFormatSpecifier.setToolTipText( Messages.getString( "Shared.Tooltip.FormatSpecifier" ) ); //$NON-NLS-1$
				btnFormatSpecifier.addSelectionListener( this );
				btnFormatSpecifier.getImage( )
						.setBackground( btnFormatSpecifier.getBackground( ) );
			}

			// Origin is not supported in 3D
			if ( getChart( ).getDimension( ).getValue( ) != ChartDimension.THREE_DIMENSIONAL )
			{
				Label lblOrigin = new Label( cmpBasic, SWT.NONE );
				lblOrigin.setText( Messages.getString( "OrthogonalAxisDataSheetImpl.Lbl.Origin" ) ); //$NON-NLS-1$

				cmbOrigin = new Combo( cmpBasic, SWT.DROP_DOWN | SWT.READ_ONLY );
				{
					GridData gd = new GridData( );
					gd.widthHint = 220;
					gd.horizontalSpan = 2;
					cmbOrigin.setLayoutData( gd );
					cmbOrigin.addSelectionListener( this );
				}

				boolean bValueOrigin = false;
				if ( getAxisForProcessing( ).getOrigin( ) != null )
				{
					if ( getAxisForProcessing( ).getOrigin( )
							.getType( )
							.equals( IntersectionType.VALUE_LITERAL ) )
					{
						bValueOrigin = true;
					}
				}

				lblValue = new Label( cmpBasic, SWT.NONE );
				{
					lblValue.setText( Messages.getString( "BaseAxisDataSheetImpl.Lbl.Value" ) ); //$NON-NLS-1$
					lblValue.setEnabled( bValueOrigin );
				}

				txtValue = new TextEditorComposite( cmpBasic, SWT.BORDER
						| SWT.SINGLE );
				{
					GridData gd = new GridData( );
					gd.widthHint = 245;
					gd.horizontalSpan = 2;
					txtValue.setLayoutData( gd );
					txtValue.addListener( this );
					txtValue.setEnabled( bValueOrigin );
				}
			}

			populateLists( );
		}

		new Label( cmpBasic, SWT.NONE ).setText( Messages.getString( "AxisYSheetImpl.Label.Labels" ) ); //$NON-NLS-1$

		fdcFont = new FontDefinitionComposite( cmpBasic,
				SWT.NONE,
				getContext( ),
				getAxisForProcessing( ).getLabel( ).getCaption( ).getFont( ),
				getAxisForProcessing( ).getLabel( ).getCaption( ).getColor( ),
				false );
		{
			GridData gdFDCFont = new GridData( );
			gdFDCFont.heightHint = fdcFont.getPreferredSize( ).y;
			gdFDCFont.widthHint = 250;
			fdcFont.setLayoutData( gdFDCFont );
			fdcFont.addListener( this );
		}

		Composite cmpLabel = new Composite( cmpBasic, SWT.NONE );
		{
			GridLayout layout = new GridLayout( 2, false );
			layout.marginHeight = 0;
			layout.marginWidth = 0;
			cmpLabel.setLayout( layout );
		}

		btnLabelVisible = new Button( cmpLabel, SWT.CHECK );
		{
			btnLabelVisible.setText( Messages.getString( "AbstractAxisSubtask.Label.Visible2" ) ); //$NON-NLS-1$
			GridData gd = new GridData( GridData.FILL_HORIZONTAL );
			btnLabelVisible.setLayoutData( gd );
			btnLabelVisible.addSelectionListener( this );
			btnLabelVisible.setSelection( getAxisForProcessing( ).getLabel( )
					.isVisible( ) );
		}

		cbStaggered = new Button( cmpLabel, SWT.CHECK );
		{
			cbStaggered.setSelection( getAxisForProcessing( ).isSetStaggered( )
					&& getAxisForProcessing( ).isStaggered( ) );
			cbStaggered.setText( Messages.getString( "AbstractAxisSubtask.Label.Stagger" ) ); //$NON-NLS-1$
			cbStaggered.addSelectionListener( this );
		}

		createButtonGroup( cmpContent );

		setStateOfTitle( );
		setStateOfLabel( );
	}

	private void setStateOfTitle( )
	{
		boolean isTitleEnabled = getAxisForProcessing( ).getTitle( )
				.isVisible( );
		txtTitle.setEnabled( isTitleEnabled );
		btnAxisTitle.setEnabled( isTitleEnabled );
	}

	private void setStateOfLabel( )
	{
		boolean isLabelEnabled = getAxisForProcessing( ).getLabel( )
				.isVisible( );
		fdcFont.setEnabled( isLabelEnabled );
		cbStaggered.setEnabled( isLabelEnabled );
		btnAxisLabel.setEnabled( isLabelEnabled );
	}

	private void createButtonGroup( Composite parent )
	{
		Composite cmp = new Composite( parent, SWT.NONE );
		{
			cmp.setLayout( new GridLayout( 6, false ) );
			GridData gridData = new GridData( GridData.FILL_HORIZONTAL );
			gridData.horizontalSpan = 2;
			gridData.grabExcessVerticalSpace = true;
			gridData.verticalAlignment = SWT.END;
			gridData.horizontalAlignment = SWT.BEGINNING;
			cmp.setLayoutData( gridData );
		}

		ITaskPopupSheet popup;
		if ( getAxisAngleType( ) != AngleType.Z )
		{
			// Scale
			popup = new AxisScaleSheet( Messages.getString( "AxisYSheetImpl.Label.Scale" ), //$NON-NLS-1$
					getContext( ),
					getAxisForProcessing( ),
					getAxisAngleType( ) );
			Button btnScale = createToggleButton( cmp,
					Messages.getString( "AxisYSheetImpl.Label.Scale&" ), //$NON-NLS-1$
					popup );
			btnScale.addSelectionListener( this );
		}

		// Title
		popup = new AxisTitleSheet( Messages.getString( "AxisYSheetImpl.Label.TitleFormat" ), //$NON-NLS-1$
				getContext( ),
				getAxisForProcessing( ),
				getAxisAngleType( ) );
		btnAxisTitle = createToggleButton( cmp,
				Messages.getString( "AxisYSheetImpl.Label.TitleFormat&" ), //$NON-NLS-1$
				popup );
		btnAxisTitle.addSelectionListener( this );
		btnAxisTitle.setEnabled( btnTitleVisible.getSelection( ) );

		// Label
		popup = new AxisLabelSheet( Messages.getString( "AxisYSheetImpl.Label.LabelFormat" ), //$NON-NLS-1$
				getContext( ),
				getAxisForProcessing( ),
				getAxisAngleType( ) );
		btnAxisLabel = createToggleButton( cmp,
				Messages.getString( "AxisYSheetImpl.Label.LabelFormat&" ), //$NON-NLS-1$
				popup );
		btnAxisLabel.addSelectionListener( this );
		btnAxisLabel.setEnabled( btnLabelVisible.getSelection( ) );

		// Gridlines
		popup = new AxisGridLinesSheet( Messages.getString( "AxisYSheetImpl.Label.Gridlines" ), //$NON-NLS-1$
				getContext( ),
				getAxisForProcessing( ),
				getAxisAngleType( ) );
		Button btnGridlines = createToggleButton( cmp,
				Messages.getString( "AxisYSheetImpl.Label.Gridlines&" ), //$NON-NLS-1$
				popup );
		btnGridlines.addSelectionListener( this );

		if ( getAxisAngleType( ) != AngleType.Z )
		{
			// Marker
			popup = new AxisMarkersSheet( Messages.getString( "AxisYSheetImpl.Label.Markers" ), //$NON-NLS-1$
					getContext( ),
					getAxisForProcessing( ) );
			Button btnMarkers = createToggleButton( cmp,
					Messages.getString( "AxisYSheetImpl.Label.Markers&" ), //$NON-NLS-1$
					popup );
			btnMarkers.addSelectionListener( this );
			// Marker is not supported for 3D
			btnMarkers.setEnabled( !ChartUIUtil.is3DType( getChart( ) ) );
		}

		// Interactivity
		popup = new InteractivitySheet( Messages.getString( "SeriesYSheetImpl.Label.Interactivity" ), //$NON-NLS-1$
				getContext( ),
				getAxisForProcessing( ).getTriggers( ),
				false,
				true );
		Button btnInteractivity = createToggleButton( cmp,
				Messages.getString( "SeriesYSheetImpl.Label.Interactivity&" ), //$NON-NLS-1$
				popup );
		btnInteractivity.addSelectionListener( this );
		btnInteractivity.setEnabled( getChart( ).getInteractivity( ).isEnable( ) );
	}

	private void populateLists( )
	{
		// Populate axis types combo
		NameSet ns = LiteralHelper.axisTypeSet;
		if ( getAxisAngleType( ) == AngleType.Y )
		{
			// Remove invalid Text choice since it can't be supported
			int indexText = ns.getSafeNameIndex( AxisType.TEXT_LITERAL.getName( ) );
			String[] originalItems = ns.getDisplayNames( );
			String[] newItems = new String[originalItems.length - 1];
			for ( int i = 0, j = 0; i < originalItems.length; i++ )
			{
				if ( i == indexText )
				{
					continue;
				}
				newItems[j++] = originalItems[i];
			}
			cmbTypes.setItems( newItems );
		}
		else
		{
			cmbTypes.setItems( ns.getDisplayNames( ) );
		}
		cmbTypes.setText( ns.getDisplayNameByName( getAxisForProcessing( ).getType( )
				.getName( ) ) );

		// Populate origin types combo
		if ( getChart( ).getDimension( ).getValue( ) != ChartDimension.THREE_DIMENSIONAL )
		{
			ns = LiteralHelper.intersectionTypeSet;
			cmbOrigin.setItems( ns.getDisplayNames( ) );
			cmbOrigin.select( ns.getSafeNameIndex( getAxisForProcessing( ).getOrigin( )
					.getType( )
					.getName( ) ) );
		}

		if ( txtValue != null
				&& !txtValue.isDisposed( )
				&& getAxisForProcessing( ).getOrigin( )
						.getType( )
						.equals( IntersectionType.VALUE_LITERAL ) )
		{
			txtValue.setText( getValue( getAxisForProcessing( ).getOrigin( )
					.getValue( ) ) );
		}
	}

	private String getValue( DataElement de )
	{
		if ( de instanceof DateTimeDataElement )
		{
			Date dt = ( (DateTimeDataElement) de ).getValueAsCalendar( )
					.getTime( );
			SimpleDateFormat sdf = new SimpleDateFormat( "MM/dd/yyyy" ); //$NON-NLS-1$
			return sdf.format( dt );
		}
		else if ( de instanceof NumberDataElement )
		{
			return ChartUIUtil.getDefaultNumberFormatInstance( )
					.format( ( (NumberDataElement) de ).getValue( ) );
		}
		return ""; //$NON-NLS-1$
	}

	private DataElement getTypedDataElement( String strDataElement )
	{
		SimpleDateFormat sdf = new SimpleDateFormat( "MM/dd/yyyy" ); //$NON-NLS-1$
		NumberFormat nf = ChartUIUtil.getDefaultNumberFormatInstance( );
		try
		{
			// First try Date
			Date dateElement = sdf.parse( strDataElement );
			Calendar cal = Calendar.getInstance( TimeZone.getDefault( ) );
			cal.setTime( dateElement );
			return DateTimeDataElementImpl.create( cal );
		}
		catch ( ParseException e )
		{
			// Next try double
			try
			{
				Number numberElement = nf.parse( strDataElement );
				return NumberDataElementImpl.create( numberElement.doubleValue( ) );
			}
			catch ( ParseException e1 )
			{
				return null;
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.widgets.Listener#handleEvent(org.eclipse.swt.widgets.Event)
	 */
	public void handleEvent( Event event )
	{
		if ( event.widget.equals( txtTitle ) )
		{
			getAxisForProcessing( ).getTitle( )
					.getCaption( )
					.setValue( (String) event.data );
		}
		else if ( event.widget.equals( txtValue ) )
		{
			DataElement de = getTypedDataElement( txtValue.getText( ) );
			if ( de != null )
			{
				getAxisForProcessing( ).getOrigin( ).setValue( de );
			}
		}
		else if ( event.widget.equals( fdcFont ) )
		{
			getAxisForProcessing( ).getLabel( )
					.getCaption( )
					.setFont( fdcFont.getFontDefinition( ) );
			getAxisForProcessing( ).getLabel( )
					.getCaption( )
					.setColor( fdcFont.getFontColor( ) );
		}
	}

	public void widgetSelected( SelectionEvent e )
	{
		// Detach popup dialog if there's selected button.
		if ( detachPopup( e.widget ) )
		{
			return;
		}

		if ( isRegistered( e.widget ) )
		{
			attachPopup( ( (Button) e.widget ).getText( ) );
		}

		if ( e.widget.equals( cmbTypes ) )
		{
			final AxisType axisType = AxisType.getByName( LiteralHelper.axisTypeSet.getNameByDisplayName( cmbTypes.getText( ) ) );
			if ( getAxisForProcessing( ).getType( ) == axisType )
			{
				// Prevent redundant operations
				return;
			}

			// Update the Sample Data and clean related format specifiers
			ChartAdapter.beginIgnoreNotifications( );
			{
				convertSampleData( axisType );
				getAxisForProcessing( ).setFormatSpecifier( null );

				EList markerLines = getAxisForProcessing( ).getMarkerLines( );
				for ( int i = 0; i < markerLines.size( ); i++ )
				{
					( (MarkerLine) markerLines.get( i ) ).setFormatSpecifier( null );
				}

				EList markerRanges = getAxisForProcessing( ).getMarkerRanges( );
				for ( int i = 0; i < markerRanges.size( ); i++ )
				{
					( (MarkerRange) markerRanges.get( i ) ).setFormatSpecifier( null );
				}
			}
			ChartAdapter.endIgnoreNotifications( );

			// Set type and refresh the preview
			getAxisForProcessing( ).setType( axisType );
			// Update popup UI
			refreshPopupSheet( );
		}
		else if ( e.widget.equals( cmbOrigin ) )
		{
			if ( LiteralHelper.intersectionTypeSet.getNameByDisplayName( cmbOrigin.getText( ) )
					.equals( IntersectionType.VALUE_LITERAL.getName( ) ) )
			{
				lblValue.setEnabled( true );
				txtValue.setEnabled( true );
			}
			else
			{
				lblValue.setEnabled( false );
				txtValue.setEnabled( false );
			}
			getAxisForProcessing( ).getOrigin( )
					.setType( IntersectionType.getByName( LiteralHelper.intersectionTypeSet.getNameByDisplayName( cmbOrigin.getText( ) ) ) );
		}
		else if ( e.widget.equals( btnTitleVisible ) )
		{
			getAxisForProcessing( ).getTitle( )
					.setVisible( btnTitleVisible.getSelection( ) );
			setStateOfTitle( );
			if ( !btnTitleVisible.getSelection( )
					&& btnAxisTitle.getSelection( ) )
			{
				btnAxisTitle.setSelection( false );
				detachPopup( btnAxisTitle );
			}
			else
			{
				refreshPopupSheet( );
			}
		}
		else if ( e.widget.equals( btnLabelVisible ) )
		{
			getAxisForProcessing( ).getLabel( )
					.setVisible( btnLabelVisible.getSelection( ) );
			setStateOfLabel( );
			if ( !btnLabelVisible.getSelection( )
					&& btnAxisLabel.getSelection( ) )
			{
				btnAxisLabel.setSelection( false );
				detachPopup( btnAxisLabel );
			}
			else
			{
				refreshPopupSheet( );
			}
		}
		else if ( e.widget.equals( btnFormatSpecifier ) )
		{
			String sAxisTitle = Messages.getString( "OrthogonalAxisDataSheetImpl.Lbl.OrthogonalAxis" ); //$NON-NLS-1$
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
				sAxisTitle += " (" + sTitleString + ")"; //$NON-NLS-1$ //$NON-NLS-2$
			}
			catch ( NullPointerException e1 )
			{
			}

			FormatSpecifier formatspecifier = null;
			if ( getAxisForProcessing( ).getFormatSpecifier( ) != null )
			{
				formatspecifier = getAxisForProcessing( ).getFormatSpecifier( );
			}
			FormatSpecifierDialog editor = new FormatSpecifierDialog( cmpContent.getShell( ),
					formatspecifier,
					getAxisForProcessing( ).getType( ),
					sAxisTitle );
			if ( editor.open( ) == Window.OK )
			{
				if ( editor.getFormatSpecifier( ) == null )
				{
					getAxisForProcessing( ).eUnset( ComponentPackage.eINSTANCE.getAxis_FormatSpecifier( ) );
					return;
				}
				getAxisForProcessing( ).setFormatSpecifier( editor.getFormatSpecifier( ) );
			}
		}
		else if ( e.getSource( ).equals( cbStaggered ) )
		{
			getAxisForProcessing( ).setStaggered( cbStaggered.getSelection( ) );
		}
	}

	public void widgetDefaultSelected( SelectionEvent e )
	{
		// TODO Auto-generated method stub
	}

	private void convertSampleData( AxisType axisType )
	{
		if ( getAxisAngleType( ) == AngleType.X )
		{
			BaseSampleData bsd = (BaseSampleData) getChart( ).getSampleData( )
					.getBaseSampleData( )
					.get( 0 );
			bsd.setDataSetRepresentation( ChartUIUtil.getConvertedSampleDataRepresentation( axisType,
					bsd.getDataSetRepresentation( ),
					0 ) );
		}
		else if ( getAxisAngleType( ) == AngleType.Y )
		{
			// Run the conversion routine for ALL orthogonal sample data entries
			// related to series definitions for this axis
			// Get the start and end index of series definitions that fall under
			// this axis
			int iStartIndex = getFirstSeriesDefinitionIndexForAxis( );
			int iEndIndex = iStartIndex
					+ getAxisForProcessing( ).getSeriesDefinitions( ).size( );
			// for each entry in orthogonal sample data, if the series index for
			// the
			// entry is in this range...run conversion
			// routine
			int iOSDSize = getChart( ).getSampleData( )
					.getOrthogonalSampleData( )
					.size( );
			for ( int i = 0; i < iOSDSize; i++ )
			{
				OrthogonalSampleData osd = (OrthogonalSampleData) getChart( ).getSampleData( )
						.getOrthogonalSampleData( )
						.get( i );
				if ( osd.getSeriesDefinitionIndex( ) >= iStartIndex
						&& osd.getSeriesDefinitionIndex( ) <= iEndIndex )
				{
					osd.setDataSetRepresentation( ChartUIUtil.getConvertedSampleDataRepresentation( axisType,
							osd.getDataSetRepresentation( ),
							i ) );
				}
			}
		}
	}

	private int getFirstSeriesDefinitionIndexForAxis( )
	{
		int iTmp = 0;
		for ( int i = 0; i < getIndex( ); i++ )
		{
			iTmp += ChartUIUtil.getAxisYForProcessing( (ChartWithAxes) getChart( ),
					i )
					.getSeriesDefinitions( )
					.size( );
		}
		return iTmp;
	}
}