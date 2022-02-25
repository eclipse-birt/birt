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
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.IToggleDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.widget.DescriptorToolkit;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.widget.TogglePropertyDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

public class TogglesSection extends Section {

	public TogglesSection(Composite parent) {
		super(" ", parent, true); //$NON-NLS-1$
	}

	public TogglesSection(Composite parent, String title) {
		super(title, parent, true);
	}

	protected TogglePropertyDescriptor[] toggles;

	@Override
	public void createSection() {
		if (!getLabelText().trim().equals("")) { //$NON-NLS-1$
			getLabelControl(parent);
		}
		getTogglesControl(parent);
		getGridPlaceholder(parent);
	}

	public Composite getTogglesControl() {
		return composite;
	}

	protected Composite getTogglesControl(Composite parent) {
		if (toggles == null && providers != null) {
			composite = new Composite(parent, SWT.NONE);
			GridLayout layout = new GridLayout();
			layout.horizontalSpacing = 7;
			layout.numColumns = providers.length;
			composite.setLayout(layout);
			composite.setLayoutData(new GridData());
			toggles = new TogglePropertyDescriptor[providers.length];
			for (int i = 0; i < providers.length; i++) {
				toggle = DescriptorToolkit.createTogglePropertyDescriptor();
				toggles[i] = toggle;
				toggle.setDescriptorProvider(providers[i]);
				toggle.createControl(composite);
				GridData gd = new GridData();
				toggle.getControl().setLayoutData(gd);
				toggle.getControl().addDisposeListener(new DisposeListener() {

					@Override
					public void widgetDisposed(DisposeEvent event) {
						toggle = null;
						boolean flag = true;
						for (int i = 0; i < providers.length; i++) {
							if (toggles[i] != null) {
								flag = false;
								break;
							}
						}
						if (flag) {
							toggles = null;
						}
					}
				});
			}
		} else {
			checkParent(composite, parent);
		}
		return composite;
	}

	@Override
	public void layout() {
		GridData gd = (GridData) composite.getLayoutData();
		if (getLayoutNum() > 0) {
			gd.horizontalSpan = getLayoutNum() - placeholder;
		} else {
			gd.horizontalSpan = ((GridLayout) parent.getLayout()).numColumns - placeholder;
		}
		if (getLabelControl() != null) {
			gd.horizontalSpan = gd.horizontalSpan - 1;
		}
		gd.horizontalAlignment = GridData.FILL;
		if (width > -1) {
			gd.widthHint = width;
			gd.grabExcessHorizontalSpace = false;
		} else {
			gd.grabExcessHorizontalSpace = fillToggle;
		}

	}

	@Override
	public void load() {
		for (int i = 0; i < toggles.length; i++) {
			if (toggles[i] != null && !toggles[i].getControl().isDisposed()) {
				toggles[i].load();
			}
		}
		if (!composite.isDisposed()) {
			composite.layout();
		}
	}

	@Override
	public void reset() {
		for (int i = 0; i < toggles.length; i++) {
			if (toggles[i] != null && !toggles[i].getControl().isDisposed()) {
				toggles[i].reset();
			}
		}
	}

	@Override
	public void setInput(Object input) {
		assert (input != null);
		for (int i = 0; i < toggles.length; i++) {
			toggles[i].setInput(input);
		}
	}

	private int width = -1;

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	private boolean fillToggle = false;

	public boolean isFillToggle() {
		return fillToggle;
	}

	public void setFillToggle(boolean fillToggle) {
		this.fillToggle = fillToggle;
	}

	@Override
	public void setHidden(boolean isHidden) {
		if (displayLabel != null) {
			WidgetUtil.setExcludeGridData(displayLabel, isHidden);
		}
		if (composite != null) {
			WidgetUtil.setExcludeGridData(composite, isHidden);
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
		if (composite != null) {
			composite.setVisible(isVisible);
		}
		if (placeholderLabel != null) {
			placeholderLabel.setVisible(isVisible);
		}
	}

	IToggleDescriptorProvider[] providers;
	private Composite composite;
	private TogglePropertyDescriptor toggle;

	public IToggleDescriptorProvider[] getProviders() {
		return providers;
	}

	public void setProviders(IToggleDescriptorProvider[] providers) {
		this.providers = providers;
	}
}
