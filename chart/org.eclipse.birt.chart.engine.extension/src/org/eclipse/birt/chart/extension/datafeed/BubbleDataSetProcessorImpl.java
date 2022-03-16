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

package org.eclipse.birt.chart.extension.datafeed;

import java.text.ParseException;
import java.util.StringTokenizer;
import java.util.Vector;

import org.eclipse.birt.chart.computation.DataSetIterator;
import org.eclipse.birt.chart.computation.IConstants;
import org.eclipse.birt.chart.datafeed.DataSetAdapter;
import org.eclipse.birt.chart.datafeed.IResultSetDataSet;
import org.eclipse.birt.chart.engine.extension.i18n.Messages;
import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.data.DataSet;
import org.eclipse.birt.chart.model.data.impl.BubbleDataSetImpl;
import org.eclipse.birt.chart.model.data.impl.DataSetImpl;
import org.eclipse.birt.chart.plugin.ChartEngineExtensionPlugin;
import org.eclipse.birt.chart.util.BigNumber;
import org.eclipse.birt.chart.util.CDateTime;
import org.eclipse.birt.chart.util.NumberUtil;

import com.ibm.icu.math.BigDecimal;
import com.ibm.icu.text.SimpleDateFormat;

/**
 * Capable of processing data sets that contains Bubble entry that wraps two
 * values (e.g. Y value, size value)
 */
public class BubbleDataSetProcessorImpl extends DataSetAdapter {

