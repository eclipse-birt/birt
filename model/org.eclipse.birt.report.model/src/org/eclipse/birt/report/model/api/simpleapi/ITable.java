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

package org.eclipse.birt.report.model.api.simpleapi;

import org.eclipse.birt.report.model.api.activity.SemanticException;

/**
 * Represents the design of a Table in the scripting environment
 */
public interface ITable extends IListing {

	/**
	 * Returns the number of columns in the table. The number is defined as 1) the
	 * sum of columns described in the "column" slot, or 2) the widest row defined
	 * in the detail, header or footer slots if column slot is empty.
	 *
	 * @return the number of columns in the table
	 */

	int getColumnCount();

	/**
	 * Tests whether to repeat the headings at the top of each page.
	 *
	 * @return <code>true</code> if repeat the headings, otherwise
	 *         <code>false</code>.
	 */

	boolean repeatHeader();

	/**
	 * Sets whether to repeat the headings at the top of each page.
	 *
	 * @param value <code>true</code> if repeat the headings, otherwise
	 *              <code>false</code>.
	 * @throws SemanticException if the property is locked.
	 */

	void setRepeatHeader(boolean value) throws SemanticException;

	/**
	 * Gets the summary of this table.
	 *
	 * @return the summary.
	 */
	String getSummary();

	/**
	 * Sets the summary of this table.
	 *
	 * @param summary the summary
	 * @throws SemanticException if this property is locked.
	 */
	void setSummary(String summary) throws SemanticException;

	/**
	 * Returns the caption text of this table.
	 *
	 * @return the caption text
	 */

	String getCaption();

	/**
	 * Sets the caption text of this table.
	 *
	 * @param caption the caption text
	 * @throws SemanticException if the property is locked.
	 */

	void setCaption(String caption) throws SemanticException;

	/**
	 * Returns the resource key of the caption.
	 *
	 * @return the resource key of the caption
	 */

	String getCaptionKey();

	/**
	 * Sets the resource key of the caption.
	 *
	 * @param captionKey the resource key of the caption
	 * @throws SemanticException if the caption resource-key property is locked.
	 */

	void setCaptionKey(String captionKey) throws SemanticException;

	/**
	 * Gets column.
	 *
	 * @param index
	 * @return column wrapper
	 */

	IColumn getColumn(int index);

}
