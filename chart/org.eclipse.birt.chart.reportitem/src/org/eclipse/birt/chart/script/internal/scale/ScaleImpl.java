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

package org.eclipse.birt.chart.script.internal.scale;

import org.eclipse.birt.chart.model.attribute.AxisType;
import org.eclipse.birt.chart.model.component.Axis;
import org.eclipse.birt.chart.model.component.Scale;
import org.eclipse.birt.chart.script.api.scale.IScale;

/**
 * 
 */

public abstract class ScaleImpl implements IScale {

	protected Axis axis;
	protected Scale scale;

	protected ScaleImpl(Axis axis) {
		this.axis = axis;
		this.scale = axis.getScale();
	}

	public boolean isAuto() {
		return !scale.isSetStep() && !scale.isSetStepNumber();
	}

	public boolean isCategory() {
		return axis.isCategoryAxis();
	}

	public void setAuto() {
		scale.unsetStep();
		scale.unsetStepNumber();
	}

	public void setCategory(boolean category) {
		axis.setCategoryAxis(category);
	}

	public static IScale createScale(Axis axis) {
		if (axis.isCategoryAxis()) {
			return new CategoryScaleImpl(axis);
		}
		switch (axis.getType().getValue()) {
		case AxisType.LINEAR:
			return new LinearScaleImpl(axis);
		case AxisType.DATE_TIME:
			return new TimeScaleImpl(axis);
		case AxisType.LOGARITHMIC:
			return new LogarithmicScaleImpl(axis);
		default:
			return new CategoryScaleImpl(axis);
		}
	}

}
