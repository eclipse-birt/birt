/*******************************************************************************
 * Copyright (c) 2006 Actuate Corporation.
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

package org.eclipse.birt.chart.script.internal.component;

import org.eclipse.birt.chart.model.component.Label;
import org.eclipse.birt.chart.model.layout.Legend;
import org.eclipse.birt.chart.script.api.attribute.ILabel;
import org.eclipse.birt.chart.script.api.component.ILegend;
import org.eclipse.birt.chart.script.internal.ChartComponentUtil;

/**
 * 
 */

public class LegendImpl implements ILegend {

	private Legend legend;

	public LegendImpl(Legend legend) {
		this.legend = legend;
	}

	public ILabel getTitle() {
		Label title = legend.getTitle();
		if (title == null) {
			title = ChartComponentUtil.createEMFLabel();
			legend.setTitle(title);
		}
		return ChartComponentUtil.convertLabel(title);
	}

	public boolean isVisible() {
		return legend.isVisible();
	}

	public void setTitle(ILabel title) {
		legend.setTitle(ChartComponentUtil.convertILabel(title));
	}

	public void setVisible(boolean visible) {
		legend.setVisible(visible);
	}

	public boolean isShowValue() {
		return legend.isShowValue();
	}

	public void setShowValue(boolean show) {
		legend.setShowValue(show);
	}

}
