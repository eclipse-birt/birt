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

import java.text.ParseException;
import java.util.Date;
import java.util.Vector;

import org.eclipse.birt.chart.computation.DataSetIterator;
import org.eclipse.birt.chart.datafeed.DataSetAdapter;
import org.eclipse.birt.chart.datafeed.IResultSetDataSet;
import org.eclipse.birt.chart.engine.extension.i18n.Messages;
import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.data.DataSet;
import org.eclipse.birt.chart.model.data.impl.GanttDataSetImpl;
import org.eclipse.birt.chart.plugin.ChartEngineExtensionPlugin;
import org.eclipse.birt.chart.util.CDateTime;

import com.ibm.icu.text.SimpleDateFormat;
import com.ibm.icu.util.Calendar;
import com.ibm.icu.util.StringTokenizer;
import com.ibm.icu.util.TimeZone;
import com.ibm.icu.util.ULocale;

/**
 * Capable of processing data sets that contain Gantt entry data elements that
 * wrap 3 values - start datetime, end datetime and a row label
 */
public final class GanttDataSetProcessorImpl extends DataSetAdapter {

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

			final GanttEntry[] gea = new GanttEntry[(int) lRowCount];
			while (rsds.hasNext()) {
				Object[] oThreeComponents = rsds.next();

				validateGanttEntryData(oThreeComponents);

				gea[i++] = new GanttEntry(oThreeComponents);
			}
			if (ds == null) {
				ds = GanttDataSetImpl.create(gea);
			} else {
				ds.setValues(gea);
			}
		} else {
			throw new ChartException(ChartEngineExtensionPlugin.ID, ChartException.DATA_SET,
					"exception.unknown.custom.dataset", //$NON-NLS-1$
					new Object[] { ds, oResultSetDef }, Messages.getResourceBundle(getULocale()));
		}
		return ds;
	}

	private void validateGanttEntryData(Object[] obja) throws ChartException {
		boolean valid = true;

		if (obja == null) {
			valid = false;
		} else if (obja.length != 3) {
			throw new ChartException(ChartEngineExtensionPlugin.ID, ChartException.DATA_SET,
					"exception.dataset.ganttseries", //$NON-NLS-1$
					Messages.getResourceBundle(getULocale()));
		} else {
			for (int i = 0; i < obja.length - 1; i++) {
				if (obja[i] != null && !(obja[i] instanceof CDateTime)) {
					valid = false;
					break;
				}
			}
		}

		if (!valid) {
			throw new ChartException(ChartEngineExtensionPlugin.ID, ChartException.VALIDATION,
					"exception.dataset.invalid.ganttentry", //$NON-NLS-1$
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

		GanttEntry gde;

		CDateTime[] cala = new CDateTime[2];
		CDateTime calMin = null;
		while (dsi.hasNext()) {
			gde = (GanttEntry) dsi.next();
			if (gde != null) {
				cala[0] = gde.getStart();
				cala[1] = gde.getEnd();

				for (int j = 0; j < 2; j++) {
					if (cala[j] == null)
						continue;
					if (calMin != null) {
						if (calMin.after(cala[j])) {
							calMin = cala[j];
						}
					} else if (cala[j] != null) {
						calMin = cala[j];
					}
				}
			}
		}
		return calMin;
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

		GanttEntry gde;

		CDateTime[] cala = new CDateTime[2];
		CDateTime calMax = null;
		while (dsi.hasNext()) {
			gde = (GanttEntry) dsi.next();
			if (gde != null) {
				cala[0] = gde.getStart();
				cala[1] = gde.getEnd();

				for (int j = 0; j < 2; j++) {
					if (cala[j] == null)
						continue;
					if (calMax != null) {
						if (calMax.before(cala[j])) {
							calMax = cala[j];
						}
					} else if (cala[j] != null) {
						calMax = cala[j];
					}
				}
			}
		}
		return calMax;
	}

	/**
	 * This method takes the data in String form and populates the DataSet (creating
	 * one if necessary). For the GanttDataElement, the data should be provided in
	 * the form: 'S <start date> E <end date> <row label>, S <next start date> E...'
	 * i.e. 'S' and 'E' are used to designate a value as either the start or end
	 * date component of the data element. DataElements should be separated by
	 * commas (,). Components within the data element are separated by a space and
	 * their sequence is not important.
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
			ds = GanttDataSetImpl.create(null);
		}
		StringTokenizer strTokDataElement = new StringTokenizer(sDataSetRepresentation, ","); //$NON-NLS-1$
		StringTokenizer strTokComponents = null;
		String strDataElement = null;
		String strComponent = null;
		Vector<GanttEntry> vData = new Vector<GanttEntry>();
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", ULocale.getDefault()); //$NON-NLS-1$

		int i = 1;
		while (strTokDataElement.hasMoreTokens()) {
			strDataElement = strTokDataElement.nextToken().trim();
			// Build a GanttDataElement from this token
			strTokComponents = new StringTokenizer(strDataElement);
			// Compatible with other sample data
			if (strTokComponents.countTokens() == 1) {
				Date startElement = null;
				try {
					startElement = sdf.parse(strDataElement);
				} catch (ParseException e1) {
					// invalid sample data, use current date instead.
					startElement = Calendar.getInstance(TimeZone.getDefault()).getTime();
				}
				Date endElement = new Date(startElement.getTime() + startElement.getTime() / 20);
				vData.add(new GanttEntry(new CDateTime(startElement), new CDateTime(endElement),
						Messages.getString("GanttDataSetProcessorImpl.data.label") + i)); //$NON-NLS-1$

				i++;
				continue;
			}
			GanttEntry entry = new GanttEntry((CDateTime) null, (CDateTime) null, null);

			while (strTokComponents.hasMoreTokens()) {
				strComponent = strTokComponents.nextToken().trim();
				try {
					if (strComponent.toUpperCase().startsWith("S")) //$NON-NLS-1$
					{
						Date dateComponent = sdf.parse(strComponent.substring(1));
						entry.setStart(new CDateTime(dateComponent));
					} else if (strComponent.toUpperCase().startsWith("E")) //$NON-NLS-1$
					{
						Date dateComponent = sdf.parse(strComponent.substring(1));
						entry.setEnd(new CDateTime(dateComponent));
					} else {
						entry.setLabel(strComponent);
					}
				} catch (ParseException e) {
					// Insert error-handling here
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
		return Messages.getString("info.gantt.sample.format", //$NON-NLS-1$
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
				buffer.append(toGanttString((Object[]) columnData[i]));
			}
			if (i < columnData.length - 1) {
				buffer.append(","); //$NON-NLS-1$
			}
		}
		return buffer.toString();
	}

	private StringBuffer toGanttString(Object[] ganttArray) throws ChartException {
		if (ganttArray.length != 3 || ganttArray[0] == null) {
			throw new ChartException(ChartEngineExtensionPlugin.ID, ChartException.DATA_SET, "Invalid data set column"); //$NON-NLS-1$
		}
		StringBuffer buffer = new StringBuffer();
		buffer.append("S" + String.valueOf(ganttArray[0]) + " "); //$NON-NLS-1$ //$NON-NLS-2$
		buffer.append("E" + String.valueOf(ganttArray[1]) + " "); //$NON-NLS-1$ //$NON-NLS-2$
		buffer.append(String.valueOf(ganttArray[2]));
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
	public int[] getDataDefIdsForGrouping(Series series) {
		return new int[] { 0, 1 };
	}

}