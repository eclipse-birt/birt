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

import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.WidgetUtil;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.IDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.widget.DescriptorToolkit;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.widget.FontSizePropertyDescriptor;
import org.eclipse.swt.accessibility.AccessibleAdapter;
import org.eclipse.swt.accessibility.AccessibleEvent;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;

public class FontSizeSection extends Section {

	protected FontSizePropertyDescriptor fontSize;

	public FontSizeSection(String labelText, Composite parent, boolean isFormStyle) {
		super(labelText, parent, isFormStyle);
		// TODO Auto-generated constructor stub
	}

	public void createSection() {
		getLabelControl(parent);
		getFontSizeControl(parent);
		getGridPlaceholder(parent);
	}

	public void layout() {
		GridData gd = (GridData) fontSize.getControl().getLayoutData();
		if (getLayoutNum() > 0)
			gd.horizontalSpan = getLayoutNum() - 1 - placeholder;
		else
			gd.horizontalSpan = ((GridLayout) parent.getLayout()).numColumns - 1 - placeholder;
		if (width > -1) {
			gd.widthHint = width;
			gd.grabExcessHorizontalSpace = false;
		} else
			gd.grabExcessHorizontalSpace = fillCombo;
	}

	public FontSizePropertyDescriptor getFontSizeControl() {
		return fontSize;
	}

	protected FontSizePropertyDescriptor getFontSizeControl(Composite parent) {
		if (fontSize == null) {
			fontSize = DescriptorToolkit.createFontSizePropertyDescriptor(true);
			if (getProvider() != null)
				fontSize.setDescriptorProvider(getProvider());
			fontSize.createControl(parent);
			fontSize.getControl().setLayoutData(new GridData());
			fontSize.getControl().addDisposeListener(new DisposeListener() {

				public void widgetDisposed(DisposeEvent event) {
					fontSize = null;
				}
			});

			setAccessible(fontSize.getControl());

		} else {
			checkParent(fontSize.getControl(), parent);
		}
		return fontSize;
	}

	private void setAccessible(Control control) {
		if (control instanceof Composite) {
			Composite parent = (Composite) control;
			if (parent != null && parent.getTabList() != null) {
				Control[] children = parent.getTabList();
				for (int i = 0; i < children.length; i++) {
					setAccessible(children[i]);
				}
			}
		} else {
			control.getAccessible().addAccessibleListener(new AccessibleAdapter() {

				public void getName(AccessibleEvent e) {
					Label lbl = getLabelControl();
					if (lbl != null) {
						e.result = UIUtil.stripMnemonic(getLabelControl().getText()) + fontSize.getFontSizeValue();
					}
				}

			});
		}
	}

	IDescriptorProvider provider;

	public IDescriptorProvider getProvider() {
		return provider;
	}

	public void setProvider(IDescriptorProvider provider) {
		this.provider = provider;
		if (fontSize != null)
			fontSize.setDescriptorProvider(provider);
	}

	private int width = -1;

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public void setInput(Object input) {
		assert (input != null);
		fontSize.setInput(input);
	}

	private boolean fillCombo = false;

	public boolean isFillCombo() {
		return fillCombo;
	}

	public void setFillCombo(boolean fillCombo) {
		this.fillCombo = fillCombo;
	}

	private String oldValue;

	public void setStringValue(String value) {
		if (fontSize != null) {
			if (value == null) {
				value = "";//$NON-NLS-1$
			}
			oldValue = fontSize.getFontSizeValue();
			if (!oldValue.equals(value)) {
				fontSize.setFontSizeValue(value);
			}
		}
	}

	public void setFocus() {
		if (fontSize != null) {
			fontSize.getControl().setFocus();
		}
	}

	public String getStringValue() {
		if (fontSize != null) {
			return fontSize.getFontSizeValue();
		}

		return null;
	}

	public void load() {
		if (fontSize != null && !fontSize.getControl().isDisposed())
			fontSize.load();
	}

	public void reset() {
		if (fontSize != null && !fontSize.getControl().isDisposed()) {
			fontSize.reset();
		}
	}

	public void setHidden(boolean isHidden) {
		if (displayLabel != null)
			WidgetUtil.setExcludeGridData(displayLabel, isHidden);
		if (fontSize != null)
			fontSize.setHidden(isHidden);
		if (placeholderLabel != null)
			WidgetUtil.setExcludeGridData(placeholderLabel, isHidden);
	}

	public void setVisible(boolean isVisible) {
		if (displayLabel != null)
			displayLabel.setVisible(isVisible);
		if (fontSize != null)
			fontSize.setVisible(isVisible);
		if (placeholderLabel != null)
			placeholderLabel.setVisible(isVisible);
	}
}
