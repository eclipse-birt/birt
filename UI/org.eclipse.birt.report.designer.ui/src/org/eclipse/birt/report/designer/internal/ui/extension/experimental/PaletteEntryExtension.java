/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.designer.internal.ui.extension.experimental;

import org.eclipse.birt.report.designer.internal.ui.command.CommandUtils;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.jface.resource.ImageDescriptor;

/**
 * PaletteEntryExtension
 */
public class PaletteEntryExtension {

	private String itemName;
	private String label;
	private String menuLabel;
	private String description;
	private ImageDescriptor icon;
	private ImageDescriptor iconLarge;
	private String category;
	private String categoryDisplayName;
	private String command;

	public String getLabel() {
		if (itemName != null && DEUtil.getMetaDataDictionary().getExtension(itemName) != null)
			return DEUtil.getMetaDataDictionary().getExtension(itemName).getDisplayName();
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public ImageDescriptor getIcon() {
		return icon;
	}

	public void setIcon(ImageDescriptor icon) {
		this.icon = icon;
	}

	public ImageDescriptor getIconLarge() {
		return iconLarge;
	}

	public void setIconLarge(ImageDescriptor iconLarge) {
		this.iconLarge = iconLarge;
	}

	public String getCategory() {
		return category;
	}

	public String getCategoryDisplayName() {
		return categoryDisplayName;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public void setCategoryDisplayName(String displayName) {
		this.categoryDisplayName = displayName;
	}

	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public Object executeCreate() throws Exception {
		if (getCommand() != null) {
			return CommandUtils.executeCommand(getCommand());
		}
		throw new Exception("create command not specified."); //$NON-NLS-1$
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getMenuLabel() {
		return menuLabel;
	}

	public void setMenuLabel(String menuLabel) {
		this.menuLabel = menuLabel;
	}
}
