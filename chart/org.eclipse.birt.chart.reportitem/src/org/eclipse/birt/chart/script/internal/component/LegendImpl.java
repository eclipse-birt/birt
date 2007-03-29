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

package org.eclipse.birt.chart.script.internal.component;

import org.eclipse.birt.chart.model.layout.Legend;
import org.eclipse.birt.chart.script.api.ChartComponentFactory;
import org.eclipse.birt.chart.script.api.attribute.ILabel;
import org.eclipse.birt.chart.script.api.component.ILegend;

/**
 * 
 */

public class LegendImpl implements ILegend
{

	private Legend legend;

	public LegendImpl( Legend legend )
	{
		this.legend = legend;
	}

	public ILabel getTitle( )
	{
		return ChartComponentFactory.convertLabel( legend.getTitle( ) );
	}

	public boolean isVisible( )
	{
		return legend.isVisible( );
	}

	public void setTitle( ILabel title )
	{
		legend.setTitle( ChartComponentFactory.convertILabel( title ) );
	}

	public void setVisible( boolean visible )
	{
		legend.setVisible( visible );
	}

	public boolean isShowValue( )
	{
		return legend.isShowValue( );
	}

	public void setShowValue( boolean show )
	{
		legend.setShowValue( show );
	}

}
