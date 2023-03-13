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
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

/**
 * @author Actuate Corporation
 *
 */
public class IntegerSpinControl extends AbstractChartIntSpinner implements SelectionListener, Listener {

	private transient int iSize = 16;

	private transient int iMinValue = 0;

	private transient int iMaxValue = 100;

	protected transient int iCurrentValue = 0;

	private transient int iIncrement = 1;

	protected transient Composite cmpContentOuter = null;

	private transient Composite cmpContentInner = null;

	private transient Composite cmpBtnContainer = null;

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
			GC gc = new GC(this);
			iSize = gc.getFontMetrics().getHeight();
		}
		this.setSize(getParent().getClientArea().width, getParent().getClientArea().height);
		vListeners = new Vector<>();
	}

	/**
	 *
	 */
	protected void placeComponents() {
		FillLayout fl = new FillLayout();
		fl.marginHeight = 0;
		fl.marginWidth = 0;
		setLayout(fl);

		// THE LAYOUT OF THE OUTER COMPOSITE (THAT GROWS VERTICALLY BUT ANCHORS
		// ITS CONTENT NORTH)
		cmpContentOuter = new Composite(this, SWT.NONE);
		GridLayout gl = new GridLayout();
		gl.verticalSpacing = 0;
		gl.horizontalSpacing = 0;
		gl.marginHeight = 0;
		gl.marginWidth = 0;
		gl.numColumns = 1;
		cmpContentOuter.setLayout(gl);

		creaetSpinner(cmpContentOuter);
	}

	protected void creaetSpinner(Composite parent) {
		// THE LAYOUT OF THE INNER COMPOSITE (ANCHORED NORTH AND ENCAPSULATES
		// THE CANVAS + BUTTON)
		cmpContentInner = new Composite(parent, SWT.NONE);
		GridLayout gl = new GridLayout();
		gl.verticalSpacing = 0;
		gl.horizontalSpacing = 0;
		gl.marginHeight = 0;
		gl.marginWidth = 0;
		gl.numColumns = 2;
		cmpContentInner.setLayout(gl);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		cmpContentInner.setLayoutData(gd);

		txtValue = new TextEditorComposite(cmpContentInner, SWT.BORDER);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.grabExcessHorizontalSpace = true;
		gd.verticalAlignment = GridData.BEGINNING;
		gd.heightHint = iSize + 8;
		gd.minimumWidth = 30;
		txtValue.setLayoutData(gd);
		txtValue.setText(String.valueOf(iCurrentValue));
		txtValue.addListener(this);

		cmpBtnContainer = new Composite(cmpContentInner, SWT.NONE);
		gd = new GridData();
		gd.verticalAlignment = GridData.BEGINNING;
		gd.horizontalAlignment = SWT.END;
		cmpBtnContainer.setLayoutData(gd);
		cmpBtnContainer.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_RED));
		gl = new GridLayout();
		gl.horizontalSpacing = 0;
		gl.verticalSpacing = 0;
		gl.marginHeight = 0;
		gl.marginWidth = 0;
		cmpBtnContainer.setLayout(gl);

		final int iHalf = (iSize + 8) / 2;
		btnIncrement = new Button(cmpBtnContainer, SWT.ARROW | SWT.UP);
		gd = new GridData();
		gd.grabExcessVerticalSpace = true;
		gd.grabExcessHorizontalSpace = true;
		gd.heightHint = iHalf;
		gd.widthHint = iHalf;
		btnIncrement.setLayoutData(gd);
		btnIncrement.addSelectionListener(this);

		btnDecrement = new Button(cmpBtnContainer, SWT.ARROW | SWT.DOWN);
		gd = new GridData();
		gd.grabExcessVerticalSpace = true;
		gd.grabExcessHorizontalSpace = true;
		gd.heightHint = iHalf;
		gd.widthHint = iHalf;
		btnDecrement.setLayoutData(gd);
		btnDecrement.addSelectionListener(this);
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
