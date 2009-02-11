/*******************************************************************************
 * Copyright (c) 2004, 2007 Actuate Corporation.
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
import org.eclipse.birt.chart.model.attribute.Fill;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.type.DialSeries;
import org.eclipse.birt.chart.model.type.impl.DialSeriesImpl;
import org.eclipse.birt.chart.ui.extension.i18n.Messages;
import org.eclipse.birt.chart.ui.plugin.ChartUIExtensionPlugin;
import org.eclipse.birt.chart.ui.swt.composites.FillChooserComposite;
import org.eclipse.birt.chart.ui.swt.composites.IntegerSpinControl;
import org.eclipse.birt.chart.ui.swt.composites.LocalizedNumberEditorComposite;
import org.eclipse.birt.chart.ui.swt.fieldassist.TextNumberEditorAssistField;
import org.eclipse.birt.chart.ui.swt.wizard.ChartWizardContext;
import org.eclipse.birt.chart.ui.util.ChartHelpContextIds;
import org.eclipse.birt.chart.ui.util.ChartUIUtil;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;

/**
 * Implement Meter Chart -> Orthogonal Series -> Series Details
 */
public class MeterSeriesAttributeComposite extends Composite implements
		Listener,
		ModifyListener
{
	private LocalizedNumberEditorComposite txtRadius = null;

	private IntegerSpinControl iscStartAngle = null;

	private IntegerSpinControl iscStopAngle = null;

	private DialSeries series = null;
	
	private FillChooserComposite fcc = null;

	private ChartWizardContext wizardContext;

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
		ChartUIUtil.bindHelp( parent, ChartHelpContextIds.SUBTASK_YSERIES_METER );
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
		Composite cmpLeft = new Composite( this, SWT.NONE );
		GridData gdLeft = new GridData( GridData.FILL_HORIZONTAL );
		cmpLeft.setLayoutData( gdLeft );
		GridLayout gl = new GridLayout( 2, false );
		gl.horizontalSpacing = 8;
		cmpLeft.setLayout( gl );

		Label lblRadius = new Label( cmpLeft, SWT.NONE );
		GridData gdLBLRadius = new GridData( GridData.HORIZONTAL_ALIGN_END );
		lblRadius.setLayoutData( gdLBLRadius );
		lblRadius.setText( Messages.getString( "MeterSeriesAttributeComposite.Lbl.Radius" ) ); //$NON-NLS-1$

		txtRadius = new LocalizedNumberEditorComposite( cmpLeft, SWT.BORDER
				| SWT.SINGLE );
		new TextNumberEditorAssistField( txtRadius.getTextControl( ), null );
		
		GridData gdTXTRadius = new GridData( GridData.FILL_HORIZONTAL );
		if ( series.getDial( ).isSetRadius( ) )
		{
			txtRadius.setValue( series.getDial( ).getRadius( ) );
		}
		txtRadius.setLayoutData( gdTXTRadius );
		txtRadius.addModifyListener( this );
		
		Label lblFill = new Label( cmpLeft, SWT.NONE );
		GridData gdFill = new GridData( GridData.HORIZONTAL_ALIGN_END );
		lblFill.setLayoutData( gdFill );
		lblFill.setText( Messages.getString( "MeterSeriesAttributeSheetImpl.Lbl.Fill" ) ); //$NON-NLS-1$

		fcc = new FillChooserComposite( cmpLeft,
				SWT.NONE,
				wizardContext,
				series.getDial( ).getFill( ),
				true,
				true );
		fcc.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
		fcc.addListener( this );

		Composite cmpRight = new Composite( this, SWT.NONE );
		GridData gdRight = new GridData( GridData.FILL_HORIZONTAL );
		cmpRight.setLayoutData( gdRight );
		gl = new GridLayout( 2, false );
		gl.horizontalSpacing = 8;
		cmpRight.setLayout( gl );
		
		Label lblStartAngle = new Label( cmpRight, SWT.NONE );
		GridData gdLBLStartAngle = new GridData( GridData.HORIZONTAL_ALIGN_END );
		lblStartAngle.setLayoutData( gdLBLStartAngle );
		lblStartAngle.setText( Messages.getString( "MeterSeriesAttributeComposite.Lbl.StartAngle" ) ); //$NON-NLS-1$

		iscStartAngle = new IntegerSpinControl( cmpRight,
				SWT.NONE,
				(int) series.getDial( ).getStartAngle( ) );
		GridData gdISCStartAngle = new GridData( GridData.FILL_HORIZONTAL );
		iscStartAngle.setLayoutData( gdISCStartAngle );
		iscStartAngle.setValue( (int) ( series.getDial( ).getStartAngle( ) ) );
		iscStartAngle.setMinimum( -360 );
		iscStartAngle.setMaximum( 360 );
		iscStartAngle.addListener( this );

		Label lblStopAngle = new Label( cmpRight, SWT.NONE );
		GridData gdLBLStopAngle = new GridData( GridData.HORIZONTAL_ALIGN_END );
		lblStopAngle.setLayoutData( gdLBLStopAngle );
		lblStopAngle.setText( Messages.getString( "MeterSeriesAttributeComposite.Lbl.StopAngle" ) ); //$NON-NLS-1$

		iscStopAngle = new IntegerSpinControl( cmpRight,
				SWT.NONE,
				(int) series.getDial( ).getStopAngle( ) );
		GridData gdISCStopAngle = new GridData( GridData.FILL_HORIZONTAL );
		iscStopAngle.setLayoutData( gdISCStopAngle );
		iscStopAngle.setValue( (int) ( series.getDial( ).getStopAngle( ) ) );
		iscStopAngle.setMinimum( -360 );
		iscStopAngle.setMaximum( 360 );
		iscStopAngle.addListener( this );
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
		else if ( event.widget.equals( fcc ) )
		{
			series.getDial( ).setFill( (Fill) event.data );
		}
	}

}
