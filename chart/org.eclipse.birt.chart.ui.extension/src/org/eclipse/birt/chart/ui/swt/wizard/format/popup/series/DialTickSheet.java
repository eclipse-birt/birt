/*******************************************************************************
 * Copyright (c) 2006 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.chart.ui.swt.wizard.format.popup.series;

import org.eclipse.birt.chart.model.attribute.ColorDefinition;
import org.eclipse.birt.chart.model.attribute.TickStyle;
import org.eclipse.birt.chart.model.component.Dial;
import org.eclipse.birt.chart.model.type.DialSeries;
import org.eclipse.birt.chart.ui.extension.i18n.Messages;
import org.eclipse.birt.chart.ui.swt.composites.GridAttributesComposite;
import org.eclipse.birt.chart.ui.swt.composites.IntegerSpinControl;
import org.eclipse.birt.chart.ui.swt.wizard.ChartWizardContext;
import org.eclipse.birt.chart.ui.swt.wizard.format.popup.AbstractPopupSheet;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;

/**
 * 
 */

public class DialTickSheet extends AbstractPopupSheet implements Listener
{

	private transient GridAttributesComposite gacMajor = null;

	private transient GridAttributesComposite gacMinor = null;

	private transient Label lblGridCount = null;

	private transient IntegerSpinControl iscGridCount = null;

	private transient DialSeries series;

	public DialTickSheet( String title, ChartWizardContext context,
			DialSeries series )
	{
		super( title, context, false );
		this.series = series;
	}

	protected Composite getComponent( Composite parent )
	{
		GridLayout glContent = new GridLayout( );
		glContent.numColumns = 2;
		glContent.marginHeight = 7;
		glContent.marginWidth = 7;
		glContent.verticalSpacing = 5;

		Composite cmpContent = new Composite( parent, SWT.NONE );
		cmpContent.setLayout( glContent );

		// Layout for the Major Grid group
		FillLayout flMajor = new FillLayout( );

		// Layout for the Minor Grid group
		FillLayout flMinor = new FillLayout( );

		// Major Grid
		Group grpMajor = new Group( cmpContent, SWT.NONE );
		GridData gdGRPMajor = new GridData( GridData.FILL_HORIZONTAL );
		grpMajor.setLayoutData( gdGRPMajor );
		grpMajor.setText( Messages.getString( "OrthogonalSeriesDataSheetImpl.Lbl.MajorGrid" ) ); //$NON-NLS-1$
		grpMajor.setLayout( flMajor );

		gacMajor = new GridAttributesComposite( grpMajor,
				SWT.NONE,
				getContext( ),
				getDialForProcessing( ).getMajorGrid( ),
				false );
		gacMajor.addListener( this );

		// Minor Grid
		Group grpMinor = new Group( cmpContent, SWT.NONE );
		GridData gdGRPMinor = new GridData( GridData.FILL_HORIZONTAL );
		grpMinor.setLayoutData( gdGRPMinor );
		grpMinor.setText( Messages.getString( "OrthogonalSeriesDataSheetImpl.Lbl.MinorGrid" ) ); //$NON-NLS-1$
		grpMinor.setLayout( flMinor );

		gacMinor = new GridAttributesComposite( grpMinor,
				SWT.NONE,
				getContext( ),
				getDialForProcessing( ).getMinorGrid( ),
				false );
		gacMinor.addListener( this );

		Composite cmpGridCount = new Composite( cmpContent, SWT.NONE );
		{
			GridData gdCMPGridCount = new GridData( GridData.FILL_HORIZONTAL );
			gdCMPGridCount.horizontalSpan = 2;
			cmpGridCount.setLayoutData( gdCMPGridCount );
			cmpGridCount.setLayout( new GridLayout( 2, false ) );
		}

		lblGridCount = new Label( cmpGridCount, SWT.NONE );
		lblGridCount.setText( Messages.getString( "OrthogonalSeriesDataSheetImpl.Lbl.MinorGridCount" ) ); //$NON-NLS-1$

		iscGridCount = new IntegerSpinControl( cmpGridCount,
				SWT.NONE,
				getDialForProcessing( ).getScale( ).getMinorGridsPerUnit( ) );
		{
			GridData gdISCGridCount = new GridData( GridData.FILL_HORIZONTAL );
			iscGridCount.setLayoutData( gdISCGridCount );
			iscGridCount.addListener( this );
		}

		setState( getDialForProcessing( ).getMinorGrid( )
				.getTickAttributes( )
				.isVisible( ) );

		return cmpContent;
	}

	public void handleEvent( Event event )
	{
		if ( this.gacMajor.equals( event.widget ) )
		{
			switch ( event.type )
			{
				case GridAttributesComposite.TICK_COLOR_CHANGED_EVENT :
					getDialForProcessing( ).getMajorGrid( )
							.getTickAttributes( )
							.setColor( (ColorDefinition) event.data );
					break;
				case GridAttributesComposite.TICK_STYLE_CHANGED_EVENT :
					getDialForProcessing( ).getMajorGrid( )
							.setTickStyle( (TickStyle) event.data );
					break;
				case GridAttributesComposite.TICK_VISIBILITY_CHANGED_EVENT :
					getDialForProcessing( ).getMajorGrid( )
							.getTickAttributes( )
							.setVisible( ( (Boolean) event.data ).booleanValue( ) );
					break;
			}
		}
		else if ( this.gacMinor.equals( event.widget ) )
		{
			switch ( event.type )
			{
				case GridAttributesComposite.TICK_COLOR_CHANGED_EVENT :
					getDialForProcessing( ).getMinorGrid( )
							.getTickAttributes( )
							.setColor( (ColorDefinition) event.data );
					break;
				case GridAttributesComposite.TICK_STYLE_CHANGED_EVENT :
					getDialForProcessing( ).getMinorGrid( )
							.setTickStyle( (TickStyle) event.data );
					break;
				case GridAttributesComposite.TICK_VISIBILITY_CHANGED_EVENT :
					getDialForProcessing( ).getMinorGrid( )
							.getTickAttributes( )
							.setVisible( ( (Boolean) event.data ).booleanValue( ) );
					setState( getDialForProcessing( ).getMinorGrid( )
							.getTickAttributes( )
							.isVisible( ) );
					break;
			}
		}
		else if ( event.widget.equals( iscGridCount ) )
		{
			getDialForProcessing( ).getScale( )
					.setMinorGridsPerUnit( ( (Integer) event.data ).intValue( ) );
		}
	}

	public Dial getDialForProcessing( )
	{
		return series.getDial( );
	}

	private void setState( boolean enabled )
	{
		lblGridCount.setEnabled( enabled );
		iscGridCount.setEnabled( enabled );
	}

}
