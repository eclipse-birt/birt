/*******************************************************************************
 * Copyright (c) 2021 Contributors to the Eclipse Foundation
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 *   See git history
 *******************************************************************************/

package org.eclipse.birt.report.designer.internal.ui.views.attributes.provider;

import java.net.URL;
import java.util.List;

import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.IResourceLocator;

public class ResourceKeyDescriptorProvider extends PropertyDescriptorProvider
		implements IResourceKeyDescriptorProvider {

	private int groupIndex;

	public void setGroupIndex(int groupIndex) {
		this.groupIndex = groupIndex;
	}

	public ResourceKeyDescriptorProvider(String property, String element) {
		super(property, element);
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

	public String getBrowseText() {
		return groupIndex == 0 ? Messages.getString("ResourceKeyDescriptor.text.Browse")
				: Messages.getString("ResourceKeyDescriptor.text.Browse.Alt"); //$NON-NLS-1$ //$NON-NLS-2$
	}

	public String getResetText() {
		return groupIndex == 0 ? Messages.getString("ResourceKeyDescriptor.text.Reset")
				: Messages.getString("ResourceKeyDescriptor.text.Reset.Alt"); //$NON-NLS-1$ //$NON-NLS-2$
	}

	public boolean isEnable() {
		return !(DEUtil.getInputSize(input) > 1);
	}

	public String getBrowseTooltipText() {
		return Messages.getString("ResourceKeyDescriptor.button.browse.tooltip"); //$NON-NLS-1$
	}

	public String getResetTooltipText() {
		return Messages.getString("ResourceKeyDescriptor.button.reset.tooltip"); //$NON-NLS-1$
	}
}
