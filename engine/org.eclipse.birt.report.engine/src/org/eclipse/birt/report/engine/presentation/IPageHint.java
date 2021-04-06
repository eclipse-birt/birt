/*******************************************************************************
 * Copyright (c) 2004, 2009 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.presentation;

import java.util.Collection;

import org.eclipse.birt.report.engine.executor.PageVariable;

public interface IPageHint {

	/**
	 * get the page number of this section
	 * 
	 * @return
	 */
	long getPageNumber();

	/**
	 * get the page offset from the page content stream.
	 * 
	 * @return
	 */
	long getOffset();

	/**
	 * get the sections contains in the content.
	 * 
	 * @return
	 */
	int getSectionCount();

	PageSection getSection(int section);

	/**
	 * get the start offset of the section.
	 * 
	 * @param section
	 * @return
	 */
	long getSectionStart(int section);

	/**
	 * get the end offset of the section.
	 * 
	 * @param section
	 * @return
	 */
	long getSectionEnd(int section);

	void addUnresolvedRowHints(Collection hints);

	int getUnresolvedRowCount();

	UnresolvedRowHint getUnresolvedRowHint(int index);

	String getMasterPage();

	int getTableColumnHintCount();

	void addTableColumnHint(TableColumnHint hint);

	TableColumnHint getTableColumnHint(int index);

	void addTableColumnHints(Collection hints);

	Collection<PageVariable> getPageVariables();

}
