/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.designer.internal.ui.dialogs.resource;

import org.eclipse.birt.report.designer.internal.ui.resourcelocator.FragmentResourceEntry;
import org.eclipse.birt.report.designer.internal.ui.resourcelocator.PathResourceEntry;
import org.eclipse.birt.report.designer.internal.ui.resourcelocator.ResourceEntry;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.ReportPlugin;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;

/**
 * AddImageResourceForNewTemplateWizard
 */
public class AddImageResourceForNewTemplateWizard extends AddImageResourceFileFolderSelectionDialog {

	private IPath containerFullPath;

	public void setContainerFullPath(IPath path) {
		this.containerFullPath = path;
	}

	protected ResourceEntry[] getAllRootEntries(String[] fileNamePattern) {
		ResourceEntry systemResource = new FragmentResourceEntry(fileNamePattern);
		ResourceEntry templateResource = new FragmentResourceEntry(fileNamePattern,
				Messages.getString("FragmentTemplateResourceEntry.RootName"), //$NON-NLS-1$
				Messages.getString("FragmentTemplateResourceEntry.RootDisplayName"), //$NON-NLS-1$
				FragmentResourceEntry.TEMPLATE_ROOT);
		ResourceEntry sharedResource = null;
		String resourceFolder = getResourceFolder();
		if ((resourceFolder == null || resourceFolder.length() == 0) && containerFullPath != null) {
			IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(containerFullPath.segment(0));

			IResource resource = ResourcesPlugin.getWorkspace().getRoot().findMember(project.getFullPath());

			sharedResource = new PathResourceEntry(fileNamePattern, resource.getLocation().toOSString());
		} else {
			sharedResource = new PathResourceEntry(fileNamePattern);
		}

		return new ResourceEntry[] { systemResource, templateResource, sharedResource };

	}

	public String getResourceFolder() {
		String resourceFolder = ReportPlugin.getDefault().getPreferenceStore()
				.getString(ReportPlugin.RESOURCE_PREFERENCE);
		if (resourceFolder != null && resourceFolder.length() > 0) {
			return resourceFolder;
		} else {
			return null;
		}

	}
}
