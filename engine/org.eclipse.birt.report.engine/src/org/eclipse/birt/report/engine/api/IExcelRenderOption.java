/*******************************************************************************
 * Copyright (c)2007 Actuate Corporation.
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

package org.eclipse.birt.report.engine.api;

public interface IExcelRenderOption extends IRenderOption {

	/**
	 * The option to decide if the text out will be wrapped
	 */
	String WRAPPING_TEXT = "excelRenderOption.wrappingText";

	/**
	 * This is the option to decide in which office version will you output the
	 * excel file
	 */
	String OFFICE_VERSION = "excelRenderOption.officeVersion";

	/**
	 * The option to hide the gridlines in the worksheet
	 */
	String HIDE_GRIDLINES = "excelRenderOption.hideGridlines";

	/**
	 * This is the option to decide in which office version will you output the
	 * excel file
	 */
	String OPTION_MULTIPLE_SHEET = "excelRenderOption.multipleSheet";

	/**
	 * Excel will ignore all image items when this option is on.
	 */
	String IGNORE_IMAGE = "excelRenderOption.ignoreImage";

	/**
	 *
	 * @param wrappingText
	 */
	void setWrappingText(boolean wrappingText);

	/**
	 *
	 * @param officeVersion
	 */
	void setOfficeVersion(String officeVersion);

	/**
	 *
	 * @return if the text is wrapped
	 */
	boolean getWrappingText();

	/**
	 *
	 * @return officeVersion
	 */
	String getOfficeVersion();

	boolean isEnableMultipleSheet();

	void setEnableMultipleSheet(boolean enableMultipleSheet);
}
