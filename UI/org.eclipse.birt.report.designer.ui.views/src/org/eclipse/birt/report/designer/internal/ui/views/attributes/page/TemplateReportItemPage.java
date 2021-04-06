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

package org.eclipse.birt.report.designer.internal.ui.views.attributes.page;

import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.DescriptionDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.TextPropertyDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.TextSection;
import org.eclipse.birt.report.model.api.TemplateReportItemHandle;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

/**
 * 
 */

public class TemplateReportItemPage extends AttributePage {

	public void buildUI(Composite parent) {
		super.buildUI(parent);
		container.setLayout(WidgetUtil.createGridLayout(3, 15));

		TextPropertyDescriptorProvider nameProvider = new TextPropertyDescriptorProvider(
				TemplateReportItemHandle.NAME_PROP, ReportDesignConstants.TEMPLATE_REPORT_ITEM);
		TextSection nameSection = new TextSection(nameProvider.getDisplayName(), container, true);
		nameSection.setProvider(nameProvider);
		nameSection.setGridPlaceholder(1, true);
		nameSection.setWidth(500);
		addSection(PageSectionId.TEMPLATE_REPORTITEM_I18N_NAME, nameSection);

		DescriptionDescriptorProvider descriptionProvider = new DescriptionDescriptorProvider();
		TextSection descriptionSection = new TextSection(descriptionProvider.getDisplayName(), container, true);
		descriptionSection.setStyle(SWT.MULTI | SWT.WRAP | SWT.H_SCROLL | SWT.V_SCROLL);
		descriptionSection.setProvider(descriptionProvider);
		descriptionSection.setWidth(500);
		descriptionSection.setGridPlaceholder(1, true);
		descriptionSection.setFillText(true);
		addSection(PageSectionId.TEMPLATE_REPORTITEM_LABEL_DESCRIPTION, descriptionSection);

		createSections();
		layoutSections();

	}
}
