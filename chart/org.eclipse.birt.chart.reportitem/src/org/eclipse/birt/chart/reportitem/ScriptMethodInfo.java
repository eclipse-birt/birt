
package org.eclipse.birt.chart.reportitem;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/*************************************************************************************
 * Copyright (c) 2007 Actuate Corporation and others. All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v2.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-2.0.html
 *
 * Contributors: Actuate Corporation - Initial implementation.
 ************************************************************************************/

public class ScriptMethodInfo extends AbstractScriptMethodInfo {

	private static Map<String, String> sJavaDoc;

	protected ScriptMethodInfo(Method method) {
		super(method);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.chart.reportitem.AbstractScriptMethodInfo#getMethodsJavaDoc(
	 * )
	 */
	@Override
	protected Map<String, String> getMethodsJavaDoc() {
		return sJavaDoc;
	}

	static {
		sJavaDoc = new HashMap<>();
		sJavaDoc.put("beforeDataSetFilled",
				"/**\n" + " * Called before populating the series dataset using the DataSetProcessor.\n" + " *\n"
						+ " * @param series\n" + " *            Series\n" + " * @param idsp\n"
						+ " *            IDataSetProcessor\n" + " * @param icsc\n"
						+ " *            IChartScriptContext\n" + " */\n");

		sJavaDoc.put("afterDataSetFilled",
				"/**\n" + " * Called after populating the series dataset.\n" + " * \n" + " * @param series\n"
						+ " *            Series\n" + " * @param dataSet\n" + " *            DataSet\n"
						+ " * @param icsc\n" + " *            IChartScriptContext\n" + " */\n");

		sJavaDoc.put("beforeGeneration",
				"/**\n" + " * Called before generation of chart model to GeneratedChartState.\n" + " * \n"
						+ " * @param chart\n" + " *            Chart\n" + " * @param icsc\n"
						+ " *            IChartScriptContext\n" + " */\n");

		sJavaDoc.put("afterGeneration",
				"/**\n" + " * Called after generation of chart model to GeneratedChartState.\n" + " * \n"
						+ " * @param gcs\n" + " *            GeneratedChartState\n" + " * @param icsc\n"
						+ " *            IChartScriptContext\n" + " */\n");

		sJavaDoc.put("beforeRendering",
				"/**\n" + " * Called befoer the chart is rendered.\n" + " * \n" + " * @param gcs\n"
						+ " *            GeneratedChartState\n" + " * @param icsc\n"
						+ " *            IChartScriptContext\n" + " */\n");

		sJavaDoc.put("afterRendering",
				"/**\n" + " * Called after the chart is rendered.\n" + " * \n" + " * @param gcs\n"
						+ " *            GeneratedChartState\n" + " * @param icsc\n"
						+ " *            IChartScriptContext\n" + " */\n");

		sJavaDoc.put("beforeDrawBlock",
				"/**\n" + " * Called before drawing each block.\n" + " * \n" + " * @param block\n"
						+ " *            Block\n" + " * @param icsc\n" + " *            IChartScriptContext\n"
						+ " */\n");

		sJavaDoc.put("afterDrawBlock", "/**\n" + " * Called after drawing each block.\n" + " * \n" + " * @param block\n"
				+ " *            Block\n" + " * @param icsc\n" + " *            IChartScriptContext\n" + " */\n");

		sJavaDoc.put("beforeDrawLegendEntry",
				"/**\n" + " * Called before drawing each entry in the legend.\n" + " * \n" + " * @param label\n"
						+ " *            Label\n" + " * @param icsc\n" + " *            IChartScriptContext\n"
						+ " * @deprecated Since 2.2.0\n" + " * 			  use beforeDrawLegendItem( ) instead\n"
						+ " */\n");

		sJavaDoc.put("afterDrawLegendEntry",
				"/**\n" + " * Called after drawing each entry in the legend.\n" + " * \n" + " * @param label\n"
						+ " *            Label\n" + " * @param icsc\n" + " *            IChartScriptContext\n"
						+ " * @deprecated Since 2.2.0 \n" + " * 			  use afterDrawLegendItem( ) instead\n"
						+ " */\n");

		sJavaDoc.put("beforeDrawLegendItem",
				"/**\n" + " * Called before drawing the legend item.\n" + " * \n" + " * @param lerh\n"
						+ " * 			  LegendEntryRenderingHints\n" + " * @param bounds\n"
						+ " * 			  Bounds\n" + " * @param icsc\n" + " * 			  IChartScriptContext\n"
						+ " * @since Version 2.2.0\n" + " */\n");

		sJavaDoc.put("afterDrawLegendItem",
				"/**\n" + " * Called after drawing the legend item.\n" + " * \n" + " * @param lerh\n"
						+ " * 			  LegendEntryRenderingHints\n" + " * @param bounds\n"
						+ " * 			  Bounds\n" + " * @param icsc\n" + " * 			  IChartScriptContext\n"
						+ " * @since Version 2.2.0\n" + " */\n");

		sJavaDoc.put("beforeDrawSeries",
				"/**\n" + " * Called before rendering Series.\n" + " * \n" + " * @param series\n"
						+ " *            Series\n" + " * @param isr\n" + " *            ISeriesRenderer\n"
						+ " * @param icsc\n" + " *            IChartScriptContext\n" + " */\n");

		sJavaDoc.put("afterDrawSeries",
				"/**\n" + " * Called after rendering Series.\n" + " * \n" + " * @param series\n"
						+ " *            Series\n" + " * @param isr\n" + " *            ISeriesRenderer\n"
						+ " * @param icsc\n" + " *            IChartScriptContext\n" + " */\n");

		sJavaDoc.put("beforeDrawSeriesTitle",
				"/**\n" + " * Called before rendering the title of a Series.\n" + " * \n" + " * @param series\n"
						+ " *            Series\n" + " * @param label\n" + " *            Label\n" + " * @param icsc\n"
						+ " *            IChartScriptContext\n" + " */\n");

		sJavaDoc.put("afterDrawSeriesTitle",
				"/**\n" + " * Called after rendering the title of a Series .\n" + " * \n" + " * @param series\n"
						+ " *            Series\n" + " * @param label\n" + " *            Label\n" + " * @param icsc\n"
						+ " *            IChartScriptContext\n" + " */\n");

		sJavaDoc.put("beforeDrawMarker",
				"/**\n" + " * Called before drawing each marker.\n" + " * \n" + " * @param marker\n"
						+ " *            Marker\n" + " * @param dph\n" + " *            DataPointHints\n"
						+ " * @param icsc\n" + " *            IChartScriptContext\n" + " */\n");

		sJavaDoc.put("afterDrawMarker",
				"/**\n" + " * Called after drawing each marker.\n" + " * \n" + " * @param marker\n"
						+ " *            Marker\n" + " * @param dph\n" + " *            DataPointHints\n"
						+ " * @param icsc\n" + " *            IChartScriptContext\n" + " */\n");

		sJavaDoc.put("beforeDrawMarkerLine",
				"/**\n" + " * Called before drawing each marker line in an Axis.\n" + " * \n" + " * @param axis\n"
						+ " *            Axis\n" + " * @param markerLine\n" + " *            MarkerLine\n"
						+ " * @param icsc\n" + " *            IChartScriptContext\n" + " */\n");

		sJavaDoc.put("afterDrawMarkerLine",
				"/**\n" + " * Called after drawing each marker line in an Axis.\n" + " * \n" + " * @param axis\n"
						+ " *            Axis\n" + " * @param markerLine\n" + " *            MarkerLine\n"
						+ " * @param icsc\n" + " *            IChartScriptContext\n" + " */\n");

		sJavaDoc.put("beforeDrawMarkerRange",
				"/**\n" + " * Called before drawing each marker range in an Axis.\n" + " * \n" + " * @param axis\n"
						+ " *            Axis\n" + " * @param markerRange\n" + " *            MarkerRange\n"
						+ " * @param icsc\n" + " *            IChartScriptContext\n" + " */\n");

		sJavaDoc.put("afterDrawMarkerRange",
				"/**\n" + " * Called after drawing each marker range in an Axis.\n" + " * \n" + " * @param axis\n"
						+ " *            Axis\n" + " * @param markerRange\n" + " *            MarkerRange\n"
						+ " * @param icsc\n" + " *            IChartScriptContext\n" + " */\n");

		sJavaDoc.put("beforeDrawDataPoint",
				"/**\n" + " * Called before drawing each datapoint graphical representation or marker.\n" + " * \n"
						+ " * @param dph\n" + " *            DataPointHints\n" + " * @param fill\n"
						+ " *            Fill\n" + " * @param icsc\n" + " *            IChartScriptContext\n"
						+ " */\n");

		sJavaDoc.put("afterDrawDataPoint",
				"/**\n" + " * Called after drawing each datapoint graphical representation or marker.\n" + " * \n"
						+ " * @param dph\n" + " *            DataPointHints\n" + " * @param fill\n"
						+ " *            Fill\n" + " * @param icsc\n" + " *            IChartScriptContext\n"
						+ " */\n");

		sJavaDoc.put("beforeDrawDataPointLabel",
				"/**\n" + " * Called before rendering the label for each datapoint.\n" + " * \n" + " * @param dph\n"
						+ " *            DataPointHints\n" + " * @param label\n" + " *            Label\n"
						+ " * @param icsc\n" + " *            IChartScriptContext\n" + " */\n");

		sJavaDoc.put("afterDrawDataPointLabel",
				"/**\n" + " * Called after rendering the label for each datapoint.\n" + " * \n" + " * @param dph\n"
						+ " *            DataPointHints\n" + " * @param label\n" + " *            Label\n"
						+ " * @param icsc\n" + " *            IChartScriptContext\n" + " */\n");

		sJavaDoc.put("beforeDrawFittingCurve",
				"/**\n" + " * Called before rendering curve fitting.\n" + " * \n" + " * @param curveFitting\n"
						+ " *            CurveFitting\n" + " * @param icsc\n" + " *            IChartScriptContext\n"
						+ " */\n");

		sJavaDoc.put("afterDrawFittingCurve",
				"/**\n" + " * Called after rendering curve fitting.\n" + " * \n" + " * @param curveFitting\n"
						+ " *            CurveFitting\n" + " * @param icsc\n" + " *            IChartScriptContext\n"
						+ " */\n");

		sJavaDoc.put("beforeDrawAxisLabel",
				"/**\n" + " * Called before rendering each label on a given Axis.\n" + " * \n" + " * @param axis\n"
						+ " *            Axis\n" + " * @param label\n" + " *            Label\n" + " * @param icsc\n"
						+ " *            IChartScriptContext\n" + " */\n");

		sJavaDoc.put("afterDrawAxisLabel",
				"/**\n" + " * Called after rendering each label on a given Axis.\n" + " * \n" + " * @param axis\n"
						+ " *            Axis\n" + " * @param label\n" + " *            Label\n" + " * @param icsc\n"
						+ " *            IChartScriptContext\n" + " */\n");

		sJavaDoc.put("beforeDrawAxisTitle",
				"/**\n" + " * Called before rendering the Title of an Axis.\n" + " * \n" + " * @param axis\n"
						+ " *            Axis\n" + " * @param label\n" + " *            Title\n" + " * @param icsc\n"
						+ " *            IChartScriptContext\n" + " */\n");

		sJavaDoc.put("afterDrawAxisTitle",
				"/**\n" + " * Called after rendering the Title of an Axis.\n" + " * \n" + " * @param axis\n"
						+ " *            Axis\n" + " * @param label\n" + " *            Title\n" + " * @param icsc\n"
						+ " *            IChartScriptContext\n" + " */\n");

		sJavaDoc.put("beforeComputations",
				"/**\n" + " * Called before computations of chart model.\n" + " * \n" + " * @param chart\n"
						+ " *            Chart\n" + " * @param plotComputation\n" + " *            PlotComputation\n"
						+ " */\n");

		sJavaDoc.put("afterComputations",
				"/**\n" + " * Called after computations of chart model.\n" + " * \n" + " * @param chart\n"
						+ " *            Chart\n" + " * @param plotComputation\n" + " *            PlotComputation\n"
						+ " */\n");
	}
}
