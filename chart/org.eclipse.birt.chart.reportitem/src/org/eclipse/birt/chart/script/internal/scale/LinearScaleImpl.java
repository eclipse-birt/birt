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

package org.eclipse.birt.chart.script.internal.scale;

import org.eclipse.birt.chart.model.component.Axis;
import org.eclipse.birt.chart.model.data.DataElement;
import org.eclipse.birt.chart.model.data.NumberDataElement;
import org.eclipse.birt.chart.model.data.impl.NumberDataElementImpl;
import org.eclipse.birt.chart.script.api.scale.ILinearScale;

/**
 * 
 */

public class LinearScaleImpl extends ScaleImpl implements ILinearScale {

	protected LinearScaleImpl(Axis axis) {
		super(axis);
	}

	public double getMax() {
		DataElement data = scale.getMax();
		if (data instanceof NumberDataElement) {
			return ((NumberDataElement) data).getValue();
		}
		return Double.NaN;
	}

	public double getMin() {
		DataElement data = scale.getMin();
		if (data instanceof NumberDataElement) {
			return ((NumberDataElement) data).getValue();
		}
		return Double.NaN;
	}

	public int getNumberOfSteps() {
		return scale.getStepNumber();
	}

	public int getStepSize() {
		return (int) scale.getStep();
	}

	public void setMax(double max) {
		scale.setMax(Double.isNaN(max) ? null : NumberDataElementImpl.create(max));
	}

	public void setMin(double min) {
		scale.setMin(Double.isNaN(min) ? null : NumberDataElementImpl.create(min));
	}

	public void setNumberOfSteps(int steps) {
		scale.setStepNumber(steps);
	}

	public void setStepSize(int size) {
		scale.setStep(size);
	}

}
