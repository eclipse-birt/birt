/***********************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
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

package org.eclipse.birt.chart.factory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Stack;

import org.eclipse.birt.chart.computation.ChartComputationFactory;
import org.eclipse.birt.chart.computation.GObjectFactory;
import org.eclipse.birt.chart.computation.IGObjectFactory;
import org.eclipse.birt.chart.computation.LegendItemRenderingHints;
import org.eclipse.birt.chart.computation.PlotComputation;
import org.eclipse.birt.chart.computation.withaxes.PlotWith2DAxes;
import org.eclipse.birt.chart.computation.withaxes.PlotWith3DAxes;
import org.eclipse.birt.chart.computation.withoutaxes.PlotWithoutAxes;
import org.eclipse.birt.chart.device.IDeviceRenderer;
import org.eclipse.birt.chart.device.IDisplayServer;
import org.eclipse.birt.chart.engine.i18n.Messages;
import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.factory.RunTimeContext.StateKey;
import org.eclipse.birt.chart.internal.datafeed.DataProcessor;
import org.eclipse.birt.chart.internal.factory.SqlDataRowEvaluator;
import org.eclipse.birt.chart.internal.layout.LayoutManager;
import org.eclipse.birt.chart.log.ILogger;
import org.eclipse.birt.chart.log.Logger;
import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.ChartWithoutAxes;
import org.eclipse.birt.chart.model.attribute.Anchor;
import org.eclipse.birt.chart.model.attribute.Bounds;
import org.eclipse.birt.chart.model.attribute.ChartDimension;
import org.eclipse.birt.chart.model.attribute.ColorDefinition;
import org.eclipse.birt.chart.model.attribute.Fill;
import org.eclipse.birt.chart.model.attribute.FontDefinition;
import org.eclipse.birt.chart.model.attribute.Image;
import org.eclipse.birt.chart.model.attribute.Insets;
import org.eclipse.birt.chart.model.attribute.LineAttributes;
import org.eclipse.birt.chart.model.attribute.Position;
import org.eclipse.birt.chart.model.attribute.Size;
import org.eclipse.birt.chart.model.attribute.StyledComponent;
import org.eclipse.birt.chart.model.attribute.Text;
import org.eclipse.birt.chart.model.component.Axis;
import org.eclipse.birt.chart.model.component.ComponentPackage;
import org.eclipse.birt.chart.model.component.Label;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.data.Query;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.model.layout.Block;
import org.eclipse.birt.chart.model.layout.Legend;
import org.eclipse.birt.chart.model.layout.Plot;
import org.eclipse.birt.chart.model.layout.TitleBlock;
import org.eclipse.birt.chart.model.type.TypePackage;
import org.eclipse.birt.chart.model.util.ChartDefaultValueUtil;
import org.eclipse.birt.chart.model.util.ChartValueUpdater;
import org.eclipse.birt.chart.plugin.ChartEnginePlugin;
import org.eclipse.birt.chart.render.BaseRenderer;
import org.eclipse.birt.chart.render.DeferredCache;
import org.eclipse.birt.chart.render.DeferredCacheManager;
import org.eclipse.birt.chart.script.ChartScriptContext;
import org.eclipse.birt.chart.script.IChartScriptContext;
import org.eclipse.birt.chart.script.IExternalContext;
import org.eclipse.birt.chart.script.IScriptClassLoader;
import org.eclipse.birt.chart.script.ScriptHandler;
import org.eclipse.birt.chart.style.IStyle;
import org.eclipse.birt.chart.style.IStyleProcessor;
import org.eclipse.birt.chart.style.SimpleProcessor;
import org.eclipse.birt.chart.style.SimpleStyle;
import org.eclipse.birt.chart.util.ChartUtil;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.mozilla.javascript.Scriptable;

import com.ibm.icu.util.ULocale;

/**
 * Provides an entry point into building a chart for a given model. It is
 * implemented as a singleton and does not maintain any state information hence
 * allowing multi-threaded requests for a single generator instance.
 */
public final class Generator implements IGenerator {

	/**
	 * An internal style processor.
	 */
	private IStyleProcessor implicitProcessor;

	/**
	 * The internal singleton Generator reference created lazily.
	 */
	private static Generator g = null;

	private static ILogger logger = Logger.getLogger("org.eclipse.birt.chart.engine/factory"); //$NON-NLS-1$

	private static final IGObjectFactory goFactory = GObjectFactory.instance();

	/**
	 * A private constructor.
	 */
	private Generator() {
		implicitProcessor = SimpleProcessor.instance();
	}

	/**
	 * Returns a singleton instance of the chart generator.
	 * 
	 * @return A singleton instance for the chart generator.
	 */
	public static synchronized final Generator instance() {
		if (g == null) {
			g = new Generator();
		}
		return g;
	}

	/**
	 * Prepare all default styles for various StyledComponent.
	 * 
	 * @param model
	 * @param externalProcessor
	 * @param rtc               runtime context.
	 */
	public final void prepareStyles(Chart model, IStyleProcessor externalProcessor, RunTimeContext rtc) {
		boolean updatedModel = false;
		if (externalProcessor != null) {
			updatedModel = externalProcessor.updateChart(model, null);
		}

		// If chart model is not updated by external processor, use default
		// value of chart to update current model.
		if (!updatedModel) {
			new ChartValueUpdater().update(model, null);
		}

		boolean needInheritingStyles = true;
		// Process styles for the whole chart model at first
		if (externalProcessor != null) {
			externalProcessor.processStyle(model);
			needInheritingStyles = externalProcessor.needInheritingStyles();
		}

		if (needInheritingStyles) {
			updateWithInhertingtyles(model, externalProcessor);
		} else {
			// If no need to inherit styles, just set inherited styles into a
			// default value chart, user can get these styles from default value
			// chart later.
			if (rtc != null) {
				Chart defChart = rtc.getDefaultValueChart();
				if (rtc.getDefaultValueChart() == null) {
					defChart = ChartDefaultValueUtil.createDefaultValueChartInstance(model);
					rtc.setDefaultValueChart(defChart);
				}
				updateWithInhertingtyles(defChart, externalProcessor);
			}
		}

		// Still set default value chart instance to avoid null.
		if (rtc != null && rtc.getDefaultValueChart() == null) {
			rtc.setDefaultValueChart(ChartDefaultValueUtil.createDefaultValueChartInstance(model));
		}
	}

	/**
	 * Prepare all default styles for various StyledComponent.
	 * 
	 * @param model
	 * @param externalProcessor
	 */
	public final void prepareStyles(Chart model, IStyleProcessor externalProcessor) {
		prepareStyles(model, externalProcessor, null);
	}

	/**
	 * Update chart UI attributes with inherited styles if those UI attributes don't
	 * be set or updated.
	 * 
	 * @param model
	 * @param externalProcessor
	 */
	protected void updateWithInhertingtyles(Chart model, IStyleProcessor externalProcessor) {
		Stack<StyledComponent> token = new Stack<StyledComponent>();

		token.push(StyledComponent.CHART_ALL_LITERAL);

		prepareComponent(model, token, model, externalProcessor);

		token.clear();
	}

