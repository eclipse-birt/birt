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

package org.eclipse.birt.chart.ui.swt.wizard.data;

import java.util.ArrayList;

import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.ui.extension.i18n.Messages;
import org.eclipse.birt.chart.ui.swt.interfaces.ISelectDataComponent;
import org.eclipse.birt.chart.ui.swt.interfaces.ISelectDataCustomizeUI;
import org.eclipse.birt.chart.ui.swt.interfaces.IUIServiceProvider;
import org.eclipse.birt.chart.ui.util.ChartUIConstancts;
import org.eclipse.birt.chart.ui.util.ChartUIUtil;
import org.eclipse.birt.chart.ui.util.UIHelper;
import org.eclipse.emf.common.util.EList;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

/**
 * This UI component is made up of data text fields for grouping series of each
 * axis.
 */

public class MultipleSeriesComponent implements ISelectDataComponent
{

	private transient EList[] seriesDefnsArray;

	private transient IUIServiceProvider serviceprovider = null;

	private transient String sTitle = null;

	private transient Object oContext = null;

	private static final String LABEL_GROUPING_YSERIES = Messages.getString( "MultipleSeriesComponent.Label.OptionalYSeriesGrouping" ); //$NON-NLS-1$
	private static final String LABEL_GROUPING_OVERLAY = Messages.getString( "MultipleSeriesComponent.Label.OptionalOverlayGrouping" ); //$NON-NLS-1$
	private static final String LABEL_GROUPING_WITHOUTAXIS = Messages.getString( "MultipleSeriesComponent.Label.OptionalGrouping" ); //$NON-NLS-1$

	private transient ISelectDataCustomizeUI selectDataUI = null;

	private transient ArrayList components = new ArrayList( );

	private transient boolean isSingle = false;

	public MultipleSeriesComponent( EList[] seriesDefnsArray,
			IUIServiceProvider builder, Object oContext, String sTitle,
			ISelectDataCustomizeUI selectDataUI )
	{
		super( );
		this.seriesDefnsArray = seriesDefnsArray;
		this.serviceprovider = builder;
		this.oContext = oContext;
		this.sTitle = sTitle;
		this.selectDataUI = selectDataUI;
	}

	public MultipleSeriesComponent( EList seriesDefns,
			IUIServiceProvider builder, Object oContext, String sTitle,
			ISelectDataCustomizeUI selectDataUI )
	{
		this( new EList[]{
			seriesDefns
		}, builder, oContext, sTitle, selectDataUI );
		isSingle = true;
	}

	public Composite createArea( Composite parent )
	{
		Composite cmp = new Composite( parent, SWT.NONE );
		{
			GridLayout gridLayout = new GridLayout( );
			gridLayout.marginWidth = 0;
			gridLayout.marginHeight = 0;
			cmp.setLayout( gridLayout );
			cmp.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
		}

		Label topAngle = new Label( cmp, SWT.NONE );
		{
			topAngle.setImage( UIHelper.getImage( ChartUIConstancts.IMAGE_RA_TOPLEFT ) );
		}

		for ( int i = 0; i < seriesDefnsArray.length; i++ )
		{
			createRightGroupArea( cmp, i, seriesDefnsArray[i] );
		}

		Label bottomAngle = new Label( cmp, SWT.NONE );
		{
			bottomAngle.setImage( UIHelper.getImage( ChartUIConstancts.IMAGE_RA_BOTTOMLEFT ) );
		}

		return cmp;
	}

	private void createRightGroupArea( Composite parent, final int axisIndex,
			final EList seriesDefn )
	{
		final String strDesc = getGroupingDescription( axisIndex );
		ISelectDataComponent subUIGroupY = new ISelectDataComponent( ) {

			private transient Composite cmpGroup;
			private transient Label lblRightYGrouping;

			public Composite createArea( Composite parent )
			{
				cmpGroup = ChartUIUtil.createCompositeWrapper( parent );
				cmpGroup.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );

				lblRightYGrouping = new Label( cmpGroup, SWT.WRAP );
				{
					GridData gd = new GridData( );
					gd.widthHint = 100;
					lblRightYGrouping.setLayoutData( gd );
					lblRightYGrouping.setText( strDesc );
				}

				int selectedSeriesIndex = selectDataUI.getSeriesIndex( )[axisIndex];
				if ( seriesDefn != null && !seriesDefn.isEmpty( ) )
				{
					// Only display current selected series
					ISelectDataComponent subUI = selectDataUI.getAreaComponent( ISelectDataCustomizeUI.GROUPING_SERIES,
							( (SeriesDefinition) seriesDefn.get( selectedSeriesIndex ) ),
							serviceprovider,
							oContext,
							sTitle );
					subUI.createArea( cmpGroup );
					components.add( subUI );
				}
				return cmpGroup;
			}

			public void selectArea( boolean selected, Object data )
			{

			}

			public void dispose( )
			{
				// TODO Auto-generated method stub

			}
		};
		subUIGroupY.createArea( parent );
		components.add( subUIGroupY );
	}

	public void selectArea( boolean selected, Object data )
	{
		for ( int i = 0; i < components.size( ); i++ )
		{
			( (ISelectDataComponent) components.get( i ) ).selectArea( selected,
					data );
		}
	}

	public void dispose( )
	{
		for ( int i = 0; i < components.size( ); i++ )
		{
			( (ISelectDataComponent) components.get( i ) ).dispose( );
		}
	}

	private String getGroupingDescription( int axisIndex )
	{
		if ( isSingle )
		{
			return LABEL_GROUPING_WITHOUTAXIS;
		}
		if ( axisIndex == 0 )
		{
			return LABEL_GROUPING_YSERIES;
		}
		return LABEL_GROUPING_OVERLAY;
	}

}
