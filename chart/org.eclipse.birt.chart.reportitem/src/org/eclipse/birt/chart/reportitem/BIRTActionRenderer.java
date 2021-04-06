/*******************************************************************************
 * Copyright (c) 2004, 2007, 2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.chart.reportitem;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Pattern;

import org.eclipse.birt.chart.computation.DataPointHints;
import org.eclipse.birt.chart.computation.LegendItemHints;
import org.eclipse.birt.chart.computation.ValueFormatter;
import org.eclipse.birt.chart.event.StructureSource;
import org.eclipse.birt.chart.event.StructureType;
import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.factory.IDataRowExpressionEvaluator;
import org.eclipse.birt.chart.factory.RunTimeContext;
import org.eclipse.birt.chart.log.ILogger;
import org.eclipse.birt.chart.log.Logger;
import org.eclipse.birt.chart.model.attribute.ActionType;
import org.eclipse.birt.chart.model.attribute.FormatSpecifier;
import org.eclipse.birt.chart.model.attribute.MultiURLValues;
import org.eclipse.birt.chart.model.attribute.ScriptValue;
import org.eclipse.birt.chart.model.attribute.TooltipValue;
import org.eclipse.birt.chart.model.attribute.URLValue;
import org.eclipse.birt.chart.model.data.Action;
import org.eclipse.birt.chart.model.data.MultipleActions;
import org.eclipse.birt.chart.render.ActionRendererAdapter;
import org.eclipse.birt.chart.script.ScriptHandler;
import org.eclipse.birt.chart.util.ChartUtil;
import org.eclipse.birt.core.data.ExpressionUtil;
import org.eclipse.birt.report.engine.api.IAction;
import org.eclipse.birt.report.engine.api.IHTMLActionHandler;
import org.eclipse.birt.report.engine.api.script.IReportContext;
import org.eclipse.birt.report.model.api.ActionHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ExpressionHandle;
import org.eclipse.birt.report.model.api.ExpressionType;
import org.eclipse.birt.report.model.api.MemberHandle;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.ModuleUtil;
import org.eclipse.birt.report.model.api.ParamBindingHandle;
import org.eclipse.birt.report.model.api.SearchKeyHandle;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;

/**
 * A BIRT action renderer implementation.
 */
public class BIRTActionRenderer extends ActionRendererAdapter {

	protected IHTMLActionHandler handler;
	protected DesignElementHandle eih;
	protected IReportContext context;
	protected IDataRowExpressionEvaluator evaluator;

	protected final static String REPORT_VARIABLE_INDICATOR = "vars"; //$NON-NLS-1$

	/**
	 * This map is used to cache evaluated script for reducing evaluation overhead
	 */
	private Map<String, String> cacheScriptEvaluator;

	private static ILogger logger = Logger.getLogger("org.eclipse.birt.chart.reportitem/trace"); //$NON-NLS-1$

	/**
	 * Used to implement IAction in various cases
	 */
	protected abstract class ChartHyperlinkActionBase implements IAction {

		private ActionHandle handle;

		protected ChartHyperlinkActionBase(ActionHandle handle) {
			this.handle = handle;
		}

		public abstract Object evaluate(String expr);

		public int getType() {
			return BIRTActionRenderer.this.getType(handle);
		}

		public String getBookmark() {
			Object value = evaluate(handle.getTargetBookmark());
			if (value instanceof Number) {
				return ChartUtil.getDefaultNumberFormat().format(value);
			}
			return ChartUtil.stringValue(value);
		}

		public String getActionString() {
			return BIRTActionRenderer.this.getActionString(this, handle);
		}

		public String getReportName() {
			return handle.getReportName();
		}

		@SuppressWarnings("rawtypes")
		public Map getParameterBindings() {
			return BIRTActionRenderer.this.getParameterBindings(this, handle);
		}

		@SuppressWarnings("rawtypes")
		public Map getSearchCriteria() {
			return BIRTActionRenderer.this.getSearchCriteria(this, handle);
		}

		public String getTargetWindow() {
			return handle.getTargetWindow();
		}

		public String getFormat() {
			return handle.getFormatType();
		}

		public boolean isBookmark() {
			return BIRTActionRenderer.this.isBookmark(handle);
		}

