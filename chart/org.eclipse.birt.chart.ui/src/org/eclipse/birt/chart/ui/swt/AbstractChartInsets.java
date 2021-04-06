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

import org.eclipse.birt.chart.model.attribute.Insets;
import org.eclipse.swt.widgets.Composite;

/**
 * AbstractChartInsets
 */

public abstract class AbstractChartInsets extends Composite {

	public AbstractChartInsets(Composite parent, int style) {
		super(parent, style);
	}

	abstract public void setInsets(Insets insets, String sUnits);

	abstract public void setDefaultInsets(Insets insets);
}
