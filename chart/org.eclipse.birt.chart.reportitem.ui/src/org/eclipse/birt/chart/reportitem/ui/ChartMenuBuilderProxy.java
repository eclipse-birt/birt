/*******************************************************************************
 * Copyright (c) 2011 Actuate Corporation.
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

import org.eclipse.birt.report.designer.ui.extensions.IMenuBuilder;
import org.eclipse.jface.action.IMenuManager;

/**
 * ChartMenuBuilderProxy
 */

public class ChartMenuBuilderProxy implements IMenuBuilder {

	private IMenuBuilder instance = ChartReportItemUIFactory.instance().createMenuBuilder();

	public void buildMenu(IMenuManager menu, List selectedList) {
		instance.buildMenu(menu, selectedList);
	}

}
