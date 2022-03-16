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

import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.PathDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.SimpleComboPropertyDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.TextPropertyDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.SeperatorSection;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.SimpleComboSection;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.TextSection;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

/**
 * The general attribute page of Module element.
 */
public abstract class ModulePage extends AttributePage {

	public abstract String getElementType();

	@Override
	public void buildUI(Composite parent) {
		super.buildUI(parent);
		container.setLayout(WidgetUtil.createGridLayout(4, 15));

		TextPropertyDescriptorProvider authorProvider = new TextPropertyDescriptorProvider(ModuleHandle.AUTHOR_PROP,
				ReportDesignConstants.MODULE_ELEMENT);
		TextSection authorSection = new TextSection(authorProvider.getDisplayName(), container, true);
		authorSection.setProvider(authorProvider);
		authorSection.setWidth(500);
		authorSection.setGridPlaceholder(2, true);
		addSection(PageSectionId.MODULE_AUTHOR, authorSection);

		TextPropertyDescriptorProvider createdByProvider = new TextPropertyDescriptorProvider(
				ModuleHandle.CREATED_BY_PROP, getElementType());
		TextSection createdBySection = new TextSection(createdByProvider.getDisplayName(), container, true);
		createdBySection.setProvider(createdByProvider);
		createdBySection.setWidth(500);
		createdBySection.setGridPlaceholder(2, true);
		addSection(PageSectionId.MODULE_CREATED_BY, createdBySection);

		PathDescriptorProvider pathProvider = new PathDescriptorProvider();
		TextSection pathSection = new TextSection(pathProvider.getDisplayName(), container, true);
		pathSection.setProvider(pathProvider);
		pathSection.setWidth(500);
		pathSection.setGridPlaceholder(2, true);
		addSection(PageSectionId.MODULE_PATH, pathSection);

		TextPropertyDescriptorProvider titleProvider = new TextPropertyDescriptorProvider(ModuleHandle.TITLE_PROP,
				ReportDesignConstants.MODULE_ELEMENT);
		TextSection titleSection = new TextSection(titleProvider.getDisplayName(), container, true);
		titleSection.setProvider(titleProvider);
		titleSection.setWidth(500);
		titleSection.setGridPlaceholder(2, true);
		addSection(PageSectionId.MODULE_TITLE, titleSection);

		SeperatorSection seperatorSection = new SeperatorSection(container, SWT.HORIZONTAL);
		addSection(PageSectionId.MODULE_SEPERATOR, seperatorSection);

		SimpleComboPropertyDescriptorProvider themeProvider = new SimpleComboPropertyDescriptorProvider(
				ModuleHandle.THEME_PROP, getElementType());
		SimpleComboSection themeSection = new SimpleComboSection(themeProvider.getDisplayName(), container, true);
		themeSection.setProvider(themeProvider);
		themeSection.setWidth(500);
		themeSection.setGridPlaceholder(2, true);
		addSection(PageSectionId.MODULE_THEME, themeSection);

		createSections();
		layoutSections();
	}

}
