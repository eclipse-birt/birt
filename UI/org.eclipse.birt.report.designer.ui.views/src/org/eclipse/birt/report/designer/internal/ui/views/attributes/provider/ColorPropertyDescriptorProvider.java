
package org.eclipse.birt.report.designer.internal.ui.views.attributes.provider;

import org.eclipse.birt.report.designer.ui.views.attributes.providers.ChoiceSetFactory;
import org.eclipse.birt.report.model.api.metadata.IChoiceSet;

public class ColorPropertyDescriptorProvider extends PropertyDescriptorProvider {

	public ColorPropertyDescriptorProvider(String property, String element) {
		super(property, element);
	}

	public IChoiceSet getElementChoiceSet() {
		return ChoiceSetFactory.getElementChoiceSet(getElement(), getProperty());
	}

}
