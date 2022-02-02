/***********************************************************************
 * Copyright (c) 2004 Actuate Corporation.
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

package org.eclipse.birt.chart.computation;

import org.eclipse.birt.chart.model.attribute.Bounds;
import org.eclipse.birt.chart.render.BaseRenderer;

/**
 * LegendItemRenderingHints
 */
public final class LegendItemRenderingHints {

	private final Bounds boLegendGraphic;

	private final BaseRenderer br;

	/**
	 * 
	 * @param _boLegendGraphic
	 * @param _seModel
	 */
	public LegendItemRenderingHints(BaseRenderer _br, Bounds _boLegendGraphic) {
		br = _br;
		boLegendGraphic = _boLegendGraphic;
	}

	/**
	 * 
	 * @return
	 */
	public final BaseRenderer getRenderer() {
		return br;
	}

	/**
	 * 
	 * @return
	 */
	public final Bounds getLegendGraphicBounds() {
		return boLegendGraphic;
	}
}
