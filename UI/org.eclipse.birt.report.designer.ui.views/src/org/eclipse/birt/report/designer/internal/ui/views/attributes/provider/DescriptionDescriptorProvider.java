
package org.eclipse.birt.report.designer.internal.ui.views.attributes.provider;

import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.TemplateReportItemHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;

public class DescriptionDescriptorProvider extends AbstractDescriptorProvider implements ITextDescriptorProvider {

	public boolean isEditable() {
		return true;
	}

	public String getDisplayName() {
		return Messages.getString("TemplateReportItemPage.description.Label.Instructions"); //$NON-NLS-1$
	}

	public Object load() {
		String result = null;
		if (DEUtil.getInputSize(input) == 1 && DEUtil.getInputFirstElement(input) instanceof TemplateReportItemHandle) {
			TemplateReportItemHandle handle = (TemplateReportItemHandle) DEUtil.getInputFirstElement(input);
			if (handle != null)
				result = handle.getDescription();
		}
		if (result == null)
			return ""; //$NON-NLS-1$
		else
			return result.trim();
	}

	public void save(Object value) throws SemanticException {
		if (value != null && DEUtil.getInputSize(input) == 1
				&& DEUtil.getInputFirstElement(input) instanceof TemplateReportItemHandle) {
			TemplateReportItemHandle handle = (TemplateReportItemHandle) DEUtil.getInputFirstElement(input);
			try {
				String desc = value.toString().trim();
				handle.setDescription(desc);
			} catch (SemanticException e1) {
				e1.printStackTrace();
			}
		}
	}

	private Object input;

	public void setInput(Object input) {
		this.input = input;
	}

}
