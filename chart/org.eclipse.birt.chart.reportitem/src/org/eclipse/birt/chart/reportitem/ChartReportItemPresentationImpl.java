/***********************************************************************
 * Copyright (c) 2004, 2007 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.chart.reportitem;

import org.eclipse.birt.chart.model.attribute.Bounds;
import org.eclipse.birt.chart.reportitem.api.ChartReportItemConstants;

/**
 * Standard presentation implementation for Chart
 */
public final class ChartReportItemPresentationImpl extends ChartReportItemPresentationBase {

	@Override
	protected Bounds computeBounds() {
		final Bounds originalBounds = cm.getBlock().getBounds();

		// we must copy the bounds to avoid that setting it on one object
		// unsets it on its precedent container
		Bounds bounds = originalBounds.copyInstance();
		if (!bounds.isSetHeight()) {
			bounds.setHeight(ChartReportItemConstants.DEFAULT_CHART_BLOCK_HEIGHT);
		}
		if (!bounds.isSetWidth()) {
			bounds.setWidth(ChartReportItemConstants.DEFAULT_CHART_BLOCK_WIDTH);
		}
		return bounds;
	}

}
