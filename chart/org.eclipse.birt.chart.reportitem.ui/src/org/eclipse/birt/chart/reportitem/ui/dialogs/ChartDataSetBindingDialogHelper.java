/*******************************************************************************
* Copyright (c) 2007 Actuate Corporation.
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v2.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-2.0.html
*
* Contributors:
*  Actuate Corporation  - initial API and implementation
*******************************************************************************/
package org.eclipse.birt.chart.reportitem.ui.dialogs;

import org.eclipse.birt.chart.reportitem.ChartReportItemUtil;
import org.eclipse.birt.report.designer.internal.ui.dialogs.BindingDialogHelper;
import org.eclipse.swt.widgets.Composite;

/**
 * 
 */

public class ChartDataSetBindingDialogHelper extends BindingDialogHelper {
	protected void createAggregateSection(Composite composite) {
		super.createAggregateSection(composite);
		if (ChartReportItemUtil.isChartHandle(getBindingHolder())) {
			btnTable.setText(LIST);
		}
	}

}
