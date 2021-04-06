/*******************************************************************************
 * Copyright (c) 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.designer.internal.ui.views.attributes.widget;

import java.util.Arrays;

import org.eclipse.birt.report.designer.internal.ui.swt.custom.ColorSelector;
import org.eclipse.birt.report.designer.internal.ui.swt.custom.FormWidgetFactory;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.WidgetUtil;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.views.attributes.providers.ChoiceSetFactory;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.metadata.IChoice;
import org.eclipse.birt.report.model.api.metadata.IChoiceSet;
import org.eclipse.birt.report.model.api.util.ColorUtil;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.accessibility.ACC;
import org.eclipse.swt.accessibility.AccessibleAdapter;
import org.eclipse.swt.accessibility.AccessibleControlAdapter;
import org.eclipse.swt.accessibility.AccessibleControlEvent;
import org.eclipse.swt.accessibility.AccessibleEvent;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;

import com.ibm.icu.util.StringTokenizer;

/**
 * A color Builder Composed of a ColorSelector Button and a Combo box.
 */
public class ColorBuilder extends Composite {

	protected CCombo combo;
	protected ColorSelector colorSelector;
	private IChoiceSet choiceSet;
	private RGB oldRgb;
	private String predefinedColor;

	private static final String NONE_CHOICE = Messages.getString("ColorBuilder.text.Auto"); //$NON-NLS-1$

	/**
	 * The constructor.
	 * 
	 * @param parent a widget which will be the parent of the new instance (cannot
	 *               be null)
	 * @param style  the style of widget to construct
	 */

	public ColorBuilder(Composite parent, int style) {
		super(parent, style);
		initColorBuilder(parent, style, false);
	}

	public ColorBuilder(Composite parent, int style, boolean isFormStyle) {
		super(parent, style);
		initColorBuilder(parent, style, isFormStyle);

	}

	private void initColorBuilder(Composite parent, int style, boolean isFormStyle) {
		setLayout(WidgetUtil.createSpaceGridLayout(2, 1));
		if (isFormStyle)
			((GridLayout) getLayout()).horizontalSpacing = 3;

		colorSelector = new ColorSelector(this);
		GridData data = new GridData();
		// data.widthHint = 50;
		data.horizontalAlignment = GridData.HORIZONTAL_ALIGN_BEGINNING;
		colorSelector.getButton().setLayoutData(data);

		colorSelector.getButton().setToolTipText(Messages.getString("ColorBuilder.Button.ChooseColor")); //$NON-NLS-1$
		colorSelector.addListener(new IPropertyChangeListener() {

			public void propertyChange(PropertyChangeEvent event) {
				predefinedColor = null;
				processAction(colorSelector.getColorValue());
			}
		});

		if (isFormStyle)
			combo = FormWidgetFactory.getInstance().createCCombo(this, false);
		else {
			combo = new CCombo(this, SWT.DROP_DOWN);
			combo.setVisibleItemCount(30);
		}
		data = new GridData();
		data.horizontalAlignment = GridData.FILL;
		data.grabExcessHorizontalSpace = true;
		// data.widthHint = 80;
		combo.setLayoutData(data);

		combo.add(NONE_CHOICE);
		combo.addFocusListener(new FocusListener() {

			public void focusGained(org.eclipse.swt.events.FocusEvent e) {
				handleComboFocusGainedEvent(e);
			}

			public void focusLost(org.eclipse.swt.events.FocusEvent e) {
				handleComboFocusLostEvent(e);

			}

		});
		combo.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent e) {
				handleComboSelectedEvent(e);
			}

