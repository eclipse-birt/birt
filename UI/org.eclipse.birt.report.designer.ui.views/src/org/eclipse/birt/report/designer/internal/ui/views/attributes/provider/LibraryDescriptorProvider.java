
package org.eclipse.birt.report.designer.internal.ui.views.attributes.provider;

import java.io.File;
import java.net.URL;

import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;

public class LibraryDescriptorProvider extends AbstractDescriptorProvider implements ITextDescriptorProvider {

	private Object input;

	public boolean isEditable() {
		return false;
	}

	public String getDisplayName() {
		return Messages.getString("GeneralPage.Library.Included"); //$NON-NLS-1$
	}

	public Object load() {
		if (input == null)
			return ""; //$NON-NLS-1$
		DesignElementHandle handle = (DesignElementHandle) DEUtil.getInputFirstElement(input);
		if (handle.getExtends() == null)
			return ""; //$NON-NLS-1$
		String filePath = null;
		try {
			filePath = DEUtil.getFilePathFormURL(new URL(handle.getExtends().getRoot().getFileName()));

		} catch (Exception e) {
			filePath = handle.getExtends().getRoot().getFileName();
		}
		if (filePath != null) {
			File libraryFile = new File(filePath);
			if (libraryFile.exists())
				return libraryFile.getAbsolutePath();
		}
		return ""; //$NON-NLS-1$
	}

	public void save(Object value) throws SemanticException {
	}

	public void setInput(Object input) {
		this.input = input;
	}

}
