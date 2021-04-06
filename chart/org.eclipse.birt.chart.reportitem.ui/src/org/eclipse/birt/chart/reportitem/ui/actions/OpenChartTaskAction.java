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

package org.eclipse.birt.chart.reportitem.ui.actions;

import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.reportitem.ChartReportItemImpl;
import org.eclipse.birt.chart.reportitem.api.ChartReportItemConstants;
import org.eclipse.birt.chart.reportitem.ui.ChartReportItemBuilderProxy;
import org.eclipse.birt.core.ui.frameworks.taskwizard.WizardBase;
import org.eclipse.birt.report.designer.ui.extensions.ReportItemBuilderUI;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.extension.ExtendedElementException;
import org.eclipse.birt.report.model.api.extension.IReportItem;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;

/**
 * Action set to open chart task.
 */

public class OpenChartTaskAction extends Action {

	private ExtendedItemHandle eih;

	private ReportItemBuilderUI uiServiceProvider;

	/**
	 * 
	 * @param handle
	 * @param taskId
	 * @param text
	 * @param bBlankEnabled indicates the enabled status if chart is blank. True,
	 *                      always enabled; false, enabled only when not blank
	 */
	public OpenChartTaskAction(ExtendedItemHandle handle, String taskId, String text, Image img,
			boolean bBlankEnabled) {
		super(text);
		this.setImageDescriptor(ImageDescriptor.createFromImage(img));
		this.eih = handle;
		this.uiServiceProvider = new ChartReportItemBuilderProxy(taskId);
		this.setEnabled(bBlankEnabled || !isBlankChart());
	}

	public void run() {
		uiServiceProvider.open(eih);
	}

	protected boolean isBlankChart() {
		IReportItem item = null;
		try {
			item = eih.getReportItem();
			if (item == null) {
				eih.loadExtendedElement();
				item = eih.getReportItem();
			}
		} catch (ExtendedElementException exception) {
			WizardBase.displayException(exception);
		}

		if (item == null) {
			return true;
		}
		ChartReportItemImpl crii = ((ChartReportItemImpl) item);
		return (Chart) crii.getProperty(ChartReportItemConstants.PROPERTY_CHART) == null;
	}
}
