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

import org.eclipse.birt.chart.model.attribute.ColorDefinition;
import org.eclipse.birt.chart.model.attribute.FontDefinition;
import org.eclipse.birt.chart.model.attribute.Text;
import org.eclipse.birt.chart.script.api.attribute.IText;
import org.eclipse.birt.chart.script.internal.ChartComponentUtil;
import org.eclipse.birt.report.model.api.extension.IColor;
import org.eclipse.birt.report.model.api.extension.IFont;

/**
 *
 */

public class TextImpl implements IText {

	private Text text;

	public TextImpl(Text text) {
		this.text = text;
	}

	@Override
	public String getValue() {
		return text.getValue();
	}

	@Override
	public void setValue(String value) {
		text.setValue(value);
	}

	@Override
	public IColor getColor() {
		ColorDefinition cd = text.getColor();
		if (cd == null) {
			cd = ChartComponentUtil.createEMFColor();
			text.setColor(cd);
		}
		return ChartComponentUtil.convertColor(cd);
	}

	@Override
	public IFont getFont() {
		FontDefinition fd = text.getFont();
		if (fd == null) {
			fd = ChartComponentUtil.createEMFFont();
			text.setFont(fd);
		}
		return ChartComponentUtil.convertFont(fd);
	}

	@Override
	public void setColor(IColor color) {
		text.setColor(ChartComponentUtil.convertIColor(color));
	}

	@Override
	public void setFont(IFont font) {
		text.setFont(ChartComponentUtil.convertIFont(font));
	}

}
