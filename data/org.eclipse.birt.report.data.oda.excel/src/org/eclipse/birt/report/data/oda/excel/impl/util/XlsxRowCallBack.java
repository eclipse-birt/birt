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
package org.eclipse.birt.report.data.oda.excel.impl.util;

import java.util.ArrayList;
import java.util.List;

public class XlsxRowCallBack implements RowCallBack {
	private ArrayList<String[]> xlsxRowData = new ArrayList<String[]>();
	private int rowNum;

	public void handleRow(List<Object> values) {
		++rowNum;

		if (values == null || values.size() == 0) {
			return;
		}
		String[] valArray = new String[values.size()];
		values.toArray(valArray);

		xlsxRowData.add(valArray);
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
}