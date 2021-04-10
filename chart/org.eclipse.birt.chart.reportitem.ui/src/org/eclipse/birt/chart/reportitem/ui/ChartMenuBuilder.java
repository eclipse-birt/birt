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

package org.eclipse.birt.chart.reportitem.ui;

import java.util.List;

import org.eclipse.birt.chart.reportitem.api.ChartCubeUtil;
import org.eclipse.birt.chart.reportitem.api.ChartItemUtil;
import org.eclipse.birt.chart.reportitem.ui.actions.FlipAxisAction;
import org.eclipse.birt.chart.reportitem.ui.actions.OpenChartTaskAction;
import org.eclipse.birt.chart.reportitem.ui.actions.ShowAxisAction;
import org.eclipse.birt.chart.reportitem.ui.i18n.Messages;
import org.eclipse.birt.chart.ui.util.ChartUIConstants;
import org.eclipse.birt.chart.ui.util.UIHelper;
import org.eclipse.birt.report.designer.ui.extensions.IMenuBuilder;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.Separator;

/**
 * Chart menu extension for designer.
 */

public class ChartMenuBuilder implements IMenuBuilder {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.designer.ui.extensions.IMenuBuilder#buildMenu(org.
	 * eclipse.jface.action.IMenuManager, java.util.List)
	 */
	public void buildMenu(IMenuManager menu, List selectedList) {
		if (selectedList != null && selectedList.size() == 1 && ChartItemUtil.isChartHandle(selectedList.get(0))) {
			ExtendedItemHandle handle = (ExtendedItemHandle) selectedList.get(0);

			Separator separator = new Separator("group.chart"); //$NON-NLS-1$
			if (menu.getItems().length > 0) {
				menu.insertBefore(menu.getItems()[0].getId(), separator);
			} else {
				menu.add(separator);
			}
			menu.appendToGroup(separator.getId(),
					new OpenChartTaskAction(handle, "org.eclipse.birt.chart.ui.swt.wizard.TaskSelectType", //$NON-NLS-1$
							Messages.getString("OpenChartTaskAction.Text.SelectChartType"), //$NON-NLS-1$
							UIHelper.getImage(ChartUIConstants.IMAGE_TASK_TYPE), true));
			menu.appendToGroup(separator.getId(),
					new OpenChartTaskAction(handle, "org.eclipse.birt.chart.ui.swt.wizard.TaskSelectData", //$NON-NLS-1$
							Messages.getString("OpenChartTaskAction.Text.SelectData"), //$NON-NLS-1$
							UIHelper.getImage(ChartUIConstants.IMAGE_TASK_DATA), false));
			menu.appendToGroup(separator.getId(),
					new OpenChartTaskAction(handle, "org.eclipse.birt.chart.ui.swt.wizard.TaskFormatChart", //$NON-NLS-1$
							Messages.getString("OpenChartTaskAction.Text.FormatChart"), //$NON-NLS-1$
							UIHelper.getImage(ChartUIConstants.IMAGE_TASK_FORMAT), false));

			if (ChartCubeUtil.isPlotChart(handle) || ChartCubeUtil.isAxisChart(handle)) {
				menu.appendToGroup(separator.getId(), new FlipAxisAction(handle));
				menu.appendToGroup(separator.getId(), new ShowAxisAction(handle));
			}
		}

	}

}
