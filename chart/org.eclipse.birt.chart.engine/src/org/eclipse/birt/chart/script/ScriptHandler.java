/***********************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.chart.script;

import java.lang.reflect.Method;
import java.util.Map;

import org.eclipse.birt.chart.computation.DataPointHints;
import org.eclipse.birt.chart.computation.LegendEntryRenderingHints;
import org.eclipse.birt.chart.computation.PlotComputation;
import org.eclipse.birt.chart.datafeed.IDataSetProcessor;
import org.eclipse.birt.chart.factory.GeneratedChartState;
import org.eclipse.birt.chart.log.ILogger;
import org.eclipse.birt.chart.log.Logger;
import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.IChartObject;
import org.eclipse.birt.chart.model.attribute.Bounds;
import org.eclipse.birt.chart.model.attribute.Fill;
import org.eclipse.birt.chart.model.component.Axis;
import org.eclipse.birt.chart.model.component.CurveFitting;
import org.eclipse.birt.chart.model.component.Label;
import org.eclipse.birt.chart.model.component.MarkerLine;
import org.eclipse.birt.chart.model.component.MarkerRange;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.data.DataSet;
import org.eclipse.birt.chart.model.layout.Block;
import org.eclipse.birt.chart.render.ISeriesRenderer;
import org.eclipse.birt.chart.util.ChartUtil;
import org.eclipse.birt.chart.util.SecurityUtil;

/**
 * This class forms the basis of script handling in the charting library. It
 * creates a default scope and/or subclasses from a predefined scope. It also
 * provides convenience methods for execution of functions defined in the scope.
 * 
 * @see IChartEventHandler
 */
public final class ScriptHandler extends AbstractScriptHandler<IChartEventHandler> {

	private static final long serialVersionUID = 1L;

	/**
	 * @deprecated This is kept for backward compatibility only.
	 */
	public static final String START_GENERATION = "startGeneration"; //$NON-NLS-1$
	/**
	 * @deprecated This is kept for backward compatibility only.
	 */
	public static final String FINISH_GENERATION = "finishGeneration"; //$NON-NLS-1$
	/**
	 * @deprecated This is kept for backward compatibility only.
	 */
	public static final String BEFORE_LAYOUT = "beforeLayout"; //$NON-NLS-1$
	/**
	 * @deprecated This is kept for backward compatibility only.
	 */
	public static final String AFTER_LAYOUT = "afterLayout"; //$NON-NLS-1$

	public static final String BEFORE_COMPUTATIONS = "beforeComputations"; //$NON-NLS-1$

	public static final String AFTER_COMPUTATIONS = "afterComputations"; //$NON-NLS-1$
	/**
	 * @deprecated This is kept for backward compatibility only.
	 */
	public static final String START_RENDERING = "startRendering"; //$NON-NLS-1$
	/**
	 * @deprecated This is kept for backward compatibility only.
	 */
	public static final String FINISH_RENDERING = "finishRendering"; //$NON-NLS-1$
	/**
	 * @deprecated This is kept for backward compatibility only.
	 */
	public static final String START_COMPUTE_SERIES = "startComputeSeries"; //$NON-NLS-1$
	/**
	 * @deprecated This is kept for backward compatibility only.
	 */
	public static final String FINISH_COMPUTE_SERIES = "finishComputeSeries"; //$NON-NLS-1$
	/**
	 * @deprecated This is kept for backward compatibility only.
	 */
	public static final String BEFORE_DRAW_ELEMENT = "beforeDrawElement"; //$NON-NLS-1$
	/**
	 * @deprecated This is kept for backward compatibility only.
	 */
	public static final String AFTER_DRAW_ELEMENT = "afterDrawElement"; //$NON-NLS-1$

	// not supported yet
	// public static final String ON_PREPARE = "onPrepare"; //$NON-NLS-1$
	public static final String BEFORE_DRAW_BLOCK = "beforeDrawBlock"; //$NON-NLS-1$

	public static final String AFTER_DRAW_BLOCK = "afterDrawBlock"; //$NON-NLS-1$

