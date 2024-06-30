/*******************************************************************************
 * Copyright (c) 2011 Actuate Corporation.
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

package org.eclipse.birt.chart.ui.swt;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Listener;

/**
 * AbstractChartIntSpinner
 */

public abstract class AbstractChartIntSpinner extends Composite {

	public AbstractChartIntSpinner(Composite parent, int style) {
		super(parent, style);
	}

	abstract public void setValue(int value);

	abstract public int getValue();

	abstract public void setIncrement(int increment);

	abstract public void setMaximum(int max);

	abstract public void setMinimum(int min);

	abstract public void addListener(Listener listener);

	abstract public boolean isSpinnerEnabled();
}
