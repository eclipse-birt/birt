/*******************************************************************************
 * Copyright (c) 2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.chart.examples.builder;

import org.eclipse.birt.chart.ui.swt.CustomPreviewTable;
import org.eclipse.birt.chart.ui.swt.DefaultChartDataSheet;
import org.eclipse.birt.chart.ui.swt.interfaces.IDataServiceProvider;
import org.eclipse.birt.chart.ui.util.ChartUIUtil;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

/**
 * 
 */

public class SampleStandardDataSheet extends DefaultChartDataSheet
{

	private IDataServiceProvider dataProvider;

	private CustomPreviewTable tablePreview = null;

	public SampleStandardDataSheet( IDataServiceProvider dataProvider )
	{
		this.dataProvider = dataProvider;
	}

	public Composite createDataDragSource( Composite parent )
	{
		Composite composite = ChartUIUtil.createCompositeWrapper( parent );
		{
			composite.setLayoutData( new GridData( GridData.FILL_BOTH ) );
		}
		Label label = new Label( composite, SWT.NONE );
		{
			label.setText( "Data Preview" );
			label.setFont( JFaceResources.getBannerFont( ) );
		}
		Label description = new Label( composite, SWT.WRAP );
		{
			GridData gd = new GridData( GridData.FILL_HORIZONTAL );
			description.setLayoutData( gd );
			description.setText( "Drag" );
		}

		tablePreview = new CustomPreviewTable( composite, SWT.SINGLE
				| SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION );
		{
			GridData gridData = new GridData( GridData.FILL_BOTH );
			gridData.widthHint = 400;
			gridData.heightHint = 120;
			tablePreview.setLayoutData( gridData );
			tablePreview.setHeaderAlignment( SWT.LEFT );
		}
		return composite;
	}

	private DefaultDataServiceProviderImpl getDataServiceProvider( )
	{
		return (DefaultDataServiceProviderImpl) dataProvider;
	}
}
