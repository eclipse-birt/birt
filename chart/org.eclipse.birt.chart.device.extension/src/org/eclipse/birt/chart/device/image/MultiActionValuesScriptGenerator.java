/*******************************************************************************
 * Copyright (c) 2009 Actuate Corporation.
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

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import org.eclipse.birt.chart.device.IScriptMenuHelper;
import org.eclipse.birt.chart.device.ScriptMenuHelper;
import org.eclipse.birt.chart.device.util.CSSHelper;
import org.eclipse.birt.chart.device.util.HTMLEncoderAdapter;
import org.eclipse.birt.chart.device.util.ICharacterEncoderAdapter;
import org.eclipse.birt.chart.log.ILogger;
import org.eclipse.birt.chart.log.Logger;
import org.eclipse.birt.chart.model.attribute.ActionType;
import org.eclipse.birt.chart.model.attribute.ActionValue;
import org.eclipse.birt.chart.model.attribute.MenuStylesKeyType;
import org.eclipse.birt.chart.model.attribute.MultiURLValues;
import org.eclipse.birt.chart.model.attribute.ScriptValue;
import org.eclipse.birt.chart.model.attribute.TooltipValue;
import org.eclipse.birt.chart.model.attribute.URLValue;
import org.eclipse.birt.chart.model.data.Action;
import org.eclipse.birt.chart.model.data.MultipleActions;
import org.eclipse.emf.common.util.EMap;

import com.ibm.icu.util.ULocale;

/**
 * The class generates JavaScript contents for multiple URL values.
 * 
 * @since 2.5
 */

public class MultiActionValuesScriptGenerator {
	private static ILogger logger = Logger.getLogger("org.eclipse.birt.chart.device.extension/image"); //$NON-NLS-1$

	private static String MENU_JS_CODE;

	private static IScriptMenuHelper SCRIPT_MENU_HELPER = ScriptMenuHelper.instance();

	/**
	 * Returns javascript content.
	 * 
	 * @return js content
	 */
	public static String getJSContent(ActionValue values) {
		StringBuilder sb = getJSContext(values);

		sb.append("var popMenu = BirtChartMenuHelper.createPopupMenu(evt, menuInfo);\n");//$NON-NLS-1$
		sb.append("if ( popMenu && popMenu != null ) popMenu.show();\n");//$NON-NLS-1$
		return sb.toString();
	}

	public static String getJSContent(MultipleActions actions, ULocale locale) {
		StringBuilder sb = getJSContext(actions, locale);

		// Still show menu if visible is true.
		if (actions.getValue() != null && actions.getValue().getLabel() != null
				&& actions.getValue().getLabel().isVisible()) {
			sb.append("menuInfo.needMenu = true;"); //$NON-NLS-1$
		}

		sb.append("if ( menuInfo.menuItemNames.length == 1 && !menuInfo.needMenu ) {\n");//$NON-NLS-1$
		sb.append("	BirtChartMenuHelper.executeMenuAction( evt, menuInfo.menuItems[0], menuInfo );\n");//$NON-NLS-1$
		sb.append("} else { \n");//$NON-NLS-1$
		sb.append("	var popMenu = BirtChartMenuHelper.createPopupMenu(evt, menuInfo);\n");//$NON-NLS-1$
		sb.append("	if ( popMenu && popMenu != null ) popMenu.show();\n");//$NON-NLS-1$
		sb.append("}");//$NON-NLS-1$
		return sb.toString();
	}

	/**
	 * Returns javascript key for current URL values.
	 * 
	 * @return js key
	 */
	public static String getJSKey(ActionValue values) {
		return getJSContext(values).toString();
	}

	public static String getJSKey(MultipleActions actions, ULocale locale) {
		return getJSContext(actions, locale).toString();
	}