	private void prepareComponent(Chart model, Stack<StyledComponent> token, EObject component,
			IStyleProcessor externalProcessor) {
		// check and apply styles
		StyledComponent currentToken = getStyledComponent(component);

		boolean pushed = false;

		if (currentToken != null) {
			token.push(currentToken);
			pushed = true;
		}

		applyStyles(model, token.peek(), component, externalProcessor);

		// prepare children
		for (Iterator<EObject> itr = component.eContents().iterator(); itr.hasNext();) {
			prepareComponent(model, token, itr.next(), externalProcessor);
		}

		if (pushed && !token.empty()) {
			token.pop();
		}
	}

	private StyledComponent getStyledComponent(EObject obj) {
		if (obj instanceof Block && obj.eContainer() instanceof Chart) {
			return StyledComponent.CHART_BACKGROUND_LITERAL;
		}
		if (obj instanceof Plot) {
			return StyledComponent.PLOT_BACKGROUND_LITERAL;
		}
		if (obj instanceof Legend) {
			return StyledComponent.LEGEND_BACKGROUND_LITERAL;
		}
		if (obj instanceof TitleBlock) {
			return StyledComponent.CHART_TITLE_LITERAL;
		}
		if (obj instanceof Label) {
			if (obj.eContainer() instanceof Axis) {
				if (obj.eContainmentFeature().getFeatureID() == ComponentPackage.AXIS__TITLE) {
					return StyledComponent.AXIS_TITLE_LITERAL;
				} else if (obj.eContainmentFeature().getFeatureID() == ComponentPackage.AXIS__LABEL) {
					return StyledComponent.AXIS_LABEL_LITERAL;
				}
			}
			if (obj.eContainer() instanceof Series) {
				if (obj.eContainmentFeature().getFeatureID() == ComponentPackage.SERIES__LABEL) {
					return StyledComponent.SERIES_LABEL_LITERAL;
				}
				if (obj.eContainmentFeature().getFeatureID() == TypePackage.PIE_SERIES__TITLE) {
					return StyledComponent.SERIES_TITLE_LITERAL;
				}
			}
		}
		if (obj instanceof Text) {
			if (obj.eContainer() instanceof Legend) {
				return StyledComponent.LEGEND_LABEL_LITERAL;
			}
		}
		if (obj instanceof LineAttributes && obj.eContainer() instanceof Axis) {
			return StyledComponent.AXIS_LINE_LITERAL;
		}

		return null;
	}

	private void applyStyles(Chart model, StyledComponent type, EObject component, IStyleProcessor externalProcessor) {
		if (component instanceof Block) {
			// only apply to chart block.
			if (component.eContainer() instanceof Chart) {
				IStyle style = getMingledStyle(model, type, externalProcessor);

				// apply background.
				ColorDefinition newBackcolor = style.getBackgroundColor();
				Image newBackimage = style.getBackgroundImage();

				Fill background = ((Block) component).getBackground();

				if (background == null) {
					if (newBackcolor != null) {
						((Block) component).setBackground(newBackcolor);
					} else {
						if (implicitProcessor.getDefaultBackgroundColor() != null) {
							((Block) component).setBackground(implicitProcessor.getDefaultBackgroundColor());
						} else {
							((Block) component).setBackground(goFactory.WHITE());
						}
					}
					if (newBackimage != null) {
						((Block) component).setBackground(newBackimage);
					}
				}

				// apply padding.
				Insets ins = ((Block) component).getInsets();
				Insets padding = style.getPadding();

				if (padding != null) {
					if (ins == null) {
						ins = goFactory.createInsets(0, 0, 0, 0);
						((Block) component).setInsets(ins);
						// Set the outside padding size directly
						ins.setTop(padding.getTop());
						ins.setLeft(padding.getLeft());
						ins.setBottom(padding.getBottom());
						ins.setRight(padding.getRight());
					}
				}
			}
		}
		// check Text items.
		else if (component instanceof Text) {
			IStyle style = getMingledStyle(model, type, externalProcessor);
			Text text = (Text) component;

			if (text.getFont() == null) {
				text.setFont(style.getFont());
			} else {
				FontDefinition newFont = style.getFont();
				FontDefinition font = text.getFont();

				ChartUtil.mergeFont(font, newFont);
			}

			if (text.getColor() == null) {
				text.setColor(style.getColor());
			}
		} else if (component instanceof LineAttributes) {
			// only apply to axis
			if (component.eContainer() instanceof Axis || component.eContainer() instanceof Series) {
				LineAttributes lia = (LineAttributes) component;
				if (lia.getColor() == null) {
					IStyle style = getMingledStyle(model, type, externalProcessor);
					lia.setColor(style.getColor());
				}
			}
		} else if (component instanceof Axis) {
			Axis axis = (Axis) component;
			if (axis.getFormatSpecifier() == null) {
				IStyle style = getMingledStyle(model, type, externalProcessor);
				switch (axis.getType()) {
				case DATE_TIME_LITERAL:
					axis.setFormatSpecifier(style.getDateTimeFormat());
					break;
				case LINEAR_LITERAL:
				case LOGARITHMIC_LITERAL:
					axis.setFormatSpecifier(style.getNumberFormat());
					break;
				case TEXT_LITERAL:
					axis.setFormatSpecifier(style.getStringFormat());
					break;
				}

			}
		}
	}

	private IStyle getMingledStyle(Chart model, StyledComponent type, IStyleProcessor externalProcessor) {
		SimpleContainer rStyle = new SimpleContainer();

		if (externalProcessor != null) {
			while (type != null && !updateHierarchyStyle(model, type, externalProcessor, rStyle)) {
				type = getParentType(type);
			}
		}

		while (type != null && !updateHierarchyStyle(model, type, implicitProcessor, rStyle)) {
			type = getParentType(type);
		}

		return rStyle.style;
	}

	private StyledComponent getParentType(StyledComponent type) {
		switch (type.getValue()) {
		case StyledComponent.CHART_ALL:
			return null;
		case StyledComponent.CHART_TITLE:
			return StyledComponent.CHART_BACKGROUND_LITERAL;
		case StyledComponent.CHART_BACKGROUND:
			return StyledComponent.CHART_ALL_LITERAL;
		case StyledComponent.PLOT_BACKGROUND:
			return StyledComponent.CHART_BACKGROUND_LITERAL;
		case StyledComponent.LEGEND_BACKGROUND:
			return StyledComponent.CHART_BACKGROUND_LITERAL;
		case StyledComponent.LEGEND_LABEL:
			return StyledComponent.LEGEND_BACKGROUND_LITERAL;
		case StyledComponent.AXIS_TITLE:
			return StyledComponent.PLOT_BACKGROUND_LITERAL;
		case StyledComponent.AXIS_LABEL:
			return StyledComponent.PLOT_BACKGROUND_LITERAL;
		case StyledComponent.AXIS_LINE:
			return StyledComponent.PLOT_BACKGROUND_LITERAL;
		case StyledComponent.SERIES_TITLE:
			return StyledComponent.PLOT_BACKGROUND_LITERAL;
		case StyledComponent.SERIES_LABEL:
			return StyledComponent.PLOT_BACKGROUND_LITERAL;
		}

		return null;
	}

