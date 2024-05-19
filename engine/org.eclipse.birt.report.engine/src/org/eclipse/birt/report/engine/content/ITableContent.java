/*******************************************************************************
 * Copyright (c) 2004, 2008 Actuate Corporation.
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

package org.eclipse.birt.report.engine.content;

import java.util.List;

/**
 * Provides the interfaces for the Table Content
 *
 *
 */
public interface ITableContent extends IContainerContent {

	/**
	 * Get the column count
	 *
	 * @return the column count
	 */
	int getColumnCount();

	/**
	 * Get the column based on index
	 *
	 * @param index column index
	 * @return the column
	 */
	IColumn getColumn(int index);

	/**
	 * Add the column
	 *
	 * @param column column
	 */
	void addColumn(IColumn column);

	/**
	 * Get the caption
	 *
	 * @return the caption
	 */
	String getCaption();

	/**
	 * Set the caption
	 *
	 * @param caption
	 */
	void setCaption(String caption);

	/**
	 * Get the caption key
	 *
	 * @return the caption key
	 */
	String getCaptionKey();

	/**
	 * Set the caption key
	 *
	 * @param key caption key
	 */
	void setCaptionKey(String key);

	/**
	 * Is header repeated
	 *
	 * @return is header repeated
	 */
	boolean isHeaderRepeat();

	/**
	 * Set the repeat of the header
	 *
	 * @param repeat header repeated
	 */
	void setHeaderRepeat(boolean repeat);

	/**
	 * Get the header band, return null if the table has no header
	 *
	 * @return the header band, return null if the table has no header
	 */
	ITableBandContent getHeader();

	/**
	 * Get the footer band, return null if the table has no footer
	 *
	 * @return the footer band, return null if the table has no footer
	 */
	ITableBandContent getFooter();

	/**
	 * Get the table columns
	 *
	 * @return the table columns
	 */
	List getColumns();

	/**
	 * Set the summary
	 *
	 * @param summary
	 */
	void setSummary(String summary);

	/**
	 * Get the summary
	 *
	 * @return the summary
	 */
	String getSummary();
}
