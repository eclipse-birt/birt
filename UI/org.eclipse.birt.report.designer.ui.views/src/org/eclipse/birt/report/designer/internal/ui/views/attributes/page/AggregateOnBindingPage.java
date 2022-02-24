/*******************************************************************************
 * Copyright (c) 2021 Contributors to the Eclipse Foundation
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 *   See git history
 *******************************************************************************/

package org.eclipse.birt.report.designer.internal.ui.views.attributes.page;

import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.AggregateOnBindingsFormHandleProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.BindingGroupDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.IDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.BindingGroupSection;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.SortingFormSection;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.widget.AggregateOnBindingsFormDescriptor;

public class AggregateOnBindingPage extends BindingPage {

	protected void applyCustomSections() {
		AggregateOnBindingsFormHandleProvider dataSetFormProvider = new AggregateOnBindingsFormHandleProvider();
		((SortingFormSection) getSection(PageSectionId.BINDING_DATASET_FORM))
				.setCustomForm(new AggregateOnBindingsFormDescriptor(true));
		((SortingFormSection) getSection(PageSectionId.BINDING_DATASET_FORM)).setProvider(dataSetFormProvider);
		if (((BindingGroupSection) getSection(PageSectionId.BINDING_GROUP)).getProvider() != null) {
			IDescriptorProvider dataSetProvider = ((BindingGroupSection) getSection(PageSectionId.BINDING_GROUP))
					.getProvider();
			if (dataSetProvider instanceof BindingGroupDescriptorProvider)
				((BindingGroupDescriptorProvider) dataSetProvider).setDependedProvider(dataSetFormProvider);
		}
	}
}
