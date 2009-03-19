
package org.eclipse.birt.chart.reportitem;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.birt.report.model.api.scripts.MethodInfo;

/*************************************************************************************
 * Copyright (c) 2007 Actuate Corporation and others. All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Actuate Corporation - Initial implementation.
 ************************************************************************************/

public class ScriptMethodInfo extends MethodInfo
{

	protected ScriptMethodInfo( Method method )
	{
		super( method );
	}

	public boolean isDeprecated( )
	{
		String javaDoc = getJavaDoc( );
		if ( javaDoc == null )
			return true;
		return getJavaDoc( ).indexOf( "@deprecated" ) != -1; //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.metadata.IMethodInfo#getJavaDoc()
	 */
	public String getJavaDoc( )
	{
		return javaDoc.get( getMethod( ).getName( ) );
	}

	private final static Map<String, String> javaDoc = new HashMap<String, String>( );
	static
	{
		javaDoc.put( "beforeDataSetFilled",
				"/**\n"
						+ " * Called before populating the series dataset using the DataSetProcessor.\n"
						+ " *\n"
						+ " * @param series\n"
						+ " *            Series\n"
						+ " * @param idsp\n"
						+ " *            IDataSetProcessor\n"
						+ " * @param icsc\n"
						+ " *            IChartScriptContext\n"
						+ " */\n" );

		javaDoc.put( "afterDataSetFilled", "/**\n"
				+ " * Called after populating the series dataset.\n"
				+ " * \n"
				+ " * @param series\n"
				+ " *            Series\n"
				+ " * @param dataSet\n"
				+ " *            DataSet\n"
				+ " * @param icsc\n"
				+ " *            IChartScriptContext\n"
				+ " */\n" );

		javaDoc.put( "beforeGeneration",
				"/**\n"
						+ " * Called before generation of chart model to GeneratedChartState.\n"
						+ " * \n"
						+ " * @param chart\n"
						+ " *            Chart\n"
						+ " * @param icsc\n"
						+ " *            IChartScriptContext\n"
						+ " */\n" );

		javaDoc.put( "afterGeneration",
				"/**\n"
						+ " * Called after generation of chart model to GeneratedChartState.\n"
						+ " * \n"
						+ " * @param gcs\n"
						+ " *            GeneratedChartState\n"
						+ " * @param icsc\n"
						+ " *            IChartScriptContext\n"
						+ " */\n" );

		javaDoc.put( "beforeRendering", "/**\n"
				+ " * Called befoer the chart is rendered.\n"
				+ " * \n"
				+ " * @param gcs\n"
				+ " *            GeneratedChartState\n"
				+ " * @param icsc\n"
				+ " *            IChartScriptContext\n"
				+ " */\n" );

		javaDoc.put( "afterRendering", "/**\n"
				+ " * Called after the chart is rendered.\n"
				+ " * \n"
				+ " * @param gcs\n"
				+ " *            GeneratedChartState\n"
				+ " * @param icsc\n"
				+ " *            IChartScriptContext\n"
				+ " */\n" );

		javaDoc.put( "beforeDrawBlock", "/**\n"
				+ " * Called before drawing each block.\n"
				+ " * \n"
				+ " * @param block\n"
				+ " *            Block\n"
				+ " * @param icsc\n"
				+ " *            IChartScriptContext\n"
				+ " */\n" );

		javaDoc.put( "afterDrawBlock", "/**\n"
				+ " * Called after drawing each block.\n"
				+ " * \n"
				+ " * @param block\n"
				+ " *            Block\n"
				+ " * @param icsc\n"
				+ " *            IChartScriptContext\n"
				+ " */\n" );

		javaDoc.put( "beforeDrawLegendEntry", "/**\n"
				+ " * Called before drawing each entry in the legend.\n"
				+ " * \n"
				+ " * @param label\n"
				+ " *            Label\n"
				+ " * @param icsc\n"
				+ " *            IChartScriptContext\n"
				+ " * @deprecated Since 2.2.0\n"
				+ " * 			  use beforeDrawLegendItem( ) instead\n"
				+ " */\n" );

		javaDoc.put( "afterDrawLegendEntry", "/**\n"
				+ " * Called after drawing each entry in the legend.\n"
				+ " * \n"
				+ " * @param label\n"
				+ " *            Label\n"
				+ " * @param icsc\n"
				+ " *            IChartScriptContext\n"
				+ " * @deprecated Since 2.2.0 \n"
				+ " * 			  use afterDrawLegendItem( ) instead\n"
				+ " */\n" );

		javaDoc.put( "beforeDrawLegendItem", "/**\n"
				+ " * Called before drawing the legend item.\n"
				+ " * \n"
				+ " * @param lerh\n"
				+ " * 			  LegendEntryRenderingHints\n"
				+ " * @param bounds\n"
				+ " * 			  Bounds\n"
				+ " * @param icsc\n"
				+ " * 			  IChartScriptContext\n"
				+ " * @since Version 2.2.0\n"
				+ " */\n" );

		javaDoc.put( "afterDrawLegendItem", "/**\n"
				+ " * Called after drawing the legend item.\n"
				+ " * \n"
				+ " * @param lerh\n"
				+ " * 			  LegendEntryRenderingHints\n"
				+ " * @param bounds\n"
				+ " * 			  Bounds\n"
				+ " * @param icsc\n"
				+ " * 			  IChartScriptContext\n"
				+ " * @since Version 2.2.0\n"
				+ " */\n" );

		javaDoc.put( "beforeDrawSeries", "/**\n"
				+ " * Called before rendering Series.\n"
				+ " * \n"
				+ " * @param series\n"
				+ " *            Series\n"
				+ " * @param isr\n"
				+ " *            ISeriesRenderer\n"
				+ " * @param icsc\n"
				+ " *            IChartScriptContext\n"
				+ " */\n" );

		javaDoc.put( "afterDrawSeries", "/**\n"
				+ " * Called after rendering Series.\n"
				+ " * \n"
				+ " * @param series\n"
				+ " *            Series\n"
				+ " * @param isr\n"
				+ " *            ISeriesRenderer\n"
				+ " * @param icsc\n"
				+ " *            IChartScriptContext\n"
				+ " */\n" );

		javaDoc.put( "beforeDrawSeriesTitle", "/**\n"
				+ " * Called before rendering the title of a Series.\n"
				+ " * \n"
				+ " * @param series\n"
				+ " *            Series\n"
				+ " * @param label\n"
				+ " *            Label\n"
				+ " * @param icsc\n"
				+ " *            IChartScriptContext\n"
				+ " */\n" );

		javaDoc.put( "afterDrawSeriesTitle", "/**\n"
				+ " * Called after rendering the title of a Series .\n"
				+ " * \n"
				+ " * @param series\n"
				+ " *            Series\n"
				+ " * @param label\n"
				+ " *            Label\n"
				+ " * @param icsc\n"
				+ " *            IChartScriptContext\n"
				+ " */\n" );

		javaDoc.put( "beforeDrawMarker", "/**\n"
				+ " * Called before drawing each marker.\n"
				+ " * \n"
				+ " * @param marker\n"
				+ " *            Marker\n"
				+ " * @param dph\n"
				+ " *            DataPointHints\n"
				+ " * @param icsc\n"
				+ " *            IChartScriptContext\n"
				+ " */\n" );

		javaDoc.put( "afterDrawMarker", "/**\n"
				+ " * Called after drawing each marker.\n"
				+ " * \n"
				+ " * @param marker\n"
				+ " *            Marker\n"
				+ " * @param dph\n"
				+ " *            DataPointHints\n"
				+ " * @param icsc\n"
				+ " *            IChartScriptContext\n"
				+ " */\n" );

		javaDoc.put( "beforeDrawMarkerLine", "/**\n"
				+ " * Called before drawing each marker line in an Axis.\n"
				+ " * \n"
				+ " * @param axis\n"
				+ " *            Axis\n"
				+ " * @param markerLine\n"
				+ " *            MarkerLine\n"
				+ " * @param icsc\n"
				+ " *            IChartScriptContext\n"
				+ " */\n" );

		javaDoc.put( "afterDrawMarkerLine", "/**\n"
				+ " * Called after drawing each marker line in an Axis.\n"
				+ " * \n"
				+ " * @param axis\n"
				+ " *            Axis\n"
				+ " * @param markerLine\n"
				+ " *            MarkerLine\n"
				+ " * @param icsc\n"
				+ " *            IChartScriptContext\n"
				+ " */\n" );

		javaDoc.put( "beforeDrawMarkerRange", "/**\n"
				+ " * Called before drawing each marker range in an Axis.\n"
				+ " * \n"
				+ " * @param axis\n"
				+ " *            Axis\n"
				+ " * @param markerRange\n"
				+ " *            MarkerRange\n"
				+ " * @param icsc\n"
				+ " *            IChartScriptContext\n"
				+ " */\n" );

		javaDoc.put( "afterDrawMarkerRange", "/**\n"
				+ " * Called after drawing each marker range in an Axis.\n"
				+ " * \n"
				+ " * @param axis\n"
				+ " *            Axis\n"
				+ " * @param markerRange\n"
				+ " *            MarkerRange\n"
				+ " * @param icsc\n"
				+ " *            IChartScriptContext\n"
				+ " */\n" );

		javaDoc.put( "beforeDrawDataPoint",
				"/**\n"
						+ " * Called before drawing each datapoint graphical representation or marker.\n"
						+ " * \n"
						+ " * @param dph\n"
						+ " *            DataPointHints\n"
						+ " * @param fill\n"
						+ " *            Fill\n"
						+ " * @param icsc\n"
						+ " *            IChartScriptContext\n"
						+ " */\n" );

		javaDoc.put( "afterDrawDataPoint",
				"/**\n"
						+ " * Called after drawing each datapoint graphical representation or marker.\n"
						+ " * \n"
						+ " * @param dph\n"
						+ " *            DataPointHints\n"
						+ " * @param fill\n"
						+ " *            Fill\n"
						+ " * @param icsc\n"
						+ " *            IChartScriptContext\n"
						+ " */\n" );

		javaDoc.put( "beforeDrawDataPointLabel", "/**\n"
				+ " * Called before rendering the label for each datapoint.\n"
				+ " * \n"
				+ " * @param dph\n"
				+ " *            DataPointHints\n"
				+ " * @param label\n"
				+ " *            Label\n"
				+ " * @param icsc\n"
				+ " *            IChartScriptContext\n"
				+ " */\n" );

		javaDoc.put( "afterDrawDataPointLabel", "/**\n"
				+ " * Called after rendering the label for each datapoint.\n"
				+ " * \n"
				+ " * @param dph\n"
				+ " *            DataPointHints\n"
				+ " * @param label\n"
				+ " *            Label\n"
				+ " * @param icsc\n"
				+ " *            IChartScriptContext\n"
				+ " */\n" );

		javaDoc.put( "beforeDrawFittingCurve", "/**\n"
				+ " * Called before rendering curve fitting.\n"
				+ " * \n"
				+ " * @param curveFitting\n"
				+ " *            CurveFitting\n"
				+ " * @param icsc\n"
				+ " *            IChartScriptContext\n"
				+ " */\n" );

		javaDoc.put( "afterDrawFittingCurve", "/**\n"
				+ " * Called after rendering curve fitting.\n"
				+ " * \n"
				+ " * @param curveFitting\n"
				+ " *            CurveFitting\n"
				+ " * @param icsc\n"
				+ " *            IChartScriptContext\n"
				+ " */\n" );

		javaDoc.put( "beforeDrawAxisLabel", "/**\n"
				+ " * Called before rendering each label on a given Axis.\n"
				+ " * \n"
				+ " * @param axis\n"
				+ " *            Axis\n"
				+ " * @param label\n"
				+ " *            Label\n"
				+ " * @param icsc\n"
				+ " *            IChartScriptContext\n"
				+ " */\n" );

		javaDoc.put( "afterDrawAxisLabel", "/**\n"
				+ " * Called after rendering each label on a given Axis.\n"
				+ " * \n"
				+ " * @param axis\n"
				+ " *            Axis\n"
				+ " * @param label\n"
				+ " *            Label\n"
				+ " * @param icsc\n"
				+ " *            IChartScriptContext\n"
				+ " */\n" );

		javaDoc.put( "beforeDrawAxisTitle", "/**\n"
				+ " * Called before rendering the Title of an Axis.\n"
				+ " * \n"
				+ " * @param axis\n"
				+ " *            Axis\n"
				+ " * @param label\n"
				+ " *            Title\n"
				+ " * @param icsc\n"
				+ " *            IChartScriptContext\n"
				+ " */\n" );

		javaDoc.put( "afterDrawAxisTitle", "/**\n"
				+ " * Called after rendering the Title of an Axis.\n"
				+ " * \n"
				+ " * @param axis\n"
				+ " *            Axis\n"
				+ " * @param label\n"
				+ " *            Title\n"
				+ " * @param icsc\n"
				+ " *            IChartScriptContext\n"
				+ " */\n" );

		javaDoc.put( "beforeComputations", "/**\n"
				+ " * Called before computations of chart model.\n"
				+ " * \n"
				+ " * @param chart\n"
				+ " *            Chart\n"
				+ " * @param plotComputation\n"
				+ " *            PlotComputation\n"
				+ " */\n" );

		javaDoc.put( "afterComputations", "/**\n"
				+ " * Called after computations of chart model.\n"
				+ " * \n"
				+ " * @param chart\n"
				+ " *            Chart\n"
				+ " * @param plotComputation\n"
				+ " *            PlotComputation\n"
				+ " */\n" );
	}
}
