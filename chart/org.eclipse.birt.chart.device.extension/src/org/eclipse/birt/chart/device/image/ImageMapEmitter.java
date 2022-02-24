/*******************************************************************************
 * Copyright (c) 2007 Actuate Corporation.
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.chart.device.image;

import java.awt.Shape;
import java.awt.geom.FlatteningPathIterator;
import java.awt.geom.PathIterator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import org.eclipse.birt.chart.computation.DataPointHints;
import org.eclipse.birt.chart.computation.LegendItemHints;
import org.eclipse.birt.chart.device.IImageMapEmitter;
import org.eclipse.birt.chart.device.swing.ShapedAction;
import org.eclipse.birt.chart.device.util.CSSHelper;
import org.eclipse.birt.chart.device.util.HTMLAttribute;
import org.eclipse.birt.chart.device.util.HTMLEncoderAdapter;
import org.eclipse.birt.chart.device.util.HTMLTag;
import org.eclipse.birt.chart.device.util.ICharacterEncoderAdapter;
import org.eclipse.birt.chart.device.util.ScriptUtil;
import org.eclipse.birt.chart.event.StructureSource;
import org.eclipse.birt.chart.event.StructureType;
import org.eclipse.birt.chart.model.attribute.ActionType;
import org.eclipse.birt.chart.model.attribute.ActionValue;
import org.eclipse.birt.chart.model.attribute.CursorType;
import org.eclipse.birt.chart.model.attribute.MultiURLValues;
import org.eclipse.birt.chart.model.attribute.ScriptValue;
import org.eclipse.birt.chart.model.attribute.TooltipValue;
import org.eclipse.birt.chart.model.attribute.TriggerCondition;
import org.eclipse.birt.chart.model.attribute.URLValue;
import org.eclipse.birt.chart.model.data.Action;
import org.eclipse.birt.chart.model.data.MultipleActions;
import org.eclipse.birt.chart.render.IActionRenderer;
import org.eclipse.birt.chart.script.ScriptHandler;
import org.eclipse.birt.core.script.JavascriptEvalUtil;

import com.ibm.icu.util.ULocale;

/**
 * 
 */

public class ImageMapEmitter implements IImageMapEmitter {

	private List<ShapedAction> saList = null;

	private boolean _bAltEnabled = false;

	private ULocale locale = null;

	private final static String NO_OP_JAVASCRIPT = "javascript:void(0);"; //$NON-NLS-1$

	private final static String POLY_SHAPE = "poly"; //$NON-NLS-1$

	// Use this registry to make sure one callback method only be added once
	private Map<String, Boolean> callbackMethodsRegistry = new HashMap<String, Boolean>(5);

	private volatile boolean hasMultipleMenu = false;

	private volatile boolean hasAddedMenuLib = false;

	private ICharacterEncoderAdapter encoderAdapter = HTMLEncoderAdapter.getInstance();

	private int dpi = 72;

	public ImageMapEmitter(List<ShapedAction> saList, boolean bAltEnabled, ULocale locale) {
		this.saList = saList;
		this._bAltEnabled = bAltEnabled;
		this.locale = locale;
	}

	public ImageMapEmitter(List<ShapedAction> saList, boolean bAltEnabled, ULocale locale, int dpi) {
		this.saList = saList;
		this._bAltEnabled = bAltEnabled;
		this.locale = locale;
		this.dpi = dpi;
	}

