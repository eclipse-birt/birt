/***********************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
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
import org.eclipse.birt.chart.model.attribute.Orientation;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.type.BubbleSeries;
import org.eclipse.birt.chart.model.type.impl.BubbleSeriesImpl;
import org.eclipse.birt.chart.ui.extension.i18n.Messages;
import org.eclipse.birt.chart.ui.plugin.ChartUIExtensionPlugin;
import org.eclipse.birt.chart.ui.swt.composites.FillChooserComposite;
import org.eclipse.birt.chart.ui.swt.composites.LineAttributesComposite;
import org.eclipse.birt.chart.ui.swt.wizard.ChartWizardContext;
import org.eclipse.birt.chart.ui.util.ChartHelpContextIds;
import org.eclipse.birt.chart.ui.util.ChartUIUtil;
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

public class BubbleSeriesAttributeComposite extends Composite implements
		SelectionListener,
		Listener
{

	private transient Button btnCurve = null;

	private transient Button btnPalette = null;

	private transient FillChooserComposite fccShadow = null;

	private transient Group grpLine = null;

	private transient LineAttributesComposite liacLine = null;

	private transient Group grpAccLine = null;

	private transient LineAttributesComposite liacAccLine = null;
	
	private transient Label lblOrientation = null;

	private transient Combo cmbOrientation;

	private transient Series series = null;
	
	private transient Label lblShadow = null;

	ChartWizardContext context;

	private static ILogger logger = Logger.getLogger( "org.eclipse.birt.chart.ui.extension/swt.series" ); //$NON-NLS-1$

	/**
	 * @param parent
	 * @param style
	 */
	public BubbleSeriesAttributeComposite( Composite parent, int style,
			ChartWizardContext context, Series series )
	{
		super( parent, style );
		if ( !( series instanceof BubbleSeriesImpl ) )
		{
			try
			{
				throw new ChartException( ChartUIExtensionPlugin.ID,
						ChartException.VALIDATION,
						"BubbleSeriesAttributeComposite.Exception.IllegalArgument", new Object[]{series.getClass( ).getName( )}, Messages.getResourceBundle( ) ); //$NON-NLS-1$
			}
			catch ( ChartException e )
			{
				logger.log( e );
				e.printStackTrace( );
			}
		}
		this.series = series;
		this.context = context;
		init( );
		placeComponents( );
		ChartUIUtil.bindHelp( parent,
				ChartHelpContextIds.SUBTASK_YSERIES_BUBBLE );
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

		grpAccLine = new Group( this, SWT.NONE );
		GridData gdGRPAccLine = new GridData( GridData.FILL_BOTH );
		gdGRPAccLine.horizontalSpan = 1;
		GridLayout glGRPAccline = new GridLayout( 2, false );
		grpAccLine.setLayout( glGRPAccline );
		grpAccLine.setLayoutData( gdGRPAccLine );
		grpAccLine.setText( Messages.getString( "BubbleSeriesAttributeComposite.Lbl.AccLine" ) ); //$NON-NLS-1$

		liacAccLine = new LineAttributesComposite( grpAccLine,
				SWT.NONE,
				context,
				( (BubbleSeries) series ).getAccLineAttributes( ),
				true,
				true,
				true );
		GridData gdLIACAccLine = new GridData( GridData.FILL_BOTH );
		gdLIACAccLine.horizontalSpan = 2;
		liacAccLine.setLayoutData( gdLIACAccLine );
		liacAccLine.addListener( this );
		
		lblOrientation = new Label( grpAccLine, SWT.NONE );
		lblOrientation.setText( Messages.getString( "BubbleSeriesAttributeComposite.Lbl.Orientation" ) ); //$NON-NLS-1$

		cmbOrientation = new Combo( grpAccLine, SWT.DROP_DOWN | SWT.READ_ONLY );
		GridData gdCMBOrientation = new GridData( GridData.FILL_HORIZONTAL );
		cmbOrientation.setLayoutData( gdCMBOrientation );
		cmbOrientation.addSelectionListener( this );

		grpLine = new Group( this, SWT.NONE );
		GridData gdGRPLine = new GridData( GridData.FILL_BOTH );
		gdGRPLine.horizontalSpan = 1;
		grpLine.setLayout( new FillLayout( ) );
		grpLine.setLayoutData( gdGRPLine );
		grpLine.setText( Messages.getString( "BubbleSeriesAttributeComposite.Lbl.Line" ) ); //$NON-NLS-1$

		liacLine = new LineAttributesComposite( grpLine,
				SWT.NONE,
				context,
				( (BubbleSeries) series ).getLineAttributes( ),
				true,
				true,
				true );
		liacLine.addListener( this );

		Composite cmp = new Composite( this, SWT.NONE );
		cmp.setLayout( new GridLayout( ) );
		cmp.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );

		Composite cmpShadow = new Composite( cmp, SWT.NONE );
		cmpShadow.setLayout( new GridLayout( 2, false ) );
		cmpShadow.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );

		lblShadow = new Label( cmpShadow, SWT.NONE );
		GridData gdLBLShadow = new GridData( );
		lblShadow.setLayoutData( gdLBLShadow );
		lblShadow.setText( Messages.getString( "BubbleSeriesAttributeComposite.Lbl.ShadowColor" ) ); //$NON-NLS-1$

		fccShadow = new FillChooserComposite( cmpShadow,
				SWT.NONE,
				context,
				( (BubbleSeries) series ).getShadowColor( ),
				false,
				false );
		GridData gdFCCShadow = new GridData( GridData.FILL_HORIZONTAL );
		gdFCCShadow.widthHint = 100;
		fccShadow.setLayoutData( gdFCCShadow );
		fccShadow.addListener( this );

		btnPalette = new Button( cmp, SWT.CHECK );
		{
			btnPalette.setText( Messages.getString( "BubbleSeriesAttributeComposite.Lbl.LinePalette" ) ); //$NON-NLS-1$
			btnPalette.setSelection( ( (BubbleSeries) series ).isPaletteLineColor( ) );
			GridData gdBTNPalette = new GridData( GridData.FILL_HORIZONTAL );
			gdBTNPalette.horizontalSpan = 2;
			btnPalette.setLayoutData( gdBTNPalette );
			btnPalette.addSelectionListener( this );
		}

		btnCurve = new Button( cmp, SWT.CHECK );
		{
			btnCurve.setText( Messages.getString( "BubbleSeriesAttributeComposite.Lbl.ShowLinesAsCurves" ) ); //$NON-NLS-1$
			btnCurve.setSelection( ( (BubbleSeries) series ).isCurve( ) );
			GridData gdBTNCurve = new GridData( GridData.FILL_HORIZONTAL );
			gdBTNCurve.horizontalSpan = 2;
			btnPalette.setLayoutData( gdBTNCurve );
			btnCurve.addSelectionListener( this );
		}

		enableLineSettings( ( (BubbleSeries) series ).getLineAttributes( )
				.isVisible( ) );
		
		enableAccLineSettings( ( (BubbleSeries) series ).getAccLineAttributes( )
				.isVisible( ) );

		populateLists( );
	}

	private void populateLists( )
	{
		NameSet ns = LiteralHelper.orientationSet;
		cmbOrientation.setItems( ns.getDisplayNames( ) );
		cmbOrientation.select( ns.getSafeNameIndex( ( (BubbleSeries) series ).getAccOrientation( )
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
			( (BubbleSeries) series ).setCurve( btnCurve.getSelection( ) );
		}
		else if ( e.getSource( ).equals( btnPalette ) )
		{
			( (BubbleSeries) series ).setPaletteLineColor( btnPalette.getSelection( ) );
		}
		else if ( e.getSource( ).equals( cmbOrientation ) )
		{
			( (BubbleSeries) series ).setAccOrientation( Orientation.getByName( LiteralHelper.orientationSet.getNameByDisplayName( cmbOrientation.getText( ) ) ) );
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.widgets.Listener#handleEvent(org.eclipse.swt.widgets.Event)
	 */
	public void handleEvent( Event event )
	{
		if ( event.widget.equals( liacLine ) )
		{
			if ( event.type == LineAttributesComposite.VISIBILITY_CHANGED_EVENT )
			{
				( (BubbleSeries) series ).getLineAttributes( )
						.setVisible( ( (Boolean) event.data ).booleanValue( ) );
				enableLineSettings( ( (BubbleSeries) series ).getLineAttributes( )
						.isVisible( ) );
			}
			else if ( event.type == LineAttributesComposite.STYLE_CHANGED_EVENT )
			{
				( (BubbleSeries) series ).getLineAttributes( )
						.setStyle( (LineStyle) event.data );
			}
			else if ( event.type == LineAttributesComposite.WIDTH_CHANGED_EVENT )
			{
				( (BubbleSeries) series ).getLineAttributes( )
						.setThickness( ( (Integer) event.data ).intValue( ) );
			}
			else if ( event.type == LineAttributesComposite.COLOR_CHANGED_EVENT )
			{
				( (BubbleSeries) series ).getLineAttributes( )
						.setColor( (ColorDefinition) event.data );
			}
		}
		else if ( event.widget.equals( liacAccLine ) )
		{
			if ( event.type == LineAttributesComposite.VISIBILITY_CHANGED_EVENT )
			{
				( (BubbleSeries) series ).getAccLineAttributes( )
						.setVisible( ( (Boolean) event.data ).booleanValue( ) );
				enableAccLineSettings( ( (BubbleSeries) series ).getAccLineAttributes( )
						.isVisible( ) );
			}
			else if ( event.type == LineAttributesComposite.STYLE_CHANGED_EVENT )
			{
				( (BubbleSeries) series ).getAccLineAttributes( )
						.setStyle( (LineStyle) event.data );
			}
			else if ( event.type == LineAttributesComposite.WIDTH_CHANGED_EVENT )
			{
				( (BubbleSeries) series ).getAccLineAttributes( )
						.setThickness( ( (Integer) event.data ).intValue( ) );
			}
			else if ( event.type == LineAttributesComposite.COLOR_CHANGED_EVENT )
			{
				( (BubbleSeries) series ).getAccLineAttributes( )
						.setColor( (ColorDefinition) event.data );
			}
		}
		else if ( event.widget.equals( fccShadow ) )
		{
			( (BubbleSeries) series ).setShadowColor( (ColorDefinition) event.data );
		}
	}

	private void enableLineSettings( boolean isEnabled )
	{
		if ( lblShadow != null )
		{
			lblShadow.setEnabled( isEnabled );
		}
		if ( fccShadow != null )
		{
			fccShadow.setEnabled( isEnabled );
		}
		if ( btnPalette != null )
		{
			btnPalette.setEnabled( isEnabled );
		}
		btnCurve.setEnabled( isEnabled );
	}
	
	private void enableAccLineSettings( boolean isEnabled )
	{
		if ( cmbOrientation != null )
		{
			cmbOrientation.setEnabled( isEnabled );
		}
		if ( lblOrientation != null )
		{
			lblOrientation.setEnabled( isEnabled );
		}
		
	}

}