	private static StringBuilder getJSContext(ActionValue values) {
		StringBuilder sb = new StringBuilder();
		if (values instanceof MultiURLValues) {
			MultiURLValues muv = (MultiURLValues) values;
			sb.append("\n\t var menuInfo = new BirtChartMenuInfo();\n"); //$NON-NLS-1$

			EMap<String, String> propMap = muv.getPropertiesMap();
			sb.append(getPropertiesJS(propMap).toString());
			int i = 0;
			for (URLValue uv : getValidURLValues(muv)) {
				sb = getURLValueJS(sb, i, uv, HTMLEncoderAdapter.getInstance());
				i++;
			}
		}

		appendInteractivityVariables(sb);

		return sb;
	}

	/**
	 * Appends chart variable values to scripts.
	 * 
	 * @param sb
	 */
	public static StringBuilder appendInteractivityVariables(StringBuilder sb) {
		sb.append("\t if ( typeof categoryData != 'undefined' ) menuInfo.categoryData = categoryData;\n");//$NON-NLS-1$
		sb.append("\t if ( typeof valueData != 'undefined' ) menuInfo.valueData = valueData;\n");//$NON-NLS-1$
		sb.append("\t if ( typeof valueSeriesName != 'undefined' ) menuInfo.valueSeriesName = valueSeriesName;\n");//$NON-NLS-1$

		sb.append("\t if ( typeof legendItemData != 'undefined' ) menuInfo.legendItemData = legendItemData;\n");//$NON-NLS-1$
		sb.append("\t if ( typeof legendItemText != 'undefined' ) menuInfo.legendItemText = legendItemText;\n");//$NON-NLS-1$
		sb.append("\t if ( typeof legendItemValue != 'undefined' ) menuInfo.legendItemValue = legendItemValue;\n");//$NON-NLS-1$
		sb.append("\t if ( typeof axisLabel != 'undefined' ) menuInfo.axisLabel = axisLabel;\n");//$NON-NLS-1$

		sb.append("\t if ( typeof id != 'undefined' ) menuInfo.id2 = id;\n");//$NON-NLS-1$
		sb.append("\t if ( typeof compList != 'undefined' ) menuInfo.compList = compList;\n");//$NON-NLS-1$
		sb.append("\t if ( typeof labelList != 'undefined' ) menuInfo.labelList = labelList;\n");//$NON-NLS-1$

		sb.append("\t menuInfo.id = " + sb.toString().hashCode() + ";\n");//$NON-NLS-1$//$NON-NLS-2$
		return sb;
	}

	private static StringBuilder getJSContext(MultipleActions actions, ULocale locale) {
		StringBuilder sb = new StringBuilder();
		sb.append("\n\t var menuInfo = new BirtChartMenuInfo();\n"); //$NON-NLS-1$

		EMap<String, String> propMap = actions.getPropertiesMap();
		sb.append(getPropertiesJS(propMap).toString());

		int i = 0;
		for (Action subAction : getValidActions(actions)) {
			ActionValue av = subAction.getValue();
			if (av instanceof URLValue) {
				sb = getURLValueJS(sb, i, (URLValue) av, HTMLEncoderAdapter.getInstance());
			} else if (av instanceof ScriptValue) {
				sb = getScriptValueJS(sb, i, (ScriptValue) av, locale);
			}
			i++;
		}

		appendInteractivityVariables(sb);

		return sb;
	}

	/**
	 * @param sv
	 * @return sb script value js
	 */
	public static StringBuilder getScriptValueJS(StringBuilder sb, int index, ScriptValue sv, ULocale locale) {
		if (index == 0) {
			sb.append("\t var mii = new BirtChartMenuItemInfo();\n");//$NON-NLS-1$
		} else {
			sb.append("\t mii = new BirtChartMenuItemInfo();\n");//$NON-NLS-1$
		}

		sb.append(SCRIPT_MENU_HELPER.getScriptValueJS(index, sv, locale));
		return sb;
	}

