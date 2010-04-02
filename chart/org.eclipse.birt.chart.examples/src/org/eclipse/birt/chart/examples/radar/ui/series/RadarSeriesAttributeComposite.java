/***********************************************************************
 * Copyright (c) 2010 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.chart.examples.radar.ui.series;

import java.math.BigInteger;

import org.eclipse.birt.chart.examples.radar.i18n.Messages;
import org.eclipse.birt.chart.examples.radar.model.type.RadarSeries;
import org.eclipse.birt.chart.examples.radar.model.type.impl.RadarSeriesImpl;
import org.eclipse.birt.chart.examples.radar.ui.type.RadarChart;
import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.log.ILogger;
import org.eclipse.birt.chart.log.Logger;
import org.eclipse.birt.chart.model.ChartWithoutAxes;
import org.eclipse.birt.chart.model.attribute.ColorDefinition;
import org.eclipse.birt.chart.model.attribute.LineStyle;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.ui.plugin.ChartUIExtensionPlugin;
import org.eclipse.birt.chart.ui.swt.composites.LineAttributesComposite;
import org.eclipse.birt.chart.ui.swt.composites.MarkerEditorComposite;
import org.eclipse.birt.chart.ui.swt.wizard.ChartWizardContext;
import org.eclipse.birt.chart.ui.util.ChartHelpContextIds;
import org.eclipse.birt.chart.ui.util.ChartUIUtil;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Spinner;

/**
 * @author Actuate Corporation
 * 
 */
