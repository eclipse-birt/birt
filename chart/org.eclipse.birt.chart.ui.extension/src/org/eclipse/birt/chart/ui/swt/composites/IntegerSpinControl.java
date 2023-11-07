/***********************************************************************
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
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.chart.ui.swt.composites;

import java.util.Vector;

import org.eclipse.birt.chart.ui.swt.AbstractChartIntSpinner;
import org.eclipse.birt.chart.ui.util.ChartUIExtensionUtil;
import org.eclipse.swt.SWT;
import org.eclipse.swt.accessibility.ACC;
import org.eclipse.swt.accessibility.AccessibleAdapter;
import org.eclipse.swt.accessibility.AccessibleControlAdapter;
import org.eclipse.swt.accessibility.AccessibleControlEvent;
import org.eclipse.swt.accessibility.AccessibleEvent;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Listener;

/**
 * @author Actuate Corporation
 *
 */
public class IntegerSpinControl extends AbstractChartIntSpinner implements SelectionListener, Listener {

	private transient int iMinValue = 0;

	private transient int iMaxValue = 100;

	protected transient int iCurrentValue = 0;

	private transient int iIncrement = 1;

	protected transient Button btnIncrement = null;

	protected transient Button btnDecrement = null;

	protected transient TextEditorComposite txtValue = null;

	protected transient Vector<Listener> vListeners = null;

	public static final int VALUE_CHANGED_EVENT = 1;

	protected transient boolean bEnabled = true;

	/**
	 * @param parent
	 * @param style
	 */
	public IntegerSpinControl(Composite parent, int style, int iCurrentValue) {
		super(parent, style);
		this.iCurrentValue = iCurrentValue;
		init();
		placeComponents();
		initAccessible();
	}

	public void addScreenreaderAccessbility(String description) {
		txtValue.addScreenreaderAccessbility(description);
	}

	/**
	 *
	 */
	private void init() {
		if (Display.getCurrent().getHighContrast()) {
			// GC gc = new GC(this);
			// iSize = gc.getFontMetrics().getHeight();
		}
//		this.setSize(getParent().getClientArea().width, getParent().getClientArea().height);
		vListeners = new Vector<>();
	}

	/**
	 *
	 */
	protected void placeComponents() {
		creaetSpinner(this);
	}

	protected void creaetSpinner(Composite parent) {
		// THE LAYOUT OF THE INNER COMPOSITE (ANCHORED NORTH AND ENCAPSULATES
		// THE CANVAS + BUTTON)

		setLayout(new InternalLayout());

		txtValue = new TextEditorComposite(this, SWT.BORDER);
		txtValue.setText(String.valueOf(iCurrentValue));
		txtValue.addListener(this);

		btnIncrement = new Button(this, SWT.ARROW | SWT.RIGHT);
		btnIncrement.addSelectionListener(this);

		btnDecrement = new Button(this, SWT.ARROW | SWT.LEFT);
		btnDecrement.addSelectionListener(this);
	}

	private class InternalLayout extends Layout {
		@Override
		public Point computeSize(Composite editor, int wHint, int hHint, boolean force) {
			if (wHint != SWT.DEFAULT && hHint != SWT.DEFAULT) {
				return new Point(wHint, hHint);
			}
			Point textBoxSize = txtValue.computeSize(SWT.DEFAULT, SWT.DEFAULT, force);
			Point incButtonSize = btnIncrement.computeSize(SWT.DEFAULT, SWT.DEFAULT, force);
			Point decButtonSize = btnDecrement.computeSize(SWT.DEFAULT, SWT.DEFAULT, force);

			return new Point(textBoxSize.x + incButtonSize.x + decButtonSize.x,
					Math.max(textBoxSize.y, incButtonSize.y));
		}

		@Override
		public void layout(Composite editor, boolean force) {
			Rectangle bounds = editor.getClientArea();

			Point textBoxSize = txtValue.computeSize(SWT.DEFAULT, SWT.DEFAULT, force);
			Point incButtonSize = btnIncrement.computeSize(SWT.DEFAULT, SWT.DEFAULT, force);
			Point decButtonSize = btnDecrement.computeSize(SWT.DEFAULT, SWT.DEFAULT, force);

			int maxButtonWidth = Math.max(incButtonSize.x, decButtonSize.x);

			btnDecrement.setBounds(0, 0, maxButtonWidth, bounds.height);
			txtValue.setBounds(maxButtonWidth, 0, bounds.width - maxButtonWidth * 2, bounds.height);
			btnIncrement.setBounds(bounds.width - maxButtonWidth, 0, maxButtonWidth,
					bounds.height);
		}
	}

	@Override
	public void setMinimum(int iMin) {
		this.iMinValue = iMin;
	}

	public int getMinimum() {
		return this.iMinValue;
	}

	@Override
	public void setMaximum(int iMax) {
		this.iMaxValue = iMax;
	}

	public int getMaximum() {
		return this.iMaxValue;
	}

	@Override
	public void setIncrement(int iIncrement) {
		this.iIncrement = iIncrement;
	}

	@Override
	public void setValue(int iCurrent) {
		this.iCurrentValue = iCurrent;
		this.txtValue.setText(String.valueOf(iCurrentValue));
	}

