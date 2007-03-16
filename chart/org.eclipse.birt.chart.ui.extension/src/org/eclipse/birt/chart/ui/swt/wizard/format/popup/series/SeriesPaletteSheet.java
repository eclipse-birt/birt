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

package org.eclipse.birt.chart.ui.swt.wizard.format.popup.series;

import org.eclipse.birt.chart.model.attribute.LegendItemType;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.ui.extension.i18n.Messages;
import org.eclipse.birt.chart.ui.swt.composites.PaletteEditorComposite;
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

/**
 * 
 */

public class SeriesPaletteSheet extends AbstractPopupSheet
{

	private transient SeriesDefinition cSeriesDefn = null;

	private transient SeriesDefinition[] vSeriesDefns = null;

	private transient ChartWizardContext context = null;

	private transient boolean isGroupedSeries = false;

	private transient StackLayout slPalette = null;

	private transient Group grpPalette = null;

	private transient PaletteEditorComposite cmpPE = null;

	private transient Composite cmpMPE = null;

	private transient TabFolder tf = null;

	public SeriesPaletteSheet( String title, ChartWizardContext context,
			SeriesDefinition cSeriesDefn, SeriesDefinition[] vSeriesDefns,
			boolean isGroupedSeries )
	{

		super( title, context, true );
		this.context = context;
		this.cSeriesDefn = cSeriesDefn;
		this.vSeriesDefns = vSeriesDefns;
		this.isGroupedSeries = isGroupedSeries;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.ui.swt.interfaces.ISheet#getComponent(org.eclipse.swt.widgets.Composite)
	 */
	public Composite getComponent( Composite parent )
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
		slPalette = new StackLayout( );

		grpPalette = new Group( cmpContent, SWT.NONE );
		GridData gdGRPPalette = new GridData( GridData.FILL_BOTH );
		gdGRPPalette.heightHint = 300;
		grpPalette.setLayoutData( gdGRPPalette );
		grpPalette.setLayout( slPalette );
		grpPalette.setText( Messages.getString( "BaseSeriesAttributeSheetImpl.Lbl.Palette" ) ); //$NON-NLS-1$

		cmpPE = new PaletteEditorComposite( grpPalette,
				getContext( ),
				cSeriesDefn.getSeriesPalette( ),
				vSeriesDefns );

		cmpMPE = new Composite( grpPalette, SWT.NONE );
		{
			GridLayout gl = new GridLayout( );
			gl.marginLeft = 0;
			gl.marginRight = 0;
			cmpMPE.setLayoutData( new GridData( GridData.FILL_BOTH ) );
			cmpMPE.setLayout( gl );
		}

		tf = new TabFolder( cmpMPE, SWT.NONE );
		{
			tf.setLayoutData( new GridData( GridData.FILL_BOTH ) );
		}

		for ( int i = 0; i < vSeriesDefns.length; i++ )
		{
			TabItem ti = new TabItem( tf, SWT.NONE );
			ti.setText( "Series" + ( i + 1 ) ); //$NON-NLS-1$
			ti.setControl( new PaletteEditorComposite( tf,
					getContext( ),
					vSeriesDefns[i].getSeriesPalette( ),
					null ) );
		}
		tf.setSelection( 0 );

		if ( isGroupedSeries && isColoredByValue( ) )
		{
			slPalette.topControl = cmpMPE;
		}
		else
		{
			slPalette.topControl = cmpPE;
		}

		return cmpContent;
	}

	public void setGroupedPalette( boolean isGroupedSeries )
	{
		this.isGroupedSeries = isGroupedSeries;
	}

	public void setCategorySeries( SeriesDefinition sd )
	{
		this.cSeriesDefn = sd;
	}

	private boolean isColoredByValue( )
	{
		return context.getModel( ).getLegend( ).getItemType( ).getValue( ) == LegendItemType.SERIES;
	}
}