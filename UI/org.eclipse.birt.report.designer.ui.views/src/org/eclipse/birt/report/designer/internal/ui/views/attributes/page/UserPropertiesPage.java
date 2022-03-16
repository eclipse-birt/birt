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

import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.UserPropertiesHandleProvier;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.FormSection;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.widget.FormPropertyDescriptor;
import org.eclipse.swt.widgets.Composite;

/**
 *
 */

public class UserPropertiesPage extends LibraryAttributePage {

	@Override
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
