/***********************************************************************
 * Copyright (c) 2005, 2006 IBM Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.chart.device.pdf;

import org.eclipse.birt.chart.device.IDeviceRenderer;
import org.eclipse.birt.chart.device.svg.SVGInteractiveRenderer;
import org.eclipse.birt.chart.event.PrimitiveRenderEvent;

/**
 * This is an internal class used by PDFRendererImpl to add interactivity in the
 * PDF output. Note this class disables any interactivity since PDF is a static
 * device renderer
 */
public class PDFInteractiveRenderer extends SVGInteractiveRenderer {

	public PDFInteractiveRenderer(IDeviceRenderer device) {
		super(device);
	}

	@Override
	public void addInteractivity() {
		// no interactions since we are rendering a static image
	}

	@Override
	protected void groupPrimitive(PrimitiveRenderEvent pre, boolean drawText) {
		// no interactions since we are rendering a static image
	}

	@Override
	protected void ungroupPrimitive(PrimitiveRenderEvent pre, boolean drawText) {
		// no interactions since we are rendering a static image
	}

}
