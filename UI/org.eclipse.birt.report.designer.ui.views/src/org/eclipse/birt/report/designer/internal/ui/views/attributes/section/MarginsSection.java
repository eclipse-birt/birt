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
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.IDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.MarginsPropertyDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.widget.DescriptorToolkit;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.widget.MarginsPropertyDescriptor;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

public class MarginsSection extends Section {

	protected MarginsPropertyDescriptor spinner;

	public MarginsSection(String labelText, Composite parent, boolean isFormStyle) {
		super(labelText, parent, isFormStyle);
		// TODO Auto-generated constructor stub
	}

	public void createSection() {
		getImageLabelControl(parent);
		getMarginsControl(parent);
		getGridPlaceholder(parent);
	}

	private Label imageLabel;

	protected Label getImageLabelControl(Composite parent) {

		if (imageLabel == null) {
			imageLabel = FormWidgetFactory.getInstance().createLabel(parent, isFormStyle);
			if (getProvider() != null && getProvider() instanceof MarginsPropertyDescriptorProvider)
				imageLabel.setImage(((MarginsPropertyDescriptorProvider) getProvider()).getImage());
			imageLabel.setLayoutData(new GridData());
			imageLabel.addDisposeListener(new DisposeListener() {

				public void widgetDisposed(DisposeEvent event) {
					imageLabel = null;
				}
			});
		} else {
			checkParent(imageLabel, parent);
		}
		return imageLabel;

	}

	public void layout() {
		GridData gd = (GridData) getMarginsControl().getControl().getLayoutData();
		if (getLayoutNum() > 0)
			gd.horizontalSpan = getLayoutNum() - 1 - placeholder;
		else
			gd.horizontalSpan = ((GridLayout) parent.getLayout()).numColumns - 1 - placeholder;
		if (width > -1) {
			gd.widthHint = width;
			gd.grabExcessHorizontalSpace = false;
		} else
			gd.grabExcessHorizontalSpace = fillSpinner;
	}

	protected MarginsPropertyDescriptor getMarginsControl(Composite parent) {
		if (spinner == null) {
			spinner = DescriptorToolkit.createSpinnerPropertyDescriptor(isFormStyle);
			if (getProvider() != null)
				spinner.setDescriptorProvider(getProvider());
			spinner.createControl(parent);
			spinner.getControl().setLayoutData(new GridData());
			spinner.getControl().addDisposeListener(new DisposeListener() {

				public void widgetDisposed(DisposeEvent event) {
					spinner = null;
				}
			});
		} else {
			checkParent(spinner.getControl(), parent);
		}
		return spinner;
	}

	public MarginsPropertyDescriptor getMarginsControl() {
		return spinner;
	}

	IDescriptorProvider provider;

	public IDescriptorProvider getProvider() {
		return provider;
	}

	public void setProvider(IDescriptorProvider provider) {
		this.provider = provider;
		if (spinner != null)
			spinner.setDescriptorProvider(provider);
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
		spinner.setInput(input);
	}

	private boolean fillSpinner = false;

	public boolean isFillSpinner() {
		return fillSpinner;
	}

	public void setFillSpinner(boolean fillSpinner) {
		this.fillSpinner = fillSpinner;
	}

	public void setFocus() {
		if (spinner != null) {
			spinner.getControl().setFocus();
		}
	}

	public void load() {
		if (spinner != null && !spinner.getControl().isDisposed())
			spinner.load();
	}

	public void setHidden(boolean isHidden) {
		if (displayLabel != null)
			WidgetUtil.setExcludeGridData(displayLabel, isHidden);
		if (spinner != null)
			spinner.setHidden(isHidden);
		if (placeholderLabel != null)
			WidgetUtil.setExcludeGridData(placeholderLabel, isHidden);
	}

	public void setVisible(boolean isVisible) {
		if (displayLabel != null)
			displayLabel.setVisible(isVisible);
		if (spinner != null)
			spinner.setVisible(isVisible);
		if (placeholderLabel != null)
			placeholderLabel.setVisible(isVisible);
	}
}
