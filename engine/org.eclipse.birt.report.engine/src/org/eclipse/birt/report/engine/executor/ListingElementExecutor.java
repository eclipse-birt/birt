/*******************************************************************************
 * Copyright (c) 2004,2009 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.executor;

import java.util.Map;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.api.EngineConstants;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.extension.IReportItemExecutor;
import org.eclipse.birt.report.engine.ir.BandDesign;
import org.eclipse.birt.report.engine.ir.ListingDesign;
import org.eclipse.birt.report.engine.ir.ReportElementDesign;
import org.eclipse.birt.report.engine.ir.ReportItemDesign;
import org.eclipse.birt.report.engine.ir.TableItemDesign;
import org.eclipse.birt.report.model.api.TableHandle;

/**
 * An abstract class that defines execution logic for a Listing element, which
 * is the base element for table and list items.
 */
public abstract class ListingElementExecutor extends QueryItemExecutor implements IPageBreakListener {

	/**
	 * the cursor position in the query result.
	 */
	protected int rsetCursor;

	protected boolean needPageBreak;

	protected int pageRowCount = 0;

	protected int pageBreakInterval = -1;

	protected int pageBreakLevel = -1;

	protected boolean breakOnDetailBand = false;

	protected boolean softBreakBefore = false;

	protected boolean addAfterBreak = false;

	private static int MAX_PAGE_BREAK_INTERVAL = 10000;

	/**
	 * @param context execution context
	 * @param visitor the visitor object that drives execution
	 */
	protected ListingElementExecutor(ExecutorManager manager, int type) {
		super(manager, type);
	}

	protected int getMaxPageBreakInterval() {
		@SuppressWarnings("rawtypes")
		Map appContext = context.getAppContext();
		if (appContext != null) {
			Object maxPageBreakObject = appContext.get(EngineConstants.APPCONTEXT_MAX_PAGE_BREAK_INTERVAL);
			if (maxPageBreakObject instanceof Number) {
				int maxPageBreakInterval = ((Number) maxPageBreakObject).intValue();
				if (maxPageBreakInterval > 0) {
					return maxPageBreakInterval;
				}
			}
		}
		return MAX_PAGE_BREAK_INTERVAL;
	}

	protected void initializeContent(ReportElementDesign design, IContent content) {
		super.initializeContent(design, content);
		pageBreakInterval = ((ListingDesign) design).getPageBreakInterval();
		int maxPageBreakInterval = getMaxPageBreakInterval();
		// pageBreakInterval -1 or 0 is a default value, we shouldn't output
		// warning message
		if (pageBreakInterval <= 0) {
			pageBreakInterval = maxPageBreakInterval;
		}
		pageBreakLevel = getPageBreakIntervalGroup();
		breakOnDetailBand = pageBreakIntervalOnDetail();
		if (pageBreakInterval > 0) {
			context.addPageBreakListener(this);
		}
	}

	protected int getPageBreakIntervalGroup() {
		ListingDesign listing = (ListingDesign) design;
		int groupCount = listing.getGroupCount();
		if (groupCount > 0) {
			for (int i = 0; i < groupCount; i++) {
				if (listing.getGroup(i).getHideDetail()) {
					return i;
				}
			}
			if (design instanceof TableItemDesign) {
				TableHandle handle = (TableHandle) design.getHandle();
				if (handle.isSummaryTable()) {
					return groupCount - 1;
				}
			}
			return groupCount;
		} else {
			return -1;
		}
	}

	/**
	 * access the query and create the contents. the execution process is:
	 * <li>the cursor is at the begin of result set.
	 * <li>call listing's onStart event
	 * <li>create the header
	 * <li>for each row:
	 * <ul>
	 * <li>call onRow event.
	 * <li>if the row start some groups, create the group header for that group.
	 * <li>create the detail row.
	 * <li>if the row end some groups, create the group footer for that group.
	 * </ul>
	 * <li>create the footer.
	 * <li>call the onFinish event.
	 */
	public void close() throws BirtException {
		if (pageBreakInterval != -1) {
			context.removePageBreakListener(this);
		}
		rsetCursor = -1;
		needPageBreak = false;
		pageRowCount = 0;
		pageBreakInterval = -1;
		executableElements = null;
		// total bands in the executabelBands
		totalElements = 0;
		// band to be executed
		currentElement = 0;
		endOfListing = false;
		breakOnDetailBand = false;
		pageBreakLevel = -1;
		softBreakBefore = false;
		addAfterBreak = false;
		super.close();
	}

	void next() {
		if (pageBreakInterval > 0) {
			pageRowCount++;
		}
	}

