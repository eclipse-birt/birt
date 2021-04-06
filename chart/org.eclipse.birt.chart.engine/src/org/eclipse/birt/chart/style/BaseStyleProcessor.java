/*******************************************************************************
 * Copyright (c) 2010 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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

	public IStyle getStyle(Chart model, StyledComponent name) {
		return null;
	}

	public void processStyle(Chart model) {

	}

	public void setDefaultBackgroundColor(ColorDefinition cd) {
		this.backgroundColor = cd;
	}

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
	public boolean updateChart(Chart model, Object obj) {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.style.IStyleProcessor#needInheritingStyles()
	 */
	public boolean needInheritingStyles() {
		return true;
	}

}
