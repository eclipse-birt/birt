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

package org.eclipse.birt.chart.ui.swt.interfaces;

import org.eclipse.birt.chart.model.IChartObject;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.widgets.Canvas;

/**
 * 
 */

public interface IChartPreviewPainter extends ControlListener {

	void dispose();

	void renderModel(IChartObject chart);

	void setPreview(Canvas previewCanvas);
}
