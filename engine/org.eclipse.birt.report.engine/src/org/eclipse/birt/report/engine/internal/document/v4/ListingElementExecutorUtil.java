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

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.extension.IQueryResultSet;
import org.eclipse.birt.report.engine.ir.ReportItemDesign;

/**
 * the listing element is executing as:
 * <ul>
 * <li>for empty resutl set:</li>
 * <ul>
 * <li>execute the header</li>
 * <li>execute the footer</li>
 * </ul>
 * <li>for none empty result set:</li>
 * <ul>
 * <li>execute the header</li>
 * <li>execute the first group</li>
 * <li>while (!end of listing)</li>
 * <ul>
 * <li>skip to next row</li>
 * <li>execute the first group</li>
 * </ul>
 * <li>execute the footer.</li>
 * </ul>
 * </ul>
 * 
 * so we can organize the execution into three steps:
 * <ul>
 * <li>execute the header</li>
 * <li>execute the inner groups</li>
 * <li>execute the footer</li>
 * </ul>
 * and label those steps as:
 * <ul>
 * <li>EXECUTE_HEADER</li>
 * <li>EXECUTE_DETAIL</li>
 * <li>EXECUTE_FOOTER</li>
 * </ul>
 */
class ListingElementExecutorUtil {

	private final static int EXECUTE_FROM_CURRENT_ROW = 0;
	private final static int EXECUTE_FROM_NEXT_ROW = 1;
	private final static int EXECUTE_END = 3;

	int group;
	ReportItemDesign header;
	ReportItemDesign child;
	ReportItemDesign footer;
	IQueryResultSet rset;
	int executeState;
	boolean includeHeader;

	ListingElementExecutorUtil(int group, ReportItemDesign header, ReportItemDesign footer, ReportItemDesign child,
			IQueryResultSet rset) {
		this(group, header, footer, child, rset, true);
	}

	ListingElementExecutorUtil(int group, ReportItemDesign header, ReportItemDesign footer, ReportItemDesign child,
			IQueryResultSet rset, boolean fromBegin) {
		this.group = group;
		this.header = header;
		this.footer = footer;
		this.child = child;
		this.rset = rset;
		this.executeState = EXECUTE_FROM_CURRENT_ROW;
		this.includeHeader = fromBegin;
	}

	public void startFromCurrentRow() {
		if (executeState != EXECUTE_END) {
			executeState = EXECUTE_FROM_CURRENT_ROW;
		}
	}

	int collectExecutableElements(ReportItemDesign[] executableElements) throws BirtException {
		while (executeState != EXECUTE_END) {
			int totalElements = doCollectExecutableElements(executableElements);
			if (totalElements != 0) {
				return totalElements;
			}
		}
		return 0;
	}

	private int doCollectExecutableElements(ReportItemDesign[] executableElements) throws BirtException {
		int totalElements = 0;

		switch (executeState) {
		case EXECUTE_FROM_CURRENT_ROW:
			if (includeHeader && header != null) {
				includeHeader = false;
				executableElements[totalElements++] = header;
			}
			if (child != null) {
				executableElements[totalElements++] = child;
			}
			executeState = EXECUTE_FROM_NEXT_ROW;
			if (isGroupEnd()) {
				if (footer != null) {
					executableElements[totalElements++] = footer;
				}
				executeState = EXECUTE_END;
			}
			break;
		case EXECUTE_FROM_NEXT_ROW:
			if (isGroupEnd()) {
				if (footer != null) {
					executableElements[totalElements++] = footer;
				}
				executeState = EXECUTE_END;
				return totalElements;
			}
			if (rset.next()) {
				if (child != null) {
					executableElements[totalElements++] = child;
				}
				if (isGroupEnd()) {
					if (footer != null) {
						executableElements[totalElements++] = footer;
					}
					executeState = EXECUTE_END;
				}
				return totalElements;
			}
			break;
		}
		return totalElements;
	}

	boolean isGroupEnd() throws BirtException {
		int groupLevel = rset.getEndingGroupLevel();
		if (groupLevel <= group) {
			return true;
		}
		return false;
	}
}
