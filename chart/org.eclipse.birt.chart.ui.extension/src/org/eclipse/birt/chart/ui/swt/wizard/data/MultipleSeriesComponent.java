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
import java.util.Iterator;

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
 * 
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
			cmp.setLayoutData( new GridData( GridData.HORIZONTAL_ALIGN_FILL ) );
		}

		Label topAngle = new Label( cmp, SWT.NONE );
		{
			topAngle.setImage( UIHelper.getImage( ChartUIConstancts.IMAGE_RA_TOPLEFT ) );
		}

		for ( int i = 0; i < seriesDefnsArray.length; i++ )
		{
			String label = null;
			if ( isSingle )
			{
				label = LABEL_GROUPING_WITHOUTAXIS;
			}
			else
			{
				if ( i == 0 )
				{
					label = LABEL_GROUPING_YSERIES;
				}
				else
				{
					label = LABEL_GROUPING_OVERLAY;
				}
			}
			createRightGroupArea( cmp, label, seriesDefnsArray[i] );
		}

		Label bottomAngle = new Label( cmp, SWT.NONE );
		{
			bottomAngle.setImage( UIHelper.getImage( ChartUIConstancts.IMAGE_RA_BOTTOMLEFT ) );
		}

		return cmp;
	}

	private void createRightGroupArea( Composite parent, final String label,
			final EList seriesDefn )
	{
		ISelectDataComponent subUIGroupY = new ISelectDataComponent( ) {

			private transient Composite cmpGroup;
			private transient Label lblRightYGrouping;

			public Composite createArea( Composite parent )
			{
				cmpGroup = ChartUIUtil.createCompositeWrapper( parent );

				lblRightYGrouping = new Label( cmpGroup, SWT.NONE );
				lblRightYGrouping.setText( label );

				if ( seriesDefn != null && !seriesDefn.isEmpty( ) )
				{
					int index = 1;
					for ( Iterator iterator = seriesDefn.iterator( ); iterator.hasNext( ); index++ )
					{
						if ( seriesDefn.size( ) == 1 )
						{
							// Remove the title when only single series
							index = 0;
						}
						createRightAxisArea( cmpGroup,
								index,
								( (SeriesDefinition) iterator.next( ) ) );
					}
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

	private void createRightAxisArea( Composite parent, final int axisIndex,
			SeriesDefinition seriesDefn )
	{
		ISelectDataComponent rightAxisArea = new ISelectDataComponent( ) {

			private transient Composite cmpRightAxisArea;
			private transient Label label;

			public Composite createArea( Composite parent )
			{
				cmpRightAxisArea = new Composite( parent, SWT.NONE );
				cmpRightAxisArea.setLayoutData( new GridData( GridData.HORIZONTAL_ALIGN_FILL
						| GridData.VERTICAL_ALIGN_FILL ) );
				{
					GridLayout gridLayout = new GridLayout( );
					gridLayout.marginWidth = 0;
					gridLayout.marginHeight = 0;
					cmpRightAxisArea.setLayout( gridLayout );
				}

				if ( axisIndex > 0 )
				{
					label = new Label( cmpRightAxisArea, SWT.NONE );
					String str = Messages.getString( "MultipleSeriesComponent.Label.Series" );//$NON-NLS-1$
					if ( !isSingle )
					{
						str = "Y " + str; //$NON-NLS-1$
					}
					label.setText( str + axisIndex + ":" ); //$NON-NLS-1$
				}

				return cmpRightAxisArea;
			}

			public void selectArea( boolean selected, Object data )
			{

			}

			public void dispose( )
			{
				// TODO Auto-generated method stub

			}
		};
		Composite cmp = rightAxisArea.createArea( parent );
		components.add( rightAxisArea );

		// Creates chart type customized UI for right area
		// ISelectDataComponent subUI = new BaseDataDefinitionComponent(
		// seriesDefn,
		// serviceprovider,
		// oContext,
		// sTitle );
		ISelectDataComponent subUI = selectDataUI.getAreaComponent( ISelectDataCustomizeUI.GROUPING_SERIES,
				seriesDefn,
				serviceprovider,
				oContext,
				sTitle );
		subUI.createArea( cmp );
		components.add( subUI );
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

}
