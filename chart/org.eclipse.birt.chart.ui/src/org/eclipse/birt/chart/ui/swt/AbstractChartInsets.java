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