	private boolean updateHierarchyStyle(Chart model, StyledComponent type, IStyleProcessor processor,
			SimpleContainer currentContainer) {
		SimpleStyle currentStyle = currentContainer.style;
		IStyle newStyle = null;

		if (currentStyle == null) {
			currentContainer.style = new SimpleStyle(processor.getStyle(model, type));
			currentStyle = currentContainer.style;
		} else {
			newStyle = processor.getStyle(model, type);
		}

		if (newStyle != null) {
			if (currentStyle.getFont() == null) {
				if (newStyle.getFont() != null) {
					currentStyle.setFont(goFactory.copyOf(newStyle.getFont()));
				}
			} else if (newStyle.getFont() != null) {
				FontDefinition fd = currentStyle.getFont();
				FontDefinition newFd = goFactory.copyOf(newStyle.getFont());

				ChartUtil.mergeFont(fd, newFd);
			}

			if (currentStyle.getColor() == null && newStyle.getColor() != null) {
				currentStyle.setColor(goFactory.copyOf(newStyle.getColor()));
			}
		}

		// check inherited styles.
		FontDefinition fd = currentStyle.getFont();
		if (fd != null && fd.getName() != null && fd.isSetSize() && fd.isSetBold() && fd.isSetItalic()
				&& fd.isSetRotation() && fd.isSetWordWrap() && fd.isSetUnderline() && fd.isSetStrikethrough()
				&& fd.getAlignment() != null && fd.getAlignment().isSetHorizontalAlignment()
				&& currentStyle.getColor() != null) {
			return true;
		}

		return false;
	}

	/**
	 * This retrieves all the data row related expressions stored in the chart
	 * model. This is useful to prepare a specific query for the chart.
	 * 
	 * @param cm The Chart model
	 * @return All row expressions in a list of String instances.
	 * @throws ChartException
	 * 
	 * @since 2.0
	 */
	public List<String> getRowExpressions(Chart cm) throws ChartException {
		return getRowExpressions(cm, null);
	}

	/**
	 * This retrieves all the row expressions stored in the chart model. This is
	 * useful to prepare a specific query for the chart. If the given
	 * IActionEvaluator is not null, then it will also search available expressions
	 * within the action.
	 * 
	 * @param cm  The Chart model
	 * @param iae An IActionEvaluator instance
	 * @return All row expressions in a list of String instances.
	 * @throws ChartException
	 * 
	 * @since 2.3
	 */
	public List<String> getRowExpressions(Chart cm, IActionEvaluator iae, boolean needChangeValueExpr)
			throws ChartException {
		if (cm instanceof ChartWithAxes) {
			return getRowExpressions((ChartWithAxes) cm, iae, needChangeValueExpr);
		} else if (cm instanceof ChartWithoutAxes) {
			return getRowExpressions((ChartWithoutAxes) cm, iae, needChangeValueExpr);
		}
		return null;
	}

	/**
	 * This retrieves all the row expressions stored in the chart model. This is
	 * useful to prepare a specific query for the chart. If the given
	 * IActionEvaluator is not null, then it will also search available expressions
	 * within the action.
	 * 
	 * @param cm  The Chart model
	 * @param iae An IActionEvaluator instance
	 * @return All row expressions in a list of String instances.
	 * @throws ChartException
	 * 
	 * @since 2.0
	 */
	public List<String> getRowExpressions(Chart cm, IActionEvaluator iae) throws ChartException {
		if (cm instanceof ChartWithAxes) {
			return getRowExpressions((ChartWithAxes) cm, iae, true);
		} else if (cm instanceof ChartWithoutAxes) {
			return getRowExpressions((ChartWithoutAxes) cm, iae, true);
		}
		return null;
	}

	/**
	 * Binds a sql Resuset to a chart model. This is based on the assumption the
	 * column names of the resultset match exactly the data query definitions and
	 * other expressions set inside the chart model.
	 * 
	 * @param resultSet A sql resultset that contains the data. The following
	 *                  methods of the interface need to be implemented: first(),
	 *                  next(), getObject(String), close()
	 * @param chart     The chart model to bind the data to
	 * @param rtc       The runtime context
	 * @throws ChartException
	 * 
	 * @since 2.0
	 */
	public void bindData(java.sql.ResultSet resultSet, Chart chart, RunTimeContext rtc) throws ChartException {
		SqlDataRowEvaluator rowEvaluator = new SqlDataRowEvaluator(resultSet);
		bindData(rowEvaluator, chart, rtc);
	}

	/**
	 * Binds data to the chart model using a row expression evaluator. The evaluator
	 * provides the ability to evaluate the expressions set in the chart on a row
	 * context.
	 * 
	 * @param expressionEvaluator The data row expression evaluator implementation
	 * @param chart               The chart model
	 * @param rtc                 The runtime context
	 * @throws ChartException
	 * 
	 * @since 2.0
	 */
	public void bindData(IDataRowExpressionEvaluator expressionEvaluator, Chart chart, RunTimeContext rtc)
			throws ChartException {
		bindData(expressionEvaluator, null, chart, rtc);
	}

	/**
	 * Binds data to the chart model using a row expression evaluator. The evaluator
	 * provides the ability to evaluate the expressions set in the chart on a row
	 * context.If the given IActionEvaluator is not null, then it will also search
	 * available expressions within the action and bind it as the user dataSets.
	 * 
	 * @param expressionEvaluator The data row expression evaluator implementation
	 * @param iae                 An IActionEvaluator instance.
	 * @param chart               The chart model
	 * @param rtc                 The runtime context
	 * @throws ChartException
	 * 
	 * @since 2.0
	 */
	public void bindData(IDataRowExpressionEvaluator expressionEvaluator, IActionEvaluator iae, Chart chart,
			RunTimeContext rtc) throws ChartException {
		// Since beforeDatasetFilled and afterDatasetFilled script functions are
		// invoked when data binding, they might use chart model, so chat model
		// is set into chart script context for the invoking in scripting.
		IChartScriptContext icsc = (IChartScriptContext) rtc.getScriptContext();
		if (icsc == null) {
			// re-init chart script context.
			ChartScriptContext csc = new ChartScriptContext();
			csc.setChartInstance(chart);
			csc.setULocale(rtc.getULocale());
			csc.setLogger(logger);

			rtc.setScriptContext(csc);
		} else if (icsc instanceof ChartScriptContext) {
			// reset logger.
			((ChartScriptContext) icsc).setLogger(logger);
			((ChartScriptContext) icsc).setChartInstance(chart);
		}

		DataProcessor helper = new DataProcessor(rtc, iae);
		helper.generateRuntimeSeries(expressionEvaluator, chart);
	}

	/**
	 * Since v2, it must be called before build( ), and should only be called once
	 * per design model.
	 * 
	 * @param model           Chart design model
	 * @param externalContext External Context
	 * @param locale          Locale
	 * @return a runtime context used by build( )
	 * 
	 * @throws ChartException
	 * 
	 * @deprecated use
	 *             {@link #prepare(Chart, IExternalContext, IScriptClassLoader, ULocale)}
	 *             instead.
	 */
	public RunTimeContext prepare(Chart model, IExternalContext externalContext, IScriptClassLoader iscl, Locale locale)
			throws ChartException {
		return prepare(model, externalContext, iscl, ULocale.forLocale(locale));
	}