	/**
	 * @param sv
	 * @return visual js
	 */
	public static StringBuilder getVisualJS(StringBuilder sb, int index, ActionValue av, String scriptActionType) {
		if (index == 0) {
			sb.append("\t var mii = new BirtChartMenuItemInfo();");//$NON-NLS-1$
		} else {
			sb.append("\t mii = new BirtChartMenuItemInfo();");//$NON-NLS-1$
		}

		String text = av.getLabel().getCaption().getValue();
		if (av instanceof TooltipValue) {
			text = ((TooltipValue) av).getText();
		}
		sb.append("\t mii.text = '" + text + "';\n");//$NON-NLS-1$//$NON-NLS-2$
		sb.append("\t mii.actionType = " + scriptActionType + ";\n"); //$NON-NLS-1$ //$NON-NLS-2$

		sb.append("\t menuInfo.addItemInfo(mii);\n"); //$NON-NLS-1$

		return sb;
	}

	/**
	 * @param index
	 * @param uv
	 * @return sb url value js
	 */
	public static StringBuilder getURLValueJS(StringBuilder sb, int index, URLValue uv,
			ICharacterEncoderAdapter transferAdapter) {
		if (index == 0) {
			sb.append("\t var mii = new BirtChartMenuItemInfo();\n");//$NON-NLS-1$
		} else {
			sb.append("\t mii = new BirtChartMenuItemInfo();\n");//$NON-NLS-1$
		}
		String text = transferAdapter
				.transformToJsConstants(transferAdapter.escape(uv.getLabel().getCaption().getValue()));
		sb.append("\t mii.text = '" + text + "';\n");//$NON-NLS-1$//$NON-NLS-2$
		String url = uv.getBaseUrl();
		if (!(url.startsWith("\"") || url.endsWith("\"")))//$NON-NLS-1$ //$NON-NLS-2$
		{
			url = "\"" + url + "\"";//$NON-NLS-1$ //$NON-NLS-2$
		}
		sb.append("\t mii.actionType = BirtChartInteractivityActions.HYPER_LINK;\n"); //$NON-NLS-1$
		sb.append("\t mii.actionValue = " + url + ";\n"); //$NON-NLS-1$//$NON-NLS-2$

		String target = uv.getTarget() == null ? "" : uv.getTarget();//$NON-NLS-1$
		sb.append("\t mii.target = '" + target + "';\n"); //$NON-NLS-1$//$NON-NLS-2$
		if (uv.getTooltip() != null && uv.getTooltip().trim().length() > 0) {
			String tooltip = transferAdapter.transformToJsConstants(uv.getTooltip());
			if (tooltip.startsWith("\"") || tooltip.startsWith("'")) //$NON-NLS-1$//$NON-NLS-2$
				sb.append("\t mii.tooltip = " + tooltip + ";\n"); //$NON-NLS-1$//$NON-NLS-2$
			else
				sb.append("\t mii.tooltip = '" + tooltip + "';\n"); //$NON-NLS-1$//$NON-NLS-2$
		}

		sb.append("\t menuInfo.addItemInfo(mii);\n"); //$NON-NLS-1$

		return sb;
	}

	/**
	 * @param sb
	 * @param propMap
	 */
	private static StringBuilder getPropertiesJS(EMap<String, String> propMap) {
		StringBuilder sb = new StringBuilder();
		for (Entry<String, String> entry : propMap.entrySet()) {
			String key = entry.getKey();
			String properties = entry.getValue();
			if (MenuStylesKeyType.MENU.getName().equals(key)) {
				if (!properties.matches(".*position[ ]*:.*")) //$NON-NLS-1$
				{
					properties = properties + ";position:absolute"; //$NON-NLS-1$
				}
				sb.append("\t menuInfo.menuStyles = '" + CSSHelper.getStylingNonHyphenFormat(properties) + "';\n");//$NON-NLS-1$//$NON-NLS-2$
			} else if (MenuStylesKeyType.MENU_ITEM.getName().equals(key)) {
				if (!properties.matches(".*cursor[ ]*:.*")) //$NON-NLS-1$
				{
					properties = properties + ";cursor:default"; //$NON-NLS-1$
				}
				sb.append("\t menuInfo.menuItemStyles = '" + CSSHelper.getStylingNonHyphenFormat(properties) + "';\n");//$NON-NLS-1$//$NON-NLS-2$
			} else if (MenuStylesKeyType.ON_MOUSE_OVER.getName().equals(key)) {
				sb.append("\t menuInfo.mouseOverStyles = '" + CSSHelper.getStylingNonHyphenFormat(properties) + "';\n");//$NON-NLS-1$//$NON-NLS-2$
			} else if (MenuStylesKeyType.ON_MOUSE_OUT.getName().equals(key)) {
				sb.append("\tmenuInfo.mouseOutStyles = '" + CSSHelper.getStylingNonHyphenFormat(properties) + "';\n");//$NON-NLS-1$//$NON-NLS-2$
			}
		}
		return sb;
	}

