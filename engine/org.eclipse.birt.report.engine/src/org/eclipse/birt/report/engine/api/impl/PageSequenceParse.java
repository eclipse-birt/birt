
/*******************************************************************************
 * Copyright (c) 2007 Actuate Corporation.
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
import java.util.List;

import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.i18n.MessageConstants;

/**
 *
 */

public class PageSequenceParse {

	public static List parsePageSequence(String pageRange, long totalPage) throws EngineException {
		ArrayList list = new ArrayList();
		if (null == pageRange || "".equals(pageRange) || pageRange.toUpperCase().indexOf("ALL") >= 0) //$NON-NLS-1$ //$NON-NLS-2$
		{
			list.add(new long[] { 1, totalPage });
			return list;
		}
		String[] ps = pageRange.split(","); //$NON-NLS-1$
		for (int i = 0; i < ps.length; i++) {
			try {
				if (ps[i].indexOf("-") > 0) //$NON-NLS-1$
				{
					String[] psi = ps[i].split("-"); //$NON-NLS-1$
					if (psi.length == 2) {
						long start = Long.parseLong(psi[0].trim());
						long end = Long.parseLong(psi[1].trim());
						if (start > 0 && end <= totalPage && end >= start) {
							list.add(new long[] { Math.max(start, 1), Math.min(end, totalPage) });
						} else {
							throw new EngineException(MessageConstants.PAGE_NUMBER_RANGE_ERROR, ps[i]);
						}
					} else {
						throw new EngineException(MessageConstants.PAGE_NUMBER_RANGE_ERROR, ps[i]);
					}
				} else {
					long number = Long.parseLong(ps[i].trim());
					if (number > 0 && number <= totalPage) {
						list.add(new long[] { number, number });
					} else {
						throw new EngineException(MessageConstants.PAGE_NUMBER_RANGE_ERROR, ps[i]);
					}

				}
			} catch (NumberFormatException ex) {
				throw new EngineException(MessageConstants.PAGE_NUMBER_RANGE_ERROR, ps[i]);
			}
		}
		return sort(list);
	}

	private static List sort(List list) {
		for (int i = 0; i < list.size(); i++) {
			long[] currentI = (long[]) list.get(i);
			int minIndex = i;
			long[] min = currentI;
			for (int j = i + 1; j < list.size(); j++) {
				long[] currentJ = (long[]) list.get(j);
				if (currentJ[0] < min[0]) {
					minIndex = j;
					min = currentJ;
				}
			}
			if (minIndex != i) {
				// swap
				list.set(i, min);
				list.set(minIndex, currentI);
			}
		}
		long[] current = null;
		long[] last = null;
		ArrayList ret = new ArrayList();
		for (int i = 0; i < list.size(); i++) {
			current = (long[]) list.get(i);
			if (last != null) {
				if (current[1] <= last[1]) {
					continue;
				}
				if (current[0] <= last[1]) {
					current[0] = last[1];
				}
				ret.add(current);
			} else {
				ret.add(current);
			}
			last = current;
		}
		return ret;
	}
}
