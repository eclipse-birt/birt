/***********************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.chart.ui.swt.series;

import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.log.ILogger;
import org.eclipse.birt.chart.log.Logger;
import org.eclipse.birt.chart.model.attribute.ColorDefinition;
import org.eclipse.birt.chart.model.attribute.LineStyle;
import org.eclipse.birt.chart.model.attribute.Marker;
import org.eclipse.birt.chart.model.attribute.MarkerType;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.type.AreaSeries;
import org.eclipse.birt.chart.model.type.LineSeries;
import org.eclipse.birt.chart.model.type.ScatterSeries;
import org.eclipse.birt.chart.model.type.impl.LineSeriesImpl;
import org.eclipse.birt.chart.ui.extension.i18n.Messages;
import org.eclipse.birt.chart.ui.plugin.ChartUIExtensionPlugin;
import org.eclipse.birt.chart.ui.swt.composites.FillChooserComposite;
import org.eclipse.birt.chart.ui.swt.composites.IntegerSpinControl;
import org.eclipse.birt.chart.ui.swt.composites.LineAttributesComposite;
import org.eclipse.birt.chart.ui.swt.composites.MarkerIconDialog;
import org.eclipse.birt.chart.util.LiteralHelper;
import org.eclipse.birt.chart.util.NameSet;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
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
public class LineSeriesAttributeComposite extends Composite implements
		SelectionListener,
		Listener
{

	private transient Button btnCurve = null;

	private transient Button btnMissingValue = null;

	private transient Button btnPalette = null;

	private transient FillChooserComposite fccShadow = null;

	private transient Group grpMarker = null;

	private transient Button btnMarkerVisible = null;

	private transient Combo cmbMarkerTypes = null;

	private transient IntegerSpinControl iscMarkerSize = null;

	private transient Group grpLine = null;

	private transient LineAttributesComposite liacLine = null;

	private transient Series series = null;

	private static ILogger logger = Logger.getLogger( "org.eclipse.birt.chart.ui.extension/swt.series" ); //$NON-NLS-1$

	/**
	 * @param parent
	 * @param style
	 */
	public LineSeriesAttributeComposite( Composite parent, int style,
			Series series )
	{
		super( parent, style );
		if ( !( series instanceof LineSeriesImpl ) )
		{
			try
			{
				throw new ChartException( ChartUIExtensionPlugin.ID,
						ChartException.VALIDATION,
						"LineSeriesAttributeComposite.Exception.IllegalArgument", new Object[]{series.getClass( ).getName( )}, Messages.getResourceBundle( ) ); //$NON-NLS-1$
			}
			catch ( ChartException e )
			{
				logger.log( e );
				e.printStackTrace( );
			}
		}
		this.series = series;
		init( );
		placeComponents( );
	}

	private void init( )
	{
		this.setSize( getParent( ).getClientArea( ).width,
				getParent( ).getClientArea( ).height );
	}

	private void placeComponents( )
	{
		// Layout for content composite
		GridLayout glContent = new GridLayout( 4, false );
		glContent.marginHeight = 2;
		glContent.marginWidth = 2;

		// Main content composite
		this.setLayout( glContent );

		if ( isShadowNeeded( ) )
		{
			Label lblShadow = new Label( this, SWT.NONE );
			GridData gdLBLShadow = new GridData( );
			lblShadow.setLayoutData( gdLBLShadow );
			lblShadow.setText( Messages.getString( "LineSeriesAttributeComposite.Lbl.ShadowColor" ) ); //$NON-NLS-1$

			fccShadow = new FillChooserComposite( this,
					SWT.NONE,
					series.eAdapters( ),
					( (LineSeries) series ).getShadowColor( ),
					false,
					false );
			GridData gdFCCShadow = new GridData( GridData.FILL_HORIZONTAL );
			gdFCCShadow.widthHint = 180;
			fccShadow.setLayoutData( gdFCCShadow );
			fccShadow.addListener( this );
		}

		btnPalette = new Button( this, SWT.CHECK );
		{
			btnPalette.setText( Messages.getString( "LineSeriesAttributeComposite.Lbl.LinePalette" ) ); //$NON-NLS-1$
			btnPalette.setSelection( ( (LineSeries) series ).isPaletteLineColor( ) );
			btnPalette.addSelectionListener( this );
		}

		btnCurve = new Button( this, SWT.CHECK );
		{
			btnCurve.setText( Messages.getString( "LineSeriesAttributeComposite.Lbl.ShowLinesAsCurves" ) ); //$NON-NLS-1$
			btnCurve.setSelection( ( (LineSeries) series ).isCurve( ) );
			btnCurve.addSelectionListener( this );
		}

		if ( !( series instanceof AreaSeries || series instanceof ScatterSeries ) )
		{
			btnMissingValue = new Button( this, SWT.CHECK );
			{
				btnMissingValue.setText( Messages.getString( "LineSeriesAttributeComposite.Lbl.ConnectMissingValue" ) ); //$NON-NLS-1$
				GridData gdBTNMissingValue = new GridData( );
				gdBTNMissingValue.horizontalSpan = 4;
				btnMissingValue.setLayoutData( gdBTNMissingValue );
				btnMissingValue.setSelection( ( (LineSeries) series ).isConnectMissingValue( ) );
				btnMissingValue.addSelectionListener( this );
			}
		}

		if ( !isShadowNeeded( ) )
		{
			GridData gd = new GridData( );
			gd.horizontalSpan = 2;
			gd.horizontalIndent = 5;
			btnPalette.setLayoutData( gd );

			gd = new GridData( );
			gd.horizontalSpan = 2;
			gd.horizontalIndent = 5;
			btnCurve.setLayoutData( gd );
		}

		// Layout for the Marker group
		GridLayout glMarker = new GridLayout( );
		glMarker.numColumns = 2;
		glMarker.marginHeight = 4;
		glMarker.marginWidth = 4;
		glMarker.verticalSpacing = 4;
		glMarker.horizontalSpacing = 4;

		grpMarker = new Group( this, SWT.NONE );
		GridData gdGRPMarker = new GridData( GridData.FILL_BOTH );
		gdGRPMarker.horizontalSpan = 2;
		grpMarker.setLayoutData( gdGRPMarker );
		grpMarker.setLayout( glMarker );
		grpMarker.setText( Messages.getString( "LineSeriesAttributeComposite.Lbl.Marker" ) ); //$NON-NLS-1$

		btnMarkerVisible = new Button( grpMarker, SWT.CHECK );
		GridData gdBTNVisible = new GridData( );
		gdBTNVisible.horizontalSpan = 2;
		btnMarkerVisible.setLayoutData( gdBTNVisible );
		btnMarkerVisible.setText( Messages.getString( "LineSeriesAttributeComposite.Lbl.IsVisible" ) ); //$NON-NLS-1$
		btnMarkerVisible.addSelectionListener( this );
		btnMarkerVisible.setSelection( ( (LineSeries) series ).getMarker( )
				.isVisible( ) );

		Label lblType = new Label( grpMarker, SWT.NONE );
		GridData gdLBLType = new GridData( );
		lblType.setLayoutData( gdLBLType );
		lblType.setText( Messages.getString( "LineSeriesAttributeComposite.Lbl.Type" ) ); //$NON-NLS-1$

		cmbMarkerTypes = new Combo( grpMarker, SWT.DROP_DOWN | SWT.READ_ONLY );
		GridData gdCMBMarkerTypes = new GridData( GridData.FILL_HORIZONTAL );
		cmbMarkerTypes.setLayoutData( gdCMBMarkerTypes );
		cmbMarkerTypes.addSelectionListener( this );
		cmbMarkerTypes.setEnabled( btnMarkerVisible.getSelection( ) );

		Label lblSize = new Label( grpMarker, SWT.NONE );
		GridData gdLBLSize = new GridData( );
		lblSize.setLayoutData( gdLBLSize );
		lblSize.setText( Messages.getString( "LineSeriesAttributeComposite.Lbl.Size" ) ); //$NON-NLS-1$

		iscMarkerSize = new IntegerSpinControl( grpMarker,
				SWT.NONE,
				( (LineSeries) series ).getMarker( ).getSize( ) );
		GridData gdISCMarkerSize = new GridData( GridData.FILL_HORIZONTAL );
		iscMarkerSize.setLayoutData( gdISCMarkerSize );
		iscMarkerSize.setMinimum( 0 );
		iscMarkerSize.setMaximum( 100 );
		iscMarkerSize.addListener( this );
		iscMarkerSize.setEnabled( btnMarkerVisible.getSelection( ) );

		grpLine = new Group( this, SWT.NONE );
		GridData gdGRPLine = new GridData( GridData.FILL_BOTH );
		gdGRPLine.horizontalSpan = 2;
		grpLine.setLayout( new FillLayout( ) );
		grpLine.setLayoutData( gdGRPLine );
		grpLine.setText( Messages.getString( "LineSeriesAttributeComposite.Lbl.Line" ) ); //$NON-NLS-1$

		liacLine = new LineAttributesComposite( grpLine,
				SWT.NONE,
				( (LineSeries) series ).getLineAttributes( ),
				true,
				true,
				true );
		liacLine.addListener( this );

		enableLineSettings( ( (LineSeries) series ).getLineAttributes( )
				.isVisible( ) );

		populateLists( );
	}

	private void populateLists( )
	{
		NameSet ns = LiteralHelper.markerTypeSet;
		cmbMarkerTypes.setItems( ns.getDisplayNames( ) );
		cmbMarkerTypes.select( ns.getSafeNameIndex( ( (LineSeries) series ).getMarker( )
				.getType( )
				.getName( ) ) );
	}

	public Point getPreferredSize( )
	{
		return new Point( 400, 200 );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.events.SelectionListener#widgetSelected(org.eclipse.swt.events.SelectionEvent)
	 */
	public void widgetSelected( SelectionEvent e )
	{
		if ( e.getSource( ).equals( btnCurve ) )
		{
			( (LineSeries) series ).setCurve( btnCurve.getSelection( ) );
		}
		else if ( e.getSource( ).equals( btnPalette ) )
		{
			( (LineSeries) series ).setPaletteLineColor( btnPalette.getSelection( ) );
		}
		else if ( e.getSource( ).equals( btnMissingValue ) )
		{
			( (LineSeries) series ).setConnectMissingValue( btnMissingValue.getSelection( ) );
		}
		else if ( e.getSource( ).equals( btnMarkerVisible ) )
		{
			( (LineSeries) series ).getMarker( )
					.setVisible( btnMarkerVisible.getSelection( ) );
			cmbMarkerTypes.setEnabled( btnMarkerVisible.getSelection( ) );
			iscMarkerSize.setEnabled( btnMarkerVisible.getSelection( ) );
		}
		else if ( e.getSource( ).equals( cmbMarkerTypes ) )
		{
			if ( MarkerType.getByName( getSelectedMarkerName( ) ) == MarkerType.ICON_LITERAL )
			{
				MarkerIconDialog iconDialog = new MarkerIconDialog( this.getShell( ),
						getSeriesMarker( ).getIconPalette( ) );

				if ( iconDialog.applyMarkerIcon( ) )
				{
					if ( iconDialog.getIconPalette( ).eAdapters( ).isEmpty( ) )
					{
						// Add adapters to new EObject
						iconDialog.getIconPalette( )
								.eAdapters( )
								.addAll( getSeriesMarker( ).eAdapters( ) );
					}
					getSeriesMarker( ).setIconPalette( iconDialog.getIconPalette( ) );
				}
				else
				{
					cmbMarkerTypes.setText( LiteralHelper.markerTypeSet.getDisplayNameByName( getSeriesMarker( ).getType( )
							.getName( ) ) );
				}
			}

			getSeriesMarker( ).setType( MarkerType.getByName( getSelectedMarkerName( ) ) );
		}
	}

	private Marker getSeriesMarker( )
	{
		return ( (LineSeries) series ).getMarker( );
	}

	private String getSelectedMarkerName( )
	{
		return LiteralHelper.markerTypeSet.getNameByDisplayName( cmbMarkerTypes.getText( ) );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.events.SelectionListener#widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent)
	 */
	public void widgetDefaultSelected( SelectionEvent e )
	{
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.widgets.Listener#handleEvent(org.eclipse.swt.widgets.Event)
	 */
	public void handleEvent( Event event )
	{
		if ( event.widget.equals( iscMarkerSize ) )
		{
			( (LineSeries) series ).getMarker( )
					.setSize( ( (Integer) event.data ).intValue( ) );
		}
		else if ( event.widget.equals( liacLine ) )
		{
			if ( event.type == LineAttributesComposite.VISIBILITY_CHANGED_EVENT )
			{
				( (LineSeries) series ).getLineAttributes( )
						.setVisible( ( (Boolean) event.data ).booleanValue( ) );
				enableLineSettings( ( (LineSeries) series ).getLineAttributes( )
						.isVisible( ) );
			}
			else if ( event.type == LineAttributesComposite.STYLE_CHANGED_EVENT )
			{
				( (LineSeries) series ).getLineAttributes( )
						.setStyle( (LineStyle) event.data );
			}
			else if ( event.type == LineAttributesComposite.WIDTH_CHANGED_EVENT )
			{
				( (LineSeries) series ).getLineAttributes( )
						.setThickness( ( (Integer) event.data ).intValue( ) );
			}
			else if ( event.type == LineAttributesComposite.COLOR_CHANGED_EVENT )
			{
				( (LineSeries) series ).getLineAttributes( )
						.setColor( (ColorDefinition) event.data );
			}
		}
		else if ( event.widget.equals( fccShadow ) )
		{
			( (LineSeries) series ).setShadowColor( (ColorDefinition) event.data );
		}
	}

	private boolean isShadowNeeded( )
	{
		return !( series instanceof AreaSeries );
	}

	private void enableLineSettings( boolean isEnabled )
	{
		if ( fccShadow != null )
		{
			fccShadow.setEnabled( isEnabled );
		}
		if ( btnPalette != null )
		{
			btnPalette.setEnabled( isEnabled );
		}
		if ( btnMissingValue != null )
		{
			btnMissingValue.setEnabled( isEnabled );
		}
		btnCurve.setEnabled( isEnabled );
	}

}