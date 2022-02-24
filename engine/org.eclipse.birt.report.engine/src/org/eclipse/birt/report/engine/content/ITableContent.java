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
	public String getCaption();

	public void setCaption(String caption);

	public String getCaptionKey();

	public void setCaptionKey(String key);

	/**
	 * @return
	 */
	public boolean isHeaderRepeat();

	public void setHeaderRepeat(boolean repeat);

	/**
	 * get the header band. return null if the table has no header.
	 * 
	 * @return
	 */
	public ITableBandContent getHeader();

	/**
	 * get the footer band,return NULL if the table has no footer.
	 * 
	 * @return
	 */
	public ITableBandContent getFooter();

	public List getColumns();

	public void setSummary(String summary);

	public String getSummary();
}
