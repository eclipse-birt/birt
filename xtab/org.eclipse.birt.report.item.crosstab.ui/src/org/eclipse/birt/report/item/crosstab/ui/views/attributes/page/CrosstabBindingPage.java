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

package org.eclipse.birt.report.item.crosstab.ui.views.attributes.page;

import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.AggregateOnBindingPage;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.PageSectionId;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.AggregateOnBindingsFormHandleProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.IDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.SortingFormSection;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.widget.AggregateOnBindingsFormDescriptor;
import org.eclipse.birt.report.item.crosstab.ui.views.attributes.provider.CrosstabBindingsFormHandleProvider;
import org.eclipse.birt.report.item.crosstab.ui.views.attributes.provider.CrosstabBindingComboPropertyDescriptorProvider;
import org.eclipse.birt.report.item.crosstab.ui.views.attributes.section.CrosstabBindingComboSection;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.birt.report.model.elements.interfaces.IReportItemModel;

/**
 * 
 */

public class CrosstabBindingPage extends AggregateOnBindingPage {

	protected void applyCustomSections() {
		IDescriptorProvider cubeProvider = new CrosstabBindingComboPropertyDescriptorProvider(
				IReportItemModel.CUBE_PROP, ReportDesignConstants.EXTENDED_ITEM);
		CrosstabBindingComboSection cubeSection = new CrosstabBindingComboSection(cubeProvider.getDisplayName(),
				getSectionContainer(), true);
		cubeSection.setProvider(cubeProvider);
		cubeSection.setWidth(280);
		cubeSection.setGridPlaceholder(2, true);
		addSection(PageSectionId.BINDING_GROUP, cubeSection);

		AggregateOnBindingsFormHandleProvider crosstabFormProvider = new CrosstabBindingsFormHandleProvider();
		((SortingFormSection) getSection(PageSectionId.BINDING_DATASET_FORM))
				.setCustomForm(new AggregateOnBindingsFormDescriptor(true));
		((SortingFormSection) getSection(PageSectionId.BINDING_DATASET_FORM)).setProvider(crosstabFormProvider);

		if (((CrosstabBindingComboSection) getSection(PageSectionId.BINDING_GROUP)).getProvider() != null) {
			IDescriptorProvider crosstabProvider = ((CrosstabBindingComboSection) getSection(
					PageSectionId.BINDING_GROUP)).getProvider();
			if (cubeProvider instanceof CrosstabBindingComboPropertyDescriptorProvider)
				((CrosstabBindingComboPropertyDescriptorProvider) crosstabProvider)
						.setCrosstabSimpleComboSection(cubeSection);
		}
	}

}
