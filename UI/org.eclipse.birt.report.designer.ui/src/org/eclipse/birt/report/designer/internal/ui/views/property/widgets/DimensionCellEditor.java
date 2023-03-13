/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
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

package org.eclipse.birt.report.designer.internal.ui.views.property.widgets;

import org.eclipse.birt.report.designer.util.NumberUtil;
import org.eclipse.birt.report.model.api.metadata.DimensionValue;
import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;

import com.ibm.icu.util.ULocale;

/**
 * A cell editor that manages a dimension field.
 */
public class DimensionCellEditor extends CDialogCellEditor {

	private String[] units;
	private String unitName;
	private Text textEditor;
	private int style;
	private int inProcessing = 0;

	Listener filter = new Listener() {

		@Override
		public void handleEvent(Event event) {
			if (textEditor.isDisposed()) {
				return;
			}
			handleFocus(SWT.FocusOut);
		}
	};

	boolean hasFocus = false;

	/**
	 * Creates a new dialog cell editor parented under the given control.
	 *
	 * @param parent    the parent control
	 * @param unitNames the name list
	 */
	public DimensionCellEditor(Composite parent, String[] unitNames) {
		super(parent);
		this.units = unitNames;
	}

	/**
	 * Creates a new dialog cell editor parented under the given control.
	 *
	 * @param parent    the parent control
	 * @param unitNames the name list
	 * @param style     the style bits
	 */
	public DimensionCellEditor(Composite parent, String[] unitNames, int style) {
		super(parent, style);
		this.units = unitNames;
		this.style = style;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.jface.viewers.DialogCellEditor#openDialogBox(org.eclipse.
	 * swt.widgets.Control)
	 */
	@Override
	protected Object openDialogBox(Control cellEditorWindow) {
		DimensionBuilderDialog dialog = new DimensionBuilderDialog(cellEditorWindow.getShell());

		DimensionValue value;
		try {
			value = StringUtil.parseInput((String) this.getDefaultText().getText(), ULocale.getDefault());
		} catch (PropertyValueException e) {
			value = null;
		}

		dialog.setUnitNames(units);
		dialog.setUnitName(unitName);

		if (value != null) {
			dialog.setMeasureData(new Double(value.getMeasure()));
		}

		inProcessing = 1;
		if (dialog.open() == Window.OK) {
			deactivate();
			inProcessing = 0;

			String newValue = null;
			Double doubleValue = 0.0;
			if (dialog.getMeasureData() instanceof Double) {
				doubleValue = (Double) dialog.getMeasureData();
			} else if (dialog.getMeasureData() instanceof DimensionValue) {
				doubleValue = ((DimensionValue) dialog.getMeasureData()).getMeasure();
			}
			DimensionValue dValue = new DimensionValue(doubleValue, dialog.getUnitName());

			if (dValue != null) {
				newValue = dValue.toDisplayString();
			}
			return newValue;
		} else {
			getDefaultText().setFocus();
			getDefaultText().selectAll();
		}
		inProcessing = 0;
		return null;

	}

