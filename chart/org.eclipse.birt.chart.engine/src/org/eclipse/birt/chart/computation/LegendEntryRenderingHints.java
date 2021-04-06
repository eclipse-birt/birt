/***********************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.chart.computation;

import org.eclipse.birt.chart.model.attribute.Fill;
import org.eclipse.birt.chart.model.component.Label;

public class LegendEntryRenderingHints {
	private Label la;
	private int dataIndex;
	private Fill fill;
	private Label valueLa;

	public LegendEntryRenderingHints(Label la, Label valueLa, int dataIndex, Fill fill) {
		this.la = la;
		this.dataIndex = dataIndex;
		this.fill = fill;
		this.valueLa = valueLa;
	}

	public Label getLabel() {
		return la;
	}

	public Label getValueLabel() {
		return valueLa;
	}

	public int getDataIndex() {
		return dataIndex;
	}

	public Fill getFill() {
		return fill;
	}
}
