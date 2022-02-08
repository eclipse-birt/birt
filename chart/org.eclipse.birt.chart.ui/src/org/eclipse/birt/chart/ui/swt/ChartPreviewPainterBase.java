/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
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

package org.eclipse.birt.chart.ui.swt;

import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.IChartObject;
import org.eclipse.birt.chart.ui.swt.wizard.ChartWizardContext;

/**
 * The class is responsible for computing and painting chart in chart builder.
 */

public abstract class ChartPreviewPainterBase extends PreviewPainterBase<ChartWizardContext> {

	protected ChartPreviewPainterBase(ChartWizardContext wizardContext) {
		super(wizardContext);
	}

	protected void doRenderModel(IChartObject object) {
		Chart chart = (Chart) object;
		// If not use live preview, use sample data to create runtime series
		if (!(isLivePreviewActive() && isLivePreviewEnabled())) {
			chart.createSampleRuntimeSeries();
		}

		super.doRenderModel(chart);
	}

}
