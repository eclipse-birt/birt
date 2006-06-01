/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.chart.ui.swt.series;

import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.log.ILogger;
import org.eclipse.birt.chart.log.Logger;
import org.eclipse.birt.chart.model.attribute.LineDecorator;
import org.eclipse.birt.chart.model.attribute.LineStyle;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.type.DialSeries;
import org.eclipse.birt.chart.model.type.impl.DialSeriesImpl;
import org.eclipse.birt.chart.ui.extension.i18n.Messages;
import org.eclipse.birt.chart.ui.plugin.ChartUIExtensionPlugin;
import org.eclipse.birt.chart.ui.swt.composites.DialScaleDialog;
import org.eclipse.birt.chart.ui.swt.composites.DialTicksDialog;
import org.eclipse.birt.chart.ui.swt.composites.HeadStyleAttributeComposite;
import org.eclipse.birt.chart.ui.swt.composites.IntegerSpinControl;
import org.eclipse.birt.chart.ui.swt.composites.LineAttributesComposite;
import org.eclipse.birt.chart.ui.swt.composites.LocalizedNumberEditorComposite;
import org.eclipse.birt.chart.ui.swt.wizard.ChartWizardContext;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
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

/**
 * Implement Meter Chart -> Orthogonal Series -> Series Details
 */
