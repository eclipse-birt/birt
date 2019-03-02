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

package org.eclipse.birt.chart.ui.swt.wizard.format.series;

import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.ChartWithoutAxes;
import org.eclipse.birt.chart.model.DialChart;
import org.eclipse.birt.chart.model.attribute.ChartDimension;
import org.eclipse.birt.chart.model.component.CurveFitting;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.model.type.DialSeries;
import org.eclipse.birt.chart.model.type.DifferenceSeries;
import org.eclipse.birt.chart.model.type.GanttSeries;
import org.eclipse.birt.chart.model.type.LineSeries;
import org.eclipse.birt.chart.model.type.PieSeries;
import org.eclipse.birt.chart.model.util.ChartDefaultValueUtil;
import org.eclipse.birt.chart.ui.extension.i18n.Messages;
import org.eclipse.birt.chart.ui.swt.ChartCheckbox;
import org.eclipse.birt.chart.ui.swt.composites.TriggerDataComposite;
import org.eclipse.birt.chart.ui.swt.interfaces.ISeriesButtonEntry;
import org.eclipse.birt.chart.ui.swt.interfaces.ISeriesUIProvider;
import org.eclipse.birt.chart.ui.swt.interfaces.ITaskPopupSheet;
import org.eclipse.birt.chart.ui.swt.wizard.format.SubtaskSheetImpl;
import org.eclipse.birt.chart.ui.swt.wizard.format.popup.InteractivitySheet;
import org.eclipse.birt.chart.ui.swt.wizard.format.popup.series.DecorationSheet;
import org.eclipse.birt.chart.ui.swt.wizard.format.popup.series.DialLabelSheet;
import org.eclipse.birt.chart.ui.swt.wizard.format.popup.series.DialScaleSheet;
import org.eclipse.birt.chart.ui.swt.wizard.format.popup.series.DialTickSheet;
import org.eclipse.birt.chart.ui.swt.wizard.format.popup.series.LineSeriesMarkerSheet;
import org.eclipse.birt.chart.ui.swt.wizard.format.popup.series.NeedleSheet;
import org.eclipse.birt.chart.ui.swt.wizard.format.popup.series.PieTitleSheet;
import org.eclipse.birt.chart.ui.swt.wizard.format.popup.series.SeriesLabelSheet;
import org.eclipse.birt.chart.ui.swt.wizard.format.popup.series.SeriesRegionSheet;
import org.eclipse.birt.chart.ui.swt.wizard.format.popup.series.SeriesTrendlineSheet;
import org.eclipse.birt.chart.ui.util.ChartUIConstants;
import org.eclipse.birt.chart.ui.util.ChartUIExtensionUtil;
import org.eclipse.birt.chart.ui.util.ChartUIUtil;
import org.eclipse.birt.chart.util.TriggerSupportMatrix;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Listener;

/**
 * Subtask for Value Series
 * 
 */
