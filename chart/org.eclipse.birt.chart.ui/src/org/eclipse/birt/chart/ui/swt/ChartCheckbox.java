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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.chart.ui.util.ChartUIUtil;
import org.eclipse.birt.chart.util.ChartUtil;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

/**
 * AbstractChartCheckbox
 * 
 */

public class ChartCheckbox extends Canvas implements Listener, SelectionListener {
	public static final int STATE_GRAYED = 0;

	public static final int STATE_SELECTED = 1;

	public static final int STATE_UNSELECTED = 2;

	protected Button button;
	protected List<SelectionListener> selectListenerList = new ArrayList<SelectionListener>(2);

	protected boolean bDefaultSelection = false;

	protected FocusListener btnFocusLinster = new FocusListener() {

		public void focusLost(FocusEvent e) {
			ChartCheckbox.this.redraw();
		}

		public void focusGained(FocusEvent e) {
			ChartCheckbox.this.redraw();
		}
	};

	protected PaintListener checkBoxPaintListener = new PaintListener() {

		public void paintControl(PaintEvent e) {
			// Do some drawing
			if (button.isFocusControl()) {
				Rectangle rect = ((Canvas) e.widget).getBounds();
				e.gc.drawFocus(0, 0, rect.width, rect.height);
			}
		}
	};

	/**
	 * Constructor.
	 * 
	 * @param container
	 * @param styles
	 */
	public ChartCheckbox(Composite container, int styles) {
		this(container, styles, false);
	}

	/**
	 * Constructor.
	 * 
	 * @param container
	 * @param styles
	 * @param defaultSelection
	 */
	public ChartCheckbox(Composite container, int styles, boolean defaultSelection) {
		super(container, SWT.NONE);
		this.bDefaultSelection = defaultSelection;
		placeComponents(styles);
		initListeners();
	}

	/**
	 * Initialize listeners.
	 */
	protected void initListeners() {
		// Create a paint handler for the canvas
		if (ChartUtil.isEmpty(button.getText())) {
			registerFocusPaintListener();
		}
	}

	protected void registerFocusPaintListener() {
		button.addFocusListener(btnFocusLinster);
		this.addPaintListener(checkBoxPaintListener);
	}

	protected void unregisterFocusPaintListener() {
		button.removeFocusListener(btnFocusLinster);
		this.removePaintListener(checkBoxPaintListener);
	}

	protected void placeComponents(int styles) {
		GridLayout gl = new GridLayout();
		gl.marginHeight = 0;
		gl.marginWidth = 0;
		gl.marginLeft = 2;
		gl.marginRight = 2;
		gl.marginTop = 2;
		gl.marginBottom = 2;
		setLayout(gl);
		button = new Button(this, SWT.CHECK | styles);
		GridData gd = new GridData(GridData.FILL_BOTH);
		button.setLayoutData(gd);
	}

	/**
	 * Set if default state is selection for this button.
	 * 
	 * @param defSelection
	 */
	public void setDefaultSelection(boolean defSelection) {
		this.bDefaultSelection = defSelection;
	}

	/**
	 * Set button text.
	 * 
	 * @param text
	 */
	public void setText(String text) {
		button.setText(text);
		// Create a paint handler for the canvas
		if (ChartUtil.isEmpty(button.getText())) {
			registerFocusPaintListener();
		} else {
			unregisterFocusPaintListener();
		}
	}

	/**
	 * Returns checkbox state, 0 means grayed state, 1 means checked state, 2 means
	 * unchecked state.
	 * 
	 * @return selection state.
	 */
	public int getSelectionState() {
		if (button.getGrayed()) {
			return this.bDefaultSelection ? STATE_SELECTED : STATE_UNSELECTED;
		} else if (button.getSelection()) {
			return STATE_SELECTED;
		} else {
			return STATE_UNSELECTED;
		}
	}

	/**
	 * Sets checkbox state.
	 * 
	 * @param state the state value, 0 means grayed state, 1 means checked state, 2
	 *              means unchecked state.
	 */
	public void setSelectionState(int state) {
		switch (state) {
		case STATE_GRAYED:
			button.setSelection(this.bDefaultSelection);
			break;
		case STATE_SELECTED:
			button.setSelection(true);
			break;
		case STATE_UNSELECTED:
			button.setSelection(false);
			break;
		}
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
		if (button != null) {
			button.addListener(eventType, this);
		}
	}

	/**
	 * Adds selection listener for button.
	 * 
	 * @param listener
	 */
	public void addSelectionListener(SelectionListener listener) {
		selectListenerList.add(listener);
		button.addSelectionListener(this);
	}

	/**
	 * Returns actual button object.
	 * 
	 * @return actual button widget.
	 */
	public Button getButton() {
		return button;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.swt.widgets.Listener#handleEvent(org.eclipse.swt.widgets.Event)
	 */
	public void handleEvent(Event event) {
		if (event.widget == button) {
			event.widget = this;
			Listener[] lis = this.getListeners(event.type);
			for (int i = (lis.length - 1); i >= 0; i--) {
				lis[i].handleEvent(event);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.swt.events.SelectionListener#widgetDefaultSelected(org.eclipse.
	 * swt.events.SelectionEvent)
	 */
	public void widgetDefaultSelected(SelectionEvent event) {
		// Do nothing.
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.events.SelectionListener#widgetSelected(org.eclipse.swt.
	 * events.SelectionEvent)
	 */
	public void widgetSelected(SelectionEvent event) {
		if (event.widget == button) {
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

			for (int i = (selectListenerList.size() - 1); i >= 0; i--) {
				selectListenerList.get(i).widgetSelected(se);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.widgets.Control#setEnabled(boolean)
	 */
	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		button.setEnabled(enabled);
	}

	public void addScreenReaderAccessiblity(String description) {
		if (ChartUtil.isEmpty(button.getText())) {
			ChartUIUtil.addScreenReaderAccessbility(button, description);
		}
	}
}
