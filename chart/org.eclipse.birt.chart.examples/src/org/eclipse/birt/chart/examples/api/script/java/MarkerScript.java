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

package org.eclipse.birt.chart.examples.api.script.java;

import java.util.Locale;
import org.eclipse.birt.chart.model.component.Axis;
import org.eclipse.birt.chart.model.component.MarkerLine;
import org.eclipse.birt.chart.model.component.MarkerRange;
import org.eclipse.birt.chart.script.ChartItemScriptHandlerAdapter;
import org.eclipse.birt.chart.script.IChartScriptContext;

/**
 * 
 */

public class MarkerScript extends ChartItemScriptHandlerAdapter
{

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.script.IChartItemScriptHandler#beforeDrawMarkerLine(org.eclipse.birt.chart.model.component.Axis,
	 *      org.eclipse.birt.chart.model.component.MarkerLine,
	 *      org.eclipse.birt.chart.script.IChartScriptContext)
	 */
	public void beforeDrawMarkerLine( Axis axis, MarkerLine mLine,
			IChartScriptContext icsc )
	{
		Locale.setDefault( Locale.US );
		if ( icsc.getLocale( ).equals( Locale.US ) )
		{
			mLine.getLabel( ).getCaption( ).getColor( ).set( 165, 184, 55 );
			mLine.getLineAttributes( ).getColor( ).set( 165, 184, 55 );
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.script.IChartItemScriptHandler#beforeDrawMarkerRange(org.eclipse.birt.chart.model.component.Axis,
	 *      org.eclipse.birt.chart.model.component.MarkerRange,
	 *      org.eclipse.birt.chart.script.IChartScriptContext)
	 */
	public void beforeDrawMarkerRange( Axis axis, MarkerRange mRange,
			IChartScriptContext icsc )
	{
		mRange.getLabel( ).getCaption( ).getColor( ).set( 225, 104, 105 );
	}

}
