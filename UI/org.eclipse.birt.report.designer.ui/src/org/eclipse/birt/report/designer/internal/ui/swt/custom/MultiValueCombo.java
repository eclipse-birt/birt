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

/**
 * 
 */

public class MultiValueCombo extends Combo {

	public static interface ISelection {
		public String[] doSelection(String input);

		public void doAfterSelection(MultiValueCombo combo);
	}

	Map<Integer, ISelection> actionMap = new HashMap<Integer, ISelection>();
	SelectionListener selListener;
	ModifyListener modifyListener;
	VerifyListener verifyListener;
	KeyListener keyListener;
	boolean setTextBySelection = false;
	boolean addListenerLock = true;
	boolean keyPressed = false;

	boolean selected = false;
	String oldValue = null;
	boolean shouldSaveValue = false;
	boolean shouldClearValues = false;
	List<String> oldValueList = new ArrayList<String>();

	String selStrings[] = null;

	public String[] getSelStrings() {
		String[] ret = selStrings;
		if (ret == null) {
			return new String[0];
		}
		return ret;
	}

	protected void checkSubclass() {

	}

	public void setTextBySelection(boolean set) {
		setTextBySelection = set;
	}

	public MultiValueCombo(Composite parent, int style) {
		super(parent, style);
		// TODO Auto-generated constructor stub

		actionMap.clear();
		initializeSelectionListener();
	}

	public void initializeSelectionListener() {

		keyListener = new KeyListener() {

			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub

			}

			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub
				MultiValueCombo combo = (MultiValueCombo) e.widget;
				String comboText = combo.getText();
				oldValueList.clear();
				oldValueList.add(comboText);
			}

		};
		// super.addSelectionListener( listener )
		selListener = new SelectionListener() {

			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
			}

			public void widgetSelected(SelectionEvent e) {
				// TODO Auto-generated method stub

//				System.out.print( "Selection Listener is involved.\n" );

				String eText = e.text;
				MultiValueCombo combo = (MultiValueCombo) e.widget;
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

					selStrings = action.doSelection(oldValue);
					String text = null;

					if (selStrings != null) {
						if (selStrings.length == 1) {
							text = selStrings[0];
						} else if (selStrings.length > 1) {
							text = "";
						}
					}

					if (text != null) {
						oldValue = text;
					}

					oldValueList.add(oldValue);
					combo.select(-1);
					combo.setText(oldValue);

					action.doAfterSelection(combo);
				}

			}
		};

		modifyListener = new ModifyListener() {

			public void modifyText(ModifyEvent e) {
				// TODO Auto-generated method stub

				MultiValueCombo combo = (MultiValueCombo) e.widget;
				String comboText = combo.getText();

//				System.out.print( "Modify Listener is involved."
//						+ comboText
//						+ "\n" );

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

		verifyListener = new VerifyListener() {

			public void verifyText(VerifyEvent e) {
				// TODO Auto-generated method stub
//				System.out.print( "Verify Listener is involved.\n" );

				selected = false;
				String eText = e.text;
				MultiValueCombo combo = (MultiValueCombo) e.widget;

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

}
