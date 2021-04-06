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

package org.eclipse.birt.report.item.crosstab.ui.views.attributes.page;

import java.util.List;

import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.LibraryAttributePage;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.WidgetUtil;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.FormSection;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.widget.FormPropertyDescriptor;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.item.crosstab.core.ICrosstabConstants;
import org.eclipse.birt.report.item.crosstab.ui.views.attributes.provider.GrandTotalProvider;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.GroupElementHandle;
import org.eclipse.birt.report.model.api.activity.NotificationEvent;
import org.eclipse.swt.widgets.Composite;

/**
 * 
 */

public class RowGrandTotalPage extends LibraryAttributePage {

	private FormSection grandTotalSection;

	public void buildUI(Composite parent) {
		super.buildUI(parent);
		needCheckLibraryReadOnly(true);
		container.setLayout(WidgetUtil.createGridLayout(1));
		final GrandTotalProvider grandTotalProvider = new GrandTotalProvider();
		grandTotalProvider.setAxis(ICrosstabConstants.ROW_AXIS_TYPE);
		grandTotalSection = new FormSection(grandTotalProvider.getDisplayName(), container, true);
		grandTotalSection.setProvider(grandTotalProvider);
		grandTotalSection.setButtonWithDialog(true);
		grandTotalSection.setStyle(FormPropertyDescriptor.NO_UP_DOWN);
		grandTotalSection.setFillForm(true);
		grandTotalSection.setHeight(170);
		addSection(CrosstabPageSectionId.ROW_SUB_TOTALS, grandTotalSection);
		createSections();
		layoutSections();
	}

	public void addElementEvent(DesignElementHandle focus, NotificationEvent ev) {
		if (checkControl(grandTotalSection))
			grandTotalSection.getFormControl().addElementEvent(focus, ev);
	}

	public void clear() {
		if (checkControl(grandTotalSection))
			grandTotalSection.getFormControl().clear();
	}

	public void postElementEvent() {

		if (checkControl(grandTotalSection))
			grandTotalSection.getFormControl().postElementEvent();

	}

	private boolean checkControl(FormSection form) {
		return form != null && form.getFormControl() != null && !form.getFormControl().getControl().isDisposed();
	}

	protected boolean isLibraryReadOnly() {
		GroupElementHandle elementHandle = null;
		if (input instanceof GroupElementHandle) {
			elementHandle = ((GroupElementHandle) input);

		} else if (input instanceof List) {
			elementHandle = DEUtil.getGroupElementHandle((List) input);
		}
		if (elementHandle != null) {
			if (DEUtil.getMultiSelectionHandle(DEUtil.getInputElements(elementHandle)).isExtendedElements()) {
				return true;
			}
		}
		return false;
	}
}