	public String getImageMap() {
		Collections.sort(saList, new Comparator<ShapedAction>() {

			public int compare(ShapedAction o1, ShapedAction o2) {
				// z-order value is marker size, not the z-order in series
				// definition.
				if (o1.getZOrder() == 0 || o2.getZOrder() == 0) {
					// do not need to sort for which marker size is 0 since its
					// sequence will be correct after reverse later.
					return 0;
				} else {
					return o2.getZOrder() - o1.getZOrder();
				}
			}

		});

		if (saList == null || saList.size() == 0) {
			return null;
		}

		// Generate image map using associated trigger list.
		StringBuffer sb = new StringBuffer();
		// reverse output
		for (ListIterator<ShapedAction> iter = saList.listIterator(saList.size()); iter.hasPrevious();) {
			ShapedAction sa = iter.previous();
			userCallback(sa, sb);

			String coords = shape2polyCoords(sa.getShape());
			if (coords != null) {
				HTMLTag tag = new HTMLTag("area"); //$NON-NLS-1$
				tag.addAttribute(HTMLAttribute.SHAPE, POLY_SHAPE);
				tag.addAttribute(HTMLAttribute.COORDS, coords);
				// #258627 "area" must has a "alt" value.
				// Add alt value if specified via extend property
				tag.addAttribute(HTMLAttribute.ALT, _bAltEnabled ? sa.getSource().getSource().toString() : ""); //$NON-NLS-1$

				// Update cursor.
				setCursorAttribute(tag, sa);

				boolean changed = false;
				changed |= processOnFocus(sa, tag);
				changed |= processOnBlur(sa, tag);
				changed |= processOnClick(sa, tag);
				changed |= processOnDoubleClick(sa, tag);
				changed |= processOnMouseOver(sa, tag);
				if (changed) {
					sb.append(tag);
				}
			}
		}

		return sb.toString();
	}

	private void setCursorAttribute(HTMLTag tag, ShapedAction sa) {
		if (sa.getCursor() == null || sa.getCursor().getType() == CursorType.AUTO) {
			return;
		}

		String value = CSSHelper.getCSSCursorValue(sa.getCursor());
		if (value != null) {
			tag.addAttribute(HTMLAttribute.STYLE, value);
		}
	}

