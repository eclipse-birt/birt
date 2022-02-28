/*******************************************************************************
 * Copyright (c) 2010 Actuate Corporation.
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

package org.eclipse.birt.chart.style;

import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.attribute.ColorDefinition;
import org.eclipse.birt.chart.model.attribute.StyledComponent;

/**
 * Provides a base implementation of style processor. It should be used as super
 * class of all style processors.
 */

public class BaseStyleProcessor implements IStyleProcessor {
	private ColorDefinition backgroundColor = null;

	@Override
	public IStyle getStyle(Chart model, StyledComponent name) {
		return null;
	}

	@Override
	public void processStyle(Chart model) {

	}

	@Override
	public void setDefaultBackgroundColor(ColorDefinition cd) {
		this.backgroundColor = cd;
	}

	@Override
	public ColorDefinition getDefaultBackgroundColor() {
		try {
			return backgroundColor;
		} finally {
			// reset the environment
			backgroundColor = null;
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.chart.style.IStyleProcessor#updateChart(org.eclipse.birt.
	 * chart.model.Chart, java.lang.Object)
	 */
	@Override
	public boolean updateChart(Chart model, Object obj) {
		return false;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.chart.style.IStyleProcessor#needInheritingStyles()
	 */
	@Override
	public boolean needInheritingStyles() {
		return true;
	}

}