	/**
	 * @deprecated This is kept for backward compatibility only.
	 */
	public static final String BEFORE_DRAW_LEGEND_ENTRY = "beforeDrawLegendEntry"; //$NON-NLS-1$

	/**
	 * @deprecated This is kept for backward compatibility only.
	 */
	public static final String AFTER_DRAW_LEGEND_ENTRY = "afterDrawLegendEntry"; //$NON-NLS-1$

	public static final String BEFORE_DRAW_LEGEND_ITEM = "beforeDrawLegendItem"; //$NON-NLS-1$

	public static final String AFTER_DRAW_LEGEND_ITEM = "afterDrawLegendItem"; //$NON-NLS-1$

	public static final String BEFORE_DRAW_SERIES = "beforeDrawSeries"; //$NON-NLS-1$

	public static final String AFTER_DRAW_SERIES = "afterDrawSeries"; //$NON-NLS-1$

	public static final String BEFORE_DRAW_SERIES_TITLE = "beforeDrawSeriesTitle"; //$NON-NLS-1$

	public static final String AFTER_DRAW_SERIES_TITLE = "afterDrawSeriesTitle"; //$NON-NLS-1$

	public static final String BEFORE_DRAW_MARKER = "beforeDrawMarker"; //$NON-NLS-1$

	public static final String AFTER_DRAW_MARKER = "afterDrawMarker"; //$NON-NLS-1$

	public static final String BEFORE_DRAW_MARKER_LINE = "beforeDrawMarkerLine"; //$NON-NLS-1$

	public static final String AFTER_DRAW_MARKER_LINE = "afterDrawMarkerLine"; //$NON-NLS-1$

	public static final String BEFORE_DRAW_MARKER_RANGE = "beforeDrawMarkerRange"; //$NON-NLS-1$

	public static final String AFTER_DRAW_MARKER_RANGE = "afterDrawMarkerRange"; //$NON-NLS-1$

	public static final String BEFORE_DRAW_DATA_POINT = "beforeDrawDataPoint"; //$NON-NLS-1$

	public static final String AFTER_DRAW_DATA_POINT = "afterDrawDataPoint"; //$NON-NLS-1$

	public static final String BEFORE_DRAW_DATA_POINT_LABEL = "beforeDrawDataPointLabel"; //$NON-NLS-1$

	public static final String AFTER_DRAW_DATA_POINT_LABEL = "afterDrawDataPointLabel"; //$NON-NLS-1$

	public static final String BEFORE_DRAW_FITTING_CURVE = "beforeDrawFittingCurve"; //$NON-NLS-1$

	public static final String AFTER_DRAW_FITTING_CURVE = "afterDrawFittingCurve"; //$NON-NLS-1$

	public static final String BEFORE_DRAW_AXIS_LABEL = "beforeDrawAxisLabel"; //$NON-NLS-1$

	public static final String AFTER_DRAW_AXIS_LABEL = "afterDrawAxisLabel"; //$NON-NLS-1$

	public static final String BEFORE_DRAW_AXIS_TITLE = "beforeDrawAxisTitle"; //$NON-NLS-1$

	public static final String AFTER_DRAW_AXIS_TITLE = "afterDrawAxisTitle"; //$NON-NLS-1$

	/**
	 * The pre-defined chart variable name to access base category data.
	 */
	public static final String BASE_VALUE = "categoryData"; //$NON-NLS-1$

	/**
	 * The pre-defined chart variable name to access base category data.
	 */
	public static final String VARIABLE_CATEGORY = BASE_VALUE;

	/**
	 * The pre-defined chart variable name to access orthogonal value data.
	 */
	public static final String ORTHOGONAL_VALUE = "valueData"; //$NON-NLS-1$

	/**
	 * The pre-defined chart variable name to access orthogonal value data.
	 */
	public static final String VARIABLE_VALUE = ORTHOGONAL_VALUE;

	/**
	 * The pre-defined chart variable name to access series name.
	 */
	public static final String SERIES_VALUE = "valueSeriesName"; //$NON-NLS-1$

	/**
	 * The pre-defined chart variable name to access series name.
	 */
	public static final String VARIABLE_SERIES = SERIES_VALUE;

