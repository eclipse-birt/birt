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

	int getColumnCount();

	IColumn getColumn(int index);

	void addColumn(IColumn column);

	/**
	 * @return Returns the caption.
	 */
	String getCaption();

	void setCaption(String caption);

	String getCaptionKey();

	void setCaptionKey(String key);

	/**
	 * @return
	 */
	boolean isHeaderRepeat();

	void setHeaderRepeat(boolean repeat);

	/**
	 * get the header band. return null if the table has no header.
	 *
	 * @return
	 */
	ITableBandContent getHeader();

	/**
	 * get the footer band,return NULL if the table has no footer.
	 *
	 * @return
	 */
	ITableBandContent getFooter();

	List getColumns();

	void setSummary(String summary);

	String getSummary();
}
