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

package org.eclipse.birt.chart.ui.swt.composites;

import org.eclipse.birt.chart.model.attribute.LineDecorator;
import org.eclipse.birt.chart.model.attribute.LineStyle;
import org.eclipse.birt.chart.model.type.DialSeries;
import org.eclipse.birt.chart.ui.swt.wizard.ChartWizardContext;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

/**
 * 
 */

public class NeedleComposite extends Composite implements Listener
{
	
	private transient DialSeries series;

	private transient LineAttributesComposite liacNeedle;
	
	private HeadStyleAttributeComposite cmbHeadStyle;
	
	public NeedleComposite( Composite coParent,
			ChartWizardContext wizardContext, 
			DialSeries series )
	{
		super( coParent, SWT.NONE );
		this.series = series;
		GridLayout gl = new GridLayout( 1, true );
		gl.verticalSpacing = 0;
		gl.marginWidth = 10;
		gl.marginHeight = 0;
		setLayout( gl );
		
		liacNeedle = new LineAttributesComposite( this,
				SWT.NONE,
				wizardContext,
				series.getNeedle( ).getLineAttributes( ),
				true,
				true,
				false,
				false );
		GridData gdLIACNeedle = new GridData( GridData.FILL_HORIZONTAL );
		liacNeedle.setLayoutData( gdLIACNeedle );
		liacNeedle.addListener( this );

		cmbHeadStyle = new HeadStyleAttributeComposite( this,
				SWT.NONE,
				series.getNeedle( ).getDecorator( ) );
		GridData gdCMBHeadStyle = new GridData( GridData.FILL_HORIZONTAL );
		cmbHeadStyle.setLayoutData( gdCMBHeadStyle );
		cmbHeadStyle.addListener( this );
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.swt.widgets.Listener#handleEvent(org.eclipse.swt.widgets.Event)
	 */
	public void handleEvent( Event event )
	{
		if ( event.widget.equals( liacNeedle ) )
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
}
