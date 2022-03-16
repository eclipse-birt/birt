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

import java.text.ParseException;
import java.util.Date;
import java.util.Vector;

import org.eclipse.birt.chart.ui.extension.i18n.Messages;
import org.eclipse.birt.chart.ui.swt.AbstractChartTextEditor;
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
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;

import com.ibm.icu.text.SimpleDateFormat;

/**
 * This class is intended to be used in the ChartBuilder UI where direct changes
 * to the model are being made via Text Fields. It internally holds a regular
 * text field but only fires events when focus is lost IF the text has been
 * modified. It is intended to reduce the number changes made to the model to
 * make the UI more responsive and to reduce the number of times the Preview is
 * refreshed.
 *
 * @author Actuate Corporation
 */
public class TextEditorComposite extends AbstractChartTextEditor implements ModifyListener, FocusListener, KeyListener {

	public static final int TYPE_NONE = 0;
	public static final int TYPE_NUMBERIC = 1;
	public static final int TYPE_DATETIME = 2;

	private transient String sText = null;

	private transient boolean bTextModified = false;

	private transient int iStyle = SWT.NONE;

	protected transient Text txtValue = null;

	private transient Vector<Listener> vListeners = null;

	public static final int TEXT_MODIFIED = 0;

	public static final int TEXT_FRACTION_CONVERTED = 1;

	public static final String TEXT_RESET_MODEL = "Reset";//$NON-NLS-1$

	protected transient boolean bEnabled = true;

	private transient int valueType = TYPE_NONE;

	private transient String defaultValue = "0"; //$NON-NLS-1$

	/**
	 * Constructor. Default argument value of isFractionSupported is true.
	 *
	 * @param parent
	 * @param iStyle
	 */
	public TextEditorComposite(Composite parent, int iStyle) {
		this(parent, iStyle, TYPE_NONE);
	}

	/**
	 *
	 * @param parent
	 * @param iStyle
	 * @param isNumber If this argument is true, only number value is valid. The
	 *                 fraction value, like "1/3" also is supported as a double
	 *                 value.
	 */
	public TextEditorComposite(Composite parent, int iStyle, boolean isNumber) {
		this(parent, iStyle, isNumber ? TYPE_NUMBERIC : TYPE_NONE);
	}

	/**
	 *
	 * @param parent
	 * @param iStyle
	 * @param valueType Value type for validation, valid type is
	 *                  {@link #TYPE_DATETIME}, {@link #TYPE_NUMBERIC} or
	 *                  {@link #TYPE_NONE}
	 */
	public TextEditorComposite(Composite parent, int iStyle, int valueType) {
		super(parent, SWT.NONE);
		this.iStyle = iStyle;
		this.valueType = valueType;
		init();
		placeComponents();
		initAccessible();
	}

	private void init() {
		sText = ""; //$NON-NLS-1$
		vListeners = new Vector<>();
		this.setLayout(new FillLayout());
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
		createTextEdit();
	}

	protected void createTextEdit() {
		txtValue = new Text(this, iStyle);
		GridData gd = new GridData(GridData.FILL_BOTH);
		txtValue.setLayoutData(gd);
		if (valueType == TYPE_NUMBERIC) {
			txtValue.setToolTipText(Messages.getString("TextEditorComposite.Tooltip.EnterDecimalOrFractionValue")); //$NON-NLS-1$
		} else if (valueType == TYPE_DATETIME) {
			txtValue.setToolTipText("MM-dd-yyyy HH:mm:ss"); //$NON-NLS-1$
		}
		txtValue.addModifyListener(this);
		txtValue.addFocusListener(this);
		txtValue.addKeyListener(this);
	}

	public void addScreenreaderAccessbility(String description) {
		ChartUIUtil.addScreenReaderAccessbility(txtValue, description);
	}

	@Override
	public void setEnabled(boolean bState) {
		this.txtValue.setEnabled(bState);
		this.bEnabled = bState;
	}

	@Override
	public boolean isEnabled() {
		return this.bEnabled;
	}

