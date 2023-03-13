
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

/**
 *
 */

public class ValueCombo extends Combo {

	int visibleCount = 30;

	public interface ISelection {

		String doSelection(String input);
	}

	public interface ISelection2 extends ISelection {

		String doSelection(String comboValue, int selectedIndex, String selectedValue);
	}

	Map<Integer, ISelection> actionMap = new HashMap<>();

	// super.addSelectionListener( listener )
	protected SelectionListener selListener = new SelectionListener() {

		@Override
		public void widgetDefaultSelected(SelectionEvent e) {
			// TODO Auto-generated method stub
		}

		@Override
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

		@Override
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

		@Override
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

		@Override
		public void keyPressed(KeyEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
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
	List<String> oldValueList = new ArrayList<>();

	@Override
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
	@Override
	public void addSelectionListener(SelectionListener listener) {
		// do nothing
	}

	// add KeyListener is forbidden
	@Override
	public void addKeyListener(KeyListener listener) {
		// do nothing
	}

	@Override
	public void addListener(int eventType, Listener listener) {
		if (addListenerLock
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
