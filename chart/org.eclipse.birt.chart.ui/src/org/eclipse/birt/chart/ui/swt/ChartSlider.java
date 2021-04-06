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

import java.util.Vector;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Slider;

/**
 * ChartSlider
 */

public class ChartSlider extends Composite implements SelectionListener {
	protected Slider slider;

	protected Vector<SelectionListener> vListeners = new Vector<SelectionListener>();

	public ChartSlider(Composite parent, int style) {
		super(parent, SWT.NONE);
		placeComponents(style);
	}

	protected void placeComponents(int style) {
		GridLayout gl = new GridLayout(1, false);
		gl.marginBottom = 0;
		gl.marginHeight = 0;
		gl.marginLeft = 0;
		gl.marginRight = 0;
		gl.marginTop = 0;
		gl.marginWidth = 0;
		this.setLayout(gl);

		slider = new Slider(this, style);
		GridData gd = new GridData(GridData.FILL_BOTH);
		slider.setLayoutData(gd);
	}

	public Slider getWidget() {
		return this.slider;
	}

	public void setValues(int selection, int minimum, int maximum, int thumb, int increment, int pageIncrement,
			double dRatio) {
		slider.setValues(selection, minimum, maximum, thumb, increment, pageIncrement);
	}

	public int getSelection() {
		return slider.getSelection();
	}

	public void addSelectionListener(SelectionListener listener) {
		vListeners.add(listener);
		slider.addSelectionListener(this);
	}

	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		slider.setEnabled(enabled);
	}

	@Override
	public void setToolTipText(String tooltip) {
		slider.setToolTipText(tooltip);
	}

	public void widgetDefaultSelected(SelectionEvent arg0) {
		// TODO Auto-generated method stub

	}

	public void widgetSelected(SelectionEvent event) {
		if (event.widget == slider) {
			Event e = new Event();
			e.detail = event.detail;
			e.data = event.data;
			e.display = event.display;
			e.doit = event.doit;
			e.height = event.height;
			e.item = event.item;
			e.stateMask = event.stateMask;
			e.text = event.text;
			e.time = event.time;
			e.width = event.width;
			e.widget = this;
			e.x = event.x;
			e.y = event.y;
			SelectionEvent se = new SelectionEvent(e);

			for (int i = (vListeners.size() - 1); i >= 0; i--) {
				vListeners.get(i).widgetSelected(se);
			}
		}

	}
}
