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

	@Override
	public int getBlue() {
		return cd.getBlue();
	}

	@Override
	public int getGreen() {
		return cd.getGreen();
	}

	@Override
	public int getRed() {
		return cd.getRed();
	}

	@Override
	public int getTransparency() {
		return cd.getTransparency();
	}

	@Override
	public void setBlue(int blue) {
		cd.setBlue(blue);
	}

	@Override
	public void setGreen(int green) {
		cd.setGreen(green);
	}

	@Override
	public void setRed(int red) {
		cd.setRed(red);
	}

	@Override
	public void setTransparency(int value) {
		cd.setTransparency(value);
	}

}
