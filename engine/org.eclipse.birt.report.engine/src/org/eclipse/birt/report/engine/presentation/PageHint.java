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

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.birt.report.engine.executor.PageVariable;

public class PageHint implements IPageHint {
	protected long pageNumber;
	protected long offset;
	protected String masterPage;
	protected Collection<PageVariable> pageVariables = new ArrayList<PageVariable>();

	/**
	 * page sections
	 */
	ArrayList sections = new ArrayList();

	/**
	 * unresolved row hints
	 */
	ArrayList unresolvedRowHints = new ArrayList();

	/**
	 * table column hints
	 */
	ArrayList columnInfo = new ArrayList();

	public PageHint() {
		pageNumber = 0;
		offset = -1;
	}

	public PageHint(long pageNumber, long pageOffset) {
		this.pageNumber = pageNumber;
		offset = pageOffset;
	}

	/**
	 * @param pageNumber
	 * @param maserPage
	 */
	public PageHint(long pageNumber, String masterPage) {
		this.pageNumber = pageNumber;
		this.masterPage = masterPage;
	}

	/**
	 * @return Returns the pageNumber.
	 */
	public long getPageNumber() {
		return pageNumber;
	}

	public int getSectionCount() {
		return sections.size();
	}

	public long getOffset() {
		return offset;
	}

	// method for test
	public long getSectionStart(int i) {
		PageSection section = (PageSection) sections.get(i);
		return section.startOffset;
	}

	// method for test
	public long getSectionEnd(int i) {
		PageSection section = (PageSection) sections.get(i);
		return section.endOffset;
	}

	public PageSection getSection(int i) {
		return (PageSection) sections.get(i);
	}

	public void addSection(PageSection section) {
		sections.add(section);
	}

	public void addUnresolvedRowHints(Collection hints) {
		this.unresolvedRowHints.addAll(hints);
	}

	public int getUnresolvedRowCount() {
		return unresolvedRowHints.size();
	}

	public UnresolvedRowHint getUnresolvedRowHint(int index) {
		assert index >= 0 && index < unresolvedRowHints.size();
		return (UnresolvedRowHint) unresolvedRowHints.get(index);
	}

	public void addUnresolvedRowHint(UnresolvedRowHint hint) {
		unresolvedRowHints.add(hint);
	}

	public void setMasterPage(String masterPage) {
		this.masterPage = masterPage;
	}

	public String getMasterPage() {
		return this.masterPage;
	}

	public void setOffset(long offset) {
		this.offset = offset;
	}

	public void addTableColumnHint(TableColumnHint hint) {
		columnInfo.add(hint);
	}

	public TableColumnHint getTableColumnHint(int index) {
		return (TableColumnHint) columnInfo.get(index);
	}

	public int getTableColumnHintCount() {
		return this.columnInfo.size();
	}

	public void addTableColumnHints(Collection hints) {
		this.columnInfo.addAll(hints);
	}

	public Collection<PageVariable> getPageVariables() {
		return pageVariables;
	}
}
