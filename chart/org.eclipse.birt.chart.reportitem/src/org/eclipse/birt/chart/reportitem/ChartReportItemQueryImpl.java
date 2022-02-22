/***********************************************************************
 * Copyright (c) 2005, 2007, 2008 Actuate Corporation.
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

package org.eclipse.birt.chart.reportitem;

import org.eclipse.birt.chart.log.ILogger;
import org.eclipse.birt.chart.log.Logger;
import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.reportitem.api.ChartItemUtil;
import org.eclipse.birt.chart.reportitem.api.ChartReportItemHelper;
import org.eclipse.birt.chart.reportitem.i18n.Messages;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.IBaseQueryDefinition;
import org.eclipse.birt.data.engine.api.IDataQueryDefinition;
import org.eclipse.birt.data.engine.api.IQueryDefinition;
import org.eclipse.birt.data.engine.olap.api.query.ICubeQueryDefinition;
import org.eclipse.birt.report.data.adapter.api.IModelAdapter;
import org.eclipse.birt.report.engine.extension.ReportItemQueryBase;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabReportItemHandle;
import org.eclipse.birt.report.item.crosstab.core.re.CrosstabQueryUtil;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.ListingHandle;
import org.eclipse.birt.report.model.api.MultiViewsHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.extension.ExtendedElementException;
import org.eclipse.birt.report.model.api.extension.IReportItem;

/**
 * Customized query implementation for Chart.
 */
public final class ChartReportItemQueryImpl extends ReportItemQueryBase {

	private Chart cm = null;

	private ExtendedItemHandle eih = null;

	private static ILogger logger = Logger.getLogger("org.eclipse.birt.chart.reportitem/trace"); //$NON-NLS-1$

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.engine.extension.IReportItemQuery#setModelObject
	 * (org.eclipse.birt.report.model.api.ExtendedItemHandle)
	 */
	@Override
	public void setModelObject(ExtendedItemHandle eih) {
		IReportItem item;
		try {
			item = eih.getReportItem();
			if (item == null) {
				try {
					eih.loadExtendedElement();
				} catch (ExtendedElementException eeex) {
					logger.log(eeex);
				}
				item = eih.getReportItem();
				if (item == null) {
					logger.log(ILogger.ERROR, Messages.getString("ChartReportItemQueryImpl.log.UnableToLocate")); //$NON-NLS-1$
					return;
				}
			}
		} catch (ExtendedElementException e) {
			logger.log(ILogger.ERROR, Messages.getString("ChartReportItemQueryImpl.log.UnableToLocate")); //$NON-NLS-1$
			return;
		}
		cm = (Chart) ((ChartReportItemImpl) item).getProperty("chart.instance"); //$NON-NLS-1$
		this.eih = eih;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @seeorg.eclipse.birt.report.engine.extension.ReportItemQueryBase#
	 * createReportQueries (org.eclipse.birt.data.engine.api.IDataQueryDefinition)
	 */
	@Override
	public IDataQueryDefinition[] createReportQueries(IDataQueryDefinition parent) throws BirtException {
		logger.log(ILogger.INFORMATION, Messages.getString("ChartReportItemQueryImpl.log.getReportQueries.start")); //$NON-NLS-1$

		if (cm == null) {
			return null;
		}
		IDataQueryDefinition idqd = createQuery(eih, parent);
		logger.log(ILogger.INFORMATION, Messages.getString("ChartReportItemQueryImpl.log.getReportQueries.end")); //$NON-NLS-1$

		// Set push down flag into query.
		if (idqd instanceof IQueryDefinition) {
			((IQueryDefinition) idqd).getQueryExecutionHints().setEnablePushDown(eih.pushDown());
		}

		return new IDataQueryDefinition[] { idqd };
	}

	/**
	 * Create query definition by report item handle.
	 *
	 * @param handle
	 * @param parent
	 * @return
	 * @throws BirtException
	 */
	IDataQueryDefinition createQuery(ExtendedItemHandle handle, IDataQueryDefinition parent) throws BirtException {

		IModelAdapter modelAdapter = context.getDataRequestSession().getModelAdaptor();
		if (ChartReportItemHelper.instance().getBindingDataSetHandle(handle) != null
				|| (ChartReportItemHelper.instance().getBindingCubeHandle(handle) == null
						&& parent instanceof IBaseQueryDefinition)) {
			// If chart is sharing query or in multiple view, it means chart
			// shares
			// bindings/groupings/filters from referred report item handle,
			// so create concrete query definition by getting
			// bindings/groupings/filters/sorts
			// information from referred report item handle.
			ReportItemHandle itemHandle = null;
			if (ChartItemUtil.isChartInheritGroups(handle)) {
				// Share groups and aggregations from container
				DesignElementHandle container = handle.getContainer();
				while (container != null) {
					if (container instanceof ListingHandle) {
						itemHandle = (ListingHandle) container;
						return new ChartSharingQueryHelper(handle, cm, modelAdapter).createQuery(parent);
					}
					container = container.getContainer();
				}
			} else {
				itemHandle = ChartItemUtil.getReportItemReference(handle);
			}
			if (itemHandle != null) {
				return new ChartSharingQueryHelper(itemHandle, cm, modelAdapter).createQuery(parent);
			}

			return ChartReportItemUtil.instanceQueryHelper(handle, cm, modelAdapter).createBaseQuery(parent);
		} else if (ChartReportItemHelper.instance().getBindingCubeHandle(handle) != null
				|| parent instanceof ICubeQueryDefinition) {
			// Fixed ED 28
			// Here we just check multiple view, because chart doesn't need
			// create query for sharing xtab case.
			if (handle.getContainer() instanceof MultiViewsHandle) {
				// Sharing crosstab.
				ExtendedItemHandle bindingHandle = (ExtendedItemHandle) ChartItemUtil.getReportItemReference(handle);
				IDataQueryDefinition cubeQuery = CrosstabQueryUtil.createCubeQuery(
						(CrosstabReportItemHandle) bindingHandle.getReportItem(), parent, modelAdapter, true, true,
						true, true, true, true);
				return cubeQuery;
			}

			// Always create cube query definition by chart itself, even if
			// sharing cross tab's
			return ChartReportItemUtil.instanceCubeQueryHelper(handle, cm, modelAdapter).createCubeQuery(parent);
		}

		return null;
	}
}
