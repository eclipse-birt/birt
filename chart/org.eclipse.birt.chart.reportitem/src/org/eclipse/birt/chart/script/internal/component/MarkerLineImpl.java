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
import org.eclipse.birt.chart.model.component.MarkerLine;
import org.eclipse.birt.chart.script.api.attribute.ILabel;
import org.eclipse.birt.chart.script.api.component.IMarkerLine;
import org.eclipse.birt.chart.script.api.data.IDataElement;
import org.eclipse.birt.chart.script.internal.ChartComponentUtil;

/**
 *
 */

public class MarkerLineImpl implements IMarkerLine {

	private MarkerLine line;

	public MarkerLineImpl(MarkerLine line) {
		this.line = line;
	}

	@Override
	public IDataElement getValue() {
		return ChartComponentUtil.convertDataElement(line.getValue());
	}

	@Override
	public void setValue(IDataElement value) {
		line.setValue(ChartComponentUtil.convertIDataElement(value));
	}

	@Override
	public ILabel getTitle() {
		Label title = line.getLabel();
		if (title == null) {
			title = ChartComponentUtil.createEMFLabel();
			line.setLabel(title);
		}
		return ChartComponentUtil.convertLabel(title);
	}

	@Override
	public boolean isVisible() {
		return line.getLineAttributes().isVisible();
	}

	public void setTitle(ILabel title) {
		line.setLabel(ChartComponentUtil.convertILabel(title));
	}

	@Override
	public void setVisible(boolean visible) {
		line.getLineAttributes().setVisible(visible);
	}

}