	/** The variable names for highlight and toggle visibility functions. */
	public static final String ID = "id";//$NON-NLS-1$

	public static final String COMP_LIST = "compList";//$NON-NLS-1$

	public static final String LABEL_LIST = "labelList";//$NON-NLS-1$

	private transient Chart cmDesignTime = null;

	private transient Chart cmRunTime = null;

	private transient GeneratedChartState gcs = null;

	private static Map<String, Method> sJavaFunctoinMap = null;
	static {
		sJavaFunctoinMap = ChartUtil.newHashMap();
		// init java function name lookup table.
		Method[] ms = SecurityUtil.getMethods(IChartEventHandler.class);
		for (int i = 0; i < ms.length; i++) {
			sJavaFunctoinMap.put(ms[i].getName(), ms[i]);
		}
	}

	private static ILogger sLogger = Logger.getLogger("org.eclipse.birt.chart.engine/model"); //$NON-NLS-1$ ;

	/**
	 * The constructor.
	 */
	public ScriptHandler() {
		super();
	}

	/**
	 * @deprecated Not used anymore. This is kept for backward compatibility only.
	 * @return An instance of the chart model used at design time
	 */
	public Object jsFunction_getDesignTimeModel() {
		return cmDesignTime;
	}

	/**
	 * @deprecated Call IChartScriptContext.getChartInstance() instead. This is kept
	 *             for backward compatibility only.
	 * @return An instance of the chart model used at run time
	 */
	public Object jsFunction_getRunTimeModel() {
		return cmRunTime;
	}

	/**
	 * @deprecated Not used anymore. This is kept for backward compatibility only.
	 * @return An instance of the run time model coupled with a computations and
	 *         series filled with datasets
	 */
	public Object jsFunction_getGeneratedChartState() {
		return gcs;
	}

	/**
	 * @deprecated Call IChartScriptContext.getLocale() instead. This is kept for
	 *             backward compatibility only.
	 * @return The locale associated with the generation request
	 */
	public final Object jsFunction_getLocale() {
		return lcl;
	}

	/**
	 * @deprecated Note used anymore. This is kept for backward compatibility only.
	 * @param eo An EMF generated model object to be cloned
	 * @return A cloned instance of the specified EMF generated model object
	 */
	public final Object jsFunction_clone(Object eo) {
		if (eo instanceof IChartObject) {
			return ((IChartObject) eo).copyInstance();
		} else {
			return null;
		}
	}

	/**
	 * @deprecated Not used anymore. This is kept for backward compatibility only.
	 * @param cm
	 */
	public final void setDesignTimeModel(Chart cmDesignTime) {
		this.cmDesignTime = cmDesignTime;
	}

	/**
	 * @deprecated Not used anymore. Use IChartScriptContext to store the run-time
	 *             model now. This is kept for backward compatibility only.
	 * @param cm
	 */
	public final void setRunTimeModel(Chart cmRunTime) {
		this.cmRunTime = cmRunTime;
	}

	/**
	 * @deprecated Not used anymore. This is kept for backward compatibility only.
	 * @param gcs
	 */
	public final void setGeneratedChartState(GeneratedChartState gcs) {
		this.gcs = gcs;
	}

