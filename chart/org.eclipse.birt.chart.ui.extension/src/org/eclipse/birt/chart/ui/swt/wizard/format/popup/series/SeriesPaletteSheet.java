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

	private SeriesDefinition cSeriesDefn = null;

	private SeriesDefinition[] vSeriesDefns = null;

	private ChartWizardContext context = null;

	private boolean isGroupedSeries = false;

	private StackLayout slPalette = null;

	private Group grpPalette = null;

	private PaletteEditorComposite cmpPE = null;

	private Composite cmpMPE = null;

	private TabFolder tf = null;
	
	private final int iFillChooserStyle;

	/**
	 * 
	 * @param title
	 * @param context
	 * @param cSeriesDefn
	 * @param vSeriesDefns
	 * @param isGroupedSeries
	 * @param iFillChooserStyle
	 *            style to decide what fill types should display in fill chooser
	 */
	public SeriesPaletteSheet( String title, ChartWizardContext context,
			SeriesDefinition cSeriesDefn, SeriesDefinition[] vSeriesDefns,
			boolean isGroupedSeries, int iFillChooserStyle )
	{
		super( title, context, true );
		this.context = context;
		this.cSeriesDefn = cSeriesDefn;
		this.vSeriesDefns = vSeriesDefns;
		this.isGroupedSeries = isGroupedSeries;
		this.iFillChooserStyle = iFillChooserStyle;
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
				vSeriesDefns,
				iFillChooserStyle );

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

		if ( isGroupedSeries && isColoredByValue( ) )
		{
			for ( int i = 0; i < vSeriesDefns.length; i++ )
			{
				TabItem ti = new TabItem( tf, SWT.NONE );
				ti.setText( "Series" + ( i + 1 ) ); //$NON-NLS-1$
				ti.setControl( new PaletteEditorComposite( tf,
						getContext( ),
						vSeriesDefns[i].getSeriesPalette( ),
						null,
						iFillChooserStyle ) );
			}
			tf.setSelection( 0 );
			slPalette.topControl = cmpMPE;
		}
		else
		{
			if ( isMultiAxes( ) && isColoredByValue( ) )
			{

				for ( int i = 0; i < ChartUIUtil.getOrthogonalAxisNumber( context.getModel( ) ); i++ )
				{
					SeriesDefinition[] seriesDefns = ChartUIUtil.getOrthogonalSeriesDefinitions( context.getModel( ),
							i )
							.toArray( new SeriesDefinition[]{} ) ;
					TabItem ti = new TabItem( tf, SWT.NONE );
					ti.setText( "Axis" + ( i + 1 ) ); //$NON-NLS-1$
					ti.setControl( new PaletteEditorComposite( tf,
							getContext( ),
							seriesDefns[0].getSeriesPalette( ),
							seriesDefns,
							iFillChooserStyle ) );
				}
				tf.setSelection( 0 );
				slPalette.topControl = cmpMPE;
			}
			else
			{
				slPalette.topControl = cmpPE;
			}
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
	
	private boolean isMultiAxes( )
	{
		return ChartUIUtil.getOrthogonalAxisNumber( context.getModel( ) ) > 1;
	}
	
}