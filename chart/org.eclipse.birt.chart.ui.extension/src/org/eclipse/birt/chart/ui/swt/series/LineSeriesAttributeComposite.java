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
import org.eclipse.birt.chart.model.attribute.ChartDimension;
import org.eclipse.birt.chart.model.attribute.ColorDefinition;
import org.eclipse.birt.chart.model.attribute.LineStyle;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.type.AreaSeries;
import org.eclipse.birt.chart.model.type.LineSeries;
import org.eclipse.birt.chart.model.type.ScatterSeries;
import org.eclipse.birt.chart.model.type.impl.LineSeriesImpl;
import org.eclipse.birt.chart.ui.extension.i18n.Messages;
import org.eclipse.birt.chart.ui.plugin.ChartUIExtensionPlugin;
import org.eclipse.birt.chart.ui.swt.composites.FillChooserComposite;
import org.eclipse.birt.chart.ui.swt.composites.LineAttributesComposite;
import org.eclipse.birt.chart.ui.swt.wizard.ChartWizardContext;
import org.eclipse.birt.chart.ui.util.ChartHelpContextIds;
import org.eclipse.birt.chart.ui.util.ChartUIUtil;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;

/**
 * @author Actuate Corporation
 * 
 */
public class LineSeriesAttributeComposite extends Composite
		implements
			SelectionListener,
			Listener
{

	private transient Button btnCurve = null;

	private transient Button btnMissingValue = null;

	private transient Button btnPalette = null;

	private transient Label lblShadow;

	private transient FillChooserComposite fccShadow = null;

	private transient Group grpLine = null;

	private transient LineAttributesComposite liacLine = null;

	private transient Series series = null;

	private transient ChartWizardContext context;

	private static ILogger logger = Logger.getLogger( "org.eclipse.birt.chart.ui.extension/swt.series" ); //$NON-NLS-1$

	/**
	 * @param parent
	 * @param style
	 */
	public LineSeriesAttributeComposite( Composite parent, int style,
			ChartWizardContext context, Series series )
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
		this.context = context;
		init( );
		placeComponents( );
		
		ChartUIUtil.bindHelp( parent, getHelpId( series ) );
	}

	private String getHelpId( Series series )
	{
		String helpId = ChartHelpContextIds.SUBTASK_YSERIES_LINE;
		if ( series instanceof AreaSeries )
		{
			helpId = ChartHelpContextIds.SUBTASK_YSERIES_AREA;
		}
		else if ( series instanceof ScatterSeries )
		{
			helpId = ChartHelpContextIds.SUBTASK_YSERIES_SCATTER;
		}
		return helpId;
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
		
		grpLine = new Group( this, SWT.NONE );
		GridLayout glLine = new GridLayout( 2, false );
		glLine.horizontalSpacing = 0;
		grpLine.setLayout( glLine );	
		grpLine.setLayoutData( new GridData( GridData.FILL_BOTH ) );
		grpLine.setText( Messages.getString( "LineSeriesAttributeComposite.Lbl.Line" ) ); //$NON-NLS-1$

		Composite cmpLine = new Composite( grpLine, SWT.NONE );
		{
			GridLayout gl = new GridLayout( 2, false );
			gl.marginHeight = 0;
			gl.marginWidth = 0;
			gl.horizontalSpacing = 0;
			gl.verticalSpacing = 0;
			cmpLine.setLayout( gl );
			cmpLine.setLayoutData( new GridData( GridData.FILL_BOTH ) );
		}
		
		liacLine = new LineAttributesComposite( cmpLine,
				SWT.NONE,
				context,
				( (LineSeries) series ).getLineAttributes( ),
				true,
				true,
				true );
		GridData gdLIACLine = new GridData( GridData.FILL_HORIZONTAL );
		gdLIACLine.horizontalSpan = 2;
		liacLine.setLayoutData( gdLIACLine );
		liacLine.addListener( this );
		
		if ( isShadowNeeded( ) )
		{
			Composite cmpShadow = new Composite( cmpLine, SWT.NONE );
			{
				GridLayout gl = new GridLayout( 2, false );
				gl.marginHeight = 0;
				gl.marginBottom = 0;
				gl.verticalSpacing = 0;
				cmpShadow.setLayout( gl );
				GridData gd = new GridData( GridData.FILL_HORIZONTAL );
				gd.horizontalSpan = 2;
				cmpShadow.setLayoutData( gd );
			}
			
			lblShadow = new Label( cmpShadow, SWT.NONE );
			lblShadow.setText( Messages.getString( "LineSeriesAttributeComposite.Lbl.ShadowColor" ) ); //$NON-NLS-1$

			fccShadow = new FillChooserComposite( cmpShadow,
					SWT.NONE,
					context,
					( (LineSeries) series ).getShadowColor( ),
					false,
					false );
			GridData gdFCCShadow = new GridData( GridData.FILL_HORIZONTAL );
			fccShadow.setLayoutData( gdFCCShadow );
			fccShadow.addListener( this );
		}

		Composite cmp = new Composite( grpLine, SWT.NONE );
		cmp.setLayout( new GridLayout( ) );

		btnPalette = new Button( cmp, SWT.CHECK );
		{
			btnPalette.setText( Messages.getString( "LineSeriesAttributeComposite.Lbl.LinePalette" ) ); //$NON-NLS-1$
			btnPalette.setSelection( ( (LineSeries) series ).isPaletteLineColor( ) );
			btnPalette.addSelectionListener( this );
		}

		btnCurve = new Button( cmp, SWT.CHECK );
		{
			btnCurve.setText( Messages.getString( "LineSeriesAttributeComposite.Lbl.ShowLinesAsCurves" ) ); //$NON-NLS-1$
			btnCurve.setSelection( ( (LineSeries) series ).isCurve( ) );
			btnCurve.addSelectionListener( this );
		}

		if ( !( series instanceof AreaSeries || series instanceof ScatterSeries ) )
		{
			btnMissingValue = new Button( cmp, SWT.CHECK );
			{
				btnMissingValue.setText( Messages.getString( "LineSeriesAttributeComposite.Lbl.ConnectMissingValue" ) ); //$NON-NLS-1$
				btnMissingValue.setSelection( ( (LineSeries) series ).isConnectMissingValue( ) );
				btnMissingValue.addSelectionListener( this );
			}
		}

		enableLineSettings( ( (LineSeries) series ).getLineAttributes( )
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
		return !( series instanceof AreaSeries )
				&& context.getModel( ).getDimension( ).getValue( ) != ChartDimension.THREE_DIMENSIONAL;
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
		if ( btnMissingValue != null )
		{
			btnMissingValue.setEnabled( isEnabled );
		}
		btnCurve.setEnabled( isEnabled );
	}

}