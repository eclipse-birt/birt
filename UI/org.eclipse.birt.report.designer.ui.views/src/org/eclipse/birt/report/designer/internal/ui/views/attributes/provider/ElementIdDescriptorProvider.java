
package org.eclipse.birt.report.designer.internal.ui.views.attributes.provider;

import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;

public class ElementIdDescriptorProvider extends AbstractDescriptorProvider implements ITextDescriptorProvider {

	public boolean isEditable() {
		return false;
	}

	public String getDisplayName() {
		return Messages.getString("ElementIdDescriptorProvider.Display.Element.ID"); //$NON-NLS-1$
	}

	public Object load() {
		if (DEUtil.getInputSize(input) != 1)
			return null;
		else
			return "" //$NON-NLS-1$
					+ ((DesignElementHandle) DEUtil.getInputFirstElement(input)).getID();
	}

	public void save(Object value) throws SemanticException {
		// TODO Auto-generated method stub

	}

	private Object input;

	public void setInput(Object input) {
		this.input = input;
	}

}
