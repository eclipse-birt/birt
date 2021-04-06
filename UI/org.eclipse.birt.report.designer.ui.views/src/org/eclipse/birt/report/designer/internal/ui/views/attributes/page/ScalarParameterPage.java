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

import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.ElementIdDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.IDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.TextPropertyDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.TextSection;
import org.eclipse.birt.report.model.api.ScalarParameterHandle;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;

/**
 * 
 */

public class ScalarParameterPage extends GeneralPage {

	protected void buildContent() {

		// Defines providers.

		IDescriptorProvider nameProvider = new TextPropertyDescriptorProvider(ScalarParameterHandle.NAME_PROP,
				ReportDesignConstants.SCALAR_PARAMETER_ELEMENT);

		IDescriptorProvider dataTypeProvider = new TextPropertyDescriptorProvider(ScalarParameterHandle.DATA_TYPE_PROP,
				ReportDesignConstants.SCALAR_PARAMETER_ELEMENT);

		IDescriptorProvider ctrlTypeProvider = new TextPropertyDescriptorProvider(
				ScalarParameterHandle.CONTROL_TYPE_PROP, ReportDesignConstants.SCALAR_PARAMETER_ELEMENT);

		// Defines sections.

		TextSection nameSection = new TextSection(nameProvider.getDisplayName(), container, true);

		TextSection dataTypeSection = new TextSection(dataTypeProvider.getDisplayName(), container, true);

		TextSection ctrlTypeSection = new TextSection(ctrlTypeProvider.getDisplayName(), container, true);

		// Sets providers.

		nameSection.setProvider(nameProvider);
		dataTypeSection.setProvider(dataTypeProvider);
		ctrlTypeSection.setProvider(ctrlTypeProvider);

		// Sets widths.

		nameSection.setWidth(500);
		dataTypeSection.setWidth(500);
		ctrlTypeSection.setWidth(500);

		// Sets fill grid num.

		nameSection.setGridPlaceholder(4, true);
		dataTypeSection.setGridPlaceholder(4, true);
		ctrlTypeSection.setGridPlaceholder(4, true);

		// Adds section into container page.

		addSection(PageSectionId.SCALAR_PARAMETER_NAME, nameSection); // $NON-NLS-1$

		ElementIdDescriptorProvider elementIdProvider = new ElementIdDescriptorProvider();
		TextSection elementIdSection = new TextSection(elementIdProvider.getDisplayName(), container, true);
		elementIdSection.setProvider(elementIdProvider);
		elementIdSection.setWidth(500);
		elementIdSection.setGridPlaceholder(4, true);
		addSection(PageSectionId.SCALAR_PARAMETER_ELEMENT_ID, elementIdSection);

		addSection(PageSectionId.SCALAR_PARAMETER_DATA_TYPE, dataTypeSection); // $NON-NLS-1$
		addSection(PageSectionId.SCALAR_PARAMETER_CTRL_TYPE, ctrlTypeSection); // $NON-NLS-1$

	}

	public boolean canReset() {
		return false;
	}
}
