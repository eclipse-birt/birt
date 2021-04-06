/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.designer.internal.ui.views.attributes.section;

import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.WidgetUtil;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.IDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.widget.BindingGroupDescriptor;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

public class BindingGroupSection extends Section {

	public BindingGroupSection(Composite parent, boolean isFormStyle) {
		super(" ", parent, isFormStyle); //$NON-NLS-1$
	}

	protected BindingGroupDescriptor bindingGroup;

	public void createSection() {
		getBindingGroupControl(parent);
		getGridPlaceholder(parent);
	}

	public BindingGroupDescriptor getBindingGroupControl() {
		return bindingGroup;
	}

	protected BindingGroupDescriptor getBindingGroupControl(Composite parent) {
		if (bindingGroup == null) {
			bindingGroup = new BindingGroupDescriptor(true);
			bindingGroup.setDescriptorProvider(provider);
			bindingGroup.createControl(parent);
			bindingGroup.getControl().setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
			bindingGroup.getControl().addDisposeListener(new DisposeListener() {

				public void widgetDisposed(DisposeEvent event) {
					bindingGroup = null;
				}
			});
		} else {
			checkParent(bindingGroup.getControl(), parent);
		}
		return bindingGroup;
	}

	public void layout() {
		GridData gd = (GridData) bindingGroup.getControl().getLayoutData();
		if (getLayoutNum() > 0)
			gd.horizontalSpan = getLayoutNum() - placeholder;
		else
			gd.horizontalSpan = ((GridLayout) parent.getLayout()).numColumns - placeholder;
		if (width > -1) {
			gd.widthHint = width;
			gd.grabExcessHorizontalSpace = false;
		} else
			gd.grabExcessHorizontalSpace = true;

		if (height > -1) {
			gd.heightHint = height;
			gd.grabExcessVerticalSpace = false;
		}
	}

	private int height = -1;

	public void setHeight(int height) {
		this.height = height;
	}

	public void load() {
		if (bindingGroup != null && !bindingGroup.getControl().isDisposed())
			bindingGroup.load();
	}

	IDescriptorProvider provider;

	public IDescriptorProvider getProvider() {
		return provider;
	}

	public void setProvider(IDescriptorProvider provider) {
		this.provider = provider;
		if (bindingGroup != null)
			bindingGroup.setDescriptorProvider(provider);
	}

	public void setInput(Object input) {
		assert (input != null);
		bindingGroup.setInput(input);
		if (getProvider() != null)
			getProvider().setInput(input);
	}

	public void setHidden(boolean isHidden) {
		if (bindingGroup != null)
			WidgetUtil.setExcludeGridData(bindingGroup.getControl(), isHidden);

	}

	public void setVisible(boolean isVisable) {
		if (bindingGroup != null)
			bindingGroup.getControl().setVisible(isVisable);

	}

	private int width = -1;

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}
}
