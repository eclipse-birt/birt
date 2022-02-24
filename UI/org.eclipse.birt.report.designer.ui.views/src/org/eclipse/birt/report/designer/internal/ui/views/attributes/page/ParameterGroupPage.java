/*******************************************************************************
 * Copyright (c) 2007 Actuate Corporation.
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

import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.IDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.TextPropertyDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.TextSection;
import org.eclipse.birt.report.model.api.ParameterGroupHandle;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;

/**
 * 
 */

public class ParameterGroupPage extends GeneralPage {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.views.attributes.page.
	 * GeneralPage#buildContent()
	 */
	@Override
	protected void buildContent() {
		// TODO Auto-generated method stub

		IDescriptorProvider nameProvider = new TextPropertyDescriptorProvider(ParameterGroupHandle.NAME_PROP,
				ReportDesignConstants.PARAMETER_GROUP_ELEMENT);

		TextSection nameSection = new TextSection(nameProvider.getDisplayName(), container, true);
		nameSection.setProvider(nameProvider);
		nameSection.setWidth(500);
		nameSection.setGridPlaceholder(4, true);
		addSection(PageSectionId.PARAMTER_GROUP_NAME, nameSection);

		IDescriptorProvider displayNameProvider = new TextPropertyDescriptorProvider(
				ParameterGroupHandle.DISPLAY_NAME_PROP, ReportDesignConstants.PARAMETER_GROUP_ELEMENT);

		TextSection displayNameSection = new TextSection(displayNameProvider.getDisplayName(), container, true);
		displayNameSection.setProvider(displayNameProvider);
		displayNameSection.setWidth(500);
		displayNameSection.setGridPlaceholder(4, true);
		addSection(PageSectionId.PARAMTER_GROUP_DISPLAY_NAME, displayNameSection);
	}

	public boolean canReset() {
		return false;
	}
}
