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

import org.eclipse.birt.chart.model.attribute.Size;
import org.eclipse.birt.chart.model.component.Label;

/**
 * LegendItemLayoutHints
 */
public final class LegendLayoutHints {

	private final Size cachedSize;
	private final Size titleSize;
	private final Label laTitle;
	private final boolean isMinSliceApplied;
	private final String minSliceText;
	private final LegendItemHints[] liha;

	public LegendLayoutHints(Size legendSize, Size titleSize, Label laTitle, boolean isMinSliceApplied,
			String minSliceText, LegendItemHints[] liha) {
		this.cachedSize = legendSize;
		this.titleSize = titleSize;
		this.laTitle = laTitle;
		this.isMinSliceApplied = isMinSliceApplied;
		this.minSliceText = minSliceText;
		this.liha = liha;
	}

	public Size getLegendSize() {
		return cachedSize;
	}

	public Size getTitleSize() {
		return titleSize;
	}

	public boolean isMinSliceApplied() {
		return isMinSliceApplied;
	}

	public String getMinSliceText() {
		return minSliceText;
	}

	public LegendItemHints[] getLegendItemHints() {
		return liha;
	}

	/**
	 * @return Returns the laTitle.
	 */
	public Label getLaTitle() {
		return laTitle;
	}
}
