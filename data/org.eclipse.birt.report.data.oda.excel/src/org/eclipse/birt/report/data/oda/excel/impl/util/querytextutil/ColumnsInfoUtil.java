/*******************************************************************************
  * Copyright (c) 2012 Megha Nidhi Dahal.
  * All rights reserved. This program and the accompanying materials
  * are made available under the terms of the Eclipse Public License v1.0
  * which accompanies this distribution, and is available at
  * http://www.eclipse.org/legal/epl-v10.html
  *
  * Contributors:
  *    Megha Nidhi Dahal - initial API and implementation and/or initial documentation
  *******************************************************************************/
package org.eclipse.birt.report.data.oda.excel.impl.util.querytextutil;

import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for extracting column names, original column names, and column
 * types from columns information
 */

public class ColumnsInfoUtil {

	private static String EMPTY_STRING = ""; //$NON-NLS-1$

	private String[] columnNames;
	private String[] columnTypeNames;
	private String[] originalColumnNames;

	/**
	 *
	 *
	 */
	public ColumnsInfoUtil(String columnsInfo) {
		assert columnsInfo != null;
		List<String[]> columnsInfoVector = getColumnsInfoList(columnsInfo);
		columnNames = new String[columnsInfoVector.size()];
		columnTypeNames = new String[columnsInfoVector.size()];
		originalColumnNames = new String[columnsInfoVector.size()];
		for (int i = 0; i < columnsInfoVector.size(); i++) {
			String[] items = columnsInfoVector.get(i);
			columnNames[i] = items[0];
			originalColumnNames[i] = items[1];
			columnTypeNames[i] = items[2];
		}
	}

	/**
	 * Extracts the column names from the columsInfo string
	 *
	 * @param columnsInfo
	 * @return
	 */
	public String[] getColumnNames() {
		return columnNames;
	}

	/**
	 * Extracts the column type names from the columsInfo string
	 *
	 * @param columnsInfo
	 * @return
	 */
	public String[] getColumnTypeNames() {
		return columnTypeNames;
	}

	/**
	 * Extracts the original column names from the columsInfo string
	 *
	 * @param columnsInfo
	 * @return
	 */
	public String[] getOriginalColumnNames() {
		return originalColumnNames;
	}

	/**
	 *
	 * @param columnsInfo
	 * @return
	 */
	private static List<String[]> getColumnsInfoList(String columnsInfo) {
		List<String[]> columnsInfoList = new ArrayList<String[]>();
		char[] columnsInfoChars = columnsInfo.toCharArray();
		boolean isEscaped = false;
		String[] columnInfo = { EMPTY_STRING, EMPTY_STRING, EMPTY_STRING };
		int index = 0;

		for (int i = 0; i < columnsInfoChars.length; i++) {
			if (columnsInfoChars[i] == '"' || columnsInfoChars[i] == '|' || columnsInfoChars[i] == ':'
					|| columnsInfoChars[i] == '<' || columnsInfoChars[i] == '>' || columnsInfoChars[i] == '?'
					|| columnsInfoChars[i] == '*' || columnsInfoChars[i] == '{' || columnsInfoChars[i] == '/') {
				if (isEscaped) {
					columnInfo[index] = columnInfo[index] + columnsInfoChars[i];
					isEscaped = !isEscaped;
				}
			} else if (columnsInfoChars[i] == '\\') {
				if (isEscaped) {
					columnInfo[index] = columnInfo[index] + columnsInfoChars[i];
					isEscaped = !isEscaped;
				} else
					isEscaped = !isEscaped;
			} else if (columnsInfoChars[i] == ',') {
				if (isEscaped) {
					columnInfo[index] = columnInfo[index] + columnsInfoChars[i];
					isEscaped = !isEscaped;
				} else {
					index++;
				}
			} else if (columnsInfoChars[i] == ';' || i == (columnsInfoChars.length - 1)) {
				if (isEscaped) {
					columnInfo[index] = columnInfo[index] + columnsInfoChars[i];
					isEscaped = !isEscaped;
				} else {

					if (i == (columnsInfoChars.length - 1)) {
						columnInfo[index] = columnInfo[index] + columnsInfoChars[i];

						columnsInfoList.add(columnInfo);
					} else {
						columnsInfoList.add(columnInfo);
						index = 0;
						columnInfo = new String[3];
						columnInfo[0] = columnInfo[1] = columnInfo[2] = EMPTY_STRING;
					}
				}
			} else {
				columnInfo[index] = columnInfo[index] + columnsInfoChars[i];
			}
		}

		return columnsInfoList;
	}

	/**
	 *
	 * @param charactor
	 * @return
	 */
	public static boolean isColumnsInfoKeyWord(char charactor) {
		return (charactor == '"' || charactor == ';' || charactor == ',' || charactor == '|' || charactor == '\\'
				|| charactor == '/' || charactor == '<' || charactor == '>' || charactor == '*' || charactor == ':'
				|| charactor == '?' || charactor == '{');
	}

}
