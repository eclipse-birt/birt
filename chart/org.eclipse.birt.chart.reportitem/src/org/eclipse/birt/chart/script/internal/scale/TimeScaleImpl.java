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

import java.util.Date;

import org.eclipse.birt.chart.model.attribute.ScaleUnitType;
import org.eclipse.birt.chart.model.component.Axis;
import org.eclipse.birt.chart.model.data.DataElement;
import org.eclipse.birt.chart.model.data.DateTimeDataElement;
import org.eclipse.birt.chart.model.data.impl.DateTimeDataElementImpl;
import org.eclipse.birt.chart.script.api.scale.ITimeScale;

/**
 * 
 */

public class TimeScaleImpl extends ScaleImpl implements ITimeScale {

	protected TimeScaleImpl(Axis axis) {
		super(axis);
	}

	public Date getMax() {
		DataElement data = scale.getMax();
		if (data instanceof DateTimeDataElement) {
			return ((DateTimeDataElement) data).getValueAsCalendar().getTime();
		}
		return null;
	}

	public Date getMin() {
		DataElement data = scale.getMin();
		if (data instanceof DateTimeDataElement) {
			return ((DateTimeDataElement) data).getValueAsCalendar().getTime();
		}
		return null;
	}

	public int getStepSize() {
		return (int) scale.getStep();
	}

	public String getStepTimeUnit() {
		return scale.getUnit().getName();
	}

	public void setMax(Date max) {
		scale.setMax(max != null ? DateTimeDataElementImpl.create(max.getTime()) : null);
	}

	public void setMin(Date min) {
		scale.setMin(min != null ? DateTimeDataElementImpl.create(min.getTime()) : null);
	}

	public void setStepSize(int size) {
		scale.setStep(size);
	}

	public void setStepTimeUnit(String unit) {
		scale.setUnit(ScaleUnitType.getByName(unit));
	}

}
