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

package org.eclipse.birt.report.designer.internal.ui.views.attributes.page;

import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.TextPropertyDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.TextSection;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

/**
 * The comments attribute page of Report element.
 */
public class DescriptionPage extends AttributePage {
	@Override
	public void buildUI(Composite parent) {
		super.buildUI(parent);
		container.setLayout(WidgetUtil.createGridLayout(2, 15));

		TextPropertyDescriptorProvider descriptorProvider = new TextPropertyDescriptorProvider(
				ReportDesignHandle.DESCRIPTION_PROP, ReportDesignConstants.REPORT_DESIGN_ELEMENT);
		TextSection discriptorSection = new TextSection(descriptorProvider.getDisplayName(), container, true);
		discriptorSection.setStyle(SWT.MULTI | SWT.WRAP | SWT.H_SCROLL | SWT.V_SCROLL);
		discriptorSection.setProvider(descriptorProvider);
		discriptorSection.setWidth(500);
		discriptorSection.setHeight(200);
		discriptorSection.setFillText(true);
		addSection(PageSectionId.DISCRIPTOR_DISCRIPTOR, discriptorSection);

		createSections();
		layoutSections();

	}
}
