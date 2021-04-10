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

import org.eclipse.birt.chart.ui.util.ChartUIUtil;
import org.eclipse.birt.core.ui.swt.custom.CustomChooserComposite;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;

/**
 * AbstractLineWidthChooserComposite
 */

public abstract class AbstractLineWidthChooserComposite extends CustomChooserComposite {

	public AbstractLineWidthChooserComposite(Composite parent, int style, Object choiceValue) {
		super(parent, style, choiceValue);
	}

	/**
	 * Returns the currently selected line width
	 * 
	 * @return currently selected line width
	 */
	abstract public int getLineWidth();

	abstract public void setLineWidth(int iWidth);

	abstract public void setLineWidth(int iWidth, EObject eParent);

	protected void initAccessible() {
		super.initAccessible();
		ChartUIUtil.addScreenReaderAccessibility(this, (Canvas) cnvSelection);
	}
}