	/**
	 * A default constructor provided for successful creation
	 */
	public BubbleDataSetProcessorImpl() {
		super();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.chart.datafeed.IDataSetProcessor#getMaximum(org.eclipse.birt
	 * .chart.model.data.DataSet)
	 */
	@Override
	public Object getMaximum(DataSet ds) throws ChartException {
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

		BubbleEntry bde;
		CDateTime cMax = null;
		double dMax = -Double.MAX_VALUE;
		Number bnMax = null;
		for (int i = 0; dsi.hasNext(); i++) {
			bde = (BubbleEntry) dsi.next();
			if (bde != null) {
				Object oValue = bde.getValue();
				if (oValue == null) {
					continue;
				}

				if (NumberUtil.isBigNumber(oValue)) {
					BigNumber bnValue = (BigNumber) oValue;
					if (bde.getSizeNumber() != null) {
						bnValue = bnValue.add((BigNumber) bde.getSizeNumber());
					}
					if (bnMax == null) {
						bnMax = bnValue;
					} else {
						bnMax = ((BigNumber) bnMax).max(bnValue);
					}
				} else if (oValue instanceof BigDecimal) {
					BigDecimal bnValue = (BigDecimal) oValue;
					if (bde.getSizeNumber() != null) {
						bnValue = bnValue.add((BigDecimal) bde.getSizeNumber());
					}
					if (bnMax == null) {
						bnMax = bnValue;
					} else {
						bnMax = ((BigDecimal) bnMax).max(bnValue);
					}
				} else if (oValue instanceof java.math.BigDecimal) {
					java.math.BigDecimal bnValue = (java.math.BigDecimal) oValue;
					if (bde.getSizeNumber() != null) {
						bnValue = bnValue.add((java.math.BigDecimal) bde.getSizeNumber());
					}
					if (bnMax == null) {
						bnMax = bnValue;
					} else {
						bnMax = ((java.math.BigDecimal) bnMax).max(bnValue);
					}
				} else if (oValue instanceof Number) {
					double dValue = ((Number) oValue).doubleValue();
					if (i == 0) {
						dMax = dValue;
					} else {
						dMax = Math.max(dMax, dValue);
					}
				} else if (oValue instanceof CDateTime) {
					CDateTime cValue = (CDateTime) oValue;
					if (i == 0) {
						cMax = cValue;
					} else if (cValue.after(cMax)) {
						cMax = cValue;
					}
				}
			}
		}
		if (cMax == null) {
			return bnMax != null ? bnMax : new Double(dMax);
		}
		return cMax;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.chart.datafeed.IDataSetProcessor#getMinimum(org.eclipse.birt
	 * .chart.model.data.DataSet)
	 */
	@Override
	public Object getMinimum(DataSet ds) throws ChartException {
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

		BubbleEntry bde;
		CDateTime cMin = null;
		double dMin = Double.MAX_VALUE;
		Number bnMin = null;
		for (int i = 0; dsi.hasNext(); i++) {
			bde = (BubbleEntry) dsi.next();
			if (bde != null) {
				Object oValue = bde.getValue();
				if (oValue == null) {
					continue;
				}
				if (NumberUtil.isBigNumber(oValue)) {
					BigNumber bnValue = (BigNumber) oValue;
					if (bde.getSizeNumber() != null) {
						bnValue = bnValue.subtract((BigNumber) bde.getSizeNumber());
					}
					if (bnMin == null) {
						bnMin = bnValue;
					} else {
						bnMin = ((BigNumber) bnMin).min(bnValue);
					}
				} else if (oValue instanceof BigDecimal) {
					BigDecimal bnValue = (BigDecimal) oValue;
					if (bde.getSizeNumber() != null) {
						bnValue = bnValue.subtract((BigDecimal) bde.getSizeNumber());
					}
					if (bnMin == null) {
						bnMin = bnValue;
					} else {
						bnMin = ((BigDecimal) bnMin).min(bnValue);
					}
				} else if (oValue instanceof java.math.BigDecimal) {
					java.math.BigDecimal bnValue = (java.math.BigDecimal) oValue;
					if (bde.getSizeNumber() != null) {
						bnValue = bnValue.subtract((java.math.BigDecimal) bde.getSizeNumber());
					}
					if (bnMin == null) {
						bnMin = bnValue;
					} else {
						bnMin = ((java.math.BigDecimal) bnMin).min(bnValue);
					}
				} else if (oValue instanceof Number) {
					double dValue = ((Number) oValue).doubleValue();
					if (i == 0) {
						dMin = dValue;
					} else {
						dMin = Math.min(dMin, dValue);
					}
				} else if (oValue instanceof CDateTime) {
					CDateTime cValue = (CDateTime) oValue;
					if (i == 0) {
						cMin = cValue;
					} else if (cValue.before(cMin)) {
						cMin = cValue;
					}
				}
			}
		}
		if (cMin == null) {
			return bnMin != null ? bnMin : new Double(dMin);
		}
		return cMin;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.chart.model.data.IDataSetProcessor#populate(java.lang.
	 * Object, org.eclipse.birt.chart.model.data.DataSet)
	 */
	@Override
	public final DataSet populate(Object oResultSetDef, DataSet ds) throws ChartException {
		if (oResultSetDef instanceof IResultSetDataSet) {
			final IResultSetDataSet rsds = (IResultSetDataSet) oResultSetDef;
			final long lRowCount = rsds.getSize();

			if (lRowCount <= 0) {
				throw new ChartException(ChartEngineExtensionPlugin.ID, ChartException.ZERO_DATASET,
						"exception.empty.dataset", //$NON-NLS-1$
						Messages.getResourceBundle(getULocale()));
			}

			// Get data types for bubble value
			int dataType = rsds.getDataType(0);
			boolean isBigDecimal = false;
			int i = 0;
			final BubbleEntry[] bea = new BubbleEntry[(int) lRowCount];

			if (dataType == IConstants.NUMERICAL) {
				while (rsds.hasNext()) {
					Object[] o = rsds.next();
					validateBubbleEntryData(o);
					bea[i++] = new BubbleEntry(NumberUtil.convertNumber(o[0]), NumberUtil.convertNumber(o[1]));
					if (!isBigDecimal && NumberUtil.isBigDecimal(o[0]) || NumberUtil.isBigDecimal(o[1])) {
						isBigDecimal = true;
					}
				}
			} else {
				while (rsds.hasNext()) {
					Object[] o = rsds.next();
					validateBubbleEntryData(o);
					Object value = o[0];
					Object size = o[1];
					if (dataType == IConstants.DATE_TIME) {
						bea[i] = new BubbleEntry(value == null ? null : value, size);
					} else {
						// For category type
						bea[i] = new BubbleEntry(value, size, i + 1);
					}
					i++;
				}
			}

			if (ds == null) {
				ds = BubbleDataSetImpl.create(bea);
			} else {
				ds.setValues(bea);
			}

			((DataSetImpl) ds).setIsBigNumber(isBigDecimal);
		} else {
			throw new ChartException(ChartEngineExtensionPlugin.ID, ChartException.DATA_SET,
					"exception.unknown.custom.dataset", //$NON-NLS-1$
					new Object[] { ds, oResultSetDef }, Messages.getResourceBundle(getULocale()));
		}
		return ds;
	}

