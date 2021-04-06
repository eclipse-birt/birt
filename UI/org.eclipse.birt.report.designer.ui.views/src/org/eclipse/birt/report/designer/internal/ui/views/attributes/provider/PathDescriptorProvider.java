
package org.eclipse.birt.report.designer.internal.ui.views.attributes.provider;

import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.osgi.util.TextProcessor;

public class PathDescriptorProvider extends AbstractDescriptorProvider implements ITextDescriptorProvider {

	private Object input;

	private static String PATH_DELIMETER = "/\\:."; //$NON-NLS-1$

	public boolean isEditable() {
		return false;
	}

	public String getDisplayName() {
		return Messages.getString("ModulePage.text.Path"); //$NON-NLS-1$
	}

	public Object load() {
		if (input == null)
			return ""; //$NON-NLS-1$
		DesignElementHandle handle = (DesignElementHandle) DEUtil.getInputFirstElement(input);
		if (handle != null)
			return TextProcessor.process(((ModuleHandle) handle).getFileName(), PATH_DELIMETER);
		return ""; //$NON-NLS-1$
	}

	public void save(Object value) throws SemanticException {
	}

	public void setInput(Object input) {
		this.input = input;
	}

}