public class SeriesYSheetImpl extends SubtaskSheetImpl implements
		Listener,
		SelectionListener
{

	private Button btnShowCurveLine;

	protected ChartCheckbox btnLabelVisible;

	private ChartCheckbox btnDecoVisible;

	public void createControl( Composite parent )
	{
		cmpContent = new Composite( parent, SWT.NONE );
		{
			GridLayout glContent = new GridLayout( );
			cmpContent.setLayout( glContent );
			GridData gd = new GridData( GridData.FILL_BOTH );
			cmpContent.setLayoutData( gd );
		}

		// Series details composite
		Group grpDetails = new Group( cmpContent, SWT.NONE );
		GridData gdCMPDetails = new GridData( GridData.FILL_BOTH );
		gdCMPDetails.horizontalSpan = 2;
		grpDetails.setLayoutData( gdCMPDetails );
		grpDetails.setLayout( new FillLayout( ) );
		grpDetails.setText( Messages.getString( "OrthogonalSeriesAttributeSheetImpl.Lbl.SeriesDetails" ) ); //$NON-NLS-1$

		// Series composite
		Series series = getCurrentDesignTimeSeries( );
		Series defSeries = ChartDefaultValueUtil.getDefaultSeries( series );
		getSeriesAttributeUI( series, grpDetails );

		Composite cmpBottom = new Composite( cmpContent, SWT.NONE );
		GridLayout glBottom = new GridLayout( 4, false );
		cmpBottom.setLayout( glBottom );

		btnLabelVisible = getContext( ).getUIFactory( )
				.createChartCheckbox( cmpBottom, SWT.NONE, false );
		{
			org.eclipse.birt.chart.model.component.Label l = null;
			org.eclipse.birt.chart.model.component.Label defLabel = null;
			if ( isMeterSeries( ) )
			{
				l = ( (DialSeries) getCurrentDesignTimeSeries( ) ).getDial( )
						.getLabel( );
				defLabel =  ( (DialSeries) defSeries ).getDial( ).getLabel( );
			}
			else
			{
				l = getCurrentDesignTimeSeries( ).getLabel( );
				defLabel = defSeries.getLabel( );
			}
			btnLabelVisible.setDefaultSelection( defLabel.isVisible( ) );
			btnLabelVisible.setSelectionState( l.isSetVisible( ) ? ( l.isVisible( ) ? ChartCheckbox.STATE_SELECTED
					: ChartCheckbox.STATE_UNSELECTED )
					: ChartCheckbox.STATE_GRAYED );
			btnLabelVisible.addSelectionListener( this );

			if ( isMeterSeries( ) )
			{
				btnLabelVisible.setText( Messages.getString( "SeriesYSheetImpl.Label.ShowDialLabels" ) ); //$NON-NLS-1$
			}
			else
			{
				btnLabelVisible.setText( Messages.getString( "SeriesYSheetImpl.Label.ShowLabels" ) ); //$NON-NLS-1$
			}
		}

		if ( isShowTaskLabelsAvailable( ) )
		{
			btnDecoVisible = getContext( ).getUIFactory( ).createChartCheckbox( cmpBottom, SWT.NONE,  false );
			{
				org.eclipse.birt.chart.model.component.Label l = ( (GanttSeries) getCurrentDesignTimeSeries( ) ).getDecorationLabel( );
				btnDecoVisible.setDefaultSelection( ( (GanttSeries) defSeries ).getDecorationLabel( )
						.isVisible( ) );
				btnDecoVisible.setSelectionState( l.isSetVisible( ) ? ( l.isVisible( ) ? ChartCheckbox.STATE_SELECTED
						: ChartCheckbox.STATE_UNSELECTED )
						: ChartCheckbox.STATE_GRAYED );
				btnDecoVisible.addSelectionListener( this );
			}
			btnDecoVisible.setText( Messages.getString( "SeriesYSheetImpl.Label.ShowDecoLabels" ) ); //$NON-NLS-1$
		}

		if ( isTrendlineAvailable( ) )
		{
			btnShowCurveLine = new Button( cmpBottom, SWT.CHECK );
			{
				GridData gd = new GridData( );
				gd.horizontalSpan = 2;
				btnShowCurveLine.setText( Messages.getString( "SeriesYSheetImpl.Label.ShowTrendline" ) ); //$NON-NLS-1$
				btnShowCurveLine.addSelectionListener( this );
				btnShowCurveLine.setSelection( getCurrentDesignTimeSeries( ).getCurveFitting( ) != null );
			}
		}

		createButtonGroup( cmpContent );
	}

	protected boolean isShowTaskLabelsAvailable( )
	{
		return isGanttSeries( );
	}

	protected void createButtonGroup( Composite parent )
	{
		Composite cmp = new Composite( parent, SWT.NONE );
		{
			cmp.setLayout( new GridLayout( 6, false ) );
			GridData gridData = new GridData( GridData.FILL_HORIZONTAL );
			gridData.horizontalSpan = 2;
			gridData.grabExcessVerticalSpace = true;
			gridData.verticalAlignment = SWT.END;
			cmp.setLayoutData( gridData );
		}

		// For Meter series and other non-Stock series
		ITaskPopupSheet popup;
		Series series = getCurrentDesignTimeSeries( );

		if ( isMeterSeries( ) )
		{
			// Label
			createDialLabelBtnUI( cmp );

			if ( needDialNeedle( ) )
			{
				// Needles
				popup = new NeedleSheet( Messages.getString( "SeriesYSheetImpl.Label.Needles" ), //$NON-NLS-1$
						getContext( ),
						getCurrentDesignTimeSeries( ) );
				Button btnNeedles = createToggleButton( cmp,
						BUTTON_NEEDLES,
						Messages.getString( "SeriesYSheetImpl.Label.Needles&" ), //$NON-NLS-1$
						popup );
				btnNeedles.addSelectionListener( this );
			}

			// Region
			popup = new SeriesRegionSheet( Messages.getString( "SeriesYSheetImpl.Label.Region" ), //$NON-NLS-1$
					getContext( ),
					getCurrentDesignTimeSeries( ) );
			Button btnDialRegion = createToggleButton( cmp,
					BUTTON_REGIONS,
					Messages.getString( "SeriesYSheetImpl.Label.Region&" ), //$NON-NLS-1$
					popup );
			btnDialRegion.addSelectionListener( this );

			// Ticks
			popup = new DialTickSheet( Messages.getString( "DialTicksDialog.Title.DialTicks" ), //$NON-NLS-1$
					getContext( ),
					( (DialSeries) getCurrentDesignTimeSeries( ) ) );
			Button btnDialTicks = createToggleButton( cmp,
					BUTTON_TICKS,
					Messages.getString( "MeterSeriesAttributeComposite.Lbl.DialTicks" ), //$NON-NLS-1$
					popup );
			btnDialTicks.addSelectionListener( this );

			// Scale
			popup = new DialScaleSheet( Messages.getString( "DialScaleDialog.Title.DialScale" ), //$NON-NLS-1$
					getContext( ),
					( (DialSeries) getCurrentDesignTimeSeries( ) ) );
			Button btnDialScale = createToggleButton( cmp,
					BUTTON_SCALE,
					Messages.getString( "MeterSeriesAttributeComposite.Lbl.DialScale" ), //$NON-NLS-1$
					popup );
			btnDialScale.addSelectionListener( this );
		}
		else
		{
			// Label
			createSeriesLabelBtnUI( cmp );
		}

		createSeriesSpecificButton( cmp );

		// Markers for Line/Area/Scatter series
		if ( isMarkerAvailable( ) )
		{
			popup = new LineSeriesMarkerSheet( Messages.getString( "SeriesYSheetImpl.Label.Markers" ), //$NON-NLS-1$
					getContext( ),
					(LineSeries) getCurrentDesignTimeSeries( ) );
			Button btnLineMarker = createToggleButton( cmp,
					BUTTON_MARKERS,
					Messages.getString( "SeriesYSheetImpl.Label.Markers&" ), //$NON-NLS-1$
					popup );
			btnLineMarker.addSelectionListener( this );
		}

		// Curve Line
		if ( isTrendlineAvailable( ) )
		{
			popup = new SeriesTrendlineSheet( Messages.getString( "SeriesYSheetImpl.Label.Trendline" ), //$NON-NLS-1$
					getContext( ),
					getCurrentDesignTimeSeries( ) );
			Button btnTrendline = createToggleButton( cmp,
					BUTTON_CURVE,
					Messages.getString( "SeriesYSheetImpl.Label.Trendline&" ), //$NON-NLS-1$
					popup,
					btnShowCurveLine.getSelection( ) );
			btnTrendline.addSelectionListener( this );
		}

		if ( getContext( ).isInteractivityEnabled( )
				&& !( getChart( ) instanceof DialChart && ( (DialChart) getChart( ) ).isDialSuperimposition( ) ) )
		{
			// Interactivity
			popup = new InteractivitySheet( Messages.getString( "SeriesYSheetImpl.Label.Interactivity" ), //$NON-NLS-1$
					getContext( ),
					getCurrentDesignTimeSeries( ).getTriggers( ),
					getCurrentDesignTimeSeries( ),
					TriggerSupportMatrix.TYPE_DATAPOINT,
					TriggerDataComposite.ENABLE_URL_PARAMETERS
							| TriggerDataComposite.ENABLE_TOOLTIP_FORMATTER );
			Button btnInteractivity = createToggleButton( cmp,
					BUTTON_INTERACTIVITY,
					Messages.getString( "SeriesYSheetImpl.Label.Interactivity&" ), //$NON-NLS-1$
					popup,
					getChart( ).getInteractivity( ).isEnable( ) );
			btnInteractivity.addSelectionListener( this );
		}

		SeriesDefinition sd = getSeriesDefinitionForProcessing( );
		for ( ISeriesButtonEntry buttonEntry : getSeriesUIProvider( series ).getCustomButtons( getContext( ),
				sd ) )
		{
			Button button = createToggleButton( cmp,
					buttonEntry.getButtonId( ),
					buttonEntry.getPopupName( ),
					buttonEntry.getPopupSheet( ),
					buttonEntry.isEnabled( ) );
			button.addSelectionListener( this );
		}
	}

	protected void createPieSeriesButton( Composite cmp )
	{
		ITaskPopupSheet popup;

		popup = new PieTitleSheet( Messages.getString( "SeriesYSheetImpl.Label.Titles" ), //$NON-NLS-1$
				getContext( ),
				getCurrentDesignTimeSeries( ) );
		Button btnPieTitle = createToggleButton( cmp,
				BUTTON_TITLE,
				Messages.getString( "SeriesYSheetImpl.Label.Titles&" ), //$NON-NLS-1$
				popup );
		btnPieTitle.addSelectionListener( this );
	}
	
	protected void createDifferenceSeriesButton( Composite cmp )
	{
		ITaskPopupSheet popup;

		// Markers for Difference series
		popup = new LineSeriesMarkerSheet( Messages.getString( "SeriesYSheetImpl.Label.PositiveMarkers" ), //$NON-NLS-1$
				getContext( ),
				(DifferenceSeries) getCurrentDesignTimeSeries( ),
				true );
		Button btnPLineMarker = createToggleButton( cmp,
				BUTTON_POSITIVE_MARKERS,
				Messages.getString( "SeriesYSheetImpl.Label.PositiveMarkers&" ), //$NON-NLS-1$
				popup );
		btnPLineMarker.addSelectionListener( this );

		popup = new LineSeriesMarkerSheet( Messages.getString( "SeriesYSheetImpl.Label.NegativeMarkers" ), //$NON-NLS-1$
				getContext( ),
				(DifferenceSeries) getCurrentDesignTimeSeries( ),
				false );
		Button btnNLineMarker = createToggleButton( cmp,
				BUTTON_NEGATIVE_MARKERS,
				Messages.getString( "SeriesYSheetImpl.Label.NegativeMarkers&" ), //$NON-NLS-1$
				popup );
		btnNLineMarker.addSelectionListener( this );
	}
	
	protected void createGanttSeriesButton( Composite cmp )
	{
		ITaskPopupSheet popup;

		// Decoration Label for Gantt series
		popup = new DecorationSheet( Messages.getString( "SeriesYSheetImpl.Label.Decoration" ), //$NON-NLS-1$
				getContext( ),
				(GanttSeries) getCurrentDesignTimeSeries( ) );
		Button btnDecoration = createToggleButton( cmp,
				BUTTON_DECORATION,
				Messages.getString( "SeriesYSheetImpl.Label.Decoration&" ), //$NON-NLS-1$
				popup,
				getContext().getUIFactory( ).canEnableUI( btnDecoVisible ) );
		btnDecoration.addSelectionListener( this );
	}
	
	protected void createSeriesSpecificButton( Composite cmp )
	{
		if ( isPieSeries( ) )
		{
			createPieSeriesButton( cmp );
		}

		if ( isDifferenceSeries( ) )
		{
			createDifferenceSeriesButton( cmp );
		}

		if ( isGanttSeries( ) )
		{
			createGanttSeriesButton( cmp );
		}
	}

	/**
	 * @param series
	 * @return
	 */
	protected ISeriesUIProvider getSeriesUIProvider( Series series )
	{
		return ChartUIUtil.getSeriesUIProvider( series );
	}

	protected boolean needDialNeedle( )
	{
		return getChart( ) instanceof DialChart
				&& !( (DialChart) getChart( ) ).isDialSuperimposition( );
	}

	protected void createSeriesLabelBtnUI( Composite cmp )
	{
		ITaskPopupSheet popup;
		popup = new SeriesLabelSheet( Messages.getString( "SeriesYSheetImpl.Label.Labels" ), //$NON-NLS-1$
				getContext( ),
				getCurrentDesignTimeSeries( ) );
		Button btnLabel = createToggleButton( cmp,
				BUTTON_LABEL,
				Messages.getString( "SeriesYSheetImpl.Label.Labels&" ), //$NON-NLS-1$
				popup,
				getContext().getUIFactory( ).canEnableUI( btnLabelVisible ) );
		btnLabel.addSelectionListener( this );
	}

	protected void createDialLabelBtnUI( Composite cmp )
	{
		ITaskPopupSheet popup;
		popup = new DialLabelSheet( Messages.getString( "SeriesYSheetImpl.Label.DialLabels" ), //$NON-NLS-1$
				getContext( ),
				getCurrentDesignTimeSeries( ) );
		Button btnDialLabel = createToggleButton( cmp,
				BUTTON_DIAL_LABELS,
				Messages.getString( "SeriesYSheetImpl.Label.DialLabels&" ), //$NON-NLS-1$
				popup,
				getContext( ).getUIFactory( ).canEnableUI( btnLabelVisible ) );
		btnDialLabel.addSelectionListener( this );
	}

	protected void getSeriesAttributeUI( Series series, Composite parent )
	{
		getSeriesUIProvider( series ).getSeriesAttributeSheet( parent,
				series,
				getContext( ) );
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

	}

	public void widgetSelected( SelectionEvent e )
	{
		// Detach popup dialog if there's selected popup button.
		if ( detachPopup( e.widget ) )
		{
			return;
		}

		if ( isRegistered( e.widget ) )
		{
			attachPopup( ( (Button) e.widget ).getData( ).toString( ) );
		}

		if ( e.widget.equals( btnShowCurveLine ) )
		{
			setToggleButtonEnabled( BUTTON_CURVE,
					btnShowCurveLine.getSelection( ) );
			if ( btnShowCurveLine.getSelection( ) )
			{
				CurveFitting cf = ChartUIExtensionUtil.createCurveFitting( getContext( ) );
				cf.eAdapters( )
						.addAll( getCurrentDesignTimeSeries( ).eAdapters( ) );
				getCurrentDesignTimeSeries( ).setCurveFitting( cf );
			}
			else
			{
				getCurrentDesignTimeSeries( ).setCurveFitting( null );
				// Close trendline popup
				Button btnTrendline = getToggleButton( BUTTON_CURVE );
				if ( btnTrendline.getSelection( ) )
				{
					btnTrendline.setSelection( false );
					detachPopup( btnTrendline );
				}
			}
		}
		else if ( e.widget == btnLabelVisible )
		{
			boolean isAuto = ( btnLabelVisible.getSelectionState( ) == ChartCheckbox.STATE_GRAYED );
			if ( isMeterSeries( ) )
			{
				setToggleButtonEnabled( BUTTON_DIAL_LABELS,
						getContext().getUIFactory( ).canEnableUI( btnLabelVisible ) );
				if ( isAuto )
				{
					( (DialSeries) getCurrentDesignTimeSeries( ) ).getDial( )
							.getLabel( )
							.unsetVisible( );
				}
				else
				{
					( (DialSeries) getCurrentDesignTimeSeries( ) ).getDial( )
							.getLabel( )
							.setVisible( btnLabelVisible.getSelectionState( ) == ChartCheckbox.STATE_SELECTED );
				}
				Button btnDialLabel = getToggleButton( BUTTON_DIAL_LABELS );
				if ( btnLabelVisible.getSelectionState( ) != ChartCheckbox.STATE_SELECTED
						&& btnDialLabel.getSelection( ) )
				{
					btnDialLabel.setSelection( false );
					detachPopup( btnDialLabel );
				}
			}
			else
			{
				setToggleButtonEnabled( BUTTON_LABEL,
						getContext().getUIFactory( ).canEnableUI( btnLabelVisible ) );
				if ( isAuto )
				{
					getCurrentDesignTimeSeries( ).getLabel( ).unsetVisible( );
				}
				else
				{
					getCurrentDesignTimeSeries( ).getLabel( )
							.setVisible( btnLabelVisible.getSelectionState( ) == ChartCheckbox.STATE_SELECTED );
				}
			}

			refreshPopupSheet( );
		}
		else if ( e.widget == btnDecoVisible )
		{
			setToggleButtonEnabled( BUTTON_DECORATION,
					getContext().getUIFactory( ).canEnableUI( btnDecoVisible ) );
			if ( btnDecoVisible.getSelectionState( ) == ChartCheckbox.STATE_GRAYED )
			{
				( (GanttSeries) getCurrentDesignTimeSeries( ) ).getDecorationLabel( )
						.unsetVisible( );
			}
			else
			{
				( (GanttSeries) getCurrentDesignTimeSeries( ) ).getDecorationLabel( )
						.setVisible( btnDecoVisible.getSelectionState( ) == ChartCheckbox.STATE_SELECTED );
			}
			Button btnDecoration = getToggleButton( BUTTON_DECORATION );
			if ( btnDecoVisible.getSelectionState( ) != ChartCheckbox.STATE_SELECTED
					&& btnDecoration.getSelection( ) )
			{
				btnDecoration.setSelection( false );
				detachPopup( btnDecoration );
			}
			else
			{
				refreshPopupSheet( );
			}
		}
	}

	public void widgetDefaultSelected( SelectionEvent e )
	{
		// TODO Auto-generated method stub
	}

	protected SeriesDefinition getSeriesDefinitionForProcessing( )
	{
		SeriesDefinition sd = null;
		if ( getChart( ) instanceof ChartWithAxes )
		{
			int iAxis = getParentAxisIndex( getIndex( ) );
			int iAxisSeries = getSeriesIndexWithinAxis( getIndex( ) );
			sd = ( (ChartWithAxes) getChart( ) ).getAxes( )
					.get( 0 )
					.getAssociatedAxes( )
					.get( iAxis )
					.getSeriesDefinitions( )
					.get( iAxisSeries );
		}
		else if ( getChart( ) instanceof ChartWithoutAxes )
		{
			sd = ( (ChartWithoutAxes) getChart( ) ).getSeriesDefinitions( )
					.get( 0 )
					.getSeriesDefinitions( )
					.get( getIndex( ) );
		}
		return sd;
	}

	private int getParentAxisIndex( int iSeriesDefinitionIndex )
	{
		int iTmp = 0;
		int iAxisCount = ( (ChartWithAxes) getChart( ) ).getAxes( )
				.get( 0 )
				.getAssociatedAxes( )
				.size( );
		for ( int i = 0; i < iAxisCount; i++ )
		{
			iTmp += ( (ChartWithAxes) getChart( ) ).getAxes( )
					.get( 0 )
					.getAssociatedAxes( )
					.get( i )
					.getSeriesDefinitions( )
					.size( );
			if ( iTmp - 1 >= iSeriesDefinitionIndex )
			{
				return i;
			}
		}
		return 0;
	}

	private int getSeriesIndexWithinAxis( int iSeriesDefinitionIndex )
	{
		int iTotalDefinitions = 0;
		int iAxisCount = ( (ChartWithAxes) getChart( ) ).getAxes( )
				.get( 0 )
				.getAssociatedAxes( )
				.size( );
		for ( int i = 0; i < iAxisCount; i++ )
		{
			int iOldTotal = iTotalDefinitions;
			iTotalDefinitions += ( (ChartWithAxes) getChart( ) ).getAxes( )
					.get( 0 )
					.getAssociatedAxes( )
					.get( i )
					.getSeriesDefinitions( )
					.size( );
			if ( iTotalDefinitions - 1 >= iSeriesDefinitionIndex )
			{
				return iSeriesDefinitionIndex - iOldTotal;
			}
		}
		return iSeriesDefinitionIndex;
	}

	protected boolean isTrendlineAvailable( )
	{
		return ( getChart( ) instanceof ChartWithAxes )
				&& ( !isGanttSeries( ) )
				&& ( !isDifferenceSeries( ) )
				&& ( getChart( ).getDimension( ) != ChartDimension.THREE_DIMENSIONAL_LITERAL )
				&& getContext( ).isEnabled( ChartUIConstants.SUBTASK_SERIES_Y
						+ ChartUIConstants.BUTTON_CURVE );
	}

	protected boolean isMeterSeries( )
	{
		return getCurrentDesignTimeSeries( ) instanceof DialSeries;
	}

	protected Series getCurrentDesignTimeSeries( )
	{
		return getSeriesDefinitionForProcessing( ).getDesignTimeSeries( );
	}

	protected boolean isGanttSeries( )
	{
		return getCurrentDesignTimeSeries( ) instanceof GanttSeries;
	}

	protected boolean isDifferenceSeries( )
	{
		return getCurrentDesignTimeSeries( ) instanceof DifferenceSeries;
	}
	
	protected boolean isPieSeries( )
	{
		return getCurrentDesignTimeSeries( ) instanceof PieSeries;
	}

	protected boolean isMarkerAvailable( )
	{
		return getCurrentDesignTimeSeries( ) instanceof LineSeries
				&& !isDifferenceSeries( );
	}
}