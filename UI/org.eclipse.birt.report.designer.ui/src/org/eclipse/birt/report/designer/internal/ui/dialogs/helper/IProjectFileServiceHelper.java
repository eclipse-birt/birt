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
/**
 * 
 */
package org.eclipse.birt.report.designer.internal.ui.dialogs.helper;

/**
 * @author cthronson
 *
 */
public interface IProjectFileServiceHelper {
	/**
	 * Provides an interface for user to choose a file.
	 * 
	 * @param isIDE
	 * @param projectFolder
	 * @param needFilter
	 * @param projectMode
	 * @param fileExt
	 * @param selectedType
	 * @param isRelativeToProjectRoot
	 * 
	 * @return Returns a String object that represents the file that the user
	 *         selected
	 */
	String getUserSelection(boolean isIDE, String projectFolder, boolean needFilter, boolean projectMode,
			String[] fileExt, String selectedType, boolean isRelativeToProjectRoot);

	/**
	 * Creates a valid file path from the user selection
	 * 
	 * @param userSelection
	 * 
	 * @return A file path that can be used in the local file system
	 */
	String getFilePath(String userSelection);

	/**
	 * Creates the text to identify the target report of a drill=through hyperlink
	 * This text is displayed to the user and used in the report design definition.
	 * 
	 * @param filename
	 * @param userSelection
	 * 
	 * @return The text of the target report design
	 */
	String getTargetReportLocation(String filename, String userSelection);
}
