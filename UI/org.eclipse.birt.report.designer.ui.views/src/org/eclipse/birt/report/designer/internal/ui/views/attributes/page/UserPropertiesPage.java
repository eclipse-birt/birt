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

import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.UserPropertiesHandleProvier;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.FormSection;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.widget.FormPropertyDescriptor;
import org.eclipse.swt.widgets.Composite;

/**
 * 
 */

public class UserPropertiesPage extends LibraryAttributePage {

	public void buildUI(Composite parent) {
		super.buildUI(parent);
		needCheckLibraryReadOnly(true);
		container.setLayout(WidgetUtil.createGridLayout(1));
		UserPropertiesHandleProvier userPropertyProvider = new UserPropertiesHandleProvier();
		FormSection userPropertySection = new FormSection(userPropertyProvider.getDisplayName(), container, true);
		userPropertySection.setProvider(userPropertyProvider);
		userPropertySection.setButtonWithDialog(true);
		userPropertySection.setStyle(FormPropertyDescriptor.SIMPLE_FUNCTION);
		userPropertySection.setFillForm(true);
		userPropertySection.setHeight(200);
		addSection(PageSectionId.USER_PROPERTIES, userPropertySection);
		createSections();
		layoutSections();
	}

}