	/**
	 * Since v2, it must be called before build( ), and should only be called once
	 * per design model.
	 * 
	 * @param model           Chart design model
	 * @param externalContext External Context
	 * @param locale          Locale
	 * @return a runtime context used by build( )
	 * 
	 * @throws ChartException
	 * 
	 * @since 2.1
	 */
	public RunTimeContext prepare(Chart model, IExternalContext externalContext, IScriptClassLoader iscl,
			ULocale locale) throws ChartException {
		RunTimeContext rtc = new RunTimeContext();
		rtc.setScriptClassLoader(iscl);

		// Update the context with a locale if it is undefined.
		final Chart cmRunTime = model.copyInstance();
		rtc.setULocale(locale != null ? locale : ULocale.getDefault());

		ChartScriptContext csc = new ChartScriptContext();
		csc.setChartInstance(cmRunTime);
		csc.setExternalContext(externalContext);
		csc.setULocale(rtc.getULocale());
		csc.setLogger(logger);
		rtc.setScriptContext(csc);

		ScriptHandler sh = new ScriptHandler();
		rtc.setScriptHandler(sh);

		sh.setScriptClassLoader(iscl);
		sh.setScriptContext(csc);
		// initialize script handler.
		if (externalContext != null && externalContext.getScriptable() != null) {
			sh.init(externalContext.getScriptable());
		} else {
			sh.init(null);
		}
		sh.setRunTimeModel(cmRunTime);

		/*
		 * The following code can cause exceptions with bad script. no meaning to
		 * register the script in prepare until we can make the prepare call work
		 * 
		 * 
		 * final String sScriptContent = cmRunTime.getScript( ); if ( sScriptContent !=
		 * null ) { sh.register( sScriptContent ); }
		 * 
		 * // Call the onPrepare script event function. // not supported yet /
		 * ScriptHandler.callFunction( sh, ScriptHandler.ON_PREPARE, cmRunTime,
		 * rtc.getScriptContext( ) );
		 */

		return rtc;
	}

	/**
	 * Builds and computes preferred sizes of various chart components offscreen
	 * using the provided display server.
	 * 
	 * @param ids       A display server using which the chart may be built.
	 * @param cmRunTime The runtime chart model (bound to a dataset).
	 * @param scParent  A parent script handler that may be attached to the existing
	 *                  chart model script handler.
	 * @param bo        The bounds associated with the chart being built.
	 * @param rtc       Encapsulates the runtime environment for the build process.
	 * 
	 * @return An instance of a generated chart state that encapsulates built chart
	 *         information that may be subsequently rendered.
	 * 
	 * @throws ChartException
	 * 
	 * @deprecated use
	 *             {@link #build(IDisplayServer, Chart, Bounds, IExternalContext, RunTimeContext)}
	 *             instead.
	 */
	public final GeneratedChartState build(IDisplayServer ids, Chart cmRunTime, Scriptable scParent, Bounds bo,
			RunTimeContext rtc) throws ChartException {
		return build(ids, cmRunTime, scParent, bo, rtc, null);
	}

	/**
	 * Builds and computes preferred sizes of various chart components offscreen
	 * using the provided display server.
	 * 
	 * @param ids               A display server using which the chart may be built.
	 * @param cmRunTime         The runtime chart model (bound to a dataset).
	 * @param scParent          A parent script handler that may be attached to the
	 *                          existing chart model script handler.
	 * @param bo                The bounds associated with the chart being built.
	 * @param rtc               Encapsulates the runtime environment for the build
	 *                          process.
	 * @param externalProcessor An external style processor. If this is null, an
	 *                          implicit processor will be used.
	 * @return An instance of a generated chart state that encapsulates built chart
	 *         information that may be subsequently rendered.
	 * 
	 * @throws ChartException
	 * 
	 * @deprecated use
	 *             {@link #build(IDisplayServer, Chart, Bounds, IExternalContext, RunTimeContext, IStyleProcessor)}
	 *             instead.
	 */
	public final GeneratedChartState build(IDisplayServer ids, Chart cmRunTime, Scriptable scParent, Bounds bo,
			RunTimeContext rtc, IStyleProcessor externalProcessor) throws ChartException {
		final Scriptable scriptContext = scParent;

		return build(ids, cmRunTime, bo, new IExternalContext() {

			private static final long serialVersionUID = 1L;

			public Object getObject() {
				return null;
			}

			public Scriptable getScriptable() {
				return scriptContext;
			}

		}, rtc, externalProcessor);
	}

	/**
	 * Builds and computes preferred sizes of various chart components offscreen
	 * using the provided display server.
	 * 
	 * @param ids       A display server using which the chart may be built.
	 * @param cmRunTime The runtime chart model (bound to a dataset).
	 * @param bo        The bounds associated with the chart being built.
	 * @param rtc       Encapsulates the runtime environment for the build process.
	 * @return An instance of a generated chart state that encapsulates built chart
	 *         information that may be subsequently rendered.
	 * 
	 * @throws ChartException
	 * @since 2.2
	 */
	public final GeneratedChartState build(IDisplayServer ids, Chart cmRunTime, Bounds bo, RunTimeContext rtc)
			throws ChartException {
		return build(ids, cmRunTime, bo, null, rtc, null);
	}

	/**
	 * Builds and computes preferred sizes of various chart components offscreen
	 * using the provided display server.
	 * 
	 * @param ids             A display server using which the chart may be built.
	 * @param cmRunTime       The runtime chart model (bound to a dataset).
	 * @param externalContext An external context object.
	 * @param bo              The bounds associated with the chart being built.
	 * @param rtc             Encapsulates the runtime environment for the build
	 *                        process.
	 * @return An instance of a generated chart state that encapsulates built chart
	 *         information that may be subsequently rendered.
	 * 
	 * @throws ChartException
	 */
	public final GeneratedChartState build(IDisplayServer ids, Chart cmRunTime, Bounds bo,
			IExternalContext externalContext, RunTimeContext rtc) throws ChartException {
		return build(ids, cmRunTime, bo, externalContext, rtc, null);
	}

