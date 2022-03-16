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
import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.WidgetUtil;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

public class LabelSection extends Section {

	/**
	 * Old text value.
	 */
	private String oldValue;

	/**
	 * The text field, or <code>null</code> if none.
	 */
	protected Label label;

	private int style = -1;

	public LabelSection(String text, Composite parent, boolean formStyle) {
		super(text, parent, formStyle);
	}

	private boolean fillLabel = false;

	private int width = -1;

	@Override
	public void createSection() {
		getLabelControl(parent);
		getGridPlaceholder(parent);
	}

	@Override
	public void layout() {
		GridData gd = (GridData) label.getLayoutData();
		if (getLayoutNum() > 0) {
			gd.horizontalSpan = getLayoutNum() - placeholder;
		} else {
			gd.horizontalSpan = ((GridLayout) parent.getLayout()).numColumns - placeholder;
		}
		gd.horizontalAlignment = SWT.FILL;
		if (width > -1) {
			gd.widthHint = width;
			gd.grabExcessHorizontalSpace = false;
		} else {
			gd.grabExcessHorizontalSpace = fillLabel;
		}

		if (height > -1) {
			gd.heightHint = width;
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

	@Override
	public Label getLabelControl() {
		return label;
	}

	@Override
	protected Label getLabelControl(Composite parent) {
		if (label == null) {
			if (style != -1) {
				label = FormWidgetFactory.getInstance().createLabel(parent, style, isFormStyle);
			} else {
				label = FormWidgetFactory.getInstance().createLabel(parent, SWT.NONE | SWT.WRAP, isFormStyle);
			}
			label.setText(getLabelText());
			label.setLayoutData(new GridData());
			label.addDisposeListener(new DisposeListener() {

				@Override
				public void widgetDisposed(DisposeEvent event) {
					label = null;
				}
			});
		} else {
			checkParent(label, parent);
		}
		return label;
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

	@Override
	public void setHidden(boolean isHidden) {
		if (displayLabel != null) {
			WidgetUtil.setExcludeGridData(displayLabel, isHidden);
		}
		if (label != null) {
			WidgetUtil.setExcludeGridData(label, isHidden);
		}
		if (placeholderLabel != null) {
			WidgetUtil.setExcludeGridData(placeholderLabel, isHidden);
		}
	}

	@Override
	public void setVisible(boolean isVisible) {
		if (displayLabel != null) {
			displayLabel.setVisible(isVisible);
		}
		if (label != null) {
			label.setVisible(isVisible);
		}
		if (placeholderLabel != null) {
			placeholderLabel.setVisible(isVisible);
		}
	}

	public int getStyle() {
		return style;
	}

	public void setStyle(int style) {
		this.style = style;
	}

	@Override
	public void load() {
		// TODO Auto-generated method stub

	}

	@Override
	public void setInput(Object input) {
		// TODO Auto-generated method stub

	}

}
