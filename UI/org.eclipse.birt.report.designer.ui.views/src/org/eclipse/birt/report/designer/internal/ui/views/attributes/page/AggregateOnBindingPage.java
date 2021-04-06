
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
