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
import org.eclipse.birt.chart.model.type.DifferenceSeries;
import org.eclipse.birt.chart.model.type.impl.LineSeriesImpl;
import org.eclipse.birt.chart.ui.extension.i18n.Messages;
import org.eclipse.birt.chart.ui.plugin.ChartUIExtensionPlugin;
import org.eclipse.birt.chart.ui.swt.composites.LineAttributesComposite;
import org.eclipse.birt.chart.ui.swt.wizard.ChartWizardContext;
import org.eclipse.birt.chart.ui.util.ChartHelpContextIds;
import org.eclipse.birt.chart.ui.util.ChartUIUtil;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Listener;


public class DifferenceSeriesAttributeComposite extends Composite
		implements
			SelectionListener,
			Listener
{

	private transient Button btnCurve = null;

	private transient Group grpLine1 = null;

	private transient LineAttributesComposite liacLine1 = null;
	
	private transient Group grpLine2 = null;

	private transient LineAttributesComposite liacLine2 = null;

	private transient Series series = null;

	private transient ChartWizardContext context;

	private static ILogger logger = Logger.getLogger( "org.eclipse.birt.chart.ui.extension/swt.series" ); //$NON-NLS-1$

	/**
	 * @param parent
	 * @param style
	 */
	public DifferenceSeriesAttributeComposite( Composite parent, int style,
			ChartWizardContext context, Series series )
	{
		super( parent, style );
		if ( !( series instanceof LineSeriesImpl ) )
		{
			try
			{
				throw new ChartException( ChartUIExtensionPlugin.ID,
						ChartException.VALIDATION,
						"DifferenceSeriesAttributeComposite.Exception.IllegalArgument", new Object[]{series.getClass( ).getName( )}, Messages.getResourceBundle( ) ); //$NON-NLS-1$
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
		
		ChartUIUtil.bindHelp( parent, getHelpId( series ) );
	}

	private String getHelpId( Series series )
	{
		String helpId = ChartHelpContextIds.SUBTASK_YSERIES_DIFFERENCE;
		return helpId;
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

		grpLine1 = new Group( this, SWT.NONE );
		{
			GridData gdGRPLine = new GridData( GridData.FILL_BOTH );
			gdGRPLine.verticalSpan = 5;
			grpLine1.setLayout( new FillLayout( ) );
			grpLine1.setLayoutData( gdGRPLine );
			grpLine1.setText( Messages.getString( "DifferenceSeriesAttributeComposite.Lbl.PositiveLine" ) ); //$NON-NLS-1$
		}

		liacLine1 = new LineAttributesComposite( grpLine1,
				SWT.NONE,
				context,
				( (DifferenceSeries) series ).getLineAttributes( ),
				true,
				true,
				true );
		liacLine1.addListener( this );
		
		grpLine2 = new Group( this, SWT.NONE );
		{
			GridData gdGRPLine = new GridData( GridData.FILL_BOTH );
			gdGRPLine.verticalSpan = 5;
			grpLine2.setLayout( new FillLayout( ) );
			grpLine2.setLayoutData( gdGRPLine );
			grpLine2.setText( Messages.getString( "DifferenceSeriesAttributeComposite.Lbl.NegativeLine" ) ); //$NON-NLS-1$
		}

		liacLine2 = new LineAttributesComposite( grpLine2,
				SWT.NONE,
				context,
				( (DifferenceSeries) series ).getNegativeLineAttributes( ),
				true,
				true,
				true );
		liacLine2.addListener( this );

		btnCurve = new Button( this, SWT.CHECK );
		{
			GridData gd = new GridData( );
			gd.horizontalSpan = 2;
			btnCurve.setLayoutData( gd );
			btnCurve.setText( Messages.getString( "DifferenceSeriesAttributeComposite.Lbl.ShowLinesAsCurves" ) ); //$NON-NLS-1$
			btnCurve.setSelection( ( (DifferenceSeries) series ).isCurve( ) );
			btnCurve.addSelectionListener( this );
		}

		enableLineSettings( ( (DifferenceSeries) series ).getLineAttributes( )
				.isVisible( )
				|| ( (DifferenceSeries) series ).getNegativeLineAttributes( )
						.isVisible( ) );
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
			( (DifferenceSeries) series ).setCurve( btnCurve.getSelection( ) );
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
		if ( event.widget.equals( liacLine1 ) )
		{
			if ( event.type == LineAttributesComposite.VISIBILITY_CHANGED_EVENT )
			{
				( (DifferenceSeries) series ).getLineAttributes( )
						.setVisible( ( (Boolean) event.data ).booleanValue( ) );
				enableLineSettings( ( (DifferenceSeries) series ).getLineAttributes( )
						.isVisible( )
						|| ( (DifferenceSeries) series ).getNegativeLineAttributes( )
								.isVisible( ) );
			}
			else if ( event.type == LineAttributesComposite.STYLE_CHANGED_EVENT )
			{
				( (DifferenceSeries) series ).getLineAttributes( )
						.setStyle( (LineStyle) event.data );
			}
			else if ( event.type == LineAttributesComposite.WIDTH_CHANGED_EVENT )
			{
				( (DifferenceSeries) series ).getLineAttributes( )
						.setThickness( ( (Integer) event.data ).intValue( ) );
			}
			else if ( event.type == LineAttributesComposite.COLOR_CHANGED_EVENT )
			{
				( (DifferenceSeries) series ).getLineAttributes( )
						.setColor( (ColorDefinition) event.data );
			}
		}
		else if ( event.widget.equals( liacLine2 ) )
		{
			if ( event.type == LineAttributesComposite.VISIBILITY_CHANGED_EVENT )
			{
				( (DifferenceSeries) series ).getNegativeLineAttributes( )
						.setVisible( ( (Boolean) event.data ).booleanValue( ) );
				enableLineSettings( ( (DifferenceSeries) series ).getNegativeLineAttributes( )
						.isVisible( )
						|| ( (DifferenceSeries) series ).getLineAttributes( )
								.isVisible( ) );
			}
			else if ( event.type == LineAttributesComposite.STYLE_CHANGED_EVENT )
			{
				( (DifferenceSeries) series ).getNegativeLineAttributes( )
						.setStyle( (LineStyle) event.data );
			}
			else if ( event.type == LineAttributesComposite.WIDTH_CHANGED_EVENT )
			{
				( (DifferenceSeries) series ).getNegativeLineAttributes( )
						.setThickness( ( (Integer) event.data ).intValue( ) );
			}
			else if ( event.type == LineAttributesComposite.COLOR_CHANGED_EVENT )
			{
				( (DifferenceSeries) series ).getNegativeLineAttributes( )
						.setColor( (ColorDefinition) event.data );
			}
		}
	}

	private void enableLineSettings( boolean isEnabled )
	{
		btnCurve.setEnabled( isEnabled );
	}

}