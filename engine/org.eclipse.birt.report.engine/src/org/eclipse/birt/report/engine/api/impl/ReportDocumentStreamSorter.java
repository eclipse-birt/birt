/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
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

package org.eclipse.birt.report.engine.api.impl;

import java.util.ArrayList;

import org.eclipse.birt.core.archive.IStreamSorter;

public class ReportDocumentStreamSorter implements IStreamSorter {
	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.core.archive.IStreamSorter#sortStream(java.util.ArrayList)
	 */
	@Override
	public ArrayList sortStream(ArrayList streamNameList) {
		ArrayList sortedList = new ArrayList();

		if (streamNameList != null && streamNameList.size() > 0) {
			if (streamNameList.contains(ReportDocumentConstants.CORE_STREAM)) {
				sortedList.add(ReportDocumentConstants.CORE_STREAM);
			}

			if (streamNameList.contains(ReportDocumentConstants.PAGEHINT_STREAM)) {
				sortedList.add(ReportDocumentConstants.PAGEHINT_STREAM);
			}

			if (streamNameList.contains(ReportDocumentConstants.TOC_STREAM)) {
				sortedList.add(ReportDocumentConstants.TOC_STREAM);
			}

			if (streamNameList.contains(ReportDocumentConstants.BOOKMARK_STREAM)) {
				sortedList.add(ReportDocumentConstants.BOOKMARK_STREAM);
			}

			if (streamNameList.contains(ReportDocumentConstants.DESIGN_STREAM)) {
				sortedList.add(ReportDocumentConstants.DESIGN_STREAM);
			}

			for (int i = 0; i < streamNameList.size(); i++) {
				String name = (String) streamNameList.get(i);
				if (!name.equals(ReportDocumentConstants.CORE_STREAM)
						&& !name.equals(ReportDocumentConstants.PAGEHINT_STREAM)
						&& !name.equals(ReportDocumentConstants.DESIGN_STREAM)
						&& !name.equals(ReportDocumentConstants.TOC_STREAM)
						&& !name.equals(ReportDocumentConstants.BOOKMARK_STREAM)) {
					sortedList.add(name);
				}
			}
		}

		return sortedList;
	}
}