public class RadarSeriesAttributeComposite extends Composite implements
		SelectionListener,
		Listener
{

	private static final int MAX_STEPS = 20;

	private Button btnTranslucentBullseye = null;

	private Button btnPalette = null;
	private Button btnFillPoly = null;
	private Button btnConnectEndPoints = null;
	private Button btnWebLabels = null;

	private Label lblWebStep = null;
	private Spinner iscScaleCnt = null;

	private MarkerEditorComposite mec = null;

	private ChartWithoutAxes chart;
	private Group grpLine = null;
	private Group grpLine2 = null;

	private LineAttributesComposite liacLine = null;

	private LineAttributesComposite wliacLine = null;

	private RadarSeries series = null;

	private ChartWizardContext context;

	private static ILogger logger = Logger.getLogger( "org.eclipse.birt.chart.examples/swt.series" ); //$NON-NLS-1$

	/**
	 * @param parent
	 * @param style
	 */
	public RadarSeriesAttributeComposite( Composite parent, int style,
			ChartWizardContext context, Series series )
	{
		super( parent, style );

		if ( !( series instanceof RadarSeriesImpl ) )
		{
			try
			{
				throw new ChartException( ChartUIExtensionPlugin.ID,
						ChartException.VALIDATION,
						"RadarSeriesAttributeComposite.Exception.IllegalArgument", new Object[]{series.getClass( ).getName( )}, Messages.getResourceBundle( ) ); //$NON-NLS-1$
			}
			catch ( ChartException e )
			{
				logger.log( e );
				e.printStackTrace( );
			}
		}
		this.series = (RadarSeries) series;
		this.context = context;
		this.chart = (ChartWithoutAxes) context.getModel( );

		init( );
		placeComponents( );

		ChartUIUtil.bindHelp( parent, ChartHelpContextIds.SUBTASK_YSERIES_LINE );
	}

	private void init( )
	{
		this.setSize( getParent( ).getClientArea( ).width,
				getParent( ).getClientArea( ).height );
	}

	private void placeComponents( )
	{
		boolean show_web_attributes = false;
		RadarSeries rsd = (RadarSeries) chart.getSeriesDefinitions( )
				.get( 0 )
				.getSeriesDefinitions( )
				.get( 0 )
				.getDesignTimeSeries( );
		if ( rsd.equals( this.series ) )
		{
			// SeriesIdentifier is not unique
			// if( firstSeries.equals(this.series.getSeriesIdentifier())){
			show_web_attributes = true;
		}
		// Main content composite
		this.setLayout( new GridLayout( ) );

		if ( show_web_attributes )
		{
			grpLine = new Group( this, SWT.NONE );
			GridLayout glLine = new GridLayout( 3, false );
			grpLine.setLayout( glLine );
			grpLine.setLayoutData( new GridData( GridData.FILL_BOTH ) );
			grpLine.setText( Messages.getString( "RadarSeriesMarkerSheet.Label.Web" ) ); //$NON-NLS-1$

			wliacLine = new LineAttributesComposite( grpLine,
					SWT.NONE,
					context,
					series.getWebLineAttributes( ),
					true,
					true,
					true );
			GridData wgdLIACLine = new GridData( );
			wgdLIACLine.widthHint = 200;
			wgdLIACLine.verticalSpan = 3;
			wliacLine.setLayoutData( wgdLIACLine );
			wliacLine.addListener( this );

			// private Label lblWebStep;
			// private Label lblWebPercentage;

			lblWebStep = new Label( grpLine, SWT.NONE );
			{
				lblWebStep.setText( Messages.getString( "Radar.Composite.Label.ScaleCount" ) ); //$NON-NLS-1$
				lblWebStep.setToolTipText( Messages.getString( "Radar.Composite.Label.ScaleCountToolTip" ) ); //$NON-NLS-1$
			}

			iscScaleCnt = new Spinner( grpLine, SWT.BORDER );
			GridData gdISCLeaderLength = new GridData( );
			gdISCLeaderLength.widthHint = 100;
			iscScaleCnt.setLayoutData( gdISCLeaderLength );
			iscScaleCnt.setMinimum( 1 );
			iscScaleCnt.setMaximum( MAX_STEPS );
			iscScaleCnt.setSelection( series.getPlotSteps( ).intValue( ) );
			iscScaleCnt.addSelectionListener( this );

			// lblWebStep = new Label( cmpLine1, SWT.NONE );
			// {
			// lblWebStep.setText( Messages.getString(
			// "Radar.Composite.Label.plotPercentage" ) );
			// lblWebStep.setToolTipText(
			// Messages.getString("Radar.Composite.Label.plotPercentageToolTip")
			// );
			// }

			// iscPlotPercentage = new Spinner( cmpLine1, SWT.BORDER );
			// iscPlotPercentage.setLayoutData( gdISCLeaderLength );
			// iscPlotPercentage.setMinimum( 1 );
			// iscPlotPercentage.setMaximum( MAX_PERCENTAGE );
			// iscPlotPercentage.setSelection( (int)
			// ((RadarSeries)series).getPlotPercentage() );
			// iscPlotPercentage.addSelectionListener( this );

			btnWebLabels = new Button( grpLine, SWT.CHECK );
			{
				btnWebLabels.setText( Messages.getString( "RadarSeriesAttributeComposite.Lbl.ShowWeb" ) ); //$NON-NLS-1$
				btnWebLabels.setSelection( series.isShowWebLabels( ) );
				btnWebLabels.addSelectionListener( this );
				GridData gd = new GridData( GridData.FILL_HORIZONTAL );
				gd.horizontalSpan = 2;
				btnWebLabels.setLayoutData( gd );
			}

			btnTranslucentBullseye = new Button( grpLine, SWT.CHECK );
			{
				btnTranslucentBullseye.setText( Messages.getString( "Radar.Composite.Label.bullsEye" ) ); //$NON-NLS-1$
				btnTranslucentBullseye.setSelection( series.isBackgroundOvalTransparent( ) );
				btnTranslucentBullseye.addSelectionListener( this );
				GridData gd = new GridData( GridData.FILL_HORIZONTAL );
				gd.horizontalSpan = 2;
				gd.verticalAlignment = SWT.TOP;
				btnTranslucentBullseye.setLayoutData( gd );
				btnTranslucentBullseye.setVisible( chart.getSubType( )
						.equals( RadarChart.BULLSEYE_SUBTYPE_LITERAL ) );
			}
		}

		grpLine2 = new Group( this, SWT.NONE );
		GridLayout glLine2 = new GridLayout( 2, false );
		glLine2.horizontalSpacing = 0;
		grpLine2.setLayout( glLine2 );
		grpLine2.setLayoutData( new GridData( GridData.FILL_BOTH ) );
		grpLine2.setText( Messages.getString( "RadarSeriesMarkerSheet.Label.Series" ) ); //$NON-NLS-1$

		liacLine = new LineAttributesComposite( grpLine2,
				SWT.NONE,
				context,
				series.getLineAttributes( ),
				true,
				true,
				true );
		GridData gdLIACLine = new GridData( );
		gdLIACLine.verticalSpan = 4;
		gdLIACLine.widthHint = 200;
		liacLine.setLayoutData( gdLIACLine );
		liacLine.addListener( this );

		Composite cmp = new Composite( grpLine2, SWT.NONE );
		cmp.setLayoutData( new GridData( GridData.FILL_BOTH ) );
		cmp.setLayout( new GridLayout( ) );

		btnPalette = new Button( cmp, SWT.CHECK );
		{
			btnPalette.setText( Messages.getString( "RadarSeriesAttributeComposite.Lbl.LinePalette" ) ); //$NON-NLS-1$
			btnPalette.setSelection( series.isPaletteLineColor( ) );
			btnPalette.addSelectionListener( this );
		}
		btnConnectEndPoints = new Button( cmp, SWT.CHECK );
		{
			btnConnectEndPoints.setText( Messages.getString( "RadarSeriesAttributeComposite.Lbl.ConnectPoints" ) ); //$NON-NLS-1$
			btnConnectEndPoints.setSelection( series.isConnectEndpoints( ) );
			btnConnectEndPoints.addSelectionListener( this );
		}
		btnFillPoly = new Button( cmp, SWT.CHECK );
		{
			btnFillPoly.setText( Messages.getString( "RadarSeriesAttributeComposite.Lbl.FillPoly" ) ); //$NON-NLS-1$
			btnFillPoly.setSelection( series.isFillPolys( ) );
			btnFillPoly.addSelectionListener( this );
			btnFillPoly.setEnabled( btnConnectEndPoints.getSelection( ) );
		}

		Group grpMarker = new Group( cmp, SWT.NONE );
		grpMarker.setText( Messages.getString( "RadarSeriesMarkerSheet.GroupLabel.Markers" ) ); //$NON-NLS-1$
		grpMarker.setLayout( new GridLayout( 2, false ) );

		// Layout for marker
		Label lblMarker = new Label( grpMarker, SWT.NONE );
		lblMarker.setText( Messages.getString( "RadarSeriesMarkerSheet.Label.Markers" ) ); //$NON-NLS-1$

		mec = new MarkerEditorComposite( grpMarker, series.getMarker( ) );

		enableLineSettings( series.getWebLineAttributes( ).isVisible( ) );
		enableLineSettings( series.getLineAttributes( ).isVisible( ) );
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
		if ( e.getSource( ).equals( btnPalette ) )
		{
			series.setPaletteLineColor( btnPalette.getSelection( ) );
		}
		else if ( e.getSource( ).equals( btnFillPoly ) )
		{
			series.setFillPolys( btnFillPoly.getSelection( ) );
		}
		else if ( e.getSource( ).equals( btnConnectEndPoints ) )
		{
			series.setConnectEndpoints( btnConnectEndPoints.getSelection( ) );
			btnFillPoly.setEnabled( btnConnectEndPoints.getSelection( ) );
		}
		else if ( e.getSource( ).equals( btnTranslucentBullseye ) )
		{
			series.setBackgroundOvalTransparent( btnTranslucentBullseye.getSelection( ) );
		}

		else if ( e.getSource( ).equals( mec ) )
		{
			series.setMarker( mec.getMarker( ) );
		}
		else if ( e.getSource( ).equals( iscScaleCnt ) )
		{
			series.setPlotSteps( BigInteger.valueOf( iscScaleCnt.getSelection( ) ) );
		}
		else if ( e.getSource( ).equals( btnWebLabels ) )
		{
			series.setShowWebLabels( btnWebLabels.getSelection( ) );
		}

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

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.swt.widgets.Listener#handleEvent(org.eclipse.swt.widgets.
	 * Event)
	 */
	public void handleEvent( Event event )
	{
		if ( event.widget.equals( liacLine ) )
		{
			if ( event.type == LineAttributesComposite.VISIBILITY_CHANGED_EVENT )
			{
				series.getLineAttributes( )
						.setVisible( ( (Boolean) event.data ).booleanValue( ) );
				enableLineSettings( series.getLineAttributes( ).isVisible( ) );
			}
			else if ( event.type == LineAttributesComposite.STYLE_CHANGED_EVENT )
			{
				series.getLineAttributes( ).setStyle( (LineStyle) event.data );
			}
			else if ( event.type == LineAttributesComposite.WIDTH_CHANGED_EVENT )
			{
				series.getLineAttributes( )
						.setThickness( ( (Integer) event.data ).intValue( ) );
			}
			else if ( event.type == LineAttributesComposite.COLOR_CHANGED_EVENT )
			{
				series.getLineAttributes( )
						.setColor( (ColorDefinition) event.data );
			}
		}
		else if ( event.widget.equals( wliacLine ) )
		{
			if ( event.type == LineAttributesComposite.VISIBILITY_CHANGED_EVENT )
			{
				series.getWebLineAttributes( )
						.setVisible( ( (Boolean) event.data ).booleanValue( ) );
				enableLineSettings( series.getWebLineAttributes( ).isVisible( ) );
			}
			else if ( event.type == LineAttributesComposite.STYLE_CHANGED_EVENT )
			{
				series.getWebLineAttributes( )
						.setStyle( (LineStyle) event.data );
			}
			else if ( event.type == LineAttributesComposite.WIDTH_CHANGED_EVENT )
			{
				series.getWebLineAttributes( )
						.setThickness( ( (Integer) event.data ).intValue( ) );
			}
			else if ( event.type == LineAttributesComposite.COLOR_CHANGED_EVENT )
			{
				series.getWebLineAttributes( )
						.setColor( (ColorDefinition) event.data );
			}
		}

	}

	private void enableLineSettings( boolean isEnabled )
	{

		if ( btnPalette != null )
		{
			btnPalette.setEnabled( isEnabled );
			btnConnectEndPoints.setEnabled( isEnabled );
		}

	}
}