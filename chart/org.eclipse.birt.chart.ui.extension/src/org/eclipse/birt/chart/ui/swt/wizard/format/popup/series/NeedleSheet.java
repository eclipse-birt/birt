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

package org.eclipse.birt.chart.ui.swt.wizard.format.popup.series;

import org.eclipse.birt.chart.model.ChartWithoutAxes;
import org.eclipse.birt.chart.model.DialChart;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.model.type.DialSeries;
import org.eclipse.birt.chart.ui.extension.i18n.Messages;
import org.eclipse.birt.chart.ui.swt.composites.NeedleComposite;
import org.eclipse.birt.chart.ui.swt.wizard.ChartWizardContext;
import org.eclipse.birt.chart.ui.swt.wizard.format.popup.AbstractPopupSheet;
import org.eclipse.birt.chart.ui.util.ChartHelpContextIds;
import org.eclipse.birt.chart.ui.util.ChartUIUtil;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;

public class NeedleSheet extends AbstractPopupSheet 
{
	
	private transient DialChart dChart;
	
	private transient int index;
	
	private transient StackLayout slNeedle = null;
	
	private transient Group grpNeedle;
	
	private transient NeedleComposite cmpN = null;

	private transient Composite cmpSI = null;	

	private transient TabFolder tf = null;

	public NeedleSheet( String title, ChartWizardContext context,
			DialChart chart, int index )
	{
		super( title, context, true );
		this.dChart = chart;
		this.index = index;
	}

	protected Composite getComponent( Composite parent )
	{
		ChartUIUtil.bindHelp( parent, ChartHelpContextIds.POPUP_SERIES_PALETTE );
		// Sheet content composite
		Composite cmpContent = new Composite( parent, SWT.NONE );
		{
			// Layout for the content composite
			GridLayout glContent = new GridLayout( );
			glContent.marginHeight = 7;
			glContent.marginWidth = 7;
			cmpContent.setLayout( glContent );
		}

		// Palete composite
		slNeedle = new StackLayout( );

		grpNeedle = new Group( cmpContent, SWT.NONE );
		grpNeedle.setLayoutData( new GridData( GridData.FILL_BOTH ) );
		grpNeedle.setLayout( slNeedle );

		cmpN = new NeedleComposite( grpNeedle,
				getContext( ),
				getSeriesForProcessing( index ) );

		cmpSI = new Composite( grpNeedle, SWT.NONE );
		{
			GridLayout gl = new GridLayout( );
			gl.marginLeft = 0;
			gl.marginRight = 0;
			cmpSI.setLayoutData( new GridData( GridData.FILL_BOTH ) );
			cmpSI.setLayout( gl );
		}

		tf = new TabFolder( cmpSI, SWT.NONE );
		{
			tf.setLayoutData( new GridData( GridData.FILL_BOTH ) );
		}

		for ( int i = 0; i < ( (SeriesDefinition) ( (ChartWithoutAxes) getChart( ) ).getSeriesDefinitions( )
				.get( 0 ) ).getSeriesDefinitions( ).size( ); i++ )
		{
			TabItem ti = new TabItem( tf, SWT.NONE );
			ti.setText( Messages.getString( "NeedleSheet.Lbl.Dial" ) + ( i + 1 ) ); //$NON-NLS-1$
			ti.setControl( new NeedleComposite( tf,
					getContext( ),
					getSeriesForProcessing( i ) ) );
		}
		tf.setSelection( 0 );

		if ( dChart.isDialSuperimposition( ) )
		{
			slNeedle.topControl = cmpSI;
		}
		else
		{
			slNeedle.topControl = cmpN;
		}
		
		return cmpContent;
	}
	
	private DialSeries getSeriesForProcessing( int index )
	{
		SeriesDefinition sd = (SeriesDefinition) ( (SeriesDefinition) ( (ChartWithoutAxes) getChart( ) ).getSeriesDefinitions( )
				.get( 0 ) ).getSeriesDefinitions( ).get( index );
		return ( (DialSeries) sd.getDesignTimeSeries( ) );
	}
}