		public String getSystemId() {
			ModuleHandle mod = eih.getRoot();
			if (mod != null) {
				return mod.getFileName();
			}
			return null;
		}

		public String getTargetFileType() {
			return handle.getTargetFileType();
		}

		public String getTooltip() {
			return handle.getToolTip();
		}
	}

	/**
	 * The constructor.
	 * 
	 * @param handler
	 */
	public BIRTActionRenderer(DesignElementHandle eih, IHTMLActionHandler handler,
			IDataRowExpressionEvaluator evaluator, IReportContext context) {
		this.eih = eih;
		this.handler = handler;
		this.evaluator = evaluator;
		this.context = context;
	}

	protected Map<String, Object> getParameterBindings(ChartHyperlinkActionBase chAction, ActionHandle handle) {
		Map<String, Object> map = new HashMap<String, Object>();
		MemberHandle params = handle.getParamBindings();
		// Parameters may be null except for DrillThrough
		if (params != null) {
			for (Iterator<?> itr = params.iterator(); itr.hasNext();) {
				ParamBindingHandle pbh = (ParamBindingHandle) itr.next();
				map.put(pbh.getParamName(), chAction.evaluate(pbh.getExpression()));
			}
		}
		return map;
	}

	protected Map<String, Object> getSearchCriteria(ChartHyperlinkActionBase chAction, ActionHandle handle) {
		Map<String, Object> map = new HashMap<String, Object>();
		MemberHandle searches = handle.getSearch();
		// Searches may be null except for DrillThrough
		if (searches != null) {
			for (Iterator<?> itr = searches.iterator(); itr.hasNext();) {
				SearchKeyHandle skh = (SearchKeyHandle) itr.next();
				map.put(skh.getExpression(), chAction.evaluate(skh.getExpression()));
			}
		}
		return map;
	}

	protected int getType(ActionHandle handle) {
		if (DesignChoiceConstants.ACTION_LINK_TYPE_HYPERLINK.equals(handle.getLinkType()))
			return IAction.ACTION_HYPERLINK;
		if (DesignChoiceConstants.ACTION_LINK_TYPE_BOOKMARK_LINK.equals(handle.getLinkType()))
			return IAction.ACTION_BOOKMARK;
		if (DesignChoiceConstants.ACTION_LINK_TYPE_DRILL_THROUGH.equals(handle.getLinkType()))
			return IAction.ACTION_DRILLTHROUGH;
		return 0;
	}

	protected String getActionString(ChartHyperlinkActionBase chAction, ActionHandle handle) {
		if (DesignChoiceConstants.ACTION_LINK_TYPE_HYPERLINK.equals(handle.getLinkType())) {
			ExpressionHandle urlExpr = handle
					.getExpressionProperty(org.eclipse.birt.report.model.api.elements.structures.Action.URI_MEMBER);
			String text = urlExpr.getStringExpression();
			if (ExpressionType.CONSTANT.equals(urlExpr.getType())) {
				return text;
			}
			return ChartUtil.stringValue(chAction.evaluate(text));
		}

		if (DesignChoiceConstants.ACTION_LINK_TYPE_BOOKMARK_LINK.equals(handle.getLinkType()))
			return ChartUtil.stringValue(chAction.evaluate(handle.getTargetBookmark()));
		return null;
	}

	protected boolean isBookmark(ActionHandle handle) {
		return DesignChoiceConstants.ACTION_BOOKMARK_TYPE_BOOKMARK.equals(handle.getTargetBookmarkType());
	}

	@Override
	public void processAction(Action action, StructureSource source, RunTimeContext rtc) {
		if (action instanceof MultipleActions) {
			for (Action subAction : ((MultipleActions) action).getActions()) {
				processActionImpl(subAction, source, rtc);
			}
		} else {
			processActionImpl(action, source, rtc);
		}
	}

