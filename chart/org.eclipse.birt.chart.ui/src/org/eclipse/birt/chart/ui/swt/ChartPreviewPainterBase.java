/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
