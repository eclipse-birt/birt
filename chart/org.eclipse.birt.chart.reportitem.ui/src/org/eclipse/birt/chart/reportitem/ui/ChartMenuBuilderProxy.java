/*******************************************************************************
 * Copyright (c) 2011 Actuate Corporation.
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

package org.eclipse.birt.chart.reportitem.ui;

import java.util.List;

import org.eclipse.birt.report.designer.ui.extensions.IMenuBuilder;
import org.eclipse.jface.action.IMenuManager;

/**
 * ChartMenuBuilderProxy
 */

public class ChartMenuBuilderProxy implements IMenuBuilder {

	private IMenuBuilder instance = ChartReportItemUIFactory.instance().createMenuBuilder();

	@Override
	public void buildMenu(IMenuManager menu, List selectedList) {
		instance.buildMenu(menu, selectedList);
	}

}
