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
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;

/**
 * AbstractChartNumberEditor
 */

public abstract class AbstractChartNumberEditor extends Composite {

	public AbstractChartNumberEditor(Composite parent, int style) {
		super(parent, style);
	}

	abstract public boolean isSetValue();

	abstract public void unsetValue();

	abstract public void setValue(double value);

	abstract public double getValue();

	abstract public Text getTextControl();

	abstract public Label getUnitLabel();

	abstract public void addFractionListener(Listener listener);

	abstract public void addModifyListener(ModifyListener listener);

	abstract public void setEObjectParent(EObject eParent);
}
