/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation and others.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors: Actuate Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.report.designer.internal.ui.swt.custom;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.core.format.NumberFormatter;
import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.widget.IValueChangedListener;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTError;
import org.eclipse.swt.accessibility.ACC;
import org.eclipse.swt.accessibility.AccessibleAdapter;
import org.eclipse.swt.accessibility.AccessibleControlAdapter;
import org.eclipse.swt.accessibility.AccessibleControlEvent;
import org.eclipse.swt.accessibility.AccessibleEvent;
import org.eclipse.swt.accessibility.AccessibleTextAdapter;
import org.eclipse.swt.accessibility.AccessibleTextEvent;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.TypedListener;

/**
 * Represents spinner swt widget
 */
public class CSpinner extends Composite {

	private static final String DLG_TITLE_INVALID_NUMBER = Messages.getString("Spinner.DialogTitle.InvalidNumber"); //$NON-NLS-1$

	private static final int BUTTON_WIDTH = 16;

	private Text text;

	private Button up, down;

	private double minimum = 0, maximum = 9, step = 1;

	/**
	 * Format the text value.
	 */
	private NumberFormatter formatter = new NumberFormatter(
			SessionHandleAdapter.getInstance().getSessionHandle().getULocale()); // $NON-NLS-1$

	/**
	 * The list keeps IValueChangedListener.
	 */
	private List listeners = new ArrayList();

	private double validValue;

	private boolean isInErrrorHandle;