	@Override
	public void setText(String sText) {
		txtValue.setText(sText);
	}

	@Override
	public String getText() {
		return txtValue.getText();
	}

	@Override
	public void setToolTipText(String string) {
		txtValue.setToolTipText(string);
	}

	@Override
	public void addListener(Listener listener) {
		vListeners.add(listener);
	}

	/**
	 * Sets the default value when current text is for numeric only. If the inputed
	 * text is not numeric, will use this default value.
	 *
	 * @param value default value
	 */
	@Override
	public void setDefaultValue(String value) {
		this.defaultValue = value;
	}

	protected void fireEvent() {
		boolean isFractionConverted = false;
		if (valueType == TYPE_NUMBERIC) {
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
					this.sText = String.valueOf(Double.parseDouble(numerator) / Double.parseDouble(denominator));
				} catch (NumberFormatException e) {
					this.sText = defaultValue == null ? "" : defaultValue; //$NON-NLS-1$
				}
				this.txtValue.setText(sText);
			} else {
				// Test if the text is a number format
				try {
					ChartUIUtil.getDefaultNumberFormatInstance().parse(this.sText).doubleValue();
				} catch (ParseException e) {
					this.sText = defaultValue == null ? "" : defaultValue; //$NON-NLS-1$
					this.txtValue.setText(this.sText);
				}
			}
		} else if (valueType == TYPE_DATETIME) {
			SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss"); //$NON-NLS-1$
			try {
				sdf.parse(this.sText);
			} catch (ParseException e) {
				if (defaultValue == null) {
					Date today = new Date();
					this.sText = sdf.format(today);
				} else {
					this.sText = defaultValue;
				}
				this.txtValue.setText(this.sText);
			}
		}

		Event e = new Event();
		e.data = this.sText;
		e.widget = this;
		e.type = TEXT_MODIFIED;
		notifyListeners(e.type, e);

		if (isFractionConverted) {
			e = new Event();
			e.data = this.sText;
			e.widget = this;
			e.type = TEXT_FRACTION_CONVERTED;
			notifyListeners(e.type, e);
		}
	}

	@Override
	public void notifyListeners(int eventType, Event event) {
		if (event == null) {
			event = new Event();
		}
		if (!TEXT_RESET_MODEL.equals(event.data)) {
			event.data = this.sText;
		}
		event.widget = this;
		event.type = eventType;
		for (int i = 0; i < vListeners.size(); i++) {
			vListeners.get(i).handleEvent(event);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.swt.events.ModifyListener#modifyText(org.eclipse.swt.events.
	 * ModifyEvent)
	 */
	@Override
	public void modifyText(ModifyEvent e) {
		this.bTextModified = true;
		this.sText = txtValue.getText();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.swt.events.FocusListener#focusGained(org.eclipse.swt.events.
	 * FocusEvent)
	 */
	@Override
	public void focusGained(FocusEvent e) {
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.swt.events.FocusListener#focusLost(org.eclipse.swt.events.
	 * FocusEvent)
	 */
	@Override
	public void focusLost(FocusEvent e) {
		if (bTextModified) {
			fireEvent();
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.keyCode == SWT.CR || e.keyCode == SWT.KEYPAD_CR) {
			if (bTextModified) {
				fireEvent();
			}
		} else if (e.keyCode == SWT.ARROW_DOWN || e.keyCode == SWT.ARROW_UP) {
			Event event = new Event();
			event.keyCode = e.keyCode;
			event.stateMask = e.stateMask;
			this.notifyListeners(SWT.KeyDown, event);
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

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
				e.result = getText();
			}
		});

		ChartUIUtil.addScreenReaderAccessibility(this, txtValue);
	}

	/**
	 * Returns text control.
	 *
	 * @return actual text widget.
	 */
	@Override
	public Text getTextControl() {
		return txtValue;
	}

	@Override
	public void setEObjectParent(EObject eParent) {
		// TODO Auto-generated method stub

	}
}
