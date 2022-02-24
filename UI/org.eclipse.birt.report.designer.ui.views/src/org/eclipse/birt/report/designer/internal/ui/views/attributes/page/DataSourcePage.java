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

import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.ElementIdDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.IDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.TextPropertyDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.TextSection;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;

/**
 * 
 */

public class DataSourcePage extends GeneralPage {

	protected void buildContent() {

		// Defines provider.

		IDescriptorProvider nameProvider = new TextPropertyDescriptorProvider(ReportItemHandle.NAME_PROP,
				ReportDesignConstants.DATA_SOURCE_ELEMENT);

		// Defines section.

		TextSection nameSection = new TextSection(nameProvider.getDisplayName(), container, true);

		nameSection.setProvider(nameProvider);
		nameSection.setWidth(500);
		nameSection.setGridPlaceholder(4, true);

		ElementIdDescriptorProvider elementIdProvider = new ElementIdDescriptorProvider();
		TextSection elementIdSection = new TextSection(elementIdProvider.getDisplayName(), container, true);
		elementIdSection.setProvider(elementIdProvider);
		elementIdSection.setWidth(500);
		elementIdSection.setGridPlaceholder(4, true);

		// Adds section into this page.

		addSection(PageSectionId.DATA_SOURCE_NAME, nameSection); // $NON-NLS-1$
		addSection(PageSectionId.DATA_SOURCE_ELEMENT_ID, elementIdSection);

	}

	public boolean canReset() {
		return false;
	}
}
