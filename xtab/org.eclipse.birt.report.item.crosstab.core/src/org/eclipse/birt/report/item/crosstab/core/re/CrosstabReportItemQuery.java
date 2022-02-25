/*******************************************************************************
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
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.item.crosstab.core.re;

import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.IDataQueryDefinition;
import org.eclipse.birt.report.engine.extension.ReportItemQueryBase;
import org.eclipse.birt.report.item.crosstab.core.CrosstabException;
import org.eclipse.birt.report.item.crosstab.core.ICrosstabConstants;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabCellHandle;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabReportItemHandle;
import org.eclipse.birt.report.item.crosstab.core.de.DimensionViewHandle;
import org.eclipse.birt.report.item.crosstab.core.de.LevelViewHandle;
import org.eclipse.birt.report.item.crosstab.core.de.MeasureViewHandle;
import org.eclipse.birt.report.item.crosstab.core.i18n.Messages;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.ReportElementHandle;
import org.eclipse.birt.report.model.api.extension.ExtendedElementException;

/**
 * CrosstabReportItemQuery
 */
public class CrosstabReportItemQuery extends ReportItemQueryBase implements ICrosstabConstants {

	private static Logger logger = Logger.getLogger(CrosstabReportItemQuery.class.getName());

	private CrosstabReportItemHandle crosstabItem;

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.engine.extension.ReportItemQueryBase#setModelObject
	 * (org.eclipse.birt.report.model.api.ExtendedItemHandle)
	 */
	@Override
	public void setModelObject(ExtendedItemHandle modelHandle) {
		super.setModelObject(modelHandle);

		try {
			crosstabItem = (CrosstabReportItemHandle) modelHandle.getReportItem();
		} catch (ExtendedElementException e) {
			logger.log(Level.SEVERE, Messages.getString("CrosstabReportItemQuery.error.crosstab.loading")); //$NON-NLS-1$
			crosstabItem = null;
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @seeorg.eclipse.birt.report.engine.extension.ReportItemQueryBase#
	 * createReportQueries (org.eclipse.birt.data.engine.api.IDataQueryDefinition)
	 */
	@Override
	public IDataQueryDefinition[] createReportQueries(IDataQueryDefinition parent) throws BirtException {
		if (crosstabItem == null) {
			throw new CrosstabException(Messages.getString("CrosstabReportItemQuery.error.query.building")); //$NON-NLS-1$
		}

		IDataQueryDefinition cubeQuery = CrosstabQueryUtil.createCubeQuery(crosstabItem, parent,
				context.getDataRequestSession().getModelAdaptor(), true, true, true, true, true, true);

		// build child element query
		if (context != null) {
			// process crosstab header
			int headerCount = crosstabItem.getHeaderCount();

			for (int i = 0; i < headerCount; i++) {
				processChildQuery(cubeQuery, crosstabItem.getHeader(i));
			}

			// process measure
			for (int i = 0; i < crosstabItem.getMeasureCount(); i++) {
				// TODO check visibility?
				MeasureViewHandle mv = crosstabItem.getMeasure(i);

				processChildQuery(cubeQuery, mv.getCell());

				for (int j = 0; j < mv.getHeaderCount(); j++) {
					processChildQuery(cubeQuery, mv.getHeader(j));
				}

				for (int j = 0; j < mv.getAggregationCount(); j++) {
					processChildQuery(cubeQuery, mv.getAggregationCell(j));
				}
			}

			// process row edge
			if (crosstabItem.getDimensionCount(ROW_AXIS_TYPE) > 0) {
				// TODO check visibility?
				for (int i = 0; i < crosstabItem.getDimensionCount(ROW_AXIS_TYPE); i++) {
					DimensionViewHandle dv = crosstabItem.getDimension(ROW_AXIS_TYPE, i);

					for (int j = 0; j < dv.getLevelCount(); j++) {
						LevelViewHandle lv = dv.getLevel(j);

						processChildQuery(cubeQuery, lv.getCell());
						processChildQuery(cubeQuery, lv.getAggregationHeader());
					}
				}

			}

			// process column edge
			if (crosstabItem.getDimensionCount(COLUMN_AXIS_TYPE) > 0) {
				// TODO check visibility?
				for (int i = 0; i < crosstabItem.getDimensionCount(COLUMN_AXIS_TYPE); i++) {
					DimensionViewHandle dv = crosstabItem.getDimension(COLUMN_AXIS_TYPE, i);

					for (int j = 0; j < dv.getLevelCount(); j++) {
						LevelViewHandle lv = dv.getLevel(j);

						processChildQuery(cubeQuery, lv.getCell());
						processChildQuery(cubeQuery, lv.getAggregationHeader());
					}
				}

			}

			// process grandtotal header
			processChildQuery(cubeQuery, crosstabItem.getGrandTotal(ROW_AXIS_TYPE));
			processChildQuery(cubeQuery, crosstabItem.getGrandTotal(COLUMN_AXIS_TYPE));
		}

		return new IDataQueryDefinition[] { cubeQuery };
	}

	private void processChildQuery(IDataQueryDefinition parent, CrosstabCellHandle cell) {
		if (cell != null) {
			for (Iterator itr = cell.getContents().iterator(); itr.hasNext();) {
				ReportElementHandle handle = (ReportElementHandle) itr.next();

				context.createQuery(parent, handle);
			}
		}
	}

}
