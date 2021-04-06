/*******************************************************************************
 * Copyright (c) 2004, 2007, 2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.chart.internal.datafeed;

import org.eclipse.birt.chart.computation.IConstants;
import org.eclipse.birt.chart.computation.Methods;
import org.eclipse.birt.chart.datafeed.IResultSetDataSet;
import org.eclipse.birt.chart.engine.i18n.Messages;
import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.log.ILogger;
import org.eclipse.birt.chart.log.Logger;
import org.eclipse.birt.chart.model.data.DataFactory;
import org.eclipse.birt.chart.model.data.DataSet;
import org.eclipse.birt.chart.model.data.impl.DateTimeDataSetImpl;
import org.eclipse.birt.chart.model.data.impl.NullDataSetImpl;
import org.eclipse.birt.chart.model.data.impl.NumberDataSetImpl;
import org.eclipse.birt.chart.model.data.impl.TextDataSetImpl;
import org.eclipse.birt.chart.plugin.ChartEnginePlugin;
import org.eclipse.birt.chart.util.NumberUtil;

import com.ibm.icu.util.Calendar;

/**
 * An internal processor which populates the user datasets.
 */
public class UserDataSetProcessor {
	private static ILogger logger = Logger.getLogger("org.eclipse.birt.chart.engine/datafeed"); //$NON-NLS-1$

	/**
	 * Populates the trigger datasets from given data source. Only Text data is
	 * supported now.
	 * 
	 * @param oResultSetDef
	 * @throws ChartException
	 */
	public DataSet[] populate(Object oResultSetDef) throws ChartException {
		DataSet[] ds;

		if (oResultSetDef instanceof IResultSetDataSet) {
			final IResultSetDataSet rsds = (IResultSetDataSet) oResultSetDef;
			final long lRowCount = rsds.getSize();

			if (lRowCount <= 0) {
				throw new ChartException(ChartEnginePlugin.ID, ChartException.ZERO_DATASET, "exception.empty.dataset", //$NON-NLS-1$
						Messages.getResourceBundle());
			}

			final int columnCount = rsds.getColumnCount();
			ds = new DataSet[columnCount];
			// init dataset
			for (int k = 0; k < columnCount; k++) {
				switch (rsds.getDataType(k)) {
				case IConstants.TEXT:
					final String[] saDataSet = new String[(int) lRowCount];
					ds[k] = TextDataSetImpl.create(saDataSet);
					break;

				case IConstants.DATE_TIME:
					final Calendar[] caDataSet = new Calendar[(int) lRowCount];
					ds[k] = DateTimeDataSetImpl.create(caDataSet);
					break;

				case IConstants.NUMERICAL:
					final Number[] doaDataSet = new Number[(int) lRowCount];
					ds[k] = NumberDataSetImpl.create(doaDataSet);
					break;

				case IConstants.BOOLEAN:
					final Boolean[] boaDataSet = new Boolean[(int) lRowCount];
					DataSet bDS = DataFactory.eINSTANCE.createDataSet();
					bDS.setValues(boaDataSet);
					ds[k] = bDS;
					break;

				case IConstants.ARRAY:
					final Object[] arrayDataSet = new Object[(int) lRowCount];
					DataSet arrayDS = DataFactory.eINSTANCE.createDataSet();
					arrayDS.setValues(arrayDataSet);
					ds[k] = arrayDS;
					break;

				default:
					boolean allNullValues = true;
					while (rsds.hasNext()) {
						if (rsds.next()[k] != null) {
							allNullValues = false;
							break;
						}
					}
					rsds.reset();
					if (!allNullValues) {
						// if can't determine applicable data type
						throw new ChartException(ChartEnginePlugin.ID, ChartException.DATA_SET,
								"exception.unknown.trigger.datatype", //$NON-NLS-1$
								Messages.getResourceBundle());
					} else {
						// create a dummy dataset which represents null
						ds[k] = NullDataSetImpl.create((int) lRowCount);
					}
				}
			}

			int i = 0;
			while (rsds.hasNext()) {

				Object row[] = rsds.next();
				for (int k = 0; k < columnCount; k++) {
					Object value = null;
					switch (rsds.getDataType(k)) {
					case IConstants.TEXT:
					case IConstants.BOOLEAN:
					case IConstants.ARRAY:
						value = row[k];
						break;

					case IConstants.DATE_TIME:
						value = Methods.asDateTime(row[k]);
						break;

					case IConstants.NUMERICAL:
						value = NumberUtil.convertNumber(row[k]);
						break;

					default:
						value = row[k];
						logger.log(new ChartException(ChartEnginePlugin.ID, ChartException.DATA_SET,
								"exception.unknown.trigger.datatype", //$NON-NLS-1$
								Messages.getResourceBundle()));
					}
					((Object[]) ds[k].getValues())[i] = value;
				}
				i++;
			}
		}

		else {
			throw new ChartException(ChartEnginePlugin.ID, ChartException.DATA_SET, "exception.unknown.custom.dataset", //$NON-NLS-1$
					Messages.getResourceBundle());
		}

		return ds;
	}
}
