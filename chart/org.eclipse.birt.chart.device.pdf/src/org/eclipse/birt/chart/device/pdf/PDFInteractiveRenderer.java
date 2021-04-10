/***********************************************************************
 * Copyright (c) 2005, 2006 IBM Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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

	public void addInteractivity() {
		// no interactions since we are rendering a static image
	}

	protected void groupPrimitive(PrimitiveRenderEvent pre, boolean drawText) {
		// no interactions since we are rendering a static image
	}

	protected void ungroupPrimitive(PrimitiveRenderEvent pre, boolean drawText) {
		// no interactions since we are rendering a static image
	}

}
