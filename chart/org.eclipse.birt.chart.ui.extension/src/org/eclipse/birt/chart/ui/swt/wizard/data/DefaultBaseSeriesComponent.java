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

import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.ui.extension.i18n.Messages;
import org.eclipse.birt.chart.ui.swt.interfaces.ISelectDataComponent;
import org.eclipse.birt.chart.ui.swt.interfaces.IUIServiceProvider;
import org.eclipse.birt.chart.ui.util.ChartUIConstancts;
import org.eclipse.birt.chart.ui.util.ChartUIUtil;
import org.eclipse.birt.chart.ui.util.UIHelper;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

/**
 * 
 */

public class DefaultBaseSeriesComponent implements ISelectDataComponent
{

	private transient Label lblBottomXSeries;

	private transient SeriesDefinition seriesDefn;

	private transient IUIServiceProvider serviceprovider = null;

	private transient String sTitle = null;

	private transient String labelText = null;

	private transient Object oContext = null;

	private transient ISelectDataComponent comData;

	public DefaultBaseSeriesComponent( SeriesDefinition seriesDefn,
			IUIServiceProvider builder, Object oContext, String sTitle )
	{
		this( seriesDefn,
				builder,
				oContext,
				sTitle,
				Messages.getString( "BarBottomAreaComponent.Label.CategoryXSeries" ) ); //$NON-NLS-1$
	}

	public DefaultBaseSeriesComponent( SeriesDefinition seriesDefn,
			IUIServiceProvider builder, Object oContext, String sTitle,
			String labelText )
	{
		super( );
		this.seriesDefn = seriesDefn;
		this.serviceprovider = builder;
		this.oContext = oContext;
		this.sTitle = sTitle;
		this.labelText = labelText;
	}

	public Composite createArea( Composite parent )
	{
		Composite cmpBottom = new Composite( parent, SWT.NONE );
		{
			GridLayout gridLayout = new GridLayout( 5, false );
			gridLayout.marginWidth = 10;
			gridLayout.marginHeight = 0;
			cmpBottom.setLayout( gridLayout );
			GridData gridData = new GridData( GridData.FILL_HORIZONTAL
					| GridData.VERTICAL_ALIGN_BEGINNING );
			cmpBottom.setLayoutData( gridData );
		}

		Label leftAngle = new Label( cmpBottom, SWT.NONE );
		{
			GridData gridData = new GridData( );
			leftAngle.setLayoutData( gridData );
			leftAngle.setImage( UIHelper.getImage( ChartUIConstancts.IMAGE_RA_LEFTUP ) );
			leftAngle.getImage( ).setBackground( leftAngle.getBackground( ) );
		}

		lblBottomXSeries = new Label( cmpBottom, SWT.NONE );
		lblBottomXSeries.setText( labelText );

		comData = new BaseDataDefinitionComponent( seriesDefn,
				ChartUIUtil.getDataQuery( seriesDefn, 0 ),
				serviceprovider,
				oContext,
				sTitle );
		comData.createArea( cmpBottom );

		Label rightAngle = new Label( cmpBottom, SWT.NONE );
		rightAngle.setImage( UIHelper.getImage( ChartUIConstancts.IMAGE_RA_RIGHTUP ) );
		rightAngle.getImage( ).setBackground( rightAngle.getBackground( ) );

		return cmpBottom;
	}

	public void selectArea( boolean selected, Object data )
	{
		comData.selectArea( selected, data );
	}

	public void dispose( )
	{
		comData.dispose( );
	}
}
