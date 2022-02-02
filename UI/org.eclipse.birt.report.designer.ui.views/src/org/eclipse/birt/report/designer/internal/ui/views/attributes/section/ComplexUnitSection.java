/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
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

package org.eclipse.birt.report.designer.internal.ui.views.attributes.section;

import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.WidgetUtil;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.IDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.widget.ComplexUnitPropertyDescriptor;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.widget.DescriptorToolkit;
import org.eclipse.swt.accessibility.AccessibleAdapter;
import org.eclipse.swt.accessibility.AccessibleEvent;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class ComplexUnitSection extends Section {

	public ComplexUnitSection(String labelText, Composite parent, boolean isFormStyle) {
		super(labelText, parent, isFormStyle);
		// TODO Auto-generated constructor stub
	}

	protected ComplexUnitPropertyDescriptor unit;

	public void createSection() {
		getLabelControl(parent);
		getUnitComboControl(parent);
		getGridPlaceholder(parent);

	}

	public ComplexUnitPropertyDescriptor getUnitComboControl() {
		return unit;
	}

	protected ComplexUnitPropertyDescriptor getUnitComboControl(Composite parent) {
		if (unit == null) {
			if (customUnit != null)
				unit = customUnit;
			else
				unit = DescriptorToolkit.createComplexUnitPropertyDescriptor(true);
			if (getProvider() != null)
				unit.setDescriptorProvider(getProvider());
			unit.createControl(parent);
			unit.getControl().setLayoutData(new GridData());
			unit.getControl().addDisposeListener(new DisposeListener() {

				public void widgetDisposed(DisposeEvent event) {
					unit = null;
				}
			});

			setAccessible(unit.getControl());
		} else {
			checkParent(unit.getControl(), parent);
		}
		return unit;
	}

	private void setAccessible(final Control control) {
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
						if (control instanceof Text) {
							e.result = UIUtil.stripMnemonic(getLabelControl().getText()) + ((Text) control).getText();
						}
					}
				}

			});
		}
	}

	public void layout() {
		GridData gd = (GridData) unit.getControl().getLayoutData();
		if (getLayoutNum() > 0)
			gd.horizontalSpan = getLayoutNum() - 1 - placeholder;
		else
			gd.horizontalSpan = ((GridLayout) parent.getLayout()).numColumns - 1 - placeholder;
		if (width > -1) {
			gd.widthHint = width;
			gd.grabExcessHorizontalSpace = false;
		} else
			gd.grabExcessHorizontalSpace = fillUnit;

	}

	public void load() {
		if (unit != null && !unit.getControl().isDisposed())
			unit.load();
	}

	public void reset() {
		if (unit != null && !unit.getControl().isDisposed()) {
			unit.reset();
		}
	}

	IDescriptorProvider provider;

	public IDescriptorProvider getProvider() {
		return provider;
	}

	public void setProvider(IDescriptorProvider provider) {
		this.provider = provider;
		if (unit != null)
			unit.setDescriptorProvider(provider);
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
		unit.setInput(input);
	}

	boolean fillUnit = false;

	public boolean isFillUnit() {
		return fillUnit;
	}

	public void setFillUnit(boolean fillUnit) {
		this.fillUnit = fillUnit;
	}

	public void setHidden(boolean isHidden) {
		if (displayLabel != null)
			WidgetUtil.setExcludeGridData(displayLabel, isHidden);
		if (unit != null)
			unit.setHidden(isHidden);
		if (placeholderLabel != null)
			WidgetUtil.setExcludeGridData(placeholderLabel, isHidden);
	}

	public void setVisible(boolean isVisible) {
		if (displayLabel != null)
			displayLabel.setVisible(isVisible);
		if (unit != null)
			unit.setVisible(isVisible);
		if (placeholderLabel != null)
			placeholderLabel.setVisible(isVisible);
	}

	ComplexUnitPropertyDescriptor customUnit;

	public void setUnit(ComplexUnitPropertyDescriptor unit) {
		this.customUnit = unit;
	}
}