	@Override
	public int getValue() {
		return this.iCurrentValue;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.swt.widgets.Control#setEnabled(boolean)
	 */
	@Override
	public void setEnabled(boolean bState) {
		super.setEnabled(bState);
		setEnabledImpl(bState);
		this.bEnabled = bState;
	}

	protected void setEnabledImpl(boolean bState) {
		this.btnIncrement.setEnabled(bState);
		this.btnDecrement.setEnabled(bState);
		this.txtValue.setEnabled(bState);
	}

	@Override
	public boolean isEnabled() {
		return this.bEnabled;
	}

	@Override
	public boolean isSpinnerEnabled() {
		return isEnabled();
	}

	@Override
	public void addListener(Listener listener) {
		vListeners.add(listener);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.swt.events.SelectionListener#widgetSelected(org.eclipse.swt.
	 * events.SelectionEvent)
	 */
	@Override
	public void widgetSelected(SelectionEvent e) {
		int iTextValue = iCurrentValue;
		try {
			iTextValue = Integer.parseInt(txtValue.getText());
		} catch (NumberFormatException e1) {
			return;
		}

		Object oSource = e.getSource();
		if (oSource.equals(btnIncrement)) {
			if (iCurrentValue < iMaxValue && iTextValue >= iMinValue && iTextValue < iMaxValue) {
				iCurrentValue = (iTextValue + iIncrement);
				txtValue.setText(String.valueOf(iCurrentValue));
			} else if (iTextValue < iMinValue) {
				iCurrentValue = (iMinValue);
				txtValue.setText(String.valueOf(iMinValue));
			}
		} else if (oSource.equals(btnDecrement)) {
			if (iCurrentValue > iMinValue && iTextValue > iMinValue && iTextValue <= iMaxValue) {
				iCurrentValue = (iTextValue - iIncrement);
				txtValue.setText(String.valueOf(iCurrentValue));
			} else if (iCurrentValue > iMaxValue) {
				iCurrentValue = (iMaxValue);
				txtValue.setText(String.valueOf(iMaxValue));
			}
		}
		// Notify Listeners that a change has occurred in the value
		fireValueChangedEvent();
	}

	protected void fireValueChangedEvent() {
		for (int iL = 0; iL < vListeners.size(); iL++) {
			Event se = new Event();
			se.widget = this;
			se.data = Integer.valueOf(iCurrentValue);
			se.type = IntegerSpinControl.VALUE_CHANGED_EVENT;
			se.detail = ChartUIExtensionUtil.PROPERTY_UPDATE;
			vListeners.get(iL).handleEvent(se);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.swt.events.SelectionListener#widgetDefaultSelected(org.eclipse.
	 * swt.events.SelectionEvent)
	 */
	@Override
	public void widgetDefaultSelected(SelectionEvent e) {
	}

	public Point getPreferredSize() {
		return new Point(80, 24);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.swt.widgets.Listener#handleEvent(org.eclipse.swt.widgets.Event)
	 */
	@Override
	public void handleEvent(Event event) {
		if (event.type == TextEditorComposite.TEXT_MODIFIED) {
			try {
				int iValue = (Integer.parseInt(txtValue.getText()));
				if (iValue >= iMinValue && iValue <= iMaxValue) {
					iCurrentValue = iValue;
					fireValueChangedEvent();
				} else {
					// Rollback the invalid value
					txtValue.setText(String.valueOf(iCurrentValue));
				}
			} catch (NumberFormatException e1) {
				// Rollback the invalid value
				txtValue.setText(String.valueOf(iCurrentValue));
			}
		} else if (event.type == SWT.KeyDown) {
			int iValue = iCurrentValue;
			if (event.keyCode == SWT.ARROW_UP) {

				if (event.stateMask == SWT.CTRL) {
					iValue = iValue + iIncrement * 10;
				} else {
					iValue = iValue + iIncrement;
				}
			} else if (event.keyCode == SWT.ARROW_DOWN) {
				if (event.stateMask == SWT.CTRL) {
					iValue = iValue - iIncrement * 10;
				} else {
					iValue = iValue - iIncrement;
				}
			}
			if (iValue < iMinValue) {
				iValue = iMinValue;
			} else if (iValue > iMaxValue) {
				iValue = iMaxValue;
			}
			iCurrentValue = iValue;
			txtValue.setText(String.valueOf(iCurrentValue));
			fireValueChangedEvent();
		}

	}

	@Override
	public void setToolTipText(String string) {
		txtValue.setToolTipText(string);
	}

	void initAccessible() {
		getAccessible().addAccessibleListener(new AccessibleAdapter() {
			@Override
			public void getHelp(AccessibleEvent e) {
				e.result = getToolTipText();
			}
		});

		getAccessible().addAccessibleControlListener(new AccessibleControlAdapter() {
			@Override
			public void getChildAtPoint(AccessibleControlEvent e) {
				Point testPoint = toControl(new Point(e.x, e.y));
				if (getBounds().contains(testPoint)) {
					e.childID = ACC.CHILDID_SELF;
				}
			}

			@Override
			public void getLocation(AccessibleControlEvent e) {
				Rectangle location = getBounds();
				Point pt = toDisplay(new Point(location.x, location.y));
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
}
