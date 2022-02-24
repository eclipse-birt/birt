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

package org.eclipse.birt.report.engine.internal.document.v4;

import java.util.List;

public class PageRangeIterator {

	private List pages;
	int curPageRange = -1;
	private long nextPage = -1;

	public PageRangeIterator(List pages) {
		this.pages = pages;
		if (pages.size() > 0) {
			curPageRange = 0;
			long[] pageRange = (long[]) pages.get(curPageRange);
			nextPage = pageRange[0];
		} else {
			nextPage = -1;
		}
	}

	public boolean hasNext() {
		return nextPage != -1;
	}

	public long next() {
		if (hasNext()) {
			long returnedPage = nextPage;
			// we still have some pages remain
			if (curPageRange < pages.size()) {
				long pageNumber = nextPage + 1;
				// test if it is in the current range
				long[] pageRange = (long[]) pages.get(curPageRange);
				if (pageRange[0] <= pageNumber && pageRange[1] >= pageNumber) {
					nextPage = pageNumber;
				} else {
					// if it exceed the current page, use the first page of the
					// next
					// page range
					curPageRange++;
					if (curPageRange < pages.size()) {
						pageRange = (long[]) pages.get(curPageRange);
						nextPage = pageRange[0];
					} else {
						nextPage = -1;
					}
				}
			}
			return returnedPage;
		}
		// all page has been outputed
		return -1;
	}
}
