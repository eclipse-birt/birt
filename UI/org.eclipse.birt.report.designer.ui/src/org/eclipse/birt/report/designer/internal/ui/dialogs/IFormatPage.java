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

package org.eclipse.birt.report.designer.internal.ui.dialogs;

import com.ibm.icu.util.ULocale;

/**
 * IFormatPage for format number, string, dateTime
 */

public interface IFormatPage {

	/**
	 * Aligns the page vietically.
	 */
	int PAGE_ALIGN_VIRTICAL = 0;

	/**
	 * Aligns the page horizontally.
	 */
	int PAGE_ALIGN_HORIZONTAL = 1;

	/**
	 * Sets preview text for default use.
	 *
	 * @param text The preview text to be set.
	 */
	void setPreviewText(String text);

	/**
	 * Sets input for the format page.
	 *
	 * @param category The format category.
	 * @param pattern  The format pattern.
	 */
	void setInput(String category, String pattern, ULocale formatLocale);

	/**
	 * Sets input for the page.
	 *
	 * @param formatString The formatString.
	 */
	void setInput(String formatString);

	/**
	 * Gets format category.
	 *
	 * @return The format category.
	 */
	String getCategory();

	/**
	 * Gets format pattern.
	 *
	 * @return The format pattern.
	 */
	String getPattern();

	ULocale getLocale();

	/**
	 * Returns the format string from the page.
	 *
	 * @return The format string.
	 */
	String getFormatString();

	/**
	 * Determines the format string of the page is modified or not.
	 *
	 * @return True if the format string is modified.
	 */
	boolean isFormatModified();

	/**
	 * Determines the page is dirty.
	 *
	 * @return True if the page is dirty.
	 */
	boolean isDirty();

	/**
	 * Adds format change listener to the format page.
	 *
	 * @param listener The format change listener to be added.
	 */
	void addFormatChangeListener(IFormatChangeListener listener);
}