	/**
	 * Builds and computes preferred sizes of various chart components offscreen
	 * using the provided display server.
	 * 
	 * @param ids               A display server using which the chart may be built.
	 * @param cmRunTime         The run time chart model (bound to a dataset).
	 * @param externalContext   An external context object.
	 * @param bo                The bounds associated with the chart being built.
	 * @param rtc               Encapsulates the runtime environment for the build
	 *                          process.
	 * @param externalProcessor An external style processor. If it's null, an
	 *                          implicit processor will be used.
	 * @return An instance of a generated chart state that encapsulates built chart
	 *         information that may be subsequently rendered.
	 * 
	 * @throws ChartException
	 */
	public final GeneratedChartState build(IDisplayServer ids, Chart cmRunTime, Bounds bo,
			IExternalContext externalContext, RunTimeContext rtc, IStyleProcessor externalProcessor)
			throws ChartException {
		if (ids == null || cmRunTime == null || bo == null) {
			throw new ChartException(ChartEnginePlugin.ID, ChartException.GENERATION, "exception.illegal.null.value", //$NON-NLS-1$
					Messages.getResourceBundle());
		}

		// CREATE A RUNTIME CONTEXT IF NEEDED
		if (rtc == null) {
			rtc = new RunTimeContext();
		}

		rtc.putState(StateKey.CHART_COMPUTATION_KEY, ChartComputationFactory.instance().createChartComputation());

		// UPDATE THE CONTEXT WITH A LOCALE IF IT IS UNDEFINED
		if (rtc.getULocale() == null) {
			rtc.setULocale(ULocale.getDefault());
		}

		// UPDATE THE CHART SCRIPT CONTEXT
		IChartScriptContext icsc = (IChartScriptContext) rtc.getScriptContext();
		if (icsc == null) {
			// re-init chart script context.
			ChartScriptContext csc = new ChartScriptContext();
			csc.setChartInstance(cmRunTime);
			csc.setExternalContext(externalContext);
			csc.setULocale(rtc.getULocale());
			csc.setLogger(logger);

			rtc.setScriptContext(csc);
			icsc = csc;
		} else if (icsc instanceof ChartScriptContext) {
			// reset logger.
			((ChartScriptContext) icsc).setLogger(logger);
			// Set runtime bounds to runtime chart model
			cmRunTime.getBlock().setBounds(bo);
			((ChartScriptContext) icsc).setChartInstance(cmRunTime);
		}

		if (externalContext != null && icsc instanceof ChartScriptContext) {
			((ChartScriptContext) icsc).setExternalContext(externalContext);
		}

		// INITIALIZE THE SCRIPT HANDLER
		ScriptHandler sh = (ScriptHandler) rtc.getScriptHandler();
		if (sh == null) // IF NOT PREVIOUSLY DEFINED BY
		// REPORTITEM ADAPTER
		{
			sh = new ScriptHandler();
			rtc.setScriptHandler(sh);

			sh.setScriptClassLoader(rtc.getScriptClassLoader());
			sh.setScriptContext(icsc);

			final String sScriptContent = cmRunTime.getScript();
			if (externalContext != null && externalContext.getScriptable() != null) {
				sh.init(externalContext.getScriptable());
			} else {
				sh.init(null);
			}
			sh.setRunTimeModel(cmRunTime);

			if (sScriptContent != null && sScriptContent.length() > 0 && rtc.isScriptingEnabled()) {
				sh.register(null, sScriptContent);
			}

		}

		// SETUP THE COMPUTATIONS
		ScriptHandler.callFunction(sh, ScriptHandler.START_GENERATION, cmRunTime);
		ScriptHandler.callFunction(sh, ScriptHandler.BEFORE_GENERATION, cmRunTime, rtc.getScriptContext());

		// remove invisible series from runtime model
		ChartUtil.pruneInvisibleSeries(cmRunTime);

		checkDataEmpty(cmRunTime, rtc);

		// flatten the default styles.
		prepareStyles(cmRunTime, externalProcessor, rtc);

		PlotComputation oComputations = null;
		if (cmRunTime instanceof ChartWithAxes) {
			try {
				if (cmRunTime.getDimension() == ChartDimension.THREE_DIMENSIONAL_LITERAL) {
					oComputations = new PlotWith3DAxes(ids, (ChartWithAxes) cmRunTime, rtc);
				} else {
					oComputations = new PlotWith2DAxes(ids, (ChartWithAxes) cmRunTime, rtc);
				}
			} catch (Exception e) {
				throw new ChartException(ChartEnginePlugin.ID, ChartException.GENERATION, e);
			}
		} else if (cmRunTime instanceof ChartWithoutAxes) {
			oComputations = new PlotWithoutAxes(ids, (ChartWithoutAxes) cmRunTime, rtc);
		}

		if (oComputations == null) {
			throw new ChartException(ChartEnginePlugin.ID, ChartException.GENERATION,
					"exception.unsupported.chart.model", //$NON-NLS-1$
					new Object[] { cmRunTime }, Messages.getResourceBundle(rtc.getULocale()));
		}

		// OBTAIN THE RENDERERS
		final LinkedHashMap<Series, LegendItemRenderingHints> lhmRenderers = new LinkedHashMap<Series, LegendItemRenderingHints>();
		BaseRenderer[] brna = null;
		try {
			brna = BaseRenderer.instances(cmRunTime, rtc, oComputations);
			for (int i = 0; i < brna.length; i++) {
				lhmRenderers.put(brna[i].getSeries(),
						new LegendItemRenderingHints(brna[i], goFactory.createBounds(0, 0, 0, 0)));
			}

			// Set series renderers info.
			rtc.setSeriesRenderers(lhmRenderers);
			// Clean legend state.
			rtc.setLegendLayoutHints(null);
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new ChartException(ChartEnginePlugin.ID, ChartException.GENERATION, ex);
		}

		if (oComputations instanceof PlotWith2DAxes) {
			// If the chart plot bounds are not fixed, initialize the bounds.
			PlotWith2DAxes pwa = (PlotWith2DAxes) oComputations;
			pwa.initDynamicPlotBounds(bo);
		}

		// PERFORM THE BLOCKS' LAYOUT
		Block bl = cmRunTime.getBlock();
		final LayoutManager lm = new LayoutManager(bl);
		ScriptHandler.callFunction(sh, ScriptHandler.BEFORE_LAYOUT, cmRunTime);
		lm.doLayout(ids, cmRunTime, bo, rtc);

		ScriptHandler.callFunction(sh, ScriptHandler.AFTER_LAYOUT, cmRunTime);

		// COMPUTE THE PLOT AREA
		Bounds boPlot = cmRunTime.getPlot().getBounds();
		Insets insPlot = cmRunTime.getPlot().getInsets();
		boPlot = goFactory.adjusteBounds(boPlot, insPlot);

		ScriptHandler.callFunction(sh, ScriptHandler.BEFORE_COMPUTATIONS, cmRunTime, oComputations);

		try {
			oComputations.compute(boPlot);
		} catch (Exception ex) {
			throw new ChartException(ChartEnginePlugin.ID, ChartException.GENERATION, ex);
		}
		ScriptHandler.callFunction(sh, ScriptHandler.AFTER_COMPUTATIONS, cmRunTime, oComputations);

		final Collection<LegendItemRenderingHints> co = lhmRenderers.values();
		final LegendItemRenderingHints[] lirha = co.toArray(new LegendItemRenderingHints[co.size()]);
		final int iSize = lhmRenderers.size();
		BaseRenderer br;

		for (int i = 0; i < iSize; i++) {
			br = lirha[i].getRenderer();
			br.set(brna);
			br.set(ids);
			br.set(rtc);
			try {
				br.set(br.getComputations().getSeriesRenderingHints(br.getSeriesDefinition(), br.getSeries()));
				ScriptHandler.callFunction(sh, ScriptHandler.START_COMPUTE_SERIES, br.getSeries());
				br.compute(bo, cmRunTime.getPlot(), br.getSeriesRenderingHints());
				ScriptHandler.callFunction(sh, ScriptHandler.FINISH_COMPUTE_SERIES, br.getSeries());
			} catch (Exception ex) {
				throw new ChartException(ChartEnginePlugin.ID, ChartException.GENERATION, ex);
			}
		}

		final GeneratedChartState gcs = new GeneratedChartState(ids, cmRunTime, lhmRenderers, rtc, oComputations);
		if (sh != null) {
			sh.setGeneratedChartState(gcs);
			ScriptHandler.callFunction(sh, ScriptHandler.FINISH_GENERATION, gcs);
			ScriptHandler.callFunction(sh, ScriptHandler.AFTER_GENERATION, gcs, rtc.getScriptContext());
		}

		return gcs;
	}

