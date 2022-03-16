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

package org.eclipse.birt.chart.examples.builder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.factory.DataRowExpressionEvaluatorAdapter;
import org.eclipse.birt.chart.factory.IDataRowExpressionEvaluator;
import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.IChartObject;
import org.eclipse.birt.chart.model.attribute.DataType;
import org.eclipse.birt.chart.ui.i18n.Messages;
import org.eclipse.birt.chart.ui.plugin.ChartUIPlugin;
import org.eclipse.birt.chart.ui.swt.interfaces.IDataServiceProvider;

/**
 * Provides a basic implementation for simulated data service. Used in launcher.
 *
 */
public class DefaultDataServiceProviderImpl implements IDataServiceProvider {

	private static final int COLUMN_COUNT = 8;
	private static final int ROW_COUNT = 6;

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.chart.ui.interfaces.IDataServiceProvider#getPreviewHeader(
	 * java.lang.String)
	 */
	public String[] getPreviewHeader() {
		String[] columns = new String[COLUMN_COUNT];
		for (int i = 0; i < columns.length; i++) {
			columns[i] = "DB Col " + (i + 1); //$NON-NLS-1$
		}
		return columns;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.chart.ui.interfaces.IDataServiceProvider#getPreviewData(java
	 * .lang.String)
	 */
	public List getPreviewData() {
		List list = new ArrayList();
		for (int rowNum = 0; rowNum < ROW_COUNT; rowNum++) {
			String[] columns = new String[COLUMN_COUNT];
			for (int i = 0; i < columns.length; i++) {
				columns[i] = String.valueOf((rowNum + 1) * (i + 1));
			}
			list.add(columns);
		}
		return list;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.chart.ui.interfaces.IDataServiceProvider#getAllStyles()
	 */
	@Override
	public String[] getAllStyles() {
		return new String[] {};
	}

	@Override
	public String[] getAllStyleDisplayNames() {
		return getAllStyles();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.chart.ui.interfaces.IDataServiceProvider#getCurrentStyle()
	 */
	@Override
	public String getCurrentStyle() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.chart.ui.interfaces.IDataServiceProvider#setStyle(java.lang.
	 * String)
	 */
	@Override
	public void setStyle(String styleName) {
		// TODO Auto-generated method stub
	}

	@Override
	public Object[] getDataForColumns(String[] sExpressions, int iMaxRecords, boolean byRow) {
		// Always provide data by column whatever the byRow is false/true.
		Object[] array = new Object[sExpressions.length];
		for (int i = 0; i < sExpressions.length; i++)// a column
		{
			Object[] innerArray = new Object[ROW_COUNT];// a row
			for (int j = 0; j < ROW_COUNT; j++) {
				String str = sExpressions[i];
				int intStart = str.lastIndexOf(' ') + 1;
				int index = Integer.parseInt(str.substring(intStart, intStart + 1)) - 1;
				innerArray[j] = new Integer(((String[]) getPreviewData().get(j))[index]);
			}
			array[i] = innerArray;
		}
		return array;
	}

	@Override
	public boolean isLivePreviewEnabled() {
		return true;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.chart.ui.swt.interfaces.IDataServiceProvider#getDataType(
	 * java.lang.String)
	 */
	@Override
	public DataType getDataType(String expression) {
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.chart.ui.swt.interfaces.IDataServiceProvider#
	 * prepareRowExpressionEvaluator(org.eclipse.birt.chart.model.Chart,
	 * java.util.List, int, boolean)
	 */
	@Override
	public IDataRowExpressionEvaluator prepareRowExpressionEvaluator(Chart cm, List expressions, int maxRecords,
			boolean byRow) throws ChartException {
		final Object[] columnData;
		columnData = getDataForColumns((String[]) expressions.toArray(new String[expressions.size()]), -1, false);

		final Map map = new HashMap();
		for (int i = 0; i < expressions.size(); i++) {
			map.put(expressions.get(i), columnData[i]);
		}
		IDataRowExpressionEvaluator evaluator = new DataRowExpressionEvaluatorAdapter() {

			private int i;
			private Object[] column;

			@Override
			public Object evaluate(String expression) {
				column = (Object[]) map.get(expression);
				if (i >= column.length) {
					throw new RuntimeException(new ChartException(ChartUIPlugin.ID, ChartException.DATA_SET,
							Messages.getString("ChartUIUtil.Exception.NoValueReturned"))); //$NON-NLS-1$
				}
				return column[i];
			}

			@Override
			public boolean first() {
				i = 0;

				if (map.size() > 0) {
					column = (Object[]) map.values().iterator().next();

					if (column != null && i <= column.length - 1) {
						return true;
					}
				}

				return false;
			}

			@Override
			public boolean next() {
				if (column != null && i < column.length - 1) {
					i++;
					return true;
				}
				return false;
			}

			@Override
			public void close() {
				// no-op
			}
		};

		return evaluator;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.chart.ui.swt.interfaces.IDataServiceProvider#update(int,
	 * java.lang.Object)
	 */
	@Override
	public boolean update(String type, Object value) {
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.chart.ui.swt.interfaces.IDataServiceProvider#
	 * getStateInformation()
	 */
	@Override
	public int getState() {
		return 0;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.chart.ui.swt.interfaces.IDataServiceProvider#checkState(int)
	 */
	@Override
	public boolean checkState(int state) {
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.chart.ui.swt.interfaces.IDataServiceProvider#checkData(java.
	 * lang.String, java.lang.Object)
	 */
	@Override
	public Object checkData(String checkType, Object data) {
		return null;
	}

	public void adaptExpressions(IChartObject ico) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.chart.ui.swt.interfaces.IDataServiceProvider#dispose()
	 */
	@Override
	public void dispose() {
		// No code here.

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.chart.ui.swt.interfaces.IDataServiceProvider#initialize()
	 */
	@Override
	public void initialize() throws ChartException {
		// No code here.
	}
}