	/**
	 * @param multiUrlValue
	 * @return list valid url values
	 */
	public static List<URLValue> getValidURLValues(MultiURLValues multiUrlValue) {
		List<URLValue> validURLValues = new ArrayList<URLValue>();
		if (multiUrlValue == null) {
			return validURLValues;
		}

		for (URLValue uv : multiUrlValue.getURLValues()) {
			if (!isValidURLValue(uv)) {
				continue;
			}
			validURLValues.add(uv);
		}
		return validURLValues;
	}

	private static boolean isValidURLValue(URLValue uv) {
		return !(uv.getBaseUrl() == null || uv.getBaseUrl().length() <= 0 || uv.getBaseUrl().equals("\"\"")); //$NON-NLS-1$
	}

	/**
	 * Check if the specified action contains redirection items.
	 * 
	 * @param action
	 * @return boolean
	 * @since 2.5.2
	 */
	public static boolean containsRedirection(Action action) {
		if (action instanceof MultipleActions) {
			return getValidActions((MultipleActions) action).size() > 0;
		} else if (action.getType().getValue() == ActionType.URL_REDIRECT) {
			ActionValue av = action.getValue();
			if (av instanceof URLValue) {
				return isValidURLValue((URLValue) av);
			} else if (av instanceof MultiURLValues) {
				return getValidURLValues((MultiURLValues) av).size() > 0;
			}
		}

		return false;
	}

	/**
	 * @param multiActions
	 * @return list valid actions
	 */
	public static List<Action> getValidActions(MultipleActions multiActions) {
		List<Action> validActions = new ArrayList<Action>();
		if (multiActions == null) {
			return validActions;
		}

		for (Action subAction : multiActions.getActions()) {
			ActionValue av = subAction.getValue();
			if (av instanceof URLValue) {
				URLValue uv = (URLValue) av;
				if (!isValidURLValue(uv)) {
					continue;
				}
			} else if (av instanceof ScriptValue) {
				ScriptValue sv = (ScriptValue) av;
				if (!isValidScripts(sv)) {
					continue;
				}
			}

			validActions.add(subAction);
		}
		return validActions;
	}

	/**
	 * @param sv
	 * @return
	 */
	private static boolean isValidScripts(ScriptValue sv) {
		return !(sv.getScript() == null || sv.getScript().length() == 0 || sv.getScript().equals("\"\""));//$NON-NLS-1$
	}

	public static String getBirtChartMenuLib() {
		if (MENU_JS_CODE == null) {
			StringBuilder sb = new StringBuilder();
			try {
				InputStream is = MultiActionValuesScriptGenerator.class
						.getResourceAsStream("/org/eclipse/birt/chart/device/util/ImageActionMenu.js"); //$NON-NLS-1$
				BufferedReader br = new BufferedReader(new InputStreamReader(is));
				String s = null;
				while (true) {
					s = br.readLine();
					if (s == null) {
						break;
					}
					sb.append(s);
					sb.append("\n"); //$NON-NLS-1$
				}
				br.close();
			} catch (FileNotFoundException e) {
				logger.log(e);
			} catch (IOException e) {
				logger.log(e);
			}

			MENU_JS_CODE = sb.toString();
		}
		return MENU_JS_CODE;
	}
}
