/*******************************************************************************
 * Copyright (c) 2011 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
