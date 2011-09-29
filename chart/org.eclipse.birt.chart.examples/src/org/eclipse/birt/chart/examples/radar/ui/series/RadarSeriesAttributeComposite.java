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

import org.eclipse.birt.chart.examples.radar.i18n.Messages;
import org.eclipse.birt.chart.examples.radar.model.type.RadarSeries;
import org.eclipse.birt.chart.examples.radar.model.type.impl.RadarSeriesImpl;
import org.eclipse.birt.chart.examples.view.util.UIHelper;
import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.log.ILogger;
import org.eclipse.birt.chart.log.Logger;
import org.eclipse.birt.chart.model.attribute.ColorDefinition;
import org.eclipse.birt.chart.model.attribute.LineStyle;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.util.ChartElementUtil;
import org.eclipse.birt.chart.ui.plugin.ChartUIExtensionPlugin;
import org.eclipse.birt.chart.ui.swt.composites.LineAttributesComposite;
import org.eclipse.birt.chart.ui.swt.composites.MarkerEditorComposite;
import org.eclipse.birt.chart.ui.swt.wizard.ChartWizardContext;
import org.eclipse.birt.chart.ui.util.ChartHelpContextIds;
import org.eclipse.birt.chart.ui.util.ChartUIUtil;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;

/**
 * @author Actuate Corporation
 * 
 */
