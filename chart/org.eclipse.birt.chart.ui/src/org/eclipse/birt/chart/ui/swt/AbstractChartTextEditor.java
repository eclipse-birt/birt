/*******************************************************************************
 * Copyright (c) 2007 Actuate Corporation.
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

import org.eclipse.emf.ecore.EObject;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;

/**
 * AbstractChartTextEditor
 */

public abstract class AbstractChartTextEditor extends Composite {

	public AbstractChartTextEditor(Composite parent, int style) {
		super(parent, style);
		// TODO Auto-generated constructor stub
	}

	abstract public void setText(String sText);

	abstract public String getText();

	abstract public void addListener(Listener listener);

	abstract public void setDefaultValue(String value);

	abstract public Text getTextControl();

	abstract public void setEObjectParent(EObject eParent);
}
