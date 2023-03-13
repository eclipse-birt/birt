/*******************************************************************************
 * Copyright (c) 2004, 2009 Actuate Corporation.
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

package org.eclipse.birt.report.engine.presentation;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.birt.report.engine.executor.PageVariable;

public class PageHint implements IPageHint {
	protected long pageNumber;
	protected long offset;
	protected String masterPage;
	protected Collection<PageVariable> pageVariables = new ArrayList<>();

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
	@Override
	public long getPageNumber() {
		return pageNumber;
	}

	@Override
	public int getSectionCount() {
		return sections.size();
	}

	@Override
	public long getOffset() {
		return offset;
	}

	// method for test
	@Override
	public long getSectionStart(int i) {
		PageSection section = (PageSection) sections.get(i);
		return section.startOffset;
	}

	// method for test
	@Override
	public long getSectionEnd(int i) {
		PageSection section = (PageSection) sections.get(i);
		return section.endOffset;
	}

	@Override
	public PageSection getSection(int i) {
		return (PageSection) sections.get(i);
	}

	public void addSection(PageSection section) {
		sections.add(section);
	}

	@Override
	public void addUnresolvedRowHints(Collection hints) {
		this.unresolvedRowHints.addAll(hints);
	}

	@Override
	public int getUnresolvedRowCount() {
		return unresolvedRowHints.size();
	}

	@Override
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

	@Override
	public String getMasterPage() {
		return this.masterPage;
	}

	public void setOffset(long offset) {
		this.offset = offset;
	}

	@Override
	public void addTableColumnHint(TableColumnHint hint) {
		columnInfo.add(hint);
	}

	@Override
	public TableColumnHint getTableColumnHint(int index) {
		return (TableColumnHint) columnInfo.get(index);
	}

	@Override
	public int getTableColumnHintCount() {
		return this.columnInfo.size();
	}

	@Override
	public void addTableColumnHints(Collection hints) {
		this.columnInfo.addAll(hints);
	}

	@Override
	public Collection<PageVariable> getPageVariables() {
		return pageVariables;
	}
}