	/**
	 * Performs a minimal rebuild of the chart if non-sizing attributes are altered
	 * or the dataset for any series has changed. However, if sizing attribute
	 * changes occur that affects the relative position of the various chart
	 * subcomponents, a re-build is required.
	 * 
	 * @param gcs A previously built chart encapsulated in a transient structure.
	 * 
	 * @throws ChartException
	 */
	public final void refresh(GeneratedChartState gcs) throws ChartException {
		Chart cm = gcs.getChartModel();

		ScriptHandler.callFunction(gcs.getRunTimeContext().getScriptHandler(), ScriptHandler.BEFORE_COMPUTATIONS, cm,
				gcs.getComputations());

		// COMPUTE THE PLOT AREA
		Bounds boPlot = cm.getPlot().getBounds();
		Insets insPlot = cm.getPlot().getInsets();
		boPlot = goFactory.adjusteBounds(boPlot, insPlot);

		try {
			gcs.getComputations().compute(boPlot);
			for (LegendItemRenderingHints lirha : gcs.getRenderers().values()) {
				BaseRenderer br = lirha.getRenderer();
				br.set(br.getComputations().getSeriesRenderingHints(br.getSeriesDefinition(), br.getSeries()));
			}
		} catch (RuntimeException ex) {
			throw new ChartException(ChartEnginePlugin.ID, ChartException.GENERATION, ex);
		}

		ScriptHandler.callFunction(gcs.getRunTimeContext().getScriptHandler(), ScriptHandler.AFTER_COMPUTATIONS, cm,
				gcs.getComputations());
	}

	/**
	 * Draws a previously built chart using the specified device renderer into a
	 * target output device.
	 * 
	 * @param idr A device renderer that determines the target context on which the
	 *            chart will be rendered.
	 * @param gcs A previously built chart that needs to be rendered.
	 * 
	 * @throws ChartException
	 */
	public final void render(IDeviceRenderer idr, GeneratedChartState gcs) throws ChartException {
		final Chart cm = gcs.getChartModel();
		final int scale = idr.getDisplayServer().getDpiResolution() / 72;
		if (scale != 1) {
			// Here multiply by integer scale so that normal dpi (96) won't
			// change thickness by default. Only PDF case would change.
			updateDeviceScale(cm, scale);
		}

		idr.getDisplayServer().setResourceFinder(gcs.getRunTimeContext().getResourceFinder());

		ScriptHandler.callFunction(gcs.getRunTimeContext().getScriptHandler(), ScriptHandler.START_RENDERING, gcs);
		ScriptHandler.callFunction(gcs.getRunTimeContext().getScriptHandler(), ScriptHandler.BEFORE_RENDERING, gcs,
				gcs.getRunTimeContext().getScriptContext());
		Legend lg = cm.getLegend();
		lg.updateLayout(cm); // RE-ORGANIZE BLOCKS IF REQUIRED
		if (lg.getPosition() == Position.INSIDE_LITERAL) {
			Bounds bo = gcs.getComputations().getPlotBounds();
			updateLegendInside(bo, lg, idr.getDisplayServer(), cm, gcs.getRunTimeContext());
		}

		final LinkedHashMap<Series, LegendItemRenderingHints> lhm = gcs.getRenderers();
		final int iSize = lhm.size();

		BaseRenderer br;
		final Collection<LegendItemRenderingHints> co = lhm.values();
		final LegendItemRenderingHints[] lirha = co.toArray(new LegendItemRenderingHints[co.size()]);

		// Fixed bugzilla bug 193234.
		// Use DeferredCacheManager instead of single DeferredCache to get
		// correct z-order of series for only 2D.
		final DeferredCacheManager dcm = new DeferredCacheManager(idr, cm);
		DeferredCache dc = null;
		// RENDERING
		// ELEMENTS WITH
		// THE CORRECT
		// Z-ORDER

		// USE SAME BOUNDS FOR RENDERING AS THOSE USED TO PREVIOUSLY COMPUTE THE
		// CHART OFFSCREEN
		final Bounds bo = gcs.getChartModel().getBlock().getBounds();
		idr.setProperty(IDeviceRenderer.EXPECTED_BOUNDS,
				goFactory.scaleBounds(bo, idr.getDisplayServer().getDpiResolution() / 72d));
		idr.setChartComputation(gcs.getComputations().getChartComputation());

		// UPDATE THE STRUCTURE DEFINITION LISTENER MAINTAINED BY THE RUNTIME
		// CONTEXT
		gcs.getRunTimeContext().setStructureDefinitionListener(idr.needsStructureDefinition() ? idr : null);

		idr.before(); // INITIALIZATION BEFORE RENDERING BEGINS
		for (int i = 0; i < iSize; i++) {
			br = lirha[i].getRenderer();

			dc = dcm.createDeferredCache(br, i);

			br.setDeferredCacheManager(dcm);
			br.set(dc);
			br.set(idr);
			br.set(gcs.getRunTimeContext());
			try {
				br.render(lhm, bo); // 'bo' MUST BE CLIENT AREA WITHIN ANY
				// 'shell' OR 'frame'
			} catch (RuntimeException ex) {
				throw new ChartException(ChartEnginePlugin.ID, ChartException.RENDERING, ex);
			}
		}
		idr.after(); // ANY CLEANUP AFTER THE CHART HAS BEEN RENDERED

		// CLEAN UP THE RENDERING STATES.
		gcs.getRunTimeContext().clearState();

		ScriptHandler.callFunction(gcs.getRunTimeContext().getScriptHandler(), ScriptHandler.FINISH_RENDERING, gcs);
		ScriptHandler.callFunction(gcs.getRunTimeContext().getScriptHandler(), ScriptHandler.AFTER_RENDERING, gcs,
				gcs.getRunTimeContext().getScriptContext());

	}

	/**
	 * Internally updates a legend position by altering the legend block hierarchy
	 * as needed.
	 * 
	 * @param boContainer The internal bounds of the client area contained within
	 *                    the plot.
	 * @param lg          An instance of the legend for which the position is being
	 *                    updated.
	 * @param ids         An instance of the display server associated with building
	 *                    the chart.
	 * @param cm          An instance of the chart model for which the legend
	 *                    position is updated.
	 * 
	 * @throws ChartException
	 */
	private static final void updateLegendInside(Bounds boContainer, Legend lg, IDisplayServer ids, Chart cm,
			RunTimeContext rtc) throws ChartException {
		final double dScale = ids.getDpiResolution() / 72d;

		double dX, dY;
		final Size sz = lg.getPreferredSize(ids, cm, rtc);
		boContainer = goFactory.scaleBounds(boContainer, 1d / dScale);

		// USE ANCHOR IN POSITIONING THE LEGEND CLIENT AREA WITHIN THE BLOCK
		// SLACK SPACE
		if (lg.isSetAnchor()) {
			int iAnchor = lg.getAnchor().getValue();

			// swap west/east
			if (rtc.isRightToLeft()) {
				if (iAnchor == Anchor.EAST) {
					iAnchor = Anchor.WEST;
				} else if (iAnchor == Anchor.NORTH_EAST) {
					iAnchor = Anchor.NORTH_WEST;
				} else if (iAnchor == Anchor.SOUTH_EAST) {
					iAnchor = Anchor.SOUTH_WEST;
				} else if (iAnchor == Anchor.WEST) {
					iAnchor = Anchor.EAST;
				} else if (iAnchor == Anchor.NORTH_WEST) {
					iAnchor = Anchor.NORTH_EAST;
				} else if (iAnchor == Anchor.SOUTH_WEST) {
					iAnchor = Anchor.SOUTH_EAST;
				}
			}

			switch (iAnchor) {
			case Anchor.NORTH:
			case Anchor.NORTH_EAST:
			case Anchor.NORTH_WEST:
				dY = boContainer.getTop();
				break;

			case Anchor.SOUTH:
			case Anchor.SOUTH_EAST:
			case Anchor.SOUTH_WEST:
				dY = boContainer.getTop() + boContainer.getHeight() - sz.getHeight();
				break;

			default: // CENTERED
				dY = boContainer.getTop() + (boContainer.getHeight() - sz.getHeight()) / 2;
				break;
			}

			switch (iAnchor) {
			case Anchor.WEST:
			case Anchor.SOUTH_WEST:
			case Anchor.NORTH_WEST:
				dX = boContainer.getLeft();
				break;

			case Anchor.NORTH_EAST:
			case Anchor.EAST:
			case Anchor.SOUTH_EAST:
				dX = boContainer.getLeft() + boContainer.getWidth() - sz.getWidth();
				break;

			default: // CENTERED
				dX = boContainer.getLeft() + (boContainer.getWidth() - sz.getWidth()) / 2;
				break;
			}
		} else {
			dX = boContainer.getLeft() + (boContainer.getWidth() - sz.getWidth()) / 2;
			dY = boContainer.getTop() + (boContainer.getHeight() - sz.getHeight()) / 2;
		}

		lg.getBounds().set(dX, dY, sz.getWidth(), sz.getHeight());
	}

