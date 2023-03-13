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

package org.eclipse.birt.chart.script.internal.attribute;

import org.eclipse.birt.chart.model.attribute.Text;
import org.eclipse.birt.chart.model.component.Label;
import org.eclipse.birt.chart.script.api.attribute.ILabel;
import org.eclipse.birt.chart.script.api.attribute.IText;
import org.eclipse.birt.chart.script.internal.ChartComponentUtil;

/**
 *
 */

public class LabelImpl implements ILabel {

	private Label label;

	public LabelImpl(Label label) {
		this.label = label;
	}

	@Override
	public IText getCaption() {
		Text caption = label.getCaption();
		if (caption == null) {
			caption = ChartComponentUtil.createEMFText();
			label.setCaption(caption);
		}
		return ChartComponentUtil.convertText(caption);
	}

	@Override
	public boolean isVisible() {
		return label.isVisible();
	}

	public void setCaption(IText text) {
		label.setCaption(ChartComponentUtil.convertIText(text));

	}

	@Override
	public void setVisible(boolean visible) {
		label.setVisible(visible);

	}

}
