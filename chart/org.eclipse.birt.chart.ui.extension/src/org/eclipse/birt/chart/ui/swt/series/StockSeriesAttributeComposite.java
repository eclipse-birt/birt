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
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.type.StockSeries;
import org.eclipse.birt.chart.ui.extension.i18n.Messages;
import org.eclipse.birt.chart.ui.plugin.ChartUIExtensionPlugin;
import org.eclipse.birt.chart.ui.swt.composites.LineAttributesComposite;
import org.eclipse.birt.chart.ui.swt.wizard.ChartWizardContext;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Spinner;

/**
 * @author Actuate Corporation
 * 
 */
public class StockSeriesAttributeComposite extends Composite implements
		Listener,
		SelectionListener
{

	// FillChooserComposite fccCandle = null;

	private LineAttributesComposite liacStock = null;

	private Spinner iscStick = null;

	private StockSeries series = null;

	private transient ChartWizardContext context;

	private static ILogger logger = Logger.getLogger( "org.eclipse.birt.chart.ui.extension/swt.series" ); //$NON-NLS-1$

	/**
	 * @param parent
	 * @param style
	 */
	public StockSeriesAttributeComposite( Composite parent, int style,
			ChartWizardContext context, Series series )
	{
		super( parent, style );
		if ( !( series instanceof StockSeries ) )
		{
			try
			{
				throw new ChartException( ChartUIExtensionPlugin.ID,
						ChartException.VALIDATION,
						"StockSeriesAttributeComposite.Exception.IllegalArgument", new Object[]{series.getClass( ).getName( )}, Messages.getResourceBundle( ) ); //$NON-NLS-1$
			}
			catch ( ChartException e )
			{
				logger.log( e );
				e.printStackTrace( );
			}
		}
		this.series = (StockSeries) series;
		this.context = context;
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
		GridLayout glContent = new GridLayout( );
		glContent.numColumns = series.isShowAsBarStick( ) ? 3 : 1;

		// Main content composite
		this.setLayout( glContent );

		// // Candle Fill composite
		// Label lblRiserOutline = new Label( this, SWT.NONE );
		// GridData gdLBLRiserOutline = new GridData( );
		// lblRiserOutline.setLayoutData( gdLBLRiserOutline );
		// lblRiserOutline.setText( Messages.getString(
		// "StockSeriesAttributeComposite.Lbl.CandleFill" ) ); //$NON-NLS-1$
		//		
		// this.fccCandle = new FillChooserComposite( this,
		// SWT.NONE,
		// series.getFill( ),
		// true,
		// true );
		// GridData gdFCCRiserOutline = new GridData( GridData.FILL_HORIZONTAL
		// );
		// fccCandle.setLayoutData( gdFCCRiserOutline );
		// fccCandle.addListener( this );

		// Line Attributes composite
		liacStock = new LineAttributesComposite( this,
				SWT.NONE,
				context,
				series.getLineAttributes( ),
				true,
				true,
				false );
		GridData gdLIACStock = new GridData( GridData.FILL_HORIZONTAL );
		gdLIACStock.verticalSpan = 3;
		liacStock.setLayoutData( gdLIACStock );
		liacStock.addListener( this );

		if ( series.isShowAsBarStick( ) )
		{
			new Label( this, SWT.NONE ).setText( Messages.getString( "StockSeriesAttributeComposite.Lbl.StickLength" ) ); //$NON-NLS-1$

			iscStick = new Spinner( this, SWT.BORDER );
			iscStick.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
			iscStick.setMinimum( 0 );
			iscStick.setMaximum( Integer.MAX_VALUE );
			iscStick.setSelection( series.getStickLength( ) );
			iscStick.addSelectionListener( this );
		}
	}

	public Point getPreferredSize( )
	{
		return new Point( 400, 200 );
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
	 * @see org.eclipse.swt.events.SelectionListener#widgetSelected(org.eclipse.swt.events.SelectionEvent)
	 */
	public void widgetSelected( SelectionEvent e )
	{
		if ( e.widget.equals( iscStick ) )
		{
			series.setStickLength( iscStick.getSelection( ) );
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.widgets.Listener#handleEvent(org.eclipse.swt.widgets.Event)
	 */
	public void handleEvent( Event event )
	{
		// if ( event.widget.equals( fccCandle ) )
		// {
		// series.setFill( (Fill) event.data );
		// }
		if ( event.widget.equals( liacStock ) )
		{
			if ( event.type == LineAttributesComposite.VISIBILITY_CHANGED_EVENT )
			{
				series.getLineAttributes( )
						.setVisible( ( (Boolean) event.data ).booleanValue( ) );
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
	}

}