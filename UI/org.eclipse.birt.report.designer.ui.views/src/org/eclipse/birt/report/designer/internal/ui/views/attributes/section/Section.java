/*******************************************************************************
 * Copyright (c) 2021 Contributors to the Eclipse Foundation
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *   See git history
 *******************************************************************************/

package org.eclipse.birt.report.designer.internal.ui.views.attributes.section;

import org.eclipse.birt.report.designer.internal.ui.swt.custom.FormWidgetFactory;
import org.eclipse.core.runtime.Assert;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;

public abstract class Section {

	protected static final int HORIZONTAL_GAP = 8;

	/**
	 * The label's text.
	 */
	private String labelText;

	/**
	 * The label control.
	 */
	protected Label displayLabel;
	/**
	 * Creates a new field editor.
	 */

	protected boolean isFormStyle;

	/**
	 * Creates a new field editor.
	 *
	 * @param name      the name of the preference this field editor works on
	 * @param labelText the label text of the field editor
	 * @param parent    the parent of the field editor's control
	 */
	protected Section(String labelText, Composite parent, boolean isFormStyle) {
		this.isFormStyle = isFormStyle;
		init(labelText);
		this.parent = parent;
	}

	private int layoutNum = -1;

	/**
	 * Checks if the given parent is the current parent of the supplied control;
	 * throws an (unchecked) exception if they are not correctly related.
	 *
	 * @param control the control
	 * @param parent  the parent control
	 */
	protected void checkParent(Control control, Composite parent) {
		Assert.isTrue(control.getParent() == parent, "Different parents");//$NON-NLS-1$
	}

	/**
	 * Creates this field editor's main control containing all of its basic
	 * controls.
	 *
	 * @param parent the parent control
	 */

	protected Composite parent;

	/**
	 * Fills this field editor's basic controls into the given parent.
	 * <p>
	 * Subclasses must implement this method to create the controls for this field
	 * editor.
	 * </p>
	 *
	 * @param parent     the composite used as a parent for the basic controls; the
	 *                   parent's layout must be a <code>GridLayout</code>
	 * @param numColumns the number of columns
	 */
	public abstract void createSection();

	public abstract void layout();

	/**
	 * Initializes this field editor with the preference value from the preference
	 * store.
	 * <p>
	 * Subclasses must implement this method to properly initialize the field
	 * editor.
	 * </p>
	 */
	public abstract void load();

	/**
	 * Returns the label control.
	 *
	 * @return the label control, or <code>null</code> if no label control has been
	 *         created
	 */
	public Label getLabelControl() {
		return displayLabel;
	}

	/**
	 * Returns this field editor's label component.
	 * <p>
	 * The label is created if it does not already exist
	 * </p>
	 *
	 * @param parent the parent
	 * @return the label control
	 */
	protected Label getLabelControl(Composite parent) {
		if (displayLabel == null) {
			displayLabel = FormWidgetFactory.getInstance().createLabel(parent, SWT.LEFT, isFormStyle);
			displayLabel.setFont(parent.getFont());
			displayLabel.setLayoutData(new GridData());
			String text = getLabelText();
			if (text != null) {
				displayLabel.setText(text);
			}
			displayLabel.addDisposeListener(new DisposeListener() {

				@Override
				public void widgetDisposed(DisposeEvent event) {
					displayLabel = null;
				}
			});
		} else {
			checkParent(displayLabel, parent);
		}
		return displayLabel;
	}

	/**
	 * Returns this field editor's label text.
	 *
	 * @return the label text
	 */
	public String getLabelText() {
		return labelText;
	}

	/**
	 * Initialize the field editor with the given preference name and label.
	 *
	 * @param name the name of the preference this field editor works on
	 * @param text the label text of the field editor
	 */
	protected void init(String text) {
		this.labelText = text;
	}

	/**
	 * Sets this field editor's label text. The label is typically presented to the
	 * left of the entry field.
	 *
	 * @param text the label text
	 */
	public void setLabelText(String text) {
		Assert.isNotNull(text);
		labelText = text;
		if (displayLabel != null) {
			displayLabel.setText(text);
		}
	}

	public abstract void setInput(Object input);

	protected int placeholder = 0;

	protected boolean holderGrabSpace = true;

	public void setGridPlaceholder(int hSpan, boolean grabSpace) {
		placeholder = hSpan;
		holderGrabSpace = grabSpace;
	}

	protected Label placeholderLabel;

	protected Label getGridPlaceholder(Composite parent) {
		if (placeholder == 0) {
			return null;
		}
		if (placeholderLabel == null) {
			placeholderLabel = FormWidgetFactory.getInstance().createLabel(parent, isFormStyle);
			GridData data = new GridData();
			data.horizontalSpan = placeholder;
			data.grabExcessHorizontalSpace = holderGrabSpace;
			placeholderLabel.setLayoutData(data);
		}
		return placeholderLabel;
	}

	public Label getGridPlaceholder() {
		return placeholderLabel;
	}

	public int getLayoutNum() {
		return layoutNum;
	}

	public void setLayoutNum(int layoutNum) {
		this.layoutNum = layoutNum;
	}

	public abstract void setHidden(boolean isHidden);

	public abstract void setVisible(boolean isVisable);

	public void reset() {

	}

	private boolean isReadOnly = false;

	public void setReadOnly(boolean readOnly) {
		this.isReadOnly = readOnly;
	}

	public boolean isReadOnly() {
		return isReadOnly;
	}

}