	/**
	 * A simple class used to pass object reference internally.
	 */
	static class SimpleContainer {

		SimpleStyle style = null;
	}

	private static List<String> getRowExpressions(ChartWithoutAxes cwoa, IActionEvaluator iae,
			boolean needChangeValueExpr) throws ChartException {
		final List<String> alExpressions = new ArrayList<String>(4);
		EList<SeriesDefinition> elSD = cwoa.getSeriesDefinitions();
		if (elSD.size() != 1) {
			throw new ChartException(ChartEnginePlugin.ID, ChartException.DATA_BINDING,
					"dataprocessor.exception.CannotDecipher", //$NON-NLS-1$
					Messages.getResourceBundle());
		}

		// PROJECT THE EXPRESSION ASSOCIATED WITH THE BASE SERIES DEFINITION
		SeriesDefinition categorySD = elSD.get(0);
		final Query qBaseSeriesDefinition = categorySD.getQuery();
		String sExpression = qBaseSeriesDefinition.getDefinition();
		if (sExpression != null && sExpression.trim().length() > 0) {
			// Ignore expression for base series definition
			logger.log(ILogger.WARNING,
					Messages.getString("dataprocessor.log.baseSeriesDefn3", sExpression, ULocale.getDefault())); //$NON-NLS-1$
		}

		// PROJECT THE EXPRESSION ASSOCIATED WITH THE BASE SERIES EXPRESSION
		final Series seBase = categorySD.getDesignTimeSeries();
		EList<Query> elBaseSeries = seBase.getDataDefinition();
		if (elBaseSeries.size() != 1) {
			throw new ChartException(ChartEnginePlugin.ID, ChartException.DATA_BINDING,
					"dataprocessor.exception.FoundDefnAssociatedWithX", //$NON-NLS-1$
					new Object[] { String.valueOf(elBaseSeries.size()) }, null);
		}

		final Query qBaseSeries = elBaseSeries.get(0);
		sExpression = qBaseSeries.getDefinition();
		if (sExpression != null && sExpression.trim().length() > 0 && !alExpressions.contains(sExpression)) {
			alExpressions.add(sExpression);

			// Add sort key
			addSortExperssion(alExpressions, categorySD, sExpression);
		} else {
			throw new ChartException(ChartEnginePlugin.ID, ChartException.DATA_BINDING,
					"dataprocessor.exception.DefinitionUnspecified", //$NON-NLS-1$
					Messages.getResourceBundle());
		}

		// PROJECT ALL DATA DEFINITIONS ASSOCIATED WITH THE ORTHOGONAL SERIES
		Query qOrthogonalSeriesDefinition, qOrthogonalSeries;
		Series seOrthogonal;
		EList<Query> elOrthogonalSeries;
		elSD = categorySD.getSeriesDefinitions(); // ALL ORTHOGONAL SERIES
		// DEFINITIONS
		int iCount = 0;
		boolean bAnyQueries;
		for (int k = 0; k < elSD.size(); k++) {
			SeriesDefinition sd = elSD.get(k);
			qOrthogonalSeriesDefinition = sd.getQuery();
			if (qOrthogonalSeriesDefinition == null) {
				continue;
			}
			sExpression = qOrthogonalSeriesDefinition.getDefinition();
			if (sExpression != null && sExpression.trim().length() > 0) {
				// FILTER OUT DUPLICATE ENTRIES
				if (alExpressions.contains(sExpression)) {
					int iRemovalIndex = alExpressions.indexOf(sExpression);
					if (iRemovalIndex > iCount) {
						alExpressions.remove(iRemovalIndex);
						alExpressions.add(iCount++, sExpression);
					} else {
						// DON'T ADD IF PREVIOUSLY ADDED BEFORE 'iCount'
						// continue;
					}
				} else {
					// INSERT AT START
					alExpressions.add(iCount++, sExpression);

					// Add sort key
					addSortExperssion(alExpressions, sd, sExpression);
				}
			}

			seOrthogonal = sd.getDesignTimeSeries();
			elOrthogonalSeries = seOrthogonal.getDataDefinition();
			if (elOrthogonalSeries.isEmpty()) {
				throw new ChartException(ChartEnginePlugin.ID, ChartException.DATA_BINDING,
						"dataprocessor.exception.DefnExpMustAssociateY", //$NON-NLS-1$
						new Object[] { String.valueOf(iCount), seOrthogonal }, Messages.getResourceBundle());
			}

			bAnyQueries = false;
			for (int i = 0; i < elOrthogonalSeries.size(); i++) {
				qOrthogonalSeries = elOrthogonalSeries.get(i);
				if (qOrthogonalSeries == null) // NPE PROTECTION
				{
					continue;
				}

				sExpression = qOrthogonalSeries.getDefinition();
				if (needChangeValueExpr) {
					sExpression = ChartUtil.createValueSeriesRowFullExpression(qOrthogonalSeries, sd, categorySD);
				}

				if (sExpression != null && sExpression.trim().length() > 0) {
					bAnyQueries = true;
					if (!alExpressions.contains(sExpression)) {
						alExpressions.add(sExpression); // APPEND AT END
					}
				}
			}
			if (!bAnyQueries) {
				throw new ChartException(ChartEnginePlugin.ID, ChartException.DATA_BINDING,
						"dataprocessor.exception.AtLeastOneDefnExpMustAssociateY", //$NON-NLS-1$
						new Object[] { String.valueOf(iCount), seOrthogonal }, Messages.getResourceBundle());
			}

			// Add orthogonal series trigger expressions.
			String[] triggerExprs = DataProcessor.getSeriesTriggerExpressions(seOrthogonal, iae, categorySD, sd);
			if (triggerExprs != null) {
				for (int t = 0; t < triggerExprs.length; t++) {
					String tgexp = triggerExprs[t];
					if (!alExpressions.contains(tgexp)) {
						alExpressions.add(tgexp); // APPEND AT END
					}
				}
			}
		}

		return alExpressions;
	}

