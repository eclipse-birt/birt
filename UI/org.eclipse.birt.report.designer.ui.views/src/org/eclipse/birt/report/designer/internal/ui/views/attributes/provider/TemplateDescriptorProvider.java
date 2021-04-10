
package org.eclipse.birt.report.designer.internal.ui.views.attributes.provider;

import java.net.URL;
import java.util.List;

import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.IResourceLocator;
import org.eclipse.birt.report.model.api.TemplateReportItemHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;

public class TemplateDescriptorProvider extends AbstractDescriptorProvider implements IResourceKeyDescriptorProvider {

	public String getBrowseText() {
		return Messages.getString("ResourceKeyDescriptor.text.Browse"); //$NON-NLS-1$
	}

	public String getResetText() {
		return Messages.getString("ResourceKeyDescriptor.text.Reset"); //$NON-NLS-1$
	}

	public boolean isEnable() {
		return !(DEUtil.getInputSize(input) > 1);
	}

	public String getDisplayName() {
		return Messages.getString("TemplateReportItemPageGenerator.List.TextKey"); //$NON-NLS-1$
	}

	public Object load() {
		String key = ""; //$NON-NLS-1$
		if (DEUtil.getInputSize(input) == 1 && DEUtil.getInputFirstElement(input) instanceof TemplateReportItemHandle) {
			TemplateReportItemHandle handle = (TemplateReportItemHandle) DEUtil.getInputFirstElement(input);
			key = (handle.getDescriptionKey() == null) ? "" //$NON-NLS-1$
					: handle.getDescriptionKey().trim();
		}
		return key;
	}

	public void save(Object value) throws SemanticException {
		if (DEUtil.getInputSize(input) == 1 && DEUtil.getInputFirstElement(input) instanceof TemplateReportItemHandle) {
			TemplateReportItemHandle handle = (TemplateReportItemHandle) DEUtil.getInputFirstElement(input);
			if (handle != null) {
				String key = null;
				if (value instanceof String)
					key = value.toString();

				if (handle.getDescriptionKey() != null && handle.getDescriptionKey().equals(value)) {
					return;
				}

				handle.setDescriptionKey(key);
			}
		}

	}

	private Object input;

	public void setInput(Object input) {
		this.input = input;
	}

	public String getBrowseTooltipText() {
		return Messages.getString("ResourceKeyDescriptor.button.browse.tooltip"); //$NON-NLS-1$
	}

	public String getResetTooltipText() {

		return Messages.getString("ResourceKeyDescriptor.button.reset.tooltip"); //$NON-NLS-1$
	}

	public String[] getBaseNames() {
		List<String> resources = SessionHandleAdapter.getInstance().getReportDesignHandle().getIncludeResources();
		if (resources == null)
			return null;
		else
			return resources.toArray(new String[0]);
	}

	public URL[] getResourceURLs() {
		String[] baseNames = getBaseNames();
		if (baseNames == null)
			return null;
		else {
			URL[] urls = new URL[baseNames.length];
			for (int i = 0; i < baseNames.length; i++) {
				urls[i] = SessionHandleAdapter.getInstance().getReportDesignHandle().findResource(baseNames[i],
						IResourceLocator.MESSAGE_FILE);
			}
			return urls;
		}
	}

}