	private boolean processCommonEvent(ShapedAction sa, HTMLTag tag, TriggerCondition condition,
			HTMLAttribute htmlAttr) {
		Action ac = sa.getActionForCondition(condition);
		if (checkSupportedAction(ac)) {
			if (ac instanceof MultipleActions) {
				List<Action> validActions = MultiActionValuesScriptGenerator.getValidActions((MultipleActions) ac);
				int size = validActions.size();
				if (size == 0) {
					return false;
				} else if (size == 1) {
					Action subAction = validActions.get(0);
					if (subAction.getValue() instanceof URLValue) {
						setURLValueAttributes(tag, condition, htmlAttr, (URLValue) subAction.getValue(), null);
						return true;
					} else if (subAction.getValue() instanceof ScriptValue) {
						setAttributesWithScript(sa, tag, condition, htmlAttr);
						return true;
					}
				} else {
					setAttributesWithScript(sa, tag, condition, htmlAttr);
					return true;
				}
			} else {
				switch (ac.getType().getValue()) {
				case ActionType.URL_REDIRECT:
					if (ac.getValue() instanceof MultiURLValues) {
						List<URLValue> validURLValues = MultiActionValuesScriptGenerator
								.getValidURLValues((MultiURLValues) ac.getValue());
						int size = validURLValues.size();
						if (size == 0) {
							setTooltipAttribute(tag, ((MultiURLValues) ac.getValue()).getTooltip());
							return false;
						} else if (size == 1) {
							URLValue uv = validURLValues.get(0);
							// When there is only one link, set URLValue tooltip if it's
							// not null. Otherwise, set MultiUrlvalues tooltip.
							setURLValueAttributes(tag, condition, htmlAttr, uv,
									((MultiURLValues) ac.getValue()).getTooltip());
							return true;
						} else {
							setTooltipAttribute(tag, ((MultiURLValues) ac.getValue()).getTooltip());
							setAttributesWithScript(sa, tag, condition, htmlAttr);
							return true;
						}
					} else {
						URLValue uv = (URLValue) ac.getValue();
						setURLValueAttributes(tag, condition, htmlAttr, uv, null);
						return true;
					}

				case ActionType.SHOW_TOOLTIP:
					// for onmouseover only.
					return false;
				case ActionType.INVOKE_SCRIPT:
					setAttributesWithScript(sa, tag, condition, htmlAttr);
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * @param tag
	 * @param tooltip
	 */
	private void setTooltipAttribute(HTMLTag tag, String tooltip) {
		if (tooltip != null && tooltip.trim().length() > 0) {
			tag.addAttribute(HTMLAttribute.TITLE, eval2HTML(tooltip));
		}
	}

	/**
	 * @param sa
	 * @param tag
	 * @param condition
	 * @param htmlAttr
	 */
	private void setAttributesWithScript(ShapedAction sa, HTMLTag tag, TriggerCondition condition,
			HTMLAttribute htmlAttr) {
		tag.addAttribute(HTMLAttribute.HREF, NO_OP_JAVASCRIPT);
		StringBuffer callbackFunction = new StringBuffer(getJSMethodName(condition, sa));
		addCallBackScript(sa, callbackFunction);
		tag.addAttribute(htmlAttr, eval2JS(callbackFunction.toString(), true));
	}

	/*
	 * To remove the surrounding double quotes if there is any.
	 */
	private String removeSurroundingQuotes(String sBaseUrl) {
		if (sBaseUrl == null) {
			return null;
		}

		if (sBaseUrl.length() > 0) {
			int iEnd = sBaseUrl.length() - 1;
			if (sBaseUrl.charAt(0) == '\"' && sBaseUrl.charAt(iEnd) == '\"') {
				return sBaseUrl.substring(1, iEnd);
			}
		}

		return sBaseUrl;
	}

	/**
	 * @param tag
	 * @param condition
	 * @param htmlAttr
	 * @param uv
	 * @param tooltip
	 */
	private void setURLValueAttributes(HTMLTag tag, TriggerCondition condition, HTMLAttribute htmlAttr, URLValue uv,
			String tooltip) {
		if (uv != null && uv.getTooltip() != null) {
			setTooltipAttribute(tag, uv.getTooltip());
		} else {
			setTooltipAttribute(tag, tooltip);
		}

		if (condition == TriggerCondition.ONCLICK_LITERAL) {
			// only click event uses href to redirect
			tag.addAttribute(HTMLAttribute.HREF, eval2HTML(removeSurroundingQuotes(uv.getBaseUrl())));
			// #258627: "target" can't be a empty String. You
			// shouldn't output target when it's empty.
			if (uv.getTarget() != null) {
				tag.addAttribute(HTMLAttribute.TARGET, uv.getTarget());
			}
		} else {
			tag.addAttribute(HTMLAttribute.HREF, NO_OP_JAVASCRIPT);
			String value = getJsURLRedirect(uv);
			if (htmlAttr.equals(HTMLAttribute.ONFOCUS)) {
				value = "this.blur();" + value;//$NON-NLS-1$
			}
			tag.addAttribute(htmlAttr, value);
		}
	}

	protected boolean processOnFocus(ShapedAction sa, HTMLTag tag) {
		// 1. onfocus
		return processCommonEvent(sa, tag, TriggerCondition.ONFOCUS_LITERAL, HTMLAttribute.ONFOCUS);
	}

	protected boolean processOnBlur(ShapedAction sa, HTMLTag tag) {
		// 2. onblur
		return processCommonEvent(sa, tag, TriggerCondition.ONBLUR_LITERAL, HTMLAttribute.ONBLUR);
	}

	protected boolean processOnClick(ShapedAction sa, HTMLTag tag) {
		// 3. onclick
		return processCommonEvent(sa, tag, TriggerCondition.ONCLICK_LITERAL, HTMLAttribute.ONCLICK);
	}

	protected boolean processOnDoubleClick(ShapedAction sa, HTMLTag tag) {
		// ondblclick
		return processCommonEvent(sa, tag, TriggerCondition.ONDBLCLICK_LITERAL, HTMLAttribute.ONDBLCLICK);
	}

	protected boolean processOnMouseOver(ShapedAction sa, HTMLTag tag) {
		// 4. onmouseover
		Action ac = sa.getActionForCondition(TriggerCondition.ONMOUSEOVER_LITERAL);
		if (checkSupportedAction(ac)) {
			switch (ac.getType().getValue()) {
			case ActionType.URL_REDIRECT:
				// not for onmouseover.
				return false;
			case ActionType.SHOW_TOOLTIP:
				TooltipValue tv = (TooltipValue) ac.getValue();
				// only add valid tooltip
				if (tv.getText() != null && tv.getText().length() > 0) {
					tag.addAttribute(HTMLAttribute.TITLE, eval2HTML(tv.getText()));
					return true;
				}
				return false;
			case ActionType.INVOKE_SCRIPT:
				StringBuffer callbackFunction = new StringBuffer(
						getJSMethodName(TriggerCondition.ONMOUSEOVER_LITERAL, sa));
				addCallBackScript(sa, callbackFunction);
				tag.addAttribute(HTMLAttribute.ONMOUSEOVER, eval2JS(callbackFunction.toString(), true));
				return true;
			}
		}
		return false;
	}

	protected String getJsURLRedirect(URLValue uv) {
		String sBaseUrl = uv.getBaseUrl() == null ? "" : uv.getBaseUrl();//$NON-NLS-1$
		sBaseUrl = removeSurroundingQuotes(sBaseUrl);
		if (sBaseUrl.startsWith("javascript:")) //$NON-NLS-1$
		{
			return sBaseUrl;
		}
		if (sBaseUrl.startsWith("#")) //$NON-NLS-1$
		{
			return "window.location='" + eval2HTML(sBaseUrl) + "'"; //$NON-NLS-1$ //$NON-NLS-2$
		}
		return "window.open('" //$NON-NLS-1$
				+ eval2HTML(sBaseUrl) + "','" + (uv.getTarget() == null ? "self" : uv.getTarget()) + "')"; //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$
	}

	/**
	 * Convert AWT shape to image map coordinates.
	 * 
	 * @param shape
	 * @return
	 */
	private String shape2polyCoords(Shape shape) {
		if (shape == null) {
			return null;
		}

		ArrayList<Double> al = new ArrayList<Double>();

		FlatteningPathIterator pitr = new FlatteningPathIterator(shape.getPathIterator(null), 1);
		double[] data = new double[6];

		while (!pitr.isDone()) {
			int type = pitr.currentSegment(data);

			switch (type) {
			case PathIterator.SEG_MOVETO:
				al.add(new Double(data[0]));
				al.add(new Double(data[1]));
				break;
			case PathIterator.SEG_LINETO:
				al.add(new Double(data[0]));
				al.add(new Double(data[1]));
				break;
			case PathIterator.SEG_QUADTO:
				al.add(new Double(data[0]));
				al.add(new Double(data[1]));
				al.add(new Double(data[2]));
				al.add(new Double(data[3]));
				break;
			case PathIterator.SEG_CUBICTO:
				al.add(new Double(data[0]));
				al.add(new Double(data[1]));
				al.add(new Double(data[2]));
				al.add(new Double(data[3]));
				al.add(new Double(data[4]));
				al.add(new Double(data[5]));
				break;
			case PathIterator.SEG_CLOSE:
				break;
			}

			pitr.next();
		}

		if (al.size() == 0) {
			return null;
		}

		StringBuffer sb = new StringBuffer();

		for (int i = 0; i < al.size(); i++) {
			Double db = al.get(i);
			if (i > 0) {
				sb.append(","); //$NON-NLS-1$
			}
			sb.append((int) translateCoor(db.doubleValue()));
		}

		return sb.toString();
	}

	private double translateCoor(double d) {
		// Bugzilla 456382
		// This function is supposed to translate the chart renderer coordinates
		// (as the coordinates are obtained when rendering the chart renderers)
		// to the browser display coordinates (as the image map is directly output
		// as the <map> and <area> tag in the web page
		// The unit is translated from pt in chart renderer to pixel in browser
		// As we do not know the exact DPI for the client browser, we use 96 based
		// on the experiment on most browsers
		// refer to http://reeddesign.co.uk/test/points-pixels.html.
		return d / dpi * 96d;
	}

	private boolean checkSupportedAction(Action action) {
		return (action != null && (action.getType() == ActionType.URL_REDIRECT_LITERAL
				|| action.getType() == ActionType.SHOW_TOOLTIP_LITERAL
				|| action.getType() == ActionType.INVOKE_SCRIPT_LITERAL));
	}

	protected String eval2HTML(String expr) {
		return encoderAdapter.escape(expr);
	}

	protected String eval2JS(String expr, boolean bCallback) {
		if (expr == null) {
			return ""; //$NON-NLS-1$
		}
		if (bCallback) {
			// Do not eval script since it's not quoted in callback method
			return expr;
		}
		return JavascriptEvalUtil.transformToJsConstants(expr);
	}

	/**
	 * When 1). The action is supported, and the action type is INVOKE_SCRIPT. 2).
	 * The script has not been added into ImageMap. 3). The action acts on the value
	 * series area. Add the script into ImageMap.
	 * 
	 * @param sa ShapedAction
	 * @param sb StringBuffer
	 */
	private void userCallback(ShapedAction sa, StringBuffer sb) {
		addCallbackMethod(sa, sb, TriggerCondition.ONCLICK_LITERAL);
		addCallbackMethod(sa, sb, TriggerCondition.ONDBLCLICK_LITERAL);
		addCallbackMethod(sa, sb, TriggerCondition.ONMOUSEOVER_LITERAL);
		addCallbackMethod(sa, sb, TriggerCondition.ONFOCUS_LITERAL);
		addCallbackMethod(sa, sb, TriggerCondition.ONBLUR_LITERAL);

		// Insert chart menu scripts.
		if (hasMultipleMenu && !hasAddedMenuLib) {
			sb.insert(0, "<Script>" + MultiActionValuesScriptGenerator.getBirtChartMenuLib() + "</Script>"); //$NON-NLS-1$ //$NON-NLS-2$
			hasAddedMenuLib = true;
		}
	}

	private void addCallbackMethod(ShapedAction sa, StringBuffer sb, TriggerCondition condition) {
		// Use the event type and action type as the key and function name to
		// handle multiple invoke script events in a chart
		String functionName = getJSMethodName(condition, sa);
		String key = condition.getLiteral() + functionName;
		if (!callbackMethodsRegistry.containsKey(key)) {
			addScriptCallBack(sa, sb, sa.getActionForCondition(condition), functionName);
			callbackMethodsRegistry.put(key, Boolean.TRUE);
		}
	}

	private void addScriptCallBack(ShapedAction sa, StringBuffer sb, Action ac, String functionName) {
		if (ac != null) {
			if (ac instanceof MultipleActions) {
				if (((MultipleActions) ac).getActions().size() > 0) {
					sb.append(wrapJSMethod(functionName, generateJSContent(ac)));
				}
			}
			// Do not use callback methods for URL_redirect since the target
			// method may be in another page
			else if (ac.getType().getValue() == ActionType.INVOKE_SCRIPT) {
				sb.append(wrapJSMethod(functionName, generateJSContent(ac)));
			} else if (ac.getType().getValue() == ActionType.URL_REDIRECT) {
				// Only generate a menu in JS function for multiple URL values.
				if (ac.getValue() instanceof MultiURLValues
						&& ((MultiURLValues) ac.getValue()).getURLValues().size() > 1) {
					sb.append(wrapJSMethod(functionName, generateJSContent(ac)));
				}
			}
		}
	}

	private String generateUniqueJSKey(Action ac) {
		if (ac == null) {
			return ""; //$NON-NLS-1$
		}

		if (ac instanceof MultipleActions) {
			List<Action> subActions = ((MultipleActions) ac).getActions();
			if (subActions.size() <= 1) {
				return generateJSContent(subActions.get(0));
			}

			// Generate a unique JS key for multiple URL values.
			return MultiActionValuesScriptGenerator.getJSKey((MultipleActions) ac, locale) + this.hashCode();
		} else if (ac.getValue() instanceof MultiURLValues) {
			MultiURLValues values = (MultiURLValues) ac.getValue();
			if (values.getURLValues().size() <= 1) {
				return generateJSContent(ac);
			}

			// Generate a unique JS key for multiple URL values.
			return MultiActionValuesScriptGenerator.getJSKey(values) + this.hashCode();
		}

		return generateJSContent(ac);

	}

	private String generateJSContent(Action ac) {
		if (ac != null) {
			if (ac instanceof MultipleActions) {
				// Still show menu if visible is true.
				boolean needMenu = ac.getValue() != null && ac.getValue().getLabel() != null
						&& ac.getValue().getLabel().isVisible();

				List<Action> validActions = MultiActionValuesScriptGenerator.getValidActions((MultipleActions) ac);
				if (validActions.size() == 0) {
					return ""; //$NON-NLS-1$
				} else if (validActions.size() == 1 && !needMenu) {
					ActionValue av = validActions.get(0).getValue();
					if (av instanceof URLValue) {
						return getJsURLRedirect((URLValue) av);
					} else if (av instanceof ScriptValue) {
						return ((ScriptValue) av).getScript();
					}
				} else {
					// Return multiple menu javascript.
					hasMultipleMenu = true;
					return MultiActionValuesScriptGenerator.getJSContent((MultipleActions) ac, locale);
				}
			} else if (ac.getType().getValue() == ActionType.INVOKE_SCRIPT) {
				ScriptValue sv = (ScriptValue) ac.getValue();
				return sv.getScript();
			}
			if (ac.getType().getValue() == ActionType.URL_REDIRECT) {
				ActionValue value = ac.getValue();
				if (value instanceof URLValue) {
					URLValue uv = (URLValue) ac.getValue();
					return getJsURLRedirect(uv);
				} else if (value instanceof MultiURLValues) {
					List<URLValue> validURLValues = MultiActionValuesScriptGenerator
							.getValidURLValues((MultiURLValues) value);
					if (validURLValues.size() == 0) {
						return ""; //$NON-NLS-1$
					} else if (validURLValues.size() == 1) {
						return getJsURLRedirect(validURLValues.get(0));
					} else {
						// Return multiple menu javascript.
						hasMultipleMenu = true;
						return MultiActionValuesScriptGenerator.getJSContent(value);
					}
				}
			}
		}
		return ""; //$NON-NLS-1$
	}

	private String wrapJSMethod(String functionName, String functionContent) {
		return "<Script>" //$NON-NLS-1$
				+ "function " //$NON-NLS-1$
				+ functionName + "(evt,"//$NON-NLS-1$
				+ ScriptHandler.BASE_VALUE + ", "//$NON-NLS-1$
				+ ScriptHandler.ORTHOGONAL_VALUE + ", "//$NON-NLS-1$
				+ ScriptHandler.SERIES_VALUE + ", "//$NON-NLS-1$
				+ IActionRenderer.LEGEND_ITEM_DATA + ", "//$NON-NLS-1$
				+ IActionRenderer.LEGEND_ITEM_TEXT + ", "//$NON-NLS-1$
				+ IActionRenderer.LEGEND_ITEM_VALUE + ", "//$NON-NLS-1$
				+ IActionRenderer.AXIS_LABEL + "){"//$NON-NLS-1$
				+ eval2JS(functionContent, true) + "};</Script>"; //$NON-NLS-1$
	}

	private String getJSMethodName(TriggerCondition tc, ShapedAction sa) {
		// Bugzilla#203044
		// Always use hashcode of script content to generate function name in
		// case that the functions of two charts may have the same name
		int hashCode = generateUniqueJSKey(sa.getActionForCondition(tc)).hashCode();
		if (hashCode != Integer.MIN_VALUE) {
			return "userCallBack" //$NON-NLS-1$
					+ Math.abs(hashCode);
		} else {
			return "userCallBack" //$NON-NLS-1$
					+ Integer.MAX_VALUE;
		}
	}

	private void addCallBackScript(ShapedAction sa, StringBuffer callbackFunction) {
		StructureSource source = sa.getSource();
		final DataPointHints dph;
		final LegendItemHints lerh;
		final String axisLabel;
		if (StructureType.SERIES_DATA_POINT.equals(source.getType())) {
			dph = (DataPointHints) source.getSource();
		} else {
			dph = null;
		}
		if (StructureType.LEGEND_ENTRY.equals(source.getType())) {
			lerh = (LegendItemHints) source.getSource();
		} else {
			lerh = null;
		}
		if (StructureType.AXIS_LABEL.equals(source.getType())) {
			axisLabel = (String) source.getSource();
		} else {
			axisLabel = null;
		}
		callbackFunction.append("(event");//$NON-NLS-1$
		ScriptUtil.script(callbackFunction, dph, lerh, axisLabel);
		callbackFunction.append(");"); //$NON-NLS-1$
	}

}
