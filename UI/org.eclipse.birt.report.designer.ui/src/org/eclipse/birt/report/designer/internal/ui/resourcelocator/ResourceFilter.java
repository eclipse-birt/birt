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

package org.eclipse.birt.report.designer.internal.ui.resourcelocator;

import org.eclipse.birt.report.designer.nls.Messages;

public class ResourceFilter {

	public static final String FILTER_CVS_RESOURCES = "cvs";//$NON-NLS-1$

	public static final String FILTER_DOT_RESOURCES = ".";//$NON-NLS-1$
	public static final String FILTER_EMPTY_FOLDERS = "empty_folder";//$NON-NLS-1$
	// public static final String FILTER_FOLDERS_WITHOUTRESOURCE =
	// "folders_withoutresource";//$NON-NLS-1$

	private String type = ""; //$NON-NLS-1$
	private String displayName = ""; //$NON-NLS-1$
	private boolean isEnabled;
	private String description = ""; //$NON-NLS-1$

	public ResourceFilter() {
	}

	public ResourceFilter(String type, String displayName, boolean isEnabled) {
		setType(type);
		setDisplayName(displayName);
		setEnabled(isEnabled);
	}

	public ResourceFilter(String type, String displayName, boolean isEnabled, String helpContent) {
		setType(type);
		setDisplayName(displayName);
		setEnabled(isEnabled);
		setDescription(helpContent);
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public boolean isEnabled() {
		return isEnabled;
	}

	public void setEnabled(boolean isEnabled) {
		this.isEnabled = isEnabled;
	}

	public static ResourceFilter generateCVSFilter() {
		return new ResourceFilter(ResourceFilter.FILTER_CVS_RESOURCES,
				Messages.getString("ResourceFilter.DisplayName.CVS"), //$NON-NLS-1$
				true, Messages.getString("ResourceFilter.Description.CVS"));//$NON-NLS-1$
	}

	public static ResourceFilter generateDotResourceFilter() {
		return new ResourceFilter(ResourceFilter.FILTER_DOT_RESOURCES,
				Messages.getString("ResourceFilter.DisplayName.Dot"), //$NON-NLS-1$
				true, Messages.getString("ResourceFilter.Description.Dot"));//$NON-NLS-1$
	}

	public static ResourceFilter generateEmptyFolderFilter() {
		return new ResourceFilter(ResourceFilter.FILTER_EMPTY_FOLDERS,
				Messages.getString("ResourceFilter.DisplayName.EmptyFolder"), //$NON-NLS-1$
				false, Messages.getString("ResourceFilter.Description.EmptyFolder"));//$NON-NLS-1$
	}

	/*
	 * public static ResourceFilter generateNoResourceInFolderFilter( ) { return new
	 * ResourceFilter( ResourceFilter.FILTER_FOLDERS_WITHOUTRESOURCE,
	 * "Folders without any needed resources", false,
	 * "Hides folders that they and their subfolders don't contain any needed resources"
	 * ); }
	 */

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getType() {
		return type;
	}

}
