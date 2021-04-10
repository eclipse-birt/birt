
package org.eclipse.birt.report.designer.internal.ui.swt.custom;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Listener;

/*******************************************************************************
 * Copyright (c) 2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

/**
 * 
 */

public class ValueCombo extends Combo {

	int visibleCount = 30;

	public static interface ISelection {

		public String doSelection(String input);
	}

	public static interface ISelection2 extends ISelection {

		public String doSelection(String comboValue, int selectedIndex, String selectedValue);
	}

	Map<Integer, ISelection> actionMap = new HashMap<Integer, ISelection>();

	// super.addSelectionListener( listener )
	protected SelectionListener selListener = new SelectionListener() {

		public void widgetDefaultSelected(SelectionEvent e) {
			// TODO Auto-generated method stub
		}

		public void widgetSelected(SelectionEvent e) {
			// TODO Auto-generated method stub

			// System.out.print( "Selection Listener is involved.\n" );

			String eText = e.text;
			ValueCombo combo = (ValueCombo) e.widget;
			String comboText = combo.getText();
			if (!selected) {
				return;
			}
			selected = false;
			int index = combo.indexOf(comboText);
			if (index >= 0) {
				if (oldValueList.size() > 0) {
					oldValue = oldValueList.get(0);
					oldValueList.clear();
				}

				ISelection action = actionMap.get(index);
				if (action == null) {
					oldValue = comboText;
					oldValueList.add(oldValue);
					return;
				}

				String text = null;
				if (action instanceof ISelection2) {
					text = ((ISelection2) action).doSelection(oldValue, index, comboText);
				} else {
					text = action.doSelection(oldValue);
				}

				if (text != null) {
					oldValue = text;
				}

				oldValueList.add(oldValue);
				combo.select(-1);
				combo.setText(oldValue);

			}

		}
	};

	protected ModifyListener modifyListener = new ModifyListener() {

		public void modifyText(ModifyEvent e) {
			// TODO Auto-generated method stub

			ValueCombo combo = (ValueCombo) e.widget;
			String comboText = combo.getText();

			// System.out.print( "Modify Listener is involved." + comboText +
			// "\n" );

			if (selected) {
				return;
			}

			if (shouldClearValues) {
				oldValueList.clear();
				shouldClearValues = false;
			}
			if (shouldSaveValue) {
				oldValueList.add(comboText);
				shouldSaveValue = false;
			}

		}

	};

	protected VerifyListener verifyListener = new VerifyListener() {

		public void verifyText(VerifyEvent e) {
			// TODO Auto-generated method stub
			// System.out.print( "Verify Listener is involved.\n" );

			selected = false;
			String eText = e.text;
			ValueCombo combo = (ValueCombo) e.widget;
			if (combo.indexOf(eText) >= 0) {
				selected = true;
				if (combo.indexOf(combo.getText()) < 0) {
					oldValueList.add(combo.getText());
					shouldClearValues = false;
					shouldSaveValue = false;
				}
				return;
			}

			if (!eText.equals("")) {
				shouldClearValues = true;
				shouldSaveValue = true;
			}
		}
	};

	protected KeyListener keyListener = new KeyListener() {

		public void keyPressed(KeyEvent e) {
			// TODO Auto-generated method stub

		}

		public void keyReleased(KeyEvent e) {
			// TODO Auto-generated method stub
			ValueCombo combo = (ValueCombo) e.widget;
			String comboText = combo.getText();
			oldValueList.clear();
			oldValueList.add(comboText);
		}

	};

	boolean setTextBySelection = false;
	boolean addListenerLock = true;
	boolean keyPressed = false;

	boolean selected = false;
	String oldValue = null;
	boolean shouldSaveValue = false;
	boolean shouldClearValues = false;
	List<String> oldValueList = new ArrayList<String>();

	protected void checkSubclass() {

	}

	public void setTextBySelection(boolean set) {
		setTextBySelection = set;
	}

	public ValueCombo(Composite parent, int style) {
		super(parent, style);
		// TODO Auto-generated constructor stub

		actionMap.clear();
		initializeSelectionListener();
	}

	protected void initializeSelectionListener() {
		addListenerLock = false;
		super.addKeyListener(keyListener);
		super.addSelectionListener(selListener);
		addListenerLock = true;
		super.addModifyListener(modifyListener);
		super.addVerifyListener(verifyListener);
	}

	// add SelectionListener is forbidden, please use addSelectionListener(index
	// , selection) instead.
	public void addSelectionListener(SelectionListener listener) {
		// do nothing
	}

	// add KeyListener is forbidden
	public void addKeyListener(KeyListener listener) {
		// do nothing
	}

	public void addListener(int eventType, Listener listener) {
		if (addListenerLock == true
				&& (eventType == SWT.Selection || eventType == SWT.KeyUp || eventType == SWT.KeyDown)) {
			return;
		}

		super.addListener(eventType, listener);
	}

	// index : [0, getItemCount - 1]
	public void addSelectionListener(int index, ISelection selection) {
		actionMap.put(index, selection);
	}

	// index : [0, getItemCount - 1]
	public void removeSelectionListener(int index) {
		actionMap.remove(index);
	}

	public void clearSelectionListeners() {
		actionMap.clear();
	}

}