	private static List<String> getRowExpressions(ChartWithAxes cwa, IActionEvaluator iae, boolean needChangeValueExpr)
			throws ChartException {
		final List<String> alExpressions = new ArrayList<String>(4);
		final Axis axPrimaryBase = cwa.getPrimaryBaseAxes()[0];
		EList<SeriesDefinition> elSD = axPrimaryBase.getSeriesDefinitions();
		if (elSD.size() != 1) {
			throw new ChartException(ChartEnginePlugin.ID, ChartException.GENERATION,
					"dataprocessor.exception.CannotDecipher2", //$NON-NLS-1$
					Messages.getResourceBundle());
		}

		// PROJECT THE EXPRESSION ASSOCIATED WITH THE BASE SERIES DEFINITION
		SeriesDefinition categorySD = elSD.get(0);
		final Query qBaseSeriesDefinition = categorySD.getQuery();
		String sExpression = qBaseSeriesDefinition.getDefinition();
		if (sExpression != null && sExpression.trim().length() > 0) {
			// ignore expression for base series definition
			logger.log(ILogger.WARNING,
					Messages.getString("dataprocessor.log.XSeriesDefn", sExpression, ULocale.getDefault())); //$NON-NLS-1$
		}

		// PROJECT THE EXPRESSION ASSOCIATED WITH THE BASE SERIES EXPRESSION
		final Series seBase = categorySD.getDesignTimeSeries();
		EList<Query> elBaseSeries = seBase.getDataDefinition();
		if (elBaseSeries.size() != 1) {
			throw new ChartException(ChartEnginePlugin.ID, ChartException.DATA_BINDING,
					"dataprocessor.exception.FoundMoreThanOneDefnAssociateX", //$NON-NLS-1$
					new Object[] { String.valueOf(elBaseSeries.size()) }, Messages.getResourceBundle());
		}

		final Query qBaseSeries = elBaseSeries.get(0);
		sExpression = qBaseSeries.getDefinition();
		if (sExpression != null && sExpression.trim().length() > 0) {
			alExpressions.add(sExpression);

			// Add sort key.
			addSortExperssion(alExpressions, categorySD, sExpression);
		} else {
			throw new ChartException(ChartEnginePlugin.ID, ChartException.DATA_BINDING,
					"dataprocessor.exception.definitionsAssociatedWithX", //$NON-NLS-1$
					Messages.getResourceBundle());
		}

		// PROJECT ALL DATA DEFINITIONS ASSOCIATED WITH THE ORTHOGONAL SERIES
		Query qOrthogonalSeriesDefinition, qOrthogonalSeries;
		Series seOrthogonal;
		EList<Query> elOrthogonalSeries;
		final Axis[] axaOrthogonal = cwa.getOrthogonalAxes(axPrimaryBase, true);
		int iCount = 0;
		boolean bAnyQueries;
		for (int j = 0; j < axaOrthogonal.length; j++) {
			elSD = axaOrthogonal[j].getSeriesDefinitions();
			for (int k = 0; k < elSD.size(); k++) {
				SeriesDefinition sd = elSD.get(k);
				qOrthogonalSeriesDefinition = sd.getQuery();
				if (qOrthogonalSeriesDefinition == null) {
					continue;
				}

				sExpression = qOrthogonalSeriesDefinition.getDefinition();
				if (sExpression != null && sExpression.trim().length() > 0) {
					// FILTER OUT DUPLICATE ENTRIES
					if (alExpressions.contains(sExpression)) {
						int iRemovalIndex = alExpressions.indexOf(sExpression);
						if (iRemovalIndex > iCount) {
							alExpressions.remove(iRemovalIndex);
							alExpressions.add(iCount++, sExpression);
						} else {
							// DON'T ADD IF PREVIOUSLY ADDED BEFORE 'iCount'
							// continue;
						}
					} else {
						// INSERT AT START
						alExpressions.add(iCount++, sExpression);

						addSortExperssion(alExpressions, sd, sExpression);
					}
				}

				seOrthogonal = sd.getDesignTimeSeries();
				elOrthogonalSeries = seOrthogonal.getDataDefinition();
				if (elOrthogonalSeries.isEmpty()) {
					throw new ChartException(ChartEnginePlugin.ID, ChartException.DATA_BINDING,
							"dataprocessor.exception.DefnExpMustAssociateY", //$NON-NLS-1$
							new Object[] { String.valueOf(iCount), seOrthogonal }, Messages.getResourceBundle());
				}

				bAnyQueries = false;
				for (int i = 0; i < elOrthogonalSeries.size(); i++) {
					qOrthogonalSeries = elOrthogonalSeries.get(i);
					if (qOrthogonalSeries == null) // NPE PROTECTION
					{
						continue;
					}

					sExpression = qOrthogonalSeries.getDefinition();
					if (sExpression != null && sExpression.trim().length() > 0) {
						if (needChangeValueExpr) {
							sExpression = ChartUtil.createValueSeriesRowFullExpression(qOrthogonalSeries, sd,
									categorySD);
						}

						bAnyQueries = true;
						if (!alExpressions.contains(sExpression)) {
							alExpressions.add(sExpression); // APPEND AT END
						}
					}
				}

				if (!bAnyQueries) {
					throw new ChartException(ChartEnginePlugin.ID, ChartException.DATA_BINDING,
							"dataprocessor.exception.AtLeastOneDefnExpMustAssociateY", //$NON-NLS-1$
							new Object[] { String.valueOf(iCount), seOrthogonal }, Messages.getResourceBundle());
				}

				// Add orthogonal series trigger expressions.
				String[] triggerExprs = DataProcessor.getSeriesTriggerExpressions(seOrthogonal, iae, categorySD, sd);
				if (triggerExprs != null) {
					for (int t = 0; t < triggerExprs.length; t++) {
						String tgexp = triggerExprs[t];
						if (!alExpressions.contains(tgexp)) {
							alExpressions.add(tgexp); // APPEND AT END
						}
					}
				}
			}
		}

		return alExpressions;
	}

	/**
	 * @param alExpressions
	 * @param seriesDefinition
	 * @param sExpression
	 */
	private static void addSortExperssion(final List<String> alExpressions, SeriesDefinition seriesDefinition,
			String sExpression) {
		if (seriesDefinition.isSetSorting() && seriesDefinition.getSortKey() != null
				&& !sExpression.equals(seriesDefinition.getSortKey().getDefinition())) {
			alExpressions.add(seriesDefinition.getSortKey().getDefinition());
		}
	}

	private static void checkDataEmpty(Chart cm, RunTimeContext rtc) {
		if (!ChartUtil.isDataEmpty(rtc)) {
			return;
		}

		// Set all axis label to invisible if there is no data.
		if (cm instanceof ChartWithAxes) {
			ChartWithAxes cwa = (ChartWithAxes) cm;
			Axis axBase = cwa.getBaseAxes()[0];
			axBase.getLabel().setVisible(false);

			for (Axis ax : axBase.getAssociatedAxes()) {
				ax.getLabel().setVisible(false);
			}

			for (Axis ax : axBase.getAncillaryAxes()) {
				ax.getLabel().setVisible(false);
			}
		}

	}

	public void setDefaultBackground(ColorDefinition cd) {
		implicitProcessor.setDefaultBackgroundColor(cd);
	}

	private void updateDeviceScale(EObject component, int scale) {
		if (component instanceof LineAttributes) {
			LineAttributes lia = (LineAttributes) component;
			lia.setThickness(lia.getThickness() * scale);
			return;
		}
		// prepare children
		for (Iterator<EObject> itr = component.eContents().iterator(); itr.hasNext();) {
			updateDeviceScale(itr.next(), scale);
		}
	}
}
