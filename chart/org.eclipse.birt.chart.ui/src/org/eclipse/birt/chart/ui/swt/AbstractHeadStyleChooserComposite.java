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

import org.eclipse.birt.chart.ui.util.ChartUIUtil;
import org.eclipse.birt.core.ui.swt.custom.CustomChooserComposite;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;

/**
 * 
 */

public abstract class AbstractHeadStyleChooserComposite extends CustomChooserComposite {

	public AbstractHeadStyleChooserComposite(Composite parent, int style, Object choiceValue) {
		super(parent, style, choiceValue);
	}

	/**
	 * Returns the current selected head style as an integer.
	 * 
	 */
	abstract public int getHeadStyle();

	abstract public void setHeadStyle(int iStyle);

	protected void initAccessible() {
		super.initAccessible();
		ChartUIUtil.addScreenReaderAccessibility(this, (Canvas) cnvSelection);
	}
}