	/**
	 * @param action
	 * @param source
	 */
	private void processActionImpl(Action action, StructureSource source, RunTimeContext rtc) {
		if (ActionType.URL_REDIRECT_LITERAL.equals(action.getType())) {
			if (action.getValue() instanceof URLValue) {
				URLValue uv = (URLValue) action.getValue();
				generateURLValue(source, uv);
			} else if (action.getValue() instanceof MultiURLValues) {
				for (URLValue uv : ((MultiURLValues) action.getValue()).getURLValues()) {
					generateURLValue(source, uv);
				}
			}
		} else if (ActionType.SHOW_TOOLTIP_LITERAL.equals(action.getType())) {
			processTooltipAction(action, source, rtc);
		} else if (ActionType.INVOKE_SCRIPT_LITERAL.equals(action.getType())) {
			ScriptValue sv = (ScriptValue) action.getValue();
			if (cacheScriptEvaluator == null) {
				cacheScriptEvaluator = new HashMap<String, String>();
			}
			String evaluatResult = cacheScriptEvaluator.get(sv.getScript());
			if (evaluatResult == null) {
				evaluatResult = evaluateExpression(sv.getScript());
				cacheScriptEvaluator.put(sv.getScript(), evaluatResult);
			}
			sv.setScript(evaluatResult);
		}
	}

	public static void processTooltipAction(Action action, StructureSource source, RunTimeContext rtc) {
		TooltipValue tv = (TooltipValue) action.getValue();
		FormatSpecifier fs = tv.getFormatSpecifier();
		if (StructureType.SERIES_DATA_POINT.equals(source.getType())) {
			final DataPointHints dph = (DataPointHints) source.getSource();
			if (!dph.isVirtual()) {
				// Output chart variable values directly
				if (ScriptHandler.VARIABLE_CATEGORY.equals(tv.getText())) {
					tv.setText(dph.getBaseDisplayValue(fs));
				} else if (ScriptHandler.VARIABLE_VALUE.equals(tv.getText())) {
					tv.setText(dph.getOrthogonalDisplayValue(fs));
				} else if (ScriptHandler.VARIABLE_SERIES.equals(tv.getText())) {
					tv.setText(dph.getSeriesDisplayValue(fs));
				} else {
					// Get evaluated values in other expressions
					Object value = dph.getUserValue(tv.getText());
					if (fs != null) {
						try {
							tv.setText(ValueFormatter.format(value, fs, rtc.getULocale(), null));
						} catch (ChartException e) {
							logger.log(e);
						}
					} else {
						if (value instanceof Number) {
							tv.setText(ChartUtil.getDefaultNumberFormat().format(value));
						} else {
							tv.setText(ChartUtil.stringValue(value));
						}
					}
				}
			} else {
				tv.setText(null);
			}
		} else if (StructureType.LEGEND_ENTRY.equals(source.getType())) {
			LegendItemHints lih = (LegendItemHints) source.getSource();
			if (tv.getText() == null || tv.getText().equals("")) //$NON-NLS-1$
			{
				tv.setText(lih.getItemText());
			}
		}
	}

	protected ActionHandle getActionHandleInstance(String sa) {
		ActionHandle handle = null;
		try {
			handle = ModuleUtil.deserializeAction(sa);
		} catch (Exception e) {
			sa = ""; //$NON-NLS-1$
			logger.log(e);
		}
		return handle;
	}

	/**
	 * @param source
	 * @param uv
	 */
	protected void generateURLValue(StructureSource source, URLValue uv) {
		String sa = uv.getBaseUrl();
		String target = null;

		final ActionHandle handle = getActionHandleInstance(sa);

		if (handle != null) {
			setTooltip(uv, handle);
			target = handle.getTargetWindow();

			// use engine api to convert actionHandle to a final url value.
			if (StructureType.SERIES_DATA_POINT.equals(source.getType())) {
				final DataPointHints dph = (DataPointHints) source.getSource();

				sa = handler.getURL(new ChartHyperlinkActionBase(handle) {

					@Override
					public Object evaluate(String expr) {
						return dph.getUserValue(expr);
					}
				}, context);
			} else if (StructureType.LEGEND_ENTRY.equals(source.getType())) {
				final LegendItemHints lih = (LegendItemHints) source.getSource();
				sa = handler.getURL(new ChartHyperlinkActionBase(handle) {

					@Override
					public Object evaluate(String expr) {
						if (expr != null) {
							// Replace special expression in Legend item
							// before evaluation.
							// Note that according to #259469, string are
							// not with quotation sign.
							if (expr.indexOf(LEGEND_ITEM_TEXT) >= 0) {
								String legendItemText = wrapQuotation(lih.getItemText());
								expr = Pattern.compile(LEGEND_ITEM_TEXT, Pattern.LITERAL).matcher(expr)
										.replaceAll(legendItemText);
							}
							if (expr.indexOf(LEGEND_ITEM_VALUE) >= 0) {
								String legendItemValue = wrapQuotation(lih.getValueText());
								expr = Pattern.compile(LEGEND_ITEM_VALUE, Pattern.LITERAL).matcher(expr)
										.replaceAll(legendItemValue);
							}
						}
						return evaluator.evaluate(expr);
					}

				}, context);
			} else {
				sa = handler.getURL(new ChartHyperlinkActionBase(handle) {

					@Override
					public Object evaluate(String expr) {
						return evaluator.evaluate(expr);
					}
				}, context);
			}
		} else {
			sa = ""; //$NON-NLS-1$
		}

		uv.setBaseUrl(sa);
		uv.setTarget(target);
	}

