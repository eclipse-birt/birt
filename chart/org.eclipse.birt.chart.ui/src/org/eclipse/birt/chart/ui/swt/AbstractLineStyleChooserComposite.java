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

import org.eclipse.birt.chart.model.attribute.LineStyle;
import org.eclipse.birt.chart.ui.util.ChartUIUtil;
import org.eclipse.birt.core.ui.swt.custom.CustomChooserComposite;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;

/**
 * AbstractLineStyleChooserComposite
 */

public abstract class AbstractLineStyleChooserComposite extends CustomChooserComposite {

	public AbstractLineStyleChooserComposite(Composite parent, int style, Object choiceValue) {
		super(parent, style, choiceValue);
	}

	abstract public int getLineStyle();

	abstract public void setLineStyle(int iStyle);

	abstract public void setLineStyle(LineStyle style, EObject eParent);

	@Override
	protected void initAccessible() {
		super.initAccessible();
		ChartUIUtil.addScreenReaderAccessibility(this, (Canvas) cnvSelection);
	}
}
