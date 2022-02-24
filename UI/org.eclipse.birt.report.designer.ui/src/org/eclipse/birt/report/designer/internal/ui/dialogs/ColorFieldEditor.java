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

package org.eclipse.birt.report.designer.internal.ui.dialogs;

import org.eclipse.birt.report.designer.ui.views.attributes.providers.ChoiceSetFactory;
import org.eclipse.birt.report.designer.ui.widget.ColorBuilder;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.birt.report.model.api.util.ColorUtil;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

/**
 * A field editor for a color type preference.
 */

public class ColorFieldEditor extends AbstractFieldEditor {

	/**
	 * The color builder, or <code>null</code> if none.
	 */
	private ColorBuilder colorSelector;

	/**
	 * Creates a new color field editor
	 */
	protected ColorFieldEditor() {
	}

	/**
	 * Creates a color field editor.
	 * 
	 * @param name      the name of the preference this field editor works on
	 * @param labelText the label text of the field editor
	 * @param parent    the parent of the field editor's control
	 */
	public ColorFieldEditor(String name, String labelText, Composite parent) {
		super(name, labelText, parent);
	}

	/*
	 * (non-Javadoc) Method declared on FieldEditor.
	 */
	protected void adjustForNumColumns(int numColumns) {
		((GridData) colorSelector.getLayoutData()).horizontalSpan = numColumns - 1;
		((GridData) colorSelector.getLayoutData()).widthHint = 85;
	}

	/*
	 * (non-Javadoc) Method declared on FieldEditor.
	 */
	protected void doFillIntoGrid(Composite parent, int numColumns) {
		Control control = getLabelControl(parent);
		GridData gd = new GridData();
		gd.horizontalSpan = numColumns - 1;
		control.setLayoutData(gd);

		Composite ctrl = getChangeControl(parent);
		gd = new GridData();
		ctrl.setLayoutData(gd);
	}

	/*
	 * (non-Javadoc) Method declared on FieldEditor.
	 */
	protected void doLoad() {
		RGB rgb = DEUtil.getRGBValue(ColorUtil.parseColor(getPreferenceStore().getString(getPreferenceName())));
		getColorSelector().setRGB(rgb);
		setOldValue(getStringValue());
	}

	/*
	 * (non-Javadoc) Method declared on FieldEditor.
	 */
	protected void doLoadDefault() {
		if (colorSelector == null)
			return;
		RGB rgb = DEUtil.getRGBValue(ColorUtil.parseColor(getPreferenceStore().getDefaultString(getPreferenceName())));
		colorSelector.setRGB(rgb);
		setDefaultValue(getStringValue());
		if (this.getPreferenceStore() instanceof StylePreferenceStore) {
			StylePreferenceStore store = (StylePreferenceStore) this.getPreferenceStore();
			if (store.hasLocalValue(getPreferenceName()))
				markDirty(true);
			else
				markDirty(false);
		} else
			markDirty(true);
	}

	/**
	 * Get the color selector used by the receiver.
	 * 
	 * @return ColorSelector/
	 */
	public ColorBuilder getColorSelector() {
		return colorSelector;
	}

	/**
	 * Returns the change button for this field editor.
	 * 
	 * @param parent The control to create the button in if required.
	 * @return the change button
	 */
	protected Composite getChangeControl(Composite parent) {
		if (colorSelector == null) {
			colorSelector = new ColorBuilder(parent, 0);
			colorSelector.setChoiceSet(
					ChoiceSetFactory.getElementChoiceSet(ReportDesignConstants.STYLE_ELEMENT, getPreferenceName()));
			colorSelector.addListener(SWT.Modify, new Listener() {

				// forward the property change of the color selector
				public void handleEvent(Event event) {
					valueChanged(VALUE);
				}
			});
		} else {
			checkParent(colorSelector, parent);
		}

		return colorSelector;
	}

	/*
	 * (non-Javadoc) Method declared on FieldEditor.
	 */
	public int getNumberOfControls() {
		return 2;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.preference.FieldEditor#setEnabled(boolean,
	 * org.eclipse.swt.widgets.Composite)
	 */
	public void setEnabled(boolean enabled, Composite parent) {
		super.setEnabled(enabled, parent);
		getChangeControl(parent).setEnabled(enabled);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.dialogs.AbstractFieldEditor
	 * #getValue()
	 */
	protected String getStringValue() {
		RGB rgb = getColorSelector().getRGB();
		if (rgb == null) {
			return null;
		}
		return ColorUtil.format(DEUtil.getRGBInt(getColorSelector().getRGB()), ColorUtil.HTML_FORMAT);
	}
}