	/**
	 * Set the tooltip.
	 * 
	 * @param uv
	 * @param handle
	 * @since 2.3
	 */
	protected void setTooltip(URLValue uv, final ActionHandle handle) {
		if (handle.getToolTip() != null && handle.getToolTip().trim().length() > 0) {
			uv.setTooltip(handle.getToolTip());
		}
	}

	protected String evaluateExpression(String script) {
		if (script == null || script.trim().length() == 0) {
			return ""; //$NON-NLS-1$
		}
		String expression = findParameterExp(script, 0);
		while (expression != null) {
			Object evaluateResult = evaluator.evaluate(expression);
			// Bugzilla#242667 Add double quotation signs automatically
			if (evaluateResult instanceof String) {
				evaluateResult = "\"" + evaluateResult + "\""; //$NON-NLS-1$//$NON-NLS-2$
			} else if (evaluateResult instanceof Date) {
				evaluateResult = "\"" + evaluateResult.toString() + "\""; //$NON-NLS-1$//$NON-NLS-2$
			} else if (evaluateResult == null) {
				evaluateResult = "null"; //$NON-NLS-1$
			}

			script = Pattern.compile(expression, Pattern.LITERAL).matcher(script).replaceAll(evaluateResult.toString());

			// Repeat to process all parameter expressions.
			expression = findParameterExp(script, 0);
		}

		expression = findVariableExp(script, 0);
		while (expression != null) {
			Object evaluateResult = evaluator.evaluate(expression);
			// Bugzilla#242667 Add double quotation signs automatically
			if (evaluateResult instanceof String) {
				evaluateResult = "\"" + evaluateResult + "\""; //$NON-NLS-1$//$NON-NLS-2$
			} else if (evaluateResult == null) {
				evaluateResult = "null"; //$NON-NLS-1$
			}

			script = Pattern.compile(expression, Pattern.LITERAL).matcher(script).replaceAll(evaluateResult.toString());

			// Repeat to process all parameter expressions.
			expression = findVariableExp(script, 0);
		}
		return script;
	}

	private static String findVariableExp(String script, int fromIndex) {
		int iStart = script.indexOf(REPORT_VARIABLE_INDICATOR + '[', fromIndex);
		if (iStart < fromIndex) {
			return null;
		}
		int iEnd = script.indexOf(']', iStart);
		if (iEnd < iStart + REPORT_VARIABLE_INDICATOR.length()) {
			return null;
		}
		return script.substring(iStart, iEnd + 1);
	}

	private static String findParameterExp(String script, int fromIndex) {
		int iStart = script.indexOf(ExpressionUtil.PARAMETER_INDICATOR + '[', fromIndex);
		if (iStart < fromIndex) {
			return null;
		}
		int iEnd = script.indexOf(']', iStart);
		if (iEnd < iStart + ExpressionUtil.PARAMETER_INDICATOR.length()) {
			return null;
		}
		return script.substring(iStart, iEnd + 1 + ExpressionUtil.EXPRESSION_VALUE_SUFFIX.length());
	}

	private static String wrapQuotation(String value) {
		if (value == null) {
			return ""; //$NON-NLS-1$
		}
		try {
			Double.parseDouble(value);
		} catch (RuntimeException e) {
			value = '"' + value + '"';
		}
		return value;
	}

}
