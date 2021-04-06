/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.chart.internal.datafeed;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.script.ScriptHandler;
import org.eclipse.birt.chart.util.ChartUtil;

/**
 * The class provides some static methods for the chart variables.
 * 
 * @since 2.5
 */

public class ChartVariableHelper {
	private static final String OPERATORS = " +-*/!=<>&|()\"\'"; //$NON-NLS-1$

	/**
	 * Parse specified script, using correct expression instead of chart variables.
	 * 
	 * @param script
	 * @param currOrthoSeries
	 * @param baseSD
	 * @param currOrthoSD
	 * @since 2.5
	 */
	public static String parseChartVariables(String script, Series currOrthoSeries, SeriesDefinition baseSD,
			SeriesDefinition currOrthoSD) {
		try {
			String categoryExpr = (baseSD.getDesignTimeSeries().getDataDefinition().get(0)).getDefinition();
			String seriesExpr = ChartUtil.createValueSeriesRowFullExpression(currOrthoSeries.getDataDefinition().get(0),
					currOrthoSD, baseSD);
			String optionalYExpr = null;

			if (currOrthoSD.getQuery() != null && currOrthoSD.getQuery().getDefinition() != null
					&& !"".equals(currOrthoSD.getQuery() //$NON-NLS-1$
							.getDefinition())) {
				optionalYExpr = currOrthoSD.getQuery().getDefinition();
			}

			String seriesName = ChartUtil.stringBlankValue(optionalYExpr).trim().length() > 0 ? optionalYExpr
					: "\"" //$NON-NLS-1$
							+ ChartUtil.stringBlankValue(currOrthoSeries.getSeriesIdentifier()) + "\""; //$NON-NLS-1$
			return parseChartVariables(script, categoryExpr, seriesExpr, seriesName);
		} catch (ChartException e) {
			return script;
		}
	}

	/**
	 * Parse specified script, using correct expression instead of chart variables.
	 * 
	 * @param script
	 * @param categoryExpr
	 * @param seriesExpr
	 * @param seriesName
	 * @return
	 */
	private static String parseChartVariables(String script, String categoryExpr, String seriesExpr,
			String seriesName) {
		if (script == null) {
			return null;
		}

		// Get symbols in script.
		List<StringBuffer> symbols = new ArrayList<StringBuffer>();
		BufferedReader br = new BufferedReader(new StringReader(script));
		boolean isComments = false;
		boolean isCPlusCommnets = false;
		boolean isInQuotation = false;
		StringBuffer sb = new StringBuffer();

		try {
			int line = -1;
			do {
				String str = br.readLine();
				if (str == null) {
					break;
				}
				line++;
				if (line > 0) {
					sb.append('\n');
					sb = addToSymbolList(symbols, sb);
				}

				// Read symbols of a line text.
				for (int i = 0; i < str.length(); i++) {
					char c = str.charAt(i);

					switch (c) {
					case '+':
					case '-':
					case '!':
					case '=':
					case '<':
					case '>':
					case '&':
					case '|':
					case '(':
					case ')':
						if (isCPlusCommnets || isComments) {
							sb.append(c);
						} else {
							sb = addToSymbolList(symbols, sb);
							sb.append(c);
						}
						break;

					case '/':
						if (sb.length() == 0) {
							// Start of a symbol.
							sb.append(c);
						} else if (isComments && sb.charAt(sb.length() - 1) == '*') {
							// End of comment.
							isComments = false;
							sb.append(c);
							sb = addToSymbolList(symbols, sb);
						} else if (isCPlusCommnets || isComments) {
							sb.append(c);
						} else if (sb.charAt(sb.length() - 1) == '/') {
							// Start of C plus comment.
							isCPlusCommnets = true;
							sb.append(c);
						} else {
							sb = addToSymbolList(symbols, sb);
							sb.append(c);
						}
						break;

					case '*':
						if (isCPlusCommnets || isComments) {
							sb.append(c);
						} else if (sb.charAt(sb.length() - 1) == '/') {
							// Start of comment.
							isComments = true;
							sb.append(c);
						} else {
							sb = addToSymbolList(symbols, sb);
							sb.append(c);
						}
						break;

					case '"':
					case '\'':
						if (isCPlusCommnets || isComments) {
							sb.append(c);
						} else {
							// Common string.
							sb.append(c);
							if (!isInQuotation) {
								isInQuotation = true;
							} else {
								isInQuotation = false;
								sb = addToSymbolList(symbols, sb);
							}
						}
						break;

					default:
						// If current is not comments or common string and
						// the last char is operator, current char is not
						// operator, then add the previous chars to symbol list.
						if (!isComments && !isCPlusCommnets && !isInQuotation && sb.length() > 0
								&& isOperator(sb.charAt(sb.length() - 1)) && !isOperator(c)) {
							sb = addToSymbolList(symbols, sb);
						}
						sb.append(c);
					}
				}

				isCPlusCommnets = false;
				isInQuotation = false;
				sb = addToSymbolList(symbols, sb);
			} while (true);
		} catch (IOException e) {
			return script;
		}

		// Assemble script again, using expression instead of chart data point
		// variables.
		StringBuffer returnSB = new StringBuffer();
		for (StringBuffer s : symbols) {
			String src = s.toString();
			String expr = src.trim();
			if (expr.equals(ScriptHandler.BASE_VALUE)) {
				returnSB.append(src.replace(ScriptHandler.BASE_VALUE, categoryExpr == null ? "" : categoryExpr)); //$NON-NLS-1$
			} else if (expr.equals(ScriptHandler.ORTHOGONAL_VALUE)) {
				returnSB.append(src.replace(ScriptHandler.ORTHOGONAL_VALUE, seriesExpr == null ? "" : seriesExpr)); //$NON-NLS-1$
			} else if (expr.equals(ScriptHandler.SERIES_VALUE)) {
				returnSB.append(src.replace(ScriptHandler.SERIES_VALUE, seriesName == null ? "" : seriesName)); //$NON-NLS-1$
			} else {
				returnSB.append(src);
			}
		}

		return returnSB.toString();
	}

	private static boolean isOperator(char c) {
		return OPERATORS.indexOf(c) >= 0;
	}

	private static StringBuffer addToSymbolList(List<StringBuffer> symbols, StringBuffer sb) {
		if (sb.length() == 0) {
			return sb;
		}

		symbols.add(sb);
		return new StringBuffer();
	}
}
