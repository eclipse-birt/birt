/***********************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.chart.extension.datafeed;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.chart.computation.DataSetIterator;
import org.eclipse.birt.chart.datafeed.DataSetAdapter;
import org.eclipse.birt.chart.datafeed.IResultSetDataSet;
import org.eclipse.birt.chart.engine.extension.i18n.Messages;
import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.model.data.DataSet;
import org.eclipse.birt.chart.model.data.impl.StockDataSetImpl;
import org.eclipse.birt.chart.plugin.ChartEngineExtensionPlugin;

import com.ibm.icu.util.StringTokenizer;

/**
 * Capable of processing data sets that contain stock entry data elements that
 * wrap 4 values - high, low, open and close
 */
public final class StockDataSetProcessorImpl extends DataSetAdapter {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.chart.datafeed.IDataSetProcessor#populate(java.lang.Object,
	 * org.eclipse.birt.chart.model.data.DataSet)
	 */
	public final DataSet populate(Object oResultSetDef, DataSet ds) throws ChartException {
		if (oResultSetDef instanceof IResultSetDataSet) {
			final IResultSetDataSet rsds = (IResultSetDataSet) oResultSetDef;
			final long lRowCount = rsds.getSize();

			if (lRowCount <= 0) {
				throw new ChartException(ChartEngineExtensionPlugin.ID, ChartException.ZERO_DATASET,
						"exception.empty.dataset", //$NON-NLS-1$
						Messages.getResourceBundle(getULocale()));
			}

			int i = 0;

			final StockEntry[] sea = new StockEntry[(int) lRowCount];
			while (rsds.hasNext()) {
				Object[] oFourComponents = rsds.next();

				validateStockEntryData(oFourComponents);

				sea[i++] = new StockEntry(oFourComponents);
			}
			if (ds == null) {
				ds = StockDataSetImpl.create(sea);
			} else {
				ds.setValues(sea);
			}
		} else {
			throw new ChartException(ChartEngineExtensionPlugin.ID, ChartException.DATA_SET,
					"exception.unknown.custom.dataset", //$NON-NLS-1$
					new Object[] { ds, oResultSetDef }, Messages.getResourceBundle(getULocale()));
		}
		return ds;
	}

	private void validateStockEntryData(Object[] obja) throws ChartException {
		boolean valid = true;

		if (obja == null) {
			valid = false;
		} else if (obja.length != 4) {
			throw new ChartException(ChartEngineExtensionPlugin.ID, ChartException.DATA_SET,
					"exception.dataset.stockseries", //$NON-NLS-1$
					Messages.getResourceBundle(getULocale()));
		}
		// !ignore this check, we can handle the invalid case now.
		// else
		// {
		// for ( int i = 0; i < obja.length; i++ )
		// {
		// if ( !( obja[i] instanceof Number ) )
		// {
		// // valid = false;
		// break;
		// }
		// }
		// }

		if (!valid) {
			throw new ChartException(ChartEngineExtensionPlugin.ID, ChartException.VALIDATION,
					"exception.dataset.null.stockentry", //$NON-NLS-1$
					Messages.getResourceBundle(getULocale()));
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.chart.datafeed.IDataSetProcessor#getMinimum(org.eclipse.birt
	 * .chart.model.data.DataSet)
	 */
	public final Object getMinimum(DataSet ds) throws ChartException {
		DataSetIterator dsi = null;
		try {
			dsi = new DataSetIterator(ds);
			dsi.reset();
		} catch (IllegalArgumentException uiex) {
			throw new ChartException(ChartEngineExtensionPlugin.ID, ChartException.DATA_SET, uiex);
		}
		if (dsi.size() == 0) {
			throw new ChartException(ChartEngineExtensionPlugin.ID, ChartException.DATA_SET, "exception.empty.dataset", //$NON-NLS-1$
					Messages.getResourceBundle(getULocale()));
		}
		StockEntry sde;

		double[] da = new double[4];
		double dMin = Double.MAX_VALUE;
		while (dsi.hasNext()) {
			sde = (StockEntry) dsi.next();
			if (sde != null) {
				da[0] = sde.getOpen();
				da[1] = sde.getClose();
				da[2] = sde.getLow();
				da[3] = sde.getHigh();

				for (int j = 0; j < 4; j++) {
					if (dMin > da[j]) {
						dMin = da[j];
					}
				}
			}
		}
		return new Double(dMin);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.chart.datafeed.IDataSetProcessor#getMaximum(org.eclipse.birt
	 * .chart.model.data.DataSet)
	 */
	public final Object getMaximum(DataSet ds) throws ChartException {
		DataSetIterator dsi = null;
		try {
			dsi = new DataSetIterator(ds);
			dsi.reset();
		} catch (IllegalArgumentException uiex) {
			throw new ChartException(ChartEngineExtensionPlugin.ID, ChartException.DATA_SET, uiex);
		}
		if (dsi.size() == 0) {
			throw new ChartException(ChartEngineExtensionPlugin.ID, ChartException.DATA_SET, "exception.empty.dataset", //$NON-NLS-1$
					Messages.getResourceBundle(getULocale()));
		}
		StockEntry sde;

		double[] da = new double[4];
		double dMax = -Double.MAX_VALUE;
		while (dsi.hasNext()) {
			sde = (StockEntry) dsi.next();
			if (sde != null) {
				da[0] = sde.getOpen();
				da[1] = sde.getClose();
				da[2] = sde.getLow();
				da[3] = sde.getHigh();

				for (int j = 0; j < 4; j++) {
					if (dMax < da[j]) {
						dMax = da[j];
					}
				}
			}
		}
		return new Double(dMax);
	}

	/**
	 * This method takes the data in String form and populates the DataSet (creating
	 * one if necessary). For the StockDataElement, the data should be provided in
	 * the form: 'H <high value> L <low value> O <open value> C <close value>, H
	 * <next high value> L...' i.e. 'H', 'L', 'O' and 'C' are used to designate a
	 * value as either the high, low, open or close component of the data element.
	 * DataElements should be separated by commas (,). Components within the data
	 * element are separated by a space and their sequence is not important.
	 * 
	 * @return DataSet populated by the entries in the String or null if the String
	 *         is null.
	 * @throws ChartException if there is any problem parsing the String passed in.
	 * @see org.eclipse.birt.chart.datafeed.IDataSetProcessor#fromString(java.lang.String,
	 *      org.eclipse.birt.chart.model.data.DataSet)
	 */
	public final DataSet fromString(String sDataSetRepresentation, DataSet ds) throws ChartException {
		// Do NOT create a DataSet if the content string is null
		if (sDataSetRepresentation == null) {
			return ds;
		}
		// Create an EMPTY DataSet if the content string is an empty string
		if (ds == null) {
			ds = StockDataSetImpl.create(null);
		}
		StringTokenizer strTokDataElement = new StringTokenizer(sDataSetRepresentation, ","); //$NON-NLS-1$
		StringTokenizer strTokComponents = null;
		String strDataElement = null;
		String strComponent = null;
		List vData = new ArrayList();
		while (strTokDataElement.hasMoreTokens()) {
			strDataElement = strTokDataElement.nextToken().trim();

			// Build a StockDataElement from this token
			strTokComponents = new StringTokenizer(strDataElement);

			// Compatible with other sample data
			if (strTokComponents.countTokens() == 1) {
				double dComponent = Double.parseDouble(strDataElement);
				vData.add(new StockEntry(dComponent, dComponent - 2, dComponent * 2, dComponent + 2));
				continue;
			}

			StockEntry entry = new StockEntry(Double.NaN, Double.NaN, Double.NaN, Double.NaN);
			while (strTokComponents.hasMoreTokens()) {
				strComponent = strTokComponents.nextToken().trim().toUpperCase();
				double dComponent = Double.parseDouble(strComponent.substring(1));
				if (strComponent.startsWith("H")) //$NON-NLS-1$
				{
					entry.setHigh(dComponent);
				} else if (strComponent.startsWith("L")) //$NON-NLS-1$
				{
					entry.setLow(dComponent);
				} else if (strComponent.startsWith("O")) //$NON-NLS-1$
				{
					entry.setOpen(dComponent);
				} else if (strComponent.startsWith("C")) //$NON-NLS-1$
				{
					entry.setClose(dComponent);
				}
			}
			vData.add(entry);
		}
		ds.setValues(vData);
		return ds;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.chart.datafeed.DataSetProcessor#getExpectedStringFormat()
	 */
	public String getExpectedStringFormat() {
		return Messages.getString("info.stock.sample.format", //$NON-NLS-1$
				getULocale());
	}

	public String toString(Object[] columnData) throws ChartException {
		if (columnData == null || columnData.length == 0) {
			throw new ChartException(ChartEngineExtensionPlugin.ID, ChartException.DATA_SET,
					"exception.base.orthogonal.null.datadefinition", //$NON-NLS-1$
					Messages.getResourceBundle(getULocale()));
		}
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < columnData.length; i++) {
			if (columnData[i] == null) {
				throw new ChartException(ChartEngineExtensionPlugin.ID, ChartException.DATA_SET,
						"exception.base.orthogonal.null.datadefinition", //$NON-NLS-1$
						Messages.getResourceBundle(getULocale()));
			}
			if (columnData[i] instanceof Object[]) {
				buffer.append(toStockString((Object[]) columnData[i]));
			}
			if (i < columnData.length - 1) {
				buffer.append(","); //$NON-NLS-1$
			}
		}
		return buffer.toString();
	}

	private StringBuffer toStockString(Object[] stockArray) throws ChartException {
		if (stockArray.length != 4 || stockArray[0] == null) {
			throw new ChartException(ChartEngineExtensionPlugin.ID, ChartException.DATA_SET, "Invalid data set column"); //$NON-NLS-1$
		}
		StringBuffer buffer = new StringBuffer();
		buffer.append("H" + String.valueOf(stockArray[0]) + " "); //$NON-NLS-1$ //$NON-NLS-2$
		buffer.append("L" + String.valueOf(stockArray[1]) + " "); //$NON-NLS-1$ //$NON-NLS-2$
		buffer.append("O" + String.valueOf(stockArray[2]) + " "); //$NON-NLS-1$ //$NON-NLS-2$
		buffer.append("C" + String.valueOf(stockArray[3])); //$NON-NLS-1$
		return buffer;
	}
}