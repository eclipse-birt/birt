/*******************************************************************************
 * Copyright (c) 2013 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.data.oda.pojo.ui.impl.contols;

import java.io.File;

import org.eclipse.birt.data.oda.pojo.ui.i18n.Messages;
import org.eclipse.birt.data.oda.pojo.ui.impl.dialogs.JarsSelectionDialog;
import org.eclipse.birt.data.oda.pojo.ui.impl.models.ClassPathElement;
import org.eclipse.birt.data.oda.pojo.ui.util.Constants;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.FileDialog;

public class MenuButtonProvider implements IMenuButtonProvider {

	private static String BUTTON_JAR_TEXT = Messages.getString("DataSource.POJOClassTabFolderPage.menuButton.addJar"); //$NON-NLS-1$

	private static String RELATIVE_PATH_MENU = Messages
			.getString("DataSource.POJOClassTabFolderPage.menuButton.item.relativePath"); //$NON-NLS-1$
	private static String ABSOLUTE_PATH_MENU = Messages
			.getString("DataSource.POJOClassTabFolderPage.menuButton.item.absolutePath"); //$NON-NLS-1$

	private ClassSelectionButton button;
	private String[] optionTypes;

	public MenuButtonProvider() {
		optionTypes = new String[] { ClassPathElement.ABSOLUTE_PATH };
	}

	public String getDefaultOptionType() {
		return optionTypes == null || optionTypes.length == 0 ? null : optionTypes[0];
	}

	public String[] getMenuItems() {
		return optionTypes;
	}

	public Image getMenuItemImage(String type) {
		return null;
	}

	public String getMenuItemText(String type) {
		if (ClassPathElement.RELATIVE_PATH.equals(type)) {
			return RELATIVE_PATH_MENU;
		}
		return ABSOLUTE_PATH_MENU;
	}

	public String getTooltipText(String type) {
		return ClassPathElement.RELATIVE_PATH.equals(type)
				? Messages.getString("DataSource.button.tooltip.AddRelativeJars") //$NON-NLS-1$
				: Messages.getString("DataSource.button.tooltip.AddAbsoluteJars");//$NON-NLS-1$
	}

	public void handleSelectionEvent(String type) {
		Object value = button.getMenuButtonHelper().getPropertyValue(Constants.RESOURCE_FILE_DIR);
		String[] fileNames = null;
		String rootPath = null;
		boolean isRelative = ClassPathElement.RELATIVE_PATH.equals(type);
		if (isRelative) {
			if (value != null && value instanceof File) {
				JarsSelectionDialog dialog = new JarsSelectionDialog(button.getControl().getShell(), (File) value);
				if (dialog.open() == Window.OK) {
					fileNames = dialog.getSelectedItems();
				}
			} else {
				ExceptionHandler.openErrorMessageBox(
						Messages.getString("DataSource.POJOClassTabFolderPage.error.title.empty.resourceIdentifier"), //$NON-NLS-1$
						Messages.getString("DataSource.POJOClassTabFolderPage.error.message.empty.resourceIdentifier")); //$NON-NLS-1$
			}
		} else {
			FileDialog dialog = new FileDialog(button.getControl().getShell(), SWT.MULTI);
			dialog.setFilterExtensions(new String[] { "*.jar;*.zip" //$NON-NLS-1$ //, $NON-NLS-2$
			});
			if (dialog.open() != null) {
				fileNames = dialog.getFileNames();
				rootPath = dialog.getFilterPath();
			}
		}

		if (fileNames != null)
			button.handleSelection(fileNames, rootPath, isRelative);

	}

	public void setInput(ClassSelectionButton input) {
		this.button = input;
	}

	public void resetProperties() {
		boolean supportsRelativePath = button.getMenuButtonHelper()
				.getPropertyValue(Constants.RESOURCE_FILE_DIR) instanceof File;
		if (supportsRelativePath) {
			optionTypes = new String[] { ClassPathElement.RELATIVE_PATH, ClassPathElement.ABSOLUTE_PATH };
		} else {
			optionTypes = new String[] { ClassPathElement.ABSOLUTE_PATH };
		}

	}

	public String getButtonImage() {
		return null;
	}

	public String getButtonText() {
		return BUTTON_JAR_TEXT;
	}

}
