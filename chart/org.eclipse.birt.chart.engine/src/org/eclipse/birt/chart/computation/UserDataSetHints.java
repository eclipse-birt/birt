/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.chart.computation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.model.data.DataSet;
import org.eclipse.emf.common.util.EMap;

/**
 * This class provides the ability to process the user dataSets.
 * 
 * WARNING: This is an internal class and subject to change
 */
public class UserDataSetHints {

	private String[] keys;
	private DataSetIterator[] dsis;

	/**
	 * The constructor.
	 * 
	 * @param allSeriesDataSets
	 * @throws ChartException
	 */
	public UserDataSetHints(EMap<String, DataSet> allSeriesDataSets) throws ChartException {
		this(allSeriesDataSets, false);
	}

	/**
	 * The constructor.
	 * 
	 * @param allSeriesDataSets
	 * @param bReverse          indicates if category is reversed
	 * @throws ChartException
	 */
	public UserDataSetHints(EMap<String, DataSet> allSeriesDataSets, boolean bReverse) throws ChartException {
		List<String> keyList = new ArrayList<String>();
		List<DataSetIterator> dsiList = new ArrayList<DataSetIterator>();

		for (Map.Entry<String, DataSet> entry : allSeriesDataSets.entrySet()) {
			if (entry.getKey() != null) {
				String key = entry.getKey();
				DataSet ds = entry.getValue();
				DataSetIterator dsi = new DataSetIterator(ds);
				dsi.reverse(bReverse);

				keyList.add(key);
				dsiList.add(dsi);
			}
		}

		keys = keyList.toArray(new String[keyList.size()]);
		dsis = dsiList.toArray(new DataSetIterator[dsiList.size()]);
	}

	/**
	 * Resets all associated datasetiterators.
	 */
	public final void reset() {
		for (int i = 0; i < dsis.length; i++) {
			dsis[i].reset();
		}
	}

	/**
	 * Next all associated datasetiterators and update the datapointhints object.
	 * 
	 * @param dph
	 */
	public final void next(DataPointHints dph) {
		for (int i = 0; i < keys.length; i++) {
			if (dsis[i].hasNext()) {
				Object val = dsis[i].next();
				if (dph != null) {
					dph.setUserValue(keys[i], val);
				}
			}
		}
	}
}