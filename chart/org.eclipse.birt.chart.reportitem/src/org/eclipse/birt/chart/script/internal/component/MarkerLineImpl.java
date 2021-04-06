/*******************************************************************************
 * Copyright (c) 2006 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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

	public IDataElement getValue() {
		return ChartComponentUtil.convertDataElement(line.getValue());
	}

	public void setValue(IDataElement value) {
		line.setValue(ChartComponentUtil.convertIDataElement(value));
	}

	public ILabel getTitle() {
		Label title = line.getLabel();
		if (title == null) {
			title = ChartComponentUtil.createEMFLabel();
			line.setLabel(title);
		}
		return ChartComponentUtil.convertLabel(title);
	}

	public boolean isVisible() {
		return line.getLineAttributes().isVisible();
	}

	public void setTitle(ILabel title) {
		line.setLabel(ChartComponentUtil.convertILabel(title));
	}

	public void setVisible(boolean visible) {
		line.getLineAttributes().setVisible(visible);
	}

}
