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

package org.eclipse.birt.chart.script.internal.data;

import org.eclipse.birt.chart.model.data.NumberDataElement;
import org.eclipse.birt.chart.script.api.data.INumberDataElement;

/**
 * 
 */

public class NumberElementImpl implements INumberDataElement {

	private double data;

	public NumberElementImpl(NumberDataElement data) {
		this.data = data.getValue();
	}

	public NumberElementImpl(double data) {
		this.data = data;
	}

	public double getValue() {
		return data;
	}

	public void setValue(double value) {
		data = value;
	}

}
