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

import java.math.BigInteger;

import org.eclipse.birt.chart.model.util.ChartElementUtil;
import org.eclipse.birt.chart.ui.util.ChartUIUtil;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Spinner;

/**
 * ChartSpinner
 */

public class ChartSpinner extends Composite implements Listener, SelectionListener {
	protected EObject eParent = null;
	protected String sProperty = null;
	protected Spinner spinner = null;
	protected double dRatio = 1d;
	protected Label lblLabel;
	protected Label lblEndLabel;

	public ChartSpinner(Composite parent, int styles, EObject obj, String property, boolean enabled) {
		this(parent, styles, obj, property, enabled, null, null);
	}

	public ChartSpinner(Composite parent, int styles, EObject eParent, String property, boolean enabled, String label,
			String endLabel) {
		super(parent, SWT.NONE);
		this.eParent = eParent;
		this.sProperty = property;
		placeComponents(styles, enabled, label, endLabel);
		initListeners();

		if (lblLabel != null) {
			ChartUIUtil.addScreenReaderAccessbility(spinner, lblLabel.getText());
		} else {
			ChartUIUtil.addScreenReaderAccessibility(this, spinner);
		}
	}

	public void addScreenReaderAccessibility(String description) {
		ChartUIUtil.addScreenReaderAccessbility(spinner, description);
	}

	protected void placeComponents(int styles, boolean enabled, String label, String endLabel) {
		int colNum = 1;
		if (label != null)
			colNum++;
		if (endLabel != null)
			colNum++;
		GridLayout gl = new GridLayout();
		gl.numColumns = colNum;
		gl.makeColumnsEqualWidth = false;
		gl.marginBottom = 0;
		gl.marginHeight = 0;
		gl.marginLeft = 0;
		gl.marginRight = 0;
		gl.marginTop = 0;
		gl.marginWidth = 0;
		this.setLayout(gl);
		if (label != null) {
			lblLabel = new Label(this, SWT.NONE);
			lblLabel.setText(label);
		}
		spinner = new Spinner(this, styles);
		GridData gd = new GridData(GridData.FILL_BOTH);
		spinner.setLayoutData(gd);
		setEnabled(enabled);
		if (endLabel != null) {
			lblEndLabel = new Label(this, SWT.NONE);
			lblEndLabel.setText(endLabel);
		}
	}

	public Spinner getWidget() {
		return spinner;
	}

	protected void initListeners() {
		spinner.addSelectionListener(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.widgets.Widget#addListener(int,
	 * org.eclipse.swt.widgets.Listener)
	 */
	@Override
	public void addListener(int eventType, Listener listener) {
		super.addListener(eventType, listener);

		// It seems Linux System event is specific, here checks null for button
		// to avoid NPE against Linux system.
		if (spinner != null) {
			spinner.addListener(eventType, this);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.widgets.Control#setEnabled(boolean)
	 */
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		spinner.setEnabled(enabled);
		if (lblLabel != null) {
			lblLabel.setEnabled(enabled);
		}
		if (lblEndLabel != null) {
			lblEndLabel.setEnabled(enabled);
		}
	}

	public void widgetDefaultSelected(SelectionEvent arg0) {
		// Do nothing.
	}

	public void widgetSelected(SelectionEvent event) {
		if (event.widget == spinner) {
			ChartElementUtil.setEObjectAttribute(eParent, sProperty,
					convertNumber(eParent, sProperty, spinner.getSelection() / dRatio), false);
		}
	}

	public void handleEvent(Event event) {
		if (event.widget == spinner) {
			event.widget = this;
			Listener[] lis = this.getListeners(event.type);
			for (int i = (lis.length - 1); i >= 0; i--) {
				lis[i].handleEvent(event);
			}
		}
	}

	public void setRatio(double ratio) {
		this.dRatio = ratio;
	}

	public static Number convertNumber(EObject eObj, String sProperty, double value) {
		EStructuralFeature esf = eObj.eClass().getEStructuralFeature(sProperty);
		String typeName = esf.getEType().getInstanceTypeName();
		if ("double".equalsIgnoreCase(typeName)) //$NON-NLS-1$
		{
			return value;
		} else if ("float".equalsIgnoreCase(typeName))//$NON-NLS-1$
		{
			return new Float(value);
		} else if ("java.math.BigInteger".equalsIgnoreCase(typeName)) //$NON-NLS-1$
		{
			return BigInteger.valueOf((long) value);
		}
		return (int) value;
	}
}