			public void widgetDefaultSelected(SelectionEvent e) {
				handleComboDefaultSelectedEvent(e);
			}
		});
		initAccessible();
	}

	protected void handleComboFocusLostEvent(FocusEvent e) {
		combo.notifyListeners(SWT.DefaultSelection, null);
	}

	protected void handleComboFocusGainedEvent(FocusEvent e) {
	}

	Label getAssociatedLabel() {
		Control[] siblings = getParent().getChildren();
		for (int i = 0; i < siblings.length; i++) {
			if (siblings[i] == this) {
				if (i > 0 && siblings[i - 1] instanceof Label) {
					return (Label) siblings[i - 1];
				}
			}
		}
		return null;
	}

	String stripMnemonic(String string) {
		int index = 0;
		int length = string.length();
		do {
			while ((index < length) && (string.charAt(index) != '&'))
				index++;
			if (++index >= length)
				return string;
			if (string.charAt(index) != '&') {
				return string.substring(0, index - 1) + string.substring(index, length);
			}
			index++;
		} while (index < length);
		return string;
	}

	char _findMnemonic(String string) {
		if (string == null)
			return '\0';
		int index = 0;
		int length = string.length();
		do {
			while (index < length && string.charAt(index) != '&')
				index++;
			if (++index >= length)
				return '\0';
			if (string.charAt(index) != '&')
				return Character.toLowerCase(string.charAt(index));
			index++;
		} while (index < length);
		return '\0';
	}

	void initAccessible() {
		AccessibleAdapter accessibleAdapter = new AccessibleAdapter() {

			public void getName(AccessibleEvent e) {
				String name = null;
				Label label = getAssociatedLabel();
				if (label != null) {
					name = stripMnemonic(label.getText());
				}
				e.result = name;
			}

			public void getKeyboardShortcut(AccessibleEvent e) {
				String shortcut = null;
				Label label = getAssociatedLabel();
				if (label != null) {
					String text = label.getText();
					if (text != null) {
						char mnemonic = _findMnemonic(text);
						if (mnemonic != '\0') {
							shortcut = "Alt+" + mnemonic; //$NON-NLS-1$
						}
					}
				}
				e.result = shortcut;
			}

			public void getHelp(AccessibleEvent e) {
				e.result = getToolTipText();
			}
		};
		getAccessible().addAccessibleListener(accessibleAdapter);

		combo.getAccessible().addAccessibleListener(new AccessibleAdapter() {

			public void getName(AccessibleEvent e) {
				String name = null;
				Label label = getAssociatedLabel();
				if (label != null) {
					name = stripMnemonic(label.getText());
				}
				e.result = name;
			}

			public void getKeyboardShortcut(AccessibleEvent e) {
				e.result = "Alt+Down Arrow"; //$NON-NLS-1$
			}

			public void getHelp(AccessibleEvent e) {
				e.result = getToolTipText();
			}
		});

		getAccessible().addAccessibleControlListener(new AccessibleControlAdapter() {

			public void getChildAtPoint(AccessibleControlEvent e) {
				Point testPoint = toControl(e.x, e.y);
				if (getBounds().contains(testPoint)) {
					e.childID = ACC.CHILDID_SELF;
				}
			}

			public void getLocation(AccessibleControlEvent e) {
				Rectangle location = getBounds();
				Point pt = toDisplay(location.x, location.y);
				e.x = pt.x;
				e.y = pt.y;
				e.width = location.width;
				e.height = location.height;
			}

			public void getChildCount(AccessibleControlEvent e) {
				e.detail = 0;
			}

			public void getRole(AccessibleControlEvent e) {
				e.detail = ACC.ROLE_COMBOBOX;
			}

			public void getState(AccessibleControlEvent e) {
				e.detail = ACC.STATE_NORMAL;
			}

			public void getValue(AccessibleControlEvent e) {
				e.result = combo.getText();
			}
		});

		combo.getAccessible().addAccessibleControlListener(new AccessibleControlAdapter() {

			public void getRole(AccessibleControlEvent e) {
				e.detail = ACC.ROLE_COMBOBOX;
				;
			}

			public void getValue(AccessibleControlEvent e) {
				e.result = combo.getText();
			}
		});

	}

	/**
	 * Sets the color choiceSet from DE model.
	 * 
	 * @param choiceSet The color ChoiceSet.
	 */
	public void setChoiceSet(IChoiceSet choiceSet) {
		this.choiceSet = choiceSet;
		String[] colors = ChoiceSetFactory.getDisplayNamefromChoiceSet(choiceSet);
		Arrays.sort(colors);
		combo.removeAll();
		combo.add(NONE_CHOICE);
		if (colors != null) {
			for (int i = 0; i < colors.length; i++) {
				combo.add(colors[i]);
			}
		}
	}

	/**
	 * Parses the input string to a GRB object.
	 * 
	 * @param string The input string.
	 * @return The RGB object represented the string.
	 */
	protected RGB parseString(String string) {
		int colors[] = ColorUtil.getRGBs(string);
		if (colors != null)
			return new RGB(colors[0], colors[1], colors[2]);

		StringTokenizer st = new StringTokenizer(string, " ,()");//$NON-NLS-1$
		if (!st.hasMoreTokens())
			return null;
		int[] rgb = new int[] { 0, 0, 0 };
		int index = 0;
		while (st.hasMoreTokens()) {
			try {
				rgb[index] = Integer.decode(st.nextToken()).intValue();
				if (rgb[index] < 0 || rgb[index] > 255)
					return null;
				index++;
			} catch (Exception e) {
				return null;
			}
		}
		return new RGB(rgb[0], rgb[1], rgb[2]);
	}

	/**
	 * Processes the save action.
	 * 
	 * @param rgb The new RGB value.
	 */
	protected void processAction(RGB rgb) {
		String newComboText = predefinedColor;
		if (newComboText == null) {
			newComboText = formatRGB(rgb);
		}

		if (!combo.getText().equals(newComboText)) {
			combo.setText(newComboText);
		}

		if (oldRgb == null && rgb == null) {
			notifyListeners(SWT.Modify, null);
			return;
		}

		if (rgb != null && rgb.equals(oldRgb)) {
			notifyListeners(SWT.Modify, null);
			return;
		}

		oldRgb = rgb;
		if (rgb == null || !rgb.equals(colorSelector.getColorValue())) {
			colorSelector.setColorValue(rgb);
		}
		notifyListeners(SWT.Modify, null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.widgets.Control#setEnabled(boolean)
	 */
	public void setEnabled(boolean enabled) {
		combo.setEnabled(enabled);
		colorSelector.setEnabled(enabled);
		super.setEnabled(enabled);
	}

	/**
	 * Gets the current RGB value
	 * 
	 * @return The current RGB value.
	 */
	public RGB getRGB() {
		return oldRgb;
	}

	public void setColorValue(String value) {
		predefinedColor = null;
		if (choiceSet != null) {
			IChoice choice = choiceSet.findChoice(value);
			if (choice != null) {
				predefinedColor = choice.getDisplayName();
			}
		}

		int[] rgbValues = ColorUtil.getRGBs(value);
		if (rgbValues == null) {
			setRGB(null);
		} else {
			setRGB(new RGB(rgbValues[0], rgbValues[1], rgbValues[2]));
		}
	}

	public void setRGB(RGB rgb) {
		oldRgb = rgb;
		if (combo.isDisposed()) {
			return;
		}

		colorSelector.setColorValue(rgb);

		String newComboText = predefinedColor;
		if (predefinedColor == null) {
			if (rgb == null) {
				newComboText = NONE_CHOICE;
			} else {
				newComboText = formatRGB(rgb);
			}
		}
		if (!combo.getText().equals(newComboText)) {
			combo.setText(newComboText);
		}
	}

	private String formatRGB(RGB rgb) {
		if (rgb == null) {
			return NONE_CHOICE;
		}

		String value = ColorUtil.getPredefinedColor(DEUtil.getRGBInt(rgb));
		if (value != null && combo != null) {
			String items[] = combo.getItems();
			for (int i = 0; i < items.length; i++) {
				if (items[i].equalsIgnoreCase(value)) {
					return items[i];
				}
			}

		}

		return ColorUtil.format(ColorUtil.formRGB(rgb.red, rgb.green, rgb.blue), ColorUtil.HTML_FORMAT);
	}

	/*
	 * public Point computeSize( int wHint, int hHint, boolean changed ) {
	 * checkWidget( );
	 * 
	 * int width = 0, height = 0;
	 * 
	 * GC gc = new GC( combo ); Point labelExtent = gc.textExtent(
	 * "RGB(255,255,255)" );//$NON-NLS-1$ gc.dispose( );
	 * 
	 * Point labelSize = combo.computeSize( labelExtent.x, labelExtent.y, changed );
	 * Point tableSize = colorSelector.getButton( ).computeSize( wHint, SWT.DEFAULT,
	 * changed ); int borderWidth = getBorderWidth( );
	 * 
	 * height = Math.max( hHint, Math.max( labelSize.y, tableSize.y ) + 2
	 * borderWidth ); width = Math.max( wHint, labelSize.x + tableSize.x + 2 *
	 * borderWidth );
	 * 
	 * return new Point( width, height ); }
	 */

	/**
	 * Returns the color in predefined format by model.
	 * 
	 * @return Returns the predefinedColor.
	 */
	public String getPredefinedColor() {
		return predefinedColor;
	}

	protected void handleComboSelectedEvent(SelectionEvent e) {
		predefinedColor = combo.getText();
		if (NONE_CHOICE.equals(predefinedColor)) {
			predefinedColor = null;
			processAction(null);
			return;
		}

		String colorName = predefinedColor;
		if (choiceSet != null) {
			if (choiceSet.findChoiceByDisplayName(colorName) != null)
				colorName = choiceSet.findChoiceByDisplayName(colorName).getName();
		}
		int[] intRgb = ColorUtil.getRGBs(colorName);
		RGB rgb = null;
		if (intRgb != null) {
			rgb = new RGB(intRgb[0], intRgb[1], intRgb[2]);
		}
		processAction(rgb);
	}

	protected void handleComboDefaultSelectedEvent(SelectionEvent e) {
		predefinedColor = null;
		String string = combo.getText();
		int index = -1;
		String[] items = combo.getItems();
		for (int i = 0; i < items.length; i++) {
			if (items[i].equalsIgnoreCase(string)) {
				index = i;
				combo.setText(items[i]);
				break;
			}
		}
		if (index != -1) {
			handleComboSelectedEvent(null);
			return;
		}
		RGB rgb = parseString(string);
		if (rgb == null) {
			combo.deselectAll();
			if (StringUtil.isBlank(string)) {
				// for blank string or null string, we reset the value to null.
				setRGB(null);
			} else {
				// for other invalid string, we keep current value.
				setRGB(getRGB());
			}
			notifyListeners(SWT.Modify, null);
		} else
			processAction(rgb);
	}
}