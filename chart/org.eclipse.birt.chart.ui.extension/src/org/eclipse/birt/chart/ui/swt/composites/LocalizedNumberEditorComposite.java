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

package org.eclipse.birt.chart.ui.swt.composites;

import java.text.ParseException;
import java.util.Vector;

import org.eclipse.birt.chart.ui.extension.i18n.Messages;
import org.eclipse.birt.chart.ui.swt.AbstractChartNumberEditor;
import org.eclipse.birt.chart.ui.util.ChartUIUtil;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.swt.SWT;
import org.eclipse.swt.accessibility.ACC;
import org.eclipse.swt.accessibility.AccessibleAdapter;
import org.eclipse.swt.accessibility.AccessibleControlAdapter;
import org.eclipse.swt.accessibility.AccessibleControlEvent;
import org.eclipse.swt.accessibility.AccessibleEvent;
import org.eclipse.swt.accessibility.AccessibleTextAdapter;
import org.eclipse.swt.accessibility.AccessibleTextEvent;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Text;

import com.ibm.icu.text.MessageFormat;
import com.ibm.icu.text.NumberFormat;

/**
 * LocalizedNumberEditorComposite
 */
public class LocalizedNumberEditorComposite extends AbstractChartNumberEditor
		implements ModifyListener, KeyListener, FocusListener {

	public static final int TEXT_MODIFIED = TextEditorComposite.TEXT_MODIFIED;

	public static final int TEXT_FRACTION_CONVERTED = TextEditorComposite.TEXT_FRACTION_CONVERTED;

	protected transient Text txtValue;

	protected transient Vector<ModifyListener> vModifyListeners;

	private transient Vector<Listener> vFractionListeners;

	private transient double dValue;

	private transient boolean bTextModified = false;

	private transient boolean bValueIsSet = false;

	private transient boolean bOriginalValueIsSet = false;

	protected transient boolean bEnabled = true;

	protected transient int iStyle = SWT.NONE;

	private transient NumberFormat numberFormat;

	protected String sUnit;

	protected Label lblUnit;

	/**
	 * Constructor.
	 *
	 * @param parent
	 * @param iStyle
	 */
	public LocalizedNumberEditorComposite(Composite parent, int iStyle) {
		this(parent, iStyle, null);
	}

	public LocalizedNumberEditorComposite(Composite parent, int iStyle, String unit) {
		super(parent, SWT.NONE);
		this.iStyle = iStyle;
		this.sUnit = unit;
		vModifyListeners = new Vector<>();
		vFractionListeners = new Vector<>();
		this.setLayout(new FillLayout());

		numberFormat = ChartUIUtil.getDefaultNumberFormatInstance();

		placeComponents();
		initAccessible();
	}

	protected void placeComponents() {
		GridLayout gl = new GridLayout(1, false);
		gl.marginBottom = 0;
		gl.marginHeight = 0;
		gl.marginLeft = 0;
		gl.marginRight = 0;
		gl.marginTop = 0;
		gl.marginWidth = 0;
		this.setLayout(gl);
		if (sUnit != null) {
			gl.numColumns = 2;
		}
		txtValue = new Text(this, iStyle);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		txtValue.setLayoutData(gd);
		txtValue.setToolTipText(Messages.getString("TextEditorComposite.Tooltip.EnterDecimalOrFractionValue")); //$NON-NLS-1$
		txtValue.addModifyListener(this);
		txtValue.addFocusListener(this);
		txtValue.addKeyListener(this);

		if (sUnit != null) {
			this.lblUnit = new Label(this, SWT.NONE);
			if (lblUnit != null) {
				lblUnit.setText(sUnit);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.swt.widgets.Control#setEnabled(boolean)
	 */
	@Override
	public void setEnabled(boolean bState) {
		bEnabled = bState;
		txtValue.setEnabled(bState);
		if (lblUnit != null) {
			lblUnit.setEnabled(bState);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.swt.widgets.Control#isEnabled()
	 */
	@Override
	public boolean isEnabled() {
		return bEnabled;
	}

	@Override
	public boolean isSetValue() {
		return bValueIsSet;
	}

	@Override
	public void unsetValue() {
		bValueIsSet = false;
		txtValue.setText(""); //$NON-NLS-1$
	}

	@Override
	public void setValue(double value) {
		bOriginalValueIsSet = true;
		bValueIsSet = true;
		dValue = value;
		txtValue.setText(numberFormat.format(value));
	}

	@Override
	public double getValue() {
		return dValue;
	}

	@Override
	public void setToolTipText(String string) {
		txtValue.setToolTipText(string);
	}

	@Override
	public void addModifyListener(ModifyListener listener) {
		vModifyListeners.add(listener);
	}

	public void removeModifyListener(ModifyListener listener) {
		vModifyListeners.remove(listener);
	}

	@Override
	public void addFractionListener(Listener listener) {
		vFractionListeners.add(listener);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.swt.events.ModifyListener#modifyText(org.eclipse.swt.events
	 * .ModifyEvent)
	 */
	@Override
	public void modifyText(ModifyEvent e) {
		this.bTextModified = true;
		fireEvent(true);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.swt.events.FocusListener#focusGained(org.eclipse.swt.events
	 * .FocusEvent)
	 */
	@Override
	public void focusGained(FocusEvent e) {
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.swt.events.FocusListener#focusLost(org.eclipse.swt.events
	 * .FocusEvent)
	 */
	@Override
	public void focusLost(FocusEvent e) {
		if (bTextModified) {
			bTextModified = false;
			fireEvent();
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.swt.events.KeyListener#keyPressed(org.eclipse.swt.events.
	 * KeyEvent)
	 */
	@Override
	public void keyPressed(KeyEvent e) {
		if (e.keyCode == SWT.CR || e.keyCode == SWT.KEYPAD_CR) {
			if (bTextModified) {
				bTextModified = false;
				fireEvent();
			}
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.swt.events.KeyListener#keyReleased(org.eclipse.swt.events
	 * .KeyEvent)
	 */
	@Override
	public void keyReleased(KeyEvent e) {
	}

	private void handleFormatError(String value) {
		MessageBox mbox = new MessageBox(getShell(), SWT.ICON_WARNING | SWT.OK);
		mbox.setText(Messages.getString("LocalizedNumberEditorComposite.error.Title")); //$NON-NLS-1$
		mbox.setMessage(MessageFormat.format(Messages.getString("LocalizedNumberEditorComposite.error.Message"), //$NON-NLS-1$
				new Object[] { value }));
		mbox.open();

		if (bOriginalValueIsSet) {
			txtValue.setText(String.valueOf((int) dValue));
		} else {
			txtValue.setText(""); //$NON-NLS-1$
		}
	}

	protected void fireEvent() {
		fireEvent(false);
	}

	protected void fireEvent(boolean bByModifyText) {
		boolean isFractionConverted = false;

		String sText = txtValue.getText();

		if (sText == null || sText.trim().length() == 0) {
			bValueIsSet = false;
			dValue = 0d;
		} else {
			int iDelimiter = sText.indexOf('/');
			if (iDelimiter < 0) {
				iDelimiter = sText.indexOf(':');
			}
			if (iDelimiter > 0) {
				// Handle the fraction conversion
				isFractionConverted = true;
				String numerator = sText.substring(0, iDelimiter);
				String denominator = sText.substring(iDelimiter + 1);
				try {
					Number nume = numberFormat.parse(numerator);
					Number deno = numberFormat.parse(denominator);
					dValue = nume.doubleValue() / deno.doubleValue();
					bValueIsSet = true;
					sText = numberFormat.format(dValue);

					if (!bByModifyText) {
						this.txtValue.setText(sText);
					}
				} catch (ParseException e) {
					if (!this.bTextModified) {
						handleFormatError(sText);
					}
				}
			} else {
				try {
					Number num = numberFormat.parse(sText);
					dValue = num.doubleValue();
					bValueIsSet = true;
					sText = numberFormat.format(dValue);
				} catch (ParseException e) {
					if (!this.bTextModified) {
						handleFormatError(sText);
					}
				}
			}
		}

		for (int i = 0; i < vModifyListeners.size(); i++) {
			Event e = new Event();
			e.data = bByModifyText ? Boolean.FALSE : Boolean.TRUE;
			e.widget = this;
			e.type = TEXT_MODIFIED;
			vModifyListeners.get(i).modifyText(new ModifyEvent(e));
		}

		if (isFractionConverted) {
			for (int i = 0; i < vFractionListeners.size(); i++) {
				Event e = new Event();
				e.data = sText;
				e.widget = this;
				e.type = TEXT_FRACTION_CONVERTED;
				vFractionListeners.get(i).handleEvent(e);
			}
		}
	}

	void initAccessible() {
		getAccessible().addAccessibleListener(new AccessibleAdapter() {

			@Override
			public void getHelp(AccessibleEvent e) {
				e.result = getToolTipText();
			}
		});

		getAccessible().addAccessibleTextListener(new AccessibleTextAdapter() {

			@Override
			public void getCaretOffset(AccessibleTextEvent e) {
				e.offset = txtValue.getCaretPosition();
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
				e.detail = ACC.ROLE_TEXT;
			}

			@Override
			public void getState(AccessibleControlEvent e) {
				e.detail = ACC.STATE_NORMAL;
			}

			@Override
			public void getValue(AccessibleControlEvent e) {
				e.result = txtValue.getText();
			}
		});

		// Set screen reader text.
		ChartUIUtil.addScreenReaderAccessibility(this, txtValue);
	}

	@Override
	public Text getTextControl() {
		return txtValue;
	}

	@Override
	public void setEObjectParent(EObject eParent) {
		// TODO Auto-generated method stub

	}

	@Override
	public Label getUnitLabel() {
		return lblUnit;
	}
}
