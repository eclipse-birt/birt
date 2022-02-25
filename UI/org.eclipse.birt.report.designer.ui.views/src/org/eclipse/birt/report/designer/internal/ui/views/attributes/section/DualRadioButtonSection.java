/*******************************************************************************
 * Copyright (c) 2009 Actuate Corporation.
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
import org.eclipse.birt.report.designer.internal.ui.views.attributes.widget.DualRadioButtonPropertyDescriptor;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;

/**
 * A Section with a pair of radio buttons.
 */

public class DualRadioButtonSection extends Section {

	private DualRadioButtonPropertyDescriptor descriptor;

	private IDescriptorProvider provider;

	private int width = -1;

	public DualRadioButtonSection(String labelText, Composite parent, boolean isFormStyle) {
		super(labelText, parent, isFormStyle);
	}

	@Override
	public void createSection() {
		getLabelControl(parent);
		getPropertyControl(parent);
		getGridPlaceholder(parent);
	}

	protected DualRadioButtonPropertyDescriptor getPropertyControl(Composite parent) {
		if (descriptor == null) {
			descriptor = DescriptorToolkit.createRadioButtonPropertyDescriptor(true);
			if (getProvider() != null) {
				descriptor.setDescriptorProvider(getProvider());
			}
			descriptor.createControl(parent);
			descriptor.getControl().setLayoutData(new GridData());
			descriptor.getControl().addDisposeListener(new DisposeListener() {

				@Override
				public void widgetDisposed(DisposeEvent event) {
					descriptor = null;
				}
			});
		} else {
			checkParent(descriptor.getControl(), parent);
		}
		return descriptor;
	}

	public IDescriptorProvider getProvider() {
		return provider;
	}

	@Override
	public void layout() {
		GridData gd = (GridData) descriptor.getControl().getLayoutData();

		if (width > -1) {
			gd.widthHint = width;
		}
		gd.grabExcessHorizontalSpace = false;

	}

	@Override
	public void setInput(Object input) {
		if (input != null) {
			descriptor.setInput(input);
		}
	}

	public void setProvider(IDescriptorProvider provider) {
		this.provider = provider;
		if (descriptor != null) {
			descriptor.setDescriptorProvider(provider);
		}
	}

	public void setFocus() {
		if (descriptor != null) {
			descriptor.getControl().setFocus();
		}
	}

	public void setWidth(int width) {
		this.width = width;
	}

	@Override
	public void load() {
		if (descriptor != null && !descriptor.getControl().isDisposed()) {
			descriptor.load();
		}
	}

	@Override
	public void reset() {
		if (descriptor != null && !descriptor.getControl().isDisposed()) {
			descriptor.reset();
		}
	}

	@Override
	public void setHidden(boolean isHidden) {
		if (displayLabel != null) {
			WidgetUtil.setExcludeGridData(displayLabel, isHidden);
		}
		if (descriptor != null) {
			descriptor.setHidden(isHidden);
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
		if (descriptor != null) {
			descriptor.setVisible(isVisible);
		}
		if (placeholderLabel != null) {
			placeholderLabel.setVisible(isVisible);
		}
	}

}
