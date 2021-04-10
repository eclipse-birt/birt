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
import org.eclipse.birt.chart.model.attribute.impl.ColorDefinitionImpl;
import org.eclipse.birt.report.model.api.extension.IColor;

/**
 * 
 */

public class ColorImpl implements IColor {

	private ColorDefinition cd = null;

	public ColorImpl(ColorDefinition cd) {
		if (cd == null) {
			// Create a dummy color. Transparency is 255 by default.
			cd = ColorDefinitionImpl.create(-1, -1, -1);
		}
		this.cd = cd;
	}

	public int getBlue() {
		return cd.getBlue();
	}

	public int getGreen() {
		return cd.getGreen();
	}

	public int getRed() {
		return cd.getRed();
	}

	public int getTransparency() {
		return cd.getTransparency();
	}

	public void setBlue(int blue) {
		cd.setBlue(blue);
	}

	public void setGreen(int green) {
		cd.setGreen(green);
	}

	public void setRed(int red) {
		cd.setRed(red);
	}

	public void setTransparency(int value) {
		cd.setTransparency(value);
	}

}
