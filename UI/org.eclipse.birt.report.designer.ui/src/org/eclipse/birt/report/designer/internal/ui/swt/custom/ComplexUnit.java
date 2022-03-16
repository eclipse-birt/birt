/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
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

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.core.format.NumberFormatter;
import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 *
 */

public class ComplexUnit extends Canvas {

	private NumberFormatter formatter = new NumberFormatter(
			SessionHandleAdapter.getInstance().getSessionHandle().getULocale()); // $NON-NLS-1$

	private Text text;
	private CCombo combo;

	private double minimum = 0, maximum = Double.MAX_VALUE, step = 1;

	private boolean dirty = false;

	public ComplexUnit(Composite parent, int style) {
		super(parent, SWT.NONE);

		layout = new GridLayout();
		layout.numColumns = 2;
		layout.horizontalSpacing = 2;
		layout.marginWidth = layout.marginHeight = 0;
		this.setLayout(layout);

		int textStyle = SWT.SINGLE;
		if ((style & SWT.READ_ONLY) != 0) {
			textStyle |= SWT.READ_ONLY;
		}
		if ((style & SWT.FLAT) != 0) {
			textStyle |= SWT.FLAT;
		}
		if ((style & SWT.BORDER) != 0) {
			textStyle |= SWT.BORDER;
		}
		text = new Text(this, textStyle);
		combo = new CCombo(this, textStyle | SWT.READ_ONLY);
		combo.setVisibleItemCount(30);

		GridData gd = new GridData(GridData.FILL_BOTH);
		text.setLayoutData(gd);

		gd = new GridData(GridData.FILL_VERTICAL);
		combo.setLayoutData(gd);

		text.addKeyListener(new KeyListener() {

			@Override
			public void keyReleased(KeyEvent e) {
				if (e.keyCode == SWT.KEYPAD_CR || e.keyCode == SWT.TRAVERSE_RETURN) {
					fireValueChanged();
				}
			}

			@Override
			public void keyPressed(KeyEvent e) {
				if (e.keyCode == SWT.ARROW_UP) {
					up();
					e.doit = false;
				}

				if (e.keyCode == SWT.ARROW_DOWN) {
					down();
					e.doit = false;
				}
			}
		});

		text.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				dirty = true;
				fireTextModified(e);
			}

		});

		combo.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				dirty = true;
			}

		});

		combo.addKeyListener(new KeyAdapter() {

			@Override
			public void keyReleased(KeyEvent e) {
				if (e.keyCode == SWT.KEYPAD_CR || e.keyCode == SWT.TRAVERSE_RETURN) {
					fireValueChanged();
				}
			}
		});

		text.addTraverseListener(new TraverseListener() {

			@Override
			public void keyTraversed(TraverseEvent e) {
				if (e.detail == SWT.TRAVERSE_RETURN) {
					fireValueChanged();
				}
				if (e.detail == SWT.TRAVERSE_ESCAPE || e.detail == SWT.TRAVERSE_ARROW_PREVIOUS
						|| e.detail == SWT.TRAVERSE_ARROW_NEXT) {
					e.doit = false;
				}

			}

		});

		combo.addTraverseListener(new TraverseListener() {

			@Override
			public void keyTraversed(TraverseEvent e) {
				if (e.detail == SWT.TRAVERSE_RETURN) {
					fireValueChanged();
				}
				if (e.detail == SWT.TRAVERSE_ESCAPE) {
					e.doit = false;
				}

			}

		});

		FocusListener focusListner = new FocusListener() {

			@Override
			public void focusGained(FocusEvent e) {
				handleFocus(SWT.FocusIn);
			}

			@Override
			public void focusLost(FocusEvent e) {
				handleFocus(SWT.FocusOut);

			}

		};

		text.addFocusListener(focusListner);

		combo.addFocusListener(focusListner);

		text.setFont(getFont());
		combo.setFont(getFont());

		filter = new Listener() {

			@Override
			public void handleEvent(Event event) {
				if (isDisposed()) {
					return;
				}
				Shell shell = ((Control) event.widget).getShell();
				if (shell == ComplexUnit.this.getShell()) {
					handleFocus(SWT.FocusOut);
				}
			}
		};

		addDisposeListener(new DisposeListener() {

			@Override
			public void widgetDisposed(DisposeEvent e) {
				Display display = getDisplay();
				display.removeFilter(SWT.FocusIn, filter);
			}

		});
	}

	private List valueChangedlisteners = new ArrayList();

	/**
	 * Adds a IValueChangedListener instance.
	 *
	 * @param listener the IValueChangedListener instance.
	 */
	public void addValueChangeListener(IDimensionValueChangedListener listener) {
		valueChangedlisteners.add(listener);
	}

	/**
	 * Removes a given IValueChangedListener instance.
	 *
	 * @param listener the IValueChangedListener instance.
	 */
	public void removeValueChangedListener(IDimensionValueChangedListener listener) {
		valueChangedlisteners.remove(listener);
	}

	private List modifyListeners = new ArrayList();

	public void addModifyListener(ModifyListener listener) {
		modifyListeners.add(listener);
	}

	public void removeModifyListener(ModifyListener listener) {
		modifyListeners.remove(listener);
	}

	/**
	 *
	 */
	protected void fireValueChanged() {
//		if ( !verify( text.getText( ) ) )
//		{
//			return;
//		}
		for (int i = 0; i < valueChangedlisteners.size(); i++) {
			String value = text.getText().trim();
			value = value.length() == 0 ? null : value;
			((IDimensionValueChangedListener) valueChangedlisteners.get(i)).valueChanged(value, getUnit());
		}
	}

	protected void fireTextModified(ModifyEvent e) {
		for (int i = 0; i < modifyListeners.size(); i++) {
			((ModifyListener) modifyListeners.get(i)).modifyText(e);
		}
	}

	/**
	 * Processes up action
	 */
	protected void up() {
		setValue(getValue() + step);
	}

	/**
	 * Processes down action
	 */
	protected void down() {
		setValue(getValue() - step);
	}

	/**
	 * Returns the current spinner value.
	 *
	 * @return The current spinner value.
	 */
	public double getValue() {
		try {
			return parse(text.getText());
		} catch (ParseException e) {
			ExceptionHandler.handle(e);
		}
		return minimum;
	}

	private double parse(String value) throws ParseException {
		if (StringUtil.isBlank(value)) {
			return 0;
		}
		return formatter.parse(value).doubleValue();
	}

	public boolean getEditable() {
		checkWidget();
		return text.getEditable();
	}

	/**
	 * Gets the step value.
	 *
	 * @return Returns the step.
	 */
	public double getStep() {
		return step;
	}

	/**
	 * Sets the step value
	 *
	 * @param step The step to set.
	 */
	public void setStep(double step) {
		this.step = step;
	}

	public String getUnit() {
		if (!combo.isDisposed()) {
			return combo.getText().trim().length() == 0 ? null : combo.getText();
		} else {
			return null;
		}
	}

	public String[] getUnits() {
		if (!combo.isDisposed()) {
			return combo.getItems();
		} else {
			return null;
		}
	}

	public int getUnitSelectionIndex() {
		if (!combo.isDisposed()) {
			return combo.getSelectionIndex();
		} else {
			return -1;
		}
	}

	public void selectUnit(int index) {
		if (!combo.isDisposed()) {
			combo.select(index);
		}
	}

	public void setUnit(String unit) {
		if (!combo.isDisposed()) {
			combo.setText(unit);
		}
	}

	public void setUnits(String[] units) {
		if (!combo.isDisposed()) {
			combo.removeAll();
			combo.setItems(units);
			this.layout();
		}
	}

	/**
	 * Sets the initial spinner value.
	 *
	 * @param selection The spinner value to set.
	 */
	public void setValue(double selection) {
		if (selection < minimum) {
			selection = minimum;
		} else if (selection > maximum) {
			selection = maximum;
		}

		text.setText(StringUtil.doubleToString(selection, digit,
				SessionHandleAdapter.getInstance().getSessionHandle().getULocale()));
		validValue = selection;
	}

	private double validValue;
	private static final String DLG_TITLE_INVALID_NUMBER = Messages.getString("Spinner.DialogTitle.InvalidNumber"); //$NON-NLS-1$

	private boolean verify(String value) {
		try {
			if (value.trim().length() == 0) {
				return true;
			}
			validValue = parse(value);
			if (validValue < minimum) {
				validValue = minimum;
			}
			if (validValue > maximum) {
				validValue = maximum;
			}
			text.setText(StringUtil.doubleToString(validValue, digit,
					SessionHandleAdapter.getInstance().getSessionHandle().getULocale()));
			return true;
		} catch (ParseException ex) {
			ExceptionHandler.openErrorMessageBox(DLG_TITLE_INVALID_NUMBER, ex.getLocalizedMessage());
			text.setText(StringUtil.doubleToString(validValue, digit,
					SessionHandleAdapter.getInstance().getSessionHandle().getULocale()));
			if (!text.isDisposed()) {
				handleFocus(SWT.FocusIn);
			}
			return false;
		}
	}

	boolean hasFocus;
	private GridLayout layout;

	void handleFocus(int type) {
		switch (type) {
		case SWT.FocusIn: {
			if (hasFocus) {
				return;
			}
			dirty = false;
			hasFocus = true;

			Display display = getDisplay();
			display.removeFilter(SWT.FocusIn, filter);
			display.addFilter(SWT.FocusIn, filter);

			break;
		}
		case SWT.FocusOut: {
			if (!hasFocus) {
				return;
			}
			Control focusControl = getDisplay().getFocusControl();
			if (focusControl == text || focusControl == combo) {
				return;
			} else if (focusControl != null) {
				Control parent = focusControl.getParent();
				while (parent != null) {
					if (parent == this) {
						return;
					}
					parent = parent.getParent();
				}
			}
			hasFocus = false;
			if (dirty) {
				fireValueChanged();
			}

			Display display = getDisplay();
			display.removeFilter(SWT.FocusIn, filter);

			break;
		}
		}
	}

	public void deselectUnit() {
		if (!combo.isDisposed()) {
			combo.deselectAll();
		}
	}

	public void setReadOnly(boolean b) {
		combo.setEnabled(!b);
		text.setEditable(!b);
	}

	public boolean isReadOnly() {
		return text.getEditable();
	}

	private int digit = 3;

	private Listener filter;

	public int getDigit() {
		return digit;
	}

	public void setDigit(int digit) {
		this.digit = digit;
	}

	public String getText() {
		if (!text.isDisposed()) {
			return text.getText();
		}
		return null;
	}

	public void setValue(String value) {
		if (verify(value)) {
			text.setText(value);
		}

	}

}
