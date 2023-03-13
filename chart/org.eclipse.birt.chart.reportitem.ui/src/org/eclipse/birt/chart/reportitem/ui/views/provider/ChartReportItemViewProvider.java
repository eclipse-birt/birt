/*******************************************************************************
 * Copyright (c) 2007, 2008 Actuate Corporation.
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

package org.eclipse.birt.chart.reportitem.ui.views.provider;

import org.eclipse.birt.chart.log.ILogger;
import org.eclipse.birt.chart.log.Logger;
import org.eclipse.birt.chart.reportitem.ui.ChartReportItemUIFactory;
import org.eclipse.birt.chart.reportitem.ui.i18n.Messages;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.designer.ui.extensions.ReportItemViewAdapter;
import org.eclipse.birt.report.model.api.DesignElementHandle;

/**
 * Provider for creating Chart view in multi-view
 */
public class ChartReportItemViewProvider extends ReportItemViewAdapter {

	protected static ILogger logger = Logger.getLogger("org.eclipse.birt.chart.reportitem.ui"); //$NON-NLS-1$

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.designer.ui.extensions.ReportItemViewAdapter#
	 * createView(org.eclipse.birt.report.model.api.DesignElementHandle)
	 */
	@Override
	public DesignElementHandle createView(DesignElementHandle host) throws BirtException {
		return ChartReportItemUIFactory.instance().createChartViewHandle(host);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.designer.ui.extensions.IReportItemViewProvider#
	 * getViewName()
	 */
	@Override
	public String getViewName() {
		return Messages.getString("ChartReportItemViewProvider.ChartViewName"); //$NON-NLS-1$
	}

}
