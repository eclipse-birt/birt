
package org.eclipse.birt.report.designer.internal.ui.views.attributes.provider;

import java.util.List;

import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.dialogs.ImageBuilder;
import org.eclipse.birt.report.designer.ui.util.UIUtil;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.CommandStack;
import org.eclipse.birt.report.model.api.GroupElementHandle;
import org.eclipse.birt.report.model.api.ImageHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.jface.window.Window;

public class ReferenceDescriptorProvider extends AbstractDescriptorProvider implements ITextDescriptorProvider {

	public boolean isEditable() {
		return false;
	}

	public String getDisplayName() {
		return Messages.getString("ReferencePage.Label.Source"); //$NON-NLS-1$
	}

	private String property;

	private boolean isEnableButton;

	public boolean isEnableButton() {
		return isEnableButton;
	}

	public Object load() {
		String source = ((ImageHandle) DEUtil.getInputFirstElement(input)).getSource();
		if (source.equals(DesignChoiceConstants.IMAGE_REF_TYPE_EMBED)) {
			property = ImageHandle.IMAGE_NAME_PROP;
		} else if (source.equals(DesignChoiceConstants.IMAGE_REF_TYPE_EXPR)) {
			property = ImageHandle.VALUE_EXPR_PROP;
		} else {
			property = ImageHandle.URI_PROP;
		}
		return getStringValue();
	}

	public void handleSelectEvent() {
		CommandStack stack = SessionHandleAdapter.getInstance().getCommandStack();
		ImageBuilder dialog = new ImageBuilder(UIUtil.getDefaultShell(), ImageBuilder.DLG_TITLE_EDIT);
		dialog.setInput(DEUtil.getInputFirstElement(input));
		stack.startTrans(Messages.getString("ImageEditPart.trans.editImage")); //$NON-NLS-1$
		if (Window.OK == dialog.open()) {
			stack.commit();
		} else {
			stack.rollback();
		}
	}

	private Object input;

	public void setInput(Object input) {
		this.input = input;
	}

	private String getStringValue() {
		String value = null;
		if (input instanceof GroupElementHandle) {
			value = ((GroupElementHandle) input).getStringProperty(property);
		} else if (input instanceof List) {
			value = DEUtil.getGroupElementHandle((List) input).getStringProperty(property);
		}
		if (value == null)
			isEnableButton = false;
		else
			isEnableButton = true;
		return value == null ? "" : value; //$NON-NLS-1$
	}

	public void save(Object value) throws SemanticException {
		// TODO Auto-generated method stub

	}

}
