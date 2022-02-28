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

import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.WidgetUtil;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.IDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.widget.DescriptorToolkit;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.widget.UnitPropertyDescriptor;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

public class UnitSection extends Section {

	public UnitSection(String labelText, Composite parent, boolean isFormStyle) {
		super(labelText, parent, isFormStyle);
		// TODO Auto-generated constructor stub
	}

	protected UnitPropertyDescriptor unit;

	@Override
	public void createSection() {
		getLabelControl(parent);
		getUnitComboControl(parent);
		getGridPlaceholder(parent);

	}

	public UnitPropertyDescriptor getUnitComboControl() {
		return unit;
	}

	protected UnitPropertyDescriptor getUnitComboControl(Composite parent) {
		if (unit == null) {
			if (customUnit != null) {
				unit = customUnit;
			} else {
				unit = DescriptorToolkit.createUnitPropertyDescriptor(true);
			}
			if (getProvider() != null) {
				unit.setDescriptorProvider(getProvider());
			}
			unit.createControl(parent);
			unit.getControl().setLayoutData(new GridData());
			unit.getControl().addDisposeListener(new DisposeListener() {

				@Override
				public void widgetDisposed(DisposeEvent event) {
					unit = null;
				}
			});
		} else {
			checkParent(unit.getControl(), parent);
		}
		return unit;
	}

	@Override
	public void layout() {
		GridData gd = (GridData) unit.getControl().getLayoutData();
		if (getLayoutNum() > 0) {
			gd.horizontalSpan = getLayoutNum() - 1 - placeholder;
		} else {
			gd.horizontalSpan = ((GridLayout) parent.getLayout()).numColumns - 1 - placeholder;
		}
		if (width > -1) {
			gd.widthHint = width;
			gd.grabExcessHorizontalSpace = false;
		} else {
			gd.grabExcessHorizontalSpace = fillUnit;
		}

	}

	@Override
	public void load() {
		if (unit != null && !unit.getControl().isDisposed()) {
			unit.load();
		}
	}

	@Override
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
		if (unit != null) {
			unit.setDescriptorProvider(provider);
		}
	}

	private int width = -1;

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	@Override
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

	@Override
	public void setHidden(boolean isHidden) {
		if (displayLabel != null) {
			WidgetUtil.setExcludeGridData(displayLabel, isHidden);
		}
		if (unit != null) {
			unit.setHidden(isHidden);
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
		if (unit != null) {
			unit.setVisible(isVisible);
		}
		if (placeholderLabel != null) {
			placeholderLabel.setVisible(isVisible);
		}
	}

	UnitPropertyDescriptor customUnit;

	public void setUnit(UnitPropertyDescriptor unit) {
		this.customUnit = unit;
	}
}
