/*******************************************************************************
  * Copyright (c) 2012 Megha Nidhi Dahal and others.
  * All rights reserved. This program and the accompanying materials
  * are made available under the terms of the Eclipse Public License v2.0
  * which accompanies this distribution, and is available at
  * http://www.eclipse.org/legal/epl-2.0.html
  *
  * Contributors:
  *    Megha Nidhi Dahal - initial API and implementation and/or initial documentation
  *    Actuate Corporation - more efficient xlsx processing;
  *         support of timestamp, datetime, time, and date data types
  *******************************************************************************/

package org.eclipse.birt.report.data.oda.excel.impl.util;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.report.data.oda.excel.ExcelODAConstants;

public class XlsxRowCallBack implements RowCallBack {
	private ArrayList<String[]> xlsxRowData = new ArrayList<String[]>();

	public void handleRow(List<Object> values) {

		if (values == null || values.size() == 0) {
			return;
		}
		String[] valArray = new String[values.size()];
		values.toArray(valArray);
		xlsxRowData.add(valArray);
	}

	public XlsxRowCallBack() {
	}

	public ArrayList<String> initArrayList(String[] strings) {
		ArrayList<String> list = new ArrayList<String>();
		for (String i : strings) {
			list.add(i);
		}
		return list;
	}

	public int getMaxRowsInSheet() {
		return (xlsxRowData.size());
	}

	public ArrayList<String> getRow(int rownum) {
		return (initArrayList(xlsxRowData.get(rownum)));
	}

	// Need this function because there is no easy way of determining the number of
	// columns in xlsx with SAX Parser.
	// This function expands all the previous data rows with blanks.
	public void columnExpansion(int newColumnCount) {
		for (int i = 0; i < xlsxRowData.size(); i++) {
			String[] currentRow = (String[]) xlsxRowData.get(i);
			if (currentRow.length < newColumnCount) {
				String[] newRow = new String[newColumnCount];
				// put in fake column names
				if (i == 0) {
					for (int j = 0; j < newRow.length; j++) {
						newRow[j] = "column_" + j;
					}
				} else {
					for (int j = 0; j < newRow.length; j++)
						newRow[j] = ExcelODAConstants.EMPTY_STRING;
				}
				for (int g = 0; g < currentRow.length; g++)
					newRow[g] = currentRow[g];
				xlsxRowData.remove(i);
				xlsxRowData.add(i, newRow);
			}
		}
	}

}
