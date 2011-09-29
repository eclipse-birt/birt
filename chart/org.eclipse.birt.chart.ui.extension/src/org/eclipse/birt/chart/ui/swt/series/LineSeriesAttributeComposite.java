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
import org.eclipse.birt.chart.model.util.ChartElementUtil;
import org.eclipse.birt.chart.ui.extension.i18n.Messages;
import org.eclipse.birt.chart.ui.plugin.ChartUIExtensionPlugin;
import org.eclipse.birt.chart.ui.swt.composites.FillChooserComposite;
import org.eclipse.birt.chart.ui.swt.composites.LineAttributesComposite;
import org.eclipse.birt.chart.ui.swt.wizard.ChartWizardContext;
import org.eclipse.birt.chart.ui.util.ChartHelpContextIds;
import org.eclipse.birt.chart.ui.util.ChartUIExtensionUtil;
import org.eclipse.birt.chart.ui.util.ChartUIUtil;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
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
public class LineSeriesAttributeComposite extends Composite
		implements
			SelectionListener,
			Listener
{

	private transient Label lblShadow;

	private transient FillChooserComposite fccShadow = null;

	protected transient Group grpLine = null;

	private transient LineAttributesComposite liacLine = null;

	protected transient Series series = null;

	protected transient ChartWizardContext context;

	private Label lblPalette;

	private Combo cmbPalette;

	private Label lblCurve;

	private Combo cmbCurve;

	private Label lblMissingValue;

	private Combo cmbMissingValue;

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

	protected void placeComponents( )
	{
		// Main content composite
		this.setLayout( new GridLayout( ) );
		grpLine = new Group( this, SWT.NONE );
		GridLayout glLine = new GridLayout( 2, true );
		glLine.horizontalSpacing = 0;
		grpLine.setLayout( glLine );	
		grpLine.setLayoutData( new GridData( GridData.FILL_BOTH ) );
		grpLine.setText( Messages.getString( "LineSeriesAttributeComposite.Lbl.Line" ) ); //$NON-NLS-1$
		initUIComponents( grpLine );
		enableLineSettings( ( (LineSeries) series ).getLineAttributes( )
				.isVisible( ) );
	}

	protected void initUIComponents( Composite parent  )
	{

		Composite cmpLine = new Composite( parent, SWT.NONE );
		{
			GridLayout gl = new GridLayout( 2, false );
			gl.marginHeight = 0;
			gl.marginWidth = 0;
			gl.horizontalSpacing = 0;
			gl.verticalSpacing = 0;
			cmpLine.setLayout( gl );
			cmpLine.setLayoutData( new GridData( GridData.FILL_BOTH ) );
		}
		
		int lineStyles = LineAttributesComposite.ENABLE_VISIBILITY
				| LineAttributesComposite.ENABLE_STYLES
				| LineAttributesComposite.ENABLE_WIDTH
				| LineAttributesComposite.ENABLE_COLOR
				| LineAttributesComposite.ENABLE_AUTO_COLOR;
		liacLine = new LineAttributesComposite( cmpLine,
				SWT.NONE,
				lineStyles,
				context,
				( (LineSeries) series ).getLineAttributes( ) );
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

			int iFillOption = FillChooserComposite.DISABLE_PATTERN_FILL
					| FillChooserComposite.ENABLE_TRANSPARENT
					| FillChooserComposite.ENABLE_TRANSPARENT_SLIDER
					| FillChooserComposite.ENABLE_AUTO;

			fccShadow = new FillChooserComposite( cmpShadow,
					SWT.DROP_DOWN | SWT.READ_ONLY,
					iFillOption,
					context,
					( (LineSeries) series ).getShadowColor( ) );

			GridData gdFCCShadow = new GridData( GridData.FILL_HORIZONTAL );
			fccShadow.setLayoutData( gdFCCShadow );
			fccShadow.addListener( this );
		}

		Composite cmp = new Composite( grpLine, SWT.NONE );
		GridLayout gl = new GridLayout( 2, false ) ;
		cmp.setLayout( gl );

		lblPalette = new Label(cmp, SWT.NONE );
		lblPalette.setText( Messages.getString( "LineSeriesAttributeComposite.Lbl.LinePalette" ) ); //$NON-NLS-1$
		cmbPalette = ChartUIExtensionUtil.createCombo( cmp, ChartUIExtensionUtil.getTrueFalseComboItems( ) );
		{
			cmbPalette.select( ( (LineSeries) series ).isSetPaletteLineColor( ) ? ( ( (LineSeries) series ).isPaletteLineColor( ) ? 1
					: 2 )
					: 0 );
			cmbPalette.addSelectionListener( this );
		}
		
		lblCurve = new Label(cmp, SWT.NONE );
		lblCurve.setText( Messages.getString( "LineSeriesAttributeComposite.Lbl.ShowLinesAsCurves" ) ); //$NON-NLS-1$
		
		cmbCurve = ChartUIExtensionUtil.createCombo( cmp, ChartUIExtensionUtil.getTrueFalseComboItems( ) );
		{
			cmbCurve.select( ( (LineSeries) series ).isSetCurve( ) ? ( ( (LineSeries) series ).isCurve( ) ? 1
					: 2 )
					: 0 );
			cmbCurve.addSelectionListener( this );
		}

		if ( !( series instanceof AreaSeries && ( series.isSetStacked( ) && series.isStacked( ) ) ) )
		{
			lblMissingValue = new Label(cmp, SWT.NONE );
			lblMissingValue.setText( Messages.getString( "LineSeriesAttributeComposite.Lbl.ConnectMissingValue" ) ); //$NON-NLS-1$
			cmbMissingValue = ChartUIExtensionUtil.createCombo( cmp, ChartUIExtensionUtil.getTrueFalseComboItems( ) );
			{
				cmbMissingValue.select( ( (LineSeries) series ).isSetConnectMissingValue( ) ? ( ( (LineSeries) series ).isConnectMissingValue( ) ? 1
						: 2 )
						: 0 );
				cmbMissingValue.addSelectionListener( this );
			}
		}
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
		if ( e.widget == cmbCurve )
		{
			ChartElementUtil.setEObjectAttribute( series,
					"curve", //$NON-NLS-1$
					cmbCurve.getSelectionIndex( ) == 1,
					cmbCurve.getSelectionIndex( ) == 0 );
		}
		else if ( e.widget == cmbPalette )
		{
			ChartElementUtil.setEObjectAttribute( series,
					"paletteLineColor", //$NON-NLS-1$
					cmbPalette.getSelectionIndex( ) == 1,
					cmbPalette.getSelectionIndex( ) == 0 );
		}
		else if ( e.widget == cmbMissingValue )
		{
			ChartElementUtil.setEObjectAttribute( series,
					"connectMissingValue", //$NON-NLS-1$
					cmbMissingValue.getSelectionIndex( ) == 1,
					cmbMissingValue.getSelectionIndex( ) == 0 );
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
		if ( event.widget.equals( liacLine ) )
		{
			if ( event.type == LineAttributesComposite.VISIBILITY_CHANGED_EVENT )
			{
				ChartElementUtil.setEObjectAttribute( ( (LineSeries) series ).getLineAttributes( ),
						"visible", //$NON-NLS-1$
						( (Boolean) event.data ).booleanValue( ),
						isUnset );
				
				enableLineSettings( ( (LineSeries) series ).getLineAttributes( )
						.isSetVisible( )
						&& ( (LineSeries) series ).getLineAttributes( )
								.isVisible( ) );
			}
			else if ( event.type == LineAttributesComposite.STYLE_CHANGED_EVENT )
			{
				ChartElementUtil.setEObjectAttribute( ( (LineSeries) series ).getLineAttributes( ),
						"style", //$NON-NLS-1$
						(LineStyle) event.data,
						isUnset );
			}
			else if ( event.type == LineAttributesComposite.WIDTH_CHANGED_EVENT )
			{
				ChartElementUtil.setEObjectAttribute( ( (LineSeries) series ).getLineAttributes( ),
						"thickness", //$NON-NLS-1$
						( (Integer) event.data ).intValue( ),
						isUnset );
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

	protected void enableLineSettings( boolean isEnabled )
	{
		if ( lblShadow != null )
		{
			lblShadow.setEnabled( isEnabled );
		}
		if ( fccShadow != null )
		{
			fccShadow.setEnabled( isEnabled );
		}
		if ( cmbPalette != null )
		{
			lblPalette.setEnabled( isEnabled );
			cmbPalette.setEnabled( isEnabled );
		}
		if ( cmbMissingValue != null )
		{
			lblMissingValue.setEnabled( isEnabled );
			cmbMissingValue.setEnabled( isEnabled );
		}
		lblCurve.setEnabled( isEnabled );
		cmbCurve.setEnabled( isEnabled );
	}

}