	/**
	 * @param parent a widget which will be the parent of the new instance (cannot
	 *               be null)
	 * @param style  the style of widget to construct
	 */
	public CSpinner(Composite parent, int style) {
		super(parent, style);

		int textStyle = SWT.SINGLE;
		if ((style & SWT.READ_ONLY) != 0) {
			textStyle |= SWT.READ_ONLY;
		}
		if ((style & SWT.FLAT) != 0) {
			textStyle |= SWT.FLAT;
		}
		text = new Text(this, textStyle);
		int arrowStyle = SWT.ARROW;
		if ((style & SWT.FLAT) != 0) {
			arrowStyle |= SWT.FLAT;
		}
		up = new Button(this, style | SWT.ARROW | SWT.UP);
		down = new Button(this, style | SWT.ARROW | SWT.DOWN);

		addListener(SWT.Traverse, new Listener() {

			@Override
			public void handleEvent(Event e) {
				traverse(e);
			}
		});
		text.addKeyListener(new KeyAdapter() {

			@Override
			public void keyReleased(KeyEvent e) {
				if (e.keyCode == SWT.KEYPAD_CR || e.keyCode == SWT.TRAVERSE_RETURN) {
					fireValueChanged();
				}

				if (e.keyCode == SWT.ARROW_UP) {
					up();
					fireValueChanged();
				}

				if (e.keyCode == SWT.ARROW_DOWN) {
					down();
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
				if (e.detail == SWT.TRAVERSE_ESCAPE) {
					e.doit = false;
				}

			}

		});

		text.addFocusListener(new FocusListener() {

			@Override
			public void focusGained(FocusEvent e) {

			}

			@Override
			public void focusLost(FocusEvent e) {
				if (!isInErrrorHandle) {
					fireValueChanged();
				}

			}

		});

		up.addListener(SWT.Selection, new Listener() {

			@Override
			public void handleEvent(Event e) {
				up();
				fireValueChanged();
			}
		});

		down.addListener(SWT.Selection, new Listener() {

			@Override
			public void handleEvent(Event e) {
				down();
				fireValueChanged();
			}
		});

		addListener(SWT.Resize, new Listener() {

			@Override
			public void handleEvent(Event e) {
				resize();
			}
		});

		addListener(SWT.FocusIn, new Listener() {

			@Override
			public void handleEvent(Event e) {
				focusIn();
			}
		});

		text.setFont(getFont());

		setSelection(minimum);
		initAccessible();
	}

	void initAccessible() {
		AccessibleAdapter accessibleAdapter = new AccessibleAdapter() {

			@Override
			public void getName(AccessibleEvent e) {
				getHelp(e);
			}

			@Override
			public void getHelp(AccessibleEvent e) {
				e.result = getToolTipText();
			}
		};
		getAccessible().addAccessibleListener(accessibleAdapter);
		up.getAccessible().addAccessibleListener(accessibleAdapter);
		down.getAccessible().addAccessibleListener(accessibleAdapter);

		getAccessible().addAccessibleTextListener(new AccessibleTextAdapter() {

			@Override
			public void getCaretOffset(AccessibleTextEvent e) {
				e.offset = text.getCaretPosition();
			}
		});

		getAccessible().addAccessibleControlListener(new AccessibleControlAdapter() {

			@Override
			public void getChildAtPoint(AccessibleControlEvent e) {
				Point pt = toControl(new Point(e.x, e.y));
				e.childID = (getBounds().contains(pt)) ? ACC.CHILDID_SELF : ACC.CHILDID_NONE;
			}

			@Override
			public void getLocation(AccessibleControlEvent e) {
				Rectangle location = getBounds();
				Point pt = toDisplay(location.x, location.y);
				e.x = pt.x;
				e.y = pt.y;
				e.width = location.width;
				e.height = location.height;
			}

			@Override
			public void getChildCount(AccessibleControlEvent e) {
				e.detail = 0;
			}

			@Override
			public void getRole(AccessibleControlEvent e) {
				e.detail = ACC.ROLE_COMBOBOX;
			}

			@Override
			public void getState(AccessibleControlEvent e) {
				e.detail = ACC.STATE_NORMAL;
			}
		});

	}

	/**
	 *
	 */
	protected void fireValueChanged() {
		if (!verify(text.getText())) {
			return;
		}
		for (int i = 0; i < listeners.size(); i++) {
			((IValueChangedListener) listeners.get(i)).valueChanged(getSelection());
		}
	}

	/**
	 * Adds a IValueChangedListener instance.
	 *
	 * @param listener the IValueChangedListener instance.
	 */
	public void addValueChangeListener(IValueChangedListener listener) {
		listeners.add(listener);
	}

	/**
	 * Removes a given IValueChangedListener instance.
	 *
	 * @param listener the IValueChangedListener instance.
	 */
	public void removeValueChangedListener(IValueChangedListener listener) {
		listeners.remove(listener);
	}

	/**
	 *
	 * @param pattern A non-localized pattern string.
	 * @see java.text.DecimalFormat
	 */
	public void setFormatPattern(String pattern) {
		formatter.applyPattern(pattern);
	}

	/**
	 * Verifies whether the input String is a valid double.
	 *
	 * @param value The string to be verified.
	 * @return Returns true if the string is a valid double value, or false if it is
	 *         invalid.
	 */
	private boolean verify(String value) {
		try {
			validValue = parse(value);
			if (validValue < minimum) {
				validValue = minimum;
			}
			if (validValue > maximum) {
				validValue = maximum;
			}
			text.setText(formatter.format(validValue));
			return true;
		} catch (ParseException ex) {
			isInErrrorHandle = true;
			ExceptionHandler.openErrorMessageBox(DLG_TITLE_INVALID_NUMBER, ex.getLocalizedMessage());
			isInErrrorHandle = false;
			text.setText(formatter.format(validValue));
			if (!text.isDisposed()) {
				focusIn();
			}
			return false;
		}
	}

	/**
	 * Processes up/down key event
	 *
	 * @param e The key event type.
	 */
	protected void traverse(Event e) {
		switch (e.detail) {
		case SWT.TRAVERSE_ARROW_PREVIOUS:
			if (e.keyCode == SWT.ARROW_UP) {
				e.doit = true;
				e.detail = SWT.NULL;
				up();
			}
			break;
		case SWT.TRAVERSE_ARROW_NEXT:
			if (e.keyCode == SWT.ARROW_DOWN) {
				e.doit = true;
				e.detail = SWT.NULL;
				down();
			}
		}
	}

	/**
	 * Processes up action
	 */
	protected void up() {
		setSelection(getSelection() + step);
		notifyListeners(SWT.Selection, new Event());
	}

	/**
	 * Processes down action
	 */
	protected void down() {
		setSelection(getSelection() - step);
		notifyListeners(SWT.Selection, new Event());
	}

	protected void focusIn() {
		text.setFocus();
		text.setSelection(0, text.getText().length());
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.swt.widgets.Control#setFont(org.eclipse.swt.graphics.Font)
	 */
	@Override
	public void setFont(Font font) {
		super.setFont(font);
		text.setFont(font);
	}

	/**
	 * Sets the initial spinner value.
	 *
	 * @param selection The spinner value to set.
	 */
	public void setSelection(double selection) {
		if (selection < minimum) {
			selection = minimum;
		} else if (selection > maximum) {
			selection = maximum;
		}

		text.setText(formatter.format(selection));
		validValue = selection;
	}

	/**
	 * Returns the current spinner value.
	 *
	 * @return The current spinner value.
	 */
	public double getSelection() {
		try {
			return parse(text.getText());
		} catch (ParseException e) {
			ExceptionHandler.handle(e);
		}
		return minimum;
	}

	/**
	 * Sets the max spinner value.
	 *
	 * @param maximum the max value.
	 */
	public void setMaximum(double maximum) {
		checkWidget();
		this.maximum = maximum;
		resize();

	}

	/**
	 * Gets the max spinner value.
	 *
	 * @return the max value.
	 */
	public double getMaximum() {
		return maximum;
	}

	/**
	 * Sets the minimum spinner value.
	 *
	 * @param minimum the minimum value.
	 */
	public void setMinimum(double minimum) {
		this.minimum = minimum;
	}

	/**
	 * Gets the minimum spinner value.
	 *
	 * @return the minimum value.
	 */
	public double getMinimum() {
		return minimum;
	}

	/**
	 * Re-layouts the control.
	 */
	protected void resize() {
		Rectangle rect = getClientArea();
		int width = rect.width;
		int height = rect.height;

		int textWidth = width - BUTTON_WIDTH;
		text.setBounds(0, 0, textWidth, height);

		int buttonHeight = height / 2;
		up.setBounds(textWidth, 0, BUTTON_WIDTH, buttonHeight);
		down.setBounds(textWidth, height - buttonHeight, BUTTON_WIDTH, buttonHeight);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.swt.widgets.Control#computeSize(int, int, boolean)
	 */
	@Override
	public Point computeSize(int wHint, int hHint, boolean changed) {
		Point textPt = text.computeSize(SWT.DEFAULT, SWT.DEFAULT, changed);
		Point upPt = up.computeSize(SWT.DEFAULT, SWT.DEFAULT, changed);

		int width = textPt.x + BUTTON_WIDTH;
		int height = Math.max(textPt.y, upPt.y);

		if (wHint != SWT.DEFAULT) {
			width = wHint;
		}

		if (hHint != SWT.DEFAULT) {
			height = hHint;
		}

		return new Point(width, height);
	}

	protected void addSelectionListener(SelectionListener listener) {

		if (listener == null) {
			throw new SWTError(SWT.ERROR_NULL_ARGUMENT);
		}
		addListener(SWT.Selection, new TypedListener(listener));
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

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.swt.widgets.Control#setEnabled(boolean)
	 */
	@Override
	public void setEnabled(boolean enabled) {
		text.setEnabled(enabled);
		up.setEnabled(enabled);
		down.setEnabled(enabled);
		super.setEnabled(enabled);
	}

	/**
	 * Returns the text of the spinner.
	 *
	 * @return the text of the spinner.
	 */

	public String getText() {
		return text.getText();
	}

	private double parse(String value) throws ParseException {
		if (StringUtil.isBlank(value)) {
			return 0;
		}
		return formatter.parse(value).doubleValue();
	}
}
