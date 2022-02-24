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

import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.JarFileFormProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.JsFileFormProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.PropertiesFileFormProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.FormSection;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.widget.FileFormPropertyDescriptor;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.widget.FormPropertyDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

/**
 * 
 */

public class ResourcesPage extends LibraryAttributePage {

	public void buildUI(Composite parent) {
		super.buildUI(parent);
		// TODO Auto-generated method stub
		container.setLayout(WidgetUtil.createGridLayout(5, 15));

		needCheckLibraryReadOnly(true);

		PropertiesFileFormProvider propertiesFileProvider = new PropertiesFileFormProvider();
		FormSection propertiesFileSection = new FormSection(propertiesFileProvider.getDisplayName(), container, true);
		propertiesFileSection.setCustomForm(new FileFormPropertyDescriptor(true));
		propertiesFileSection.setButtonGroupIndex(0);
		propertiesFileSection.setProvider(propertiesFileProvider);
		propertiesFileSection.showDisplayLabel(true);
		propertiesFileSection.setButtonWithDialog(true);
		propertiesFileSection.setStyle(FormPropertyDescriptor.FULL_FUNCTION);
		propertiesFileSection.setFillForm(true);
		propertiesFileSection.setWidth(500);
		propertiesFileSection.setHeight(120);
		propertiesFileSection.setDisplayLabelStyle(SWT.HORIZONTAL);
		propertiesFileSection.setGridPlaceholder(1, true);
		addSection(PageSectionId.RESOURCE_INCLUDE, propertiesFileSection);

		JarFileFormProvider jarFileProvider = new JarFileFormProvider();
		FormSection jarFileSection = new FormSection(jarFileProvider.getDisplayName(), container, true);
		jarFileSection.setCustomForm(new FileFormPropertyDescriptor(true));
		jarFileSection.setButtonGroupIndex(1);
		jarFileSection.setProvider(jarFileProvider);
		jarFileSection.showDisplayLabel(true);
		jarFileSection.setButtonWithDialog(true);
		jarFileSection.setStyle(FormPropertyDescriptor.FULL_FUNCTION);
		jarFileSection.setFillForm(true);
		jarFileSection.setWidth(500);
		jarFileSection.setHeight(120);
		jarFileSection.setDisplayLabelStyle(SWT.HORIZONTAL);
		jarFileSection.setGridPlaceholder(1, true);
		addSection(PageSectionId.RESOURCE_JARFILE, jarFileSection);

		JsFileFormProvider jsFileProvider = new JsFileFormProvider();
		FormSection jsFileSection = new FormSection(jarFileProvider.getDisplayName(), container, true);
		jsFileSection.setCustomForm(new FileFormPropertyDescriptor(true));
		jsFileSection.setButtonGroupIndex(2);
		jsFileSection.setProvider(jsFileProvider);
		jsFileSection.showDisplayLabel(true);
		jsFileSection.setButtonWithDialog(true);
		jsFileSection.setStyle(FormPropertyDescriptor.FULL_FUNCTION);
		jsFileSection.setFillForm(true);
		jsFileSection.setWidth(500);
		jsFileSection.setHeight(120);
		jsFileSection.setDisplayLabelStyle(SWT.HORIZONTAL);
		jsFileSection.setGridPlaceholder(1, true);
		addSection(PageSectionId.RESOURCE_JAVASCRIPTFILE, jsFileSection);

		createSections();
		layoutSections();

	}
}
