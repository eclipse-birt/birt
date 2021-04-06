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

import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.MarginsPropertyDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.MarginsSection;
import org.eclipse.birt.report.model.api.MasterPageHandle;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.swt.widgets.Composite;

/**
 * The margins attribute page of Marster-Page element.
 */
public class MarginsPage extends AttributePage {

	public void buildUI(Composite parent) {
		super.buildUI(parent);
		container.setLayout(WidgetUtil.createGridLayout(8, 15));

		MarginsPropertyDescriptorProvider leftProvider = new MarginsPropertyDescriptorProvider(
				MasterPageHandle.LEFT_MARGIN_PROP, ReportDesignConstants.MASTER_PAGE_ELEMENT);
		MarginsSection leftSection = new MarginsSection(leftProvider.getDisplayName(), container, true);
		leftSection.setProvider(leftProvider);
		leftSection.setLayoutNum(2);
		leftSection.setWidth(120);
		addSection(PageSectionId.MARGINS_LEFT, leftSection);

		MarginsPropertyDescriptorProvider topProvider = new MarginsPropertyDescriptorProvider(
				MasterPageHandle.TOP_MARGIN_PROP, ReportDesignConstants.MASTER_PAGE_ELEMENT);
		MarginsSection topSection = new MarginsSection(topProvider.getDisplayName(), container, true);
		topSection.setProvider(topProvider);
		topSection.setLayoutNum(2);
		topSection.setWidth(120);
		addSection(PageSectionId.MARGINS_TOP, topSection);

		MarginsPropertyDescriptorProvider rightProvider = new MarginsPropertyDescriptorProvider(
				MasterPageHandle.RIGHT_MARGIN_PROP, ReportDesignConstants.MASTER_PAGE_ELEMENT);
		MarginsSection rightSection = new MarginsSection(rightProvider.getDisplayName(), container, true);
		rightSection.setProvider(rightProvider);
		rightSection.setLayoutNum(2);
		rightSection.setWidth(120);
		addSection(PageSectionId.MARGINS_RIGHT, rightSection);

		MarginsPropertyDescriptorProvider bottomProvider = new MarginsPropertyDescriptorProvider(
				MasterPageHandle.BOTTOM_MARGIN_PROP, ReportDesignConstants.MASTER_PAGE_ELEMENT);
		MarginsSection bottomSection = new MarginsSection(bottomProvider.getDisplayName(), container, true);
		bottomSection.setProvider(bottomProvider);
		bottomSection.setLayoutNum(2);
		bottomSection.setWidth(120);
		addSection(PageSectionId.MARGINS_BOTTOM, bottomSection);

		createSections();
		layoutSections();
	}
}