/***********************************************************************
 * Copyright (c) 2004 Actuate Corporation.
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

package org.eclipse.birt.chart.datafeed;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.eclipse.birt.chart.computation.Methods;
import org.eclipse.birt.chart.engine.i18n.Messages;
import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.data.DataSet;
import org.eclipse.birt.chart.model.data.Query;
import org.eclipse.birt.chart.plugin.ChartEnginePlugin;
import org.eclipse.emf.common.util.EList;

import com.ibm.icu.text.SimpleDateFormat;
import com.ibm.icu.util.ULocale;

/**
 * Provides a no-op implementation of the
 * {@link org.eclipse.birt.chart.datafeed.IDataSetProcessor}interface definition
 * to be subclassed by each extension writer as needed.
 */
public class DataSetAdapter extends Methods implements IDataSetProcessor {

	/**
	 * An internal instance of the locale being used for processing
	 */
	private transient ULocale lcl = null;

	protected static final String DELIMITER = ","; //$NON-NLS-1$

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.datafeed.IDataSetProcessor#fromString(java.lang.
	 * String, org.eclipse.birt.chart.model.data.DataSet)
	 */
	public DataSet fromString(String sDataSetRepresentation, DataSet ds) throws ChartException {
		// NO-OP IMPL
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.chart.datafeed.IDataSetProcessor#populate(java.lang.Object,
	 * org.eclipse.birt.chart.model.data.DataSet)
	 */
	public DataSet populate(Object oResultSetDef, DataSet ds) throws ChartException {
		// NO-OP IMPL
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.chart.datafeed.IDataSetProcessor#getMinimum(org.eclipse.birt
	 * .chart.model.data.DataSet)
	 */
	public Object getMinimum(DataSet ds) throws ChartException {
		// NO-OP IMPL
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.chart.datafeed.IDataSetProcessor#getMaximum(org.eclipse.birt
	 * .chart.model.data.DataSet)
	 */
	public Object getMaximum(DataSet ds) throws ChartException {
		// NO-OP IMPL
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.chart.datafeed.IDataSetProcessor#getExpectedStringFormat()
	 */
	public String getExpectedStringFormat() {
		// NO-OP IMPL
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.datafeed.IDataSetProcessor#getLocale()
	 */
	public Locale getLocale() {
		return getULocale().toLocale();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.datafeed.IDataSetProcessor#getULocale()
	 */
	public ULocale getULocale() {
		return (lcl == null) ? ULocale.getDefault() : lcl;
	}

	/**
	 * A convenience method provided to associate a locale with a display server
	 * 
	 * @param lcl The locale to be set
	 */
	public final void setLocale(ULocale lcl) {
		this.lcl = lcl;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.chart.datafeed.IDataSetProcessor#toString(java.lang.Object[]
	 * )
	 */
	public String toString(Object[] columnData) throws ChartException {
		if (columnData == null || columnData.length == 0) {
			throw new ChartException(ChartEnginePlugin.ID, ChartException.DATA_SET,
					"exception.base.orthogonal.null.datadefinition", //$NON-NLS-1$
					Messages.getResourceBundle(getULocale()));
		}
		StringBuffer buffer = new StringBuffer();
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy"); //$NON-NLS-1$

		// Gets the data type first
		int dataType = 0;
		for (int i = 0; i < columnData.length; i++) {
			if (dataType > 0) {
				break;
			}
			// Unwrap array
			if (columnData[i] instanceof Object[]) {
				columnData[i] = ((Object[]) columnData[i])[0];
			}
			if (columnData[i] instanceof String) {
				dataType = 1;
			} else if (columnData[i] instanceof Date) {
				dataType = 2;
			} else if (columnData[i] instanceof Number) {
				dataType = 3;
			}
		}
		// If data is null
		if (dataType == 0) {
			throw new ChartException(ChartEnginePlugin.ID, ChartException.DATA_SET,
					"exception.base.orthogonal.null.datadefinition", //$NON-NLS-1$
					Messages.getResourceBundle(getULocale()));
		}

		// Generates a string reprensentation
		for (int i = 0; i < columnData.length; i++) {
			// Unwrap array
			if (columnData[i] instanceof Object[]) {
				columnData[i] = ((Object[]) columnData[i])[0];
			}

			if (dataType == 1) {
				buffer.append("'" + formatString((String) columnData[i]) + "'"); //$NON-NLS-1$ //$NON-NLS-2$
			} else if (dataType == 2) {
				buffer.append(sdf.format((Date) columnData[i]));
			} else if (dataType == 3) {
				buffer.append(String.valueOf(columnData[i]));
			}
			if (i < columnData.length - 1) {
				buffer.append(DELIMITER);
			}
		}
		return buffer.toString();
	}

	/**
	 * Formats sample data representation to escape delimiter
	 * 
	 * @param str original string
	 */
	protected String formatString(String str) {
		if (str == null)
			return ""; //$NON-NLS-1$
		return str.replaceAll("\\,", "\\\\,"); //$NON-NLS-1$ //$NON-NLS-2$
	}

	/**
	 * return the array of indexes, the DataDefinitions with these id could be
	 * computed with aggregation function. By default, all the indexes will be added
	 * to the array. And this method should be overridden for some special chart
	 * types, such as bubble, stock...
	 * 
	 * @param series
	 * @return
	 */
	protected int[] getDataDefIdsForGrouping(Series series) {
		EList<Query> elDD = series.getDataDefinition();
		int[] DataDefIds = new int[elDD.size()];
		for (int i = 0; i < DataDefIds.length; i++) {
			DataDefIds[i] = i;
		}
		return DataDefIds;
	}

	/**
	 * 
	 */
	public List<Query> getDataDefinitionsForGrouping(Series series) {
		List<Query> list = new ArrayList<Query>(1);
		EList<Query> elDD = series.getDataDefinition();
		int IDs[] = getDataDefIdsForGrouping(series);

		for (int i = 0; i < IDs.length; i++) {
			Query query = elDD.get(IDs[i]);
			String sExpression = query.getDefinition();

			if (sExpression != null && sExpression.trim().length() > 0) {
				// ADD NEW VALID EXPRESSION
				list.add(query);
			}

		}

		return list;
	}
}
