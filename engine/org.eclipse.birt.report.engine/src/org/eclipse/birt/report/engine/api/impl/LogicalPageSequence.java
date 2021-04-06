/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.api.impl;

import java.util.ArrayList;
import java.util.List;

public class LogicalPageSequence {

	private long[][] visiblePages;
	private long totalVisiblePageCount;
	private long totalPhysicalPageCount;

	public LogicalPageSequence(long[][] visiblePages) {
		this.visiblePages = visiblePages;
		this.totalVisiblePageCount = calculateTotalPageCount(visiblePages);
	}

	public LogicalPageSequence(ArrayList<long[][]> pages) {
		this(pages, -1);
	}

	public LogicalPageSequence(ArrayList<long[][]> pages, long totalPhysicalPageNumber) {
		this.totalPhysicalPageCount = totalPhysicalPageNumber;
		this.visiblePages = mergeVisiblePages(pages.get(0));
		for (int i = 1; i < pages.size(); i++) {
			this.visiblePages = mergeVisiblePages(visiblePages, pages.get(i));
		}
		this.totalVisiblePageCount = calculateTotalPageCount(visiblePages);
	}

	public long getTotalVisiblePageCount() {
		return totalVisiblePageCount;
	}

	public long[][] getVisiblePages() {
		return visiblePages;
	}

	public long getLogicalPageNumber(long physicalPageNumber) {
		assert visiblePages != null;
		long logicalPageNumber = 0;
		for (int i = 0; i < visiblePages.length; i++) {
			long firstPage = visiblePages[i][0];
			long lastPage = visiblePages[i][1];
			if (firstPage > physicalPageNumber) {
				return -1;
			}
			if (lastPage >= physicalPageNumber) {
				return logicalPageNumber + physicalPageNumber - firstPage + 1;
			}
			// last page < physical page number
			logicalPageNumber += lastPage - firstPage + 1;
		}
		return -1;
	}

	public long[][] getPhysicalPageNumbers(long[][] logicalPages) {
		List<long[]> physicalPages = new ArrayList<long[]>();
		long logicalPageNumber = 0;
		int j = 0;
		for (int i = 0; i < visiblePages.length; i++) {
			long firstPhysicalPage = visiblePages[i][0];
			long lastPhysicalPage = visiblePages[i][1];
			long firstLogicalPage = logicalPageNumber + 1;
			long lastLogicalPage = logicalPageNumber + lastPhysicalPage - firstPhysicalPage + 1;

			// skip the logical segment which before the first logical page
			while (j < logicalPages.length) {
				long[] range = logicalPages[j];
				if (range[1] >= firstLogicalPage) {
					break;
				}
				j++;
			}

			// add the logical segment which are in the visible pages
			while (j < logicalPages.length) {
				long[] range = logicalPages[j];
				if (range[0] > lastLogicalPage) {
					break;
				}
				long firstPage = firstPhysicalPage + Math.max(firstLogicalPage, range[0]) - firstLogicalPage;
				long lastPage = firstPhysicalPage + Math.min(lastLogicalPage, range[1]) - firstLogicalPage;
				physicalPages.add(new long[] { firstPage, lastPage });
				if (range[1] > lastLogicalPage) {
					break;
				}
				j++;
			}

			if (j >= logicalPages.length) {
				break;
			}
			// last page < physical page number
			logicalPageNumber += lastPhysicalPage - firstPhysicalPage + 1;
		}
		return physicalPages.toArray(new long[physicalPages.size()][]);
	}

	private long[][] mergeVisiblePages(long[][] pages) {
		if (totalPhysicalPageCount == -1) {
			return pages;
		}

		ArrayList<long[]> visiblePages = new ArrayList<long[]>();
		for (int i = 0; i < pages.length; i++) {
			long firstPage = pages[i][0];
			long lastPage = pages[i][1];
			if (lastPage <= totalPhysicalPageCount) {
				visiblePages.add(pages[i]);
			} else if (firstPage <= totalPhysicalPageCount) {
				visiblePages.add(new long[] { firstPage, totalPhysicalPageCount });
			}
		}
		return visiblePages.toArray(new long[visiblePages.size()][]);
	}

	private long[][] mergeVisiblePages(long[][] pages1, long[][] pages2) {
		ArrayList<long[]> pages = new ArrayList<long[]>();
		int i = 0;
		int j = 0;
		while (i < pages1.length) {
			while (j < pages2.length && pages1[i][0] > pages2[j][1]) {
				j++;
			}
			if (j >= pages2.length) {
				break;
			}
			while (j < pages2.length && pages1[i][1] >= pages2[j][0]) {
				long firstPage = Math.max(pages1[i][0], pages2[j][0]);
				long lastPage = Math.min(pages1[i][1], pages2[j][1]);
				if (totalPhysicalPageCount == -1) {
					pages.add(new long[] { firstPage, lastPage });
				} else {
					if (lastPage <= totalPhysicalPageCount) {
						pages.add(new long[] { firstPage, lastPage });
					}
				}

				j++;
			}
			if (j >= pages2.length) {
				break;
			}
			i++;
		}
		return pages.toArray(new long[pages.size()][]);
	}

	private long calculateTotalPageCount(long[][] pages) {
		long totalPage = 0;
		for (int i = 0; i < pages.length; i++) {
			totalPage += pages[i][1] - pages[i][0] + 1;
		}
		return totalPage;
	}
}