	/**
	 * Set current units
	 *
	 * @param units
	 */
	public void setUnits(String units) {
		this.unitName = units;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.jface.viewers.CellEditor#doSetFocus()
	 */
	@Override
	protected void doSetFocus() {
		getDefaultText().setFocus();
		getDefaultText().selectAll();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @seeorg.eclipse.birt.report.designer.internal.ui.views.property.widgets.
	 * CDialogCellEditor#doValueChanged()
	 */
	@Override
	protected void doValueChanged() {
		Object oldValue = doGetValue();
		if (oldValue instanceof String) {
			oldValue = parseInputString2dValue((String) oldValue);
		}
		String newValue = textEditor.getText();
		DimensionValue dValue = parseInputString2dValue(newValue);
		if (dValue != null) {
			newValue = dValue.toDisplayString();
		}

		if (dValue != null && (!dValue.equals(oldValue))) {
			markDirty();
			doSetValue(newValue);
		} else if ((dValue == null && oldValue != null)) {
			markDirty();
			doSetValue(null);
		}
	}

	private DimensionValue parseInputString2dValue(String strValue) {
		DimensionValue dValue = null;
		try {
			dValue = StringUtil.parseInput((String) strValue, ULocale.getDefault());
		} catch (PropertyValueException e) {
			dValue = null;
		}

		return dValue;
	}

	/**
	 * Creates the controls used to show the value of this cell editor.
	 * <p>
	 * The default implementation of this framework method creates a label widget,
	 * using the same font and background color as the parent control.
	 * </p>
	 * <p>
	 * Subclasses may reimplement. If you reimplement this method, you should also
	 * reimplement <code>updateContents</code>.
	 * </p>
	 *
	 * @param cell the control for this cell editor
	 */
	@Override
	protected Control createContents(Composite cell) {
		textEditor = new Text(cell, SWT.LEFT | style);
		textEditor.setFont(cell.getFont());
		textEditor.setBackground(cell.getBackground());

		textEditor.addKeyListener(new KeyAdapter() {

			// hook key pressed - see PR 14201
			@Override
			public void keyPressed(KeyEvent e) {
				keyReleaseOccured(e);
			}
		});

		textEditor.addTraverseListener(new TraverseListener() {

			@Override
			public void keyTraversed(TraverseEvent e) {
				if (e.detail == SWT.TRAVERSE_ESCAPE || e.detail == SWT.TRAVERSE_RETURN) {
					e.doit = false;
				}
			}
		});

		textEditor.addFocusListener(new FocusAdapter() {

			@Override
			public void focusLost(FocusEvent e) {
				if (textEditor != null && !textEditor.isDisposed()) {
					DimensionCellEditor.this.focusLost();
				}
			}

			@Override
			public void focusGained(FocusEvent e) {
				handleFocus(SWT.FocusIn);
			}
		});

		return textEditor;
	}

	void handleFocus(int type) {
		switch (type) {
		case SWT.FocusIn: {
			if (hasFocus) {
				return;
			}
			textEditor.selectAll();
			hasFocus = true;
			Display display = textEditor.getDisplay();
			display.removeFilter(SWT.FocusIn, filter);
			display.addFilter(SWT.FocusIn, filter);
			Event e = new Event();
			textEditor.notifyListeners(SWT.FocusIn, e);
			break;
		}
		case SWT.FocusOut: {
			if (!hasFocus) {
				return;
			}
			Control focusControl = textEditor.getDisplay().getFocusControl();
			if (focusControl == textEditor) {
				return;
			}
			hasFocus = false;
			Display display = textEditor.getDisplay();
			display.removeFilter(SWT.FocusIn, filter);
			Event e = new Event();
			textEditor.notifyListeners(SWT.FocusOut, e);
			break;
		}
		}
	}

	/**
	 * Updates the controls showing the value of this cell editor.
	 * <p>
	 * The default implementation of this framework method just converts the passed
	 * object to a string using <code>toString</code> and sets this as the text of
	 * the label widget.
	 * </p>
	 * <p>
	 * Subclasses may reimplement. If you reimplement this method, you should also
	 * reimplement <code>createContents</code>.
	 * </p>
	 *
	 * @param value the new value of this cell editor
	 */
	@Override
	protected void updateContents(Object value) {
		if (textEditor == null) {
			return;
		}

		String text = "";//$NON-NLS-1$
		if (value != null) {
			if (value instanceof String) {
				DimensionValue dValue;
				try {
					dValue = StringUtil.parseInput((String) value, ULocale.getDefault());
				} catch (PropertyValueException e) {
					dValue = null;
				}

				if (dValue == null) {
					return;
				}
				text = NumberUtil.double2LocaleNum(dValue.getMeasure()) + dValue.getUnits();
			} else {
				text = value.toString();
			}
		}
		textEditor.setText(text);
	}

	/**
	 * Returns the default label widget created by <code>createContents</code>.
	 *
	 * @return the default label widget
	 */
	protected Text getDefaultText() {
		return textEditor;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.jface.viewers.CellEditor#keyReleaseOccured(org.eclipse.swt
	 * .events.KeyEvent)
	 */
	@Override
	protected void keyReleaseOccured(KeyEvent keyEvent) {
		if (keyEvent.character == '\u001b') { // Escape character
			fireCancelEditor();
		} else if (keyEvent.character == '\t') { // tab key
			applyEditorValueAndDeactivate();
		} else if (keyEvent.character == '\r') { // Return key
			applyEditorValueAndDeactivate();
		}
	}

	/**
	 *
	 */
	private void applyEditorValueAndDeactivate() {
		inProcessing = 1;
		doValueChanged();
		fireApplyEditorValue();
		deactivate();
		inProcessing = 0;
	}

	/**
	 * Processes a focus lost event that occurred in this cell editor.
	 * <p>
	 * The default implementation of this framework method applies the current value
	 * and deactivates the cell editor. Subclasses should call this method at
	 * appropriate times. Subclasses may also extend or reimplement.
	 * </p>
	 */
	@Override
	protected void focusLost() {
		if (inProcessing == 1) {
			return;
		} else {
			// if click button, ignore focuslost event.
			Rectangle rect = getButton().getBounds();
			Point location = getButton().toDisplay(0, 0);
			rect.x = location.x;
			rect.y = location.y;
			Point cursorLocation = getButton().getDisplay().getCursorLocation();
			if (rect.contains(cursorLocation)) {
				return;
			}
		}
		super.focusLost();
	}
}
