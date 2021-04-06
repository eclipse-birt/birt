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

	public String getValue() {
		return text.getValue();
	}

	public void setValue(String value) {
		text.setValue(value);
	}

	public IColor getColor() {
		ColorDefinition cd = text.getColor();
		if (cd == null) {
			cd = ChartComponentUtil.createEMFColor();
			text.setColor(cd);
		}
		return ChartComponentUtil.convertColor(cd);
	}

	public IFont getFont() {
		FontDefinition fd = text.getFont();
		if (fd == null) {
			fd = ChartComponentUtil.createEMFFont();
			text.setFont(fd);
		}
		return ChartComponentUtil.convertFont(fd);
	}

	public void setColor(IColor color) {
		text.setColor(ChartComponentUtil.convertIColor(color));
	}

	public void setFont(IFont font) {
		text.setFont(ChartComponentUtil.convertIFont(font));
	}

}