	private void validateBubbleEntryData(Object[] obja) throws ChartException {
		boolean valid = true;

		if (obja == null) {
			valid = false;
		} else if (obja.length != 2) {
			throw new ChartException(ChartEngineExtensionPlugin.ID, ChartException.DATA_SET,
					"exception.dataset.bubbleseries", //$NON-NLS-1$
					Messages.getResourceBundle(getULocale()));
		}

		if (!valid) {
			throw new ChartException(ChartEngineExtensionPlugin.ID, ChartException.VALIDATION,
					"exception.dataset.null.bubbleentry", //$NON-NLS-1$
					Messages.getResourceBundle(getULocale()));
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.chart.datafeed.DataSetProcessor#fromString(java.lang.String,
	 * org.eclipse.birt.chart.model.data.DataSet)
	 */
	@Override
	public final DataSet fromString(String sDataSetRepresentation, DataSet ds) throws ChartException {
		// Do NOT create a DataSet if the content string is null
		if (sDataSetRepresentation == null) {
			return ds;
		}
		// Create an EMPTY DataSet if the content string is an empty string
		if (ds == null) {
			ds = BubbleDataSetImpl.create(null);
		}
		StringTokenizer strTokDataElement = new StringTokenizer(sDataSetRepresentation, ","); //$NON-NLS-1$
		StringTokenizer strTokComponents = null;
		String strDataElement = null;
		String strComponent = null;
		Vector<BubbleEntry> vData = new Vector<>();
		int[] sizes = { 3, 5, 4, 6, 4 };
		int i = 0;
		while (strTokDataElement.hasMoreTokens()) {
			strDataElement = strTokDataElement.nextToken().trim();
			strTokComponents = new StringTokenizer(strDataElement);
			// Compatible with other sample data
			if (strTokComponents.countTokens() == 1) {
				Object value = null;
				try {
					// Parse number
					value = new Double(Double.parseDouble(strDataElement));
				} catch (NumberFormatException ex) {
					SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy"); //$NON-NLS-1$
					try {
						// Parse date
						value = new CDateTime(sdf.parse(strDataElement));
					} catch (ParseException e) {
						value = null;
					}
				}
				vData.add(new BubbleEntry(value, Integer.valueOf(sizes[i++])));
				continue;
			}
			BubbleEntry entry = new BubbleEntry(Integer.valueOf(1), Integer.valueOf(1));

			// Build a BubbleDataElement from this token

			while (strTokComponents.hasMoreTokens()) {
				strComponent = strTokComponents.nextToken().trim().toUpperCase();
				if (strComponent.startsWith("Y")) //$NON-NLS-1$
				{
					entry.setValue(strComponent.substring(1));
				} else if (strComponent.startsWith("S")) //$NON-NLS-1$
				{
					entry.setSize(Double.parseDouble(strComponent.substring(1)));
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
	@Override
	public String getExpectedStringFormat() {
		return Messages.getString("info.bubble.sample.format", getULocale()); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.chart.datafeed.DataSetAdapter#toString(java.lang.Object[])
	 */
	@Override
	public String toString(Object[] columnData) throws ChartException {
		if (columnData == null || columnData.length == 0) {
			throw new ChartException(ChartEngineExtensionPlugin.ID, ChartException.DATA_SET,
					"exception.base.orthogonal.null.datadefinition", //$NON-NLS-1$
					Messages.getResourceBundle(getULocale()));
		}
		StringBuilder buffer = new StringBuilder();
		for (int i = 0; i < columnData.length; i++) {
			if (columnData[i] == null) {
				throw new ChartException(ChartEngineExtensionPlugin.ID, ChartException.DATA_SET,
						"exception.base.orthogonal.null.datadefinition", //$NON-NLS-1$
						Messages.getResourceBundle(getULocale()));
			}
			if (columnData[i] instanceof Object[]) {
				buffer.append(toBubbleString((Object[]) columnData[i]));
			}
			if (i < columnData.length - 1) {
				buffer.append(","); //$NON-NLS-1$
			}
		}
		return buffer.toString();
	}

	private StringBuffer toBubbleString(Object[] bubbleArray) throws ChartException {
		if (bubbleArray.length != 2 || bubbleArray[0] == null) {
			throw new ChartException(ChartEngineExtensionPlugin.ID, ChartException.DATA_SET, "Invalid data set column"); //$NON-NLS-1$
		}
		StringBuffer buffer = new StringBuffer();
		buffer.append("Y" + String.valueOf(bubbleArray[0]) + " "); //$NON-NLS-1$ //$NON-NLS-2$
		buffer.append("S" + String.valueOf(bubbleArray[1])); //$NON-NLS-1$
		return buffer;
	}

	/**
	 * return the array of indexes, the DataDefinitions with these id could be
	 * computed with aggregation function. By default, all the indexes will be added
	 * to the array. And this method should be overridden for some special chart
	 * types, such as bubble, stock...
	 *
	 * @param series
	 * @return index array
	 */
	@Override
	public int[] getDataDefIdsForGrouping(Series series) {
		return new int[] { 0, 1 };
	}

}
