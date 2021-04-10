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
