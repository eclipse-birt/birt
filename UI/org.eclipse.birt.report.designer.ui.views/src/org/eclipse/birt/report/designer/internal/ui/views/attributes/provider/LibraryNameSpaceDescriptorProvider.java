
package org.eclipse.birt.report.designer.internal.ui.views.attributes.provider;

import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.LibraryHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;

public class LibraryNameSpaceDescriptorProvider extends AbstractDescriptorProvider implements ITextDescriptorProvider {

	public boolean isEditable() {
		return false;
	}

	public String getDisplayName() {
		return Messages.getString("LibraryPage.Label.Namespace"); //$NON-NLS-1$
	}

	public Object load() {
		if (DEUtil.getInputSize(input) == 1 && DEUtil.getInputFirstElement(input) instanceof LibraryHandle) {
			LibraryHandle handle = (LibraryHandle) DEUtil.getInputFirstElement(input);
			if (DEUtil.isIncluded(handle)) {
				return handle.getNamespace();
			}
		}
		return ""; //$NON-NLS-1$
	}

	public void save(Object value) throws SemanticException {
		// TODO Auto-generated method stub

	}

	private Object input;

	public void setInput(Object input) {
		this.input = input;
	}

}