public class RadarSeriesAttributeComposite extends Composite implements
		Listener
{

	private MarkerEditorComposite mec = null;

	private RadarSeries series = null;

	private ChartWizardContext context;

	private LineAttributesComposite liacLine = null;
	private Label lblPalette;

	private Combo cmbPalette;

	private Label lblConnectEndPoints;

	private Combo cmbConnectEndPoints;

	private Label lblFillPoly;

	private Combo cmbFillPoly;

	private static ILogger logger = Logger.getLogger( "org.eclipse.birt.chart.examples/swt.series" ); //$NON-NLS-1$
	
	public static final String SUBTASK_YSERIES_RADAR = ChartHelpContextIds.PREFIX
			+ "FormatRadarChartSeries_ID"; //$NON-NLS-1$
	
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

		init( );
		placeComponents( );

		ChartUIUtil.bindHelp( parent, SUBTASK_YSERIES_RADAR );
	}

	private void init( )
	{
		this.setSize( getParent( ).getClientArea( ).width,
				getParent( ).getClientArea( ).height );
	}

	private void placeComponents( )
	{
		// Main content composite
		this.setLayout( new GridLayout( ) );

		// individual series
		Group grpLine2 = new Group( this, SWT.NONE );
		GridLayout glLine2 = new GridLayout( 2, false );
		glLine2.horizontalSpacing = 0;
		grpLine2.setLayout( glLine2 );
		grpLine2.setLayoutData( new GridData( GridData.FILL_BOTH ) );
		grpLine2.setText( Messages.getString( "RadarSeriesMarkerSheet.Label.Series" ) ); //$NON-NLS-1$

		int lineStyles = LineAttributesComposite.ENABLE_AUTO_COLOR
				| LineAttributesComposite.ENABLE_COLOR
				| LineAttributesComposite.ENABLE_STYLES
				| LineAttributesComposite.ENABLE_VISIBILITY
				| LineAttributesComposite.ENABLE_WIDTH;
		liacLine = new LineAttributesComposite( grpLine2,
				SWT.NONE,
				lineStyles,
				context,
				series.getLineAttributes( ) );
		GridData gdLIACLine = new GridData( );
		gdLIACLine.verticalSpan = 4;
		gdLIACLine.widthHint = 200;
		liacLine.setLayoutData( gdLIACLine );
		liacLine.addListener( this );

		Composite cmp = new Composite( grpLine2, SWT.NONE );
		cmp.setLayoutData( new GridData( GridData.FILL_BOTH ) );
		cmp.setLayout( new GridLayout(2, false) );

		lblPalette = new Label( cmp, SWT.NONE );
		lblPalette.setText( Messages.getString( "RadarSeriesAttributeComposite.Lbl.LinePalette" ) ); //$NON-NLS-1$
		
		cmbPalette = UIHelper.createTrueFalseItemsCombo( cmp );
		{
			cmbPalette.select( series.isSetPaletteLineColor( ) ? ( series.isPaletteLineColor( ) ? 1
					: 2 )
					: 0 );
			cmbPalette.addListener( SWT.Selection, this );
		}
		
		lblConnectEndPoints = new Label( cmp, SWT.NONE );
		lblConnectEndPoints.setText( Messages.getString( "RadarSeriesAttributeComposite.Lbl.ConnectPoints" ) ); //$NON-NLS-1$
		
		cmbConnectEndPoints = UIHelper.createTrueFalseItemsCombo( cmp );
		{
			
			cmbConnectEndPoints.select( series.isSetConnectEndpoints( ) ? ( series.isConnectEndpoints( ) ? 1
					: 2 )
					: 0 );
			cmbConnectEndPoints.addListener( SWT.Selection, this );
		}
		
		lblFillPoly = new Label( cmp, SWT.NONE );
		lblFillPoly.setText( Messages.getString( "RadarSeriesAttributeComposite.Lbl.FillPoly" ) ); //$NON-NLS-1$
		
		cmbFillPoly = UIHelper.createTrueFalseItemsCombo( cmp );
		{
			cmbFillPoly.select( series.isSetFillPolys( ) ? ( series.isFillPolys( ) ? 1
					: 2 )
					: 0 );
			cmbFillPoly.addListener( SWT.Selection, this );
			lblFillPoly.setEnabled( cmbConnectEndPoints.getSelectionIndex( ) == 1 );
			cmbFillPoly.setEnabled( cmbConnectEndPoints.getSelectionIndex( ) == 1 );
		}

		Group grpMarker = new Group( cmp, SWT.NONE );
		GridData gd = new GridData();
		gd.horizontalSpan=2;
		grpMarker.setLayoutData( gd );
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
	 * org.eclipse.swt.widgets.Listener#handleEvent(org.eclipse.swt.widgets.
	 * Event)
	 */
	public void handleEvent( Event event )
	{
		boolean isUnset = ( event.detail == ChartElementUtil.PROPERTY_UNSET );
		if ( event.widget.equals( liacLine ) )
		{
			if ( event.type == LineAttributesComposite.VISIBILITY_CHANGED_EVENT )
			{
				ChartElementUtil.setEObjectAttribute( series.getLineAttributes( ),
						"visible",//$NON-NLS-1$
						( (Boolean) event.data ).booleanValue( ),
						isUnset );
				enableLineSettings( series.getLineAttributes( ).isSetVisible( )
						&& series.getLineAttributes( ).isVisible( ) );
			}
			else if ( event.type == LineAttributesComposite.STYLE_CHANGED_EVENT )
			{
				ChartElementUtil.setEObjectAttribute( series.getLineAttributes( ),
						"style",//$NON-NLS-1$
						(LineStyle) event.data,
						isUnset );
			}
			else if ( event.type == LineAttributesComposite.WIDTH_CHANGED_EVENT )
			{
				ChartElementUtil.setEObjectAttribute( series.getLineAttributes( ),
						"thickness",//$NON-NLS-1$
						( (Integer) event.data ).intValue( ),
						isUnset );
			}
			else if ( event.type == LineAttributesComposite.COLOR_CHANGED_EVENT )
			{
				series.getLineAttributes( )
						.setColor( (ColorDefinition) event.data );
			}
		}
		else if ( event.widget.equals( cmbPalette ) )
		{
			ChartElementUtil.setEObjectAttribute( series,
					"paletteLineColor",//$NON-NLS-1$
					cmbPalette.getSelectionIndex( ) == 1,
					cmbPalette.getSelectionIndex( ) == 0 );
		}
		else if ( event.widget.equals( cmbFillPoly ) )
		{
			ChartElementUtil.setEObjectAttribute( series,
					"fillPolys",//$NON-NLS-1$
					cmbFillPoly.getSelectionIndex( ) == 1,
					cmbFillPoly.getSelectionIndex( ) == 0 );
		}
		else if ( event.widget.equals( cmbConnectEndPoints ) )
		{
			ChartElementUtil.setEObjectAttribute( series,
					"connectEndpoints",//$NON-NLS-1$
					cmbConnectEndPoints.getSelectionIndex( ) == 1,
					cmbConnectEndPoints.getSelectionIndex( ) == 0 );
			lblFillPoly.setEnabled( cmbConnectEndPoints.getSelectionIndex( ) == 1 );
			cmbFillPoly.setEnabled( cmbConnectEndPoints.getSelectionIndex( ) == 1 );
		}
		else if ( event.widget.equals( mec ) )
		{
			series.setMarker( mec.getMarker( ) );
		}
	}

	private void enableLineSettings( boolean isEnabled )
	{
		if ( cmbPalette != null )
		{
			cmbPalette.setEnabled( isEnabled );
			cmbConnectEndPoints.setEnabled( isEnabled );
		}
	}
}