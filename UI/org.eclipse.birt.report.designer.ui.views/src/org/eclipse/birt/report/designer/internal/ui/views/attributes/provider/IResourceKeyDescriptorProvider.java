package org.eclipse.birt.report.designer.internal.ui.views.attributes.provider;

import java.net.URL;

public interface IResourceKeyDescriptorProvider extends IDescriptorProvider {

	public String[] getBaseNames();

	public URL[] getResourceURLs();

	public String getBrowseText();

	public String getResetText();

	public boolean isEnable();

	public String getBrowseTooltipText();

	public String getResetTooltipText();

}
