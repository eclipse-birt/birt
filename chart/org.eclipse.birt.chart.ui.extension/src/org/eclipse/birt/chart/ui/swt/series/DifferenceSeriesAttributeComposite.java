/***********************************************************************
 * Copyright (c) 2004, 2007 Actuate Corporation.
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
import org.eclipse.birt.chart.model.type.LineSeries;
import org.eclipse.birt.chart.model.type.impl.LineSeriesImpl;
import org.eclipse.birt.chart.model.util.ChartElementUtil;
import org.eclipse.birt.chart.ui.extension.i18n.Messages;
import org.eclipse.birt.chart.ui.plugin.ChartUIExtensionPlugin;
import org.eclipse.birt.chart.ui.swt.composites.LineAttributesComposite;
import org.eclipse.birt.chart.ui.swt.wizard.ChartWizardContext;
import org.eclipse.birt.chart.ui.util.ChartHelpContextIds;
import org.eclipse.birt.chart.ui.util.ChartUIExtensionUtil;
import org.eclipse.birt.chart.ui.util.ChartUIUtil;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;


public class DifferenceSeriesAttributeComposite extends Composite
		implements
			SelectionListener,
			Listener
{

	private Group grpLine1 = null;

	private LineAttributesComposite liacLine1 = null;
	
	private Group grpLine2 = null;

	private LineAttributesComposite liacLine2 = null;

	private Series series = null;

	private ChartWizardContext context;

	private Combo cmbPalette;

	private Combo cmbCurve;

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
		GridLayout glContent = new GridLayout( 2, false );
		glContent.marginHeight = 2;
		glContent.marginWidth = 2;

		// Main content composite
		this.setLayout( glContent );
		
		Group grpLine = new Group( this, SWT.NONE );
		{
			GridLayout glGroup = new GridLayout( 2, true );
			glGroup.horizontalSpacing = 5;
			grpLine.setLayout( glGroup );
			grpLine.setLayoutData( new GridData( GridData.FILL_BOTH ) );
			grpLine.setText( Messages.getString( "DifferenceSeriesAttributeComposite.Lbl.Line" ) ); //$NON-NLS-1$
		}

		grpLine1 = new Group( grpLine, SWT.NONE );
		{
			GridData gdGRPLine = new GridData( GridData.FILL_BOTH );
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
				true,
				true,
				true );
		liacLine1.addListener( this );
		
		grpLine2 = new Group( grpLine, SWT.NONE );
		{
			GridData gdGRPLine = new GridData( GridData.FILL_BOTH );
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
				true,
				true,
				true );
		liacLine2.addListener( this );
		
		Composite cmpButton = new Composite( grpLine, SWT.NONE );
		{
			GridData gd = new GridData( );
			gd.horizontalSpan = 2;
			cmpButton.setLayoutData( gd );
			cmpButton.setLayout( new GridLayout( 4, false ) );
		}
		
		Label lbl = new Label( cmpButton, SWT.NONE );
		lbl.setText( Messages.getString( "DifferenceSeriesAttributeComposite.Lbl.LinePalette" ) ); //$NON-NLS-1$
		
		cmbPalette = ChartUIExtensionUtil.createTrueFalseItemsCombo( cmpButton );
		{
			cmbPalette.setLayoutData( new GridData( ) );
			cmbPalette.select( ( (LineSeries) series ).isSetPaletteLineColor( ) ? ( ( (LineSeries) series ).isPaletteLineColor( ) ? 1
					: 2 )
					: 0 );
			cmbPalette.addSelectionListener( this );
		}

		lbl = new Label( cmpButton, SWT.NONE );
		lbl.setText( Messages.getString( "DifferenceSeriesAttributeComposite.Lbl.ShowLinesAsCurves" ) ); //$NON-NLS-1$
		
		cmbCurve = ChartUIExtensionUtil.createTrueFalseItemsCombo( cmpButton );
		{
			cmbCurve.setLayoutData( new GridData( ) );
			cmbCurve.select( ( (DifferenceSeries) series ).isSetCurve( ) ? ( ( (DifferenceSeries) series ).isCurve( ) ? 1
					: 2 )
					: 0 );
			cmbCurve.addSelectionListener( this );
		}

		enableLinePaletteSetting( ( (DifferenceSeries) series ).getLineAttributes( )
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
		if ( e.getSource( ).equals( cmbCurve ) )
		{
			ChartElementUtil.setEObjectAttribute( ( (DifferenceSeries) series ),
					"curve", //$NON-NLS-1$
					cmbCurve.getSelectionIndex( ) == 1,
					cmbCurve.getSelectionIndex( ) == 0 );
		}
		else if ( e.getSource( ).equals( cmbPalette ) )
		{
			ChartElementUtil.setEObjectAttribute( ( (DifferenceSeries) series ),
					"paletteLineColor", //$NON-NLS-1$
					cmbPalette.getSelectionIndex( ) == 1,
					cmbPalette.getSelectionIndex( ) == 0 );
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
		boolean isUnset = ( event.detail == ChartUIExtensionUtil.PROPERTY_UNSET );
		if ( event.widget.equals( liacLine1 ) )
		{
			if ( event.type == LineAttributesComposite.VISIBILITY_CHANGED_EVENT )
			{
				ChartElementUtil.setEObjectAttribute( ( (DifferenceSeries) series ).getLineAttributes( ),
						"visible",//$NON-NLS-1$
						( (Boolean) event.data ).booleanValue( ),
						isUnset );
				enableLinePaletteSetting( ( ( (DifferenceSeries) series ).getLineAttributes( )
						.isSetVisible( ) && ( (DifferenceSeries) series ).getLineAttributes( )
						.isVisible( ) )
						|| ( ( (DifferenceSeries) series ).getNegativeLineAttributes( )
								.isSetVisible( ) && ( (DifferenceSeries) series ).getNegativeLineAttributes( )
								.isVisible( ) ) );
			}
			else if ( event.type == LineAttributesComposite.STYLE_CHANGED_EVENT )
			{
				ChartElementUtil.setEObjectAttribute( ( (DifferenceSeries) series ).getLineAttributes( ),
						"style",//$NON-NLS-1$
						(LineStyle) event.data,
						isUnset );
			}
			else if ( event.type == LineAttributesComposite.WIDTH_CHANGED_EVENT )
			{
				ChartElementUtil.setEObjectAttribute( ( (DifferenceSeries) series ).getLineAttributes( ),
						"thickness",//$NON-NLS-1$
						( (Integer) event.data ).intValue( ),
						isUnset );
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
				ChartElementUtil.setEObjectAttribute( ( (DifferenceSeries) series ).getNegativeLineAttributes( ),
						"visible",//$NON-NLS-1$
						( (Boolean) event.data ).booleanValue( ),
						isUnset );
				enableLinePaletteSetting( ( ( (DifferenceSeries) series ).getNegativeLineAttributes( )
						.isSetVisible( ) && ( (DifferenceSeries) series ).getNegativeLineAttributes( )
						.isVisible( ) )
						|| ( ( (DifferenceSeries) series ).getLineAttributes( )
								.isSetVisible( ) && ( (DifferenceSeries) series ).getLineAttributes( )
								.isVisible( ) ) );
			}
			else if ( event.type == LineAttributesComposite.STYLE_CHANGED_EVENT )
			{
				ChartElementUtil.setEObjectAttribute( ( (DifferenceSeries) series ).getNegativeLineAttributes( ),
						"style",//$NON-NLS-1$
						(LineStyle) event.data,
						isUnset );
			}
			else if ( event.type == LineAttributesComposite.WIDTH_CHANGED_EVENT )
			{
				ChartElementUtil.setEObjectAttribute( ( (DifferenceSeries) series ).getNegativeLineAttributes( ),
						"thickness",//$NON-NLS-1$
						( (Integer) event.data ).intValue( ),
						isUnset );
			}
			else if ( event.type == LineAttributesComposite.COLOR_CHANGED_EVENT )
			{
				( (DifferenceSeries) series ).getNegativeLineAttributes( )
						.setColor( (ColorDefinition) event.data );
			}
		}
	}

	/**
	 * Enable the LinePalette button.
	 * 
	 * @param isEnabled
	 *            enabled status.
	 */
	private void enableLinePaletteSetting( boolean isEnabled )
	{
		if ( cmbPalette != null )
		{
			cmbPalette.setEnabled( isEnabled );
		}
	}
}