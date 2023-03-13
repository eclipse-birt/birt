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

import java.util.Vector;

import org.eclipse.birt.chart.ui.util.ChartUIUtil;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

/**
 * ChartCombo
 */

public class ChartCombo extends Composite implements SelectionListener {

	protected Combo cmbItems;

	protected Vector<SelectionListener> vListeners = new Vector<>();

	protected String[] data;

	private String defaultItem;

	private int defaultIndex;

	protected EObject eParent = null;

	protected String sProperty = null;

	public ChartCombo(Composite parent, int style, EObject eParent, String sProperty, String defaultItem) {
		super(parent, SWT.NONE);
		this.eParent = eParent;
		this.sProperty = sProperty;
		placeComponents(style);
		this.defaultItem = defaultItem;
		ChartUIUtil.addScreenReaderAccessibility(this, cmbItems);
	}

	public void addScreenReaderAccessibility(String description) {
		ChartUIUtil.addScreenReaderAccessbility(cmbItems, description);
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

		cmbItems = new Combo(this, style);
		GridData gd = new GridData(GridData.FILL_BOTH);
		cmbItems.setLayoutData(gd);
	}

	public void add(String item) {
		cmbItems.add(item);
	}

	public void add(String item, int index) {
		cmbItems.add(item, index);
	}

	public void setItems(String[] items) {
		cmbItems.setItems(items);
	}

	public String[] getItems() {
		return cmbItems.getItems();
	}

	public int getSelectionIndex() {
		return cmbItems.getSelectionIndex();
	}

	public void removeAll() {
		cmbItems.removeAll();
	}

	public void setItemData(String[] data) {
		this.data = data;
		this.defaultIndex = indexOf(this.defaultItem);
	}

	public void setDefaultItem(String itemName) {
		this.defaultItem = itemName;
		this.defaultIndex = indexOf(this.defaultItem);
	}

	public void select(int index) {
		cmbItems.select(index);
	}

	public void setText(String text) {
		if (indexOf(text) < 0) {
			cmbItems.select(this.defaultIndex);
			return;
		}
		cmbItems.setText(text);
	}

	public void setSelection(String itemName) {
		if (data != null) {
			for (int i = 0; i < data.length; i++) {
				if (itemName.equals(data[i])) {
					cmbItems.select(i);
					return;
				}
			}
		}
	}

	protected int indexOf(String itemName) {
		if (data != null && itemName != null) {
			for (int i = 0; i < data.length; i++) {
				if (itemName.equals(data[i])) {
					return i;
				}
			}
		}
		return -1;
	}

	public String getSelectedItemData() {
		return data[cmbItems.getSelectionIndex()];
	}

	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		cmbItems.setEnabled(enabled);
	}

	public Combo getWidget() {
		return cmbItems;
	}

	public void addSelectionListener(SelectionListener listener) {
		vListeners.add(listener);
		cmbItems.addSelectionListener(this);
	}

	@Override
	public void addListener(int eventType, final Listener listener) {
		// Only add support for addListener(SWT.Selection, listener)
		if (eventType == SWT.Selection) {
			addSelectionListener(new SelectionListener() {

				@Override
				public void widgetSelected(SelectionEvent event) {
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
					e.widget = event.widget;
					e.x = event.x;
					e.y = event.y;
					listener.handleEvent(e);
				}

				@Override
				public void widgetDefaultSelected(SelectionEvent evt) {
					// TODO Auto-generated method stub

				}
			});
		}
		super.addListener(eventType, listener);
	}

	@Override
	public void widgetDefaultSelected(SelectionEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void widgetSelected(SelectionEvent event) {
		if (event.widget == cmbItems) {
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

	public void setEObjectParent(EObject eParent) {
		this.eParent = eParent;
	}
}