public class MeterSeriesAttributeComposite extends Composite implements
		Listener,
		ModifyListener,
		SelectionListener
{

	private transient Composite cmpContent = null;

	private transient Composite cmpButton = null;

	private transient LocalizedNumberEditorComposite txtRadius = null;

	private transient IntegerSpinControl iscStartAngle = null;

	private transient IntegerSpinControl iscStopAngle = null;

	private transient DialSeries series = null;

	private transient Button btnTicks = null;

	private transient Button btnScale = null;

	private transient Group grpNeedle = null;

	private transient LineAttributesComposite liacNeedle = null;

	private transient HeadStyleAttributeComposite cmbHeadStyle = null;

	private transient ChartWizardContext wizardContext;

	private static ILogger logger = Logger.getLogger( "org.eclipse.birt.chart.ui.extension/swt.series" ); //$NON-NLS-1$

	/**
	 * @param parent
	 * @param style
	 * @param series
	 */
	public MeterSeriesAttributeComposite( Composite parent, int style,
			ChartWizardContext wizardContext, Series series )
	{
		super( parent, style );
		if ( !( series instanceof DialSeriesImpl ) )
		{
			try
			{
				throw new ChartException( ChartUIExtensionPlugin.ID,
						ChartException.VALIDATION,
						"MeterSeriesAttributeComposite.Exception.IllegalArgument", new Object[]{series.getClass( ).getName( )}, Messages.getResourceBundle( ) ); //$NON-NLS-1$
			}
			catch ( ChartException e )
			{
				logger.log( e );
				e.printStackTrace( );
			}
		}
		this.series = (DialSeries) series;
		this.wizardContext = wizardContext;
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
		// Layout for the content composite
		GridLayout glContent = new GridLayout( 2, true );
		glContent.verticalSpacing = 0;
		glContent.horizontalSpacing = 10;
		glContent.marginHeight = 7;
		glContent.marginWidth = 7;

		// Main content composite
		this.setLayout( glContent );

		// Composite for Content
		cmpContent = new Composite( this, SWT.NONE );
		GridData gdCMPContent = new GridData( GridData.FILL_HORIZONTAL );
		cmpContent.setLayoutData( gdCMPContent );
		cmpContent.setLayout( new GridLayout( 2, false ) );

		Label lblRadius = new Label( cmpContent, SWT.NONE );
		GridData gdLBLRadius = new GridData( GridData.HORIZONTAL_ALIGN_END );
		lblRadius.setLayoutData( gdLBLRadius );
		lblRadius.setText( Messages.getString( "MeterSeriesAttributeComposite.Lbl.Radius" ) ); //$NON-NLS-1$

		txtRadius = new LocalizedNumberEditorComposite( cmpContent, SWT.BORDER
				| SWT.SINGLE );
		GridData gdTXTRadius = new GridData( GridData.FILL_HORIZONTAL );
		if ( series.getDial( ).isSetRadius( ) )
		{
			txtRadius.setValue( series.getDial( ).getRadius( ) );
		}
		txtRadius.setLayoutData( gdTXTRadius );
		txtRadius.addModifyListener( this );

		Label lblStartAngle = new Label( cmpContent, SWT.NONE );
		GridData gdLBLStartAngle = new GridData( GridData.HORIZONTAL_ALIGN_END );
		lblStartAngle.setLayoutData( gdLBLStartAngle );
		lblStartAngle.setText( Messages.getString( "MeterSeriesAttributeComposite.Lbl.StartAngle" ) ); //$NON-NLS-1$

		iscStartAngle = new IntegerSpinControl( cmpContent,
				SWT.NONE,
				(int) series.getDial( ).getStartAngle( ) );
		GridData gdISCStartAngle = new GridData( GridData.FILL_HORIZONTAL );
		iscStartAngle.setLayoutData( gdISCStartAngle );
		iscStartAngle.setValue( (int) ( series.getDial( ).getStartAngle( ) ) );
		iscStartAngle.setMinimum( -360 );
		iscStartAngle.setMaximum( 360 );
		iscStartAngle.addListener( this );

		Label lblStopAngle = new Label( cmpContent, SWT.NONE );
		GridData gdLBLStopAngle = new GridData( GridData.HORIZONTAL_ALIGN_END );
		lblStopAngle.setLayoutData( gdLBLStopAngle );
		lblStopAngle.setText( Messages.getString( "MeterSeriesAttributeComposite.Lbl.StopAngle" ) ); //$NON-NLS-1$

		iscStopAngle = new IntegerSpinControl( cmpContent,
				SWT.NONE,
				(int) series.getDial( ).getStopAngle( ) );
		GridData gdISCStopAngle = new GridData( GridData.FILL_HORIZONTAL );
		iscStopAngle.setLayoutData( gdISCStopAngle );
		iscStopAngle.setValue( (int) ( series.getDial( ).getStopAngle( ) ) );
		iscStopAngle.setMinimum( -360 );
		iscStopAngle.setMaximum( 360 );
		iscStopAngle.addListener( this );

		cmpButton = new Composite( cmpContent, SWT.NONE );
		GridData gdCMPButton = new GridData( GridData.FILL_HORIZONTAL );
		gdCMPButton.horizontalSpan = 2;
		cmpButton.setLayoutData( gdCMPButton );
		cmpButton.setLayout( new GridLayout( 2, true ) );

		btnTicks = new Button( cmpButton, SWT.PUSH );
		GridData gdBTNTicks = new GridData( GridData.FILL_HORIZONTAL );
		btnTicks.setLayoutData( gdBTNTicks );
		btnTicks.setText( Messages.getString( "MeterSeriesAttributeComposite.Lbl.DialTicks" ) );//$NON-NLS-1$
		btnTicks.addSelectionListener( this );

		btnScale = new Button( cmpButton, SWT.PUSH );
		GridData gdBTNScale = new GridData( GridData.FILL_HORIZONTAL );
		btnScale.setLayoutData( gdBTNScale );
		btnScale.setText( Messages.getString( "MeterSeriesAttributeComposite.Lbl.DialScale" ) );//$NON-NLS-1$
		btnScale.addSelectionListener( this );

		// Layout for the Needle group
		GridLayout glNeedle = new GridLayout( 1, true );
		glNeedle.verticalSpacing = 0;
		glNeedle.marginWidth = 10;
		glNeedle.marginHeight = 0;

		// Needle
		grpNeedle = new Group( this, SWT.NONE );
		GridData gdGRPNeedle = new GridData( GridData.FILL_HORIZONTAL
				| GridData.VERTICAL_ALIGN_BEGINNING );
		gdGRPNeedle.heightHint = 93;
		grpNeedle.setLayoutData( gdGRPNeedle );
		grpNeedle.setText( Messages.getString( "MeterSeriesAttributeSheetImpl.Lbl.Needle" ) );//$NON-NLS-1$
		grpNeedle.setLayout( glNeedle );

		liacNeedle = new LineAttributesComposite( grpNeedle,
				SWT.NONE,
				wizardContext,
				series.getNeedle( ).getLineAttributes( ),
				true,
				true,
				false,
				false );
		GridData gdLIACNeedle = new GridData( GridData.FILL_HORIZONTAL );
		gdLIACNeedle.horizontalIndent = 24;
		liacNeedle.setLayoutData( gdLIACNeedle );
		liacNeedle.addListener( this );

		cmbHeadStyle = new HeadStyleAttributeComposite( grpNeedle,
				SWT.NONE,
				series.getNeedle( ).getDecorator( ) );
		GridData gdCMBHeadStyle = new GridData( GridData.FILL_HORIZONTAL );
		cmbHeadStyle.setLayoutData( gdCMBHeadStyle );
		cmbHeadStyle.addListener( this );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.events.ModifyListener#modifyText(org.eclipse.swt.events.ModifyEvent)
	 */
	public void modifyText( ModifyEvent e )
	{
		if ( e.widget.equals( txtRadius ) )
		{
			if ( txtRadius.isSetValue( ) )
			{
				series.getDial( ).setRadius( txtRadius.getValue( ) );
			}
			else
			{
				series.getDial( ).unsetRadius( );
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.widgets.Listener#handleEvent(org.eclipse.swt.widgets.Event)
	 */
	public void handleEvent( Event event )
	{
		if ( event.widget.equals( iscStartAngle ) )
		{
			series.getDial( )
					.setStartAngle( ( (Integer) event.data ).intValue( ) );
		}
		else if ( event.widget.equals( iscStopAngle ) )
		{
			series.getDial( )
					.setStopAngle( ( (Integer) event.data ).intValue( ) );
		}
		else if ( event.widget.equals( liacNeedle ) )
		{
			if ( event.type == LineAttributesComposite.STYLE_CHANGED_EVENT )
			{
				series.getNeedle( )
						.getLineAttributes( )
						.setStyle( (LineStyle) event.data );
			}
			else if ( event.type == LineAttributesComposite.WIDTH_CHANGED_EVENT )
			{
				series.getNeedle( )
						.getLineAttributes( )
						.setThickness( ( (Integer) event.data ).intValue( ) );
			}
		}
		else if ( event.widget.equals( cmbHeadStyle ) )
		{
			if ( event.type == HeadStyleAttributeComposite.STYLE_CHANGED_EVENT )
			{
				series.getNeedle( ).setDecorator( (LineDecorator) event.data );
			}
		}
	}

	public void widgetDefaultSelected( SelectionEvent e )
	{
		// TODO Auto-generated method stub
	}

	public void widgetSelected( SelectionEvent e )
	{
		if ( e.widget.equals( btnTicks ) )
		{
			DialTicksDialog ticksDialog = new DialTicksDialog( this.getShell( ),
					wizardContext,
					series );
			series.setDial( ticksDialog.getDialForProcessing( ) );
		}
		else if ( e.widget.equals( btnScale ) )
		{
			DialScaleDialog scaleDialog = new DialScaleDialog( this.getShell( ),
					series );
			series.setDial( scaleDialog.getDialForProcessing( ) );
		}
	}

}
