/***********************************************************************
 * Copyright (c) 2004, 2009 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.chart.factory;

import java.util.LinkedHashMap;

import org.eclipse.birt.chart.computation.IConstants;
import org.eclipse.birt.chart.computation.LegendItemRenderingHints;
import org.eclipse.birt.chart.device.IDisplayServer;
import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.ChartWithoutAxes;
import org.eclipse.birt.chart.model.attribute.Bounds;
import org.eclipse.birt.chart.model.component.Series;
import org.mozilla.javascript.Scriptable;

/**
 * Maintains state information containing the original chart model and runtime
 * built information based on offscreen computations performed using the
 * {@link org.eclipse.birt.chart.factory.Generator#build(IDisplayServer, Chart, Scriptable, Bounds, RunTimeContext)}
 * method.
 */
public final class GeneratedChartState
{

	private final LinkedHashMap<Series, LegendItemRenderingHints> _lhmRenderers;

	private final Object _oComputations;

	private final IDisplayServer _ids;

	private final Chart _cm;

	private final int _iType;

	private final RunTimeContext _rtc;

	/**
	 * A default constructor provided to create an instance internally via the
	 * build process.
	 * 
	 * @param ids
	 *            An instance of the display server used in building the chart
	 * @param cm
	 *            An instance of the chart model for which the chart was built
	 * @param lhmRenderers
	 *            A linked hashmap providing a sorted lookup list for the series
	 *            renderers
	 * @param oComputations
	 *            A computation helper used to build the chart offscreen
	 */
	GeneratedChartState( IDisplayServer ids, Chart cm,
			LinkedHashMap<Series, LegendItemRenderingHints> lhmRenderers,
			RunTimeContext rtc, Object oComputations )
	{
		_lhmRenderers = lhmRenderers;
		_oComputations = oComputations;
		_ids = ids;
		_cm = cm;
		_rtc = rtc;
		if ( cm instanceof ChartWithAxes )
		{
			_iType = Generator.WITH_AXES;
		}
		else if ( cm instanceof ChartWithoutAxes )
		{
			_iType = Generator.WITHOUT_AXES;
		}
		else
		{
			_iType = IConstants.UNDEFINED;
		}
	}

	/**
	 * Returns a sorted lookup list of all series renderers.
	 * 
	 * @return A sorted lookup list of all series renderers.
	 */
	public final LinkedHashMap<Series, LegendItemRenderingHints> getRenderers( )
	{
		return _lhmRenderers;
	}

	/**
	 * Returns an internal class capable of computing the chart content.
	 * 
	 * @return An internal class capable of computing the chart content.
	 */
	public final Object getComputations( )
	{
		return _oComputations;
	}

	/**
	 * Returns an instance of a display server used in building the chart
	 * content.
	 * 
	 * @return An instance of a display server used in building the chart
	 *         content.
	 */
	public final IDisplayServer getDisplayServer( )
	{
		return _ids;
	}

	/**
	 * Returns an instance of the source chart model associated with the built
	 * chart content.
	 * 
	 * @return An instance of the source chart model associated with the built
	 *         chart content.
	 */
	public final Chart getChartModel( )
	{
		return _cm;
	}

	/**
	 * Returns an instance of the runtime context used in building the chart.
	 * 
	 * @return An instance of the runtime context used in building the chart.
	 */
	public final RunTimeContext getRunTimeContext( )
	{
		return _rtc;
	}

	/**
	 * Returns the type of the chart that was built.
	 * 
	 * @return The type of the chart that was built.
	 */
	public final int getType( )
	{
		return _iType;
	}
}