	void previous() {
		if (pageBreakInterval > 0) {
			pageRowCount--;
		}
	}

	boolean needSoftBreakAfter() {
		return (pageBreakInterval > 0) && (pageBreakInterval <= pageRowCount);
	}

	boolean pageBreakIntervalOnDetail() {
		if (pageBreakInterval > 0) {
			ListingDesign listing = (ListingDesign) design;
			int groupCount = listing.getGroupCount();
			if (groupCount == pageBreakLevel || groupCount == 0) {
				return true;
			}
		}
		return false;
	}

	public boolean hasNextChild() {
		if (currentElement < totalElements) {
			return true;
		}
		if (endOfListing) {
			return false;
		}
		try {
			while (!endOfListing) {
				int endGroup = rset.getEndingGroupLevel();
				if (endGroup <= 0) {
					ListingDesign listingDesign = (ListingDesign) getDesign();
					totalElements = 0;
					currentElement = 0;
					if (listingDesign.getFooter() != null) {
						executableElements[totalElements++] = listingDesign.getFooter();
					}
					endOfListing = true;
					return currentElement < totalElements;
				}
				if (rset.next()) {
					collectExecutableElements();
					if (currentElement < totalElements) {
						return true;
					}
				} else {
					ListingDesign listingDesign = (ListingDesign) getDesign();
					totalElements = 0;
					currentElement = 0;
					if (listingDesign.getFooter() != null) {
						executableElements[totalElements++] = listingDesign.getFooter();
					}
					endOfListing = true;
					return currentElement < totalElements;
				}
			}
		} catch (BirtException ex) {
			context.addException(this.getDesign(), ex);
		}
		return false;
	}

	public IReportItemExecutor getNextChild() {
		if (hasNextChild()) {
			assert (currentElement < totalElements);
			ReportItemDesign nextDesign = executableElements[currentElement++];

			ReportItemExecutor nextExecutor = manager.createExecutor(this, nextDesign);
			if (nextExecutor instanceof GroupExecutor) {
				GroupExecutor groupExecutor = (GroupExecutor) nextExecutor;
				groupExecutor.setLisingExecutor(this);
			}
			return nextExecutor;
		}
		return null;
	}

	// bands to be execute in current row.
	ReportItemDesign[] executableElements;
	// total bands in the executabelBands
	int totalElements;
	// band to be executed
	int currentElement;
	boolean endOfListing;

	protected void prepareToExecuteChildren() {
		ListingDesign listingDesign = (ListingDesign) getDesign();

		// prepare the bands to be executed.
		executableElements = new ReportItemDesign[3];
		if (rset == null || rsetEmpty) {
			BandDesign header = listingDesign.getHeader();
			if (header != null) {
				executableElements[totalElements++] = header;
			}
			BandDesign footer = listingDesign.getFooter();
			if (footer != null) {
				executableElements[totalElements++] = footer;
			}
			endOfListing = true;
		} else {
			collectExecutableElements();
		}
	}

	void collectExecutableElements() {
		try {
			currentElement = 0;
			totalElements = 0;
			endOfListing = false;
			ListingDesign listingDesign = (ListingDesign) getDesign();
			int groupCount = listingDesign.getGroupCount();
			int startGroup = rset.getStartingGroupLevel();
			if (startGroup == 0) {
				// this is the first record
				BandDesign header = listingDesign.getHeader();
				if (header != null) {
					executableElements[totalElements++] = header;
				}
			}
			if (groupCount > 0) {
				executableElements[totalElements++] = listingDesign.getGroup(0);
			} else {
				BandDesign detail = listingDesign.getDetail();
				if (detail != null) {
					executableElements[totalElements++] = detail;
				}
			}
			int endGroup = rset.getEndingGroupLevel();
			if (endGroup <= 0) {
				// this is the last record
				BandDesign footer = listingDesign.getFooter();
				if (footer != null) {
					executableElements[totalElements++] = footer;
				}
				endOfListing = true;
			}
		} catch (BirtException ex) {
			context.addException(this.getDesign(), ex);
		}

	}

	public void onPageBreak(boolean isHorizontalPageBreak, boolean isSizeOverflowPageBreak) {
		// FIXME refactor
		if (!isHorizontalPageBreak) {
			pageRowCount = 0;
			if (addAfterBreak) {
				next();
				addAfterBreak = false;
			} else {
				// Move cursor to reduce row count in this page only if the page
				// break is triggered by last page content size overflow
				if (isSizeOverflowPageBreak) {
					next();
				}
				// this onPagebreak is caused by fixed-layout
				if (softBreakBefore) {
					softBreakBefore = false;
				}

			}
		}
	}
}