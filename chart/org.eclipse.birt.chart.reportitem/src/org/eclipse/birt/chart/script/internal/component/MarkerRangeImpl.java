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
import org.eclipse.birt.chart.model.component.MarkerRange;
import org.eclipse.birt.chart.script.api.attribute.ILabel;
import org.eclipse.birt.chart.script.api.component.IMarkerRange;
import org.eclipse.birt.chart.script.api.data.IDataElement;
import org.eclipse.birt.chart.script.internal.ChartComponentUtil;

/**
 *
 */

public class MarkerRangeImpl implements IMarkerRange {

	private MarkerRange range;

	public MarkerRangeImpl(MarkerRange range) {
		this.range = range;
	}

	@Override
	public IDataElement getEndValue() {
		return ChartComponentUtil.convertDataElement(range.getEndValue());
	}

	@Override
	public IDataElement getStartValue() {
		return ChartComponentUtil.convertDataElement(range.getStartValue());
	}

	@Override
	public void setEndValue(IDataElement value) {
		range.setEndValue(ChartComponentUtil.convertIDataElement(value));
	}

	@Override
	public void setStartValue(IDataElement value) {
		range.setStartValue(ChartComponentUtil.convertIDataElement(value));
	}

	@Override
	public ILabel getTitle() {
		Label title = range.getLabel();
		if (title == null) {
			title = ChartComponentUtil.createEMFLabel();
			range.setLabel(title);
		}
		return ChartComponentUtil.convertLabel(title);
	}

	@Override
	public boolean isVisible() {
		return range.getOutline().isVisible();
	}

	public void setTitle(ILabel title) {
		range.setLabel(ChartComponentUtil.convertILabel(title));
	}

	@Override
	public void setVisible(boolean visible) {
		range.getOutline().setVisible(visible);
	}

}
