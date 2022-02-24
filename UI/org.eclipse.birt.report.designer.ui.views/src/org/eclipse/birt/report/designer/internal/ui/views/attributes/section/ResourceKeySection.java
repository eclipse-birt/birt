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

import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.WidgetUtil;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.IDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.widget.DescriptorToolkit;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.widget.ResourceKeyDescriptor;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

public class ResourceKeySection extends Section {

	protected ResourceKeyDescriptor resource;

	public ResourceKeySection(String labelText, Composite parent, boolean isFormStyle) {
		super(labelText, parent, isFormStyle);
		// TODO Auto-generated constructor stub
	}

	public void createSection() {
		getLabelControl(parent);
		getResourceKeyControl(parent);
		getGridPlaceholder(parent);
	}

	public void layout() {
		GridData gd = (GridData) resource.getControl().getLayoutData();
		if (getLayoutNum() > 0)
			gd.horizontalSpan = getLayoutNum() - 1 - placeholder;
		else
			gd.horizontalSpan = ((GridLayout) parent.getLayout()).numColumns - 1 - placeholder;
		if (width > -1) {
			gd.widthHint = width;
			gd.grabExcessHorizontalSpace = false;
		} else
			gd.grabExcessHorizontalSpace = fillResource;
	}

	protected ResourceKeyDescriptor getResourceKeyControl(Composite parent) {
		if (resource == null) {
			resource = DescriptorToolkit.createResourceKeyDescriptor(isFormStyle);
			if (getProvider() != null)
				resource.setDescriptorProvider(getProvider());
			resource.createControl(parent);
			resource.getControl().setLayoutData(new GridData());
			resource.getControl().addDisposeListener(new DisposeListener() {

				public void widgetDisposed(DisposeEvent event) {
					resource = null;
				}
			});
		} else {
			checkParent(resource.getControl(), parent);
		}
		return resource;
	}

	public ResourceKeyDescriptor getResourceKeyControl() {
		return resource;
	}

	IDescriptorProvider provider;

	public IDescriptorProvider getProvider() {
		return provider;
	}

	public void setProvider(IDescriptorProvider provider) {
		this.provider = provider;
		if (resource != null)
			resource.setDescriptorProvider(provider);
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
		resource.setInput(input);
	}

	private boolean fillResource = false;

	public boolean isFillResource() {
		return fillResource;
	}

	public void setFillResource(boolean fillResource) {
		this.fillResource = fillResource;
	}

	private String oldValue;

	public void setStringValue(String value) {
		if (resource != null) {
			if (value == null) {
				value = "";//$NON-NLS-1$
			}
			oldValue = resource.getStringValue();
			if (!oldValue.equals(value)) {
				resource.setStringValue(value);
			}
		}
	}

	public void setFocus() {
		if (resource != null) {
			resource.getControl().setFocus();
		}
	}

	public String getStringValue() {
		if (resource != null) {
			return resource.getStringValue();
		}

		return null;
	}

	public void load() {
		if (resource != null && !resource.getControl().isDisposed())
			resource.load();
	}

	public void setHidden(boolean isHidden) {
		if (displayLabel != null)
			WidgetUtil.setExcludeGridData(displayLabel, isHidden);
		if (resource != null)
			resource.setHidden(isHidden);
		if (placeholderLabel != null)
			WidgetUtil.setExcludeGridData(placeholderLabel, isHidden);
	}

	public void setVisible(boolean isVisible) {
		if (displayLabel != null)
			displayLabel.setVisible(isVisible);
		if (resource != null)
			resource.setVisible(isVisible);
		if (placeholderLabel != null)
			placeholderLabel.setVisible(isVisible);
	}
}
