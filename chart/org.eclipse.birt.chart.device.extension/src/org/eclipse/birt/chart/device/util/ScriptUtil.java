/***********************************************************************
 * Copyright (c) 2004, 2007 Actuate Corporation.
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

package org.eclipse.birt.chart.device.util;

import org.eclipse.birt.chart.computation.DataPointHints;
import org.eclipse.birt.chart.computation.IConstants;
import org.eclipse.birt.chart.computation.LegendItemHints;
import org.eclipse.birt.chart.model.data.DateTimeDataElement;
import org.eclipse.birt.chart.model.data.NumberDataElement;
import org.eclipse.birt.chart.util.ChartUtil;

import com.ibm.icu.util.Calendar;

public class ScriptUtil {

	/**
	 * Add the value of categoryData, valueData, and valueSeriesName into script.
	 *
	 * @param str
	 * @param dph
	 */
	public static void script(StringBuffer str, DataPointHints dph, LegendItemHints lerh, String axisLabel) {
		// ScriptHandler.BASE_VALUE
		// ScriptHandler.ORTHOGONAL_VALUE
		// ScriptHandler.SERIES_VALUE
		if (dph != null) {
			str.append(","); //$NON-NLS-1$
			str.append(addDataValueToScript(dph.getBaseValue()));
			str.append(","); //$NON-NLS-1$
			str.append(addDataValueToScript(dph.getOrthogonalValue()));
			str.append(","); //$NON-NLS-1$
			str.append(addDataValueToScript(dph.getSeriesValue()));
		} else {
			str.append(",null,null,null"); //$NON-NLS-1$
		}
		// IActionRenderer.LEGEND_ITEM_TEXT
		// IActionRenderer.LEGEND_ITEM_VALUE
		if (lerh != null) {
			str.append(","); //$NON-NLS-1$
			str.append(addDataValueToScript(lerh.getItemValue()));
			str.append(","); //$NON-NLS-1$
			str.append(addDataValueToScript(lerh.getItemText()));
			str.append(","); //$NON-NLS-1$
			str.append(addDataValueToScript(lerh.getValueText()));
		} else {
			str.append(",null,null,null"); //$NON-NLS-1$
		}
		// IActionRenderer.AXIS_LABEL
		if (axisLabel != null) {
			str.append(","); //$NON-NLS-1$
			str.append(addDataValueToScript(axisLabel));
		} else {
			str.append(",null"); //$NON-NLS-1$
		}
	}

	/**
	 * Return the correct string according the the data type.
	 *
	 * @param oValue
	 * @return the formatted string
	 */
	private static String addDataValueToScript(Object oValue) {
		if (oValue instanceof String) {
			return "'" + transformToJsConstants((String) oValue) + "'";//$NON-NLS-1$ //$NON-NLS-2$
		} else if (oValue instanceof Double) {
			return ((Double) oValue).toString();
		} else if (oValue instanceof NumberDataElement) {
			return ((NumberDataElement) oValue).toString();
		} else if (oValue instanceof Calendar) {
			return "'" + ChartUtil.stringValue(oValue) + "'";//$NON-NLS-1$ //$NON-NLS-2$
		} else if (oValue instanceof DateTimeDataElement) {
			return "'" + ChartUtil.stringValue(((DateTimeDataElement) oValue).getValueAsCalendar()) + "'";//$NON-NLS-1$ //$NON-NLS-2$
		} else if (oValue == null) {
			return "''"; //$NON-NLS-1$
		} else {
			return "'" + oValue.toString() + "'";//$NON-NLS-1$ //$NON-NLS-2$
		}
	}

	/**
	 * This method transforms a string to JS string constants.
	 *
	 * @param s
	 * @return escaped js value
	 */
	public static String transformToJsConstants(String s) {
		if (s == null) {
			return null;
		}

		StringBuilder buffer = new StringBuilder();
		int length = s.length();
		for (int i = 0; i < length; i++) {
			char c = s.charAt(i);
			switch (c) {
			case '\\':
				buffer.append("\\\\");//$NON-NLS-1$
				break;
			case '\b':
				buffer.append("\\b");//$NON-NLS-1$
				break;
			case '\t':
				buffer.append("\\t");//$NON-NLS-1$
				break;
			case '\n':
				buffer.append("\\n");//$NON-NLS-1$
				break;
			case '\f':
				buffer.append("\\f");//$NON-NLS-1$
				break;
			case '\r':
				buffer.append("\\r");//$NON-NLS-1$
				break;
			case '"':
				buffer.append("\\\"");//$NON-NLS-1$
				break;
			case '\'':
				buffer.append("\\\'");//$NON-NLS-1$
				break;
			default:
				buffer.append(c);
			}
		}
		return buffer.toString();
	}

	/**
	 * This method transforms a string to HTML text. e.g. '\r\n' to '<br>
	 * '.
	 *
	 * @param s
	 * @return converted HTML text
	 */
	public static String transformToHTMLText(String s) {
		if (s == null) {
			return IConstants.EMPTY_STRING;
		}

		return s.replace("\r\n", "<br>"); //$NON-NLS-1$ //$NON-NLS-2$
	}

}
