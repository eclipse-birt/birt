
package org.eclipse.birt.report.designer.internal.ui.views.attributes.provider;

import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.util.ExceptionUtil;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.ListingHandle;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;

public class RepeatHeaderDescriptorProvider extends AbstractDescriptorProvider {

	public String getDisplayName() {
		return Messages.getString("ListingSectionPage.RepeatHeader"); //$NON-NLS-1$
	}

	public Object load() {
		if (DEUtil.getInputSize(input) == 1 && DEUtil.getInputFirstElement(input) instanceof ListingHandle) {
			ListingHandle listingHandle = (ListingHandle) DEUtil.getInputFirstElement(input);

			return Boolean.valueOf(listingHandle.repeatHeader()).toString();

		}
		return Boolean.FALSE.toString(); // $NON-NLS-1$
	}

	public void save(Object value) throws SemanticException {
		if (DEUtil.getInputSize(input) == 1 && DEUtil.getInputFirstElement(input) instanceof ListingHandle) {
			ListingHandle listingHandle = (ListingHandle) DEUtil.getInputFirstElement(input);
			try {
				PropertyHandle propertyHandle = listingHandle.getPropertyHandle(ListingHandle.REPEAT_HEADER_PROP);
				if (propertyHandle != null && propertyHandle.getValue() != null
						&& propertyHandle.getValue().equals(value)) {
					return;
				}
				listingHandle.setProperty(ListingHandle.REPEAT_HEADER_PROP, value);
			} catch (SemanticException e) {
				ExceptionUtil.handle(e);
			}

		}
	}

	private Object input;

	public void setInput(Object input) {
		this.input = input;

	}

}
