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

import java.util.List;

import org.eclipse.birt.chart.model.attribute.AxisType;
import org.eclipse.birt.chart.model.component.Axis;
import org.eclipse.birt.chart.model.component.Label;
import org.eclipse.birt.chart.model.component.MarkerLine;
import org.eclipse.birt.chart.model.component.MarkerRange;
import org.eclipse.birt.chart.script.api.attribute.ILabel;
import org.eclipse.birt.chart.script.api.component.IAxis;
import org.eclipse.birt.chart.script.api.component.IMarkerLine;
import org.eclipse.birt.chart.script.api.component.IMarkerRange;
import org.eclipse.birt.chart.script.api.scale.IScale;
import org.eclipse.birt.chart.script.internal.ChartComponentUtil;
import org.eclipse.birt.chart.script.internal.scale.ScaleImpl;

/**
 * 
 */

public class AxisImpl implements IAxis {

	private Axis axis;

	public AxisImpl(Axis axis) {
		this.axis = axis;
	}

	public IMarkerLine[] getMarkerLines() {
		List lines = axis.getMarkerLines();
		if (lines == null || lines.isEmpty()) {
			return new IMarkerLine[0];
		}
		IMarkerLine[] array = new IMarkerLine[lines.size()];
		for (int i = 0; i < array.length; i++) {
			array[i] = new MarkerLineImpl((MarkerLine) lines.get(i));
		}
		return array;
	}

	public IMarkerRange[] getMarkerRanges() {
		List ranges = axis.getMarkerRanges();
		if (ranges == null || ranges.isEmpty()) {
			return new IMarkerRange[0];
		}
		IMarkerRange[] array = new IMarkerRange[ranges.size()];
		for (int i = 0; i < array.length; i++) {
			array[i] = new MarkerRangeImpl((MarkerRange) ranges.get(i));
		}
		return array;
	}

	public IScale getScale() {
		return ScaleImpl.createScale(axis);
	}

	public String getType() {
		return axis.getType().getName();
	}

	public void setType(String type) {
		axis.setType(AxisType.getByName(type));
	}

	public ILabel getTitle() {
		Label title = axis.getTitle();
		if (title == null) {
			title = ChartComponentUtil.createEMFLabel();
			axis.setTitle(title);
		}
		return ChartComponentUtil.convertLabel(title);
	}

	public boolean isVisible() {
		return axis.getLineAttributes().isVisible();
	}

	public void setTitle(ILabel title) {
		axis.setTitle(ChartComponentUtil.convertILabel(title));
	}

	public void setVisible(boolean visible) {
		axis.getLineAttributes().setVisible(visible);
	}

}
