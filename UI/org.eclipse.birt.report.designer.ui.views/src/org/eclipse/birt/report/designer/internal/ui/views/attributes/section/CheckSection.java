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

import org.eclipse.birt.report.designer.internal.ui.util.WidgetUtil;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.IDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.widget.CheckPropertyDescriptor;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.widget.DescriptorToolkit;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

public class CheckSection extends Section {

	public CheckSection(Composite parent, boolean isFormStyle) {
		super(" ", parent, isFormStyle); //$NON-NLS-1$
	}

	public CheckSection(Composite parent, String title, boolean isFormStyle) {
		super(title, parent, isFormStyle);
	}

	protected CheckPropertyDescriptor check;

	private boolean showDisplayLabel;

	public boolean isShowDisplayLabel() {
		return showDisplayLabel;
	}

	public void setShowDisplayLabel(boolean showDisplayLabel) {
		this.showDisplayLabel = showDisplayLabel;
	}

	@Override
	public void createSection() {
		if (!getLabelText().trim().equals("") || showDisplayLabel) { //$NON-NLS-1$
			getLabelControl(parent);
		}
		getCheckControl(parent);
		getGridPlaceholder(parent);
	}

	protected CheckPropertyDescriptor getCheckControl(Composite parent) {
		if (check == null) {
			check = DescriptorToolkit.createCheckPropertyDescriptor(isFormStyle);
			check.setDescriptorProvider(getProvider());
			check.createControl(parent);
			check.getControl().setLayoutData(new GridData());
			check.getControl().addDisposeListener(new DisposeListener() {

				@Override
				public void widgetDisposed(DisposeEvent event) {
					check = null;
				}
			});
		} else {
			checkParent(check.getControl(), parent);
		}
		return check;
	}

	public CheckPropertyDescriptor getCheckControl() {
		return check;
	}

	@Override
	public void layout() {
		GridData gd = (GridData) check.getControl().getLayoutData();
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
			gd.grabExcessHorizontalSpace = fillCheck;
		}

	}

	@Override
	public void load() {
		if (check != null && !check.getControl().isDisposed()) {
			check.load();
		}
	}

	@Override
	public void reset() {
		if (check != null && !check.getControl().isDisposed()) {
			check.reset();
		}
	}

	@Override
	public void setInput(Object input) {
		assert (input != null);
		check.setInput(input);
	}

	private int width = -1;

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	private boolean fillCheck = false;

	public boolean isFillCheck() {
		return fillCheck;
	}

	public void setFillCheck(boolean fillCheck) {
		this.fillCheck = fillCheck;
	}

	@Override
	public void setHidden(boolean isHidden) {
		if (displayLabel != null) {
			WidgetUtil.setExcludeGridData(displayLabel, isHidden);
		}
		if (check != null) {
			check.setHidden(isHidden);
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
		if (check != null) {
			check.setVisible(isVisible);
		}
		if (placeholderLabel != null) {
			placeholderLabel.setVisible(isVisible);
		}
	}

	IDescriptorProvider provider;

	public IDescriptorProvider getProvider() {
		return provider;
	}

	public void setProvider(IDescriptorProvider provider) {
		this.provider = provider;
	}

}