	/**
	 * @param functionName
	 * @param arguments
	 */
	protected boolean callRegularJavaFunction(String functionName, Object[] arguments) {
		boolean called = false;
		// use regular interface call instead of reflection to gain performance.
		/*
		 * if ( ScriptHandler.ON_PREPARE.equals( name ) ) { javahandler.onPrepare(
		 * (Chart) tmpArgs[0], (IChartScriptContext) tmpArgs[1] ); } else
		 */// not supported yet
		if (ScriptHandler.BEFORE_DATA_SET_FILLED.equals(functionName)) {
			javahandler.beforeDataSetFilled((Series) arguments[0], (IDataSetProcessor) arguments[1],
					(IChartScriptContext) arguments[2]);
			called = true;
		} else if (ScriptHandler.AFTER_DATA_SET_FILLED.equals(functionName)) {
			javahandler.afterDataSetFilled((Series) arguments[0], (DataSet) arguments[1],
					(IChartScriptContext) arguments[2]);
			called = true;
		} else if (ScriptHandler.BEFORE_GENERATION.equals(functionName)) {
			javahandler.beforeGeneration((Chart) arguments[0], (IChartScriptContext) arguments[1]);
			called = true;
		} else if (ScriptHandler.AFTER_GENERATION.equals(functionName)) {
			javahandler.afterGeneration((GeneratedChartState) arguments[0], (IChartScriptContext) arguments[1]);
			called = true;
		} else if (ScriptHandler.BEFORE_COMPUTATIONS.equals(functionName)) {
			javahandler.beforeComputations((Chart) arguments[0], (PlotComputation) arguments[1]);
			called = true;
		} else if (ScriptHandler.AFTER_COMPUTATIONS.equals(functionName)) {
			javahandler.afterComputations((Chart) arguments[0], (PlotComputation) arguments[1]);
			called = true;
		} else if (ScriptHandler.BEFORE_RENDERING.equals(functionName)) {
			javahandler.beforeRendering((GeneratedChartState) arguments[0], (IChartScriptContext) arguments[1]);
			called = true;
		} else if (ScriptHandler.AFTER_RENDERING.equals(functionName)) {
			javahandler.afterRendering((GeneratedChartState) arguments[0], (IChartScriptContext) arguments[1]);
			called = true;
		} else if (ScriptHandler.BEFORE_DRAW_BLOCK.equals(functionName)) {
			javahandler.beforeDrawBlock((Block) arguments[0], (IChartScriptContext) arguments[1]);
			called = true;
		} else if (ScriptHandler.AFTER_DRAW_BLOCK.equals(functionName)) {
			javahandler.afterDrawBlock((Block) arguments[0], (IChartScriptContext) arguments[1]);
			called = true;
		} else if (ScriptHandler.BEFORE_DRAW_LEGEND_ENTRY.equals(functionName)) {
			javahandler.beforeDrawLegendEntry((Label) arguments[0], (IChartScriptContext) arguments[1]);
			called = true;
		} else if (ScriptHandler.AFTER_DRAW_LEGEND_ENTRY.equals(functionName)) {
			javahandler.afterDrawLegendEntry((Label) arguments[0], (IChartScriptContext) arguments[1]);
			called = true;
		} else if (ScriptHandler.BEFORE_DRAW_LEGEND_ITEM.equals(functionName)) {
			javahandler.beforeDrawLegendItem((LegendEntryRenderingHints) arguments[0], (Bounds) arguments[1],
					(IChartScriptContext) arguments[2]);
			called = true;
		} else if (ScriptHandler.AFTER_DRAW_LEGEND_ITEM.equals(functionName)) {
			javahandler.afterDrawLegendItem((LegendEntryRenderingHints) arguments[0], (Bounds) arguments[1],
					(IChartScriptContext) arguments[2]);
			called = true;
		} else if (ScriptHandler.BEFORE_DRAW_SERIES.equals(functionName)) {
			javahandler.beforeDrawSeries((Series) arguments[0], (ISeriesRenderer) arguments[1],
					(IChartScriptContext) arguments[2]);
			called = true;
		} else if (ScriptHandler.AFTER_DRAW_SERIES.equals(functionName)) {
			javahandler.afterDrawSeries((Series) arguments[0], (ISeriesRenderer) arguments[1],
					(IChartScriptContext) arguments[2]);
			called = true;
		} else if (ScriptHandler.BEFORE_DRAW_SERIES_TITLE.equals(functionName)) {
			javahandler.beforeDrawSeriesTitle((Series) arguments[0], (Label) arguments[1],
					(IChartScriptContext) arguments[2]);
			called = true;
		} else if (ScriptHandler.AFTER_DRAW_SERIES_TITLE.equals(functionName)) {
			javahandler.afterDrawSeriesTitle((Series) arguments[0], (Label) arguments[1],
					(IChartScriptContext) arguments[2]);
			called = true;
		} else if (ScriptHandler.BEFORE_DRAW_MARKER_LINE.equals(functionName)) {
			javahandler.beforeDrawMarkerLine((Axis) arguments[0], (MarkerLine) arguments[1],
					(IChartScriptContext) arguments[2]);
			called = true;
		} else if (ScriptHandler.AFTER_DRAW_MARKER_LINE.equals(functionName)) {
			javahandler.afterDrawMarkerLine((Axis) arguments[0], (MarkerLine) arguments[1],
					(IChartScriptContext) arguments[2]);
			called = true;
		} else if (ScriptHandler.BEFORE_DRAW_MARKER_RANGE.equals(functionName)) {
			javahandler.beforeDrawMarkerRange((Axis) arguments[0], (MarkerRange) arguments[1],
					(IChartScriptContext) arguments[2]);
			called = true;
		} else if (ScriptHandler.AFTER_DRAW_MARKER_RANGE.equals(functionName)) {
			javahandler.afterDrawMarkerRange((Axis) arguments[0], (MarkerRange) arguments[1],
					(IChartScriptContext) arguments[2]);
			called = true;
		} else if (ScriptHandler.BEFORE_DRAW_DATA_POINT.equals(functionName)) {
			javahandler.beforeDrawDataPoint((DataPointHints) arguments[0], (Fill) arguments[1],
					(IChartScriptContext) arguments[2]);
			called = true;
		} else if (ScriptHandler.AFTER_DRAW_DATA_POINT.equals(functionName)) {
			javahandler.afterDrawDataPoint((DataPointHints) arguments[0], (Fill) arguments[1],
					(IChartScriptContext) arguments[2]);
			called = true;
		} else if (ScriptHandler.BEFORE_DRAW_DATA_POINT_LABEL.equals(functionName)) {
			javahandler.beforeDrawDataPointLabel((DataPointHints) arguments[0], (Label) arguments[1],
					(IChartScriptContext) arguments[2]);
			called = true;
		} else if (ScriptHandler.AFTER_DRAW_DATA_POINT_LABEL.equals(functionName)) {
			javahandler.afterDrawDataPointLabel((DataPointHints) arguments[0], (Label) arguments[1],
					(IChartScriptContext) arguments[2]);
			called = true;
		} else if (ScriptHandler.BEFORE_DRAW_FITTING_CURVE.equals(functionName)) {
			javahandler.beforeDrawFittingCurve((CurveFitting) arguments[0], (IChartScriptContext) arguments[1]);
			called = true;
		} else if (ScriptHandler.AFTER_DRAW_FITTING_CURVE.equals(functionName)) {
			javahandler.afterDrawFittingCurve((CurveFitting) arguments[0], (IChartScriptContext) arguments[1]);
			called = true;
		} else if (ScriptHandler.BEFORE_DRAW_AXIS_LABEL.equals(functionName)) {
			javahandler.beforeDrawAxisLabel((Axis) arguments[0], (Label) arguments[1],
					(IChartScriptContext) arguments[2]);
			called = true;
		} else if (ScriptHandler.AFTER_DRAW_AXIS_LABEL.equals(functionName)) {
			javahandler.afterDrawAxisLabel((Axis) arguments[0], (Label) arguments[1],
					(IChartScriptContext) arguments[2]);
			called = true;
		} else if (ScriptHandler.BEFORE_DRAW_AXIS_TITLE.equals(functionName)) {
			javahandler.beforeDrawAxisTitle((Axis) arguments[0], (Label) arguments[1],
					(IChartScriptContext) arguments[2]);
			called = true;
		} else if (ScriptHandler.AFTER_DRAW_AXIS_TITLE.equals(functionName)) {
			javahandler.afterDrawAxisTitle((Axis) arguments[0], (Label) arguments[1],
					(IChartScriptContext) arguments[2]);
			called = true;
		}

		return called;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.script.AbstractScriptHandler#getLogger()
	 */
	@Override
	protected ILogger getLogger() {
		return sLogger;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.chart.script.AbstractScriptHandler#getEventHandlerClass()
	 */
	@Override
	protected Class getEventHandlerClass() {
		return IChartEventHandler.class;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.script.AbstractScriptHandler#getJavaFunctionMap()
	 */
	@Override
	protected Map<String, Method> getJavaFunctionMap() {
		return sJavaFunctoinMap;
	}
}
