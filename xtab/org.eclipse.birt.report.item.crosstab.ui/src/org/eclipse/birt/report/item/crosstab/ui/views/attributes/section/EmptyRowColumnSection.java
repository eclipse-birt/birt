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

package org.eclipse.birt.report.item.crosstab.ui.views.attributes.section;

import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.WidgetUtil;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.Section;
import org.eclipse.birt.report.item.crosstab.ui.views.attributes.provider.EmptyRowColumnProvider;
import org.eclipse.birt.report.item.crosstab.ui.views.attributes.widget.EmptyRowColumnDescriptor;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

public class EmptyRowColumnSection extends Section {

	/**
	 * Old text value.
	 */
	private String oldValue;

	/**
	 * The text field, or <code>null</code> if none.
	 */
	protected Label label;

	private int style = -1;

	public EmptyRowColumnSection(Composite parent, boolean formStyle) {
		super(null, parent, formStyle);
	}

	private boolean fillLabel = false;

	private int width = -1;

	private EmptyRowColumnDescriptor emptyRowColumn;

	public void createSection() {
		getEmptyRowColumnDescriptor(parent);
		getGridPlaceholder(parent);
	}

	public void layout() {
		GridData gd = (GridData) emptyRowColumn.getControl().getLayoutData();
		if (getLayoutNum() > 0)
			gd.horizontalSpan = getLayoutNum() - placeholder;
		else
			gd.horizontalSpan = ((GridLayout) parent.getLayout()).numColumns - placeholder;
		if (width > -1) {
			gd.widthHint = width;
			gd.grabExcessHorizontalSpace = false;
		} else
			gd.grabExcessHorizontalSpace = fillLabel;

		if (height > -1) {
			gd.heightHint = height;
			gd.grabExcessVerticalSpace = false;
			if (displayLabel != null) {
				gd = (GridData) displayLabel.getLayoutData();
				gd.verticalAlignment = GridData.VERTICAL_ALIGN_FILL;
			}
		} else {
			gd.grabExcessVerticalSpace = fillLabel;
			if (fillLabel) {
				gd.verticalAlignment = GridData.FILL;
				if (displayLabel != null) {
					gd = (GridData) displayLabel.getLayoutData();
					gd.verticalAlignment = GridData.VERTICAL_ALIGN_FILL;
				}
			}

		}

	}

	public String getStringValue() {
		if (label != null) {
			return label.getText();
		}

		return null;
	}

	public EmptyRowColumnDescriptor getEmptyRowColumnDescriptor() {
		return emptyRowColumn;
	}

	protected EmptyRowColumnDescriptor getEmptyRowColumnDescriptor(Composite parent) {
		if (emptyRowColumn == null) {
			emptyRowColumn = new EmptyRowColumnDescriptor(true);
			if (getProvider() != null)
				emptyRowColumn.setDescriptorProvider(getProvider());
			emptyRowColumn.createControl(parent);
			emptyRowColumn.getControl().setLayoutData(new GridData());
			emptyRowColumn.getControl().addDisposeListener(new DisposeListener() {

				public void widgetDisposed(DisposeEvent event) {
					emptyRowColumn = null;
				}
			});
		} else {
			checkParent(emptyRowColumn.getControl(), parent);
		}
		return emptyRowColumn;
	}

	public void setFocus() {
		if (label != null) {
			label.setFocus();
		}
	}

	public void setStringValue(String value) {
		if (label != null) {
			if (value == null) {
				value = "";//$NON-NLS-1$
			}
			oldValue = label.getText();
			if (!oldValue.equals(value)) {
				label.setText(value);
			}
		}
	}

	private int height = -1;

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public boolean isFillLabel() {
		return fillLabel;
	}

	public void setFillLabel(boolean fillLabel) {
		this.fillLabel = fillLabel;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public void setHidden(boolean isHidden) {
		if (displayLabel != null)
			WidgetUtil.setExcludeGridData(displayLabel, isHidden);
		if (label != null)
			WidgetUtil.setExcludeGridData(label, isHidden);
		if (placeholderLabel != null)
			WidgetUtil.setExcludeGridData(placeholderLabel, isHidden);
	}

	public void setVisible(boolean isVisible) {
		if (displayLabel != null)
			displayLabel.setVisible(isVisible);
		if (label != null)
			label.setVisible(isVisible);
		if (placeholderLabel != null)
			placeholderLabel.setVisible(isVisible);
	}

	public int getStyle() {
		return style;
	}

	public void setStyle(int style) {
		this.style = style;
	}

	public void setInput(Object input) {
		assert (input != null);
		emptyRowColumn.setInput(input);
	}

	public void load() {
		if (emptyRowColumn != null && !emptyRowColumn.getControl().isDisposed())
			emptyRowColumn.load();
	}

	EmptyRowColumnProvider provider;

	public EmptyRowColumnProvider getProvider() {
		return provider;
	}

	public void setProvider(EmptyRowColumnProvider provider) {
		this.provider = provider;
		if (emptyRowColumn != null)
			emptyRowColumn.setDescriptorProvider(provider);
	}
}
