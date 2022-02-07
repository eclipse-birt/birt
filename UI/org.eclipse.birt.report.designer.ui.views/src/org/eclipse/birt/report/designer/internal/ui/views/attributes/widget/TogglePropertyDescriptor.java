/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
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

package org.eclipse.birt.report.designer.internal.ui.views.attributes.widget;

import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.WidgetUtil;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.BorderToggleDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.FontStylePropertyDescriptorProvider;
import org.eclipse.birt.report.designer.ui.IReportGraphicConstants;
import org.eclipse.birt.report.designer.ui.ReportPlatformUIImages;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.accessibility.Accessible;
import org.eclipse.swt.accessibility.AccessibleAdapter;
import org.eclipse.swt.accessibility.AccessibleEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

/**
 */
public class TogglePropertyDescriptor extends PropertyDescriptor {

	Button button;

	public TogglePropertyDescriptor() {
		setFormStyle(false);
	}

	public void load() {
		String value = getDescriptorProvider().load().toString();
		boolean stateFlag = ((value == null) == button.getEnabled());
		if (stateFlag)
			button.setEnabled(value != null);
		if (getDescriptorProvider() instanceof FontStylePropertyDescriptorProvider) {
			String imageName = ((FontStylePropertyDescriptorProvider) getDescriptorProvider()).getImageName();
			if (!button.isEnabled())
				imageName += IReportGraphicConstants.DIS;
			if (button.getImage() == null)
				button.setImage(ReportPlatformUIImages.getImage(imageName));
			boolean boolValue = ((FontStylePropertyDescriptorProvider) getDescriptorProvider()).getToogleValue()
					.equals(value);
			if (button.getSelection() != boolValue) {
				button.setSelection(boolValue);
			}
			button.setToolTipText(((FontStylePropertyDescriptorProvider) getDescriptorProvider()).getTooltipText());
		}
		if (getDescriptorProvider() instanceof BorderToggleDescriptorProvider) {
			String imageName = ((BorderToggleDescriptorProvider) getDescriptorProvider()).getImageName();
			if (button.getImage() == null)
				button.setImage(ReportPlatformUIImages.getImage(imageName));
			boolean boolValue = ((Boolean) ((BorderToggleDescriptorProvider) getDescriptorProvider()).load())
					.booleanValue();
			if (button.getSelection() != boolValue) {
				button.setSelection(boolValue);
			}
			button.setToolTipText(((BorderToggleDescriptorProvider) getDescriptorProvider()).getTooltipText());
		}

		button.getAccessible().addAccessibleListener(new AccessibleAdapter() {
			public void getName(AccessibleEvent e) {
				Accessible accessible = (Accessible) e.getSource();
				Button item = (Button) accessible.getControl();
				if (item != null) {
					e.result = item.getToolTipText();
				}
			}
		});
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.views.attributes.widget.
	 * PropertyDescriptor#getControl()
	 */
	public Control getControl() {
		return button;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.ui.extensions.IPropertyDescriptor#
	 * createControl(org.eclipse.swt.widgets.Composite)
	 */
	public Control createControl(Composite parent) {
		button = new Button(parent, SWT.TOGGLE);
		button.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				handleSelectEvent();
			}
		});
		return button;
	}

	public void setToolTipText(String toolTip) {
		if (button != null)
			button.setToolTipText(toolTip);
	}

	public void save(Object value) throws SemanticException {
		descriptorProvider.save(value);
	}

	public void setHidden(boolean isHidden) {
		WidgetUtil.setExcludeGridData(button, isHidden);
	}

	public void setVisible(boolean isVisible) {
		button.setVisible(isVisible);
	}

	protected void handleSelectEvent() {
		try {
			save(Boolean.valueOf(button.getSelection()));
		} catch (SemanticException e1) {
			WidgetUtil.processError(button.getShell(), e1);
		}
	}

	public void setInput(Object handle) {
		this.input = handle;
		getDescriptorProvider().setInput(input);
	}
}
